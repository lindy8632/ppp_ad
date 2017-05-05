package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.common.FileUtil;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.BorrowType;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseProductPageInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 产品列表 --- 政信贷定期理财
 * 
 * @author Administrator
 */
public class AsyncProductPageInfo extends AsyncTaskBase {
	private Context context;

	private String pageNo;
	private String pageSize;
	private String borrowType;
	private String borrowStatus;
	private String moneyStatus;
	private String isShow;
	private String isWap;
	private String plan;
	private String enableShow = "1";
	private boolean isFirst;
	private String isNewHand;
	private String isVip;
	private OnCommonInter mOnCommonInter;

	private BaseInfo baseInfo;

	public AsyncProductPageInfo(Context context, String pageNo,
			String pageSize, String borrowType, String borrowStatus,String moneyStatus,
			String isShow, String isWap, String plan, boolean isFirst,String isNewHand,String isVip,
			OnCommonInter mOnCommonInter) {
		this.context = context;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.borrowType = borrowType;
		this.borrowStatus = borrowStatus;
		this.moneyStatus = moneyStatus;
		this.isShow = isShow;
		this.isWap = isWap;
		this.isFirst = isFirst;
		this.plan = plan;
		this.isNewHand = isNewHand;
		this.isVip = isVip;
		this.mOnCommonInter = mOnCommonInter;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getProduceListUrl(pageNo,
					pageSize, borrowType, borrowStatus,moneyStatus, isShow, isWap, plan,enableShow,isNewHand,isVip);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				baseInfo = JsonParseProductPageInfo.parseData(result);
				if (isFirst) {
					if ("1".equals(plan)) {
						FileUtil.save(context, FileUtil.YLFCF_YJH_CACHE, result);// 本地缓存
					}else if(BorrowType.VIP.equals(borrowType)){
						FileUtil.save(context, FileUtil.YLFCF_VIP_TOTAL_CACHE, result);// 本地缓存
					} else {
						if (BorrowType.SUYING.equals(borrowType)) {
							// 速盈
							FileUtil.save(context,
									FileUtil.YLFCF_ZXD_SUYING_CACHE, result);// 本地缓存
						} else if (BorrowType.BAOYING.equals(borrowType)) {
							// 保盈
							FileUtil.save(context,
									FileUtil.YLFCF_ZXD_BAOYING_CACHE, result);// 本地缓存
						} else if (BorrowType.WENYING.equals(borrowType)) {
							// 稳赢
							FileUtil.save(context,
									FileUtil.YLFCF_ZXD_WENYING_CACHE, result);// 本地缓存
						} else {
							// 全部
							FileUtil.save(context,
									FileUtil.YLFCF_ZXD_TOTAL_CACHE, result);// 本地缓存
						}
					}
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
			mOnCommonInter.back(baseInfo);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			mOnCommonInter.back(baseInfo);
		} else {
			// 获取成功
			// apiQueryInter.back(baseInfo);
			mOnCommonInter.back(baseInfo);
		}
	}

}
