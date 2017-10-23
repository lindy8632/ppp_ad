package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.ylfcf.info.DateData;
import com.example.ylfcf.info.MarkStyle;
import com.example.ylfcf.inter.OnExpDateClickListener;
import com.example.ylfcf.inter.OnMonthScrollListener;
import com.example.ylfcf.widget.ExpCalendarView;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncRepayment;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.RepaymentInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.ui.AccountCenterActivity;
import com.ylfcf.ppp.ui.UserInvestRecordActivity;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.view.DateWheelPopupwindow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ylfcf.ppp.R.id.account_center_hkrl_date_tv;

/**
 * 账户中心 -- 回款日历
 * Created by Administrator on 2017/8/2.
 */

public class AccountCenterHKRLFragment extends BaseFragment implements OnClickListener {
    private static final String className = "AccountCenterHKRLFragment";
    private static final int REQUEST_REPAYMENT_WHAT = 8945;
    private static final int REQUEST_REPAYMENT_SUC = 8946;
    private Map<String,RepaymentDayData> repaymentDayDataMap = new HashMap<String,RepaymentDayData>();

    private AccountCenterActivity mainActivity;
    private View rootView;
    private ScrollView mainLayout;

    private ImageView arrowLeftImg,arrowRightImg;
    private TextView curMonthTV;//当前月份
    private TextView hkTotalMoneyTV;//回款总额
    private ExpCalendarView mExpCalendarView;//日历控件

    private RelativeLayout hkInfoLayout;//某日回款信息
    private LinearLayout hkInfoNorLayout;
    private TextView curDateTV;//当前日期
    private TextView hkCountTV;//当月还款笔数
    private TextView hkMoneyTV;//当日回款金额
    private LinearLayout hkInfoWdyLayout;
    private TextView curDateWdyTV;
    private TextView hkCountWdyTV;
    private Button catInvestRecordBtn;//查看回款日历

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月");
    private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
    private SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMdd");
    private List<String> repaymentDateList = new ArrayList<>();//回款日期 yyyyMMdd
    private List<String> repaymentCurDayMoneyList = new ArrayList<>();//当日回款金额
    private List<String> repaymentCurDayCountList = new ArrayList<>();//当日回款笔数
    private List<String> wdyDateList = new ArrayList<>();//薪盈计划回款日期
    private int curYear = 2017;//当前的年份
    private int thisYear = 2017;//今年的年份
    private int curMonth = 1;//当前的月份


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REQUEST_REPAYMENT_WHAT:
                    requestRepaymentInfo(SettingsManager.getUserId(mainActivity));
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (AccountCenterActivity) getActivity();
        if(rootView==null){
            rootView = inflater.inflate(R.layout.account_center_hkrl_fragment, null);
            findViews(rootView,inflater);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        handler.sendEmptyMessage(REQUEST_REPAYMENT_WHAT);
        return rootView;
    }

    private void findViews(View rootView,LayoutInflater layoutInflater){
        arrowLeftImg = (ImageView)rootView.findViewById(R.id.account_center_hkrl_arrow_left);
        arrowLeftImg.setOnClickListener(this);
        arrowRightImg = (ImageView)rootView.findViewById(R.id.account_center_hkrl_arrow_right);
        arrowRightImg.setOnClickListener(this);
        curMonthTV = (TextView) rootView.findViewById(account_center_hkrl_date_tv);
        curMonthTV.setOnClickListener(this);
        hkTotalMoneyTV = (TextView) rootView.findViewById(R.id.account_center_hkrl_curmonth_totalmoney);
        mExpCalendarView = (ExpCalendarView) rootView.findViewById(R.id.account_center_hkrl_calendarview);
        hkInfoLayout = (RelativeLayout) rootView.findViewById(R.id.account_center_hkrl_info_layout);
        hkInfoNorLayout = (LinearLayout) rootView.findViewById(R.id.account_center_hkrl_info_layout_nor);
        curDateTV = (TextView) rootView.findViewById(R.id.account_center_hkrl_hkinfo_cur_date_tv);
        hkCountTV = (TextView) rootView.findViewById(R.id.account_center_hkrl_hkinfo_count);
        hkMoneyTV = (TextView) rootView.findViewById(R.id.account_center_hkrl_hkinfo_money);
        hkInfoWdyLayout = (LinearLayout) rootView.findViewById(R.id.account_center_hkrl_info_layout_wdy);
        curDateWdyTV = (TextView) rootView.findViewById(R.id.account_center_hkrl_hkinfo_cur_date_tv_wdy);
        hkCountWdyTV = (TextView) rootView.findViewById(R.id.account_center_hkrl_hkinfo_count_wdy);
        catInvestRecordBtn = (Button) rootView.findViewById(R.id.account_center_hkrl_cat_invest_record_btn);
        catInvestRecordBtn.setOnClickListener(this);
        mainLayout = (ScrollView) rootView.findViewById(R.id.account_center_hkrl_fragment_mainlayout);

        initListners();
        initCurMonthData("");
    }

