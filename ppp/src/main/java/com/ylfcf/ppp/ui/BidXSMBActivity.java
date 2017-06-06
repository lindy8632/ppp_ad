package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncXSMBDetail;
import com.ylfcf.ppp.async.AsyncXSMBInvest;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 限时秒标
 * @author Mr.liu
 *
 */
public class BidXSMBActivity extends BaseActivity implements OnClickListener{
	private static final int REQUEST_USER_ACCOUNT_WHAT = 8291;//个人账户
	private static final int REQUEST_XSMB_WHAT = 8292;//请求限时秒标详情
	private static final int REQUEST_XSMB_INVEST_WHAT = 8293;//限时秒标投资接口
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private TextView borrowName;
	private TextView borrowRate;//年化收益
	private TextView borrowPeriod;//期限
	private TextView useBalanceTV;//用户可用余额
	private Button rechargeBtn;
	private EditText investMoenyET;//投资金额的输入框
	private TextView nhllTV;//享年化利率
	private TextView yqsyTV;//预期收益
	private Button bidBtn;//立即秒杀
	private CheckBox cb;
	private TextView compactTV;//借款协议
	
	private ProductInfo mProductInfo;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_USER_ACCOUNT_WHAT:
				requestUserAccountInfo(SettingsManager.getUserId(getApplicationContext()));
				break;
			case REQUEST_XSMB_WHAT:
				//限时秒标
				requestXSMBDetails("发布");
				break;
			case REQUEST_XSMB_INVEST_WHAT:
				//限时秒标投资接口
				requestXSMBInvest(mProductInfo.getId(),mProductInfo.getSingle_invest_money(),
						SettingsManager.getUserId(getApplicationContext()),SettingsManager.USER_FROM);
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
		setContentView(R.layout.bid_xsmb_activity);
		mProductInfo = (ProductInfo) getIntent().getSerializableExtra(
				"PRODUCT_INFO");
		findViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		handler.sendEmptyMessage(REQUEST_USER_ACCOUNT_WHAT);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("投资");
		
		borrowName = (TextView) findViewById(R.id.bid_xsmb_activity_borrow_name);
		borrowRate = (TextView) findViewById(R.id.bid_xsmb_activity_rate);
		borrowPeriod = (TextView) findViewById(R.id.bid_xsmb_activity_peroid);
		useBalanceTV = (TextView) findViewById(R.id.bid_xsmb_activity_user_balance);
		rechargeBtn = (Button) findViewById(R.id.bid_xsmb_activity_recharge_btn);
		rechargeBtn.setOnClickListener(this);
		investMoenyET = (EditText) findViewById(R.id.bid_xsmb_activity_invest_et);
		nhllTV = (TextView) findViewById(R.id.bid_xsmb_activity_nhsy);
		yqsyTV = (TextView) findViewById(R.id.bid_xsmb_activity_yjsy);
		bidBtn = (Button) findViewById(R.id.bid_xsmb_activity_borrow_bidBtn);
		bidBtn.setOnClickListener(this);
		cb = (CheckBox) findViewById(R.id.bid_xsmb_activity_cb);
		compactTV = (TextView) findViewById(R.id.bid_xsmb_activity_compact_text);
		compactTV.setOnClickListener(this);
		
