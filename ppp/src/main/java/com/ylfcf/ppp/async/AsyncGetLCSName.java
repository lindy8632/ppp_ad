package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 获取理财师的名字（VIP注册时候需要）
 * @author Mr.liu
 *
 */
public class AsyncGetLCSName extends AsyncTaskBase{
	private Context context;
	private BaseInfo baseInfo;
	
	private String phone;
	private OnCommonInter onLCSNameInter;
	public AsyncGetLCSName(Context context, String phone,
			OnCommonInter onLCSNameInter) {
		this.context = context;
		this.phone = phone;
		this.onLCSNameInter = onLCSNameInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getLCSNameURL(phone);
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
			onLCSNameInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onLCSNameInter.back(null);
		} else {
			// 获取成功
			onLCSNameInter.back(baseInfo);
		}
	}
}
