package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 判断当前用户是否投资过该vip标的
 * @author Administrator
 *
 */
public class AsyncVIPCurrentUserInvest extends AsyncTaskBase{
	private Context context;
	private BaseInfo baseInfo;
	
	private String investUserId;
	private String borrowId;
	private OnCommonInter onCurrentUserInvestInter;
	
	public AsyncVIPCurrentUserInvest(Context context, String investUserId,String borrowId,
			OnCommonInter onCurrentUserInvestInter) {
		this.context = context;
		this.investUserId = investUserId;
		this.borrowId = borrowId;
		this.onCurrentUserInvestInter = onCurrentUserInvestInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getVIPCurrentUserInvestURL(investUserId, borrowId);
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
			onCurrentUserInvestInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onCurrentUserInvestInter.back(null);
		} else {
			// 获取成功
			onCurrentUserInvestInter.back(baseInfo);
		}
	}

}
