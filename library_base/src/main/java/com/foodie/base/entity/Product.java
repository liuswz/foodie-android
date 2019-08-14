package com.foodie.base.entity;

import androidx.lifecycle.MutableLiveData;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
@NoArgsConstructor
@Data
@Accessors(chain=true)

public class Product implements Serializable {


    private Integer id;
    private String productName;
    private String productTypeName;
    private Integer productSales;
    private String productIntro;
    private Double priceForShop;
    private Double priceForUser;
    private String photoUrl;
    private String moneyOffIds;
    private String fullNum;
    private String minusNum;
    private MutableLiveData<Integer> num;


}
