package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.YJHBiteInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 获取产品信息
 * 
 * @author Administrator
 * 
 */
public class JsonParseProductInfo {

	private BaseInfo baseInfo;
	private ProductInfo productInfo;
	private List<YJHBiteInfo> yjhBiteList;

	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	public void parseBite(String list){
		yjhBiteList = new ArrayList<YJHBiteInfo>();
		try {
			JSONArray jsonArray = new JSONArray(list);
			int size = jsonArray.length();
			for(int i=0;i<size;i++){
				JSONObject object = jsonArray.getJSONObject(i);
				YJHBiteInfo info = (YJHBiteInfo) MainJson.fromJson(YJHBiteInfo.class, object);
				yjhBiteList.add(info);
			}
			productInfo.setYjhBiteList(yjhBiteList);
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
			if (object != null) {
				productInfo = (ProductInfo) MainJson.fromJson(ProductInfo.class, object);
				if(productInfo.getList() != null){
					parseBite(productInfo.getList());
				}
				baseInfo.setmProductInfo(productInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param result
	 * @throws Exception
	 */
	public void parseMain(String result) {
		JSONObject object = null;
		try {
			object = new JSONObject(result);
			if (object != null) {
				baseInfo = (BaseInfo) MainJson.fromJson(BaseInfo.class, object);
				int resultCode = SettingsManager.getResultCode(baseInfo);
				if(resultCode == 0){
					parseMsg(baseInfo.getMsg());
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 解析调用接口
	 * 
	 * @param result
	 * @return
	 * @throws JSONException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static BaseInfo parseData(String result) throws Exception {
		JsonParseProductInfo jsonParse = new JsonParseProductInfo();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
