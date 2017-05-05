package com.ylfcf.ppp.ui;

import android.content.Intent;
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
import com.ylfcf.ppp.async.AsyncAddPhoneInfo;
import com.ylfcf.ppp.async.AsyncCompLogin;
import com.ylfcf.ppp.async.AsyncLogin;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.inter.Inter.OnLoginInter;
import com.ylfcf.ppp.util.Constants.UserType;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.widget.LoadingDialog;
/**
 * 登录
 * @author Mr.liu
 *
 */
public class LoginActivity extends BaseActivity implements OnClickListener{
	private static final int REQUEST_PERSONAL_LOGIN_SUCCESS_WHAT = 1001;
	private static final int REQUEST_PERSONAL_LOGIN_EXCEPTION_WHAT = 1002;
	private static final int REQUEST_COMPANY_LOGIN_SUCCESS_WHAT = 1003;
	private static final int REQUEST_COMPANY_LOGIN_EXCEPTION_WHAT = 1004;
	
    private LinearLayout topLeftBtn;
    private TextView topTitleTV;
    
    private Button navPersonalBtn,navCompanyBtn;
    private View personalLoginLayout,companyLoginLayout;
    
    //个人登录
    private TextView registerPersonalTV,forgetPwdPersonalTV;
    private Button loginPersonalBtn;
    private EditText phonePersonalET,pwdPersonalET;
    private String phonePersonal = "";
    private String pwdPersonal = "";
    
    //企业登录
    private TextView registerCompanyTV,forgetPwdCompanyTV;
    private Button loginCompanyBtn;
    private EditText phoneCompanyET,pwdCompanyET;
    private String phoneCompany = "";
    private String pwdCompany = "";
    
    private String fromFlag = null;
    private LoadingDialog loadingDialog;
    
    private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_PERSONAL_LOGIN_SUCCESS_WHAT:
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				if(baseInfo != null){
					UserInfo userInfo = baseInfo.getUserInfo();
					if(userInfo != null){
						SettingsManager.setUser(LoginActivity.this,phonePersonal);
						SettingsManager.setLoginPassword(LoginActivity.this,pwdPersonal,true);
						SettingsManager.setUserId(LoginActivity.this, userInfo.getId());
						SettingsManager.setUserName(LoginActivity.this, userInfo.getUser_name());
						SettingsManager.setUserRegTime(LoginActivity.this, userInfo.getReg_time());
						if("vip".equals(userInfo.getType())){
							SettingsManager.setUserType(LoginActivity.this, UserType.USER_VIP_PERSONAL);
						}else{
							SettingsManager.setUserType(LoginActivity.this, UserType.USER_NORMAL_PERSONAL);
						}
						addPhoneInfo(userInfo.getId(), phonePersonal, "", "");
					}
				}
				
				if(fromFlag != null && "from_gesture_verify_activity".equals(fromFlag)){
					Intent intent = new Intent(LoginActivity.this,MainFragmentActivity.class);
					startActivity(intent);
				}
				if(fromFlag != null && "from_mainfragment_activity_shared".equals(fromFlag)){
					//先判断是否实名是否绑卡
					checkIsVerify("邀请有奖");
				}
				if(fromFlag != null && "DZPTempActivity".equals(fromFlag)){
					//从大转盘页面过来
					Intent intentDZP = new Intent();
					setResult(121, intentDZP);
				}
				if(fromFlag != null && "srzx".equals(fromFlag)){
					Intent intentSRZX = new Intent();
					setResult(1220, intentSRZX);
				}
				finish();
				break;
			case REQUEST_PERSONAL_LOGIN_EXCEPTION_WHAT:
				Util.toastShort(LoginActivity.this, "用户名或密码错误");
				break;
			case REQUEST_COMPANY_LOGIN_SUCCESS_WHAT:
				BaseInfo baseInfoComp = (BaseInfo) msg.obj;
				if(baseInfoComp != null){
					UserInfo userInfo = baseInfoComp.getUserInfo();
					if(userInfo != null){
						SettingsManager.setUser(LoginActivity.this,phoneCompany);
						SettingsManager.setLoginPassword(LoginActivity.this,pwdCompany,true);
						SettingsManager.setUserId(LoginActivity.this, userInfo.getId());
						SettingsManager.setUserName(LoginActivity.this, userInfo.getUser_name());
						SettingsManager.setUserRegTime(LoginActivity.this, userInfo.getReg_time());
						SettingsManager.setUserType(LoginActivity.this, UserType.USER_COMPANY);
						SettingsManager.setCompPhone(LoginActivity.this, userInfo.getCo_mobile());
						addPhoneInfo(userInfo.getId(), phonePersonal, "", "");
					}
				}
				
