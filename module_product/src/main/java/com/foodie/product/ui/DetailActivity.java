package com.foodie.product.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.entity.Product;
import com.foodie.base.entity.Product;
import com.foodie.base.entity.Product;
import com.foodie.base.entity.ProductBase;
import com.foodie.base.router.RouterActivityPath;
import com.foodie.base.router.RouterFragmentPath;
import com.foodie.base.util.ACache;
import com.foodie.base.util.ClearShopCarEvent;
import com.foodie.base.util.MsgEvent;
import com.foodie.base.util.RxBus;
import com.foodie.product.R;
import com.foodie.product.adapter.viewAdapter.GoodCarRecyclerAdapter;

import com.foodie.product.bindingParam.GoodCarCallBack;
import com.foodie.product.bindingParam.ProductClickCallBack;

import com.foodie.product.databinding.ActivityDetailBinding;
import com.foodie.product.databinding.FragmentProductBinding;
import com.foodie.product.myview.GoodCarDialog;
import com.foodie.product.ui.viewmodel.ProductViewModel;
import com.foodie.product.util.ACacheUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nex3z.flowlayout.FlowLayout;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


@Route(path = RouterActivityPath.Detail.PRODUCTDETAIL)
public class DetailActivity extends AppCompatActivity {
    @Autowired()
    int id=0;
    private ActivityDetailBinding activityDetailBinding;
    private ProductViewModel viewModel ;
    //  private ProgressDialog mDefaultDialog;
    private GoodCarDialog goodCarDialog;
    private GoodCarRecyclerAdapter goodCarRecyclerAdapter;
    private ACache mCache;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        viewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        Intent intent = getIntent();//获取传来的intent对象
        if(id==0){
            id = Integer.parseInt(intent.getStringExtra("id"));
        }
        viewModel.setId(id);

        activityDetailBinding.setAddGoodCallback(addProductCallBack);
        activityDetailBinding.setMinusGoodCallback(minusProductCallBack);
        activityDetailBinding.checkComment.setOnClickListener(checkComment);
        mCache = ACache.get(this);
        gson = new Gson();

        //  Map<Integer, Product> goodCarItemMap = gson.fromJson(mCache.getAsString("goodCarItem"), new TypeToken< Map<Integer, Product> >(){}.getType());
        goodCarRecyclerAdapter = new GoodCarRecyclerAdapter(addHadAddDishCallback ,minusHadAddDishCallback);
        goodCarDialog = new GoodCarDialog(goodCarRecyclerAdapter,clearGoodCar);
        activityDetailBinding.shopcarLayout.setOnClickListener(checkGoodCar);
        activityDetailBinding.shopcarLayout.setEnabled(false);
        activityDetailBinding.detailBackPage.setOnClickListener(backPage);
        activityDetailBinding.payNow.setOnClickListener(payNow);
        updateGoodCar();


