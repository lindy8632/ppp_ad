package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.view.CommonPopwindow;

/**
 * 选择充值类型
 * Created by Administrator on 2018/1/15.
 */

public class RechargeChooseActivity extends BaseActivity implements View.OnClickListener{
    private static final String className = "RechargeChooseActivity";

    private static final int REQUEST_VERIFY_WHAT = 2654;
    private static final int REQUEST_BINDCARD_WHAT = 2655;


    private LinearLayout mainLayout;
    private LinearLayout topLeftBtn;
    private TextView topTitleTV;

    private ImageView kjczIV,posIV;

    private boolean isVerify = false;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REQUEST_VERIFY_WHAT:
                    checkIsVerify();
                    break;
                case REQUEST_BINDCARD_WHAT:
                    checkIsBindCard("充值");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.recharge_choose_activity);
        findViews();

        if(mLoadingDialog != null){
            mLoadingDialog.show();
        }
        handler.sendEmptyMessageDelayed(REQUEST_VERIFY_WHAT,500L);
    }

    private void findViews(){
        topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
        topLeftBtn.setOnClickListener(this);
        topTitleTV = (TextView) findViewById(R.id.common_page_title);
        topTitleTV.setText("选择充值类型");

        mainLayout = (LinearLayout) findViewById(R.id.recharge_choose_activity_mainlayout);
        kjczIV = (ImageView) findViewById(R.id.recharge_choose_activity_kjzf);
        kjczIV.setOnClickListener(this);
        posIV = (ImageView) findViewById(R.id.recharge_choose_activity_pos);
        posIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_topbar_left_layout:
                finish();
                break;
            case R.id.recharge_choose_activity_kjzf:
                //快捷充值
                if(isVerify){
                    handler.sendEmptyMessage(REQUEST_BINDCARD_WHAT);
                }else{
                    showVerifyPrompt("kjcz");
                }
                break;
            case R.id.recharge_choose_activity_pos:
                //pos充值
                if(isVerify){
                    Intent intent = new Intent(RechargeChooseActivity.this,RechargePosActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    showVerifyPrompt("pos");
                }
                break;
        }
    }

    /**
     * 用户进入这个页面给出要实名认证的提示
     * @param rechargeType 充值类型 kjcz:快捷充值 pos:pos机充值
     */
    private void showVerifyPrompt(final String rechargeType){
        View popView = LayoutInflater.from(this).inflate(R.layout.common_popwindow, null);
        int[] screen = SettingsManager.getScreenDispaly(RechargeChooseActivity.this);
        int width = screen[0]*4/5;
        int height = screen[1]*1/5;
        CommonPopwindow popwindow = new CommonPopwindow(RechargeChooseActivity.this,popView, width, height,"实名认证","",
                new OKBtnListener(){
                    @Override
                    public void back() {
                        Intent intent = new Intent(RechargeChooseActivity.this,UserVerifyActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("type","充值");
                        bundle.putString("recharge_type",rechargeType);
                        intent.putExtra("bundle",bundle);
                        startActivity(intent);
                        finish();
                    }
                });
        popwindow.show(mainLayout);
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

    /**
     * 验证用户是否已经认证
     */
    private void checkIsVerify(){
        RequestApis.requestIsVerify(RechargeChooseActivity.this, SettingsManager.getUserId(this), new Inter.OnIsVerifyListener() {
            @Override
            public void isVerify(boolean flag, Object object) {
                if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                    mLoadingDialog.dismiss();
                }
                isVerify = flag;
            }

            @Override
            public void isSetWithdrawPwd(boolean flag, Object object) {
                //用户是否已经设置提现密码
            }
        });
    }

    /**
     * 判断用户是否已经绑卡
     * @param type "充值","提现","邀请有奖"
     */
    private void checkIsBindCard(final String type){
        if(mLoadingDialog != null){
            mLoadingDialog.show();
        }
        RequestApis.requestIsBinding(RechargeChooseActivity.this,
                SettingsManager.getUserId(RechargeChooseActivity.this), "宝付", new Inter.OnIsBindingListener() {
            @Override
            public void isBinding(boolean flag, Object object) {
                if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                    mLoadingDialog.dismiss();
                }
                Intent intent = new Intent();
                if(flag){
                    //用户已经绑卡
                    if("充值".equals(type)){
                        //那么直接跳到充值页面
                        intent.setClass(RechargeChooseActivity.this, RechargeActivity.class);
                    }
                    startActivity(intent);
                }else{
                    //用户还没有绑卡
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
                    intent.putExtra("bundle", bundle);
                    intent.setClass(RechargeChooseActivity.this, BindCardActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });
    }

    /**
     * 点击popwindow的确定按钮的监听
     */
    public interface OKBtnListener{
        void back();
    }
}
