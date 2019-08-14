package com.foodie.base.router;

/**
 * 用于组件开发中，ARouter多Fragment跳转的统一路径注册
 * 在这里注册添加路由路径，需要清楚的写好注释，标明功能界面
 * Created by goldze on 2018/6/21
 */

public class RouterFragmentPath {
    /**
     * 首页组件
     */
    public static class Home {
        private static final String HOME = "/home";
        /*首页*/
        public static final String PAGER_HOME = HOME + "/Home";
    }



    /**
     * 消息组件
     */
    public static class Product {
        private static final String PRODUCT = "/product";
        /*消息*/
        public static final String PAGER_PRODUCT = PRODUCT + "/Product";
    }


    /**
     * 订单组件
     */
    public static class Order {
        private static final String ORDER = "/order";
        /*工作*/
        public static final String PAGER_ORDER = ORDER + "/Order";
    }

    /**
     * 用户组件
     */
    public static class User {
        private static final String USER = "/user";
        /*我的*/
        public static final String PAGER_USER = USER + "/User";
    }
}
