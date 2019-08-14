package com.foodie.product.service.networkService;

import com.foodie.base.base.BaseJson;
import com.foodie.base.entity.PageResults;
import com.foodie.base.entity.Product;

import com.foodie.base.entity.ShopDetail;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductService {


    @GET("product/findAllProductWithMoneyOff")
    Observable<PageResults<Product>> findAllProductWithMoneyOff(@Query("page") Integer page, @Query("size") Integer size,@Query("value") String value);

    @GET("product/findAllProductWithMoneyOffByType")
    Observable<PageResults<Product>> findAllProductWithMoneyOffByType(@Query("page") Integer page, @Query("size") Integer size, @Query("productType") String productType);


    @GET("product/getHotProductSearchName")
    Observable<BaseJson<List<String>>> getShopHotSearchNames();

    @GET("product/getProductNamesByValue/{value}")
    Observable<PageResults<String>> getSearchPromptValue(@Path("value") String value);

    @GET("product/getProductDetailForUser/{id}")
    Observable<BaseJson<Product>> getProductDetailForUser(@Path("id") Integer id);

}

