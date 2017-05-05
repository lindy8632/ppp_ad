package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.YXBRedeemRecordInfo;
import com.ylfcf.ppp.entity.YXBRedeemRecordPageInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 元信宝  ---  赎回记录
 * @author Administrator
 *
 */
public class JsonParseYXBRedeemRecord {
	private BaseInfo baseInfo;
	private YXBRedeemRecordPageInfo pageInfo;
	private List<YXBRedeemRecordInfo> yxbRecordList;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	/**
	 * 解析列表
	 * @param data
	 */
	private void parseList(String data){
		yxbRecordList = new ArrayList<YXBRedeemRecordInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for(int i=0;i<size;i++){
				JSONObject object = jsonArray.getJSONObject(i);
				YXBRedeemRecordInfo info = (YXBRedeemRecordInfo) MainJson.fromJson(YXBRedeemRecordInfo.class, object);
				yxbRecordList.add(info);
			}
			pageInfo.setYxbRecordList(yxbRecordList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 计息分页对象
	 * @param data
	 */
	private void parsePageInfo(String data){
		JSONObject object = null;
		try {
			object = new JSONObject(data);
		} catch (Exception e) {
		}
		
		if(object != null) {
			try {
				pageInfo = (YXBRedeemRecordPageInfo)MainJson.fromJson(YXBRedeemRecordPageInfo.class, object);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			parseList(pageInfo.getList());
			baseInfo.setYxbRedeemRecordPageInfo(pageInfo);
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
				parsePageInfo(baseInfo.getMsg());
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
		JsonParseYXBRedeemRecord jsonParse = new JsonParseYXBRedeemRecord();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
