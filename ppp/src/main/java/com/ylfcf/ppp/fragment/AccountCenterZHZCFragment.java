package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ylfcf.widget.YLFCircle;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncJXQLogList;
import com.ylfcf.ppp.async.AsyncRedbgList;
import com.ylfcf.ppp.async.AsyncUserYUANAccount;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.JiaxiquanPageInfo;
import com.ylfcf.ppp.entity.RedBagPageInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.entity.UserYUANAccountInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.ui.AccountCenterActivity;
import com.ylfcf.ppp.ui.MyHongbaoActivity;
import com.ylfcf.ppp.ui.MyJXQActivity;
import com.ylfcf.ppp.ui.MyYuanMoneyActivity;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;

import java.text.DecimalFormat;

/**
 * 账户中心 -- 账户资产
 * Created by Administrator on 2017/8/2.
 */

public class AccountCenterZHZCFragment extends BaseFragment implements View.OnClickListener {
    private static final int REQUEST_YUANMONEY_ACCOUNT_WHAT = 8888;
    private static final int REQUEST_YUANMONEY_ACCOUNT_SUC = 8889;

    private static final int REQUEST_JXQ_LIST_WHAT = 8890;//加息券
    private static final int REQUEST_JXQ_LIST_SUCCESS = 8891;

    private static final int REQUEST_HB_LIST_WHAT = 8892;
    private static final int REQUEST_HB_LIST_SUC = 8893;

    private static final String className = "AccountCenterZHZCFragment";
    private AccountCenterActivity mainActivity;
    private YLFCircle mYLFCircle;
    private View rootView;

    //资产
    private TextView totalMoneyTV;//账户资产总额
    private TextView balanceMoneyTV;//账户余额
    private TextView waitMoneyTV;//待收余额
    private TextView freezeMoneyTV;//冻结金额
    private ImageView balanceMoneyWenhaoImg;//账户余额右上角问号
    private ImageView waitMoneyWenhaoImg;//待收余额右上角问号
    private ImageView freezeMoneyWenhaoImg;//冻结余额右上角问号
    private View zhyeCircle,dsyeCircle,djjeCircle;
    private TextView zhyeTitleTV,dsyeTitleTV,djjeTitleTV;

    //元金币
    private TextView yuanMoneyCountTV;//元金币个数
    private TextView yuanMoneyUseBtn;
    private TextView yuanMoneyDetailBtn;

    //红包
    private TextView redbagCountTV;//红包个数
    private TextView redbagUseBtn;
    private TextView redbagDetailBtn;

