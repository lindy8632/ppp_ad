package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.common.FileUtil;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseBanner;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * Banner接口
 * @author Administrator
 *
 */
public class AsyncBanner extends AsyncTaskBase{
	private Context context;
	
	private String page;
	private String pageSize;
	private String status;
	private String type;
	
	private OnCommonInter onBannerInter;
	private BaseInfo baseInfo;
	
	public AsyncBanner(Context context, String page,String pageSize,String status,String type,OnCommonInter onBannerInter) {
		this.context = context;
		this.page =page;
		this.pageSize = pageSize;
		this.status = status;
		this.type = type;
		this.onBannerInter = onBannerInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getBannerURL(page, pageSize, status,type);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}
			YLFLogger.d("banner结果："+result+"banner链接："+url[0]+url[1]);
			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseBanner.parseData(result);
				int resultCode = SettingsManager.getResultCode(baseInfo);
				if(resultCode == 0){
					FileUtil.save(context, FileUtil.YLFCF_BANNER_CACHE, result);//本地缓存
					SettingsManager.setBannerCacheTime(context, System.currentTimeMillis());
				}
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
			onBannerInter.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onBannerInter.back(null);
		} else {
			// 获取成功
			onBannerInter.back(baseInfo);
		}
	}

}
