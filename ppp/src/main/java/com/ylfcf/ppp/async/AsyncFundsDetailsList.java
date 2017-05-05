package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseFundsDetailsList;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 资金明细 ---- 所有的
 * @author Mr.liu
 *
 */
public class AsyncFundsDetailsList extends AsyncTaskBase {
	private Context context;

	private String userId;
	private String type;
	private String page;
	private String pageSize;
	private String startTime;
	private String endTime;
	private OnCommonInter onCommonInter;

	private BaseInfo baseInfo;

	public AsyncFundsDetailsList(Context context, String userId,
			String type, String page, String pageSize, String startTime,
			String endTime, OnCommonInter onCommonInter) {
		this.context = context;
		this.userId = userId;
		this.type = type;
		this.page = page;
		this.pageSize = pageSize;
		this.startTime = startTime;
		this.endTime = endTime;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getFundsDetailsListURL(
					userId, type, page, pageSize, startTime, endTime);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseFundsDetailsList.parseData(result);
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
			onCommonInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onCommonInter.back(null);
		} else {
			// 获取成功
			onCommonInter.back(baseInfo);
		}
	}
}
