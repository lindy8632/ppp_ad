package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.parse.JsonParseRechargeResult;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
/**
 * 宝付充值
 * @author Mr.liu
 *
 */
public class AsyncBFRecharge extends AsyncTaskBase{
	private Context context;
	
	private String userId;//用户ID
	private String account;//身份证号码
	private String smsCode;//真身姓名
	private String orderId;//订单号，获取验证码返回回来的
	
	private OnCommonInter onCommonInter;
	
	private BaseInfo baseInfo;
	
	public AsyncBFRecharge(Context context, String userId,String account,String smsCode,String orderId,OnCommonInter onCommonInter) {
		this.context = context;
		this.userId = userId;
		this.account = account;
		this.smsCode = smsCode;
		this.orderId = orderId;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getBFRechargeURL(userId, account, smsCode,orderId);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseRechargeResult.parseData(result);
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
