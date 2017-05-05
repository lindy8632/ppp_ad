package com.ylfcf.ppp.ui;

import com.ylfcf.ppp.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 元信宝 --- 认购成功页面
 * @author Administrator
 *
 */
public class YXBBidSuccessActivity extends BaseActivity implements OnClickListener{
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView catRecords;
	private Button continueBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.yxb_bid_suc_activity);
		
		findViews();
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("认购");
		continueBtn = (Button)findViewById(R.id.yxb_bid_success_activity_continue_btn);
		continueBtn.setOnClickListener(this);
		catRecords = (TextView)findViewById(R.id.yxb_bid_success_activity_cat_record);
		catRecords.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.yxb_bid_success_activity_continue_btn:
			Intent yxbBidIntent = new Intent(YXBBidSuccessActivity.this,BidYXBActivity.class);
			startActivity(yxbBidIntent);
			finish();
			break;
		case R.id.yxb_bid_success_activity_cat_record:
			Intent intent = new Intent(YXBBidSuccessActivity.this,YXBTransRecordActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}
}
