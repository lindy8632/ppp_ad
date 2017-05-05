package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnUpdateUserInfoInter;
import com.ylfcf.ppp.parse.JsonParseInvest;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 更新用户信息
 * 
 * @author Administrator
 * 
 */
public class AsyncUpdateUserInfo extends AsyncTaskBase {
	private Context context;

	private String id;
	private String password;
	private String phone;
	private String email;
	private String openId;
	private String dealEnabled;
	private String dealPwd;
	private String tmpData;
	private String initPwd;

	private OnUpdateUserInfoInter onUpdateUserInfo;

	private BaseInfo baseInfo;

	public AsyncUpdateUserInfo(Context context, String id, String password,
			String phone, String email, String openId, String dealEnabled,
			String dealPwd, String tmpData,String initPwd,
			OnUpdateUserInfoInter onUpdateUserInfo) {
		this.context = context;
		this.id = id;
		this.password = password;
		this.phone = phone;
		this.email = email;
		this.openId = openId;
		this.dealEnabled = dealEnabled;
		this.dealPwd = dealPwd;
		this.initPwd = initPwd;
		this.onUpdateUserInfo = onUpdateUserInfo;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getUpdateUserInfoURL(id, password,
					phone, email, openId, dealEnabled, dealPwd, tmpData,initPwd);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseInvest.parseData(result);
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
			onUpdateUserInfo.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onUpdateUserInfo.back(null);
		} else {
			// 获取成功
			onUpdateUserInfo.back(baseInfo);
		}
	}

}
