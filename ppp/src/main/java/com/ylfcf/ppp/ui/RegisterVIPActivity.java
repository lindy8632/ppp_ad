package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncAddPhoneInfo;
import com.ylfcf.ppp.async.AsyncCheckRegister;
import com.ylfcf.ppp.async.AsyncGetLCSName;
import com.ylfcf.ppp.async.AsyncLogin;
import com.ylfcf.ppp.async.AsyncRegiste;
import com.ylfcf.ppp.async.AsyncSMSRegiste;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.SMSType;
import com.ylfcf.ppp.entity.TaskDate;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnLoginInter;
import com.ylfcf.ppp.inter.Inter.OnRegisteInter;
import com.ylfcf.ppp.util.CountDownAsyncTask;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * VIP注册
 * @author Mr.liu
 *
 */
public class RegisterVIPActivity extends BaseActivity implements OnClickListener{
	private EditText phoneET, pwdET, authnumSMSET;
	private EditText managerNum;//理财经理号
	private TextView managerName;//理财师的名字
	private Button registeBtn, getAuthnumBtn;
	private LinearLayout recNumLayout;//推荐码
	private LinearLayout managerNumLayout;//理财经理号
	private TextView loginText;//立即登录
	private TextView protocolText;
	private String authnumSMSUser;// 用户输入的手机验证码
	private String authnumSMSWeb;// 系统生成的手机验证码

	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private CountDownAsyncTask countDownAsynTask = null;
	private final long intervalTime = 1000L;
	private String phoneNum = null;

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
				getAuthnumBtn.setText("获取验证码");
				getAuthnumBtn.setEnabled(true);
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
		setContentView(R.layout.registe_main);
		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("VIP用户注册");

//		phoneET = (EditText) findViewById(R.id.registe_main_phone);
		pwdET = (EditText) findViewById(R.id.registe_main_normal_personal_pwd);
		authnumSMSET = (EditText) findViewById(R.id.registe_main_normal_personal_sms_authnum);
//		managerNum = (EditText) findViewById(R.id.registe_main_manager_num);
		managerName = (TextView) findViewById(R.id.registe_main_normal_personal_manager_name);
		recNumLayout = (LinearLayout) findViewById(R.id.registe_main_normal_personal_recnum_layout);
		recNumLayout.setVisibility(View.GONE);
//		managerNumLayout = (LinearLayout) findViewById(R.id.registe_main_manager_layout);
		managerNumLayout.setVisibility(View.VISIBLE);
		loginText = (TextView) findViewById(R.id.registe_main_normal_personal_login_text);
		loginText.setOnClickListener(this);
		registeBtn = (Button) findViewById(R.id.registe_main_normal_personal_btn);
		registeBtn.setOnClickListener(this);
		registeBtn.setEnabled(false);
		getAuthnumBtn = (Button) findViewById(R.id.registe_main_normal_personal_authnum_btn);
		getAuthnumBtn.setOnClickListener(this);
		protocolText = (TextView) findViewById(R.id.registe_main_normal_personal_protocol);
		protocolText.setOnClickListener(this);
//		managerNum.setOnFocusChangeListener(new OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if(!hasFocus){
//					String num = managerNum.getText().toString();
//					getLCSName(num);
//				}
//			}
//		});
		managerNum.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				YLFLogger.d("onTextChanged");
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				YLFLogger.d("beforeTextChanged");
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				final String num = managerNum.getText().toString();
				if(Util.checkPhoneNumber(num)){
					//2秒钟没输入表示用户已输完，开始请求后台
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							getLCSName(num);
						}
					}, 1000L);
				}
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
		case R.id.registe_main_normal_personal_login_text:
			finish();
			break;
		case R.id.registe_main_normal_personal_btn:
			checkUserData();
			break;
		case R.id.registe_main_normal_personal_authnum_btn:
			// 先判断手机是否已经被注册
			checkAuthNumData();
			break;
		case R.id.registe_main_normal_personal_protocol:
			Intent intent = new Intent(RegisterVIPActivity.this,
					RegisteAgreementActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 注册
	 */
	private void checkUserData() {
		phoneNum = phoneET.getText().toString();
		String pwd = pwdET.getText().toString();
		String managerCode = managerNum.getText().toString();
		authnumSMSUser = authnumSMSET.getText().toString();
		if (Util.checkPhoneNumber(phoneNum)) {
			if (Util.checkPassword(pwd)) {
				if (authnumSMSUser.equals(authnumSMSWeb)) {
					// 请求注册接口
					if(!managerCode.isEmpty()){
						requestRegiste(phoneNum, pwd, "",managerCode);
					}else{
						Util.toastShort(RegisterVIPActivity.this, "请输入理财经理号");	
					}
				} else {
					Util.toastShort(RegisterVIPActivity.this, "手机验证码输入错误");
				}
			} else {
				Util.toastShort(RegisterVIPActivity.this, "密码格式错误");
			}
		} else {
			Util.toastShort(RegisterVIPActivity.this, "手机号码格式错误");
		}
	}

	/**
	 * 获取手机验证码
	 * 
	 */
	private void checkAuthNumData() {

		phoneNum = phoneET.getText().toString();
		String pwd = pwdET.getText().toString();
		if (Util.checkPhoneNumber(phoneNum)) {
			if (Util.checkPassword(pwd)) {
				isPhoneRegisted(phoneNum);
			} else {
				Util.toastShort(RegisterVIPActivity.this, "密码长度不能小于6位");
			}
		} else {
			Util.toastShort(RegisterVIPActivity.this, "手机号码格式错误");
		}
	}

	private void requestGetSMSAuthNum() {
		// 请求短信验证码接口
		String Params[] = SettingsManager.getSMSRegisteParams();// 拼接短信验证码格式
		authnumSMSWeb = Params[0];
		requestSMSAuthCode(phoneNum, SMSType.SMS_REGISTER, Params[1],
				Params[0], "");
		YLFLogger.d("短信验证码：" + Params[0]);
		getAuthnumBtn.setEnabled(false);
		long createTime = System.currentTimeMillis();
		countDownAsynTask = new CountDownAsyncTask(handler, "",
				System.currentTimeMillis(), createTime + 1000 * 60,
				intervalTime);
		SettingsManager.FULL_TASK_EXECUTOR.execute(countDownAsynTask);
	}

	private void countDownView(long time) {
		time /= intervalTime;
		StringBuffer sb = new StringBuffer();
		sb.append(time).append("秒后重发");
		getAuthnumBtn.setText(sb.toString());
	}

	/**
	 * 注册
	 * 
	 * @param phone
	 * @param password
	 */
	private void requestRegiste(final String phone, final String password,
			String extension_code,String salesPhone) {
		if(mLoadingDialog != null)
		mLoadingDialog.show();
		String open_id = "";
		String user_from_host = "";

		AsyncRegiste registeTask = new AsyncRegiste(RegisterVIPActivity.this,
				phone, password, open_id, SettingsManager.USER_FROM,
				SettingsManager.getUserFromSub(getApplicationContext()), user_from_host, extension_code,"vip",salesPhone,
				new OnRegisteInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						int resultCode = SettingsManager
								.getResultCode(baseInfo);
						if (resultCode == 0) {
							UserInfo userInfo = baseInfo.getUserInfo();
							// 发送注册成功的短信
							if (userInfo != null) {
								String Params[] = SettingsManager
										.getSMSRegisteSuccessParams(userInfo
												.getUser_name());
								requestSMSAuthCode(phoneNum,
										SMSType.SMS_REGISTER_SUCCESS,
										Params[1], "", "register_success");
								requestLogin(phone, password);
							}
						} else {
							if (mLoadingDialog != null
									&& mLoadingDialog.isShowing()) {
								mLoadingDialog.dismiss();
							}
							Util.toastShort(RegisterVIPActivity.this,
									baseInfo.getMsg());
						}
					}
				});
		registeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 登录
	 * @param phone
	 * @param pwd
	 */
	private void requestLogin(final String phone,final String pwd){
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		
		AsyncLogin loginTask = new AsyncLogin(RegisterVIPActivity.this, phone, pwd, new OnLoginInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				if(baseInfo == null){
					return;
				}
				int resultCode = SettingsManager.getResultCode(baseInfo);
				if(resultCode == 0){
					UserInfo userInfo = baseInfo.getUserInfo();
					if(userInfo != null){
						SettingsManager.setUser(RegisterVIPActivity.this,phone);
						SettingsManager.setLoginPassword(RegisterVIPActivity.this,pwd,true);
						SettingsManager.setUserId(RegisterVIPActivity.this, userInfo.getId());
						SettingsManager.setUserName(RegisterVIPActivity.this, userInfo.getUser_name());
						SettingsManager.setUserRegTime(RegisterVIPActivity.this, userInfo.getReg_time());
						addPhoneInfo(userInfo.getId(), phone, "", "");
					}
					Intent intent = new Intent(RegisterVIPActivity.this,RegisterSucActivity.class);
					startActivity(intent);
					finish();
				}else{
				}
			}
		});
		loginTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 将手机信息加入到后台数据库
	 * @param userId
	 * @param phone
	 * @param location
	 * @param contact
	 */
	private void addPhoneInfo(String userId,String phone,String location,String contact){
		String phoneModel = android.os.Build.MODEL;
		String sdkVersion = android.os.Build.VERSION.SDK;
		String systemVersion = android.os.Build.VERSION.RELEASE;
		AsyncAddPhoneInfo addPhoneInfoTask = new AsyncAddPhoneInfo(RegisterVIPActivity.this, userId, phone, phoneModel, 
				sdkVersion, systemVersion, "android", location, contact, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						
					}
				});
		addPhoneInfoTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 发送短信验证码
	 */
	private void requestSMSAuthCode(String phone, String template,
			String params, String verfiy, final String flag) {
		AsyncSMSRegiste asyncSMSRegiste = new AsyncSMSRegiste(
				RegisterVIPActivity.this, phone, template, params, verfiy,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0
									&& "register_success".equals(flag)) {
							}
						}
					}
				});
		asyncSMSRegiste.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 判断手机号码是否已经被注册
	 * 
	 * @param phone
	 */
	private void isPhoneRegisted(String phone) {
		AsyncCheckRegister checkRegisterTask = new AsyncCheckRegister(
				RegisterVIPActivity.this, phone, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								String msg = baseInfo.getMsg();
								if ("1".equals(msg)) {
									// 表示已经被注册
									Util.toastLong(RegisterVIPActivity.this,
											"该手机号码已经被注册");
								} else {
									requestGetSMSAuthNum();
								}
							}
						}
					}
				});
		checkRegisterTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 获取理财师的名字
	 * @param phone
	 */
	private void getLCSName(String phone){
		AsyncGetLCSName lcsTask = new AsyncGetLCSName(RegisterVIPActivity.this, phone, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//获取成功
								registeBtn.setEnabled(true);
								managerName.setVisibility(View.VISIBLE);
								managerName.setText("理财经理姓名："+baseInfo.getMsg());
								managerNum.setEnabled(false);
							}else{
								//获取失败
								Util.toastLong(RegisterVIPActivity.this, baseInfo.getMsg());
							}
						}
					}
				});
		lcsTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
