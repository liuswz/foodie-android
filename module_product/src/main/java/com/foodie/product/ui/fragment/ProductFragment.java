package com.foodie.product.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;

import com.foodie.base.entity.Product;
import com.foodie.base.entity.ProductBase;
import com.foodie.base.router.RouterFragmentPath;

import com.foodie.base.util.ACache;
import com.foodie.base.util.ClearShopCarEvent;
import com.foodie.base.util.MsgEvent;
import com.foodie.base.util.RxBus;
import com.foodie.product.R;
//import com.foodie.product.adapter.viewAdapter.ProductRecyclerAdapter;
import com.foodie.base.adapter.OnClickEvent;
import com.foodie.product.adapter.viewAdapter.GoodCarRecyclerAdapter;
import com.foodie.product.adapter.viewAdapter.ProductRecyclerAdapter;
import com.foodie.product.bindingParam.GoodCarCallBack;
import com.foodie.product.bindingParam.ProductTypeClickCallBack;
import com.foodie.product.databinding.FragmentProductBinding;
import com.foodie.base.entity.Product;
import com.foodie.product.myview.GoodCarDialog;
import com.foodie.product.ui.DetailActivity;
import com.foodie.product.ui.OrderDetailActivity;
import com.foodie.product.ui.ProductSearchActivity;
import com.foodie.product.ui.viewmodel.ProductViewModel;
import com.foodie.product.util.ACacheUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.foodie.product.bindingParam.ProductClickCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * Created by goldze on 2018/6/21
 */
@Route(path = RouterFragmentPath.Product.PAGER_PRODUCT)
public class ProductFragment extends Fragment  {
    private FragmentProductBinding fragmentProductBinding;
    private ProductRecyclerAdapter productRecyclerAdapter;
    private XRecyclerView xRecyclerView;
    private ProductViewModel viewModel ;
    private Integer page=1;


