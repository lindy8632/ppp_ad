package com.ylfcf.ppp.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncGetUserInfoByPhone;
import com.ylfcf.ppp.async.AsyncSMSRegiste;
import com.ylfcf.ppp.async.AsyncUpdateUserInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.SMSType;
import com.ylfcf.ppp.entity.TaskDate;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;
import com.ylfcf.ppp.inter.Inter.OnUpdateUserInfoInter;
import com.ylfcf.ppp.util.Constants.UserType;
import com.ylfcf.ppp.util.CountDownAsyncTask;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * 忘记密码
 * 
 * @author Administrator
 * 
 */
@SuppressLint("ResourceAsColor")
public class ForgetPwdActivity extends BaseActivity implements OnClickListener {
	private static final String className = "ForgetPwdActivity";
	private static final int REQUEST_UPDATE_USERINFO_WHAT = 1301;
	private static final int REQUEST_UPDATE_USERINFO_SUCCESS = 1302;
	private static final int REQUEST_UPDATE_USERINFO_FAILE = 1303;
	private static final int REQUEST_UPDATE_USERINFO_EXCEPTION = 1304;

	private static final int REQUEST_SEND_SMS_SUCCESS = 1305;
	private static final int REQEUST_SEND_SMS_FAILE = 1306;

	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView stepTopText1, stepTopText2, stepTopText3, stepTopText4;// 步骤
																			// 上面的圆
	private Button navPersonalBtn,navCompanyBtn;
	private Button companyBtn;
	private View personalForgotLayout,companyForgotLayout;
	private TextView stepBottomText1, stepBottomText2, stepBottomText3,
			stepBottomText4;// 步骤 下面的字
	private LinearLayout phoneLayout, checkPwdLayout, resetPwdLayout,
			finishLayout;// 输入手机号，验证密码，重置密码，完成
	private Button inputPhoneNext, checkPwdNext, resetPwdNext, finishBtn,
			sendSMSCodeBtn, finishRepeatLoginBtn;
	private EditText inputPhoneET, smsAuthCodeET, newPwdET, repeatNewPwdET;	
	/**
	 * 提示
	 */
	private LinearLayout promptLayout;
	private TextView promptPhone;

	private String phoneNum;
	private String authnumSMSUser = "";// 用户输入的手机验证码
	private String authnumSMSWeb = "";// 系统生成的手机验证码
	private UserInfo user;

	private CountDownAsyncTask countDownAsynTask = null;
	private final long intervalTime = 1000L;
	private String fromWhere = "";

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case CountDownAsyncTask.PROGRESS_UPDATE:
				TaskDate date = (TaskDate) msg.obj;
				long time = date.getTime();
				countDownView(time);
				break;
			case CountDownAsyncTask.FINISH:
				sendSMSCodeBtn.setText("获取验证码");
				sendSMSCodeBtn.setEnabled(true);
				break;
			case REQUEST_UPDATE_USERINFO_WHAT:

