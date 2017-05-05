package com.ylfcf.ppp.ui;

import java.util.ArrayList;
import java.util.List;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.BorrowRecordsAdapter;
import com.ylfcf.ppp.async.AsyncInvestRecord;
import com.ylfcf.ppp.async.AsyncVIPRecordList;
import com.ylfcf.ppp.async.AsyncXSBIscanbuy;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.InvestRecordPageInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.inter.Inter.OnIsVipUserListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.widget.LoadingDialog;
import com.ylfcf.ppp.widget.RefreshLayout;
import com.ylfcf.ppp.widget.RefreshLayout.OnLoadListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * VIP产品记录
 * @author Mr.liu
 *
 */
public class VIPProductRecordActivity extends BaseActivity implements
					OnClickListener{
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private ProductInfo productInfo;
	private ProjectInfo projectInfo;

	private static final int REQUEST_INVEST_RECORD_WHAT = 1021;
	private static final int REQUEST_INVEST_RECORD_SUCCESS = 1022;
	private static final int REQUEST_INVEST_RECORD_NODATA = 1023; // 无数据

	private BorrowDetailZXDActivity borrowDetailActivity;
	private ListView recordListView;
	private LayoutInflater layoutInflater;
	private View headerView;
	private View nodataView;
	private View footerView;
	private TextView nodataText;
	private Button nodataBtn;
	private Button investBtn;

	private RefreshLayout refreshLayout;
	private BorrowRecordsAdapter investRecordsAdapter;
	private List<InvestRecordInfo> investRecordList = new ArrayList<InvestRecordInfo>();

	// 请求参数
	private String status = "";// 投资中
	private int pageNo = 0;
	private int pageSize = 50;
	private LoadingDialog loadingDialog;
	private boolean isRefresh = true;// 下拉刷新
	private boolean isLoad = false;// 上拉加载更多
	private boolean isFirst = true;
	private AlertDialog.Builder builder = null; // 先得到构造器

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_INVEST_RECORD_WHAT:
				if (productInfo != null) {
					getInvestRecordList("", productInfo.getId(), status);
				}
				break;
			case REQUEST_INVEST_RECORD_SUCCESS:
				refreshLayout.setVisibility(View.VISIBLE);
				nodataView.setVisibility(View.GONE);
				InvestRecordPageInfo pageInfo = (InvestRecordPageInfo) msg.obj;
				if (isRefresh) {
					investRecordList.clear();
					investRecordList.addAll(pageInfo.getInvestRecordList());
				} else if (isLoad) {
					investRecordList.addAll(pageInfo.getInvestRecordList());
				}
				updateAdapter(investRecordList);
				break;
			case REQUEST_INVEST_RECORD_NODATA:
				if (isRefresh) {
					refreshLayout.setVisibility(View.GONE);
					nodataView.setVisibility(View.VISIBLE);
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
		setContentView(R.layout.product_record_activity);
		builder = new AlertDialog.Builder(VIPProductRecordActivity.this,
				R.style.Dialog_Transparent); // 先得到构造器
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		if(bundle != null){
			projectInfo = (ProjectInfo) bundle.getSerializable("PROJECT_INFO");
			productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
		}
		
		layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		loadingDialog = new LoadingDialog(VIPProductRecordActivity.this,
				"正在加载...", R.anim.loading);
		findViews();
	}

	private void updateAdapter(List<InvestRecordInfo> recordList) {
		investRecordsAdapter.setItems(recordList);
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("投资记录");

		refreshLayout = (RefreshLayout) findViewById(R.id.product_record_activity_refreshlayout);
		nodataText = (TextView) findViewById(R.id.nodata_layout_text);
		nodataText.setText("暂无记录");
		nodataBtn = (Button) findViewById(R.id.nodata_layout_btn);
		nodataBtn.setOnClickListener(this);
		nodataView = findViewById(R.id.product_record_activity_nodata_layout);
		headerView = layoutInflater.inflate(R.layout.invest_records_header,null);
		footerView = layoutInflater.inflate(R.layout.bottom_button_invest_layout, null);
		investBtn = (Button) footerView.findViewById(R.id.product_data_activity_bidBtn);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				getResources().getDimensionPixelSize(R.dimen.common_measure_58dp));
		params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.common_measure_50dp);
		params.leftMargin = getResources().getDimensionPixelSize(R.dimen.common_measure_20dp);
		params.rightMargin = getResources().getDimensionPixelSize(R.dimen.common_measure_20dp);
		params.topMargin = getResources().getDimensionPixelSize(R.dimen.common_measure_30dp);
		investBtn.setLayoutParams(params);
		investBtn.setOnClickListener(this);
		recordListView = (ListView) findViewById(R.id.product_record_activity_listview);
		recordListView.addHeaderView(headerView);
		recordListView.addFooterView(footerView);
		if(productInfo != null){
			if("未满标".equals(productInfo.getMoney_status())){
				investBtn.setEnabled(true);
				investBtn.setText("立即投资");
				nodataBtn.setVisibility(View.VISIBLE);
			}else{
				investBtn.setEnabled(false);
				investBtn.setText("投资已结束");
				nodataBtn.setVisibility(View.GONE);
			}
		}
		investRecordsAdapter = new BorrowRecordsAdapter(VIPProductRecordActivity.this);
		recordListView.setAdapter(investRecordsAdapter);
		initListeners();
		handler.sendEmptyMessage(REQUEST_INVEST_RECORD_WHAT);
	}

	private void initListeners() {
		refreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				refreshLayout.postDelayed(new Runnable() {

					@Override
					public void run() {
						pageNo = 0;
						updateLoadStatus(true, false);
						// 更新数据
						handler.sendEmptyMessage(REQUEST_INVEST_RECORD_WHAT);
						// 更新完后调用该方法结束刷新
						refreshLayout.setRefreshing(false);
					}
				}, 1000);
			}
		});

		// 加载监听器
		refreshLayout.setOnLoadListener(new OnLoadListener() {
			@Override
			public void onLoad() {
				++pageNo;
				refreshLayout.postDelayed(new Runnable() {
					@Override
					public void run() {
						updateLoadStatus(false, true);
						handler.sendEmptyMessage(REQUEST_INVEST_RECORD_WHAT);
						// 加载完后调用该方法
						refreshLayout.setLoading(false);
					}
				}, 1500);
			}
		});
	}

	private void updateLoadStatus(boolean isRef, boolean isload) {
		this.isRefresh = isRef;
		this.isLoad = isload;
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
		case R.id.product_data_activity_bidBtn:
		case R.id.nodata_layout_btn:
			// 从SettingsManager中读取密码，如果为空意味着没有登录。
			boolean isLogin = !SettingsManager.getLoginPassword(
					VIPProductRecordActivity.this).isEmpty()
					&& !SettingsManager.getUser(VIPProductRecordActivity.this).isEmpty();
			// isLogin = true;// 测试
			Intent intent = new Intent();
			// 1、检测是否已经登录
			if (isLogin) {
				// 已经登录，跳转到购买页面
				intent.putExtra("PRODUCT_INFO", productInfo);
				if("新手标".equals(productInfo.getBorrow_type())){
					isCanbuyXSB(SettingsManager.getUserId(getApplicationContext()), productInfo.getId());
				}else if("vip".equals(productInfo.getBorrow_type())){
					checkIsVip();
				}else{
					checkIsVerify("政信贷投资");
				}
			} else {
				// 未登录，跳转到登录页面
				intent.setClass(VIPProductRecordActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 显示弹出框
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
					intent.setClass(VIPProductRecordActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					if("新手标".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "新手标投资");
					}else if("vip".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "VIP投资");
					}else{
						bundle.putString("type", "政信贷投资");
					}
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
					investBtn.setEnabled(true);
				}else if("绑卡".equals(type)){
					Bundle bundle = new Bundle();
					if("新手标".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "新手标投资");
					}else if("vip".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "VIP投资");
					}else{
						bundle.putString("type", "政信贷投资");
					}
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					intent.setClass(VIPProductRecordActivity.this, BindCardActivity.class);
					startActivity(intent);
					investBtn.setEnabled(true);
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
		// 参数都设置完成了，创建并显示出来
		dialog.show();
		// 设置dialog的宽度
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth() * 6 / 7;
		lp.height = display.getHeight()/3;
		dialog.getWindow().setAttributes(lp);
	}
	
	/**
	 * 判断用户是否为vip用户
	 */
	private void checkIsVip(){
		RequestApis.requestIsVip(VIPProductRecordActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVipUserListener() {
			@Override
			public void isVip(boolean isvip) {
				if(isvip){
					checkIsVerify("VIP投资"); // 只判断有没有实名，不再判断是否绑卡
				}else{
					//非VIP用户不能投资
					showCanotInvestVIPDialog();
				}
			}
		});
	}
	
	/**
	 * 显示弹出框  非VIP用户不能购买元月盈
	 * @param type
	 * @param msg
	 */
	private void showCanotInvestVIPDialog(){
		View contentView = LayoutInflater.from(this)
				.inflate(R.layout.borrow_details_vip_msg_dialog, null);
		final Button leftBtn = (Button) contentView
				.findViewById(R.id.borrow_details_vip_msg_dialog_leftbtn);
		final Button rightBtn = (Button) contentView.
				findViewById(R.id.borrow_details_vip_msg_dialog_rightbtn);
		final TextView msgTV = (TextView) contentView
				.findViewById(R.id.borrow_details_vip_msg_dialog_msg);
		final ImageView delBtn = (ImageView) contentView
				.findViewById(R.id.borrow_details_vip_msg_dialog_delete);
		msgTV.setText("非VIP用户不能购买VIP产品啦~");
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				setResult(111,intent);
				dialog.dismiss();
				finish();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(VIPProductRecordActivity.this,VIPProductCJWTActivity.class);
				startActivity(intent);
			}
		});
		delBtn.setOnClickListener(new OnClickListener() {
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
		lp.height = display.getHeight()/3;
		dialog.getWindow().setAttributes(lp);
	}
	
	/**
	 * 验证用户是否已经认证
	 * @param type “充值”,“提现”
	 */
	private void checkIsVerify(final String type){
		investBtn.setEnabled(false);
		RequestApis.requestIsVerify(VIPProductRecordActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//用户已经实名
					checkIsBindCard(type);
				}else{
					//用户没有实名
					showMsgDialog(VIPProductRecordActivity.this, "实名认证", "请先实名认证！");
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
		RequestApis.requestIsBinding(VIPProductRecordActivity.this, SettingsManager.getUserId(getApplicationContext()), "宝付", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//用户已经绑卡
					if("新手标投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(VIPProductRecordActivity.this, BidXSBActivity.class);
					}else if("政信贷投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(VIPProductRecordActivity.this, BidZXDActivity.class);
					}else if("VIP投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(VIPProductRecordActivity.this, BidVIPActivity.class);
					}
					startActivity(intent);
					investBtn.setEnabled(true);
					finish();
				}else{
					//用户还没有绑卡
					showMsgDialog(VIPProductRecordActivity.this, "绑卡", "因我司变更支付渠道，请您重新绑卡！");
				}
			}
		});
	}
	
	/**
	 * 获取投资记录列表
	 * 
	 * @param investUserId
	 * @param borrowId
	 * @param status
	 * @param pageNo
	 * @param pageSize
	 */
	private void getInvestRecordList(String investUserId, String borrowId,
			String status) {
		if (isFirst && loadingDialog != null) {
			loadingDialog.show();
		}

		AsyncVIPRecordList asyncInvestRecord = new AsyncVIPRecordList(
				VIPProductRecordActivity.this, investUserId, borrowId, 
				status, pageNo, pageSize, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						isFirst = false;
						if (loadingDialog != null && loadingDialog.isShowing()) {
							loadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler
										.obtainMessage(REQUEST_INVEST_RECORD_SUCCESS);
								msg.obj = baseInfo.getmInvestRecordPageInfo();
								handler.sendMessage(msg);
							} else {
								Message msg = handler
										.obtainMessage(REQUEST_INVEST_RECORD_NODATA);
								msg.obj = baseInfo.getMsg();
								handler.sendMessage(msg);
							}
						}

					}
				});
		asyncInvestRecord.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 判断是否可以购买新手标
	 * @param userId
	 * @param borrowId
	 */
	private void isCanbuyXSB(String userId,String borrowId){
		AsyncXSBIscanbuy task = new AsyncXSBIscanbuy(VIPProductRecordActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//用户可以购买新手标
								Intent intent = new Intent();
								intent.putExtra("PRODUCT_INFO", productInfo);
								intent.setClass(VIPProductRecordActivity.this, BidXSBActivity.class);
								startActivity(intent);
							}else if(resultCode == 1001){
								//请先进行实名
								showMsgDialog(VIPProductRecordActivity.this, "实名认证", "请先实名认证！");
							}else if(resultCode == 1002){
								//请先进行绑卡
								boolean isNewUser = SettingsManager.checkIsNewUser(SettingsManager.getUserRegTime(getApplicationContext()));
								if(isNewUser){
									showMsgDialog(VIPProductRecordActivity.this, "绑卡", "请您先绑卡！");
								}else{
									showMsgDialog(VIPProductRecordActivity.this, "绑卡", "因我司变更支付渠道，请您重新绑卡！");
								}
							}else{
								showMsgDialog(VIPProductRecordActivity.this, "不能购买新手标", "此产品限首次购买用户专享！");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
