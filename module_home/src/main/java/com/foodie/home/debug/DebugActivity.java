package com.foodie.home.debug;


import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentTransaction;


import com.foodie.home.ui.fragment.HomeFragment;
import com.foodie.home.R;

import io.reactivex.annotations.Nullable;


/**
 * 组件单独运行时的调试界面，不会被编译进release里
 * Created by goldze on 2018/6/21
 */

public class DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.fragment_main);
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frameLayoutTest, fragment);
        transaction.commitAllowingStateLoss();


    }


}
