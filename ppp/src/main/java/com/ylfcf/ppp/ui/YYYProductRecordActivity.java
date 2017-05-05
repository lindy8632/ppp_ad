package com.ylfcf.ppp.ui;

import java.util.ArrayList;
import java.util.List;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.BorrowRecordsAdapter;
import com.ylfcf.ppp.async.AsyncInvestRecord;
import com.ylfcf.ppp.async.AsyncWDYInvestDetail;
import com.ylfcf.ppp.async.AsyncWDYInvestRecord;
import com.ylfcf.ppp.async.AsyncXSBIscanbuy;
import com.ylfcf.ppp.async.AsyncYYYInvestRecord;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.InvestRecordPageInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
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
 * 投资记录 -- 产品详情里面的投资记录 ---- 元月盈
 * @author Mr.liu
 *
 */
public class YYYProductRecordActivity extends BaseActivity implements
		OnClickListener {
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private ProductInfo productInfo;

	private static final int REQUEST_YYY_INVEST_RECORD_WHAT = 1021;//元月盈投资记录
	private static final int REQUEST_INVEST_RECORD_SUCCESS = 1022;
	private static final int REQUEST_INVEST_RECORD_NODATA = 1023; // 无数据
	private static final int REQUEST_WDY_INVEST_RECORD_WHAT = 1024;//稳定盈投资记录

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
			case REQUEST_YYY_INVEST_RECORD_WHAT:
				if (productInfo != null) {
					getYYYInvestRecordList(productInfo.getId(), "", "", "", "用户投资");
				}
				break;
			case REQUEST_WDY_INVEST_RECORD_WHAT:
				//稳定盈
				if (productInfo != null) {
					getWDYInvestRecordList(productInfo.getId());
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
		builder = new AlertDialog.Builder(YYYProductRecordActivity.this,
				R.style.Dialog_Transparent); // 先得到构造器
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		if(bundle != null){
			productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
		}
		
		layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		loadingDialog = new LoadingDialog(YYYProductRecordActivity.this,
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
				investBtn.setText("投资结束");
				nodataBtn.setVisibility(View.GONE);
			}
		}
		investRecordsAdapter = new BorrowRecordsAdapter(YYYProductRecordActivity.this);
		recordListView.setAdapter(investRecordsAdapter);
		initListeners();
		if("元月盈".equals(productInfo.getBorrow_type())){
			handler.sendEmptyMessage(REQUEST_YYY_INVEST_RECORD_WHAT);
		}else if("稳定盈".equals(productInfo.getBorrow_type())){
			handler.sendEmptyMessage(REQUEST_WDY_INVEST_RECORD_WHAT);
		}
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
						if("元月盈".equals(productInfo.getBorrow_type())){
							handler.sendEmptyMessage(REQUEST_YYY_INVEST_RECORD_WHAT);
						}else if("稳定盈".equals(productInfo.getBorrow_type())){
							handler.sendEmptyMessage(REQUEST_WDY_INVEST_RECORD_WHAT);
						}
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
						if("元月盈".equals(productInfo.getBorrow_type())){
							handler.sendEmptyMessage(REQUEST_YYY_INVEST_RECORD_WHAT);
						}else if("稳定盈".equals(productInfo.getBorrow_type())){
							handler.sendEmptyMessage(REQUEST_WDY_INVEST_RECORD_WHAT);
						}
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.product_data_activity_bidBtn:
		case R.id.nodata_layout_btn:
			// 从SettingsManager中读取密码，如果为空意味着没有登录。
			boolean isLogin = !SettingsManager.getLoginPassword(
					YYYProductRecordActivity.this).isEmpty()
					&& !SettingsManager.getUser(YYYProductRecordActivity.this).isEmpty();
			// isLogin = true;// 测试
			Intent intent = new Intent();
			// 1、检测是否已经登录
			if (isLogin) {
				// 已经登录，跳转到购买页面
				checkIsVerify(productInfo.getBorrow_type());
			} else {
				// 未登录，跳转到登录页面
				intent.setClass(YYYProductRecordActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
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
		msgTV.setText(msg);
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if("实名认证".equals(type)){
					intent.setClass(YYYProductRecordActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					if("元月盈".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "元月盈投资");
					}else if("稳定盈".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "稳定盈投资");
					}
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
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
	 * 验证用户是否已经认证
	 * @param type “充值”,“提现”
	 */
	private void checkIsVerify(final String type){
		investBtn.setEnabled(false);
		RequestApis.requestIsVerify(YYYProductRecordActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//用户已经实名,此处只判断有没有实名即可，不再判断有没有绑卡
					if("元月盈".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(YYYProductRecordActivity.this, BidYYYActivity.class);
					}else if("稳定盈".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(YYYProductRecordActivity.this, BidWDYActivity.class);
					}
					startActivity(intent);
					investBtn.setEnabled(true);
					finish();
				}else{
					//用户没有实名
					showMsgDialog(YYYProductRecordActivity.this, "实名认证", "请先实名认证！");
				}
			}
			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
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
	private void getYYYInvestRecordList(String borrowId, String investStatus,
			String investUserId, String returnStatus,String type) {
		if (isFirst && loadingDialog != null) {
			loadingDialog.show();
		}
		AsyncYYYInvestRecord asyncInvestRecord = new AsyncYYYInvestRecord(
				YYYProductRecordActivity.this, borrowId, investStatus, investUserId,
				returnStatus,type, pageNo, pageSize, new OnCommonInter() {
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
	 * 稳定盈的投资记录
	 * @param borrowId
	 * @param userId
	 * @param type
	 * @param status
	 */
	private void getWDYInvestRecordList(String borrowId){
		if (isFirst && loadingDialog != null) {
			loadingDialog.show();
		}
		AsyncWDYInvestRecord task = new AsyncWDYInvestRecord(YYYProductRecordActivity.this, borrowId, "","","",pageNo, pageSize, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						isFirst = false;
						if (loadingDialog != null && loadingDialog.isShowing()) {
							loadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
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
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
