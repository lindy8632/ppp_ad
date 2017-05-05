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
import com.ylfcf.ppp.async.AsyncUserYUANAccount;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserYUANAccountInfo;
import com.ylfcf.ppp.inter.Inter.OnUserYUANAccountInter;
import com.ylfcf.ppp.util.SettingsManager;
/**
 * 我的元金币
 * @author jianbing
 *
 */
public class MyYuanMoneyActivity extends BaseActivity implements OnClickListener{
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private TextView yuanMoneyUsed;//可用
	private TextView yuanMoneyFrozen;//冻结
	private TextView yuanMoneyDaishou;//待收
	private Button bidBtn;
	private View mainLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myyuanmoney_activity);
		
		findViews();
		requestYuanMoney(SettingsManager.getUserId(getApplicationContext()));
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("我的元金币");
		
		mainLayout = findViewById(R.id.myyuanmoney_activity_mainlayout);
		
		yuanMoneyUsed = (TextView)findViewById(R.id.myyuanmoney_actiivty_used_balance);
		yuanMoneyFrozen = (TextView)findViewById(R.id.myyuanmoney_actiivty_frozen_balance);
		yuanMoneyDaishou = (TextView)findViewById(R.id.myyuanmoney_actiivty_daishou_balance);
		bidBtn = (Button)findViewById(R.id.myyuanmoney_activity_bidBtn);
		bidBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.myyuanmoney_activity_bidBtn:
			SettingsManager.setMainProductListFlag(getApplicationContext(), true);
			Intent intent = new Intent(MyYuanMoneyActivity.this,MainFragmentActivity.class);
			startActivity(intent);
			mApp.finishAllActivityExceptMain();
			break;
		default:
			break;
		}
	}
	
	private void initData(UserYUANAccountInfo info){
		yuanMoneyUsed.setText(info.getUse_coin());
		yuanMoneyFrozen.setText(info.getFrozen_coin());
		yuanMoneyDaishou.setText(info.getCollection_coin());
	}
	
	private void requestYuanMoney(String userId){
		AsyncUserYUANAccount accountTask = new AsyncUserYUANAccount(MyYuanMoneyActivity.this, 
				userId, new OnUserYUANAccountInter() {
					@Override
					public void back(BaseInfo info) {
						int resultCode = SettingsManager.getResultCode(info);
						if(resultCode == 0){
							initData(info.getYuanAccountInfo());
						}
					}
				});
		accountTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
