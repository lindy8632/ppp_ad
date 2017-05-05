package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseGiftCode;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 礼品
 * @author Mr.liu
 *
 */
public class AsyncGiftCode extends AsyncTaskBase{
	private Context context;
	
	private String id;
	private String giftId;
	private String userId;
	private String status;
	
	private OnCommonInter mOnCommonInter;
	private BaseInfo baseInfo;
	
	public AsyncGiftCode(Context context, String id,String giftId,String userId,String status,OnCommonInter mOnCommonInter) {
		this.context = context;
		this.id = id;
		this.giftId = giftId;
		this.userId = userId;
		this.status = status;
		this.mOnCommonInter = mOnCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getHDGiftById(id,giftId, userId,status);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseGiftCode.parseData(result);
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
			mOnCommonInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			mOnCommonInter.back(null);
		} else {
			// 获取成功
			mOnCommonInter.back(baseInfo);
		}
	}

}
