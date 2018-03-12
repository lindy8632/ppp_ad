package com.ylfcf.ppp.entity;

/**
 * Created by Administrator on 2018/1/18.
 */

public class TLOrderInfo implements java.io.Serializable {
    private static final long serialVersionUID = -6272700963455667204L;

    private String order;
    private String add_time;
    private String account;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
