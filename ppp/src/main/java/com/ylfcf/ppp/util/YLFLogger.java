package com.ylfcf.ppp.util;

import android.util.Log;
/**
 * 系统日志工具类
 * @author Mr.liu
 *
 */
public class YLFLogger {
	private static final boolean DEBUG = false;

	/**
	 * 运行时日志，打包之后不会显示
	 * @param msg
	 */
	public static void d(String msg) {
		if (DEBUG) {
			Log.d("yxb", msg);
		}
	}

	public static void d(String tag, String msg) {
		if (DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void i(String msg) {
		if (DEBUG) {
			Log.i("yxb", msg);
		}
	}
	
	public static void i(String tag,String msg) {
		if (DEBUG) {
			Log.i(tag, msg);
		}
	}
}
