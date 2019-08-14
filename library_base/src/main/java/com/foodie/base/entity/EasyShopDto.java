package com.foodie.base.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
//@AllArgsConstructor
@Data
@Accessors(chain=true)
public class EasyShopDto {
    private String shopName;
    private String shopCity;
    private String shopTypeName;
    private String shopAddress;
    private String shopPhone;
}
