package com.foodie.product.bindingParam;
import android.view.View;

import com.foodie.base.entity.Product;

/**
 * Created by mikeluo on 2019/3/18.
 */

public interface ProductClickCallBack {
    void onClick(Product product, View view);
   // void onClick(View view, ShopDetail shopDetail);
}
