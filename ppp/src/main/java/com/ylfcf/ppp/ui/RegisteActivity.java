package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
import com.ylfcf.ppp.async.AsyncCompApplyRegiste;
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
import com.ylfcf.ppp.util.Constants.UserType;
import com.ylfcf.ppp.util.CountDownAsyncTask;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.view.RegisteSucCompWindow;
import com.ylfcf.ppp.widget.LoadingDialog;

/**
 * 注册
 * 
 * @author Administrator
 * 
 */
public class RegisteActivity extends BaseActivity implements OnClickListener {
	//注册主页面
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private Button navNormalPersonalBtn,navVipPersonalBtn,navCompanyBtn;
	private View normalPersonalLayout,vipPersonalLayout,companyLayout;
	private final long intervalTime = 1000L;
	private LoadingDialog loadingDialog;
	private LinearLayout mainLayout;
	private String fromWhere;
	
	//普通个人用户
	private EditText phoneNPET, pwdNPET, authnumNPSMSET;
	private EditText recommendNPNum;// 推荐码
	private Button registeNPBtn, getNPAuthnumBtn;
	private TextView loginNPText;//立即登录
	private TextView protocolNPText;
	private String authnumNPSMSUser;// 用户输入的手机验证码
	private String authnumNPSMSWeb;// 系统生成的手机验证码
	private String phoneNPNum = null;
	private String pwdNP = null;
	private String extension_code_np = "";
	private CountDownAsyncTask countNPDownAsynTask = null;
	private Handler handlerNP = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case CountDownAsyncTask.PROGRESS_UPDATE:
				TaskDate date = (TaskDate) msg.obj;
				long time = date.getTime();
				countNPDownView(time);
				break;
			case CountDownAsyncTask.FINISH:
				getNPAuthnumBtn.setText("获取验证码");
				getNPAuthnumBtn.setEnabled(true);
				break;
			default:
				break;
			}
		}
	};
	
	//VIP个人用户
	private EditText phoneVIPET, pwdVIPET, authnumVIPSMSET;
	private EditText managerVIPNum;//理财经理号
	private TextView managerVIPName;//理财师的名字
	private Button registeVIPBtn, getVIPAuthnumBtn;
	private TextView loginVIPText;//立即登录
	private TextView protocolVIPText;
	private String authnumVIPSMSUser;// 用户输入的手机验证码
	private String authnumVIPSMSWeb;// 系统生成的手机验证码
	private CountDownAsyncTask countVIPDownAsynTask = null;
	private String phoneVIPNum = null;
	private String activityFlag = "";
	private Handler handlerVIP = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case CountDownAsyncTask.PROGRESS_UPDATE:
				TaskDate date = (TaskDate) msg.obj;
				long time = date.getTime();
				countVIPDownView(time);
				break;
			case CountDownAsyncTask.FINISH:
				getVIPAuthnumBtn.setText("获取验证码");
				getVIPAuthnumBtn.setEnabled(true);
				break;
			default:
				break;
			}
		}
	};

	//企业用户
	private EditText phoneCompanyET, authnumCompanySMSET,recommendCompanyNum;
	private Button registeCompanyBtn, getCompanyAuthnumBtn;
	private TextView loginCompanyText;//立即登录
	private TextView managerNameComp;//理财经理姓名
	private TextView protocolCompanyText;//注册服务协议
	private String authnumCompanySMSUser;// 用户输入的手机验证码
	private String authnumCompanySMSWeb;// 系统生成的手机验证码
	private CountDownAsyncTask countCompanyDownAsynTask = null;
	private String phoneCompanyNum = null;
	private Handler handlerCompany = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case CountDownAsyncTask.PROGRESS_UPDATE:
				TaskDate date = (TaskDate) msg.obj;
				long time = date.getTime();
				countCompanyDownView(time);
				break;
			case CountDownAsyncTask.FINISH:
				getCompanyAuthnumBtn.setText("获取验证码");
				getCompanyAuthnumBtn.setEnabled(true);
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
		loadingDialog = new LoadingDialog(RegisteActivity.this, "正在加载...",R.anim.loading);
		fromWhere = getIntent().getStringExtra("from_where");
		activityFlag = getIntent().getStringExtra("FLAG");
		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("注册");
		
		mainLayout = (LinearLayout) findViewById(R.id.registe_main_layout);
		//主注册页面
		navNormalPersonalBtn = (Button) findViewById(R.id.registe_main_nav_normal_personal_btn);
		navNormalPersonalBtn.setOnClickListener(this);
		navVipPersonalBtn = (Button) findViewById(R.id.registe_main_nav_vip_personal_btn);
		navVipPersonalBtn.setOnClickListener(this);
		navCompanyBtn = (Button) findViewById(R.id.registe_main_nav_company_btn);
		navCompanyBtn.setOnClickListener(this);
		normalPersonalLayout = findViewById(R.id.registe_main_normal_personal_mainlayout);
		vipPersonalLayout = findViewById(R.id.registe_main_vip_personal_mainlayout);
		companyLayout = findViewById(R.id.registe_main_company_personal_mainlayout);

		//普通个人用户
		phoneNPET = (EditText) findViewById(R.id.registe_main_normal_personal_phone);
		pwdNPET = (EditText) findViewById(R.id.registe_main_normal_personal_pwd);
		authnumNPSMSET = (EditText) findViewById(R.id.registe_main_normal_personal_sms_authnum);
		recommendNPNum = (EditText) findViewById(R.id.registe_main_normal_personal_recommend_num);
		loginNPText = (TextView) findViewById(R.id.registe_main_normal_personal_login_text);
		loginNPText.setOnClickListener(this);
		registeNPBtn = (Button) findViewById(R.id.registe_main_normal_personal_btn);
		registeNPBtn.setOnClickListener(this);
		registeNPBtn.setEnabled(false);
		getNPAuthnumBtn = (Button) findViewById(R.id.registe_main_normal_personal_authnum_btn);
		getNPAuthnumBtn.setOnClickListener(this);
		protocolNPText = (TextView) findViewById(R.id.registe_main_normal_personal_protocol);
		protocolNPText.setOnClickListener(this);
		
		//VIP个人用户
		phoneVIPET = (EditText) findViewById(R.id.registe_main_vip_personal_phone);
		pwdVIPET = (EditText) findViewById(R.id.registe_main_vip_personal_pwd);
		authnumVIPSMSET = (EditText) findViewById(R.id.registe_main_vip_personal_sms_authnum);
		managerVIPNum = (EditText) findViewById(R.id.registe_main_vip_personal_manager_num);
		managerVIPName = (TextView) findViewById(R.id.registe_main_vip_personal_manager_name);
		loginVIPText = (TextView) findViewById(R.id.registe_main_vip_personal_login_text);
		loginVIPText.setOnClickListener(this);
		registeVIPBtn = (Button) findViewById(R.id.registe_main_vip_personal_btn);
		registeVIPBtn.setOnClickListener(this);
		registeVIPBtn.setEnabled(false);
		getVIPAuthnumBtn = (Button) findViewById(R.id.registe_main_vip_personal_authnum_btn);
		getVIPAuthnumBtn.setOnClickListener(this);
		protocolVIPText = (TextView) findViewById(R.id.registe_main_vip_personal_protocol);
		protocolVIPText.setOnClickListener(this);
		managerVIPNum.addTextChangedListener(new TextWatcher() {
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
				final String num = managerVIPNum.getText().toString();
				if(Util.checkPhoneNumber(num)){
					//2秒钟没输入表示用户已输完，开始请求后台
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							getLCSName(num,"vip_personal");
						}
					}, 1000L);
				}
			}
		});
		
		//企业用户
		phoneCompanyET = (EditText) findViewById(R.id.registe_main_company_phone);
		authnumCompanySMSET = (EditText) findViewById(R.id.registe_main_company_sms_authnum);
		recommendCompanyNum = (EditText) findViewById(R.id.registe_main_company_recommend_num);
		registeCompanyBtn = (Button) findViewById(R.id.registe_main_company_btn);
		registeCompanyBtn.setOnClickListener(this);
		registeCompanyBtn.setEnabled(false);
		getCompanyAuthnumBtn = (Button) findViewById(R.id.registe_main_company_authnum_btn);
		getCompanyAuthnumBtn.setOnClickListener(this);
		loginCompanyText = (TextView) findViewById(R.id.registe_main_company_personal_login_text);
		loginCompanyText.setOnClickListener(this);
		protocolCompanyText = (TextView) findViewById(R.id.registe_main_company_personal_protocol);
		protocolCompanyText.setOnClickListener(this);
		managerNameComp = (TextView) findViewById(R.id.registe_main_company_rec_name);
		recommendCompanyNum.addTextChangedListener(new TextWatcher() {
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
				final String num = recommendCompanyNum.getText().toString();
				if(Util.checkPhoneNumber(num)){
					//2秒钟没输入表示用户已输完，开始请求后台
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							getLCSName(num,"vip_company");
						}
					}, 1000L);
				}
			}
		});
		
		if(UserType.USER_COMPANY.equals(fromWhere)){
			//企业用户
			initCompanyLayout();
		}else if(UserType.USER_NORMAL_PERSONAL.equals(fromWhere)){
			//普通个人用户
			initNormalPersonalLayout();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handlerNP.removeCallbacksAndMessages(null);
		handlerVIP.removeCallbacksAndMessages(null);
		handlerCompany.removeCallbacksAndMessages(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
		case R.id.registe_main_normal_personal_login_text:
		case R.id.registe_main_vip_personal_login_text:
		case R.id.registe_main_company_personal_login_text:
			finish();
			break;
		case R.id.registe_main_nav_normal_personal_btn:
			//普通个人用户导航
			initNormalPersonalLayout();
			break;
		case R.id.registe_main_nav_vip_personal_btn:
			//vip个人用户导航
			initVipPersonalLayout();
			break;
		case R.id.registe_main_nav_company_btn:
			//企业用户
			initCompanyLayout();
			break;
		case R.id.registe_main_normal_personal_btn:
			checkNPUserData();
			break;
		case R.id.registe_main_vip_personal_btn:
			checkVIPUserData();
			break;
		case R.id.registe_main_company_btn:
			checkCompanyUserData();
			break;
		case R.id.registe_main_normal_personal_authnum_btn:
			checkNPAuthNumData();
			break;
		case R.id.registe_main_vip_personal_authnum_btn:
			checkVIPAuthNumData();
			break;
		case R.id.registe_main_company_authnum_btn:
			checkCompanyAuthData();
			break;
		case R.id.registe_main_normal_personal_protocol:
		case R.id.registe_main_vip_personal_protocol:
		case R.id.registe_main_company_personal_protocol:
			Intent intent = new Intent(RegisteActivity.this,
					RegisteAgreementActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 普通个人用户注册页面
	 */
	private void initNormalPersonalLayout(){
		normalPersonalLayout.setVisibility(View.VISIBLE);
		vipPersonalLayout.setVisibility(View.GONE);
		companyLayout.setVisibility(View.GONE);
		
		navNormalPersonalBtn.setEnabled(false);
		navNormalPersonalBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
		navVipPersonalBtn.setEnabled(true);
		navVipPersonalBtn.setTextColor(getResources().getColor(R.color.gray));
		navCompanyBtn.setEnabled(true);
		navCompanyBtn.setTextColor(getResources().getColor(R.color.gray));
	}
	
	/**
	 * VIP个人用户注册页面
	 */
	private void initVipPersonalLayout(){
		normalPersonalLayout.setVisibility(View.GONE);
		vipPersonalLayout.setVisibility(View.VISIBLE);
		companyLayout.setVisibility(View.GONE);
		
		navNormalPersonalBtn.setEnabled(true);
		navNormalPersonalBtn.setTextColor(getResources().getColor(R.color.gray));
		navVipPersonalBtn.setEnabled(false);
		navVipPersonalBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
		navCompanyBtn.setEnabled(true);
		navCompanyBtn.setTextColor(getResources().getColor(R.color.gray));
	}
	
	/**
	 * 企业用户注册页面
	 */
	private void initCompanyLayout(){
		normalPersonalLayout.setVisibility(View.GONE);
		vipPersonalLayout.setVisibility(View.GONE);
		companyLayout.setVisibility(View.VISIBLE);
		
		navNormalPersonalBtn.setEnabled(true);
		navNormalPersonalBtn.setTextColor(getResources().getColor(R.color.gray));
		navVipPersonalBtn.setEnabled(true);
		navVipPersonalBtn.setTextColor(getResources().getColor(R.color.gray));
		navCompanyBtn.setEnabled(false);
		navCompanyBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
	}
	
	/**
	 * 普通个人用户注册
	 */
	private void checkNPUserData() {
		phoneNPNum = phoneNPET.getText().toString();
		pwdNP = pwdNPET.getText().toString();
		extension_code_np = recommendNPNum.getText().toString();
		authnumNPSMSUser = authnumNPSMSET.getText().toString();
		if (Util.checkPhoneNumber(phoneNPNum)) {
			if (Util.checkPassword(pwdNP)) {
				if (authnumNPSMSUser.equals(authnumNPSMSWeb)) {
					// 请求注册接口
					requestNPRegiste(phoneNPNum, pwdNP, extension_code_np);
				} else {
					Util.toastShort(RegisteActivity.this, "手机验证码输入错误");
				}
			} else {
				Util.toastShort(RegisteActivity.this, "密码格式错误");
			}
		} else {
			Util.toastShort(RegisteActivity.this, "手机号码格式错误");
		}
	}

	/**
	 * VIP个人用户注册
	 */
	private void checkVIPUserData(){
		phoneVIPNum = phoneVIPET.getText().toString();
		String pwd = pwdVIPET.getText().toString();
		String managerCode = managerVIPNum.getText().toString();
		authnumVIPSMSUser = authnumVIPSMSET.getText().toString();
		if (Util.checkPhoneNumber(phoneVIPNum)) {
			if (Util.checkPassword(pwd)) {
				if (authnumVIPSMSUser.equals(authnumVIPSMSWeb)) {
					// 请求注册接口
					if(!managerCode.isEmpty()){
						requestVIPRegiste(phoneVIPNum, pwd, "",managerCode);
					}else{
						Util.toastShort(RegisteActivity.this, "请输入理财经理号");	
					}
				} else {
					Util.toastShort(RegisteActivity.this, "手机验证码输入错误");
				}
			} else {
				Util.toastShort(RegisteActivity.this, "密码格式错误");
			}
		} else {
			Util.toastShort(RegisteActivity.this, "手机号码格式错误");
		}
	}
	
	/**
	 * 企业用户注册
	 */
	private void checkCompanyUserData(){
		phoneCompanyNum = phoneCompanyET.getText().toString();
		String extension_code_company = recommendCompanyNum.getText().toString();
		authnumCompanySMSUser = authnumCompanySMSET.getText().toString();
		if (Util.checkPhoneNumber(phoneCompanyNum)) {
				if (authnumCompanySMSUser.equals(authnumCompanySMSWeb)) {
					// 请求申请注册接口
					requestCompApplyRegiste(phoneCompanyNum,SettingsManager.USER_FROM,extension_code_company);
				} else {
					Util.toastShort(RegisteActivity.this, "手机验证码输入错误");
				}
		} else {
			Util.toastShort(RegisteActivity.this, "手机号码格式错误");
		}
	}
	
	/**
	 * 获取手机验证码 普通个人用户
	 * 
	 */
	private void checkNPAuthNumData() {
		phoneNPNum = phoneNPET.getText().toString();
		String pwd = pwdNPET.getText().toString();
		if (Util.checkPhoneNumber(phoneNPNum)) {
			if (Util.checkPassword(pwd)) {
				isNPPhoneRegisted(phoneNPNum);
			} else {
				Util.toastShort(RegisteActivity.this, "密码长度不能小于6位");
			}
		} else {
			Util.toastShort(RegisteActivity.this, "手机号码格式错误");
		}
	}

	/**
	 * 获取手机验证码 VIP个人用户
	 */
	private void checkVIPAuthNumData(){
		phoneVIPNum = phoneVIPET.getText().toString();
		String pwd = pwdVIPET.getText().toString();
		if (Util.checkPhoneNumber(phoneVIPNum)) {
			if (Util.checkPassword(pwd)) {
				isVIPPhoneRegisted(phoneVIPNum);
			} else {
				Util.toastShort(RegisteActivity.this, "密码长度不能小于6位");
			}
		} else {
			Util.toastShort(RegisteActivity.this, "手机号码格式错误");
		}
	}
	
	/**
	 * 企业用户
	 */
	private void checkCompanyAuthData(){
		phoneCompanyNum = phoneCompanyET.getText().toString();
		if (Util.checkPhoneNumber(phoneCompanyNum)) {
			requestCompanyGetSMSAuthNum();
		} else {
			Util.toastShort(RegisteActivity.this, "手机号码格式错误");
		}
	}
	
	/**
	 * 普通个人用户获取短信验证码
	 */
	private void requestNPGetSMSAuthNum() {
		// 请求短信验证码接口
		String Params[] = SettingsManager.getSMSRegisteParams();// 拼接短信验证码格式
		authnumNPSMSWeb = Params[0];
		requestSMSAuthCode(phoneNPNum, SMSType.SMS_REGISTER, Params[1],
				Params[0], "get_authcode_np");
		YLFLogger.d("短信验证码：" + Params[0]);
		getNPAuthnumBtn.setEnabled(false);
		long createTime = System.currentTimeMillis();
		countNPDownAsynTask = new CountDownAsyncTask(handlerNP, "",
				System.currentTimeMillis(), createTime + 1000 * 60,
				intervalTime);
		SettingsManager.FULL_TASK_EXECUTOR.execute(countNPDownAsynTask);
	}

	/**
	 * VIP个人用户获取短信验证码
	 */
	private void requestVIPGetSMSAuthNum() {
		// 请求短信验证码接口
		String Params[] = SettingsManager.getSMSRegisteParams();// 拼接短信验证码格式
		authnumVIPSMSWeb = Params[0];
		requestSMSAuthCode(phoneVIPNum, SMSType.SMS_REGISTER, Params[1],
				Params[0], "get_authcode_vip");
		YLFLogger.d("短信验证码：" + Params[0]);
		getVIPAuthnumBtn.setEnabled(false);
		long createTime = System.currentTimeMillis();
		countVIPDownAsynTask = new CountDownAsyncTask(handlerVIP, "",
				System.currentTimeMillis(), createTime + 1000 * 60,
				intervalTime);
		SettingsManager.FULL_TASK_EXECUTOR.execute(countVIPDownAsynTask);
	}
	
	/**
	 * 企业用户获取短信验证码
	 */
	private void requestCompanyGetSMSAuthNum() {
		// 请求短信验证码接口
		String Params[] = SettingsManager.getSMSRegisteParams();// 拼接短信验证码格式
		authnumCompanySMSWeb = Params[0];
		requestSMSAuthCode(phoneCompanyNum, SMSType.SMS_REGISTER, Params[1],
				Params[0], "get_authcode_comp");
		YLFLogger.d("短信验证码：" + Params[0]);
		getCompanyAuthnumBtn.setEnabled(false);
		long createTime = System.currentTimeMillis();
		countCompanyDownAsynTask = new CountDownAsyncTask(handlerCompany, "",
				System.currentTimeMillis(), createTime + 1000 * 60,
				intervalTime);
		SettingsManager.FULL_TASK_EXECUTOR.execute(countCompanyDownAsynTask);
	}
	
	/**
	 * 短信验证码倒计时 普通用户注册
	 * @param time
	 */
	private void countNPDownView(long time) {
		time /= intervalTime;
		StringBuffer sb = new StringBuffer();
		sb.append(time).append("秒后重发");
		getNPAuthnumBtn.setText(sb.toString());
	}
	
	/**
	 * 短信验证码倒计时 VIP用户
	 * @param time
	 */
	private void countVIPDownView(long time) {
		time /= intervalTime;
		StringBuffer sb = new StringBuffer();
		sb.append(time).append("秒后重发");
		getVIPAuthnumBtn.setText(sb.toString());
	}

	/**
	 * 企业用户
	 * @param time
	 */
	private void countCompanyDownView(long time){
		time /= intervalTime;
		StringBuffer sb = new StringBuffer();
		sb.append(time).append("秒后重发");
		getCompanyAuthnumBtn.setText(sb.toString());
	}
	
	/**
	 * 企业用户注册成功
	 */
	private void showCompanyRegisteSuc(String phone){
		View popView = LayoutInflater.from(this).inflate(
				R.layout.registe_suc_comp_layout, null);
		int[] screen = SettingsManager.getScreenDispaly(RegisteActivity.this);
		int width = screen[0] * 4 / 5;
		int height = screen[1] * 3 / 8;
		RegisteSucCompWindow popwindow = new RegisteSucCompWindow(RegisteActivity.this,
				popView, width, height,phone,new OnRegisteCompSucClickListener() {
					@Override
					public void onClick() {
						finish();
					}
				});
		popwindow.show(mainLayout);
	}
	
	/**
	 * 普通个人用户注册
	 * 
	 * @param phone
	 * @param password
	 */
	private void requestNPRegiste(String phone, String password,
			String extension_code) {
		loadingDialog.show();
		String open_id = "";
		AsyncRegiste registeTask = new AsyncRegiste(RegisteActivity.this,
				phone, password, open_id, SettingsManager.USER_FROM,activityFlag,
				SettingsManager.getUserFromSub(getApplicationContext()), extension_code,"外部投资","",
				new OnRegisteInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								UserInfo userInfo = baseInfo.getUserInfo();
								// 发送注册成功的短信
								if (userInfo != null) {
									String Params[] = SettingsManager
											.getSMSRegisteSuccessParams(userInfo
													.getUser_name());
									requestSMSAuthCode(phoneNPNum,
											SMSType.SMS_REGISTER_SUCCESS,
											Params[1], "", "register_success");
									requestNPLogin(phoneNPNum, pwdNP);
								}
							} else {
								if (loadingDialog != null
										&& loadingDialog.isShowing()) {
									loadingDialog.dismiss();
								}
								Util.toastShort(RegisteActivity.this,
										baseInfo.getMsg());
							}
						}else{
						}
					}
				});
		registeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * VIP个人用户注册
	 * 
	 * @param phone
	 * @param password
	 */
	private void requestVIPRegiste(final String phone, final String password,
			String extension_code,String salesPhone) {
		loadingDialog.show();
		String open_id = "";

		AsyncRegiste registeTask = new AsyncRegiste(RegisteActivity.this,
				phone, password, open_id, SettingsManager.USER_FROM,
				SettingsManager.getUserFromSub(getApplicationContext()), activityFlag, extension_code,"vip",salesPhone,
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
								requestSMSAuthCode(phoneVIPNum,
										SMSType.SMS_REGISTER_SUCCESS,
										Params[1], "", "register_success");
								requestVIPLogin(phone, password);
							}
						} else {
							if (loadingDialog != null
									&& loadingDialog.isShowing()) {
								loadingDialog.dismiss();
							}
							Util.toastShort(RegisteActivity.this,
									baseInfo.getMsg());
						}
					}
				});
		registeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 发送短信验证码
	 */
	private void requestSMSAuthCode(String phone, String template,
			String params, String verfiy, final String flag) {
		AsyncSMSRegiste asyncSMSRegiste = new AsyncSMSRegiste(
				RegisteActivity.this, phone, template, params, verfiy,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (loadingDialog != null && loadingDialog.isShowing()) {
							loadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								if("register_success".equals(flag)){
									//注册成功
								}else if("get_authcode_np".equals(flag)){
									//普通用户获取验证码
									registeNPBtn.setEnabled(true);
								}else if("get_authcode_vip".equals(flag)){
									//vip用户获取验证码
									registeVIPBtn.setEnabled(true);
								}else if("get_authcode_comp".equals(flag)){
									//企业用户获取验证码
									registeCompanyBtn.setEnabled(true);
								}
							}
						}
					}
				});
		asyncSMSRegiste.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 普通个人用户登录
	 * @param phone
	 * @param pwd
	 */
	private void requestNPLogin(final String phone,final String pwd){
		if(loadingDialog != null){
			loadingDialog.show();
		}
		
		AsyncLogin loginTask = new AsyncLogin(RegisteActivity.this, phone, pwd, new OnLoginInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(loadingDialog != null && loadingDialog.isShowing()){
					loadingDialog.dismiss();
				}
				if(baseInfo == null){
					return;
				}
				int resultCode = SettingsManager.getResultCode(baseInfo);
				if(resultCode == 0){
					UserInfo userInfo = baseInfo.getUserInfo();
					if(userInfo != null){
						SettingsManager.setUser(RegisteActivity.this,phone);
						SettingsManager.setLoginPassword(RegisteActivity.this,pwd,true);
						SettingsManager.setUserId(RegisteActivity.this, userInfo.getId());
						SettingsManager.setUserName(RegisteActivity.this, userInfo.getUser_name());
						SettingsManager.setUserRegTime(RegisteActivity.this, userInfo.getReg_time());
						addPhoneInfo(userInfo.getId(), phone, "", "");
					}
					Intent intent = new Intent(RegisteActivity.this,RegisterSucActivity.class);
					intent.putExtra("extension_code", extension_code_np);
					startActivity(intent);
					finish();
				}else{
				}
			}
		});
		loginTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * VIP个人用户登录
	 * @param phone
	 * @param pwd
	 */
	private void requestVIPLogin(final String phone,final String pwd){
		if(loadingDialog != null){
			loadingDialog.show();
		}
		
		AsyncLogin loginTask = new AsyncLogin(RegisteActivity.this, phone, pwd, new OnLoginInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(loadingDialog != null && loadingDialog.isShowing()){
					loadingDialog.dismiss();
				}
				if(baseInfo == null){
					return;
				}
				int resultCode = SettingsManager.getResultCode(baseInfo);
				if(resultCode == 0){
					UserInfo userInfo = baseInfo.getUserInfo();
					if(userInfo != null){
						SettingsManager.setUser(RegisteActivity.this,phone);
						SettingsManager.setLoginPassword(RegisteActivity.this,pwd,true);
						SettingsManager.setUserId(RegisteActivity.this, userInfo.getId());
						SettingsManager.setUserName(RegisteActivity.this, userInfo.getUser_name());
						SettingsManager.setUserRegTime(RegisteActivity.this, userInfo.getReg_time());
						addPhoneInfo(userInfo.getId(), phone, "", "");
					}
					Intent intent = new Intent(RegisteActivity.this,RegisterSucActivity.class);
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
		AsyncAddPhoneInfo addPhoneInfoTask = new AsyncAddPhoneInfo(RegisteActivity.this, userId, phone, phoneModel, 
				sdkVersion, systemVersion, "android", location, contact, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						
					}
				});
		addPhoneInfoTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 判断手机号码是否已经被注册
	 * 
	 * @param phone
	 */
	private void isNPPhoneRegisted(String phone) {
		AsyncCheckRegister checkRegisterTask = new AsyncCheckRegister(
				RegisteActivity.this, phone, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								String msg = baseInfo.getMsg();
								if ("1".equals(msg)) {
									// 表示已经被注册
									Util.toastLong(RegisteActivity.this,
											"该手机号码已经被注册");
								} else {
									requestNPGetSMSAuthNum();
								}
							}
						}
					}
				});
		checkRegisterTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 判断手机号码是否已经被注册  VIP个人用户
	 * 
	 * @param phone
	 */
	private void isVIPPhoneRegisted(String phone) {
		AsyncCheckRegister checkRegisterTask = new AsyncCheckRegister(
				RegisteActivity.this, phone, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								String msg = baseInfo.getMsg();
								if ("1".equals(msg)) {
									// 表示已经被注册
									Util.toastLong(RegisteActivity.this,
											"该手机号码已经被注册");
								} else {
									requestVIPGetSMSAuthNum();
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
	private void getLCSName(String phone,final String type){
		AsyncGetLCSName lcsTask = new AsyncGetLCSName(RegisteActivity.this, phone, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//获取成功
								if("vip_personal".equals(type)){
									registeVIPBtn.setEnabled(true);
									managerVIPName.setVisibility(View.VISIBLE);
									managerVIPName.setText("理财经理姓名："+baseInfo.getMsg());
									managerVIPNum.setEnabled(false);
								}else if("vip_company".equals(type)){
									registeCompanyBtn.setEnabled(true);
									managerNameComp.setVisibility(View.VISIBLE);
									managerNameComp.setText("理财经理姓名："+baseInfo.getMsg());
									recommendCompanyNum.setEnabled(false);
								}
								
							}else{
								//获取失败
								Util.toastLong(RegisteActivity.this, baseInfo.getMsg());
							}
						}
					}
				});
		lcsTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 企业用户申请注册
	 * @param phone
	 * @param userFrom
	 * @param extensionCode
	 */
	private void requestCompApplyRegiste(final String phone,String userFrom,String extensionCode){
		AsyncCompApplyRegiste task = new AsyncCompApplyRegiste(RegisteActivity.this, phone, userFrom, extensionCode, 
				new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								showCompanyRegisteSuc(phone);
							}else{
								Util.toastLong(RegisteActivity.this, baseInfo.getMsg());
							}
						}
					}
		});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 企业用户注册成功
	 * @author Mr.liu
	 *
	 */
	public interface OnRegisteCompSucClickListener{
		void onClick();
	}
}
