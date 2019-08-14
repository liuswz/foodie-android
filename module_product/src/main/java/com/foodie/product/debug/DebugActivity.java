package com.foodie.product.debug;

import android.os.Bundle;

import io.reactivex.annotations.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


import com.foodie.product.R;
import com.foodie.product.ui.fragment.ProductFragment;


/**
 * 组件单独运行时的调试界面，不会被编译进release里
 * Created by goldze on 2018/6/21
 */

public class DebugActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        ProductFragment fragment = new ProductFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frameLayoutTest2, fragment);
        transaction.commitAllowingStateLoss();
    }
}
