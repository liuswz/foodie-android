package com.foodie.home.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.foodie.base.enums.ResultCode;
import com.foodie.base.entity.PageResults;
import com.foodie.base.entity.ShopDetail;
import com.foodie.base.config.RetrofitServiceManager;
import com.foodie.home.service.networkService.ShopService;
import com.rxjava.rxlife.RxLife;
import com.rxjava.rxlife.ScopeViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TypeViewModel extends ScopeViewModel {

    public MutableLiveData<List<ShopDetail>> shopList;
    private Integer size=10;
    private int total;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private String city;
    private String type;

    public TypeViewModel(@NonNull Application application) {
        super(application);
        shopList = new MutableLiveData<>();
    }
    public int getTotal(){
        return total;
    }
    public void setLocation(Double longitude,Double latitude){
        this.longitude = longitude;
        this.latitude =latitude;
    }
    public void setCity(String city){
        this.city = city;
    }
    public void setType(String type){
        this.type = type;
    }

    public void loadShopList(int page){

        RetrofitServiceManager.getInstance(getApplication()).create(ShopService.class)
                .findAllShopDetailsByTypeId(longitude,latitude,type,city,page,size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<PageResults<ShopDetail>>as(this))
                .subscribe(new Consumer<PageResults<ShopDetail>>() {
                               @Override
                               public void accept(PageResults<ShopDetail> results) throws Exception {
                                   if(results.getCode()== ResultCode.SUCCESS.getIndex()){
                                       shopList.setValue(results.getRows());
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
    public void loadShopListByMark(int page){
        RetrofitServiceManager.getInstance(getApplication()).create(ShopService.class)
                .findAllShopDetailsByMarkAndTypeId(longitude,latitude,type,city,page,size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<PageResults<ShopDetail>>as(this))
                .subscribe(new Consumer<PageResults<ShopDetail>>() {
                               @Override
                               public void accept(PageResults<ShopDetail> results) throws Exception {
                                   if(results.getCode()== ResultCode.SUCCESS.getIndex()){
                                       shopList.setValue(results.getRows());
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
    public MutableLiveData<List<ShopDetail>> getShopList(){
        if(shopList==null) return new MutableLiveData<List<ShopDetail>>();
        return  shopList;
    }
}
