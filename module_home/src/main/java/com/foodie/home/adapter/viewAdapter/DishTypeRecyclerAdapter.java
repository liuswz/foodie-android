//package com.foodie.home.adapter.viewAdapter;
//
//import android.util.TypedValue;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.databinding.DataBindingUtil;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.foodie.base.entity.DishType;
//import com.foodie.base.entity.ShopDetail;
//import com.foodie.home.R;
//import com.foodie.home.bindingParam.ShopClickCallBack;
//import com.foodie.home.bindingParam.ShopTypeClickCallBack;
//import com.foodie.home.databinding.DishTypeItemBinding;
//import com.foodie.home.databinding.ShopItemBinding;
//import com.nex3z.flowlayout.FlowLayout;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class DishTypeRecyclerAdapter extends RecyclerView.Adapter<DishTypeRecyclerAdapter.MyViewHolder> {
//
//    private final ShopTypeClickCallBack shopTypeClickCallBack;
//    private List<DishType> typeList;
//    public DishTypeRecyclerAdapter(ShopTypeClickCallBack shopTypeClickCallBack) {
//        this.shopTypeClickCallBack = shopTypeClickCallBack;
//        typeList=new ArrayList<>();
//    }
//    public void setData(List<DishType> list){
//        typeList=list;
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public DishTypeRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        DishTypeItemBinding binding = DataBindingUtil
//                .inflate(LayoutInflater.from(parent.getContext()), R.layout.dish_type_item,
//                        parent, false);
//        binding.setCallback(shopTypeClickCallBack);
//        return new DishTypeRecyclerAdapter.MyViewHolder(binding);
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull DishTypeRecyclerAdapter.MyViewHolder holder, int position) {
//        if(typeList.size()!=0){
//            holder.getBinding().setDishType(typeList.get(position));
//            holder.getBinding().executePendingBindings();
//        }
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return typeList == null ? 0 : typeList.size();
//    }
//
//
//
//    static class MyViewHolder extends RecyclerView.ViewHolder{
//
//        DishTypeItemBinding dishTypeItemBinding;
//
//        public MyViewHolder(DishTypeItemBinding dishTypeItemBinding) {
//            super(dishTypeItemBinding.getRoot());
//            this.dishTypeItemBinding = dishTypeItemBinding;
//        }
//        public DishTypeItemBinding getBinding() {
//            return dishTypeItemBinding;
//        }
//
//        public void setBinding(DishTypeItemBinding dishTypeItemBinding) {
//            this.dishTypeItemBinding = dishTypeItemBinding;
//        }
//    }
//}