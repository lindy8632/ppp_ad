package com.ylfcf.ppp.parse;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.FriendInfo;
import com.ylfcf.ppp.entity.FriendsPageInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的好友列表
 * Created by Administrator on 2017/7/5.
 */

public class JsonParseMyFriendsPageInfo {
    private BaseInfo baseInfo;
    private FriendsPageInfo pageInfo;
    private List<FriendInfo> infoList;

    public BaseInfo getBaseInfo() {
        return baseInfo;
    }

    /**
     * 推广收益的列表
     * @param data
     */
    private void parseFriendInfoList(String data) {
        infoList = new ArrayList<FriendInfo>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            int size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                FriendInfo info = (FriendInfo) MainJson
                        .fromJson(FriendInfo.class, object);
                infoList.add(info);
            }
            pageInfo.setFriendList(infoList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 解析Msg字段
     * @param result
     * @throws Exception
     */
    public void parseMsg(String result) throws Exception {
        JSONObject object = null;
        object = new JSONObject(result);
        if (object != null) {
            pageInfo = (FriendsPageInfo) MainJson.fromJson(
                    FriendsPageInfo.class, object);
            parseFriendInfoList(pageInfo.getExtension_user_list());
            baseInfo.setmFriendsPageInfo(pageInfo);
        }
    }

    /**
     * @param result
     * @throws Exception
     */
    public void parseMain(String result) throws Exception {
        JSONObject object = null;
        try {
            object = new JSONObject(result);
        } catch (Exception e) {
        }

        if (object != null) {
            baseInfo = (BaseInfo) MainJson.fromJson(BaseInfo.class, object);
            int resultCode = SettingsManager.getResultCode(baseInfo);
            if(resultCode == 0)
                parseMsg(baseInfo.getMsg());
        }
    }

    /**
     * 解析调用接口
     *
     * @param result
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static BaseInfo parseData(String result) throws Exception {
        JsonParseMyFriendsPageInfo jsonParse = new JsonParseMyFriendsPageInfo();
        jsonParse.parseMain(result);
        return jsonParse.getBaseInfo();
    }
}
