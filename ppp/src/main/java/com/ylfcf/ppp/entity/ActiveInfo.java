package com.ylfcf.ppp.entity;

/**
 * »î¶¯
 * Created by Administrator on 2017/5/16.
 */

public class ActiveInfo implements java.io.Serializable {
    private String active_title;
    private String name;
    private String money;
    private String type;
    private String status;
    private String start_time;
    private String end_time;
    private String use_start_time;
    private String use_end_time;
    private String need_invest_money;
    private String where;
    private String url_link;
    private String pic;
    private String pic_show_status;
    private String add_time;
    private String update_time;
    private String active_status;
    private ActiveStatus mActiveStatus;

    public String getActive_title() {
        return active_title;
    }

    public void setActive_title(String active_title) {
        this.active_title = active_title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getUse_start_time() {
        return use_start_time;
    }

    public void setUse_start_time(String use_start_time) {
        this.use_start_time = use_start_time;
    }

    public String getUse_end_time() {
        return use_end_time;
    }

    public void setUse_end_time(String use_end_time) {
        this.use_end_time = use_end_time;
    }

    public String getNeed_invest_money() {
        return need_invest_money;
    }

    public void setNeed_invest_money(String need_invest_money) {
        this.need_invest_money = need_invest_money;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getUrl_link() {
        return url_link;
    }

    public void setUrl_link(String url_link) {
        this.url_link = url_link;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic_show_status() {
        return pic_show_status;
    }

    public void setPic_show_status(String pic_show_status) {
        this.pic_show_status = pic_show_status;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getActive_status() {
        return active_status;
    }

    public void setActive_status(String active_status) {
        this.active_status = active_status;
    }

    public ActiveStatus getmActiveStatus() {
        return mActiveStatus;
    }

    public void setmActiveStatus(ActiveStatus mActiveStatus) {
        this.mActiveStatus = mActiveStatus;
    }
}
