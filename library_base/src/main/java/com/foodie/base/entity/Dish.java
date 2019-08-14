package com.foodie.base.entity;

import androidx.lifecycle.MutableLiveData;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Data
@Accessors(chain=true)
public class Dish {
    private Integer id;
    private String name;
    private Double price;
    private String introduction;
    private String photoUrl;
    private Integer dishSales;
    private Integer shopId;

    private MutableLiveData<Integer> num;

}
