package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.parse.JsonParseLogin;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * 元信宝预约和取消赎回
 * @author Mr.liu
 *
 */
public class AsyncYYYApplyCancelReturn extends AsyncTaskBase {
	private Context context;

	private String investId;
	private String userId;
	private String type;
	private OnCommonInter onCommonInter;

	private BaseInfo baseInfo;

	public AsyncYYYApplyCancelReturn(Context context, String investId, String userId,String type,
			OnCommonInter onCommonInter) {
		this.context = context;
		this.investId = investId;
		this.userId = userId;
		this.type = type;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getYYYApplyOrCancelReturnURL(investId, userId, type);
			YLFLogger.d("URL:" + url[0] + "\n" + "参数：" + url[1]);
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
