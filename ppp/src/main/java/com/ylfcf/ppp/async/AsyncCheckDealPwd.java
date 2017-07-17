package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 校验交易密码
 * 
 * @author Administrator
 * 
 */
public class AsyncCheckDealPwd extends AsyncTaskBase {
	private Context context;

	private String userId;
	private String dealpwd; // 交易密码
	private OnCommonInter onCheckDealInter;

	private BaseInfo baseInfo;

	public AsyncCheckDealPwd(Context context, String userId, String dealpwd,
			OnCommonInter onCheckDealInter) {
		this.context = context;
		this.userId = userId;
		this.dealpwd = dealpwd;
		this.onCheckDealInter = onCheckDealInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getExchangePwdCheck(userId,
					dealpwd);
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
			onCheckDealInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onCheckDealInter.back(null);
		} else {
			// 获取成功
			onCheckDealInter.back(baseInfo);
		}
	}
}
