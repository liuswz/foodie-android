package com.foodie.home.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.entity.Advertisement;
import com.foodie.base.entity.ShopDetail;

import com.foodie.home.adapter.viewAdapter.ShopRecyclerAdapter;
import com.foodie.home.bindingParam.ShopClickCallBack;
import com.foodie.home.bindingParam.ShopTypeClickCallBack;
import com.foodie.home.ui.DetailActivity;
import com.foodie.home.ui.SearchActivity;
import com.foodie.home.ui.SimpleScannerActivity;
import com.foodie.home.ui.TypeActivity;
import com.foodie.home.ui.viewmodel.HomeViewModel;

import com.foodie.base.router.RouterFragmentPath;
import com.foodie.home.R;
import com.foodie.home.databinding.FragmentHomeBinding;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rxjava.rxlife.RxLife;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import io.reactivex.functions.Consumer;


/**
 * Created by Owen on 2019/7/14
 */
@Route(path = RouterFragmentPath.Home.PAGER_HOME)
public class HomeFragment extends Fragment implements AMapLocationListener {

    private FragmentHomeBinding fragmentHomeBinding;
    private ShopRecyclerAdapter shopRecyclerAdapter;
    private PullToRefreshScrollView rsv;
    private XRecyclerView xRecyclerView;
    private HomeViewModel viewModel ;
    private Integer page=1;
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    //显示距离排序或评分排序 标志位
    private Integer showFlag=1;
    //是否加载更多标志位
    private boolean showMoreFlag=false;

    private  RxPermissions rxPermissions;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        //初始化定位
        initLocation();
        // 添加所需权限
        rxPermissions = new RxPermissions(this);
        getPermissions();
        return fragmentHomeBinding.getRoot();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        fragmentHomeBinding.setListenerByLocation(listenerByLocation);
//        fragmentHomeBinding.setListenerByMark(listenerByMark);
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        fragmentHomeBinding.loadShopBylocation.setOnClickListener(listenerByLocation);
        fragmentHomeBinding.loadShopBymark.setOnClickListener(listenerByMark);
        fragmentHomeBinding.setCallback(shopTypeClickCallBack);
        fragmentHomeBinding.search.setOnClickListener(gotoSearch);
        fragmentHomeBinding.saoma.setOnClickListener(saoma);
        initScrollView();
        initXRecyclerView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //1. 初始化viewModel


        observeViewModelAdList(viewModel);

        observeViewModelShopList(viewModel);
    }
    private OnClickEvent saoma = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            rxPermissions.request(Manifest.permission.CAMERA
            )
                    .as(RxLife.<Boolean>as(getActivity()))
                    .subscribe(new Consumer<Boolean>() {

                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                Intent intent =new Intent(getActivity(), SimpleScannerActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getActivity(), "请给相机权限，非则无法扫码", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }
    };

    private void getPermissions() {

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
                            viewModel.setPosition("长春市抚松路");
                            viewModel.loadAdList();
                            viewModel.loadShopList(page);
                            fragmentHomeBinding.setViewModel(viewModel);
                        }


                    }
                });

    }
    private void initLocation() {

        mLocationClient = new AMapLocationClient(getContext());
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
    private void initScrollView() {
        rsv = fragmentHomeBinding.refreshScrollView;
        rsv.setMode(PullToRefreshBase.Mode.BOTH);

        //2.通过调用getLoadingLayoutProxy方法，设置下拉刷新状况布局中显示的文字 ，第一个参数为true,代表下拉刷新
        ILoadingLayout headLables = rsv.getLoadingLayoutProxy(true, false);
        headLables.setPullLabel("下拉刷新");
        headLables.setRefreshingLabel("正在刷新");
        headLables.setReleaseLabel("松开刷新");

        //2.设置上拉加载底部视图中显示的文字，第一个参数为false,代表上拉加载更多
        ILoadingLayout footerLables = rsv.getLoadingLayoutProxy(false, true);
        footerLables.setPullLabel("上拉加载");
        footerLables.setRefreshingLabel("正在载入...");
        footerLables.setReleaseLabel("松开加载更多");

        //3.设置监听事件
        rsv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

                showFlag=1;
                page=1;
                showMoreFlag=false;
                mLocationClient.startLocation();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

                if(viewModel.getTotal()>page){
                    showMoreFlag=true;
                    if(showFlag==1){
                        viewModel.loadShopList(++page);
                    }else{
                        viewModel.loadShopListByMark(++page);
                    }

                }else{
                    rsv.onRefreshComplete();

                }
            }
        });
    }
    private void initXRecyclerView() {
        shopRecyclerAdapter = new ShopRecyclerAdapter(shopClickCallBack);
//        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        xRecyclerView=fragmentHomeBinding.recyclerview;
        xRecyclerView.setAdapter(shopRecyclerAdapter);
        xRecyclerView.setLayoutManager(mLayoutManager);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);




//        xRecyclerView.setPullRefreshEnabled(false);
//        xRecyclerView.setLoadingMoreEnabled(true);
//        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
//            @Override
//            public void onRefresh() {
//            }
//            @Override
//            public void onLoadMore() {
//                if(viewModel.getTotal()>page){
//                    showMoreFlag=true;
//                    if(showFlag==1){
//                        viewModel.loadShopList(++page);
//                    }else{
//                        viewModel.loadShopListByMArk(++page);
//                    }
//
//                }else{
//
//                    xRecyclerView.setLoadingMoreEnabled(false);
//                }
//            }
//        });

    }
    private void observeViewModelAdList(final HomeViewModel viewModel) {

        viewModel.getAdList().observe(this, new Observer<List<Advertisement>>() {
            @Override
            public void onChanged(List<Advertisement> advertisements) {

                fragmentHomeBinding.setViewModel(viewModel);

            }
        });

    }

    private void observeViewModelShopList(final HomeViewModel viewModel) {

        viewModel.getShopList().observe(this, new Observer<List<ShopDetail>>() {
            @Override
            public void onChanged(List<ShopDetail> shopDetailList) {
                if(showMoreFlag){
                    shopRecyclerAdapter.addData(shopDetailList);
                    rsv.onRefreshComplete();

                }else{
                    shopRecyclerAdapter.setData(shopDetailList);
                    rsv.onRefreshComplete();
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

                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("id",shopDetail.getShopId()+"");
                getContext().startActivity(intent);

                mLastClickTime = nowTime;
            }

        }
    };

    public ShopTypeClickCallBack shopTypeClickCallBack = new ShopTypeClickCallBack() {
        @Override
        public void onClick(String typeName,View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime > TIME_INTERVAL) {

                Intent intent = new Intent(getContext(), TypeActivity.class);
                intent.putExtra("type",typeName);
                getContext().startActivity(intent);
                mLastClickTime = nowTime;
            }

        }
    };
    public View.OnClickListener gotoSearch = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            getContext().startActivity(intent);
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
                viewModel.setPosition(aMapLocation.getDistrict()+aMapLocation.getStreet());
                viewModel.loadAdList();
                viewModel.loadShopList(page);
                fragmentHomeBinding.setViewModel(viewModel);
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
