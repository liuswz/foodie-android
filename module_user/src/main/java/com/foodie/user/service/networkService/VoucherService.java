package com.foodie.user.service.networkService;

import com.foodie.base.base.BaseJson;
import com.foodie.base.dto.VoucherDto;
import com.foodie.base.dto.VoucherForUserDto;
import com.foodie.base.entity.PageResults;
import com.foodie.base.entity.VoucherForUser;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface VoucherService {


    @GET("voucher/getVoucherByCity/{page}/{size}/{city}")
    Observable<PageResults<VoucherDto>> getVoucherByCity(@Path("page") Integer page, @Path("size") Integer size,@Path("city") String city);

    @POST("voucher/addShopVoucherForUser")
    Observable<BaseJson<String>> addShopVoucherForUser(@Body VoucherForUser voucher);

    @GET("voucher/getVoucherForUserByUserId/{page}/{size}/{userId}")
    Observable<PageResults<VoucherDto>> getVoucherForUserByUserId(@Path("page") Integer page, @Path("size") Integer size, @Path("userId") Integer userId);

}

