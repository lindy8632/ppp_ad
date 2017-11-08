package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.WithdrawListAdapter;
import com.ylfcf.ppp.async.AsyncWithdrawList;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.WithdrawOrderInfo;
import com.ylfcf.ppp.entity.WithdrawOrderPageInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.widget.RefreshLayout;
import com.ylfcf.ppp.widget.RefreshLayout.OnLoadListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 提现申请列表
 * 
 * @author Administrator
 * 
 */
public class WithdrawListActivity extends BaseActivity implements
		OnClickListener {
	private static final String className = "WithdrawListActivity";
	private static final int REQUEST_WITHDRAW_RECORD_WHAT = 2801;
	private static final int REQUEST_WITHDRAW_RECORD_SUCCESS = 2802;
	private static final int REQUEST_WITHDRAW_RECORD_FAILE = 2803;

	private ListView listview;
	private WithdrawListAdapter adapter;
	private LayoutInflater layoutInflater = null;
	private List<WithdrawOrderInfo> orderList = new ArrayList<WithdrawOrderInfo>();
	private int page = 0;
	private int pageSize = 10;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private RefreshLayout refreshLayout;
	private TextView nodataPrompt;
	private View headerView;

	private boolean isRefresh = true;// 下拉刷新
	private boolean isLoad = false;// 上拉加载更多
	private boolean isFirst = true;// 是否是首次加载数据

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_WITHDRAW_RECORD_WHAT:
				withdrawList();
				break;
			case REQUEST_WITHDRAW_RECORD_SUCCESS:
				WithdrawOrderPageInfo pageInfo = (WithdrawOrderPageInfo) msg.obj;
				nodataPrompt.setVisibility(View.GONE);
				refreshLayout.setVisibility(View.VISIBLE);
				if (isRefresh) {
					orderList.clear();
					orderList.addAll(pageInfo.getOrderList());
				} else if (isLoad) {
					orderList.addAll(pageInfo.getOrderList());
				}
				updateAdapter(orderList);
				break;
			case REQUEST_WITHDRAW_RECORD_FAILE:
				if (isRefresh) {
					nodataPrompt.setVisibility(View.VISIBLE);
					refreshLayout.setVisibility(View.GONE);
				} else if (isLoad) {

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
		setContentView(R.layout.withdraw_list_activity);
		layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		findViews();
		if (isFirst && !isFinishing()) {
			mLoadingDialog.show();
		}
		handler.sendEmptyMessage(REQUEST_WITHDRAW_RECORD_WHAT);
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("提现申请列表");

		headerView = layoutInflater.inflate(
				R.layout.withdraw_listview_headerview, null);
		refreshLayout = (RefreshLayout) findViewById(R.id.withdraw_list_activity_mainlayout);
		nodataPrompt = (TextView) findViewById(R.id.withdraw_list_activity_prompt_text);
		nodataPrompt.setText("暂无提现记录");

		listview = (ListView) findViewById(R.id.withdraw_list_activity_listview);
		adapter = new WithdrawListAdapter(WithdrawListActivity.this);
		listview.addHeaderView(headerView);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				WithdrawOrderInfo orderInfo = (WithdrawOrderInfo) parent
						.getAdapter().getItem(position);
				if(orderInfo == null)
					return;
				Intent intent = new Intent(WithdrawListActivity.this,
						WithdrawDetailsActivity.class);
				intent.putExtra("WithdrawOrderInfo", orderInfo);
				startActivity(intent);

			}
		});
		// 设置下拉刷新监听器
		refreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				refreshLayout.postDelayed(new Runnable() {

					@Override
					public void run() {
						page = 0;
						updateLoadStatus(true, false);
						// 更新数据
						handler.sendEmptyMessage(REQUEST_WITHDRAW_RECORD_WHAT);
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
				++page;
				refreshLayout.postDelayed(new Runnable() {
					@Override
					public void run() {
						updateLoadStatus(false, true);
						handler.sendEmptyMessage(REQUEST_WITHDRAW_RECORD_WHAT);
						// 加载完后调用该方法
						refreshLayout.setLoading(false);
					}
				}, 1500);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//友盟统计页面跳转
		UMengStatistics.statisticsResume(this);//友盟统计时长
		page = 0;
		updateLoadStatus(true, false);
		// 更新数据
		handler.sendEmptyMessage(REQUEST_WITHDRAW_RECORD_WHAT);
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
	
	private void updateLoadStatus(boolean isRef, boolean isload) {
		this.isRefresh = isRef;
		this.isLoad = isload;
	}

	private void withdrawList() {
		long endTimeLong = System.currentTimeMillis();
		long startTimeLong = endTimeLong - 1000 * 60 * 60 * 24 * 30;
		requestWithdrawList(SettingsManager.getUserId(getApplicationContext()),
				"", "", "");
	}

	private void updateAdapter(List<WithdrawOrderInfo> list) {
		adapter.setItems(list);
	}

	private void requestWithdrawList(String userId, String status,
			String startTime, String endTime) {
		AsyncWithdrawList task = new AsyncWithdrawList(
				WithdrawListActivity.this, userId, String.valueOf(page),
				String.valueOf(pageSize), status, startTime, endTime,
				new OnCommonInter() {
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
								Message msg = handler
										.obtainMessage(REQUEST_WITHDRAW_RECORD_SUCCESS);
								msg.obj = baseInfo.getWithdrawOrderPageInfo();
								handler.sendMessage(msg);
							} else {
								Message msg = handler
										.obtainMessage(REQUEST_WITHDRAW_RECORD_FAILE);
								handler.sendMessage(msg);
							}
						} else {
							Message msg = handler
									.obtainMessage(REQUEST_WITHDRAW_RECORD_FAILE);
							handler.sendMessage(msg);
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
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
}
