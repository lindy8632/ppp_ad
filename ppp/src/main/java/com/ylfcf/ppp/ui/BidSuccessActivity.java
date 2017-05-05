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
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 政信贷--- 投资成功页面
 * @author Administrator
 *
 */
public class BidSuccessActivity extends BaseActivity implements OnClickListener{
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView catRecords;
	private TextView promptTV;
	private Button continueBtn;
	private String fromWhere;
	private BaseInfo mBaseInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bid_success_activity);
		fromWhere = getIntent().getStringExtra("from_where");
		mBaseInfo = (BaseInfo) getIntent().getSerializableExtra("base_info");
		findViews();
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("投资");
		promptTV = (TextView)findViewById(R.id.bid_success_activity_prompt_text);
		continueBtn = (Button)findViewById(R.id.bid_success_activity_continue_btn);
		continueBtn.setOnClickListener(this);
		catRecords = (TextView)findViewById(R.id.bid_success_activity_cat_record);
		catRecords.setOnClickListener(this);

		if(mBaseInfo != null && mBaseInfo.getmInvestResultInfo() != null){
			if("0".equals(mBaseInfo.getmInvestResultInfo().getmInvestStatus().getStatus())){
				//当天第一次投资
				promptTV.setVisibility(View.VISIBLE);
			}else{
				promptTV.setVisibility(View.GONE);
			}
		}else{
			promptTV.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.bid_success_activity_continue_btn:
			SettingsManager.setMainProductListFlag(getApplicationContext(), true);
			finish();
			break;
		case R.id.bid_success_activity_cat_record:
			Intent intent = new Intent(BidSuccessActivity.this, UserInvestRecordActivity.class);
			intent.putExtra("from_where", fromWhere);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}
}
