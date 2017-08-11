package com.ylfcf.ppp.parse;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.RedBagInfo;
import com.ylfcf.ppp.util.MainJson;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/8/1.
 */

public class JsonParseRedbagInfo {
    private BaseInfo baseInfo;
    private RedBagInfo redBagInfo;

    public BaseInfo getBaseInfo() {
        return baseInfo;
    }

    /**
     * 解析msg字段
     * @param result
     */
    public void parseMsg(String result) {
        JSONObject object = null;
        try {
            object = new JSONObject(result);
            if(object != null){
                redBagInfo = (RedBagInfo) MainJson.fromJson(RedBagInfo.class, object);
            }
            baseInfo.setmRedBagInfo(redBagInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param result
     * @throws Exception
     */
    public void parseMain(String result){
        JSONObject object = null;
        try {
            object = new JSONObject(result);
            if(object != null){
                baseInfo = (BaseInfo) MainJson.fromJson(BaseInfo.class, object);
                parseMsg(baseInfo.getMsg());
            }
        } catch (Exception e) {
        }
    }

    /**
     * 解析调用接口
     * @param result
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static BaseInfo parseData(String result) throws Exception {
        JsonParseRedbagInfo jsonParse = new JsonParseRedbagInfo();
        jsonParse.parseMain(result);
        return jsonParse.getBaseInfo();
    }
}
