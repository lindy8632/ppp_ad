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
import com.ylfcf.ppp.adapter.UserInvestRecordAdapter;
import com.ylfcf.ppp.async.AsyncInvestYJYRecord;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.InvestRecordPageInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.ui.BorrowDetailYJYActivity;
import com.ylfcf.ppp.ui.UserInvestRecordActivity;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;

import java.util.ArrayList;
import java.util.List;

/**
 * 元聚盈（员工专属产品）
 * Created by Administrator on 2017/7/26.
 */

public class UserInvestYJYRecordFragment extends BaseFragment{
    private static final String className = "UserInvestYJYRecordFragment";
    private static final int REQUEST_INVEST_RECORD_WHAT = 1021;
    private static final int REQUEST_INVEST_RECORD_SUCCESS = 1022;
    private static final int REQUEST_INVEST_RECORD_NODATA = 1023;	//无数据

    private UserInvestRecordActivity mainActivity;
    private View rootView;

    private UserInvestRecordAdapter recordAdapter;
    private PullToRefreshListView pullToRefreshListView;
    private TextView nodataText;
    private List<InvestRecordInfo> investRecordList = new ArrayList<InvestRecordInfo>();

    private int pageNo = 0;
    private int pageSize = 20;
    private boolean isLoadMore = false;// 加载更多

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_INVEST_RECORD_WHAT:
                    getInvestRecordList(SettingsManager.getUserId(mainActivity.getApplicationContext()), "");
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
                        recordAdapter.setItems(investRecordList);
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
            rootView = inflater.inflate(R.layout.userinvest_srzx_record_fragment, null);
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
                .findViewById(R.id.userinvest_srzx_record_fragment_pull_refresh_list);
        nodataText = (TextView) view
                .findViewById(R.id.userinvest_srzx_record_fragment_nodata);
        recordAdapter = new UserInvestRecordAdapter(mainActivity,"yjy");
        pullToRefreshListView.setAdapter(recordAdapter);
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
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                InvestRecordInfo info = (InvestRecordInfo)parent.getAdapter().getItem(position);
                Intent intent = new Intent(mainActivity,BorrowDetailYJYActivity.class);
                intent.putExtra("InvestRecordInfo", info);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取投资记录列表
     * @param investUserId
     * @param borrowId
     */
    private void getInvestRecordList(String investUserId,String borrowId){
        if(mainActivity.loadingDialog != null){
            mainActivity.loadingDialog.show();
        }
        AsyncInvestYJYRecord asyncInvestRecord = new AsyncInvestYJYRecord(mainActivity, investUserId,borrowId,
                pageNo, pageSize, new Inter.OnCommonInter(){
            @Override
            public void back(BaseInfo baseInfo) {
                mainActivity.loadingDialog.dismiss();
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
