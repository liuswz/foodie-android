package com.foodie.order.bindingParam;
import android.view.View;

import com.foodie.base.entity.Dish;
import com.foodie.base.entity.SimpleOrder;

/**
 * Created by mikeluo on 2019/3/18.
 */

public interface OrderClickCallBack {
    void onClick(SimpleOrder order, View view);
}
