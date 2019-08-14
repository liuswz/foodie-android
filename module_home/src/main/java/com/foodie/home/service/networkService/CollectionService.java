package com.foodie.home.service.networkService;

import com.foodie.base.base.BaseJson;
import com.foodie.base.entity.ShopCollect;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CollectionService {
    @POST("usershopdetail/addShopCollect")
    Observable<BaseJson<Integer>> addShopCollect(@Body ShopCollect collect);
    @GET("usershopdetail/checkShopCollect/{userId}/{shopId}")
    Observable<BaseJson<Integer>> checkShopCollect(@Path("userId") Integer userId,@Path("shopId") Integer shopId);

}
