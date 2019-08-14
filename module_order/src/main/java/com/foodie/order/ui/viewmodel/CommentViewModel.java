package com.foodie.order.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.foodie.base.base.BaseJson;
import com.foodie.base.config.RetrofitServiceManager;
import com.foodie.base.entity.ProductComment;
import com.foodie.base.entity.ShopComment;
import com.foodie.order.service.networkService.CommentService;
import com.rxjava.rxlife.RxLife;
import com.rxjava.rxlife.ScopeViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CommentViewModel extends ScopeViewModel {
    public MutableLiveData<Integer> addResponse;
    public CommentViewModel(@NonNull Application application) {
        super(application);
        addResponse=  new MutableLiveData<Integer>() ;
    }

    public void addShopComment(ShopComment shopComment){
        RetrofitServiceManager.getInstance(getApplication()).create(CommentService.class)
                .addShopComment(shopComment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<String>>as(this))
                .subscribe(new Consumer<BaseJson<String>>() {
                               @Override
                               public void accept(BaseJson<String> results) throws Exception {
                                   addResponse.setValue(results.getCode());
                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }
    public void addProductComment(ProductComment productComment){
        RetrofitServiceManager.getInstance(getApplication()).create(CommentService.class)
                .addProductComment(productComment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<String>>as(this))
                .subscribe(new Consumer<BaseJson<String>>() {
                               @Override
                               public void accept(BaseJson<String> results) throws Exception {
                                   addResponse.setValue(results.getCode());
                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }
    public MutableLiveData<Integer> getaddResponse(){
        return addResponse;
    }
}
