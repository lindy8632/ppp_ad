package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncAddPhoneInfo;
import com.ylfcf.ppp.async.AsyncCompLogin;
import com.ylfcf.ppp.async.AsyncGetLCSName;
import com.ylfcf.ppp.async.AsyncHuiFuRMBAccount;
import com.ylfcf.ppp.async.AsyncJXQLogList;
import com.ylfcf.ppp.async.AsyncLogin;
import com.ylfcf.ppp.async.AsyncPrizeList;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.async.AsyncUserYUANAccount;
import com.ylfcf.ppp.async.AsyncXCFLActiveTime;
import com.ylfcf.ppp.async.AsyncYJBInterest;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.db.DBGesturePwdManager;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.GesturePwdEntity;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.entity.UserYUANAccountInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.inter.Inter.OnLoginInter;
import com.ylfcf.ppp.inter.Inter.OnUserYUANAccountInter;
import com.ylfcf.ppp.ptr.PtrClassicFrameLayout;
import com.ylfcf.ppp.ptr.PtrDefaultHandler;
import com.ylfcf.ppp.ptr.PtrFrameLayout;
import com.ylfcf.ppp.ptr.PtrHandler;
import com.ylfcf.ppp.ui.AccountSettingActivity;
import com.ylfcf.ppp.ui.AccountSettingCompActivity;
import com.ylfcf.ppp.ui.AwardDetailsActivity;
import com.ylfcf.ppp.ui.BannerTopicActivity;
import com.ylfcf.ppp.ui.BindCardActivity;
import com.ylfcf.ppp.ui.BorrowListYJYActivity;
import com.ylfcf.ppp.ui.ForgetPwdActivity;
import com.ylfcf.ppp.ui.FundsDetailsActivity;
import com.ylfcf.ppp.ui.GestureEditActivity;
import com.ylfcf.ppp.ui.InvitateActivity;
import com.ylfcf.ppp.ui.LXFXTempActivity;
import com.ylfcf.ppp.ui.MainFragmentActivity;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnUserFragmentLoginSucListener;
import com.ylfcf.ppp.ui.ModifyLoginPwdActivity;
import com.ylfcf.ppp.ui.PrizeRegion2TempActivity;
import com.ylfcf.ppp.ui.RechargeActivity;
import com.ylfcf.ppp.ui.RechargeCompActivity;
import com.ylfcf.ppp.ui.RegisteActivity;
import com.ylfcf.ppp.ui.UserInvestRecordActivity;
import com.ylfcf.ppp.ui.UserVerifyActivity;
import com.ylfcf.ppp.ui.WithdrawActivity;
import com.ylfcf.ppp.ui.WithdrawCompActivity;
import com.ylfcf.ppp.ui.WithdrawPwdGetbackActivity;
import com.ylfcf.ppp.util.Constants;
import com.ylfcf.ppp.util.Constants.UserType;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;

import java.text.DecimalFormat;

import static com.ylfcf.ppp.R.id.my_account_personal_zscp_invest_btn;

/**
 * 我的
 * @author Administrator
 *	充值余额是易联和汇付相加
 *	冻结总额和待收总额只请求易联接口即可
 */
public class UserFragment extends BaseFragment implements OnClickListener{
	private static final String className = "UserFragment";
	private MainFragmentActivity mainActivity;

	private static final int REQUEST_PERSONAL_LOGIN_SUCCESS_WHAT = 1001;
	private static final int REQUEST_PERSONAL_LOGIN_EXCEPTION_WHAT = 1002;

	private static final int REQUEST_COMPANY_LOGIN_SUCCESS_WHAT = 1005;
	private static final int REQUEST_COMPANY_LOGIN_EXCEPTION = 1006;

	private static final int REQUEST_GET_USERINFO_WHAT = 1003;
	private static final int REQUEST_GET_USERINFO_SUCCESS = 1004;

	private static final int REQUEST_ISLCS_WHAT = 1007;//改用户是否是理财师
	private static final int REQUEST_ISLCS_SUC = 1008;

	private View rootView;
	private View topLayout;
	private TextView topTitle;
	private LinearLayout topbarLeftLayout;

	/**
	 * 登录界面
	 */
	private LinearLayout loginLayout;
	private Button navPersonalBtn,navCompanyBtn;
	private View personalLoginLayout,companyLoginLayout;

	/**
	 * 个人登录
	 */
	private TextView registerPersonalTV,forgetPwdPersonalTV;
	private Button loginPersonalBtn;
	private EditText phonePersonalET,pwdPersonalET;
	private String phonePersonal = "";
	private String pwdPersonal = "";
	private String userId = "";
	private String hfuserId = "";

	/**
	 * 企业登录
	 */
	private TextView registerCompanyTV,forgetPwdCompanyTV;
	private Button loginCompanyBtn;
	private EditText phoneCompanyET,pwdCompanyET;
	private String usernameCompany = "";
	private String pwdCompany = "";
	/**
	 * 我的账户
	 */
	//个人用户顶部
	private LinearLayout personalTopLayout;//个人顶部布局
	private TextView usernameTVNP;//个人用户的用户名
	private TextView zhyeTotalTVNP;//账户总余额

	//企业用户顶部
	private LinearLayout companyTopLayout;//企业顶部布局
	private TextView usernameTVComp;//企业用户的用户名
	private TextView companyNameTV;//企业名
	private TextView zhyeTotalTVComp;//企业用户的账户总额

	private TextView zhyeYiLianTV;//账户余额
	private TextView djjeTV;//冻结金额
	private TextView dsjeTV;//待收金额
	private Button withdrawBtn;
	private Button rechargeBtn;
	private TextView usedYJBTV;//元金币可用金额
	private LinearLayout jlmxLayout;//奖励明细
	private LinearLayout zjmxLayout;//资金明细
	private LinearLayout tbjlLayout;//投标记录
	private LinearLayout yqyjLayout;//邀请有奖
	private LinearLayout zhszLayout;//账户设置
	private View line1,line2,line3,line4,line6;
	private View compMainLayout;
	private LinearLayout accouncenterLayout;

	//元聚盈模块
	private LinearLayout yjyLayout;//元聚盈模块
	private Button yjyInvestBtn,yjyAppointBtn;//元聚盈投资以及预约按钮

	private PtrClassicFrameLayout mainRefreshLayout = null;//下拉刷新的布局

	private UserRMBAccountInfo yilianAccountInfo;//易联账户信息
	private UserRMBAccountInfo huifuAccountInfo;//汇付账户信息
	private BaseInfo yjbInterestBaseInfo;//元金币产生的收益
	private UserInfo mUserInfo;

