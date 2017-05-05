package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.InvestRecordPageInfo;
import com.ylfcf.ppp.util.MainJson;

/**
 * 投资记录列表
 * @author Administrator
 * 
 */
public class JsonParseInvestRecordList {
	private BaseInfo baseInfo;
	private InvestRecordPageInfo mInvestRecordPageInfo;
	private List<InvestRecordInfo> investRecordList;

	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	/**
	 * 解析投资记录
	 * @param data
	 */
	private void parseInvestRecordList(String data) {
		investRecordList = new ArrayList<InvestRecordInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				InvestRecordInfo info = (InvestRecordInfo) MainJson.fromJson(
						InvestRecordInfo.class, object);
				investRecordList.add(info);
			}
			mInvestRecordPageInfo.setInvestRecordList(investRecordList);
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
			mInvestRecordPageInfo = (InvestRecordPageInfo) MainJson.fromJson(
					InvestRecordPageInfo.class, object);
			parseInvestRecordList(mInvestRecordPageInfo.getList());
			baseInfo.setmInvestRecordPageInfo(mInvestRecordPageInfo);
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
			int resultCode = Integer.parseInt(baseInfo.getError_id());
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
		JsonParseInvestRecordList jsonParse = new JsonParseInvestRecordList();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}

}
