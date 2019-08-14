package com.foodie.product.adapter.bindingAdapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.foodie.base.config.GlideOption;

public class ViewAdapter {


    //防重复点击间隔(秒)

    @BindingAdapter(value = {"imgurl"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(GlideOption.getOption())
                    .into(imageView);

        }
    }

}
