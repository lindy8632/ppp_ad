package com.ylfcf.ppp.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.RechargeOrderInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;
/**
 * 发送充值短信验证码
 * @author Mr.liu
 *
 */
public class JsonParseSendRechargeSms {
	private BaseInfo baseInfo;
	private RechargeOrderInfo rechargeOrderInfo;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	/**
	 * 解析Msg字段
	 * @param result
	 * @throws Exception
	 */
	public void parseMsg(String result) throws Exception {
		JSONObject object = null;
		object = new JSONObject(result);
		if(object != null){
			rechargeOrderInfo = (RechargeOrderInfo) MainJson.fromJson(RechargeOrderInfo.class, object);
			baseInfo.setRechargeOrderInfo(rechargeOrderInfo);
		}
	}
	
	/**
	 * 开始解析
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
			int resultCode = SettingsManager.getResultCode(baseInfo);
			if(resultCode == 0){
				parseMsg(baseInfo.getMsg());
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
		JsonParseSendRechargeSms jsonParse = new JsonParseSendRechargeSms();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
