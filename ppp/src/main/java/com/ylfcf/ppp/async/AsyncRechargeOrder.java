package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.parse.JsonParseSMSRegiste;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 生成支付订单
 * 
 * @author Administrator
 * 
 */
public class AsyncRechargeOrder extends AsyncTaskBase {
	private Context context;

	private String userId;
	private String orderId;
	private String smsCode;
	private OnCommonInter onRechargeOrderInter;

	private BaseInfo baseInfo;

	public AsyncRechargeOrder(Context context, String userId, String orderId,
			String smsCode, OnCommonInter onRechargeOrderInter) {
		this.context = context;
		this.userId = userId;
		this.orderId = orderId;
		this.smsCode = smsCode;
		this.onRechargeOrderInter = onRechargeOrderInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getRechargeOrderURL(userId,
					orderId, smsCode);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseCommon.parseData(result);
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
			onRechargeOrderInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onRechargeOrderInter.back(null);
		} else {
			// 获取成功
			onRechargeOrderInter.back(baseInfo);
		}
	}

}
