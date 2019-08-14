package com.foodie.user.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.dto.VoucherDto;
import com.foodie.base.entity.ShopDetail;
import com.foodie.base.enums.ResultCode;
import com.foodie.base.router.RouterActivityPath;
import com.foodie.user.R;

import com.foodie.user.adapter.viewAdapter.VoucherRecyclerAdapter;
import com.foodie.user.bindingParam.ShopClickCallBack;

import com.foodie.user.bindingParam.VoucherClickCallBack;
import com.foodie.user.databinding.ActivityVoucherBinding;
import com.foodie.user.ui.viewmodel.CollectionViewModel;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rxjava.rxlife.RxLife;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import io.reactivex.functions.Consumer;

public class VoucherActivity extends AppCompatActivity implements AMapLocationListener {
    private ActivityVoucherBinding activityVoucherBinding;
    private CollectionViewModel viewModel;
    private Integer page=1;
    private XRecyclerView xRecyclerView;
    private VoucherRecyclerAdapter voucherRecyclerAdapter;
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        activityVoucherBinding = DataBindingUtil.setContentView(this, R.layout.activity_voucher);
        activityVoucherBinding.backPage.setOnClickListener(backPage);

        viewModel = ViewModelProviders.of(this).get(CollectionViewModel.class);

        SharedPreferences sharedPreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
        Integer  userId = sharedPreferences.getInt("userId", 0);
        viewModel.setUserId(userId);

        initXRecyclerView();
        initLocation();
        getPermissions();

        observeViewModelVoucherList(viewModel);
        observeViewModelVoucherStatus(viewModel);
    }

    private void getPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        )
                .as(RxLife.<Boolean>as(this))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            mLocationClient.startLocation();
                        }else{
                            viewModel.setCity("长春市");
                            viewModel.loadVoucher(page);
                        }


                    }
                });

    }

    private void initLocation() {

        mLocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mLocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
//      mLocationOption.setInterval(2000);
        // mLocationOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    private void initXRecyclerView() {
        voucherRecyclerAdapter = new VoucherRecyclerAdapter(voucherClickCallBack);
//        activityTypeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        xRecyclerView=activityVoucherBinding.xrecyclerView;
        xRecyclerView.setAdapter(voucherRecyclerAdapter);
        xRecyclerView.setLayoutManager(mLayoutManager);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }
            @Override
            public void onLoadMore() {
                if(viewModel.getTotal()>page){
                    viewModel.loadVoucher(++page);
                }else{
                    xRecyclerView.setLoadingMoreEnabled(false);
                }
            }
        });

    }
    private VoucherClickCallBack voucherClickCallBack = new VoucherClickCallBack() {
        @Override
        public void onClick(VoucherDto voucher, View view) {
            new AlertDialog.Builder(VoucherActivity.this)
                    .setTitle("提示")
                    .setMessage("确定领取词代金卷吗")
                    // .setIcon(R.mipmap.ic_launcher_round)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            viewModel.addVoucher(voucher);

                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create()
                    .show();
        }
    };
    private void observeViewModelVoucherList(final CollectionViewModel viewModel) {

        viewModel.getVoucher().observe(this, new Observer<List<VoucherDto>>() {
            @Override
            public void onChanged(List<VoucherDto> voucherDtos) {

                voucherRecyclerAdapter.addData(voucherDtos);
                xRecyclerView.loadMoreComplete();

            }
        });
    }
    private void observeViewModelVoucherStatus(final CollectionViewModel viewModel) {

        viewModel.getVoucherStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer status) {
                if(status==ResultCode.FAIL.getIndex()){
                    Toast.makeText(VoucherActivity.this, "此代金卷已经领啦", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(VoucherActivity.this, "领取成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private OnClickEvent backPage = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            finish();
        }
    };

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                viewModel.setCity(aMapLocation.getCity());
                viewModel.loadVoucher(page);

                mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
            }else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }
}

