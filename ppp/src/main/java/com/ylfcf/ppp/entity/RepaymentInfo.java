package com.ylfcf.ppp.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/8/18.
 */

public class RepaymentInfo implements java.io.Serializable{

    private String repayment_date_list;//回款日期
    private String repayment_total_list;//当天的回款金额
    private String repayment_count_list;//当天回款笔数
    private String wdy_date_list;//薪盈计划
    private List<String> repaymentDateList;
    private List<String> repaymentCurDayMoneyList;
    private List<String> repaymentCurDayCountList;
    private List<String> wdyDateList;

    public String getRepayment_date_list() {
        return repayment_date_list;
    }

    public void setRepayment_date_list(String repayment_date_list) {
        this.repayment_date_list = repayment_date_list;
    }

    public String getRepayment_total_list() {
        return repayment_total_list;
    }

    public void setRepayment_total_list(String repayment_total_list) {
        this.repayment_total_list = repayment_total_list;
    }

    public String getRepayment_count_list() {
        return repayment_count_list;
    }

    public void setRepayment_count_list(String repayment_count_list) {
        this.repayment_count_list = repayment_count_list;
    }

    public String getWdy_date_list() {
        return wdy_date_list;
    }

    public void setWdy_date_list(String wdy_date_list) {
        this.wdy_date_list = wdy_date_list;
    }

    public List<String> getRepaymentDateList() {
        return repaymentDateList;
    }

    public void setRepaymentDateList(List<String> repaymentDateList) {
        this.repaymentDateList = repaymentDateList;
    }

    public List<String> getRepaymentCurDayMoneyList() {
        return repaymentCurDayMoneyList;
    }

    public void setRepaymentCurDayMoneyList(List<String> repaymentCurDayMoneyList) {
        this.repaymentCurDayMoneyList = repaymentCurDayMoneyList;
    }

    public List<String> getRepaymentCurDayCountList() {
        return repaymentCurDayCountList;
    }

    public void setRepaymentCurDayCountList(List<String> repaymentCurDayCountList) {
        this.repaymentCurDayCountList = repaymentCurDayCountList;
    }

    public List<String> getWdyDateList() {
        return wdyDateList;
    }

    public void setWdyDateList(List<String> wdyDateList) {
        this.wdyDateList = wdyDateList;
    }
}
