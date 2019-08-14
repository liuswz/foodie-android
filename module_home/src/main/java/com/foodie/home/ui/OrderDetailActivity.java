package com.foodie.home.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.duma.ld.mylibrary.SwitchView;
import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.dto.VoucherForUserDto;
import com.foodie.base.entity.DishBase;
import com.foodie.base.entity.EasyShopDetail;
import com.foodie.base.entity.ProductBase;
import com.foodie.base.entity.Voucher;
import com.foodie.base.enums.PayStatus;
import com.foodie.base.enums.PayWay;
import com.foodie.base.enums.ResultCode;
import com.foodie.base.util.ACache;
import com.foodie.base.util.ClearShopCarEvent;
import com.foodie.base.util.MapUtil;
import com.foodie.base.util.MsgEvent;
import com.foodie.base.util.RxBus;
import com.foodie.home.R;
import com.foodie.home.adapter.viewAdapter.OrderShopCarRecyclerAdapter;
import com.foodie.home.databinding.ActivityOrderDetailBinding;
import com.foodie.home.entity.DishForAppointOrder;
import com.foodie.home.entity.DishForGoShopOrder;
import com.foodie.home.ui.viewmodel.DetailViewModel;
import com.foodie.home.ui.viewmodel.OrderDetailViewModel;
import com.foodie.home.util.DateUtil;
import com.foodie.home.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnViewClickListener;

import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

public class OrderDetailActivity extends AppCompatActivity {

    private Gson gson;
    private OptionsPickerView  pvNoLinkOptions;
    private ActivityOrderDetailBinding activityOrderDetailBinding;
    private OrderDetailViewModel viewModel;
    private SwitchView switchView;
    private OrderShopCarRecyclerAdapter orderShopCarRecyclerAdapter;
    private Double sumPrice=0D;
    private Double sum=0D;
    private Double moneyOffMinusPrice = 0D;
    private Double voucherMinusPrice = 0D;
    private Integer id;
    private String orderDate;
    private String orderTime;
    private Integer voucherId;
    private ACache mCache;
    private Integer  payWay = PayWay.WeChat.getIndex();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        gson = new Gson();
        mCache = ACache.get(this);
        Intent intent = getIntent();
        String shopName = intent.getStringExtra("shopName");
        id = Integer.parseInt(intent.getStringExtra("id"));
        String fullNum = intent.getStringExtra("fullNum");
        String minusNum = intent.getStringExtra("minusNum");
        String photoUrl = intent.getStringExtra("photoUrl");

        activityOrderDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        viewModel = ViewModelProviders.of(this).get(OrderDetailViewModel.class);
        viewModel.setShopName(shopName);
        viewModel.setPhotoUrl(photoUrl);
        viewModel.setShopId(id);
        Integer userId =1;
        viewModel.setUserId(userId);

        activityOrderDetailBinding.setGoShopVisibility(true);
        activityOrderDetailBinding.setAppointVisibility(false);
        activityOrderDetailBinding.setPayWay("微信");

        activityOrderDetailBinding.setShopName(shopName);
        switchView = activityOrderDetailBinding.switchView;
        switchView.setOnClickCheckedListener(switchViewCheck);
        activityOrderDetailBinding.chooseTime.setOnClickListener(chooseTime);
        activityOrderDetailBinding.choosePayWay.setOnClickListener(checkPosition);
        activityOrderDetailBinding.chooseVoucher.setOnClickListener(chooseVoucher);
        String phoneNum = "18043547071";
        activityOrderDetailBinding.setPhoneNum(phoneNum);
        activityOrderDetailBinding.detailBackPage.setOnClickListener(backPage);
        activityOrderDetailBinding.payNow.setOnClickListener(payNow);
        activityOrderDetailBinding.payAfter.setOnClickListener(payAfter);
        activityOrderDetailBinding.paySome.setOnClickListener(paySome);
        activityOrderDetailBinding.payAll.setOnClickListener(payAll);

        initNoLinkOptionsPicker();
        updateGoodCar(intent.getStringExtra("shopCarItem"),intent.getStringExtra("shopSumPrice"),intent.getStringExtra("shopIdList"));
        observeVoucher();
        if(fullNum==null||minusNum==null||fullNum.equals("")||minusNum.equals("")){
            activityOrderDetailBinding.setMoneyOffMinusMoney(0D);
            activityOrderDetailBinding.setMoneyOffDetail("无活动");
        }else{
            String result[] = viewModel.moneyOffMinusMoney(sumPrice,fullNum,minusNum);
            if(result==null){
                activityOrderDetailBinding.setMoneyOffMinusMoney(0D);
                activityOrderDetailBinding.setMoneyOffDetail("无适合您消费的活动");
            }
            else{
                moneyOffMinusPrice=Double.parseDouble(result[1]);
                activityOrderDetailBinding.setMoneyOffMinusMoney(moneyOffMinusPrice);
                activityOrderDetailBinding.setMoneyOffDetail("满"+result[0]+"减"+result[1]);
            }


        }
        sum = sumPrice-moneyOffMinusPrice-voucherMinusPrice;
        activityOrderDetailBinding.setMinusPrice(moneyOffMinusPrice+voucherMinusPrice);
        activityOrderDetailBinding.setSumPrice(sum);

