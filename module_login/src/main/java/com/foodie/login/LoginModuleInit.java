package com.foodie.login;

import android.app.Application;

import com.foodie.base.base.IModuleInit;
import com.mob.MobSDK;
import com.socks.library.KLog;

/**
 * Created by goldze on 2018/6/21 0021.
 */

public class LoginModuleInit implements IModuleInit {
    @Override
    public boolean onInitAhead(Application application) {
        //MobSDK.init(application);
        KLog.e("消息模块初始化 -- onInitAhead");
        return false;
    }

    @Override
    public boolean onInitLow(Application application) {
        KLog.e("消息模块初始化 -- onInitLow");
        return false;
    }
}
