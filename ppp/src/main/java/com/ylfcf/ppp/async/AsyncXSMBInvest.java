package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.parse.JsonParseRegiste;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 限时秒标投标接口
 * @author Mr.liu
 *
 */
public class AsyncXSMBInvest extends AsyncTaskBase{
	private Context context;
	private String borrowId;
	private String money;
	private String userId;
	private String investFrom;

	private OnCommonInter onCommonInter;
	private BaseInfo baseInfo;

	public AsyncXSMBInvest(Context context, String borrowId, String money,
			String userId, String investFrom, OnCommonInter onCommonInter) {
		this.context = context;
		this.borrowId = borrowId;
		this.money = money;
		this.userId = userId;
		this.investFrom = investFrom;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getXSMBInvestURL(borrowId, money, userId, investFrom);
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
