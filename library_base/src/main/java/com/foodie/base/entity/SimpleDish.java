package com.foodie.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor
@Data
@Accessors(chain=true)

public class SimpleDish implements Serializable {
    private Integer id;

    private String dishName;
    private Double dishPrice;
    private String dishPhotoUrl;

}
