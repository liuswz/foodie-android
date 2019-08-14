package com.foodie.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Data
@Accessors(chain=true)

public class SearchFindShopDto implements Serializable {

    private Integer shopId;
    private String shopName;
    private String shopCity;

    private String photoUrl;
    private Double shopMark;
    private Integer shopSales;
    private String shopNotice;

    private String fullNum;
    private String minusNum;
    private String distance;
 //   private Integer shopId;

}
