package com.ylfcf.ppp.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.RechargeTempInfo;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 支付结果界面
 * 
 * @author Administrator
 * 
 */
public class RechargeResultActivity extends BaseActivity implements
		OnClickListener {
	private TextView timeTV;
	private TextView moneyTV;
	private LinearLayout moneyLayout;
	private TextView promptTV;
	private Button investBtn,catProofBtn;
	private SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd    HH:mm:ss");
	private RechargeTempInfo tempInfo;
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recharge_result_activity);
		Intent intent = getIntent();
		tempInfo = (RechargeTempInfo) intent
				.getSerializableExtra("RechargeTempInfo");
		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);

		promptTV = (TextView) findViewById(R.id.recharge_result_prompttext);
		timeTV = (TextView) findViewById(R.id.recharge_result_time);
		timeTV.setText(sdf.format(new Date()));
		moneyTV = (TextView) findViewById(R.id.recharge_result_money);
		moneyLayout = (LinearLayout) findViewById(R.id.recharge_result_money_layout);
		investBtn = (Button) findViewById(R.id.recharge_result_invest_btn);
		investBtn.setOnClickListener(this);
		catProofBtn = (Button) findViewById(R.id.recharge_result_proof_btn);
		catProofBtn.setOnClickListener(this);

		if ("recharge".equals(tempInfo.getType())) {
			// 充值跳转过来的
			double rechargeMoney = 0d;
			try {
				rechargeMoney = Double.parseDouble(tempInfo.getRechargeMoney());
			} catch (Exception e) {
			}
			topTitleTV.setText("充值");
			moneyLayout.setVisibility(View.VISIBLE);

			moneyTV.setText(Util.double2PointDouble(rechargeMoney));
			promptTV.setText("操作成功,充值订单正在处理");
		} else if ("bindcard".equals(tempInfo.getType())) {
			// 绑卡页面条转过来的
			topTitleTV.setText("绑卡认证");
			moneyLayout.setVisibility(View.GONE);
			promptTV.setText("操作成功,绑卡认证正在处理");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.recharge_result_invest_btn:
			SettingsManager.setMainProductListFlag(getApplicationContext(),
					true);
			Intent intent = new Intent(RechargeResultActivity.this,
					MainFragmentActivity.class);
			startActivity(intent);
			finish();
			mApp.finishAllActivityExceptMain();
			break;
		case R.id.recharge_result_proof_btn:
			Intent intentProof = new Intent(RechargeResultActivity.this,RechargeProofActivity.class);
			intentProof.putExtra("recharge_id", tempInfo.getOrder_sn());
			startActivity(intentProof);
			break;
		default:
			break;
		}
	}
}
