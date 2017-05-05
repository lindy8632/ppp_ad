package com.ylfcf.ppp.entity;

/**
 * Created by Administrator on 2017/4/26.
 */

public class RobcashMoneyInfo implements java.io.Serializable {
    private String id;
    private String show_money;
    private String money;//本周可抢金额
    private String red_bag_num;
    private String use_red_bag_num;
    private String use_money;
    private String add_time;
    private String start_time;
    private String end_time;
    private String _show_money;
    private String next_week_money;//下周待抢金额
    private String prize;

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String get_show_money() {
        return _show_money;
    }

    public void set_show_money(String _show_money) {
        this._show_money = _show_money;
    }

    public String getNext_week_money() {
        return next_week_money;
    }

    public void setNext_week_money(String next_week_money) {
        this.next_week_money = next_week_money;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShow_money() {
        return show_money;
    }

    public void setShow_money(String show_money) {
        this.show_money = show_money;
    }

    public String getRed_bag_num() {
        return red_bag_num;
    }

    public void setRed_bag_num(String red_bag_num) {
        this.red_bag_num = red_bag_num;
    }

    public String getUse_red_bag_num() {
        return use_red_bag_num;
    }

    public void setUse_red_bag_num(String use_red_bag_num) {
        this.use_red_bag_num = use_red_bag_num;
    }

    public String getUse_money() {
        return use_money;
    }

    public void setUse_money(String use_money) {
        this.use_money = use_money;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
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
}
