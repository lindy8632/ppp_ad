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
import com.ylfcf.ppp.async.AsyncYYYProductInfo;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectCailiaoInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
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
 * 元月盈---项目详情
 * @author Mr.liu
 *
 */
public class BorrowDetailYYYActivity extends BaseActivity implements
		OnClickListener {
	private static final String className = "BorrowDetailYYYActivity";
	private static final int REFRESH_PROGRESSBAR = 1902;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView borrowName;
	private TextView borrowRateMin;// 年化收益 基础利率
	private TextView borrowRateMax;// 加0.8的年化利率
	private TextView borrowMoney;// 募集金额
	private TextView timeFrozen;// 锁定期
	private Button bidBtn;// 立即投资
	private TextView repayType1;
	private TextView borrowBalanceTV;
	private ProgressBar progressBar;
	// private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private LinearLayout introLayout, detailsLayout, danbaoLayout, cjwtLayout,recordLayout;
	private LinearLayout extraInterestLayout;
	private TextView extraInterestText;
	private TextView jxBtn;//加息

	public ProductInfo productInfo;
	private ProjectInfo project = new ProjectInfo();// 项目信息
	private InvestRecordInfo recordInfo = null;
	
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.borrow_details_yyy_activity);
		recordInfo = (InvestRecordInfo) getIntent().getSerializableExtra("InvestRecordInfo");
		findViews();
		if(recordInfo == null){
			getProductDetailsById("", "发布", "");
		}else{
			getProductDetailsById(recordInfo.getBorrow_id(), "", "");
		}
		
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

		borrowName = (TextView) findViewById(R.id.borrow_detail_yyy_activity_borrowname);
		borrowRateMin = (TextView) findViewById(R.id.borrow_details_yyy_activity_invest_minrate);
		borrowRateMax = (TextView) findViewById(R.id.borrow_details_yyy_activity_invest_maxrate);
		borrowMoney = (TextView) findViewById(R.id.borrow_details_yyy_activity_invest_total_money);
		timeFrozen = (TextView) findViewById(R.id.borrow_details_yyy_invest_time_frozen);
		bidBtn = (Button) findViewById(R.id.borrow_detail_yyy_activity_bidBtn);
		bidBtn.setOnClickListener(this);
		repayType1 = (TextView) findViewById(R.id.borrow_details_yyy_activity_repay_type1);
		progressBar = (ProgressBar) findViewById(R.id.borrow_details_yyy_activity_pb);
		borrowBalanceTV = (TextView) findViewById(R.id.borrow_details_yyy_activity_borrow_balance_text);

		introLayout = (LinearLayout) findViewById(R.id.borrow_details_yyy_activity_intro_layout);
		introLayout.setOnClickListener(this);
		detailsLayout = (LinearLayout) findViewById(R.id.borrow_details_yyy_activity_details_layout);
		detailsLayout.setOnClickListener(this);
		danbaoLayout = (LinearLayout) findViewById(R.id.borrow_details_yyy_activity_certificate_layout);
		danbaoLayout.setOnClickListener(this);
		cjwtLayout = (LinearLayout) findViewById(R.id.borrow_details_yyy_activity_cjwt_layout);
		cjwtLayout.setOnClickListener(this);
		recordLayout = (LinearLayout) findViewById(R.id.borrow_details_yyy_activity_record_layout);
		recordLayout.setOnClickListener(this);
		extraInterestLayout = (LinearLayout) findViewById(R.id.borrow_details_yyy_extra_interest_layout);
		extraInterestText = (TextView) findViewById(R.id.borrow_details_yyy_extra_interest_text);
		jxBtn = (TextView) findViewById(R.id.borrow_details_yyy_activity_jxbtn);
		jxBtn.setOnClickListener(this);
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

	int biteIntFromRecord = 0;
	private void initData(ProductInfo info) {
		initInvestBalance(info);
		productInfo = info;
		if("未满标".equals(info.getMoney_status())){
			bidBtn.setEnabled(true);
			bidBtn.setText("立即投资");
		}else{
			bidBtn.setEnabled(false);
			bidBtn.setText("投资已结束");
		}
		borrowName.setText(info.getBorrow_name());

		// 年化利率
		String rate = info.getInterest_rate();
		String extraRate = productInfo.getAndroid_interest_rate();
		double rateF = 0f;
		try {
			rateF = Double.parseDouble(rate);
		} catch (Exception e) {
		}

		if(SettingsManager.checkActiveStatusBySysTime(productInfo.getAdd_time(),
				SettingsManager.yyyJIAXIStartTime,SettingsManager.yyyJIAXIEndTime) == 0){
			extraInterestLayout.setVisibility(View.VISIBLE);
		}else{
			extraInterestLayout.setVisibility(View.GONE);
		}
		// 锁定期
		String frozenPeriod = info.getFrozen_period();
		double rateD = 0d;
		try {
			rateD = Double.parseDouble(productInfo.getInterest_rate());
			if((int)(rateD * 10)%10 == 0){
				//说明利率是整数，没有小数
				borrowRateMin.setText((int)rateD + "");
			}else{
				borrowRateMin.setText(Util.double2PointDoubleOne(rateD));
			}
		} catch (Exception e) {
			borrowRateMin.setText(productInfo.getInterest_rate());
		}
		
		try {
			if((int)((rateD + 0.8)* 10)%10 == 0){
				//说明利率是整数，没有小数
				borrowRateMax.setText((int)(rateD + 0.8) + "");
			}else{
				borrowRateMax.setText(Util.double2PointDoubleOne(rateD + 0.8));
			}
		} catch (Exception e) {
			borrowRateMax.setText(String.valueOf(Util.double2PointDouble(rateF + 0.80)));
		}
		timeFrozen.setText(frozenPeriod);
		repayType1.setText(frozenPeriod+"天锁定期");

		double totalMoneyD = 0d;
		double investedMoneyD = 0d;//已投资的钱
		int totalMoneyI = 0;
		int investedMoneyI = 0; 
		try {
			totalMoneyD = Double.parseDouble(info.getTotal_money());
			totalMoneyI = (int) totalMoneyD;
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
		//注意int类型的范围
		try {
			if(investedMoneyI > 10000){
				biteFloat = (investedMoneyI/100)/(totalMoneyI/10000);
			}else{
				biteFloat = investedMoneyI*100/totalMoneyI;
			}
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
		case R.id.borrow_detail_yyy_activity_bidBtn:
			// 从SettingsManager中读取密码，如果为空意味着没有登录。
			boolean isLogin = !SettingsManager.getLoginPassword(
					BorrowDetailYYYActivity.this).isEmpty()
					&& !SettingsManager.getUser(BorrowDetailYYYActivity.this)
							.isEmpty();
			// isLogin = true;// 测试
			Intent intent = new Intent();
			// 1、检测是否已经登录
			if (isLogin) {
				//判断是否实名绑卡
				checkIsVerify("投资"); //在标的详情页面只判断是否实名，不判断有没有绑卡
			} else {
				// 未登录，跳转到登录页面
				intent.setClass(BorrowDetailYYYActivity.this, LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.common_topbar_left_layout:
			finish();
			break;
		// 元月盈介绍介绍
		case R.id.borrow_details_yyy_activity_intro_layout:
			// setViewPagerCurrentPosition(0);
			Intent intentProductInfo = new Intent(BorrowDetailYYYActivity.this,ProductIntroActivity.class);
			Bundle bundleIntro = new Bundle();
			bundleIntro.putSerializable("PRODUCT_INFO", productInfo);
			bundleIntro.putString("from_where", "yyy");
			intentProductInfo.putExtra("BUNDLE", bundleIntro);
			startActivity(intentProductInfo);
			break;
		// 产品详情
		case R.id.borrow_details_yyy_activity_details_layout:
			// setViewPagerCurrentPosition(1);
			Intent intentDetail = new Intent(BorrowDetailYYYActivity.this,YYYProductDetailActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putSerializable("PRODUCT_INFO", productInfo);
			intentDetail.putExtra("BUNDLE", bundle1);
			startActivity(intentDetail);
			break;
		// 相关资料
		case R.id.borrow_details_yyy_activity_certificate_layout:
			Intent intentProductData = new Intent(BorrowDetailYYYActivity.this,YYYProductDataActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putSerializable("PRODUCT_INFO", productInfo);
			bundle2.putSerializable("PROJECT_INFO", project);
			intentProductData.putExtra("BUNDLE", bundle2);
			startActivity(intentProductData);
			break;
		//常见问题
		case R.id.borrow_details_yyy_activity_cjwt_layout:
			Intent intentCJWT = new Intent(BorrowDetailYYYActivity.this,YYYProductCJWTActivity.class);
			intentCJWT.putExtra("PRODUCT_INFO", productInfo);
			intentCJWT.putExtra("from_where", "yyy");
			startActivity(intentCJWT);
			break;
		// 投资记录
		case R.id.borrow_details_yyy_activity_record_layout:
			Intent intentProductRecord = new Intent(BorrowDetailYYYActivity.this,YYYProductRecordActivity.class);
			Bundle bundle3 = new Bundle();
			bundle3.putSerializable("PRODUCT_INFO", productInfo);
			intentProductRecord.putExtra("BUNDLE", bundle3);
			startActivity(intentProductRecord);
			break;
		case R.id.borrow_details_yyy_activity_jxbtn:
			//跳转到元月盈加息活动banner
			Intent intentBanner = new Intent(BorrowDetailYYYActivity.this,BannerTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setArticle_id("70");
			bannerInfo.setLink_url("http://wap.ylfcf.com/home/index/yyyjx.html#app");
			intentBanner.putExtra("BannerInfo", bannerInfo);
			startActivity(intentBanner);
			finish();
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
		bidBtn.setEnabled(false);
		RequestApis.requestIsVerify(BorrowDetailYYYActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					Intent intent = new Intent();
						//那么直接跳到充值页面
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(BorrowDetailYYYActivity.this, BidYYYActivity.class);
						startActivity(intent);
						bidBtn.setEnabled(true);
				}else{
					//用户没有实名
					Intent intent = new Intent(BorrowDetailYYYActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
					bidBtn.setEnabled(true);
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
		RequestApis.requestIsBinding(BorrowDetailYYYActivity.this, SettingsManager.getUserId(getApplicationContext()), "宝付", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//用户已经绑卡
					if("元月盈投资".equals(type)){
						//那么直接跳到充值页面
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(BorrowDetailYYYActivity.this, BidYYYActivity.class);
					}
				}else{
					//用户还没有绑卡
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					intent.setClass(BorrowDetailYYYActivity.this, BindCardActivity.class);
				}
				startActivity(intent);
				bidBtn.setEnabled(true);
			}
		});
	}
	
	/**
	 * 解析打码的材料的图片
	 * @param info
	 */
	private void parseProductCailiaoMarkImg(ProductInfo info){
		String imageNames[] = info.getImgs_name().split("\\|");
		ArrayList<ProjectCailiaoInfo> cailiaoListTemp = new ArrayList<ProjectCailiaoInfo>();
		ArrayList<ProjectCailiaoInfo> cailiaoList = new ArrayList<ProjectCailiaoInfo>();
		String materials = info.getMaterials().replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;", "\"");//打码的图片
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
	private void parseProductCailiaoNomarkImg(ProductInfo info){
		String imageNames[] = info.getImgs_name().split("\\|");
		ArrayList<ProjectCailiaoInfo> cailiaoListTemp = new ArrayList<ProjectCailiaoInfo>();
		ArrayList<ProjectCailiaoInfo> cailiaoList = new ArrayList<ProjectCailiaoInfo>();
		String materials = info.getMaterials_nomark().replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;", "\"");//没打码的图片
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
	 * 根据产品id获取产品详情
	 * 
	 * @param borrowStatus
	 */
	private void getProductDetailsById(String id, String borrowStatus,String moneyStatus) {
		if (mLoadingDialog != null && !mLoadingDialog.isShowing()
				&& !isFinishing()) {
			mLoadingDialog.show();
		}
		AsyncYYYProductInfo task = new AsyncYYYProductInfo(BorrowDetailYYYActivity.this,
				id, borrowStatus, moneyStatus, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null){
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								productInfo = baseInfo.getProductPageInfo().getProductList().get(0);
								productInfo.setBorrow_type("元月盈");
								initData(productInfo);
								parseProductCailiaoNomarkImg(productInfo);
								parseProductCailiaoMarkImg(productInfo);
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
