package com.foodie.base.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor
@Data
@Accessors(chain=true)
public class VoucherForUserDto implements Serializable {
    private Integer id;

    private Double money;
    private String startDate;
    private String deadLine;


}