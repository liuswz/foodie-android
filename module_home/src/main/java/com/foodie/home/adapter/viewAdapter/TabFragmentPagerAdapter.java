package com.foodie.home.adapter.viewAdapter;



import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;



public class TabFragmentPagerAdapter extends FragmentPagerAdapter  {

    private List<Fragment> mFragments;

    private List<String> mTitles;
  //  private LinearLayout shopcarBottomlayout;


    public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> framents, List<String> titles) {

        super(fm);

        mFragments = framents;

        mTitles = titles;
        //this.shopcarBottomlayout = shopcarBottomlayout;
    }



    @Override

    public Fragment getItem(int position) {

        return mFragments.get(position);

    }



    @Override

    public int getCount() {

        return mFragments == null ? 0 : mFragments.size();

    }



    @Override

    public CharSequence getPageTitle(int position) {

        return mTitles.get(position);

    }

}
