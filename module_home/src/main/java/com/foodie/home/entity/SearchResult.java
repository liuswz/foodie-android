package com.foodie.home.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@NoArgsConstructor
public class SearchResult<T> implements Serializable {

    private Integer shouldPage;;
    private String message="成功"; //返回信息
    private int code=0; //默认0为成功
    private List<T> rows;
    private Boolean hasMore;
}