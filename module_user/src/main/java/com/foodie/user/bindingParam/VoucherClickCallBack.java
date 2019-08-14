package com.foodie.user.bindingParam;
import android.view.View;

import com.foodie.base.dto.VoucherDto;
import com.foodie.base.dto.VoucherForUserDto;

/**
 * Created by mikeluo on 2019/3/18.
 */

public interface VoucherClickCallBack {
    void onClick(VoucherDto voucher, View view);
   // void onClick(View view, ShopDetail shopDetail);
}
