package com.ylfcf.ppp.util;

import com.umeng.analytics.MobclickAgent;

import android.content.Context;

/**
 * 友盟
 * 
 * @author Administrator
 * 
 */
public class UMengStatistics {
	/**
	 * 友盟更新应用
	 * 
	 * @param context
	 */
	// public static void updateApp(final Context context) {
	// UmengUpdateAgent.update(context);
	// UmengUpdateAgent.setUpdateAutoPopup(false);
	// UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
	// @Override
	// public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo)
	// {
	// if (updateStatus == 0) {
	// UmengUpdateAgent.showUpdateDialog(context, updateInfo);
	// }
	// }
	// });
	// }

	/**
	 * 在调用这组API统计页面之前，需要在尽早在程序入口处， 调用
	 * MobclickAgent.openActivityDurationTrack(false) 禁止默认的页面统计 方式，这样
	 * onResume(Context) 和 onPause(Context)
	 * 方法将不会再自动统计页面。应用程序页面的实现可能是基于Activity也可能是Fragment， 推荐在相应的 onResume 和
	 * onPause 方法中调用
	 */
	public static void statisticsInit() {
		MobclickAgent.openActivityDurationTrack(false);
	}

	/**
	 * 统计自定义事件
	 * 
	 * @param context
	 * @param eventId
	 * @param label
	 */
	public static void statisticsCustomEvent(Context context, String eventId,
			String label) {
		MobclickAgent.onEvent(context, eventId, label);
	}

	/**
	 * 统计时长
	 * 
	 * @param context
	 */
	public static void statisticsResume(Context context) {
		MobclickAgent.onResume(context);
	}

	/**
	 * 统计时长
	 * 
	 * @param context
	 */
	public static void statisticsPause(Context context) {
		MobclickAgent.onPause(context);
	}

	/**
	 * 统计页面开始
	 * 
	 * @param context
	 */
	public static void statisticsOnPageStart(String mClassName) {
		// String contextStr = getRunningActivityName(context);
		MobclickAgent.onPageStart(mClassName);
	}

	/**
	 * 统计页面结束
	 * 
	 * @param context
	 */
	public static void statisticsOnPageEnd(String mClassName) {
		// String contextStr = getRunningActivityName(context);
		MobclickAgent.onPageEnd(mClassName);
	}

	/**
	 * 根据Context获取类名
	 * 
	 * @param context
	 * @return
	 */
	private static String getRunningActivityName(Context context) {
		// String contextString = context.toString();
		// return contextString.substring(contextString.lastIndexOf(".") + 1,
		// contextString.indexOf("@"));
		return context.getClass().getSimpleName();
	}

	public static void reportError(Context context, String error) {
		MobclickAgent.reportError(context, error);
	}

	// 或
	public static void reportError(Context context, Throwable e) {
		MobclickAgent.reportError(context, e);
	}
}
