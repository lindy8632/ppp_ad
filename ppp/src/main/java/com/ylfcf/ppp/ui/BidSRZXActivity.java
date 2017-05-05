package com.ylfcf.ppp.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncAppointBorrowInvest;
import com.ylfcf.ppp.async.AsyncCurrentUserRedbagList;
import com.ylfcf.ppp.async.AsyncVipBorrowInvest;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.RedBagInfo;
import com.ylfcf.ppp.entity.RedBagPageInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.inter.Inter.OnBorrowInvestInter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.ui.BidZXDActivity.OnHBWindowItemClickListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.view.HBListPopupwindow;
import com.ylfcf.ppp.widget.LoadingDialog;

/**
 * 私人尊享的投资页面
 * @author Mr.liu
 */
public class BidSRZXActivity extends BaseActivity implements OnClickListener{
	private static final int REQUEST_INVEST_WHAT = 1201;
	private static final int REQUEST_INVEST_SUCCESS = 1202;
	private static final int REQUEST_INVEST_EXCEPTION = 1203;
	private static final int REQUEST_INVEST_FAILE = 1204;

	private static final int REQUEST_INVEST_INCREASE = 1205;
	private static final int REQUEST_INVEST_DESCEND = 1206;

	private LinearLayout topLeftBtn;
	private TextView topTitleTV, borrowName;
	private TextView userBalanceTV;// 用户可用余额
	private TextView borrowBalanceTV;// 标的剩余可投金额
	private Button rechargeBtn;// 充值

	private Button descendBtn;// 递减按钮
	private Button increaseBtn;// 递增按钮
	private EditText investMoneyET;
	private ImageView deleteImg;// x号
	private TextView yjsyText;// 预计收益
	private CheckBox compactCB;
	private TextView vipCompact;//VIP借款协议
	private LinearLayout hbLayout;//红包布局
	private EditText hbET;//红包输入框
	private RelativeLayout hbArrowLayout;
	private View line1,line2;

	private ProductInfo mProductInfo;
	private int moneyInvest = 0;
	private int hbMoney = 0;
	private Button investBtn;
	private LoadingDialog loadingDialog;
	private List<RedBagInfo> hbList = new ArrayList<RedBagInfo>();// 用户未使用的红包列表

	private LinearLayout mainLayout;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_INVEST_WHAT:
				requestInvest(mProductInfo.getId(),
						SettingsManager.getUserId(getApplicationContext()),
						String.valueOf(moneyInvest), 
						SettingsManager.USER_FROM,String.valueOf(hbET.getTag()));
				break;
			case REQUEST_INVEST_SUCCESS:
				Intent intentSuccess = new Intent(BidSRZXActivity.this,
						BidSuccessActivity.class);
				intentSuccess.putExtra("from_where", "私人尊享");
				startActivity(intentSuccess);
				mApp.finishAllActivityExceptMain();
				break;
			case REQUEST_INVEST_EXCEPTION:
				BaseInfo base = (BaseInfo) msg.obj;
				Util.toastShort(BidSRZXActivity.this, base.getMsg());
				break;
			case REQUEST_INVEST_FAILE:
				Util.toastShort(BidSRZXActivity.this, "网络异常");
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
		setContentView(R.layout.bid_srzx_activity);

