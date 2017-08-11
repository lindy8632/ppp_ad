package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncYXBInvest;
import com.ylfcf.ppp.async.AsyncYXBProductLog;
import com.ylfcf.ppp.async.AsyncYXBUserAccount;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.entity.YXBProductLogInfo;
import com.ylfcf.ppp.entity.YXBUserAccountInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 元信宝---我要投资
 * 
 * @author Administrator
 * 
 */
public class BidYXBActivity extends BaseActivity implements OnClickListener {
	private static final String className = "BidYXBActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView borrowName;

	private TextView userBalanceTV;// 用户可用余额
	private TextView borrowBalanceTV;// 剩余募集额度
	private Button rechargeBtn;// 充值

	private Button descendBtn;// 递减按钮
	private Button increaseBtn;// 递增按钮
	private Button investBtn;// 投资
	private EditText investMoneyET;
	private ImageView deleteImg;// x号
	private TextView yjsyText;// 预计收益
	private TextView agreementText;// 服务协议
	private CheckBox cb;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	private double yxbInvestRate = 0.06;// 元信宝的年化收益
	private YXBUserAccountInfo yxbUserAccountInfo;
	private UserRMBAccountInfo userRMBAccountInfo;
	private YXBProductLogInfo yxbProductLogInfo;

