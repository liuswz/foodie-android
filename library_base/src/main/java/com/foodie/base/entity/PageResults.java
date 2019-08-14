package com.foodie.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
@NoArgsConstructor
@Data
@Accessors(chain=true)

public class PageResults<T> implements Serializable {

    private long total;

    private String message; //返回信息
    private int code; //默认0为成功
    private List<T> rows;
}