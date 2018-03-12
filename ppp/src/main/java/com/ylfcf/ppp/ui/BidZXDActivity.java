package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
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
import com.ylfcf.ppp.async.AsyncBorrowInvest;
import com.ylfcf.ppp.async.AsyncCurrentUserRedbagList;
import com.ylfcf.ppp.async.AsyncExperiencePageInfo;
import com.ylfcf.ppp.async.AsyncJXQPageInfo;
import com.ylfcf.ppp.async.AsyncUserYUANAccount;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.JiaxiquanInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.RedBagInfo;
import com.ylfcf.ppp.entity.RedBagPageInfo;
import com.ylfcf.ppp.entity.TYJInfo;
import com.ylfcf.ppp.entity.TYJPageInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.entity.UserYUANAccountInfo;
import com.ylfcf.ppp.inter.Inter.OnBorrowInvestInter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.inter.Inter.OnUserYUANAccountInter;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.view.HBListPopupwindow;
import com.ylfcf.ppp.view.JXQListPopupwindow;
import com.ylfcf.ppp.view.TYJListPopupwindow;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 政信贷---我要投资 元金币使用规则:投资期限为35天的时候，元金币使用率为0.2% 投资期限为95天的时候，元金币使用率为0.5%
 * 投资期限为185和365天的时候，元金币使用率为1%
 * @author Mr.liu
 */
public class BidZXDActivity extends BaseActivity implements OnClickListener {
	private static final String className = "BidZXDActivity";
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
	private TextView dtdzBtn;//多投多赚
	private Button rechargeBtn;// 充值
	private TextView nhsyText;//年化收益 根据投资金额的不同而不同

	private Button descendBtn;// 递减按钮
	private Button increaseBtn;// 递增按钮
	private EditText investMoneyET;
	private ImageView deleteImg;// x号
	private TextView yjsyText;// 预计收益

	private TextView daojuPromptTV;

	// 元金币
	private EditText yuanMoneyET;// 元金币
	private TextView yuanUsedTV;// 元金币可用金额
	private TextView yuanBalanceTV;// 元金币余额
	private ImageView borrowLogo;
	private TextView yjbText;
	private ImageView yjbDuihaoImg;
	private LinearLayout yjbLayout;
	private LinearLayout yjbBalanceLayout;// 元金币余额

	// 体验金
	private EditText tiyanjinET;// 体验金
	private RelativeLayout tyjArrowLayout;// 体验金箭头
	private LinearLayout tyjLayout;// 体验金

	// 红包
	private LinearLayout hbLayout;// 红包
	private EditText hbEditText;
	private RelativeLayout hbArrowLayout;// 红包的箭头
	
	// 加息券
	private LinearLayout jxqLayout;// 加息券
	private EditText jxqEditText;
	private RelativeLayout jxqArrowLayout;// 加息券的箭头
	private TextView usePrompt;//使用说明，加息券，元金币不能同时使用等
	private List<String> usePromptList = new ArrayList<String>();

	private ProductInfo mProductInfo;
	private int moneyInvest = 0;//投资金额
	private int bonusMoney = 0;// 元金币
	private double hbMoney = 0;//红包金额
	private double jxqMoney = 0d;//加息券
	private Button investBtn;
	private UserYUANAccountInfo mUserYUANAccountInfo = null;// 元金币账户

	private LinearLayout mainLayout;
	private View line1, line2, line3, line4, line5;// 分割线
	private CheckBox cb;
	private TextView compactText;//借款协议

