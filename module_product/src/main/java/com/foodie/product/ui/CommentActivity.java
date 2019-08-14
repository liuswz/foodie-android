package com.foodie.product.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.foodie.base.entity.ProductComment;
import com.foodie.product.R;
import com.foodie.product.adapter.viewAdapter.CommentRecyclerAdapter;
import com.foodie.product.databinding.ActivityCommentBinding;
import com.foodie.product.ui.viewmodel.CommentViewModel;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private ActivityCommentBinding activityCommentBinding;
    private CommentViewModel viewModel;
    private Integer page=1;
    private XRecyclerView xRecyclerView;
    private CommentRecyclerAdapter commentRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        activityCommentBinding = DataBindingUtil.setContentView(this, R.layout.activity_comment);
        //初始化定位
        Intent intent = getIntent();
        initXRecyclerView();
        viewModel = ViewModelProviders.of(this).get(CommentViewModel.class);
        viewModel.setProductId(Integer.parseInt(intent.getStringExtra("productId")));
        viewModel.loadCommentList(page);
        observeViewModelCommentList(viewModel);
    }

    private void initXRecyclerView() {
        commentRecyclerAdapter = new CommentRecyclerAdapter();
//        activityTypeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        xRecyclerView=activityCommentBinding.xrecyclerView;
        xRecyclerView.setAdapter(commentRecyclerAdapter);
        xRecyclerView.setLayoutManager(mLayoutManager);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }
            @Override
            public void onLoadMore() {
                if(viewModel.getTotal()>page){
                    viewModel.loadCommentList(++page);


                }else{

                    xRecyclerView.setLoadingMoreEnabled(false);
                }
            }
        });

    }
    private void observeViewModelCommentList(final CommentViewModel viewModel) {

        viewModel.getCommentList().observe(this, new Observer<List<ProductComment>>() {
            @Override
            public void onChanged(List<ProductComment> productCommentList) {

                commentRecyclerAdapter.addData(productCommentList);
                xRecyclerView.loadMoreComplete();



            }
        });
    }
}
