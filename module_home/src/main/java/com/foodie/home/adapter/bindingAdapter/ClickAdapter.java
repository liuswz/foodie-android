package com.foodie.home.adapter.bindingAdapter;

import android.util.Log;
import android.view.View;

import androidx.databinding.BindingAdapter;

public class ClickAdapter {
//    public static final int CLICK_INTERVAL = 1;
//    @BindingAdapter(value = {"onClickCommand"})
//    public static void onClickCommand(View view, final View.OnClickListener listener) {
//        Toast.makeText(view.getContext(), "11111111111111", Toast.LENGTH_SHORT).show();
//        Log.e("aaaaaaaaaaaaaaaa","1111111111111111111111111111");
//        RxView.clicks(view)
//                .throttleFirst(CLICK_INTERVAL, TimeUnit.SECONDS)//1秒钟内只允许点击1次
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(Object object) throws Exception {
//                        if (listener != null) {
//                           listener.onClick(view);
//                        }
//                    }
//                });
//
//
//    }
    private static long mLastClickTime = 0;
    private static final long TIME_INTERVAL = 1000L;
    @BindingAdapter(value = {"onClickCommand"})
    public static void onClickCommand(View view, final View.OnClickListener listener) {
        Log.e("aaaaaaaaaaaaaaaa","1111111111111111111111111111");
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > TIME_INTERVAL) {
            if (listener != null) {

                listener.onClick(view);
            }
            mLastClickTime = nowTime;
        }


    }
}
