package com.example.ylfcf.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ylfcf.utils.ExpCalendarUtil;

/**
 * Created by Administrator on 2017/8/17.
 */

public class WeekColumnView extends LinearLayout {
    private int backgroundColor = Color.rgb(240,255,255);
    private int startendTextColor = Color.rgb(132, 130, 132);
    private int midTextColor = Color.rgb(132, 130, 132);

    public WeekColumnView(Context context) {
        super(context);
        init();
    }

    public WeekColumnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        initLayout();
        initViews();
    }

    private void initLayout() {
        this.setOrientation(HORIZONTAL);
        this.setBackgroundColor(backgroundColor);
    }


    private TextView[] textView = new TextView[7];

    private void initViews() {
        LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        lp.topMargin = 10;
        lp.bottomMargin = 10;

        textView[0] = new TextView(getContext());
        textView[0].setText(ExpCalendarUtil.number2Week(7));
        textView[0].setTextColor(startendTextColor);
        textView[0].setGravity(Gravity.CENTER);
        this.addView(textView[0], lp);

        for (int i = 1; i < 6; i++) {
            textView[i] = new TextView(getContext());
            textView[i].setGravity(Gravity.CENTER);
            textConfig(textView[i], ExpCalendarUtil.number2Week(i), midTextColor);
            this.addView(textView[i], lp);
        }

        textView[6] = new TextView(getContext());
        textView[6].setText(ExpCalendarUtil.number2Week(6));
        textView[6].setTextColor(startendTextColor);
        textView[6].setGravity(Gravity.CENTER);
        this.addView(textView[6], lp);
    }

    /**
     * is this nessesary ?
     *
     * @param textview
     * @param text
     * @param textColor
     */
    private void textConfig(TextView textview, String text, int textColor) {
        textview.setText(text);
        textview.setTextColor(textColor);
    }
}
