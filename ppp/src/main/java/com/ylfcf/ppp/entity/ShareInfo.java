package com.ylfcf.ppp.entity;

/**
 * Created by Administrator on 2017/5/23.
 */

public class ShareInfo implements java.io.Serializable {
    private String title;
    private String content;
    private String sharePicURL;
    private String activeURL;
    private String name;
    private String activeTitle;

    public String getActiveURL() {
        return activeURL;
    }

    public void setActiveURL(String activeURL) {
        this.activeURL = activeURL;
    }

    public String getActiveTitle() {
        return activeTitle;
    }

    public void setActiveTitle(String activeTitle) {
        this.activeTitle = activeTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSharePicURL() {
        return sharePicURL;
    }

    public void setSharePicURL(String sharePicURL) {
        this.sharePicURL = sharePicURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
