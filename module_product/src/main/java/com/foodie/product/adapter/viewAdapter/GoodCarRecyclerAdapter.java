package com.foodie.product.adapter.viewAdapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.foodie.product.R;
import com.foodie.product.bindingParam.GoodCarCallBack;
import com.foodie.product.databinding.GoodcarProductItemBinding;
import com.foodie.base.entity.Product;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GoodCarRecyclerAdapter extends RecyclerView.Adapter<GoodCarRecyclerAdapter.MyViewHolder> {

    private final GoodCarCallBack addGoodCallback;
    private final GoodCarCallBack minusGoodCallback;
    private LifecycleOwner owner;
    private List<String> idList;
    private Map<Integer, Product> goodCarItemMap;
    // private Context context;
    public GoodCarRecyclerAdapter(GoodCarCallBack addGoodCallback, GoodCarCallBack minusGoodCallback) {

        this.addGoodCallback = addGoodCallback;
        this.minusGoodCallback = minusGoodCallback;

        //this.context = context.getApplicationContext();//防止内存泄漏
        goodCarItemMap=new HashMap<Integer, Product>();
        idList = new LinkedList<>();
    }
    public void setData(Map<Integer, Product> map, List<String> list){
        goodCarItemMap=map;
        idList =list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public GoodCarRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GoodcarProductItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.goodcar_product_item,
                        parent, false);

        return new GoodCarRecyclerAdapter.MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull GoodCarRecyclerAdapter.MyViewHolder holder, int position) {
        if(idList.size()!=0){

            GoodcarProductItemBinding shopcarDishItemBinding = holder.getBinding();
            Integer id = Integer.parseInt(idList.get(position));
            shopcarDishItemBinding.setProduct(goodCarItemMap.get(id));
            shopcarDishItemBinding.setAddGoodCallback(addGoodCallback);
            shopcarDishItemBinding.setMinusGoodCallback(minusGoodCallback);
            shopcarDishItemBinding.setId(id);
            shopcarDishItemBinding.executePendingBindings();
        }

    }

    @Override
    public int getItemCount() {
        return idList == null ? 0 : idList.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder{

        GoodcarProductItemBinding goodcarProductItemBinding;

        public MyViewHolder(GoodcarProductItemBinding goodcarProductItemBinding) {
            super(goodcarProductItemBinding.getRoot());
            this.goodcarProductItemBinding = goodcarProductItemBinding;
        }
        public GoodcarProductItemBinding getBinding() {
            return goodcarProductItemBinding;
        }
        public void setBinding(GoodcarProductItemBinding shopcarDishItemBinding) {
            this.goodcarProductItemBinding = goodcarProductItemBinding;
        }
    }
}