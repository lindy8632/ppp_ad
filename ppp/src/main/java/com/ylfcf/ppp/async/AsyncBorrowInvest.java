package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnBorrowInvestInter;
import com.ylfcf.ppp.parse.JsonParseInvest;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * 投资  ---- 新手标、定期理财
 * @author Administrator
 *
 */
public class AsyncBorrowInvest extends AsyncTaskBase{
	private Context context;
	
	private String borrowId;
	private String investUserId;
	private String money;
	private int bonusMoney;
	private String investFrom;
	private String investFromSub;
	private String experienceCode;
	private String investFromHost;
	private String merPriv;
	private String redBagLogId;
	private String couponLogId;
	
	private OnBorrowInvestInter onInvestInter;
	
	private BaseInfo baseInfo;
	
	public AsyncBorrowInvest(Context context, String borrowId,String investUserId,String money,int bonusMoney,String investFrom,
			String investFromSub,String experienceCode,String investFromHost,String merPriv,String redBagLogId,String couponLogId,OnBorrowInvestInter onInvestInter) {
		this.context = context;
		this.borrowId = borrowId;
		this.investUserId = investUserId;
		this.money = money;
		this.bonusMoney = bonusMoney;
		this.investFrom = investFrom;
		this.investFromSub = investFromSub;
		this.experienceCode = experienceCode;
		this.investFromHost = investFromHost;
		this.merPriv = merPriv;
		this.redBagLogId = redBagLogId;
		this.couponLogId = couponLogId;
		this.onInvestInter = onInvestInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getBorrowInvestURL(borrowId, investUserId, money, bonusMoney, 
					investFrom, investFromSub, experienceCode, investFromHost, merPriv,redBagLogId,couponLogId);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseInvest.parseData(result);
			}
			YLFLogger.d("投资结果："+result);
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
