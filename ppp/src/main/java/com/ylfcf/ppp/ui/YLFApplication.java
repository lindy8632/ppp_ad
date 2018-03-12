package com.ylfcf.ppp.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.ylfcf.ppp.util.ImageLoaderManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class YLFApplication extends android.app.Application {
	private List<Activity> activityList;
	private static YLFApplication theApplication;

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		//阿里云热修复
		SophixManager.getInstance().setContext(this)
				.setAppVersion(String.valueOf(Util.getClientVersion(this)))
				.setAesKey(null)
				.setEnableDebug(true)
				.setPatchLoadStatusStub(new PatchLoadStatusListener() {
					@Override
					public void onLoad(int mode, int code, String info, int handlePatchVersion) {
						//补丁加载回调通知
						if(code == PatchStatus.CODE_LOAD_SUCCESS){
							//补丁加载成功
						}else if(code == PatchStatus.CODE_LOAD_RELAUNCH){
							//表明补丁生效需要重启
							//建议：用户可以监听进入后台事件，然后调用KillProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
						}else{
							//其他错误信息 查看PatchStatus类说明
						}
					}
				}).initialize();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		theApplication = this;
		activityList = new ArrayList<Activity>();
		SophixManager.getInstance().queryAndLoadNewPatch();
		int pid = android.os.Process.myPid();
		YLFLogger.d("pid application onCreate():"+pid);
		String processNameString = "";
		ActivityManager mActivityManager = (ActivityManager)this.getSystemService(getApplicationContext().ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				processNameString = appProcess.processName;
			}
		}
		if("com.ylfcf.ppp".equals(processNameString)){
			init();
		}else{

		}
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.register(new IUmengRegisterCallback() {
			@Override
			public void onSuccess(String deviceToken) {
				YLFLogger.d("deviceToken:"+deviceToken);
			}

			@Override
			public void onFailure(String s, String s1) {

			}
		});
	}

	private void init(){
		UMengStatistics.statisticsInit();
		UMShareAPI.get(this);
		PlatformConfig.setWeixin("wx9b6d21b05d725f48",
				"a6c9bdc0cf7eda498049a9f1b9fbb380");
		PlatformConfig.setSinaWeibo("2704475990",
				"b9fba6681da2f894a6f9a2705b38dd28",
				"http://sns.whalecloud.com/sina2/callback");
		PlatformConfig.setQQZone("1105044430", "gAWcQhAJpPebuG7y");
		ImageLoaderManager.configurationImageLoader(this);
	}

	/**
	 */
	private static void initialize() {
		theApplication = new YLFApplication();
		theApplication.onCreate();
	}

	/**
	 *
	 * @return
	 */
	public static YLFApplication getApplication() {
		if (theApplication == null)
			initialize();
		return theApplication;
	}

	public MainFragmentActivity getMainFragmentActivity() {
		MainFragmentActivity mainActivity = null;
		for (Activity activity : activityList) {
			if (activity instanceof MainFragmentActivity) {
				mainActivity = (MainFragmentActivity) activity;
				break;
			}
		}
		return mainActivity;
	}

	public InvitateActivity getInvitateActivity(){
		InvitateActivity invitateActivity = null;
		for (Activity activity : activityList) {
			if (activity instanceof InvitateActivity) {
				invitateActivity = (InvitateActivity) activity;
				break;
			}
		}
		return invitateActivity;
	}

	/**
	 * @return
	 */
	public boolean getActivitysRegionActivity(){
		boolean flag = false;
		for (Activity activity : activityList) {
			if (activity instanceof ActivitysRegionActivity) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 *
	 * @return
	 */
	public static YLFApplication getContext() {
		return theApplication;
	}

	public void removeActivity(Activity activity) {
		activityList.remove(activity);
	}

	public void addActivity(Activity activity) {
		if (!activityList.contains(activity)) {
			activityList.add(activity);
		}
	}

	public void finishAllActivity() {
		for (Activity activity : activityList) {
			if (null != activity) {
				activity.finish();
			}
		}
	}

	/**
	 */
	public void finishAllActivityExceptMain() {
		for (Activity activity : activityList) {
			if (null != activity && !(activity instanceof MainFragmentActivity)) {
				activity.finish();
			}
		}
	}

}