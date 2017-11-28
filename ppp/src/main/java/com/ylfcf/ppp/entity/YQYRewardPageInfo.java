package com.ylfcf.ppp.entity;

import java.util.List;

/**
 * 元企盈体系A级及以下用户的投资列表
 * Created by Administrator on 2017/11/24.
 */

public class YQYRewardPageInfo implements java.io.Serializable {

    private static final long serialVersionUID = 5030345335754009655L;

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getOne_level() {
        return one_level;
    }

    public void setOne_level(String one_level) {
        this.one_level = one_level;
    }

    public String getSecond_level() {
        return second_level;
    }

    public void setSecond_level(String second_level) {
        this.second_level = second_level;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<YqyRewardInfo> getYqyRewardInfoList() {
        return yqyRewardInfoList;
    }

    public void setYqyRewardInfoList(List<YqyRewardInfo> yqyRewardInfoList) {
        this.yqyRewardInfoList = yqyRewardInfoList;
    }

    private String list;
    private String total_money;
    private String one_level;
    private String second_level;
    private String total;
    private List<YqyRewardInfo> yqyRewardInfoList;

}
