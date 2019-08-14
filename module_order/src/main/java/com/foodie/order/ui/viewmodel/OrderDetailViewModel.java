package com.foodie.order.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.foodie.base.base.BaseJson;
import com.foodie.base.config.RetrofitServiceManager;
import com.foodie.base.entity.Order;
import com.foodie.base.entity.PageResults;
import com.foodie.base.entity.SimpleOrder;
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

public class OrderDetailViewModel extends ScopeViewModel {
    public MutableLiveData<List<SimpleOrderItem>> orderItemList;
    public MutableLiveData<Order> order;
    public MutableLiveData<String> phoneNum;
    public MutableLiveData<Integer> payResponse;
    public OrderDetailViewModel(@NonNull Application application) {
        super(application);
        orderItemList = new MutableLiveData<>();
        order = new MutableLiveData<>();
        phoneNum = new  MutableLiveData<>();
        payResponse=  new MutableLiveData<Integer>() ;
    }

    public void loadOrder (Integer id){
        RetrofitServiceManager.getInstance(getApplication()).create(OrderService.class)
                .getOrderDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<Order>>as(this))
                .subscribe(new Consumer<BaseJson<Order>>() {
                               @Override
                               public void accept(BaseJson<Order> results) throws Exception {
                                   if(results.getCode()== ResultCode.SUCCESS.getIndex()){
                                       order.setValue(results.getResult());
                                   }
                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
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

    public void loadPhoneNum(Integer shopId){
        RetrofitServiceManager.getInstance(getApplication()).create(ShopService.class)
                .getShopPhoneById(shopId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<String>>as(this))
                .subscribe(new Consumer<BaseJson<String>>() {
                               @Override
                               public void accept(BaseJson<String> results) throws Exception {
                                   if(results.getCode()== ResultCode.SUCCESS.getIndex()){
                                       phoneNum.setValue(results.getResult());
                                   }
                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }
    public void updatePayStatus(Integer id,Integer payWay){
        RetrofitServiceManager.getInstance(getApplication()).create(OrderService.class)
                .updatePayStatus(id,payWay)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<String>>as(this))
                .subscribe(new Consumer<BaseJson<String>>() {
                               @Override
                               public void accept(BaseJson<String> results) throws Exception {
                                   payResponse.setValue(results.getCode());
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
    public MutableLiveData<Order> getOrder(){
        return order;
    }
    public MutableLiveData<String> getPhoneNum(){
        return phoneNum;
    }
    public MutableLiveData<Integer> getPayResponse(){
        return payResponse;
    }

}
