package com.foodie.home.myview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.foodie.base.entity.SimpleDish;
import com.foodie.home.R;
import com.foodie.home.databinding.SimpleDishBinding;

public class MyLinearLayout extends LinearLayout {
    private SimpleDishBinding simpleDishBinding;
    public MyLinearLayout(Context context, SimpleDish dish) {
        super(context);
        initView(context,dish);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);


    }
    private void initView(Context context, SimpleDish dish)
    {
        LayoutInflater inflater= (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        simpleDishBinding = DataBindingUtil.inflate(inflater, R.layout.simple_dish, null, false);
        simpleDishBinding.setDish(dish);
        addView(simpleDishBinding.getRoot());
    }
}
