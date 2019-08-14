package com.foodie.order.adapter.viewAdapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.foodie.base.entity.SimpleOrderItem;
import com.foodie.order.R;
import com.foodie.order.bindingParam.OrderItemClickCallBack;
import com.foodie.order.databinding.CommentChooseProductItemBinding;
import com.foodie.order.databinding.CommentChooseProductItemBinding;

import java.util.List;

public class OrderItemRecyclerAdapter extends RecyclerView.Adapter<OrderItemRecyclerAdapter.MyViewHolder> {

    private List<SimpleOrderItem> orderItemList;
    private OrderItemClickCallBack orderItemClickCallBack;
    public OrderItemRecyclerAdapter(List<SimpleOrderItem> list, OrderItemClickCallBack orderItemClickCallBack) {
        orderItemList=list;
        this.orderItemClickCallBack=orderItemClickCallBack;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommentChooseProductItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.comment_choose_product_item,
                        parent, false);

        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(orderItemList.size()!=0){
            CommentChooseProductItemBinding commentChooseProductItemBinding = holder.getBinding();
            commentChooseProductItemBinding.setOrderItem(orderItemList.get(position));
            commentChooseProductItemBinding.setCallBack(orderItemClickCallBack);
            commentChooseProductItemBinding.executePendingBindings();
        }

    }

    @Override
    public int getItemCount() {
        return orderItemList == null ? 0 : orderItemList.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder{

        CommentChooseProductItemBinding commentChooseProductItemBinding;

        public MyViewHolder(CommentChooseProductItemBinding commentChooseProductItemBinding) {
            super(commentChooseProductItemBinding.getRoot());
            this.commentChooseProductItemBinding = commentChooseProductItemBinding;
        }

        public CommentChooseProductItemBinding getBinding() {
            return commentChooseProductItemBinding;
        }

        public void setBinding(CommentChooseProductItemBinding commentChooseProductItemBinding) {
            this.commentChooseProductItemBinding = commentChooseProductItemBinding;
        }
    }
}