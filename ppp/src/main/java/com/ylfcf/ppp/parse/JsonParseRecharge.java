package com.ylfcf.ppp.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.RechargeOrderInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 充值
 * @author Administrator
 *
 */
public class JsonParseRecharge {
	private BaseInfo baseInfo;
	private RechargeOrderInfo orderInfo;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	/**
	 * 解析msg字段
	 * @param result
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private void parseMsg(String result) throws InstantiationException, IllegalAccessException{
		JSONObject object = null;
		try {
			object = new JSONObject(result);
		} catch (Exception e) {
		}
		if(object != null){
			orderInfo = (RechargeOrderInfo) MainJson.fromJson(RechargeOrderInfo.class, object);
			baseInfo.setRechargeOrderInfo(orderInfo);
		}
	}
	
	/**
	 * @param result
	 * @throws Exception
	 */
	public void parseMain(String result) throws Exception{
		JSONObject object = null;
		try {
			object = new JSONObject(result);
		} catch (Exception e) {
		}
		
		if(object != null) {
			baseInfo = (BaseInfo)MainJson.fromJson(BaseInfo.class, object);
			if(baseInfo != null){
				int resultCode = SettingsManager.getResultCode(baseInfo);
				if(resultCode == 0){
					parseMsg(baseInfo.getMsg());
				}
			}
		}
	}
	
	/**
	 * 解析调用接口
	 * @param result
	 * @return
	 * @throws JSONException
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static BaseInfo parseData(String result) throws Exception {
		JsonParseRecharge jsonParse = new JsonParseRecharge();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
