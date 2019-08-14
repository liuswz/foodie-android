package com.foodie.order.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.foodie.base.entity.SimpleOrderItem;
import com.foodie.base.enums.GoodType;
import com.foodie.order.R;
import com.foodie.order.adapter.viewAdapter.OrderGoodCarRecyclerAdapter;
import com.foodie.order.adapter.viewAdapter.OrderItemRecyclerAdapter;
import com.foodie.order.bindingParam.OrderItemClickCallBack;
import com.foodie.order.databinding.ActivityCommentChooseProductBinding;
import com.foodie.order.ui.viewmodel.CommentChooseProductViewModel;
import com.foodie.order.ui.viewmodel.OrderDetailViewModel;

import java.util.List;

public class CommentChooseProductActivity extends AppCompatActivity {
    private ActivityCommentChooseProductBinding activityCommentChooseProductBinding;
    private OrderItemRecyclerAdapter orderItemRecyclerAdapter;
    private CommentChooseProductViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_choose_product);
        activityCommentChooseProductBinding =  DataBindingUtil.setContentView(this, R.layout.activity_comment_choose_product);
        viewModel = ViewModelProviders.of(this).get(CommentChooseProductViewModel.class);
        Intent intent  =  getIntent();
        viewModel.loadOrderItemList(Integer.parseInt( intent.getStringExtra("orderId")));
        observeViewModelOrderItemList(viewModel);


    }

    private OrderItemClickCallBack chooseProduct = new OrderItemClickCallBack() {
        @Override
        public void onClick(SimpleOrderItem orderItem, View view) {
            Intent intent  =new Intent(CommentChooseProductActivity.this, CommentActivity.class);
            intent.putExtra("orderId",orderItem.getOrderId()+"");

            intent.putExtra("goodId",orderItem.getGoodId()+"");
            intent.putExtra("goodType", GoodType.Product.getIndex() +"");
            startActivity(intent);
        }
    };
    private void observeViewModelOrderItemList(final CommentChooseProductViewModel viewModel) {

        viewModel.getOrderItemList().observe(this, new Observer<List<SimpleOrderItem>>() {
            @Override
            public void onChanged(List<SimpleOrderItem> orderItemList) {
                orderItemRecyclerAdapter = new OrderItemRecyclerAdapter(orderItemList,chooseProduct);
                LinearLayoutManager manager = new LinearLayoutManager(CommentChooseProductActivity.this);
                // 设置布局管理器
                activityCommentChooseProductBinding.recyclerview.setLayoutManager(manager);
                activityCommentChooseProductBinding.recyclerview.setAdapter(orderItemRecyclerAdapter);
            }
        });
    }


}
