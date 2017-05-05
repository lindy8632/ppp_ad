package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnBorrowInvestInter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.parse.JsonParseInvest;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 元信宝 认购接口
 * @author Administrator
 *
 */
public class AsyncYXBInvest extends AsyncTaskBase{
	private Context context;
	
	private String productId;
	private String userId;
	private String orderMoney;
	private OnCommonInter onYXBInvestInter;
	
	private BaseInfo baseInfo;
	
	public AsyncYXBInvest(){}
	/**
	 * 
	 * @param context
	 * @param productId
	 * @param userId
	 * @param orderMoney
	 * @param onYXBInvestInter
	 */
	public AsyncYXBInvest(Context context, String productId,String userId,String orderMoney,OnCommonInter onYXBInvestInter) {
		this.context = context;
		this.productId = productId;
		this.userId = userId;
		this.orderMoney = orderMoney;
		this.onYXBInvestInter = onYXBInvestInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getYXBInvestURL(productId, userId, orderMoney);
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
			onYXBInvestInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onYXBInvestInter.back(null);
		} else {
			// 获取成功
			onYXBInvestInter.back(baseInfo);
		}
	}

}
