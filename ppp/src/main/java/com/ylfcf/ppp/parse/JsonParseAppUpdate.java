package com.ylfcf.ppp.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.AppInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.util.MainJson;

/**
 * 版本更新
 * 
 * @author Administrator
 * 
 */
public class JsonParseAppUpdate {
	private AppInfo appInfo;

	public AppInfo getBaseInfo() {
		return appInfo;
	}

	/**
	 * @param result
	 * @throws Exception
	 */
	public void parseMain(String result) throws Exception {
		JSONObject object = null;
		try {
			object = new JSONObject(result);
		} catch (Exception e) {
		}

		if (object != null) {
			appInfo = (AppInfo) MainJson.fromJson(AppInfo.class, object);
		}
	}

	/**
	 * 解析调用接口
	 * 
	 * @param result
	 * @return
	 * @throws JSONException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static AppInfo parseData(String result) throws Exception {
		JsonParseAppUpdate jsonParse = new JsonParseAppUpdate();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
