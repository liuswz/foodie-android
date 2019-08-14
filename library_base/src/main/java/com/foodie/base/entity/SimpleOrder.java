package com.foodie.base.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
//@AllArgsConstructor
@Data
@Accessors(chain=true)
public class SimpleOrder {
    private Integer id;
    private String orderNo;
    private double cost;
  //  private Integer num;
    private String showNames;
    private String shopName;
    private String shopPhotoUrl;

  //  private Integer userType; //用户类型
  //  private Integer userId;
    private Integer goodId;
    private Integer goodType;
    private Integer ifGoodHadReach;
    private Integer getWay;
    private Integer payStatus;//是否已支付
    private Integer ifFinish; //是否已结束
    private Integer ifComment;

    private String createTime;
}
