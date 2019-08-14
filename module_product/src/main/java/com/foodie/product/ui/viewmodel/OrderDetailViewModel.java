package com.foodie.product.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.foodie.base.base.BaseJson;
import com.foodie.base.config.RetrofitServiceManager;

import com.foodie.base.entity.ProductBase;
import com.foodie.base.entity.OrderItem;
import com.foodie.base.enums.ResultCode;
import com.foodie.base.enums.UserType;

import com.foodie.product.entity.ProductForDeliveryOrder;
import com.foodie.product.entity.ProductForGoShopOrder;
import com.foodie.base.dto.SearchFindShopDto;
import com.foodie.product.service.networkService.OrderService;
import com.foodie.product.service.networkService.ShopService;
import com.foodie.product.util.GetJsonDataUtil;
import com.foodie.product.util.JsonBean;
import com.google.gson.Gson;
import com.rxjava.rxlife.RxLife;
import com.rxjava.rxlife.ScopeViewModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OrderDetailViewModel extends ScopeViewModel {
//    private String date;
//    private String time;
    private String shopName;
    private String photoUrl;
    private Map<Integer, ProductBase> productMap;
    private Integer userId;
    private Gson gson;
    public MutableLiveData<Integer> requestStatus;
    public MutableLiveData<List<SearchFindShopDto>> shopList;
    private String orderItems = "";


    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public void setShopName(String shopName){
        this.shopName=shopName;
    }
    public void setPhotoUrl(String photoUrl){
        this.photoUrl=photoUrl;
    }
    public void setProductMap(Map<Integer, ProductBase> productMap){
        this.productMap=productMap;
    }
    public OrderDetailViewModel(@NonNull Application application) {
        super(application);
        gson = new Gson();
        requestStatus =new MutableLiveData<Integer>();
        shopList = new MutableLiveData<>();
    }
    public ProductForDeliveryOrder setProductForDeliveryOrder(ProductForDeliveryOrder order){
        Integer sumNum=0;
        String shopNames="";
        int index = 0;
        List<OrderItem> orderItemList = new ArrayList<>();
        for(ProductBase dish : productMap.values()){
            sumNum+=dish.getNumber();
            if(index<3){
                shopNames=shopNames+dish.getProductName()+",";
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setGoodId(dish.getId());
            orderItem.setPhotoUrl(dish.getPhotoUrl());
            orderItem.setGoodName(dish.getProductName());
            orderItem.setGoodNum(dish.getNumber());
            orderItem.setTotalCost(dish.getNumber()*dish.getPriceForUser());
            orderItemList.add(orderItem);
            index++;
        }
        order.setNum(sumNum);
        order.setShowNames(shopNames);
        order.setShopName(shopName);
        order.setUserId(userId);
        order.setUserType(UserType.CommonUser.getIndex());
        order.setShopPhotoUrl(photoUrl);
        orderItems = gson.toJson(orderItemList);

        return order;
    }
    public void addProductForDeliveryOrder(ProductForDeliveryOrder order){
        ProductForDeliveryOrder order1 = setProductForDeliveryOrder(order);

        RetrofitServiceManager.getInstance(getApplication()).create(OrderService.class)
                .addProductForDeliveryOrder(order1,orderItems)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<String>>as(this))
                .subscribe(new Consumer<BaseJson<String>>() {
                               @Override
                               public void accept(BaseJson<String> baseJson) throws Exception {
                                   if(baseJson.getCode()== ResultCode.SUCCESS.getIndex()){
                                       requestStatus.setValue(baseJson.getCode());
                                   }
                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }

    public ProductForGoShopOrder setProductForGoShopOrder(ProductForGoShopOrder order){
        Integer sumNum=0;
        String shopNames="";
        int index = 0;
        List<OrderItem> orderItemList = new ArrayList<>();
        for(ProductBase dish : productMap.values()){
            sumNum+=dish.getNumber();
            if(index<3){
                shopNames=shopNames+dish.getProductName()+",";
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setGoodId(dish.getId());
            orderItem.setPhotoUrl(dish.getPhotoUrl());
            orderItem.setGoodName(dish.getProductName());
            orderItem.setGoodNum(dish.getNumber());
            orderItem.setTotalCost(dish.getNumber()*dish.getPriceForUser());
            orderItemList.add(orderItem);
            index++;
        }
        order.setNum(sumNum);
        order.setShowNames(shopNames);
        order.setShopName(shopName);
        order.setUserId(userId);
        order.setUserType(UserType.CommonUser.getIndex());
        order.setShopPhotoUrl(photoUrl);
        orderItems = gson.toJson(orderItemList);

        return order;
    }
    public void addProductForGoShopOrder(ProductForGoShopOrder order){
//OrderAddParam<DishForAppointOrder> orderAddParam = new OrderAddParam<>();
//orderAddParam.setOrder(setDishForAppointOrder(order))
        ProductForGoShopOrder order1 = setProductForGoShopOrder(order);
        RetrofitServiceManager.getInstance(getApplication()).create(OrderService.class)
                .addProductForGoShopOrder(order1,orderItems)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<String>>as(this))
                .subscribe(new Consumer<BaseJson<String>>() {
                               @Override
                               public void accept(BaseJson<String> baseJson) throws Exception {
                                   requestStatus.setValue(baseJson.getCode());

                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }

    public void getNearShop(String city,Double mylat,Double mylon){
        RetrofitServiceManager.getInstance(getApplication()).create(ShopService.class)
                .getNearShop(city,mylat,mylon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<List<SearchFindShopDto>>>as(this))
                .subscribe(new Consumer<BaseJson<List<SearchFindShopDto>>>() {
                               @Override
                               public void accept(BaseJson<List<SearchFindShopDto>> baseJson) throws Exception {

                                   if(baseJson.getCode()== ResultCode.SUCCESS.getIndex()){
                                       shopList.setValue(baseJson.getResult());
                                   }
                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }
    public Double moneyOffMinusMoney(){
        Double minusPrice=0D;
        for(ProductBase product : productMap.values()){
            String fullNum = product.getFullNum();
            String minusNum = product.getMinusNum();

            if(fullNum==null||minusNum==null||fullNum.equals("")||minusNum.equals("")){
                break;
            }else{

                String fullNums[] = fullNum.split(",");
                String minusNums[] =minusNum.split(",");
                Integer index = 0;
                Double sumPrice = product.getPriceForUser()*product.getNumber();

                Double minDiffMoney = sumPrice;
                for(int i=0;i<fullNums.length;i++){
                    Double m = Double.parseDouble(fullNums[i]);
                    Double diffMoney =sumPrice-m;
                    if(diffMoney<minDiffMoney&&diffMoney>=0){
                        minDiffMoney =diffMoney;
                        index = i;
                    }
                }
                if(minDiffMoney !=sumPrice){
                    minusPrice += Double.parseDouble(minusNums[index]);
                }



            }

        }
        return minusPrice;
    }
    public MutableLiveData<List<SearchFindShopDto>> getShopList(){
        return  shopList;
    }
    public MutableLiveData<Integer> getRequestStatus(){
        return  requestStatus;
    }





    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return detail;
    }

}
