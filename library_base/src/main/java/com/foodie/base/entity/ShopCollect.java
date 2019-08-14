package com.foodie.base.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor
@Data
@Accessors(chain=true)
public class ShopCollect implements Serializable {

    private Integer id;
    private Integer userId;
    private Integer shopId;
    private String createTime;
}