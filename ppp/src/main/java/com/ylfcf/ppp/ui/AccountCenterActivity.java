package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.fragment.AccountCenterHKRLFragment;
import com.ylfcf.ppp.fragment.AccountCenterZHZCFragment;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.widget.LoadingDialog;

/**
 * 账户中心
 * Created by Administrator on 2017/8/2.
 */

public class AccountCenterActivity extends BaseActivity implements View.OnClickListener{
    private static final String className = "AccountCenterActivity";
    private LinearLayout topLeftBtn;
    private TextView topTitleTV;

    private Button zhzcTabBtn,hkrlTabBtn;//账户资产、回款日历
    public LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.account_center_activity);
        loadingDialog = mLoadingDialog;
        fragmentManager = getSupportFragmentManager();
        findViews();
    }

    private void findViews(){
        topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
        topLeftBtn.setOnClickListener(this);
        topTitleTV = (TextView)findViewById(R.id.common_page_title);
        topTitleTV.setText("账户中心");

        zhzcTabBtn = (Button)findViewById(R.id.account_center_activity_tab_zhzc);
        zhzcTabBtn.setOnClickListener(this);
        hkrlTabBtn = (Button)findViewById(R.id.account_center_activity_tab_hkrl);
        hkrlTabBtn.setOnClickListener(this);
        onNavBtnOnClick(zhzcTabBtn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_topbar_left_layout:
                finish();
                break;
            case R.id.account_center_activity_tab_zhzc:
            case R.id.account_center_activity_tab_hkrl:
                onNavBtnOnClick(v);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UMengStatistics.statisticsResume(this);//友盟统计
    }

    @Override
    protected void onPause() {
        super.onPause();
        UMengStatistics.statisticsPause(this);//友盟统计
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragmentManager = null;
        finish();
    }

    Fragment zhzcFragment;
    Fragment hkrlFragment;
    public FragmentManager fragmentManager;
    private void onNavBtnOnClick(View v){
        FragmentTransaction trasection = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.account_center_activity_tab_zhzc:
                //账户资产
                String flag1 = (String) v.getTag();
                if("0".equals(flag1)){
                    zhzcFragment = new AccountCenterZHZCFragment();
                    trasection.replace(R.id.account_center_activity_mainlayout, zhzcFragment);

                    zhzcTabBtn.setBackgroundResource(R.drawable.style_funds_details_nav_yxb);
                    zhzcTabBtn.setTextColor(getResources().getColor(R.color.white));
                    zhzcTabBtn.setTag("1");
                    hkrlTabBtn.setBackgroundResource(R.color.transparent);
                    hkrlTabBtn.setTag("0");
                    hkrlTabBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
                }
                break;
            case R.id.funds_details_activity_nav_zxd_btn:
                String flag2 = (String) v.getTag();
                if("0".equals(flag2)){
                    hkrlFragment = new AccountCenterHKRLFragment();
                    trasection.replace(R.id.account_center_activity_mainlayout, hkrlFragment);

                    zhzcTabBtn.setBackgroundResource(R.drawable.style_funds_details_nav_dqvip);
                    zhzcTabBtn.setTextColor(getResources().getColor(R.color.white));
                    zhzcTabBtn.setTag("1");
                    hkrlTabBtn.setBackgroundResource(R.color.transparent);
                    hkrlTabBtn.setTag("0");
                    hkrlTabBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
                }
                break;
            default:
                break;
        }
        trasection.commit();
    }
}
