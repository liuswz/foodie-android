package com.foodie.base.enums;

public enum GetOrderFlag {
    AllOrder("全部订单",0), NotPay("未支付",1) ,HadPay("已支付",2), Appoint("已预约",3);

    private String name ;
    private int index ;

    private GetOrderFlag(String name , int index){
        this.name = name ;
        this.index = index ;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}
