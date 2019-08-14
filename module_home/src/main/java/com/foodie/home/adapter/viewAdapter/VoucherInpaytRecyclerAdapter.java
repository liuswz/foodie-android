package com.foodie.home.adapter.viewAdapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.foodie.base.dto.VoucherForUserDto;
import com.foodie.home.R;
import com.foodie.home.bindingParam.VoucherInPayClickCallBack;
import com.foodie.home.databinding.VoucherInpayItemBinding;

import java.util.ArrayList;
import java.util.List;

public class VoucherInpaytRecyclerAdapter extends RecyclerView.Adapter<VoucherInpaytRecyclerAdapter.MyViewHolder> {

    private final VoucherInPayClickCallBack voucherInPayClickCallBack;
    private List<VoucherForUserDto> voucherList;
   // private Context context;
    public VoucherInpaytRecyclerAdapter(VoucherInPayClickCallBack voucherInPayClickCallBack,List<VoucherForUserDto> list) {
        this.voucherInPayClickCallBack = voucherInPayClickCallBack;
        //this.context = context.getApplicationContext();//防止内存泄漏
        voucherList=list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        VoucherInpayItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.voucher_inpay_item,
                        parent, false);
        binding.setCallback(voucherInPayClickCallBack);// 双向绑定；自动生成一个setXXX方法
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(voucherList.size()!=0){
            holder.getBinding().setVoucher(voucherList.get(position));
            holder.getBinding().executePendingBindings();
        }

    }

    @Override
    public int getItemCount() {
        return voucherList == null ? 0 : voucherList.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder{

        VoucherInpayItemBinding voucherInpayItemBinding;

        public MyViewHolder(VoucherInpayItemBinding voucherInpayItemBinding) {
            super(voucherInpayItemBinding.getRoot());
            this.voucherInpayItemBinding = voucherInpayItemBinding;
        }

        public VoucherInpayItemBinding getBinding() {
            return voucherInpayItemBinding;
        }

        public void setBinding(VoucherInpayItemBinding voucherInpayItemBinding) {
            this.voucherInpayItemBinding = voucherInpayItemBinding;
        }
    }
}