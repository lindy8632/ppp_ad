package com.ylfcf.ppp.widget;
/**
 * 自动增加的数字接口
 * @author Mr.liu
 *
 */
public interface RiseNumberBase {
	public void start();
    public RiseNumberTextView withNumber(float number);
    public RiseNumberTextView withNumber(int number);
    public RiseNumberTextView setDuration(long duration);
    public void setOnEnd(RiseNumberTextView.EndListener callback);
}
