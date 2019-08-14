package com.foodie.home.service.networkService;

import androidx.paging.DataSource;

import com.foodie.base.base.BaseJson;
import com.foodie.base.entity.Advertisement;
import com.foodie.base.entity.EasyShopDetail;
import com.foodie.base.entity.EasyShopDto;
import com.foodie.base.entity.EntireShopDetail;
import com.foodie.base.entity.PageResults;
import com.foodie.base.entity.SearchShop;
import com.foodie.base.entity.ShopDetail;
import com.foodie.base.entity.ShopInSiteAndTypeParam;
import com.foodie.base.entity.ShopInTypeParam;
import com.foodie.base.entity.ShopSearchPrompt;
import com.foodie.home.entity.SearchResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ShopService {

//    @GET("advertisement/getAdvertisementByCity/{city}")
//    Observable<BaseJson<List<Advertisement>>> findAllShopDetails(@Query("longitude") double longitude,@Query("latitude") double latitude,
//    @Query("shopCity") String shopCity,@Query("page") Integer page);
//
   // @Headers("Cache-Control:public ,max-age=60")
    @GET("usershopdetail/findAllShopDetails")
    Observable<PageResults<ShopDetail>> findAllShopDetails(@Query("longitude") double longitude,@Query("latitude") double latitude,
                                                    @Query("shopCity") String shopCity,@Query("page") Integer page,@Query("size") Integer size);
  //  @Headers("Cache-Control:public ,max-age=60")
    @GET("usershopdetail/findAllShopDetailsByMark")
    Observable<PageResults<ShopDetail>> findAllShopDetailsByMark( @Query("longitude") double longitude,@Query("latitude") double latitude,
                                                                  @Query("shopCity") String shopCity,@Query("page") Integer page,@Query("size") Integer size);
  //  @Headers("Cache-Control:public ,max-age=60")
    @GET("usershopdetail/findAllShopDetailsByTypeId")
    Observable<PageResults<ShopDetail>> findAllShopDetailsByTypeId(@Query("longitude") double longitude,@Query("latitude") double latitude,@Query("shopTypeName") String shopTypeName,
                                                                   @Query("shopCity") String shopCity,@Query("page") Integer page,@Query("size") Integer size);
   // @Headers("Cache-Control:public ,max-age=60")
    @GET("usershopdetail/findAllShopDetailsByMarkAndTypeId")
    Observable<PageResults<ShopDetail>> findAllShopDetailsByMarkAndTypeId(@Query("longitude") double longitude,@Query("latitude") double latitude,@Query("shopTypeName") String shopTypeName,
                                                                          @Query("shopCity") String shopCity,@Query("page") Integer page,@Query("size") Integer size);


    @GET("usershopdetail/getShopHotSearchNames")
    Observable<BaseJson<List<String>>> getShopHotSearchNames(@Query("city") String city);

    @GET("usershopdetail/getSearchPromptValue")
    Observable<PageResults<ShopSearchPrompt>> getSearchPromptValue(@Query("city") String city, @Query("value") String value);

    @GET("usershopdetail/searchShopByLocation")
    Observable<SearchResult<SearchShop>> searchShopByLocation(@Query("value") String value, @Query("longitude") double longitude, @Query("latitude") double latitude,
                                                              @Query("shopCity") String shopCity, @Query("page") Integer page, @Query("size") Integer size);
    @GET("usershopdetail/searchShopByMark")
    Observable<SearchResult<SearchShop>> searchShopByMark(@Query("value") String value,@Query("longitude") double longitude,@Query("latitude") double latitude,
                                                             @Query("shopCity") String shopCity,@Query("page") Integer page,@Query("size") Integer size);


    @GET("usershopdetail/getEntireShopDetail/{shopId}")
    Observable<BaseJson<EntireShopDetail>> getEntireShopDetail(@Path("shopId") Integer shopId);

    @GET("usershopdetail/getEasyShopDetail/{shopId}")
    Observable<BaseJson<EasyShopDetail>> getEasyShopDetail(@Path("shopId") Integer shopId);

    @GET("usershopdetail/getEasyShop/{shopId}")
    Observable<BaseJson<EasyShopDto>> getEasyShop(@Path("shopId") Integer shopId);
}

