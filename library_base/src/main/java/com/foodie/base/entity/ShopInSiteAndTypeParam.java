package com.foodie.base.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Data
@Accessors(chain=true)
public class ShopInSiteAndTypeParam {
    private Double longitude;
    private Double latitude;
    private String shopCity;
    private Integer shopType;

    private Integer page;
    private Integer size;
}