		initViewData();
	}

	private void initViewData(){
		if(mProductInfo == null)
			return;
		borrowName.setText(mProductInfo.getBorrow_name());
		borrowPeriod.setText(mProductInfo.getInterest_period());
		double rateD = 0d;
		try {
			rateD = Double.parseDouble(mProductInfo.getInterest_rate());
			if((int)(rateD * 10)%10 == 0){
				//说明利率是整数，没有小数
				borrowRate.setText((int)rateD + "");
				nhllTV.setText((int)rateD + "%");
			}else{
				borrowRate.setText(Util.double2PointDoubleOne(rateD));
				nhllTV.setText(Util.double2PointDoubleOne(rateD)+"%");
			}
		} catch (Exception e) {
			borrowRate.setText(mProductInfo.getInterest_rate());
			nhllTV.setText(mProductInfo.getInterest_rate()+"%");
		}
		try {
			double singleInvestMoneyD = Double.parseDouble(mProductInfo.getSingle_invest_money());
			if(singleInvestMoneyD > 0){
				investMoenyET.setText(mProductInfo.getSingle_invest_money());
			}else{
				investMoenyET.setText("2000");
			}
		} catch (Exception e) {
			investMoenyET.setText("2000");
		}
		computeIncome(mProductInfo.getInterest_rate(), "0.00", mProductInfo.getSingle_invest_money(), mProductInfo.getInterest_period());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.bid_xsmb_activity_recharge_btn:
			//去充值
			if(SettingsManager.isPersonalUser(getApplicationContext())){
				checkIsVerify("充值");
			}else if(SettingsManager.isCompanyUser(getApplicationContext())){
				Intent intent = new Intent(BidXSMBActivity.this,RechargeCompActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.bid_xsmb_activity_compact_text:
			//借款协议
			Intent intent = new Intent(BidXSMBActivity.this,CompactActivity.class);
			intent.putExtra("mProductInfo", mProductInfo);
			intent.putExtra("from_where", "xsmb");
			startActivity(intent);
			break;
		case R.id.bid_xsmb_activity_borrow_bidBtn:
			//立即秒杀
			handler.sendEmptyMessage(REQUEST_XSMB_WHAT);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 根据年化率和投资金额计算收益
	 */
	private String computeIncome(String rateStr, String extraRateStr,
			String investMoney, String daysStr) {
		float rateF = 0f;
		float extraRateF = 0f;
		int days = 0;
		int investMoneyInt = 0;
		try {
			rateF = Float.parseFloat(rateStr);
			extraRateF = Float.parseFloat(extraRateStr);
		} catch (Exception e) {
		}
		try {
			days = Integer.parseInt(daysStr);
		} catch (Exception e) {
		}
		try {
			investMoneyInt = Integer.parseInt(investMoney);
		} catch (Exception e) {
			investMoneyInt = 2000;
		}
		float income = 0f;
		income = (rateF + extraRateF) * investMoneyInt * days / 36500;
		DecimalFormat df = new java.text.DecimalFormat("#.00");
		if (income < 1) {
			yqsyTV.setText("0" + df.format(income));
		} else {
			yqsyTV.setText(df.format(income));
		}

		return df.format(income);
	}
	
	/**
	 * 判断是否可以投资
	 * @param info
	 */
	long remainTimeL = 0l;
	private void checkInvest(ProductInfo productInfo){
		if(productInfo != null && "未满标".equals(productInfo.getMoney_status())){
//			handler.sendEmptyMessage(REQUEST_XSMB_INVEST_WHAT);
			String nowTimeStr = productInfo.getNow_time();
			String willStartTimeStr = productInfo.getWill_start_time();//开始时间
			try {
				Date nowDate = sdf.parse(nowTimeStr);
				Date willStartDate = sdf.parse(willStartTimeStr);
				remainTimeL = willStartDate.getTime() - nowDate.getTime();
				if(remainTimeL >= 0){
					showPromptDialog("1");
				}else{
//					//正在投资中。。
					bidBtn.setEnabled(true);
					handler.sendEmptyMessage(REQUEST_XSMB_INVEST_WHAT);
				}
			} catch (ParseException e) {
				bidBtn.setEnabled(false);
				bidBtn.setText("投资结束");
				bidBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
				e.printStackTrace();
			}
		}else if("已满标".equals(productInfo.getMoney_status())){
			showPromptDialog("1");
			bidBtn.setEnabled(false);
			bidBtn.setText("投资结束");
			bidBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
		}
	}
	
	/**
	 * 用户账户信息
	 */
	private void requestUserAccountInfo(String userId) {
		AsyncYiLianRMBAccount yilianTask = new AsyncYiLianRMBAccount(
				BidXSMBActivity.this, userId, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								UserRMBAccountInfo info = baseInfo
										.getRmbAccountInfo();
								useBalanceTV.setText(info.getUse_money());
							}
						}
					}
				});
		yilianTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 验证用户是否已经认证
	 * @param type “充值”,“提现”
	 */
	private void checkIsVerify(final String type){
		rechargeBtn.setEnabled(false);
		RequestApis.requestIsVerify(BidXSMBActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//用户已经实名
					checkIsBindCard(type);
				}else{
					//用户没有实名
					Intent intent = new Intent(BidXSMBActivity.this,UserVerifyActivity.class);
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
		RequestApis.requestIsBinding(BidXSMBActivity.this, SettingsManager.getUserId(getApplicationContext()), "宝付", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//用户已经绑卡
					if("充值".equals(type)){
						//那么直接跳到充值页面
						intent.setClass(BidXSMBActivity.this, RechargeActivity.class);
					}
				}else{
					//用户还没有绑卡
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					intent.setClass(BidXSMBActivity.this, BindCardActivity.class);
				}
				startActivity(intent);
				rechargeBtn.setEnabled(true);
			}
		});
	}
	
	/**
	 * 秒标详情
	 */
	private void requestXSMBDetails(String borrowStatus){
		bidBtn.setEnabled(false);
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		AsyncXSMBDetail xsmbTask = new AsyncXSMBDetail(BidXSMBActivity.this, borrowStatus,new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				bidBtn.setEnabled(true);
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						ProductInfo info = baseInfo.getmProductInfo();
						checkInvest(info);
					}else{
						Util.toastLong(BidXSMBActivity.this, baseInfo.getMsg());
						mLoadingDialog.dismiss();
					}
				}else{
					mLoadingDialog.dismiss();
				}
			}
		});
		xsmbTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 限时秒标投标接口
	 * @param borrowId
	 * @param money
	 * @param userId
	 * @param investFrom
	 */
	private void requestXSMBInvest(String borrowId,String money,String userId,String investFrom){
		AsyncXSMBInvest xsmbTask = new AsyncXSMBInvest(BidXSMBActivity.this,borrowId,money,userId,investFrom,new OnCommonInter(){
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						Intent intentSuccess = new Intent(BidXSMBActivity.this,BidSuccessActivity.class);
						intentSuccess.putExtra("from_where", "秒标");
						startActivity(intentSuccess);
						mApp.finishAllActivityExceptMain();
					}else if(resultCode == -102){
						//秒杀机会用完
						showPromptDialog("0");
					}else{
						Util.toastLong(BidXSMBActivity.this, baseInfo.getMsg());
					}
				}
			}
		});
		xsmbTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 
	 * @param flag 1表示已抢光 0表示秒杀机会用完
	 */
	private void showPromptDialog(final String flag){
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.borrow_detail_prompt_layout, null);
		Button leftBtn = (Button) contentView
				.findViewById(R.id.borrow_detail_prompt_layout_leftbtn);
		Button rightBtn = (Button) contentView
				.findViewById(R.id.borrow_detail_prompt_layout_rightbtn);
		TextView topTV = (TextView) contentView
				.findViewById(R.id.borrow_detail_prompt_top_text);
		TextView bottomTV = (TextView) contentView
				.findViewById(R.id.borrow_detail_prompt_bottom_text);
		if("0".equals(flag)){
			bottomTV.setVisibility(View.VISIBLE);
			rightBtn.setVisibility(View.VISIBLE);
			topTV.setText("您的本场秒杀机会已使用");
			bottomTV.setText("每场秒杀，每个用户只有一次秒杀机会哟~");
			leftBtn.setText("查看投资记录");
			leftBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
			leftBtn.setBackground(getResources().getDrawable(R.drawable.style_rect_fillet_blue));
			rightBtn.setText("关注其他项目");
		}else if("1".equals(flag)){
			bottomTV.setVisibility(View.GONE);
			rightBtn.setVisibility(View.GONE);
			topTV.setText("已秒光！\n请下一场再来试试吧~");
			leftBtn.setText("确定");
			leftBtn.setTextColor(getResources().getColor(R.color.white));
			leftBtn.setBackground(getResources().getDrawable(R.drawable.style_rect_fillet_filling_blue));
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				R.style.Dialog_Transparent); // 先得到构造器
		builder.setView(contentView);
		builder.setCancelable(true);
		final AlertDialog dialog = builder.create();
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if("0".equals(flag)){
					Intent intent = new Intent(BidXSMBActivity.this,UserInvestRecordActivity.class);
					intent.putExtra("from_where", "秒标");
					startActivity(intent);
				}
				dialog.dismiss();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingsManager.setMainProductListFlag(getApplicationContext(), true);	
				dialog.dismiss();
				finish();
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
}
