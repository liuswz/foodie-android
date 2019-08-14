package com.foodie.home.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MyViewPager extends ViewPager {

    private int position;

    private HashMap<Integer, Integer> maps = new LinkedHashMap<Integer, Integer>();

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            maps.put(i, h);
        }
        Log.e("Owen",getChildCount()+"------------------------");
        if (getChildCount() > 0) {
            height = getChildAt(position).getMeasuredHeight();
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 在切换tab的时候，重置viewPager的高度
     */
    public void resetHeight(int position) {

        this.position = position;
        if (maps.size() > position) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, maps.get(position));
            } else {
                layoutParams.height = maps.get(position);
            }
            setLayoutParams(layoutParams);
        }
    }

}
