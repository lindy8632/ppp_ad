package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseRedBagPageInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 当前用户可以使用的红包列表
 * @author Administrator
 *
 */
public class AsyncCurrentUserRedbagList extends AsyncTaskBase{
	private Context context;
	
	private String userId;
	private String borrowType;
	
	private OnCommonInter onRedbagCurUserListInter;
	private BaseInfo baseInfo;
	
	public AsyncCurrentUserRedbagList(Context context, String userId,String borrowType,OnCommonInter onRedbagListInter) {
		this.context = context;
		this.userId = userId;
		this.borrowType = borrowType;
		this.onRedbagCurUserListInter = onRedbagListInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getRedbagCurrentUserListURL(userId, borrowType);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseRedBagPageInfo.parseData(result);
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
			onRedbagCurUserListInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onRedbagCurUserListInter.back(null);
		} else {
			// 获取成功
			onRedbagCurUserListInter.back(baseInfo);
		}
	}
}
