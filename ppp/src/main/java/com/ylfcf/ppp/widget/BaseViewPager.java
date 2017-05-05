package com.ylfcf.ppp.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * 自定义高度的viewpapger
 */
public class BaseViewPager extends ViewPager{
	private boolean scrollable = true;
	private ViewGroup parent;  

	public BaseViewPager(Context context) {
		super(context);
	}

	public BaseViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setParent(ViewGroup parent) {
		this.parent = parent;
	}
	
	/**
	 * 设置viewpager是否可以滚动
	 * 
	 * @param enable
	 */
	public void setScrollable(boolean enable) {
		scrollable = enable;
	}
	
	float mDownX = 0f;
	float mDownY = 0f;
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
//		if (parent != null) {
//			parent.requestDisallowInterceptTouchEvent(true);
//		}
//		return super.dispatchTouchEvent(ev);
//		switch (ev.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			mDownX = ev.getX();
//			mDownY = ev.getY();
//			parent.requestDisallowInterceptTouchEvent(false);
//			break;
//		case MotionEvent.ACTION_MOVE:
//			if(Math.abs(ev.getX() - mDownX) > Math.abs(ev.getY() - mDownY)){
//				parent.requestDisallowInterceptTouchEvent(false);
//			}else{
//				parent.requestDisallowInterceptTouchEvent(true);
//			}
//			break;
//		case MotionEvent.ACTION_UP:
//		case MotionEvent.ACTION_CANCEL:
//			parent.requestDisallowInterceptTouchEvent(true);
//			break;
//		}
		return super.dispatchTouchEvent(ev);
	}
	
//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent event) {
//		if (parent != null) {
//			parent.requestDisallowInterceptTouchEvent(true);
//		}
//		if (scrollable) {
//			return super.onInterceptTouchEvent(event);
//		} else {
//			return false;
//		}
//	}
//	
//	@Override
//	public boolean onTouchEvent(MotionEvent arg0) {
//		if (parent != null) {
//			parent.requestDisallowInterceptTouchEvent(true);
//		}
//		return super.onTouchEvent(arg0);
//	}
}
