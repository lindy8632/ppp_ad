package com.ylfcf.ppp.ui;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncHDRobcashGetGift;
import com.ylfcf.ppp.async.AsyncRobcashIsReceiveStatus;
import com.ylfcf.ppp.async.AsyncRobcashMoney;
import com.ylfcf.ppp.async.AsyncRobcashRob;
import com.ylfcf.ppp.async.AsyncXCFLActiveTime;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.RobcashMoneyInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.util.Constants;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.view.InvitateFriendsPopupwindow;

/**
 * 5月份每周一抢现金活动
 */
public class LXJ5TempActivity extends BaseActivity implements View.OnClickListener{
    private static final int REQUEST_ROBCASH_MONEY_WHAT = 1954;//今日可抢金额，下周待抢金额
    private static final int REQUEST_ROBCASH_GETGIFT_WHAT = 1957;//领绵羊奶粉接口
    private static final int REQUEST_ACTIVITY_ISSTART = 1958;//周一抢现金活动是否开始
    private static final int REQUEST_ROBCASH_ISRECEIVE_WHAT = 1959;//是否已经领取过羊奶粉 是否已经领完羊奶粉
    private static final int REQUEST_ROBCASH_ROB_WHAT = 1960;//抢现金接口
    private LinearLayout mainLayout;
    private LinearLayout topLeftBtn;
    private TextView topTitleTV;

