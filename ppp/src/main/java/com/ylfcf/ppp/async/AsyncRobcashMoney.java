package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.parse.JsonParseRobcashMoneyInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * 5月份活动 下周待抢金额以及本周可抢金额
 * Created by Administrator on 2017/4/26.
 */

public class AsyncRobcashMoney extends AsyncTaskBase{
    private Context context;
    private BaseInfo baseInfo;
    private String nowTime;

    private Inter.OnCommonInter mOnCommonInter;
    public AsyncRobcashMoney(Context context,String nowTime, Inter.OnCommonInter mOnCommonInter) {
        this.context = context;
        this.nowTime = nowTime;
        this.mOnCommonInter = mOnCommonInter;
    }

    @Override
    protected String doInBackground(String... params) {
        String url[] = null;
        String result = null;
        try {
            url = URLGenerator.getInstance().getHDRobCashCashURL(nowTime);
            if (result == null) {
                result = HttpConnection.postConnection(url[0], url[1]);
            }
            YLFLogger.d("五月份抢现金活动："+result);
            if (result == null) {
                result = BackType.FAILE;
            } else {
                baseInfo = JsonParseRobcashMoneyInfo.parseData(result);
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
