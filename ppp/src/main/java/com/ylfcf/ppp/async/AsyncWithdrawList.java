package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseInvest;
import com.ylfcf.ppp.parse.JsonParseWithdrawList;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 提现列表
 * @author Administrator
 *
 */
public class AsyncWithdrawList extends AsyncTaskBase{
	private Context context;
	
	private String userId;
	private String page;
	private String pageSize;
	private String status;
	private String startTime;
	private String endTime;
	
	private OnCommonInter onCommonInter;
	
	private BaseInfo baseInfo;
	
	public AsyncWithdrawList(Context context, String userId,String page,String pageSize,String status,
			String startTime,String endTime,OnCommonInter onCommonInter) {
		this.context = context;
		this.userId = userId;
		this.page = page;
		this.pageSize = pageSize;
		this.status = status;
		this.startTime = startTime;
		this.endTime = endTime;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getWithdrawListURL(userId, page, pageSize, status, startTime, endTime);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseWithdrawList.parseData(result);
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
