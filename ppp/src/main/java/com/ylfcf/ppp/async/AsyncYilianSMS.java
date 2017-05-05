package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 易联发送短信
 * @author Administrator
 *
 */
public class AsyncYilianSMS extends AsyncTaskBase{
	private Context context;
	
	private String userId;
	private String order;
	private OnCommonInter onYilianSmsInter;
	
	private BaseInfo baseInfo;
	
	public AsyncYilianSMS(Context context,String userId, String order,OnCommonInter onYilianSmsInter) {
		this.context = context;
		this.userId = userId;
		this.order = order;
		this.onYilianSmsInter = onYilianSmsInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getYilianSMSURL(userId, order);
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
			onYilianSmsInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onYilianSmsInter.back(null);
		} else {
			// 获取成功
			onYilianSmsInter.back(baseInfo);
		}
	}

	
}
