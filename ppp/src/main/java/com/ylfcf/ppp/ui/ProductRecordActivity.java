package com.ylfcf.ppp.ui;

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
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.BorrowRecordsAdapter;
import com.ylfcf.ppp.async.AsyncInvestRecord;
import com.ylfcf.ppp.async.AsyncXSBIscanbuy;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.InvestRecordPageInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.util.Constants;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.widget.RefreshLayout;
import com.ylfcf.ppp.widget.RefreshLayout.OnLoadListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 投资记录 -- 产品详情里面的投资记录 ---- 政信贷
 * 
 * @author Administrator
 * 
 */
public class ProductRecordActivity extends BaseActivity implements
		OnClickListener {
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private ProductInfo productInfo;
	private ProjectInfo projectInfo;

	private static final int REQUEST_INVEST_RECORD_WHAT = 1021;
	private static final int REQUEST_INVEST_RECORD_SUCCESS = 1022;
	private static final int REQUEST_INVEST_RECORD_NODATA = 1023; // 无数据

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
					getInvestRecordList("", productInfo.getId(), status, "1");
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
		builder = new AlertDialog.Builder(ProductRecordActivity.this,
				R.style.Dialog_Transparent); // 先得到构造器
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		if(bundle != null){
			projectInfo = (ProjectInfo) bundle.getSerializable("PROJECT_INFO");
			productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
		}
		
		layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
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
		Date addDate = null;
		try{
			addDate = sdf.parse(productInfo.getAdd_time());
		}catch (Exception e){
			e.printStackTrace();
		}
		if(productInfo != null){
			if("未满标".equals(productInfo.getMoney_status())){
				if(SettingsManager.checkYYYJIAXI(addDate)==0 && "元年鑫".equals(productInfo.getBorrow_type())&&Constants.UserType.USER_COMPANY.
						equals(SettingsManager.getUserType(ProductRecordActivity.this))){
					investBtn.setEnabled(false);
				}else{
					investBtn.setEnabled(true);
				}
				investBtn.setText("立即投资");
				nodataBtn.setVisibility(View.VISIBLE);
			}else{
				investBtn.setEnabled(false);
				investBtn.setText("投资已结束");
				nodataBtn.setVisibility(View.GONE);
			}
		}
		investRecordsAdapter = new BorrowRecordsAdapter(ProductRecordActivity.this);
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
					ProductRecordActivity.this).isEmpty()
					&& !SettingsManager.getUser(ProductRecordActivity.this).isEmpty();
			// isLogin = true;// 测试
			Intent intent = new Intent();
			// 1、检测是否已经登录
			if (isLogin) {
				// 已经登录，跳转到购买页面
				intent.putExtra("PRODUCT_INFO", productInfo);
				if("新手标".equals(productInfo.getBorrow_type())){
					isCanbuyXSB(SettingsManager.getUserId(getApplicationContext()), productInfo.getId());
				}else if("vip".equals(productInfo.getBorrow_type())){
					checkIsVerify("VIP投资");
				}else{
					checkIsVerify("政信贷投资");
				}
			} else {
				// 未登录，跳转到登录页面
				intent.setClass(ProductRecordActivity.this,LoginActivity.class);
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
					intent.setClass(ProductRecordActivity.this,UserVerifyActivity.class);
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
					intent.setClass(ProductRecordActivity.this, BindCardActivity.class);
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
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		RequestApis.requestIsVerify(ProductRecordActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				if(flag){
					//用户已经实名 不判断有没有绑卡
					investBtn.setEnabled(true);
					Intent intent = new Intent();
					intent.putExtra("PRODUCT_INFO", productInfo);
					intent.setClass(ProductRecordActivity.this, BidZXDActivity.class);
					startActivity(intent);
				}else{
					//用户没有实名
					showMsgDialog(ProductRecordActivity.this, "实名认证", "请先实名认证！");
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
		RequestApis.requestIsBinding(ProductRecordActivity.this, SettingsManager.getUserId(getApplicationContext()), "宝付", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//用户已经绑卡
					if("新手标投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductRecordActivity.this, BidXSBActivity.class);
					}else if("政信贷投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductRecordActivity.this, BidZXDActivity.class);
					}else if("VIP投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductRecordActivity.this, BidVIPActivity.class);
					}
					startActivity(intent);
					investBtn.setEnabled(true);
					finish();
				}else{
					//用户还没有绑卡
					showMsgDialog(ProductRecordActivity.this, "绑卡", "因我司变更支付渠道，请您重新绑卡！");
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
	 */
	private void getInvestRecordList(String investUserId, String borrowId,
			String status, String isAddCoin) {
		if (isFirst && mLoadingDialog != null) {
			mLoadingDialog.show();
		}

		AsyncInvestRecord asyncInvestRecord = new AsyncInvestRecord(
				ProductRecordActivity.this, investUserId, borrowId, status,
				isAddCoin, pageNo, pageSize, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						isFirst = false;
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler.obtainMessage(REQUEST_INVEST_RECORD_SUCCESS);
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
		AsyncXSBIscanbuy task = new AsyncXSBIscanbuy(ProductRecordActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//用户可以购买新手标
								Intent intent = new Intent();
								intent.putExtra("PRODUCT_INFO", productInfo);
								intent.setClass(ProductRecordActivity.this, BidXSBActivity.class);
								startActivity(intent);
							}else if(resultCode == 1001){
								//请先进行实名
								showMsgDialog(ProductRecordActivity.this, "实名认证", "请先实名认证！");
							}else if(resultCode == 1002){
								//请先进行绑卡
								boolean isNewUser = SettingsManager.checkIsNewUser(SettingsManager.getUserRegTime(getApplicationContext()));
								if(isNewUser){
									showMsgDialog(ProductRecordActivity.this, "绑卡", "请您先绑卡！");
								}else{
									showMsgDialog(ProductRecordActivity.this, "绑卡", "因我司变更支付渠道，请您重新绑卡！");
								}
							}else{
								showMsgDialog(ProductRecordActivity.this, "不能购买新手标", "此产品限首次购买用户专享！");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
