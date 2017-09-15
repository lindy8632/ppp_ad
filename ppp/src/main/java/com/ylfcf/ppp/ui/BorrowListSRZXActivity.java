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
import com.ylfcf.ppp.adapter.BorrowListSRZXAdapter;
import com.ylfcf.ppp.async.AsyncAppointBorrowList;
import com.ylfcf.ppp.common.FileUtil;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.MoneyStatus;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseProductPageInfo;
import com.ylfcf.ppp.util.Constants;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * 私人尊享产品列表页面
 * @author Mr.liu
 *
 */
public class BorrowListSRZXActivity extends BaseActivity implements
		OnClickListener{

	private final String className = "BorrowListSRZXActivity";
	public final int REQUEST_PRODUCT_LIST_WHAT = 1800;
	private final int REQUEST_PRODUCT_LIST_SUCCESS = 1801;
	private final int REQUEST_PRODUCT_LIST_FAILE = 1802;

	private View topLayout;
	private TextView topTitle;
	private LinearLayout topLeftLayout;
	
	private int pageNo = 0;
	private int pageSize = 10;
	private PullToRefreshListView pullListView;
	private BorrowListSRZXAdapter mBidListAdapter;
	private List<ProductInfo> productList = new ArrayList<ProductInfo>();
	private boolean isFirst = true;
	private boolean isLoadMore = false;// 加载更多

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_PRODUCT_LIST_WHAT:
				requestProductPageInfo("发布",MoneyStatus.NOFULL);
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
					if("10".equals(baseInfo.getProductPageInfo().getCount())){
						pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
					}else{
						pullListView.setMode(PullToRefreshBase.Mode.BOTH);
					}
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
		setContentView(R.layout.borrowlist_srzx_activity);
		findViews();
		
		handler.sendEmptyMessage(REQUEST_PRODUCT_LIST_WHAT);
	}
	
	private void findViews(){
		topLeftLayout = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftLayout.setOnClickListener(this);
		topTitle = (TextView) findViewById(R.id.common_page_title);
		topTitle.setText("私人尊享");
		
		View topLayout = LayoutInflater.from(this).inflate(
				R.layout.dq_zxdlist_toplayout, null);
		ImageView topImg = (ImageView) topLayout.findViewById(R.id.dq_zxdlist_toplayout_img);
		topImg.setBackgroundResource(R.drawable.srzx_top_logo);
		pullListView = (PullToRefreshListView) findViewById(R.id.zxd_fragment_pull_refresh_list);
		mBidListAdapter = new BorrowListSRZXAdapter(this);
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
					intent.setClass(BorrowListSRZXActivity.this, BorrowDetailSRZXActivity.class);
					if (item != null) {
						intent.putExtra("PRODUCT_INFO", item);
					} else {
					}
					BorrowListSRZXActivity.this.startActivity(intent);
				}else{
					Intent intentSRZXAppoint = new Intent(BorrowListSRZXActivity.this,BannerTopicActivity.class);
					BannerInfo info = new BannerInfo();
					info.setArticle_id(Constants.TopicType.SRZX_APPOINT);
					info.setLink_url(URLGenerator.SRZX_TOPIC_URL);
					intentSRZXAppoint.putExtra("BannerInfo",info);
					startActivity(intentSRZXAppoint);
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
						isFirst = false;
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
						isFirst = false;
						handler.sendEmptyMessage(REQUEST_PRODUCT_LIST_WHAT);
					}
				}, 1000L);

			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//友盟统计页面跳转
		UMengStatistics.statisticsResume(this);//友盟统计时长
		if(!isFirst){
			isLoadMore = false;
			isFirst = false;
			handler.sendEmptyMessage(REQUEST_PRODUCT_LIST_WHAT);
		}
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
	 * 私人尊享产品列表
	 * 
	 */
	private void requestProductPageInfo(String borrowStatus, final String moneyStatus) {
		if (isFirst && mLoadingDialog != null && !isFinishing()) {
			mLoadingDialog.show();
		}
		// 如果是第一次请求此接口，先加载缓存里面的数据，然后再请求接口刷新
		if (isFirst) {
			String result = null;
			BaseInfo baseInfo = null;
			try {
				byte[] initJsonB = FileUtil.readByte(BorrowListSRZXActivity.this,
						FileUtil.YLFCF_SRZX_TOTAL_CACHE);
				result = new String(initJsonB);
				// 解析init.json
				if (result != null && !"".equals(result)) {
					baseInfo = JsonParseProductPageInfo.parseData(result);
				}
			} catch (Exception exx) {
			}
			int resultCode = SettingsManager.getResultCode(baseInfo);
			if (resultCode == 0) {
				Message msg = handler
						.obtainMessage(REQUEST_PRODUCT_LIST_SUCCESS);
				msg.obj = baseInfo;
				handler.sendMessage(msg);
			}
		}
		AsyncAppointBorrowList productTask = new AsyncAppointBorrowList(
				BorrowListSRZXActivity.this, borrowStatus,moneyStatus,String.valueOf(pageNo), String.valueOf(pageSize),
				isFirst,new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}
						isFirst = false;
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
