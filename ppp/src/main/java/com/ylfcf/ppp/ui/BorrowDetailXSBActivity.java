package com.ylfcf.ppp.ui;

import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncProductInfo;
import com.ylfcf.ppp.async.AsyncProjectDetails;
import com.ylfcf.ppp.async.AsyncXSBDetails;
import com.ylfcf.ppp.async.AsyncXSBIscanbuy;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectCailiaoInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.fragment.ProductInfoFragment.OnProductInfoListener;
import com.ylfcf.ppp.fragment.ProductSafetyFragment.OnProductSafetyListener;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 新手标详情
 * @author Mr.liu
 *
 */
public class BorrowDetailXSBActivity extends BaseActivity implements
			OnClickListener{
	private static final String className = "BorrowDetailXSBActivity";
	private static final int REFRESH_PROGRESSBAR = 1902;
	private static final int REQUEST_XSBDETAILS_WHAT = 5704;
	private static final int REQUEST_XSBDETAILS_SUCCESS = 5705;
	private static final int REQUEST_XSBDETAILS_FAILE = 5706;

	private static final int REQUEST_PRODUCT_DETAILS_BYID_WHAT = 5707;//根据id获取产品详情
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView borrowName;
	private TextView borrowRate;
	private TextView borrowMoney;
	private TextView timeLimit;
	private Button bidBtn;
	private TextView repayType1;
	private TextView repayType2;
	private TextView borrowBalanceTV;
	private TextView profitTv;
	private TextView highestMoneyTv;
	private ProgressBar progressBar;
	// private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private LinearLayout introLayout,infoLayout, safeLayout, zizhiLayout, recordLayout;

	public ProductInfo productInfo;
	public InvestRecordInfo recordInfo;
	private ProjectInfo project;
	private OnProductInfoListener productInfoListener;
	private OnProductSafetyListener productSafetyListener;
	private AlertDialog.Builder builder = null;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_PROGRESSBAR:
				int progress_ = (Integer) msg.obj;
				progressbarIncrease(progress_);
				break;
			case REQUEST_XSBDETAILS_WHAT:
				requestXSBDetails("发布");
				break;
			case REQUEST_XSBDETAILS_SUCCESS:
				productInfo = (ProductInfo) msg.obj;
				if (productInfo != null) {
					getProjectDetails(productInfo.getProject_id());
					if(recordInfo != null){
						initDataFromRecord(productInfo);
					}else{
						initDataFromProductList();
					}
				}
				break;
			case REQUEST_XSBDETAILS_FAILE:
				String errorMsg = (String) msg.obj;
				Util.toastLong(BorrowDetailXSBActivity.this, errorMsg);
				break;
			case REQUEST_PRODUCT_DETAILS_BYID_WHAT:
				getProductDetailsById(recordInfo.getBorrow_id(), "","");
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
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.borrow_details_xsb_activity);
		recordInfo = (InvestRecordInfo) getIntent()
				.getSerializableExtra("InvestRecordInfo");
		builder = new AlertDialog.Builder(BorrowDetailXSBActivity.this,
				R.style.Dialog_Transparent);
		findViews();
		if(recordInfo != null){
			handler.sendEmptyMessage(REQUEST_PRODUCT_DETAILS_BYID_WHAT);
		}else{
			handler.sendEmptyMessage(REQUEST_XSBDETAILS_WHAT);
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

		borrowName = (TextView) findViewById(R.id.borrow_detail_xsb_activity_borrowname);
		borrowRate = (TextView) findViewById(R.id.borrow_details_xsb_activity_invest_rate);
		borrowMoney = (TextView) findViewById(R.id.borrow_details_xsb_activity_invest_total_money);
		timeLimit = (TextView) findViewById(R.id.borrow_details_xsb_invest_time_limit);
		bidBtn = (Button) findViewById(R.id.borrow_detail_xsb_activity_bidBtn);
		bidBtn.setOnClickListener(this);
		repayType1 = (TextView) findViewById(R.id.borrow_details_xsb_activity_repay_type1);
		repayType2 = (TextView) findViewById(R.id.borrow_details_xsb_activity_repay_type2);
		progressBar = (ProgressBar) findViewById(R.id.borrow_details_xsb_activity_pb);
		borrowBalanceTV = (TextView) findViewById(R.id.borrow_details_xsb_activity_borrow_balance_text);
		profitTv = (TextView) findViewById(R.id.borrow_details_xsb_activity_profit);
		highestMoneyTv = (TextView)findViewById(R.id.borrow_detail_xsb_activity_highest);

		introLayout = (LinearLayout) findViewById(R.id.borrow_details_xsb_activity_intro_layout);
		introLayout.setOnClickListener(this);
		infoLayout = (LinearLayout) findViewById(R.id.borrow_details_xsb_activity_info_layout);
		infoLayout.setOnClickListener(this);
		safeLayout = (LinearLayout) findViewById(R.id.borrow_details_xsb_activity_safe_layout);
		safeLayout.setOnClickListener(this);
		zizhiLayout = (LinearLayout) findViewById(R.id.borrow_details_xsb_activity_certificate_layout);
		zizhiLayout.setOnClickListener(this);
		recordLayout = (LinearLayout) findViewById(R.id.borrow_details_xsb_activity_record_layout);
		recordLayout.setOnClickListener(this);
	}
	
	/**
	 * @param type
	 * @param msg
	 */
	private void showMsgDialog(Context context,final String type,String msg){
		View contentView = LayoutInflater.from(context)
				.inflate(R.layout.borrow_details_msg_dialog, null);
		final Button sureBtn = (Button) contentView
				.findViewById(R.id.borrow_details_msg_dialog_surebtn);
		final TextView msgTV = (TextView) contentView
				.findViewById(R.id.borrow_details_msg_dialog_msg);
		final ImageView delBtn = (ImageView) contentView
				.findViewById(R.id.borrow_details_msg_dialog_delete);
		if("不能购买新手标".equals(type)){
			sureBtn.setVisibility(View.GONE);
		}else{
			sureBtn.setVisibility(View.VISIBLE);
		}
		msgTV.setText(msg);
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if("实名认证".equals(type)){
					intent.setClass(BorrowDetailXSBActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("type", "新手标投资");
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
				}else if("绑卡".equals(type)){
					Bundle bundle = new Bundle();
					bundle.putString("type", "新手标投资");
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					intent.setClass(BorrowDetailXSBActivity.this, BindCardActivity.class);
					startActivity(intent);
				}
				dialog.dismiss();
			}
		});
		delBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth() * 6 / 7;
		lp.height = display.getHeight()/3;
		dialog.getWindow().setAttributes(lp);
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

	int biteIntFromProduct = 0;
	private void initDataFromProductList() {
		initInvestBalance(productInfo);
		if("未满标".equals(productInfo.getMoney_status())){
			bidBtn.setEnabled(true);
			bidBtn.setText("立即投资");
		}else{
			bidBtn.setEnabled(false);
			bidBtn.setText("投资已结束");
		}
		borrowName.setText(productInfo.getBorrow_name());

		//年化利率
		String rate = productInfo.getInterest_rate();
		String extraRate = productInfo.getAndroid_interest_rate();
		float extraRateF = 0f;
		try {
			extraRateF = Float.parseFloat(extraRate);
		} catch (Exception e) {
		}
		//投资期限
		String horizon = productInfo.getInvest_horizon().replace("天", "");
		int horizonInt = 0;
		try {
			horizonInt = Integer.parseInt(horizon);
		} catch (Exception e) {
			horizonInt = 0;
		}
		
		double rateD = 0d;
		try {
			rateD = Double.parseDouble(productInfo.getInterest_rate());
			if((int)(rateD * 10)%10 == 0){
				//说明利率是整数，没有小数
				borrowRate.setText((int)rateD + "");
			}else{
				borrowRate.setText(Util.double2PointDoubleOne(rateD));
			}
		} catch (Exception e) {
			borrowRate.setText(productInfo.getInterest_rate());
		}
		
		timeLimit.setText(horizon);
		repayType1.setText(productInfo.getRepay_way());
		repayType2.setText(productInfo.getRepay_way());
		highestMoneyTv.setText(productInfo.getInvest_highest()+"元");

		double totalMoneyL = 0d;
		int totalMoneyI = 0;
		try {
			totalMoneyL = Double.parseDouble(productInfo.getTotal_money());
			totalMoneyI = (int) totalMoneyL;
		} catch (Exception e) {
		}
		if(totalMoneyI < 10000){
			borrowMoney.setText(Util.double2PointDoubleOne(totalMoneyI / 10000d) + "");
		}else{
			borrowMoney.setText((totalMoneyI / 10000) + "");
		}
		
		String bite = productInfo.getBite();
		float biteFloat = 0f;
		
		if (bite != null) {
			bite = bite.replace("%", "");
		}
		try {
			biteFloat = Float.parseFloat(bite) * 100;
			biteIntFromProduct = (int) biteFloat;
		} catch (Exception e) {
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				progressbarIncrease(biteIntFromProduct);
			}
		}, 500L);
		float rateF = 0f;
		try {
			rateF = Float.parseFloat(rate);
		} catch (Exception e) {
		}
		profitTv.setText(new DecimalFormat("#.00").format((rateF + extraRateF) * 30/365*horizonInt) + "");
	}

	int biteIntFromRecord = 0;
	private void initDataFromRecord(ProductInfo info) {
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
		float extraRateF = 0f;
		try {
			extraRateF = Float.parseFloat(extraRate);
		} catch (Exception e) {
		}
		// 投资期限
		String horizon = info.getInvest_horizon().replace("天", "");
		int horizonInt = Integer.parseInt(horizon);
		double rateD = 0d;
		try {
			rateD = Double.parseDouble(productInfo.getInterest_rate());
			if((int)(rateD * 10)%10 == 0){
				//说明利率是整数，没有小数
				borrowRate.setText((int)rateD + "");
			}else{
				borrowRate.setText(Util.double2PointDoubleOne(rateD));
			}
		} catch (Exception e) {
			borrowRate.setText(productInfo.getInterest_rate());
		}
		
		timeLimit.setText(horizon);
		repayType1.setText(info.getRepay_way());
		repayType2.setText(info.getRepay_way());
		highestMoneyTv.setText(info.getInvest_highest()+"元");

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
		
		float rateF = 0f;
		try {
			rateF = Float.parseFloat(rate);
		} catch (Exception e) {
		}
		profitTv.setText(new DecimalFormat("#.00").format((rateF + extraRateF) * 30/365*horizonInt) + "");
	}
	
	int increaseInt = 0;
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
		case R.id.borrow_detail_xsb_activity_bidBtn:
			// 从SettingsManager中读取密码，如果为空意味着没有登录。
			boolean isLogin = !SettingsManager.getLoginPassword(
					BorrowDetailXSBActivity.this).isEmpty()
					&& !SettingsManager.getUser(BorrowDetailXSBActivity.this)
							.isEmpty();
			bidBtn.setEnabled(false);
			Intent intent = new Intent();
			//  1、检测是否已经登录
			if (isLogin) {
				isCanbuyXSB(SettingsManager.getUserId(getApplicationContext()), productInfo.getId());
			} else {
				// 未登录，跳转到登录页面
				intent.setClass(BorrowDetailXSBActivity.this, LoginActivity.class);
				startActivity(intent);
				bidBtn.setEnabled(true);
			}
			break;
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.borrow_details_xsb_activity_intro_layout:
			Intent intentIntroXSB = new Intent(BorrowDetailXSBActivity.this,ProductIntroActivity.class);
			Bundle bundle0 = new Bundle();
			if(productInfo == null && recordInfo != null){
				productInfo = new ProductInfo();
				productInfo.setId(recordInfo.getBorrow_id());
			}
			bundle0.putSerializable("PRODUCT_INFO", productInfo);
			bundle0.putString("from_where", "xsb");
			intentIntroXSB.putExtra("BUNDLE", bundle0);
			startActivity(intentIntroXSB);
			break;
			// 项目介绍
		case R.id.borrow_details_xsb_activity_info_layout:
			// setViewPagerCurrentPosition(0);
			Intent intentProductInfo = new Intent(BorrowDetailXSBActivity.this,ProductInfoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("PROJECT_INFO", project);
			bundle.putSerializable("PRODUCT_INFO", productInfo);
			intentProductInfo.putExtra("BUNDLE", bundle);
			startActivity(intentProductInfo);
			break;
			// 安全保障
		case R.id.borrow_details_xsb_activity_safe_layout:
			// setViewPagerCurrentPosition(1);
			Intent intentSaft = new Intent(BorrowDetailXSBActivity.this,ProductSafetyActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putSerializable("PROJECT_INFO", project);
			bundle1.putSerializable("PRODUCT_INFO", productInfo);
			intentSaft.putExtra("BUNDLE", bundle1);
			startActivity(intentSaft);
			break;
			// 相关资料
		case R.id.borrow_details_xsb_activity_certificate_layout:
			// setViewPagerCurrentPosition(2);
			Intent intentProductData = new Intent(BorrowDetailXSBActivity.this,ProductDataActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putSerializable("PROJECT_INFO", project);
			bundle2.putSerializable("PRODUCT_INFO", productInfo);
			intentProductData.putExtra("BUNDLE", bundle2);
			startActivity(intentProductData);
			break;
			// 投资记录
		case R.id.borrow_details_xsb_activity_record_layout:
			// setViewPagerCurrentPosition(3);
			Intent intentProductRecord = new Intent(BorrowDetailXSBActivity.this,ProductRecordActivity.class);
			Bundle bundle3 = new Bundle();
			bundle3.putSerializable("PROJECT_INFO", project);
			bundle3.putSerializable("PRODUCT_INFO", productInfo);
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
		String materials = info.getMaterials_nomark();//
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
	 * 获取项目详情
	 * 
	 * @param id
	 */
	private void getProjectDetails(String id) {
		if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
			mLoadingDialog.show();
		}
		AsyncProjectDetails task = new AsyncProjectDetails(
				BorrowDetailXSBActivity.this, id, new Inter.OnCommonInter() {
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
	 * 判断是否可以购买新手标
	 * @param userId
	 * @param borrowId
	 */
	private void isCanbuyXSB(String userId,String borrowId){
		AsyncXSBIscanbuy task = new AsyncXSBIscanbuy(BorrowDetailXSBActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						bidBtn.setEnabled(true);
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//那么直接跳到充值页面
								Intent intent = new Intent();
								intent.setClass(BorrowDetailXSBActivity.this, BidXSBActivity.class);
								intent.putExtra("PRODUCT_INFO", productInfo);
								startActivity(intent);
							}else if(resultCode == 1001){
								//请先进行实名
								showMsgDialog(BorrowDetailXSBActivity.this, "实名认证", "请先实名认证！");
							}else if(resultCode == 1002){
								//请先进行绑卡
								boolean isNewUser = SettingsManager.checkIsNewUser(SettingsManager.getUserRegTime(getApplicationContext()));
								if(isNewUser){
									showMsgDialog(BorrowDetailXSBActivity.this, "绑卡", "请您先绑卡！");
								}else{
									showMsgDialog(BorrowDetailXSBActivity.this, "绑卡", "因我司变更支付渠道，请您重新绑卡！");
								}
							}else{
								showMsgDialog(BorrowDetailXSBActivity.this, "不能购买新手标", "此产品限首次购买用户专享！");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 获取新手标详情
	 * @param borrowStatus
	 */
	private void requestXSBDetails(String borrowStatus){
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		AsyncXSBDetails xsbDetails = new AsyncXSBDetails(BorrowDetailXSBActivity.this, borrowStatus, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null){
					mLoadingDialog.dismiss();
				}
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						List<ProductInfo> list = baseInfo.getProductPageInfo().getProductList();
						Message msg = handler.obtainMessage(REQUEST_XSBDETAILS_SUCCESS);
						msg.obj = list.get(0);
						handler.sendMessage(msg);
					}else{
						Message msg = handler.obtainMessage(REQUEST_XSBDETAILS_FAILE);
						msg.obj = baseInfo.getMsg();
						handler.sendMessage(msg);
					}
				}else{
					Message msg = handler.obtainMessage(REQUEST_XSBDETAILS_FAILE);
					msg.obj = "您的网络不给力";
					handler.sendMessage(msg);
				}
			}
		});
		xsbDetails.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 根据id获取产品详情
	 *
	 * @param borrowId
	 * @param borrowStatus
	 */
	private void getProductDetailsById(String borrowId, String borrowStatus,String plan) {
		if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
			mLoadingDialog.show();
		}
		AsyncProductInfo task = new AsyncProductInfo(BorrowDetailXSBActivity.this,
				borrowId, borrowStatus, plan, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null){
					mLoadingDialog.dismiss();
				}
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						Message msg = handler.obtainMessage(REQUEST_XSBDETAILS_SUCCESS);
						msg.obj = baseInfo.getmProductInfo();
						handler.sendMessage(msg);
					}else{
						Message msg = handler.obtainMessage(REQUEST_XSBDETAILS_FAILE);
						msg.obj = baseInfo.getMsg();
						handler.sendMessage(msg);
					}
				}else{
					Message msg = handler.obtainMessage(REQUEST_XSBDETAILS_FAILE);
					msg.obj = "您的网络不给力";
					handler.sendMessage(msg);
				}
			}
		});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
