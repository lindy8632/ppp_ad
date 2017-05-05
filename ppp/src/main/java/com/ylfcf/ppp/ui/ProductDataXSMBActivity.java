package com.ylfcf.ppp.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.ProductDataAdapter;
import com.ylfcf.ppp.async.AsyncAsscociatedCompany;
import com.ylfcf.ppp.async.AsyncXSMBCurrentUserInvest;
import com.ylfcf.ppp.async.AsyncXSMBDetail;
import com.ylfcf.ppp.async.AsyncXSMBSelectone;
import com.ylfcf.ppp.entity.AssociatedCompanyParentInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectCailiaoInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.ImageLoaderManager;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.widget.GridViewWithHeaderAndFooter;
import com.ylfcf.ppp.widget.LoadingDialog;

/**
 * 限时秒标资质证书
 * @author Mr.liu
 *
 */
public class ProductDataXSMBActivity extends BaseActivity implements OnClickListener{
	private static final int REQUEST_ASSC_WHAT = 7421;
	private static final int REQUEST_ASSC_SUCCESS = 7422;
	private static final int REQUEST_ASSC_NODATA = 7423;
	
	private static final int REQUEST_XSMB_REFRESH_WHAT = 8291;// 请求接口刷新数据
	private static final int REQUEST_CURRENT_USERINVEST = 8294;//请求当前用户是否投资过该秒标
	private static final int REQUEST_XSMB_BTNCLICK_WHAT = 8292;//点击“立即秒杀”按钮
	private static final int REFRESH_REMAIN_TIME = 8293;//刷新下个秒标的剩余时间
	
	private static final int REQUEST_XSMB_SELECTONE = 8296;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private GridViewWithHeaderAndFooter dataGridView;
	private Button investBtn;
	private ProductDataAdapter adapter;
	
	private ProjectInfo projectInfo;
	private ProductInfo productInfo;
	private InvestRecordInfo recordInfo;
	private LayoutInflater layoutInflater = null;
	private View bottomView;
	private LoadingDialog loadingDialog = null;
	private ArrayList<ProjectCailiaoInfo> noMarksCailiaoList = new ArrayList<ProjectCailiaoInfo>();
	private ArrayList<ProjectCailiaoInfo> marksCailiaoList = new ArrayList<ProjectCailiaoInfo>();
	
