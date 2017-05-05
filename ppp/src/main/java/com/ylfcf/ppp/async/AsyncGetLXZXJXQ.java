package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
/**
 * 领取加息券
 * @author Mr.liu
 *
 */
public class AsyncGetLXZXJXQ extends AsyncTaskBase{
	private Context context;

	private String userId;
	private String ticket;//加息券规则id
	private String startTime;
	private String endTime;
	private String remark;//备注
	private String type;//发送方式
	private String isBatch;//是批量还是指定用户发送

	private OnCommonInter onCommonInter;
	private BaseInfo baseInfo;

	public AsyncGetLXZXJXQ(Context context, String userId, String ticket,String startTime,String endTime,
			String remark,String type,String isBatch,OnCommonInter onCommonInter) {
		this.context = context;
		this.userId = userId;
		this.ticket = ticket;
		this.startTime = startTime;
		this.endTime = endTime;
		this.remark = remark;
		this.type = type;
		this.isBatch = isBatch;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getLxfxJXQURL(userId, ticket, startTime, endTime, remark, type, isBatch);
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
