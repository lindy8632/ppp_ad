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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncJXQPageInfo;
import com.ylfcf.ppp.async.AsyncWDYInvest;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.JiaxiquanInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.ui.BidZXDActivity.OnHBWindowItemClickListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.view.JXQListPopupwindow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/**
 * 零存整取产品投资页面
 * @author Mr.liu
 *
 */
public class BidWDYActivity extends BaseActivity implements OnClickListener{
	private static final String className = "BidWDYActivity";
	private static final int REQUEST_INVEST_WHAT = 1201;
	private static final int REQUEST_INVEST_SUCCESS = 1202;
	private static final int REQUEST_INVEST_EXCEPTION = 1203;
	private static final int REQUEST_INVEST_FAILE = 1204;

	private static final int REQUEST_INVEST_INCREASE = 1205;
	private static final int REQUEST_INVEST_DESCEND = 1206;

	private static final int REQUEST_JXQ_LIST_WHAT = 1207;
	private static final int REQUEST_JXQ_LIST_SUCCESS = 1208;

	private LinearLayout topLeftBtn;
	private TextView topTitleTV, borrowName;
	private TextView userBalanceTV;// 用户可用余额
	private TextView borrowBalanceTV;// 标的剩余可投金额
	private Button rechargeBtn;// 充值
	private TextView shouyiText;//年化收益 根据投资金额的不同而不同
	private TextView tzqxText;

	private LinearLayout jxqLayout;// 加息券
	private EditText jxqEditText;
	private RelativeLayout jxqArrowLayout;// 加息券的箭头
	private Button descendBtn;// 递减按钮
	private Button increaseBtn;// 递增按钮
	private EditText investMoneyET;
	private ImageView deleteImg;// x号
	private List<JiaxiquanInfo> jxqList = new ArrayList<JiaxiquanInfo>();//可使用的加息券列表

	private ProductInfo mProductInfo;
	private int moneyInvest = 0;//投资金额
	private Button investBtn;

	private LinearLayout mainLayout;
	private View line1;// 分割线
	private CheckBox cb;
	private TextView compactText;//借款协议

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_INVEST_WHAT:
				requestInvest(mProductInfo.getId(),
						SettingsManager.getUserId(getApplicationContext()),
						String.valueOf(moneyInvest), "",
						SettingsManager.USER_FROM,"",String.valueOf(jxqEditText.getTag()));
				break;
			case REQUEST_INVEST_SUCCESS:
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				Intent intentSuccess = new Intent(BidWDYActivity.this,
						BidSuccessActivity.class);
				intentSuccess.putExtra("from_where", "稳定盈");
				intentSuccess.putExtra("base_info",baseInfo);
				startActivity(intentSuccess);
				mApp.finishAllActivityExceptMain();
				break;
			case REQUEST_INVEST_EXCEPTION:
				BaseInfo base = (BaseInfo) msg.obj;
				Util.toastShort(BidWDYActivity.this, base.getMsg());
				break;
			case REQUEST_INVEST_FAILE:
				Util.toastShort(BidWDYActivity.this, "网络异常");
				break;
			case REQUEST_INVEST_INCREASE:
				investMoneyIncrease();
				break;
			case REQUEST_INVEST_DESCEND:
				investMoneyDescend();
				break;
				case REQUEST_JXQ_LIST_WHAT:
					requestJXQList(SettingsManager.getUserId(getApplicationContext()), "未使用");
					break;
				case REQUEST_JXQ_LIST_SUCCESS:
					Date endDate = null;
					BaseInfo baseInfo1 = (BaseInfo) msg.obj;
					List<JiaxiquanInfo> jiaxiList = baseInfo1.getmJiaxiquanPageInfo().getInfoList();
					for(int i=0;i<jiaxiList.size();i++){
						JiaxiquanInfo info = jiaxiList.get(i);
						if("未使用".equals(info.getUse_status()) && info.getBorrow_type().contains("薪盈计划首期")){
							try {
								endDate = sdf.parse(info.getEffective_end_time());
								if(endDate.compareTo(sdf.parse(baseInfo1.getTime())) == 1 && "0".equals(info.getTransfer())){
									//表示加息券还未过期 ,并且使不可转让的加息券
									jxqList.add(info);
								}
							} catch (Exception e) {
							}
						}
					}
					if(jxqList.size() > 0){
						jxqLayout.setVisibility(View.VISIBLE);
					}
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
		setContentView(R.layout.bid_wdy_activity);
		
