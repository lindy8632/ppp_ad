package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseGiftPageInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
/**
 * 奖品列表
 * @author Mr.liu
 *
 */
public class AsyncHDGiftList extends AsyncTaskBase{
	private Context context;

	private String type;
	private String isShow;
	private String page;
	private String pageSize;

	private OnCommonInter mOnCommonInter;

	private BaseInfo baseInfo;

	public AsyncHDGiftList(Context context, String type,
			String isShow,String page,String pageSize,OnCommonInter mOnCommonInter) {
		this.context = context;
		this.type = type;
		this.isShow = isShow;
		this.page = page;
		this.pageSize = pageSize;
		this.mOnCommonInter = mOnCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getHDGiftListURL(type, isShow,page,pageSize);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseGiftPageInfo.parseData(result);
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
			mOnCommonInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			mOnCommonInter.back(null);
		} else {
			// 获取成功
			mOnCommonInter.back(baseInfo);
		}
	}
}
