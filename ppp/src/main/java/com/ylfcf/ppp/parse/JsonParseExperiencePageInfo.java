package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.TYJInfo;
import com.ylfcf.ppp.entity.TYJPageInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 解析体验金
 * @author Mr.liu
 *
 */
public class JsonParseExperiencePageInfo {
	private BaseInfo baseInfo;
	private TYJPageInfo pageInfo;
	private List<TYJInfo> experienceList;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	/**
	 * 解析体验金列表
	 * @param data
	 */
	private void parseExperienceList(String data){
		experienceList = new ArrayList<TYJInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for(int i=0;i<size;i++){
				JSONObject object = jsonArray.getJSONObject(i);
				TYJInfo info = (TYJInfo) MainJson.fromJson(TYJInfo.class, object);
				experienceList.add(info);
			}
			pageInfo.setTyjList(experienceList);
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
			pageInfo = (TYJPageInfo) MainJson.fromJson(TYJPageInfo.class, object);
			parseExperienceList(pageInfo.getList());
			baseInfo.setmTYJPageInfo(pageInfo);
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
		JsonParseExperiencePageInfo jsonParse = new JsonParseExperiencePageInfo();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}

}
