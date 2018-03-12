package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncTLOrder;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.TLOrderInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.view.CommonPopwindow;
import com.ylfcf.ppp.widget.AuthImageView;

/**
 * pos机充值页面
 * Created by Administrator on 2018/1/15.
 */

public class RechargePosActivity extends BaseActivity implements View.OnClickListener {
    private static final String className = "RechargePosActivity";

    private static final int REQUEST_USERINFO_WHAT = 1000;
    private static final int REQUEST_USERINFO_SUC = 1001;
    private static final int DECIMAL_DIGITS = 1;//小数的位数

    private static final int REQUEST_ORDER_WHAT = 1002;
    private static final int REQUEST_ORDER_SUC = 1003;

    private LinearLayout mainLayout;
    private UserInfo userInfo;
    private AuthImageView authImageView;
    private LinearLayout topLeftBtn;
    private TextView topTitleTV;
    private String authString;
    private EditText moneyET;
    private EditText authET;
    private TextView promptTV;
    private TextView topPromptTV;
    private Button paycodeBtn;//点击生成付款码
    private TextView catRechargeRecord,catOperation;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REQUEST_USERINFO_WHAT:
                    requestUserInfo(SettingsManager.getUserId(getApplicationContext()),"");
                    break;
                case REQUEST_USERINFO_SUC:

