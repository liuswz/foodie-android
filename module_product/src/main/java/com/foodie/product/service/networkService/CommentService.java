package com.foodie.product.service.networkService;

import com.foodie.base.entity.PageResults;

import com.foodie.base.entity.ProductComment;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CommentService {

    @GET("order/getAllProductComment/{page}/{size}/{productId}")
    Observable<PageResults<ProductComment>> getAllProductComment(@Path("page") Integer page, @Path("size") Integer size, @Path("productId") Integer productId);

}

