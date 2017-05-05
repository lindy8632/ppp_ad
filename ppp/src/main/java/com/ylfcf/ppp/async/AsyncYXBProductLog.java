package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.common.FileUtil;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseYXBProduct;
import com.ylfcf.ppp.parse.JsonParseYXBProductLog;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
/**
 * 元信宝产品每日统计
 * @author Administrator
 *
 */
public class AsyncYXBProductLog extends AsyncTaskBase{
	private Context context;
	private String id;
	private String dateTime;
	private OnCommonInter onYXBProductLogInter;
	
	private BaseInfo baseInfo;
	
	public AsyncYXBProductLog(Context context, String id,
			String dateTime ,OnCommonInter onYXBProductLogInter) {
		this.context = context;
		this.id = id;
		this.dateTime = dateTime;
		this.onYXBProductLogInter = onYXBProductLogInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getYXBProductLogURL(id, dateTime);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseYXBProductLog.parseData(result);
				FileUtil.save(context, FileUtil.YLFCF_YXB_PRODUCTLOG_CACHE, result);//本地缓存
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
			onYXBProductLogInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onYXBProductLogInter.back(null);
		} else {
			// 获取成功
			onYXBProductLogInter.back(baseInfo);
		}
	}

}
