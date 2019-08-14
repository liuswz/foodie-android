package com.foodie.home.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.foodie.base.entity.ShopComment;
import com.foodie.base.entity.ShopDetail;
import com.foodie.home.R;
import com.foodie.home.adapter.viewAdapter.CommentRecyclerAdapter;
import com.foodie.home.adapter.viewAdapter.ShopRecyclerAdapter;
import com.foodie.home.databinding.FragmentDetailCommentBinding;
import com.foodie.home.ui.viewmodel.DetailViewModel;
import com.foodie.home.ui.viewmodel.TypeViewModel;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;


public class DetailCommentFragment extends Fragment {

    private FragmentDetailCommentBinding fragmentDetailCommentBinding;
    private CommentRecyclerAdapter commentRecyclerAdapter;
    private Integer page=1;
    private XRecyclerView xRecyclerView;
    private DetailViewModel viewModel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentDetailCommentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_comment, container, false);

        //初始化定位
        initXRecyclerView();
        return fragmentDetailCommentBinding.getRoot();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(DetailViewModel.class);
        viewModel.loadCommentList(page);
        observeViewModelCommentList(viewModel);

    }
    private void initXRecyclerView() {
        commentRecyclerAdapter = new CommentRecyclerAdapter();
//        activityTypeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        xRecyclerView=fragmentDetailCommentBinding.xrecyclerView;
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
    private void observeViewModelCommentList(final DetailViewModel viewModel) {

        viewModel.getCommentList().observe(this, new Observer<List<ShopComment>>() {
            @Override
            public void onChanged(List<ShopComment> shopCommentList) {

                commentRecyclerAdapter.addData(shopCommentList);
                xRecyclerView.loadMoreComplete();



            }
        });
    }

}
