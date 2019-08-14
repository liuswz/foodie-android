package com.foodie.home.service.networkService;

import com.foodie.base.base.BaseJson;
import com.foodie.base.dto.VoucherForUserDto;
import com.foodie.base.entity.EasyShopDetail;
import com.foodie.base.entity.Voucher;
import com.foodie.base.entity.VoucherForUser;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface VoucherService {

    @GET("voucher/getVoucherById/{shopId}")
    Observable<BaseJson<Voucher>> getVoucherById(@Path("shopId") Integer shopId);
    @POST("voucher/addShopVoucherForUser")
    Observable<BaseJson<String>> addShopVoucherForUser(@Body VoucherForUser voucher);
    @GET("voucher/checkVoucherForUser/{shopId}/{userId}/{voucherId}")
    Observable<BaseJson<String>> checkVoucherForUser(@Path("shopId") Integer shopId,@Path("userId") Integer userId,@Path("voucherId") Integer voucherId);


    @GET("voucher/getVoucherForUserById/{shopId}/{userId}")
    Observable<BaseJson<List<VoucherForUserDto>>> getVoucherForUserById(@Path("shopId") Integer shopId, @Path("userId") Integer userId);
//    @GET("voucher/delVoucherForUserById/{id}")
//    BaseJson<String> delVoucherForUserById(@Path("id") Integer id);
}
