package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.umeng.message.PushAgent;
import com.ylfcf.ppp.util.UMengStatistics;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApp = YLFApplication.getApplication();
		PushAgent.getInstance(getApplicationContext()).onAppStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsResume(this);//友盟统计
	}

	@Override
	protected void onPause() {
		super.onPause();
		UMengStatistics.statisticsPause(this);//友盟统计
	}
}
