package com.foodie.home.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.entity.DishBase;
import com.foodie.base.entity.EasyShopDetail;
import com.foodie.base.entity.Dish;
import com.foodie.base.entity.Voucher;
import com.foodie.base.enums.CollectionResponse;
import com.foodie.base.enums.ResultCode;
import com.foodie.base.router.RouterActivityPath;
import com.foodie.base.util.ACache;
import com.foodie.base.util.ClearShopCarEvent;
import com.foodie.base.util.MapUtil;
import com.foodie.base.util.RxBus;
import com.foodie.home.R;
import com.foodie.home.adapter.viewAdapter.ShopCarRecyclerAdapter;
import com.foodie.home.adapter.viewAdapter.TabFragmentPagerAdapter;
import com.foodie.home.bindingParam.AddDishCallBack;
import com.foodie.home.bindingParam.AddHadAddDishCallBack;
import com.foodie.home.databinding.ActivityDetailBinding;
import com.foodie.home.myview.MyViewPager;
import com.foodie.home.myview.ShopCarDialog;
import com.foodie.home.ui.fragment.DetailCommentFragment;
import com.foodie.home.ui.fragment.DetailDishFragment;
import com.foodie.home.ui.fragment.DetailShopFragment;
import com.foodie.home.ui.viewmodel.DetailViewModel;
import com.foodie.home.util.ACacheUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nex3z.flowlayout.FlowLayout;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnViewClickListener;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

@Route(path = RouterActivityPath.Detail.SHOPDETAIL)
public class DetailActivity extends AppCompatActivity {
    private DetailViewModel viewModel;
    private ActivityDetailBinding activityDetailBinding;

    private Voucher voucher;
    private TabLayout tabLayout;
    private MyViewPager viewPager;
    private List<Fragment> mFragments;
    private List<String> mTitles;
    private TabFragmentPagerAdapter mAdapter;
    private ShopCarDialog shopCarDialog;
    private ShopCarRecyclerAdapter shopCarRecyclerAdapter;
    private ACache mCache;
    private Gson gson;

    @Autowired()
    int id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();//获取传来的intent对象

        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        if(id==0){
            id = Integer.parseInt(intent.getStringExtra("id"));
        }
        viewModel.setShopId(id);

        mCache = ACache.get(this);
        gson = new Gson();

        activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        tabLayout = activityDetailBinding.tabLayout;
        viewPager = activityDetailBinding.viewpager;
        initViewPager();
        activityDetailBinding.checkPosition.setOnClickListener(checkPosition);
        //设置Viewpager和Tablayout进行联动
        updateGoodCar();
        viewModel.loadEasyShopDetail();
        viewModel.loadVoucher();
        viewModel.checkShopCollect();

        shopCarRecyclerAdapter = new ShopCarRecyclerAdapter(addDishCallback ,minusDishCallback);
        shopCarDialog = new ShopCarDialog(shopCarRecyclerAdapter,clearShopCar);

