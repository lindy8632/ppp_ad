package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 当前用户是否投资过该秒标产品
 * @author Mr.liu
 *
 */
public class AsyncXSMBCurrentUserInvest extends AsyncTaskBase{
	private Context context;
	
	private String userId;
	private String borrowId;
	private OnCommonInter onCommonInter;
	private BaseInfo baseInfo;
	
	public AsyncXSMBCurrentUserInvest(Context context,String userId,String borrowId,OnCommonInter onCommonInter) {
		this.context = context;
		this.userId =userId;
		this.borrowId = borrowId;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getXSMBCurrentUserInvestURL(userId, borrowId);
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
