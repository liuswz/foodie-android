package com.foodie.home.bindingParam;
import android.view.View;

import com.foodie.base.entity.SearchShop;
import com.foodie.base.entity.ShopDetail;

/**
 * Created by mikeluo on 2019/3/18.
 */

public interface ShopSearchClickCallBack {
    void onClick(Integer id, View view);
   // void onClick(View view, ShopDetail shopDetail);
}
