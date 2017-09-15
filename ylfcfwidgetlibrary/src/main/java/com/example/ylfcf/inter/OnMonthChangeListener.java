package com.example.ylfcf.inter;

import android.support.v4.view.ViewPager;

import com.example.ylfcf.utils.CalendarUtil;

/**
 * Created by Administrator on 2017/8/17.
 */

public abstract class OnMonthChangeListener implements ViewPager.OnPageChangeListener{
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Log.wtf("OnPageScrolled:", ""+position);
    }

    @Override
    public void onPageSelected(int position) {
//        Log.wtf("OnPageSelected:", ""+position);
        onMonthChange(CalendarUtil.position2Year(position), CalendarUtil.position2Month(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public abstract void onMonthChange(int year, int month);
}
