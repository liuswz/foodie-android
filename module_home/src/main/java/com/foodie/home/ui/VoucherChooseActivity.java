package com.foodie.home.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.dto.VoucherForUserDto;
import com.foodie.base.enums.VoucherCantUseType;
import com.foodie.base.util.MsgEvent;
import com.foodie.base.util.RxBus;
import com.foodie.home.R;
import com.foodie.home.adapter.viewAdapter.VoucherInpaytRecyclerAdapter;
import com.foodie.home.bindingParam.VoucherInPayClickCallBack;
import com.foodie.home.databinding.ActivityVoucherChooseBinding;
import com.foodie.home.ui.viewmodel.DetailViewModel;
import com.foodie.home.ui.viewmodel.OrderDetailViewModel;
import com.foodie.home.util.DateUtil;

import java.util.List;

public class VoucherChooseActivity extends AppCompatActivity {
    private ActivityVoucherChooseBinding activityVoucherChooseBinding;
    private VoucherInpaytRecyclerAdapter voucherInpaytRecyclerAdapter;
    private OrderDetailViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_choose);

        Intent intent = getIntent();
        Integer shopId = Integer.parseInt(intent.getStringExtra("shopId"));
        Integer userId = 1;
        viewModel = ViewModelProviders.of(this).get(OrderDetailViewModel.class);
        activityVoucherChooseBinding = DataBindingUtil.setContentView(this, R.layout.activity_voucher_choose);
        activityVoucherChooseBinding.backPage.setOnClickListener(backPage);
        viewModel.loadVoucher(shopId,userId);
        observeViewModelVoucher(viewModel);
    }

    private void observeViewModelVoucher(final OrderDetailViewModel viewModel) {

        viewModel.getVoucher().observe(this, new Observer<List<VoucherForUserDto>>() {
            @Override
            public void onChanged(List<VoucherForUserDto> voucherList) {
                if(voucherList.size()==0||voucherList==null){
                    activityVoucherChooseBinding.setRecyclerviewVisibility(false);
                }else{
                    activityVoucherChooseBinding.setRecyclerviewVisibility(true);
                    voucherInpaytRecyclerAdapter = new VoucherInpaytRecyclerAdapter(chooseVoucher,voucherList);
                    activityVoucherChooseBinding.voucherRecyclerview.setAdapter(voucherInpaytRecyclerAdapter);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(VoucherChooseActivity.this);
                    activityVoucherChooseBinding.voucherRecyclerview.setLayoutManager(mLayoutManager);
                }
            }
        });
    }
    private VoucherInPayClickCallBack chooseVoucher  = new VoucherInPayClickCallBack() {
        @Override
        public void onClick(VoucherForUserDto voucher, View view) {
            Integer flag = DateUtil.ifExpire(voucher.getStartDate(),voucher.getDeadLine());
            if(flag== VoucherCantUseType.NotTime.getIndex()){
                Toast.makeText(VoucherChooseActivity.this, "此代金卷还没到时间", Toast.LENGTH_SHORT).show();
            }else if(flag== VoucherCantUseType.Expire.getIndex()){
                Toast.makeText(VoucherChooseActivity.this, "此代金卷已过期", Toast.LENGTH_SHORT).show();
            }else{
                RxBus.getInstance().post(voucher);
                finish();
            }
        }
    };

    private OnClickEvent backPage = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            finish();
        }
    };
}
