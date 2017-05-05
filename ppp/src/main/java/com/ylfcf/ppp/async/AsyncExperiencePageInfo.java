package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseExperiencePageInfo;
import com.ylfcf.ppp.parse.JsonParseExtensionPageInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 体验金
 * 
 * @author Administrator
 * 
 */
public class AsyncExperiencePageInfo extends AsyncTaskBase {
	private Context context;

	private String status;
	private String userId;
	private String putStatus;
	private String activeTitle;

	private OnCommonInter onExperienceInter;

	private BaseInfo baseInfo;

	public AsyncExperiencePageInfo(Context context, String status,
			String userId, String putStatus, String activeTitle,
			OnCommonInter onExperienceInter) {
		this.context = context;
		this.userId = userId;
		this.status = status;
		this.putStatus = putStatus;
		this.activeTitle = activeTitle;
		this.onExperienceInter = onExperienceInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getArticleTYJListByStatus(status,
					userId, putStatus, activeTitle);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseExperiencePageInfo.parseData(result);
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
			onExperienceInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onExperienceInter.back(null);
		} else {
			// 获取成功
			onExperienceInter.back(baseInfo);
		}
	}

}
