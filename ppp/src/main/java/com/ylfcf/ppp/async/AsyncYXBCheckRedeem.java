package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseYXBCheckRedeem;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 元信宝  验证赎回
 * @author Administrator
 *
 */
public class AsyncYXBCheckRedeem extends AsyncTaskBase{
	private Context context;
	
	private String userId;
	private String productId;  //充值金额
	private String applyMoney;
	private OnCommonInter onYXBCheckRedeemInter;
	
	private BaseInfo baseInfo;
	
	public AsyncYXBCheckRedeem(Context context, String productId,
			String userId,String applyMoney,OnCommonInter onYXBCheckRedeemInter) {
		this.context = context;
		this.userId = userId;
		this.productId = productId;
		this.applyMoney = applyMoney;
		this.onYXBCheckRedeemInter = onYXBCheckRedeemInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getYXBCheckRedeemURL(productId, userId, applyMoney);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseYXBCheckRedeem.parseData(result);
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
			onYXBCheckRedeemInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onYXBCheckRedeemInter.back(null);
		} else {
			// 获取成功
			onYXBCheckRedeemInter.back(baseInfo);
		}
	}

}
