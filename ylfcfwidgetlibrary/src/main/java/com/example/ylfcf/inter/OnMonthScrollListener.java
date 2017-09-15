package com.example.ylfcf.inter;

import android.support.v4.view.ViewPager;

import com.example.ylfcf.info.DateData;
import com.example.ylfcf.utils.ExpCalendarUtil;
import com.example.ylfcf.view.CellConfig;

/**
 * Created by Administrator on 2017/8/17.
 */

public abstract class OnMonthScrollListener implements ViewPager.OnPageChangeListener{
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        onMonthScroll(positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        CellConfig.middlePosition = position;
        DateData date;
        if (CellConfig.ifMonth)
            date = ExpCalendarUtil.position2Month(position);
        else
            date = ExpCalendarUtil.position2Week(position);
        onMonthChange(date.getYear(), date.getMonth());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public abstract void onMonthChange(int year, int month);

    public abstract void onMonthScroll(float positionOffset);
}
