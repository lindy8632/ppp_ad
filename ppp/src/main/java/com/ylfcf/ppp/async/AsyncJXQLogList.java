package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseJXQLogList;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
/**
 * 加息券列表
 * @author Mr.liu
 *
 */
public class AsyncJXQLogList extends AsyncTaskBase{
	private Context context;

	private String userId;
	private String couponFrom;
	private String useStatus;
	private String page;
	private String pageSize;
	private String transfer;

	private OnCommonInter onCommonInter;
	private BaseInfo baseInfo;

	public AsyncJXQLogList(Context context, String userId, String couponFrom,String useStatus,String page,
						   String pageSize,String transfer, OnCommonInter onCommonInter) {
		this.context = context;
		this.userId = userId;
		this.couponFrom = couponFrom;
		this.useStatus = useStatus;
		this.page = page;
		this.pageSize = pageSize;
		this.transfer = transfer;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getIsGetJXQURL(userId, couponFrom, useStatus, page, pageSize, transfer);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseJXQLogList.parseData(result);
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
