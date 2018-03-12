package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.BidListVIPAdapter;
import com.ylfcf.ppp.async.AsyncProductPageInfo;
import com.ylfcf.ppp.common.FileUtil;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseProductPageInfo;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;

import java.util.ArrayList;
import java.util.List;

/**
 * VIP产品列表页面
 * @author Mr.liu
 *
 */
public class BorrowListVIPActivity extends BaseActivity implements
		OnClickListener{
	private final String className = "BorrowListVIPActivity";
	public final int REQUEST_PRODUCT_LIST_WHAT = 1800;
	private final int REQUEST_PRODUCT_LIST_SUCCESS = 1801;
	private final int REQUEST_PRODUCT_LIST_FAILE = 1802;
	
	private View topLayout;
	private TextView topTitle;
	private LinearLayout topLeftLayout;
	
	private int pageNo = 0;
	private int pageSize = 20;
	private PullToRefreshListView pullListView;
	private BidListVIPAdapter mBidListAdapter;
	private List<ProductInfo> productList = new ArrayList<ProductInfo>();
	private boolean isFirst = true;
	private boolean isLoadMore = false;// 加载更多

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_PRODUCT_LIST_WHAT:
				requestProductPageInfo("vip", "发布","未满标", "是", "", "");
				break;
			case REQUEST_PRODUCT_LIST_SUCCESS:
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				if (baseInfo != null) {
					if (!isLoadMore) {
						productList.clear();
					}
					productList.addAll(baseInfo.getProductPageInfo()
							.getProductList());
					mBidListAdapter.setItems(productList);
				}
				isLoadMore = false;
				pullListView.onRefreshComplete();
				break;
			case REQUEST_PRODUCT_LIST_FAILE:
				pullListView.onRefreshComplete();
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
		setContentView(R.layout.borrowlist_vip_activity);
		findViews();
		
		handler.sendEmptyMessage(REQUEST_PRODUCT_LIST_WHAT);
	}

	private void findViews(){
		topLeftLayout = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftLayout.setOnClickListener(this);
		topTitle = (TextView) findViewById(R.id.common_page_title);
		topTitle.setText("VIP投资");
		
		View topLayout = LayoutInflater.from(this).inflate(
				R.layout.dq_zxdlist_toplayout, null);
		ImageView topImg = (ImageView) topLayout.findViewById(R.id.dq_zxdlist_toplayout_img);
		topImg.setBackgroundResource(R.drawable.vip_top_logo);
		pullListView = (PullToRefreshListView) findViewById(R.id.zxd_fragment_pull_refresh_list);
		mBidListAdapter = new BidListVIPAdapter(this);
		pullListView.getRefreshableView().addHeaderView(topLayout);
		pullListView.setAdapter(mBidListAdapter);
		initListeners();
	}
	
	private void initListeners(){
		pullListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// index从1开始。。。
				if (position != 1) {
					ProductInfo item = (ProductInfo) parent.getAdapter()
							.getItem(position);
					Intent intent = new Intent();
					intent.setClass(BorrowListVIPActivity.this, BorrowDetailVIPActivity.class);
					if (item != null) {
						intent.putExtra("PRODUCT_INFO", item);
					} else {
					}
					BorrowListVIPActivity.this.startActivityForResult(intent, 110);
				}
			}
		});

		pullListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 下拉刷新
				pageNo = 0;
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						isLoadMore = false;
						handler.sendEmptyMessage(REQUEST_PRODUCT_LIST_WHAT);
					}
				}, 1000L);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 上拉加载更多
				pageNo++;
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						isLoadMore = true;
						handler.sendEmptyMessage(REQUEST_PRODUCT_LIST_WHAT);
					}
				}, 1000L);

			}
		});
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 110:
//			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//友盟统计页面跳转
		UMengStatistics.statisticsResume(this);//友盟统计时长
	}
	
	@Override
	public void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);//友盟统计页面跳转
		UMengStatistics.statisticsPause(this);//友盟统计时长
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
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
	 * 产品列表
	 * 
	 */
	private void requestProductPageInfo(String borrowType, String borrowStatus,String moneyStatus,
			String isShow, String isWap, String plan) {
		if (isFirst && mLoadingDialog != null && !isFinishing()) {
			mLoadingDialog.show();
		}

		// 先加载缓存里面的数据，然后再请求接口刷新
		String result = null;
		BaseInfo baseInfo = null;
		try {
			byte[] initJsonB = FileUtil.readByte(BorrowListVIPActivity.this,
					FileUtil.YLFCF_VIP_TOTAL_CACHE);
			result = new String(initJsonB);
			// 解析init.json
			if (result != null && !"".equals(result)) {
				baseInfo = JsonParseProductPageInfo.parseData(result);
			}
		} catch (Exception exx) {
		}

		if (baseInfo != null && isFirst) {
			int resultCode = SettingsManager.getResultCode(baseInfo);
			if (resultCode == 0) {
				Message msg = handler
						.obtainMessage(REQUEST_PRODUCT_LIST_SUCCESS);
				msg.obj = baseInfo;
				handler.sendMessage(msg);
			}
		}
		isFirst = false;
		AsyncProductPageInfo productTask = new AsyncProductPageInfo(
				BorrowListVIPActivity.this, String.valueOf(pageNo), String.valueOf(pageSize),
				borrowType, borrowStatus,moneyStatus,isShow, isWap, plan, isFirst,"2","",
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}

						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler
										.obtainMessage(REQUEST_PRODUCT_LIST_SUCCESS);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							} else {
								Message msg = handler
										.obtainMessage(REQUEST_PRODUCT_LIST_FAILE);
								msg.obj = baseInfo.getError();
								handler.sendMessage(msg);
							}
						} else {
							Message msg = handler
									.obtainMessage(REQUEST_PRODUCT_LIST_FAILE);
							handler.sendMessage(msg);
						}
					}
				});
		productTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
