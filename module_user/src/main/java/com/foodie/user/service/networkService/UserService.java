package com.foodie.user.service.networkService;

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

public interface UserService {



    @POST("shopdetail/updateUsername")
    Observable<BaseJson<String>> updateUsername(@Query("username") String username,@Query("id") Integer id);
    @POST("shopdetail/updatePhotoUrl")
    Observable<BaseJson<String>> updatePhotoUrl(@Query("photoUrl")  String username,@Query("id") Integer id);

}

