package com.ylfcf.ppp.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ½ûÖ¹»¬¶¯µÄviewpager
 * @author Administrator
 *
 */
public class NoScrollViewPager extends ViewPager{
	private boolean isCanScroll = false;  
	  
    public NoScrollViewPager(Context context) {  
        super(context);  
    }  
  
    public NoScrollViewPager(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public void setScanScroll(boolean isCanScroll){  
        this.isCanScroll = isCanScroll;  
    }  
  
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
    	if (isCanScroll == false) {
    		return false;
    	} else {
    		return super.onTouchEvent(ev);
    	}
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	if (isCanScroll == false) {
    		return false;
    	} else {
    		return super.onInterceptTouchEvent(ev);
    	}
    }
}