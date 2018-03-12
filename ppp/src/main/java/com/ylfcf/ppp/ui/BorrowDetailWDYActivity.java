package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncProjectDetails;
import com.ylfcf.ppp.async.AsyncWDYBorrowDetail;
import com.ylfcf.ppp.async.AsyncWDYSelectone;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectCailiaoInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.fragment.ProductInfoFragment.OnProductInfoListener;
import com.ylfcf.ppp.fragment.ProductSafetyFragment.OnProductSafetyListener;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * 稳定盈详情页面
 * @author Mr.liu
 *
 */
public class BorrowDetailWDYActivity extends BaseActivity implements OnClickListener{
	private static final String className = "BorrowDetailWDYActivity";
	private static final int REFRESH_PROGRESSBAR = 1902;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView borrowName;
	private TextView borrowRate;// 年化收益 浮动利率的最小利率
	private TextView borrowMoney;// 募集金额
	private TextView timeLimit;// 期限
	private Button bidBtn;// 立即投资
	private TextView repayType1;
	private TextView borrowBalanceTV;
	private TextView qitouMoneyTv;
//	private TextView reInvestDay;//每月投资日
	private ProgressBar progressBar;
	// private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private LinearLayout introLayout, detailsLayout, cailiaoLayout, quesLayout,investRecordLayout;
	private LinearLayout extraInterestLayout;//加息利率
	private TextView extraInterestText;

	public InvestRecordInfo recordInfo;
	private ProductInfo mProductInfo;
	private ProjectInfo project;// 项目信息
	private OnProductInfoListener productInfoListener;
	private OnProductSafetyListener productSafetyListener;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_PROGRESSBAR:
				int progress_ = (Integer) msg.obj;
				progressbarIncrease(progress_);
				break;

