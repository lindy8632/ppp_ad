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
import com.ylfcf.ppp.async.AsyncSMSRegiste;
import com.ylfcf.ppp.async.AsyncUserBankCard;
import com.ylfcf.ppp.async.AsyncWithdraw;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.SMSType;
import com.ylfcf.ppp.entity.TaskDate;
import com.ylfcf.ppp.entity.UserCardInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnUserBankCardInter;
import com.ylfcf.ppp.util.CountDownAsyncTask;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.view.DealPwdErrorPopwindow;

/**
 * �˻�����
 * @author Administrator
 *
 */
public class WithdrawActivity extends BaseActivity implements OnClickListener{
	private final int REQUEST_WITHDRAW_WHAT = 2301;
	private final int REQUEST_WITHDRAW_SUCCESS = 2302;
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private LinearLayout mainLayout;
	
	private TextView banlanceTV;//�˻����
	private TextView bankcardTV;//�������п�
	private Button withdrawBtn;//ȷ������
	private UserRMBAccountInfo accountInfo;
	private UserCardInfo usercardInfo;
	private EditText withdrawMoneyET,dealPwdET,authcodeET;
	private Button getAuthcodeBtn;//��ȡ��֤��
	private TextView withdrawCancel;//�������볷��
	private TextView withdrawPwdGetback;//�һ���������
	private TextView smsPromptTV;//������֤�뷢�ͽ����ʾ����
	private TextView promptText;
	
	private String authnumSMSUser = "";//�û�������ֻ���֤��
	private String authnumSMSWeb = "";//ϵͳ���ɵ��ֻ���֤��
	private CountDownAsyncTask countDownAsynTask = null;
	private final long intervalTime = 1000L;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_WITHDRAW_WHAT:
				requestWithdraw(SettingsManager.getUserId(getApplicationContext()), withdrawMoneyET.getText().toString());
				break;
			case CountDownAsyncTask.PROGRESS_UPDATE:
         		TaskDate date = (TaskDate) msg.obj;
				long time = date.getTime();
				countDownView(time);
         		break;
         	case CountDownAsyncTask.FINISH:
         		getAuthcodeBtn.setText("��ȡ��֤��");
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
		setContentView(R.layout.withdraw_activity);
		
		findViews();
		requestUserVerify(SettingsManager.getUserId(getApplicationContext()), "����");
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("�˻�����");
		promptText = (TextView)findViewById(R.id.withdraw_activity_prompt);
        promptText.setText("* ���ͨ�����������룬һ��T�������գ�+1�����յ��ˣ����磺�����������壬һ���������һ���ˣ���ʵ�ʵ���ʱ�����Ը������е���ʱ��Ϊ׼��\n* �������ֲ��õ���2Ԫ��\n* ����������ȡ������2Ԫ�����������ֵĽ�������ȿ۳�����ʵ�ʵ����������������ֽ���ȥ������֮��Ľ�");
		withdrawBtn = (Button)findViewById(R.id.withdraw_activity_btn);
		withdrawBtn.setOnClickListener(this);
		mainLayout = (LinearLayout)findViewById(R.id.withdraw_activity_mainlayout);
		banlanceTV = (TextView)findViewById(R.id.withdraw_activity_balance_tv);
		bankcardTV = (TextView)findViewById(R.id.withdraw_activity_bankcard_tv);
		withdrawMoneyET = (EditText)findViewById(R.id.withdraw_activity_money_et);
		withdrawMoneyET.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
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
		dealPwdET = (EditText)findViewById(R.id.withdraw_activity_pwd_et);
		authcodeET = (EditText)findViewById(R.id.withdraw_activity_authcode_et);
		smsPromptTV = (TextView) findViewById(R.id.withdraw_activity_sms_prompt);
		getAuthcodeBtn = (Button)findViewById(R.id.withdraw_activity_get_authnum_btn);
		getAuthcodeBtn.setOnClickListener(this);
		withdrawCancel = (TextView) findViewById(R.id.withdraw_activity_cancel_btn);
		withdrawCancel.setOnClickListener(this);
		withdrawCancel.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //�»���
		withdrawPwdGetback = (TextView)findViewById(R.id.withdraw_activity_getback_withdraw_pwd);
		withdrawPwdGetback.setOnClickListener(this);
		withdrawPwdGetback.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //�»���
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	private void initVerifyLayout(){
		bankcardTV.setText(Util.hiddenBankCard(usercardInfo.getBank_card()));
	}

