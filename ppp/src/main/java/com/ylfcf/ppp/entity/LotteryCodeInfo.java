package com.ylfcf.ppp.entity;

/**
 * Created by Administrator on 2017/4/13.
 */

public class LotteryCodeInfo implements java.io.Serializable {
    private String id;
    private String lottery_id;
    private String lottery_code;
    private String status;
    private String add_time;
    private String user_id;

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLottery_code() {
        return lottery_code;
    }

    public void setLottery_code(String lottery_code) {
        this.lottery_code = lottery_code;
    }

    public String getLottery_id() {
        return lottery_id;
    }

    public void setLottery_id(String lottery_id) {
        this.lottery_id = lottery_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
