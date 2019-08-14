package com.foodie.home.service.databaseDao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.foodie.home.entity.ShopSearchHistory;

import java.util.List;

@Dao
public interface ShopSearchHistoryDao {

    @Insert
    Long insert(ShopSearchHistory shopSearchHistory);

    @Query("select * from shop_search_history order by id desc limit :num")
    List<ShopSearchHistory> queryAll(Integer num);

    @Query("select id from shop_search_history where value=:value")
    Long queryValue(String value);

    @Query("delete from shop_search_history where id = :id")
    void deleteById(Integer id);
    @Query("delete from shop_search_history")
    void deleteAll();
}
