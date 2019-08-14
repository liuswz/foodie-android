package com.foodie.base.enums;

public enum VoucherCantUseType {
    NotTime("代金卷还没到时间",0), Expire("代金卷已过期",1), CanUser("可以使用",2);

    private String name ;
    private int index ;

    private VoucherCantUseType(String name , int index){
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
