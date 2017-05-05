package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.ArticleInfo;
import com.ylfcf.ppp.entity.ArticlePageInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.util.MainJson;

/**
 * 文章、新闻、资讯列表
 * @author Administrator
 *
 */
public class JsonParseArticlePageInfo {
	private BaseInfo baseInfo;
	private ArticlePageInfo mArticlePageInfo;
	private List<ArticleInfo> articleList;
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	/*
	 * 解析list列表
	 */
	public void parsePageInfo(String data) throws Exception{
		articleList = new ArrayList<ArticleInfo>();
		JSONArray array = new JSONArray(data);
		int size = array.length();
		for(int i = 0;i < size;i++){
			JSONObject object = array.getJSONObject(i);
			ArticleInfo info = (ArticleInfo) MainJson.fromJson(ArticleInfo.class, object);
			articleList.add(info);
		}
		mArticlePageInfo.setArticleList(articleList);
	}
	
	/*
	 * 解析msg字段
	 */
	public void parseMsg(String result) throws Exception {
		JSONObject object = null;
		object = new JSONObject(result);
		if(object != null){
			mArticlePageInfo = (ArticlePageInfo) MainJson.fromJson(ArticlePageInfo.class, object);
			parsePageInfo(mArticlePageInfo.getList());
			baseInfo.setmArticlePageInfo(mArticlePageInfo);
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
			parseMsg(baseInfo.getMsg());
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
		JsonParseArticlePageInfo jsonParse = new JsonParseArticlePageInfo();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}

}
