package com.ylfcf.ppp.ui;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncUpdateUserInfo;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;
import com.ylfcf.ppp.inter.Inter.OnUpdateUserInfoInter;
import com.ylfcf.ppp.util.Constants.UserType;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.widget.LoadingDialog;

import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 修改登录密码
 * 
 * @author Administrator
 * 
 */
public class ModifyLoginPwdActivity extends BaseActivity implements
		OnClickListener {
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView userName;
	private EditText oldPwdET;
	private EditText newPwdET;
	private EditText newPwdRepeatET;
	private TextView pwdPrompt;
	private Button cmpBtn;
	private UserInfo userInfo;
	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.modify_pwd_activity);
		loadingDialog = new LoadingDialog(ModifyLoginPwdActivity.this,
				"正在加载...", R.anim.loading);
		userInfo = (UserInfo) getIntent().getSerializableExtra("USERINFO");

		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("修改登录密码");

		userName = (TextView) findViewById(R.id.modify_loginpwd_activity_name);
		if (userInfo != null) {
			if(SettingsManager.isPersonalUser(getApplicationContext())){
				userName.setText(Util.hidRealName(userInfo.getReal_name()));
			}else{
				userName.setText(userInfo.getReal_name());
			}
		}
		oldPwdET = (EditText) findViewById(R.id.modify_loginpwd_activity_oldpwd);
		newPwdET = (EditText) findViewById(R.id.modify_loginpwd_activity_newpwd);
		newPwdRepeatET = (EditText) findViewById(R.id.modify_loginpwd_activity_newpwd_repeat);
		cmpBtn = (Button) findViewById(R.id.modify_loginpwd_activity_btn);
		cmpBtn.setOnClickListener(this);

		pwdPrompt = (TextView) findViewById(R.id.modify_loginpwd_activity_pwdprompt);
		SpannableStringBuilder builder = new SpannableStringBuilder(pwdPrompt
				.getText().toString());
		// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
		ForegroundColorSpan graySpan = new ForegroundColorSpan(getResources()
				.getColor(R.color.gray1));
		ForegroundColorSpan blueSpan = new ForegroundColorSpan(getResources()
				.getColor(R.color.common_topbar_bg_color));
		builder.setSpan(graySpan, 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(blueSpan, 6, 10, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		builder.setSpan(graySpan, 10, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		pwdPrompt.setText(builder);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.modify_loginpwd_activity_btn:
			checkData();
		default:
			break;
		}
	}

	String newPwd = "";
	private void checkData() {
		String oldPwdLocation = SettingsManager
				.getLoginPassword(getApplicationContext());
		String oldPwd = oldPwdET.getText().toString();
		String oldPwdMd5 = Util.md5Encryption(oldPwd);
		newPwd = newPwdET.getText().toString();
		String repeatPwd = newPwdRepeatET.getText().toString();
		if (!"".equals(oldPwd)) {
			if (oldPwdMd5.equals(oldPwdLocation)) {
				if (!"".equals(newPwd)) {
					if (Util.checkPassword(newPwd)) {
						if (newPwd.equals(repeatPwd)) {
							// 修改密码接口
							requestModifyPwd(
									SettingsManager
											.getUserId(getApplicationContext()),
									newPwd, SettingsManager
											.getUser(getApplicationContext()),"");
						} else {
							Util.toastShort(ModifyLoginPwdActivity.this,
									"新密码输入不一致");
						}
					} else {
						Util.toastShort(ModifyLoginPwdActivity.this,
								"密码长度需为6~16位");
					}
				} else {
					Util.toastShort(ModifyLoginPwdActivity.this, "请输入新密码");
				}
			} else {
				Util.toastShort(ModifyLoginPwdActivity.this, "原始密码输入错误");
			}
		} else {
			Util.toastShort(ModifyLoginPwdActivity.this, "请输入原始密码");
		}
	}

	private void requestModifyPwd(String userId, String newPwd, String phone,final String initPwd) {
		if (loadingDialog != null) {
			loadingDialog.show();
		}
		AsyncUpdateUserInfo task = new AsyncUpdateUserInfo(
				ModifyLoginPwdActivity.this, userId, newPwd, phone, "", "", "",
				"", "", initPwd,new OnUpdateUserInfoInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								if("0".equals(initPwd) || "".equals(initPwd)){
									requestUserInfo(
											SettingsManager
													.getUserId(getApplicationContext()),
											SettingsManager
													.getUser(getApplicationContext()),
											"");
								}else if("1".equals(initPwd)){
									Util.toastShort(ModifyLoginPwdActivity.this,
											"密码已修改成功");
									finish();
								}
								
							} else {
								Util.toastShort(ModifyLoginPwdActivity.this,
										baseInfo.getMsg());
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 获取用户信息
	 * 
	 * @param userId
	 * @param phone
	 * @param openId
	 */
	private void requestUserInfo(String userId, String phone, String openId) {
		AsyncUserSelectOne userTask = new AsyncUserSelectOne(
				ModifyLoginPwdActivity.this, userId, phone, openId,
				new OnGetUserInfoByPhone() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (loadingDialog != null && loadingDialog.isShowing()) {
							loadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								userInfo = baseInfo.getUserInfo();
								SettingsManager.setLoginPassword(
										getApplicationContext(),
										userInfo.getPassword(), true);
								if(SettingsManager.isPersonalUser(getApplicationContext())){
									Util.toastShort(ModifyLoginPwdActivity.this,
											"密码已修改成功");
									finish();
								}else if(SettingsManager.isCompanyUser(getApplicationContext())){
									//再请求一遍接口改变状态
									requestModifyPwd(
											SettingsManager
													.getUserId(getApplicationContext()),
											newPwd, SettingsManager
													.getUser(getApplicationContext()),"1");
								}
							} else {
								Util.toastLong(ModifyLoginPwdActivity.this,
										baseInfo.getMsg());
							}
						} else {
							Util.toastLong(ModifyLoginPwdActivity.this,
									"您的网络不给力");
						}
					}
				});
		userTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
