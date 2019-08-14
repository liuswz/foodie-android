package com.foodie.product.ui;

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

import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.entity.Product;
import com.foodie.base.entity.Product;
import com.foodie.base.entity.ProductBase;
import com.foodie.base.util.ACache;
import com.foodie.base.util.ClearShopCarEvent;
import com.foodie.base.util.MsgEvent;
import com.foodie.base.util.RxBus;
import com.foodie.product.R;
import com.foodie.product.adapter.viewAdapter.GoodCarRecyclerAdapter;
import com.foodie.product.adapter.viewAdapter.ProductRecyclerAdapter;
import com.foodie.product.adapter.viewAdapter.ProductSearchPromptRecyclerAdapter;
import com.foodie.product.bindingParam.GoodCarCallBack;
import com.foodie.product.bindingParam.ProductClickCallBack;
import com.foodie.product.bindingParam.ProductSearchPromptClickCallBack;
import com.foodie.product.databinding.ActivityProductSearchBinding;

import com.foodie.product.entity.ProductSearchHistory;
import com.foodie.product.myview.GoodCarDialog;
import com.foodie.product.ui.viewmodel.PSearchViewModel;
import com.foodie.product.ui.viewmodel.ProductViewModel;
import com.foodie.product.util.ACacheUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nex3z.flowlayout.FlowLayout;
import com.rxjava.rxlife.RxLife;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ProductSearchActivity extends AppCompatActivity {
    private PSearchViewModel viewModel ;
    private ActivityProductSearchBinding activitySearchBinding;
    private ProductSearchPromptRecyclerAdapter productSearchPromptRecyclerAdapter;
    private ProductRecyclerAdapter productRecyclerAdapter;

    private XRecyclerView xRecyclerViewPrompt;
    private XRecyclerView xRecyclerViewSearchResult;
    private Integer page=1;
    //是否加载更多标志位
    private Boolean promptOrResult=true;
    private Boolean promptFlag=true;

    private GoodCarDialog goodCarDialog;
    private GoodCarRecyclerAdapter goodCarRecyclerAdapter;
    private ACache mCache;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);
        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_search);
        viewModel = ViewModelProviders.of(this).get(PSearchViewModel.class);
        goodCarRecyclerAdapter = new GoodCarRecyclerAdapter(addHadAddDishCallback ,minusHadAddDishCallback);
        goodCarDialog = new GoodCarDialog(goodCarRecyclerAdapter,clearGoodCar);
        activitySearchBinding.shopcarLayout.setOnClickListener(checkGoodCar);
        activitySearchBinding.shopcarLayout.setEnabled(false);
        activitySearchBinding.payNow.setOnClickListener(payNow);
        mCache = ACache.get(this);
        gson = new Gson();
        updateGoodCar();
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


        EditText editText = activitySearchBinding.searchText;
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.setOnEditorActionListener(keyboardSearch);
        editText.addTextChangedListener(editclick);
        getPermissions();

        viewModel.loadSearchHot();
        observeViewModelSearchHot(viewModel);
        activitySearchBinding.setViewModel(viewModel);
        observeViewModelSearchValue(viewModel);
        observeViewModelSearchPrompt(viewModel);
        observeViewModelSearchResult(viewModel);
        observeViewModelHadAddDishMap(viewModel);
    }
    void initXRecyclerView(){
        productSearchPromptRecyclerAdapter = new ProductSearchPromptRecyclerAdapter(productSearchPromptClickCallBack);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        xRecyclerViewPrompt=activitySearchBinding.searchResultPrompt;
        xRecyclerViewPrompt.setAdapter(productSearchPromptRecyclerAdapter);
        xRecyclerViewPrompt.setLayoutManager(mLayoutManager);
        xRecyclerViewPrompt.setPullRefreshEnabled(false);
        xRecyclerViewPrompt.setLoadingMoreEnabled(false);

        productRecyclerAdapter = new ProductRecyclerAdapter(productClickCallBack, addProductCallBack,minusProductCallBack,this);
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        xRecyclerViewSearchResult=activitySearchBinding.searchResult;
        xRecyclerViewSearchResult.setAdapter(productRecyclerAdapter);
        xRecyclerViewSearchResult.setLayoutManager(mLayoutManager2);
        xRecyclerViewSearchResult.setPullRefreshEnabled(false);
        xRecyclerViewSearchResult.setLoadingMoreEnabled(true);
        xRecyclerViewSearchResult.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }
            @Override
            public void onLoadMore() {
                if(viewModel.getTotal()>page){
                    viewModel.loadSearchProduct(viewModel.getSearchValue().getValue().trim(),++page);

                }else{
                    xRecyclerViewSearchResult.setLoadingMoreEnabled(false);
                }
            }
        });
    }

    private OnClickEvent payNow = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            Intent intent = new Intent(ProductSearchActivity.this, OrderDetailActivity.class);
            intent.putExtra("goodCarItem",gson.toJson(ACacheUtil.changeFormProduct(viewModel.getProductMap().getValue())));
            intent.putExtra("productSumPrice",viewModel.getSumPrice().toString());
            intent.putExtra("productIdList",gson.toJson(viewModel.getIdList()));
            startActivity(intent);
        }
    };
    private void getPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
                .as(RxLife.<Boolean>as(this))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            viewModel.loadSearchHistory();
                            observeViewModelSearchHistory(viewModel);

                        }
                    }
                });

    }
    private void updateGoodCar(){
        String goodCarItem = mCache.getAsString("goodCarItem");
        String sumPrice = mCache.getAsString("sumPrice");
        String idList = mCache.getAsString("idList");
        //   List<Integer> idList =  gson.fromJson(mCache.getAsString("idList"), new TypeToken<List<Integer>>(){}.getType());

        if(goodCarItem!=null&&!goodCarItem.equals("")){
            viewModel.setProductMap(ACacheUtil.changeToProduct(gson.fromJson(goodCarItem, new TypeToken<Map<Integer, ProductBase>>(){}.getType())));
            activitySearchBinding.shopcarLayout.setEnabled(true);
        }
        if(sumPrice!=null&&!sumPrice.equals("")){
            viewModel.setSumPrice(Double.parseDouble(sumPrice));
        }
        if(idList!=null&&!idList.equals("")){
            viewModel.setIdList(gson.fromJson(idList, new TypeToken<List<String>>(){}.getType()));
        }
        observeUpdateGoodCar();
        observeClearGoodCar();
    }
    private static long mLastClickTime0 = 0;
    public static final long TIME_INTERVAL0 = 400L;
    private ProductClickCallBack addProductCallBack = new ProductClickCallBack() {
        @Override
        public void onClick(Product product,View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime0 > TIME_INTERVAL0) {

                Integer num = product.getNum().getValue();
                product.getNum().setValue((num+1));
                viewModel.addProductMap(product);

                mLastClickTime0 = nowTime;
            }

        }
    };


    private ProductClickCallBack minusProductCallBack = new ProductClickCallBack() {
        @Override
        public void onClick(Product product,View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime0 > TIME_INTERVAL0) {

                Integer num = product.getNum().getValue();
                if (num > 0) {
                    product.getNum().setValue((num - 1) );
                }
                viewModel.minusProductMap(product);

                mLastClickTime0 = nowTime;
            }

        }
    };

    private GoodCarCallBack addHadAddDishCallback = new GoodCarCallBack() {
        @Override
        public void onClick(Integer productId,View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime0 > TIME_INTERVAL0) {
                viewModel.addGoodCarProductMap(productId);

                mLastClickTime0 = nowTime;
            }

        }
    };
    private GoodCarCallBack minusHadAddDishCallback = new GoodCarCallBack() {
        @Override
        public void onClick(Integer productId,View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime0 > TIME_INTERVAL0) {
                viewModel.minusGoodCarProductMap(productId);

                mLastClickTime0 = nowTime;
            }

        }
    };
    public OnClickEvent checkGoodCar = new OnClickEvent() {
        @Override
        public void singleClick(View v) {

            goodCarDialog.show(getSupportFragmentManager(),"");

        }

    };
    public OnClickEvent clearGoodCar = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            viewModel.clearGoodCar();
            mCache.remove("goodCarItem");
            mCache.remove("sumPrice");
            mCache.remove("idList");
            goodCarDialog.dismiss();

        }

    };

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
    private ProductSearchPromptClickCallBack productSearchPromptClickCallBack = new ProductSearchPromptClickCallBack() {
        @Override
        public void onClick(String value, View view) {
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
    private ProductClickCallBack productClickCallBack = new ProductClickCallBack() {
        @Override
        public void onClick(Product product, View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime3 > TIME_INTERVAL) {
                Intent intent = new Intent(ProductSearchActivity.this, DetailActivity.class);
                intent.putExtra("id",product.getId()+"");
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
        public void singleClick(View videw) {
            viewModel.deleteSearchHistory();

        }
    };

    public OnClickEvent backPageClick = new OnClickEvent() {
        @Override
        public void singleClick(View view) {
            ProductSearchActivity.this.finish();
        }
    };

    private void observeViewModelSearchHistory(final PSearchViewModel viewModel) {

        viewModel.getSearchHistoryList().observe(this, new Observer<List<ProductSearchHistory>>() {
            @Override
            public void onChanged(List<ProductSearchHistory> shopSearchHistories) {
                FlowLayout flowLayout = activitySearchBinding.historyFlowlayout;
                if(shopSearchHistories==null||shopSearchHistories.size()==0){
                    flowLayout.removeAllViews();
                }else{
                    for (ProductSearchHistory history :shopSearchHistories) {
                        TextView tv = new TextView(ProductSearchActivity.this);
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
    private void observeViewModelSearchHot(final PSearchViewModel viewModel) {

        viewModel.getSearchHotList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> shopSearchHots) {
                FlowLayout flowLayout = activitySearchBinding.hotFlowlayout;

                for (String hot : shopSearchHots) {
                    TextView tv = new TextView(ProductSearchActivity.this);
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

    private void observeViewModelSearchValue(final PSearchViewModel viewModel) {

        viewModel.getSearchValue().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String value) {
                value=value.trim();
                if(!"".equals(value)){


                    if(promptOrResult){

                        //     Toast.makeText(SearchActivity.this, "11111", Toast.LENGTH_SHORT).show();
                        Log.e("Owen","11111111");
                        activitySearchBinding.setResultVisibility(false);
                        activitySearchBinding.setNoContentVisibility(false);
                        activitySearchBinding.setBaseVisibility(false);
                        activitySearchBinding.setPromptVisibility(true);
                        viewModel.loadSearchPromptList(value);



                    }else{

                        viewModel.loadSearchProduct(value,page);
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
    private void observeViewModelSearchPrompt(final PSearchViewModel viewModel) {

        viewModel.getSearchPromptList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> values) {


                productSearchPromptRecyclerAdapter.setData(values);

            }
        });
    }
    private void observeViewModelSearchResult(final PSearchViewModel viewModel) {

        viewModel.getSearchResultList().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> productList) {
                if(productList.size()==0||productList==null){
                    activitySearchBinding.setResultVisibility(false);
                    activitySearchBinding.setBaseVisibility(false);
                    activitySearchBinding.setPromptVisibility(false);
                    activitySearchBinding.setNoContentVisibility(true);
                }else{
                    activitySearchBinding.setResultVisibility(true);
                    activitySearchBinding.setBaseVisibility(false);
                    activitySearchBinding.setPromptVisibility(false);
                    activitySearchBinding.setNoContentVisibility(false);
                    productRecyclerAdapter.setData(productList);
                }


            }
        });
    }
    private void observeViewModelHadAddDishMap(final PSearchViewModel viewModel) {
        viewModel.getProductMap().observe(this, new Observer<Map<Integer, Product>>() {
            @Override
            public void onChanged(Map<Integer,Product> goodCarItemMap) {
                if(goodCarItemMap.size()>0){
                    activitySearchBinding.shopcarLayout.setEnabled(true);
                    activitySearchBinding.payNow.setEnabled(true);
                    activitySearchBinding.payNow.setBackgroundResource(R.color.green);
                }else{
                    activitySearchBinding.shopcarLayout.setEnabled(false);
                    activitySearchBinding.payNow.setEnabled(false);
                    activitySearchBinding.payNow.setBackgroundResource(R.color.grey);
                }
                activitySearchBinding.setViewModel(viewModel);
                goodCarRecyclerAdapter.setData(goodCarItemMap,viewModel.getIdList());
//                mCache.put("goodCarItem",gson.toJson(ACacheUtil.changeFormProduct(goodCarItemMap)));
//                mCache.put("sumPrice",viewModel.getSumPrice().toString());
//                mCache.put("idList", gson.toJson(viewModel.getIdList()));
//                RxBus.getInstance().post(new MsgEvent("A"));
            }
        });
    }
    private void observeUpdateGoodCar(){
        RxBus.getInstance().toObservable(MsgEvent.class).subscribe(new io.reactivex.Observer<MsgEvent>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(MsgEvent msgEvent) {
                //处理事件
                updateGoodCar();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }
    private void observeClearGoodCar(){
        RxBus.getInstance().toObservable(ClearShopCarEvent.class).subscribe(new io.reactivex.Observer<ClearShopCarEvent>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ClearShopCarEvent msgEvent) {
                //处理事件

                viewModel.clearGoodCar();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }
    @Override
    public void onPause() {
        if(viewModel.getProductMap().getValue()!=null){
            mCache.put("goodCarItem",gson.toJson(ACacheUtil.changeFormProduct(viewModel.getProductMap().getValue())));
        }
        if(viewModel.getSumPrice()!=0){
            mCache.put("sumPrice",viewModel.getSumPrice().toString());
        }

        if(viewModel.getIdList()!=null){
            mCache.put("idList", gson.toJson(viewModel.getIdList()));
        }

        RxBus.getInstance().post(new MsgEvent("A"));
        super.onPause();
    }
    public void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

}
