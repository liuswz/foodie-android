package com.foodie.home.adapter.viewAdapter;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.foodie.base.entity.ShopComment;
import com.foodie.home.R;
import com.foodie.home.bindingParam.ShopClickCallBack;
import com.foodie.home.databinding.CommentItemBinding;
import com.foodie.home.databinding.CommentItemBinding;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.MyViewHolder> {


    private List<ShopComment> commentList;
   // private Context context;
    public CommentRecyclerAdapter() {
        //this.context = context.getApplicationContext();//防止内存泄漏
        commentList=new ArrayList<>();
    }
    public void setData(List<ShopComment> list){
        commentList=list;
        notifyDataSetChanged();
    }
    public void addData(List<ShopComment> list){
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