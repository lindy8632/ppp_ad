package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.RechargeRecordInfo;
import com.ylfcf.ppp.entity.RechargeRecordPageInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;
/**
 * 充值记录
 * @author Mr.liu
 */
public class JsonParseRechargeRecordPageInfo {
	private BaseInfo baseInfo;
	private RechargeRecordPageInfo pageInfo;
	private List<RechargeRecordInfo> rechargeRecordList;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	/**
	 * 解析体验金列表
	 * @param data
	 */
	private void parseRechargeRecordList(String data){
		rechargeRecordList = new ArrayList<RechargeRecordInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for(int i=0;i<size;i++){
				JSONObject object = jsonArray.getJSONObject(i);
				RechargeRecordInfo info = (RechargeRecordInfo) MainJson.fromJson(RechargeRecordInfo.class, object);
				rechargeRecordList.add(info);
			}
			pageInfo.setRechargeRecordList(rechargeRecordList);
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
		if(object != null){
			pageInfo = (RechargeRecordPageInfo) MainJson.fromJson(RechargeRecordPageInfo.class, object);
			parseRechargeRecordList(pageInfo.getList());
			baseInfo.setmRechargeRecordPageInfo(pageInfo);
		}
	}
	
	/**
	 * 开始解析...
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
		JsonParseRechargeRecordPageInfo jsonParse = new JsonParseRechargeRecordPageInfo();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
