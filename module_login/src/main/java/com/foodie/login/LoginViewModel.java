package com.foodie.login;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.foodie.base.base.BaseJson;
import com.foodie.base.enums.ResultCode;
import com.foodie.base.config.RetrofitServiceManager;
import com.foodie.base.entity.User;
import com.foodie.login.service.networkService.UserService;
import com.rxjava.rxlife.RxLife;
import com.rxjava.rxlife.ScopeViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ScopeViewModel {

    public MutableLiveData<User> userdata;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        userdata = new MutableLiveData<User>();
    }
    public MutableLiveData<User> getUserdata(){
        return userdata;
    }
    public void loginSuccess(String phoneNum){

        User user = new User();
        user.setPhoneNum(phoneNum);
        RetrofitServiceManager.getInstance(getApplication()).create(UserService.class)
                .addUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<User>>as(this))
                .subscribe(new Consumer<BaseJson<User>>() {
                               @Override
                               public void accept(BaseJson<User> baseJson) throws Exception {
                                   if(baseJson.getCode()== ResultCode.SUCCESS.getIndex()){
                                       userdata.setValue(baseJson.getResult());
                                   }


                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );


    }
}