        observeViewModelCollectionStatus(viewModel);
    }

    private OnClickEvent payAfter = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            DishForGoShopOrder order = new DishForGoShopOrder();
            String remark = activityOrderDetailBinding.remark.getText().toString().trim();
            String tableNum = activityOrderDetailBinding.tableNum.getText().toString().trim();
//            if(StringUtil.isNumeric(tableNum)){
//                order.setTableNum(Integer.parseInt(tableNum));
//            }else{
//                showDialog("请填写正确的桌号");
//            }
            order.setPayStatus(PayStatus.NotPay.getIndex());
            order.setCost(sum);
            order.setVoucherCost(voucherMinusPrice);
            order.setMoneyOffCost(moneyOffMinusPrice);
            order.setRemark(remark);
            order.setTableNum(Integer.parseInt(tableNum));
            viewModel.addDishForGoShopOrder(order,voucherId);

        }
    };
    private OnClickEvent payNow = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            DishForGoShopOrder order = new DishForGoShopOrder();
            String remark = activityOrderDetailBinding.remark.getText().toString().trim();
            String tableNum = activityOrderDetailBinding.tableNum.getText().toString().trim();
//            if(StringUtil.isNumeric(tableNum)){
//                order.setTableNum(Integer.parseInt(tableNum));
//            }else{
//                showDialog("请填写正确的桌号");
//            }
            order.setPayStatus(PayStatus.HadPay.getIndex());
            order.setCost(sum);
            order.setRemark(remark);
            order.setPayWay(payWay);
            order.setVoucherCost(voucherMinusPrice);
            order.setMoneyOffCost(moneyOffMinusPrice);
            order.setTableNum(Integer.parseInt(tableNum));
            viewModel.addDishForGoShopOrder(order,voucherId);

        }
    };
    private OnClickEvent paySome = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            DishForAppointOrder order = new DishForAppointOrder();
            String remark = activityOrderDetailBinding.remark.getText().toString().trim();
            String peopleNum = activityOrderDetailBinding.peopleNum.getText().toString().trim();
            String phoneNum = activityOrderDetailBinding.phoneNum.getText().toString().trim();
            if(orderDate==null||orderTime==null){
                showDialog("请填写预订时间");
            }else if("".equals(peopleNum)||StringUtil.isNumeric(peopleNum)){
                showDialog("请填写正确的人数");

            }else if(!StringUtil.isPhone(phoneNum)){
                showDialog("请填写正确的手机号");

            }else {
                order.setPeopleNum(Integer.parseInt(peopleNum));
                order.setGoDate(orderDate);
                order.setGoTime(orderTime);
                order.setPhoneNum(phoneNum);
                order.setPayStatus(PayStatus.PaySome.getIndex());
                order.setCost(sum);
                order.setPayWay(payWay);
                order.setVoucherCost(voucherMinusPrice);
                order.setMoneyOffCost(moneyOffMinusPrice);
                order.setRemark(remark);
                viewModel.addDishForAppointOrder(order,voucherId);
            }

        }
    };
    private OnClickEvent payAll = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            DishForAppointOrder order = new DishForAppointOrder();
            String remark = activityOrderDetailBinding.remark.getText().toString().trim();
            String peopleNum = activityOrderDetailBinding.peopleNum.getText().toString().trim();

            String phoneNum = activityOrderDetailBinding.phoneNum.getText().toString().trim();
            if(orderDate==null||orderTime==null){
                showDialog("请填写预订时间");
            }else if("".equals(peopleNum)||StringUtil.isNumeric(peopleNum)){
                showDialog("请填写正确的人数");
            }else if(!StringUtil.isPhone(phoneNum)){
                showDialog("请填写正确的手机号");

            }else {
                order.setPeopleNum(Integer.parseInt(peopleNum));
                order.setGoDate(orderDate);
                order.setGoTime(orderTime);
                order.setPhoneNum(phoneNum);
                order.setPayStatus(PayStatus.HadPay.getIndex());
                order.setCost(sum);
                order.setVoucherCost(voucherMinusPrice);
                order.setMoneyOffCost(moneyOffMinusPrice);
                order.setPayWay(payWay);
                order.setRemark(remark);
                viewModel.addDishForAppointOrder(order,voucherId);
            }

        }
    };


    private OnClickEvent chooseTime = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            pvNoLinkOptions.show();
        }
    };

    private SwitchView.onClickCheckedListener switchViewCheck= new SwitchView.onClickCheckedListener() {
        @Override
        public void onClick() {
            if(switchView.isChecked()){
                activityOrderDetailBinding.setGoShopVisibility(false);
                activityOrderDetailBinding.setAppointVisibility(true);
            }else{
                activityOrderDetailBinding.setGoShopVisibility(true);
                activityOrderDetailBinding.setAppointVisibility(false);

            }

        }

    };


    private void updateGoodCar( String goodCarItem,String sumPricePrice,String idList){

        //   List<Integer> idList =  gson.fromJson(mCache.getAsString("idList"), new TypeToken<List<Integer>>(){}.getType());
        Map<Integer, DishBase> map=null;
        if(goodCarItem!=null&&!goodCarItem.equals("")){
             map = gson.fromJson(goodCarItem, new TypeToken<Map<Integer, DishBase>>(){}.getType());
             viewModel.setDishMap(map);
        }
        if(sumPricePrice!=null&&!sumPricePrice.equals("")){
            sumPrice = Double.parseDouble(sumPricePrice);
        }
        if(idList!=null&&!idList.equals("")){
            List<String> ids = gson.fromJson(idList, new TypeToken<List<String>>(){}.getType());

            orderShopCarRecyclerAdapter = new OrderShopCarRecyclerAdapter(map,ids);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            // 设置布局管理器
            activityOrderDetailBinding.shopcarRecyclerview.setLayoutManager(manager);
            activityOrderDetailBinding.shopcarRecyclerview.setAdapter(orderShopCarRecyclerAdapter);

        }else{
            finish();
        }

    }

    private void initNoLinkOptionsPicker() {// 不联动的多级选项
        List<String> dayList = DateUtil.getDayList();
        List<String> minuteList = DateUtil.getMinuteList();
        List<String> getShowHourList = DateUtil.getShowHourList();
        List<String> getHourList = DateUtil.getHourList();
        pvNoLinkOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {


                String date = DateUtil.transferDate(dayList.get(options1));
                String time = getHourList.get(options2)+":"+minuteList.get(options3 );

                if(DateUtil.compareTime(date+" "+time)){

                    activityOrderDetailBinding.setTime(dayList.get(options1)+getShowHourList.get(options2)+minuteList.get(options3));
                    orderDate = date;
                    orderTime = time;
                }else {
                    Toast.makeText(OrderDetailActivity.this, "您选择的时间已经过了", Toast.LENGTH_SHORT).show();
                }


            }
        })
