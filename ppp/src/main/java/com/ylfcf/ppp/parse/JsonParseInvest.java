package com.ylfcf.ppp.parse;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestResultInfo;
import com.ylfcf.ppp.entity.InvestStatus;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 投资
 * @author Administrator
 *
 */
public class JsonParseInvest {
	private BaseInfo baseInfo;
	private InvestResultInfo mInvestResultInfo;
	private InvestStatus mInvestStatus;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	private void parseInvestStatus(String data){
		JSONObject object = null;
		try {
			object = new JSONObject(data);
			if(object != null) {
				mInvestStatus = (InvestStatus)MainJson.fromJson(InvestStatus.class, object);
				mInvestResultInfo.setmInvestStatus(mInvestStatus);
			}
		} catch (Exception e) {
		}
	}

	private void parseResult(String data){
		JSONObject object = null;
		try {
			object = new JSONObject(data);
			if(object != null) {
				mInvestResultInfo = (InvestResultInfo)MainJson.fromJson(InvestResultInfo.class, object);
				parseInvestStatus(mInvestResultInfo.getDaily_prize_res());
				baseInfo.setmInvestResultInfo(mInvestResultInfo);
			}
		} catch (Exception e) {
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
			int resultCode = SettingsManager.getResultCode(baseInfo);
			if(resultCode == 0){
				parseResult(baseInfo.getMsg());
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
		JsonParseInvest jsonParse = new JsonParseInvest();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
