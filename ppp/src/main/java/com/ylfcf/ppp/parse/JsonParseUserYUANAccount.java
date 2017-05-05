package com.ylfcf.ppp.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserYUANAccountInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 用户的元金币账户
 * 
 * @author Administrator
 * 
 */
public class JsonParseUserYUANAccount {
	private BaseInfo baseInfo;
	private UserYUANAccountInfo yuanAccountInfo;

	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	/**
	 * 解析msg字段
	 * @param result
	 * @throws Exception
	 */
	public void parseMsg(String result) throws Exception {
		JSONObject object = null;
		object = new JSONObject(result);
		if (object != null) {
			yuanAccountInfo = (UserYUANAccountInfo) MainJson.fromJson(
					UserYUANAccountInfo.class, object);
			baseInfo.setYuanAccountInfo(yuanAccountInfo);
		}
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
			baseInfo = (BaseInfo) MainJson.fromJson(BaseInfo.class, object);
			int resultCode = SettingsManager.getResultCode(baseInfo);
			if (resultCode == 0) {
				parseMsg(baseInfo.getMsg());
			}
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
	public static BaseInfo parseData(String result) throws Exception {
		JsonParseUserYUANAccount jsonParse = new JsonParseUserYUANAccount();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
