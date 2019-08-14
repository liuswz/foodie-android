package com.foodie.base.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor
//@AllArgsConstructor
@Data
@Accessors(chain=true)
public class SimpleOrderItem implements Serializable {
    private Integer orderId;
    private Integer goodId;
    private String goodName;
    private String photoUrl;
    private Integer goodNum;
    private Double totalCost;

}
