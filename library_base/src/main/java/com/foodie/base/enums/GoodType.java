package com.foodie.base.enums;

public enum GoodType {
    Dish("菜品",0), Product("商品",1);


    private String name ;
    private int index ;

    private GoodType(String name , int index){
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
