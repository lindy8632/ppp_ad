package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ExtensionNewInfo;
import com.ylfcf.ppp.entity.ExtensionNewPageInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 推广收益 -- 新的接口
 * @author Mr.liu
 *
 */
public class JsonParseNewExtensionPageInfo {
	private BaseInfo baseInfo;
	private ExtensionNewPageInfo pageInfo;
	private List<ExtensionNewInfo> infoList;

	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	/**
	 * 推广收益的列表
	 * @param data
	 */
	private void parseExtensionInfoList(String data) {
		infoList = new ArrayList<ExtensionNewInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				ExtensionNewInfo info = (ExtensionNewInfo) MainJson
						.fromJson(ExtensionNewInfo.class, object);
				infoList.add(info);
			}
			pageInfo.setExtensionList(infoList);
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
			pageInfo = (ExtensionNewPageInfo) MainJson.fromJson(
					ExtensionNewPageInfo.class, object);
			parseExtensionInfoList(pageInfo.getList());
			baseInfo.setExtensionNewPageInfo(pageInfo);
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
			if(resultCode == 1 || resultCode == -1)
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
		JsonParseNewExtensionPageInfo jsonParse = new JsonParseNewExtensionPageInfo();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
