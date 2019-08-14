package com.foodie.base.enums;

public enum GoodGetWayType {
    GoToShop("到店吃",0), Appoint("预约",1) ,Delivery("快递",2), GetInShop("到店取",3);

    private String name ;
    private int index ;

    private GoodGetWayType(String name , int index){
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
