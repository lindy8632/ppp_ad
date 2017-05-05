package com.ylfcf.ppp.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.FundsDetailsAdapter;
import com.ylfcf.ppp.async.AsyncYXBNewAccount;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.FundsDetailsInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.ui.FundsDetailsActivity;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 资金明细  ----  元信宝
 * @author Administrator
 */
public class FundDetailsYXBFragment extends BaseFragment{
	
	private static final int REQUEST_YXBFUNDSDETAILS_WHAT = 3501;
	private static final int REQUEST_YXBFUNDSDETAILS_SUCCESS = 3502;
	private static final int REQUEST_YXBFUNDSDETAILS_NODATA = 3503;
	
	private View rootView;
	static FundDetailsYXBFragment fragment = null;
	private PullToRefreshListView pullListView;
	private TextView nodataText;
	private FundsDetailsActivity fundsDetailsActivity;
	private FundsDetailsAdapter fundsDetailsAdapter;
	private List<FundsDetailsInfo> fundsDetailsList = new ArrayList<FundsDetailsInfo>();
	
	private int page = 0;
	private int pageSize = 20;
	private boolean isFirst = true;
	private boolean isLoadMore = false;//加载更多
	
	public static Fragment newInstance(){
		if(fragment == null){
			fragment = new FundDetailsYXBFragment();
		}
		return fragment;
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_YXBFUNDSDETAILS_WHAT:
				requestYXBFundsDetails(SettingsManager.getUserId(fundsDetailsActivity.getApplicationContext()), page, pageSize);
				break;
			case REQUEST_YXBFUNDSDETAILS_SUCCESS:
				pullListView.setVisibility(View.VISIBLE);
				nodataText.setVisibility(View.GONE);
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				if(baseInfo != null){
					if(!isLoadMore){
						fundsDetailsList.clear();
					}
					fundsDetailsList.addAll(baseInfo.getFundsDetailsPageInfo().getFundsDetailsList());
					fundsDetailsAdapter.setItems(fundsDetailsList);
				}
				isLoadMore = false;
				pullListView.onRefreshComplete();
				break;
			case REQUEST_YXBFUNDSDETAILS_NODATA:
				if(isLoadMore){
					pullListView.setVisibility(View.VISIBLE);
					nodataText.setVisibility(View.GONE);
					isLoadMore = false;
					pullListView.onRefreshComplete();
				}else{
					pullListView.setVisibility(View.GONE);
					nodataText.setVisibility(View.VISIBLE);
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
		fundsDetailsActivity = (FundsDetailsActivity) getActivity();
		if(rootView==null){
            rootView=inflater.inflate(R.layout.funds_details_yxb_fragment, null);
            findViews(rootView,inflater);
        }
		//缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        } 
        handler.sendEmptyMessage(REQUEST_YXBFUNDSDETAILS_WHAT);
        return rootView;
	}

	private void findViews(View rootView,LayoutInflater layoutInflater){
		pullListView = (PullToRefreshListView)rootView.findViewById(R.id.funds_details_yxb_fragment_pull_refresh_list);
		pullListView.setMode(Mode.BOTH);
		nodataText = (TextView)rootView.findViewById(R.id.funds_details_yxb_nodata_text);
		pullListView.setOnRefreshListener(new OnRefreshListener2<ListView>(){
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				page = 0;
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						handler.sendEmptyMessage(REQUEST_YXBFUNDSDETAILS_WHAT);
					}
				}, 1000L);
			}
			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				//上拉加载更多
				page++;
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						isLoadMore = true;
						handler.sendEmptyMessage(REQUEST_YXBFUNDSDETAILS_WHAT);
					}
				}, 1000L);
			}
		});
		initAdapter();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	private void initAdapter(){
		fundsDetailsAdapter = new FundsDetailsAdapter(fundsDetailsActivity, "", "yxb");
		pullListView.setAdapter(fundsDetailsAdapter);
	}
	
	/**
	 * 元信宝资金明细
	 * @param userId
	 * @param page
	 * @param pageSize
	 */
	private void requestYXBFundsDetails(String userId,int page,int pageSize){
		if(isFirst && fundsDetailsActivity.loadingDialog != null){
			fundsDetailsActivity.loadingDialog.show();
		}
		isFirst = false;
		AsyncYXBNewAccount yxbAccountTask = new AsyncYXBNewAccount(fundsDetailsActivity, userId, String.valueOf(page), String.valueOf(pageSize), 
				new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						if(fundsDetailsActivity.loadingDialog != null){
							fundsDetailsActivity.loadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								Message msg = handler.obtainMessage(REQUEST_YXBFUNDSDETAILS_SUCCESS);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							}else{
								Message msg = handler.obtainMessage(REQUEST_YXBFUNDSDETAILS_NODATA);
								msg.obj = baseInfo.getMsg();
								handler.sendMessage(msg);
							}
						}
					}
		});
		yxbAccountTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