    private void initListners(){
        mExpCalendarView.setOnDateClickListener(new OnExpDateClickListener(){
            @Override
            public void onDateClick(View view, DateData date) {
                super.onDateClick(view, date);
                updateCurDayRepayment(date);
            }
        }).setOnMonthScrollListener(new OnMonthScrollListener() {
            @Override
            public void onMonthChange(int year, int month) {
                curMonthTV.setText(String.format("%d年%d月", year, month));
                hkInfoLayout.setVisibility(View.GONE);
                if(month < 10){
                    getCurMonthRepayment(new StringBuffer().append(year).append("0").append(month).toString());
                }else{
                    getCurMonthRepayment(new StringBuffer().append(year).append(month).toString());
                }
                curYear = year;
                curMonth = month;
            }

            @Override
            public void onMonthScroll(float positionOffset) {
                YLFLogger.i("listener", "onMonthScroll:" + positionOffset);
            }
        });
    }

    private void updateCurDayRepayment(DateData data){
        if(data == null){
            return;
        }
        String key = new StringBuffer().append(data.getYear()).append(data.getMonthString()).append(data.getDayString()).toString();
        RepaymentDayData dayData = repaymentDayDataMap.get(key);
        hkInfoLayout.setVisibility(View.VISIBLE);
        if(dayData == null){
            hkInfoNorLayout.setVisibility(View.VISIBLE);
            hkInfoWdyLayout.setVisibility(View.GONE);
            curDateTV.setText(data.getMonth()+"月"+data.getDay()+"日");
            hkCountTV.setText("0");
            hkMoneyTV.setText("0");
            return;
        }
        if("0".equals(dayData.getMoney())){
            //说明是薪盈计划
            hkInfoNorLayout.setVisibility(View.GONE);
            hkInfoWdyLayout.setVisibility(View.VISIBLE);
            curDateWdyTV.setText(data.getMonth()+"月"+data.getDay()+"日");
            hkCountWdyTV.setText(dayData.getCount());
        }else{
            hkInfoNorLayout.setVisibility(View.VISIBLE);
            hkInfoWdyLayout.setVisibility(View.GONE);
            curDateTV.setText(data.getMonth()+"月"+data.getDay()+"日");
            hkCountTV.setText(dayData.getCount());
            double moneyD = 0d;
            try{
                moneyD = Double.parseDouble(dayData.getMoney());
                hkMoneyTV.setText(Util.double2PointDouble(moneyD));
            }catch (Exception e){
                hkMoneyTV.setText(dayData.getMoney());
            }
        }
    }

