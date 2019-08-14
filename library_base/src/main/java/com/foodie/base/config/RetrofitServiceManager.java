package com.foodie.base.config;

import android.content.Context;

import com.foodie.base.base.Constant;

import java.io.File;
import java.util.concurrent.TimeUnit;


import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceManager {

    private final int TIMEOUT = 10;
    private static Retrofit mRetrofit;
    private static RetrofitServiceManager mRetrofitManager;
    private Context mContext;//manager需要持有context
    private RetrofitServiceManager(Context context){
        this.mContext = context;
        int CACHE_TIMEOUT = 10 * 1024 * 1024;
        //缓存存放的文件
        File httpCacheDirectory = new File(context.getCacheDir(), "goldze_cache");
        //缓存对象
        Cache cache = new Cache(context.getCacheDir(), CACHE_TIMEOUT);

        // 创建 OKHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)  //连接超时设置
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)  //写入缓存超时10s
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)  //读取缓存超时10s
                .retryOnConnectionFailure(true)  //失败重连
              //  .addInterceptor(new HeaderInterceptor())  //添加header

                .addInterceptor(new CacheInterceptor(mContext))
                .cache(cache);
//        Interceptor headerInterceptor = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request originalRequest = chain.request();
//                Request.Builder requestBuilder = originalRequest.newBuilder()
//                        .addHeader("Accept-Encoding", "gzip")
//                        .addHeader("Accept", "application/json")
//                        .addHeader("Content-Type", "application/json; charset=utf-8")
//                        .method(originalRequest.method(), originalRequest.body());
//             //   requestBuilder.addHeader("Authorization", "Bearer " + BaseConstant.TOKEN);//添加请求头信息，服务器进行token有效性验证
//                Request request = requestBuilder.build();
//                return chain.proceed(request);
//            }
//        };



             //  .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)));



        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.BASE_URL)
                .build();
    }

//    private static class SingletonHolder{
//        private static final RetrofitServiceManager INSTANCE = new RetrofitServiceManager();
//    }

    /**
     * 获取RetrofitServiceManager
     * @return
     */
    public static RetrofitServiceManager getInstance(Context context){
        if (mRetrofitManager == null) {//双重校验锁
            synchronized(RetrofitServiceManager.class) {
                if (mRetrofitManager == null) {
                    mRetrofitManager = new RetrofitServiceManager(context);
                }
            }
        }
        return mRetrofitManager;
     //   return SingletonHolder.INSTANCE;
    }

    /**
     * 获取对应的Service
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service){

        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return mRetrofit.create(service);
    }



}
