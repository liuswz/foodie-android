package com.foodie.home.service.networkService;

import com.foodie.base.base.BaseJson;
import com.foodie.base.dto.OrderAddParam;
import com.foodie.base.entity.VoucherForUser;
import com.foodie.home.entity.DishForAppointOrder;
import com.foodie.home.entity.DishForGoShopOrder;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderService {

    @POST("order/addDishForAppointOrder")
    Observable<BaseJson<String>> addDishForAppointOrder(@Body DishForAppointOrder order , @Query("orderItemList") String orderItemList, @Query("voucherId") Integer voucherId);

    @POST("order/addDishForGoShopOrder")
    Observable<BaseJson<String>> addDishForGoShopOrder(@Body DishForGoShopOrder order , @Query("orderItemList") String orderItemList,@Query("voucherId") Integer voucherId);

//    @POST("order/addDishForAppointOrder")
//    Observable<BaseJson<String>> addDishForAppointOrder(@Body OrderAddParam<DishForAppointOrder> orderAddParam);
//
//    @POST("order/addDishForGoShopOrder")
//    Observable<BaseJson<String>> addDishForGoShopOrder(@Body  OrderAddParam<DishForGoShopOrder> orderAddParam);

}
