package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.parse.JsonParsePrizeInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * 微信公众号抽奖活动 --  奖品详情
 * Created by Administrator on 2017/4/11.
 */

public class AsyncLottery extends AsyncTaskBase {
    private Context context;

    private String id;
    private String name;
    private String type;
    private Inter.OnCommonInter onCommonInter;

    private BaseInfo baseInfo;

    public AsyncLottery(Context context, String id, String name,String type,
                      Inter.OnCommonInter onCommonInter) {
        this.context = context;
        this.id = id;
        this.name = name;
        this.type = type;
        this.onCommonInter = onCommonInter;
    }

    @Override
    protected String doInBackground(String... params) {
        String url[] = null;
        String result = null;
        try {
            url = URLGenerator.getInstance().getHDLotterySelectoneURL(id,name,type);
            YLFLogger.d("URL:" + url[0] + "\n" + "参数：" + url[1]);
            if (result == null) {
                result = HttpConnection.postConnection(url[0], url[1]);
            }

            if (result == null) {
                result = BackType.FAILE;
            } else {
                baseInfo = JsonParsePrizeInfo.parseData(result);
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
