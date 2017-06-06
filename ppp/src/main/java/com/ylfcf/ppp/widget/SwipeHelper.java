package com.ylfcf.ppp.widget;

import android.app.Activity;
import android.view.LayoutInflater;

import com.ylfcf.ppp.R;

/**
 * Created by Administrator on 2017/5/25.
 */

public class SwipeHelper {
    private Activity mActivity;
    private BaseSwipeLayout mBaseSwipeLayout;

    public SwipeHelper(Activity activity) {
        this.mActivity = activity;
    }

    public void onActivityCreate() {
        mBaseSwipeLayout = (BaseSwipeLayout) LayoutInflater.from(mActivity)
                .inflate(R.layout.swipe_layout, null);
        mBaseSwipeLayout.setOnFinishScroll(new BaseSwipeLayout.OnFinishScroll() {
            @Override
            public void complete() {
                mActivity.finish();
            }
        });
    }

    public void onPostCreate() {
        mBaseSwipeLayout.attachToActivity(mActivity);
    }

    public void setSwipeEdge(int edgeFlag) {
        mBaseSwipeLayout.setSwipeEdge(edgeFlag);
    }
}
