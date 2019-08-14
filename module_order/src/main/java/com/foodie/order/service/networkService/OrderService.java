package com.foodie.order.service.networkService;

import com.foodie.base.base.BaseJson;
import com.foodie.base.entity.Advertisement;
import com.foodie.base.entity.Order;
import com.foodie.base.entity.PageResults;
import com.foodie.base.entity.SimpleOrder;
import com.foodie.base.entity.SimpleOrderItem;
import com.foodie.base.entity.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderService {

    @GET("order/findAllSimpleOrder/{page}/{size}/{userId}/{flag}")
    Observable<PageResults<SimpleOrder>> findAllSimpleOrder(@Path("page") Integer page, @Path("size") Integer size, @Path("userId")  Integer userId, @Path("flag")  Integer flag);
//    @GET("order/findAllNotPayOrder/{page}/{size}/{userId}")
//    Observable<PageResults<SimpleOrder>> findAllNotPayOrder(@Path("page") Integer page, @Path("size") Integer size, @Path("userId")  Integer userId);
//    @GET("order/findAllHadPayOrder/{page}/{size}/{userId}")
//    Observable<PageResults<SimpleOrder>> findAllHadPayOrder(@Path("page") Integer page, @Path("size") Integer size, @Path("userId")  Integer userId);
//    @GET("order/findAllAppointOrder/{page}/{size}/{userId}")
//    Observable<PageResults<SimpleOrder>> findAllAppointOrder(@Path("page") Integer page, @Path("size") Integer size, @Path("userId")  Integer userId);

    @GET("order/getOrderDetails/{id}")
    Observable<BaseJson<Order>> getOrderDetails(@Path("id") Integer id);

    @GET("order/getOrderItems/{orderId}")
    Observable<BaseJson<List<SimpleOrderItem>>> getOrderItems(@Path("orderId") Integer orderId);

    @POST(" order/updatePayStatus")
    Observable<BaseJson<String>> updatePayStatus(@Query("id") Integer id, @Query("payWay") Integer payWay);
    @POST(" order/updateFinishStatus")
    Observable<BaseJson<String>> updateFinishStatus(@Query("id") Integer id);

}

