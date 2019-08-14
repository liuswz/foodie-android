package com.foodie.base.enums;

public enum PayStatus {

    NotPay("NOTPAY", 0),PaySome("PAYSOME", 1),HadPay("HADPAY", 2);


    private String name ;
    private Integer index ;

    private PayStatus( String name , Integer index ){
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
