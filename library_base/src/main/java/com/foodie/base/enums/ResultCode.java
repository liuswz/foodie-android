package com.foodie.base.enums;

public enum ResultCode {

    SUCCESS( 0), FAIL( 1);



    private int index ;

    private ResultCode( int index ){

        this.index = index ;
    }


    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

}
