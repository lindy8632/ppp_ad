package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.graphics.Paint;
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
import com.ylfcf.ppp.async.AsyncCheckDealPwd;
import com.ylfcf.ppp.async.AsyncCompApplyCash;
import com.ylfcf.ppp.async.AsyncCompUserSelectOne;
import com.ylfcf.ppp.async.AsyncSMSRegiste;
import com.ylfcf.ppp.async.AsyncUserBankCard;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.SMSType;
import com.ylfcf.ppp.entity.TaskDate;
import com.ylfcf.ppp.entity.UserCardInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnUserBankCardInter;
import com.ylfcf.ppp.util.CountDownAsyncTask;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.view.DealPwdErrorPopwindow;
import com.ylfcf.ppp.widget.LoadingDialog;

/**
 * 企业用户申请提现
 * 
 * @author Mr.liu
 * 
 */
public class WithdrawCompActivity extends BaseActivity implements
		OnClickListener {
	private final int REQUEST_WITHDRAW_WHAT = 2301;
	private final int REQUEST_WITHDRAW_SUCCESS = 2302;
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private LinearLayout mainLayout;

	private TextView banlanceTV;// 账户余额
	private TextView bankcardTV;// 提现银行卡
	private TextView bankName;//银行名字
	private Button withdrawBtn;// 确认提现
	private UserRMBAccountInfo accountInfo;
	private EditText withdrawMoneyET, dealPwdET, authcodeET;
	private Button getAuthcodeBtn;// 获取验证码
	private TextView withdrawCancel;// 提现申请撤销
	private TextView withdrawPwdGetback;// 找回提现密码
	private TextView smsPromptTV;// 短信验证码发送结果提示文字
	private LoadingDialog loadingDialog;
	private TextView promptText;

	private String authnumSMSUser = "";// 用户输入的手机验证码
	private String authnumSMSWeb = "";// 系统生成的手机验证码
	private CountDownAsyncTask countDownAsynTask = null;
	private final long intervalTime = 1000L;
	private UserInfo userInfo;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_WITHDRAW_WHAT:
				requestWithdraw(
						SettingsManager.getUserId(getApplicationContext()),
						withdrawMoneyET.getText().toString());
				break;
			case CountDownAsyncTask.PROGRESS_UPDATE:
				TaskDate date = (TaskDate) msg.obj;
				long time = date.getTime();
				countDownView(time);
				break;
			case CountDownAsyncTask.FINISH:
				getAuthcodeBtn.setText("获取验证码");
				getAuthcodeBtn.setEnabled(true);
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
		setContentView(R.layout.withdraw_comp_activity);

		loadingDialog = new LoadingDialog(WithdrawCompActivity.this, "正在加载...",
				R.anim.loading);
		findViews();
		requestCompUserInfo(SettingsManager.getUserId(getApplicationContext()), "", "", "");
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("账户提现");
		
		promptText = (TextView) findViewById(R.id.withdraw_comp_activity_prompt);
		promptText
				.setText("* 审核通过的提现申请，一般T（申请日）+1工作日到账（例如：申请日是周五，一般会在下周一到账），实际到账时间请以各个银行到账时间为准；\n* 单笔提现不得低于5元；\n* 单笔提现收取手续费5元，从提现的金额中优先扣除，即实际到卡金额等于申请提现金额减去手续费之后的金额。");
		withdrawBtn = (Button) findViewById(R.id.withdraw_comp_activity_btn);
		withdrawBtn.setOnClickListener(this);
		mainLayout = (LinearLayout) findViewById(R.id.withdraw_comp_activity_mainlayout);
		banlanceTV = (TextView) findViewById(R.id.withdraw_comp_activity_balance_tv);
		bankcardTV = (TextView) findViewById(R.id.withdraw_comp_activity_bankcard_tv);
		bankName = (TextView) findViewById(R.id.withdraw_comp_activity_bankname_tv);
		withdrawMoneyET = (EditText) findViewById(R.id.withdraw_comp_activity_money_et);
		withdrawMoneyET.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						withdrawMoneyET.setText(s);
						withdrawMoneyET.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					withdrawMoneyET.setText(s);
					withdrawMoneyET.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						withdrawMoneyET.setText(s.subSequence(0, 1));
						withdrawMoneyET.setSelection(1);
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		dealPwdET = (EditText) findViewById(R.id.withdraw_comp_activity_pwd_et);
		authcodeET = (EditText) findViewById(R.id.withdraw_comp_activity_authcode_et);
		smsPromptTV = (TextView) findViewById(R.id.withdraw_comp_activity_sms_prompt);
		getAuthcodeBtn = (Button) findViewById(R.id.withdraw_comp_activity_get_authnum_btn);
		getAuthcodeBtn.setOnClickListener(this);
		withdrawCancel = (TextView) findViewById(R.id.withdraw_comp_activity_cancel_btn);
		withdrawCancel.setOnClickListener(this);
		withdrawCancel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		withdrawPwdGetback = (TextView) findViewById(R.id.withdraw_comp_activity_getback_withdraw_pwd);
		withdrawPwdGetback.setOnClickListener(this);
		withdrawPwdGetback.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
	}

	private void initVerifyLayout(UserInfo userInfo) {
		bankcardTV.setText(Util.hiddenBankCard(userInfo.getBank_card()));
		bankName.setText(userInfo.getBank_name());
	}

	/**
	 * 获取验证码倒计时
	 * 
	 * @param time
	 */
	private void countDownView(long time) {
		time /= intervalTime;
		StringBuffer sb = new StringBuffer();
		sb.append(time).append("秒后重发");
		getAuthcodeBtn.setText(sb.toString());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.withdraw_comp_activity_cancel_btn:
			Intent intent = new Intent(WithdrawCompActivity.this,
					WithdrawListActivity.class);
			startActivity(intent);
			break;
		case R.id.withdraw_comp_activity_btn:
			checkWithdrawInfo();
			break;
		case R.id.withdraw_comp_activity_getback_withdraw_pwd:
			Intent intentA = new Intent(WithdrawCompActivity.this,
					WithdrawPwdGetbackActivity.class);
			intentA.putExtra("type", "找回");
			startActivity(intentA);
			break;
		case R.id.withdraw_comp_activity_get_authnum_btn:
			// 获取验证码
			sendSMScode();
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
	 * 发送短信验证码
	 */
	private void sendSMScode() {
		String params[] = null;
		String userName = SettingsManager.getUserName(getApplicationContext());
		String userPhone = SettingsManager.getCompPhone(getApplicationContext());
		if (userName != null && !"".equals(userName)) {
			params = SettingsManager.getSMSWithdrawApplyCompParams(userName);// 拼接短信验证码格式
		} else {
			params = SettingsManager.getSMSWithdrawApplyDefaultParams();// 拼接短信验证码格式
		}

		authnumSMSWeb = params[0];
		YLFLogger.d("申请提现验证码：" + authnumSMSWeb);
		requestSMSAuthCode(userPhone, SMSType.SMS_WITHDRAW_APPLY, params[1],
				params[0]);
		getAuthcodeBtn.setEnabled(false);
	}

	private void checkWithdrawInfo() {
		String accountStr = withdrawMoneyET.getText().toString();
		String dealPwd = dealPwdET.getText().toString();
		authnumSMSUser = authcodeET.getText().toString();
		double accountInt = 0;
		try {
			accountInt = Double.parseDouble(accountStr);
		} catch (Exception e) {
		}
		if (accountInt < 5) {
			Util.toastShort(WithdrawCompActivity.this, "提现金额不能小于5元");
		} else if ("".equals(dealPwd)) {
			Util.toastShort(WithdrawCompActivity.this, "请输入提现密码");
		} else if ("".equals(authnumSMSUser)) {
			Util.toastShort(WithdrawCompActivity.this, "请输入验证码");
		} else if (!authnumSMSUser.equals(authnumSMSWeb)) {
			Util.toastShort(WithdrawCompActivity.this, "验证码输入错误");
		} else {
			checkDealPwd(SettingsManager.getUserId(getApplicationContext()),
					dealPwd);
		}
	}

	/**
	 * 显示提现密码输入错误的文字
	 */
	private void showDealPwdErrorPopwindow(String errorMsg) {
		View popView = LayoutInflater.from(this).inflate(
				R.layout.dealpwd_error_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(WithdrawCompActivity.this);
		int width = screen[0] * 4 / 5;
		int height = screen[1] * 2 / 7;
		DealPwdErrorPopwindow popwindow = new DealPwdErrorPopwindow(
				WithdrawCompActivity.this, popView, width, height, errorMsg);
		popwindow.show(mainLayout);
	}

	/**
	 * 请求提现接口
	 * 
	 * @param userId
	 * @param cashAccount
	 *            提现金额
	 */
	private void requestWithdraw(String userId, final String cashAccount) {
		AsyncCompApplyCash cashTask = new AsyncCompApplyCash(WithdrawCompActivity.this, userId, cashAccount, new OnCommonInter(){
			@Override
			public void back(BaseInfo baseInfo) {
				loadingDialog.dismiss();
				if (baseInfo != null) {
					int resultCode = SettingsManager
							.getResultCode(baseInfo);
					if (resultCode == 0) {
						// 跳转提现成功页面
						Intent intent = new Intent(
								WithdrawCompActivity.this,
								WithdrawSuccessActivity.class);
						intent.putExtra("withdraw_money", cashAccount);
						startActivity(intent);
						finish();
					} else {
						Util.toastShort(WithdrawCompActivity.this,
								baseInfo.getMsg());
					}
				}
			}
		});
		cashTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 获取企业用户信息
	 * @param userId
	 * @param yyzzCode 营业执照
	 * @param jgxyCode 机构信用号
	 * @param khxkCode  开户许可号
	 */
	private void requestCompUserInfo(String userId,String yyzzCode,String jgxyCode,String khxkCode){
		AsyncCompUserSelectOne compTask = new AsyncCompUserSelectOne(WithdrawCompActivity.this, userId, yyzzCode, jgxyCode, khxkCode, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								userInfo = baseInfo.getUserInfo();
								initVerifyLayout(userInfo);
								requestYilianAccount(SettingsManager
										.getUserId(getApplicationContext()));
							}else{
								Util.toastLong(WithdrawCompActivity.this, baseInfo.getMsg());
							}
						}
					}
				});
		compTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 校验提现密码
	 * 
	 * @param userId
	 * @param dealPwd
	 */
	private void checkDealPwd(String userId, String dealPwd) {
		if (loadingDialog != null) {
			loadingDialog.show();
		}
		AsyncCheckDealPwd dealPwdTask = new AsyncCheckDealPwd(
				WithdrawCompActivity.this, userId, dealPwd, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								// 校验成功
								handler.sendEmptyMessage(REQUEST_WITHDRAW_WHAT);
							} else if (resultCode == -1) {
								loadingDialog.dismiss();
								Util.toastShort(WithdrawCompActivity.this,
										baseInfo.getMsg());
							} else if (resultCode == -2) {
								// 校验失败
								loadingDialog.dismiss();
								showDealPwdErrorPopwindow(baseInfo.getMsg());
							}
						}
					}
				});
		dealPwdTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 易联账户
	 * 
	 * @param userId
	 */
	private void requestYilianAccount(String userId) {
		AsyncYiLianRMBAccount yilianTask = new AsyncYiLianRMBAccount(
				WithdrawCompActivity.this, userId, new OnCommonInter() {
					@Override
					public void back(BaseInfo info) {
						if (info != null) {
							int resultCode = SettingsManager
									.getResultCode(info);
							if (resultCode == 0) {
								accountInfo = info.getRmbAccountInfo();
								if (accountInfo != null)
									banlanceTV.setText(accountInfo
											.getUse_money() + "元");
							}
						}
					}
				});
		yilianTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 发送短信验证码
	 */
	private void requestSMSAuthCode(String phone, String template,
			String params, String verfiy) {
		AsyncSMSRegiste asyncSMSRegiste = new AsyncSMSRegiste(
				WithdrawCompActivity.this, phone, template, params, verfiy,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								long createTime = System.currentTimeMillis();
								countDownAsynTask = new CountDownAsyncTask(
										handler, "",
										System.currentTimeMillis(),
										createTime + 1000 * 60, intervalTime);
								SettingsManager.FULL_TASK_EXECUTOR
										.execute(countDownAsynTask);
								smsPromptTV.setVisibility(View.VISIBLE);
							} else {
								getAuthcodeBtn.setEnabled(true);
								smsPromptTV.setVisibility(View.GONE);
								Util.toastShort(WithdrawCompActivity.this,
										baseInfo.getMsg());
							}
						} else {
							getAuthcodeBtn.setEnabled(true);
						}
					}
				});
		asyncSMSRegiste.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
