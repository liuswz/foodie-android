package com.foodie.home.myview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.foodie.base.entity.Dish;
import com.foodie.home.R;
import com.foodie.home.bindingParam.AddDishCallBack;
import com.foodie.home.databinding.HotDishItemBinding;



public class HotDishLinearLayout extends LinearLayout {
    private HotDishItemBinding hotDishItemBinding;
    public HotDishLinearLayout(Context context, Dish dish, AddDishCallBack addDishCallBack, AddDishCallBack minusDishCallBack, AddDishCallBack getDishDetailCallBack, LifecycleOwner owner) {
        super(context);
        initView(context,dish,addDishCallBack,minusDishCallBack,getDishDetailCallBack,owner);
    }

    public HotDishLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public HotDishLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HotDishLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);


    }
    private void initView(Context context, Dish dish, AddDishCallBack addDishCallBack, AddDishCallBack minusDishCallBack, AddDishCallBack getDishDetailCallBack, LifecycleOwner owner)
    {
        LayoutInflater inflater= (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        hotDishItemBinding = DataBindingUtil.inflate(inflater, R.layout.hot_dish_item, null, false);

        
        if(dish.getNum()==null){
            dish.setNum(new MutableLiveData<Integer>());
            dish.getNum().setValue(0);
            hotDishItemBinding.setMinusVisibility(false);
        }else{
            hotDishItemBinding.setMinusVisibility(true);
        }
        
        dish.getNum().observe(owner, new Observer<Integer>() {
            @Override
            public void onChanged(Integer num) {

                if(num==0){
                    hotDishItemBinding.setMinusVisibility(false);
                    hotDishItemBinding.setDish(dish);
                }else {
                    hotDishItemBinding.setMinusVisibility(true);
                    hotDishItemBinding.setDish(dish);
                }
            }
        });

        hotDishItemBinding.setDish(dish);
        hotDishItemBinding.setAddDishCallback(addDishCallBack);
        hotDishItemBinding.setMinusDishCallback(minusDishCallBack);
        hotDishItemBinding.setGetDishDetailCallback(getDishDetailCallBack);



        addView(hotDishItemBinding.getRoot());
    }

}
