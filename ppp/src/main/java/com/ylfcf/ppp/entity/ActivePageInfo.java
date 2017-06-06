package com.ylfcf.ppp.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */

public class ActivePageInfo implements java.io.Serializable{
    private String list;
    private String total;
    private List<ActiveInfo> activeList;

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ActiveInfo> getActiveList() {
        return activeList;
    }

    public void setActiveList(List<ActiveInfo> activeList) {
        this.activeList = activeList;
    }
}
