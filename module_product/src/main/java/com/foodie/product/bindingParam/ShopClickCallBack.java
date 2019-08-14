package com.foodie.product.bindingParam;
import android.view.View;

import com.foodie.base.dto.SearchFindShopDto;

/**
 * Created by mikeluo on 2019/3/18.
 */

public interface ShopClickCallBack {
    void onClick(SearchFindShopDto shop, View view);
   // void onClick(View view, ShopDetail shopDetail);
}
