package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.WithdrawOrderInfo;
import com.ylfcf.ppp.entity.WithdrawOrderPageInfo;
import com.ylfcf.ppp.util.MainJson;

/**
 * 提现列表
 * 
 * @author Administrator
 * 
 */
public class JsonParseWithdrawList {
	private BaseInfo baseInfo;
	private WithdrawOrderPageInfo orderPageInfo;
	private List<WithdrawOrderInfo> mWithdrawInfoList;

	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	/**
	 * 解析产品列表
	 * @param data
	 */
	private void parseProductList(String data) {
		mWithdrawInfoList = new ArrayList<WithdrawOrderInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				WithdrawOrderInfo info = (WithdrawOrderInfo) MainJson.fromJson(
						WithdrawOrderInfo.class, object);
				mWithdrawInfoList.add(info);
			}
			orderPageInfo.setOrderList(mWithdrawInfoList);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 解析Msg字段
	 * @param result
	 */
	public void parseMsg(String result) {
		JSONObject object = null;
		try {
			object = new JSONObject(result);
			if (object != null) {
				orderPageInfo = (WithdrawOrderPageInfo) MainJson.fromJson(
						WithdrawOrderPageInfo.class, object);
				baseInfo.setWithdrawOrderPageInfo(orderPageInfo);
				parseProductList(orderPageInfo.getList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param result
	 * @throws Exception
	 */
	public void parseMain(String result) {
		JSONObject object = null;
		try {
			object = new JSONObject(result);
			if (object != null) {
				baseInfo = (BaseInfo) MainJson.fromJson(BaseInfo.class, object);
				parseMsg(baseInfo.getMsg());
			}
		} catch (Exception e) {
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
		JsonParseWithdrawList jsonParse = new JsonParseWithdrawList();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}

}
