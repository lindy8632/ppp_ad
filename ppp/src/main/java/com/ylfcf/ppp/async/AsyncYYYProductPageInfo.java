package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseProductPageInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 产品列表 --- 元月盈产品
 * @author Mr.liu
 *
 */
public class AsyncYYYProductPageInfo extends AsyncTaskBase {
	private Context context;

	private String pageNo;
	private String pageSize;
	private String borrowStatus;
	private String moneyStatus;
	private OnCommonInter mOnCommonInter;

	private BaseInfo baseInfo;

	public AsyncYYYProductPageInfo(Context context, String pageNo,
			String pageSize, String borrowStatus, String moneyStatus,
			OnCommonInter mOnCommonInter) {
		this.context = context;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.borrowStatus = borrowStatus;
		this.moneyStatus = moneyStatus;
		this.mOnCommonInter = mOnCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getYYYBorrowListURL(borrowStatus, moneyStatus, pageNo, pageSize);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseProductPageInfo.parseData(result);
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
			mOnCommonInter.back(baseInfo);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			mOnCommonInter.back(baseInfo);
		} else {
			// 获取成功
			// apiQueryInter.back(baseInfo);
			mOnCommonInter.back(baseInfo);
		}
	}
}
