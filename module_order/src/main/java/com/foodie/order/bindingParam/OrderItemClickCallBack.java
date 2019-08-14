package com.foodie.order.bindingParam;
import android.view.View;

import com.foodie.base.entity.SimpleOrder;
import com.foodie.base.entity.SimpleOrderItem;

/**
 * Created by mikeluo on 2019/3/18.
 */

public interface OrderItemClickCallBack {
    void onClick(SimpleOrderItem orderItem, View view);
}
