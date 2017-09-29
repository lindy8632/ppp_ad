package com.example.ylfcf.info;

/**
 * Created by Administrator on 2017/8/17.
 */

public class DayData {
    private DateData date;
    private int textColor;
    private int textSize;
    private boolean enabled;

    public DayData(DateData date){
        this.date = date;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public String getText(){
        return "" + date.getDay();
    }

    public DateData getDate(){
        return date;
    }
}
