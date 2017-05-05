package com.ylfcf.ppp.widget;

import com.ylfcf.ppp.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * 上拉刷新
 * 
 * @author Administrator
 * 
 */
public class LoadMoreListView extends ListView implements OnScrollListener {
	/**
	 * 滑动到最下面时的上拉操作
	 */

	private int mTouchSlop;
	private OnLoadMoreListener mOnLoadListener;
	/**
	 * ListView的加载中footer
	 */
	private View mListViewFooter;
	/**
	 * 按下时的y坐标
	 */
	private int mYDown;
	/**
	 * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
	 */
	private int mLastY;
	/**
	 * 是否在加载中 ( 上拉加载更多 )
	 */
	private boolean isLoading = false;

	public LoadMoreListView(Context context) {
		super(context);
		mListViewFooter = LayoutInflater.from(context).inflate(
				R.layout.listview_footer, null, false);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	public LoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mListViewFooter = LayoutInflater.from(context).inflate(
				R.layout.listview_footer, null, false);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// 滚动时到了最底部也可以加载更多
		if (canLoad() && visibleItemCount < this.getAdapter().getCount()) {
			loadData();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// 按下
			mYDown = (int) event.getRawY();
			break;

		case MotionEvent.ACTION_MOVE:
			// 移动
			mLastY = (int) event.getRawY();
			break;

		case MotionEvent.ACTION_UP:
			// 抬起
			if (canLoad()) {
				loadData();
			}
			break;
		default:
			break;
		}

		return super.dispatchTouchEvent(event);
	}

	/**
	 * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作.
	 * 
	 * @return
	 */
	private boolean canLoad() {
		return isBottom() && !isLoading && isPullUp();
	}

	/**
	 * 判断是否到了最底部
	 */
	private boolean isBottom() {
		if (this.getAdapter() != null) {
			return this.getLastVisiblePosition() == (this.getAdapter()
					.getCount() - 1);
		}
		return false;
	}

	/**
	 * 是否是上拉操作
	 * 
	 * @return
	 */
	private boolean isPullUp() {
		return (mYDown - mLastY) >= mTouchSlop;
	}

	/**
	 * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
	 */
	private void loadData() {
		if (mOnLoadListener != null) {
			// 设置状态
			setLoading(true);
			//
			mOnLoadListener.onLoadMore();
		}
	}

	/**
	 * @param loading
	 */
	public void setLoading(boolean loading) {
		isLoading = loading;
		if (isLoading) {
			this.addFooterView(mListViewFooter);
		} else {
			this.removeFooterView(mListViewFooter);
			mYDown = 0;
			mLastY = 0;
		}
	}

	/**
	 * @param loadListener
	 */
	public void setOnLoadMoreListener(OnLoadMoreListener loadListener) {
		mOnLoadListener = loadListener;
	}

	public interface OnLoadMoreListener {
		void onLoadMore();
	}
}
