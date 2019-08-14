package com.foodie.home;

import android.app.Application;

import com.foodie.base.base.IModuleInit;


import com.socks.library.KLog;

/**
 * Created by goldze on 2018/6/21 0021.
 */

public class HomeModuleInit implements IModuleInit {
    @Override
    public boolean onInitAhead(Application application) {
        KLog.e("消息模块初始化 -- onInitAhead");
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        KLog.e("消息模块初始化 -- onInitLow");
        return false;
    }
}
