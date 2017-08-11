package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * 元月盈投资
 * @author Mr.liu
 *
 */
public class AsyncYYYInvest extends AsyncTaskBase{
	private Context context;
	
	private String borrowId;
	private String investUserId;
	private String money;
	private String investFrom;//投资来源
	private String couponId;
	private String redbagId;
	private OnCommonInter onCommonInter;
	
	private BaseInfo baseInfo;
	
	public AsyncYYYInvest(Context context, String borrowId,String investUserId,String money,String investFrom,
			String couponId,String redbagId,OnCommonInter onCommonInter) {
		this.context = context;
		this.borrowId = borrowId;
		this.investUserId = investUserId;
		this.money = money;
		this.investFrom = investFrom;
		this.couponId = couponId;
		this.redbagId = redbagId;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getYYYBorrowInvestURL(borrowId, money, investUserId,investFrom,couponId,redbagId);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}
			YLFLogger.d("url:"+url[0]+"/n参数"+url[1]);
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