		mApp.addActivity(this);
		loadingDialog = new LoadingDialog(BidSRZXActivity.this, "正在加载...",
				R.anim.loading);
		mProductInfo = (ProductInfo) getIntent().getSerializableExtra(
				"PRODUCT_INFO");
		if(mProductInfo != null){
			requestHBPageInfoByBorrowType(
					SettingsManager.getUserId(getApplicationContext()),"私人尊享");
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
		mApp.removeActivity(this);
		handler.removeCallbacksAndMessages(null);
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("投资");

		borrowName = (TextView) findViewById(R.id.bid_srzx_activity_borrow_name);
		borrowName.setText(mProductInfo.getBorrow_name());
		userBalanceTV = (TextView) findViewById(R.id.bid_srzx_activity_user_balance);
		borrowBalanceTV = (TextView) findViewById(R.id.bid_srzx_activity_borrow_balance);
		rechargeBtn = (Button) findViewById(R.id.bid_srzx_activity_recharge_btn);
		rechargeBtn.setOnClickListener(this);
		descendBtn = (Button) findViewById(R.id.bid_srzx_activity_discend_btn);
		descendBtn.setOnClickListener(this);
		descendBtn.setOnTouchListener(mOnTouchListener);
		increaseBtn = (Button) findViewById(R.id.bid_srzx_activity_increase_btn);
		increaseBtn.setOnClickListener(this);
		increaseBtn.setOnTouchListener(mOnTouchListener);
		compactCB = (CheckBox) findViewById(R.id.bid_srzx_activity_cb);
		vipCompact = (TextView) findViewById(R.id.bid_srzx_activity_compact_text);
		vipCompact.setOnClickListener(this);
		investMoneyET = (EditText) findViewById(R.id.bid_srzx_activity_invest_et);
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
					// 判断投资金额是否为100的整数倍
					investMoney = Integer.parseInt(investMoneyStr);
					// 判断投资金额是否大于标的所剩额度
					String borrowBalanceStr = String.valueOf(borrowBalanceTemp);
					borrowBalanceDouble = Double.parseDouble(borrowBalanceStr);
					if (investMoney > borrowBalanceDouble) {
						Util.toastLong(BidSRZXActivity.this, "标的剩余可投金额不足");
					}else if(investMoney < borrowBalanceDouble){
						Util.toastLong(BidSRZXActivity.this, "投标金额不得小于起投金额");
					}
				} catch (Exception e) {
				}

			}
		});
		deleteImg = (ImageView) findViewById(R.id.bid_srzx_activity_delete);
		deleteImg.setOnClickListener(this);
		yjsyText = (TextView) findViewById(R.id.bid_srzx_activity_yjsy);
		investBtn = (Button) findViewById(R.id.bid_srzx_activity_borrow_bidBtn);
		investBtn.setOnClickListener(this);
		mainLayout = (LinearLayout) findViewById(R.id.bid_srzx_activity_mainlayout);
		
		hbLayout = (LinearLayout) findViewById(R.id.bid_srzx_activity_hb_layout);
		hbET = (EditText) findViewById(R.id.bid_srzx_activity_hb_et);
		hbArrowLayout = (RelativeLayout) findViewById(R.id.bid_srzx_activity_hb_arrow_layout);
		hbArrowLayout.setOnClickListener(this);
		line1 = findViewById(R.id.bid_srzx_activity_line1);
		line2 = findViewById(R.id.bid_srzx_activity_line2);
	}

	/**
	 * 标的剩余可投金额
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
			totalMoneyI = (int) totalMoneyL;
			investMoneyI = (int) investMoneyL;
			borrowBalance = totalMoneyI - investMoneyI;
			borrowBalanceTemp = borrowBalance;
		} catch (Exception e) {
		}
		try {
			extraRateF = Float.parseFloat(info.getAndroid_interest_rate());
		} catch (Exception e) {
		}
		borrowBalanceTV.setText(Util.commaSpliteData(String
				.valueOf(borrowBalance)));
		//默认是标的剩余可投金额
		investMoneyET.setText(String
				.valueOf(borrowBalance));
		computeIncome(mProductInfo.getInterest_rate(),
				mProductInfo.getAndroid_interest_rate(), borrowBalance,
				mProductInfo.getInvest_period());
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
						mProductInfo.getInvest_period());
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
		case R.id.bid_srzx_activity_borrow_bidBtn:
			borrowInvest();
			break;
		case R.id.bid_srzx_activity_recharge_btn:
			//去充值
			if(SettingsManager.isPersonalUser(getApplicationContext())){
				checkIsVerify("充值");
			}else if(SettingsManager.isCompanyUser(getApplicationContext())){
				Intent intent = new Intent(BidSRZXActivity.this,RechargeCompActivity.class);
				startActivity(intent);
				
			}
			break;
		case R.id.bid_srzx_activity_discend_btn:
			// 递减
			investMoneyDescend();
			break;
		case R.id.bid_srzx_activity_increase_btn:
			investMoneyIncrease();
			break;
		case R.id.bid_srzx_activity_delete:
			resetInvestMoneyET();
			break;
		case R.id.bid_srzx_activity_compact_text:
			//查看合同
			Intent intent = new Intent(BidSRZXActivity.this,CompactActivity.class);
			intent.putExtra("from_where", "srzx");
			intent.putExtra("mProductInfo", mProductInfo);
			startActivity(intent);
			break;
		case R.id.bid_srzx_activity_hb_layout:
		case R.id.bid_srzx_activity_hb_arrow_layout:
			checkHB();
			break;
		default:
			break;
		}
	}
	
	private void checkHB() {
		showHBListWindow();
	}
	
	/**
	 * 红包
	 */
	private void showHBListWindow() {
		if (hbList == null || hbList.size() < 1) {
			Util.toastLong(BidSRZXActivity.this, "没有红包可用");
			return;
		}
		View popView = LayoutInflater.from(this).inflate(
				R.layout.tyj_list_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(BidSRZXActivity.this);
		int width = screen[0];
		int height = screen[1] * 1 / 3;
		HBListPopupwindow popwindow = new HBListPopupwindow(
				BidSRZXActivity.this, popView, width, height,"请选择红包");
		popwindow.show(mainLayout, hbList, new OnHBWindowItemClickListener() {
			@Override
			public void onItemClickListener(View view, int position) {
				if (position == 0) {
					hbET.setText("");
					hbET.setTag("");
					hbArrowLayout.setTag("0");
				} else {
					RedBagInfo info = hbList.get(position - 1);
					String moneyStr = investMoneyET.getText().toString();
					int investMoney = 0;//输入框中输入的投资金额
					int limitMoney = 0;//需要投资的金额
					try {
						investMoney = Integer.parseInt(moneyStr);
					} catch (Exception e) {
					}
					try {
						limitMoney = Integer.parseInt(info.getNeed_invest_money());
					} catch (Exception e) {
					}
					if(investMoney < limitMoney){
						hbET.setText("");
						Util.toastLong(BidSRZXActivity.this, "投资金额尚未达到"+info.getMoney()+"元红包的单笔投资金额要求");
					}else{
						hbET.setText(info.getMoney()+"元红包，"+"需投资"+info.getNeed_invest_money()+"元及以上可用");
						hbET.setTag(info.getId());
						hbArrowLayout.setTag(info.getMoney());
					}
				}
			}
		});
	}
	
	/**
	 * 验证用户是否已经认证
	 * @param type “充值”,“提现”
	 */
	private void checkIsVerify(final String type){
		rechargeBtn.setEnabled(false);
		RequestApis.requestIsVerify(BidSRZXActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//用户已经实名
					checkIsBindCard(type);
				}else{
					//用户没有实名
					Intent intent = new Intent(BidSRZXActivity.this,UserVerifyActivity.class);
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
	 * 判断用户是否已经绑卡
	 * @param type "充值提现"
	 */
	private void checkIsBindCard(final String type){
		RequestApis.requestIsBinding(BidSRZXActivity.this, SettingsManager.getUserId(getApplicationContext()), "宝付", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//用户已经绑卡
					if("充值".equals(type)){
						//那么直接跳到充值页面
						intent.setClass(BidSRZXActivity.this, RechargeActivity.class);
					}
				}else{
					//用户还没有绑卡
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					intent.setClass(BidSRZXActivity.this, BindCardActivity.class);
				}
				startActivity(intent);
				rechargeBtn.setEnabled(true);
			}
		});
	}

	//投资前的判断
	private void borrowInvest() {
		String moneyStr = investMoneyET.getText().toString();
		double needRechargeMoeny = 0d;// 需要支付的金额
		double userBanlanceDouble = 0d;//用户账户余额
		double borrowBalanceDouble = 0d;//标的剩余可投金额
		try {
			moneyInvest = Integer.parseInt(moneyStr);
		} catch (Exception e) {
		}
		try {
			hbMoney = Integer.parseInt(hbArrowLayout.getTag().toString());
		} catch (Exception e) {
		}
		needRechargeMoeny = moneyInvest;
		// 判断投资金额是否大于账户余额
		String userBanlance = userBalanceTV.getText().toString();
		try {
			userBanlanceDouble = Double.parseDouble(userBanlance);
		} catch (Exception e) {
		}
		String borrowBalance = String.valueOf(borrowBalanceTemp);
		try {
			borrowBalanceDouble = Double.parseDouble(borrowBalance);
		} catch (Exception e) {
		}
		// 标的可投金额小于30W，则需用户一次性投完
		if (moneyInvest < borrowBalanceDouble) {
			// 提示用户须1次性投完
			Util.toastLong(BidSRZXActivity.this, "投标金额不得小于起投金额");
		} else if (moneyInvest > borrowBalanceDouble) {
			Util.toastLong(BidSRZXActivity.this, "标的剩余可投金额不足");
		} else if (!compactCB.isChecked()) {
			Util.toastLong(BidSRZXActivity.this, "请先阅读并同意借款协议");
		} else {
			showInvestDialog();
		}
	}

	/**
	 * 确认投资的dialog
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
		RelativeLayout hbLayout = (RelativeLayout) contentView.findViewById(R.id.invest_prompt_hb_layout_detail);//红包布局
		TextView hbMoneyTV = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_hb_count);
		detailLayout.setVisibility(View.GONE);
		if(hbMoney > 0){
			hbLayout.setVisibility(View.VISIBLE);
			hbMoneyTV.setText(hbMoney + "元红包");
		}else{
			hbLayout.setVisibility(View.GONE);
		}
		totalMoney.setText(moneyInvest + "");

		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				R.style.Dialog_Transparent); // 先得到构造器
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
		// 参数都设置完成了，创建并显示出来
		dialog.show();
		// 设置dialog的宽度
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth() * 6 / 7;
		dialog.getWindow().setAttributes(lp);
	}

	/**
	 * 递减
	 */
	private void investMoneyDescend() {
		String investMoneyStr = investMoneyET.getText().toString();
		int investMoneyInt = 0;
		try {
			investMoneyInt = Integer.parseInt(investMoneyStr);
		} catch (Exception e) {
		}
		if (investMoneyInt >= 0) {
			investMoneyInt = 0;
		} 
		if (investMoneyInt <= 0) {
			investMoneyET.setText(null);
		} else {
			investMoneyET.setText(investMoneyInt + "");
		}
		computeIncome(mProductInfo.getInterest_rate(),
				mProductInfo.getAndroid_interest_rate(), investMoneyInt,
				mProductInfo.getInvest_period());
	}

	/**
	 * 递增
	 */
	private void investMoneyIncrease() {
		double totalMoneyL = 0d;
		int totalMoneyI = 0;
		double investMoneyL = 0d;
		int investMoneyI = 0;
		int borrowBalance = 0;
		try {
			totalMoneyL = Double.parseDouble(mProductInfo.getTotal_money());
			investMoneyL = Double.parseDouble(mProductInfo.getInvest_money());
			totalMoneyI = (int) totalMoneyL;
			investMoneyI = (int) investMoneyL;
			borrowBalance = totalMoneyI - investMoneyI;
		} catch (Exception e) {
		}
		investMoneyET.setText(borrowBalance + "");
		computeIncome(mProductInfo.getInterest_rate(),
				mProductInfo.getAndroid_interest_rate(), borrowBalance,
				mProductInfo.getInvest_period());
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
	 * 根据年化率和投资金额计算收益
	 */
	private String computeIncome(String rateStr, String extraRateStr,
			int investMoney, String daysStr) {
		float rateF = 0f;
		float extraRateF = 0f;
		int days = 0;
		try {
			rateF = Float.parseFloat(rateStr);
			days = Integer.parseInt(daysStr);
		} catch (Exception e) {
		}
		try {
			extraRateF = Float.parseFloat(extraRateStr);
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
	 * 请求立即投资接口
	 * 
	 * @param borrowId
	 * @param investUserId
	 * @param money
	 * @param bonusMoney
	 *            元金币
	 * @param investFrom
	 * @param investFromSub
	 * @param experienceCode
	 *            体验金编号
	 * @param investFromHost
	 * @param merPriv
	 */
	private void requestInvest(String borrowId, String investUserId,
			String money, String investFrom,String redbagId) {
		if (loadingDialog != null) {
			loadingDialog.show();
		}
		AsyncAppointBorrowInvest asyncBorrowInvest = new AsyncAppointBorrowInvest(
				BidSRZXActivity.this, borrowId, investUserId, money,investFrom,redbagId, new OnBorrowInvestInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (loadingDialog != null && loadingDialog.isShowing()) {
							loadingDialog.dismiss();
						}

						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
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
	 * 获取当前用户的可用红包的列表
	 * 
	 * @param userId
	 * @param borrowType
	 */
	private void requestHBPageInfoByBorrowType(String userId, String borrowType) {
		if(loadingDialog != null){
			loadingDialog.show();
		}
		AsyncCurrentUserRedbagList currentUserRedbagListTask = new AsyncCurrentUserRedbagList(
				BidSRZXActivity.this, userId, borrowType, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(loadingDialog != null && loadingDialog.isShowing()){
							loadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								RedBagPageInfo pageInfo = baseInfo
										.getmRedBagPageInfo();
								if (pageInfo != null) {
									hbList.addAll(pageInfo.getRedbagList());
								}
								if (hbList.size() < 1) {
									hbLayout.setVisibility(View.GONE);
									line1.setVisibility(View.GONE);
									line2.setVisibility(View.GONE);
								} else {
//									if (extraRateF <= 0) {
										hbLayout.setVisibility(View.VISIBLE);
										line1.setVisibility(View.VISIBLE);
										line2.setVisibility(View.VISIBLE);
//									} else {
//										hbLayout.setVisibility(View.GONE);
//										line1.setVisibility(View.GONE);
//										line2.setVisibility(View.GONE);
//									}
								}
							} else {
								hbLayout.setVisibility(View.GONE);
								line1.setVisibility(View.GONE);
								line2.setVisibility(View.GONE);
							}
						}else{
							hbLayout.setVisibility(View.GONE);
							line1.setVisibility(View.GONE);
							line2.setVisibility(View.GONE);
						}
					}
				});
		currentUserRedbagListTask
				.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 用户账户信息
	 */
	private void requestUserAccountInfo(String userId) {
		AsyncYiLianRMBAccount yilianTask = new AsyncYiLianRMBAccount(
				BidSRZXActivity.this, userId, new OnCommonInter() {
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
