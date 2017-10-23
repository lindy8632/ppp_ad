package com.ylfcf.ppp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.fragment.AccountCenterHKRLFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/10.
 */

public class DateWheelPopupwindow extends PopupWindow implements
        View.OnClickListener {
    private Activity context;
    private WindowManager.LayoutParams lp = null;
    private TextView cancelBtn,okBtn;
    private WheelView mYearWheelView;
    private WheelView mMonthWheelView;
    private AccountCenterHKRLFragment.OnDatePopwindowOkListener mOnDatePopwindowOkListener;
    private int yearInt,thisYearInt,monthInt;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

    public DateWheelPopupwindow(Context context, View convertView,
                                AccountCenterHKRLFragment.OnDatePopwindowOkListener mOnDatePopwindowOkListener,
                                int yearInt,int thisYearInt,int monthInt) {
        super(convertView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        this.context = (Activity) context;
        this.yearInt = yearInt;
        this.thisYearInt = thisYearInt;
        this.monthInt = monthInt;
        this.mOnDatePopwindowOkListener = mOnDatePopwindowOkListener;
        findViews(convertView);
    }

    private void findViews(View popView) {
        lp = context.getWindow().getAttributes();
        lp.alpha = 0.4f;
        context.getWindow().setAttributes(lp);

        cancelBtn = (TextView)popView.findViewById(R.id.date_wheel_popwindow_cancel_btn);
        cancelBtn.setOnClickListener(this);
        okBtn = (TextView)popView.findViewById(R.id.date_wheel_popwindow_ok_btn);
        okBtn.setOnClickListener(this);
        mYearWheelView = (WheelView) popView.findViewById(R.id.date_wheel_popwindow_yearwheelview);
        mMonthWheelView = (WheelView) popView.findViewById(R.id.date_wheel_popwindow_monthwheelview);
        initData();
    }

    public void show(View parentView) {
        this.setBackgroundDrawable(new PaintDrawable(R.color.transparent)); // 使得返回键有效
        this.setAnimationStyle(R.style.bidPopwindowStyle);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }

    private void initData(){
        mYearWheelView.setWheelAdapter(new ArrayWheelAdapter(context));
        mYearWheelView.setSkin(WheelView.Skin.Holo);
        mYearWheelView.setWheelData(createYearDatas());
        mYearWheelView.setWheelSize(5);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextSize = 18;
        style.textSize = 14;
        style.selectedTextColor = context.getResources().getColor(R.color.common_topbar_bg_color);
        style.textColor = context.getResources().getColor(R.color.gray1);
        mYearWheelView.setStyle(style);
        mYearWheelView.setSelection(yearInt - 2012);

        mMonthWheelView.setWheelAdapter(new ArrayWheelAdapter(context));
        mMonthWheelView.setSkin(WheelView.Skin.Holo);
        mMonthWheelView.setWheelData(createMonthDatas());
        mMonthWheelView.setWheelSize(5);
        WheelView.WheelViewStyle styleM = new WheelView.WheelViewStyle();
        styleM.selectedTextSize = 18;
        styleM.textSize = 14;
        styleM.selectedTextColor = context.getResources().getColor(R.color.common_topbar_bg_color);
        styleM.textColor = context.getResources().getColor(R.color.gray1);
        mMonthWheelView.setStyle(styleM);
        mMonthWheelView.setSelection(monthInt-1);
    }

    private ArrayList<String> createYearDatas(){
        ArrayList<String> yearList = new ArrayList<>();
        int curYearI = this.thisYearInt;
        int fromYearI = 2012;
        int endYearI = 2030;
        if(curYearI <= fromYearI){
            endYearI = 2030;
        }else{
            endYearI = curYearI + 10;
        }
        for(int i=fromYearI;i<=endYearI;i++){
            yearList.add(i+"年");
        }
        return yearList;
    }

    private ArrayList<String> createMonthDatas(){
        ArrayList<String> monthList = new ArrayList<>();
        for(int i=1;i<=12;i++){
            monthList.add(i+"月");
        }
        return monthList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_wheel_popwindow_cancel_btn:
                this.dismiss();
                break;
            case R.id.date_wheel_popwindow_ok_btn:
                mOnDatePopwindowOkListener.onClickListener((String)mYearWheelView.getSelectionItem(),
                        (String)mMonthWheelView.getSelectionItem());
                this.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        lp.alpha = 1.0f;
        context.getWindow().setAttributes(lp);
    }
}