                    break;
                case REQUEST_ORDER_WHAT:
                    double amountD = (Double) msg.obj;
                    requestTLOrderNum(SettingsManager.getUserId(getApplicationContext()),
                            amountD,"android");
                    break;
                case REQUEST_ORDER_SUC:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.recharge_pos_activity_layout);
        findViews();
        handler.sendEmptyMessageDelayed(REQUEST_USERINFO_WHAT,200L);
    }

    private void findViews(){
        topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
        topLeftBtn.setOnClickListener(this);
        topTitleTV = (TextView) findViewById(R.id.common_page_title);
        topTitleTV.setText("POS充值");

        mainLayout = (LinearLayout) findViewById(R.id.recharge_pos_activity_mainlayout);
        moneyET = (EditText) findViewById(R.id.recharge_pos_activity_money_et);
        moneyET.addTextChangedListener(watcherRechargeMoney);
        topPromptTV = (TextView) findViewById(R.id.recharge_pos_activity_prompt);
        promptTV = (TextView) findViewById(R.id.recharge_pos_activity_prompt_tv);
        authET = (EditText) findViewById(R.id.recharge_pos_activity_sms_et);
        paycodeBtn = (Button) findViewById(R.id.recharge_pos_activity_fukuanma_btn);
        paycodeBtn.setOnClickListener(this);
        catRechargeRecord = (TextView) findViewById(R.id.recharge_pos_activity_catrecord_tv);
        catRechargeRecord.setOnClickListener(this);
        catOperation = (TextView) findViewById(R.id.recharge_pos_activity_catoperation_tv);
        catOperation.setOnClickListener(this);
        authImageView = findViewById(R.id.recharge_pos_activity_auth_iv);
        authImageView.setOnClickListener(this);
        authString = getResponseStr(authImageView.getValidataAndSetImage(getRandomInteger()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        UMengStatistics.statisticsOnPageStart(className);//友盟统计页面跳转
        UMengStatistics.statisticsResume(this);//友盟统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        UMengStatistics.statisticsOnPageEnd(className);//友盟统计页面跳转
        UMengStatistics.statisticsPause(this);//友盟统计时长
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }


    // 获取1~9的4个随机数
    private String[] getRandomInteger() {
        String[] reuestArray = new String[4];
        for (int i = 0; i < 4; i++) {
            reuestArray[i] = String.valueOf((int) (Math.random() * 9 + 1));
        }
        return reuestArray;
    }

    // 获取返回的数组
    private String getResponseStr(String[] response) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String str : response) {
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    private TextWatcher watcherRechargeMoney = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > DECIMAL_DIGITS) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + DECIMAL_DIGITS+1);
                    moneyET.setText(s);
                    moneyET.setSelection(s.length());
                }
            }
            //用户直接输入“.”的情况
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                moneyET.setText(s);
                moneyET.setSelection(2);
            }
            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    moneyET.setText(s.subSequence(1, 2));
                    moneyET.setSelection(1);
                }
            }
            if(s.toString().endsWith(".")){
                updateRechargePrompt(s.toString().replace(".",""));
            }else{
                updateRechargePrompt(s.toString());
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String rechargeS = s.toString();
            double rechargeD = 0d;
            try{
                rechargeD = Double.parseDouble(rechargeS);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(rechargeD > 300){
                moneyET.setText("300");
                moneyET.setSelection(3);
            }
        }
    };

    /**
     * 更新充值金额提示
     * @param s
     */
    private void updateRechargePrompt(String s){
        YLFLogger.d("pos充值金额："+s);
        double d = 0d;
        try{
            d = Double.parseDouble(s);
        }catch (Exception e){
        }
        if(d <= 0){
            promptTV.setVisibility(View.GONE);
            return;
        }
        promptTV.setVisibility(View.VISIBLE);
        if(s.contains(".")){
            //带小数点的情况
            String[] sArr = s.split("\\.");
            YLFLogger.d("分隔后："+sArr[0]+"|"+sArr[1]);
            int sArr1 = 0;
            int sArr2 = 0;
            try{
                sArr1 = Integer.parseInt(sArr[0]);
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                sArr2 = Integer.parseInt(sArr[1]);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(sArr1 <= 0){
                promptTV.setText("充值金额："+sArr2+"千元");
            }else{
                if(sArr2 <= 0){
                    promptTV.setText("充值金额："+sArr1+"万元");
                }else{
                    promptTV.setText("充值金额："+sArr1+"万零"+sArr2+"千元");
                }
            }
        }else{
            //不带小数点的情况
            promptTV.setText("充值金额："+(int)d+"万元");
        }
    }

    private void initUserInfo(UserInfo userInfo){
        topPromptTV.setText("如确定使用POS向"+userInfo.getReal_name()+"的元立方账户充值，请在输入充值金额后点击按钮生成付款码。");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_topbar_left_layout:
                finish();
                break;
            case R.id.recharge_pos_activity_auth_iv:
                authString = getResponseStr(authImageView.getValidataAndSetImage(getRandomInteger()));
                break;
            case R.id.recharge_pos_activity_fukuanma_btn:
                //点击生成付款码
                checkRechargeData();
                break;
            case R.id.recharge_pos_activity_catrecord_tv:
                //查看充值记录
                Intent intent = new Intent(RechargePosActivity.this,RechargeRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.recharge_pos_activity_catoperation_tv:
                //查看使用说明
                Intent intentInstructions = new Intent(RechargePosActivity.this,RechargePosInstructionsActivity.class);
                startActivity(intentInstructions);
                break;
        }
    }

    /**
     * 检查充值金额
     */
    private void checkRechargeData(){
        double rechargeMoneyD = 0d;
        String rechargeMoneyS = moneyET.getText().toString();
        String authStr = authET.getText().toString();
        try{
            rechargeMoneyD = Double.parseDouble(rechargeMoneyS);
        }catch (Exception e){

        }
        if(rechargeMoneyS == null || "".endsWith(rechargeMoneyS)){
            //请输入充值金额
            showRechargePrompt("","请输入充值金额");
        }
        else if(rechargeMoneyD < 5 || rechargeMoneyD > 300){
            //请输入正确的充值金额
            showRechargePrompt("","请输入正确的充值金额");
        }
        else if(!authStr.equals(authString)){
            //请输入正确的验证码
            showRechargePrompt("","请输入正确的验证码");
        }else{
            Message msg = handler.obtainMessage(REQUEST_ORDER_WHAT);
            msg.obj = rechargeMoneyD*10000;
            handler.sendMessage(msg);
        }
    }

    /**
     * 用户进入这个页面给出要实名认证的提示
     * @param rechargeType 充值类型 kjcz:快捷充值 pos:pos机充值
     */
    private void showRechargePrompt(final String rechargeType,String contentStr){
        View popView = LayoutInflater.from(this).inflate(R.layout.common_popwindow, null);
        int[] screen = SettingsManager.getScreenDispaly(RechargePosActivity.this);
        int width = screen[0]*4/5;
        int height = screen[1]*1/5;
        CommonPopwindow popwindow = new CommonPopwindow(RechargePosActivity.this,popView, width, height,rechargeType,contentStr,
                null);
        popwindow.show(mainLayout);
    }

    /**
     *获取通联交易订单号
     * @param userId
     * @param amount
     * @param from
     */
    private void requestTLOrderNum(String userId,final double amount,String from){
        if(mLoadingDialog != null){
            mLoadingDialog.show();
        }
        AsyncTLOrder orderTask = new AsyncTLOrder(RechargePosActivity.this, userId,
                String.valueOf(amount), from, new Inter.OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                    mLoadingDialog.dismiss();
                }
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        TLOrderInfo orderInfo = baseInfo.getTlOrderInfo();
                        Intent intent = new Intent(RechargePosActivity.this,RechargePosQRCodeActivity.class);
                        intent.putExtra("amount",amount);
                        intent.putExtra("userInfo",userInfo);
                        intent.putExtra("orderInfo",orderInfo);
                        startActivity(intent);
                        finish();
                    }else{
                        Util.toastLong(RechargePosActivity.this,baseInfo.getMsg());
                    }
                }
            }
        });
        orderTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * 获取用户信息
     * @param userId
     * @param phone
     */
    private void requestUserInfo(final String userId,String phone){
        if(mLoadingDialog != null){
            mLoadingDialog.show();
        }
        AsyncUserSelectOne userTask = new AsyncUserSelectOne(RechargePosActivity.this, userId, phone, "","",
                new Inter.OnGetUserInfoByPhone() {
                    @Override
                    public void back(BaseInfo baseInfo) {
                        if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                            mLoadingDialog.dismiss();
                        }
                        if(baseInfo != null){
                            int resultCode = SettingsManager.getResultCode(baseInfo);
                            if(resultCode == 0){
                                userInfo = baseInfo.getUserInfo();
                                initUserInfo(userInfo);
                            }
                        }
                    }
                });
        userTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }
}
