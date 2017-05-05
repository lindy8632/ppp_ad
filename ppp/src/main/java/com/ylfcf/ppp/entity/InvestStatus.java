package com.ylfcf.ppp.entity;

/**
 * Created by Administrator on 2017/4/11.
 */

public class InvestStatus implements java.io.Serializable{
    private String status;
    private String msg;

    public InvestStatus() {
    }

    public String getMsg() {
        return msg;
    }

    public String getStatus() {
        return status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
