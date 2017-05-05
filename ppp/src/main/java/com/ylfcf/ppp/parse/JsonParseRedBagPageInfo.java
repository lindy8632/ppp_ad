package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProductPageInfo;
import com.ylfcf.ppp.entity.RedBagInfo;
import com.ylfcf.ppp.entity.RedBagPageInfo;
import com.ylfcf.ppp.util.MainJson;

/**
 * 我的红包
 * @author Administrator
 *
 */
public class JsonParseRedBagPageInfo {
	private BaseInfo baseInfo;
	private RedBagPageInfo mRedBagPageInfo;
	private List<RedBagInfo> redBagList;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	/**
	 * 解析列表
	 * @param data
	 */
	private void parseRedbagList(String data){
		redBagList = new ArrayList<RedBagInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for(int i=0;i<size;i++){
				JSONObject object = jsonArray.getJSONObject(i);
				RedBagInfo info = (RedBagInfo) MainJson.fromJson(RedBagInfo.class, object);
				redBagList.add(info);
			}
			mRedBagPageInfo.setRedbagList(redBagList);
			baseInfo.setmRedBagPageInfo(mRedBagPageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 解析msg字段
	 * @param result
	 */
	public void parseMsg(String result) {
		JSONObject object = null;
		try {
			object = new JSONObject(result);
			if(object != null){
				mRedBagPageInfo = (RedBagPageInfo) MainJson.fromJson(RedBagPageInfo.class, object);
				parseRedbagList(mRedBagPageInfo.getList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param result
	 * @throws Exception
	 */
	public void parseMain(String result){
		JSONObject object = null;
		try {
			object = new JSONObject(result);
			if(object != null){
				baseInfo = (BaseInfo) MainJson.fromJson(BaseInfo.class, object);
				parseMsg(baseInfo.getMsg());
			}
		} catch (Exception e) {
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
		JsonParseRedBagPageInfo jsonParse = new JsonParseRedBagPageInfo();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
