package com.foodie.user.service.networkService;

import com.foodie.base.base.BaseJson;
import com.foodie.base.entity.PageResults;
import com.foodie.base.entity.ShopDetail;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CollectionService {


    @GET("usershopdetail/getShopCollect/{page}/{size}/{userId}/{lat}/{lon}")
    Observable<PageResults<ShopDetail>> getShopCollect(@Path("page") Integer page, @Path("size")  Integer size, @Path("userId") Integer userId, @Path("lat") Double lat, @Path("lon") Double lon);


    @GET("usershopdetail/deleteShopCollect/{userId}/{shopId}")
    Observable<BaseJson<String>> deleteShopCollect(@Path("userId") Integer userId, @Path("shopId")  Integer shopId);
}

