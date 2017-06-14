package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncVipBorrowInvest;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.inter.Inter.OnBorrowInvestInter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;

import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * VIP��Ͷ��ҳ��
 * @author Mr.liu
 *
 */
public class BidVIPActivity extends BaseActivity implements OnClickListener {
	private static final int REQUEST_INVEST_WHAT = 1201;
	private static final int REQUEST_INVEST_SUCCESS = 1202;
	private static final int REQUEST_INVEST_EXCEPTION = 1203;
	private static final int REQUEST_INVEST_FAILE = 1204;

	private static final int REQUEST_INVEST_INCREASE = 1205;
	private static final int REQUEST_INVEST_DESCEND = 1206;

	private LinearLayout topLeftBtn;
	private TextView topTitleTV, borrowName;
	private TextView userBalanceTV;// �û��������
	private TextView borrowBalanceTV;// ���ʣ���Ͷ���
	private Button rechargeBtn;// ��ֵ

	private Button descendBtn;// �ݼ���ť
	private Button increaseBtn;// ������ť
	private TextView increaseMoneyText;//�������
	private EditText investMoneyET;
	private ImageView deleteImg;// x��
	private TextView yjsyText;// Ԥ������
	private CheckBox compactCB;
	private TextView vipCompact;//VIP���Э��

