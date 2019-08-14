package com.foodie.base.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
@NoArgsConstructor
@Data
@Accessors(chain=true)

public class ShopInTypeParam {

    private String shopCity;
    private String shopType;

    private Integer page;
    private Integer size;
}
