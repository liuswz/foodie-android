package com.foodie.login.service.networkService;

import com.foodie.base.base.BaseJson;
import com.foodie.base.entity.Advertisement;
import com.foodie.base.entity.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

    @POST("shopdetail/addUser")
    Observable<BaseJson<User>> addUser(@Body User user);

}

