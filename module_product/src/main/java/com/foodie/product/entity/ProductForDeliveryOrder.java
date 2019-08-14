package com.foodie.product.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor
//@AllArgsConstructor
@Data
@Accessors(chain=true)
public class ProductForDeliveryOrder implements Serializable {

    private Integer id;
    private String orderNo;

    private double cost;
    private Double voucherCost;
    private Double moneyOffCost;

    private Integer num;
    private String showNames;
    private String shopName;
    private String shopPhotoUrl;

    private Integer userType; //用户类型
    private Integer userId;

    private String phoneNum;

    //商品
    private Integer getWay;
    private String city;
    private String address;

    private Integer goodType;
    private Integer goodId;
    private Integer payStatus;//是否已支付
    private Integer ifFinish; //是否已结束
    private Integer ifTransfer;//运营商是否已转账
    private Integer ifComment;
    private Integer payWay;

    private String remark;

    private String createTime;
}
