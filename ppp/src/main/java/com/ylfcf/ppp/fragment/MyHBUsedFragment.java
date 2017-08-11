package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.RedBagAdapter;
import com.ylfcf.ppp.async.AsyncRedbgList;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.RedBagInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.ui.BorrowDetailSRZXActivity;
import com.ylfcf.ppp.ui.BorrowDetailYYYActivity;
import com.ylfcf.ppp.ui.BorrowDetailZXDActivity;
import com.ylfcf.ppp.ui.MyHongbaoActivity;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的红包 ---- 已使用
 * 
 * @author Administrator
 * 
 */
public class MyHBUsedFragment extends BaseFragment {
	private static final String className = "MyHBUsedFragment";
	public final int REQUEST_HB_LIST_WHAT = 1800;
	private final int REQUEST_HB_LIST_SUCCESS = 1801;
	private final int REQUEST_HB_LIST_FAILE = 1802;

	private MyHongbaoActivity mainActivity;
	private View rootView;

	private RedBagAdapter redBagAdapter;
	private PullToRefreshListView pullToRefreshListView;
	private TextView nodataText;
	private List<RedBagInfo> redbagList = new ArrayList<RedBagInfo>();

	private int pageNo = 0;
	private int pageSize = 10;
	private boolean isFirst = true;
	private boolean isLoadMore = false;// 加载更多

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_HB_LIST_WHAT:
				requestHBList(SettingsManager.getUserId(mainActivity
						.getApplicationContext()), "2");
				break;
			case REQUEST_HB_LIST_SUCCESS:
				nodataText.setVisibility(View.GONE);
				pullToRefreshListView.setVisibility(View.VISIBLE);
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				if (baseInfo != null) {
					if (!isLoadMore) {
						redbagList.clear();
					}
					redbagList.addAll(baseInfo.getmRedBagPageInfo()
							.getRedbagList());
					redBagAdapter.setItems(redbagList,baseInfo.getTime());
				}
				isLoadMore = false;
				pullToRefreshListView.onRefreshComplete();
				break;
			case REQUEST_HB_LIST_FAILE:
				if(!isLoadMore){
					nodataText.setVisibility(View.VISIBLE);
					pullToRefreshListView.setVisibility(View.GONE);
				}
				pullToRefreshListView.onRefreshComplete();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainActivity = (MyHongbaoActivity) getActivity();
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.myhb_used_fragment, null);
			findViews(rootView);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		handler.sendEmptyMessage(REQUEST_HB_LIST_WHAT);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//友盟统计页面跳转
	}

	@Override
	public void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);//友盟统计页面跳转
	}

	private void initListeners() {
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 下拉刷新
						pageNo = 0;
						isLoadMore = false;
						handler.sendEmptyMessage(REQUEST_HB_LIST_WHAT);
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
								handler.sendEmptyMessage(REQUEST_HB_LIST_WHAT);
							}
						}, 1000L);

					}
				});

		pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				RedBagInfo info = (RedBagInfo)parent.getAdapter().getItem(position);
				InvestRecordInfo recordInfo = new InvestRecordInfo();
				recordInfo.setBorrow_type(info.getBorrow_type());
				recordInfo.setBorrow_id(info.getBorrow_id());
				recordInfo.setBorrow_name(info.getBorrow_name());
				Intent intent = new Intent();
				if("元月通".equals(info.getBorrow_type()) || "元季融".equals(info.getBorrow_type()) || "元定和".equals(info.getBorrow_type())
						|| "元年鑫".equals(info.getBorrow_type()) || "元政盈".equals(info.getBorrow_type())){
					intent.putExtra("InvestRecordInfo",recordInfo);
					intent.setClass(mainActivity,BorrowDetailZXDActivity.class);
					startActivity(intent);
				}else if("私人尊享".equals(info.getBorrow_type())){
					intent.putExtra("InvestRecordInfo",recordInfo);
					intent.setClass(mainActivity, BorrowDetailSRZXActivity.class);
					startActivity(intent);
				}else if("元月盈".equals(info.getBorrow_type())){
					intent.putExtra("InvestRecordInfo",recordInfo);
					intent.setClass(mainActivity, BorrowDetailYYYActivity.class);
					startActivity(intent);
				}
			}
		});
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

	private void findViews(View view) {
		pullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.myhb_used_fragment_pull_refresh_list);
		nodataText = (TextView) view
				.findViewById(R.id.myhb_used_fragment_nodata);
		redBagAdapter = new RedBagAdapter(mainActivity, null);
		pullToRefreshListView.setAdapter(redBagAdapter);
		initListeners();
	}

	private void requestHBList(String userId, String flag) {
		AsyncRedbgList redbagTask = new AsyncRedbgList(mainActivity, userId,
				flag, String.valueOf(pageNo),String.valueOf(pageSize),new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler
										.obtainMessage(REQUEST_HB_LIST_SUCCESS);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							} else {
								Message msg = handler
										.obtainMessage(REQUEST_HB_LIST_FAILE);
								msg.obj = baseInfo.getError();
								handler.sendMessage(msg);
							}
						} else {
							Message msg = handler
									.obtainMessage(REQUEST_HB_LIST_FAILE);
							handler.sendMessage(msg);
						}
					}
				});
		redbagTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
