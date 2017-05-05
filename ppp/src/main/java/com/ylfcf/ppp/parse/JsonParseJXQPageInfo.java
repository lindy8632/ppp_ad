package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.ArticlePageInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.JiaxiquanInfo;
import com.ylfcf.ppp.entity.JiaxiquanPageInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 加息券
 * @author Mr.liu
 *
 */
public class JsonParseJXQPageInfo {
	private BaseInfo baseInfo;
	private JiaxiquanPageInfo mJiaxiquanPageInfo;
	private List<JiaxiquanInfo> infoList;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	/*
	 * 解析list列表
	 */
	public void parsePageInfo(String data) throws Exception{
		mJiaxiquanPageInfo = new JiaxiquanPageInfo();
		infoList = new ArrayList<JiaxiquanInfo>();
		JSONArray array = new JSONArray(data);
		int size = array.length();
		for(int i = 0;i < size;i++){
			JSONObject object = array.getJSONObject(i);
			JiaxiquanInfo info = (JiaxiquanInfo) MainJson.fromJson(JiaxiquanInfo.class, object);
			infoList.add(info);
		}
		mJiaxiquanPageInfo.setInfoList(infoList);
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
		baseInfo.setmJiaxiquanPageInfo(mJiaxiquanPageInfo);
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
		JsonParseJXQPageInfo jsonParse = new JsonParseJXQPageInfo();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
