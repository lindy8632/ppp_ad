package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.parse.JsonParseProductInfo;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.inter.Inter;

/**
 * 元聚盈
 * Created by Administrator on 2017/7/25.
 */

public class AsyncYJYBorrowDetails extends AsyncTaskBase{
    private Context context;

    private String borrowId;
    private Inter.OnCommonInter onProductInter;

    private BaseInfo baseInfo;

    public AsyncYJYBorrowDetails(Context context, String borrowId, Inter.OnCommonInter onProductInter) {
        this.context = context;
        this.borrowId = borrowId;
        this.onProductInter = onProductInter;
    }

    @Override
    protected String doInBackground(String... params) {
        String url[] = null;
        String result = null;
        try {
            url = URLGenerator.getInstance().getYGZXBorrowDetailsURL(borrowId);
            if (result == null) {
                result = HttpConnection.postConnection(url[0], url[1]);
            }

            if (result == null) {
                result = BackType.FAILE;
            } else {
                baseInfo = JsonParseProductInfo.parseData(result);
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
            onProductInter.back(null);
        } else if (BackType.FAILE.equals(result)) {
            // 获取失败
            onProductInter.back(null);
        } else {
            // 获取成功
            onProductInter.back(baseInfo);
        }
    }
}
