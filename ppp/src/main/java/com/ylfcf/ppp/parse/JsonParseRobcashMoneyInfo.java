package com.ylfcf.ppp.parse;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.RobcashMoneyInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/4/26.
 */

public class JsonParseRobcashMoneyInfo {
    private BaseInfo baseInfo;
    private RobcashMoneyInfo mRobcashMoneyInfo;

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
            mRobcashMoneyInfo = (RobcashMoneyInfo) MainJson.fromJson(RobcashMoneyInfo.class, object);
            baseInfo.setmRobcashMoneyInfo(mRobcashMoneyInfo);
        }
    }

    /**
     * 开始解析
     * @param result
     * @throws Exception
     */
    public void parseMain(String result) throws Exception{
        JSONObject object = null;
        try {
            object = new JSONObject(result);
        } catch (Exception e) {
        }

        if(object != null) {
            baseInfo = (BaseInfo)MainJson.fromJson(BaseInfo.class, object);
            int resultCode = SettingsManager.getResultCode(baseInfo);
            if(resultCode == 0 || resultCode == 504){
                //504是抢现金 如果已经抢过了。
                parseMsg(baseInfo.getMsg());
            }
        }
    }

    /**
     * 解析调用接口
     * @param result
     * @return
     */
    public static BaseInfo parseData(String result) throws Exception {
        JsonParseRobcashMoneyInfo jsonParse = new JsonParseRobcashMoneyInfo();
        jsonParse.parseMain(result);
        return jsonParse.getBaseInfo();
    }
}
