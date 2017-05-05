package com.ylfcf.ppp.widget;

import java.util.Date;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.ylfcf.ppp.R;

/**
 * 加载dialog
 * @author Mr.liu
 *
 */
public class LoadingDialog extends ProgressDialog {
	private AnimationDrawable mAnimation;  
    private Context mContext;  
    private ImageView mImageView;  
    private String mLoadingTip;  
    private TextView mLoadingTv;  
    private int count = 0;  
    private String oldLoadingTip;  
    private int mResid;  
    private long showTime;//loadingDialog开始显示的时间
    private long endTimeTemp;
  
    public LoadingDialog(Context context, String content, int id) {  
        super(context,R.style.CustomProgressDialog);  
        this.mContext = context;  
        this.mLoadingTip = content;  
        this.mResid = id;  
        setCanceledOnTouchOutside(false);  
    }
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        initView();  
        initData();  
    }  
  
    @Override
    public void show() {
    	super.show();
    	showTime = new Date().getTime();
    	endTimeTemp = showTime + 1000;
    }
    
    @Override
    public void dismiss() {
//    	long nowTime = new Date().getTime();
//    	if(nowTime < endTimeTemp){
//    		new Handler().postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					dismiss();
//				}
//			}, 300L);//延迟半秒消失。不至于网速太快时会出现闪屏的情况。
//    		return;
//    	}
    	super.dismiss();
    }
    
    private void initData() {  
  
        mImageView.setBackgroundResource(mResid);  
        // 通过ImageView对象拿到背景显示的AnimationDrawable  
        mAnimation = (AnimationDrawable) mImageView.getBackground();  
        // 为了防止在onCreate方法中只显示第一帧的解决方案之一  
        mImageView.post(new Runnable() {  
            @Override  
            public void run() {  
                mAnimation.start();  
  
            }  
        });  
        mLoadingTv.setText(mLoadingTip);  
  
    }  
  
    public void setContent(String str) {  
        mLoadingTv.setText(str);  
    }  
  
    private void initView() { 	 
        setContentView(R.layout.progress_dialog);  
        mLoadingTv = (TextView) findViewById(R.id.loadingTv);  
        mImageView = (ImageView) findViewById(R.id.loadingIv);  
    }  

}
