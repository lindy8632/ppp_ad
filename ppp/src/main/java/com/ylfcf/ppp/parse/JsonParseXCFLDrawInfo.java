package com.ylfcf.ppp.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.XCFLDrawInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;
/**
 * 17年领压岁钱 领奖接口返回的数据
 * @author Mr.liu
 *
 */
public class JsonParseXCFLDrawInfo {
	private BaseInfo baseInfo;
	private XCFLDrawInfo xcflDrawInfo;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	/**
	 * 解析msg字段
	 * @param result
	 * @throws Exception
	 */
	public void parseMsg(String result) throws Exception {
		JSONObject object = null;
		object = new JSONObject(result);
		if(object != null){
			xcflDrawInfo = (XCFLDrawInfo) MainJson.fromJson(XCFLDrawInfo.class, object);
			baseInfo.setXcflDrawInfo(xcflDrawInfo);
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
		JsonParseXCFLDrawInfo jsonParse = new JsonParseXCFLDrawInfo();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
