package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseProductInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 某支产品的详情 ---- 政信贷
 * 
 * @author Administrator
 * 
 */
public class AsyncProductInfo extends AsyncTaskBase {
	private Context context;

	private String borrowId;
	private String borrowStatus;
	private String plan;
	private OnCommonInter onProductInter;

	private BaseInfo baseInfo;

	public AsyncProductInfo(Context context, String borrowId,
			String borrowStatus, String plan, OnCommonInter onProductInter) {
		this.context = context;
		this.borrowId = borrowId;
		this.borrowStatus = borrowStatus;
		this.plan = plan;
		this.onProductInter = onProductInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getBorrowOnewURL(borrowId,
					borrowStatus, plan);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseProductInfo.parseData(result);
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
			onProductInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onProductInter.back(null);
		} else {
			// 获取成功
			onProductInter.back(baseInfo);
		}
	}

}
