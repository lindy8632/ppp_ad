package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 将手机信息添加到数据库
 * @author Administrator
 *
 */
public class AsyncAddPhoneInfo extends AsyncTaskBase{
	private Context context;
	private BaseInfo baseInfo;
	
	private String userId;
	private String phone;
	private String phoneModel;
	private String sdkVersion;
	private String systemVersion;
	private String phoneType;
	private String location;
	private String contact;
	
	
	private OnCommonInter onAddPhoneInfoInter;
	public AsyncAddPhoneInfo(Context context, String userId,String phone,String phoneModel,String sdkVersion,String systemVersion,
			String phoneType,String location,String contact,OnCommonInter onAddPhoneInfoInter) {
		this.context = context;
		this.userId = userId;
		this.phone = phone;
		this.phoneModel = phoneModel;
		this.sdkVersion = sdkVersion;
		this.systemVersion = systemVersion;
		this.phoneType = phoneType;
		this.location = location;
		this.contact = contact;
		this.onAddPhoneInfoInter = onAddPhoneInfoInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getAddPhoneInfoURL(userId, phone, phoneModel, sdkVersion, systemVersion, phoneType, location, contact);
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
			onAddPhoneInfoInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onAddPhoneInfoInter.back(null);
		} else {
			// 获取成功
			onAddPhoneInfoInter.back(baseInfo);
		}
	}

}
