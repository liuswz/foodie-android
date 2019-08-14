package com.foodie.base.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Data
@Accessors(chain=true)
public class DishBase {

    private Integer id;
    private String name;
    private Double price;

    private String photoUrl;

    private Integer number;
}
