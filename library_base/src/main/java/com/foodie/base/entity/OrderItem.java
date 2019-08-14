package com.foodie.base.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
//@AllArgsConstructor
@Data
@Accessors(chain=true)
public class OrderItem implements Serializable {

    private Integer id;
    private Integer goodType;
    private Integer goodId;
    private String goodName;
    private String photoUrl;
    private Integer goodNum;
    private Double totalCost;
    private Integer orderId;
    private String createTime;
}
