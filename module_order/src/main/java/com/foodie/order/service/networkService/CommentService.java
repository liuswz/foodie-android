package com.foodie.order.service.networkService;

import com.foodie.base.base.BaseJson;

import com.foodie.base.entity.ProductComment;
import com.foodie.base.entity.ShopComment;



import io.reactivex.Observable;
import retrofit2.http.Body;

import retrofit2.http.POST;


public interface CommentService {



    @POST("comment/addShopComment")
    Observable<BaseJson<String>> addShopComment(@Body ShopComment shopComment);
    @POST("comment/addProductComment")
    Observable<BaseJson<String>> addProductComment(@Body ProductComment shopComment);

}

