package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.YXBInvestRecordInfo;
import com.ylfcf.ppp.entity.YXBInvestRecordPageInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 元信宝认购记录
 * @author Administrator
 *
 */
public class JsonParseYXBInvestRecord {
	private BaseInfo baseInfo;
	private YXBInvestRecordPageInfo pageInfo;
	private List<YXBInvestRecordInfo> yxbRecordList;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	/**
	 * 解析产品列表
	 * @param data
	 */
	private void parseList(String data){
		yxbRecordList = new ArrayList<YXBInvestRecordInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for(int i=0;i<size;i++){
				JSONObject object = jsonArray.getJSONObject(i);
				YXBInvestRecordInfo info = (YXBInvestRecordInfo) MainJson.fromJson(YXBInvestRecordInfo.class, object);
				yxbRecordList.add(info);
			}
			pageInfo.setYxbInvestRecordList(yxbRecordList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 解析分页对象
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
				pageInfo = (YXBInvestRecordPageInfo)MainJson.fromJson(YXBInvestRecordPageInfo.class, object);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			parseList(pageInfo.getList());
			baseInfo.setYxbInvestRecordPageInfo(pageInfo);
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
		JsonParseYXBInvestRecord jsonParse = new JsonParseYXBInvestRecord();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
