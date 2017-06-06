package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;

/**
 * 支付通道变更说明
 * @author Mr.liu
 *
 */
public class RechargeChannelActivity extends BaseActivity implements OnClickListener{
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private Button okBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recharge_channel_activity);
		
		findViews();
	}

	private void findViews(){
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("绑定银行卡");
		
		okBtn = (Button) findViewById(R.id.recharge_channel_activity_btn);
		okBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
		case R.id.recharge_channel_activity_btn:
			finish();
			break;

		default:
			break;
		}
	}
}
