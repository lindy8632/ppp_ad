package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseYXBRedeemRecord;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 元信宝赎回记录
 * 
 * @author Administrator
 */
public class AsyncYXBRedeemRecord extends AsyncTaskBase {
	private Context context;

	private String id;
	private String userId; // 充值金额
	private String page;
	private String pageSize;
	private OnCommonInter onYXBRecordInter;

	private BaseInfo baseInfo;

	public AsyncYXBRedeemRecord(Context context, String id, String userId,
			String page, String pageSize, OnCommonInter onYXBRecordInter) {
		this.context = context;
		this.userId = userId;
		this.id = id;
		this.page = page;
		this.pageSize = pageSize;
		this.onYXBRecordInter = onYXBRecordInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getYXBRedeemRecordsURL(id, userId,
					page, pageSize);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseYXBRedeemRecord.parseData(result);
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
			onYXBRecordInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onYXBRecordInter.back(null);
		} else {
			// 获取成功
			onYXBRecordInter.back(baseInfo);
		}
	}
}
