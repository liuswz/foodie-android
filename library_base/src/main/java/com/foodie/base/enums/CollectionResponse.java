package com.foodie.base.enums;

public enum CollectionResponse {
    HadDelete("HadDELETE", 0), HadAdd("HADADD", 1);

    private String name ;
    private int index ;

    private CollectionResponse( String name , int index ){
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
