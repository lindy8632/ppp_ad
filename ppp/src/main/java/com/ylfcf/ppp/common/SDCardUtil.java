package com.ylfcf.ppp.common;

import android.os.Environment;

/**
 * sdk的工具类
 * 
 * @author Administrator
 * 
 */
public class SDCardUtil {
	/**
	 * 检测sd卡是否可用
	 * 
	 * @return
	 */
	public static boolean checkSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取sd卡路径
	 * 
	 * @return
	 */
	public static String getRootPath() {
		if (checkSDCard()) {
			return Environment.getExternalStorageDirectory().getPath();
		}
		return "";
	}
}
