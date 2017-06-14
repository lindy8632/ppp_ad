package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ylfcf.view.CustomerFooter;
import com.example.ylfcf.widget.XRefreshView;
import com.example.ylfcf.widget.XRefreshView.SimpleXRefreshListener;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.LimitMoneyAdapter;
import com.ylfcf.ppp.async.AsyncQuickBankList;
import com.ylfcf.ppp.entity.BankInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 限额说明
 * 
 * @author Administrator
 * 
 */
public class LimitPromptActivity extends BaseActivity implements
		OnClickListener {
	private static final int REQUEST_QUICK_BANK_WHAT = 7120;
	private static final int REQUEST_QUICK_BANK_SUC = 7121;
	private static final int REQUEST_QUICK_BANK_FAILE = 7122;

	private XRefreshView mXRefreshView;
	private ListView mListView;
//	private PullToRefreshListView listview;
	private LimitMoneyAdapter adapter;
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private View bottomView;
	private boolean isRefresh = false;//是否下拉刷新数据。
	private boolean isLoadMore = false;//上否是上拉加载更多

	private int page = 0;
	private int pageSize = 50;
	private List<BankInfo> bankInfoListTemp = new ArrayList<BankInfo>();
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_QUICK_BANK_WHAT:
				requestBankList("启用", "宝付支付");
				break;
			case REQUEST_QUICK_BANK_SUC:
				List<BankInfo> banklist = (List<BankInfo>)msg.obj;
				if(isRefresh){
					bankInfoListTemp.clear();
				}
				bankInfoListTemp.addAll(banklist);
				saveBankList(bankInfoListTemp);
				updateAdapter(bankInfoListTemp);
				isRefresh = false;
				isLoadMore = false;
				break;
			case REQUEST_QUICK_BANK_FAILE:
				if(isLoadMore){
					mXRefreshView.setLoadComplete(true);
				}
				isRefresh = false;
				isLoadMore = false;
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
		setContentView(R.layout.limit_prompt_activity);
		findViews();
		handler.sendEmptyMessage(REQUEST_QUICK_BANK_WHAT);
	}

	@Override
	protected void onResume() {
		super.onResume();
//		mXRefreshView.startRefresh();
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
		topTitleTV.setText("限额说明");

		mXRefreshView = (XRefreshView) findViewById(R.id.limit_prompt_activity_xrefreshview);
		bottomView = LayoutInflater.from(LimitPromptActivity.this).inflate(
				R.layout.limit_bottom_layout, null);
		mListView = (ListView) findViewById(R.id.limit_prompt_activity_listview);
		mListView.addFooterView(bottomView);
//		listview = (PullToRefreshListView) findViewById(R.id.limit_prompt_activity_listview);
//		listview.getRefreshableView().addFooterView(bottomView);
		adapter = new LimitMoneyAdapter(LimitPromptActivity.this);
//		listview.setAdapter(adapter);
		mListView.setAdapter(adapter);
		initXRefrshView();
		initListeners();
	}

	private void initXRefrshView(){
		mXRefreshView.setPullLoadEnable(true);
		mXRefreshView.setPinnedTime(1000);
		mXRefreshView.setAutoLoadMore(false);
//		xRefreshView.setCustomHeaderView(new CustomHeader(this));
//		xRefreshView.setCustomHeaderView(new XRefreshViewHeader(this));
		mXRefreshView.setMoveForHorizontal(true);
		mXRefreshView.setCustomFooterView(new CustomerFooter(this));
//		xRefreshView.setPinnedContent(true);
		//设置当非RecyclerView上拉加载完成以后的回弹时间
		mXRefreshView.setScrollBackDuration(300);
	}

	private void initListeners() {
		mXRefreshView.setXRefreshViewListener(new SimpleXRefreshListener() {
			@Override
			public void onRefresh(boolean isPullDown) {
				isRefresh = true;
				isLoadMore = false;
				page = 0;
				handler.sendEmptyMessage(REQUEST_QUICK_BANK_WHAT);
			}

			@Override
			public void onLoadMore(boolean isSilence) {
				isRefresh = false;
				isLoadMore = true;
				page ++;
				handler.sendEmptyMessage(REQUEST_QUICK_BANK_WHAT);
			}
		});
//		listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {
//					@Override
//					public void onPullDownToRefresh(
//							PullToRefreshBase<ListView> refreshView) {
//						// 下拉刷新
//						isRefresh = true;
//						handler.sendEmptyMessage(REQUEST_QUICK_BANK_WHAT);
//					}
//
//					@Override
//					public void onPullUpToRefresh(
//							PullToRefreshBase<ListView> refreshView) {
//						// 上拉加载更多
//					}
//
//				});
	}

	private void updateAdapter(List<BankInfo> bankList) {
		if (bankList == null) {
			return;
		}
		adapter.setItems(bankList);
	}

	private void updateAdapter(Map<String, BankInfo> bankMap) {
		List<BankInfo> bankList = new ArrayList<BankInfo>();
		Iterator<Map.Entry<String, BankInfo>> entries = bankMap.entrySet()
				.iterator();
		while (entries.hasNext()) {
			Map.Entry<String, BankInfo> entry = entries.next();
			bankList.add(entry.getValue());
		}
		adapter.setItems(bankList);
	}

	private void saveBankList(List<BankInfo> bankList) {
		if (bankList == null || bankList.size() <= 0) {
			return;
		}
		SettingsManager.bankMap.clear();
		for (int i = 0; i < bankList.size(); i++) {
			BankInfo bankInfo = bankList.get(i);
			SettingsManager.bankMap.put(bankInfo.getBank_code(), bankInfo);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 银行列表
	 * 
	 * @param status
	 * @param payWayname
	 */
	private void requestBankList(String status, String payWayname) {
		if (SettingsManager.bankMap != null && !SettingsManager.bankMap.isEmpty() && !isRefresh && !isLoadMore) {
			updateAdapter(SettingsManager.bankMap);
			return;
		}
		AsyncQuickBankList bankTask = new AsyncQuickBankList(
				LimitPromptActivity.this, status, payWayname,
				String.valueOf(page), String.valueOf(pageSize),
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								mXRefreshView.stopRefresh();
							}
						},1000L);
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								List<BankInfo> bankList = baseInfo.getBankPageInfo().getBankList();
								Message msg = handler.obtainMessage(REQUEST_QUICK_BANK_SUC);
								msg.obj = bankList;
								handler.sendMessage(msg);
							}else{
								Message msg = handler.obtainMessage(REQUEST_QUICK_BANK_FAILE);
								msg.obj = baseInfo.getMsg();
								handler.sendMessage(msg);
							}
						}else{
							Message msg = handler.obtainMessage(REQUEST_QUICK_BANK_FAILE);
							msg.obj = "";
							handler.sendMessage(msg);
						}
					}
				});
		bankTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
