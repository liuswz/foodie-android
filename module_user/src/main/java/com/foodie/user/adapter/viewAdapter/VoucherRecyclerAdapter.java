package com.foodie.user.adapter.viewAdapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.foodie.base.dto.VoucherDto;


import com.foodie.user.R;
import com.foodie.user.bindingParam.VoucherClickCallBack;
import com.foodie.user.databinding.VoucherItemBinding;


import java.util.ArrayList;
import java.util.List;

public class VoucherRecyclerAdapter extends RecyclerView.Adapter<VoucherRecyclerAdapter.MyViewHolder> {

    private final VoucherClickCallBack voucherClickCallBack;
    private List<VoucherDto> voucherList;
   // private Context context;
    public VoucherRecyclerAdapter(VoucherClickCallBack voucherClickCallBack) {
        this.voucherClickCallBack = voucherClickCallBack;
        //this.context = context.getApplicationContext();//防止内存泄漏
        voucherList = new ArrayList<VoucherDto>();
    }
    public void setData(List<VoucherDto> list){
        voucherList=list;
        notifyDataSetChanged();
    }
    public void addData(List<VoucherDto> list){
        voucherList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        VoucherItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.voucher_item,
                        parent, false);
        binding.setCallback(voucherClickCallBack);// 双向绑定；自动生成一个setXXX方法
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

        VoucherItemBinding voucherItemBinding;

        public MyViewHolder(VoucherItemBinding voucherItemBinding) {
            super(voucherItemBinding.getRoot());
            this.voucherItemBinding = voucherItemBinding;
        }

        public VoucherItemBinding getBinding() {
            return voucherItemBinding;
        }

        public void setBinding(VoucherItemBinding voucherItemBinding) {
            this.voucherItemBinding = voucherItemBinding;
        }
    }
}