	private boolean isSetWithdrawPwd = false;//用户是否已经设置交易密码
	private boolean isShowCompLoginPWDDialog = false;//是否已经弹出过修改企业用户初始登录密码的dialog
	private boolean isLcs = false;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case REQUEST_PERSONAL_LOGIN_SUCCESS_WHAT:
					BaseInfo baseInfo = (BaseInfo) msg.obj;
					UserInfo userInfo = null;
					if(baseInfo != null){
						userInfo = baseInfo.getUserInfo();
						if(userInfo != null){
							SettingsManager.setUser(mainActivity,phonePersonal);
							SettingsManager.setLoginPassword(mainActivity,pwdPersonal,true);
							SettingsManager.setUserId(mainActivity, userInfo.getId());
							SettingsManager.setUserName(mainActivity, userInfo.getUser_name());
							SettingsManager.setUserRegTime(mainActivity, userInfo.getReg_time());
							if("vip".equals(userInfo.getType())){
								SettingsManager.setUserType(mainActivity, UserType.USER_VIP_PERSONAL);
							}else{
								SettingsManager.setUserType(mainActivity, UserType.USER_NORMAL_PERSONAL);
							}
							requestUserInfo(userInfo.getId(), userInfo.getPhone());
							requestYuanMoney(userInfo.getId());
							addPhoneInfo(userInfo.getId(), phonePersonal, "", "");
							onLoginSuc.onLoginSuc();
						}
					}
					topTitle.setText("我的账户");
					mainRefreshLayout.setVisibility(View.VISIBLE);
					personalTopLayout.setVisibility(View.VISIBLE);
					companyTopLayout.setVisibility(View.GONE);
					loginLayout.setVisibility(View.GONE);
					line2.setVisibility(View.VISIBLE);
					line4.setVisibility(View.VISIBLE);
					if(SettingsManager.checkLXFXActivity() == 0 && SettingsManager.isPersonalUser(mainActivity) &&
							(SettingsManager.getLXFXJXQFlag(mainActivity,SettingsManager.getUserId(getActivity())+"lxfx") ||
									SettingsManager.getLXFXJXQFlag(mainActivity,SettingsManager.getUserId(getActivity())+"lxfx_noverify"))){
						checkIsGetJXQ(userInfo,SettingsManager.getUserId(getActivity()), "开门红 乐享返现","未使用","0","2","");
					}
					//判断会员福利2期是否还在进行
					requestActiveTime("HYFL_02");
					break;
				case REQUEST_COMPANY_LOGIN_SUCCESS_WHAT:
					BaseInfo baseInfoComp = (BaseInfo) msg.obj;
					if(baseInfoComp != null){
						mUserInfo = baseInfoComp.getUserInfo();
						if(mUserInfo != null){
							topTitle.setText("我的账户");
							if(mUserInfo.getUser_name() != null && !"".equals(mUserInfo.getUser_name())){
								usernameTVComp.setText("您好！" + mUserInfo.getUser_name());
							}else{
								usernameTVComp.setText("您好！尊敬的客户！" );
							}
							companyNameTV.setText(mUserInfo.getReal_name());
							hfuserId = mUserInfo.getHf_user_id();
							if(hfuserId != null && !"".equals(hfuserId)){
								requestYilianAccount(userId,true);
							}else{
								requestYilianAccount(userId,false);
							}

							SettingsManager.setUser(mainActivity,usernameCompany);
							SettingsManager.setLoginPassword(mainActivity,pwdCompany,true);
							SettingsManager.setUserId(mainActivity, mUserInfo.getId());
							SettingsManager.setUserName(mainActivity, mUserInfo.getUser_name());
							SettingsManager.setUserRegTime(mainActivity, mUserInfo.getReg_time());
							SettingsManager.setUserType(mainActivity, UserType.USER_COMPANY);
							SettingsManager.setCompPhone(mainActivity, mUserInfo.getCo_mobile());
//						requestUserInfo(userInfo.getId(), userInfo.getCo_phone());
							requestYuanMoney(mUserInfo.getId());
							addPhoneInfo(mUserInfo.getId(), mUserInfo.getCo_phone(), "", "");
							requestYilianAccount(mUserInfo.getId(), false);
							onLoginSuc.onLoginSuc();
							if("0".equals(mUserInfo.getInit_pwd())){
								//企业用户没有修改初始登录密码
								showCompLoginPwdDialog();
							}else{
								//已经修改过初始登录密码
							}
						}
					}
					topTitle.setText("我的账户");
					mainRefreshLayout.setVisibility(View.VISIBLE);
					personalTopLayout.setVisibility(View.GONE);
					companyTopLayout.setVisibility(View.VISIBLE);
					loginLayout.setVisibility(View.GONE);
					jlmxLayout.setVisibility(View.GONE);
					yqyjLayout.setVisibility(View.VISIBLE);
					line2.setVisibility(View.GONE);
					line4.setVisibility(View.VISIBLE);
					break;
				case REQUEST_PERSONAL_LOGIN_EXCEPTION_WHAT:
					String loginMsg = (String) msg.obj;
					Util.toastShort(mainActivity, loginMsg);
					break;
				case REQUEST_GET_USERINFO_WHAT:
					requestUserInfo(SettingsManager.getUserId(mainActivity), phonePersonal);
					break;
				case REQUEST_GET_USERINFO_SUCCESS:
					mUserInfo = (UserInfo) msg.obj;
					topTitle.setText("我的账户");
					mainRefreshLayout.setVisibility(View.VISIBLE);
					loginLayout.setVisibility(View.GONE);
					if(SettingsManager.isPersonalUser(mainActivity)){
						personalTopLayout.setVisibility(View.VISIBLE);
						companyTopLayout.setVisibility(View.GONE);
						jlmxLayout.setVisibility(View.VISIBLE);
						yqyjLayout.setVisibility(View.VISIBLE);
						line2.setVisibility(View.VISIBLE);
						line4.setVisibility(View.VISIBLE);
						if(mUserInfo.getReal_name() != null && !"".equals(mUserInfo.getReal_name())){
							usernameTVNP.setText("您好！" + mUserInfo.getReal_name());
						}else{
							usernameTVNP.setText("您好！" + mUserInfo.getUser_name());
						}
					}else if(SettingsManager.isCompanyUser(mainActivity)){
						if("0".equals(mUserInfo.getInit_pwd())){
							//企业用户没有修改初始登录密码
							showCompLoginPwdDialog();
						}else{
							//已经修改过初始登录密码
						}
						personalTopLayout.setVisibility(View.GONE);
						companyTopLayout.setVisibility(View.VISIBLE);
						jlmxLayout.setVisibility(View.GONE);
						yqyjLayout.setVisibility(View.VISIBLE);
						line2.setVisibility(View.GONE);
						line4.setVisibility(View.VISIBLE);
						if(mUserInfo.getUser_name() != null && !"".equals(mUserInfo.getUser_name())){
							usernameTVComp.setText("您好！" + mUserInfo.getUser_name());
						}else{
							usernameTVComp.setText("您好！尊敬的客户！" );
						}
						companyNameTV.setText(mUserInfo.getReal_name());
					}

