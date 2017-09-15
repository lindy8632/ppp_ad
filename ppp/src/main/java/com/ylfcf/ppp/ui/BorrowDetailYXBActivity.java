package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncYXBProduct;
import com.ylfcf.ppp.async.AsyncYXBProductLog;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.YXBProductInfo;
import com.ylfcf.ppp.entity.YXBProductLogInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 元信宝投资详情页面
 * 
 * @author Administrator
 */
public class BorrowDetailYXBActivity extends BaseActivity implements
		OnClickListener {
	private static final String className = "BorrowDetailYXBActivity";
	private static final int REQUEST_PRODUCT_WHAT = 2301;// 产品信息
	private static final int REQUEST_PRODUCT_SUCCESS = 2302;
	private static final int REQUEST_PRODUCTLOG_WHAT = 2303;// 产品每日统计
	private static final int REQUEST_PRODUCTLOG_SUCCESS = 2304;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private ImageView rightImage;

	private LinearLayout xmjsLayout;// 项目介绍
	private LinearLayout cpysLayout;// 产品要素
	private LinearLayout cjwtLayout;// 常见问题
	private Button buyBtn;// 立即认购

	private TextView raiseMoneyText;// 当日剩余募集额度
	private TextView yearRateText;// 预期年化收益
	private TextView applyWithdrawMoneyText;// 今日剩余可赎回额度
	private TextView maxInvestMoneyText;// 最高认购额度
	private YXBProductInfo yxbProductInfo;
	private YXBProductLogInfo yxbProductLogInfo;
	private AlertDialog.Builder builder = null; // 先得到构造器
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_PRODUCT_WHAT:
				requestYXBProduct("", "已发布");
				break;
			case REQUEST_PRODUCT_SUCCESS:
				handler.sendEmptyMessage(REQUEST_PRODUCTLOG_WHAT);
				break;
			case REQUEST_PRODUCTLOG_WHAT:
				String dateTime = sdf.format(new Date());
				requestYXBProductLog("", dateTime);
				break;
			case REQUEST_PRODUCTLOG_SUCCESS:
				initYXBData();
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
		setContentView(R.layout.borrow_details_yxb_activity);
		builder = new AlertDialog.Builder(BorrowDetailYXBActivity.this); // 先得到构造器
		findViews();
		handler.sendEmptyMessage(REQUEST_PRODUCT_WHAT);
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("元信宝");
		rightImage = (ImageView) findViewById(R.id.common_topbar_right);
		rightImage.setVisibility(View.VISIBLE);
		rightImage.setOnClickListener(this);

		xmjsLayout = (LinearLayout) findViewById(R.id.borrow_details_yxb_xmjs_layout);
		xmjsLayout.setOnClickListener(this);
		cpysLayout = (LinearLayout) findViewById(R.id.borrow_details_yxb_cpys_layout);
		cpysLayout.setOnClickListener(this);
		cjwtLayout = (LinearLayout) findViewById(R.id.borrow_details_yxb_cjwt_layout);
		cjwtLayout.setOnClickListener(this);
		buyBtn = (Button) findViewById(R.id.borrow_details_yxb_activity_btn);
		buyBtn.setOnClickListener(this);

		raiseMoneyText = (TextView) findViewById(R.id.borrow_details_yxb_activity_raisemoney);
		yearRateText = (TextView) findViewById(R.id.borrow_details_yxb_activity_year_rate);
		applyWithdrawMoneyText = (TextView) findViewById(R.id.borrow_details_yxb_activity_apply_withdraw_money);
		maxInvestMoneyText = (TextView) findViewById(R.id.borrow_details_yxb_activity_max_invest_money);
	}

	private void initYXBData() {
		if (yxbProductInfo != null && yxbProductLogInfo != null) {
			DecimalFormat df = new DecimalFormat("#.00");
			String needRaiseMoney = yxbProductLogInfo.getNeed_raise_money();
			String raiseMoney = yxbProductLogInfo.getRaise_money();
			double raiseBalance = 0d;
			try {
				double needRaiseMoneyD = Double.parseDouble(needRaiseMoney);
				double raiseMoneyD = Double.parseDouble(raiseMoney);
				raiseBalance = needRaiseMoneyD - raiseMoneyD;
			} catch (Exception e) {
			}
			raiseMoneyText.setText(Util.commaSpliteData(String
					.valueOf(raiseBalance)));
			maxInvestMoneyText.setText(yxbProductInfo.getMax_invest_money()
					+ "元");

			String yearInterest = yxbProductInfo.getYear_get_interest();// 年化收益
			try {
				double yearInterestD = Double.parseDouble(yearInterest);
				yearRateText.setText(df.format(yearInterestD) + "%");
			} catch (Exception e) {
				yearRateText.setText("6.00%");
			}
			try {
				double needApplyWithD = Double.parseDouble(yxbProductLogInfo
						.getNeed_apply_withdraw_money());
				double applyWithD = Double.parseDouble(yxbProductLogInfo
						.getApply_withdraw_money());
				applyWithdrawMoneyText.setText(df.format(needApplyWithD
						- applyWithD)
						+ "元");
			} catch (Exception e) {
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//友盟统计页面跳转
		UMengStatistics.statisticsResume(this);//友盟统计时长
		if (handler != null) {
			handler.sendEmptyMessage(REQUEST_PRODUCT_WHAT);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);//友盟统计页面跳转
		UMengStatistics.statisticsPause(this);//友盟统计时长
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
			finish();
			break;
		case R.id.common_topbar_right:
			// 元信宝介绍
			Intent intentYxbIntro = new Intent(BorrowDetailYXBActivity.this,
					YXBIntroActivity.class);
			startActivity(intentYxbIntro);
			break;
		case R.id.borrow_details_yxb_xmjs_layout:
			Intent intentYXBPro = new Intent(BorrowDetailYXBActivity.this,
					YXBProjectIntroActivity.class);
			intentYXBPro.putExtra("yxb_project_flag", "yxb_xmjs");
			startActivity(intentYXBPro);
			break;
		case R.id.borrow_details_yxb_cpys_layout:
			Intent intentYXBEle = new Intent(BorrowDetailYXBActivity.this,
					YXBProjectIntroActivity.class);
			intentYXBEle.putExtra("yxb_project_flag", "yxb_cpys");
			startActivity(intentYXBEle);
			break;
		case R.id.borrow_details_yxb_cjwt_layout:
			Intent intentYXBQues = new Intent(BorrowDetailYXBActivity.this,
					YXBProjectIntroActivity.class);
			intentYXBQues.putExtra("yxb_project_flag", "yxb_cjwt");
			startActivity(intentYXBQues);
			break;
		case R.id.borrow_details_yxb_activity_btn:
			// 从SettingsManager中读取密码，如果为空意味着没有登录。
			if (Util.isYXBENabled()) {
				boolean isLogin = !SettingsManager.getLoginPassword(
						BorrowDetailYXBActivity.this).isEmpty()
						&& !SettingsManager.getUser(
								BorrowDetailYXBActivity.this).isEmpty();
				Intent intentYXBBid = new Intent();
				// 1、检测是否已经登录
				if (isLogin) {
					// 已经登录，跳转到购买页面
					intentYXBBid.setClass(BorrowDetailYXBActivity.this,
							BidYXBActivity.class);
				} else {
					// 未登录，跳转到登录页面
					intentYXBBid.setClass(BorrowDetailYXBActivity.this,
							LoginActivity.class);
				}
				startActivity(intentYXBBid);
			} else {
				showYXBRedeemErrorDialog("每日的23:00到次日1:00是系统批处理时间，此时段将不对外开放元信宝的“认购”和“赎回”交易。");
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 元信宝维护时间
	 * 
	 * @param msg
	 */
	private void showYXBRedeemErrorDialog(String msg) {
		View contentView = LayoutInflater.from(BorrowDetailYXBActivity.this)
				.inflate(R.layout.yxb_redeem_error_dialog, null);
		final Button sureBtn = (Button) contentView
				.findViewById(R.id.yxb_redeem_error_dialog_btn);
		final TextView errorText = (TextView) contentView
				.findViewById(R.id.yxb_redeem_error_dialog_reason);
		final TextView titleText = (TextView) contentView
				.findViewById(R.id.yxb_redeem_error_dialog_title);
		titleText.setText("认购失败");
		errorText.setText(msg);
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		sureBtn.setOnClickListener(new OnClickListener() {
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
	 * 元信宝产品
	 * 
	 * @param id
	 * @param status
	 */
	private void requestYXBProduct(String id, String status) {
		if (mLoadingDialog != null && !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncYXBProduct productTask = new AsyncYXBProduct(
				BorrowDetailYXBActivity.this, id, status, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								yxbProductInfo = baseInfo.getYxbProductInfo();
								handler.sendEmptyMessage(REQUEST_PRODUCT_SUCCESS);
							} else {
								mLoadingDialog.dismiss();
							}
						} else {
							mLoadingDialog.dismiss();
						}
					}
				});
		productTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 元信宝每日统计
	 * 
	 * @param id
	 * @param dateTime
	 */
	private void requestYXBProductLog(String id, String dateTime) {
		AsyncYXBProductLog productLogTask = new AsyncYXBProductLog(
				BorrowDetailYXBActivity.this, id, dateTime,
				new OnCommonInter() {
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
								handler.sendEmptyMessage(REQUEST_PRODUCTLOG_SUCCESS);
							}
						}
					}
				});
		productLogTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
