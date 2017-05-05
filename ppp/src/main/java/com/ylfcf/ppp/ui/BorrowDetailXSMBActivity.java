package com.ylfcf.ppp.ui;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncProjectDetails;
import com.ylfcf.ppp.async.AsyncXSMBCurrentUserInvest;
import com.ylfcf.ppp.async.AsyncXSMBDetail;
import com.ylfcf.ppp.async.AsyncXSMBSelectone;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectCailiaoInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnProjectDetails;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.widget.LoadingDialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 限时秒标
 * @author Mr.liu
 *
 */
public class BorrowDetailXSMBActivity extends BaseActivity implements OnClickListener{
	private static final int REQUEST_XSMB_REFRESH_WHAT = 8291;//请求接口刷新数据
	private static final int REQUEST_XSMB_BTNCLICK_WHAT = 8292;//点击“立即秒杀”按钮
	private static final int REFRESH_REMAIN_TIME = 8293;//刷新下个秒标的剩余时间
	private static final int REQUEST_CURRENT_USERINVEST = 8294;//请求当前用户是否投资过该秒标
	private static final int REFRESH_PROGRESSBAR = 8295;//刷新进度条
	
	private static final int REQUEST_XSMB_SELECTONE = 8296;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private TextView nodataTV;//暂无此标的信息
	private ScrollView mainLayout;
	
	private TextView borrowName;
	private TextView borrowRate;// 年化收益
	private TextView timeLimit;// 期限
	private Button bidBtn;// 立即秒杀
	private TextView repayType1;
	private TextView repayType2;
	private TextView profitTv;// 预期收益
	private ProgressBar progressBar;
	private TextView progressText;//已投进度
	private LinearLayout introLayout,infoLayout, safeLayout, zizhiLayout;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private ProductInfo xsmbDetails;
	private ProjectInfo project;
	private LoadingDialog loadingDialog = null;
	private boolean isFirst = true;
	private InvestRecordInfo recordInfo;
	//请求请求原因
	private enum ReasonFlag{
		REFRESH_DATA,//页面刷新数据
		BTN_CLICK,// 通过按钮点击
		RECORD_DATA //投资记录跳转
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_XSMB_REFRESH_WHAT:
				requestXSMBDetails("发布",ReasonFlag.REFRESH_DATA);
				break;
			case REQUEST_XSMB_BTNCLICK_WHAT:
				requestXSMBDetails("发布",ReasonFlag.BTN_CLICK);
				break;
			case REFRESH_REMAIN_TIME:
				long times = (Long) msg.obj;
				updateCountDown(times);
				break;
			case REFRESH_PROGRESSBAR:
				//刷新进度条
				int progress_ = (Integer) msg.obj;
				progressbarIncrease(progress_);
				break;
			case REQUEST_CURRENT_USERINVEST:
				requestCurrentUserInvest(SettingsManager.getUserId(getApplicationContext()),xsmbDetails.getId());
				break;
			case REQUEST_XSMB_SELECTONE:
				requestXSMBSelectone(recordInfo.getBorrow_id(), "发布",ReasonFlag.RECORD_DATA);
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
		setContentView(R.layout.borrow_detail_xsmb_activity);
		mApp.addActivity(this);
		recordInfo = (InvestRecordInfo) getIntent().getSerializableExtra("InvestRecordInfo");
		loadingDialog = new LoadingDialog(BorrowDetailXSMBActivity.this, "正在加载...", R.anim.loading);
		findViews();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(recordInfo == null){
			handler.sendEmptyMessage(REQUEST_XSMB_REFRESH_WHAT);
		}else{
			handler.sendEmptyMessage(REQUEST_XSMB_SELECTONE);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		handler.removeCallbacksAndMessages(null);
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("产品详情");
		
		nodataTV = (TextView) findViewById(R.id.borrow_detail_xsmb_activity_nodata);
		mainLayout = (ScrollView) findViewById(R.id.borrow_detail_xsmb_activity_main);
		borrowName = (TextView) findViewById(R.id.borrow_detail_xsmb_activity_borrowname);
		borrowRate = (TextView) findViewById(R.id.borrow_details_xsmb_activity_invest_rate);
		timeLimit = (TextView) findViewById(R.id.borrow_details_xsmb_invest_time_limit);
		repayType1 = (TextView) findViewById(R.id.borrow_details_xsmb_activity_repay_type1);
		repayType2  = (TextView) findViewById(R.id.borrow_details_xsmb_activity_repay_type2);
		bidBtn = (Button) findViewById(R.id.borrow_detail_xsmb_activity_bidBtn);
		bidBtn.setOnClickListener(this);
		profitTv = (TextView) findViewById(R.id.borrow_details_xsmb_activity_profit);
		progressBar = (ProgressBar) findViewById(R.id.borrow_details_xsmb_activity_pb);
		progressText = (TextView) findViewById(R.id.borrow_details_xsmb_activity_progress_text);
		introLayout = (LinearLayout) findViewById(R.id.borrow_details_xsmb_activity_intro_layout);
		introLayout.setOnClickListener(this);
		infoLayout = (LinearLayout) findViewById(R.id.borrow_details_xsmb_activity_info_layout);
		infoLayout.setOnClickListener(this);
		safeLayout = (LinearLayout) findViewById(R.id.borrow_details_xsmb_activity_safe_layout);
		safeLayout.setOnClickListener(this);
		zizhiLayout = (LinearLayout) findViewById(R.id.borrow_details_xsmb_activity_certificate_layout);
		zizhiLayout.setOnClickListener(this);
	}

