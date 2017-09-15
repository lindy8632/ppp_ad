package com.example.ylfcf.info;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Administrator on 2017/8/17.
 */

public class MarkedDates extends Observable {
    private static MarkedDates staticInstance;
    private ArrayList<DateData> data;

    private MarkedDates(){
        super();
        data = new ArrayList<>();
    }

    public static MarkedDates getInstance(){
        if (staticInstance == null)
            staticInstance = new MarkedDates();
        return staticInstance;
    }

    public MarkStyle check(DateData date){
        int index = data.indexOf(date);
        if (index == -1) {
            return null;
        }
        return data.get(index).getMarkStyle();
    }

    public boolean remove(DateData date){
        this.setChanged();
        this.notifyObservers();
        return data.remove(date);

    }

    public MarkedDates add(DateData dateData){
        data.add(dateData);
        this.setChanged();
        this.notifyObservers();
        return this;
    }


    public ArrayList<DateData> getAll(){
        return data;
    }

    public MarkedDates removeAdd(){
        data.clear();
        this.setChanged();
        this.notifyObservers();
        return this;
    }
}
