package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;
import com.ylfcf.ppp.parse.JsonParseUserInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 获取企业用户信息
 * @author Mr.liu
 */
public class AsyncCompUserSelectOne extends AsyncTaskBase{
	private Context context;
	
	private String user_id;
	private String yyzz_code;//营业执照
	private String jgxy_code;//机构信用号 
	private String khxk_code;//开户许可号
	private OnCommonInter onCommonInter;
	
	private BaseInfo baseInfo;
	
	public AsyncCompUserSelectOne(Context context,String userId, String yyzzCode,String jgxyCode,String khxkCode,OnCommonInter onCommonInter) {
		this.context = context;
		this.user_id = userId;
		this.yyzz_code = yyzzCode;
		this.jgxy_code = jgxyCode;
		this.khxk_code = khxkCode;
		this.onCommonInter = onCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getCompUserInfo(user_id, yyzz_code, jgxy_code, khxk_code);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseUserInfo.parseData(result);
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
