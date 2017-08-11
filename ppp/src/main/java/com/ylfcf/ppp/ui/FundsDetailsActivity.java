package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.fragment.FundDetailsYXBFragment;
import com.ylfcf.ppp.fragment.FundDetailsZXDFragment;
import com.ylfcf.ppp.inter.Inter.OnIsYXBInvestorListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.widget.LoadingDialog;

/**
 * 资金明细
 * @author Administrator
 *
 */
public class FundsDetailsActivity extends BaseActivity implements OnClickListener{
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private LinearLayout navLayout;
	
	private Button yxbBtn,dqlcBtn;
	public LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.funds_details_activity);
		loadingDialog = mLoadingDialog;
		fragmentManager = getSupportFragmentManager();
		findViews();
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("资金明细");
			
		navLayout = (LinearLayout) findViewById(R.id.funds_details_activity_navlayout);
		yxbBtn = (Button)findViewById(R.id.funds_details_activity_nav_yxb_btn);
		yxbBtn.setOnClickListener(this);
		dqlcBtn = (Button)findViewById(R.id.funds_details_activity_nav_zxd_btn);
		dqlcBtn.setOnClickListener(this);
		onNavBtnOnClick(dqlcBtn);
		RequestApis.requestIsYXBInvestor(FundsDetailsActivity.this, SettingsManager.getUserId(getApplicationContext()), 0, 10, 
				new OnIsYXBInvestorListener() {
					@Override
					public void isYXBInvestor(boolean flag) {
						if(flag){
							//投资过元信宝
							navLayout.setVisibility(View.VISIBLE);
						}else{
							//未投资过元信宝
							navLayout.setVisibility(View.GONE);
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.funds_details_activity_nav_zxd_btn:
		case R.id.funds_details_activity_nav_yxb_btn:
			onNavBtnOnClick(v);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsResume(this);//友盟统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		UMengStatistics.statisticsPause(this);//友盟统计时长
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		fragmentManager = null;
		finish();
	}

	Fragment yxbFragment;
	Fragment zxdFragment;
	public FragmentManager fragmentManager; 
	private void onNavBtnOnClick(View v){
		FragmentTransaction trasection = fragmentManager.beginTransaction();
		switch (v.getId()) {
		case R.id.funds_details_activity_nav_yxb_btn:
			String flag1 = (String) v.getTag();
			if("0".equals(flag1)){
				yxbFragment = new FundDetailsYXBFragment();
				trasection.replace(R.id.funds_details_activity_mainlayout, yxbFragment);
				
				yxbBtn.setBackgroundResource(R.drawable.style_funds_details_nav_yxb);
				yxbBtn.setTextColor(getResources().getColor(R.color.white));
				yxbBtn.setTag("1");
				dqlcBtn.setBackgroundResource(R.color.transparent);
				dqlcBtn.setTag("0");
				dqlcBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
			}
			break;
		case R.id.funds_details_activity_nav_zxd_btn:
			String flag2 = (String) v.getTag();
			if("0".equals(flag2)){
				zxdFragment = new FundDetailsZXDFragment();
				trasection.replace(R.id.funds_details_activity_mainlayout, zxdFragment);
				
				dqlcBtn.setBackgroundResource(R.drawable.style_funds_details_nav_dqvip);
				dqlcBtn.setTextColor(getResources().getColor(R.color.white));
				dqlcBtn.setTag("1");
				yxbBtn.setBackgroundResource(R.color.transparent);
				yxbBtn.setTag("0");
				yxbBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
			}
			break;
		default:
			break;
		}
		trasection.commit();
	}
}