        activityDetailBinding.voucherLayout.setOnClickListener(addVoucher);
        activityDetailBinding.collectIcon.setOnClickListener(changeCollection);
        activityDetailBinding.shopcarLayout.setOnClickListener(checkShopCar);
        activityDetailBinding.shopcarLayout.setEnabled(false);
        activityDetailBinding.payNow.setOnClickListener(payNow);
        observeViewModeltEasyShopDetail(viewModel);
        observeViewModelVoucher(viewModel);
        observeViewModelVoucherStatus(viewModel);
        observeViewModelCollectionStatus(viewModel);
        observeViewModelDishMap(viewModel);
        observeClearGoodCar();
        //observeViewModelSumPrice(viewModel);

    }


    private OnClickEvent payNow = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            Intent intent = new Intent(DetailActivity.this, OrderDetailActivity.class);
            intent.putExtra("id",viewModel.getShopId()+"");
            EasyShopDetail shopDetail = viewModel.getEasyShopDetail().getValue();
            intent.putExtra("shopName",shopDetail.getShopName());
            intent.putExtra("photoUrl",shopDetail.getPhotoUrl());
            if(shopDetail.getFullNum()!=null&&shopDetail.getMinusNum()!=null){
                intent.putExtra("fullNum",shopDetail.getFullNum());
                intent.putExtra("minusNum",shopDetail.getMinusNum());
            }
            intent.putExtra("shopCarItem",gson.toJson(ACacheUtil.changeFormDish(viewModel.getDishMap().getValue())));
            intent.putExtra("shopSumPrice",viewModel.getSumPrice().toString());
            intent.putExtra("shopIdList",gson.toJson(viewModel.getIdList()));
            startActivity(intent);
        }
    };

    private void updateGoodCar(){
        Integer id = viewModel.getShopId();
        String goodCarItem = mCache.getAsString("shopCarItem_"+id);
        String sumPrice = mCache.getAsString("shopSumPrice_"+id);
        String idList = mCache.getAsString("shopIdList_"+id);
        //   List<Integer> idList =  gson.fromJson(mCache.getAsString("idList"), new TypeToken<List<Integer>>(){}.getType());

        if(goodCarItem!=null&&!goodCarItem.equals("")){
            viewModel.setHadAdddishMap(ACacheUtil.changeToDish(gson.fromJson(goodCarItem, new TypeToken<Map<Integer, DishBase>>(){}.getType())));
        }
        if(sumPrice!=null&&!sumPrice.equals("")){
            viewModel.setSumPrice(Double.parseDouble(sumPrice));
        }
        if(idList!=null&&!idList.equals("")){
            viewModel.setIdList(gson.fromJson(idList, new TypeToken<List<String>>(){}.getType()));
        }

    }
    private static long mLastClickTime = 0;
    public static final long TIME_INTERVAL = 400L;
    private AddHadAddDishCallBack addDishCallback = new AddHadAddDishCallBack() {
        @Override
        public void onClick(Integer dishId,View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime > TIME_INTERVAL) {

                viewModel.addShopCarDishMap(dishId);

                mLastClickTime = nowTime;
            }

        }
    };
    private AddHadAddDishCallBack minusDishCallback = new AddHadAddDishCallBack() {
        @Override
        public void onClick(Integer dishId,View view) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime > TIME_INTERVAL) {

                viewModel.minusShopCarDishMap(dishId);

                mLastClickTime = nowTime;
            }

        }
    };
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

    public OnClickEvent checkShopCar = new OnClickEvent() {
        @Override
        public void singleClick(View v) {

            shopCarDialog.show(getSupportFragmentManager(),"");

        }

    };
    public OnClickEvent clearShopCar = new OnClickEvent() {
        @Override
        public void singleClick(View v) {

            viewModel.clearShopCar();
            Integer id = viewModel.getShopId();
            mCache.remove("shopCarItem_"+id);
            mCache.remove("shopSumPrice_"+id);
            mCache.remove("shopIdList_"+id);
            shopCarDialog.dismiss();

        }

    };
    private void observeViewModelDishMap(final DetailViewModel viewModel) {
        viewModel.getDishMap().observe(this, new Observer<Map<Integer, Dish>>() {
            @Override
            public void onChanged(Map<Integer,Dish> hadAddDishMap) {
                if(hadAddDishMap.size()>0){
                    activityDetailBinding.shopcarLayout.setEnabled(true);
                    activityDetailBinding.payNow.setEnabled(true);
                    activityDetailBinding.payNow.setBackgroundResource(R.color.green);
                }else{
                    activityDetailBinding.shopcarLayout.setEnabled(false);
                    activityDetailBinding.payNow.setEnabled(false);
                    activityDetailBinding.payNow.setBackgroundResource(R.color.grey);
                }
                activityDetailBinding.setViewModel(viewModel);
                shopCarRecyclerAdapter.setData(hadAddDishMap,viewModel.getIdList());


            }
        });
    }
