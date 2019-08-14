//package com.foodie.base.entity;
//
//import androidx.lifecycle.MutableLiveData;
//import androidx.room.ColumnInfo;
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
//
////@Entity(tableName = "product")
//public class Product {
//
//    /**
//     * id : 1
//     * productName : 好大米5
//     * productType : 大米
//     * productSales : 0
//     * priceForShop : 5.0
//     * priceForUser : 3.0
//     * photoUrl : https://lanke-foodie.oss-cn-beijing.aliyuncs.com/product/15625938987275773
//     * fullNum : 30,20,10
//     * minusNum : 20,5,2
//     */
//  //  @PrimaryKey //主键
//    private Integer id;
//   // @ColumnInfo
//    private String productName;
// //   @ColumnInfo
//    private String productTypeName;
//   // @ColumnInfo
//    private String productSales;
//    //    @ColumnInfo
//    private Double priceForShop;
//    //   @ColumnInfo
//    private Double priceForUser;
//    //    @ColumnInfo
//    private String photoUrl;
//    //  @ColumnInfo
//    private String fullNum;
//    //   @ColumnInfo
//    private String minusNum;
//
//    private MutableLiveData<Integer> num;
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getProductName() {
//        return productName;
//    }
//
//    public void setProductName(String productName) {
//        this.productName = productName;
//    }
//
//    public String getProductTypeName() {
//        return productTypeName;
//    }
//
//    public void setProductTypeName(String productTypeName) {
//        this.productTypeName = productTypeName;
//    }
//
//    public String getProductSales() {
//        return productSales;
//    }
//
//    public void setProductSales(Integer productSales) {
//        this.productSales = productSales.toString();
//    }
//
//    public Double getPriceForShop() {
//        return priceForShop;
//    }
//
//    public void setPriceForShop(Double priceForShop) {
//        this.priceForShop = priceForShop;
//    }
//
//    public Double getPriceForUser() {
//        return priceForUser;
//    }
//
//    public void setPriceForUser(Double priceForUser) {
//        this.priceForUser = priceForUser;
//    }
//
//    public String getPhotoUrl() {
//        return photoUrl;
//    }
//
//    public void setPhotoUrl(String photoUrl) {
//        this.photoUrl = photoUrl;
//    }
//
//    public String getFullNum() {
//        return fullNum;
//    }
//
//    public void setFullNum(String fullNum) {
//        this.fullNum = fullNum;
//    }
//
//    public String getMinusNum() {
//        return minusNum;
//    }
//
//    public void setMinusNum(String minusNum) {
//        this.minusNum = minusNum;
//    }
//
//    public MutableLiveData<Integer> getNum() {
//        return num;
//    }
//
//    public void setNum(MutableLiveData<Integer> num) {
//        this.num = num;
//    }
//}
