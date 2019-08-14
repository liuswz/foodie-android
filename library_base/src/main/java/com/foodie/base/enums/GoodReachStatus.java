package com.foodie.base.enums;

public enum GoodReachStatus {
    NotReach("未到货",0), HadReach("已到货",1);

    private String name ;
    private int index ;

    private GoodReachStatus(String name , int index){
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
