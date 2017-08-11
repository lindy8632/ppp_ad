package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.util.UMengStatistics;

/**
 * 注册成功页面
 * @author Mr.liu
 *
 */
public class RegisterSucActivity extends BaseActivity implements OnClickListener{
	private static final String className = "RegisterSucActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private ImageView logo;
	private Button verifyBtn;//立即实名认证
	private Button catMoneyBtn;//查看元金币
	private Button ztcBtn;//轻松赚提成
	private String extensionCode = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_suc_activity);
		extensionCode = getIntent().getStringExtra("extension_code");
		findViews();
	}

	private void findViews(){
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("注册成功");
		
		logo = (ImageView) findViewById(R.id.register_suc_activity_logo);
		verifyBtn = (Button) findViewById(R.id.register_suc_activity_verify_btn);
		verifyBtn.setOnClickListener(this);
		catMoneyBtn = (Button) findViewById(R.id.register_suc_activity_catyuanmoney_btn);
		catMoneyBtn.setOnClickListener(this);
		ztcBtn = (Button) findViewById(R.id.register_suc_activity_layout_ztc_btn);
		ztcBtn.setOnClickListener(this);
		if("".equals(extensionCode) || extensionCode == null){
			logo.setImageResource(R.drawable.register_suc_logo);
		}else{
			logo.setImageResource(R.drawable.register_suc_logo1);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.register_suc_activity_verify_btn:
			//立即认证
			intent.setClass(RegisterSucActivity.this,UserVerifyActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.register_suc_activity_catyuanmoney_btn:
			//查看元金币
			intent.setClass(RegisterSucActivity.this,MyYuanMoneyActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.register_suc_activity_layout_ztc_btn:
			//轻松赚提成
			intent.setClass(RegisterSucActivity.this,InvitateActivity.class);
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
