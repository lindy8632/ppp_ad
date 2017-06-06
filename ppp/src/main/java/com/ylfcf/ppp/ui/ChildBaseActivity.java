package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;

import com.ylfcf.ppp.widget.SwipeHelper;

/**
 * 子Activity的基础类(左滑finish)
 * Created by Administrator on 2017/5/25.
 */

public class ChildBaseActivity extends BaseActivity {
    private SwipeHelper mSwipeHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeHelper = new SwipeHelper(this);
        mSwipeHelper.onActivityCreate();
        mSwipeHelper.setSwipeEdge(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSwipeHelper.onPostCreate();
    }
}
