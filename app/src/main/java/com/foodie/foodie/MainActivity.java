package com.foodie.foodie;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.alibaba.android.arouter.launcher.ARouter;


import com.foodie.base.router.RouterFragmentPath;
import com.foodie.foodie.databinding.ActivityMainBinding;


import java.util.ArrayList;
import java.util.List;




/**
 * Created by goldze on 2018/6/21
 */

public class MainActivity extends AppCompatActivity {
    private List<Fragment> mFragments;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//初始化Fragment
        initFragment();
        //初始化底部Button
        initBottomTab();

    }

    private void initFragment() {
        //ARouter拿到多Fragment(这里需要通过ARouter获取，不能直接new,因为在组件独立运行时，宿主app是没有依赖其他组件，所以new不到其他组件的Fragment)
        Fragment homeFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Home.PAGER_HOME).navigation();
        Fragment productFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Product.PAGER_PRODUCT).navigation();
        Fragment orderFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Order.PAGER_ORDER).navigation();
        Fragment userFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.User.PAGER_USER).navigation();
        mFragments = new ArrayList<>();
        mFragments.add(homeFragment);
        mFragments.add(productFragment);
        mFragments.add(orderFragment);
        mFragments.add(userFragment);
        if (homeFragment != null) {
            //默认选中第一个

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frameLayout, homeFragment);
            transaction.commitAllowingStateLoss();
        }
    }

    private void initBottomTab() {
        NavigationController navigationController = binding.pagerBottomTab.material()
                .addItem(R.mipmap.yingyong, "首页")
                .addItem(R.mipmap.huanzhe, "商品")
                .addItem(R.mipmap.xiaoxi_select, "订单")
                .addItem(R.mipmap.wode_select, "我的")
                .setDefaultColor(ContextCompat.getColor(this, R.color.gray))
                .build();
        //底部按钮的点击事件监听
        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                Fragment currentFragment = mFragments.get(index);

                if (currentFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameLayout, currentFragment);
                    transaction.commitAllowingStateLoss();
                }
            }

            @Override
            public void onRepeat(int index) {
            }
        });
    }
}
