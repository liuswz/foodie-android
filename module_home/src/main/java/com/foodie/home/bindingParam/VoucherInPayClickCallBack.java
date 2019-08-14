package com.foodie.home.bindingParam;
import android.view.View;

import com.foodie.base.dto.VoucherForUserDto;
import com.foodie.base.entity.ShopDetail;

/**
 * Created by mikeluo on 2019/3/18.
 */

public interface VoucherInPayClickCallBack {
    void onClick(VoucherForUserDto voucher, View view);
   // void onClick(View view, ShopDetail shopDetail);
}
