package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseJXQRule;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
/**
 * 加息券规则
 * @author Mr.liu
 *
 */
public class AsyncJXQRule extends AsyncTaskBase{
	private Context context;

	private String money;
	private String needInvestMoney;
	private String borrowType;

	private OnCommonInter onCommonInter;
	private BaseInfo baseInfo;

	public AsyncJXQRule(Context context, String money, String needInvestMoney,String borrowType,
			OnCommonInter onCommonInter) {
		this.context = context;
		this.money = money;
		this.needInvestMoney = needInvestMoney;
		this.borrowType = borrowType;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getJXQIDURL(money, needInvestMoney,borrowType);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseJXQRule.parseData(result);
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
