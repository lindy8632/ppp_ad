package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnBorrowInvestInter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseAsscociatedCompany;
import com.ylfcf.ppp.parse.JsonParseInvest;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 关联公司
 * @author Administrator
 *
 */
public class AsyncAsscociatedCompany extends AsyncTaskBase{
	private Context context;
	
	private String loanId;
	private String recommendId;
	private String guaranteeId;
	
	private OnCommonInter onAsscociatedCompanyInter;
	
	private BaseInfo baseInfo;
	
	public AsyncAsscociatedCompany(Context context, String loanId,String recommendId,String guaranteeId,
			OnCommonInter onAsscociatedCompanyInter) {
		this.context = context;
		this.loanId = loanId;
		this.recommendId = recommendId;
		this.guaranteeId = guaranteeId;
		this.onAsscociatedCompanyInter = onAsscociatedCompanyInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getAssociatedCompanyURL(loanId, recommendId, guaranteeId);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseAsscociatedCompany.parseData(result);
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
			onAsscociatedCompanyInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onAsscociatedCompanyInter.back(null);
		} else {
			// 获取成功
			onAsscociatedCompanyInter.back(baseInfo);
		}
	}
}
