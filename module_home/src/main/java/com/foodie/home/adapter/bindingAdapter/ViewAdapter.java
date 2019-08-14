package com.foodie.home.adapter.bindingAdapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.BindingAdapter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.foodie.base.entity.Advertisement;
import com.foodie.base.config.GlideOption;
import com.foodie.base.enums.AdType;
import com.foodie.base.router.RouterActivityPath;
import com.foodie.home.ui.DetailActivity;
import com.foodie.home.ui.TypeActivity;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.XBanner.XBannerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewAdapter {


    //防重复点击间隔(秒)


    private static long mLastClickTime = 0;
    public static final long TIME_INTERVAL = 1000L;

    @BindingAdapter(value = {"bannerItem"})
    public static void setBanner(XBanner banner, final List<Advertisement> adlist) {
            //使用Glide框架加载图片

        if(adlist!=null){
            final List<String> list_path = new ArrayList<String>();
            for(int i=0;i<adlist.size();i++){
                list_path.add(adlist.get(i).getPhotoUrl());
            }
            banner.setData(list_path, null);
            // XBanner适配数据
            banner.setmAdapter(new XBannerAdapter() {
                @Override
                public void loadBanner(XBanner banner, Object model, View view, int position) {
                    Glide.with(banner.getContext()).load(list_path.get(position)).apply(GlideOption.getOption())
                            .into((ImageView) view);
                }

            });
            banner.setOnItemClickListener(new XBanner.OnItemClickListener() {
                @Override
                public void onItemClick(XBanner banner, int position) {
                    long nowTime = System.currentTimeMillis();
                    if (nowTime - mLastClickTime > TIME_INTERVAL) {


                        Advertisement ad = adlist.get(position);
                        if(ad.getTypeId()== AdType.Shop.getIndex()){
                            Intent intent = new Intent(banner.getContext(), DetailActivity.class);
                            intent.putExtra("id",ad.getRedirectId()+"");
                            banner.getContext().startActivity(intent);
                        }else{

                            ARouter.getInstance().build(RouterActivityPath.Detail.PRODUCTDETAIL)
                                    .withInt("id",ad.getRedirectId())
                                    .navigation();
                        }

                        mLastClickTime = nowTime;
                    }

                }
            });
        }


    }

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