    //加息券
    private TextView jxqCountTV;//加息券个数
    private TextView jxqUseBtn;
    private TextView jxqDetailBtn;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REQUEST_YUANMONEY_ACCOUNT_WHAT:
                    requestYuanMoney(SettingsManager.getUserId(mainActivity));
                    break;
                case REQUEST_JXQ_LIST_WHAT:
                    requestJXQList(SettingsManager.getUserId(mainActivity
                            .getApplicationContext()), "","未使用",0,2,"");
                    break;
                case REQUEST_HB_LIST_WHAT:
                    requestHBList(SettingsManager.getUserId(mainActivity
                            .getApplicationContext()),"1");
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (AccountCenterActivity) getActivity();
        if(rootView==null){
            rootView = inflater.inflate(R.layout.account_center_zhzc_fragment, null);
            findViews(rootView,inflater);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void findViews(View rootView,LayoutInflater layoutInflater){
        mYLFCircle = (YLFCircle)rootView.findViewById(R.id.account_center_zhzc_fragment_circle);
        int circleX = Util.getScreenWidthAndHeight(mainActivity)[0]/2;
        mYLFCircle.initCircleData(circleX,circleX - getResources().getDimensionPixelSize(R.dimen.common_measure_100dp),
                getResources().getDimensionPixelSize(R.dimen.common_measure_100dp),getResources().getDimensionPixelSize(R.dimen.common_measure_25dp));
        mYLFCircle.setColors(getResources().getColor(R.color.circle_blue),
                getResources().getColor(R.color.circle_orange),
                getResources().getColor(R.color.circle_green),
                getResources().getColor(R.color.edittext_hint_color));

        totalMoneyTV = (TextView) rootView.findViewById(R.id.account_center_zhzc_fragment_total_tv);
        balanceMoneyTV = (TextView) rootView.findViewById(R.id.account_center_zhzc_fragment_zhye_tv);
        balanceMoneyWenhaoImg = (ImageView) rootView.findViewById(R.id.account_center_zhzc_fragment_wenhao_zhye);
        balanceMoneyWenhaoImg.setOnClickListener(this);
        zhyeCircle = rootView.findViewById(R.id.account_center_zhzc_fragment_zhye_circle);
        zhyeCircle.setOnClickListener(this);
        zhyeTitleTV = (TextView) rootView.findViewById(R.id.account_center_zhzc_fragment_zhye_title);
        zhyeTitleTV.setOnClickListener(this);

        waitMoneyTV = (TextView) rootView.findViewById(R.id.account_center_zhzc_fragment_dsye_tv);
        waitMoneyWenhaoImg = (ImageView) rootView.findViewById(R.id.account_center_zhzc_fragment_wenhao_dsye);
        waitMoneyWenhaoImg.setOnClickListener(this);
        dsyeCircle = rootView.findViewById(R.id.account_center_zhzc_fragment_dsye_circle);
        dsyeCircle.setOnClickListener(this);
        dsyeTitleTV = (TextView) rootView.findViewById(R.id.account_center_zhzc_fragment_dsye_title);
        dsyeTitleTV.setOnClickListener(this);

        freezeMoneyTV = (TextView) rootView.findViewById(R.id.account_center_zhzc_fragment_djje_tv);
        freezeMoneyWenhaoImg = (ImageView) rootView.findViewById(R.id.account_center_zhzc_fragment_wenhao_djje);
        freezeMoneyWenhaoImg.setOnClickListener(this);
        djjeCircle = rootView.findViewById(R.id.account_center_zhzc_fragment_djje_circle);
        djjeCircle.setOnClickListener(this);
        djjeTitleTV = (TextView) rootView.findViewById(R.id.account_center_zhzc_fragment_djje_title);
        djjeTitleTV.setOnClickListener(this);

        yuanMoneyCountTV = (TextView) rootView.findViewById(R.id.account_center_zhzc_fragment_yjb_count_tv);
        yuanMoneyUseBtn = (TextView) rootView.findViewById(R.id.account_center_zhzc_fragment_yjb_use_btn);
        yuanMoneyUseBtn.setOnClickListener(this);
        yuanMoneyDetailBtn = (TextView) rootView.findViewById(R.id.account_center_zhzc_fragment_yjb_detail_btn);
        yuanMoneyDetailBtn.setOnClickListener(this);

        redbagCountTV = (TextView) rootView.findViewById(R.id.account_center_zhzc_fragment_hb_count_tv);
        redbagUseBtn = (TextView) rootView.findViewById(R.id.account_center_zhzc_fragment_hb_use_btn);
        redbagUseBtn.setOnClickListener(this);
        redbagDetailBtn = (TextView) rootView.findViewById(R.id.account_center_zhzc_fragment_hb_detail_btn);
        redbagDetailBtn.setOnClickListener(this);

        jxqCountTV = (TextView) rootView.findViewById(R.id.account_center_zhzc_fragment_jxq_count_tv);
        jxqUseBtn = (TextView) rootView.findViewById(R.id.account_center_zhzc_fragment_jxq_use_btn);
        jxqUseBtn.setOnClickListener(this);
        jxqDetailBtn = (TextView) rootView.findViewById(R.id.account_center_zhzc_fragment_jxq_detail_btn);
        jxqDetailBtn.setOnClickListener(this);

        initAccountData(mainActivity.getYLAccountInfo(),mainActivity.getHFAccountInfo(),mainActivity.getYJBInterestInfo());
        handler.sendEmptyMessage(REQUEST_YUANMONEY_ACCOUNT_WHAT);
        handler.sendEmptyMessage(REQUEST_JXQ_LIST_WHAT);
        handler.sendEmptyMessage(REQUEST_HB_LIST_WHAT);
    }

    private void initAccountData(UserRMBAccountInfo yilianAccount,
                                 UserRMBAccountInfo huifuAccount,
                                 BaseInfo yjbInterest){
        double yilianBalance = 0d;
        double huifuBalance = 0d;
        double totalBalance = 0d;
        double dsBalance = 0d;//待收
        double djBalance = 0d;//冻结
        double accuntTotal = 0d;
        double yjbInterestD = 0d;
        DecimalFormat df = new DecimalFormat("#.00");
        try{
            yjbInterestD = Double.parseDouble(yjbInterest.getMsg());
        }catch (Exception e){
            e.printStackTrace();
            yjbInterestD = 0;
        }
        try {
            dsBalance = Double.parseDouble(yilianAccount.getCollection_money()) + yjbInterestD;
        } catch (Exception e) {
        }
        try {
            djBalance = Double.parseDouble(yilianAccount.getFrozen_money());
        } catch (Exception e) {
        }
        freezeMoneyTV.setText(yilianAccount.getFrozen_money());
        if(dsBalance < 1){
            waitMoneyTV.setText("0"+df.format(dsBalance));
        }else{
            waitMoneyTV.setText(df.format(dsBalance));
        }

        try {
            yilianBalance = Double.parseDouble(yilianAccount.getUse_money());
            if(huifuAccount != null){
                huifuBalance = Double.parseDouble(huifuAccount.getUse_money());
            }
            totalBalance = yilianBalance + huifuBalance;
            if(totalBalance < 1){
                balanceMoneyTV.setText("0"+df.format(totalBalance));
            }else{
                balanceMoneyTV.setText(df.format(totalBalance));
            }
            accuntTotal = totalBalance + dsBalance + djBalance;
            if(accuntTotal < 1){
                totalMoneyTV.setText("0"+df.format(accuntTotal));
            }else{
                totalMoneyTV.setText(df.format(accuntTotal));
            }
        } catch (Exception e) {
        }

        initCircleData((float)accuntTotal,(float)totalBalance,(float)dsBalance,(float)djBalance);
    }

    private void initCircleData(float totalMoneyF,float balanceMoneyF,
                                float waitMoneyF,float freezeMoneyF){
        if(totalMoneyF <= 0){
            mYLFCircle.setAngles(0,0,0);
        }else{
            mYLFCircle.setAngles(freezeMoneyF*360/totalMoneyF,waitMoneyF*360/totalMoneyF,balanceMoneyF*360/totalMoneyF);
        }
        YLFLogger.d("angle:"+freezeMoneyF*360/totalMoneyF+"------"+
                waitMoneyF*360/totalMoneyF+"-------"+balanceMoneyF*360/totalMoneyF);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.account_center_zhzc_fragment_yjb_use_btn:
            case R.id.account_center_zhzc_fragment_hb_use_btn:
            case R.id.account_center_zhzc_fragment_jxq_use_btn:
                SettingsManager.setMainProductListFlag(mainActivity,true);
                mainActivity.finish();
                break;
            case R.id.account_center_zhzc_fragment_yjb_detail_btn:
                Intent intentYJB = new Intent(mainActivity, MyYuanMoneyActivity.class);
                startActivity(intentYJB);
                break;
            case R.id.account_center_zhzc_fragment_hb_detail_btn:
                Intent intentHB = new Intent(mainActivity, MyHongbaoActivity.class);
                startActivity(intentHB);
                break;
            case R.id.account_center_zhzc_fragment_jxq_detail_btn:
                Intent intentJXQ = new Intent(mainActivity, MyJXQActivity.class);
                startActivity(intentJXQ);
                break;
            case R.id.account_center_zhzc_fragment_zhye_circle:
            case R.id.account_center_zhzc_fragment_zhye_title:
            case R.id.account_center_zhzc_fragment_wenhao_zhye:
                //账户余额
                showTipsDialog("zhye");
                break;
            case R.id.account_center_zhzc_fragment_dsye_circle:
            case R.id.account_center_zhzc_fragment_dsye_title:
            case R.id.account_center_zhzc_fragment_wenhao_dsye:
                //待收余额注释
                showTipsDialog("dsye");
                break;
            case R.id.account_center_zhzc_fragment_djje_circle:
            case R.id.account_center_zhzc_fragment_djje_title:
            case R.id.account_center_zhzc_fragment_wenhao_djje:
                //冻结金额注释
                showTipsDialog("djje");
                break;
        }
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
    }

