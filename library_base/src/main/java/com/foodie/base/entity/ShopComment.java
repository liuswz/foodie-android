package com.foodie.base.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor
@Data
@Accessors(chain=true)
public class ShopComment implements Serializable {
    private Integer id;
    private String content;
    private String photoUrl;
    private Double mark;
    private Integer likeNum;
    private Integer shopId;
    private Integer orderId;
    private Integer userId;

    private String username;
    private String userPhotoUrl;

    private String createTime;

}