		mProductInfo = (ProductInfo) getIntent().getSerializableExtra(
				"PRODUCT_INFO");
		findViews();
		initInvestBalance(mProductInfo);
		handler.sendEmptyMessageDelayed(REQUEST_JXQ_LIST_WHAT,1100L);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//友盟统计页面跳转
		UMengStatistics.statisticsResume(this);//友盟统计时长
		requestUserAccountInfo(SettingsManager
				.getUserId(getApplicationContext()));
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
	
	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("投资");

		borrowName = (TextView) findViewById(R.id.bid_wdy_activity_borrow_name);
		userBalanceTV = (TextView) findViewById(R.id.bid_wdy_activity_user_balance);
		borrowBalanceTV = (TextView) findViewById(R.id.bid_wdy_activity_borrow_balance);
		shouyiText = (TextView) findViewById(R.id.bid_wdy_activity_shouyi);
		tzqxText = (TextView) findViewById(R.id.bid_wdy_activity_tzqx);//投资期限
		tzqxText.setText(mProductInfo.getInterest_period_month() + "个月最多可获得收益：");
		rechargeBtn = (Button) findViewById(R.id.bid_wdy_activity_recharge_btn);
		rechargeBtn.setOnClickListener(this);
		descendBtn = (Button) findViewById(R.id.bid_wdy_activity_discend_btn);
		descendBtn.setOnClickListener(this);
		descendBtn.setOnTouchListener(mOnTouchListener);
		increaseBtn = (Button) findViewById(R.id.bid_wdy_activity_increase_btn);
		increaseBtn.setOnClickListener(this);
		increaseBtn.setOnTouchListener(mOnTouchListener);
		investMoneyET = (EditText) findViewById(R.id.bid_wdy_activity_invest_et);
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
					if (investMoney == 0) {
						Util.toastLong(BidWDYActivity.this, "投资金额不能为0");
					} else if (investMoney % 100 != 0) {
						Util.toastLong(BidWDYActivity.this, "投资金额必须为100的整数倍");
					}
					// 判断投资金额是否大于标的所剩额度
					String borrowBalanceStr = String.valueOf(borrowBalanceTemp);
					borrowBalanceDouble = Double.parseDouble(borrowBalanceStr);
					if (investMoney > borrowBalanceDouble) {
						Util.toastLong(BidWDYActivity.this, "标的剩余可投金额不足");
					}
				} catch (Exception e) {
				}
			}
		});
		deleteImg = (ImageView) findViewById(R.id.bid_wdy_activity_delete);
		deleteImg.setOnClickListener(this);
		investBtn = (Button) findViewById(R.id.bid_wdy_activity_borrow_bidBtn);
		investBtn.setOnClickListener(this);
		if(mProductInfo.getBorrow_name() != null && !"".equals(mProductInfo.getBorrow_name())){
			borrowName.setText(mProductInfo.getBorrow_name());
		}else{
			borrowName.setText("薪盈计划-"+mProductInfo.getBorrow_period()+"期");
		}
		mainLayout = (LinearLayout) findViewById(R.id.bid_activity_mainlayout);
		line1 = findViewById(R.id.bid_wdy_activity_line1);

		cb = (CheckBox) findViewById(R.id.bid_wdy_activity_cb);
		compactText = (TextView) findViewById(R.id.bid_wdy_activity_compact_text);
		compactText.setOnClickListener(this);

		jxqLayout = (LinearLayout) findViewById(R.id.bid_wdy_activity_jxq_layout);
		jxqLayout.setOnClickListener(this);
		jxqEditText = (EditText) findViewById(R.id.bid_wdy_activity_jxq_et);
		jxqArrowLayout = (RelativeLayout) findViewById(R.id.bid_wdy_activity_jxq_arrow_layout);
		jxqArrowLayout.setOnClickListener(this);

	}
	
	float extraRateF = 0f;
	int borrowBalanceTemp = 0;

	/**
	 * 标的剩余可投金额
	 * @param info
	 */
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
				computeIncome(mProductInfo.getInterest_rate(),String.valueOf(jxqArrowLayout.getTag()),investMoney,
						mProductInfo.getInterest_period_month());
			} catch (Exception e) {
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
		case R.id.bid_wdy_activity_borrow_bidBtn:
			borrowInvest();
			break;
		case R.id.bid_wdy_activity_recharge_btn:
			//去充值
			if(SettingsManager.isPersonalUser(getApplicationContext())){
				checkIsVerify("充值");
			}else if(SettingsManager.isCompanyUser(getApplicationContext())){
				Intent intent = new Intent(BidWDYActivity.this,RechargeCompActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.bid_wdy_activity_discend_btn:
			// 递减
			investMoneyDescend();
			break;
		case R.id.bid_wdy_activity_increase_btn:
			investMoneyIncrease();
			break;
		case R.id.bid_wdy_activity_delete:
			resetInvestMoneyET();
			break;
		case R.id.bid_wdy_activity_compact_text:
			//借款协议
			Intent intent = new Intent(BidWDYActivity.this,CompactActivity.class);
			intent.putExtra("from_where", "wdy");
			intent.putExtra("mProductInfo", mProductInfo);
			startActivity(intent);
			break;
		case R.id.bid_wdy_activity_jxq_arrow_layout:
		case R.id.bid_wdy_activity_jxq_layout:
			checkJXQ();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 验证用户是否已经认证
	 * @param type “充值”,“提现”
	 */
	private void checkIsVerify(final String type){
		rechargeBtn.setEnabled(false);
		RequestApis.requestIsVerify(BidWDYActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//用户已经实名
					checkIsBindCard(type);
				}else{
					//用户没有实名
					Intent intent = new Intent(BidWDYActivity.this,UserVerifyActivity.class);
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

	private void checkJXQ(){
		showJXQListWindow();
	}

	/**
	 * 显示加息券的列表
	 */
	private void showJXQListWindow(){
		if (jxqList == null || jxqList.size() < 1) {
			Util.toastLong(BidWDYActivity.this, "没有加息券可用");
			return;
		}
		View popView = LayoutInflater.from(this).inflate(
				R.layout.tyj_list_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(BidWDYActivity.this);
		int width = screen[0];
		int height = screen[1] * 1 / 3;
		JXQListPopupwindow popwindow = new JXQListPopupwindow(
				BidWDYActivity.this, popView, width, height,"请选择加息券");
		popwindow.show(mainLayout, jxqList, new OnHBWindowItemClickListener() {
			@Override
			public void onItemClickListener(View view, int position) {
				if (position == 0) {
					jxqEditText.setText(null);
					jxqEditText.setTag("");
					jxqArrowLayout.setTag("0");
					updateInterest();
				} else {
					JiaxiquanInfo info = jxqList.get(position - 1);
					String moneyStr = investMoneyET.getText().toString();
					int investMoney = 0;//输入框中输入的投资金额
					double limitMoney = 0;//需要投资的金额
					try {
						investMoney = Integer.parseInt(moneyStr);
					} catch (Exception e) {
					}
					try {
						limitMoney = Double.parseDouble(info.getMin_invest_money());
					} catch (Exception e) {
					}
					if(investMoney < limitMoney){
						jxqEditText.setText("");
						Util.toastLong(BidWDYActivity.this, "您的投资金额不满足加息券要求");
					}else{
						if(limitMoney >= 10000){
							jxqEditText.setText(info.getMoney()+"%的加息券，"+"需投资"+(int)(limitMoney/10000)+"万元及以上可用");
						}else{
							jxqEditText.setText(info.getMoney()+"%的加息券，"+"需投资"+(int)(limitMoney)+"元及以上可用");
						}
						jxqEditText.setTag(info.getId());
						jxqArrowLayout.setTag(info.getMoney());
						updateInterest();
					}
				}
			}
		});
	}

	/**
	 * 判断用户是否已经绑卡
	 * @param type "充值提现"
	 */
	private void checkIsBindCard(final String type){
		RequestApis.requestIsBinding(BidWDYActivity.this, SettingsManager.getUserId(getApplicationContext()), "宝付", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//用户已经绑卡
					if("充值".equals(type)){
						//那么直接跳到充值页面
						intent.setClass(BidWDYActivity.this, RechargeActivity.class);
					}
				}else{
					//用户还没有绑卡
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					intent.setClass(BidWDYActivity.this, BindCardActivity.class);
				}
				startActivity(intent);
				rechargeBtn.setEnabled(true);
			}
		});
	}

	/**
	 * 
	 */
	private void borrowInvest() {
		String moneyStr = investMoneyET.getText().toString();
		double needRechargeMoeny = 0d;// 需要支付的金额 等于 输入的金额减去所用元金币的金额
		double userBanlanceDouble = 0d;
		double borrowBalanceDouble = 0d;
		double investLowestD = 0d;//最低投资金额
		try {
			moneyInvest = Integer.parseInt(moneyStr);
		} catch (Exception e) {
			moneyInvest = 0;
		}
		try {
			investLowestD = Double.parseDouble(mProductInfo.getInvest_lowest());
		} catch (Exception e) {
		}
		needRechargeMoeny = moneyInvest;
		// 判断投资金额是否大于账户余额
		String userBanlance = userBalanceTV.getText().toString();
		userBanlanceDouble = Double.parseDouble(userBanlance);
		String borrowBalance = String.valueOf(borrowBalanceTemp);
		borrowBalanceDouble = Double.parseDouble(borrowBalance);
		if(borrowBalanceDouble < investLowestD){
			//标的可投金额小于起投金额，则需用户一次性投完
			if(moneyInvest < borrowBalanceDouble){
				//提示用户须1次性投完
				showWDYInvestDialog(borrowBalanceDouble);
			}else{
				if (needRechargeMoeny > userBanlanceDouble) {
					Util.toastLong(BidWDYActivity.this, "账户余额不足");
				} else if (needRechargeMoeny > borrowBalanceDouble) {
					Util.toastLong(BidWDYActivity.this, "标的可投余额不足");
				} else if(!cb.isChecked()){
					Util.toastLong(BidWDYActivity.this, "请先阅读并同意薪盈计划服务协议书");
				} else {
					showInvestDialog();
				}
			}
		}else{
			if (moneyInvest < investLowestD) {
				Util.toastShort(BidWDYActivity.this, "投资金额不能小于"+(int)investLowestD+"元");
			} else if (moneyInvest % 100 != 0) {
				Util.toastLong(BidWDYActivity.this, "投资金额必须为100的整数倍");
			} else if (needRechargeMoeny > userBanlanceDouble) {
				Util.toastLong(BidWDYActivity.this, "账户余额不足");
			} else if (needRechargeMoeny > borrowBalanceDouble) {
				Util.toastLong(BidWDYActivity.this, "标的可投余额不足");
			} else if(!cb.isChecked()){
				Util.toastLong(BidWDYActivity.this, "请先阅读并同意薪盈计划服务协议书");
			}else {
				showInvestDialog();
			}
		}
	}

	/**
	 * 稳定盈投资的弹窗
	 * @param borrowBalanceDouble
	 */
	private void showWDYInvestDialog(final double borrowBalanceDouble){
		View contentView = LayoutInflater.from(this).inflate(R.layout.common_popwindow, null);
		final Button sureBtn = (Button) contentView.findViewById(R.id.common_popwindow_btn);
		final TextView content = (TextView) contentView.findViewById(R.id.common_popwindow_content);
		content.setText("当前剩余可投金额已小于起投金额，请全额认购！");
		AlertDialog.Builder builder=new AlertDialog.Builder(this, R.style.Dialog_Transparent);  //先得到构造器  
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
        //参数都设置完成了，创建并显示出来  
        dialog.show();  
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*6/7;
        dialog.getWindow().setAttributes(lp);
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
		TextView totalMoneyTV = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_total);
		LinearLayout yuanMoneyLayout = (LinearLayout) contentView.findViewById(R.id.invest_prompt_yjb_layout_detail);
		yuanMoneyLayout.setVisibility(View.GONE);
		totalMoneyTV.setText(moneyInvest + "");

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
		computeIncome(mProductInfo.getInterest_rate(),String.valueOf(jxqArrowLayout.getTag()),investMoneyInt,
				mProductInfo.getInterest_period_month());
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
		computeIncome(mProductInfo.getInterest_rate(),String.valueOf(jxqArrowLayout.getTag()),investMoneyInt,
				mProductInfo.getInterest_period_month());
	}

	/**
	 * 选择加息券的时候刷新预期收益
	 */
	private void updateInterest(){
		String investMoneyStr = investMoneyET.getText().toString();
		int investMoneyInt = 0;
		try {
			investMoneyInt = Integer.parseInt(investMoneyStr);
		} catch (Exception e) {
		}
		computeIncome(mProductInfo.getInterest_rate(),String.valueOf(jxqArrowLayout.getTag()), investMoneyInt,
				mProductInfo.getInterest_period_month());
	}
	
	private void resetInvestMoneyET() {
		if (investMoneyET != null) {
			investMoneyET.setText(null);
			shouyiText.setText("0.00");
		}
	}

	private ScheduledExecutorService myExecuter;

	private void updateInvestCounter(final View v) {
		myExecuter = Executors.newSingleThreadScheduledExecutor();
		myExecuter.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				if (v.getId() == R.id.bid_wdy_activity_increase_btn) {
					handler.sendEmptyMessage(REQUEST_INVEST_INCREASE);
				} else if (v.getId() == R.id.bid_wdy_activity_discend_btn) {
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
	 * 计算稳定盈的收益
	 * @param rateStr
	 * @param couponRateStr 加息券利率
	 * @param investMoney
	 * @param months 投资月数
	 * @return
	 */
	private void computeIncome(String rateStr, String couponRateStr,int investMoney, String months) {
		YLFLogger.d("coupon:"+"\n基础利率："+ rateStr +"\n加息券利率："+couponRateStr+"\n投资金额："+investMoney+"\n投资期限："+months);
		shouyiText.setText(Util.getWDYInterest(rateStr,couponRateStr, investMoney, months));
	}

	/**
	 * 加息券
	 * @param userId
	 * @param useStatus
	 */
	private void requestJXQList(String userId, String useStatus) {
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncJXQPageInfo redbagTask = new AsyncJXQPageInfo(BidWDYActivity.this, userId,useStatus,
				String.valueOf(1),String.valueOf(100), new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				if (baseInfo != null) {
					int resultCode = SettingsManager
							.getResultCode(baseInfo);
					if (resultCode == 0) {
						Message msg = handler
								.obtainMessage(REQUEST_JXQ_LIST_SUCCESS);
						msg.obj = baseInfo;
						handler.sendMessage(msg);
					}
				}
			}
		});
		redbagTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 投标接口
	 * @param borrowId
	 * @param investUserId
	 * @param money 
	 * @param coinMoney 元金币金额
	 * @param investFrom
	 * @param redBagLogId 红包id
	 * @param couponLogId 加息券id
	 */
	private void requestInvest(String borrowId, String investUserId,
			String money, String coinMoney, String investFrom,
			String redBagLogId,String couponLogId) {
		if (mLoadingDialog != null && !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncWDYInvest asyncBorrowInvest = new AsyncWDYInvest(
				BidWDYActivity.this, borrowId, investUserId, money, 
				investFrom,coinMoney,redBagLogId, couponLogId,new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}

						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler
										.obtainMessage(REQUEST_INVEST_SUCCESS);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							} else {
								Message msg = handler
										.obtainMessage(REQUEST_INVEST_EXCEPTION);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							}
						} else {
							Message msg = handler
									.obtainMessage(REQUEST_INVEST_FAILE);
							handler.sendMessage(msg);
						}
					}
				});
		asyncBorrowInvest.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 用户账户信息
	 */
	private void requestUserAccountInfo(String userId) {
		AsyncYiLianRMBAccount yilianTask = new AsyncYiLianRMBAccount(
				BidWDYActivity.this, userId, new OnCommonInter() {
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
