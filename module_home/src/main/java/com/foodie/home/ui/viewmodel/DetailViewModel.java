package com.foodie.home.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.foodie.base.base.BaseJson;
import com.foodie.base.entity.EasyShopDto;
import com.foodie.base.entity.Dish;
import com.foodie.base.entity.PageResults;
import com.foodie.base.entity.ShopComment;
import com.foodie.base.enums.ResultCode;
import com.foodie.base.config.RetrofitServiceManager;
import com.foodie.base.entity.Dish;
import com.foodie.base.entity.DishType;
import com.foodie.base.entity.EasyShopDetail;
import com.foodie.base.entity.EntireShopDetail;
import com.foodie.base.entity.ShopCollect;
import com.foodie.base.entity.Voucher;
import com.foodie.base.entity.VoucherForUser;
import com.foodie.home.service.networkService.CollectionService;
import com.foodie.home.service.networkService.CommentService;
import com.foodie.home.service.networkService.DishService;
import com.foodie.home.service.networkService.ShopService;
import com.foodie.home.service.networkService.VoucherService;
import com.rxjava.rxlife.RxLife;
import com.rxjava.rxlife.ScopeViewModel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DetailViewModel extends ScopeViewModel {


    public MutableLiveData<EntireShopDetail> entireShopDetail;
    public MutableLiveData<EasyShopDetail> easyShopDetail;
    public MutableLiveData<EasyShopDto> easyShop;
    public MutableLiveData<Voucher> voucher;
    public MutableLiveData<Integer> voucherStatus;
    public MutableLiveData<Integer> collectionStatus;
    public MutableLiveData<List<DishType>> dishTypeList;
    public MutableLiveData<List<Dish>> hotDishList;
    public MutableLiveData<List<Dish>> dishList;
    //public MutableLiveData<Double> sumPrice;
    public Double sumPrice=0D;
    public String ifGetVoucher="未领取";
    public MutableLiveData<Map<Integer,Dish>> hadAdddishMap;
    public List<String> idList;
    public void clearShopCar(){
        idList.clear();
        Map<Integer,Dish> map =  hadAdddishMap.getValue();
        map.clear();
        hadAdddishMap.setValue(map);
    //    sumPrice.setValue(0D);
        sumPrice=0D;
        for(Dish dish:dishList.getValue()){
            dish.getNum().setValue(0);
        }
        for(Dish dish:hotDishList.getValue()){
            dish.getNum().setValue(0);
        }
    }
    public void addDishMap(Dish dish){

        Map<Integer,Dish> map = hadAdddishMap.getValue();
        if(map==null){
            map = new HashMap<>();
        }
        if(!map.containsKey(dish.getId())){
            map.put(dish.getId(),dish);
            idList.add(dish.getId()+"");
        }
        sumPrice+=dish.getPrice();
        hadAdddishMap.setValue(map);
    }
    public void minusDishMap(Dish dish){
        Map<Integer,Dish> map = hadAdddishMap.getValue();
        if(map.containsKey(dish.getId())){
            Dish hadAddDish =  map.get(dish.getId());
            if(hadAddDish.getNum().getValue()==0){
                map.remove(dish.getId());
                idList.remove(dish.getId()+"");
            }
        }
        if(sumPrice!=0){
            sumPrice-=dish.getPrice();
        }

        hadAdddishMap.setValue(map);
    }
    public void addShopCarDishMap(Integer dishId){

        Map<Integer,Dish> map = hadAdddishMap.getValue();
        Dish hadAddDish = map.get(dishId);

        hadAddDish.getNum().setValue( hadAddDish.getNum().getValue()+1);

        sumPrice+=hadAddDish.getPrice();
        hadAdddishMap.setValue(map);
    }
    public void minusShopCarDishMap(Integer dishId){

        Map<Integer,Dish> map = hadAdddishMap.getValue();
        Dish hadAddDish = map.get(dishId);

        if(hadAddDish.getNum().getValue()==1){
            map.remove(dishId);
            idList.remove(dishId+"");
        }else{
            hadAddDish.getNum().setValue( hadAddDish.getNum().getValue()-1);
        }
        sumPrice-=hadAddDish.getPrice();
        hadAdddishMap.setValue(map);

    }
    private Integer shopId;
    private Integer userId=1;
    public void setShopId(Integer shopId){
        this.shopId = shopId;
    }
    public Integer getShopId(){
        return  shopId;
    }

//    public void addSumPrice(Double price){
//      //  sumPrice.setValue(sumPrice.getValue()+price);
//        sumPrice+=price;
//    }
//    public void minusSumPrice(Double price){
//     //   sumPrice.setValue(sumPrice.getValue()-price);
//        sumPrice-=price;
//    }
    public Double getSumPrice(){
        return sumPrice;
    }
    public void setSumPrice(Double sumPrice){
        this.sumPrice = sumPrice;
    }
    public void setIdList(List<String> idList){
        this.idList = idList;
    }
    public void setHadAdddishMap(Map<Integer,Dish> dishMap){
        hadAdddishMap.setValue(dishMap);;
    }
    public List<String> getIdList(){
        return idList;
    }
    public void setUserId(Integer userId){
        this.userId = userId;
    }
    public Integer getUserId(){
        return userId;
    }
    public String getIfGetVoucher(){
        return ifGetVoucher;
    }
    public DetailViewModel(@NonNull Application application) {
        super(application);
        entireShopDetail=new MutableLiveData<EntireShopDetail>();
        easyShopDetail=new MutableLiveData<EasyShopDetail>();
        easyShop=new MutableLiveData<EasyShopDto>();
        voucher=new MutableLiveData<Voucher>();
        voucherStatus=new MutableLiveData<>();
        collectionStatus=new MutableLiveData<>();
        dishTypeList=new MutableLiveData<>();
        hotDishList=new MutableLiveData<List<Dish>>();
        dishList=new MutableLiveData<>();
        hadAdddishMap =new MutableLiveData<>();
        idList = new LinkedList<String>();
        commentList = new MutableLiveData<>();
//        sumPrice = new MutableLiveData<Double>();
//        sumPrice.setValue(0D);
    }
    public void loadEntireShopDetail(){
        RetrofitServiceManager.getInstance(getApplication()).create(ShopService.class)
                .getEntireShopDetail(shopId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<EntireShopDetail>>as(this))
                .subscribe(new Consumer<BaseJson<EntireShopDetail>>() {
                               @Override
                               public void accept(BaseJson<EntireShopDetail> baseJson) throws Exception {
                                   if(baseJson.getCode()== ResultCode.SUCCESS.getIndex()){
                                       entireShopDetail.setValue(baseJson.getResult());
                                   }

                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }
    public void loadEasyShopDetail(){
        RetrofitServiceManager.getInstance(getApplication()).create(ShopService.class)
                .getEasyShopDetail(shopId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<EasyShopDetail>>as(this))
                .subscribe(new Consumer<BaseJson<EasyShopDetail>>() {
                               @Override
                               public void accept(BaseJson<EasyShopDetail> baseJson) throws Exception {
                                   if(baseJson.getCode()== ResultCode.SUCCESS.getIndex()){
                                       easyShopDetail.setValue(baseJson.getResult());
                                   }

                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }

    public void loadEasyShop(){
        RetrofitServiceManager.getInstance(getApplication()).create(ShopService.class)
                .getEasyShop(shopId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<EasyShopDto>>as(this))
                .subscribe(new Consumer<BaseJson<EasyShopDto>>() {
                               @Override
                               public void accept(BaseJson<EasyShopDto> baseJson) throws Exception {
                                   if(baseJson.getCode()== ResultCode.SUCCESS.getIndex()){
                                       easyShop.setValue(baseJson.getResult());
                                   }

                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }
    public void loadVoucher(){
        RetrofitServiceManager.getInstance(getApplication()).create(VoucherService.class)
                .getVoucherById(shopId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<Voucher>>as(this))
                .subscribe(new Consumer<BaseJson<Voucher>>() {
                               @Override
                               public void accept(BaseJson<Voucher> baseJson) throws Exception {
                                   if(baseJson.getCode()== ResultCode.SUCCESS.getIndex()){
                                       voucher.setValue(baseJson.getResult());
                                   }
                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }
    public void addVoucher(Voucher voucher){
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
    public void checkVoucher(Integer voucherId){
        RetrofitServiceManager.getInstance(getApplication()).create(VoucherService.class)
                .checkVoucherForUser( shopId, userId,voucherId)
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
    public void loadDishTypeList(){
        RetrofitServiceManager.getInstance(getApplication()).create(DishService.class)
                .getAllDishType(shopId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<List<DishType>>>as(this))
                .subscribe(new Consumer<BaseJson<List<DishType>>>() {
                               @Override
                               public void accept(BaseJson<List<DishType>> baseJson) throws Exception {
                                   if(baseJson.getCode()== ResultCode.SUCCESS.getIndex()){
                                       dishTypeList.setValue(baseJson.getResult());
                                     
                                   }
                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());
                               }
                           }
                );
    }
    public void loadHotDishList(){

        RetrofitServiceManager.getInstance(getApplication()).create(DishService.class)
                .getHotDish(shopId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<List<Dish>>>as(this))
                .subscribe(new Consumer<BaseJson<List<Dish>>>() {
                               @Override
                               public void accept(BaseJson<List<Dish>> baseJson) throws Exception {
                                   if(baseJson.getCode()== ResultCode.SUCCESS.getIndex()){
                                       hotDishList.setValue(setGoodCarItem(baseJson.getResult()));

                                   }
                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>aaa", "" + throwable.getMessage());

                               }
                           }
                );
    }
    public void loadDishList(Integer typeId){

        RetrofitServiceManager.getInstance(getApplication()).create(DishService.class)
                .getDishByTypeId(shopId,typeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<List<Dish>>>as(this))
                .subscribe(new Consumer<BaseJson<List<Dish>>>() {
                               @Override
                               public void accept(BaseJson<List<Dish>> baseJson) throws Exception {
                                   if(baseJson.getCode()== ResultCode.SUCCESS.getIndex()){
                                       dishList.setValue(setGoodCarItem(baseJson.getResult()));
                                   }
                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());
                               }
                           }
                );
    }
    public void checkShopCollect(){
        RetrofitServiceManager.getInstance(getApplication()).create(CollectionService.class)
                .checkShopCollect(userId,shopId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<Integer>>as(this))
                .subscribe(new Consumer<BaseJson<Integer>>() {
                               @Override
                               public void accept(BaseJson<Integer> baseJson) throws Exception {
                                   if(baseJson.getCode()== ResultCode.SUCCESS.getIndex()){
                                       collectionStatus.setValue(baseJson.getResult());
                                   }

                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }
    public void addShopCollect(){
        ShopCollect shopCollect = new ShopCollect();
        shopCollect.setShopId(shopId);
        shopCollect.setUserId(userId);
        RetrofitServiceManager.getInstance(getApplication()).create(CollectionService.class)
                .addShopCollect(shopCollect)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<Integer>>as(this))
                .subscribe(new Consumer<BaseJson<Integer>>() {
                               @Override
                               public void accept(BaseJson<Integer> baseJson) throws Exception {
                                   if(baseJson.getCode()== ResultCode.SUCCESS.getIndex()){
                                       collectionStatus.setValue(baseJson.getResult());
                                   }

                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }


    private List<Dish> setGoodCarItem (List<Dish> list){
        Map<Integer,Dish> dishMap = hadAdddishMap.getValue();
        if(dishMap==null||list==null||dishMap.size()==0||list.size()==0){
            return list;
        }

        for(Dish dish:list){
            if(dishMap.containsKey(dish.getId())){
                dish.setNum(dishMap.get(dish.getId()).getNum());

            }
        }
        return list;
    }
    public MutableLiveData<EntireShopDetail> getEntireShopDetail(){
        return  entireShopDetail;
    }

    public MutableLiveData<EasyShopDetail> getEasyShopDetail(){
        return  easyShopDetail;
    }
    public MutableLiveData<EasyShopDto> getEasyShop(){
        return  easyShop;
    }
    public MutableLiveData<List<DishType>> getDishTypeList(){
        return  dishTypeList;
    }
    public MutableLiveData<List<Dish>> getHotDishList(){
        return  hotDishList;
    }
    public MutableLiveData<List<Dish>> getDishList(){
        return  dishList;
    }

    public MutableLiveData<Voucher> getVoucher(){
        return  voucher;
    }
    public MutableLiveData<Integer> getVoucherStatus(){
        return  voucherStatus;
    }
    public MutableLiveData<Integer> getCollectionStatus(){
        return  collectionStatus;
    }

    public MutableLiveData<Map<Integer,Dish>> getDishMap(){
        return  hadAdddishMap;
    }



    //评价
    public MutableLiveData<List<ShopComment>> commentList;
    private Integer size=10;
    private int total;


    public int getTotal(){
        return total;
    }
    public void loadCommentList(int page){

        RetrofitServiceManager.getInstance(getApplication()).create(CommentService.class)
                .getAllShopComment(page,size,shopId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<PageResults<ShopComment>>as(this))
                .subscribe(new Consumer<PageResults<ShopComment>>() {
                               @Override
                               public void accept(PageResults<ShopComment> results) throws Exception {
                                   if(results.getCode()== ResultCode.SUCCESS.getIndex()){
                                       commentList.setValue(results.getRows());
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

    public MutableLiveData<List<ShopComment>> getCommentList(){
        if(commentList==null) return new MutableLiveData<List<ShopComment>>();
        return  commentList;
    }

}
