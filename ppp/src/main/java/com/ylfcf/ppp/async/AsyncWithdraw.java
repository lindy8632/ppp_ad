package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 提现
 * @author Administrator
 *
 */
public class AsyncWithdraw extends AsyncTaskBase{
	private Context context;
	
	private String userId;
	private String cashAccount;  //取款金额
	private OnCommonInter onWithdrawInter;
	
	private BaseInfo baseInfo;
	
	public AsyncWithdraw(Context context, String userId,
			String cashAccount,OnCommonInter onWithdrawInter) {
		this.context = context;
		this.userId = userId;
		this.cashAccount = cashAccount;
		this.onWithdrawInter = onWithdrawInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getWithdrawURL(userId, cashAccount);
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
			onWithdrawInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onWithdrawInter.back(null);
		} else {
			// 获取成功
			onWithdrawInter.back(baseInfo);
		}
	}

}
