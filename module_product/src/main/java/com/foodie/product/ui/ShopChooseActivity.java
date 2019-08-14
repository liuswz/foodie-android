package com.foodie.product.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.dto.SearchFindShopDto;
import com.foodie.base.dto.VoucherForUserDto;
import com.foodie.base.enums.VoucherCantUseType;
import com.foodie.base.util.RxBus;

import com.foodie.product.R;
import com.foodie.product.adapter.viewAdapter.ShopRecyclerAdapter;
import com.foodie.product.bindingParam.ShopClickCallBack;
import com.foodie.product.databinding.ActivityShopChooseBinding;
import com.foodie.product.ui.viewmodel.OrderDetailViewModel;

import java.util.List;

public class ShopChooseActivity extends AppCompatActivity {
    private ActivityShopChooseBinding activityVoucherChooseBinding;
    private ShopRecyclerAdapter shopRecyclerAdapter;
    private OrderDetailViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_choose);

        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        Double mylat = Double.parseDouble(intent.getStringExtra("mylat"));
        Double mylon = Double.parseDouble(intent.getStringExtra("mylon"));

        viewModel = ViewModelProviders.of(this).get(OrderDetailViewModel.class);
        activityVoucherChooseBinding = DataBindingUtil.setContentView(this, R.layout.activity_shop_choose);
        activityVoucherChooseBinding.backPage.setOnClickListener(backPage);
        viewModel.getNearShop(city,mylat,mylon);
        observeViewModelVoucher(viewModel);
    }

    private void observeViewModelVoucher(final OrderDetailViewModel viewModel) {

        viewModel.getShopList().observe(this, new Observer<List<SearchFindShopDto>>() {
            @Override
            public void onChanged(List<SearchFindShopDto> searchFindShopDtos) {
                if(searchFindShopDtos.size()==0||searchFindShopDtos==null){
                    activityVoucherChooseBinding.setRecyclerviewVisibility(false);
                }else{
                    activityVoucherChooseBinding.setRecyclerviewVisibility(true);
                    shopRecyclerAdapter = new ShopRecyclerAdapter(chooseVoucher,searchFindShopDtos);
                    activityVoucherChooseBinding.voucherRecyclerview.setAdapter(shopRecyclerAdapter);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(ShopChooseActivity.this);
                    activityVoucherChooseBinding.voucherRecyclerview.setLayoutManager(mLayoutManager);
                }
            }
        });
    }
    private ShopClickCallBack chooseVoucher  = new ShopClickCallBack() {
        @Override
        public void onClick(SearchFindShopDto shopDto, View view) {
            RxBus.getInstance().post(shopDto);
            finish();
        }
    };

    private OnClickEvent backPage = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            finish();
        }
    };
}
