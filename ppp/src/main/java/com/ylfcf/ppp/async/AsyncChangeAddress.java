package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 修改地址和邮编
 * 
 * @author Administrator
 * 
 */
public class AsyncChangeAddress extends AsyncTaskBase {
	private Context context;

	private String userId;
	private String address; //
	private String postCode;// 邮编
	private OnCommonInter onChangeAddress;

	private BaseInfo baseInfo;

	public AsyncChangeAddress(Context context, String userId, String address,
			String postCode, OnCommonInter onChangeAddress) {
		this.context = context;
		this.userId = userId;
		this.address = address;
		this.postCode = postCode;
		this.onChangeAddress = onChangeAddress;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getAddressURL(userId, address,
					postCode);
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
			onChangeAddress.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onChangeAddress.back(null);
		} else {
			// 获取成功
			onChangeAddress.back(baseInfo);
		}
	}

}
