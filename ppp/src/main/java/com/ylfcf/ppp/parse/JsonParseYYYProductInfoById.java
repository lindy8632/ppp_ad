package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProductPageInfo;
import com.ylfcf.ppp.util.MainJson;
/**
 * 根据id获取元月盈产品详情
 * @author Mr.liu
 */
public class JsonParseYYYProductInfoById {
	private BaseInfo baseInfo;
	private ProductInfo productInfo;
	private ProductPageInfo pageInfo;
	private List<ProductInfo> productList;

	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	/**
	 * 解析msg字段
	 * @param result
	 */
	public void parseMsg(String result) {
		JSONObject object = null;
		productList = new ArrayList<ProductInfo>();
		pageInfo = new ProductPageInfo();
		try {
			object = new JSONObject(result);
			if (object != null) {
				productInfo = (ProductInfo) MainJson.fromJson(ProductInfo.class, object);
				productList.add(productInfo);
				pageInfo.setProductList(productList);
				baseInfo.setProductPageInfo(pageInfo);
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
				parseMsg(baseInfo.getMsg());
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
		JsonParseYYYProductInfoById jsonParse = new JsonParseYYYProductInfoById();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
