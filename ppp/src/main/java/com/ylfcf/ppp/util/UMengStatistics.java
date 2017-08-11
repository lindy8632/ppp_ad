package com.ylfcf.ppp.util;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

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
	 */
	public static void statisticsOnPageStart(String mClassName) {
		// String contextStr = getRunningActivityName(context);
		MobclickAgent.onPageStart(mClassName);
	}

	/**
	 * 统计页面结束
	 * 
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

	/**
	 * 当应用在后台运行超过30秒（默认）再回到前端，将被认为是两个独立的session(启动)，
	 * 例如用户回到home，或进入其他程序，经过一段时间后再返回之前的应用。
	 * 此方法可以设置这个间隔（单位为毫秒）
	 * @param interval
	 */
	public static void setSessionContinueMillis(long interval){
		MobclickAgent.setSessionContinueMillis(interval);
	}

	/**
	 * 如果开发者调用Process.kill或者System.exit之类的方法杀死进程，
	 * 请务必在此之前调用本方法保存统计数据
	 * @param context
	 */
	public static void onKillProcess(Context context){
		MobclickAgent.onKillProcess(context);
	}
}
