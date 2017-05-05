package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ExtensionIncomeInfo;
import com.ylfcf.ppp.entity.ExtensionPageInfo;
import com.ylfcf.ppp.entity.ExtensionUserInfo;
import com.ylfcf.ppp.util.MainJson;

/**
 * 推广收益
 * 
 * @author Administrator
 * 
 */
public class JsonParseExtensionPageInfo {
	private BaseInfo baseInfo;
	private ExtensionPageInfo pageInfo;
	private List<ExtensionIncomeInfo> incomeInfoList;
	private List<ExtensionUserInfo> userInfoList;

	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	/**
	 * 推荐人的列表
	 * @param data
	 */
	private void parseExtensionUserList(String data) {
		userInfoList = new ArrayList<ExtensionUserInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				ExtensionUserInfo info = (ExtensionUserInfo) MainJson.fromJson(
						ExtensionUserInfo.class, object);
				userInfoList.add(info);
			}
			pageInfo.setUserInfoList(userInfoList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 推广收益的列表
	 * @param data
	 */
	private void parseExtensionIncomeList(String data) {
		incomeInfoList = new ArrayList<ExtensionIncomeInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				ExtensionIncomeInfo info = (ExtensionIncomeInfo) MainJson
						.fromJson(ExtensionIncomeInfo.class, object);
				incomeInfoList.add(info);
			}
			pageInfo.setIncomeInfoList(incomeInfoList);
		} catch (Exception e) {
			e.printStackTrace();
		}

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
			pageInfo = (ExtensionPageInfo) MainJson.fromJson(
					ExtensionPageInfo.class, object);
			parseExtensionIncomeList(pageInfo.getList());
			parseExtensionUserList(pageInfo.getUser_list());
			baseInfo.setExtensionPageInfo(pageInfo);

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
		JsonParseExtensionPageInfo jsonParse = new JsonParseExtensionPageInfo();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}

}
