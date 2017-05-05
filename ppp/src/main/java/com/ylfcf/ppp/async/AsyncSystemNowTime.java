package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;
/**
 * 获取系统当前时间
 * @author Mr.liu
 *
 */
public class AsyncSystemNowTime extends AsyncTaskBase{
	private Context context;

	private String type;
	private OnCommonInter mOnCommonInter;

	private BaseInfo baseInfo;

	public AsyncSystemNowTime(Context context, String type,
			OnCommonInter mOnCommonInter) {
		this.context = context;
		this.type = type;
		this.mOnCommonInter = mOnCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getSystemNowTimeURL(type);
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
			mOnCommonInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			mOnCommonInter.back(null);
		} else {
			// 获取成功
			mOnCommonInter.back(baseInfo);
		}
	}

}
