package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseSMSRegiste;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 用户银行卡认证
 * @author Administrator
 *
 */
public class AsyncUserVerify extends AsyncTaskBase{
	private Context context;
	
	private String userId;
	private String bankCard;
	private String realName; //格式      a:1:{s:11:"VERIFY_CODE";s:9:"张三丰";}  “张三丰”是VERIFY_CODE的值，前面的9表示字节长度
	private String idNumber;
	private String bankPhone;
	private OnCommonInter onUserVerifyInter;
	
	private BaseInfo baseInfo;
	
	public AsyncUserVerify(Context context, String userId,
			String bankCard,String realName,String idNumber,String bankPhone,OnCommonInter onUserVerifyInter) {
		this.context = context;
		this.userId = userId;
		this.bankCard = bankCard;
		this.realName = realName;
		this.idNumber = idNumber;
		this.bankPhone = bankPhone;
		this.onUserVerifyInter = onUserVerifyInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getUserVerifyURL(userId, bankCard, realName, idNumber,bankPhone);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseSMSRegiste.parseData(result);
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
			onUserVerifyInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onUserVerifyInter.back(null);
		} else {
			// 获取成功
			onUserVerifyInter.back(baseInfo);
		}
	}
}
