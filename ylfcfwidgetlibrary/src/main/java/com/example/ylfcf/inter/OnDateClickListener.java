package com.example.ylfcf.inter;

import android.view.View;

import com.example.ylfcf.info.DateData;

/**
 * Created by Administrator on 2017/8/17.
 */

public abstract class OnDateClickListener {
    public static OnDateClickListener instance;

    public abstract void onDateClick(View view, DateData date);
}
