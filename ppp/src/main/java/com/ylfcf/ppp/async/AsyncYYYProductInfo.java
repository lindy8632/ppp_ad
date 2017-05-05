package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseProductInfo;
import com.ylfcf.ppp.parse.JsonParseYYYProductInfo;
import com.ylfcf.ppp.parse.JsonParseYYYProductInfoById;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 元月盈的产品详情（最新的一期产品）
 * @author Mr.liu
 *
 */
public class AsyncYYYProductInfo extends AsyncTaskBase {
	private Context context;

	private String id;
	private String borrowStatus;
	private String moneyStatus;
	private OnCommonInter onProductInter;

	private BaseInfo baseInfo;

	public AsyncYYYProductInfo(Context context, String id,
			String borrowStatus, String moneyStatus, OnCommonInter onProductInter) {
		this.context = context;
		this.id = id;
		this.borrowStatus = borrowStatus;
		this.moneyStatus = moneyStatus;
		this.onProductInter = onProductInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			if(id.isEmpty()){
				url = URLGenerator.getInstance().getYYYBorrowDetailsURL(borrowStatus);
			}else{
				url = URLGenerator.getInstance().getYYYBorrowDetailsByIdURL(id, borrowStatus, moneyStatus);
			}
			
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				if(id.isEmpty()){
					baseInfo = JsonParseYYYProductInfo.parseData(result);
				}else{
					baseInfo = JsonParseYYYProductInfoById.parseData(result);
				}
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
