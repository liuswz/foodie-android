package com.foodie.home.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.entity.SearchShop;
import com.foodie.base.entity.ShopDetail;
import com.foodie.base.entity.ShopSearchPrompt;
import com.foodie.home.R;
import com.foodie.home.adapter.viewAdapter.ShopSearchPromptRecyclerAdapter;
import com.foodie.home.adapter.viewAdapter.ShopSearchResultRecyclerAdapter;
import com.foodie.home.bindingParam.ShopClickCallBack;
import com.foodie.home.bindingParam.ShopSearchClickCallBack;
import com.foodie.home.bindingParam.ShopSearchPromptClickCallBack;
import com.foodie.home.databinding.ActivitySearchBinding;
import com.foodie.home.entity.ShopSearchHistory;
import com.foodie.home.ui.viewmodel.SearchViewModel;
import com.foodie.home.ui.viewmodel.TypeViewModel;
import com.jakewharton.rxbinding2.view.RxView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nex3z.flowlayout.FlowLayout;
import com.rxjava.rxlife.RxLife;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class SearchActivity extends AppCompatActivity implements AMapLocationListener {

    private SearchViewModel viewModel ;
    private ActivitySearchBinding activitySearchBinding;
    private ShopSearchPromptRecyclerAdapter shopSearchPromptRecyclerAdapter;
    private ShopSearchResultRecyclerAdapter shopSearchResultRecyclerAdapter;

    private XRecyclerView xRecyclerViewPrompt;
    private XRecyclerView xRecyclerViewSearchResult;

    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;

    private Boolean promptOrResult=true;
    private Boolean promptFlag=true;
    private Integer locationOrMarkFlag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        initView();
        initXRecyclerView();
    }
    private void initView(){
        activitySearchBinding.setBaseVisibility(true);
        activitySearchBinding.setNoContentVisibility(false);
        activitySearchBinding.setPromptVisibility(false);
        activitySearchBinding.setResultVisibility(false);
        activitySearchBinding.searchButton.setOnClickListener(searchButtonClick);
        activitySearchBinding.backPage.setOnClickListener(backPageClick);
        activitySearchBinding.clearAll.setOnClickListener(clearAllHistoryClick);
        activitySearchBinding.loadShopBylocation.setOnClickListener(listenerByLocation);
        activitySearchBinding.loadShopBymark.setOnClickListener(listenerByMark);

        initLocation();
        EditText editText = activitySearchBinding.searchText;
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.setOnEditorActionListener(keyboardSearch);
        editText.addTextChangedListener(editclick);


        getPermissions();
        observeViewModelSearchHot(viewModel);
        activitySearchBinding.setViewModel(viewModel);
        observeViewModelSearchValue(viewModel);
        observeViewModelSearchPrompt(viewModel);
        observeViewModelSearchResult(viewModel);

    }
    void initXRecyclerView(){
        shopSearchPromptRecyclerAdapter = new ShopSearchPromptRecyclerAdapter(shopSearchPromptClickCallBack);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        xRecyclerViewPrompt=activitySearchBinding.searchResultPrompt;
        xRecyclerViewPrompt.setAdapter(shopSearchPromptRecyclerAdapter);
        xRecyclerViewPrompt.setLayoutManager(mLayoutManager);
        xRecyclerViewPrompt.setPullRefreshEnabled(false);
        xRecyclerViewPrompt.setLoadingMoreEnabled(false);

        shopSearchResultRecyclerAdapter = new ShopSearchResultRecyclerAdapter(shopSearchClickCallBack);
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        xRecyclerViewSearchResult=activitySearchBinding.searchResult;
        xRecyclerViewSearchResult.setAdapter(shopSearchResultRecyclerAdapter);
        xRecyclerViewSearchResult.setLayoutManager(mLayoutManager2);
        xRecyclerViewSearchResult.setPullRefreshEnabled(false);
        xRecyclerViewSearchResult.setLoadingMoreEnabled(true);
        xRecyclerViewSearchResult.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }
            @Override
            public void onLoadMore() {
                if(viewModel.getHasMore()){
                    viewModel.addPage();
                    if(locationOrMarkFlag==1){
                        viewModel.loadSearchShopByLocation(viewModel.getSearchValue().getValue().trim());
                    }else{
                        viewModel.loadSearchShopByMark(viewModel.getSearchValue().getValue().trim());
                    }

                }else{
                    xRecyclerViewSearchResult.setLoadingMoreEnabled(false);

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

    private void getPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
                .as(RxLife.<Boolean>as(this))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            viewModel.loadSearchHistory();
                            observeViewModelSearchHistory(viewModel);
                            mLocationClient.startLocation();
                        }else{
                            viewModel.setCity("长春市");
                            viewModel.setLocation(122.08400000000002, 37.421998333333335);

                            viewModel.loadSearchHot();

                        }


                    }
                });

    }
    private static long mLastClickTime = 0;
    public static final long TIME_INTERVAL = 1000L;
    private TextView.OnEditorActionListener keyboardSearch = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            long nowTime = System.currentTimeMillis();
            if (actionId == EditorInfo.IME_ACTION_SEARCH&&nowTime - mLastClickTime > TIME_INTERVAL) {

                promptOrResult=false;
                hideKeyboard(activitySearchBinding.searchText);
                viewModel.setSearchValue( viewModel.getSearchValue().getValue());
                promptFlag=true;

                mLastClickTime = nowTime;

                return true;
            }
            return false;
        }
    };
    private static long mLastClickTime2 = 0;
    private ShopSearchPromptClickCallBack shopSearchPromptClickCallBack = new ShopSearchPromptClickCallBack() {
        @Override
        public void onClick(String value,View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime2 > TIME_INTERVAL) {
                hideKeyboard(activitySearchBinding.searchText);
                promptOrResult=false;
                activitySearchBinding.setViewModel(viewModel);
                viewModel.setSearchValue(value);

                mLastClickTime2 = nowTime;
            }

        }
    };
    private static long mLastClickTime3 = 0;
    private ShopSearchClickCallBack shopSearchClickCallBack = new ShopSearchClickCallBack() {
        @Override
        public void onClick(Integer id,View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime3 > TIME_INTERVAL) {

                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);

                mLastClickTime3 = nowTime;
            }

        }
    };

    public OnClickEvent searchButtonClick = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            promptOrResult=false;
            hideKeyboard(activitySearchBinding.searchText);
            viewModel.setSearchValue( viewModel.getSearchValue().getValue());
            promptFlag=true;
        }

    };

    private TextWatcher editclick = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {

                if(promptFlag){
                    promptOrResult=true;
                    viewModel.setSearchValue(activitySearchBinding.searchText.getText().toString());
                    activitySearchBinding.setViewModel(viewModel);


                }
                promptFlag=true;
            }


    };

    public OnClickEvent clearAllHistoryClick = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            viewModel.deleteSearchHistory();
        }


    };

    public OnClickEvent backPageClick = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            SearchActivity.this.finish();

        }
    };

    public OnClickEvent listenerByLocation = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            String value = viewModel.getSearchValue().getValue().trim();
            if(!"".equals(value)){
                xRecyclerViewSearchResult.setLoadingMoreEnabled(true);
                viewModel.setPage(1);
                locationOrMarkFlag=1;
                viewModel.loadSearchShopByLocation(value);
            }
        }


    };

    public OnClickEvent listenerByMark = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            String value = viewModel.getSearchValue().getValue().trim();
            if(!"".equals(value)){
                xRecyclerViewSearchResult.setLoadingMoreEnabled(true);
                viewModel.setPage(1);
                locationOrMarkFlag=2;
                viewModel.loadSearchShopByMark(value);
            }
        }

    };
    private void observeViewModelSearchHistory(final SearchViewModel viewModel) {

        viewModel.getSearchHistoryList().observe(this, new Observer<List<ShopSearchHistory>>() {
            @Override
            public void onChanged(List<ShopSearchHistory> shopSearchHistories) {
                FlowLayout flowLayout = activitySearchBinding.historyFlowlayout;
                if(shopSearchHistories==null||shopSearchHistories.size()==0){
                    flowLayout.removeAllViews();
                }else{
                    for (ShopSearchHistory history :shopSearchHistories) {
                        TextView tv = new TextView(SearchActivity.this);
                        tv.setText(history.getValue());
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                        tv.setPadding(16,8,16,8);
                        tv.setBackgroundResource(R.drawable.tv_bg_shape);
                        tv.setOnClickListener(new OnClickEvent() {
                            @Override
                            public void singleClick(View v) {
                                promptOrResult=false;
                                hideKeyboard(activitySearchBinding.searchText);
                                viewModel.setSearchValue(history.getValue());
                                activitySearchBinding.setViewModel(viewModel);
                            }

                        });
                        flowLayout.addView(tv);
                    }
                }



            }
        });
    }
    private void observeViewModelSearchHot(final SearchViewModel viewModel) {

        viewModel.getSearchHotList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> shopSearchHots) {
                FlowLayout flowLayout = activitySearchBinding.hotFlowlayout;

                for (String hot : shopSearchHots) {
                    TextView tv = new TextView(SearchActivity.this);
                    tv.setText(hot);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    tv.setPadding(16,8,16,8);
                    tv.setBackgroundResource(R.drawable.tv_bg_shape);
                    tv.setOnClickListener(new OnClickEvent() {
                        @Override
                        public void singleClick(View v) {
                            promptOrResult=false;
                            hideKeyboard(activitySearchBinding.searchText);
                            viewModel.setSearchValue(hot);
                            activitySearchBinding.setViewModel(viewModel);
                        }

                    });
                    flowLayout.addView(tv);
                }

            }
        });
    }

    private void observeViewModelSearchValue(final SearchViewModel viewModel) {

        viewModel.getSearchValue().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String value) {
                value=value.trim();
                if(!"".equals(value)){

                    Log.e("Owen",promptOrResult+"-------");
                    if(promptOrResult){

                       //     Toast.makeText(SearchActivity.this, "11111", Toast.LENGTH_SHORT).show();
                       //     Log.e("Owen","11111111");
                            activitySearchBinding.setResultVisibility(false);
                            activitySearchBinding.setNoContentVisibility(false);
                            activitySearchBinding.setBaseVisibility(false);
                            activitySearchBinding.setPromptVisibility(true);
                            viewModel.loadSearchPromptList(value);



                    }else{
                      //  Log.e("Owen","2222222222");
                        viewModel.setPage(1);
                        viewModel.loadSearchShopByLocation(value);
                        promptOrResult=true;
                        promptFlag=false;
                    }
                }

               else if("".equals(value)){

                    activitySearchBinding.setBaseVisibility(true);
                    activitySearchBinding.setPromptVisibility(false);
                    activitySearchBinding.setResultVisibility(false);
                    activitySearchBinding.setNoContentVisibility(false);

                }


            }
        });
    }
    private void observeViewModelSearchPrompt(final SearchViewModel viewModel) {

        viewModel.getSearchPromptList().observe(this, new Observer<List<ShopSearchPrompt>>() {
            @Override
            public void onChanged(List<ShopSearchPrompt> shopSearchHots) {


                shopSearchPromptRecyclerAdapter.setData(shopSearchHots);

            }
        });
    }
    private void observeViewModelSearchResult(final SearchViewModel viewModel) {

        viewModel.getSearchResultList().observe(this, new Observer<List<SearchShop>>() {
            @Override
            public void onChanged(List<SearchShop> searchShopList) {
                if(searchShopList.size()==0||searchShopList==null){
                    activitySearchBinding.setResultVisibility(false);
                    activitySearchBinding.setBaseVisibility(false);
                    activitySearchBinding.setPromptVisibility(false);
                    activitySearchBinding.setNoContentVisibility(true);
                }else{
                    activitySearchBinding.setResultVisibility(true);
                    activitySearchBinding.setBaseVisibility(false);
                    activitySearchBinding.setPromptVisibility(false);
                    activitySearchBinding.setNoContentVisibility(false);
                    shopSearchResultRecyclerAdapter.setData(searchShopList);
                }


            }
        });
    }
    public void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {

                viewModel.setCity(aMapLocation.getCity());
                viewModel.setLocation(aMapLocation.getLongitude(), aMapLocation.getLatitude());

                viewModel.loadSearchHot();

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
