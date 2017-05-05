package com.ylfcf.ppp.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.ArticleInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.util.MainJson;

/**
 * 活动公告
 * 
 * @author Administrator
 * 
 */
public class JsonParseArticle {
	private BaseInfo baseInfo;
	private ArticleInfo mArticleInfo;

	public BaseInfo getBaseInfo() {
		return baseInfo;
	}

	/**
	 * 解析msg字段
	 * @param result
	 * @throws Exception
	 */
	public void parseMsg(String result) throws Exception {
		JSONObject object = null;
		object = new JSONObject(result);
		if (object != null) {
			mArticleInfo = (ArticleInfo) MainJson.fromJson(ArticleInfo.class,
					object);
			baseInfo.setmArticleInfo(mArticleInfo);

		}
	}

	/**
	 * 开始解析
	 * @param result
	 * @throws Exception
	 */
	public void parseMain(String result) throws Exception {
		JSONObject object = null;
		try {
			object = new JSONObject(result);
		} catch (Exception e) {
		}

		if (object != null) {
			baseInfo = (BaseInfo) MainJson.fromJson(BaseInfo.class, object);
			parseMsg(baseInfo.getMsg());
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
		JsonParseArticle jsonParse = new JsonParseArticle();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}

}
