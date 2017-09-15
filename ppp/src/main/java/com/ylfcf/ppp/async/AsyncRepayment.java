package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.parse.JsonParseRepaymentInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * 回款信息
 * Created by Administrator on 2017/8/18.
 */

public class AsyncRepayment extends AsyncTaskBase{
    private Context context;

    private String userId;

    private Inter.OnCommonInter mOnCommonInter;

    private BaseInfo baseInfo;

    public AsyncRepayment(Context context, String userId,
                          Inter.OnCommonInter mOnCommonInter) {
        this.context = context;
        this.userId = userId;
        this.mOnCommonInter = mOnCommonInter;
    }

    @Override
    protected String doInBackground(String... params) {
        String url[] = null;
        String result = null;
        try {
            url = URLGenerator.getInstance().getAccountLogRepaymentURL(userId);
            YLFLogger.d("注册url:"+url[0]+url[1]);
            if (result == null) {
                result = HttpConnection.postConnection(url[0], url[1]);
            }

            if (result == null) {
                result = BackType.FAILE;
            } else {
                baseInfo = JsonParseRepaymentInfo.parseData(result);
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
