package com.ylfcf.ppp.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.PrizeActiveInfo;
import com.ylfcf.ppp.util.MainJson;

/**
 * 奖品对应的活动
 * @author Administrator
 *
 */
public class JsonParsePrizeActive {
	private BaseInfo baseInfo;
	private PrizeActiveInfo mPrizeActiveInfo;
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	/**
	 * 解析msg字段
	 * @param result
	 * @throws Exception
	 */
	public void parseMsg(String result) throws Exception{
		JSONObject object = null;
		try {
			object = new JSONObject(result);
		} catch (Exception e) {
		}
		
		if(object != null) {
			mPrizeActiveInfo = (PrizeActiveInfo)MainJson.fromJson(PrizeActiveInfo.class, object);
			baseInfo.setmPrizeActiveInfo(mPrizeActiveInfo);
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
			parseMsg(baseInfo.getMsg());
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
		JsonParsePrizeActive jsonParse = new JsonParsePrizeActive();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}

}
