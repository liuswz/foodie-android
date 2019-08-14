//package com.foodie.product.database;
//
//
//import android.content.Context;
//
//import androidx.annotation.NonNull;
//import androidx.room.Database;
//import androidx.room.DatabaseConfiguration;
//import androidx.room.InvalidationTracker;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//import androidx.sqlite.db.SupportSQLiteOpenHelper;
//
//import com.foodie.base.entity.Product;
//import com.foodie.product.entity.ProductSearchHistory;
//import com.foodie.product.service.databaseDao.ProductSearchHistoryDao;
//
///**
// * Created by mikeluo on 2019/3/18.
// */
//@Database(entities = {Product.class}, version = 1,exportSchema = false) // 声明版本号1
//public abstract class ProductDatabase extends RoomDatabase {
//    private static ProductDatabase INSTANCE;
//    public abstract ProductSearchHistoryDao productSearchHistoryDao();
//
//    public static ProductDatabase getInstance(Context context) {
//        if(INSTANCE == null) {//单例设计模式 双重校验
//            synchronized (ProductDatabase.class) {
//                if(INSTANCE == null) {
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ProductDatabase.class,
//                            "foodie.db").
//                            allowMainThreadQueries().//允许在主线程读取数据库
//                            build();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//
//    /**
//     * 数据库变动添加Migration
//     */
//    @NonNull
//    @Override
//    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
//        return null;
//    }
//
//    @NonNull
//    @Override
//    protected InvalidationTracker createInvalidationTracker() {
//        return null;
//    }
//
//    @Override
//    public void clearAllTables() {
//
//    }
//}
