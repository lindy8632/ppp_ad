package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseJXQPageInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
/**
 * 加息券
 * @author Mr.liu
 *
 */
public class AsyncJXQPageInfo extends AsyncTaskBase{
	private Context context;

	private String userId;
	private String useStatus;
	private String pageNo;
	private String pageSize;

	private OnCommonInter onCommonInter;

	private BaseInfo baseInfo;

	public AsyncJXQPageInfo(Context context, String userId,String useStatus,
			String pageNo, String pageSize, OnCommonInter onCommonInter) {
		this.context = context;
		this.userId = userId;
		this.useStatus = useStatus;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getJXQListURL(userId, useStatus, pageNo, pageSize);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseJXQPageInfo.parseData(result);
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
