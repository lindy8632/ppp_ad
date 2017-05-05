package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseNewExtensionPageInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 推广收益 （新的接口）
 * @author Mr.liu
 *
 */
public class AsyncExtensionNewPageInfo extends AsyncTaskBase {
	private Context context;

	private String userId;
	private String pageNo;
	private String pageSize;

	private OnCommonInter onExtensionInter;

	private BaseInfo baseInfo;

	public AsyncExtensionNewPageInfo(Context context, String userId,
			String pageNo, String pageSize, OnCommonInter onExtensionInter) {
		this.context = context;
		this.userId = userId;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.onExtensionInter = onExtensionInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getNewExtensionIncomeURL(userId, pageNo, pageSize);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseNewExtensionPageInfo.parseData(result);
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
			onExtensionInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onExtensionInter.back(null);
		} else {
			// 获取成功
			onExtensionInter.back(baseInfo);
		}
	}

}
