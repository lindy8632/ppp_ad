package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.HDPrizePageInfo;
import com.ylfcf.ppp.entity.PrizeInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

public class JsonParseHDPrizeList {
	private BaseInfo baseInfo;
	private HDPrizePageInfo mHDPrizePageInfo;
	private List<PrizeInfo> prizeList;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	/*
	 * 解析list列表
	 */
	public void parsePageInfo(String data) throws Exception{
		prizeList = new ArrayList<PrizeInfo>();
		JSONArray array = new JSONArray(data);
		int size = array.length();
		for(int i = 0;i < size;i++){
			JSONObject object = array.getJSONObject(i);
			PrizeInfo info = (PrizeInfo) MainJson.fromJson(PrizeInfo.class, object);
			prizeList.add(info);
		}
		mHDPrizePageInfo.setPrizeList(prizeList);
	}
	
	/*
	 * 解析msg字段
	 */
	public void parseMsg(String result) throws Exception {
		JSONObject object = null;
		object = new JSONObject(result);
		if(object != null){
			mHDPrizePageInfo = (HDPrizePageInfo) MainJson.fromJson(HDPrizePageInfo.class, object);
			parsePageInfo(mHDPrizePageInfo.getList());
			baseInfo.setmHDPrizePageInfo(mHDPrizePageInfo);
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
		JsonParseHDPrizeList jsonParse = new JsonParseHDPrizeList();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}

}
