package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnBorrowInvestInter;
import com.ylfcf.ppp.parse.JsonParseInvest;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
/**
 * VIP产品投资接口
 * @author Mr.liu
 *
 */
public class AsyncVipBorrowInvest extends AsyncTaskBase{
	private Context context;
	
	private String borrowId;
	private String investUserId;
	private String money;
	private OnBorrowInvestInter onInvestInter;
	
	private BaseInfo baseInfo;
	
	public AsyncVipBorrowInvest(Context context, String borrowId,String investUserId,String money,
			OnBorrowInvestInter onInvestInter) {
		this.context = context;
		this.borrowId = borrowId;
		this.investUserId = investUserId;
		this.money = money;
		this.onInvestInter = onInvestInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getVIPInvestURL(borrowId, money, investUserId);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseInvest.parseData(result);
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
			onInvestInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onInvestInter.back(null);
		} else {
			// 获取成功
			onInvestInter.back(baseInfo);
		}
	}

}