				break;
			case REQUEST_UPDATE_USERINFO_SUCCESS:
				updateStepLayoutBg(4);
				break;
			case REQUEST_UPDATE_USERINFO_FAILE:
				Util.toastShort(ForgetPwdActivity.this, "网络异常");
				break;
			case REQUEST_UPDATE_USERINFO_EXCEPTION:
				BaseInfo info = (BaseInfo) msg.obj;
				Util.toastShort(ForgetPwdActivity.this, info.getError());
				break;
			case REQUEST_SEND_SMS_SUCCESS:
				promptLayout.setVisibility(View.VISIBLE);
				promptPhone.setText(phoneNum);
				break;
			case REQEUST_SEND_SMS_FAILE:
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				Util.toastShort(ForgetPwdActivity.this, baseInfo.getError());
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forget_pwd_activity);
		fromWhere = getIntent().getStringExtra("from_where");
		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("找回密码");

		stepTopText1 = (TextView) findViewById(R.id.forget_pwd_top_step1);
		stepTopText2 = (TextView) findViewById(R.id.forget_pwd_top_step2);
		stepTopText3 = (TextView) findViewById(R.id.forget_pwd_top_step3);
		stepTopText4 = (TextView) findViewById(R.id.forget_pwd_top_step4);

		stepBottomText1 = (TextView) findViewById(R.id.forget_pwd_bottom_step1);
		stepBottomText2 = (TextView) findViewById(R.id.forget_pwd_bottom_step2);
		stepBottomText3 = (TextView) findViewById(R.id.forget_pwd_bottom_step3);
		stepBottomText4 = (TextView) findViewById(R.id.forget_pwd_bottom_step4);

		phoneLayout = (LinearLayout) findViewById(R.id.forget_pwd_phonenum_layout);
		checkPwdLayout = (LinearLayout) findViewById(R.id.forget_pwd_check_layout);
		resetPwdLayout = (LinearLayout) findViewById(R.id.forget_pwd_reset_layout);
		finishLayout = (LinearLayout) findViewById(R.id.forget_pwd_finish_layout);

		inputPhoneNext = (Button) findViewById(R.id.forget_pwd_phonenum_btn);// 填写手机号码点击下一步
		inputPhoneNext.setOnClickListener(this);
		checkPwdNext = (Button) findViewById(R.id.forget_pwd_check_btn);// 验证密码点击下一步
		checkPwdNext.setOnClickListener(this);
		resetPwdNext = (Button) findViewById(R.id.forget_pwd_reset_btn);// 重置密码点击下一步
		resetPwdNext.setOnClickListener(this);
		finishBtn = (Button) findViewById(R.id.forget_pwd_finish_loginbtn);// 完成
		finishBtn.setOnClickListener(this);
		sendSMSCodeBtn = (Button) findViewById(R.id.forget_pwd_get_authnum_btn);
		sendSMSCodeBtn.setOnClickListener(this);

		inputPhoneET = (EditText) findViewById(R.id.forget_pwd_phonenum_et);
		smsAuthCodeET = (EditText) findViewById(R.id.forget_pwd_check_et);
		newPwdET = (EditText) findViewById(R.id.forget_pwd_reset_newpwd_input);
		repeatNewPwdET = (EditText) findViewById(R.id.forget_pwd_reset_newpwd_ensure);

		promptLayout = (LinearLayout) findViewById(R.id.forget_pwd_activity_prompt_layout);
		promptPhone = (TextView) findViewById(R.id.forget_pwd_activity_prompt_phonenum);
		
		personalForgotLayout = findViewById(R.id.forget_pwd_activity_personal_mainlayout);
		companyForgotLayout = findViewById(R.id.forget_pwd_activity_company_mainlayout);
		navPersonalBtn = (Button) findViewById(R.id.forget_pwd_activity_nav_personal_btn);
		navPersonalBtn.setOnClickListener(this);
		navCompanyBtn = (Button) findViewById(R.id.forget_pwd_activity_nav_company_btn);
		navCompanyBtn.setOnClickListener(this);
		companyBtn = (Button) findViewById(R.id.forgot_pwd_company_layout_btn);
		companyBtn.setOnClickListener(this);
		if(UserType.USER_NORMAL_PERSONAL.equals(fromWhere)){
			initPersonalLayout();
		}else if(UserType.USER_COMPANY.equals(fromWhere)){
			initCompanyLayout();
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

	private void updateStepLayoutBg(int position) {
		if (position == 1) {
			stepTopText1.setBackgroundResource(R.drawable.style_circle_blue);
			stepBottomText1
					.setTextColor(getResources().getColor(R.color.blue1));
			stepTopText2.setBackgroundResource(R.drawable.style_circle_gray);
			stepBottomText2
					.setTextColor(getResources().getColor(R.color.gray2));
			stepTopText3.setBackgroundResource(R.drawable.style_circle_gray);
			stepBottomText3
					.setTextColor(getResources().getColor(R.color.gray2));
			stepTopText4.setBackgroundResource(R.drawable.style_circle_gray);
			stepBottomText4
					.setTextColor(getResources().getColor(R.color.gray2));
			phoneLayout.setVisibility(View.VISIBLE);
			checkPwdLayout.setVisibility(View.GONE);
			resetPwdLayout.setVisibility(View.GONE);
			finishLayout.setVisibility(View.GONE);
		} else if (position == 2) {
			stepTopText1.setBackgroundResource(R.drawable.style_circle_gray);
			stepBottomText1
					.setTextColor(getResources().getColor(R.color.gray2));
			stepTopText2.setBackgroundResource(R.drawable.style_circle_blue);
			stepBottomText2
					.setTextColor(getResources().getColor(R.color.blue1));
			stepTopText3.setBackgroundResource(R.drawable.style_circle_gray);
			stepBottomText3
					.setTextColor(getResources().getColor(R.color.gray2));
			stepTopText4.setBackgroundResource(R.drawable.style_circle_gray);
			stepBottomText4
					.setTextColor(getResources().getColor(R.color.gray2));
			phoneLayout.setVisibility(View.GONE);
			checkPwdLayout.setVisibility(View.VISIBLE);
			resetPwdLayout.setVisibility(View.GONE);
			finishLayout.setVisibility(View.GONE);
		} else if (position == 3) {
			stepTopText1.setBackgroundResource(R.drawable.style_circle_gray);
			stepBottomText1
					.setTextColor(getResources().getColor(R.color.gray2));
			stepTopText2.setBackgroundResource(R.drawable.style_circle_gray);
			stepBottomText2
					.setTextColor(getResources().getColor(R.color.gray2));
			stepTopText3.setBackgroundResource(R.drawable.style_circle_blue);
			stepBottomText3
					.setTextColor(getResources().getColor(R.color.blue1));
			stepTopText4.setBackgroundResource(R.drawable.style_circle_gray);
			stepBottomText4
					.setTextColor(getResources().getColor(R.color.gray2));
			phoneLayout.setVisibility(View.GONE);
			checkPwdLayout.setVisibility(View.GONE);
			resetPwdLayout.setVisibility(View.VISIBLE);
			finishLayout.setVisibility(View.GONE);
		} else if (position == 4) {
			stepTopText1.setBackgroundResource(R.drawable.style_circle_gray);
			stepBottomText1
					.setTextColor(getResources().getColor(R.color.gray2));
			stepTopText2.setBackgroundResource(R.drawable.style_circle_gray);
			stepBottomText2
					.setTextColor(getResources().getColor(R.color.gray2));
			stepTopText3.setBackgroundResource(R.drawable.style_circle_gray);
			stepBottomText3
					.setTextColor(getResources().getColor(R.color.gray2));
			stepTopText4.setBackgroundResource(R.drawable.style_circle_blue);
			stepBottomText4
					.setTextColor(getResources().getColor(R.color.blue1));
			phoneLayout.setVisibility(View.GONE);
			checkPwdLayout.setVisibility(View.GONE);
			resetPwdLayout.setVisibility(View.GONE);
			finishLayout.setVisibility(View.VISIBLE);
		}
	}

	private void countDownView(long time) {
		time /= intervalTime;
		StringBuffer sb = new StringBuffer();
		sb.append(time).append("秒后重发");
		sendSMSCodeBtn.setText(sb.toString());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.forget_pwd_phonenum_btn:
			inputPhone();
			break;
		case R.id.forget_pwd_check_btn:
			checkAuthCode();
			break;
		case R.id.forget_pwd_reset_btn:
			resetPwd();
			break;
		case R.id.forget_pwd_finish_loginbtn:
			finish();
			break;
		case R.id.forgot_pwd_company_layout_btn:
			SettingsManager.setMainFirstpageFlag(this, true);
			finish();
			break;
		case R.id.forget_pwd_get_authnum_btn:
			// 获取验证码
			sendSMScode();
			break;
		case R.id.forget_pwd_activity_nav_personal_btn:
			initPersonalLayout();
			break;
		case R.id.forget_pwd_activity_nav_company_btn:
			initCompanyLayout();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	/**
	 * 初始化个人用户的布局
	 */
	private void initPersonalLayout(){
		personalForgotLayout.setVisibility(View.VISIBLE);
		companyForgotLayout.setVisibility(View.GONE);
		navPersonalBtn.setEnabled(false);
		navPersonalBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
		navCompanyBtn.setEnabled(true);
		navCompanyBtn.setTextColor(getResources().getColor(R.color.gray));
	}
	
	/**
	 * 初始化企业用户的布局
	 */
	private void initCompanyLayout(){
		personalForgotLayout.setVisibility(View.GONE);
		companyForgotLayout.setVisibility(View.VISIBLE);
		navPersonalBtn.setEnabled(true);
		navPersonalBtn.setTextColor(getResources().getColor(R.color.gray));
		navCompanyBtn.setEnabled(false);
		navCompanyBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
	}
	
	/**
	 * 输入手机号码
	 */
	private void inputPhone() {
		phoneNum = inputPhoneET.getText().toString();
		if ("".equals(phoneNum) || !Util.checkPhoneNumber(phoneNum)) {
			Util.toastShort(ForgetPwdActivity.this, "手机号码不合法");
		} else {
			// 请求接口判断该手机号码存不存在
			checkRegister(phoneNum);
		}
	}

	private void sendSMScode() {
		String params[] = null;
		if (user != null) {
			params = SettingsManager.getSMSForgetPwdParams(user.getUser_name());// 拼接短信验证码格式
		} else {
			params = SettingsManager.getSMSForgetPwdParams("用户");// 拼接短信验证码格式
		}

		authnumSMSWeb = params[0];
		YLFLogger.d("忘记密码验证码：" + authnumSMSWeb);
		requestSMSAuthCode(phoneNum, SMSType.SMS_FORGET_PASSWORD, params[1],
				params[0]);
		sendSMSCodeBtn.setEnabled(false);
		long createTime = System.currentTimeMillis();
		countDownAsynTask = new CountDownAsyncTask(handler, "",
				System.currentTimeMillis(), createTime + 1000 * 60,
				intervalTime);
		SettingsManager.FULL_TASK_EXECUTOR.execute(countDownAsynTask);

	}

	private void checkAuthCode() {
		authnumSMSUser = smsAuthCodeET.getText().toString();
		if(authnumSMSUser == null || "".equals(authnumSMSUser)){
			Util.toastShort(ForgetPwdActivity.this, "请输入短信验证码");
			return;
		}
		if (authnumSMSWeb.equals(authnumSMSUser)) {
			updateStepLayoutBg(3);
		} else {
			Util.toastShort(ForgetPwdActivity.this, "短信验证码输入错误");
		}
	}

	private void resetPwd() {
		String newPwd = newPwdET.getText().toString();
		String repeatPwd = repeatNewPwdET.getText().toString();
		if (Util.checkPassword(newPwd)) {
			if (newPwd.equals(repeatPwd)) {
				if (user != null)
					updateUserInfo(user.getId(), newPwd, user.getPhone(),
							user.getEmail(), "", "", "", "");
			} else {
				Util.toastShort(ForgetPwdActivity.this, "密码前后两次输入不一致");
			}
		} else {
			Util.toastShort(ForgetPwdActivity.this, "密码格式不正确");
		}
	}

	/**
	 * 更新用户信息
	 * 
	 * @param id
	 * @param password
	 * @param phone
	 * @param email
	 * @param openId
	 */
	private void updateUserInfo(String id, String password, String phone,
			String email, String openId, String dealEnabled, String dealPwd,
			String tmpData) {
		if (mLoadingDialog != null) {
			mLoadingDialog.show();
		}
		AsyncUpdateUserInfo asyncUpdateUserInfo = new AsyncUpdateUserInfo(
				ForgetPwdActivity.this, id, password, phone, email, openId,
				dealEnabled, dealPwd, tmpData,"", new OnUpdateUserInfoInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler
										.obtainMessage(REQUEST_UPDATE_USERINFO_SUCCESS);
								handler.sendMessage(msg);
							} else {
								Message msg = handler
										.obtainMessage(REQUEST_UPDATE_USERINFO_EXCEPTION);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							}
						} else {
							Message msg = handler
									.obtainMessage(REQUEST_UPDATE_USERINFO_FAILE);
							handler.sendMessage(msg);
						}
					}
				});
		asyncUpdateUserInfo
				.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 
	 * @param phone
	 */
	private void checkRegister(String phone) {
		if (mLoadingDialog != null) {
			mLoadingDialog.show();
		}
		AsyncGetUserInfoByPhone asyncGetUserByPhone = new AsyncGetUserInfoByPhone(
				ForgetPwdActivity.this, phone, new OnGetUserInfoByPhone() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}
						int resultCode = SettingsManager
								.getResultCode(baseInfo);
						UserInfo userInfo = null;
						if (resultCode == 0) {
							userInfo = baseInfo.getUserInfo();
							if (userInfo != null) {
								user = userInfo;
								updateStepLayoutBg(2);
							} else {
								Util.toastShort(ForgetPwdActivity.this,
										"手机号码不存在");
							}
						} else {
							Util.toastShort(ForgetPwdActivity.this, "手机号码不存在");
						}

					}
				});
		asyncGetUserByPhone
				.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 发送短信验证码
	 */
	private void requestSMSAuthCode(String phone, String template,
			String params, String verfiy) {
		AsyncSMSRegiste asyncSMSRegiste = new AsyncSMSRegiste(
				ForgetPwdActivity.this, phone, template, params, verfiy,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler
										.obtainMessage(REQUEST_SEND_SMS_SUCCESS);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							} else {
								Message msg = handler
										.obtainMessage(REQEUST_SEND_SMS_FAILE);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							}
						}
					}
				});
		asyncSMSRegiste.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

}
