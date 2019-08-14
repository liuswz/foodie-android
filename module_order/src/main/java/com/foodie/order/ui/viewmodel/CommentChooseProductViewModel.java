package com.foodie.order.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.foodie.base.base.BaseJson;
import com.foodie.base.config.RetrofitServiceManager;
import com.foodie.base.entity.Order;
import com.foodie.base.entity.SimpleOrderItem;
import com.foodie.base.enums.ResultCode;
import com.foodie.order.service.networkService.OrderService;
import com.foodie.order.service.networkService.ShopService;
import com.rxjava.rxlife.RxLife;
import com.rxjava.rxlife.ScopeViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CommentChooseProductViewModel extends ScopeViewModel {
    public MutableLiveData<List<SimpleOrderItem>> orderItemList;

    public CommentChooseProductViewModel(@NonNull Application application) {
        super(application);
        orderItemList = new MutableLiveData<>();

    }

    public void loadOrderItemList(Integer id){
        RetrofitServiceManager.getInstance(getApplication()).create(OrderService.class)
                .getOrderItems(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<List<SimpleOrderItem>>>as(this))
                .subscribe(new Consumer<BaseJson<List<SimpleOrderItem>>>() {
                               @Override
                               public void accept(BaseJson<List<SimpleOrderItem>> results) throws Exception {
                                   if(results.getCode()== ResultCode.SUCCESS.getIndex()){
                                       orderItemList.setValue(results.getResult());
                                   }
                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }


    public MutableLiveData<List<SimpleOrderItem>> getOrderItemList(){
        return orderItemList;
    }


}
