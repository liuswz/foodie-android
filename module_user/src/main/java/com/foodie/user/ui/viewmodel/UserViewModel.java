package com.foodie.user.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.foodie.base.base.BaseJson;
import com.foodie.base.config.RetrofitServiceManager;
import com.foodie.user.service.networkService.UserService;
import com.rxjava.rxlife.RxLife;
import com.rxjava.rxlife.ScopeViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by goldze on 2018/6/21.
 */

public class UserViewModel extends ScopeViewModel {

    public MutableLiveData<String> photoUrlLiveData;
    public MutableLiveData<String> usernameLiveData;
    public Integer userId;
    public void setUserId( Integer userId){
        this.userId = userId;
    }
    public UserViewModel(@NonNull Application application) {
        super(application);
        photoUrlLiveData = new MutableLiveData<String>();
        usernameLiveData = new MutableLiveData<String>();
    }

    public void updatePhotoUrl(String photoUrl){
        RetrofitServiceManager.getInstance(getApplication()).create(UserService.class)
                .updatePhotoUrl(photoUrl,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<String>>as(this))
                .subscribe(new Consumer<BaseJson<String>>() {
                               @Override
                               public void accept(BaseJson<String> results) throws Exception {
                                   photoUrlLiveData.setValue(results.getResult());
                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }
    public void updateUsername(String username){
        RetrofitServiceManager.getInstance(getApplication()).create(UserService.class)
                .updateUsername(username,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<String>>as(this))
                .subscribe(new Consumer<BaseJson<String>>() {
                               @Override
                               public void accept(BaseJson<String> results) throws Exception {
                                   usernameLiveData.setValue(results.getResult());
                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }

    public MutableLiveData<String> getPhotoUrlLiveData(){
        return photoUrlLiveData;
    }
    public MutableLiveData<String> getUsernameLiveData(){
        return usernameLiveData;
    }
}
