package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseTYJPageInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 体验金
 * 
 * @author Administrator
 * 
 */
public class AsyncTYJPageInfo extends AsyncTaskBase {
	private Context context;

	private String pageNo;
	private String pageSize;
	private String status;
	private String userId;
	private String putStatus;
	private String activeTitle;
	private OnCommonInter tyjInter;

	private BaseInfo baseInfo;

	public AsyncTYJPageInfo(Context context, String pageNo, String pageSize,
			String status, String userId, String putStatus, String activeTitle,
			OnCommonInter tyjInter) {
		this.context = context;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.status = status;
		this.userId = userId;
		this.putStatus = putStatus;
		this.activeTitle = activeTitle;
		this.tyjInter = tyjInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getTYJURL(pageNo, pageSize,
					status, userId, putStatus, activeTitle);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseTYJPageInfo.parseData(result);
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
			tyjInter.back(baseInfo);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			tyjInter.back(baseInfo);
		} else {
			// 获取成功
			tyjInter.back(baseInfo);
		}
	}

}
