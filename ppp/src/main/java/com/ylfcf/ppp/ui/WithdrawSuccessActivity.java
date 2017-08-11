package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

/**
 * 提现成功页面
 * 
 * @author Administrator
 * 
 */
public class WithdrawSuccessActivity extends BaseActivity implements
		OnClickListener {
	private static final String className = "WithdrawSuccessActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView withdrawMoneyText;
	private Button withdrawBtn;

	String withdrawMoney = "";
	double withdrawMoneyD = 0d;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.withdraw_success_activity);
		withdrawMoney = getIntent().getStringExtra("withdraw_money");
		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("账户提现");

		try {
			withdrawMoneyD = Double.parseDouble(withdrawMoney);
		} catch (Exception e) {
		}
		withdrawMoneyText = (TextView) findViewById(R.id.withdraw_result_money);
		withdrawMoneyText.setText(Util.double2PointDouble(withdrawMoneyD));
		withdrawBtn = (Button) findViewById(R.id.withdraw_result_btn);
		withdrawBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.withdraw_result_btn:
			Intent intent = new Intent(WithdrawSuccessActivity.this,
					WithdrawListActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
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
}
