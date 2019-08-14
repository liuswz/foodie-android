package com.foodie.product.service.networkService;

import com.foodie.base.base.BaseJson;
import com.foodie.base.dto.SearchFindShopDto;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ShopService {

    @GET(" usershopdetail/getNearShop")
    Observable<BaseJson<List<SearchFindShopDto>>> getNearShop(@Query("city") String city, @Query("mylat") Double mylat, @Query("mylon") Double mylon);
}
