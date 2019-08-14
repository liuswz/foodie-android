package com.foodie.order.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.base.Constant;
import com.foodie.base.entity.Order;
import com.foodie.base.entity.SimpleOrder;
import com.foodie.base.entity.SimpleOrderItem;
import com.foodie.base.enums.GoodGetWayType;
import com.foodie.base.enums.GoodType;
import com.foodie.base.enums.PayStatus;
import com.foodie.base.enums.PayWay;
import com.foodie.base.enums.ResultCode;
import com.foodie.base.router.RouterActivityPath;
import com.foodie.order.R;
import com.foodie.order.adapter.viewAdapter.OrderGoodCarRecyclerAdapter;
import com.foodie.order.bindingParam.OrderClickByIdCallBack;
import com.foodie.order.bindingParam.OrderClickCallBack;
import com.foodie.order.databinding.ActivityOrderDetailBinding;
import com.foodie.order.ui.viewmodel.OrderDetailViewModel;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnViewClickListener;

import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private Integer id;
    private ActivityOrderDetailBinding activityOrderDetailBinding;
    private OrderDetailViewModel viewModel;
    private OrderGoodCarRecyclerAdapter orderGoodCarRecyclerAdapter;
    private Order order;
    private Double shouldPay=0D;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Intent intent = getIntent();
        id = Integer.parseInt(intent.getStringExtra("id"));

        activityOrderDetailBinding= DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        activityOrderDetailBinding.setCheckGetShop(checkGetShop);
        activityOrderDetailBinding.detailBackPage.setOnClickListener(backPage);
        viewModel = ViewModelProviders.of(this).get(OrderDetailViewModel.class);
        viewModel.loadOrder(id);
        viewModel.loadOrderItemList(id);
        observeViewModelOrder(viewModel);
        observeViewModelOrderItemList(viewModel);
        observeViewModelPayResponse(viewModel);
        observeViewModelPhoneNum(viewModel);

    }
    private void setValue(Order order){
        activityOrderDetailBinding.setGetWayFlag(order.getGetWay());
        switch (order.getGetWay()){
            case 0:
                activityOrderDetailBinding.setGetWay(GoodGetWayType.GoToShop.getName());
                break;
            case 1:
                activityOrderDetailBinding.setGetWay(GoodGetWayType.Appoint.getName());
                break;
            case 2:
                activityOrderDetailBinding.setGetWay(GoodGetWayType.Delivery.getName());
                break;
            case 3:
                activityOrderDetailBinding.setGetWay(GoodGetWayType.GetInShop.getName());
                break;
        }


        if(order.getGetWay()== GoodGetWayType.Appoint.getIndex()&&order.getPayStatus()== PayStatus.PaySome.getIndex()){

            activityOrderDetailBinding.setIfAppointVisibility(true);
            shouldPay = order.getCost()*0.7;
            activityOrderDetailBinding.setPayDetail("应付￥"+shouldPay);
            activityOrderDetailBinding.setShouldPay(shouldPay);
        }else if(order.getPayStatus()== PayStatus.NotPay.getIndex()){
            shouldPay = order.getCost();
            activityOrderDetailBinding.setPayDetail("应付￥"+shouldPay);
            activityOrderDetailBinding.setShouldPay(shouldPay);
        }else if(order.getPayStatus()== PayStatus.HadPay.getIndex()){

            activityOrderDetailBinding.setPayDetail("已付￥"+order.getCost());
        }
    }
    private OrderClickByIdCallBack checkGetShop = new OrderClickByIdCallBack() {
        @Override
        public void onClick(Integer id, View view) {
            ARouter.getInstance().build(RouterActivityPath.Detail.PRODUCTDETAIL)
                    .withInt("id",id)
                    .navigation();
        }
    };
    private OnClickEvent callShop = new OnClickEvent() {
        @Override
        public void singleClick(View v) {

            if(order.getGoodType()== GoodType.Dish.getIndex()){
                viewModel.loadPhoneNum(order.getGoodId());
            }else{
                Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Constant.OPERATER_PHONE));
                startActivity(dialIntent);
            }

        }
    };
    private OnClickEvent copyOrderNo = new OnClickEvent() {
        @Override
        public void singleClick(View v) {

           if(copy(order.getOrderNo())){
               Toast.makeText(OrderDetailActivity.this, "拷贝成功", Toast.LENGTH_SHORT).show();
           }else{
               Toast.makeText(OrderDetailActivity.this, "拷贝失败", Toast.LENGTH_SHORT).show();
           }
        }
    };

    private OnClickEvent payNow =new OnClickEvent() {
        @Override
        public void singleClick(View view) {
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
                                    viewModel.updatePayStatus(order.getId(),PayWay.WeChat.getIndex());
                                    tDialog.dismiss();
                                    break;
                                case R.id.tv_alipay:
                                    viewModel.updatePayStatus(order.getId(),PayWay.AliPay.getIndex());
                                    tDialog.dismiss();
                                    break;
                            }
                        }
                    })
                    .create()
                    .show();
        }
    };

    private void observeViewModelOrder(final OrderDetailViewModel viewModel) {

        viewModel.getOrder().observe(this, new Observer<Order>() {
            @Override
            public void onChanged(Order order1) {
                order = order1;
                activityOrderDetailBinding.setOrder(order);
                setValue(order);
                activityOrderDetailBinding.callShop.setOnClickListener(callShop);
                activityOrderDetailBinding.copyOrderno.setOnClickListener(copyOrderNo);
                activityOrderDetailBinding.payNow.setOnClickListener(payNow);
            }
        });
    }

    private void observeViewModelOrderItemList(final OrderDetailViewModel viewModel) {

        viewModel.getOrderItemList().observe(this, new Observer<List<SimpleOrderItem>>() {
            @Override
            public void onChanged(List<SimpleOrderItem> orderItemList) {
                orderGoodCarRecyclerAdapter = new OrderGoodCarRecyclerAdapter(orderItemList);
                LinearLayoutManager manager = new LinearLayoutManager(OrderDetailActivity.this);
                // 设置布局管理器
                activityOrderDetailBinding.shopcarRecyclerview.setLayoutManager(manager);
                activityOrderDetailBinding.shopcarRecyclerview.setAdapter(orderGoodCarRecyclerAdapter);
            }
        });
    }

    private void observeViewModelPhoneNum(final OrderDetailViewModel viewModel) {

        viewModel.getPhoneNum().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String phonenum) {
                Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phonenum));
                startActivity(dialIntent);
            }
        });
    }

    private void observeViewModelPayResponse(final OrderDetailViewModel viewModel) {

        viewModel.getPayResponse().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer flag) {
                if(flag== ResultCode.SUCCESS.getIndex()){
                    Toast.makeText(OrderDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    viewModel.loadOrder(id);
                }

            }
        });
    }


    private boolean copy(String copyStr) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private OnClickEvent backPage = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            finish();
        }
    };
}
