package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
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
import com.ylfcf.ppp.view.VerifySucPopwindow;

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
	private String rechargeType = "";//充值类型 kjcz:快捷充值 pos:pos机充值
	private boolean isVerify = false;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_verify_activity);
		Bundle bundle = getIntent().getBundleExtra("bundle");
		if(bundle != null){
			type = bundle.getString("type");
			rechargeType = bundle.getString("recharge_type");
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

//		if(!"充值".equals(type)){
//			handler.postDelayed(new Runnable() {
//
//				@Override
//				public void run() {
//					showVerifyPrompt();
//				}
//			}, 500L);
//		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_verify_activity_sure_btn:
			chackData();
			break;
		case R.id.common_topbar_left_layout:
			if(isVerify && "邀请有奖".equals(type)){
				try{
					mApp.getInvitateActivity().finish();
				}catch (Exception e){
					e.printStackTrace();
				}
				Intent intent = new Intent(UserVerifyActivity.this,InvitateActivity.class);
				intent.putExtra("is_verify", true);
				startActivity(intent);
			}
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.out.println("按下了back键   onKeyDown()");
			if("邀请有奖".equals(type)){
				try{
					mApp.getInvitateActivity().finish();
				}catch (Exception e){
					e.printStackTrace();
				}
				Intent intent = new Intent(UserVerifyActivity.this,InvitateActivity.class);
				intent.putExtra("is_verify", true);
				startActivity(intent);
			}
			finish();
			return false;
		}else {
			return super.onKeyDown(keyCode, event);
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
		handler.removeCallbacksAndMessages(null);
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
		CommonPopwindow popwindow = new CommonPopwindow(UserVerifyActivity.this,popView, width, height,"实名认证","",null);
		popwindow.show(mainLayout);
	}

	/**
	 * 认证成功的弹窗
	 */
	private void showVerifySucPrompt(){
		View popView = LayoutInflater.from(this).inflate(
				R.layout.verify_suc_popwindow_layout, null);
		int[] screen = SettingsManager.getScreenDispaly(this);
		int width = screen[0] * 6 / 7;
		int height = screen[1] /3;
		VerifySucPopwindow popwindow = new VerifySucPopwindow(UserVerifyActivity.this,
				popView, width, height,new OnPopRechargeBtnListener(){
			@Override
			public void back() {
				//去充值
				if("投资".equals(type) || "提现".equals(type) || "标示条".equals(type) || "邀请有奖".equals(type)
						|| "注册成功".equals(type)){
					Intent intent = new Intent(UserVerifyActivity.this,RechargeChooseActivity.class);
					startActivity(intent);
				}else{
					if("kjcz".equals(rechargeType)){
						//快捷充值
						Intent intent = new Intent(UserVerifyActivity.this,BindCardActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("type","充值");
						intent.putExtra("bundle",bundle);
						startActivity(intent);
					}else if("pos".equals(rechargeType)){
						//pos支付
						Intent intent = new Intent(UserVerifyActivity.this,RechargePosActivity.class);
						startActivity(intent);
					}
				}
				if("邀请有奖".equals(type)){
					try{
						mApp.getInvitateActivity().finish();
					}catch (Exception e){
						e.printStackTrace();
					}
				}
				finish();
			}
		},new OnPopCancelBtnListener(){
			@Override
			public void back() {
				//下次再说
				if("邀请有奖".equals(type)){
					try{
						mApp.getInvitateActivity().finish();
					}catch (Exception e){
						e.printStackTrace();
					}
					Intent intent = new Intent(UserVerifyActivity.this,InvitateActivity.class);
					intent.putExtra("is_verify", true);
					startActivity(intent);
				}
				finish();
			}
		});
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
		final AsyncBFVerify task = new AsyncBFVerify(UserVerifyActivity.this, userId, idNumber, realName, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						isVerify = true;
						if("中秋大转盘分享".equals(type)){
							Intent intent = new Intent();
							setResult(101,intent);
							finish();
						}else if("领取加息券".equals(type)){
							finish();
						}else{
							showVerifySucPrompt();
						}
					}else{
						Util.toastShort(UserVerifyActivity.this, baseInfo.getMsg());
					}
				}
			}
		});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 实名认证成功的弹窗 监听“去充值按钮”
	 */
	public interface OnPopRechargeBtnListener{
		void back();
	}

	/**
	 * 实名认证成功的弹窗 监听“下次再说按钮”
	 */
	public interface OnPopCancelBtnListener{
		void back();
	}
	
}