    private TextView kqjeText;//可抢金额
    private Button qxjBtn;//抢现金
    private TextView dqjeText;//待抢金额
    private Button catDetailsBtn,getBtn;//查看礼品详情，领取绵羊奶粉
    private Button shareBtn;//分享

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case REQUEST_ROBCASH_MONEY_WHAT:
                    requestRobcashMoney();
                    break;
                case REQUEST_ROBCASH_GETGIFT_WHAT:
                    requestGetGift(SettingsManager.getUserId(LXJ5TempActivity.this));
                    break;
                case REQUEST_ACTIVITY_ISSTART:
                    requestActiveTime("MONDAY_ROB_CASH");
                    break;
                case REQUEST_ROBCASH_ISRECEIVE_WHAT:
                    requestRobCashIsReceiveStatus(SettingsManager.getUserId(LXJ5TempActivity.this),"LYNF_01");
                    break;
                case REQUEST_ROBCASH_ROB_WHAT:
                    requestRobcashRob(SettingsManager.getUserId(LXJ5TempActivity.this));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lxj5_temp_activity);
        findViews();
    }

    private void findViews(){
        topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
        topLeftBtn.setOnClickListener(this);
        topTitleTV = (TextView) findViewById(R.id.common_page_title);
        topTitleTV.setText("每周一 抢现金");

        mainLayout = (LinearLayout) findViewById(R.id.lxj5_temp_activity_mainlayout);
        kqjeText = (TextView) findViewById(R.id.lxj5_temp_activity_money);
        qxjBtn = (Button) findViewById(R.id.lxj5_temp_activity_qxjbtn);
        qxjBtn.setOnClickListener(this);
        dqjeText = (TextView) findViewById(R.id.lxj5_temp_activity_dqje);
        catDetailsBtn = (Button) findViewById(R.id.lxj5_temp_activity_catdetails);
        catDetailsBtn.setOnClickListener(this);
        getBtn = (Button) findViewById(R.id.lxj5_temp_activity_getgift_btn);
        getBtn.setOnClickListener(this);
        shareBtn = (Button) findViewById(R.id.lxj5_temp_activity_shared_btn);
        shareBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessage(REQUEST_ACTIVITY_ISSTART);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_topbar_left_layout:
                finish();
                break;
            case R.id.lxj5_temp_activity_qxjbtn:
                //抢现金按钮
                boolean isLogin = !SettingsManager.getLoginPassword(
                        LXJ5TempActivity.this).isEmpty()
                        && !SettingsManager.getUser(LXJ5TempActivity.this)
                        .isEmpty();
                if(isLogin){
                    handler.sendEmptyMessage(REQUEST_ROBCASH_ROB_WHAT);
                }else{
                    Intent intent = new Intent(LXJ5TempActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.lxj5_temp_activity_catdetails:
                //查看礼品详情
                showMYNFDetailsDialog();
                break;
            case R.id.lxj5_temp_activity_getgift_btn:
                //领羊奶粉
                boolean isLoginA = !SettingsManager.getLoginPassword(
                        LXJ5TempActivity.this).isEmpty()
                        && !SettingsManager.getUser(LXJ5TempActivity.this)
                        .isEmpty();
                if(isLoginA){
                    handler.sendEmptyMessage(REQUEST_ROBCASH_GETGIFT_WHAT);
                }else{
                    Intent intent = new Intent(LXJ5TempActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.lxj5_temp_activity_shared_btn:
                showFriendsSharedWindow();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * 绵羊奶粉详情
     */
    private void showMYNFDetailsDialog(){
        View contentView = LayoutInflater.from(LXJ5TempActivity.this).inflate(R.layout.lxj5_mynf_details_dialog_layout, null);
        final Button okBtn = (Button) contentView.findViewById(R.id.lxj5_mynf_details_dialog_okbtn);
        final Button delBtn = (Button) contentView.findViewById(R.id.lxj5_mynf_details_dialog_delbtn);
        AlertDialog.Builder builder=new AlertDialog.Builder(LXJ5TempActivity.this, R.style.Dialog_Transparent);  //先得到构造器
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //参数都设置完成了，创建并显示出来
        dialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*3/4;
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * 领取羊奶粉以及抢现金的提示弹窗
     */
    private void showPromptDialog(final String type,final String money){
        View contentView = LayoutInflater.from(LXJ5TempActivity.this).inflate(R.layout.lxj5_prompt_dialog_layout, null);
        final Button leftBtn = (Button) contentView.findViewById(R.id.lxj5_prompt_dialog_leftbtn);
        final Button rightBtn = (Button) contentView.findViewById(R.id.lxj5_prompt_dialog_rightbtn);
        final Button delBtn = (Button) contentView.findViewById(R.id.lxj5_prompt_dialog_delbtn);
        final TextView topText = (TextView) contentView.findViewById(R.id.lxj5_prompt_dialog_toptext);
        final TextView bottomText = (TextView) contentView.findViewById(R.id.lxj5_prompt_dialog_bottomtext);
        AlertDialog.Builder builder=new AlertDialog.Builder(LXJ5TempActivity.this, R.style.Dialog_Transparent);  //先得到构造器
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        if("ynf_suc".equals(type)){
            //羊奶粉领取成功
            topText.setVisibility(View.VISIBLE);
            topText.setText("恭喜您领取成功！");
            bottomText.setText("可前往个人中心-我的礼品中查看");
            leftBtn.setText("查看礼品");
        }else if("active_ynf_faile".equals(type) || "active_cash_faile".equals(type)){
            //领羊奶粉或者抢现金失败 --- 未达到条件
            topText.setVisibility(View.GONE);
            leftBtn.setText("前往投资");
            if("active_ynf_faile".equals(type)){
                bottomText.setText("投资指定产品达5000元后，才有机会参与活动哟！");
            }else{
                bottomText.setText("上周投资指定产品达5000元后，才有机会参与活动哟！");
            }
        }else if("rob_suc".equals(type)){
            //抢现金成功
            topText.setVisibility(View.VISIBLE);
            topText.setText("恭喜您抢到现金"+money+"元");
            bottomText.setText("可前往个人中心-我的礼品中查看");
            leftBtn.setText("查看奖励");
        }else if("rob_has_suc".equals(type)){
            //已经抢过现金
            topText.setVisibility(View.VISIBLE);
            topText.setText("您本周已抢过现金："+money+"元");
            bottomText.setText("可前往个人中心-我的礼品中查看");
            leftBtn.setText("查看奖励");
        }
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("ynf_suc".equals(type)){
                    //羊奶粉领取成功
                    Intent intent = new Intent(LXJ5TempActivity.this,MyGiftsActivity.class);
                    startActivity(intent);
                }else if("rob_suc".equals(type)){
                    //抢现金成功
                    Intent intent = new Intent(LXJ5TempActivity.this,MyGiftsActivity.class);
                    startActivity(intent);
                }else if("rob_has_suc".equals(type)){
                    // 抢现金成功 已经抢过现金
                    Intent intent = new Intent(LXJ5TempActivity.this,MyGiftsActivity.class);
                    startActivity(intent);
                }else{
                    //跳转到首页 理财栏目
                    SettingsManager.setMainProductListFlag(LXJ5TempActivity.this,true);
                    mApp.finishAllActivityExceptMain();
                }
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //参数都设置完成了，创建并显示出来
        dialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*4/5;
        lp.height = display.getHeight() * 1 / 2;
        dialog.getWindow().setAttributes(lp);
    }

    private void initMoneyData(BaseInfo baseInfo){
        if(baseInfo == null || baseInfo.getmRobcashMoneyInfo() == null)
            return;
        RobcashMoneyInfo mRobcashMoneyInfo = baseInfo.getmRobcashMoneyInfo();
        if(!"0".equals(mRobcashMoneyInfo.getRed_bag_num()) && mRobcashMoneyInfo.getRed_bag_num().equals(mRobcashMoneyInfo.getUse_red_bag_num())){
            //现金已抢完
            qxjBtn.setEnabled(false);
            qxjBtn.setBackgroundResource(R.drawable.qxj5_over_btn);
            kqjeText.setText("本周已抢完，期待下一场");
            kqjeText.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.common_measure_11dp));
            dqjeText.setText(mRobcashMoneyInfo.getNext_week_money()+"元");
            return;
        }
        boolean isStart = SettingsManager.checkRobCashIsStart(baseInfo);
        boolean isLogin = !SettingsManager.getLoginPassword(
                LXJ5TempActivity.this).isEmpty()
                && !SettingsManager.getUser(LXJ5TempActivity.this)
                .isEmpty();
        if(isLogin){
            if(isStart && !SettingsManager.isCompanyUser(LXJ5TempActivity.this)
                    &&!Constants.UserType.USER_VIP_PERSONAL.equals(SettingsManager.getUserType(LXJ5TempActivity.this))){
                qxjBtn.setBackgroundResource(R.drawable.qxj5_qxj_btn);
                qxjBtn.setEnabled(true);
            }else{
                qxjBtn.setBackgroundResource(R.drawable.qxj5_qxj_btn_unalble);
                qxjBtn.setEnabled(false);
            }
        }
        try{
            double d = Double.parseDouble(mRobcashMoneyInfo.getMoney());
            kqjeText.setText(mRobcashMoneyInfo.get_show_money());
            kqjeText.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.common_measure_14dp));
        }catch (Exception e){
            e.printStackTrace();
            kqjeText.setText(mRobcashMoneyInfo.get_show_money());
            kqjeText.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.common_measure_11dp));
        }
        dqjeText.setText(mRobcashMoneyInfo.getNext_week_money()+"元");
    }

    /**
     * 根据系统时间来判断各个按钮的状态
     * @param baseInfo
     */
    private void initBtnsStatus(BaseInfo baseInfo,int resultCode){
        if(resultCode == 0){
            //请求可抢金额 待抢金额 现金是否已抢完等等
            handler.sendEmptyMessage(REQUEST_ROBCASH_MONEY_WHAT);
            //活动已开始
            boolean isLogin = !SettingsManager.getLoginPassword(
                    LXJ5TempActivity.this).isEmpty()
                    && !SettingsManager.getUser(LXJ5TempActivity.this)
                    .isEmpty();
            if(isLogin){
                //请求结果判断用户是否已经领取过羊奶粉
                if(!SettingsManager.isCompanyUser(LXJ5TempActivity.this)
                        &&!Constants.UserType.USER_VIP_PERSONAL.equals(SettingsManager.getUserType(LXJ5TempActivity.this))){
                    //普通用户
                    handler.sendEmptyMessage(REQUEST_ROBCASH_ISRECEIVE_WHAT);
                }else{
                    getBtn.setBackgroundResource(R.drawable.qxj5_get_btn_unenable);
                    getBtn.setEnabled(false);
                }
            }else{
                //未登录
                qxjBtn.setBackgroundResource(R.drawable.qxj5_unlogin_btn);
                qxjBtn.setEnabled(true);
                getBtn.setBackgroundResource(R.drawable.qxj5_unlogin_btn);
                getBtn.setEnabled(true);
            }
        }else if(resultCode == -3){
            //活动结束
            qxjBtn.setBackgroundResource(R.drawable.qxj5_end_btn);
            qxjBtn.setEnabled(false);
            getBtn.setBackgroundResource(R.drawable.qxj5_end_btn);
            getBtn.setEnabled(false);
            kqjeText.setText("0.00元");
            kqjeText.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.common_measure_14dp));
            dqjeText.setText("0.00元");
        }else if(resultCode == -2){
            //活动还未开始
            qxjBtn.setBackgroundResource(R.drawable.qxj5_jqqd_btn);
            qxjBtn.setEnabled(false);
            getBtn.setBackgroundResource(R.drawable.qxj5_jqqd_btn);
            getBtn.setEnabled(false);
            kqjeText.setText("5月15日零点后揭晓");
            kqjeText.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.common_measure_11dp));
            dqjeText.setText("0.00元");
        }
    }

    /**
     * 弹出分享的提示框
     */
    private void showFriendsSharedWindow() {
        View popView = LayoutInflater.from(this).inflate(
                R.layout.invitate_friends_popupwindow, null);
        int[] screen = SettingsManager.getScreenDispaly(LXJ5TempActivity.this);
        int width = screen[0];
        int height = screen[1] / 5 * 2;
        InvitateFriendsPopupwindow popwindow = new InvitateFriendsPopupwindow(LXJ5TempActivity.this,
                popView, width, height);
        popwindow.show(mainLayout, URLGenerator.LXJ5_WAP_URL,"每周一抢现金",null,null);
    }

    /**
     * 今日剩余可抢金额  下周待抢金额 以及现金是否抢完
     */
    private void requestRobcashMoney(){
        if(mLoadingDialog != null){
            mLoadingDialog.show();
        }
        AsyncRobcashMoney cashMoenyTask = new AsyncRobcashMoney(LXJ5TempActivity.this,"", new Inter.OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                    mLoadingDialog.dismiss();
                }
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        RobcashMoneyInfo info = baseInfo.getmRobcashMoneyInfo();
                        initMoneyData(baseInfo);
                    }
                }
            }
        });
        cashMoenyTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * 领取绵羊奶粉
     * @param userId
     */
    private void requestGetGift(String userId){
        if(mLoadingDialog != null){
            mLoadingDialog.show();
        }
        AsyncHDRobcashGetGift task = new AsyncHDRobcashGetGift(LXJ5TempActivity.this, userId, new Inter.OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                    mLoadingDialog.dismiss();
                }
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        //领取成功
                        showPromptDialog("ynf_suc","");
                        getBtn.setEnabled(false);
                        getBtn.setBackgroundResource(R.drawable.qxj5_has_get_btn);
                    }else if(resultCode == -105){
                        //投资未满5000元
                        showPromptDialog("active_ynf_faile","");
                    }else if(resultCode == -102){
                        //活动已结束
                        initBtnsStatus(null,-3);
                        Util.toastLong(LXJ5TempActivity.this,baseInfo.getMsg());
                    }else{
                        Util.toastLong(LXJ5TempActivity.this,baseInfo.getMsg());
                    }
                }
            }
        });
        task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * 判断抢现金活动是否开始
     * @param activeTitle
     */
    private void requestActiveTime(final String activeTitle){
        if(mLoadingDialog != null){
            mLoadingDialog.show();
        }
        AsyncXCFLActiveTime task = new AsyncXCFLActiveTime(LXJ5TempActivity.this, activeTitle,
                new Inter.OnCommonInter() {
                    @Override
                    public void back(BaseInfo baseInfo) {
                        if(mLoadingDialog != null){
                            mLoadingDialog.dismiss();
                        }
                        if(baseInfo != null){
                            int resultCode = SettingsManager.getResultCode(baseInfo);
                            initBtnsStatus(baseInfo,resultCode);
                        }
                    }
                });
        task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * 是否领取过羊奶粉，是否已经领完羊奶粉
     * @param userId
     * @param activeTitle
     */
    private void requestRobCashIsReceiveStatus(String userId,String activeTitle){
        AsyncRobcashIsReceiveStatus task = new AsyncRobcashIsReceiveStatus(LXJ5TempActivity.this, userId, activeTitle, new Inter.OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        //表示可以领取
                        getBtn.setEnabled(true);
                        getBtn.setBackgroundResource(R.drawable.qxj5_get_btn);
                    }else if(resultCode == -101){
                        //已经领取过了
                        getBtn.setEnabled(false);
                        getBtn.setBackgroundResource(R.drawable.qxj5_has_get_btn);
                    }else if(resultCode == -102){
                        //已领完
                        getBtn.setEnabled(false);
                        getBtn.setBackgroundResource(R.drawable.qxj5_get_over_btn);
                    }else{
                        getBtn.setEnabled(false);
                        getBtn.setBackgroundResource(R.drawable.qxj5_get_btn_unenable);
                    }
                }
            }
        });
        task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * 抢现金
     * @param userId
     */
    private void requestRobcashRob(String userId){
        if(mLoadingDialog != null){
            mLoadingDialog.show();
        }
        AsyncRobcashRob task = new AsyncRobcashRob(LXJ5TempActivity.this, userId, new Inter.OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                    mLoadingDialog.dismiss();
                }
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        RobcashMoneyInfo moneyInfo = baseInfo.getmRobcashMoneyInfo();
                        showPromptDialog("rob_suc",moneyInfo.getMoney());
                    }else if(resultCode == 502){
                        //投资为满足条件
                        showPromptDialog("active_cash_faile","");
                    }else if(resultCode == 504){
                        //已经抢过了。
                        RobcashMoneyInfo moneyInfo = baseInfo.getmRobcashMoneyInfo();
                        showPromptDialog("rob_has_suc",moneyInfo.getPrize());
                    }else if(resultCode == 107){
                        //活动未开始
                        Util.toastLong(LXJ5TempActivity.this,baseInfo.getMsg());
                        initBtnsStatus(null,-2);
                    }else if(resultCode == 108){
                        //活动已结束
                        Util.toastLong(LXJ5TempActivity.this,baseInfo.getMsg());
                        initBtnsStatus(null,-3);
                    }else{
                        Util.toastLong(LXJ5TempActivity.this,baseInfo.getMsg());
                    }
                }
            }
        });
        task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }
}
