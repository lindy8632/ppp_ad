package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.WDYChildRecordInfo;
import com.ylfcf.ppp.entity.WDYChildRecordPageInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;
/**
 * 
 * @author Mr.liu
 *
 */
public class JsonParseWDYChildRecordPageInfo {
	private BaseInfo baseInfo;
	private WDYChildRecordPageInfo pageInfo;
	private List<WDYChildRecordInfo> infoList;

	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	/**
	 * 推广收益的列表
	 * @param data
	 */
	private void parseWDYChildRecordList(String data) {
		infoList = new ArrayList<WDYChildRecordInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				WDYChildRecordInfo info = (WDYChildRecordInfo) MainJson
						.fromJson(WDYChildRecordInfo.class, object);
				infoList.add(info);
			}
			pageInfo.setWdyChildRecordList(infoList);
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
			pageInfo = (WDYChildRecordPageInfo) MainJson.fromJson(
					WDYChildRecordPageInfo.class, object);
			parseWDYChildRecordList(pageInfo.getList());
			baseInfo.setmWDYChildRecordPageInfo(pageInfo);
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
			if(resultCode == 0)
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
		JsonParseWDYChildRecordPageInfo jsonParse = new JsonParseWDYChildRecordPageInfo();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