        observeViewModelProductDetail(viewModel);
        observeViewModelGoodCar(viewModel);
         observeClearGoodCar();

    }
    private void initMoneyOff(String fullNum,String minusNum){
        FlowLayout flowlayout = activityDetailBinding.moneyoffFlowlayout;
        flowlayout.removeAllViewsInLayout();
        if("".equals(fullNum)&&fullNum==null){
            flowlayout=null;
        }else{
            String fullNums[] = fullNum.split(",");
            String minusNums[] = minusNum.split(",");

            for(int i=0;i<fullNums.length;i++){
                TextView tv = new TextView(flowlayout.getContext());
                tv.setText("满"+fullNums[i]+"减"+minusNums[i]);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                tv.setPadding(2,2,2,2);
                tv.setBackgroundResource(R.drawable.tv_bg_shape);
                flowlayout.addView(tv);
            }
        }
    }
    private void observeViewModelProductDetail(final ProductViewModel viewModel) {

        viewModel.getProductDetail().observe(this, new Observer<Product>() {
            @Override
            public void onChanged(Product product) {

                activityDetailBinding.setProduct(product);
                initMoneyOff(product.getFullNum(),product.getMinusNum());
                //  mDefaultDialog.cancel();
                observeViewModelNum(product);
            }
        });
    }
    public OnClickEvent backPage = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            finish();
        }
    };
    public OnClickEvent checkComment = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            Intent intent = new Intent(DetailActivity.this,CommentActivity.class);
            intent.putExtra("productId",id);
            startActivity(intent);
        }
    };

    private void observeViewModelNum(Product product){

        product.getNum().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer num) {

                if(num==0){
                    activityDetailBinding.setMinusVisibility(false);
                    activityDetailBinding.setProduct(product);
                }else {
                    activityDetailBinding.setMinusVisibility(true);
                    activityDetailBinding.setProduct(product);
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
        }
        if(sumPrice!=null&&!sumPrice.equals("")){
            viewModel.setSumPrice(Double.parseDouble(sumPrice));
        }
        if(idList!=null&&!idList.equals("")){
            viewModel.setIdList(gson.fromJson(idList, new TypeToken<List<String>>(){}.getType()));
        }
        viewModel.loadProductDetail();

    }

    private static long mLastClickTime = 0;
    public static final long TIME_INTERVAL = 400L;
    private ProductClickCallBack addProductCallBack = new ProductClickCallBack() {
        @Override
        public void onClick(Product product, View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime > TIME_INTERVAL) {

                Integer num = product.getNum().getValue();
                product.getNum().setValue((num+1));
                viewModel.addProductMap(product);

                mLastClickTime = nowTime;
            }


        }
    };
    private ProductClickCallBack minusProductCallBack = new ProductClickCallBack() {
        @Override
        public void onClick(Product product, View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime > TIME_INTERVAL) {

                Integer num = product.getNum().getValue();
                if (num > 0) {
                    product.getNum().setValue((num - 1) );
                }
                viewModel.minusProductMap(product);

                mLastClickTime = nowTime;
            }


        }
    };
    private OnClickEvent payNow = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            Intent intent = new Intent(DetailActivity.this, OrderDetailActivity.class);

            intent.putExtra("goodCarItem",gson.toJson(ACacheUtil.changeFormProduct(viewModel.getProductMap().getValue())));
            intent.putExtra("productSumPrice",viewModel.getSumPrice().toString());
            intent.putExtra("productIdList",gson.toJson(viewModel.getIdList()));
            startActivity(intent);
        }
    };
    private GoodCarCallBack addHadAddDishCallback = new GoodCarCallBack() {
        @Override
        public void onClick(Integer productId,View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime > TIME_INTERVAL) {

                viewModel.addGoodCarProductMap(productId);

                mLastClickTime = nowTime;
            }

        }
    };
    private GoodCarCallBack minusHadAddDishCallback = new GoodCarCallBack() {
        @Override
        public void onClick(Integer productId,View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime > TIME_INTERVAL) {

                viewModel.minusGoodCarProductMap(productId);

                mLastClickTime = nowTime;
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
            viewModel.clearGoodCarInDetail();
            mCache.remove("goodCarItem");
            mCache.remove("sumPrice");
            mCache.remove("idList");
            goodCarDialog.dismiss();

        }

    };

    private void observeViewModelGoodCar(final ProductViewModel viewModel) {
        viewModel.getProductMap().observe(this, new Observer<Map<Integer, Product>>() {
            @Override
            public void onChanged(Map<Integer,Product> goodCarItemMap) {
                if(goodCarItemMap.size()>0){
                    activityDetailBinding.shopcarLayout.setEnabled(true);
                    activityDetailBinding.payNow.setEnabled(true);
                    activityDetailBinding.payNow.setBackgroundResource(R.color.green);
                }else{
                    activityDetailBinding.shopcarLayout.setEnabled(false);
                    activityDetailBinding.payNow.setEnabled(false);
                    activityDetailBinding.payNow.setBackgroundResource(R.color.grey);
                }
                activityDetailBinding.setViewModel(viewModel);
                goodCarRecyclerAdapter.setData(goodCarItemMap,viewModel.getIdList());


//                mCache.put("goodCarItem",gson.toJson(ACacheUtil.changeFormProduct(goodCarItemMap)));
//                mCache.put("sumPrice",viewModel.getSumPrice().toString());
//                mCache.put("idList", gson.toJson(viewModel.getIdList()));
//
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

                viewModel.clearGoodCarInDetail();
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

}
