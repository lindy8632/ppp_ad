package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ylfcf.view.CustomerFooter;
import com.example.ylfcf.widget.XRefreshView;
import com.example.ylfcf.widget.XRefreshView.SimpleXRefreshListener;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.RechargeRecordAdapter;
import com.ylfcf.ppp.adapter.RechargeRecordAdapter.OnRechargeRecordItemClickListener;
import com.ylfcf.ppp.async.AsyncRechargeRecord;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.RechargeRecordInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 充值记录
 * @author Mr.liu
 *
 */
public class RechargeRecordActivity extends BaseActivity implements OnClickListener{
	private static final int REQUEST_RECHARGE_RECORD_WHAT = 2311;
	private static final int REQUEST_RECHARGE_RECORD_SUCCESS = 2312;
	private static final int REQUEST_RECHARGE_RECORD_NODATA = 2313;//暂无数据
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private int page = 0;
	private int pageSize = 20;
	private LayoutInflater mLayoutInflater;
	private XRefreshView mXRefreshView;
	private ListView mListView;
	private TextView nodataTV;
	private RechargeRecordAdapter rechargeAdapter;
	private View topLayout;
	private List<RechargeRecordInfo> rechargeRecordList = new ArrayList<RechargeRecordInfo>();
	private boolean isLoadMore = false;// 加载更多
	private boolean isRefresh = false;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_RECHARGE_RECORD_WHAT:
				getRechargeRecordList(SettingsManager.getUserId(getApplicationContext()));
				break;
			case REQUEST_RECHARGE_RECORD_SUCCESS:
				List<RechargeRecordInfo> list = (List<RechargeRecordInfo>) msg.obj;
				if (!isLoadMore) {
					rechargeRecordList.clear();
				}
				rechargeRecordList.addAll(list);
				updateAdapter(rechargeRecordList);
				isRefresh = false;
				isLoadMore = false;
				break;
			case REQUEST_RECHARGE_RECORD_NODATA:
				if(isLoadMore){
					mXRefreshView.setLoadComplete(true);
				}else{
					nodataTV.setVisibility(View.VISIBLE);
					mListView.setVisibility(View.GONE);
				}
				isRefresh = false;
				isLoadMore = false;
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
		setContentView(R.layout.recharge_record_activity);
		mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		findViews();
		handler.sendEmptyMessage(REQUEST_RECHARGE_RECORD_WHAT);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

	private void findViews(){
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("充值记录");
		topLayout = mLayoutInflater.inflate(R.layout.recharge_record_activity_listview_top, null);
		mListView = (ListView)findViewById(R.id.recharge_record_activity_listview);
		mListView.addHeaderView(topLayout);
		mXRefreshView = (XRefreshView)findViewById(R.id.recharge_record_activity_xrefreshview);
		nodataTV = (TextView) findViewById(R.id.recharge_record_activity_nodata);
		
		rechargeAdapter = new RechargeRecordAdapter(RechargeRecordActivity.this, new OnRechargeRecordItemClickListener() {
			@Override
			public void onClick(View v, RechargeRecordInfo info) {
				Intent intent = new Intent(RechargeRecordActivity.this,RechargeProofActivity.class);
				intent.putExtra("recharge_id", info.getOrder());
				startActivity(intent);
			}
		});
		mListView.setAdapter(rechargeAdapter);
		initXRefrshView();
		initListeners();
	}

	private void initXRefrshView(){
		mXRefreshView.setPullLoadEnable(true);
		mXRefreshView.setPinnedTime(1000);
		mXRefreshView.setAutoLoadMore(false);
//		xRefreshView.setCustomHeaderView(new CustomHeader(this));
//		xRefreshView.setCustomHeaderView(new XRefreshViewHeader(this));
		mXRefreshView.setMoveForHorizontal(true);
		mXRefreshView.setCustomFooterView(new CustomerFooter(this));
//		xRefreshView.setPinnedContent(true);
		//设置当非RecyclerView上拉加载完成以后的回弹时间
		mXRefreshView.setScrollBackDuration(100);
	}

	private void initListeners(){
		mXRefreshView.setXRefreshViewListener(new SimpleXRefreshListener() {
			@Override
			public void onRefresh(boolean isPullDown) {
				isRefresh = true;
				isLoadMore = false;
				mXRefreshView.setLoadComplete(false);
				page = 0;
				handler.sendEmptyMessage(REQUEST_RECHARGE_RECORD_WHAT);
			}

			@Override
			public void onLoadMore(boolean isSilence) {
				isRefresh = false;
				isLoadMore = true;
				page ++;
				handler.sendEmptyMessage(REQUEST_RECHARGE_RECORD_WHAT);
			}
		});
	}

	private void updateRefreshListViewStatus(boolean isRefresh,boolean isLoadMore){
		if(isRefresh){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					mXRefreshView.stopRefresh();
				}
			},1000L);
		}else if(isLoadMore){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					mXRefreshView.stopLoadMore();
				}
			},1000L);
		}
	}

	private void updateAdapter(List<RechargeRecordInfo> list){
		rechargeAdapter.setItems(list);
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
	 * 充值记录列表
	 * @param userId
	 */
	private void getRechargeRecordList(String userId){
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		AsyncRechargeRecord rechargeRecordTask = new AsyncRechargeRecord(RechargeRecordActivity.this, String.valueOf(page), 
				String.valueOf(pageSize), userId, new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null){
							mLoadingDialog.dismiss();
						}
						updateRefreshListViewStatus(isRefresh,isLoadMore);
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								List<RechargeRecordInfo> list = baseInfo.getmRechargeRecordPageInfo().getRechargeRecordList();
								Message msg = handler.obtainMessage(REQUEST_RECHARGE_RECORD_SUCCESS);
								msg.obj = list;
								handler.sendMessage(msg);
							}else{
								Message msg = handler.obtainMessage(REQUEST_RECHARGE_RECORD_NODATA);
								msg.obj = baseInfo.getMsg();
								handler.sendMessage(msg);
							}
						}
					}
		});
		rechargeRecordTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
