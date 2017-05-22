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
import com.ylfcf.ppp.async.AsyncXCFLActiveTime;
import com.ylfcf.ppp.entity.ActiveInfo;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.widget.LoadingDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动专区
 * @author Mr.liu
 *
 */
public class ActivitysRegionActivity extends BaseActivity implements OnClickListener{
	private static final int REQUEST_ACTIVE_SIGN_START_WHAT = 2412;//三月份签到活动
	private static final int REQUEST_ACTIVE_PRIZEREGION2_START_WHAT = 2414;//福利专区2期
	private static final int REQUEST_ACTIVE_START_SUCCESS = 2413;
	private static final int REQUEST_ACTIVE_YQHY_START_WHAT = 2415;//四月份邀请好友活动
	private static final int REQUEST_ACTIVE_QXJ5_START_WHAT = 2416;//五月份每周一抢现金

	private static final int REQUEST_ACTIVE_LIST = 2417;//活动列表
	private static final int REQUEST_ACTIVE_LIST_SUC = 2418;
	private static final int REQUEST_ACTIVE_LIST_FAILE = 2419;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private LinearLayout signLayout;//天天签到
	private LinearLayout ysqLayout;//领压岁钱
	private LinearLayout mbLayout;//秒标
	private LinearLayout prizeRegionLayout;//会员礼品专区
	private LinearLayout xsfxLayout;//限时返现
	private LinearLayout prizeRegion2Layout;//会员福利计划2期
	private LinearLayout yqhyLayout;//邀请好友返现。
	private LinearLayout qxj5Layout;//5月份活动 每周一抢现金
	private TextView signTV;//签到
	private TextView ysqTV;//压岁钱
	private TextView mbTV;//秒标
	private TextView prizeRegionTV;//会员礼品专区
	private TextView xsfxTV;//显示秒标
	private TextView prizeRegion2TV;//会员福利计划
	private TextView yqhyTV;//邀请好友返现
	private TextView qxj5TV;//

	private PullToRefreshListView mPullToRefreshListView;
	private ActivitysRegionAdapter mActivitysRegionAdapter;

