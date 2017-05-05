package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BannerPageInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ExtensionIncomeInfo;
import com.ylfcf.ppp.entity.ExtensionPageInfo;
import com.ylfcf.ppp.entity.ExtensionUserInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * Banner 
 * @author Administrator
 *
 */
public class JsonParseBanner {
	private BaseInfo baseInfo;
	private BannerPageInfo pageInfo;
	private List<BannerInfo> bannerInfoList;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	private void parseList(String data){
		bannerInfoList = new ArrayList<BannerInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for(int i=0;i<size;i++){
				JSONObject object = jsonArray.getJSONObject(i);
				BannerInfo info = (BannerInfo) MainJson.fromJson(BannerInfo.class, object);
				bannerInfoList.add(info);
			}
			pageInfo.setBannerList(bannerInfoList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void parsePageInfo(String data){
		JSONObject object = null;
		try {
			object = new JSONObject(data);
		} catch (Exception e) {
		}
		
		if(object != null) {
			try {
				pageInfo = (BannerPageInfo)MainJson.fromJson(BannerPageInfo.class, object);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			parseList(pageInfo.getList());
			baseInfo.setmBannerPageInfo(pageInfo);
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
		JsonParseBanner jsonParse = new JsonParseBanner();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}

}
