package com.example.ylfcf.view;

import android.content.Context;
import android.util.AttributeSet;

import com.example.ylfcf.info.DateData;
import com.example.ylfcf.inter.OnDateClickListener;

/**
 * Created by Administrator on 2017/8/17.
 */

public abstract class BaseMarkView extends BaseCellView{
    private OnDateClickListener clickListener;
    private DateData date;

    public BaseMarkView(Context context) {
        super(context);
    }

    public BaseMarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
