package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParsePrizeCode;
import com.ylfcf.ppp.parse.JsonParsePrizeInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

public class AsyncPrizeCode extends AsyncTaskBase{
	private Context context;

	private String id;
	private String prizeCode;
	private String prizeName;
	private String status;
	private String openid;
	private String userId;
	private OnCommonInter onCommonInter;

	private BaseInfo baseInfo;

	public AsyncPrizeCode(Context context, String id, String prizeCode,String prizeName,String status,String openid,String userId,
			OnCommonInter onCommonInter) {
		this.context = context;
		this.id = id;
		this.prizeCode = prizeCode;
		this.prizeName = prizeName;
		this.status = status;
		this.openid = openid;
		this.userId = userId;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getFLJHPrizeCodeSelectOne(id, prizeCode, prizeName, status, openid, userId);
			YLFLogger.d("URL:" + url[0] + "\n" + "参数：" + url[1]);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParsePrizeCode.parseData(result);
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