				if(fromFlag != null && "from_gesture_verify_activity".equals(fromFlag)){
					Intent intent = new Intent(LoginActivity.this,MainFragmentActivity.class);
					startActivity(intent);
				}
				if(fromFlag != null && "from_mainfragment_activity_shared".equals(fromFlag)){
					//先判断是否实名是否绑卡
//					checkIsVerify("邀请有奖");
//					企业用户目前没有推广系统，故不再跳转邀请好友的页面。
				}
				if(fromFlag != null && "DZPTempActivity".equals(fromFlag)){
					//从大转盘页面过来
					Intent intentDZP = new Intent();
					setResult(121, intentDZP);
				}
				if(fromFlag != null && "srzx".equals(fromFlag)){
					Intent intentSRZX = new Intent();
					setResult(1221, intentSRZX);
				}
				finish();
				break;
			default:
				break;
			}
		}
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_activity);
		fromFlag = getIntent().getStringExtra("FLAG");
		loadingDialog = new LoadingDialog(LoginActivity.this, "正在登录...", R.anim.loading);
		findViews();
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		if("from_gesture_verify_activity".equals(fromFlag)){
			topLeftBtn.setVisibility(View.GONE);
		}
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("登录");
		
		personalLoginLayout = findViewById(R.id.login_personal_login_layout);
		companyLoginLayout = findViewById(R.id.login_company_login_layout);
		navPersonalBtn = (Button) findViewById(R.id.login_activity_nav_personal_btn);
		navPersonalBtn.setOnClickListener(this);
		navCompanyBtn = (Button) findViewById(R.id.login_activity_nav_company_btn);
		navCompanyBtn.setOnClickListener(this);
		
		phonePersonalET = (EditText)findViewById(R.id.login_personal_phone_et);
		pwdPersonalET = (EditText)findViewById(R.id.login_personal_pwd_et);
		loginPersonalBtn = (Button)findViewById(R.id.login_personal_loginbtn);
		loginPersonalBtn.setOnClickListener(this);
		registerPersonalTV = (TextView)findViewById(R.id.login_personal_register_tv);
		registerPersonalTV.setOnClickListener(this);
		forgetPwdPersonalTV = (TextView)findViewById(R.id.login_personal_forget_pwd_tv);
		forgetPwdPersonalTV.setOnClickListener(this);
		
		phoneCompanyET = (EditText)findViewById(R.id.login_company_phone_et);
		pwdCompanyET = (EditText)findViewById(R.id.login_company_pwd_et);
		loginCompanyBtn = (Button)findViewById(R.id.login_company_loginbtn);
		loginCompanyBtn.setOnClickListener(this);
		registerCompanyTV = (TextView)findViewById(R.id.login_company_register_tv);
		registerCompanyTV.setOnClickListener(this);
		forgetPwdCompanyTV = (TextView)findViewById(R.id.login_company_forget_pwd_tv);
		forgetPwdCompanyTV.setOnClickListener(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	/**
	 * 验证用户是否已经认证
	 * @param type “充值”,“提现”,"邀请有奖"
	 */
	private void checkIsVerify(final String type){
		RequestApis.requestIsVerify(LoginActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//邀请好友只需判断有没有实名即可。
//					checkIsBindCard(type);
					Intent intent = new Intent(LoginActivity.this,InvitateActivity.class);
					intent.putExtra("is_verify", true);
					startActivity(intent);
				}else{
					//用户没有实名
					Intent intent = new Intent(LoginActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
				}
				finish();
			}

			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}
	
	/**
	 * 判断用户是否已经绑卡
	 * @param type "充值提现"
	 */
	private void checkIsBindCard(final String type){
		RequestApis.requestIsBinding(LoginActivity.this, SettingsManager.getUserId(getApplicationContext()), "宝付", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//用户已经绑卡
					if("邀请有奖".equals(type)){
						intent.setClass(LoginActivity.this, InvitateActivity.class);
					}
				}else{
					//用户还没有绑卡
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					intent.setClass(LoginActivity.this, BindCardActivity.class);
				}
				startActivity(intent);
			}
		});
	}
	
	/**
	 * 置顶栏左边返回按钮具体动作
	 */
	private final View.OnClickListener backListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent selfIntent	= getIntent();
			finish();
			if ("Notification".equals(selfIntent.getStringExtra("ParentActivity"))){
				// TODO:如果是从系统通知跳转的，则需要新启动主菜单页面。
                //Intent intent = new Intent();
                //intent.setClass(Application.getInstance().getContext(),
                //        MenuActivity.class);
                //startActivity(intent);
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.login_personal_loginbtn:
			checkPersonalUserData();
			break;
		case R.id.login_company_loginbtn:
			checkCompanyUserData();
			break;
		case R.id.login_personal_register_tv:
			Intent intentPer = new Intent(LoginActivity.this,RegisteActivity.class);
			intentPer.putExtra("from_where", UserType.USER_NORMAL_PERSONAL);
			intentPer.putExtra("FLAG", fromFlag);
			startActivity(intentPer);
			break;
		case R.id.login_company_register_tv:
			Intent intentComp = new Intent(LoginActivity.this,RegisteActivity.class);
			intentComp.putExtra("from_where", UserType.USER_COMPANY);
			intentComp.putExtra("FLAG", fromFlag);
			startActivity(intentComp);
			break;
		case R.id.login_personal_forget_pwd_tv:
			Intent intentFP = new Intent(LoginActivity.this,ForgetPwdActivity.class);
			intentFP.putExtra("from_where", UserType.USER_NORMAL_PERSONAL);
			startActivity(intentFP);
			break;
		case R.id.login_company_forget_pwd_tv:
			Intent intentFC = new Intent(LoginActivity.this,ForgetPwdActivity.class);
			intentFC.putExtra("from_where", UserType.USER_COMPANY);
			startActivity(intentFC);
			break;
		case R.id.login_activity_nav_personal_btn:
			//个人登录
			initPersonalLayout();
			break;
		case R.id.login_activity_nav_company_btn:
			//企业登录
			initCompanyLayout();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 初始化个人登录的布局
	 */
	private void initPersonalLayout(){
		personalLoginLayout.setVisibility(View.VISIBLE);
		companyLoginLayout.setVisibility(View.GONE);
		navPersonalBtn.setEnabled(false);
		navPersonalBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
		navCompanyBtn.setEnabled(true);
		navCompanyBtn.setTextColor(getResources().getColor(R.color.gray));
	}
	
	/**
	 * 初始化企业登录的布局
	 */
	private void initCompanyLayout(){
		personalLoginLayout.setVisibility(View.GONE);
		companyLoginLayout.setVisibility(View.VISIBLE);
		navPersonalBtn.setEnabled(true);
		navPersonalBtn.setTextColor(getResources().getColor(R.color.gray));
		navCompanyBtn.setEnabled(false);
		navCompanyBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
	}
	
	//个人用户登录
	private void checkPersonalUserData(){
		phonePersonal = phonePersonalET.getText().toString().trim();
		pwdPersonal = pwdPersonalET.getText().toString().trim();
        if(Util.checkPhoneNumber(phonePersonal)) {
        	
        	if(Util.checkPassword(pwdPersonal)) {
        		requestLogin(phonePersonal,pwdPersonal);
        	} else {
        		Util.toastLong(LoginActivity.this, "请输入登录密码");
        	}
        	
        } else {
        	Util.toastLong(LoginActivity.this, "请输入手机号码");
        }
	}
	
	//企业用户登录
	private void checkCompanyUserData(){
		phoneCompany = phoneCompanyET.getText().toString().trim();
		pwdCompany = pwdCompanyET.getText().toString().trim();
        if(!"".equals(phoneCompany)) {
        	if(Util.checkPassword(pwdCompany)) {
        		requestCompLogin(phoneCompany,pwdCompany);
        	} else {
        		Util.toastLong(LoginActivity.this, "请输入登录密码");
        	}
        } else {
        	Util.toastLong(LoginActivity.this, "请输入用户名");
        }
	}

	/**
	 * 注册接口
	 * @param phone
	 * @param pwd
	 */
	private void requestLogin(String phone,String pwd){
		if(loadingDialog != null){
			loadingDialog.show();
		}
		
		AsyncLogin loginTask = new AsyncLogin(LoginActivity.this, phone, pwd, new OnLoginInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(loadingDialog != null && loadingDialog.isShowing()){
					loadingDialog.dismiss();
				}
				int resultCode = SettingsManager.getResultCode(baseInfo);
				if(resultCode == 0){
					Message msg = handler.obtainMessage(REQUEST_PERSONAL_LOGIN_SUCCESS_WHAT);
					msg.obj = baseInfo;
					handler.sendMessage(msg);
				}else{
					Message msg = handler.obtainMessage(REQUEST_PERSONAL_LOGIN_EXCEPTION_WHAT);
					msg.obj = "用户名或密码错误";
					handler.sendMessage(msg);
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
		AsyncAddPhoneInfo addPhoneInfoTask = new AsyncAddPhoneInfo(LoginActivity.this, userId, phone, phoneModel, 
				sdkVersion, systemVersion, "android", location, contact, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						
					}
				});
		addPhoneInfoTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 企业用户登录
	 * @param username
	 * @param password
	 */
	private void requestCompLogin(String username,String password){
		if(loadingDialog != null){
			loadingDialog.show();
		}
		AsyncCompLogin loginTask = new AsyncCompLogin(LoginActivity.this, username, password, 
				new OnCommonInter(){
					@Override
					public void back(final BaseInfo baseInfo) {
						loadingDialog.dismiss();
						if(baseInfo == null){
							Message msg = handler.obtainMessage(REQUEST_PERSONAL_LOGIN_EXCEPTION_WHAT);
							msg.obj = "您的网络不给力";
							handler.sendMessage(msg);
							return;
						}
						int resultCode = SettingsManager.getResultCode(baseInfo);
						if(resultCode == 0){
//							UserInfo user = baseInfo.getUserInfo();
//							if(user != null){
//								GesturePwdEntity entity = DBGesturePwdManager.getInstance(LoginActivity.this.getApplicationContext()).getGesturePwdEntity(user.getId());
//								if(entity != null && !entity.getPwd().isEmpty()){
//									
//								}else{
//									Intent intent = new Intent(LoginActivity.this,GestureEditActivity.class);
//									startActivity(intent);
//								}
//							}
							
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									Message msg = handler.obtainMessage(REQUEST_COMPANY_LOGIN_SUCCESS_WHAT);
									msg.obj = baseInfo;
									handler.sendMessage(msg);
								}
							}, 200L);
						}else{
							Message msg = handler.obtainMessage(REQUEST_PERSONAL_LOGIN_EXCEPTION_WHAT);
							msg.obj = "用户名或密码错误";
							handler.sendMessage(msg);
						}
					}
		});
		loginTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
