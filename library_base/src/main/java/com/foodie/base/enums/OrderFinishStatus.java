package com.foodie.base.enums;

public enum OrderFinishStatus {

    NotFinish("未完成", 0),HadFinish("已完成", 1);


    private String name ;
    private int index ;

    private OrderFinishStatus(String name , int index ){
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
