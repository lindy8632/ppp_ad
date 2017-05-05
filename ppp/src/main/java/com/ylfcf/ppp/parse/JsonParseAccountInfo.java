package com.ylfcf.ppp.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.AccountTotalInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 账户总信息 --- 累计收益、累计投资等等
 * 
 * @author Administrator
 * 
 */
public class JsonParseAccountInfo {
	private BaseInfo baseInfo;
	private AccountTotalInfo accountTotalInfo;

	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	public void parseMsg(String result) throws Exception {
		JSONObject object = null;
		object = new JSONObject(result);
		if (object != null) {
			accountTotalInfo = (AccountTotalInfo) MainJson.fromJson(
					AccountTotalInfo.class, object);
			baseInfo.setAccountTotalInfo(accountTotalInfo);
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
		JsonParseAccountInfo jsonParse = new JsonParseAccountInfo();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}

}
