package com.foodie.home.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shop_search_history")
public class ShopSearchHistory {
    @PrimaryKey //主键
    private Integer id;
    @ColumnInfo
    private String value;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