	/**
	 * ��ȡ��֤�뵹��ʱ
	 * @param time
	 */
	private void countDownView(long time){
		time /= intervalTime; 
		StringBuffer sb = new StringBuffer();
		sb.append(time).append("����ط�");
		getAuthcodeBtn.setText(sb.toString());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.withdraw_activity_cancel_btn:
			Intent intent = new Intent(WithdrawActivity.this,WithdrawListActivity.class);
			startActivity(intent);
			break;
		case R.id.withdraw_activity_btn:
			checkWithdrawInfo();
			break;
		case R.id.withdraw_activity_getback_withdraw_pwd:
			Intent intentA = new Intent(WithdrawActivity.this,WithdrawPwdGetbackActivity.class);
			intentA.putExtra("type", "�һ�");
			startActivity(intentA);
			break;
		case R.id.withdraw_activity_get_authnum_btn:
			//��ȡ��֤��
			sendSMScode();
			break;
		default:
			break;
		}
	}
	
	/**
	 * ���Ͷ�����֤��
	 */
	private void sendSMScode(){
		String params[] = null;
		String userName = SettingsManager.getUserName(getApplicationContext());
		String userPhone = SettingsManager.getUser(getApplicationContext());
		if(userName != null && !"".equals(userName)){
			params = SettingsManager.getSMSWithdrawApplyParams(userName);//ƴ�Ӷ�����֤���ʽ
		}else{
			params = SettingsManager.getSMSWithdrawApplyDefaultParams();//ƴ�Ӷ�����֤���ʽ
		}
		
		authnumSMSWeb = params[0];
		YLFLogger.d("����������֤�룺"+authnumSMSWeb);
		requestSMSAuthCode(userPhone,SMSType.SMS_WITHDRAW_APPLY,params[1],params[0]);
		getAuthcodeBtn.setEnabled(false);
	}
	
	private void checkWithdrawInfo(){
		String accountStr = withdrawMoneyET.getText().toString();
		String dealPwd = dealPwdET.getText().toString();
		authnumSMSUser = authcodeET.getText().toString();
		double accountInt = 0;
		try {
			accountInt = Double.parseDouble(accountStr);
		} catch (Exception e) {
		}
		if(accountInt < 2){
			Util.toastShort(WithdrawActivity.this, "���ֽ���С��2Ԫ");
		}else if("".equals(dealPwd)){
			Util.toastShort(WithdrawActivity.this, "��������������");
		}else if("".equals(authnumSMSUser)){
			Util.toastShort(WithdrawActivity.this, "��������֤��");
		}else if(!authnumSMSUser.equals(authnumSMSWeb)){
			Util.toastShort(WithdrawActivity.this, "��֤���������");
		}else{
			checkDealPwd(SettingsManager.getUserId(getApplicationContext()), dealPwd);
		}
	}
	
	/**
	 * ��ʾ��������������������
	 */
	private void showDealPwdErrorPopwindow(String errorMsg){
		View popView = LayoutInflater.from(this).inflate(R.layout.dealpwd_error_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(WithdrawActivity.this);
		int width = screen[0]*4/5;
		int height = screen[1]*2/7;
		DealPwdErrorPopwindow popwindow = new DealPwdErrorPopwindow(WithdrawActivity.this,popView, width, height,errorMsg);
		popwindow.show(mainLayout);
	}
	
	/**
	 * �������ֽӿ�
	 * @param userId 
	 * @param cashAccount ���ֽ��
	 */
	private void requestWithdraw(String userId,final String cashAccount){
		AsyncWithdraw task = new AsyncWithdraw(WithdrawActivity.this, userId, cashAccount, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null)
						mLoadingDialog.dismiss();
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//��ת���ֳɹ�ҳ��
								Intent intent = new Intent(WithdrawActivity.this,WithdrawSuccessActivity.class);
								intent.putExtra("withdraw_money", cashAccount);
								startActivity(intent);
								finish();
							}else{
								Util.toastShort(WithdrawActivity.this, baseInfo.getMsg());
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ��ȡ���п���Ϣ
	 * @param userId
	 * @param type
	 */
	private void requestUserVerify(String userId,String type){
		if(mLoadingDialog != null)
		mLoadingDialog.show();
		AsyncUserBankCard bankcardTask = new AsyncUserBankCard(WithdrawActivity.this, userId, type, new OnUserBankCardInter(){
			@Override
			public void back(BaseInfo info) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing())
					mLoadingDialog.dismiss();
				if(info != null){
					int resultCode = SettingsManager.getResultCode(info);
					if(resultCode == 0){
						usercardInfo = info.getUserCardInfo();
						initVerifyLayout();
						requestYilianAccount(SettingsManager.getUserId(getApplicationContext()));
					}
				}
			}
		});
		bankcardTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * У����������
	 * @param userId
	 * @param dealPwd
	 */
	private  void checkDealPwd(String userId,String dealPwd){
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		AsyncCheckDealPwd dealPwdTask = new AsyncCheckDealPwd(WithdrawActivity.this, userId, dealPwd, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						//У��ɹ�
						handler.sendEmptyMessage(REQUEST_WITHDRAW_WHAT);
					}else if(resultCode == -1){
						if(mLoadingDialog != null && mLoadingDialog.isShowing())
							mLoadingDialog.dismiss();
						Util.toastShort(WithdrawActivity.this, baseInfo.getMsg());
					}else if(resultCode == -2){
						//У��ʧ��
						if(mLoadingDialog != null && mLoadingDialog.isShowing())
							mLoadingDialog.dismiss();
						showDealPwdErrorPopwindow(baseInfo.getMsg());
					}
				}
			}
		});
		dealPwdTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * �����˻�
	 * @param userId
	 */
	private void requestYilianAccount(String userId){
		AsyncYiLianRMBAccount yilianTask = new AsyncYiLianRMBAccount(WithdrawActivity.this, userId, new OnCommonInter(){
			@Override
			public void back(BaseInfo info) {
				if(info != null){
					int resultCode = SettingsManager.getResultCode(info);
					if(resultCode == 0){
						accountInfo = info.getRmbAccountInfo();
						if(accountInfo != null)
						banlanceTV.setText(accountInfo.getUse_money()+"Ԫ");
					}
				}
			}
		});
		yilianTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * ���Ͷ�����֤��
	 */
	private void requestSMSAuthCode(String phone,String template,String params,String verfiy){
		AsyncSMSRegiste asyncSMSRegiste = new AsyncSMSRegiste(WithdrawActivity.this, phone, template, params, verfiy, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								long createTime = System.currentTimeMillis();
								countDownAsynTask = new CountDownAsyncTask(handler,"",System.currentTimeMillis(),
										createTime+1000*60,intervalTime);
								SettingsManager.FULL_TASK_EXECUTOR.execute(countDownAsynTask);
								smsPromptTV.setVisibility(View.VISIBLE);
							}else{
								getAuthcodeBtn.setEnabled(true);
								smsPromptTV.setVisibility(View.GONE);
								Util.toastShort(WithdrawActivity.this, baseInfo.getMsg());
							}
						}else{
							getAuthcodeBtn.setEnabled(true);
						}
					}
				});
		asyncSMSRegiste.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}