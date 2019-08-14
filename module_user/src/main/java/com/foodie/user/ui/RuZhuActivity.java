package com.foodie.user.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.foodie.base.adapter.OnClickEvent;
import com.foodie.base.base.Constant;


import com.foodie.user.databinding.ActivityRuZhuBinding;
import com.foodie.user.R;

public class RuZhuActivity extends AppCompatActivity {

    private ActivityRuZhuBinding activityRuZhuBinding;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ru_zhu);
        activityRuZhuBinding = DataBindingUtil.setContentView(this, R.layout.activity_ru_zhu);
        activityRuZhuBinding.backPage.setOnClickListener(backPage);
        webView = activityRuZhuBinding.webview;
        webView.loadUrl(Constant.RuZhuUrl);
        WebSettings webSettings = webView.getSettings();

//设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
            webSettings.setLoadWithOverviewMode(true); //
        }


    }
    private OnClickEvent backPage = new OnClickEvent() {
        @Override
        public void singleClick(View v) {
            finish();
        }
    };
}
