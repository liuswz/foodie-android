package com.foodie.product.service.networkService;

import com.foodie.base.base.BaseJson;
import com.foodie.product.entity.ProductForDeliveryOrder;
import com.foodie.product.entity.ProductForGoShopOrder;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderService {

    @POST("order/addProductForDeliveryOrder")
    Observable<BaseJson<String>> addProductForDeliveryOrder(@Body ProductForDeliveryOrder order, @Query("orderItemList") String orderItemList);

    @POST("order/addProductForGoShopOrder")
    Observable<BaseJson<String>> addProductForGoShopOrder(@Body ProductForGoShopOrder order, @Query("orderItemList") String orderItemList);

//    @POST("order/addDishForAppointOrder")
//    Observable<BaseJson<String>> addDishForAppointOrder(@Body OrderAddParam<DishForAppointOrder> orderAddParam);
//
//    @POST("order/addDishForGoShopOrder")
//    Observable<BaseJson<String>> addDishForGoShopOrder(@Body  OrderAddParam<DishForGoShopOrder> orderAddParam);

}