			default:
				break;
			}
		}
	};

	public void setOnProductInfoListener(OnProductInfoListener listener) {
		this.productInfoListener = listener;
	}

	public void setOnProductSafetyListener(OnProductSafetyListener listener) {
		this.productSafetyListener = listener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.borrow_detail_wdy_activity);
		Intent intent = getIntent();
		recordInfo = (InvestRecordInfo) intent
				.getSerializableExtra("InvestRecordInfo");
		findViews();
		if (recordInfo != null) {
			//从投资记录跳转过来
			getWDYDetailById(recordInfo.getBorrow_id());
		} else {
			// 从首页跳转过来。
			getWDYBorrowDetails("发布", "是");
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	@SuppressWarnings("deprecation")
	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("产品详情");

		borrowName = (TextView) findViewById(R.id.borrow_detail_wdy_activity_borrowname);
		borrowRate = (TextView) findViewById(R.id.borrow_details_wdy_activity_invest_rate);
		borrowMoney = (TextView) findViewById(R.id.borrow_details_wdy_activity_invest_total_money);
		timeLimit = (TextView) findViewById(R.id.borrow_details_wdy_invest_time_limit);
		bidBtn = (Button) findViewById(R.id.borrow_detail_wdy_activity_bidBtn);
		bidBtn.setOnClickListener(this);
		repayType1 = (TextView) findViewById(R.id.borrow_details_wdy_activity_repay_type1);
		progressBar = (ProgressBar) findViewById(R.id.borrow_details_wdy_activity_pb);
		borrowBalanceTV = (TextView) findViewById(R.id.borrow_details_wdy_activity_borrow_balance_text);
		qitouMoneyTv = (TextView)findViewById(R.id.borrow_detail_wdy_activity_qitou);
//		reInvestDay = (TextView) findViewById(R.id.borrow_details_wdy_activity_reinvest_date);

		introLayout = (LinearLayout) findViewById(R.id.borrow_details_wdy_activity_intro_layout);
		introLayout.setOnClickListener(this);
		detailsLayout = (LinearLayout) findViewById(R.id.borrow_details_wdy_activity_details_layout);
		detailsLayout.setOnClickListener(this);
		cailiaoLayout = (LinearLayout) findViewById(R.id.borrow_details_wdy_activity_certificate_layout);
		cailiaoLayout.setOnClickListener(this);
		quesLayout = (LinearLayout) findViewById(R.id.borrow_details_wdy_activity_ques_layout);
		quesLayout.setOnClickListener(this);
		investRecordLayout = (LinearLayout) findViewById(R.id.borrow_details_wdy_activity_record_layout);
		investRecordLayout.setOnClickListener(this);
		extraInterestLayout = (LinearLayout) findViewById(R.id.borrow_details_wdy_extra_interest_layout);
		extraInterestText = (TextView) findViewById(R.id.borrow_details_wdy_extra_interest_text);
	}

	int biteIntFromProduct = 0;
	private void initData() {
		if(mProductInfo == null){
			return;
		}
		initInvestBalance(mProductInfo);
		if("未满标".equals(mProductInfo.getMoney_status())){
			bidBtn.setEnabled(true);
			bidBtn.setText("立即投资");
		}else{
			bidBtn.setEnabled(false);
			bidBtn.setText("投资已结束"); 
		}
		if(mProductInfo.getBorrow_name() != null && !"".equals(mProductInfo.getBorrow_name())){
			borrowName.setText(mProductInfo.getBorrow_name());
		}else{
			borrowName.setText("薪盈计划-"+mProductInfo.getBorrow_period()+"期");
		}
		
		// 年化利率
		double rateD = 0d;
		try {
			rateD = Double.parseDouble(mProductInfo.getInterest_rate());
			if((int)(rateD * 10)%10 == 0){
				//说明利率是整数，没有小数
				borrowRate.setText((int)rateD + "");
			}else{
				borrowRate.setText(Util.double2PointDoubleOne(rateD));
			}
		} catch (Exception e) {
			borrowRate.setText(mProductInfo.getInterest_rate());
		}
		String extraRate = mProductInfo.getAndroid_interest_rate();
		float extraRateF = 0f;
		try {
			extraRateF = Float.parseFloat(extraRate);
		} catch (Exception e) {
		}
		if(extraRateF > 0){
			extraInterestLayout.setVisibility(View.VISIBLE);
			extraInterestText.setText("+"+extraRateF);
		}else{
			extraInterestLayout.setVisibility(View.GONE);
		}
		timeLimit.setText(mProductInfo.getInterest_period_month());
		repayType1.setText(mProductInfo.getRepay_way());
		double investLowestD = 0d;
		double investHighestD = 0d;
		try {
			investLowestD = Double.parseDouble(mProductInfo.getInvest_lowest());
			investHighestD = Double.parseDouble(mProductInfo.getInvest_highest());
		} catch (Exception e) {
		}
		qitouMoneyTv.setText((int)investLowestD+" - " + (int)investHighestD + "元（注：首次加入时确定，后续月份将不可更改）");
//		reInvestDay.setText(getResources().getString(R.string.wdy_invest_day).replace("DAY", mProductInfo.getInvest_day()));
		double totalMoneyL = 0d;
		int totalMoneyI = 0;
		try {
			totalMoneyL = Double.parseDouble(mProductInfo.getTotal_money());
			totalMoneyI = (int) totalMoneyL;
		} catch (Exception e) {
		}
		if(totalMoneyI < 10000){
			borrowMoney.setText(Util.double2PointDoubleOne(totalMoneyI / 10000d) + "");
		}else{
			borrowMoney.setText((totalMoneyI / 10000) + "");
		}
		float investMoneyF = 0f;
		float totalMoneyF = 0f;
		float biteFloat = 0f;
		try {
			investMoneyF = Float.parseFloat(mProductInfo.getInvest_money());
			totalMoneyF = Float.parseFloat(mProductInfo.getTotal_money());
			biteFloat = (investMoneyF/totalMoneyF) * 100;
			biteIntFromProduct = (int) biteFloat;
		} catch (Exception e) {
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				progressbarIncrease(biteIntFromProduct*100);
			}
		}, 500L);
	}

	int biteIntFromRecord = 0;
	private void initDataFromRecord(ProductInfo info) {
		initInvestBalance(info);
		mProductInfo = info;
		if("未满标".equals(info.getMoney_status())){
			bidBtn.setEnabled(true);
			bidBtn.setText("立即投资");
		}else{
			bidBtn.setEnabled(false);
			bidBtn.setText("投资已结束");
		}
		if(mProductInfo.getBorrow_name() != null && !"".equals(mProductInfo.getBorrow_name())){
			borrowName.setText(mProductInfo.getBorrow_name());
		}else{
			borrowName.setText("薪盈计划-"+mProductInfo.getBorrow_period()+"期");
		}

		// 年化利率
		double rateD = 0d;
		try {
			rateD = Double.parseDouble(mProductInfo.getInterest_rate());
			if((int)(rateD * 10)%10 == 0){
				//说明利率是整数，没有小数
				borrowRate.setText((int)rateD + "");
			}else{
				borrowRate.setText(Util.double2PointDoubleOne(rateD));
			}
		} catch (Exception e) {
			borrowRate.setText(mProductInfo.getInterest_rate());
		}
		String extraRate = mProductInfo.getAndroid_interest_rate();
		float extraRateF = 0f;
		try {
			extraRateF = Float.parseFloat(extraRate);
		} catch (Exception e) {
		}
		if(extraRateF > 0){
			extraInterestLayout.setVisibility(View.VISIBLE);
			extraInterestText.setText("+"+extraRateF);
		}else{
			extraInterestLayout.setVisibility(View.GONE);
		}
		timeLimit.setText(mProductInfo.getInterest_period_month());
		repayType1.setText(info.getRepay_way());
		
		double investLowestD = 0d;
		double investHighestD = 0d;
		try {
			investLowestD = Double.parseDouble(mProductInfo.getInvest_lowest());
			investHighestD = Double.parseDouble(mProductInfo.getInvest_highest());
		} catch (Exception e) {
		}
		qitouMoneyTv.setText((int)investLowestD + " - "+(int)investHighestD+"元（注：首次加入时确定，后续月份将不可更改）");
//		reInvestDay.setText(getResources().getString(R.string.wdy_invest_day).replace("DAY", mProductInfo.getInvest_day()));
		
		double totalMoneyL = 0d;
		double investedMoneyD = 0d;//已投资的钱
		int totalMoneyI = 0;
		int investedMoneyI = 0; 
		try {
			totalMoneyL = Double.parseDouble(info.getTotal_money());
			totalMoneyI = (int) totalMoneyL;
			investedMoneyD = Double.parseDouble(info.getInvest_money());
			investedMoneyI = (int)investedMoneyD;
		} catch (Exception e) {
		}
		if(totalMoneyI < 10000){
			borrowMoney.setText(Util.double2PointDoubleOne(totalMoneyI / 10000d) + "");
		}else{
			borrowMoney.setText((totalMoneyI / 10000) + "");
		}
		float biteFloat = 0f;
		try {
			biteFloat = investedMoneyI*100/totalMoneyI;
			biteIntFromRecord = (int) biteFloat;
		} catch (Exception e) {
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				progressbarIncrease(biteIntFromRecord*100);
			}
		}, 500L);
	}
	
	int increaseInt = 0;//增量
	int progressTemp = 0;
	private void progressbarIncrease(int progress){
		progressBar.setProgress(progressTemp);
		increaseInt = progress / 50;
		if(progressTemp >= progress){
			return;
		}
		progressTemp += increaseInt;
		if(progressTemp >= progress){
			progressTemp = progress;
		}
		Message msg = handler.obtainMessage(REFRESH_PROGRESSBAR);
		msg.obj = progress;
		handler.sendMessageDelayed(msg, 10L);
	}
	
	private void initInvestBalance(ProductInfo info){
		if(info == null){
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
        	totalMoneyI = (int)totalMoneyL;
        	investMoneyI = (int)investMoneyL;
        	borrowBalance = totalMoneyI - investMoneyI;
		} catch (Exception e) {
		}
        
        borrowBalanceTV.setText(borrowBalance+"");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.borrow_detail_wdy_activity_bidBtn:
			// 从SettingsManager中读取密码，如果为空意味着没有登录。
			boolean isLogin = !SettingsManager.getLoginPassword(
					BorrowDetailWDYActivity.this).isEmpty()
					&& !SettingsManager.getUser(BorrowDetailWDYActivity.this)
							.isEmpty();
			// 1、检测是否已经登录
			if (isLogin) {
				//判断是否实名绑卡
				checkIsVerify("投资");
			} else {
				// 未登录，跳转到登录页面
				Intent intent = new Intent();
				intent.setClass(BorrowDetailWDYActivity.this, LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.common_topbar_left_layout:
			finish();
			break;
		// 项目介绍
		case R.id.borrow_details_wdy_activity_intro_layout:
			Intent intentProductInfo = new Intent(BorrowDetailWDYActivity.this,ProductIntroActivity.class);
			Bundle bundleIntro = new Bundle();
			bundleIntro.putSerializable("PRODUCT_INFO", mProductInfo);
			bundleIntro.putString("from_where", "wdy");
			intentProductInfo.putExtra("BUNDLE", bundleIntro);
			startActivity(intentProductInfo);
			break;
		// 产品详情
		case R.id.borrow_details_wdy_activity_details_layout:
			Intent intentSaft = new Intent(BorrowDetailWDYActivity.this,WDYProductDetailActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putSerializable("PROJECT_INFO", project);
			bundle1.putSerializable("PRODUCT_INFO", mProductInfo);
			intentSaft.putExtra("BUNDLE", bundle1);
			startActivity(intentSaft);
			break;
		// 相关资料
		case R.id.borrow_details_wdy_activity_certificate_layout:
			Intent intentProductData = new Intent(BorrowDetailWDYActivity.this,ProductDataActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putSerializable("PROJECT_INFO", project);
			bundle2.putSerializable("PRODUCT_INFO", mProductInfo);
			bundle2.putString("from_where", "wdy");
			intentProductData.putExtra("BUNDLE", bundle2);
			startActivity(intentProductData);
			break;
		// 常见问题
		case R.id.borrow_details_wdy_activity_ques_layout:
			Intent intentCJWT = new Intent(BorrowDetailWDYActivity.this,YYYProductCJWTActivity.class);
			intentCJWT.putExtra("from_where", "wdy");
			intentCJWT.putExtra("PRODUCT_INFO", mProductInfo);
			startActivity(intentCJWT);
			break;
		case R.id.borrow_details_wdy_activity_record_layout:
			Intent intentProductRecord = new Intent(BorrowDetailWDYActivity.this,YYYProductRecordActivity.class);
			Bundle bundle3 = new Bundle();
			bundle3.putSerializable("PRODUCT_INFO", mProductInfo);
			intentProductRecord.putExtra("BUNDLE", bundle3);
			startActivity(intentProductRecord);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 解析打码的材料的图片
	 * @param info
	 */
	private void parseProjectCailiaoMarkImg(ProjectInfo info){
		if(info == null)
			return;
		String imageNames[] = info.getImgs_name().split("\\|");
		ArrayList<ProjectCailiaoInfo> cailiaoListTemp = new ArrayList<ProjectCailiaoInfo>();
		ArrayList<ProjectCailiaoInfo> cailiaoList = new ArrayList<ProjectCailiaoInfo>();
		String materials = info.getMaterials();//打码的图片
		Document doc = Jsoup.parse(materials);
		Elements ele=doc.getElementsByTag("p");
		for(Element e :ele){
			ProjectCailiaoInfo cailiaoInfo = null;
			String imageUrl = e.getElementsByTag("img").attr("src");
			String imageTitle = e.getElementsByTag("img").attr("title");
			System.out.println("图片链接："+imageUrl);
			System.out.println("图片标题："+imageTitle);
			if(imageUrl != null && !"".equals(imageUrl)){
				cailiaoInfo = new ProjectCailiaoInfo();
				cailiaoInfo.setImgURL(imageUrl);
				cailiaoListTemp.add(cailiaoInfo);
			}
        }
		
		for(int i=0;i<cailiaoListTemp.size();i++){
			ProjectCailiaoInfo cailiao = cailiaoListTemp.get(i);
			if(i<imageNames.length){
				cailiao.setTitle(imageNames[i]);
			}
			cailiaoList.add(cailiao);
		}
		
		if(project != null){
			project.setCailiaoMarkList(cailiaoList);
		}
	}
	
	/**
	 * 解析没打码的材料的图片
	 * @param info
	 */
	private void parseProjectCailiaoNomarkImg(ProjectInfo info){
		if(info == null)
			return;
		String imageNames[] = info.getImgs_name().split("\\|");
		ArrayList<ProjectCailiaoInfo> cailiaoListTemp = new ArrayList<ProjectCailiaoInfo>();
		ArrayList<ProjectCailiaoInfo> cailiaoList = new ArrayList<ProjectCailiaoInfo>();
		String materials = info.getMaterials_nomark();//没打码的图片
		Document doc = Jsoup.parse(materials);
		Elements ele=doc.getElementsByTag("p");
		for(Element e :ele){
			ProjectCailiaoInfo cailiaoInfo = null;
			String imageUrl = e.getElementsByTag("img").attr("src");
			String imageTitle = e.getElementsByTag("img").attr("title");
			System.out.println("图片链接："+imageUrl);
			System.out.println("图片标题："+imageTitle);
			if(imageUrl != null && !"".equals(imageUrl)){
				cailiaoInfo = new ProjectCailiaoInfo();
				cailiaoInfo.setImgURL(imageUrl);
				cailiaoListTemp.add(cailiaoInfo);
			}
        }
		
		for(int i=0;i<cailiaoListTemp.size();i++){
			ProjectCailiaoInfo cailiao = cailiaoListTemp.get(i);
			if(i<imageNames.length){
				cailiao.setTitle(imageNames[i]);
			}
			cailiaoList.add(cailiao);
		}
		
		if(project != null){
			project.setCailiaoNoMarkList(cailiaoList);
		}
	}

	/**
	 * 验证用户是否已经认证
	 * @param type “充值”,“提现”
	 */
	private void checkIsVerify(final String type){
		bidBtn.setEnabled(false);
		RequestApis.requestIsVerify(BorrowDetailWDYActivity.this, SettingsManager.getUserId(getApplicationContext()), new Inter.OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//用户已经实名
					Intent intent = new Intent();
					intent.putExtra("PRODUCT_INFO", mProductInfo);
					intent.setClass(BorrowDetailWDYActivity.this, BidWDYActivity.class);
					startActivity(intent);
				}else{
					//用户没有实名
					Intent intent = new Intent(BorrowDetailWDYActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					bundle.putSerializable("PRODUCT_INFO", mProductInfo);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
				}
				bidBtn.setEnabled(true);
			}
			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}

	/**
	 * 获取项目详情
	 * 
	 * @param id
	 */
	private void getProjectDetails(String id) {
		if (mLoadingDialog != null && !mLoadingDialog.isShowing()
				&& !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncProjectDetails task = new AsyncProjectDetails(
				BorrowDetailWDYActivity.this, id, new Inter.OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							project = baseInfo.getmProjectInfo();
							parseProjectCailiaoMarkImg(project);
							parseProjectCailiaoNomarkImg(project);
							if (productInfoListener != null) {
								productInfoListener.back(project);
							}
							if (productSafetyListener != null) {
								productSafetyListener.back(project);
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 获取最新一期的稳定赢产品详情
	 * @param borrowStatus
	 * @param isShow
	 */
	private void getWDYBorrowDetails(String borrowStatus,String isShow){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncWDYBorrowDetail wdyTask = new AsyncWDYBorrowDetail(BorrowDetailWDYActivity.this, borrowStatus, isShow, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								try {
									ProductInfo info = baseInfo.getProductPageInfo().getProductList().get(0);
									mProductInfo = info;
									getProjectDetails(info.getProject_id());
									initData();
								} catch (Exception e) {
								}
								
							}
						}
					}
				});
		wdyTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 根据产品id获取产品详情
	 * 
	 * @param borrowId
	 */
	private void getWDYDetailById(String borrowId) {
		if (mLoadingDialog != null && !mLoadingDialog.isShowing()
				&& !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncWDYSelectone task = new AsyncWDYSelectone(BorrowDetailWDYActivity.this,
				borrowId, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								ProductInfo info = baseInfo.getmProductInfo();
								initDataFromRecord(info);
								getProjectDetails(info.getProject_id());
							}else{
								mLoadingDialog.dismiss();
							}
						}else{
							mLoadingDialog.dismiss();
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
}
