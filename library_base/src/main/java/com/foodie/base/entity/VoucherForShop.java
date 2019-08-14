package com.foodie.base.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

//用户使用商家的代金卷
@NoArgsConstructor
@Data
@Accessors(chain=true)
public class VoucherForShop implements Serializable {
    private Integer id;
    private Integer shopId;
    private Double money;

    private String startDate;
    private String deadLine;

    private String createTime;
}
