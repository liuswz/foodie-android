package com.foodie.base.config;

/**
 * Created by goldze on 2018/6/21 0021.
 * 组件生命周期反射类名管理，在这里注册需要初始化的组件，通过反射动态调用各个组件的初始化方法
 * 注意：以下模块中初始化的Module类不能被混淆
 */

public class ModuleLifecycleReflexs {
    private static final String BaseInit = "com.foodie.base.base.BaseModuleInit";
//    //主业务模块
//    private static final String MainInit = "com.foodie.main.MainModuleInit";
    //登录验证模块
  //  private static final String SignInit = "com.foodie.sign.SignModuleInit";
    //首页业务模块
    private static final String HomeInit = "com.foodie.home.HomeModuleInit";

    //消息业务模块
    private static final String ProductInit = "com.foodie.product.ProductModuleInit";
    //工作业务模块
    private static final String OrderInit = "com.foodie.order.OrderModuleInit";
    //用户业务模块
    private static final String UserInit = "com.foodie.user.UserModuleInit";
    private static final String LoginInit = "com.foodie.login.LoginModuleInit";

    public static String[] initModuleNames = {BaseInit,HomeInit,ProductInit,OrderInit,UserInit,LoginInit};
}
