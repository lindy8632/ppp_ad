package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseBanner;
import com.ylfcf.ppp.parse.JsonParseYXBUserCenter;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 元信宝用户账户
 * @author Administrator
 *
 */
public class AsyncYXBUserAccount extends AsyncTaskBase{
	private Context context;
	private String userId;
	
	private OnCommonInter onYXBAccountInter;
	private BaseInfo baseInfo;
	
	public AsyncYXBUserAccount(Context context, String userId,OnCommonInter onYXBAccountInter) {
		this.context = context;
		this.userId = userId;
		this.onYXBAccountInter = onYXBAccountInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getYXBUserCenterURL(userId);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseYXBUserCenter.parseData(result);
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
			onYXBAccountInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onYXBAccountInter.back(null);
		} else {
			// 获取成功
			onYXBAccountInter.back(baseInfo);
		}
	}

}
