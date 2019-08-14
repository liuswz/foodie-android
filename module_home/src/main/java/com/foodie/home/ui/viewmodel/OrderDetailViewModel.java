package com.foodie.home.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.foodie.base.base.BaseJson;
import com.foodie.base.config.RetrofitServiceManager;
import com.foodie.base.dto.OrderAddParam;
import com.foodie.base.dto.VoucherForUserDto;
import com.foodie.base.entity.Dish;
import com.foodie.base.entity.DishBase;
import com.foodie.base.entity.OrderItem;
import com.foodie.base.entity.Voucher;
import com.foodie.base.entity.VoucherForUser;
import com.foodie.base.enums.PayStatus;
import com.foodie.base.enums.ResultCode;
import com.foodie.base.enums.UserType;
import com.foodie.home.entity.DishForAppointOrder;
import com.foodie.home.entity.DishForGoShopOrder;
import com.foodie.home.service.networkService.OrderService;
import com.foodie.home.service.networkService.VoucherService;
import com.google.gson.Gson;
import com.rxjava.rxlife.RxLife;
import com.rxjava.rxlife.ScopeViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class OrderDetailViewModel extends ScopeViewModel {
//    private String date;
//    private String time;
    private String shopName;
    private String photoUrl;
    private Map<Integer, DishBase> dishMap;
    private Integer shopId;
    private Integer userId;
    private Gson gson;
    public MutableLiveData<List<VoucherForUserDto>> voucherList;
    public MutableLiveData<Integer> requestStatus;
    private String orderItems = "";

//    public void setDate(String date){
//        this.date=date;
//    }
//    public void setTime(String time){
//        this.time=time;
//    }
    public void setShopId(Integer shopId){
    this.shopId=shopId;
}
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public void setShopName(String shopName){
        this.shopName=shopName;
    }
    public void setPhotoUrl(String photoUrl){
        this.photoUrl=photoUrl;
    }
    public void setDishMap(Map<Integer, DishBase> dishMap){
        this.dishMap=dishMap;
    }
    public OrderDetailViewModel(@NonNull Application application) {
        super(application);
        gson = new Gson();
        voucherList = new MutableLiveData<>();
        requestStatus =new MutableLiveData<Integer>();
    }
    public DishForGoShopOrder setDishForGoShopOrder(DishForGoShopOrder order){
        Integer sumNum=0;
        String shopNames="";
        int index = 0;
        List<OrderItem> orderItemList = new ArrayList<>();
        for(DishBase dish : dishMap.values()){
            sumNum+=dish.getNumber();
            if(index<3){
                shopNames=shopNames+dish.getName()+",";
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setGoodId(dish.getId());
            orderItem.setPhotoUrl(dish.getPhotoUrl());
            orderItem.setGoodName(dish.getName());
            orderItem.setGoodNum(dish.getNumber());
            orderItem.setTotalCost(dish.getNumber()*dish.getPrice());
            orderItemList.add(orderItem);
            index++;
        }
        order.setNum(sumNum);
        order.setShowNames(shopNames);
        order.setShopName(shopName);
        order.setUserId(userId);
        order.setGoodId(shopId);
        order.setUserType(UserType.CommonUser.getIndex());
        order.setShopPhotoUrl(photoUrl);
        orderItems = gson.toJson(orderItemList);

        return order;
    }
    public void addDishForGoShopOrder(DishForGoShopOrder order,Integer voucherId){
        DishForGoShopOrder order1 = setDishForGoShopOrder(order);
//        OrderAddParam<DishForGoShopOrder> orderAddParam = new OrderAddParam<>();
//        orderAddParam.setOrder(order1);
//        orderAddParam.setOrderItemList(orderItems);
//        orderAddParam.setVoucherId(voucherId);
        RetrofitServiceManager.getInstance(getApplication()).create(OrderService.class)
                .addDishForGoShopOrder(order1,orderItems,voucherId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<String>>as(this))
                .subscribe(new Consumer<BaseJson<String>>() {
                               @Override
                               public void accept(BaseJson<String> baseJson) throws Exception {
                                   requestStatus.setValue(baseJson.getCode());
//                                   if(baseJson.getCode()== ResultCode.SUCCESS.getIndex()){
//                                       requestStatus.setValue(baseJson.getCode());
//                                   }
                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }

    public DishForAppointOrder setDishForAppointOrder(DishForAppointOrder order){
        Integer sumNum=0;
        String shopNames="";
        int index = 0;
        List<OrderItem> orderItemList = new ArrayList<>();
        for(DishBase dish : dishMap.values()){
            sumNum+=dish.getNumber();
            if(index<3){
                shopNames=shopNames+dish.getName()+",";
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setGoodId(dish.getId());
            orderItem.setPhotoUrl(dish.getPhotoUrl());
            orderItem.setGoodName(dish.getName());
            orderItem.setGoodNum(dish.getNumber());
            orderItem.setTotalCost(dish.getNumber()*dish.getPrice());
            orderItemList.add(orderItem);
            index++;
        }
        order.setNum(sumNum);
        order.setShowNames(shopNames);
        order.setShopName(shopName);
        order.setUserId(userId);
        order.setGoodId(shopId);
        order.setUserType(UserType.CommonUser.getIndex());
        order.setShopPhotoUrl(photoUrl);
        orderItems = gson.toJson(orderItemList);

        return order;
    }
    public void addDishForAppointOrder(DishForAppointOrder order,Integer voucherId){
//OrderAddParam<DishForAppointOrder> orderAddParam = new OrderAddParam<>();
//orderAddParam.setOrder(setDishForAppointOrder(order))
        DishForAppointOrder order1 = setDishForAppointOrder(order);
        RetrofitServiceManager.getInstance(getApplication()).create(OrderService.class)
                .addDishForAppointOrder(order1,orderItems,voucherId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<String>>as(this))
                .subscribe(new Consumer<BaseJson<String>>() {
                               @Override
                               public void accept(BaseJson<String> baseJson) throws Exception {
                                   requestStatus.setValue(baseJson.getCode());
//                                   if(baseJson.getCode()== ResultCode.SUCCESS.getIndex()){
//                                       requestStatus.setValue(baseJson.getCode());
//                                   }
                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }

    public void loadVoucher(Integer shopId,Integer userId){
        RetrofitServiceManager.getInstance(getApplication()).create(VoucherService.class)
                .getVoucherForUserById(shopId,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<List<VoucherForUserDto>>>as(this))
                .subscribe(new Consumer<BaseJson<List<VoucherForUserDto>>>() {
                               @Override
                               public void accept(BaseJson<List<VoucherForUserDto>> baseJson) throws Exception {

                                   if(baseJson.getCode()== ResultCode.SUCCESS.getIndex()){
                                       voucherList.setValue(baseJson.getResult());
                                   }
                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }
    public String[] moneyOffMinusMoney(Double sumPrice,String fullNum,String minusNum){
        String fullNums[] = fullNum.split(",");
        String minusNums[] =minusNum.split(",");
        Integer index = 0;
        Double minDiffMoney = sumPrice;
        for(int i=0;i<fullNums.length;i++){
            Double m = Double.parseDouble(fullNums[i]);
            Double diffMoney =sumPrice-m;
            if(diffMoney<minDiffMoney&&diffMoney>=0){
                minDiffMoney =diffMoney;
                index = i;
            }
        }
        if(minDiffMoney ==sumPrice){
            return null;
        }
        String result[] = new String[2];
        result[0] = fullNums[index];
        result[1] = minusNums[index];
        return result;
    }
    public MutableLiveData<List<VoucherForUserDto>> getVoucher(){
        return  voucherList;
    }
    public MutableLiveData<Integer> getRequestStatus(){
        return  requestStatus;
    }

}
