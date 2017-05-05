package com.ylfcf.ppp.fragment;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.YXBRedeemRecordAdapter;
import com.ylfcf.ppp.async.AsyncYXBRedeemRecord;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.YXBRedeemRecordInfo;
import com.ylfcf.ppp.entity.YXBRedeemRecordPageInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.ui.UserInvestRecordActivity;
import com.ylfcf.ppp.ui.YXBTransRecordActivity;
import com.ylfcf.ppp.util.SettingsManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 元信宝赎回记录
 * @author Administrator
 * 
 */
public class YXBRedeemRecordFragment extends BaseFragment {
	private UserInvestRecordActivity mUserInvestRecordActivity;
	private View rootView;

	private static final int REQUEST_YXB_INVEST_RECORD_WHAT = 3301;
	private static final int REQUEST_YXB_INVEST_RECORD_SUCCESS = 3302;
	private static final int REQUEST_YXB_INVEST_RECORD_NODATA = 3303;

	private PullToRefreshListView yxbInvestRecordListView;
	private YXBRedeemRecordAdapter yxbRecordAdapter;
	private TextView promptTV;

	private int page = 0;
	private int pageSize = 10;
	private List<YXBRedeemRecordInfo> yxbRecordList = new ArrayList<YXBRedeemRecordInfo>();

	private boolean isRefresh = true;
	private boolean isLoadMore = false;// 上拉加载更多

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_YXB_INVEST_RECORD_WHAT:
				requestYXBRedeemRecordList("",
						SettingsManager.getUserId(mUserInvestRecordActivity
								.getApplicationContext()), page, pageSize);
				break;
			case REQUEST_YXB_INVEST_RECORD_SUCCESS:
				promptTV.setVisibility(View.GONE);
				yxbInvestRecordListView.setVisibility(View.VISIBLE);
				YXBRedeemRecordPageInfo pageInfo = (YXBRedeemRecordPageInfo) msg.obj;
				if (!isLoadMore) {
					yxbRecordList.clear();
				}
				yxbRecordList.addAll(pageInfo.getYxbRecordList());
				updateAdapter(yxbRecordList);
				yxbInvestRecordListView.onRefreshComplete();
				break;
			case REQUEST_YXB_INVEST_RECORD_NODATA:
				if (isRefresh) {
					promptTV.setVisibility(View.VISIBLE);
					yxbInvestRecordListView.setVisibility(View.GONE);
					promptTV.setText("暂无投资记录");
				} else if (isLoadMore) {
					yxbInvestRecordListView.onRefreshComplete();
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mUserInvestRecordActivity = (UserInvestRecordActivity) getActivity();
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.yxb_redeem_record_fragment,
					null);
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

	private void findViews(View rootView) {
		promptTV = (TextView) rootView
				.findViewById(R.id.yxb_redeem_record_fragment_prompt);
		yxbInvestRecordListView = (PullToRefreshListView) rootView
				.findViewById(R.id.yxb_redeem_record_fragment_pull_refresh_list);
		yxbInvestRecordListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 下拉刷新
						isRefresh = true;
						isLoadMore = false;
						page = 0;
						handler.sendEmptyMessage(REQUEST_YXB_INVEST_RECORD_WHAT);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 上拉加载更多
						page++;
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								isLoadMore = true;
								isRefresh = false;
								handler.sendEmptyMessage(REQUEST_YXB_INVEST_RECORD_WHAT);
							}
						}, 1000L);

					}

				});
		initAdapter();
		handler.sendEmptyMessage(REQUEST_YXB_INVEST_RECORD_WHAT);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	private void initAdapter() {
		yxbRecordAdapter = new YXBRedeemRecordAdapter(mUserInvestRecordActivity);
		yxbInvestRecordListView.setAdapter(yxbRecordAdapter);
	}

	private void updateAdapter(List<YXBRedeemRecordInfo> recordList) {
		yxbRecordAdapter.setItems(recordList);
	}

	/**
	 * 元信宝投资记录
	 * 
	 * @param id
	 * @param userId
	 * @param page
	 * @param pageSize
	 */
	private void requestYXBRedeemRecordList(String id, String userId, int page,
			int pageSize) {
		AsyncYXBRedeemRecord yxbRecordTask = new AsyncYXBRedeemRecord(
				mUserInvestRecordActivity, id, userId, String.valueOf(page),
				String.valueOf(pageSize), new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler
										.obtainMessage(REQUEST_YXB_INVEST_RECORD_SUCCESS);
								msg.obj = baseInfo.getYxbRedeemRecordPageInfo();
								handler.sendMessage(msg);
							} else {
								Message msg = handler
										.obtainMessage(REQUEST_YXB_INVEST_RECORD_NODATA);
								msg.obj = baseInfo.getMsg();
								handler.sendMessage(msg);
							}
						} else {
							Message msg = handler
									.obtainMessage(REQUEST_YXB_INVEST_RECORD_NODATA);
							handler.sendMessage(msg);
						}
					}
				});
		yxbRecordTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
