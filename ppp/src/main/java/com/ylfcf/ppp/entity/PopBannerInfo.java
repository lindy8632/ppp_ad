package com.ylfcf.ppp.entity;

/**
 * Created by Administrator on 2017/6/26.
 */

public class PopBannerInfo implements java.io.Serializable {
    private static final long serialVersionUID = 7256471238090800632L;

    private String id;
    private String active_title;
    private String where;
    private String url_link;
    private String pic;
    private String pic_show_status;
    private String active_name;
    private String start_time;
    private String end_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActive_title() {
        return active_title;
    }

    public void setActive_title(String active_title) {
        this.active_title = active_title;
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

    public String getActive_name() {
        return active_name;
    }

    public void setActive_name(String active_name) {
        this.active_name = active_name;
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
