package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.YQYCompFriendsAdapter;
import com.ylfcf.ppp.async.AsyncYQYCompFriends;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.YQYRewardPageInfo;
import com.ylfcf.ppp.entity.YqyRewardInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.util.Constants;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业用户丶好友
 * Created by Administrator on 2017/11/24.
 */

public class CompUserFriendsActivity extends BaseActivity implements View.OnClickListener{
    private static final int YQY_COMP_FRIENDS_WHAT = 19921;
    private static final int YQY_COMP_FRIENDS_SUC = 19922;

    private LinearLayout topLayout;
    private TextView countTV;
    private TextView moneyTV;
    private TextView nodataTV;
    private PullToRefreshListView mListView;
    private LinearLayout topLeftBtn;
    private TextView topTitleTV;
    private RelativeLayout btnsLayout;
    private Button catTipsBtn,rewardBtn;

    private boolean isLoadMore = false;
    private int page = 0;
    private int pageSize = 20;

    private YQYCompFriendsAdapter adapter;
    private List<YqyRewardInfo> yqyRewardInfoListTemp = new ArrayList<YqyRewardInfo>();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case YQY_COMP_FRIENDS_WHAT:
                    requestCompFriends(SettingsManager.getUserId(getApplicationContext()));
                    break;
                case YQY_COMP_FRIENDS_SUC:
                    YQYRewardPageInfo pageInfo = (YQYRewardPageInfo)msg.obj;
                    if(pageInfo != null){
                        topLayout.setVisibility(View.VISIBLE);
                        btnsLayout.setVisibility(View.VISIBLE);
                        initTopMoneyData(pageInfo);
                        if(isLoadMore){
                            yqyRewardInfoListTemp.addAll(pageInfo.getYqyRewardInfoList());
                        }else{
                            yqyRewardInfoListTemp.clear();
                            yqyRewardInfoListTemp.addAll(pageInfo.getYqyRewardInfoList());
                        }
                        updateAdapter(yqyRewardInfoListTemp);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.compuser_friends_activity_layout);
        findViews();
        handler.sendEmptyMessage(YQY_COMP_FRIENDS_WHAT);
    }

    private void findViews(){
        topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
        topLeftBtn.setOnClickListener(this);
        topTitleTV = (TextView)findViewById(R.id.common_page_title);
        topTitleTV.setText("好友投资");

        topLayout = (LinearLayout) findViewById(R.id.compuser_friends_activity_totalmoney_layout);
        countTV = (TextView) findViewById(R.id.compuser_friends_activity_count);
        moneyTV = (TextView) findViewById(R.id.compuser_friends_activity_money);
        nodataTV = (TextView) findViewById(R.id.compuser_friends_activity_nodata);
        mListView = (PullToRefreshListView) findViewById(R.id.compuser_friends_activity_listview);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        btnsLayout = (RelativeLayout) findViewById(R.id.compuser_friends_activity_btns_layout);
        catTipsBtn = (Button) findViewById(R.id.compuser_friends_top_cat_tipsbtn);
        catTipsBtn.setOnClickListener(this);
        rewardBtn = (Button) findViewById(R.id.compuser_friends_activity_top_btn);
        rewardBtn.setOnClickListener(this);
        initListeners();
        initAdapter();
    }

    private void initTopMoneyData(YQYRewardPageInfo pageInfo){
        countTV.setText(pageInfo.getTotal());
        moneyTV.setText(pageInfo.getTotal_money());
    }

    private void initListeners() {
        mListView.setOnRefreshListener(new OnRefreshListener2<ListView>(){
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // 下拉刷新
                page = 0;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isLoadMore = false;
                        handler.sendEmptyMessage(YQY_COMP_FRIENDS_WHAT);
                    }
                }, 1000L);
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
                        handler.sendEmptyMessage(YQY_COMP_FRIENDS_WHAT);
                    }
                }, 1000L);
            }
        });
    }

    private void initAdapter() {
        adapter = new YQYCompFriendsAdapter(this);
        mListView.setAdapter(adapter);
    }

    private void updateAdapter(List<YqyRewardInfo> list) {
        if(list == null || list.size() <= 0){
            nodataTV.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            return;
        }
        nodataTV.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        adapter.setItems(list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_topbar_left_layout:
                finish();
                break;
            case R.id.compuser_friends_top_cat_tipsbtn:
                //查看提示
                showTipsDialog();
                break;
            case R.id.compuser_friends_activity_top_btn:
                //轻松赚提成
                Intent intentBanner = new Intent(CompUserFriendsActivity.this,BannerTopicActivity.class);
                BannerInfo bannerInfo = new BannerInfo();
                bannerInfo.setArticle_id(Constants.TopicType.TUIGUANGYUAN);
                bannerInfo.setLink_url(URLGenerator.PROMOTER_URL);
                bannerInfo.setFrom_where("人脉收益");
                intentBanner.putExtra("BannerInfo", bannerInfo);
                startActivityForResult(intentBanner, 100);
                break;
        }
    }

    /**
     * 退出登录的Dialog
     */
    private void showTipsDialog(){
        View contentView = LayoutInflater.from(this).inflate(R.layout.myinvitation_tips_dialog_layout, null);
        final Button okBtn = (Button) contentView.findViewById(R.id.myinvitation_tips_dialog_sure_btn);
        final TextView contentTV = (TextView) contentView.findViewById(R.id.myinvitation_tips_content);
            //非理财师
        contentTV.setText("1. 仅显示A层好友的投资详情。");
        AlertDialog.Builder builder=new AlertDialog.Builder(this, R.style.Dialog_Transparent);  //先得到构造器
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //参数都设置完成了，创建并显示出来
        dialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*4/5;
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * 企业好友
     * @param userId
     */
    private void requestCompFriends(String userId){
        if(mLoadingDialog != null){
            mLoadingDialog.show();
        }
        AsyncYQYCompFriends task = new AsyncYQYCompFriends(CompUserFriendsActivity.this, userId,
                String.valueOf(page), String.valueOf(pageSize), new Inter.OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                    mLoadingDialog.dismiss();
                }
                mListView.onRefreshComplete();
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        YQYRewardPageInfo pageInfo = baseInfo.getmYQYRewardPageInfo();
                        Message msg = handler.obtainMessage(YQY_COMP_FRIENDS_SUC);
                        msg.obj = pageInfo;
                        handler.sendMessage(msg);
                    }else if(resultCode == -1){

                    }else{
                        Util.toastLong(CompUserFriendsActivity.this,baseInfo.getMsg());
                    }
                }else{
                    Util.toastLong(CompUserFriendsActivity.this,"您的网络不给力~");
                }
            }
        });
        task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }
}
