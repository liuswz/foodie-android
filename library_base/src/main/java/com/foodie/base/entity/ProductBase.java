package com.foodie.base.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Data
@Accessors(chain=true)
public class ProductBase implements Serializable {

    private Integer id;
    private String productName;

    private Double priceForShop;
    private Double priceForUser;
    private String photoUrl;

    private String fullNum;
    private String minusNum;

    private Integer number;
}
