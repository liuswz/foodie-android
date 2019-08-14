package com.foodie.home.myview;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.entity.Dish;

import com.foodie.home.R;
import com.foodie.home.adapter.viewAdapter.ShopCarRecyclerAdapter;
import com.foodie.home.databinding.DishDialogBinding;
import com.foodie.home.databinding.ShopcarBinding;

import java.util.List;
import java.util.Map;

public class ShopCarDialog extends DialogFragment {
    private Map<Integer, Dish> hadAddDishMap;
    private ShopCarRecyclerAdapter shopCarRecyclerAdapter;
    private List<String> idList;
    private OnClickEvent clickEvent;
    //private View.OnClickListener listener;
    public ShopCarDialog(ShopCarRecyclerAdapter shopCarRecyclerAdapter, OnClickEvent clickEvent){
     //   this.hadAddDishMap = hadAddDishMap;
    //    this.idList = idList;
       // Map<Integer, HadAddDish> hadAddDishMap, List<Integer> idList,
        this.shopCarRecyclerAdapter = shopCarRecyclerAdapter;
        this.clickEvent = clickEvent;

    }
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //  window.setWindowAnimations(R.style.BottomDialog_Animation);
        //设置边距
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        window.setLayout((int) (dm.widthPixels), ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        ShopcarBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.shopcar,
                        container, false);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.shopcarRecyclerview.setLayoutManager(mLayoutManager);
        binding.shopcarRecyclerview.setAdapter(shopCarRecyclerAdapter);
        binding.clearShopcar.setOnClickListener(clickEvent);
        //  binding.closePage.setOnClickListener(listener);
        return binding.getRoot();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}