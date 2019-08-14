package com.foodie.home.adapter.viewAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;

import androidx.recyclerview.widget.RecyclerView;

import com.foodie.base.entity.Dish;
import com.foodie.home.R;
import com.foodie.home.bindingParam.AddHadAddDishCallBack;
import com.foodie.home.databinding.ShopcarDishItemBinding;


import java.util.HashMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShopCarRecyclerAdapter extends RecyclerView.Adapter<ShopCarRecyclerAdapter.MyViewHolder> {


    private final AddHadAddDishCallBack addHadAddDishCallback;
    private final AddHadAddDishCallBack minusHadAddDishCallback;
    private LifecycleOwner owner;
    private List<String> idList;
    private Map<Integer, Dish> hadAddDishMap;
    // private Context context;
    public ShopCarRecyclerAdapter(AddHadAddDishCallBack addHadAddDishCallback, AddHadAddDishCallBack minusHadAddDishCallback) {

        this.addHadAddDishCallback = addHadAddDishCallback;
        this.minusHadAddDishCallback = minusHadAddDishCallback;

        //this.context = context.getApplicationContext();//防止内存泄漏
        hadAddDishMap=new HashMap<Integer, Dish>();
        idList = new LinkedList<>();
    }
    public void setData(Map<Integer, Dish> map, List<String> list){
        hadAddDishMap=map;
        idList =list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ShopCarRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ShopcarDishItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.shopcar_dish_item,
                        parent, false);

        return new ShopCarRecyclerAdapter.MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ShopCarRecyclerAdapter.MyViewHolder holder, int position) {
        if(idList.size()!=0){

            ShopcarDishItemBinding shopcarDishItemBinding = holder.getBinding();
            Integer id = Integer.parseInt(idList.get(position));
            shopcarDishItemBinding.setDish(hadAddDishMap.get(id));
            shopcarDishItemBinding.setAddHadAddDishCallback(addHadAddDishCallback);
            shopcarDishItemBinding.setMinusHadAddDishCallback(minusHadAddDishCallback);
            shopcarDishItemBinding.setId(id);
            shopcarDishItemBinding.executePendingBindings();
        }

    }

    @Override
    public int getItemCount() {
        return idList == null ? 0 : idList.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder{

        ShopcarDishItemBinding shopcarDishItemBinding;

        public MyViewHolder(ShopcarDishItemBinding shopcarDishItemBinding) {
            super(shopcarDishItemBinding.getRoot());
            this.shopcarDishItemBinding = shopcarDishItemBinding;
        }
        public ShopcarDishItemBinding getBinding() {
            return shopcarDishItemBinding;
        }
        public void setBinding(ShopcarDishItemBinding shopcarDishItemBinding) {
            this.shopcarDishItemBinding = shopcarDishItemBinding;
        }
    }
}