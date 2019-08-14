package com.foodie.home.database;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.foodie.home.entity.ShopSearchHistory;
import com.foodie.home.service.databaseDao.ShopSearchHistoryDao;

/**
 * Created by mikeluo on 2019/3/18.
 */
@Database(entities = {ShopSearchHistory.class}, version = 1,exportSchema = false) // 声明版本号1
public abstract class HistoryDatabase extends RoomDatabase {
    private static HistoryDatabase INSTANCE;
    public abstract ShopSearchHistoryDao shopSearchHistoryDao();

    public static HistoryDatabase getInstance(Context context) {
        if(INSTANCE == null) {//单例设计模式 双重校验
            synchronized (HistoryDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),HistoryDatabase.class,
                            "foodie.db").
                            allowMainThreadQueries().//允许在主线程读取数据库
                            build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 数据库变动添加Migration
     */
    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
