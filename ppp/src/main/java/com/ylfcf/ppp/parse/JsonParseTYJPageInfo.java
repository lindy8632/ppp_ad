package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProductPageInfo;
import com.ylfcf.ppp.entity.TYJInfo;
import com.ylfcf.ppp.entity.TYJPageInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 体验金
 * @author Administrator
 *
 */
public class JsonParseTYJPageInfo {
	private BaseInfo baseInfo;
	private TYJPageInfo mTYJPageInfo;
	private List<TYJInfo> tyjList;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	/**
	 * 解析体验金列表
	 * @param data
	 */
	private void parseTYJList(String data){
		tyjList = new ArrayList<TYJInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for(int i=0;i<size;i++){
				JSONObject object = jsonArray.getJSONObject(i);
				TYJInfo info = (TYJInfo) MainJson.fromJson(TYJInfo.class, object);
				tyjList.add(info);
			}
			mTYJPageInfo.setTyjList(tyjList);
			baseInfo.setmTYJPageInfo(mTYJPageInfo);
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
				mTYJPageInfo = (TYJPageInfo) MainJson.fromJson(TYJPageInfo.class, object);
				parseTYJList(mTYJPageInfo.getList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 开始解析..
	 * @param result
	 * @throws Exception
	 */
	public void parseMain(String result){
		JSONObject object = null;
		try {
			object = new JSONObject(result);
			if(object != null){
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
	 * @param result
	 * @return
	 * @throws JSONException
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static BaseInfo parseData(String result) throws Exception {
		JsonParseTYJPageInfo jsonParse = new JsonParseTYJPageInfo();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
