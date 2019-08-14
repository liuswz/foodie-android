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
public class DishType implements Serializable {

    private Integer id;
    private String typeName;
    private Integer shopId;


    private String createTime;

}
