package com.foodie.product.ui.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.foodie.base.base.BaseJson;
import com.foodie.base.entity.Product;
import com.foodie.base.enums.ResultCode;
import com.foodie.base.entity.PageResults;
import com.foodie.base.entity.Product;
import com.foodie.base.config.RetrofitServiceManager;
import com.foodie.product.database.ProductHistoryDatabase;
import com.foodie.product.entity.ProductSearchHistory;
import com.foodie.product.service.networkService.ProductService;
import com.rxjava.rxlife.RxLife;
import com.rxjava.rxlife.ScopeViewModel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PSearchViewModel extends ScopeViewModel {

    public MutableLiveData<String> searchValue;
    public MutableLiveData<List<ProductSearchHistory>> searchHistoryList;
    public MutableLiveData<List<String>> searchHotList;
    public MutableLiveData<List<String>> searchPromptList;
    public MutableLiveData<List<Product>> searchResultList;

    public Double sumPrice=0D;
    public MutableLiveData<Map<Integer, Product>> goodCarItemMap;
    public List<String> idList;
    private Integer size=6;

    private int total;
    public int getTotal(){
        return total;
    }

    public void clearGoodCar(){
        idList.clear();
        Map<Integer,Product> map =  goodCarItemMap.getValue();
        if(map!=null){
            map.clear();
            goodCarItemMap.setValue(map);
        }
        sumPrice=0D;
        for(Product product:searchResultList.getValue()){
            if(product.getNum()==null){
                break;
            }
            product.getNum().setValue(0);
        }

    }
    public void addProductMap(Product product){

        Map<Integer,Product> map = goodCarItemMap.getValue();
        if(map==null){
            map = new HashMap<>();
        }
        if(!map.containsKey(product.getId())){
            map.put(product.getId(),product);
            idList.add(product.getId()+"");

        }
        sumPrice+=product.getPriceForUser();
        goodCarItemMap.setValue(map);
    }
    public void minusProductMap(Product product){
        Map<Integer,Product> map = goodCarItemMap.getValue();
        if(map.containsKey(product.getId())){
            Product goodCarItem =  map.get(product.getId());
            if(goodCarItem.getNum().getValue()==1){
                map.remove(product.getId());
                idList.remove(product.getId()+"");
            }
        }
        if(sumPrice!=0){
            sumPrice-=product.getPriceForUser();
        }
        goodCarItemMap.setValue(map);
    }

    public void addGoodCarProductMap(Integer productId){

        Map<Integer,Product> map = goodCarItemMap.getValue();
        Product goodCarItem = map.get(productId);

        goodCarItem.getNum().setValue( goodCarItem.getNum().getValue()+1);

        sumPrice+=goodCarItem.getPriceForUser();
        goodCarItemMap.setValue(map);
    }
    public void minusGoodCarProductMap(Integer productId){

        Map<Integer,Product> map = goodCarItemMap.getValue();
        Product goodCarItem = map.get(productId);

        if(goodCarItem.getNum().getValue()==1){
            map.remove(productId);
            idList.remove(productId);
        }else{
            goodCarItem.getNum().setValue( goodCarItem.getNum().getValue()-1);

        }
        sumPrice-=goodCarItem.getPriceForUser();
        goodCarItemMap.setValue(map);

    }


    public Double  getSumPrice(){
        return sumPrice;
    }
    public void setIdList(List<String> idList){
        this.idList = idList;
    }
    public void setSumPrice(Double price){
        this.sumPrice = price;
    }
    public void setProductMap(Map<Integer, Product> goodCarItemMap){
        this.goodCarItemMap.setValue(goodCarItemMap);
    }
    public List<String> getIdList(){
        return idList;
    }
    public MutableLiveData<Map<Integer,Product>> getProductMap(){
        return  goodCarItemMap;
    }



    public PSearchViewModel(@NonNull Application application) {
        super(application);
        searchValue = new MutableLiveData<>();
        searchHistoryList=new MutableLiveData<>();
        searchHotList=new MutableLiveData<>();
        searchPromptList=new MutableLiveData<>();
        searchResultList=new MutableLiveData<>();
        goodCarItemMap = new MutableLiveData<>();
        idList = new LinkedList<>();
    }
    public void loadSearchHistory(){
        searchHistoryList.setValue(ProductHistoryDatabase.getInstance(getApplication()).productSearchHistoryDao().queryAll(8));

    }
    public void addSearchHistory(String value){
        Long oldId = ProductHistoryDatabase.getInstance(getApplication()).productSearchHistoryDao().queryValue(value);
        if(oldId!=null){
            ProductHistoryDatabase.getInstance(getApplication()).productSearchHistoryDao().deleteById(oldId.intValue());
        }
        ProductSearchHistory productSearchHistory = new ProductSearchHistory();
        productSearchHistory.setValue(value);
        Long id = ProductHistoryDatabase.getInstance(getApplication()).productSearchHistoryDao().insert(productSearchHistory);

        //  HistoryDatabase.getInstance(getApplication()).shopSearchHistoryDao().deleteById(id.intValue());
    }
    public void deleteSearchHistory(){
        ProductHistoryDatabase.getInstance(getApplication()).productSearchHistoryDao().deleteAll();
        searchHistoryList.setValue(null);
    }
    public void loadSearchHot(){

        RetrofitServiceManager.getInstance(getApplication()).create(ProductService.class)
                .getShopHotSearchNames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<BaseJson<List<String>>>as(this))
                .subscribe(new Consumer<BaseJson<List<String>>>() {
                               @Override
                               public void accept(BaseJson<List<String>> results) throws Exception {
                                   if(results.getCode()== ResultCode.SUCCESS.getIndex()){
                                       searchHotList.setValue(results.getResult());
                                   }

                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }

    public void loadSearchPromptList(String value){

        RetrofitServiceManager.getInstance(getApplication()).create(ProductService.class)
                .getSearchPromptValue(value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<PageResults<String>>as(this))
                .subscribe(new Consumer<PageResults<String>>() {
                               @Override
                               public void accept(PageResults<String> results) throws Exception {
                                   if(results.getCode()== ResultCode.SUCCESS.getIndex()){
                                       searchPromptList.setValue(results.getRows());
                                   }

                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }
    public void loadSearchProduct(String value,Integer page){
        addSearchHistory(value);
        RetrofitServiceManager.getInstance(getApplication()).create(ProductService.class)
                .findAllProductWithMoneyOff(page,size,value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<PageResults<Product>>as(this))
                .subscribe(new Consumer<PageResults<Product>>() {
                               @Override
                               public void accept(PageResults<Product> results) throws Exception {
                                   if(results.getCode()== ResultCode.SUCCESS.getIndex()){
                                       searchResultList.setValue(setProduct(results.getRows()));
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
    private List<Product> setProduct (List<Product> list){
        Map<Integer, Product> carItemMap = goodCarItemMap.getValue();
        if(carItemMap==null||list==null||carItemMap.size()==0||list.size()==0){
            return list;
        }
        for(Product product:list){
            if(carItemMap.containsKey(product.getId())){
                product.setNum(carItemMap.get(product.getId()).getNum());
            }
        }

        return list;
    }
    public MutableLiveData<String> getSearchValue(){
        return  searchValue;
    }
    public void setSearchValue(String value){
        searchValue.setValue(value);
    }
    public MutableLiveData<List<ProductSearchHistory>> getSearchHistoryList(){
        return  searchHistoryList;
    }
    public MutableLiveData<List<String>>  getSearchHotList(){
        if(searchHotList==null) return new MutableLiveData<List<String>>();
        return  searchHotList;
    }
    public MutableLiveData<List<String>> getSearchPromptList(){
        if(searchPromptList==null) return new MutableLiveData<List<String>>();
        return  searchPromptList;
    }
    public MutableLiveData<List<Product>> getSearchResultList(){
        if(searchResultList==null) return new MutableLiveData<List<Product>>();
        return  searchResultList;
    }

}
