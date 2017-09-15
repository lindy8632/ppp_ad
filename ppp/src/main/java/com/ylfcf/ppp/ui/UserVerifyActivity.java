package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncBFVerify;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.view.CommonPopwindow;

/**
 * 用户实名认证页面
 * 
 * 充值、提现或者认购时，如果用户还没有实名过，则跳转到此页面;
 * 当用户已经填写完个人资料并且点击了提交按钮，系统会有一段时间的认证过程；
 * 如果在此过程中用户再次点击充值、提现、认购按钮跳转到此页面的时候，用户的真实姓名和身份证号等信息是不可编辑的状态，
 * 提交按钮不可点击，并在此页面也有相应的认证结果提示；
 * 一旦认证成功，此页面不会再显示出来，没有入口跳转到此页面。
 * @author Mr.liu
 *
 */
public class UserVerifyActivity extends BaseActivity implements OnClickListener{
	private static final String className = "UserVerifyActivity";
	private EditText realNameET;
	private EditText idNumberET;
	private Button commitBtn;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private LinearLayout mainLayout;
	
	private String type = "";//充值、提现；表示是从充值流程还是提现流程跳转过来的。
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_verify_activity);
		Bundle bundle = getIntent().getBundleExtra("bundle");
		if(bundle != null){
			type = bundle.getString("type");
		}
		findViews();
		
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("实名认证");
		
		mainLayout = (LinearLayout) findViewById(R.id.user_verify_activity_mainlayout);
		realNameET = (EditText)findViewById(R.id.user_verify_activity_realname);
		idNumberET = (EditText)findViewById(R.id.user_verify_activity_idnumber); 
		
		commitBtn = (Button)findViewById(R.id.user_verify_activity_sure_btn);
		commitBtn.setOnClickListener(this);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				showVerifyPrompt();
			}
		}, 500L);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_verify_activity_sure_btn:
			chackData();
			break;
		case R.id.common_topbar_left_layout:
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void chackData(){
		String idNumber = idNumberET.getText().toString();
		String realName = realNameET.getText().toString();
		if (!"".equals(idNumber)) {
			if (!"".equals(realName)) {
				requestUserVerify(
						SettingsManager.getUserId(UserVerifyActivity.this),
						idNumber, realName);
			} else {
				Util.toastShort(UserVerifyActivity.this, "真实姓名不能为空");
			}
		} else {
			Util.toastShort(UserVerifyActivity.this, "身份证号码不合法");
		}
	}
	
	/**
	 * 用户进入这个页面给出要实名认证的提示
	 */
	private void showVerifyPrompt(){
		View popView = LayoutInflater.from(this).inflate(R.layout.common_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(UserVerifyActivity.this);
		int width = screen[0]*4/5;
		int height = screen[1]*1/5;
		CommonPopwindow popwindow = new CommonPopwindow(UserVerifyActivity.this,popView, width, height,"实名认证");
		popwindow.show(mainLayout);
	}
	
	/**
	 * 用户实名认证
	 * @param userId
	 * @param realName
	 * @param idNumber
	 */
	private void requestUserVerify(String userId,String idNumber,String realName){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncBFVerify task = new AsyncBFVerify(UserVerifyActivity.this, userId, idNumber, realName, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						Util.toastShort(UserVerifyActivity.this, "实名认证成功");
						if("邀请有奖".equals(type)){
							Intent intent = new Intent(UserVerifyActivity.this,InvitateActivity.class);
							intent.putExtra("is_verify", true);
							startActivity(intent);
						}else if("中秋大转盘分享".equals(type)){
							Intent intent = new Intent();
							setResult(101,intent);
							finish();
						}else if("领取加息券".equals(type)){
							finish();
						}else{
							Intent intent = new Intent(UserVerifyActivity.this,BindCardActivity.class);
							intent.putExtra("bundle", getIntent().getBundleExtra("bundle"));
							startActivity(intent);
						}
						finish();
					}else{
						Util.toastShort(UserVerifyActivity.this, baseInfo.getMsg());
					}
				}
			}
		});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
}
