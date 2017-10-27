package com.ylfcf.ppp.entity;

/**
 * Created by Administrator on 2017/4/11.
 */

public class InvestResultInfo implements java.io.Serializable{
    private String msg;
    private String daily_prize_res;
    private String red_bag_value;//红包值
    private InvestStatus mInvestStatus;//投资状态

    public InvestResultInfo() {
    }

    public String getDaily_prize_res() {
        return daily_prize_res;
    }

    public InvestStatus getmInvestStatus() {
        return mInvestStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setDaily_prize_res(String daily_prize_res) {
        this.daily_prize_res = daily_prize_res;
    }

    public void setmInvestStatus(InvestStatus mInvestStatus) {
        this.mInvestStatus = mInvestStatus;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRed_bag_value() {
        return red_bag_value;
    }

    public void setRed_bag_value(String red_bag_value) {
        this.red_bag_value = red_bag_value;
    }
}
