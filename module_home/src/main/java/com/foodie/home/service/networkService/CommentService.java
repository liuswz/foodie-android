package com.foodie.home.service.networkService;

import com.foodie.base.base.BaseJson;
import com.foodie.base.entity.Advertisement;
import com.foodie.base.entity.PageResults;
import com.foodie.base.entity.ShopComment;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CommentService {

    @GET("order/getAllShopComment/{page}/{size}/{shopId}")
    Observable<PageResults<ShopComment>> getAllShopComment(@Path("page") Integer page, @Path("size") Integer size, @Path("shopId")  Integer shopId);

}

