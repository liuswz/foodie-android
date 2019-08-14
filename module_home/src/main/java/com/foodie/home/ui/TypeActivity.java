package com.foodie.home.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.entity.ShopDetail;
import com.foodie.home.adapter.viewAdapter.ShopRecyclerAdapter;
import com.foodie.home.bindingParam.ShopClickCallBack;
import com.foodie.home.ui.viewmodel.TypeViewModel;
import com.foodie.home.R;
import com.foodie.home.databinding.ActivityTypeBinding;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rxjava.rxlife.RxLife;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import io.reactivex.functions.Consumer;

public class TypeActivity extends AppCompatActivity implements AMapLocationListener {
    
    private ActivityTypeBinding activityTypeBinding;
    private ShopRecyclerAdapter shopRecyclerAdapter;
    private XRecyclerView xRecyclerView;
    private TypeViewModel viewModel ;
    private Integer page=1;
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    //显示距离排序或评分排序 标志位
    private Integer showFlag=1;
    //是否加载更多标志位
    private boolean showMoreFlag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        viewModel = ViewModelProviders.of(this).get(TypeViewModel.class);
        Intent intent = getIntent();//获取传来的intent对象
     //   Toast.makeText(this, intent.getStringExtra("type")+"---------", Toast.LENGTH_SHORT).show();
        viewModel.setType(intent.getStringExtra("type"));//获取键值对的键名
        //初始化定位
        initLocation();
        // 添加所需权限
        getPermissions();
        activityTypeBinding = DataBindingUtil.setContentView(this, R.layout.activity_type);

        activityTypeBinding.loadShopBylocation.setOnClickListener(listenerByLocation);
        activityTypeBinding.loadShopBymark.setOnClickListener(listenerByMark);


        initXRecyclerView();
        observeViewModelShopList(viewModel);
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
                            viewModel.setLocation(122.08400000000002, 37.421998333333335);
                            viewModel.loadShopList(page);

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
        shopRecyclerAdapter = new ShopRecyclerAdapter(shopClickCallBack);
//        activityTypeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        xRecyclerView=activityTypeBinding.recyclerview;
        xRecyclerView.setAdapter(shopRecyclerAdapter);
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
                    showMoreFlag=true;
                    if(showFlag==1){
                        viewModel.loadShopList(++page);
                    }else{
                        viewModel.loadShopListByMark(++page);
                    }

                }else{

                    xRecyclerView.setLoadingMoreEnabled(false);
                }
            }
        });

    }
    private void observeViewModelShopList(final TypeViewModel viewModel) {

        viewModel.getShopList().observe(this, new Observer<List<ShopDetail>>() {
            @Override
            public void onChanged(List<ShopDetail> shopDetailList) {
                if(showMoreFlag){
                    shopRecyclerAdapter.addData(shopDetailList);
                    xRecyclerView.loadMoreComplete();

                }else{
                    shopRecyclerAdapter.setData(shopDetailList);
                    xRecyclerView.loadMoreComplete();
                }

            }
        });
    }


    private static long mLastClickTime = 0;
    public static final long TIME_INTERVAL = 1000L;
    private ShopClickCallBack shopClickCallBack = new ShopClickCallBack() {
        @Override
        public void onClick(ShopDetail shopDetail,View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime > TIME_INTERVAL) {
                Toast.makeText(TypeActivity.this, "333333333333333333", Toast.LENGTH_SHORT).show();

                mLastClickTime = nowTime;
            }

        }
    };


    public OnClickEvent listenerByLocation = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            xRecyclerView.setLoadingMoreEnabled(true);
            showFlag=1;
            page=1;
            showMoreFlag=false;
            viewModel.loadShopList(page);
        }


    };
    public OnClickEvent listenerByMark = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            xRecyclerView.setLoadingMoreEnabled(true);
            showFlag=1;
            page=1;
            showMoreFlag=false;
            viewModel.loadShopListByMark(page);
        }


    };
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {

                viewModel.setCity(aMapLocation.getCity());
                viewModel.setLocation(aMapLocation.getLongitude(), aMapLocation.getLatitude());

                viewModel.loadShopList(page);

                mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
            }else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
    }
}
