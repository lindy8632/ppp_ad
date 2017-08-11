package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.ActivitysRegionAdapter;
import com.ylfcf.ppp.async.AsyncActiveList;
import com.ylfcf.ppp.entity.ActiveInfo;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动专区
 * @author Mr.liu
 *
 */
public class ActivitysRegionActivity extends BaseActivity implements OnClickListener{
	private static final String className = "ActivitysRegionActivity";
	private static final int REQUEST_ACTIVE_LIST = 2417;//活动列表
	private static final int REQUEST_ACTIVE_LIST_SUC = 2418;
	private static final int REQUEST_ACTIVE_LIST_FAILE = 2419;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private PullToRefreshListView mPullToRefreshListView;
	private ActivitysRegionAdapter mActivitysRegionAdapter;

	private int pageNo = 0;
	private int pageSize = 10;
	private boolean isLoadMore = false;// 加载更多
	private List<ActiveInfo> activeListTemp = new ArrayList<ActiveInfo>();
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_ACTIVE_LIST:
				requestActiveList(pageNo,pageSize,"正常","app","显示");
				break;
			case REQUEST_ACTIVE_LIST_SUC:
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				if (baseInfo != null) {
					if (!isLoadMore) {
						activeListTemp.clear();
					}
					activeListTemp.addAll(baseInfo.getmActivePageInfo().getActiveList());
					mActivitysRegionAdapter.setItems(activeListTemp);
				}
				isLoadMore = false;
				mPullToRefreshListView.onRefreshComplete();
				break;
			case REQUEST_ACTIVE_LIST_FAILE:
				isLoadMore = false;
				mPullToRefreshListView.onRefreshComplete();
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
		setContentView(R.layout.activitys_region_activity);
		findViews();
		handler.sendEmptyMessage(REQUEST_ACTIVE_LIST);
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("活动专区");

		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.activitys_region_activity_listview);

		mActivitysRegionAdapter = new ActivitysRegionAdapter(this);
		mPullToRefreshListView.setAdapter(mActivitysRegionAdapter);
		initListeners();
	}

	private void initListeners(){
		mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// index从1开始。。。
				if (position != 0) {
					ActiveInfo item = (ActiveInfo) parent.getAdapter()
							.getItem(position);
					if("-5".equals(item.getmActiveStatus().getStatus())){
						//活动已结束
						return;
					}
					Intent intent = new Intent();
					if (item != null) {
						if("HYFL_02".equals(item.getActive_title())){
							//会员福利计划二期
							intent.setClass(ActivitysRegionActivity.this,PrizeRegion2TempActivity.class);
						}else if("MONDAY_ROB_CASH".equals(item.getActive_title())){
							//每周一抢现金
							intent.setClass(ActivitysRegionActivity.this,LXJ5TempActivity.class);
						}else{
							BannerInfo info = new BannerInfo();
							info.setLink_url(item.getUrl_link());
							intent.putExtra("BannerInfo", info);
							intent.setClass(ActivitysRegionActivity.this, BannerTopicActivity.class);
						}
					}
					ActivitysRegionActivity.this.startActivity(intent);
				}
			}
		});

		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 下拉刷新
				pageNo = 0;
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						handler.sendEmptyMessage(REQUEST_ACTIVE_LIST);
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
						handler.sendEmptyMessage(REQUEST_ACTIVE_LIST);
					}
				}, 1000L);

			}
		});
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

	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//友盟统计页面跳转
		UMengStatistics.statisticsResume(this);//友盟统计
	}

	@Override
	protected void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);//友盟统计页面跳转
		UMengStatistics.statisticsPause(this);//友盟统计
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

	/**
	 * 活动专区列表
	 * @param page
	 * @param pageSize
	 * @param status
	 * @param fromWhere
	 * @param picShowStatus
	 */
	private void requestActiveList(int page,int pageSize,
								   String status,String fromWhere,String picShowStatus){
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		AsyncActiveList task = new AsyncActiveList(ActivitysRegionActivity.this,String.valueOf(page),String.valueOf(pageSize),status,fromWhere,picShowStatus,
			new OnCommonInter(){
				@Override
				public void back(BaseInfo baseInfo) {
					if(mLoadingDialog != null && mLoadingDialog.isShowing()){
						mLoadingDialog.dismiss();
					}
					if(baseInfo != null){
						int resultCode = SettingsManager.getResultCode(baseInfo);
						if(resultCode == 0){
							List<ActiveInfo> activeList = baseInfo.getmActivePageInfo().getActiveList();
							Message msg = handler
									.obtainMessage(REQUEST_ACTIVE_LIST_SUC);
							msg.obj = baseInfo;
							handler.sendMessage(msg);
						}else{
							Message msg = handler
									.obtainMessage(REQUEST_ACTIVE_LIST_FAILE);
							msg.obj = baseInfo;
							handler.sendMessage(msg);
						}
					}
				}
			});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
