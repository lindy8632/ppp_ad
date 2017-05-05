package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseInvestRecordList;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 稳定盈标的投资记录
 * @author Mr.liu
 *
 */
public class AsyncWDYInvestDetail extends AsyncTaskBase {
	private Context context;

	private String borrowId;
	private int pageNo;
	private int pageSize;
	private OnCommonInter investRecordListInter;
	private BaseInfo baseInfo;
	
	public AsyncWDYInvestDetail(Context context, String borrowId,int pageNo, int pageSize,
			OnCommonInter investRecordListInter) {
		this.context = context;
		this.borrowId = borrowId;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.investRecordListInter = investRecordListInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance()
					.getWDYBorrowInvestDetailURL(borrowId, String.valueOf(pageNo), String.valueOf(pageSize));
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseInvestRecordList
						.parseData(result);
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
			investRecordListInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			investRecordListInter.back(null);
		} else {
			// 获取成功
			investRecordListInter.back(baseInfo);
		}
	}
}
