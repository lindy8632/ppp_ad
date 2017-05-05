package com.ylfcf.ppp.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.YXBUserAccountInfo;
import com.ylfcf.ppp.util.MainJson;

/**
 * 元信宝用户中心
 * 
 * @author Administrator
 * 
 */
public class JsonParseYXBUserCenter {
	private BaseInfo baseInfo;
	private YXBUserAccountInfo yxbUserAccountInfo;

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
			yxbUserAccountInfo = (YXBUserAccountInfo) MainJson.fromJson(
					YXBUserAccountInfo.class, object);
			baseInfo.setYxbUserAccountInfo(yxbUserAccountInfo);
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
			parseMsg(baseInfo.getMsg());
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
		JsonParseYXBUserCenter jsonParse = new JsonParseYXBUserCenter();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}

}