	private int limitInvest = 0;// 投资期限
	private String borrowType = "";
	private List<TYJInfo> experienceList = new ArrayList<TYJInfo>();// 用户未使用的体验金列表
	private List<RedBagInfo> hbList = new ArrayList<RedBagInfo>();// 用户未使用的红包列表
	private List<JiaxiquanInfo> jxqList = new ArrayList<JiaxiquanInfo>();//可使用的加息券列表
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String sysTimeStr = "";
	
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
						SettingsManager.getUserFromSub(getApplicationContext()), tiyanjinET.getText()
								.toString(), "", "", String.valueOf(hbEditText
								.getTag()),String.valueOf(jxqEditText.getTag()));
				break;
			case REQUEST_INVEST_SUCCESS:
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				Intent intentSuccess = new Intent(BidZXDActivity.this,
						BidSuccessActivity.class);
				intentSuccess.putExtra("from_where", "元政盈");
				intentSuccess.putExtra("base_info",baseInfo);
				startActivity(intentSuccess);
				mApp.finishAllActivityExceptMain();
				break;
			case REQUEST_INVEST_EXCEPTION:
				BaseInfo base = (BaseInfo) msg.obj;
				Util.toastShort(BidZXDActivity.this, base.getMsg());
				break;
			case REQUEST_INVEST_FAILE:
				Util.toastShort(BidZXDActivity.this, "网络异常");
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
					if("未使用".equals(info.getUse_status()) && info.getBorrow_type().contains(borrowType)){
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
					usePromptList.add("加息券");
					updateUsePrompt(usePromptList);
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
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bid_zxd_activity);

		mProductInfo = (ProductInfo) getIntent().getSerializableExtra(
				"PRODUCT_INFO");
		
		if (mProductInfo != null) {
			String horizon = "";
			if(mProductInfo.getInvest_horizon() == null || "".equals(mProductInfo.getInvest_horizon())){
				horizon = mProductInfo.getInterest_period();
			}else{
				horizon = mProductInfo.getInvest_horizon().replace("天", "");
			}
			try {
				limitInvest = Integer.parseInt(horizon);
			} catch (Exception e) {
			}
		}

		findViews();
		initInvestBalance(mProductInfo);
		if(mProductInfo != null){
	        	if(mProductInfo.getInterest_period().contains("92")){
	            	//元季融
	        		requestHBPageInfoByBorrowType(
	    					SettingsManager.getUserId(getApplicationContext()),"元季融");
	            }else if(mProductInfo.getInterest_period().contains("32")){
	            	//元月通
	            	requestHBPageInfoByBorrowType(
	    					SettingsManager.getUserId(getApplicationContext()),"元月通");
	            }else if(mProductInfo.getInterest_period().contains("182")){
	            	//元定和
	            	requestHBPageInfoByBorrowType(
	    					SettingsManager.getUserId(getApplicationContext()),"元定和");
	            }else if(mProductInfo.getInterest_period().contains("365")){
	            	//元年鑫
	            	requestHBPageInfoByBorrowType(
	    					SettingsManager.getUserId(getApplicationContext()),"元年鑫");
	            }
		}
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				requestExperiencePageInfoByStatus("未使用",
						SettingsManager.getUserId(getApplicationContext()), "",
						"");
			}
		}, 50L);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				requestYuanAccountInfo(SettingsManager
						.getUserId(getApplicationContext()));
			}
		}, 60L);

		handler.sendEmptyMessageDelayed(REQUEST_JXQ_LIST_WHAT,60L);
	}

	private void updateUsePrompt(List<String> usePromptList){
		StringBuffer sb = new StringBuffer();
		if(usePromptList != null && usePromptList.size() > 0){
			daojuPromptTV.setVisibility(View.VISIBLE);
		}else{
			daojuPromptTV.setVisibility(View.GONE);
		}
		if(usePromptList != null && usePromptList.size() > 1){

			for(int i=0;i<usePromptList.size();i++){
				String prompt = usePromptList.get(i);
				if(i == usePromptList.size() - 1){
					sb.append(prompt);
				}else{
					sb.append(prompt).append("、");
				}
			}
			usePrompt.setVisibility(View.VISIBLE);
			usePrompt.setText(sb.toString()+"只能使用其中一种");
		}else{
			usePrompt.setVisibility(View.GONE);
			yjbLayoutVisible(View.GONE);
		}
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

		usePrompt = (TextView) findViewById(R.id.bid_zxd_activity_use_prompt);
		borrowName = (TextView) findViewById(R.id.bid_zxd_activity_borrow_name);
		userBalanceTV = (TextView) findViewById(R.id.bid_zxd_activity_user_balance);
		borrowBalanceTV = (TextView) findViewById(R.id.bid_zxd_activity_borrow_balance);
		nhsyText = (TextView) findViewById(R.id.bid_zxd_activity_nhsy);
		nhsyText.setText(mProductInfo.getInterest_rate()+"%");
		if(SettingsManager.checkFloatRate(mProductInfo) && !"元年鑫".equals(mProductInfo.getBorrow_type())){
			dtdzBtn = (TextView) findViewById(R.id.bid_zxd_activity_dtdz_btn);
			dtdzBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //下划线
			dtdzBtn.getPaint().setAntiAlias(true);//抗锯齿
			dtdzBtn.setVisibility(View.VISIBLE);
			dtdzBtn.setOnClickListener(this);
		}
		
		rechargeBtn = (Button) findViewById(R.id.bid_zxd_activity_recharge_btn);
		rechargeBtn.setOnClickListener(this);
		descendBtn = (Button) findViewById(R.id.bid_zxd_activity_discend_btn);
		descendBtn.setOnClickListener(this);
		descendBtn.setOnTouchListener(mOnTouchListener);
		increaseBtn = (Button) findViewById(R.id.bid_zxd_activity_increase_btn);
		increaseBtn.setOnClickListener(this);
		increaseBtn.setOnTouchListener(mOnTouchListener);
		investMoneyET = (EditText) findViewById(R.id.bid_zxd_activity_invest_et);
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
						Util.toastLong(BidZXDActivity.this, "投资金额不能为0");
					} else if (investMoney % 100 != 0) {
						Util.toastLong(BidZXDActivity.this, "投资金额必须为100的整数倍");
					}

					// 判断投资金额是否大于标的所剩额度
					String borrowBalanceStr = String.valueOf(borrowBalanceTemp);
					borrowBalanceDouble = Double.parseDouble(borrowBalanceStr);
					if (investMoney > borrowBalanceDouble) {
						Util.toastLong(BidZXDActivity.this, "标的剩余可投金额不足");
					}
				} catch (Exception e) {
				}

			}
		});
		deleteImg = (ImageView) findViewById(R.id.bid_zxd_activity_delete);
		deleteImg.setOnClickListener(this);
		yjsyText = (TextView) findViewById(R.id.bid_zxd_activity_yjsy);
		yuanMoneyET = (EditText) findViewById(R.id.bid_zxd_activity_yuanmoney_et);
		yuanMoneyET.addTextChangedListener(watcherYuanMoney);
		yuanUsedTV = (TextView) findViewById(R.id.bid_zxd_activity_used_yjb_text);
		yuanBalanceTV = (TextView) findViewById(R.id.bid_zxd_activity_balance_yjb_text);
		tiyanjinET = (EditText) findViewById(R.id.bid_zxd_activity_tyj_et);
		tyjArrowLayout = (RelativeLayout) findViewById(R.id.bid_zxd_activity_tyj_arrow_layout);
		tyjArrowLayout.setOnClickListener(this);
		investBtn = (Button) findViewById(R.id.bid_zxd_activity_borrow_bidBtn);
		investBtn.setOnClickListener(this);
		borrowLogo = (ImageView) findViewById(R.id.bid_zxd_activity_prompt_logo);
		if (mProductInfo != null) {
			borrowName.setText(mProductInfo.getBorrow_name());
			if (mProductInfo.getInterest_period().contains("182")) {
				borrowLogo.setImageResource(R.drawable.bid_ydh_logo);
				borrowType = "元定和";
			} else if (mProductInfo.getInterest_period().contains("32")) {
				borrowLogo.setImageResource(R.drawable.bid_yyt_logo);
				borrowType = "元月通";
			} else if (mProductInfo.getInterest_period().contains("92")) {
				borrowLogo.setImageResource(R.drawable.bid_yjr_logo);
				borrowType = "元季融";
			} else if (mProductInfo.getInterest_period().contains("365")){
				borrowLogo.setImageResource(R.drawable.bid_ynx_logo);
				borrowType = "元年鑫";
			}
		}
		mainLayout = (LinearLayout) findViewById(R.id.bid_activity_mainlayout);
		line1 = findViewById(R.id.bid_zxd_activity_line1);
		line2 = findViewById(R.id.bid_zxd_activity_line2);
		line3 = findViewById(R.id.bid_zxd_activity_line3);
		line4 = findViewById(R.id.bid_zxd_activity_line4);
		line5 = findViewById(R.id.bid_zxd_activity_line5);

		daojuPromptTV = (TextView) findViewById(R.id.bid_zxd_activity_daoju_prompt);
		yjbLayout = (LinearLayout) findViewById(R.id.bid_zxd_activity_yjb_layout);
		yjbBalanceLayout = (LinearLayout) findViewById(R.id.bid_zxd_activity_yjb_balance_layout);
		yjbDuihaoImg = (ImageView) findViewById(R.id.bid_zxd_activity_yjb_duihao);
		yjbText = (TextView) findViewById(R.id.bid_zxd_activity_yjb_text);
		tyjLayout = (LinearLayout) findViewById(R.id.bid_zxd_activity_tyj_layout);
		hbLayout = (LinearLayout) findViewById(R.id.bid_zxd_activity_hb_layout);
		hbLayout.setOnClickListener(this);
		hbEditText = (EditText) findViewById(R.id.bid_zxd_activity_hb_et);
		hbEditText.setOnClickListener(this);
		hbArrowLayout = (RelativeLayout) findViewById(R.id.bid_zxd_activity_hb_arrow_layout);
		hbArrowLayout.setOnClickListener(this);
		jxqLayout = (LinearLayout) findViewById(R.id.bid_zxd_activity_jxq_layout);
		jxqLayout.setOnClickListener(this);
		jxqEditText = (EditText) findViewById(R.id.bid_zxd_activity_jxq_et);
		jxqEditText.setOnClickListener(this);
		jxqArrowLayout = (RelativeLayout) findViewById(R.id.bid_zxd_activity_jxq_arrow_layout);
		jxqArrowLayout.setOnClickListener(this);
		cb = (CheckBox) findViewById(R.id.bid_zxd_activity_cb);
		compactText = (TextView) findViewById(R.id.bid_zxd_activity_compact_text);
		compactText.setOnClickListener(this);
		// 判断体验金和元金币是否开启，来进行相应布局的显示和隐藏
	}

	private void checkYjbLayoutVisible(){
		if(mUserYUANAccountInfo == null){
			yjbLayoutVisible(View.GONE);
			return;
		}
		double useCoinD = 0d;
		try{
			useCoinD = Double.parseDouble(mUserYUANAccountInfo.getUse_coin());
		}catch (Exception e){
		}

		if ("开启".equals(mProductInfo.getIs_coin()) && useCoinD > 0) {
			// 元金币开启
			usePromptList.add("元金币");
			updateUsePrompt(usePromptList);
			yjbLayoutVisible(View.VISIBLE);
		} else {
			// 元金币关闭
			yjbLayoutVisible(View.GONE);
		}
	}

	private void yjbLayoutVisible(int isVisible){
		yjbLayout.setVisibility(isVisible);
		yjbBalanceLayout.setVisibility(isVisible);
		line1.setVisibility(isVisible);
		line2.setVisibility(isVisible);
	}

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

	/**
	 * 元金币输入框监听
	 */
	private TextWatcher watcherYuanMoney = new TextWatcher(){
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			YLFLogger.d("yuanmoneyet beforeTextChanged()");
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			YLFLogger.d("yuanmoneyet onTextChanged()");
			String yuanmoneyS = yuanMoneyET.getText().toString();//元金币输入的金额
			String yuanmoneyCurUsed = yuanUsedTV.getText().toString();//元金币当前可用的金额，
			int yuanMoneyI = 0;
			int yuanMoneyCurUsedI = 0;
			try{
				yuanMoneyI = Integer.parseInt(yuanmoneyS);
				yuanMoneyCurUsedI = Integer.parseInt(yuanmoneyCurUsed);
			}catch (Exception e){
				e.printStackTrace();
			}
			if(yuanMoneyI > yuanMoneyCurUsedI){
				//输入的金额不会大于当前可用最大金额
				yuanMoneyI = yuanMoneyCurUsedI;
				yuanMoneyET.setText(String.valueOf(yuanMoneyCurUsedI));
				yuanMoneyET.setSelection(yuanmoneyCurUsed.length());
			}else{
			}
			if(yuanMoneyI > 0){
				yjbDuihaoImg.setImageResource(R.drawable.duihao_selected);
				yjbText.setTextColor(getResources().getColor(R.color.black));

				//其他道具置零
				jxqEditText.setText(null);
				jxqEditText.setTag("");
				jxqArrowLayout.setTag("0");
				jxqArrowLayout.setTag(R.id.tag_third,0);
				hbEditText.setText("");
				hbEditText.setTag("");
				hbArrowLayout.setTag(R.id.tag_first,"0");
				hbArrowLayout.setTag(R.id.tag_second,"0");
				hbArrowLayout.setTag(R.id.tag_third,0);
			}else{
				yjbDuihaoImg.setImageResource(R.drawable.duihao_unselected);
				yjbText.setTextColor(getResources().getColor(R.color.gray1));
			}
			updateInterest();
		}

		@Override
		public void afterTextChanged(Editable s) {
			YLFLogger.d("yuanmoneyet afterTextChanged()");
		}
	};

	private TextWatcher watcherInvestMoney = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String investMoneyStr = investMoneyET.getText().toString();
			String floatRateStr = "0";
			int investMoney = 0;
			try {
				investMoney = Integer.parseInt(investMoneyStr);
				//元年鑫产品不享受浮动利率
				if(investMoney < 100000 || mProductInfo.getInterest_period().contains("365")){
					floatRateStr = "0";
				}else if(investMoney >= 100000 && investMoney < 300000){
					floatRateStr = "0.1";
				}else if(investMoney >= 300000 && investMoney < 500000){
					floatRateStr = "0.2";
				}else if(investMoney >= 500000){
					floatRateStr = "0.3";
				}
				computeIncome(mProductInfo.getInterest_rate(),
						mProductInfo.getAndroid_interest_rate(),floatRateStr, (String)jxqArrowLayout.getTag(),
						investMoney,(String)hbArrowLayout.getTag(R.id.tag_first),
						mProductInfo.getInvest_horizon());
				computeUsedYuanMoney(investMoney);// 计算可使用的元金币
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
		case R.id.bid_zxd_activity_borrow_bidBtn:
			borrowInvest();
			break;
		case R.id.bid_zxd_activity_recharge_btn:
			//去充值
			if(SettingsManager.isPersonalUser(getApplicationContext())){
				checkIsVerify("充值");
			}else if(SettingsManager.isCompanyUser(getApplicationContext())){
				Intent intent = new Intent(BidZXDActivity.this,RechargeCompActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.bid_zxd_activity_tyj_arrow_layout:
			checkTYJ();
			break;
		case R.id.bid_zxd_activity_discend_btn:
			// 递减
			investMoneyDescend();
			break;
		case R.id.bid_zxd_activity_increase_btn:
			investMoneyIncrease();
			break;
		case R.id.bid_zxd_activity_delete:
			resetInvestMoneyET();
			break;
		case R.id.bid_zxd_activity_hb_arrow_layout:
		case R.id.bid_zxd_activity_hb_layout:
		case R.id.bid_zxd_activity_hb_et:
			checkHB();
			break;
		case R.id.bid_zxd_activity_dtdz_btn:
			Intent intentBanner = new Intent(BidZXDActivity.this,BannerTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setArticle_id("110");
			bannerInfo.setLink_url(URLGenerator.FLOAT_RATE_URL);
			intentBanner.putExtra("BannerInfo", bannerInfo);
			startActivity(intentBanner);
			break;
		case R.id.bid_zxd_activity_jxq_arrow_layout:
		case R.id.bid_zxd_activity_jxq_layout:
		case R.id.bid_zxd_activity_jxq_et:
			checkJXQ();
			break;
		case R.id.bid_zxd_activity_compact_text:
			//借款协议
			Intent intent = new Intent(BidZXDActivity.this,CompactActivity.class);
			intent.putExtra("from_where", "yzy");
			intent.putExtra("mProductInfo", mProductInfo);
			startActivity(intent);
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
		RequestApis.requestIsVerify(BidZXDActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//用户已经实名
					checkIsBindCard(type);
				}else{
					//用户没有实名
					Intent intent = new Intent(BidZXDActivity.this,UserVerifyActivity.class);
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
		RequestApis.requestIsBinding(BidZXDActivity.this, SettingsManager.getUserId(getApplicationContext()), "宝付", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//用户已经绑卡
					if("充值".equals(type)){
						//那么直接跳到充值页面
						intent.setClass(BidZXDActivity.this, RechargeActivity.class);
					}
				}else{
					//用户还没有绑卡
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					intent.setClass(BidZXDActivity.this, BindCardActivity.class);
				}
				startActivity(intent);
				rechargeBtn.setEnabled(true);
			}
		});
	}

	/**
	 * 检查体验金
	 */
	private void checkTYJ() {
		// 可以使用体验金
		showTYJListWindow();
	}

	private void checkHB() {
		showHBListWindow();
	}
	
	private void checkJXQ(){
		showJXQListWindow();
	}

	/**
	 * 
	 */
	private void borrowInvest() {
		bonusMoney = 0;
		hbMoney = 0;
		jxqMoney = 0;
		String moneyStr = investMoneyET.getText().toString();
		String bonusStr = yuanMoneyET.getText().toString();
		double yuanUsedMoney = 0d;// 元金币可用余额
		double yuanInputMoney = 0d;//元金币
		double needRechargeMoeny = 0d;// 需要支付的金额 等于 输入的金额减去所用元金币的金额
		double userBanlanceDouble = 0d;
		double borrowBalanceDouble = 0d;
		double hbDouble = 0d;//红包金额
		double hbNeedMoneyD = 0d;
		double jxqDouble = 0d;//加息券
		double androidRateD = 0d;//安卓加息字段
		String yuanUsedMoneyStr = "0";
		String yuanInputMoneyStr = "0";
		try {
			yuanUsedMoneyStr = yuanUsedTV.getText().toString();
			if (yuanUsedMoneyStr != null && !"".equals(yuanUsedMoneyStr)) {
				yuanUsedMoney = Double.parseDouble(yuanUsedMoneyStr);
			}
		} catch (Exception e) {
		}

		try {
			yuanInputMoneyStr = yuanMoneyET.getText().toString();
			if (yuanInputMoneyStr != null && !"".equals(yuanInputMoneyStr)) {
				yuanInputMoney = Double.parseDouble(yuanInputMoneyStr);
			}
		} catch (Exception e) {
		}
		try {
			moneyInvest = Integer.parseInt(moneyStr);
		} catch (Exception e) {
			moneyInvest = 0;
		}
		try {
			bonusMoney = Integer.parseInt(bonusStr);
		} catch (Exception e) {
		}
		try {
			hbMoney = Double.parseDouble(hbArrowLayout.getTag(R.id.tag_first).toString());//红包金额
		} catch (Exception e) {
		}
		try{
			hbNeedMoneyD = Double.parseDouble(hbArrowLayout.getTag(R.id.tag_second).toString());//红包限制金额
		}catch (Exception e){

		}
		try {
			jxqMoney = Double.parseDouble(jxqArrowLayout.getTag().toString());
		} catch (Exception e) {
		}
		try {
			hbDouble = Double.parseDouble(hbArrowLayout.getTag(R.id.tag_first).toString());
		} catch (Exception e) {
		}
		try {
			jxqDouble = Double.parseDouble(jxqArrowLayout.getTag().toString());
		} catch (Exception e) {
		}
		try{
			androidRateD = Double.parseDouble(mProductInfo.getAndroid_interest_rate());
		}catch (Exception e){

		}
		needRechargeMoeny = moneyInvest - yuanInputMoney;
		// 判断投资金额是否大于账户余额
		String userBanlance = userBalanceTV.getText().toString();
		userBanlanceDouble = Double.parseDouble(userBanlance);
		String borrowBalance = String.valueOf(borrowBalanceTemp);
		borrowBalanceDouble = Double.parseDouble(borrowBalance);
		int flagOtc = SettingsManager.checkActiveStatusBySysTime(mProductInfo.getAdd_time(),
				SettingsManager.activeOct2017_StartTime,SettingsManager.activeOct2017_EndTime);//10月活动
		int flagNov = SettingsManager.checkActiveStatusBySysTime(mProductInfo.getAdd_time(),
				SettingsManager.activeNov2017_StartTime,SettingsManager.activeNov2017_EndTime);//11月活动
		if (moneyInvest < 100L) {
			Util.toastShort(BidZXDActivity.this, "投资金额不能小于100元");
		} else if (moneyInvest % 100 != 0) {
			Util.toastLong(BidZXDActivity.this, "投资金额必须为100的整数倍");
		} else if (hbDouble > 0 && yuanInputMoney > 0 && jxqDouble > 0 || (hbDouble > 0 && yuanInputMoney > 0) || 
				(yuanInputMoney > 0 && jxqDouble > 0) || (hbDouble > 0 && jxqDouble > 0)) {
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<usePromptList.size();i++){
				sb.append(usePromptList.get(i));
			}
			Util.toastLong(BidZXDActivity.this, sb.toString()+"不能同时使用");
		} else if (yuanInputMoney > yuanUsedMoney) {
			Util.toastLong(BidZXDActivity.this, "元金币使用超限");
		} else if(moneyInvest < hbNeedMoneyD){
			Util.toastLong(BidZXDActivity.this, "投资金额尚未达到"+hbArrowLayout.getTag(R.id.tag_second).toString()+"元红包的单笔投资金额要求");
		}else if (needRechargeMoeny > userBanlanceDouble + yuanInputMoney) {
			Util.toastLong(BidZXDActivity.this, "账户余额不足");
		} else if (needRechargeMoeny > borrowBalanceDouble) {
			Util.toastLong(BidZXDActivity.this, "标的可投余额不足");
		} else if(!cb.isChecked()){
			Util.toastLong(BidZXDActivity.this, "请先阅读并同意产品协议");
		}else if(flagOtc == 0 && (mProductInfo.getInterest_period().contains("92")||mProductInfo.getInterest_period().contains("365"))){
			//2017十月活动加息 元季融和元年鑫才有加息
			if(hbDouble > 0){
				showOtcActivityPromptDialog("红包");
			}else if(yuanInputMoney > 0){
				showOtcActivityPromptDialog("元金币");
			}else if(jxqDouble > 0){
				showOtcActivityPromptDialog("加息券");
			}else{
				showInvestDialog();
			}
		}else if(SettingsManager.isPersonalUser(getApplicationContext()) && androidRateD > 0){
			//2017双十一活动加息 元年鑫才有加息(只针对个人用户)
			if(hbDouble > 0){
				showNovActivityPromptDialog("红包",androidRateD);
			}else if(yuanInputMoney > 0){
				showNovActivityPromptDialog("元金币",androidRateD);
			}else if(jxqDouble > 0){
				showNovActivityPromptDialog("加息券",androidRateD);
			}else{
				showInvestDialog();
			}
		}else {
			showInvestDialog();
		}
	}

	/**
	 * 10月加息活动
	 * @param flag
	 */
	private void showOtcActivityPromptDialog(String flag){
		View contentView = LayoutInflater.from(this).inflate(R.layout.active_otc_dialog_layout, null);
		final Button leftBtn = (Button) contentView.findViewById(R.id.active_otc_dialog_left_btn);
		final Button rightBtn = (Button) contentView.findViewById(R.id.active_otc_dialog_right_btn);
		TextView contentTV = (TextView) contentView.findViewById(R.id.active_otc_dialog_content);
		if(mProductInfo.getInterest_period().contains("92")){
			//元季融
			if("红包".equals(flag)){
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					contentTV.setText(Html.fromHtml("您使用了<font color='#31B2FE'>红包</font>，" +
							"将不能享受<font color='#31B2FE'>0.5%</font>的活动加息，是否要继续？",Html.FROM_HTML_MODE_LEGACY));
				} else {
					contentTV.setText(Html.fromHtml("您使用了<font color='#31B2FE'>红包</font>，" +
							"将不能享受<font color='#31B2FE'>0.5%</font>的活动加息，是否要继续？"));
				}
			}else if("元金币".equals(flag)){
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					contentTV.setText(Html.fromHtml("您使用了<font color='#31B2FE'>元金币</font>，" +
							"将不能享受<font color='#31B2FE'>0.5%</font>的活动加息，是否要继续？",Html.FROM_HTML_MODE_LEGACY));
				} else {
					contentTV.setText(Html.fromHtml("您使用了<font color='#31B2FE'>元金币</font>，" +
							"将不能享受<font color='#31B2FE'>0.5%</font>的活动加息，是否要继续？"));
				}
			}else if("加息券".equals(flag)){
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					contentTV.setText(Html.fromHtml("您使用了<font color='#31B2FE'>加息券</font>，" +
							"将不能享受<font color='#31B2FE'>0.5%</font>的活动加息，是否要继续？",Html.FROM_HTML_MODE_LEGACY));
				} else {
					contentTV.setText(Html.fromHtml("您使用了<font color='#31B2FE'>加息券</font>，" +
							"将不能享受<font color='#31B2FE'>0.5%</font>的活动加息，是否要继续？"));
				}
			}
		}else if(mProductInfo.getInterest_period().contains("365")){
			//元季融
			if("红包".equals(flag)){
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					contentTV.setText(Html.fromHtml("您使用了<font color='#31B2FE'>红包</font>，" +
							"将不能享受<font color='#31B2FE'>0.8%</font>的活动加息，是否要继续？",Html.FROM_HTML_MODE_LEGACY));
				} else {
					contentTV.setText(Html.fromHtml("您使用了<font color='#31B2FE'>红包</font>，" +
							"将不能享受<font color='#31B2FE'>0.8%</font>的活动加息，是否要继续？"));
				}
			}else if("元金币".equals(flag)){
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					contentTV.setText(Html.fromHtml("您使用了<font color='#31B2FE'>元金币</font>，" +
							"将不能享受<font color='#31B2FE'>0.8%</font>的活动加息，是否要继续？",Html.FROM_HTML_MODE_LEGACY));
				} else {
					contentTV.setText(Html.fromHtml("您使用了<font color='#31B2FE'>元金币</font>，" +
							"将不能享受<font color='#31B2FE'>0.8%</font>的活动加息，是否要继续？"));
				}
			}else if("加息券".equals(flag)){
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					contentTV.setText(Html.fromHtml("您使用了<font color='#31B2FE'>加息券</font>，" +
							"将不能享受<font color='#31B2FE'>0.8%</font>的活动加息，是否要继续？",Html.FROM_HTML_MODE_LEGACY));
				} else {
					contentTV.setText(Html.fromHtml("您使用了<font color='#31B2FE'>加息券</font>，" +
							"将不能享受<font color='#31B2FE'>0.8%</font>的活动加息，是否要继续？"));
				}
			}
		}

		AlertDialog.Builder builder=new AlertDialog.Builder(this, R.style.Dialog_Transparent);  //先得到构造器
		builder.setView(contentView);
		builder.setCancelable(true);
		final AlertDialog dialog = builder.create();
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				showInvestDialog();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
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
	 * 2017双11加息活动，个人用户投资元年鑫加息1.1%
	 * @param flag
	 */
	private void showNovActivityPromptDialog(String flag,double androidRateD){
		View contentView = LayoutInflater.from(this).inflate(R.layout.active_otc_dialog_layout, null);
		final Button leftBtn = (Button) contentView.findViewById(R.id.active_otc_dialog_left_btn);
		final Button rightBtn = (Button) contentView.findViewById(R.id.active_otc_dialog_right_btn);
		TextView contentTV = (TextView) contentView.findViewById(R.id.active_otc_dialog_content);
		if(mProductInfo.getInterest_period().contains("365")){
			//元季融
			if("红包".equals(flag)){
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					contentTV.setText(Html.fromHtml("您使用了<font color='#31B2FE'>红包</font>，" +
							"将不能享受<font color='#31B2FE'>" + androidRateD + "</font>的活动加息，是否要继续？",Html.FROM_HTML_MODE_LEGACY));
				} else {
					contentTV.setText(Html.fromHtml("您使用了<font color='#31B2FE'>红包</font>，" +
							"将不能享受<font color='#31B2FE'>" + androidRateD + "</font>的活动加息，是否要继续？"));
				}
			}else if("元金币".equals(flag)){
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					contentTV.setText(Html.fromHtml("您使用了<font color='#31B2FE'>元金币</font>，" +
							"将不能享受<font color='#31B2FE'>" + androidRateD + "</font>的活动加息，是否要继续？",Html.FROM_HTML_MODE_LEGACY));
				} else {
					contentTV.setText(Html.fromHtml("您使用了<font color='#31B2FE'>元金币</font>，" +
							"将不能享受<font color='#31B2FE'>" + androidRateD + "</font>的活动加息，是否要继续？"));
				}
			}else if("加息券".equals(flag)){
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					contentTV.setText(Html.fromHtml("您使用了<font color='#31B2FE'>加息券</font>，" +
							"将不能享受<font color='#31B2FE'>"+ androidRateD +"</font>的活动加息，是否要继续？",Html.FROM_HTML_MODE_LEGACY));
				} else {
					contentTV.setText(Html.fromHtml("您使用了<font color='#31B2FE'>加息券</font>，" +
							"将不能享受<font color='#31B2FE'>" + androidRateD + "</font>的活动加息，是否要继续？"));
				}
			}
		}

		AlertDialog.Builder builder=new AlertDialog.Builder(this, R.style.Dialog_Transparent);  //先得到构造器
		builder.setView(contentView);
		builder.setCancelable(true);
		final AlertDialog dialog = builder.create();
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				showInvestDialog();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
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
		LinearLayout yuanMoneyLayout = (LinearLayout) contentView.findViewById(R.id.invest_prompt_yjb_layout_detail);//元金币备注布局
		RelativeLayout hbLayout = (RelativeLayout) contentView.findViewById(R.id.invest_prompt_hb_layout_detail);//红包布局
		LinearLayout yjbjxqLayout = (LinearLayout) contentView.findViewById(R.id.invest_prompt_yjbjxq_layout_detail);//元金币和加息券使用的布局
		Button sureBtn = (Button) contentView
				.findViewById(R.id.invest_prompt_layout_surebtn);
		Button cancelBtn = (Button) contentView
				.findViewById(R.id.invest_prompt_layout_cancelbtn);
		TextView totalMoneyTV = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_total);
		TextView benjinTV = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_benjin);
		TextView yuanMoneyTV = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_yuan);
		TextView hbMoneyTV = (TextView) contentView
				.findViewById(R.id.invest_prompt_layout_hb_count);
		TextView yjbjxqText = (TextView) contentView.findViewById(R.id.invest_prompt_layout_yjbjxq_count);//元金币加息券 已使用多少
		
		//两周年回馈返现
		LinearLayout twoyearsLayoutMain = 
				(LinearLayout) contentView.findViewById(R.id.invest_prompt_layout_twoyears_layout);
		LinearLayout twoyearsLayoutTop = 
				(LinearLayout) contentView.findViewById(R.id.invest_prompt_layout_twoyears_layouttop);
		LinearLayout twoyearsLayoutBottom = 
				(LinearLayout) contentView.findViewById(R.id.invest_prompt_layout_twoyears_layoutbottom);
		TextView twoyearsInterestMoney = (TextView) contentView.findViewById(R.id.invest_prompt_layout_twoyears_interestmoney);
		TextView twoyearsInvestMoney = (TextView) contentView.findViewById(R.id.invest_prompt_layout_twoyears_investmoney);
		TextView twoyearsPrompt = (TextView) contentView.findViewById(R.id.invest_prompt_layout_twoyears_prompt);
		if(SettingsManager.checkTwoYearsTZFXActivity()){
			//两周年回馈返现活动正在进行中。。
			twoyearsLayoutMain.setVisibility(View.VISIBLE);
			if(moneyInvest < 10000){
				twoyearsLayoutTop.setVisibility(View.GONE);
				twoyearsLayoutBottom.setVisibility(View.VISIBLE);
				twoyearsInvestMoney.setText("1万");
				twoyearsPrompt.setText("元可获得返现奖励哦");
			}else if(moneyInvest >= 10000 && moneyInvest < 50000){
				twoyearsLayoutTop.setVisibility(View.VISIBLE);
				twoyearsLayoutBottom.setVisibility(View.VISIBLE);
				twoyearsInvestMoney.setText("5万");
				twoyearsPrompt.setText("元可获得更高比例返现奖励哦");
				twoyearsInterestMoney.setText(Util.double2PointDouble(moneyInvest*0.002*limitInvest/365));
			}else{
				//投资金额大于5万
				twoyearsLayoutTop.setVisibility(View.VISIBLE);
				twoyearsLayoutBottom.setVisibility(View.GONE);
				twoyearsInterestMoney.setText(Util.double2PointDouble(moneyInvest*0.004*limitInvest/365));
			}
		}
		if(bonusMoney > 0){
			//使用元金币
			yjbjxqText.setText(String.valueOf(bonusMoney));
			yuanMoneyLayout.setVisibility(View.VISIBLE);
			yjbjxqLayout.setVisibility(View.VISIBLE);
		}else{
			yuanMoneyLayout.setVisibility(View.GONE);
			yjbjxqLayout.setVisibility(View.GONE);
		}
		//使用红包
		if(hbMoney > 0){
			hbLayout.setVisibility(View.VISIBLE);
		}else{
			hbLayout.setVisibility(View.GONE);
		}
		//使用加息券
		if(jxqMoney > 0){
			yjbjxqLayout.setVisibility(View.VISIBLE);
			yjbjxqText.setText(Util.formatRate(String.valueOf(jxqMoney))+"%的加息券");
		}else{
			yjbjxqLayout.setVisibility(View.GONE);
		}
		double benjinD = moneyInvest - bonusMoney;
		totalMoneyTV.setText(moneyInvest + "");
		yuanMoneyTV.setText(bonusMoney + "");
		benjinTV.setText((int)benjinD + "");
		hbMoneyTV.setText(Util.formatRate(String.valueOf(hbMoney))+"元红包");

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
		String floatRateStr = "0";
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
		if(investMoneyInt < 100000 || mProductInfo.getInterest_period().contains("365")){
			floatRateStr = "0";
		}else if(investMoneyInt >= 100000 && investMoneyInt <300000){
			floatRateStr = "0.1";
		}else if(investMoneyInt >= 300000 && investMoneyInt < 500000){
			floatRateStr = "0.2";
		}else if(investMoneyInt >= 500000){
			floatRateStr = "0.3";
		}
		computeIncome(mProductInfo.getInterest_rate(),
				mProductInfo.getAndroid_interest_rate(), floatRateStr,(String)jxqArrowLayout.getTag(),investMoneyInt,
				(String)hbArrowLayout.getTag(R.id.tag_first),mProductInfo.getInvest_horizon());
	}

	/**
	 * 递增
	 */
	private void investMoneyIncrease() {
		String floatRateStr = "0";
		String investMoneyStr = investMoneyET.getText().toString();
		int investMoneyInt = 0;
		try {
			investMoneyInt = Integer.parseInt(investMoneyStr);
		} catch (Exception e) {
		}
		investMoneyInt += 100;
		investMoneyET.setText(investMoneyInt + "");
		if(investMoneyInt < 100000 || mProductInfo.getInterest_period().contains("365")){
			floatRateStr = "0";
		}else if(investMoneyInt >= 100000 && investMoneyInt <300000){
			floatRateStr = "0.1";
		}else if(investMoneyInt >= 300000 && investMoneyInt < 500000){
			floatRateStr = "0.2";
		}else if(investMoneyInt >= 500000){
			floatRateStr = "0.3";
		}
		computeIncome(mProductInfo.getInterest_rate(),
				mProductInfo.getAndroid_interest_rate(),floatRateStr,(String)jxqArrowLayout.getTag(), investMoneyInt,
				(String)hbArrowLayout.getTag(R.id.tag_first),mProductInfo.getInvest_horizon());
	}

	/**
	 * 选择加息券和红包的时候刷新预期收益
	 */
	private void updateInterest(){
		String floatRateStr = "0";
		String investMoneyStr = investMoneyET.getText().toString();
		int investMoneyInt = 0;
		try {
			investMoneyInt = Integer.parseInt(investMoneyStr);
		} catch (Exception e) {
		}
		if(investMoneyInt < 100000 || mProductInfo.getInterest_period().contains("365")){
			floatRateStr = "0";
		}else if(investMoneyInt >= 100000 && investMoneyInt <300000){
			floatRateStr = "0.1";
		}else if(investMoneyInt >= 300000 && investMoneyInt < 500000){
			floatRateStr = "0.2";
		}else if(investMoneyInt >= 500000){
			floatRateStr = "0.3";
		}
		computeIncome(mProductInfo.getInterest_rate(),
				mProductInfo.getAndroid_interest_rate(),floatRateStr,(String)jxqArrowLayout.getTag(), investMoneyInt,
				(String)hbArrowLayout.getTag(R.id.tag_first),mProductInfo.getInvest_horizon());
	}
	
	private void resetInvestMoneyET() {
		if (investMoneyET != null) {
			investMoneyET.setText(null);
			yjsyText.setText("0.00");
			yuanUsedTV.setText("0");
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
	 * 根据利率计算预期收益
	 * @param rateStr	基础利率
	 * @param extraRateStr  加息利率（android_interest_rate）
	 * @param floatRateStr 浮动利率
	 * @param couponRateStr 加息券利率 
	 * @param investMoney 投资金额
	 * @param  hbMoney 红包金额
	 * @param daysStr 投资期限
	 * @return
	 */
	private String computeIncome(String rateStr, String extraRateStr,String floatRateStr,String couponRateStr,
			int investMoney, String hbMoney,String daysStr) {
		double rateD = 0d;
		double extraRateD = 0d;
		double floatRateD = 0d;
		double couponRateD = 0d;
		int days = 0;
		double hbMoneyD = 0d;
		try {
			rateD = Double.parseDouble(rateStr);
		} catch (Exception e) {
		}
		try {
			floatRateD = Double.parseDouble(floatRateStr);
		} catch (Exception e) {
		}
		try {
			extraRateD = Double.parseDouble(extraRateStr);
		} catch (Exception e) {
		}
		try {
			couponRateD = Double.parseDouble(couponRateStr);
		} catch (Exception e) {
		}
		try{
			hbMoneyD = Double.parseDouble(hbMoney);
		}catch (Exception e){

		}
		days = limitInvest;
		int flagNov = SettingsManager.checkActiveStatusBySysTime(mProductInfo.getAdd_time(),
				SettingsManager.activeNov2017_StartTime,SettingsManager.activeNov2017_EndTime);//11月双十一加息活动
		if(SettingsManager.isCompanyUser(getApplicationContext())){
			nhsyText.setText(Util.double2PointDouble((rateD + floatRateD)) + "%");//享的利率
		}else{
			nhsyText.setText(Util.double2PointDouble((rateD + floatRateD + extraRateD)) + "%");//享的利率
		}

		double income = 0d;
		if(SettingsManager.isCompanyUser(getApplicationContext())){
			income = (rateD + floatRateD + couponRateD) * investMoney * days / 36500
					+ (rateD + floatRateD) * hbMoneyD * days / 36500;//算上红包的收益，红包只算产品本身的基础利率产生的收益
		}else{
			income = (rateD + extraRateD + floatRateD + couponRateD) * investMoney * days / 36500
					+ (rateD + floatRateD) * hbMoneyD * days / 36500;//算上红包的收益，红包只算产品本身的基础利率产生的收益
		}

		DecimalFormat df = new java.text.DecimalFormat("#.00");
		if (income < 1) {
			yjsyText.setText("0" + df.format(income));
		} else {
			yjsyText.setText(df.format(income));
		}
		return df.format(income);
	}

	/**
	 * 计算可使用的元金币
	 * 
	 * @param inputMoney
	 */
	private void computeUsedYuanMoney(int inputMoney) {
		int usedTemp = 0;
		if (limitInvest <= 35) {
			usedTemp = inputMoney / 500;
		} else if (limitInvest > 35 && limitInvest <= 95) {
			usedTemp = inputMoney / 200;
		} else {
			usedTemp = inputMoney / 100;
		}

		int usedYuanTotalInt = 0;// 元金币的总额
		float usedYuanTotalFloat = 0;
		try {
			if (mUserYUANAccountInfo != null) {
				usedYuanTotalFloat = Float.parseFloat(mUserYUANAccountInfo
						.getUse_coin());
				usedYuanTotalInt = (int) usedYuanTotalFloat;
			}
		} catch (Exception e) {
		}
		if (usedTemp <= usedYuanTotalInt) {
			yuanUsedTV.setText(usedTemp + "");
		} else {
			yuanUsedTV.setText(usedYuanTotalInt + "");
		}

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
	 * @param redBagLogId 红包id
	 * @param couponLogId 加息券id
	 */
	private void requestInvest(String borrowId, String investUserId,
			String money, int bonusMoney, String investFrom,
			String investFromSub, String experienceCode, String investFromHost,
			String merPriv, String redBagLogId,String couponLogId) {
		if (mLoadingDialog != null && !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncBorrowInvest asyncBorrowInvest = new AsyncBorrowInvest(
				BidZXDActivity.this, borrowId, investUserId, money, bonusMoney,
				investFrom, investFromSub, experienceCode, investFromHost,
				merPriv, redBagLogId, couponLogId,new OnBorrowInvestInter() {
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
				BidZXDActivity.this, userId, new OnCommonInter() {
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

	/**
	 * 元金币账户
	 * 
	 * @param userId
	 */
	private void requestYuanAccountInfo(String userId) {
		AsyncUserYUANAccount yuanAccountTask = new AsyncUserYUANAccount(
				BidZXDActivity.this, userId, new OnUserYUANAccountInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							sysTimeStr = baseInfo.getTime();
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if (resultCode == 0) {
								mUserYUANAccountInfo = baseInfo.getYuanAccountInfo();
								if (mUserYUANAccountInfo != null) {
									yuanBalanceTV.setText(Util.formatRate(mUserYUANAccountInfo
											.getUse_coin()));
								}
							}
						}else{
							sysTimeStr = sdf.format(new Date());
						}
						checkYjbLayoutVisible();
					}
				});
		yuanAccountTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 体验金
	 * 
	 * @param status
	 * @param userId
	 * @param putStatus
	 * @param activeTitle
	 */
	private void requestExperiencePageInfoByStatus(String status,
			String userId, String putStatus, String activeTitle) {
		AsyncExperiencePageInfo experienceTask = new AsyncExperiencePageInfo(
				BidZXDActivity.this, status, userId, putStatus, activeTitle,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							sysTimeStr = baseInfo.getTime();
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if (resultCode == 0) {
								if ("开启".equals(mProductInfo.getIs_TYJ())) {
									// 体验金开启
									tyjLayout.setVisibility(View.VISIBLE);
								} else {
									// 体验金关闭
									tyjLayout.setVisibility(View.GONE);
								}
								TYJPageInfo pageInfo = baseInfo
										.getmTYJPageInfo();
								if (pageInfo != null) {
									experienceList.addAll(pageInfo
											.getTyjList());
								}
							} else {
								tyjLayout.setVisibility(View.GONE);
							}
						}else{
							sysTimeStr = sdf.format(new Date());
						}
					}
				});
		experienceTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 获取当前用户的可用红包的列表
	 * 
	 * @param userId
	 * @param borrowType
	 */
	private void requestHBPageInfoByBorrowType(String userId, String borrowType) {
		AsyncCurrentUserRedbagList currentUserRedbagListTask = new AsyncCurrentUserRedbagList(
				BidZXDActivity.this, userId, borrowType, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							sysTimeStr = baseInfo.getTime();
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
									line4.setVisibility(View.GONE);
									line5.setVisibility(View.GONE);
								} else {
//									if (extraRateF <= 0) {
										hbLayout.setVisibility(View.VISIBLE);
										line4.setVisibility(View.VISIBLE);
										line5.setVisibility(View.VISIBLE);
										usePromptList.add("红包");
										updateUsePrompt(usePromptList);
//									} else {
//										hbLayout.setVisibility(View.GONE);
//										line5.setVisibility(View.GONE);
//									}
								}
							} else {
								hbLayout.setVisibility(View.GONE);
								line4.setVisibility(View.GONE);
								line5.setVisibility(View.GONE);
							}
						}else{
							sysTimeStr = sdf.format(new Date());
						}
					}
				});
		currentUserRedbagListTask
				.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
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
		AsyncJXQPageInfo redbagTask = new AsyncJXQPageInfo(BidZXDActivity.this, userId,useStatus,
				String.valueOf(1),String.valueOf(100), new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							sysTimeStr = baseInfo.getTime();
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler
										.obtainMessage(REQUEST_JXQ_LIST_SUCCESS);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							} 
						}else{
							sysTimeStr = sdf.format(new Date());
						}
					}
				});
		redbagTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	private void showTYJListWindow() {
		if (experienceList == null || experienceList.size() < 1) {
			Util.toastLong(BidZXDActivity.this, "没有体验金可用");
			return;
		}
		View popView = LayoutInflater.from(this).inflate(
				R.layout.tyj_list_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(BidZXDActivity.this);
		int width = screen[0];
		int height = screen[1] * 1 / 5;
		TYJListPopupwindow popwindow = new TYJListPopupwindow(
				BidZXDActivity.this, popView, width, height);
		popwindow.show(mainLayout, experienceList,
				new OnTYJWindowItemClickListener() {
					@Override
					public void onItemClickListener(View view, int position) {
						TYJInfo info = experienceList.get(position);
						tiyanjinET.setText(info.getActive_title());
					}
				});
	}

	/**
	 * 红包
	 */
	private void showHBListWindow() {
		if (hbList == null || hbList.size() < 1) {
			Util.toastLong(BidZXDActivity.this, "没有红包可用");
			return;
		}
		View popView = LayoutInflater.from(this).inflate(
				R.layout.tyj_list_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(BidZXDActivity.this);
		int width = screen[0];
		int height = screen[1] * 1 / 3 + getResources().getDimensionPixelSize(R.dimen.common_measure_100dp);
		int hbCurPosition = 0;
		try{
			hbCurPosition = (int)hbArrowLayout.getTag(R.id.tag_third);
		}catch (Exception e){
			hbCurPosition = 0;
		}
		HBListPopupwindow popwindow = new HBListPopupwindow(
				BidZXDActivity.this, popView, width, height,"选择红包 (共"+hbList.size()+"个)",hbCurPosition);
		popwindow.show(mainLayout, hbList, new OnHBWindowItemClickListener() {
			@Override
			public void onItemClickListener(View view, int position) {
				hbArrowLayout.setTag(R.id.tag_third,position);
				if (position == 0) {
					hbEditText.setText("");
					hbEditText.setTag("");
					hbArrowLayout.setTag(R.id.tag_first,"0");
					hbArrowLayout.setTag(R.id.tag_second,"0");
					updateInterest();
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
						hbEditText.setText("");
						hbArrowLayout.setTag(R.id.tag_third,0);
						Util.toastLong(BidZXDActivity.this, "您的投资金额不满足红包要求");
					}else{
						//同时置零其他几种道具
						yuanMoneyET.setText(null);
						jxqEditText.setText(null);
						jxqEditText.setTag("");
						jxqArrowLayout.setTag("0");
						jxqArrowLayout.setTag(R.id.tag_third,0);

						if(limitMoney >= 10000){
							hbEditText.setText(Html.fromHtml("<font color='#31B2FE'>"+Util.formatRate(info.getMoney())+"元</font>红包，"
									+"需投资"+Util.formatRate(String.valueOf(limitMoney/10000d))+"万元及以上可用"));
						}else{
							hbEditText.setText(Html.fromHtml("<font color='#31B2FE'>"+Util.formatRate(info.getMoney())+"元</font>红包，"
									+"需投资"+info.getNeed_invest_money()+"元及以上可用"));
						}

						hbEditText.setTag(info.getId());
						hbArrowLayout.setTag(R.id.tag_first,info.getMoney());
						hbArrowLayout.setTag(R.id.tag_second,info.getNeed_invest_money());
						updateInterest();//红包也计算利息
					}
				}
			}
		});
	}

	/**
	 * 显示加息券的列表
	 */
	private void showJXQListWindow(){
		if (jxqList == null || jxqList.size() < 1) {
			Util.toastLong(BidZXDActivity.this, "没有加息券可用");
			return;
		}
		View popView = LayoutInflater.from(this).inflate(
				R.layout.tyj_list_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(BidZXDActivity.this);
		int width = screen[0];
		int height = screen[1] * 1 / 3 + getResources().getDimensionPixelSize(R.dimen.common_measure_100dp);
		int jxqCurPosition = 0;
		try{
			jxqCurPosition = (int)jxqArrowLayout.getTag(R.id.tag_third);
		}catch (Exception e){
			jxqCurPosition = 0;
		}
		JXQListPopupwindow popwindow = new JXQListPopupwindow(
				BidZXDActivity.this, popView, width, height,"选择加息券 (共"+jxqList.size()+"个)",jxqCurPosition);
		popwindow.show(mainLayout, jxqList, new OnHBWindowItemClickListener() {
			@Override
			public void onItemClickListener(View view, int position) {
				jxqArrowLayout.setTag(R.id.tag_third,position);
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
						jxqArrowLayout.setTag(R.id.tag_third,0);
						Util.toastLong(BidZXDActivity.this, "您的投资金额不满足加息券要求");
					}else{
						//同时置零其他几种道具
						yuanMoneyET.setText(null);
						hbEditText.setText("");
						hbEditText.setTag("");
						hbArrowLayout.setTag(R.id.tag_first,"0");
						hbArrowLayout.setTag(R.id.tag_second,"0");
						hbArrowLayout.setTag(R.id.tag_third,0);

						if(limitMoney >= 10000){
							jxqEditText.setText(Html.fromHtml("<font color='#31B2FE'>"+Util.formatRate(info.getMoney())+"%</font>加息券，"
									+"需投资"+Util.formatRate(String.valueOf(limitMoney/10000))+"万元及以上可用"));
						}else{
							jxqEditText.setText(Html.fromHtml("<font color='#31B2FE'>"+Util.formatRate(info.getMoney())+"%</font>加息券，"+"需投资"+(int)(limitMoney)+"元及以上可用"));
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
	 * 体验金列表点击item时的回调
	 * 
	 * @author Administrator
	 * 
	 */
	public interface OnTYJWindowItemClickListener {
		void onItemClickListener(View view, int position);
	}

	/**
	 * 红包列表点击Item的监听事件
	 * 
	 * @author Administrator
	 * 
	 */
	public interface OnHBWindowItemClickListener {
		void onItemClickListener(View v, int position);
	}
}
