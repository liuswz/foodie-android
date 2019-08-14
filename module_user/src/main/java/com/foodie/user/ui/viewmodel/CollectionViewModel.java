package com.foodie.user.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.foodie.base.base.BaseJson;
import com.foodie.base.config.RetrofitServiceManager;
import com.foodie.base.dto.VoucherDto;
import com.foodie.base.entity.EntireShopDetail;
import com.foodie.base.entity.PageResults;
import com.foodie.base.entity.ShopDetail;
import com.foodie.base.entity.Voucher;
import com.foodie.base.entity.VoucherForUser;
import com.foodie.base.enums.ResultCode;
import com.foodie.user.service.networkService.CollectionService;
import com.foodie.user.service.networkService.VoucherService;
import com.rxjava.rxlife.RxLife;
import com.rxjava.rxlife.ScopeViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CollectionViewModel extends ScopeViewModel {
    private Integer size=10;
    private int total;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private String city;
    private Integer userId=1;

    public void setCity(String city){
        this.city = city;
    }
    public void setUserId(Integer shopId){
        this.userId = userId;
    }
    public int getTotal(){
        return total;
    }
    public MutableLiveData<List<ShopDetail>> shopList;
    public MutableLiveData<List<VoucherDto>> voucherList;
    public MutableLiveData<Integer> collectionStatus;
    public MutableLiveData<Integer> voucherStatus;
    public String ifGetVoucher="未领取";
    public void setLocation(Double longitude,Double latitude){
        this.longitude = longitude;
        this.latitude =latitude;
    }
    public CollectionViewModel(@NonNull Application application) {
        super(application);
        shopList = new MutableLiveData<>();
        collectionStatus = new MutableLiveData<>();
        voucherList = new MutableLiveData<>();
        voucherStatus = new MutableLiveData<>();
    }

    public void loadShopCollect(Integer page){
        RetrofitServiceManager.getInstance(getApplication()).create(CollectionService.class)
                .getShopCollect(page,size,userId,latitude,longitude)
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
    public void deleteShopCollect(Integer shopId){
        RetrofitServiceManager.getInstance(getApplication()).create(CollectionService.class)
                .deleteShopCollect(userId,shopId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<String>>as(this))
                .subscribe(new Consumer<BaseJson<String>>() {
                               @Override
                               public void accept(BaseJson<String> baseJson) throws Exception {
                                   collectionStatus.setValue(baseJson.getCode());
//                                   if(baseJson.getCode()== ResultCode.SUCCESS.getIndex()){
//
//                                   }

                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }
    public void loadVoucher(Integer page){
        RetrofitServiceManager.getInstance(getApplication()).create(VoucherService.class)
                .getVoucherByCity(page,size,city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<PageResults<VoucherDto>>as(this))
                .subscribe(new Consumer<PageResults<VoucherDto>>() {
                               @Override
                               public void accept(PageResults<VoucherDto> results) throws Exception {
                                   if(results.getCode()== ResultCode.SUCCESS.getIndex()){
                                       voucherList.setValue(results.getRows());
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
    public void loadMyVoucher(Integer page){
        RetrofitServiceManager.getInstance(getApplication()).create(VoucherService.class)
                .getVoucherForUserByUserId(page,size,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<PageResults<VoucherDto>>as(this))
                .subscribe(new Consumer<PageResults<VoucherDto>>() {
                               @Override
                               public void accept(PageResults<VoucherDto> results) throws Exception {
                                   if(results.getCode()== ResultCode.SUCCESS.getIndex()){
                                       voucherList.setValue(results.getRows());
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
    public void addVoucher(VoucherDto voucher){
        VoucherForUser voucherForUser = new VoucherForUser();
        voucherForUser.setShopId(voucher.getShopId());
        voucherForUser.setMoney(voucher.getMoney());
        voucherForUser.setStartDate(voucher.getStartDate());
        voucherForUser.setDeadLine(voucher.getDeadLine());
        voucherForUser.setVoucherId(voucher.getId());
        voucherForUser.setUserId(userId);
        RetrofitServiceManager.getInstance(getApplication()).create(VoucherService.class)
                .addShopVoucherForUser(voucherForUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<String>>as(this))
                .subscribe(new Consumer<BaseJson<String>>() {
                               @Override
                               public void accept(BaseJson<String> baseJson) throws Exception {
                                   ifGetVoucher = baseJson.getResult();
                                   voucherStatus.setValue(baseJson.getCode());

                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }
    public MutableLiveData<List<ShopDetail>> getShopList(){
        return shopList;
    }
    public MutableLiveData<Integer> getCollectionStatus(){
        return collectionStatus;
    }
    public MutableLiveData<List<VoucherDto>> getVoucher(){
        return voucherList;
    }
    public MutableLiveData<Integer> getVoucherStatus(){
        return  voucherStatus;
    }
}
