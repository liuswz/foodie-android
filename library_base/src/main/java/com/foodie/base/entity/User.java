package com.foodie.base.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor
@Data
@Accessors(chain=true)
public class User implements Serializable {
    private Integer id;
    private String phoneNum;
    private String wechatId;
    private String qqId;
    private String username;
    private String password;
    private String nickname;
    private String address;
    private String photoUrl;

}
