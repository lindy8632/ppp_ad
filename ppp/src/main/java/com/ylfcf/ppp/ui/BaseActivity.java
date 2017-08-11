package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.umeng.message.PushAgent;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.widget.LoadingDialog;

import java.text.SimpleDateFormat;

/**
 * Activity基础类
 * @author Mr.liu
 *
 */
public class BaseActivity extends FragmentActivity{
	/**
	 * Application实例
	 */
	protected YLFApplication mApp;
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected LoadingDialog mLoadingDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApp = YLFApplication.getApplication();
		mLoadingDialog = new LoadingDialog(this,"正在加载...", R.anim.loading);
		PushAgent.getInstance(getApplicationContext()).onAppStart();
		mApp.addActivity(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mApp.removeActivity(this);
		if(mLoadingDialog != null){
			mLoadingDialog.dismiss();
		}
	}
}