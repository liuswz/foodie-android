package com.foodie.home.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor
//@AllArgsConstructor
@Data
@Accessors(chain=true)
public class DishForAppointOrder implements Serializable {

    private Integer id;
    private String orderNo;

    private double cost;
    private Double voucherCost;
    private Double moneyOffCost;

    private Integer num;
    private String showNames;
    private String shopName;
    private String shopPhotoUrl;
   // private Integer tableNum;

    private Integer userType; //用户类型
    private Integer userId;

    private String phoneNum;
    private String goDate;
    private String goTime;
    private Integer peopleNum;
    private Integer getWay;
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
