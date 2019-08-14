package com.foodie.base.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
@NoArgsConstructor
@Data
@Accessors(chain=true)
public class OrderAddParam<T> implements Serializable {
    private T order;
    private String orderItemList;
    private Integer voucherId;
}