//    private void observeViewModelSumPrice(final DetailViewModel viewModel) {
//        viewModel.getSumPrice().observe(this, new Observer<Double>() {
//            @Override
//            public void onChanged(Double sumprice) {
//                if (sumprice>0){
//
//                }else{
//
//                }
//
//
//            }
//        });
//    }
    private void observeViewModeltEasyShopDetail(final DetailViewModel viewModel) {

        viewModel.getEasyShopDetail().observe(this, new Observer<EasyShopDetail>() {
            @Override
            public void onChanged(EasyShopDetail shopDetail) {
                activityDetailBinding.setShop(shopDetail);
                initMoneyOff(shopDetail.getFullNum(),shopDetail.getMinusNum());
            }
        });
    }
    private void observeViewModelVoucher(final DetailViewModel viewModel) {

        viewModel.getVoucher().observe(this, new Observer<Voucher>() {
            @Override
            public void onChanged(Voucher voucherNew) {
                voucher = voucherNew;
                activityDetailBinding.setVoucher(voucher);
                viewModel.checkVoucher( voucher.getId());
            }
        });
    }
    private void observeViewModelVoucherStatus(final DetailViewModel viewModel) {

        viewModel.getVoucherStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer status) {
                if(status==ResultCode.FAIL.getIndex()){
                    activityDetailBinding.voucherLayout.setClickable(false);
                }
                activityDetailBinding.setViewModel(viewModel);
            }
        });
    }

    private void observeViewModelCollectionStatus(final DetailViewModel viewModel) {

        viewModel.getCollectionStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer status) {
                if(CollectionResponse.HadAdd.getIndex()==status){
                    activityDetailBinding.collectIcon.setBackgroundResource(R.drawable.shoucang);
                }else{
                    activityDetailBinding.collectIcon.setBackgroundResource(R.drawable.shoucang2);
                }
            }
        });
    }
    private void initViewPager() {
        //初始化导航标题,如果是title在json数据中,在初始化的时候可以使用异步任务加载的形式添加
        viewPager.setOffscreenPageLimit(3);
        mTitles=new ArrayList<>();
        mTitles.add("点餐");
        mTitles.add("评价");
        mTitles.add("商家");
        //初始化Fragment
        mFragments=new ArrayList<>();
        for (int i = 0; i <mTitles.size() ; i++) {
            if(i==0){
                mFragments.add(new DetailDishFragment());

            }else if(i==1){
                mFragments.add(new DetailCommentFragment());

            }else if(i==2){
                mFragments.add(new DetailShopFragment());
            }
        }

        //getSupportFragmentManager()是Activity嵌套fragment时使用

        //getChildFragmentManager()是Fragment嵌套Fragment时使用

        mAdapter=new TabFragmentPagerAdapter(getSupportFragmentManager(),mFragments,mTitles);

        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
                if (position == 0) {
                    viewPager.resetHeight(0);
                } else if (position == 1) {
                    viewPager.resetHeight(1);
                } else if (position == 2) {
                    viewPager.resetHeight(2);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0){
                    activityDetailBinding.shopcarBottomlayout.setVisibility(View.VISIBLE);
                }else{
                    activityDetailBinding.shopcarBottomlayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
      //  mAdapter.notifyDataSetChanged();

    }
    public OnClickEvent changeCollection = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            viewModel.addShopCollect();
        }

    };
    public OnClickEvent addVoucher = new OnClickEvent() {
        @Override
        public void singleClick(View v) {

            viewModel.addVoucher(voucher);
        }

    };
    public OnClickEvent checkPosition = new OnClickEvent() {
        @Override
        public void singleClick(View v) {

            new TDialog.Builder(getSupportFragmentManager())
                    .setLayoutRes(R.layout.dialog_change_avatar)
                    .setScreenWidthAspect(DetailActivity.this, 1.0f)
                    .setGravity(Gravity.BOTTOM)
                    .addOnClickListener(R.id.tv_open_camera, R.id.tv_open_album, R.id.tv_tencent)
                    .setOnViewClickListener(new OnViewClickListener() {
                        @Override
                        public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
                            EasyShopDetail easyShopDetail = viewModel.getEasyShopDetail().getValue();
                            Double latx = easyShopDetail.getLatitude();
                            Double laty = easyShopDetail.getLongitude();
                            String mAddress = easyShopDetail.getShopName();
                            switch (view.getId()) {
                                case R.id.tv_open_camera:
                                    if (MapUtil.isGdMapInstalled()) {
                                        MapUtil.openGaoDeNavi(DetailActivity.this, 0, 0, null, latx, laty, mAddress);
                                    } else {
                                        //这里必须要写逻辑，不然如果手机没安装该应用，程序会闪退，这里可以实现下载安装该地图应用
                                        Toast.makeText(DetailActivity.this, "尚未安装高德地图", Toast.LENGTH_SHORT).show();
                                    }
                                    tDialog.dismiss();
                                    break;
                                case R.id.tv_open_album:
                                    if (MapUtil.isBaiduMapInstalled()){

                                        MapUtil.openBaiDuNavi(DetailActivity.this, 0, 0, null, latx, laty, mAddress);

                                    } else {

                                        Toast.makeText(DetailActivity.this, "尚未安装百度地图", Toast.LENGTH_SHORT).show();

                                    }
                                    tDialog.dismiss();
                                    break;
                                case R.id.tv_tencent:
                                    if (MapUtil.isTencentMapInstalled()){
                                        MapUtil.openTencentMap(DetailActivity.this, 0, 0, null, latx, laty, mAddress);
                                    } else {
                                        Toast.makeText(DetailActivity.this, "尚未安装腾讯地图", Toast.LENGTH_SHORT).show();
                                    }

                                    tDialog.dismiss();
                                    break;
                            }
                        }
                    })
                    .create()
                    .show();
        }

    };
    private void observeClearGoodCar(){
        RxBus.getInstance().toObservable(ClearShopCarEvent.class).subscribe(new io.reactivex.Observer<ClearShopCarEvent>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ClearShopCarEvent msgEvent) {
                //处理事件
                viewModel.clearShopCar();
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
        Integer id = viewModel.getShopId();
        if(viewModel.getDishMap().getValue()!=null){
            mCache.put("shopCarItem_"+id,gson.toJson(ACacheUtil.changeFormDish(viewModel.getDishMap().getValue())));
        }
        if(viewModel.getSumPrice()!=0){
            mCache.put("shopSumPrice_"+id,viewModel.getSumPrice().toString());
        }
        if(viewModel.getIdList()!=null){
            mCache.put("shopIdList_"+id, gson.toJson(viewModel.getIdList()));
        }

        super.onPause();
    }

}
