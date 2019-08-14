package com.foodie.user.ui;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.content.Context;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;

import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.dto.VoucherDto;

import com.foodie.user.R;
import com.foodie.user.adapter.viewAdapter.MyVoucherRecyclerAdapter;

import com.foodie.user.databinding.ActivityMyVoucherBinding;

import com.foodie.user.ui.viewmodel.CollectionViewModel;
import com.jcodecraeer.xrecyclerview.XRecyclerView;


import java.util.List;

import io.reactivex.functions.Consumer;

public class MyVoucherActivity extends AppCompatActivity {
    private ActivityMyVoucherBinding activityVoucherBinding;
    private CollectionViewModel viewModel;
    private Integer page=1;
    private XRecyclerView xRecyclerView;
    private MyVoucherRecyclerAdapter voucherRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        activityVoucherBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_voucher);
        activityVoucherBinding.backPage.setOnClickListener(backPage);

        viewModel = ViewModelProviders.of(this).get(CollectionViewModel.class);

        SharedPreferences sharedPreferences =getSharedPreferences("user", Context.MODE_PRIVATE);
        Integer  userId = sharedPreferences.getInt("userId", 0);
        viewModel.setUserId(userId);

        initXRecyclerView();
        viewModel.loadMyVoucher(page);
        observeViewModelVoucherList(viewModel);

    }


    private void initXRecyclerView() {
        voucherRecyclerAdapter = new MyVoucherRecyclerAdapter();
//        activityTypeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        xRecyclerView=activityVoucherBinding.xrecyclerView;
        xRecyclerView.setAdapter(voucherRecyclerAdapter);
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
                    viewModel.loadMyVoucher(++page);
                }else{
                    xRecyclerView.setLoadingMoreEnabled(false);
                }
            }
        });

    }

    private void observeViewModelVoucherList(final CollectionViewModel viewModel) {

        viewModel.getVoucher().observe(this, new Observer<List<VoucherDto>>() {
            @Override
            public void onChanged(List<VoucherDto> voucherDtos) {

                voucherRecyclerAdapter.addData(voucherDtos);
                xRecyclerView.loadMoreComplete();

            }
        });
    }

    private OnClickEvent backPage = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            finish();
        }
    };


}
