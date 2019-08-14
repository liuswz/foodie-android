package com.foodie.foodie;



import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.foodie.base.config.ModuleLifecycleConfig;


/**
 * Created by goldze on 2018/6/21 0021.
 */

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


       // 初始化组件(靠前)
        ModuleLifecycleConfig.getInstance().initModuleAhead(this);
        //....
        //初始化组件(靠后)
        ModuleLifecycleConfig.getInstance().initModuleLow(this);
    }
}
