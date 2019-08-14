package com.foodie.product.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.duma.ld.mylibrary.SwitchView;
import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.base.Constant;
import com.foodie.base.dto.SearchFindShopDto;
import com.foodie.base.dto.VoucherForUserDto;
import com.foodie.base.entity.DishBase;
import com.foodie.base.entity.ProductBase;
import com.foodie.base.enums.PayStatus;
import com.foodie.base.enums.PayWay;
import com.foodie.base.enums.ResultCode;
import com.foodie.base.util.ACache;
import com.foodie.base.util.ClearShopCarEvent;
import com.foodie.base.util.RxBus;
import com.foodie.product.R;
import com.foodie.product.adapter.viewAdapter.OrderGoodCarRecyclerAdapter;
import com.foodie.product.databinding.ActivityOrderDetailBinding;
import com.foodie.product.entity.ProductForDeliveryOrder;
import com.foodie.product.entity.ProductForGoShopOrder;
import com.foodie.product.ui.viewmodel.OrderDetailViewModel;
import com.foodie.product.util.GetJsonDataUtil;
import com.foodie.product.util.JsonBean;
import com.foodie.product.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rxjava.rxlife.RxLife;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnViewClickListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OrderDetailActivity extends AppCompatActivity  implements AMapLocationListener {

    private Gson gson;

    private ActivityOrderDetailBinding activityOrderDetailBinding;
    private OrderDetailViewModel viewModel;
    private SwitchView switchView;
    private OrderGoodCarRecyclerAdapter orderGoodCarRecyclerAdapter;
    private Double sumPrice=0D;
    private Double sum=0D;
    private String productName = Constant.OPERATER_SHOPNAME;
    private Double minusPrice = 0D;
    private Integer getShopId = 0;
    private Integer  payWay = PayWay.WeChat.getIndex();
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private String city;
    private Double mylat ;
    private Double mylon;
    private String addressOnCity;
    private String addressOnDetail;
    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private ACache mCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Intent intent = getIntent();
        mCache = ACache.get(this);
        activityOrderDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        viewModel = ViewModelProviders.of(this).get(OrderDetailViewModel.class);
        gson = new Gson();
        updateGoodCar(intent.getStringExtra("goodCarItem"),intent.getStringExtra("productSumPrice"),intent.getStringExtra("productIdList"));

        viewModel.setShopName(productName);
        Integer userId =1;
        viewModel.setUserId(userId);
        activityOrderDetailBinding.setGoShopVisibility(true);
        activityOrderDetailBinding.setPayWay("微信");
        activityOrderDetailBinding.setShopName(productName);

        switchView = activityOrderDetailBinding.switchView;
        switchView.setOnClickCheckedListener(switchViewCheck);

        String phoneNum = "18043547071";
        String photoUrl = Constant.OPERATER_SHOP_PHOTOURL;
        viewModel.setPhotoUrl(photoUrl);
        activityOrderDetailBinding.setPhoneNum(phoneNum);
        activityOrderDetailBinding.detailBackPage.setOnClickListener(backPage);
        activityOrderDetailBinding.payByDelivery.setOnClickListener(payByDelivery);
        activityOrderDetailBinding.payByGoshop.setOnClickListener(payByGoshop);
        activityOrderDetailBinding.choosePayWay.setOnClickListener(checkPosition);
        activityOrderDetailBinding.chooseShop.setOnClickListener(chooseShop);
        activityOrderDetailBinding.chooseAddress.setOnClickListener(chooseAddress);
        //初始化定位
        initLocation();
        // 添加所需权限
        getPermissions();

        observeFindShop();

        minusPrice = viewModel.moneyOffMinusMoney();

        sum = sumPrice-minusPrice;
        activityOrderDetailBinding.setMinusPrice(minusPrice);
        activityOrderDetailBinding.setSumPrice(sum);

        observeViewModelRequestStatus(viewModel);
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
    private OnClickEvent payByDelivery = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            ProductForDeliveryOrder order = new ProductForDeliveryOrder();
            String remark = activityOrderDetailBinding.remark.getText().toString().trim();
            String phoneNum = activityOrderDetailBinding.phoneNum.getText().toString().trim();
            String addressDetail = activityOrderDetailBinding.addressDetail.getText().toString().trim();
            if(!StringUtil.isPhone(phoneNum)){
                showDialog("请填写正确的手机号");
            }else if(TextUtils.isEmpty(addressOnCity )){
                showDialog("请填写城市");
            }else if(TextUtils.isEmpty(addressDetail)){
                showDialog("请填写详情地址");
            }else{
                order.setPayStatus(PayStatus.HadPay.getIndex());
                order.setCost(sum);
                order.setRemark(remark);
                order.setPhoneNum(phoneNum);
                order.setCity(city);
                order.setPayWay(payWay);
                order.setMoneyOffCost(minusPrice);
                order.setVoucherCost(0D);
                order.setAddress(addressDetail);
                viewModel.addProductForDeliveryOrder(order);
            }





        }
    };
    private OnClickEvent payByGoshop = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            ProductForGoShopOrder order = new ProductForGoShopOrder();
            String remark = activityOrderDetailBinding.remark.getText().toString().trim();
            String phoneNum = activityOrderDetailBinding.phoneNum.getText().toString().trim();

            if(!StringUtil.isPhone(phoneNum)){
                showDialog("请填写正确的手机号");
            }else if(getShopId==null||getShopId==0){
                showDialog("选择去取的店面");
            }else{
                order.setPayStatus(PayStatus.HadPay.getIndex());
                order.setCost(sum);
                order.setRemark(remark);
                order.setPhoneNum(phoneNum);
                order.setMoneyOffCost(minusPrice);
                order.setVoucherCost(0D);
                order.setPayWay(payWay);
                order.setGetInShopId(getShopId);
                viewModel.addProductForGoShopOrder(order);
            }


        }
    };

    private OnClickEvent chooseAddress = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            if(options3Items.size()==0){
                initJsonData();
            }
            showPickerView();
        }
    };
    private SwitchView.onClickCheckedListener switchViewCheck= new SwitchView.onClickCheckedListener() {
        @Override
        public void onClick() {
            if(switchView.isChecked()){
                activityOrderDetailBinding.setGoShopVisibility(false);
            }else{
                activityOrderDetailBinding.setGoShopVisibility(true);

            }

        }

    };


    private void updateGoodCar( String goodCarItem,String sumPriceString,String idList){

        //   List<Integer> idList =  gson.fromJson(mCache.getAsString("idList"), new TypeToken<List<Integer>>(){}.getType());
        Map<Integer, ProductBase> map=null;

        if(goodCarItem!=null&&!goodCarItem.equals("")){
            map = gson.fromJson(goodCarItem, new TypeToken<Map<Integer, ProductBase>>(){}.getType());
            viewModel.setProductMap(map);
        }
        if(sumPriceString!=null&&!sumPriceString.equals("")){
            sumPrice = Double.parseDouble(sumPriceString);
        }
        if(idList!=null&&!idList.equals("")){
            List<String> ids = gson.fromJson(idList, new TypeToken<List<String>>(){}.getType());
            orderGoodCarRecyclerAdapter = new OrderGoodCarRecyclerAdapter(map,ids);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            // 设置布局管理器
            activityOrderDetailBinding.shopcarRecyclerview.setLayoutManager(manager);
            activityOrderDetailBinding.shopcarRecyclerview.setAdapter(orderGoodCarRecyclerAdapter);

        }else{
            finish();
        }

    }


    public OnClickEvent chooseShop = new OnClickEvent() {

        @Override
        public void singleClick(View v) {
            if(mylat==null||mylat==0){
                Toast.makeText(OrderDetailActivity.this, "定位失败，无法选择店面", Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent(OrderDetailActivity.this, ShopChooseActivity.class);
                intent.putExtra("mylat",mylat+"");
                intent.putExtra("mylon",mylon+"");
                intent.putExtra("city",city);
                startActivity(intent);
            }

        }
    };

    public OnClickEvent checkPosition = new OnClickEvent() {
        @Override
        public void singleClick(View v) {

            new TDialog.Builder(getSupportFragmentManager())
                    .setLayoutRes(R.layout.dialog_pay_way)
                    .setScreenWidthAspect(OrderDetailActivity.this, 1.0f)
                    .setGravity(Gravity.BOTTOM)
                    .addOnClickListener(R.id.tv_wechat, R.id.tv_alipay)
                    .setOnViewClickListener(new OnViewClickListener() {
                        @Override
                        public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
                            switch (view.getId()) {
                                case R.id.tv_wechat:
                                    activityOrderDetailBinding.setPayWay("微信");
                                    payWay = PayWay.WeChat.getIndex();
                                    tDialog.dismiss();
                                    break;
                                case R.id.tv_alipay:
                                    activityOrderDetailBinding.setPayWay("支付宝");
                                    payWay = PayWay.AliPay.getIndex();
                                    tDialog.dismiss();
                                    break;
                            }
                        }
                    })
                    .create()
                    .show();
        }

    };



    private void observeViewModelRequestStatus(final OrderDetailViewModel viewModel) {

        viewModel.getRequestStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer status) {
                if(status== ResultCode.SUCCESS.getIndex()){
                    mCache.remove("goodCarItem");
                    mCache.remove("sumPrice");
                    mCache.remove("idList");
                    RxBus.getInstance().post(new ClearShopCarEvent("B"));
                    Toast.makeText(OrderDetailActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void observeFindShop(){
        RxBus.getInstance().toObservable(SearchFindShopDto.class).subscribe(new io.reactivex.Observer<SearchFindShopDto>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(SearchFindShopDto shopDto) {
                //处理事件
                getShopId = shopDto.getShopId();
                activityOrderDetailBinding.setChooseShopName(shopDto.getShopName());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }
    private void showDialog(String value){

        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
        builder.setTitle(value).setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();

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
                mylat = aMapLocation.getLatitude();
                mylon =aMapLocation.getLongitude();
                city  =aMapLocation.getCity();
                addressOnCity=aMapLocation.getProvince()+aMapLocation.getCity()+aMapLocation.getDistrict();
                addressOnDetail=aMapLocation.getStreet()+aMapLocation.getStreetNum();
                activityOrderDetailBinding.setAddressCity(addressOnCity);
                activityOrderDetailBinding.setAddressDetail(addressOnDetail);
                mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
            }else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    private void showPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";

                String tx = opt1tx + opt2tx + opt3tx;

                addressOnCity=tx;
                city = opt2tx;
                activityOrderDetailBinding.setAddressCity(addressOnCity);
                activityOrderDetailBinding.setAddressDetail(addressOnDetail);

                Toast.makeText(OrderDetailActivity.this, tx, Toast.LENGTH_SHORT).show();
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }
//    private void loadCityData(){
//        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//
//            }
//        });
//        Consumer<String> consumer = new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                initJsonData();
//            }
//        };
////关注点
//        observable.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(consumer);
//    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = viewModel.parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }



    }
}
