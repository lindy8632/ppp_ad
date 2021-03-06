package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.MyJXQListAdapter;
import com.ylfcf.ppp.adapter.MyJXQListAdapter.OnJXQItemClickListener;
import com.ylfcf.ppp.async.AsyncJXQPageInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.JiaxiquanInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.ui.BorrowListZXDActivity;
import com.ylfcf.ppp.ui.MyJXQActivity;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 加息券 已过期
 * @author Mr.liu
 *
 */
public class MyJXQOvertimeFragment extends BaseFragment{
	private static final String className = "MyJXQOvertimeFragment";
	public final int REQUEST_JXQ_LIST_WHAT = 2000;
	private final int REQUEST_JXQ_LIST_SUCCESS = 2001;
	private final int REQUEST_JXQ_LIST_FAILE = 2002;

	private MyJXQActivity mainActivity;
	private View rootView;

	private MyJXQListAdapter mMyJXQListAdapter;
	private PullToRefreshListView pullToRefreshListView;
	private TextView nodataText;
	private List<JiaxiquanInfo> jxqList = new ArrayList<JiaxiquanInfo>();
	
	private int pageNo = 1;
	private int pageSize = Integer.MAX_VALUE;
	private boolean isFirst = true;
	private boolean isLoadMore = false;// 加载更多
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_JXQ_LIST_WHAT:
				requestJXQList(SettingsManager.getUserId(mainActivity
						.getApplicationContext()), "未使用");
				break;
			case REQUEST_JXQ_LIST_SUCCESS:
				Date endDate = null;
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				pullToRefreshListView.setVisibility(View.VISIBLE);
				nodataText.setVisibility(View.GONE);
				if (baseInfo != null) {
					if (!isLoadMore) {
						jxqList.clear();
					}
					for(int i=0;i<baseInfo.getmJiaxiquanPageInfo().getInfoList().size();i++){
						JiaxiquanInfo info = baseInfo.getmJiaxiquanPageInfo().getInfoList().get(i);
						try {
							endDate = sdf.parse(info.getEffective_end_time());
							if(endDate.compareTo(sdf.parse(baseInfo.getTime())) == -1){
								//表示加息券已过期
								info.setUse_status("已过期");
								jxqList.add(info);
							}
						} catch (Exception e) {
						}
					}
					mMyJXQListAdapter.setItems(jxqList,baseInfo.getTime());
				}
				isLoadMore = false;
				pullToRefreshListView.onRefreshComplete();
				break;
			case REQUEST_JXQ_LIST_FAILE:
				if(!isLoadMore){
					pullToRefreshListView.setVisibility(View.GONE);
					nodataText.setVisibility(View.VISIBLE);
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
		mainActivity = (MyJXQActivity) getActivity();
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.myhb_noused_fragment, null);
			findViews(rootView);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
			handler.sendEmptyMessage(REQUEST_JXQ_LIST_WHAT);
		}
	}

	private void findViews(View view) {
		pullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.myhb_noused_fragment_pull_refresh_list);
		nodataText = (TextView) view
				.findViewById(R.id.myhb_noused_fragment_nodata);
		mMyJXQListAdapter = new MyJXQListAdapter(mainActivity,
				new OnJXQItemClickListener() {
					@Override
					public void onClick(JiaxiquanInfo jxqInfo,int position) {
						Intent intent = new Intent(mainActivity,BorrowListZXDActivity.class);
						startActivity(intent);
					}
				});
		pullToRefreshListView.setAdapter(mMyJXQListAdapter);
		initListeners();
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	private void initListeners() {
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 下拉刷新
						isLoadMore = false;
						pageNo = 1;
						handler.sendEmptyMessage(REQUEST_JXQ_LIST_WHAT);
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
								handler.sendEmptyMessage(REQUEST_JXQ_LIST_WHAT);
							}
						}, 1000L);

					}

				});
	}

	private void requestJXQList(String userId, String useStatus) {
		if (isFirst) {
			mainActivity.loadingDialog.show();
		}
		isFirst = false;
		AsyncJXQPageInfo redbagTask = new AsyncJXQPageInfo(mainActivity, userId,useStatus,
				String.valueOf(pageNo),String.valueOf(pageSize), new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mainActivity.loadingDialog.isShowing()) {
							mainActivity.loadingDialog.dismiss();
						}

						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler
										.obtainMessage(REQUEST_JXQ_LIST_SUCCESS);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							} else {
								Message msg = handler.obtainMessage(REQUEST_JXQ_LIST_FAILE);
								msg.obj = baseInfo.getError();
								handler.sendMessage(msg);
							}
						} else {
							Message msg = handler
									.obtainMessage(REQUEST_JXQ_LIST_FAILE);
							handler.sendMessage(msg);
						}
					}
				});
		redbagTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
