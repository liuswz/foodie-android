package com.foodie.base.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
@NoArgsConstructor
@Data
@Accessors(chain=true)
public class SearchShop implements Serializable {

    private Integer id;
    private String shopName;
    private String shopCity;
    private String shopTypeName;
    private String photoUrl;
    private Double shopMark;
    private Integer shopSales;
    private String shopNotice;
    private String fullNum;
    private String minusNum;
    private String distance;
    private List<SimpleDish> dishList;
}
