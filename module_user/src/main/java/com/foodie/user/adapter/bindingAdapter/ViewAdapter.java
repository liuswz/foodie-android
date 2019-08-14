package com.foodie.user.adapter.bindingAdapter;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.foodie.base.config.GlideOption;
import com.foodie.user.R;


public class ViewAdapter {


    //防重复点击间隔(秒)



    static RequestOptions options = new RequestOptions().circleCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
    @BindingAdapter(value = {"imgurl"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            //  Toast.makeText(this, picture, Toast.LENGTH_SHORT).show();
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(options)
                    .into(imageView);

        }else{
         //   imageView.setBackgroundResource();
            imageView.setImageResource(R.drawable.icon_person);
        }
    }

}
