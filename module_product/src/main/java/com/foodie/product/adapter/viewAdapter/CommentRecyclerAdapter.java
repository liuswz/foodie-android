package com.foodie.product.adapter.viewAdapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.foodie.base.entity.ProductComment;

import com.foodie.product.R;
import com.foodie.product.databinding.CommentItemBinding;

import java.util.ArrayList;
import java.util.List;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.MyViewHolder> {


    private List<ProductComment> commentList;
   // private Context context;
    public CommentRecyclerAdapter() {
        //this.context = context.getApplicationContext();//防止内存泄漏
        commentList=new ArrayList<>();
    }
    public void setData(List<ProductComment> list){
        commentList=list;
        notifyDataSetChanged();
    }
    public void addData(List<ProductComment> list){
        commentList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CommentItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.comment_item,
                        parent, false);

        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(commentList.size()!=0){
            holder.getBinding().setComment(commentList.get(position));
            holder.getBinding().executePendingBindings();

        }

    }

    @Override
    public int getItemCount() {
        return commentList == null ? 0 : commentList.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder{

        CommentItemBinding commentItemBinding;

        public MyViewHolder(CommentItemBinding commentItemBinding) {
            super(commentItemBinding.getRoot());
            this.commentItemBinding = commentItemBinding;
        }

        public CommentItemBinding getBinding() {
            return commentItemBinding;
        }

        public void setBinding(CommentItemBinding commentItemBinding) {
            this.commentItemBinding = commentItemBinding;
        }
    }
}