package com.foodie.home.adapter.viewAdapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.foodie.base.entity.DishType;
import com.foodie.home.R;
import com.foodie.home.bindingParam.ShopTypeClickCallBack;
import com.foodie.home.databinding.DishTypeItemBinding;

import java.util.ArrayList;
import java.util.List;

public class DishTypeListAdapter extends BaseAdapter {
  //  private final ShopTypeClickCallBack shopTypeClickCallBack;
    private List<DishType> typeList;
    private int mSelect;
    public DishTypeListAdapter(List<DishType> typeList) {
   //     this.shopTypeClickCallBack = shopTypeClickCallBack;
        this.typeList=typeList;
    }
    @Override
    public int getCount() {
        return typeList == null ? 0 : typeList.size();
    }

    @Override
    public Object getItem(int i) {
        return typeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DishTypeItemBinding binding;
        if (view == null) {
            //这里的最后一个参数必须为 false
             binding = DataBindingUtil
                    .inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.dish_type_item,
                            viewGroup, false);
          //  binding.setCallback(shopTypeClickCallBack);
            view = binding.getRoot();
        } else {
            binding = DataBindingUtil.getBinding(view);
        }
        binding.setDishType(typeList.get(i));
        if (mSelect == i) {
            //选中项背景
            binding.typeLayout.setBackgroundResource(R.color.grey);
        } else {
            //其他项背景
            binding.typeLayout.setBackgroundResource(R.color.white);
        }
        return view;

    }

    public void changeSelected(int positon) {
        if (positon != mSelect) {
            mSelect = positon;
            notifyDataSetChanged();
        }
    }

}
