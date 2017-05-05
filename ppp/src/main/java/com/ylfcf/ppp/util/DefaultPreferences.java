package com.ylfcf.ppp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;

import com.ylfcf.ppp.ui.YLFApplication;

/**
 * Application setting is stored in the default shared preference file.
 * This class is used to management the settings(Add/Remove/Change etc.)
 * of application.
 * 
 * @author Waggoner.wang
 */
public abstract class DefaultPreferences {
	
	private final static String DEBUG_LOG_ENABLE	= "base.util.debug_enable";
	
	private static int mVersionCode;
	private static String mVersionName;

	public DefaultPreferences() {
		init();
	}
	
	private static void init() {
		try {
			PackageInfo packageInfo = YLFApplication.getApplication().getPackageManager()
					.getPackageInfo(YLFApplication.getApplication().getPackageName(),
							PackageManager.GET_ACTIVITIES);
			mVersionCode	= packageInfo.versionCode;
			mVersionName	= packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
	}

	public static int getInt(Context context,String key, int def) {
		return getSharedPreferences(context).getInt(key, def);
	}

	public static boolean getBoolean(Context context,String key, boolean def) {
		return getSharedPreferences(context).getBoolean(key, def);
	}

	public static void setBoolean(Context context,String key, boolean value) {
		Editor editor = getSharedPreferences(context).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static String getString(Context context,String key, String def) {
		return getSharedPreferences(context).getString(key, def);
	}

	public static void setString(Context context,String key, String value) {
		Editor editor = getSharedPreferences(context).edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static long getLong(Context context,String key, long def) {
		return getSharedPreferences(context).getLong(key, def);
	}

	public static void setLong(Context context,String key, long value) {
		Editor editor = getSharedPreferences(context).edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	public static void clearAll(Context context){
		Editor editor = getSharedPreferences(context).edit();
		editor.clear().commit();
	}

	public static int getVersionCode() {
		return mVersionCode;
	}
	
	public static String getVersionName() {
		return mVersionName;
	}

}
