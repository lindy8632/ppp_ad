package com.ylfcf.ppp.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.FundsDetailsAdapter;
import com.ylfcf.ppp.async.AsyncAccountTotalInfo;
import com.ylfcf.ppp.async.AsyncFundsDetailsList;
import com.ylfcf.ppp.entity.AccountTotalInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.FundsDetailsInfo;
import com.ylfcf.ppp.entity.FundsDetailsPageInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.ui.FundsDetailsActivity;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.widget.LoadingDialog;

/**
 * 资金明细 --- 政信贷
 * 
 * @author Administrator
 * 
 */
public class FundDetailsZXDFragment extends BaseFragment implements
		OnClickListener {
	private static final int REQUEST_FUNDS_DETAILS_WHAT = 4191;
	private static final int REQUEST_FUNDS_DETAILS_SUCCESS = 4192;
	private static final int REQUEST_FUNDS_DETAILS_FAILE = 4193;
	
	private FundsDetailsActivity fundsDetailsActivity;
	private View rootView;

	private final int REQUEST_ACCOUNT_WHAT = 2501;
	private final int REQUEST_ACCOUNT_SUCCESS = 2502;

	private AccountTotalInfo accountInfo;
//	private TextView investTotalMoneyTV;// 累计投资
//	private TextView interestTotalMoneyTV;// 累计收益
	private LoadingDialog loadingDialog;
	private UserInfo mUserInfo;
	
	private TextView nodataText;
	private PullToRefreshListView listview;
	private FundsDetailsAdapter adapter;
	static FundDetailsZXDFragment fragment = null;
	private List<FundsDetailsInfo> fundsList = new ArrayList<FundsDetailsInfo>();
	
	private int pageNo = 0;
	private int pageSize = 20;
	private boolean isLoadMore = false;// 加载更多

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_ACCOUNT_WHAT:
//				requestAccountInfo(SettingsManager
//						.getUserId(fundsDetailsActivity.getApplicationContext()));
				break;
			case REQUEST_ACCOUNT_SUCCESS:
				break;
			case REQUEST_FUNDS_DETAILS_WHAT:
				requestFundsDetailsList(SettingsManager.getUserId(fundsDetailsActivity));
				break;
			case REQUEST_FUNDS_DETAILS_SUCCESS:
				FundsDetailsPageInfo huifuPageInfo = (FundsDetailsPageInfo) msg.obj;
				nodataText.setVisibility(View.GONE);
				listview.setVisibility(View.VISIBLE);
				if (!isLoadMore) {
					fundsList.clear();
				}
				fundsList.addAll(huifuPageInfo.getFundsDetailsList());
				updateAdapter(fundsList);
				isLoadMore = false;
				listview.onRefreshComplete();
				break;
			case REQUEST_FUNDS_DETAILS_FAILE:
				if(isLoadMore){
					listview.setVisibility(View.VISIBLE);
					nodataText.setVisibility(View.GONE);
					isLoadMore = false;
					listview.onRefreshComplete();
				}else{
					listview.setVisibility(View.GONE);
					nodataText.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}
		}
	};

	public static Fragment newInstance() {
		if (fragment == null) {
			fragment = new FundDetailsZXDFragment();
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fundsDetailsActivity = (FundsDetailsActivity) getActivity();
		mUserInfo = (UserInfo) fundsDetailsActivity.getIntent().getSerializableExtra("userinfo");
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.funds_details_zxd_fragment,
					null);
			findViews(rootView, inflater);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
//		handler.sendEmptyMessage(REQUEST_ACCOUNT_WHAT);
		handler.sendEmptyMessage(REQUEST_FUNDS_DETAILS_WHAT);
		return rootView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	private void updateAdapter(List<FundsDetailsInfo> huifuFundsList) {
		adapter.setItems(huifuFundsList);
	}

	private void findViews(View rootView, LayoutInflater inflater) {
//		investTotalMoneyTV = (TextView) rootView
//				.findViewById(R.id.funds_details_zxd_top_invest_total_money);
//		interestTotalMoneyTV = (TextView) rootView
//				.findViewById(R.id.funds_details_zxd_top_interest_total_money);
		nodataText = (TextView) rootView
				.findViewById(R.id.funds_details_zxd_nodata_text);
		listview = (PullToRefreshListView) rootView
				.findViewById(R.id.funds_details_zxd_listview);
		listview.setMode(Mode.BOTH);
		adapter = new FundsDetailsAdapter(fundsDetailsActivity, "", "zxd");
		listview.setAdapter(adapter);
		initListeners();
	}
	
	private void initListeners() {
		listview.setOnRefreshListener(new OnRefreshListener2<ListView>(){
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 下拉刷新
				pageNo = 0;
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						handler.sendEmptyMessage(REQUEST_FUNDS_DETAILS_WHAT);
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
						handler.sendEmptyMessage(REQUEST_FUNDS_DETAILS_WHAT);
					}
				}, 1000L);
			}
		});
	}

//	private void initTopDatas() {
//		investTotalMoneyTV.setText(accountInfo.getSum_invest_money());
//		interestTotalMoneyTV.setText(accountInfo.getHas_interest());
//
//	}

//	private void requestAccountInfo(String userId) {
//		if (loadingDialog != null) {
//			loadingDialog.show();
//		}
//		AsyncAccountTotalInfo accountTask = new AsyncAccountTotalInfo(
//				fundsDetailsActivity, userId, new OnCommonInter() {
//					@Override
//					public void back(BaseInfo baseInfo) {
//						if (loadingDialog != null && loadingDialog.isShowing()) {
//							loadingDialog.dismiss();
//						}
//						if (baseInfo != null) {
//							int resultCode = SettingsManager
//									.getResultCode(baseInfo);
//							if (resultCode == 0) {
//								accountInfo = baseInfo.getAccountTotalInfo();
//								initTopDatas();
//							} else {
//								if (loadingDialog != null
//										&& loadingDialog.isShowing()) {
//									loadingDialog.dismiss();
//								}
//							}
//						} else {
//							if (loadingDialog != null
//									&& loadingDialog.isShowing()) {
//								loadingDialog.dismiss();
//							}
//						}
//					}
//				});
//		accountTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
//	}

	/*
	 * 资金明细列表(汇付、易联、宝付)
	 */
	private void requestFundsDetailsList(String userId) {
		if(fundsDetailsActivity != null && fundsDetailsActivity.loadingDialog != null){
			fundsDetailsActivity.loadingDialog.show();
		}
		AsyncFundsDetailsList task = new AsyncFundsDetailsList(
				fundsDetailsActivity, userId, "", String.valueOf(pageNo),
				String.valueOf(pageSize), "", "", new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(fundsDetailsActivity != null && fundsDetailsActivity.loadingDialog != null){
							fundsDetailsActivity.loadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler
										.obtainMessage(REQUEST_FUNDS_DETAILS_SUCCESS);
								msg.obj = baseInfo.getFundsDetailsPageInfo();
								handler.sendMessage(msg);
							} else {
								Message msg = handler.obtainMessage(REQUEST_FUNDS_DETAILS_FAILE);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							}
						} else {
							Message msg = handler.obtainMessage(REQUEST_FUNDS_DETAILS_FAILE);
							msg.obj = baseInfo;
							handler.sendMessage(msg);
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	@Override
	public void onClick(View v) {
	}
}
