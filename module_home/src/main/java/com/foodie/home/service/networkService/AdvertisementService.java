package com.foodie.home.service.networkService;

import com.foodie.base.base.BaseJson;
import com.foodie.base.entity.Advertisement;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdvertisementService {

    @GET("advertisement/getAdvertisementByCity/{city}")
    Observable<BaseJson<List<Advertisement>>> getAdvertisementByCity(@Path("city") String city);

}