	private ProductInfo mProductInfo;
	private int moneyInvest = 0;
	private int bonusMoney = 0;// Ԫ���
	private int upMoney = 0;//�������
	private double lowestMoney = 0;//���Ͷ�ʽ���Ͷ��
	private Button investBtn;
	private LinearLayout mainLayout;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_INVEST_WHAT:
				requestInvest(mProductInfo.getId(),
						SettingsManager.getUserId(getApplicationContext()),
						String.valueOf(moneyInvest), bonusMoney,
						SettingsManager.USER_FROM,
						SettingsManager.getUserFromSub(getApplicationContext()), "", "", "", "");
				break;
			case REQUEST_INVEST_SUCCESS:
				Intent intentSuccess = new Intent(BidVIPActivity.this,
						BidSuccessActivity.class);
				intentSuccess.putExtra("from_where", "VIP");
				startActivity(intentSuccess);
				mApp.finishAllActivityExceptMain();
				break;
			case REQUEST_INVEST_EXCEPTION:
				BaseInfo base = (BaseInfo) msg.obj;
				Util.toastShort(BidVIPActivity.this, base.getMsg());
				break;
			case REQUEST_INVEST_FAILE:
				Util.toastShort(BidVIPActivity.this, "�����쳣");
				break;
			case REQUEST_INVEST_INCREASE:
				investMoneyIncrease();
				break;
			case REQUEST_INVEST_DESCEND:
				investMoneyDescend();
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
		setContentView(R.layout.bid_vip_activity);
		mProductInfo = (ProductInfo) getIntent().getSerializableExtra(
				"PRODUCT_INFO");
		try {
			upMoney = Integer.parseInt(mProductInfo.getUp_money());
		} catch (Exception e) {
		}
		try {
			lowestMoney = Double.parseDouble(mProductInfo.getInvest_lowest().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		findViews();
		initInvestBalance(mProductInfo);
	}

	@Override
	protected void onResume() {
		super.onResume();
		requestUserAccountInfo(SettingsManager
				.getUserId(getApplicationContext()));
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("Ͷ��");

		borrowName = (TextView) findViewById(R.id.bid_vip_activity_borrow_name);
		borrowName.setText(mProductInfo.getBorrow_name());
		userBalanceTV = (TextView) findViewById(R.id.bid_vip_activity_user_balance);
		borrowBalanceTV = (TextView) findViewById(R.id.bid_vip_activity_borrow_balance);
		rechargeBtn = (Button) findViewById(R.id.bid_vip_activity_recharge_btn);
		rechargeBtn.setOnClickListener(this);
		descendBtn = (Button) findViewById(R.id.bid_vip_activity_discend_btn);
		descendBtn.setOnClickListener(this);
		descendBtn.setOnTouchListener(mOnTouchListener);
		increaseBtn = (Button) findViewById(R.id.bid_vip_activity_increase_btn);
		increaseBtn.setOnClickListener(this);
		increaseBtn.setOnTouchListener(mOnTouchListener);
		compactCB = (CheckBox) findViewById(R.id.bid_vip_activity_cb);
		vipCompact = (TextView) findViewById(R.id.bid_vip_activity_compact_text);
		vipCompact.setOnClickListener(this);
		increaseMoneyText = (TextView) findViewById(R.id.bid_vip_activity_increase_money);
		int increaseMoneyInt = 0;
		try {
			increaseMoneyInt = Integer.parseInt(mProductInfo.getUp_money());
			if(increaseMoneyInt >= 10000){
				increaseMoneyText.setText(increaseMoneyInt/10000+"��Ԫ����");
			}else{
				increaseMoneyText.setText(increaseMoneyInt+"Ԫ����");
			}
		} catch (Exception e) {
		}
		investMoneyET = (EditText) findViewById(R.id.bid_vip_activity_invest_et);
		investMoneyET.addTextChangedListener(watcherInvestMoney);
		investMoneyET.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					return;
				String investMoneyStr = investMoneyET.getText().toString();
				int investMoney = 0;
				double borrowBalanceDouble = 0d;
				try {
					// �ж�Ͷ�ʽ���Ƿ�Ϊ100��������
					investMoney = Integer.parseInt(investMoneyStr);
					if (investMoney == 0) {
						Util.toastLong(BidVIPActivity.this, "Ͷ�ʽ���Ϊ0");
					} else if (upMoney != 0 && investMoney % upMoney != 0) {
						Util.toastLong(BidVIPActivity.this, "Ͷ�ʽ�����Ϊ"+upMoney+"��������");
					}

					// �ж�Ͷ�ʽ���Ƿ���ڱ����ʣ���
					String borrowBalanceStr = String.valueOf(borrowBalanceTemp);
					borrowBalanceDouble = Double.parseDouble(borrowBalanceStr);
					if (investMoney > borrowBalanceDouble) {
						Util.toastLong(BidVIPActivity.this, "���ʣ���Ͷ����");
					}

				} catch (Exception e) {
				}

			}
		});
		deleteImg = (ImageView) findViewById(R.id.bid_vip_activity_delete);
		deleteImg.setOnClickListener(this);
		yjsyText = (TextView) findViewById(R.id.bid_vip_activity_yjsy);
		investBtn = (Button) findViewById(R.id.bid_vip_activity_borrow_bidBtn);
		investBtn.setOnClickListener(this);
		mainLayout = (LinearLayout) findViewById(R.id.bid_vip_activity_mainlayout);
	}

	/**
	 * ���ʣ���Ͷ���
	 * @param info
	 */
	float extraRateF = 0f;
	int borrowBalanceTemp = 0;
	private void initInvestBalance(ProductInfo info) {
		if (info == null) {
			return;
		}

		double totalMoneyL = 0d;
		int totalMoneyI = 0;
		double investMoneyL = 0d;
		int investMoneyI = 0;
		int borrowBalance = 0;
		try {
			totalMoneyL = Double.parseDouble(info.getTotal_money());
			investMoneyL = Double.parseDouble(info.getInvest_money());
			extraRateF = Float.parseFloat(info.getAndroid_interest_rate());
			totalMoneyI = (int) totalMoneyL;
			investMoneyI = (int) investMoneyL;
			borrowBalance = totalMoneyI - investMoneyI;
			borrowBalanceTemp = borrowBalance;
		} catch (Exception e) {
		}
		borrowBalanceTV.setText(Util.commaSpliteData(String
				.valueOf(borrowBalance)));
	}

	private OnTouchListener mOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				updateInvestCounter(v);
			} else if (event.getAction() == MotionEvent.ACTION_UP
					|| event.getAction() == MotionEvent.ACTION_CANCEL) {
				stopInvestCounter();
			}
			return false;
		}
	};

	private TextWatcher watcherInvestMoney = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String investMoneyStr = investMoneyET.getText().toString();
			int investMoney = 0;
			try {
				investMoney = Integer.parseInt(investMoneyStr);
				computeIncome(mProductInfo.getInterest_rate(),
						mProductInfo.getAndroid_interest_rate(), investMoney,
						mProductInfo.getInvest_horizon());
			} catch (Exception e) {
				yjsyText.setText("0.00");
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.bid_vip_activity_borrow_bidBtn:
			borrowInvest();
			break;
		case R.id.bid_vip_activity_recharge_btn:
			//ȥ��ֵ
			if(SettingsManager.isPersonalUser(getApplicationContext())){
				checkIsVerify("��ֵ");
			}else if(SettingsManager.isCompanyUser(getApplicationContext())){
				Intent intent = new Intent(BidVIPActivity.this,RechargeCompActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.bid_vip_activity_discend_btn:
			// �ݼ�
			investMoneyDescend();
			break;
		case R.id.bid_vip_activity_increase_btn:
			investMoneyIncrease();
			break;
		case R.id.bid_vip_activity_delete:
			resetInvestMoneyET();
			break;
		case R.id.bid_vip_activity_compact_text:
			//�鿴��ͬ
			Intent intent = new Intent(BidVIPActivity.this,CompactActivity.class);
			intent.putExtra("from_where", "vip");
			intent.putExtra("mProductInfo", mProductInfo);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	/**
	 * ��֤�û��Ƿ��Ѿ���֤
	 * @param type ����ֵ��,�����֡�
	 */
	private void checkIsVerify(final String type){
		rechargeBtn.setEnabled(false);
		RequestApis.requestIsVerify(BidVIPActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//�û��Ѿ�ʵ��
					checkIsBindCard(type);
				}else{
					//�û�û��ʵ��
					Intent intent = new Intent(BidVIPActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
					rechargeBtn.setEnabled(true);
				}
			}
			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}
	
	/**
	 * �ж��û��Ƿ��Ѿ���
	 * @param type "��ֵ����"
	 */
	private void checkIsBindCard(final String type){
		RequestApis.requestIsBinding(BidVIPActivity.this, SettingsManager.getUserId(getApplicationContext()), "����", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//�û��Ѿ���
					if("��ֵ".equals(type)){
						//��ôֱ��������ֵҳ��
						intent.setClass(BidVIPActivity.this, RechargeActivity.class);
					}
				}else{
					//�û���û�а�
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					intent.setClass(BidVIPActivity.this, BindCardActivity.class);
				}
				startActivity(intent);
				rechargeBtn.setEnabled(true);
			}
		});
	}


	private void borrowInvest() {
		bonusMoney = 0;
		String moneyStr = investMoneyET.getText().toString();
		double needRechargeMoeny = 0d;// ��Ҫ֧���Ľ�� ���� ����Ľ���ȥ����Ԫ��ҵĽ��
		double userBanlanceDouble = 0d;
		double borrowBalanceDouble = 0d;
		try {
			moneyInvest = Integer.parseInt(moneyStr);
		} catch (Exception e) {
			moneyInvest = 0;
		}

		needRechargeMoeny = moneyInvest;
		// �ж�Ͷ�ʽ���Ƿ�����˻����
		String userBanlance = userBalanceTV.getText().toString();
		userBanlanceDouble = Double.parseDouble(userBanlance);
		String borrowBalance = String.valueOf(borrowBalanceTemp);
		borrowBalanceDouble = Double.parseDouble(borrowBalance);
		if(borrowBalanceDouble < lowestMoney){
			//��Ŀ�Ͷ���С����Ͷ�������û�һ����Ͷ��
			if(moneyInvest < borrowBalanceDouble){
				//��ʾ�û���1����Ͷ��
				showVIPInvestDialog(borrowBalanceDouble);
			}else{
				if (needRechargeMoeny > userBanlanceDouble) {
					Util.toastLong(BidVIPActivity.this, "�˻�����");
				} else if (needRechargeMoeny > borrowBalanceDouble) {
					Util.toastLong(BidVIPActivity.this, "��Ŀ�Ͷ����");
				} else if(!compactCB.isChecked()){
					Util.toastLong(BidVIPActivity.this, "�����Ķ���ͬ����Э��");
				} else {
					showInvestDialog();
				}
			}
		}else{
			if (moneyInvest < lowestMoney) {
				Util.toastShort(BidVIPActivity.this, "Ͷ�ʽ���С��"+lowestMoney/10000+"��Ԫ");
			} else if (moneyInvest % upMoney != 0) {
				Util.toastLong(BidVIPActivity.this, "Ͷ�ʽ�����Ϊ"+upMoney+"��������");
			} else if (needRechargeMoeny > userBanlanceDouble) {
				Util.toastLong(BidVIPActivity.this, "�˻�����");
			} else if (needRechargeMoeny > borrowBalanceDouble) {
				Util.toastLong(BidVIPActivity.this, "��Ŀ�Ͷ����");
			} else if(!compactCB.isChecked()){
				Util.toastLong(BidVIPActivity.this, "�����Ķ���ͬ����Э��");
			} else {
				showInvestDialog();
			}
		}
	}

	private void showVIPInvestDialog(final double borrowBalanceDouble){
		View contentView = LayoutInflater.from(this).inflate(R.layout.common_popwindow, null);
		final Button sureBtn = (Button) contentView.findViewById(R.id.common_popwindow_btn);
		final TextView content = (TextView) contentView.findViewById(R.id.common_popwindow_content);
		content.setText("���ڱ�Ŀ�Ͷ���С��"+lowestMoney/10000+"��Ԫ������ȫ���Ϲ�ʣ��ɹ��ݶ");
		AlertDialog.Builder builder=new AlertDialog.Builder(this, R.style.Dialog_Transparent);  //�ȵõ�������  
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				investMoneyET.setText(String.valueOf((int)borrowBalanceDouble));
			}
		});
        //��������������ˣ���������ʾ����  
        dialog.show();  
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*6/7;
        dialog.getWindow().setAttributes(lp);
	}
	
	/**
	 * ȷ��Ͷ�ʵ�dialog
	 */
	private void showInvestDialog() {
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.invest_prompt_layout, null);
		Button sureBtn = (Button) contentView
				.findViewById(R.id.invest_prompt_layout_surebtn);
		Button cancelBtn = (Button) contentView
				.findViewById(R.id.invest_prompt_layout_cancelbtn);
		TextView totalMoney = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_total);
		LinearLayout detailLayout = (LinearLayout) contentView.findViewById(R.id.invest_prompt_yjb_layout_detail);
		detailLayout.setVisibility(View.GONE);
		totalMoney.setText(moneyInvest + "");

		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				R.style.Dialog_Transparent); // �ȵõ�������
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(REQUEST_INVEST_WHAT);
				dialog.dismiss();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// ��������������ˣ���������ʾ����
		dialog.show();
		// ����dialog�Ŀ���
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth() * 6 / 7;
		dialog.getWindow().setAttributes(lp);
	}

	/**
	 * �ݼ�
	 */
	private void investMoneyDescend() {
		String investMoneyStr = investMoneyET.getText().toString();
		int investMoneyInt = 0;
		try {
			investMoneyInt = Integer.parseInt(investMoneyStr);
		} catch (Exception e) {
		}
		if (investMoneyInt <= upMoney) {
			investMoneyInt = 0;
		} else {
			investMoneyInt -= upMoney;
		}
		if (investMoneyInt <= 0) {
			investMoneyET.setText(null);
		} else {
			investMoneyET.setText(investMoneyInt + "");
		}
		computeIncome(mProductInfo.getInterest_rate(),
				mProductInfo.getAndroid_interest_rate(), investMoneyInt,
				mProductInfo.getInvest_horizon());
	}

	/**
	 * ����
	 */
	private void investMoneyIncrease() {
		String investMoneyStr = investMoneyET.getText().toString();
		int investMoneyInt = 0;
		try {
			investMoneyInt = Integer.parseInt(investMoneyStr);
		} catch (Exception e) {
		}
		investMoneyInt += upMoney;
		investMoneyET.setText(investMoneyInt + "");
		computeIncome(mProductInfo.getInterest_rate(),
				mProductInfo.getAndroid_interest_rate(), investMoneyInt,
				mProductInfo.getInvest_horizon());
	}

	private void resetInvestMoneyET() {
		if (investMoneyET != null) {
			investMoneyET.setText(null);
			yjsyText.setText("0.00");
		}
	}

	private ScheduledExecutorService myExecuter;

	private void updateInvestCounter(final View v) {
		myExecuter = Executors.newSingleThreadScheduledExecutor();
		myExecuter.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				if (v.getId() == R.id.bid_zxd_activity_increase_btn) {
					handler.sendEmptyMessage(REQUEST_INVEST_INCREASE);
				} else if (v.getId() == R.id.bid_zxd_activity_discend_btn) {
					handler.sendEmptyMessage(REQUEST_INVEST_DESCEND);
				}
			}
		}, 200, 200, TimeUnit.MILLISECONDS);
	}

	public void stopInvestCounter() {
		if (myExecuter != null) {
			myExecuter.shutdownNow();
			myExecuter = null;
		}
	}

	/**
	 * �����껯�ʺ�Ͷ�ʽ���������
	 */
	private String computeIncome(String rateStr, String extraRateStr,
			int investMoney, String daysStr) {
		float rateF = 0f;
		float extraRateF = 0f;
		int days = 0;
		try {
			rateF = Float.parseFloat(rateStr);
			extraRateF = Float.parseFloat(extraRateStr);
			days = Integer.parseInt(daysStr.replace("��", ""));
		} catch (Exception e) {
		}
		float income = 0f;
		income = (rateF + extraRateF) * investMoney * days / 36500;
		DecimalFormat df = new java.text.DecimalFormat("#.00");
		if (income < 1) {
			yjsyText.setText("0" + df.format(income));
		} else {
			yjsyText.setText(df.format(income));
		}

		return df.format(income);
	}

	/**
	 * ��������Ͷ�ʽӿ�
	 * 
	 * @param borrowId
	 * @param investUserId
	 * @param money
	 * @param bonusMoney
	 *            Ԫ���
	 * @param investFrom
	 * @param investFromSub
	 * @param experienceCode
	 *            �������
	 * @param investFromHost
	 * @param merPriv
	 */
	private void requestInvest(String borrowId, String investUserId,
			String money, int bonusMoney, String investFrom,
			String investFromSub, String experienceCode, String investFromHost,
			String merPriv, String redBagLogId) {
		if (mLoadingDialog != null) {
			mLoadingDialog.show();
		}
		AsyncVipBorrowInvest asyncBorrowInvest = new AsyncVipBorrowInvest(
				BidVIPActivity.this, borrowId, investUserId, money, new OnBorrowInvestInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}

						if (baseInfo != null) {
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler.obtainMessage(REQUEST_INVEST_SUCCESS);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							} else {
								Message msg = handler.obtainMessage(REQUEST_INVEST_EXCEPTION);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							}
						} else {
							Message msg = handler.obtainMessage(REQUEST_INVEST_FAILE);
							handler.sendMessage(msg);
						}
					}
				});
		asyncBorrowInvest.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * �û��˻���Ϣ
	 */
	private void requestUserAccountInfo(String userId) {
		AsyncYiLianRMBAccount yilianTask = new AsyncYiLianRMBAccount(
				BidVIPActivity.this, userId, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								UserRMBAccountInfo info = baseInfo
										.getRmbAccountInfo();
								userBalanceTV.setText(info.getUse_money());
							}
						}
					}
				});
		yilianTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

}