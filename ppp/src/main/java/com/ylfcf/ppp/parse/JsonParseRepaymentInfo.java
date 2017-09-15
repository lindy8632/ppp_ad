package com.ylfcf.ppp.parse;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.RepaymentInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 回款日历
 * Created by Administrator on 2017/8/18.
 */

public class JsonParseRepaymentInfo {
    private BaseInfo baseInfo;
    private RepaymentInfo mRepaymentInfo;
    private List<String> repaymentDateList;
    private List<String> repaymentCurDayMoneyList;
    private List<String> repaymentCurDayCountList;
    private List<String> wdyDateList;

    public BaseInfo getBaseInfo() {
        return baseInfo;
    }

    /**
     * @param data
     */
    private void parseRepaymentDateList(String data){
        repaymentDateList = new ArrayList<String>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            int size = jsonArray.length();
            for(int i=0;i<size;i++){
                String date = jsonArray.getString(i);
                repaymentDateList.add(date);
            }
            mRepaymentInfo.setRepaymentDateList(repaymentDateList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param data
     */
    private void parseRepaymentCurDayMoneyList(String data){
        repaymentCurDayMoneyList = new ArrayList<String>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            int size = jsonArray.length();
            for(int i=0;i<size;i++){
                String money = jsonArray.getString(i);
                repaymentCurDayMoneyList.add(money);
            }
            mRepaymentInfo.setRepaymentCurDayMoneyList(repaymentCurDayMoneyList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param data
     */
    private void parseRepaymentCurDayCountList(String data){
        repaymentCurDayCountList = new ArrayList<String>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            int size = jsonArray.length();
            for(int i=0;i<size;i++){
                String count = jsonArray.getString(i);
                repaymentCurDayCountList.add(count);
            }
            mRepaymentInfo.setRepaymentCurDayCountList(repaymentCurDayCountList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param data
     */
    private void parseWayDateList(String data){
        wdyDateList = new ArrayList<String>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            int size = jsonArray.length();
            for(int i=0;i<size;i++){
                String wdyDate = jsonArray.getString(i);
                wdyDateList.add(wdyDate);
            }
            mRepaymentInfo.setWdyDateList(wdyDateList);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                mRepaymentInfo = (RepaymentInfo) MainJson.fromJson(RepaymentInfo.class, object);
                parseRepaymentDateList(mRepaymentInfo.getRepayment_date_list());
                parseRepaymentCurDayMoneyList(mRepaymentInfo.getRepayment_total_list());
                parseRepaymentCurDayCountList(mRepaymentInfo.getRepayment_count_list());
                parseWayDateList(mRepaymentInfo.getWdy_date_list());
            }
            baseInfo.setmRepaymentInfo(mRepaymentInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始解析..
     * @param result
     * @throws Exception
     */
    public void parseMain(String result){
        JSONObject object = null;
        try {
            object = new JSONObject(result);
            if(object != null){
                baseInfo = (BaseInfo) MainJson.fromJson(BaseInfo.class, object);
                int resultCode = SettingsManager.getResultCode(baseInfo);
                if(resultCode == 0){
                    parseMsg(baseInfo.getMsg());
                }
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
        JsonParseRepaymentInfo jsonParse = new JsonParseRepaymentInfo();
        jsonParse.parseMain(result);
        return jsonParse.getBaseInfo();
    }
}
