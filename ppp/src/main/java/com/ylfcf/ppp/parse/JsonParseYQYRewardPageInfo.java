package com.ylfcf.ppp.parse;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.YQYRewardPageInfo;
import com.ylfcf.ppp.entity.YqyRewardInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 元企盈A级以及以下用户好友的投资列表
 * Created by Administrator on 2017/11/24.
 */

public class JsonParseYQYRewardPageInfo {
    private BaseInfo baseInfo;
    private YQYRewardPageInfo mYQYRewardPageInfo;
    private List<YqyRewardInfo> yqyRewardInfos;

    public BaseInfo getBaseInfo() {
        return baseInfo;
    }

    /*
     * 解析list列表
     */
    public void parsePageInfo(String data) throws Exception{
        yqyRewardInfos = new ArrayList<YqyRewardInfo>();
        JSONArray array = new JSONArray(data);
        int size = array.length();
        for(int i = 0;i < size;i++){
            JSONObject object = array.getJSONObject(i);
            YqyRewardInfo info = (YqyRewardInfo) MainJson.fromJson(YqyRewardInfo.class, object);
            yqyRewardInfos.add(info);
        }
        mYQYRewardPageInfo.setYqyRewardInfoList(yqyRewardInfos);
    }

    /*
     * 解析msg字段
     */
    public void parseMsg(String result) throws Exception {
        JSONObject object = null;
        object = new JSONObject(result);
        if(object != null){
            mYQYRewardPageInfo = (YQYRewardPageInfo) MainJson.fromJson(YQYRewardPageInfo.class, object);
            parsePageInfo(mYQYRewardPageInfo.getList());
            baseInfo.setmYQYRewardPageInfo(mYQYRewardPageInfo);
        }
    }

    /**
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
            baseInfo = (BaseInfo) MainJson.fromJson(BaseInfo.class, object);
            int resultCode = SettingsManager.getResultCode(baseInfo);
            if(resultCode == 0)
            parseMsg(baseInfo.getMsg());
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
        JsonParseYQYRewardPageInfo jsonParse = new JsonParseYQYRewardPageInfo();
        jsonParse.parseMain(result);
        return jsonParse.getBaseInfo();
    }
}