	private int pageNo = 0;
	private int pageSize = 10;
	private boolean isLoadMore = false;// 加载更多
	private List<ActiveInfo> activeListTemp = new ArrayList<ActiveInfo>();
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private LoadingDialog mLoadingDialog;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_ACTIVE_SIGN_START_WHAT:
				requestActiveTime("QD_03");
				break;
			case REQUEST_ACTIVE_PRIZEREGION2_START_WHAT:
				requestActiveTime("HYFL_02");
				break;
			case REQUEST_ACTIVE_YQHY_START_WHAT:
				//四月份邀请好友返现活动
				requestActiveTime("TGY_01");
				break;
			case REQUEST_ACTIVE_QXJ5_START_WHAT:
				requestActiveTime("MONDAY_ROB_CASH");
				break;
			case REQUEST_ACTIVE_START_SUCCESS:
				break;
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
		mApp.addActivity(this);
		mLoadingDialog = new LoadingDialog(ActivitysRegionActivity.this, "正在加载...", R.anim.loading);
		handler.sendEmptyMessage(REQUEST_ACTIVE_SIGN_START_WHAT);
		handler.sendEmptyMessageDelayed(REQUEST_ACTIVE_PRIZEREGION2_START_WHAT, 200L);
		handler.sendEmptyMessageDelayed(REQUEST_ACTIVE_YQHY_START_WHAT, 220L);
		handler.sendEmptyMessageDelayed(REQUEST_ACTIVE_QXJ5_START_WHAT,240L);
		findViews();
		handler.sendEmptyMessage(REQUEST_ACTIVE_LIST);
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("活动专区");

		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.activitys_region_activity_listview);
		prizeRegion2Layout = (LinearLayout) findViewById(R.id.activitys_region_activity_prizeregion2_layout);
		prizeRegion2Layout.setOnClickListener(this);
		signLayout = (LinearLayout) findViewById(R.id.activitys_region_activity_sign_layout);
		signLayout.setOnClickListener(this);
		ysqLayout = (LinearLayout) findViewById(R.id.activitys_region_activity_ysq_layout);
		ysqLayout.setOnClickListener(this);
		mbLayout = (LinearLayout) findViewById(R.id.activitys_region_activity_mb_layout);
		mbLayout.setOnClickListener(this);
		prizeRegionLayout = (LinearLayout) findViewById(R.id.activitys_region_activity_prizeregion_layout);
		prizeRegionLayout.setOnClickListener(this);
		xsfxLayout = (LinearLayout) findViewById(R.id.activitys_region_activity_xsfx_layout);
		xsfxLayout.setOnClickListener(this);
		yqhyLayout = (LinearLayout) findViewById(R.id.activitys_region_activity_yqhy_layout);
		yqhyLayout.setOnClickListener(this);
		qxj5Layout = (LinearLayout) findViewById(R.id.activitys_region_activity_qxj5_layout);
		qxj5Layout.setOnClickListener(this);
		
		signTV = (TextView) findViewById(R.id.activitys_region_sign_tv);
		ysqTV = (TextView) findViewById(R.id.activitys_region_ysq_tv);
		mbTV = (TextView) findViewById(R.id.activitys_region_mb_tv);
		prizeRegionTV = (TextView) findViewById(R.id.activitys_region_prizeregion_tv);
		xsfxTV = (TextView) findViewById(R.id.activitys_region_xsfx_tv);
		prizeRegion2TV = (TextView) findViewById(R.id.activitys_region_prizeregion2_tv);
		yqhyTV = (TextView) findViewById(R.id.activitys_region_yqhy_tv);
		qxj5TV = (TextView) findViewById(R.id.activitys_region_qxj5_tv);

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

	private void updateAdapter(List<ActiveInfo> list){
		mActivitysRegionAdapter.setItems(list);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.activitys_region_activity_prizeregion2_layout:
			//会员福利计划2期
			Intent intentPrizeRegion2 = new Intent(ActivitysRegionActivity.this,PrizeRegion2TempActivity.class);
			startActivity(intentPrizeRegion2);
			break;
		case R.id.activitys_region_activity_sign_layout:
			//签到
			Intent intentSign = new Intent(ActivitysRegionActivity.this,SignTopicTempActivity.class);
			startActivity(intentSign);
			break;
		case R.id.activitys_region_activity_ysq_layout:
			//压岁钱
//			Intent intentysq = new Intent(ActivitysRegionActivity.this,XCFLTempActivity.class);
//			startActivity(intentysq);
			break;
		case R.id.activitys_region_activity_mb_layout:
			//秒标
			break;
		case R.id.activitys_region_activity_prizeregion_layout:
			//会员福利计划
			break;
		case R.id.activitys_region_activity_xsfx_layout:
			//限时返现
			break;
		case R.id.activitys_region_activity_yqhy_layout:
			//邀请好友返现
			Intent yqhyIntent = new Intent(ActivitysRegionActivity.this,YQHYTempActivity.class);
			startActivity(yqhyIntent);
//			Intent yqhyIntent = new Intent(ActivitysRegionActivity.this,WebViewCookieTestActivity.class);
//			startActivity(yqhyIntent);
			break;
		case R.id.activitys_region_activity_qxj5_layout:
			Intent intentQXJ5 = new Intent(ActivitysRegionActivity.this,LXJ5TempActivity.class);
			startActivity(intentQXJ5);
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化活动状态
	 */
	private void initActivityStatus(int resultCode,String activeTitle){
		//签到活动是否开始
		if("QD_03".equals(activeTitle)){
			//三月份签到活动
			if(resultCode == 0){
				signLayout.setEnabled(true);
				signTV.setText("立即参加");
				signTV.setBackgroundColor(getResources().getColor(R.color.common_topbar_bg_color));
			}else if(resultCode == -3){
				signLayout.setEnabled(false);
				signTV.setText("活动结束");
				signTV.setBackgroundColor(getResources().getColor(R.color.gray1));
			}else if(resultCode == -2){
				signLayout.setEnabled(true);
				signTV.setText("敬请期待");
				signTV.setBackgroundColor(getResources().getColor(R.color.common_topbar_bg_color));
			}
		}else if("HYFL_02".equals(activeTitle)){
			//会员福利计划2期
			if(resultCode == 0){
				prizeRegion2Layout.setEnabled(true);
				prizeRegion2TV.setText("立即参加");
				prizeRegion2TV.setBackgroundColor(getResources().getColor(R.color.common_topbar_bg_color));
			}else if(resultCode == -3){
				prizeRegion2Layout.setEnabled(false);
				prizeRegion2TV.setText("活动结束");
				prizeRegion2TV.setBackgroundColor(getResources().getColor(R.color.gray1));
			}else if(resultCode == -2){
				prizeRegion2Layout.setEnabled(true);
				prizeRegion2TV.setText("敬请期待");
				prizeRegion2TV.setBackgroundColor(getResources().getColor(R.color.common_topbar_bg_color));
			}
		}else if("TGY_01".equals(activeTitle)){
			//四月份邀请好友返现活动
			if(resultCode == 0){
				yqhyLayout.setEnabled(true);
				yqhyTV.setText("立即参加");
				yqhyTV.setBackgroundColor(getResources().getColor(R.color.common_topbar_bg_color));
			}else if(resultCode == -3){
				yqhyLayout.setEnabled(false);
				yqhyTV.setText("活动结束");
				yqhyTV.setBackgroundColor(getResources().getColor(R.color.gray1));
			}else if(resultCode == -2){
				yqhyLayout.setEnabled(true);
				yqhyTV.setText("敬请期待");
				yqhyTV.setBackgroundColor(getResources().getColor(R.color.common_topbar_bg_color));
			}
		}else if("MONDAY_ROB_CASH".equals(activeTitle)){
			//5月份每周一抢现金活动
			if(resultCode == 0){
				qxj5Layout.setEnabled(true);
				qxj5TV.setText("立即参加");
				qxj5TV.setBackgroundColor(getResources().getColor(R.color.common_topbar_bg_color));
			}else if(resultCode == -3){
				qxj5Layout.setEnabled(false);
				qxj5TV.setText("活动结束");
				qxj5TV.setBackgroundColor(getResources().getColor(R.color.gray1));
			}else if(resultCode == -2){
				qxj5Layout.setEnabled(true);
				qxj5TV.setText("敬请期待");
				qxj5TV.setBackgroundColor(getResources().getColor(R.color.common_topbar_bg_color));
			}
		}
		
		//新春红包 压岁钱
		ysqLayout.setEnabled(false);
		ysqTV.setText("活动结束");
		ysqTV.setBackgroundColor(getResources().getColor(R.color.gray1));
		
		//显示秒标
		mbLayout.setEnabled(false);
		mbTV.setText("活动结束");
		mbTV.setBackgroundColor(getResources().getColor(R.color.gray1));
		
		//会员礼品专区
		prizeRegionLayout.setEnabled(false);
		prizeRegionTV.setText("活动结束");
		prizeRegionTV.setBackgroundColor(getResources().getColor(R.color.gray1));
		
		xsfxLayout.setEnabled(false);
		xsfxTV.setText("活动结束");
		xsfxTV.setBackgroundColor(getResources().getColor(R.color.gray1));
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
		AsyncActiveList task = new AsyncActiveList(ActivitysRegionActivity.this,String.valueOf(page),String.valueOf(pageSize),status,fromWhere,picShowStatus,
			new OnCommonInter(){
				@Override
				public void back(BaseInfo baseInfo) {
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

	/**
	 * 判断签到活动是否开始
	 * @param activeTitle
	 */
	private void requestActiveTime(final String activeTitle){ 
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		AsyncXCFLActiveTime task = new AsyncXCFLActiveTime(ActivitysRegionActivity.this, activeTitle, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							initActivityStatus(resultCode,activeTitle);
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
