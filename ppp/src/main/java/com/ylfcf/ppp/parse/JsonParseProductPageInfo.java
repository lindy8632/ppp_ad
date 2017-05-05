package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProductPageInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 获取产品列表
 * @author Administrator
 *
 */
public class JsonParseProductPageInfo {
	private BaseInfo baseInfo;
	private ProductPageInfo mProductPageInfo;
	private List<ProductInfo> mProductList;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	/**
	 * 解析产品列表
	 * @param data
	 */
	private void parseProductList(String data){
		mProductList = new ArrayList<ProductInfo>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			int size = jsonArray.length();
			for(int i=0;i<size;i++){
				JSONObject object = jsonArray.getJSONObject(i);
				ProductInfo info = (ProductInfo) MainJson.fromJson(ProductInfo.class, object);
				mProductList.add(info);
			}
			mProductPageInfo.setProductList(mProductList);
			baseInfo.setProductPageInfo(mProductPageInfo);
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
				mProductPageInfo = (ProductPageInfo) MainJson.fromJson(ProductPageInfo.class, object);
				parseProductList(mProductPageInfo.getList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 开始解析
	 * @param result
	 * @throws Exception
	 */
	public void parseMain(String result){
		JSONObject object = null;
		try {
			object = new JSONObject(result);
			if(object != null){
				baseInfo = (BaseInfo) MainJson.fromJson(BaseInfo.class, object);
				if(SettingsManager.getResultCode(baseInfo) == 0){
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
		JsonParseProductPageInfo jsonParse = new JsonParseProductPageInfo();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}

}
