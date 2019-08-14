//package com.foodie.product.service.databaseDao;
//
//import androidx.room.Dao;
//import androidx.room.Insert;
//import androidx.room.Query;
//
//import com.foodie.product.entity.ProductSearchHistory;
//
//import java.util.List;
//
//@Dao
//public interface ProductDao {
//
//    @Insert
//    Long insert(ProductSearchHistory productSearchHistory);
//
//    @Query("select * from product_search_history order by id desc limit :num")
//    List<ProductSearchHistory> queryAll(Integer num);
//
//    @Query("delete from product_search_history where id = :id")
//    void deleteById(Integer id);
//
//    @Query("delete from product_search_history")
//    void deleteAll();
//
//    @Query("select id from product_search_history where value=:value")
//    Long queryValue(String value);
//
//}
//
//
