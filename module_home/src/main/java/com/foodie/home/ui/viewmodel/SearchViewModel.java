package com.foodie.home.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.foodie.base.base.BaseJson;
import com.foodie.base.enums.ResultCode;
import com.foodie.base.entity.PageResults;
import com.foodie.base.entity.SearchShop;
import com.foodie.base.entity.ShopSearchPrompt;
import com.foodie.base.config.RetrofitServiceManager;
import com.foodie.home.database.HistoryDatabase;
import com.foodie.home.entity.SearchResult;
import com.foodie.home.entity.ShopSearchHistory;
import com.foodie.home.service.networkService.ShopService;
import com.rxjava.rxlife.RxLife;
import com.rxjava.rxlife.ScopeViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends ScopeViewModel {

    public MutableLiveData<String> searchValue;
    public MutableLiveData<List<ShopSearchHistory>> searchHistoryList;
    public MutableLiveData<List<String>> searchHotList;
    public MutableLiveData<List<ShopSearchPrompt>> searchPromptList;
    public MutableLiveData<List<SearchShop>> searchResultList;

    private Integer size=10;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private String city="长春市";
    private Integer page=1;

    private Boolean hasMore = true;
    private Integer showFlag=1;
    public Boolean getHasMore(){
        return  hasMore;
    }
    public SearchViewModel(@NonNull Application application) {
        super(application);
        searchValue = new MutableLiveData<>();
        searchHistoryList=new MutableLiveData<>();
        searchHotList=new MutableLiveData<>();
        searchPromptList=new MutableLiveData<>();
        searchResultList=new MutableLiveData<>();

    }
    public void setLocation(Double longitude,Double latitude){
        this.longitude = longitude;
        this.latitude =latitude;
    }
    public void setCity(String city){
        this.city = city;
    }
    public void setPage(Integer page){
        this.page = page;
    }
    public void addPage(){
        page++;
    }
    public void loadSearchHistory(){
        searchHistoryList.setValue(HistoryDatabase.getInstance(getApplication()).shopSearchHistoryDao().queryAll(8));
    }
    public void addSearchHistory(String value){
        Long oldId = HistoryDatabase.getInstance(getApplication()).shopSearchHistoryDao().queryValue(value);
        if(oldId!=null){
            HistoryDatabase.getInstance(getApplication()).shopSearchHistoryDao().deleteById(oldId.intValue());
        }
        ShopSearchHistory shopSearchHistory = new ShopSearchHistory();
        shopSearchHistory.setValue(value);
        Long id = HistoryDatabase.getInstance(getApplication()).shopSearchHistoryDao().insert(shopSearchHistory);

      //  HistoryDatabase.getInstance(getApplication()).shopSearchHistoryDao().deleteById(id.intValue());
    }
    public void deleteSearchHistory(){
         HistoryDatabase.getInstance(getApplication()).shopSearchHistoryDao().deleteAll();
        searchHistoryList.setValue(null);
    }
    public void loadSearchHot(){
        RetrofitServiceManager.getInstance(getApplication()).create(ShopService.class)
                .getShopHotSearchNames(city)
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

        RetrofitServiceManager.getInstance(getApplication()).create(ShopService.class)
                .getSearchPromptValue(city,value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<PageResults<ShopSearchPrompt>>as(this))
                .subscribe(new Consumer<PageResults<ShopSearchPrompt>>() {
                               @Override
                               public void accept(PageResults<ShopSearchPrompt> results) throws Exception {

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
    public void loadSearchShopByLocation(String value){
        addSearchHistory(value);

        RetrofitServiceManager.getInstance(getApplication()).create(ShopService.class)
                .searchShopByLocation(value,longitude,latitude,city,page,size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<SearchResult<SearchShop>>as(this))
                .subscribe(new Consumer<SearchResult<SearchShop>>() {
                               @Override
                               public void accept(SearchResult<SearchShop> results) throws Exception {
                                   if(results.getCode()== ResultCode.SUCCESS.getIndex()){
                                       searchResultList.setValue(results.getRows());
                                       page = results.getShouldPage();
                                       hasMore=results.getHasMore();
                                   }

                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }
    public void loadSearchShopByMark(String value){

        RetrofitServiceManager.getInstance(getApplication()).create(ShopService.class)
                .searchShopByMark(value,longitude,latitude,city,page,size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.<SearchResult<SearchShop>>as(this))
                .subscribe(new Consumer<SearchResult<SearchShop>>() {
                               @Override
                               public void accept(SearchResult<SearchShop> results) throws Exception {
                                   if(results.getCode()== ResultCode.SUCCESS.getIndex()){
                                       searchResultList.setValue(results.getRows());
                                       page = results.getShouldPage();
                                       hasMore=results.getHasMore();
                                   }

                               }}, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.e("TAG>>>", "" + throwable.getMessage());

                               }
                           }
                );
    }

    public MutableLiveData<String> getSearchValue(){
        return  searchValue;
    }
    public void setSearchValue(String value){
        searchValue.setValue(value);
    }
    public MutableLiveData<List<ShopSearchHistory>> getSearchHistoryList(){
        return  searchHistoryList;
    }
    public MutableLiveData<List<String>>  getSearchHotList(){
        if(searchHotList==null) return new MutableLiveData<List<String>>();
        return  searchHotList;
    }
    public MutableLiveData<List<ShopSearchPrompt>> getSearchPromptList(){
        if(searchPromptList==null) return new MutableLiveData<List<ShopSearchPrompt>>();
        return  searchPromptList;
    }
    public MutableLiveData<List<SearchShop>> getSearchResultList(){
        if(searchResultList==null) return new MutableLiveData<List<SearchShop>>();
        return  searchResultList;
    }


}
