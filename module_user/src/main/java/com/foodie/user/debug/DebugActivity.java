package com.foodie.user.debug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


import com.foodie.user.R;
import com.foodie.user.ui.fragment.UserFragment;



/**
 * 组件单独运行时的调试界面，不会被编译进release里
 * Created by goldze on 2018/6/21
 */

public class DebugActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        UserFragment fragment = new UserFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frameLayoutTest4, fragment);
        transaction.commitAllowingStateLoss();
    }
}
