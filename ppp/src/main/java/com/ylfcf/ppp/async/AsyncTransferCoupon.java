package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.parse.JsonParseCommon;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * 转让多张加息券
 * Created by Administrator on 2017/7/7.
 */

public class AsyncTransferCoupon extends AsyncTaskBase {
    private Context context;

    private String userId;
    private String couponIds;

    private Inter.OnCommonInter onCommonInter;
    private BaseInfo baseInfo;

    public AsyncTransferCoupon(Context context, String userId,String couponIds,
                                    Inter.OnCommonInter onCommonInter) {
        this.context = context;
        this.userId = userId;
        this.couponIds = couponIds;
        this.onCommonInter = onCommonInter;
    }

    @Override
    protected String doInBackground(String... params) {
        String url[] = null;
        String result = null;
        try {
            url = URLGenerator.getInstance().getTransferCouponURL(userId,couponIds);
            if (result == null) {
                result = HttpConnection.postConnection(url[0], url[1]);
            }
            YLFLogger.d("转让加息券url:"+url[0]+"\n参数："+url[1]+"\nresult:"+result);
            if (result == null) {
                result = BackType.FAILE;
            } else {
                baseInfo = JsonParseCommon.parseData(result);
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