    //是否加载更多标志位
    private boolean showMoreFlag=false;
  //  private ProgressDialog mDefaultDialog;
    private GoodCarDialog goodCarDialog;
    private GoodCarRecyclerAdapter goodCarRecyclerAdapter;
    private ACache mCache;
    private Gson gson;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentProductBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false);
        viewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        fragmentProductBinding.setCallback(productTypeClickCallBack);
        fragmentProductBinding.search.setOnClickListener(gotoSearch);
        fragmentProductBinding.payNow.setOnClickListener(payNow);
         mCache = ACache.get(getActivity());
         gson = new Gson();

      //  Map<Integer, Product> goodCarItemMap = gson.fromJson(mCache.getAsString("goodCarItem"), new TypeToken< Map<Integer, Product> >(){}.getType());
        goodCarRecyclerAdapter = new GoodCarRecyclerAdapter(addHadAddDishCallback ,minusHadAddDishCallback);
        goodCarDialog = new GoodCarDialog(goodCarRecyclerAdapter,clearGoodCar);
        fragmentProductBinding.shopcarLayout.setOnClickListener(checkGoodCar);
        fragmentProductBinding.shopcarLayout.setEnabled(false);
        initXRecyclerView();


       // initProgress();
        return fragmentProductBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    //    Log.e("Owen","1111111111111111");
        observeUpdateGoodCar();
        observeClearGoodCar();
        observeViewModelproductList(viewModel);
        observeViewModelHadAddDishMap(viewModel);
        updateGoodCar();
    }


    private void updateGoodCar(){
        String goodCarItem = mCache.getAsString("goodCarItem");
        String sumPrice = mCache.getAsString("sumPrice");
        String idList = mCache.getAsString("idList");
        //   List<Integer> idList =  gson.fromJson(mCache.getAsString("idList"), new TypeToken<List<Integer>>(){}.getType());

        if(goodCarItem!=null&&!goodCarItem.equals("")){
            viewModel.setProductMap(ACacheUtil.changeToProduct(gson.fromJson(goodCarItem, new TypeToken<Map<Integer, ProductBase>>(){}.getType())));
            fragmentProductBinding.shopcarLayout.setEnabled(true);
        }
        if(sumPrice!=null&&!sumPrice.equals("")){
            viewModel.setSumPrice(Double.parseDouble(sumPrice));
        }
        if(idList!=null&&!idList.equals("")){
            viewModel.setIdList(gson.fromJson(idList, new TypeToken<List<String>>(){}.getType()));
        }
        viewModel.loadProductList(page);
    }

    private OnClickEvent payNow = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);

            intent.putExtra("goodCarItem",gson.toJson(ACacheUtil.changeFormProduct(viewModel.getProductMap().getValue())));
            intent.putExtra("productSumPrice",viewModel.getSumPrice().toString());
            intent.putExtra("productIdList",gson.toJson(viewModel.getIdList()));
            startActivity(intent);
        }
    };

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

            goodCarDialog.show(getFragmentManager(),"");

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

    private void initXRecyclerView() {
        productRecyclerAdapter = new ProductRecyclerAdapter(productClickCallBack, addProductCallBack,minusProductCallBack,this.getViewLifecycleOwner());
//        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        xRecyclerView=fragmentProductBinding.recyclerview;
        xRecyclerView.setAdapter(productRecyclerAdapter);
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
                    viewModel.loadProductList(++page);

                }else{
                    xRecyclerView.setLoadingMoreEnabled(false);
                }
            }
        });

    }
    private void observeViewModelproductList(final ProductViewModel viewModel) {

        viewModel.getProductList().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> productList) {
                if(showMoreFlag){
                    productRecyclerAdapter.addData(productList);
                    xRecyclerView.loadMoreComplete();
                }else{
                    productRecyclerAdapter.setData(productList);
                    xRecyclerView.loadMoreComplete();
                }

              //  mDefaultDialog.cancel();

            }
        });
    }

    private static long mLastClickTime = 0;
    public static final long TIME_INTERVAL = 1000L;
    private ProductClickCallBack productClickCallBack = new ProductClickCallBack() {
        @Override
        public void onClick(Product product, View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime > TIME_INTERVAL) {

                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("id",product.getId()+"");
                getContext().startActivity(intent);

                mLastClickTime = nowTime;
            }

        }
    };
    public OnClickEvent gotoSearch = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            Intent intent = new Intent(getContext(), ProductSearchActivity.class);
            getContext().startActivity(intent);
        }
    };
    private ProductTypeClickCallBack productTypeClickCallBack = new ProductTypeClickCallBack() {
        @Override
        public void onClick(String typeName, View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime > TIME_INTERVAL) {
                xRecyclerView.setLoadingMoreEnabled(true);
                viewModel.setType(typeName);
                page=1;
                showMoreFlag=false;
          //      mDefaultDialog.show();

                viewModel.loadProductList(page);

                mLastClickTime = nowTime;
            }

        }
    };
    private void observeViewModelHadAddDishMap(final ProductViewModel viewModel) {
        viewModel.getProductMap().observe(this, new Observer<Map<Integer, Product>>() {
            @Override
            public void onChanged(Map<Integer,Product> goodCarItemMap) {
                if(goodCarItemMap.size()>0){
                    fragmentProductBinding.shopcarLayout.setEnabled(true);
                    fragmentProductBinding.payNow.setEnabled(true);
                    fragmentProductBinding.payNow.setBackgroundResource(R.color.green);
                }else{
                    fragmentProductBinding.shopcarLayout.setEnabled(false);
                    fragmentProductBinding.payNow.setEnabled(false);
                    fragmentProductBinding.payNow.setBackgroundResource(R.color.grey);
                }
                fragmentProductBinding.setViewModel(viewModel);
                goodCarRecyclerAdapter.setData(goodCarItemMap,viewModel.getIdList());

//                mCache.put("goodCarItem",gson.toJson(ACacheUtil.changeFormProduct(goodCarItemMap)));
//                mCache.put("sumPrice",viewModel.getSumPrice().toString());
//                mCache.put("idList", gson.toJson(viewModel.getIdList()));
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


        super.onPause();
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
//    public void initProgress() {
//         mDefaultDialog = new ProgressDialog(getContext());
////        mDefaultDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER); //默认就是小圆圈的那种形式
//        mDefaultDialog.setMessage("正在进行网络请求...");
////        mDefaultDialog.setCancelable(true);//默认true
//        mDefaultDialog.setCanceledOnTouchOutside(false);//默认true
//
//    }


}