	double investMoneyD = 0d;
	double totalMoneyD = 0d;
	private void initViewData(ProductInfo productInfo,final Enum flag){
		if(productInfo == null){
			nodataTV.setVisibility(View.VISIBLE);
			mainLayout.setVisibility(View.GONE);
			return;
		}
		if(flag == ReasonFlag.REFRESH_DATA || flag == ReasonFlag.RECORD_DATA){
			//刷新数据
			float rateF = 0f;
			int limitInt = 0;
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
			borrowName.setText(productInfo.getBorrow_name());
			timeLimit.setText(productInfo.getInterest_period());
			repayType1.setText(productInfo.getRepay_way());
			repayType2.setText(productInfo.getRepay_way());
			try {
				rateF = Float.parseFloat(productInfo.getInterest_rate());
			} catch (Exception e) {
			}
			try {
				limitInt = Integer.parseInt(productInfo.getInterest_period());
			} catch (Exception e) {
			}
			profitTv.setText("投资2000元，预期收益"+new DecimalFormat("#.00").format(rateF * 2000/100/365*limitInt) + "元");
			try {
				investMoneyD = Double.parseDouble(productInfo.getInvest_money());
			} catch (Exception e) {
			}
			try {
				totalMoneyD = Double.parseDouble(productInfo.getTotal_money());
			} catch (Exception e) {
			}
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					progressbarIncrease((int)(investMoneyD*10000/totalMoneyD));
				}
			}, 500L);
			progressText.setText((int)(investMoneyD*100/totalMoneyD)+"%");
			if("未满标".equals(productInfo.getMoney_status())){
				initBidBtnCountDown(productInfo,flag);//倒计时
			}else{
				bidBtn.setEnabled(false);
				bidBtn.setText("投资结束");
				bidBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
			}
		}else if(flag == ReasonFlag.BTN_CLICK){
			//点击“立即秒杀”按钮
			try {
				investMoneyD = Double.parseDouble(productInfo.getInvest_money());
			} catch (Exception e) {
			}
			try {
				totalMoneyD = Double.parseDouble(productInfo.getTotal_money());
			} catch (Exception e) {
			}
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					progressbarIncrease((int)(investMoneyD*10000/totalMoneyD));
				}
			}, 500L);
			progressText.setText((int)(investMoneyD*100/totalMoneyD)+"%");
			if("未满标".equals(productInfo.getMoney_status())){
				//未满标，再请求用户是否已经投资过该秒标
				initBidBtnCountDown(productInfo,flag);//倒计时
			}else{
				//已满标
				bidBtn.setEnabled(false);
				bidBtn.setText("投资结束");
				bidBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
				showPromptDialog("1");
			}
		}
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
	
	/**
	 * 投资按钮倒计时
	 * @param productInfo
	 * @throws ParseException 
	 */
	long remainTimeL = 0l;
	private void initBidBtnCountDown(ProductInfo productInfo,Enum flag){
		bidBtn.setEnabled(false);
		String nowTimeStr = productInfo.getNow_time();
		String willStartTimeStr = productInfo.getWill_start_time();//开始时间
		int hour = 0,minute = 0,second = 0;
		String hourStr = "",minuteStr = "",secondStr = "";
		try {
			Date nowDate = sdf.parse(nowTimeStr);
			Date willStartDate = sdf.parse(willStartTimeStr);
			remainTimeL = willStartDate.getTime() - nowDate.getTime();
			if(remainTimeL >= 0){
				if(flag == ReasonFlag.RECORD_DATA){
					bidBtn.setText("投资结束");
					bidBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
					return;
				}
				hour = (int) (remainTimeL/1000/3600);
				minute = (int) ((remainTimeL/1000%3600)/60);
				second = (int) (remainTimeL/1000%3600%60);
				if(hour < 10){
					hourStr = "0" + hour;
				}else{
					hourStr = hour + "";
				}
				if(minute < 10){
					minuteStr = "0" + minute;
				}else{
					minuteStr = minute + "";
				}
				if(second < 10){
					secondStr = "0" + second;
				}else{
					secondStr = second + "";
				}
				bidBtn.setText("距离下一场“秒杀”开始还剩："+hourStr+"时"+minuteStr+"分"+secondStr+"秒");
				bidBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_21dp));
				Message msg = handler.obtainMessage(REFRESH_REMAIN_TIME);
				msg.obj = remainTimeL;
				handler.sendMessage(msg);
			}else{
//				//正在投资中。。
				bidBtn.setText("立即秒杀");
				bidBtn.setEnabled(true);
				bidBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
				if(flag == ReasonFlag.BTN_CLICK){
					handler.sendEmptyMessage(REQUEST_CURRENT_USERINVEST);
				}else if(flag == ReasonFlag.REFRESH_DATA){
					
				}
			}
		} catch (ParseException e) {
			bidBtn.setText("投资结束");
			bidBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
			e.printStackTrace();
		}
	}
	
	//即时更新倒计时
	private void updateCountDown(long times){
		int hour = 0,minute = 0,second = 0;
		String hourStr = "",minuteStr = "",secondStr = "";
		times -= 1000;
		if(times <= 0){
			bidBtn.setEnabled(true);
			bidBtn.setText("立即秒杀");
			bidBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
			return;
		}
		hour = (int) (times/1000/3600);
		minute = (int) ((times/1000%3600)/60);
		second = (int) (times/1000%3600%60);
		if(hour < 10){
			hourStr = "0" + hour;
		}else{
			hourStr = hour + "";
		}
		if(minute < 10){
			minuteStr = "0" + minute;
		}else{
			minuteStr = minute + "";
		}
		if(second < 10){
			secondStr = "0" + second;
		}else{
			secondStr = second + "";
		}
		bidBtn.setText("距离下一场“秒杀”开始还剩："+hourStr+"时"+minuteStr+"分"+secondStr+"秒");
		bidBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_21dp));
		Message msg = handler.obtainMessage(REFRESH_REMAIN_TIME);
		msg.obj = times;
		handler.sendMessageDelayed(msg, 1000L);
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
		case R.id.borrow_details_xsmb_activity_intro_layout:
			//产品介绍
			Intent intentIntroXSMB = new Intent(BorrowDetailXSMBActivity.this,ProductIntroXSMBActivity.class);
			intentIntroXSMB.putExtra("InvestRecordInfo", recordInfo);
			startActivity(intentIntroXSMB);
			break;
		case R.id.borrow_details_xsmb_activity_info_layout:
			//项目信息
			Intent intentProductInfo = new Intent(BorrowDetailXSMBActivity.this,ProductInfoXSMBActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("PROJECT_INFO", project);
			bundle.putSerializable("PRODUCT_INFO", xsmbDetails);
			bundle.putSerializable("InvestRecordInfo", recordInfo);
			intentProductInfo.putExtra("BUNDLE", bundle);
			startActivity(intentProductInfo);
			break;
		case R.id.borrow_details_xsmb_activity_safe_layout:
			//安全保障
			Intent intentSaft = new Intent(BorrowDetailXSMBActivity.this,ProductSafetyXSMBActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putSerializable("PROJECT_INFO", project);
			bundle1.putSerializable("PRODUCT_INFO", xsmbDetails);
			bundle1.putSerializable("InvestRecordInfo", recordInfo);
			intentSaft.putExtra("BUNDLE", bundle1);
			startActivity(intentSaft);
			break;
		case R.id.borrow_details_xsmb_activity_certificate_layout:
			//资质证书
			Intent intentProductData = new Intent(BorrowDetailXSMBActivity.this,ProductDataXSMBActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putSerializable("PROJECT_INFO", project);
			bundle2.putSerializable("PRODUCT_INFO", xsmbDetails);
			bundle2.putSerializable("InvestRecordInfo", recordInfo);
			intentProductData.putExtra("BUNDLE", bundle2);
			startActivity(intentProductData);
			break;
		case R.id.borrow_detail_xsmb_activity_bidBtn:
			// 从SettingsManager中读取密码，如果为空意味着没有登录。
			boolean isLogin = !SettingsManager.getLoginPassword(
					BorrowDetailXSMBActivity.this).isEmpty()&& !SettingsManager.getUser(BorrowDetailXSMBActivity.this).isEmpty();
			bidBtn.setEnabled(false);
			Intent intent = new Intent();
			// 1、检测是否已经登录
			if (isLogin) {
				//请求是否还可以购买
				handler.sendEmptyMessage(REQUEST_XSMB_BTNCLICK_WHAT);
			} else {
				// 未登录，跳转到登录页面
				intent.setClass(BorrowDetailXSMBActivity.this,LoginActivity.class);
				startActivity(intent);
				bidBtn.setEnabled(true);
			}
			
			break;
		default:
			break;
		}
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
					Intent intent = new Intent(BorrowDetailXSMBActivity.this,UserInvestRecordActivity.class);
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
	
	/**
	 * 解析打码的材料的图片
	 * @param info
	 */
	private void parseProjectCailiaoMarkImg(ProjectInfo info){
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
	 * 秒标详情
	 * @param reasonFlag 自动刷新数据 or 通过按钮点击
	 * @param isFirst 是否首次请求
	 */
	private void requestXSMBDetails(String borrowStatus,final Enum reasonFlag){
		AsyncXSMBDetail xsmbTask = new AsyncXSMBDetail(BorrowDetailXSMBActivity.this, borrowStatus,new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				bidBtn.setEnabled(true);
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						progressTemp = 0;
						xsmbDetails = baseInfo.getmProductInfo();
						initViewData(xsmbDetails,reasonFlag);
						if(isFirst){
							//请求project接口
							getProjectDetails(xsmbDetails.getProject_id());
						}
					}else{
						nodataTV.setVisibility(View.VISIBLE);
						mainLayout.setVisibility(View.GONE);
					}
				}
				isFirst = false;
			}
		});
		xsmbTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 根据id获取秒标详情
	 * @param borrowId
	 * @param borrowStatus
	 */
	private void requestXSMBSelectone(String borrowId,String borrowStatus,final Enum reasonFlag){
		AsyncXSMBSelectone task = new AsyncXSMBSelectone(BorrowDetailXSMBActivity.this, borrowId, borrowStatus, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						bidBtn.setEnabled(true);
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								progressTemp = 0;
								xsmbDetails = baseInfo.getmProductInfo();
								initViewData(xsmbDetails,reasonFlag);
								if(isFirst){
									//请求project接口
									getProjectDetails(xsmbDetails.getProject_id());
								}
							}else{
								Util.toastLong(BorrowDetailXSMBActivity.this, baseInfo.getMsg());
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 判断当前用户是否投资过该秒标
	 * @param userId
	 * @param borrowId
	 */
	private void requestCurrentUserInvest(String userId,String borrowId){
		AsyncXSMBCurrentUserInvest task = new AsyncXSMBCurrentUserInvest(BorrowDetailXSMBActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//没有投资过该秒标
								Intent intent = new Intent(BorrowDetailXSMBActivity.this,BidXSMBActivity.class);
								intent.putExtra("PRODUCT_INFO", xsmbDetails);
								startActivity(intent);
							}else{
								//投资过该秒标
								showPromptDialog("0");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 获取项目详情
	 * 
	 * @param id
	 */
	private void getProjectDetails(String id) {
		if (loadingDialog != null && !loadingDialog.isShowing()) {
			loadingDialog.show();
		}
		AsyncProjectDetails task = new AsyncProjectDetails(
				BorrowDetailXSMBActivity.this, id, new OnProjectDetails() {
					@Override
					public void back(ProjectInfo projectInfo) {
						if (loadingDialog != null && loadingDialog.isShowing()) {
							loadingDialog.dismiss();
						}
						if (projectInfo != null) {
							project = projectInfo;
							parseProjectCailiaoMarkImg(project);
							parseProjectCailiaoNomarkImg(project);
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
