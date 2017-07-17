package com.ylfcf.ppp.entity;

/**
 * Created by Administrator on 2017/7/5.
 */

public class FriendInfo implements java.io.Serializable {
    private static final long serialVersionUID = -8159007884124039125L;

    private String id;
    private String phone;
    private String mobile;
    private String real_name;
    private String sales_phone;
    private String u_name;
    private String status;
    private String reg_time;
    private String is_invest;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getSales_phone() {
        return sales_phone;
    }

    public void setSales_phone(String sales_phone) {
        this.sales_phone = sales_phone;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReg_time() {
        return reg_time;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public String getIs_invest() {
        return is_invest;
    }

    public void setIs_invest(String is_invest) {
        this.is_invest = is_invest;
    }
}
