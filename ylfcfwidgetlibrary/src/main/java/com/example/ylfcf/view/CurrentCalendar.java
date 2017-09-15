package com.example.ylfcf.view;

import com.example.ylfcf.info.DateData;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/8/17.
 */

public class CurrentCalendar {
    public static DateData getCurrentDateData(){
        Calendar calendar = Calendar.getInstance();
        return new DateData(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH) + 1, calendar.get(calendar.DAY_OF_MONTH));
    }
}
