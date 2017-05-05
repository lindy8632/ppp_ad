package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 会员福利计划领取奖品
 * @author Mr.liu
 *
 */
public class AsyncFLJHReceivePrize extends AsyncTaskBase{
	private Context context;
	private BaseInfo baseInfo;
	
	private String userId;
	private String prize;
	private String giftId;
	private String activeTitle;
	private String operatingRemark;
	private String remark;
	private String status;
	private String source;
	private OnCommonInter onCommonInter;
	public AsyncFLJHReceivePrize(Context context, String userId,String prize,String giftId,String activeTitle,
			String operatingRemark,String remark,String status,String source, OnCommonInter onCommonInter) {
		this.context = context;
		this.userId = userId;
		this.prize = prize;
		this.giftId = giftId;
		this.activeTitle = activeTitle;
		this.operatingRemark = operatingRemark;
		this.remark = remark;
		this.status = status;
		this.source = source;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getFLJHReceivePrizeURL(userId, prize,giftId, activeTitle, operatingRemark, remark, status, source);
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
