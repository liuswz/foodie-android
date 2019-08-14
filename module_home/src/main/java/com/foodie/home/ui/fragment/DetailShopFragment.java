package com.foodie.home.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.entity.Dish;
import com.foodie.base.entity.EasyShopDetail;
import com.foodie.base.entity.EasyShopDto;
import com.foodie.base.entity.EntireShopDetail;
import com.foodie.home.R;
import com.foodie.home.bindingParam.CheckBigImageCallBack;
import com.foodie.home.databinding.FragmentDetailDishBinding;
import com.foodie.home.databinding.FragmentDetailShopBinding;
import com.foodie.home.myview.HotDishLinearLayout;
import com.foodie.home.myview.ImageDialog;
import com.foodie.home.myview.ImageLinearLayout;
import com.foodie.home.ui.viewmodel.DetailViewModel;
import com.nex3z.flowlayout.FlowLayout;

import java.util.List;

public class DetailShopFragment extends Fragment {

    private FragmentDetailShopBinding fragmentDetailShopBinding;
    private DetailViewModel viewModel;
    private ImageDialog imageDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentDetailShopBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_shop, container, false);
        fragmentDetailShopBinding.callPhone.setOnClickListener(callPhone);
        //初始化定位

        return fragmentDetailShopBinding.getRoot();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(DetailViewModel.class);
        viewModel.loadEasyShop();
        viewModel.loadEntireShopDetail();
        observeViewModeltEasyShop(viewModel);
        observeViewModeltEntireShopDetail(viewModel);

    }

    private void initFlowLayout(EntireShopDetail shop){
        FlowLayout dishFlowlayout = fragmentDetailShopBinding.imageFlowlayout;
        dishFlowlayout.removeAllViewsInLayout();
        if(shop.getShopPhoto1()!=null){
            ImageLinearLayout imageLinearLayout = new ImageLinearLayout(dishFlowlayout.getContext(),shop.getShopPhoto1(),checkBigImg);
            dishFlowlayout.addView(imageLinearLayout);
        }else if(shop.getShopPhoto2()!=null){
            ImageLinearLayout imageLinearLayout = new ImageLinearLayout(dishFlowlayout.getContext(),shop.getShopPhoto2(),checkBigImg);
            dishFlowlayout.addView(imageLinearLayout);
        }else if(shop.getShopPhoto3()!=null){
            ImageLinearLayout imageLinearLayout = new ImageLinearLayout(dishFlowlayout.getContext(),shop.getShopPhoto3(),checkBigImg);
            dishFlowlayout.addView(imageLinearLayout);
        }else if(shop.getShopPhoto4()!=null){
            ImageLinearLayout imageLinearLayout = new ImageLinearLayout(dishFlowlayout.getContext(),shop.getShopPhoto4(),checkBigImg);
            dishFlowlayout.addView(imageLinearLayout);
        }else if(shop.getShopPhoto5()!=null){
            ImageLinearLayout imageLinearLayout = new ImageLinearLayout(dishFlowlayout.getContext(),shop.getShopPhoto5(),checkBigImg);
            dishFlowlayout.addView(imageLinearLayout);
        }
    }
    private CheckBigImageCallBack checkBigImg = new CheckBigImageCallBack() {

        @Override
        public void onClick(String imgUrl, View view) {
            imageDialog = new ImageDialog(imgUrl);
            imageDialog.show(getFragmentManager(),"");
        }
    };
    private OnClickEvent callPhone = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + viewModel.getEasyShop().getValue().getShopPhone()));//跳转到拨号界面，同时传递电话号码
            startActivity(dialIntent);
        }
    };
    private void observeViewModeltEasyShop(final DetailViewModel viewModel) {

        viewModel.getEasyShop().observe(this, new Observer<EasyShopDto>() {
            @Override
            public void onChanged(EasyShopDto shopDto) {

                fragmentDetailShopBinding.setShop(shopDto);

            }
        });
    }

    private void observeViewModeltEntireShopDetail(final DetailViewModel viewModel) {

        viewModel.getEntireShopDetail().observe(this, new Observer<EntireShopDetail>() {
            @Override
            public void onChanged(EntireShopDetail shopDetail) {
                fragmentDetailShopBinding.setShopdetail(shopDetail);
                initFlowLayout(shopDetail);
            }
        });
    }
}