    private void initCurMonthData(String curDateStr){
        if(curDateStr == null || "".equals(curDateStr)){
            curMonthTV.setText(sdf.format(new Date()));
            getCurMonthRepayment(String.valueOf(sdf2.format(new Date())));
            intCurYearAndMonth(sdf1.format(new Date()));
            return;
        }
        try{
            curMonthTV.setText(sdf.format(sdf1.parse(curDateStr)));
            getCurMonthRepayment(sdf2.format(sdf1.parse(curDateStr)));
            intCurYearAndMonth(curDateStr);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * date yyyyMM格式
     * @param date
     */
    private void intCurYearAndMonth(String date){
        String yearS="";
        String monthS="";
        String dayS="";
        try {
            yearS = sdfYear.format(sdf1.parse(date));
            monthS = sdfMonth.format(sdf1.parse(date));
            dayS = sdfDay.format(sdf1.parse(date));
        }catch(Exception e){
        }

        int year = 0;
        int month = 0;
        int day = 0;
        try {
            year = Integer.parseInt(yearS);
            month = Integer.parseInt(monthS.startsWith("0")?monthS.replace("0",""):monthS);
            day = Integer.parseInt(dayS.startsWith("0")?dayS.replace("0",""):dayS);
        }catch (Exception e){

        }
        curYear = year;
        thisYear = year;
        curMonth = month;
    }

    /**
     * 将每天的回款信息整理到一个集合里面
     */
    private SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
    private void initRepaymentData(){
        if(repaymentDateList == null || repaymentCurDayMoneyList == null
                || repaymentCurDayCountList == null){
            return;
        }
        repaymentDayDataMap.clear();
        for(int i=0;i<repaymentDateList.size();i++){
            RepaymentDayData dayData = new RepaymentDayData();
            dayData.setDate(repaymentDateList.get(i));
            dayData.setCount(repaymentCurDayCountList.get(i));
            dayData.setMoney(repaymentCurDayMoneyList.get(i));
            repaymentDayDataMap.put(repaymentDateList.get(i),dayData);
        }
        for (Map.Entry<String, RepaymentDayData> entry : repaymentDayDataMap.entrySet()) {
            String key  = entry.getKey();
            String yearS="";
            String monthS="";
            String dayS="";
            try {
                yearS = sdfYear.format(sdf3.parse(key));
                monthS = sdfMonth.format(sdf3.parse(key));
                dayS = sdfDay.format(sdf3.parse(key));
            }catch(Exception e){

            }

            int year = 0;
            int month = 0;
            int day = 0;
            try {
                year = Integer.parseInt(yearS);
                month = Integer.parseInt(monthS.startsWith("0")?monthS.replace("0",""):monthS);
                day = Integer.parseInt(dayS.startsWith("0")?dayS.replace("0",""):dayS);
            }catch (Exception e){

            }
            DateData data = new DateData(year,month,day);
            data.setMarkStyle(new MarkStyle(MarkStyle.DEFAULT, MarkStyle.defaultColor));
            mExpCalendarView.markDate(data);
        }
    }

    /**
     * 获取当月的回款总金额
     * @param curMonth 格式yyyy-MM
     */
    private void getCurMonthRepayment(String curMonth){
        List<Double> curMonthRepaymentList = new ArrayList<Double>();
        for (Map.Entry<String, RepaymentDayData> entry : repaymentDayDataMap.entrySet()) {
            if(entry.getKey().contains(curMonth)){
                RepaymentDayData repaymentDayData  = entry.getValue();
                double dayMoneyD = 0d;
                try {
                    dayMoneyD = Double.parseDouble(repaymentDayData.getMoney());
                }catch (Exception e){

                }
                curMonthRepaymentList.add(dayMoneyD);
            }
        }
        double dayMoneyTotalD = 0d;
        for(int i=0;i<curMonthRepaymentList.size();i++){
            dayMoneyTotalD += curMonthRepaymentList.get(i);
        }
        hkTotalMoneyTV.setText(Util.double2PointDouble(dayMoneyTotalD));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.account_center_hkrl_arrow_left:
                //向左的箭头
                if(curMonth == 1){
                    curMonth = 12;
                    curYear --;
                }else{
                    curMonth--;
                }
                DateData data = new DateData(curYear,curMonth,0);
                mExpCalendarView.travelTo(data);
                hkInfoLayout.setVisibility(View.GONE);
                break;
            case R.id.account_center_hkrl_arrow_right:
                //向右的箭头
                if(curMonth == 12){
                    curMonth = 1;
                    curYear ++;
                }else{
                    curMonth++;
                }
                DateData data1 = new DateData(curYear,curMonth,0);
                mExpCalendarView.travelTo(data1);
                hkInfoLayout.setVisibility(View.GONE);
                break;
            case account_center_hkrl_date_tv:
                showDatePopwindow();
                break;
            case R.id.account_center_hkrl_cat_invest_record_btn:
                //查看投资记录
                Intent intent = new Intent(mainActivity, UserInvestRecordActivity.class);
                intent.putExtra("from_where","元政盈");
                startActivity(intent);
                break;
        }
    }

    private void showDatePopwindow(){
        View popView = LayoutInflater.from(mainActivity).inflate(
                R.layout.date_wheel_popupwindow, null);
        DateWheelPopupwindow popwindow = new DateWheelPopupwindow(mainActivity,
                popView,new OnDatePopwindowOkListener(){
            @Override
            public void onClickListener(String year, String month) {
                try{
                    curYear = Integer.parseInt(year.replace("年",""));
                    curMonth = Integer.parseInt(month.replace("月",""));
                }catch (Exception e){
                }
                DateData data = new DateData(curYear,curMonth,0);
                mExpCalendarView.travelTo(data);
            }
        },curYear,thisYear,curMonth);
        popwindow.show(mainLayout);
    }

    public void onResume() {
        super.onResume();
        UMengStatistics.statisticsOnPageStart(className);
    }
    public void onPause() {
        super.onPause();
        UMengStatistics.statisticsOnPageEnd(className);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        mExpCalendarView.unMarkDateAll();
        mExpCalendarView = null;
    }

    /**
     * 回款信息
     * @param userId
     */
    private void requestRepaymentInfo(String userId){
        AsyncRepayment task = new AsyncRepayment(mainActivity, userId, new Inter.OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        repaymentDateList.clear();
                        repaymentCurDayMoneyList.clear();
                        repaymentCurDayCountList.clear();
                        wdyDateList.clear();
                        RepaymentInfo info = baseInfo.getmRepaymentInfo();
                        if(info.getRepaymentDateList() != null){
                            repaymentDateList.addAll(info.getRepaymentDateList());
                        }
                        if(info.getRepaymentCurDayMoneyList() != null){
                            repaymentCurDayMoneyList.addAll(info.getRepaymentCurDayMoneyList());
                        }
                        if(info.getRepaymentCurDayCountList() != null){
                            repaymentCurDayCountList.addAll(info.getRepaymentCurDayCountList());
                        }
//                        if(info.getWdyDateList() != null){
//                            wdyDateList.addAll(info.getWdyDateList());
//                        }
                        initRepaymentData();
                        initCurMonthData(baseInfo.getTime());
                    }
                }
            }
        });
        task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    class RepaymentDayData{
        String date;
        String money;//当天回款金额
        String count;//当天回款笔数

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }

    public interface OnDatePopwindowOkListener{
        void onClickListener(String year,String month);
    }
}
