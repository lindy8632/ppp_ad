package com.ylfcf.ppp.entity;

/**
 * Created by Administrator on 2017/5/16.
 */

public class ActiveStatus implements java.io.Serializable{
    private String status;
    private String msg;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
