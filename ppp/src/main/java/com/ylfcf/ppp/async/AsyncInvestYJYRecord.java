package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.parse.JsonParseInvestRecordList;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;

/**
 * 员工专属产品投资记录
 * Created by Administrator on 2017/7/26.
 */

public class AsyncInvestYJYRecord extends AsyncTaskBase {
    private Context context;

    private String investUserId;
    private String borrowId;
    private int pageNo;
    private int pageSize;
    private OnCommonInter mOnCommonInter;

    private BaseInfo baseInfo;

    public AsyncInvestYJYRecord(Context context, String investUserId,
                                 String borrowId, int pageNo, int pageSize,
                                 OnCommonInter mOnCommonInter) {
        this.context = context;
        this.investUserId = investUserId;
        this.borrowId = borrowId;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.mOnCommonInter = mOnCommonInter;
    }

    @Override
    protected String doInBackground(String... params) {
        String url[] = null;
        String result = null;
        try {
            url = URLGenerator.getInstance()
                    .getYGZXBorrowInvestRecordURL(investUserId,borrowId,String.valueOf(pageNo),String.valueOf(pageSize));
            if (result == null) {
                result = HttpConnection.postConnection(url[0], url[1]);
            }

            if (result == null) {
                result = BackType.FAILE;
            } else {
                baseInfo = JsonParseInvestRecordList.parseData(result);
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