					hfuserId = mUserInfo.getHf_user_id();
					if(hfuserId != null && !"".equals(hfuserId)){
						requestYilianAccount(userId,true);
						return;
					}
					requestYilianAccount(userId,false);
					break;
				case REQUEST_ISLCS_WHAT:
					requestLcsName(SettingsManager.getUser(getActivity().getApplicationContext()));
					break;
				default:
					break;
			}
		}
	};

	/**
	 * 创建当前Fragment的实例对象
	 * @param position
	 * @return
	 */
	static OnUserFragmentLoginSucListener onLoginSuc;
	public static Fragment newInstance(int position,OnUserFragmentLoginSucListener onLoginSucListener) {
		UserFragment f = new UserFragment();
		onLoginSuc = onLoginSucListener;
		Bundle args = new Bundle();
		args.putInt("num", position);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		YLFLogger.d("UserFragment -- onActivityCreated");
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		YLFLogger.d("UserFragment -- onCreate");
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
							 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mainActivity = (MainFragmentActivity) getActivity();
		if(rootView==null){
			rootView=inflater.inflate(R.layout.user_fragment, null);
		}
		findViews(rootView);
//		//缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		YLFLogger.d("UserFragment -- onCreateView");
		return rootView;
	}

	private void findViews(View view){
		initRefreshLayout(view);
		loginLayout = (LinearLayout) view.findViewById(R.id.user_fragment_login_layout);
		topLayout = view.findViewById(R.id.user_fragment_toplayout);
		topbarLeftLayout = (LinearLayout) topLayout.findViewById(R.id.common_topbar_left_layout);
		topbarLeftLayout.setVisibility(View.GONE);
		topTitle = (TextView) topLayout.findViewById(R.id.common_page_title);

		navPersonalBtn = (Button) view.findViewById(R.id.user_fragment_nav_personal_btn);
		navPersonalBtn.setOnClickListener(this);
		navCompanyBtn = (Button) view.findViewById(R.id.user_fragment_nav_company_btn);
		navCompanyBtn.setOnClickListener(this);
		personalLoginLayout = view.findViewById(R.id.user_fragment_personal_login_layout);
		companyLoginLayout = view.findViewById(R.id.user_fragment_company_login_layout);

		//个人用户登录页面
		phonePersonalET = (EditText)view.findViewById(R.id.login_personal_phone_et);
		pwdPersonalET = (EditText)view.findViewById(R.id.login_personal_pwd_et);
		loginPersonalBtn = (Button)view.findViewById(R.id.login_personal_loginbtn);
		loginPersonalBtn.setOnClickListener(this);
		registerPersonalTV = (TextView)view.findViewById(R.id.login_personal_register_tv);
		registerPersonalTV.setOnClickListener(this);
		forgetPwdPersonalTV = (TextView)view.findViewById(R.id.login_personal_forget_pwd_tv);
		forgetPwdPersonalTV.setOnClickListener(this);

		//企业用户登录页面
		phoneCompanyET = (EditText)view.findViewById(R.id.login_company_phone_et);
		pwdCompanyET = (EditText)view.findViewById(R.id.login_company_pwd_et);
		loginCompanyBtn = (Button)view.findViewById(R.id.login_company_loginbtn);
		loginCompanyBtn.setOnClickListener(this);
		registerCompanyTV = (TextView)view.findViewById(R.id.login_company_register_tv);
		registerCompanyTV.setOnClickListener(this);
		forgetPwdCompanyTV = (TextView)view.findViewById(R.id.login_company_forget_pwd_tv);
		forgetPwdCompanyTV.setOnClickListener(this);

		//个人用户的账户中心
		personalTopLayout = (LinearLayout) view.findViewById(R.id.my_account_personal_toplayout);
		companyTopLayout = (LinearLayout) view.findViewById(R.id.my_account_company_toplayout);
		usernameTVNP = (TextView)view.findViewById(R.id.my_account_username);
		zhyeTotalTVNP = (TextView)view.findViewById(R.id.my_account_zhye_total_tv);
		zhyeYiLianTV = (TextView)view.findViewById(R.id.my_account_zhye_balance);
		djjeTV = (TextView)view.findViewById(R.id.my_account_djje_tv);
		dsjeTV = (TextView)view.findViewById(R.id.my_account_dsje_tv);
		withdrawBtn = (Button)view.findViewById(R.id.my_account_withdraw_btn);
		withdrawBtn.setOnClickListener(this);
		rechargeBtn = (Button)view.findViewById(R.id.my_account_recharge_btn);
		rechargeBtn.setOnClickListener(this);
		jlmxLayout = (LinearLayout)view.findViewById(R.id.my_account_jlmx_layout);
		jlmxLayout.setOnClickListener(this);
		usedYJBTV = (TextView)view.findViewById(R.id.my_account_used_yjb);
		zjmxLayout = (LinearLayout)view.findViewById(R.id.my_account_zjmx_layout);
		zjmxLayout.setOnClickListener(this);
		tbjlLayout = (LinearLayout)view.findViewById(R.id.my_account_tbjl_layout);
		tbjlLayout.setOnClickListener(this);
		yqyjLayout = (LinearLayout)view.findViewById(R.id.my_account_yqyj_layout);
		yqyjLayout.setOnClickListener(this);
		zhszLayout = (LinearLayout)view.findViewById(R.id.my_account_zhsz_layout);
		zhszLayout.setOnClickListener(this);
		compMainLayout = view.findViewById(R.id.user_fragment_company_layout);
		line1 = view.findViewById(R.id.my_account_line1);
		line2 = view.findViewById(R.id.my_account_line2);
		line3 = view.findViewById(R.id.my_account_line3);
		line4 = view.findViewById(R.id.my_account_line4);
		line6 = view.findViewById(R.id.my_account_line6);
		accouncenterLayout = (LinearLayout)view.findViewById(R.id.my_account_comp_layout_account_center);
		accouncenterLayout.setOnClickListener(this);

		//企业用户的账户中心
		usernameTVComp = (TextView) view.findViewById(R.id.company_account_username);
		companyNameTV = (TextView) view.findViewById(R.id.company_account_companyname);
		zhyeTotalTVComp = (TextView) view.findViewById(R.id.company_account_zhye_total_tv);

		//元聚盈
		yjyLayout = (LinearLayout) view.findViewById(R.id.my_account_personal_zscp_layout);
		yjyInvestBtn = (Button) view.findViewById(my_account_personal_zscp_invest_btn);
		yjyInvestBtn.setOnClickListener(this);
		yjyAppointBtn = (Button) view.findViewById(R.id.my_account_personal_zscp_yy_btn);
		yjyAppointBtn.setOnClickListener(this);

	}

	private void initMainLayout(){
		if(mainActivity == null){
			mainActivity = (MainFragmentActivity) getActivity();
		}
		if(mainActivity == null)
			return;
		userId = SettingsManager.getUserId(mainActivity.getApplicationContext());
		if(userId != null && !"".equals(userId)){
			mainRefreshLayout.setVisibility(View.VISIBLE);
			loginLayout.setVisibility(View.GONE);
			topTitle.setText("我的账户");
			if(SettingsManager.isPersonalUser(mainActivity)){
				personalTopLayout.setVisibility(View.VISIBLE);
				companyTopLayout.setVisibility(View.GONE);
				jlmxLayout.setVisibility(View.VISIBLE);
				yqyjLayout.setVisibility(View.VISIBLE);
				line2.setVisibility(View.VISIBLE);
				line4.setVisibility(View.VISIBLE);
			}else if(SettingsManager.isCompanyUser(mainActivity)){
				personalTopLayout.setVisibility(View.GONE);
				companyTopLayout.setVisibility(View.VISIBLE);
				jlmxLayout.setVisibility(View.GONE);
				yqyjLayout.setVisibility(View.VISIBLE);
				line2.setVisibility(View.GONE);
				line4.setVisibility(View.VISIBLE);
			}
			handler.sendEmptyMessage(REQUEST_ISLCS_WHAT);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					String phone = "";
					if(SettingsManager.isPersonalUser(mainActivity.getApplicationContext())){
						phone = SettingsManager.getUser(mainActivity.getApplicationContext());
					}else if(SettingsManager.isCompanyUser(mainActivity.getApplicationContext())){
						phone = "";
					}
					requestUserInfo(userId, phone);
				}
			}, 1000L);
			requestYuanMoney(userId);

		}else{
			loginLayout.setVisibility(View.VISIBLE);
			mainRefreshLayout.setVisibility(View.GONE);
			phonePersonalET.requestFocus();
			phonePersonalET.requestFocusFromTouch();
			pwdPersonalET.setText(null);
			topTitle.setText("登录");
			isShowCompLoginPWDDialog = false;
		}
	}

	/**
	 * 下拉刷新的布局
	 * @param v
	 */
	private void initRefreshLayout(View v){
		mainRefreshLayout = (PtrClassicFrameLayout) v.findViewById(R.id.user_fragment_refresh_layout);
		mainRefreshLayout.setLastUpdateTimeRelateObject(this);
		mainRefreshLayout.setPtrHandler(new PtrHandler() {
			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						requestUserInfo(userId, SettingsManager.getUser(getActivity().getApplicationContext()));
					}
				}, 1000L);
				requestYuanMoney(userId);
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
				return PtrDefaultHandler.checkContentCanBePulledDown(frame, compMainLayout, header);
			}
		});
		mainRefreshLayout.setResistance(1.7f);
		mainRefreshLayout.setRatioOfHeaderHeightToRefresh(1.2f);
		mainRefreshLayout.setDurationToClose(200);
		mainRefreshLayout.setDurationToCloseHeader(1000);
		// default is false
		mainRefreshLayout.setPullToRefresh(false);
		// default is true
		mainRefreshLayout.setKeepHeaderWhenRefresh(true);
	}

	@Override
	public void onStart() {
		super.onStart();
		YLFLogger.d("UserFragment -- onStart");
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		YLFLogger.d("UserFragment -- onHiddenChanged");
	}

	//相当于activity的onResume()
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		YLFLogger.d("UserFragment -- setUserVisibleHint ---" + isVisibleToUser);
		if(isVisibleToUser){
			initMainLayout();
		}
		mainActivity = (MainFragmentActivity) getActivity();
		if(isVisibleToUser && SettingsManager.checkLXFXActivity() == 0 && SettingsManager.isPersonalUser(mainActivity)){
			boolean isLogin = !SettingsManager.getLoginPassword(
					mainActivity).isEmpty()
					&& !SettingsManager.getUser(mainActivity)
					.isEmpty();
			if(isLogin && (SettingsManager.getLXFXJXQFlag(mainActivity,SettingsManager.getUserId(getActivity())+"lxfx") ||
					SettingsManager.getLXFXJXQFlag(mainActivity,SettingsManager.getUserId(getActivity())+"lxfx_noverify")))
				checkIsGetJXQ(mUserInfo,SettingsManager.getUserId(getActivity()), "开门红 乐享返现","未使用","0","2","");
		}

		if(isVisibleToUser && SettingsManager.isPersonalUser(mainActivity) &&
				SettingsManager.getLXFXJXQFlag(mainActivity,SettingsManager.getUserId(getActivity())+"hyfl02")){
			boolean isLogin = !SettingsManager.getLoginPassword(
					mainActivity).isEmpty()
					&& !SettingsManager.getUser(mainActivity)
					.isEmpty();
			if(isLogin){
				requestActiveTime("HYFL_02");
			}
		}
	}

	private boolean isInitedMain = false;
	@Override
	public void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);
		YLFLogger.d("UserFragment ------onResume()");
		if(isInitedMain){
			initMainLayout();
		}
	}

	@Override
	public void onPause() {
		isInitedMain = true;
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);
		YLFLogger.d("UserFragment -- onPause");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
		YLFLogger.d("UserFragment -- onDestroy");
	}

	/**
	 * 检查个人用户登录信息
	 */
	private void checkPersonalUserData(){
		//隐藏键盘
		Util.hiddenSoftInputWindow(mainActivity);
		phonePersonal = phonePersonalET.getText().toString().trim();
		pwdPersonal = pwdPersonalET.getText().toString().trim();
		if(Util.checkPhoneNumber(phonePersonal)) {
			if(Util.checkPassword(pwdPersonal)) {
				requestLogin(phonePersonal,pwdPersonal);
			}else{
				Util.toastShort(mainActivity, "请输入登录密码");
			}
		}else{
			Util.toastShort(mainActivity, "手机号码不合法");
		}
	}

	/**
	 * 检查企业用户登录信息
	 */
	private void checkCompanyUserData(){
		//隐藏键盘
		try{
			Util.hiddenSoftInputWindow(mainActivity);//当此界面没有焦点的时候会报出空指针的异常。
		}catch (Exception e){
			e.printStackTrace();
		}
		usernameCompany = phoneCompanyET.getText().toString().trim();
		pwdCompany = pwdCompanyET.getText().toString().trim();
		if (!usernameCompany.isEmpty()) {
			if (Util.checkPassword(pwdCompany)) {
				requestCompLogin(usernameCompany, pwdCompany);
			} else {
				Util.toastShort(mainActivity, "请输入登录密码");
			}
		} else {
			Util.toastShort(mainActivity, "请输入用户名");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.login_personal_loginbtn:
				checkPersonalUserData();
				break;
			case R.id.login_company_loginbtn:
				checkCompanyUserData();
				break;
			case R.id.login_personal_register_tv:
				Intent intentPer = new Intent(mainActivity,RegisteActivity.class);
				intentPer.putExtra("from_where", UserType.USER_NORMAL_PERSONAL);
				startActivity(intentPer);
				break;
			case R.id.login_company_register_tv:
				Intent intentComp = new Intent(mainActivity,RegisteActivity.class);
				intentComp.putExtra("from_where", UserType.USER_COMPANY);
				startActivity(intentComp);
				break;
			case R.id.login_personal_forget_pwd_tv:
				Intent intentFP = new Intent(mainActivity,ForgetPwdActivity.class);
				intentFP.putExtra("from_where", UserType.USER_NORMAL_PERSONAL);
				startActivity(intentFP);
				break;
			case R.id.login_company_forget_pwd_tv:
				Intent intentFC = new Intent(mainActivity,ForgetPwdActivity.class);
				intentFC.putExtra("from_where", UserType.USER_COMPANY);
				startActivity(intentFC);
				break;
			case R.id.my_account_withdraw_btn:
				//提现
				withdrawBtn.setEnabled(false);
				checkIsVerify("提现");
				break;
			case R.id.my_account_recharge_btn:
				//充值
				if(SettingsManager.isPersonalUser(mainActivity)){
					checkIsVerify("充值");
				}else if(SettingsManager.isCompanyUser(mainActivity)){
					Intent intentRechargeComp = new Intent(mainActivity,RechargeCompActivity.class);
					startActivity(intentRechargeComp);
				}

				break;
			case R.id.my_account_jlmx_layout:
				Intent intentAward = new Intent(mainActivity,AwardDetailsActivity.class);
				startActivity(intentAward);
				break;
			case R.id.my_account_zjmx_layout:
				//资金明细
				Intent intentFund = new Intent(mainActivity,FundsDetailsActivity.class);
				intentFund.putExtra("userinfo", mUserInfo);
				startActivity(intentFund);
				break;
			case R.id.my_account_tbjl_layout:
//			initAllRecLayout();
				Intent intentUserRecord = new Intent(mainActivity,UserInvestRecordActivity.class);
				startActivity(intentUserRecord);
				break;
			case R.id.my_account_yqyj_layout:
				//邀请有奖、
				yqyjLayout.setEnabled(false);
				checkIsVerify("邀请有奖");
				break;
			case R.id.my_account_zhsz_layout:
				//账户设置
				if(SettingsManager.isPersonalUser(mainActivity)){
					zhszLayout.setEnabled(false);
					checkIsVerify("账户设置");
				}else if(SettingsManager.isCompanyUser(mainActivity)){
					Intent intentZHSZComp = new Intent(mainActivity,AccountSettingCompActivity.class);
					startActivity(intentZHSZComp);
				}
				break;
			case R.id.user_fragment_nav_personal_btn:
				//个人登录的导航
				initPersonalLayout();
				break;
			case R.id.user_fragment_nav_company_btn:
				//企业登录导航
				initCompanyLayout();
				break;
			case R.id.my_account_personal_zscp_invest_btn:
				//元聚盈投资按钮
				Intent yjyInvestIntent = new Intent(mainActivity,BorrowListYJYActivity.class);
				startActivity(yjyInvestIntent);
				break;
			case R.id.my_account_personal_zscp_yy_btn:
				//元聚盈预约
				Intent intentYJYAppoint = new Intent(getActivity(),BannerTopicActivity.class);
				BannerInfo info = new BannerInfo();
				info.setArticle_id(Constants.TopicType.YJY_APPOINT);
				info.setLink_url(URLGenerator.YJY_TOPIC_URL);
				intentYJYAppoint.putExtra("BannerInfo",info);
				startActivity(intentYJYAppoint);
				break;
			case R.id.my_account_comp_layout_account_center:
//			Intent accCenterIntent = new Intent(mainActivity, AccountCenterActivity.class);
//			accCenterIntent.putExtra("ylUserRMBAccountInfo",yilianAccountInfo);
//			accCenterIntent.putExtra("hfUserRMBAccountInfo",huifuAccountInfo);
//			accCenterIntent.putExtra("yjbBaseInfo",yjbInterestBaseInfo);
//			startActivity(accCenterIntent);
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

	private void initAccountData(UserRMBAccountInfo yilianAccount,UserRMBAccountInfo huifuAccount,double yjbInterest){
		double yilianBalance = 0d;
		double huifuBalance = 0d;
		double totalBalance = 0d;
		double dsBalance = 0d;//待收
		double djBalance = 0d;//冻结
		double accuntTotal = 0d;
		DecimalFormat df = new DecimalFormat("#.00");
		try {
			dsBalance = Double.parseDouble(yilianAccount.getCollection_money()) + yjbInterest;
		} catch (Exception e) {
		}
		try {
			djBalance = Double.parseDouble(yilianAccount.getFrozen_money());
		} catch (Exception e) {
		}
		djjeTV.setText(yilianAccount.getFrozen_money());
		if(dsBalance < 1){
			dsjeTV.setText("0"+df.format(dsBalance));
		}else{
			dsjeTV.setText(df.format(dsBalance));
		}

		try {
			yilianBalance = Double.parseDouble(yilianAccount.getUse_money());
			if(huifuAccount != null){
				huifuBalance = Double.parseDouble(huifuAccount.getUse_money());
			}
			totalBalance = yilianBalance + huifuBalance;
			if(totalBalance < 1){
				zhyeYiLianTV.setText("0"+df.format(totalBalance));
			}else{
				zhyeYiLianTV.setText(df.format(totalBalance));
			}
			accuntTotal = totalBalance + dsBalance + djBalance;
			if(accuntTotal < 1){
				zhyeTotalTVNP.setText("0"+df.format(accuntTotal));
				zhyeTotalTVComp.setText("0"+df.format(accuntTotal));
			}else{
				zhyeTotalTVNP.setText(df.format(accuntTotal));
				zhyeTotalTVComp.setText(df.format(accuntTotal));
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 获取理财师的名字
	 * @param phone
	 */
	private void requestLcsName(String phone){
		AsyncGetLCSName lcsTask = new AsyncGetLCSName(mainActivity, phone, new Inter.OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						//是理财师
						isLcs = true;
						yjyLayout.setVisibility(View.VISIBLE);
					}else{
						//非理财师
						isLcs = false;
						yjyLayout.setVisibility(View.GONE);
					}
				}else{
					isLcs = false;
					yjyLayout.setVisibility(View.GONE);
				}
			}
		});
		lcsTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}


	/**
	 * 验证用户是否已经认证
	 * @param type “充值”,“提现”，“邀请有奖”
	 */
	private void checkIsVerify(final String type){
		if(mainActivity.loadingDialog != null){
			mainActivity.loadingDialog.show();
		}
		RequestApis.requestIsVerify(mainActivity, SettingsManager.getUserId(mainActivity), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(mainActivity.loadingDialog != null && mainActivity.loadingDialog.isShowing()){
					mainActivity.loadingDialog.dismiss();
				}
				if("邀请有奖".equals(type)){
					rechargeBtn.setEnabled(true);
					withdrawBtn.setEnabled(true);
					yqyjLayout.setEnabled(true);
					zhszLayout.setEnabled(true);
					Intent intent = new Intent();
					intent.setClass(mainActivity,InvitateActivity.class);
					intent.putExtra("is_verify", flag);
					startActivity(intent);
					return;
				}
				if(flag){
					//用户已经实名
					checkIsBindCard(type);
				}else{
					rechargeBtn.setEnabled(true);
					withdrawBtn.setEnabled(true);
					yqyjLayout.setEnabled(true);
					zhszLayout.setEnabled(true);
					if("账户设置".equals(type)){
						Intent intentAccountSetting = new Intent(mainActivity,AccountSettingActivity.class);
						intentAccountSetting.putExtra("is_binding", false);
						startActivity(intentAccountSetting);
						return;
					}
					//用户没有实名
					Intent intent = new Intent(mainActivity,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
				}
			}

			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
				//用户是否已经设置提现密码
				isSetWithdrawPwd = flag;
				if(SettingsManager.isCompanyUser(getActivity().getApplicationContext())&&"提现".equals(type)){
					rechargeBtn.setEnabled(true);
					withdrawBtn.setEnabled(true);
					yqyjLayout.setEnabled(true);
					zhszLayout.setEnabled(true);
					Intent intent = new Intent();
					//企业用户
					//要先判断用户是否已经设置提现密码（已经在判断用户是否实名的时候判断过，即字段isSetWithdrawPwd）
					if(isSetWithdrawPwd){
						//用户已经设置提现密码
						intent.setClass(mainActivity, WithdrawCompActivity.class);
					}else{
						intent.setClass(mainActivity, WithdrawPwdGetbackActivity.class);
						intent.putExtra("type", "设置");
					}
					startActivity(intent);
				}
			}
		});
	}

	/**
	 * 判断用户是否已经绑卡
	 * @param type "充值","提现","邀请有奖"
	 */
	private void checkIsBindCard(final String type){
		RequestApis.requestIsBinding(mainActivity, SettingsManager.getUserId(mainActivity), "宝付", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				rechargeBtn.setEnabled(true);
				withdrawBtn.setEnabled(true);
				yqyjLayout.setEnabled(true);
				zhszLayout.setEnabled(true);
				Intent intent = new Intent();
				if(flag){
					//用户已经绑卡
					if("充值".equals(type)){
						//那么直接跳到充值页面
						intent.setClass(mainActivity, RechargeActivity.class);
					}else if("提现".equals(type)){
						//要先判断用户是否已经设置提现密码（已经在判断用户是否实名的时候判断过，即字段isSetWithdrawPwd）
						if(isSetWithdrawPwd){
							//用户已经设置提现密码
							intent.setClass(mainActivity, WithdrawActivity.class);
						}else{
							intent.setClass(mainActivity, WithdrawPwdGetbackActivity.class);
							intent.putExtra("type", "设置");
						}
					}else if("邀请有奖".equals(type)){
						intent.setClass(mainActivity, InvitateActivity.class);
					}else if("账户设置".equals(type)){
						intent.setClass(mainActivity, AccountSettingActivity.class);
						intent.putExtra("is_binding", true);
					}
				}else{
					//用户还没有绑卡
					if("账户设置".equals(type)){
						intent.setClass(mainActivity, AccountSettingActivity.class);
						intent.putExtra("is_binding", false);
					}else{
						Bundle bundle = new Bundle();
						bundle.putString("type", type);
						intent.putExtra("bundle", bundle);
						intent.setClass(mainActivity, BindCardActivity.class);
					}
				}
				startActivity(intent);
			}
		});
	}

	/**
	 *
	 * @param phone
	 * @param pwd
	 */
	private void requestLogin(String phone,String pwd){
		mainActivity.loadingDialog.show();
		AsyncLogin loginTask = new AsyncLogin(mainActivity, phone, pwd, new OnLoginInter() {
			@Override
			public void back(final BaseInfo baseInfo) {
				mainActivity.loadingDialog.dismiss();
				if(baseInfo == null){
					Message msg = handler.obtainMessage(REQUEST_PERSONAL_LOGIN_EXCEPTION_WHAT);
					msg.obj = "您的网络不给力";
					handler.sendMessage(msg);
					return;
				}
				int resultCode = SettingsManager.getResultCode(baseInfo);
				if(resultCode == 0){
					UserInfo user = baseInfo.getUserInfo();
					if(user != null){
						GesturePwdEntity entity = DBGesturePwdManager.getInstance(getActivity().getApplicationContext()).getGesturePwdEntity(user.getId());
						if(entity != null && !entity.getPwd().isEmpty()){

						}else{
							Intent intent = new Intent(mainActivity,GestureEditActivity.class);
							startActivity(intent);
						}
					}

					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							Message msg = handler.obtainMessage(REQUEST_PERSONAL_LOGIN_SUCCESS_WHAT);
							msg.obj = baseInfo;
							handler.sendMessage(msg);
						}
					}, 200L);
				}else{
					Message msg = handler.obtainMessage(REQUEST_PERSONAL_LOGIN_EXCEPTION_WHAT);
					msg.obj = baseInfo.getMsg();
					handler.sendMessage(msg);
				}
			}
		});
		loginTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 请求用户信息，根据hf_user_id字段判断用户是否有汇付账户
	 * @param userId
	 * @param phone
	 */
	private void requestUserInfo(final String userId,String phone){
		AsyncUserSelectOne userTask = new AsyncUserSelectOne(mainActivity, userId, phone,"", "", new OnGetUserInfoByPhone() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						UserInfo userInfo = baseInfo.getUserInfo();
						Message msg = handler.obtainMessage(REQUEST_GET_USERINFO_SUCCESS);
						msg.obj = userInfo;
						handler.sendMessage(msg);
					}else{
						requestYilianAccount(userId,false);
					}
				}else{
					requestYilianAccount(userId,false);
				}
			}
		});
		userTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 易联账户
	 * @param userId
	 * @param isRequestHuifu 是否有汇付的账户
	 */
	private void requestYilianAccount(final String userId,final boolean isRequestHuifu){
		AsyncYiLianRMBAccount yilianTask = new AsyncYiLianRMBAccount(mainActivity, userId, new OnCommonInter(){
			@Override
			public void back(BaseInfo info) {
				if(info != null){
					int resultCode = SettingsManager.getResultCode(info);
					if(resultCode == 0){
						yilianAccountInfo = info.getRmbAccountInfo();
						if(isRequestHuifu){
							requestHuifuAccount(userId);
						}else{
							requestYJBInterest(userId, "未还款");
						}
					}
				}
			}
		});
		yilianTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 汇付账户信息
	 * @param userId
	 */
	private void requestHuifuAccount(final String userId){
		AsyncHuiFuRMBAccount huifuTask = new AsyncHuiFuRMBAccount(mainActivity, userId, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						huifuAccountInfo = baseInfo.getRmbAccountInfo();
						requestYJBInterest(userId, "未还款");
					}
				}
			}
		});
		huifuTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
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
		AsyncAddPhoneInfo addPhoneInfoTask = new AsyncAddPhoneInfo(mainActivity, userId, phone, phoneModel,
				sdkVersion, systemVersion, "android", location, contact, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {

			}
		});
		addPhoneInfoTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 元金币账户
	 * @param userId
	 */
	private void requestYuanMoney(String userId){
		AsyncUserYUANAccount accountTask = new AsyncUserYUANAccount(mainActivity,
				userId, new OnUserYUANAccountInter() {
			@Override
			public void back(BaseInfo info) {
				mainRefreshLayout.refreshComplete();
				if(info != null){
					int resultCode = SettingsManager.getResultCode(info);
					if(resultCode == 0){
						UserYUANAccountInfo accountInfo = info.getYuanAccountInfo();
						if(accountInfo != null){
							double coinD = 0d;
							try {
								coinD= Double.parseDouble(accountInfo.getUse_coin());
							} catch (Exception e) {
							}
							if(coinD <= 0){
								usedYJBTV.setVisibility(View.GONE);
							}else{
								usedYJBTV.setVisibility(View.VISIBLE);
								usedYJBTV.setText(accountInfo.getUse_coin()+"元金币可用");
							}
						}
					}else{
						usedYJBTV.setVisibility(View.GONE);
					}
				}else{
					usedYJBTV.setVisibility(View.GONE);
				}
			}
		});
		accountTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 元金币本金及收益
	 * @param userId
	 * @param repayStatus
	 */
	private void requestYJBInterest(String userId,String repayStatus){
		AsyncYJBInterest yjbTask = new AsyncYJBInterest(mainActivity, userId, repayStatus, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						yjbInterestBaseInfo = baseInfo;
						try {
							initAccountData(yilianAccountInfo,huifuAccountInfo,Double.parseDouble(baseInfo.getMsg()));
						} catch (Exception e) {
							initAccountData(yilianAccountInfo,huifuAccountInfo,0);
						}
					}else{
						initAccountData(yilianAccountInfo,huifuAccountInfo,0);
					}
				}
			}
		});
		yjbTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 企业用户登录
	 * @param username
	 * @param password
	 */
	private void requestCompLogin(String username,String password){
		if(mainActivity.loadingDialog != null){
			mainActivity.loadingDialog.show();
		}
		AsyncCompLogin loginTask = new AsyncCompLogin(mainActivity, username, password,
				new OnCommonInter(){
					@Override
					public void back(final BaseInfo baseInfo) {
						mainActivity.loadingDialog.dismiss();
						if(baseInfo == null){
							Message msg = handler.obtainMessage(REQUEST_PERSONAL_LOGIN_EXCEPTION_WHAT);
							msg.obj = "您的网络不给力";
							handler.sendMessage(msg);
							return;
						}
						int resultCode = SettingsManager.getResultCode(baseInfo);
						if(resultCode == 0){
							UserInfo user = baseInfo.getUserInfo();
							if(user != null){
								GesturePwdEntity entity = DBGesturePwdManager.getInstance(getActivity().getApplicationContext()).getGesturePwdEntity(user.getId());
								if(entity != null && !entity.getPwd().isEmpty()){

								}else{
									Intent intent = new Intent(mainActivity,GestureEditActivity.class);
									startActivity(intent);
								}
							}

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
							msg.obj = baseInfo.getMsg();
							handler.sendMessage(msg);
						}
					}
				});
		loginTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 判断是否领取过加息券
	 * @param userId
	 * @param couponFrom 来源
	 */
	private void checkIsGetJXQ(final UserInfo info,String userId,String couponFrom,String useStatus,String page,
							   String pageSize,String transfer){
		if(mainActivity.loadingDialog != null){
			mainActivity.loadingDialog.show();
		}
		AsyncJXQLogList jxqListTask = new AsyncJXQLogList(mainActivity, userId, couponFrom,useStatus,
				page,pageSize,transfer, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						//已经领取过
						mainActivity.loadingDialog.dismiss();
						if(SettingsManager.checkLXFXActivity() == 0){
						}
					}else{
						//未领取过
						//判断有没有实名
						checkIsVerify();
					}
				}else{
					mainActivity.loadingDialog.dismiss();
				}
			}
		});
		jxqListTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}


	/**
	 * 我的礼品列表
	 * @param userId
	 */
	private void requestPrizeList(String userId){
		AsyncPrizeList prizeListTask = new AsyncPrizeList(mainActivity, userId, "0", "5", "","HYFL_02",
				new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//
							}else{
								//没有领取过,福利计划2期
								if(SettingsManager.getLXFXJXQFlag(mainActivity,SettingsManager.getUserId(getActivity())+"hyfl02")){
									showGetJXQDialog("HYFL_02");
								}
							}
						}else{
						}
					}
				});
		prizeListTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 判断活动是否已经开始
	 * @param activeTitle
	 */
	private void requestActiveTime(String activeTitle){
		AsyncXCFLActiveTime task = new AsyncXCFLActiveTime(mainActivity, activeTitle,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if (resultCode == 0) {
								//活动已开始
								requestPrizeList(SettingsManager.getUserId(getActivity().getApplicationContext()));
							} else if (resultCode == -3) {
								//活动结束
							} else if (resultCode == -2) {
								//活动还没开始
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 验证用户是否已经认证
	 */
	private void checkIsVerify(){
		RequestApis.requestIsVerify(mainActivity, SettingsManager.getUserId(mainActivity), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				mainActivity.loadingDialog.dismiss();
				if(flag){
					//用户已经实名
					if(SettingsManager.getLXFXJXQFlag(mainActivity,SettingsManager.getUserId(getActivity())+"lxfx")){
						showGetJXQDialog("JXQ");
					}
				}else{
					//用户没有实名
					if(SettingsManager.getLXFXJXQFlag(mainActivity,SettingsManager.getUserId(getActivity())+"lxfx_noverify")){
						showNoVerifyDialog();
					}
				}
			}
			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}

	/**
	 * 企业用户弹出修改初始密码的dialog
	 */
	private void showCompLoginPwdDialog(){
		if(isShowCompLoginPWDDialog)
			return;
		View contentView = LayoutInflater.from(mainActivity).inflate(R.layout.comp_change_pwd_dialog, null);
		final Button sureBtn = (Button) contentView.findViewById(R.id.comp_change_pwd_dialog_sure_btn);//马上修改
		final Button cancelBtn = (Button) contentView.findViewById(R.id.comp_change_pwd_dialog_cancel_btn);//稍后修改
		AlertDialog.Builder builder=new AlertDialog.Builder(mainActivity, R.style.Dialog_Transparent);  //先得到构造器
		builder.setView(contentView);
		builder.setCancelable(true);
		final AlertDialog dialog = builder.create();
		sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent(mainActivity,ModifyLoginPwdActivity.class);
				intent.putExtra("USERINFO",mUserInfo);
				startActivity(intent);
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		//参数都设置完成了，创建并显示出来
		dialog.show();
		WindowManager windowManager = mainActivity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth()*6/7;
		dialog.getWindow().setAttributes(lp);
		isShowCompLoginPWDDialog = true;
	}

	/**
	 * 领取加息券的Dialog
	 */
	private void showGetJXQDialog(final String type) {
		View contentView = LayoutInflater.from(mainActivity).inflate(
				R.layout.user_fragment_lxfx_jxq, null);
		Button leftBtn = (Button) contentView.findViewById(R.id.user_fragment_lxfx_dialog_layout_leftBtn);
		Button rightBtn = (Button) contentView.findViewById(R.id.user_fragment_lxfx_dialog_layout_rightBtn);
		ImageView delBtn = (ImageView) contentView.findViewById(R.id.user_fragment_lxfx_dialog_layout_delbtn);
		TextView content = (TextView) contentView.findViewById(R.id.user_fragment_lxfx_content);
		if("JXQ".equals(type)){
			content.setText("您有一张加息券未领取！");
		}else if("HYFL_02".equals(type)){
			content.setText("亲爱的用户，您有20种礼品可领取哟~");
		}
		final CheckBox cb = (CheckBox) contentView.findViewById(R.id.user_fragment_lxfx_dialog_cb);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				mainActivity, R.style.Dialog_Transparent); // 先得到构造器
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		final String keyLXFX = SettingsManager.getUserId(getActivity())+"lxfx";
		final String keyHYFL = SettingsManager.getUserId(getActivity())+"hyfl02";
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cb.isChecked()){
					if("JXQ".equals(type)){
						SettingsManager.setLXFXJXQFlag(mainActivity,keyLXFX, false);
					}else if("HYFL_02".equals(type)){
						SettingsManager.setHYFLFlag(mainActivity,keyHYFL, false);
					}
				}else{
					if("JXQ".equals(type)){
						SettingsManager.setLXFXJXQFlag(mainActivity,keyLXFX, true);
					}else if("HYFL_02".equals(type)){
						SettingsManager.setHYFLFlag(mainActivity,keyHYFL, true);
					}
				}
				if("JXQ".equals(type)){
					Intent intent = new Intent(mainActivity,LXFXTempActivity.class);
					startActivity(intent);
				}else if("HYFL_02".equals(type)){
					Intent intent = new Intent(mainActivity,PrizeRegion2TempActivity.class);
					startActivity(intent);
				}
				dialog.dismiss();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cb.isChecked()){
					if("JXQ".equals(type)){
						SettingsManager.setLXFXJXQFlag(mainActivity,keyLXFX, false);
					}else if("HYFL_02".equals(type)){
						SettingsManager.setHYFLFlag(mainActivity,keyHYFL, false);
					}
				}else{
					if("JXQ".equals(type)){
						SettingsManager.setLXFXJXQFlag(mainActivity,keyLXFX, true);
					}else if("HYFL_02".equals(type)){
						SettingsManager.setHYFLFlag(mainActivity,keyHYFL, true);
					}
				}
				dialog.dismiss();
			}
		});
		delBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cb.isChecked()){
					if("JXQ".equals(type)){
						SettingsManager.setLXFXJXQFlag(mainActivity,keyLXFX, false);
					}else if("HYFL_02".equals(type)){
						SettingsManager.setHYFLFlag(mainActivity,keyHYFL, false);
					}
				}else{
					if("JXQ".equals(type)){
						SettingsManager.setLXFXJXQFlag(mainActivity,keyLXFX, true);
					}else if("HYFL_02".equals(type)){
						SettingsManager.setHYFLFlag(mainActivity,keyHYFL, true);
					}
				}
				dialog.dismiss();
			}
		});
		// 参数都设置完成了，创建并显示出来
		dialog.show();
		WindowManager windowManager = mainActivity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth() * 4 / 5;
		dialog.getWindow().setAttributes(lp);
	}

	/**
	 * 未实名 乐享返现
	 */
	private void showNoVerifyDialog() {
		View contentView = LayoutInflater.from(mainActivity).inflate(
				R.layout.user_fragment_lxfx_noverify_layout, null);
		Button leftBtn = (Button) contentView.findViewById(R.id.user_fragment_lxfx_noverify_dialog_layout_leftBtn);
		Button rightBtn = (Button) contentView.findViewById(R.id.user_fragment_lxfx_noverify_dialog_layout_rightBtn);
		ImageView delBtn = (ImageView) contentView.findViewById(R.id.user_fragment_lxfx_noverify_dialog_layout_delbtn);
		final CheckBox cb = (CheckBox) contentView.findViewById(R.id.user_fragment_lxfx_noverify_dialog_cb);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				mainActivity, R.style.Dialog_Transparent); // 先得到构造器
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		final String key = SettingsManager.getUserId(getActivity())+"lxfx_noverify";
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cb.isChecked()){
					SettingsManager.setLXFXJXQFlag(mainActivity,key, false);
				}else{
					SettingsManager.setLXFXJXQFlag(mainActivity,key, true);
				}
				Intent intent = new Intent(mainActivity,LXFXTempActivity.class);
				startActivity(intent);
				dialog.dismiss();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cb.isChecked()){
					SettingsManager.setLXFXJXQFlag(mainActivity, key,false);
				}else{
					SettingsManager.setLXFXJXQFlag(mainActivity, key,true);
				}
				dialog.dismiss();
			}
		});
		delBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// 参数都设置完成了，创建并显示出来
		dialog.show();
		WindowManager windowManager = mainActivity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth() * 4 / 5;
		dialog.getWindow().setAttributes(lp);
	}
}