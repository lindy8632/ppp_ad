package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.GiftInfo;
import com.ylfcf.ppp.entity.GiftPageInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 奖品列表
 * @author Mr.liu
 *
 */
public class JsonParseGiftPageInfo {
	private BaseInfo baseInfo;
	private GiftPageInfo pageInfo;
	private List<GiftInfo> giftInfoList;
	private List<String> rulesApp;

	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	private void parseRulesList(String data){
		rulesApp = new ArrayList<String>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				String s = jsonArray.getString(i);
				rulesApp.add(s);
			}
			giftInfo.setRulesAppList(rulesApp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 推广收益的列表
	 * @param data
	 */
	GiftInfo giftInfo = null;
	private void parseGiftList(String data) {
		giftInfoList = new ArrayList<GiftInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				giftInfo = (GiftInfo) MainJson
						.fromJson(GiftInfo.class, object);
				parseRulesList(giftInfo.getRules_app());
				giftInfoList.add(giftInfo);
			}
			pageInfo.setGiftList(giftInfoList);
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
			pageInfo = (GiftPageInfo) MainJson.fromJson(
					GiftPageInfo.class, object);
			parseGiftList(pageInfo.getList());
			baseInfo.setmGiftPageInfo(pageInfo);
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
			if(resultCode == 0){
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
		JsonParseGiftPageInfo jsonParse = new JsonParseGiftPageInfo();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
