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
 * Created by Administrator on 2017/4/26.
 */

public class AsyncRobcashIsReceiveStatus extends AsyncTaskBase {
    private Context context;
    private BaseInfo baseInfo;
    private String userId;
    private String ativeTitle;
    private Inter.OnCommonInter mOnCommonInter;
    public AsyncRobcashIsReceiveStatus(Context context, String userId,String activeTitle,Inter.OnCommonInter mOnCommonInter) {
        this.context = context;
        this.userId = userId;
        this.ativeTitle = activeTitle;
        this.mOnCommonInter = mOnCommonInter;
    }

    @Override
    protected String doInBackground(String... params) {
        String url[] = null;
        String result = null;
        try {
            url = URLGenerator.getInstance().getHDRobCashCheckIsReceiveURL(userId,ativeTitle);
            if (result == null) {
                result = HttpConnection.postConnection(url[0], url[1]);
            }
            YLFLogger.d("抢现金接口："+url[0]+"-----------"+url[1]+"--------------"+result);
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
