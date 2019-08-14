package com.foodie.base.config;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.foodie.base.R;

public class GlideOption {

    public static RequestOptions getOption(){
        return  new RequestOptions()
                .placeholder(R.drawable.timg).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
    }
}