	private boolean isInvested = false;//用户是否投标过
	private boolean isRequestAssc = false;//是否请求过关联公司的接口
	private AlertDialog.Builder builder = null; // 先得到构造器
	private String fromWhere = "";
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//请求请求原因
	private enum ReasonFlag {
		REFRESH_DATA, // 页面刷新数据
		BTN_CLICK,// 通过按钮点击
		RECORD_DATA
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
			case REQUEST_XSMB_SELECTONE:
				requestXSMBSelectone(recordInfo.getBorrow_id(), "发布",ReasonFlag.RECORD_DATA);
				break;
			case REQUEST_ASSC_WHAT:
				if(projectInfo != null)
				requestAssociatedCompany(projectInfo.getLoan_id(), projectInfo.getRecommend_id(), projectInfo.getGuarantee_id());
				break;
			case REQUEST_ASSC_SUCCESS:
				AssociatedCompanyParentInfo pageInfo = (AssociatedCompanyParentInfo) msg.obj;
				try {
					combineImgDatas(pageInfo);
				} catch (Exception e) {
				}
				break;
			case REQUEST_CURRENT_USERINVEST:
				if(productInfo != null)
				requestCurrentUserInvest(SettingsManager.getUserId(getApplicationContext()),productInfo.getId());
				break;
			case REQUEST_ASSC_NODATA:
				initImgData();
				break;
			case REFRESH_REMAIN_TIME:
				long times = (Long) msg.obj;
				updateCountDown(times);
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
		setContentView(R.layout.product_data_activity);
		mApp.addActivity(this);
		builder = new AlertDialog.Builder(ProductDataXSMBActivity.this,
				R.style.Dialog_Transparent); // 先得到构造器
		loadingDialog = new LoadingDialog(ProductDataXSMBActivity.this, "正在加载...", R.anim.loading);
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		if(bundle != null){
			projectInfo = (ProjectInfo) bundle.getSerializable("PROJECT_INFO");
			productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
			fromWhere = bundle.getString("from_where");
			recordInfo = (InvestRecordInfo) bundle.getSerializable("InvestRecordInfo");
		}
		findViews();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		if(recordInfo == null){
//			handler.sendEmptyMessage(REQUEST_XSMB_REFRESH_WHAT);
//		}else{
//			handler.sendEmptyMessage(REQUEST_XSMB_SELECTONE);
//		}
		handler.sendEmptyMessage(REQUEST_CURRENT_USERINVEST);
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("资质证书");
		bottomView = layoutInflater.inflate(R.layout.bottom_button_invest_layout, null);
		investBtn = (Button) bottomView.findViewById(R.id.product_data_activity_bidBtn);
		investBtn.setEnabled(false);
		investBtn.setOnClickListener(this);
		dataGridView = (GridViewWithHeaderAndFooter)findViewById(R.id.product_data_gv);
		dataGridView.addFooterView(bottomView);
		
		dataGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ProductDataXSMBActivity.this,ProductDataDetailsActivity.class);
				if(isInvested){
					intent.putExtra("cailiao_list",noMarksCailiaoList);
				}else{
					intent.putExtra("cailiao_list",marksCailiaoList);
				}
				intent.putExtra("position", position);
				startActivity(intent);
			}
		});
		initAdapter();
	}
	
	private void initAdapter(){
		adapter = new ProductDataAdapter(ProductDataXSMBActivity.this,layoutInflater);
		dataGridView.setAdapter(adapter);
	}
	
	/**
	 * 将关联公司的图片材料合并到项目的图片中
	 * @param pageInfo
	 */
	private void combineImgDatas(AssociatedCompanyParentInfo pageInfo){
		//借款方
		List<ProjectCailiaoInfo> nomarkListLoan = new ArrayList<ProjectCailiaoInfo>();
		List<ProjectCailiaoInfo> markListLoan = new ArrayList<ProjectCailiaoInfo>();

		List<String> picNamesLoan = pageInfo.getLoanInfo()
				.getMaterialsNamesList();
		List<String> nomarkListStrLoan = pageInfo.getLoanInfo()
				.getNomarkPicsList();
		List<String> markListStrLoan = pageInfo.getLoanInfo()
				.getMarkPicsList();
		for (int i = 0; i < picNamesLoan.size(); i++) {
			ProjectCailiaoInfo info = new ProjectCailiaoInfo();
			ProjectCailiaoInfo info1 = new ProjectCailiaoInfo();
			try {
				info.setTitle(picNamesLoan.get(i));
			} catch (Exception e) {
				info.setTitle("");
			}
			try {
				info.setImgURL(nomarkListStrLoan.get(i));
			} catch (Exception e) {
				info.setImgURL("");
			}
			nomarkListLoan.add(info);
			
			try {
				info1.setTitle(picNamesLoan.get(i));
			} catch (Exception e) {
				info1.setTitle("");
			}
			try {
				info1.setImgURL(markListStrLoan.get(i));
			} catch (Exception e) {
				info1.setImgURL("");
			}
			markListLoan.add(info1);
		}
		noMarksCailiaoList.addAll(nomarkListLoan);
		marksCailiaoList.addAll(markListLoan);
		//推荐方
		List<ProjectCailiaoInfo> nomarkListRecommend = new ArrayList<ProjectCailiaoInfo>();
		List<ProjectCailiaoInfo> markListRecommend = new ArrayList<ProjectCailiaoInfo>();

		List<String> picNamesRecommend = pageInfo.getRecommendInfo()
				.getMaterialsNamesList();
		List<String> nomarkListStrRecommend = pageInfo.getRecommendInfo()
				.getNomarkPicsList();
		List<String> markListStrRecommend = pageInfo.getRecommendInfo()
				.getMarkPicsList();
		for (int i = 0; i < picNamesRecommend.size(); i++) {
			ProjectCailiaoInfo info = new ProjectCailiaoInfo();
			ProjectCailiaoInfo info1 = new ProjectCailiaoInfo();
			try {
				info.setTitle(picNamesRecommend.get(i));
			} catch (Exception e) {
				info.setTitle("");
			}
			try {
				info.setImgURL(nomarkListStrRecommend.get(i));
			} catch (Exception e) {
				info.setImgURL("");
			}
			nomarkListRecommend.add(info);
			
			try {
				info1.setTitle(picNamesRecommend.get(i));
			} catch (Exception e) {
				info1.setTitle("");
			}
			try {
				info1.setImgURL(markListStrRecommend.get(i));
			} catch (Exception e) {
				info1.setImgURL("");
			}
			markListRecommend.add(info1);
		}
		noMarksCailiaoList.addAll(nomarkListRecommend);
		marksCailiaoList.addAll(markListRecommend);
		//担保方
		List<ProjectCailiaoInfo> nomarkListGuarantee = new ArrayList<ProjectCailiaoInfo>();
		List<ProjectCailiaoInfo> markListGuarantee = new ArrayList<ProjectCailiaoInfo>();
		
		List<String> picNamesGuarantee = pageInfo.getGuaranteeInfo().getMaterialsNamesList();
		List<String> nomarkListStrGuarantee = pageInfo.getGuaranteeInfo().getNomarkPicsList();
		List<String> markListStrGuarantee = pageInfo.getGuaranteeInfo().getMarkPicsList();
		for(int i=0;i<picNamesGuarantee.size();i++){
			ProjectCailiaoInfo info = new ProjectCailiaoInfo();
			ProjectCailiaoInfo info1 = new ProjectCailiaoInfo();
			try {
				info.setTitle(picNamesGuarantee.get(i));
			} catch (Exception e) {
				info.setTitle("");
			}
			try {
				info.setImgURL(nomarkListStrGuarantee.get(i));
			} catch (Exception e) {
				info.setImgURL("");
			}
			nomarkListGuarantee.add(info);
			
			try {
				info1.setTitle(picNamesGuarantee.get(i));
			} catch (Exception e) {
				info1.setTitle("");
			}
			try {
				info1.setImgURL(markListStrGuarantee.get(i));
			} catch (Exception e) {
				info1.setImgURL("");
			}
			
			markListGuarantee.add(info1);
		}
		noMarksCailiaoList.addAll(nomarkListGuarantee);
		marksCailiaoList.addAll(markListGuarantee);
		
		if(projectInfo != null){
			noMarksCailiaoList.addAll(projectInfo.getCailiaoNoMarkList());
			marksCailiaoList.addAll(projectInfo.getCailiaoMarkList());
		}
		
		if(isInvested){
			adapter.setItems(noMarksCailiaoList);
		}else{
			adapter.setItems(marksCailiaoList);
		}
	}
	
	private void initImgData(){
		noMarksCailiaoList.clear();
		marksCailiaoList.clear();
		if(projectInfo != null){
			noMarksCailiaoList.addAll(projectInfo.getCailiaoNoMarkList());
			marksCailiaoList.addAll(projectInfo.getCailiaoMarkList());
		}
		if(isInvested){
			adapter.setItems(noMarksCailiaoList);
		}else{
			adapter.setItems(marksCailiaoList);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ImageLoaderManager.clearMemoryCache();
		handler.removeCallbacksAndMessages(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.product_data_activity_bidBtn:
			// 从SettingsManager中读取密码，如果为空意味着没有登录。
			boolean isLogin = !SettingsManager.getLoginPassword(
					ProductDataXSMBActivity.this).isEmpty()
					&& !SettingsManager.getUser(ProductDataXSMBActivity.this)
							.isEmpty();
			investBtn.setEnabled(false);
			Intent intent = new Intent();
			// 1、检测是否已经登录
			if (isLogin) {
				// 请求是否还可以购买
				handler.sendEmptyMessage(REQUEST_XSMB_BTNCLICK_WHAT);
			} else {
				// 未登录，跳转到登录页面
				intent.setClass(ProductDataXSMBActivity.this,LoginActivity.class);
				startActivity(intent);
				investBtn.setEnabled(true);
			}
			break;
		default:
			break;
		}
	}
	
	private void initViewData(ProductInfo productInfo,Enum flag) {
		if(flag == ReasonFlag.REFRESH_DATA || flag == ReasonFlag.RECORD_DATA){
			//刷新数据
			if("未满标".equals(productInfo.getMoney_status())){
				initBidBtnCountDown(productInfo,flag);//倒计时
			}else{
				investBtn.setEnabled(false);
				investBtn.setText("投资结束");
				investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
			}
		}else if(flag == ReasonFlag.BTN_CLICK){
			//点击“立即秒杀”按钮
			if("未满标".equals(productInfo.getMoney_status())){
				//未满标，再请求用户是否已经投资过该秒标
				initBidBtnCountDown(productInfo,flag);//倒计时
			}else{
				//已满标
				showPromptDialog("1");
			}
		}
	}
	/**
	 * 投资按钮倒计时
	 * @param productInfo
	 * @throws ParseException 
	 */
	long remainTimeL = 0l;
	private void initBidBtnCountDown(ProductInfo productInfo,Enum flag){
		investBtn.setEnabled(false);
		String nowTimeStr = productInfo.getNow_time();
		String willStartTimeStr = productInfo.getWill_start_time();//开始时间
		int hour = 0,minute = 0,second = 0;
		String hourStr = "",minuteStr = "",secondStr = "";
		try {
			Date nowDate = sdf.parse(nowTimeStr);
			Date willStartDate = sdf.parse(willStartTimeStr);
			remainTimeL = willStartDate.getTime() - nowDate.getTime();
			if(remainTimeL >= 0){
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
				investBtn.setText("距离下一场“秒杀”开始还剩："+hourStr+"时"+minuteStr+"分"+secondStr+"秒");
				investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_21dp));
				Message msg = handler.obtainMessage(REFRESH_REMAIN_TIME);
				msg.obj = remainTimeL;
				handler.sendMessage(msg);
			}else{
//				//正在投资中。。
				investBtn.setText("立即秒杀");
				investBtn.setEnabled(true);
				investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
				if(flag == ReasonFlag.BTN_CLICK && !isInvested){
//					handler.sendEmptyMessage(REQUEST_CURRENT_USERINVEST);
					Intent intent = new Intent(ProductDataXSMBActivity.this,BidXSMBActivity.class);
					intent.putExtra("PRODUCT_INFO", productInfo);
					startActivity(intent);
				}else if(flag == ReasonFlag.BTN_CLICK && isInvested){
					showPromptDialog("0");
				}else if(flag == ReasonFlag.REFRESH_DATA){
					
				}
			}
		} catch (ParseException e) {
			investBtn.setText("投资结束");
			investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
			e.printStackTrace();
		}
	}
	
	// 即时更新倒计时
		private void updateCountDown(long times) {
			int hour = 0, minute = 0, second = 0;
			String hourStr = "", minuteStr = "", secondStr = "";
			times -= 1000;
			if (times <= 0) {
				investBtn.setEnabled(true);
				investBtn.setText("立即秒杀");
				investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
						.getDimensionPixelSize(R.dimen.common_measure_26dp));
				return;
			}
			hour = (int) (times / 1000 / 3600);
			minute = (int) ((times / 1000 % 3600) / 60);
			second = (int) (times / 1000 % 3600 % 60);
			if (hour < 10) {
				hourStr = "0" + hour;
			} else {
				hourStr = hour + "";
			}
			if (minute < 10) {
				minuteStr = "0" + minute;
			} else {
				minuteStr = minute + "";
			}
			if (second < 10) {
				secondStr = "0" + second;
			} else {
				secondStr = second + "";
			}
			investBtn.setText("距离下一场“秒杀”开始还剩：" + hourStr + "时" + minuteStr + "分"
					+ secondStr + "秒");
			investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
					.getDimensionPixelSize(R.dimen.common_measure_21dp));
			Message msg = handler.obtainMessage(REFRESH_REMAIN_TIME);
			msg.obj = times;
			handler.sendMessageDelayed(msg, 1000L);
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
			topTV.setText("已秒光！\n请下一场再来试试~");
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
					Intent intent = new Intent(ProductDataXSMBActivity.this,UserInvestRecordActivity.class);
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
				mApp.finishAllActivityExceptMain();
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
	 * 关联公司的信息
	 * @param loanId
	 * @param recommendId
	 * @param guaranteeId
	 */
	private void requestAssociatedCompany(String loanId,String recommendId,String guaranteeId){
		AsyncAsscociatedCompany task = new AsyncAsscociatedCompany(ProductDataXSMBActivity.this, loanId, recommendId, guaranteeId, 
				new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						isRequestAssc = true;
						dataGridView.setVisibility(View.VISIBLE);
						if(loadingDialog != null && loadingDialog.isShowing()){
							loadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								AssociatedCompanyParentInfo info = baseInfo.getAssociatedCompanyParentInfo();
								Message msg = handler.obtainMessage(REQUEST_ASSC_SUCCESS);
								msg.obj = info;
								handler.sendMessage(msg);
							}else{
								Message msg = handler.obtainMessage(REQUEST_ASSC_NODATA);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							}
						}else{
							Message msg = handler.obtainMessage(REQUEST_ASSC_NODATA);
							msg.obj = baseInfo;
							handler.sendMessage(msg);
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
		AsyncXSMBCurrentUserInvest task = new AsyncXSMBCurrentUserInvest(ProductDataXSMBActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//没有投资过
								isInvested = false;
							}else if(resultCode == -102){
								isInvested = true;
							}else{
								isInvested = false;
							}
						}else{
							isInvested = false;
						}
						if(recordInfo == null){
							handler.sendEmptyMessage(REQUEST_XSMB_REFRESH_WHAT);
						}else{
							handler.sendEmptyMessage(REQUEST_XSMB_SELECTONE);
						}
						if(!isRequestAssc){
							handler.sendEmptyMessage(REQUEST_ASSC_WHAT);
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 秒标详情
	 * 
	 * @param reasonFlag
	 *            自动刷新数据 or 通过按钮点击
	 * @param isFirst
	 *            是否首次请求
	 */
	private void requestXSMBDetails(String borrowStatus,final Enum flag) {
		AsyncXSMBDetail xsmbTask = new AsyncXSMBDetail(
				ProductDataXSMBActivity.this, borrowStatus,new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								investBtn.setVisibility(View.VISIBLE);
								productInfo = baseInfo
										.getmProductInfo();
								initViewData(productInfo,flag);
							} else {
								investBtn.setVisibility(View.GONE);
							}
						}
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
		AsyncXSMBSelectone task = new AsyncXSMBSelectone(ProductDataXSMBActivity.this, borrowId, borrowStatus, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								investBtn.setVisibility(View.VISIBLE);
								productInfo = baseInfo.getmProductInfo();
								initViewData(productInfo, reasonFlag);
							} else {
								investBtn.setVisibility(View.GONE);
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
