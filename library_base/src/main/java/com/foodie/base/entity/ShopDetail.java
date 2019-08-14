package com.foodie.base.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// @Entity(tableName = "shop_detail")
public class ShopDetail {

    /**
     * shopId : 2
     * shopNotice : null
     * shopMark : null
     * shopSales : 0
     * moneyOffIds : null
     * fullNum : null
     * minusNum : null
     * distance : 33.35850000000001
     * shopName : 东方饺子王
     * shopCity : 长春市
     * shopType : 1955
     * photoUrl : https://lanke-foodie.oss-cn-beijing.aliyuncs.com/shop/15622883195626678
     */
    //  @PrimaryKey //主键
    private Integer id;
    //   @ColumnInfo
    private Integer shopId;
    //   @ColumnInfo
    private String shopNotice;
    //  @ColumnInfo
    private Double shopMark;
    //  @ColumnInfo
    private int shopSales;
    private String moneyOffIds;
    // @ColumnInfo
    private String fullNum;
    //   @ColumnInfo
    private String minusNum;
    // @ColumnInfo
    private Double distance;
    //  @ColumnInfo
    private String shopName;
    private String shopCity;
    private String shopTypeName;
    //   @ColumnInfo
    private String photoUrl;
    private String distanceDetail;

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopNotice() {
        return shopNotice;
    }

    public void setShopNotice(String shopNotice) {
        this.shopNotice = shopNotice;
    }

    public Double getShopMark() {
        return shopMark;
    }

    public void setShopMark(Double shopMark) {
        this.shopMark = shopMark;
    }

    public int getShopSales() {
        return shopSales;
    }

    public void setShopSales(int shopSales) {
        this.shopSales = shopSales;
    }

    public String getMoneyOffIds() {
        return moneyOffIds;
    }

    public void setMoneyOffIds(String moneyOffIds) {
        this.moneyOffIds = moneyOffIds;
    }

    public String getFullNum() {
        return fullNum;
    }

    public void setFullNum(String fullNum) {
        this.fullNum = fullNum;
    }

    public String getMinusNum() {
        return minusNum;
    }

    public void setMinusNum(String minusNum) {
        this.minusNum = minusNum;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDistanceDetail() {
        return distanceDetail;
    }

    public void setDistanceDetail(String distanceDetail) {
        this.distanceDetail = distanceDetail;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopCity() {
        return shopCity;
    }

    public void setShopCity(String shopCity) {
        this.shopCity = shopCity;
    }

    public String getShopTypeName() {
        return shopTypeName;
    }

    public void setShopTypeName(String shopTypeName) {
        this.shopTypeName = shopTypeName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
