package com.foodie.home.service.networkService;

import com.foodie.base.base.BaseJson;
import com.foodie.base.entity.Dish;
import com.foodie.base.entity.DishType;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DishService {


    @GET("shopdishes/getAllDishType/{shopId}")
    Observable<BaseJson<List<DishType>>> getAllDishType(@Path("shopId") Integer shopId);
    @GET("shopdishes/getHotDish/{shopId}")
    Observable<BaseJson<List<Dish>>> getHotDish(@Path("shopId") Integer shopId);
    @GET("shopdishes/getDishByTypeId/{shopId}/{typeId}")
    Observable<BaseJson<List<Dish>>> getDishByTypeId(@Path("shopId") Integer shopId, @Path("typeId") Integer typeId);
}
