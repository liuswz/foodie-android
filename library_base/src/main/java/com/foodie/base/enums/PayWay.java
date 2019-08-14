package com.foodie.base.enums;

public enum PayWay {

    WeChat("微信", 0),AliPay("支付宝", 1);


    private String name ;
    private Integer index ;

    private PayWay(String name , Integer index ){
        this.name = name ;
        this.index = index ;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getIndex() {
        return index;
    }
    public void setIndex(Integer index) {
        this.index = index;
    }
}