//                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
//                    @Override
//                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
//                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
//                        Toast.makeText(OrderDetailActivity.this, str, Toast.LENGTH_SHORT).show();
//                    }
//                })
                // .setSelectOptions(0, 1, 1)
                .build();
        pvNoLinkOptions.setNPicker(dayList, getShowHourList ,minuteList );
        pvNoLinkOptions.setTitleText("选择时间");
        pvNoLinkOptions.setSelectOptions(0, 1, 1);


    }

    public OnClickEvent chooseVoucher = new OnClickEvent() {

        @Override
        public void singleClick(View v) {
            Intent intent = new Intent(OrderDetailActivity.this, VoucherChooseActivity.class);
            intent.putExtra("shopId",id+"");
            startActivity(intent);
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



    private void observeViewModelCollectionStatus(final OrderDetailViewModel viewModel) {

        viewModel.getRequestStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer status) {
                if(status== ResultCode.SUCCESS.getIndex()){
                    mCache.remove("shopCarItem_"+id);
                    mCache.remove("shopSumPrice_"+id);
                    mCache.remove("shopIdList_"+id);

                    RxBus.getInstance().post(new ClearShopCarEvent("A"));
                    Toast.makeText(OrderDetailActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void observeVoucher(){
        RxBus.getInstance().toObservable(VoucherForUserDto.class).subscribe(new io.reactivex.Observer<VoucherForUserDto>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(VoucherForUserDto voucher) {
                //处理事件
                voucherMinusPrice=voucher.getMoney();
                activityOrderDetailBinding.setVoucherMinusMoney(voucherMinusPrice);
                sum = sumPrice-voucherMinusPrice-moneyOffMinusPrice;
                if(sum<0){
                    sum =0D;
                    activityOrderDetailBinding.setMinusPrice(sumPrice);
                }else {
                    activityOrderDetailBinding.setMinusPrice(voucherMinusPrice+moneyOffMinusPrice);

                }
                voucherId = voucher.getId();
                activityOrderDetailBinding.setSumPrice(sum);
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
}
