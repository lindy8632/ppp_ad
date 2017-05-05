package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.FundsDetailsInfo;
import com.ylfcf.ppp.entity.FundsDetailsPageInfo;
import com.ylfcf.ppp.util.MainJson;

/**
 * 资金明细列表
 * 
 * @author Administrator
 * 
 */
public class JsonParseFundsDetailsList {
	private BaseInfo baseInfo;
	private FundsDetailsPageInfo pageInfo;
	private List<FundsDetailsInfo> fundsDetailsList;

	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	/**
	 * 解析资金明细列表
	 * @param data
	 */
	private void parseFundsDetailsList(String data) {
		fundsDetailsList = new ArrayList<FundsDetailsInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				FundsDetailsInfo info = (FundsDetailsInfo) MainJson.fromJson(
						FundsDetailsInfo.class, object);
				fundsDetailsList.add(info);
			}
			pageInfo.setFundsDetailsList(fundsDetailsList);
		} catch (Exception e) {
			e.printStackTrace();
		}

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
			pageInfo = (FundsDetailsPageInfo) MainJson.fromJson(
					FundsDetailsPageInfo.class, object);
			parseFundsDetailsList(pageInfo.getList());
			baseInfo.setFundsDetailsPageInfo(pageInfo);
		}
	}

	/**
	 * 开始解析
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
		JsonParseFundsDetailsList jsonParse = new JsonParseFundsDetailsList();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}

}
