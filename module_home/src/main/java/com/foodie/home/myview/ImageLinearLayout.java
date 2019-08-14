package com.foodie.home.myview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.foodie.base.entity.Dish;
import com.foodie.home.R;
import com.foodie.home.bindingParam.AddDishCallBack;
import com.foodie.home.bindingParam.CheckBigImageCallBack;
import com.foodie.home.databinding.HotDishItemBinding;
import com.foodie.home.databinding.ImageItemBinding;


public class ImageLinearLayout extends LinearLayout {
    private ImageItemBinding imageItemBinding;
    private CheckBigImageCallBack listener;
    public ImageLinearLayout(Context context, String imageUrl, CheckBigImageCallBack listener) {
        super(context);
        initView(context,imageUrl,listener);
    }

    public ImageLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public ImageLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);


    }
    private void initView(Context context, String imageUrl, CheckBigImageCallBack listener)
    {
        LayoutInflater inflater= (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageItemBinding = DataBindingUtil.inflate(inflater, R.layout.image_item, null, false);
        imageItemBinding.setImageUrl(imageUrl);
        imageItemBinding.setCallback(listener);

        addView(imageItemBinding.getRoot());
    }

}
