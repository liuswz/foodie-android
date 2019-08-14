package com.foodie.home.adapter.viewAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.foodie.base.entity.Dish;
import com.foodie.base.entity.DishBase;
import com.foodie.home.R;
import com.foodie.home.databinding.OrderShopcarItemBinding;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderShopCarRecyclerAdapter extends RecyclerView.Adapter<OrderShopCarRecyclerAdapter.MyViewHolder> {

    private Map<Integer, DishBase> hadAddDishMap;
    private List<String> idList;
   // private Context context;
    public OrderShopCarRecyclerAdapter(Map<Integer, DishBase> map, List<String> list) {

        //this.context = context.getApplicationContext();//防止内存泄漏
        hadAddDishMap=map;
        idList =list;
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
        if(idList.size()!=0){
            OrderShopcarItemBinding dishItemBinding = holder.getBinding();
            Integer id = Integer.parseInt(idList.get(position));
            dishItemBinding.setDish(hadAddDishMap.get(id));
            Log.e("Owen",id+"--------");
            System.out.println(       hadAddDishMap.get(id));

            dishItemBinding.executePendingBindings();
        }

    }

    @Override
    public int getItemCount() {
        return idList == null ? 0 : idList.size();
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