	private float sunInvestMoney = 0;// 用户总共投的金额

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bid_yxb_activity);
		findViews();
		requestYXBProductLog("", sdf.format(new Date()));
		requestUserAccountInfo(SettingsManager
				.getUserId(getApplicationContext()));
		requestYxbUserAccount(SettingsManager
				.getUserId(getApplicationContext()));
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("认购");

		borrowName = (TextView) findViewById(R.id.bid_yxb_activity_borrow_name);
		borrowName.setText("元信宝（活期）");
		userBalanceTV = (TextView) findViewById(R.id.bid_yxb_activity_user_balance);
		borrowBalanceTV = (TextView) findViewById(R.id.bid_yxb_activity_borrow_balance);
		agreementText = (TextView) findViewById(R.id.bid_yxb_activity_agreement);
		agreementText.setOnClickListener(this);
		rechargeBtn = (Button) findViewById(R.id.bid_yxb_activity_recharge_btn);
		rechargeBtn.setOnClickListener(this);
		descendBtn = (Button) findViewById(R.id.bid_yxb_activity_discend_btn);
		descendBtn.setOnClickListener(this);
		increaseBtn = (Button) findViewById(R.id.bid_yxb_activity_increase_btn);
		increaseBtn.setOnClickListener(this);
		investMoneyET = (EditText) findViewById(R.id.bid_yxb_activity_invest_et);
		deleteImg = (ImageView) findViewById(R.id.bid_yxb_activity_delete);
		deleteImg.setOnClickListener(this);
		investBtn = (Button) findViewById(R.id.bid_yxb_activity_borrow_bidBtn);
		investBtn.setOnClickListener(this);
		yjsyText = (TextView) findViewById(R.id.bid_yxb_activity_yjsy);
		cb = (CheckBox) findViewById(R.id.bid_yxb_activity_cb);
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				String moneyA = investMoneyET.getText().toString();
				if (isChecked) {
					investBtn.setEnabled(true);
				} else {
					investBtn.setEnabled(false);
				}
			}
		});
		investMoneyET.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String moneyA = investMoneyET.getText().toString();
				if (cb.isChecked() && !moneyA.isEmpty()) {
					investBtn.setEnabled(true);
				} else {
					investBtn.setEnabled(false);
				}
				double moneyD = 0;
				try {
					moneyD = Double.parseDouble(moneyA);
					if (moneyD > 500000) {
						investMoneyET.setText("500000");
						Util.toastLong(BidYXBActivity.this, "每人最高认购额度为50W人民币");
					} else {
						int investMoney = 0;
						investMoney = Integer.parseInt(moneyA);
						computeIncome(yxbInvestRate, investMoney);
					}
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
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.bid_yxb_activity_recharge_btn:
			Intent intent = new Intent(BidYXBActivity.this,
					RechargeActivity.class);
			startActivity(intent);
			break;
		case R.id.bid_yxb_activity_discend_btn:
			// 递减
			investMoneyDescend();
			break;
		case R.id.bid_yxb_activity_increase_btn:
			// 递增
			investMoneyIncrease();
			break;
		case R.id.bid_yxb_activity_delete:
			resetInvestMoneyET();
			break;
		case R.id.bid_yxb_activity_borrow_bidBtn:
			// 元信宝认购
			checkYXBInvestData();
			break;
		case R.id.bid_yxb_activity_agreement:
			// 服务协议
			Intent intentAgreement = new Intent(BidYXBActivity.this,
					YXBAgreementActivity.class);
			startActivity(intentAgreement);
			break;
		default:
			break;
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

	/**
	 * 检查认购金额是否符合要求
	 */
	private void checkYXBInvestData() {
		int investMoneyInt = 0;
		String investMoneyStr = investMoneyET.getEditableText().toString();
		try {
			investMoneyInt = Integer.parseInt(investMoneyStr);
		} catch (Exception e) {
		}
		String sumInvestMoneyStr = null;// 用户已认购金额
		float userAccountBalance = 0;// 用户账户余额
		float needRaiseMoney = 0;// 剩余募集额度
		try {
			sumInvestMoneyStr = yxbUserAccountInfo.getSum_invest_money();
			sunInvestMoney = Float.parseFloat(sumInvestMoneyStr);
		} catch (Exception e) {
		}
		try {
			userAccountBalance = Float.parseFloat(userRMBAccountInfo
					.getUse_money());
		} catch (Exception e) {
		}
		try {
			needRaiseMoney = Float.parseFloat(yxbProductLogInfo
					.getNeed_raise_money());
		} catch (Exception e) {
		}
		if (investMoneyStr.isEmpty()) {
			Util.toastLong(BidYXBActivity.this, "请输入认购金额");
		} else if (investMoneyInt > userAccountBalance) {
			Util.toastLong(BidYXBActivity.this, "账户余额不足，请充值");
		} else if (investMoneyInt > (500000 - sunInvestMoney)) {
			Util.toastLong(BidYXBActivity.this, "认购超额，您当前的剩余认购额度为："
					+ (500000 - sunInvestMoney) + "元");
		} else if (investMoneyInt > needRaiseMoney) {
			Util.toastLong(BidYXBActivity.this, "剩余募集额度不足");
		} else if (investMoneyInt < 100) {
			Util.toastLong(BidYXBActivity.this, "认购金额不能少于100元");
		} else if (investMoneyInt % 100 != 0) {
			Util.toastLong(BidYXBActivity.this, "认购金额必须为100的整数倍");
		} else {
			showInvestDialog(investMoneyInt);
		}
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
		if (investMoneyInt <= 100) {
			investMoneyInt = 0;
		} else {
			investMoneyInt -= 100;
		}
		if (investMoneyInt <= 0) {
			investMoneyET.setText(null);
		} else {
			investMoneyET.setText(investMoneyInt + "");
		}
	}

	/**
	 * 递增
	 */
	private void investMoneyIncrease() {
		String investMoneyStr = investMoneyET.getText().toString();
		int investMoneyInt = 0;
		try {
			investMoneyInt = Integer.parseInt(investMoneyStr);
		} catch (Exception e) {
		}
		investMoneyInt += 100;
		investMoneyET.setText(investMoneyInt + "");
	}

	private void resetInvestMoneyET() {
		if (investMoneyET != null) {
			investMoneyET.setText(null);
			yjsyText.setText("0.00");
		}
	}

	/**
	 * 计算收益
	 * 
	 * @param rate
	 * @param investMoney
	 * 
	 */
	private void computeIncome(double rate, int investMoney) {
		double incomeDouble = rate / 365 * investMoney;
		DecimalFormat df = new java.text.DecimalFormat("#.00");
		df.setRoundingMode(RoundingMode.FLOOR);// 不四舍五入
		if (incomeDouble < 1) {
			yjsyText.setText("0" + df.format(incomeDouble));
		} else {
			yjsyText.setText(df.format(incomeDouble));
		}
	}

	/**
	 * 标的剩余可投金额
	 * 
	 * @param loginfo
	 */
	private void initBorrowBalance(YXBProductLogInfo loginfo) {
		String needRaiseMoney = loginfo.getNeed_raise_money();
		String raiseMoney = loginfo.getRaise_money();
		double raiseBalance = 0d;
		try {
			double needRaiseMoneyD = Double.parseDouble(needRaiseMoney);
			double raiseMoneyD = Double.parseDouble(raiseMoney);
			raiseBalance = needRaiseMoneyD - raiseMoneyD;
		} catch (Exception e) {
		}
		borrowBalanceTV.setText(Util.commaSpliteData(String
				.valueOf(raiseBalance)));
	}

	/**
	 * 确认认购的dialog
	 */
	private void showInvestDialog(final int investMoneyInt) {
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.invest_prompt_layout, null);
		LinearLayout detailLayout = (LinearLayout) contentView
				.findViewById(R.id.invest_prompt_yjb_layout_detail);
		detailLayout.setVisibility(View.GONE);
		Button sureBtn = (Button) contentView
				.findViewById(R.id.invest_prompt_layout_surebtn);
		Button cancelBtn = (Button) contentView
				.findViewById(R.id.invest_prompt_layout_cancelbtn);
		TextView totalMoney = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_total);
		TextView text1 = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_text1);
		text1.setText("您确认认购");
		totalMoney.setText(String.valueOf(investMoneyInt));
		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				R.style.Dialog_Transparent); // 先得到构造器
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (yxbProductLogInfo != null) {
					requestYxbInvest(yxbProductLogInfo.getProduct_id(),
							SettingsManager.getUserId(getApplicationContext()),
							String.valueOf(investMoneyInt));
				} else {
					requestYxbInvest("1",
							SettingsManager.getUserId(getApplicationContext()),
							String.valueOf(investMoneyInt));
				}
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
	 * 元信宝每日统计
	 * 
	 * @param id
	 * @param dateTime
	 */
	private void requestYXBProductLog(String id, String dateTime) {
		if (mLoadingDialog != null) {
			mLoadingDialog.show();
		}
		AsyncYXBProductLog productLogTask = new AsyncYXBProductLog(
				BidYXBActivity.this, id, dateTime, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								yxbProductLogInfo = baseInfo
										.getYxbProductLogInfo();
								initBorrowBalance(yxbProductLogInfo);
							}
						}
					}
				});
		productLogTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 用户账户信息
	 */
	private void requestUserAccountInfo(String userId) {
		AsyncYiLianRMBAccount yilianTask = new AsyncYiLianRMBAccount(
				BidYXBActivity.this, userId, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								userRMBAccountInfo = baseInfo
										.getRmbAccountInfo();
								userBalanceTV.setText(userRMBAccountInfo
										.getUse_money());
							}
						}
					}
				});
		yilianTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 元信宝投资接口
	 * 
	 * @param productId
	 * @param userId
	 * @param orderMoney
	 */
	private void requestYxbInvest(String productId, String userId,
			String orderMoney) {
		AsyncYXBInvest yxbInvestTask = new AsyncYXBInvest(BidYXBActivity.this,
				productId, userId, orderMoney, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Intent intent = new Intent(BidYXBActivity.this,
										YXBBidSuccessActivity.class);
								startActivity(intent);
								finish();
							} else {
								Util.toastLong(BidYXBActivity.this,
										baseInfo.getMsg());
							}
						}
					}
				});
		yxbInvestTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 元信宝的账户信息
	 * 
	 * @param userId
	 */
	private void requestYxbUserAccount(String userId) {
		AsyncYXBUserAccount yxbAccountTask = new AsyncYXBUserAccount(
				BidYXBActivity.this, userId, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								yxbUserAccountInfo = baseInfo
										.getYxbUserAccountInfo();
							}
						}
					}
				});
		yxbAccountTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
