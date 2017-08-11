package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.ArticleAdapter;
import com.ylfcf.ppp.async.AsyncArticleList;
import com.ylfcf.ppp.entity.ArticleInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.ui.ArticleDetailsActivity;
import com.ylfcf.ppp.ui.ArticleListActivity;
import com.ylfcf.ppp.util.Constants.ArticleType;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;

import java.util.ArrayList;
import java.util.List;

/**
 * 平台公告
 * 
 * @author Administrator
 * 
 */
public class NoticeFragment extends BaseFragment {
	private static final String className = "NoticeFragment";
	private static final int REQUEST_ARTICLELIST_WHAT = 5601;
	private static final int REQUEST_ARTICLELIST_SUCCESS = 5602;
	private static final int REQUEST_ARTICLELIST_NODATA = 5603;

	private ArticleListActivity articleListActivity;
	private View rootView;

	private PullToRefreshListView pullListView;
	private ArticleAdapter articleAdapter;
	private TextView nodataText;

	private List<ArticleInfo> articleInfoList = new ArrayList<ArticleInfo>();
	private int pageNo = 0;
	private int pageSize = 20;
	private boolean isLoadMore = false;//加载更多
	private boolean isFirst = true;//是否是第一次请求

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_ARTICLELIST_WHAT:
				requestNoticeList("正常", ArticleType.NOTICE);
				break;
			case REQUEST_ARTICLELIST_SUCCESS:
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				if(baseInfo != null){
					if(!isLoadMore){
						articleInfoList.clear();
					}
					articleInfoList.addAll(baseInfo.getmArticlePageInfo().getArticleList());
					updateAdapter(articleInfoList);
				}
				isLoadMore = false;
				pullListView.onRefreshComplete();
				break;
			case REQUEST_ARTICLELIST_NODATA:
				if(isLoadMore){
					pullListView.setVisibility(View.VISIBLE);
					nodataText.setVisibility(View.GONE);
				}else{
					pullListView.setVisibility(View.GONE);
					nodataText.setVisibility(View.VISIBLE);
				}
				pullListView.onRefreshComplete();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		articleListActivity = (ArticleListActivity) getActivity();
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.notice_fragment, null);
			findViews(rootView);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		handler.sendEmptyMessage(REQUEST_ARTICLELIST_WHAT);
		return rootView;
	}

	private void findViews(View view) {
		pullListView = (PullToRefreshListView) view
				.findViewById(R.id.notice_fragment_pull_refresh_list);
		nodataText = (TextView) view.findViewById(R.id.notice_fragment_nodata);

		articleAdapter = new ArticleAdapter(articleListActivity);
		pullListView.setAdapter(articleAdapter);
		initListeners();
	}

	private void initListeners() {
		pullListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// index从1开始。。。
				ArticleInfo item = (ArticleInfo) parent.getAdapter().getItem(position);
				Intent intent = new Intent(articleListActivity,ArticleDetailsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("title", "平台公告");
				bundle.putSerializable("ARTICLE_INFO", item);
				intent.putExtra("bundle", bundle);
				articleListActivity.startActivity(intent);
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
						handler.sendEmptyMessage(REQUEST_ARTICLELIST_WHAT);
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
						handler.sendEmptyMessage(REQUEST_ARTICLELIST_WHAT);
					}
				}, 1000L);

			}

		});
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
	
	private void updateAdapter(List<ArticleInfo> list) {
		articleAdapter.setItems(list);
	}

	private void requestNoticeList(String status, String type) {
		if(isFirst && articleListActivity.loadingDialog != null){
			articleListActivity.loadingDialog.show();
		}
		isFirst = false;
		AsyncArticleList articleTask = new AsyncArticleList(
				articleListActivity, String.valueOf(pageNo),
				String.valueOf(pageSize), status, type, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						articleListActivity.loadingDialog.dismiss();
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler
										.obtainMessage(REQUEST_ARTICLELIST_SUCCESS);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							} else {
								Message msg = handler
										.obtainMessage(REQUEST_ARTICLELIST_NODATA);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							}
						}else{
							Message msg = handler.obtainMessage(REQUEST_ARTICLELIST_NODATA);
							msg.obj = baseInfo;
							handler.sendMessage(msg);
						}

					}
				});
		articleTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