    /**
     * 账户金额名词提示
     */
    private void showTipsDialog(String type){
        View contentView = LayoutInflater.from(mainActivity).inflate(R.layout.myinvitation_tips_dialog_layout, null);
        final Button okBtn = (Button) contentView.findViewById(R.id.myinvitation_tips_dialog_sure_btn);
        final TextView contentTV = (TextView) contentView.findViewById(R.id.myinvitation_tips_content);
        final TextView titleTV = (TextView) contentView.findViewById(R.id.myinvitation_tips_title);
        titleTV.setVisibility(View.GONE);
        if("zhye".equals(type)){
            contentTV.setText(getResources().getString(R.string.account_center_tips_string1));
        }else if("dsye".equals(type)){
            contentTV.setText(getResources().getString(R.string.account_center_tips_string2));
        }else if("djje".equals(type)){
            contentTV.setText(getResources().getString(R.string.account_center_tips_string3));
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(mainActivity, R.style.Dialog_Transparent);  //先得到构造器
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //参数都设置完成了，创建并显示出来
        dialog.show();
        WindowManager windowManager = mainActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*2/3;
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * 元金币账户
     * @param userId
     */
    private void requestYuanMoney(String userId){
        AsyncUserYUANAccount accountTask = new AsyncUserYUANAccount(mainActivity,
                userId, new Inter.OnUserYUANAccountInter() {
            @Override
            public void back(BaseInfo info) {
                if(info != null){
                    int resultCode = SettingsManager.getResultCode(info);
                    if(resultCode == 0){
                        UserYUANAccountInfo accountInfo = info.getYuanAccountInfo();
                        if(accountInfo != null){
                            yuanMoneyCountTV.setText(accountInfo.getUse_coin());
                        }
                    }
                }
            }
        });
        accountTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * 加息券
     * @param userId
     * @param useStatus
     */
    private void requestJXQList(String userId, String couponFrom,String useStatus,
                                int pageNo,int pageSize,String transfer) {
        AsyncJXQLogList jxqTask = new AsyncJXQLogList(mainActivity, userId,couponFrom,useStatus,
                String.valueOf(pageNo), String.valueOf(pageSize),transfer, new Inter.OnCommonInter(){
            @Override
            public void back(BaseInfo baseInfo) {
                if (baseInfo != null) {
                    int resultCode = SettingsManager
                            .getResultCode(baseInfo);
                    if (resultCode == 0) {
                        JiaxiquanPageInfo pageInfo = baseInfo.getmJiaxiquanPageInfo();
                        jxqCountTV.setText(pageInfo.getTotal());
                    }
                }
            }
        });
        jxqTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * 红包
     * @param userId
     * @param flag
     */
    private void requestHBList(String userId, String flag) {
        AsyncRedbgList redbagTask = new AsyncRedbgList(mainActivity, userId,
                flag, String.valueOf(0), String.valueOf(2), new Inter.OnCommonInter(){
            @Override
            public void back(BaseInfo baseInfo) {
                if (baseInfo != null) {
                    int resultCode = SettingsManager
                            .getResultCode(baseInfo);
                    if (resultCode == 0) {
                        RedBagPageInfo pageInfo = baseInfo.getmRedBagPageInfo();
                        redbagCountTV.setText(pageInfo.getTotal());
                    }
                }
            }
        });
        redbagTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }
}
