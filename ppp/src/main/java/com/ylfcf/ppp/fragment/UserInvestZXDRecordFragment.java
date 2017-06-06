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
import com.ylfcf.ppp.adapter.UserInvestRecordAdapter;
import com.ylfcf.ppp.async.AsyncInvestTotalRecord;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.InvestRecordPageInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.ui.BorrowDetailXSBActivity;
import com.ylfcf.ppp.ui.BorrowDetailZXDActivity;
import com.ylfcf.ppp.ui.UserInvestRecordActivity;
import com.ylfcf.ppp.util.SettingsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户投资记录 --- 政信贷（元政盈）
 * @author Mr.liu
 *
 */
public class UserInvestZXDRecordFragment extends BaseFragment {
	private static final int REQUEST_INVEST_RECORD_WHAT = 1021;
	private static final int REQUEST_INVEST_RECORD_SUCCESS = 1022;
	private static final int REQUEST_INVEST_RECORD_NODATA = 1023;	//无数据

	private UserInvestRecordActivity mainActivity;
	private View rootView;

	private UserInvestRecordAdapter investRecordsAdapter;
	private List<InvestRecordInfo> investRecordList = new ArrayList<InvestRecordInfo>();
	private PullToRefreshListView pullToRefreshListView;
	private TextView nodataText;

	//请求参数
	private String status = "";//投资中
	private int pageNo = 0;
	private int pageSize = 20;
	private boolean isFirst = true;
	private boolean isLoadMore = false;// 加载更多
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_INVEST_RECORD_WHAT:
				getInvestRecordList(SettingsManager.getUserId(mainActivity.getApplicationContext()), "", status);
				break;
			case REQUEST_INVEST_RECORD_SUCCESS:
				InvestRecordPageInfo pageInfo = (InvestRecordPageInfo) msg.obj;
				pullToRefreshListView.setVisibility(View.VISIBLE);
				nodataText.setVisibility(View.GONE);
				if (pageInfo != null) {
					if (!isLoadMore) {
						investRecordList.clear();
					}
					investRecordList.addAll(pageInfo.getInvestRecordList());
					investRecordsAdapter.setItems(investRecordList);
				}
				isLoadMore = false;
				pullToRefreshListView.onRefreshComplete();
				break;
			case REQUEST_INVEST_RECORD_NODATA:
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
		mainActivity = (UserInvestRecordActivity) getActivity();
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.userinvest_zxd_record_fragment, null);
			findViews(rootView);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		handler.sendEmptyMessage(REQUEST_INVEST_RECORD_WHAT);
		return rootView;
	}

	private void findViews(View view) {
		pullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.userinvest_zxd_record_fragment_pull_refresh_list);
		nodataText = (TextView) view
				.findViewById(R.id.userinvest_zxd_record_fragment_nodata);
		investRecordsAdapter = new UserInvestRecordAdapter(mainActivity,"yzy");
		pullToRefreshListView.setAdapter(investRecordsAdapter);
		initListeners();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	private void initListeners() {
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 下拉刷新
						pageNo = 0;
						handler.sendEmptyMessage(REQUEST_INVEST_RECORD_WHAT);
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
								handler.sendEmptyMessage(REQUEST_INVEST_RECORD_WHAT);
							}
						}, 1000L);

					}

				});
		pullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				InvestRecordInfo info = investRecordList.get(position - 1);
				Intent intent = null;
				if("新手标".equals(info.getBorrow_type())){
					//新手标
					intent = new Intent(mainActivity,BorrowDetailXSBActivity.class);
				}else{
					intent = new Intent(mainActivity,BorrowDetailZXDActivity.class);
				}
				intent.putExtra("InvestRecordInfo", info);
				startActivity(intent);
			}
		});
	}

	/**
	 * 获取投资记录列表
	 * @param investUserId
	 * @param borrowId
	 * @param status
	 */
	private void getInvestRecordList(String investUserId,String borrowId,
			String status){
		if(mainActivity.loadingDialog != null && isFirst){
			mainActivity.loadingDialog.show();
		}
		isFirst = false;
		AsyncInvestTotalRecord asyncInvestRecord = new AsyncInvestTotalRecord(mainActivity, investUserId, borrowId, 
				status, pageNo, pageSize, new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						if(mainActivity.loadingDialog.isShowing()){
							mainActivity.loadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								Message msg = handler.obtainMessage(REQUEST_INVEST_RECORD_SUCCESS);
								msg.obj = baseInfo.getmInvestRecordPageInfo();
								handler.sendMessage(msg);
							}else{
								Message msg = handler.obtainMessage(REQUEST_INVEST_RECORD_NODATA);
								msg.obj = baseInfo.getMsg();
								handler.sendMessage(msg);
							}
						}
					}
		});
		asyncInvestRecord.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
