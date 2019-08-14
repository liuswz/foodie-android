package com.foodie.order.service.networkService;

import com.foodie.base.base.BaseJson;
import com.foodie.base.entity.Order;
import com.foodie.base.entity.PageResults;
import com.foodie.base.entity.SimpleOrder;
import com.foodie.base.entity.SimpleOrderItem;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ShopService {


    @GET("usershopdetail/getShopPhoneById/{shopId}")
    Observable<BaseJson<String>> getShopPhoneById(@Path("shopId") Integer shopId);
    


}

