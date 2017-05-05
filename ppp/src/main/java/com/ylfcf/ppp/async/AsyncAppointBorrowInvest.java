package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnBorrowInvestInter;
import com.ylfcf.ppp.parse.JsonParseInvest;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 私人尊享投标
 * @author Mr.liu
 *
 */
public class AsyncAppointBorrowInvest extends AsyncTaskBase{
	private Context context;
	
	private String borrowId;
	private String investUserId;
	private String money;
	private String investFrom;
	private String redbagId;
	
	private OnBorrowInvestInter onInvestInter;
	
	private BaseInfo baseInfo;
	
	public AsyncAppointBorrowInvest(Context context, String borrowId,String investUserId,String money,String investFrom,String redbagId,
			OnBorrowInvestInter onInvestInter) {
		this.context = context;
		this.borrowId = borrowId;
		this.investUserId = investUserId;
		this.money = money;
		this.investFrom = investFrom;
		this.redbagId = redbagId;
		this.onInvestInter = onInvestInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getAppointBorrowInvest(borrowId, investUserId, money, investFrom,redbagId);
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
