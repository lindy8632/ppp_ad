package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 取消提现
 * @author Administrator
 *
 */
public class AsyncWithdrawCancel extends AsyncTaskBase{
	private Context context;
	
	private String id;//是id,不是取订单号cash_order
	private String status;//用户取消  写死
	private String userId;
	private String auditType;//用户  写死
	private OnCommonInter onWithdrawCancel;
	
	private BaseInfo baseInfo;
	
	public AsyncWithdrawCancel(Context context, String id,String status,String userId,String auditType,
			OnCommonInter onWithdrawCancel) {
		this.context = context;
		this.id = id;
		this.status = status;
		this.userId = userId;
		this.auditType = auditType;
		this.onWithdrawCancel = onWithdrawCancel;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getWithdrawCancelURL(id, status, userId, auditType);
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
			onWithdrawCancel.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onWithdrawCancel.back(null);
		} else {
			// 获取成功
			onWithdrawCancel.back(baseInfo);
		}
	}
}
