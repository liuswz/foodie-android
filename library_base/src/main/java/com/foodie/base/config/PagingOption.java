package com.foodie.base.config;

import androidx.paging.PagedList;

public class PagingOption {

    public static PagedList.Config getConfig(){
        return new PagedList.Config.Builder()
                .setPageSize(10)                         //配置分页加载的数量
                .setEnablePlaceholders(false)     //配置是否启动PlaceHolders
                .setInitialLoadSizeHint(10)              //初始化加载的数量
                .build();
    }
}
