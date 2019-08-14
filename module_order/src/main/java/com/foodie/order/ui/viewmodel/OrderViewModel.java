package com.foodie.order.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.foodie.base.base.BaseJson;
import com.foodie.base.config.RetrofitServiceManager;
import com.foodie.base.entity.PageResults;
import com.foodie.base.entity.Product;
import com.foodie.base.entity.ShopDetail;
import com.foodie.base.entity.SimpleOrder;
import com.foodie.base.enums.ResultCode;
import com.foodie.order.service.networkService.OrderService;
import com.rxjava.rxlife.RxLife;
import com.rxjava.rxlife.ScopeViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by goldze on 2018/6/21.
 */

public class OrderViewModel extends ScopeViewModel {

    public MutableLiveData<List<SimpleOrder>> orderList;
    public MutableLiveData<Integer> payResponse;
    public MutableLiveData<Integer> finishResponse;
    private Integer userId;
    private Integer size=6;
    private int total;
    private Integer flag=0;
    public int getTotal(){
        return total;
    }
    public void setUserId(Integer userId){
        this.userId =userId;
    }
    public void setFlag(Integer flag){
        this.flag =flag;
    }
    public OrderViewModel(@NonNull Application application) {
        super(application);
        orderList = new MutableLiveData<List<SimpleOrder>>();
        payResponse=  new MutableLiveData<Integer>() ;
        finishResponse = new MutableLiveData<Integer> ();
    }

    public void loadAllSimpleOrder(int page){
        RetrofitServiceManager.getInstance(getApplication()).create(OrderService.class)
                .findAllSimpleOrder(page,size,userId,flag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<PageResults<SimpleOrder>>as(this))
                .subscribe(new Consumer<PageResults<SimpleOrder>>() {
                               @Override
                               public void accept(PageResults<SimpleOrder> results) throws Exception {
                                   if(results.getCode()== ResultCode.SUCCESS.getIndex()){
                                       orderList.setValue(results.getRows());
                                       Log.e("Owen",results.getRows().get(0).getOrderNo()+"----------");
                                       total= (int) (results.getTotal()%size==0?results.getTotal()/size:results.getTotal()/size+1);
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
    public void updateFinishStatus(Integer id){
        RetrofitServiceManager.getInstance(getApplication()).create(OrderService.class)
                .updateFinishStatus(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<String>>as(this))
                .subscribe(new Consumer<BaseJson<String>>() {
                               @Override
                               public void accept(BaseJson<String> results) throws Exception {
                                   finishResponse.setValue(results.getCode());
                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }
    public MutableLiveData<Integer> getPayResponse(){
        return payResponse;
    }
    public MutableLiveData<Integer> getFinishResponse(){
        return finishResponse;
    }
    public MutableLiveData<List<SimpleOrder>> getOrderList(){
        return orderList;
    }
}
