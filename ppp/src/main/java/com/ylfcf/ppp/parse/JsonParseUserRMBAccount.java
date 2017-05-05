package com.ylfcf.ppp.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 用户的人民币账户
 * 
 * @author Administrator
 * 
 */
public class JsonParseUserRMBAccount {
	private BaseInfo baseInfo;
	private UserRMBAccountInfo rmbAccountInfo;

	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	/**
	 * 解析Msg字段
	 * @param result
	 * @throws Exception
	 */
	public void parseMsg(String result) throws Exception {
		JSONObject object = null;
		object = new JSONObject(result);
		if (object != null) {
			rmbAccountInfo = (UserRMBAccountInfo) MainJson.fromJson(
					UserRMBAccountInfo.class, object);
			baseInfo.setRmbAccountInfo(rmbAccountInfo);
		}
	}

	/**
	 * 开始解析。。
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
		JsonParseUserRMBAccount jsonParse = new JsonParseUserRMBAccount();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
