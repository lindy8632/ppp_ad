package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseArticlePageInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 新闻、资讯、公告列表
 * 
 * @author Administrator
 * 
 */
public class AsyncArticleList extends AsyncTaskBase {
	private Context context;
	private BaseInfo baseInfo;

	private String page;
	private String pageSize;
	private String status;
	private String type;

	private OnCommonInter onArticleInter;

	public AsyncArticleList(Context context, String page, String pageSize,
			String status, String type, OnCommonInter onArticleInter) {
		this.context = context;
		this.page = page;
		this.pageSize = pageSize;
		this.status = status;
		this.type = type;
		this.onArticleInter = onArticleInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getArticleListURL(status, type,
					page, pageSize);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseArticlePageInfo.parseData(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BackType.ERROR;
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (isCancelled()) {
			return;
		}
		if (BackType.ERROR.equals(result)) {
			// 访问错误
			onArticleInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onArticleInter.back(null);
		} else {
			// 获取成功
			onArticleInter.back(baseInfo);
		}
	}

}
