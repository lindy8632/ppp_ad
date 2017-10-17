package com.example.ylfcf.view;

import android.support.v7.widget.GridLayoutManager;

/**
 * Created by Administrator on 2017/6/9.
 */

public class XSpanSizeLookup extends GridLayoutManager.SpanSizeLookup{
    private BaseRecyclerAdapter adapter;
    private int mSpanSize = 1;

    public XSpanSizeLookup(BaseRecyclerAdapter adapter, int spanSize) {
        this.adapter = adapter;
        this.mSpanSize = spanSize;
    }

    @Override
    public int getSpanSize(int position) {
        boolean isHeaderOrFooter = adapter.isFooter(position) || adapter.isHeader(position);
        return isHeaderOrFooter ? mSpanSize : 1;
    }
}
