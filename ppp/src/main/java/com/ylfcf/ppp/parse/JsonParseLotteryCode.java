package com.ylfcf.ppp.parse;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.LotteryCodeInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 礼品的兑换码
 * Created by Administrator on 2017/4/13.
 */

public class JsonParseLotteryCode {
    private BaseInfo baseInfo;
    private LotteryCodeInfo lotteryCodeInfo;

    public BaseInfo getBaseInfo() {
        return baseInfo;
    }

    /**
     * 解析msg字段
     * @param result
     * @throws Exception
     */
    public void parseMsg(String result) throws Exception {
        JSONObject object = null;
        object = new JSONObject(result);
        if(object != null){
            lotteryCodeInfo = (LotteryCodeInfo) MainJson.fromJson(LotteryCodeInfo.class, object);
            baseInfo.setmLotteryCodeInfo(lotteryCodeInfo);
        }
    }

    public void parseMain(String result) throws Exception{
        JSONObject object = null;
        try {
            object = new JSONObject(result);
        } catch (Exception e) {
        }

        if(object != null) {
            baseInfo = (BaseInfo)MainJson.fromJson(BaseInfo.class, object);
            int resultCode = SettingsManager.getResultCode(baseInfo);
            if(resultCode == 0){
                parseMsg(baseInfo.getMsg());
            }
        }
    }

    /**
     * 解析调用接口
     * @param result
     * @return
     * @throws JSONException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static BaseInfo parseData(String result) throws Exception {
        JsonParseLotteryCode jsonParse = new JsonParseLotteryCode();
        jsonParse.parseMain(result);
        return jsonParse.getBaseInfo();
    }
}
