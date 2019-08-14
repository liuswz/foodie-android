package com.foodie.product.adapter.viewAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.foodie.base.entity.ProductBase;
import com.foodie.base.entity.ProductBase;
import com.foodie.product.R;
import com.foodie.product.databinding.OrderGoodcarItemBinding;


import java.util.List;
import java.util.Map;

public class OrderGoodCarRecyclerAdapter extends RecyclerView.Adapter<OrderGoodCarRecyclerAdapter.MyViewHolder> {

    private Map<Integer, ProductBase> hadAddDishMap;
    private List<String> idList;
   // private Context context;
    public OrderGoodCarRecyclerAdapter(Map<Integer, ProductBase> map, List<String> list) {

        //this.context = context.getApplicationContext();//防止内存泄漏
        hadAddDishMap=map;
        idList =list;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderGoodcarItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.order_goodcar_item,
                        parent, false);

        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(idList.size()!=0){
            OrderGoodcarItemBinding dishItemBinding = holder.getBinding();
            Integer id = Integer.parseInt(idList.get(position));
            dishItemBinding.setProduct(hadAddDishMap.get(id));

            dishItemBinding.executePendingBindings();
        }

    }

    @Override
    public int getItemCount() {
        return idList == null ? 0 : idList.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder{

        OrderGoodcarItemBinding dishItemBinding;

        public MyViewHolder(OrderGoodcarItemBinding dishItemBinding) {
            super(dishItemBinding.getRoot());
            this.dishItemBinding = dishItemBinding;
        }

        public OrderGoodcarItemBinding getBinding() {
            return dishItemBinding;
        }

        public void setBinding(OrderGoodcarItemBinding dishItemBinding) {
            this.dishItemBinding = dishItemBinding;
        }
    }
}