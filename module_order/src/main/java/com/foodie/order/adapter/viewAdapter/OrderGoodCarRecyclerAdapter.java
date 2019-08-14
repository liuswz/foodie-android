package com.foodie.order.adapter.viewAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.foodie.base.entity.DishBase;
import com.foodie.base.entity.SimpleOrderItem;
import com.foodie.order.R;
import com.foodie.order.databinding.OrderShopcarItemBinding;

import java.util.List;
import java.util.Map;

public class OrderGoodCarRecyclerAdapter extends RecyclerView.Adapter<OrderGoodCarRecyclerAdapter.MyViewHolder> {

    private List<SimpleOrderItem> orderItemList;

    public OrderGoodCarRecyclerAdapter(List<SimpleOrderItem> list) {

        orderItemList=list;

    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderShopcarItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.order_shopcar_item,
                        parent, false);

        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(orderItemList.size()!=0){
            OrderShopcarItemBinding dishItemBinding = holder.getBinding();
            dishItemBinding.setGood(orderItemList.get(position));
            dishItemBinding.executePendingBindings();
        }

    }

    @Override
    public int getItemCount() {
        return orderItemList == null ? 0 : orderItemList.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder{

        OrderShopcarItemBinding dishItemBinding;

        public MyViewHolder(OrderShopcarItemBinding dishItemBinding) {
            super(dishItemBinding.getRoot());
            this.dishItemBinding = dishItemBinding;
        }

        public OrderShopcarItemBinding getBinding() {
            return dishItemBinding;
        }

        public void setBinding(OrderShopcarItemBinding dishItemBinding) {
            this.dishItemBinding = dishItemBinding;
        }
    }
}