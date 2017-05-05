package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.parse.JsonParseLotteryCode;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

public class AsyncLotteryCode extends AsyncTaskBase {
    private Context context;

    private String lotteryId;
    private String userId;
    private Inter.OnCommonInter onCommonInter;
    private BaseInfo baseInfo;

    public AsyncLotteryCode(Context context, String lotteryId, String userId,
                        Inter.OnCommonInter onCommonInter) {
        this.context = context;
        this.lotteryId = lotteryId;
        this.userId = userId;
        this.onCommonInter = onCommonInter;
    }

    @Override
    protected String doInBackground(String... params) {
        String url[] = null;
        String result = null;
        try {
            url = URLGenerator.getInstance().getHDLotteryCodeURL(lotteryId,userId);
            if (result == null) {
                result = HttpConnection.postConnection(url[0], url[1]);
            }
            if (result == null) {
                result = BackType.FAILE;
            } else {
                baseInfo = JsonParseLotteryCode.parseData(result);
            }
            YLFLogger.d(result);
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
            onCommonInter.back(null);
        } else if (BackType.FAILE.equals(result)) {
            onCommonInter.back(null);
        } else {
            onCommonInter.back(baseInfo);
        }
    }
}