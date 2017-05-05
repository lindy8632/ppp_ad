package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnLoginInter;
import com.ylfcf.ppp.parse.JsonParseLogin;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * 登录
 * 
 * @author Administrator
 * 
 */
public class AsyncLogin extends AsyncTaskBase {
	private Context context;

	private String phone;
	private String password;
	private OnLoginInter onLoginInter;

	private BaseInfo baseInfo;

	public AsyncLogin(Context context, String phone, String password,
			OnLoginInter onLoginInter) {
		this.context = context;
		this.phone = phone;
		this.password = password;
		this.onLoginInter = onLoginInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getUserLoginURL(phone, password);
			YLFLogger.d("URL:" + url[0] + "\n" + "参数：" + url[1]);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseLogin.parseData(result);
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
			onLoginInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onLoginInter.back(null);
		} else {
			// 获取成功
			onLoginInter.back(baseInfo);
		}
	}

}
