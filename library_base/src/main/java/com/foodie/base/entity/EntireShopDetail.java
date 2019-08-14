package com.foodie.base.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor
@Data
@Accessors(chain=true)
public class EntireShopDetail  implements Serializable {

    private String shopIntro;
    private String shopPhoto1;
    private String shopPhoto2;
    private String shopPhoto3;
    private String shopPhoto4;
    private String shopPhoto5;

}
