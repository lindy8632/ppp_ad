package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ylfcf.ppp.adapter.YQYExtensionAdapter;
import com.ylfcf.ppp.async.AsyncYQYReward;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.YQYRewardPageInfo;
import com.ylfcf.ppp.entity.YqyRewardInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.ui.BannerTopicActivity;
import com.ylfcf.ppp.ui.MyInvitationActivity;
import com.ylfcf.ppp.util.Constants;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 元企盈
 * Created by Administrator on 2017/11/24.
 */

public class MyInvitationYQYFragment extends BaseFragment implements View.OnClickListener{
    private static final int REQUEST_EXTENSION_WHAT = 1200;
    private static final int REQUEST_EXTENSION_SUCCESS_WHAT = 1201;

    private MyInvitationActivity mainActivity;
    private View rootView;

    private RelativeLayout btnsLayout;
    private Button catTipsBtn,rewardBtn;
    private LinearLayout rewardMoneyLayout;
    private TextView totalMoneyTV;
    private PullToRefreshListView mListView;
    private TextView nodataTV;
    private LoadingDialog mLoadingDialog;
    private YQYExtensionAdapter adapter;
    private List<YqyRewardInfo> yqyRewardInfoListTemp = new ArrayList<YqyRewardInfo>();

    private boolean isLoadMore = false;
    private int page = 0;
    private int pageSize = 20;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REQUEST_EXTENSION_WHAT:
                    requestExtension(SettingsManager.getUserId(mainActivity));
                    break;
                case REQUEST_EXTENSION_SUCCESS_WHAT:
                    YQYRewardPageInfo pageInfo = (YQYRewardPageInfo)msg.obj;
                    if(pageInfo != null){
                        if(isLoadMore){
                            yqyRewardInfoListTemp.addAll(pageInfo.getYqyRewardInfoList());
                        }else{
                            yqyRewardInfoListTemp.clear();
                            yqyRewardInfoListTemp.addAll(pageInfo.getYqyRewardInfoList());
                        }
                        updateAdapter(yqyRewardInfoListTemp);
                        initRewardMoneyData(pageInfo,yqyRewardInfoListTemp);
                    }
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivity = (MyInvitationActivity) getActivity();
        mLoadingDialog = new LoadingDialog(mainActivity,"正在加载...", R.anim.loading);
        if(rootView==null){
            rootView = inflater.inflate(R.layout.myinvitation_yqy_fragment_layout, null);
            findViews(rootView);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        handler.sendEmptyMessage(REQUEST_EXTENSION_WHAT);
        return rootView;
    }

    private void findViews(View rootView){
        btnsLayout = (RelativeLayout) rootView.findViewById(R.id.myinvitation_yqy_fragment_btns_layout);
        catTipsBtn = (Button) rootView.findViewById(R.id.myinvitation_yqy_fragment_top_cat_tipsbtn);
        catTipsBtn.setOnClickListener(this);
        rewardBtn = (Button) rootView.findViewById(R.id.myinvitation_yqy_fragment_top_btn);
        rewardBtn.setOnClickListener(this);
        rewardMoneyLayout = (LinearLayout) rootView.findViewById(R.id.myinvitation_yqy_fragment_totalmoney_layout);
        totalMoneyTV = (TextView) rootView.findViewById(R.id.myinvitation_yqy_fragment_totalmoney);
        mListView = (PullToRefreshListView) rootView.findViewById(R.id.myinvitation_yqy_fragment_listview);
        nodataTV = (TextView) rootView.findViewById(R.id.myinvitation_yqy_fragment_nodata);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        initListeners();
        initAdapter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myinvitation_yqy_fragment_top_cat_tipsbtn:
                //查看提示
                showTipsDialog();
                break;
            case R.id.myinvitation_yqy_fragment_top_btn:
                //轻松赚提成
                Intent intentBanner = new Intent(mainActivity,BannerTopicActivity.class);
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
        View contentView = LayoutInflater.from(mainActivity).inflate(R.layout.myinvitation_tips_dialog_layout, null);
        final Button okBtn = (Button) contentView.findViewById(R.id.myinvitation_tips_dialog_sure_btn);
        final TextView contentTV = (TextView) contentView.findViewById(R.id.myinvitation_tips_content);
        contentTV.setText("1、奖励将在好友投资标的起息后予以显示并按\"预计到账时间\"发放。");
        AlertDialog.Builder builder=new AlertDialog.Builder(mainActivity, R.style.Dialog_Transparent);  //先得到构造器
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
        WindowManager windowManager = mainActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*4/5;
        dialog.getWindow().setAttributes(lp);
    }

    private void initRewardMoneyData(YQYRewardPageInfo pageInfo,List<YqyRewardInfo> list){
        rewardMoneyLayout.setVisibility(View.VISIBLE);
        totalMoneyTV.setText(pageInfo.getTotal_money());
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
                        handler.sendEmptyMessage(REQUEST_EXTENSION_WHAT);
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
                        handler.sendEmptyMessage(REQUEST_EXTENSION_WHAT);
                    }
                }, 1000L);
            }
        });
    }

    private void initAdapter() {
        adapter = new YQYExtensionAdapter(mainActivity);
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

    /**
     * 推荐好友的投资信息
     * @param userId
     */
    private void requestExtension(String userId){
        if(mLoadingDialog != null && !mainActivity.isFinishing()){
            mLoadingDialog.show();
        }
        AsyncYQYReward taks = new AsyncYQYReward(
                mainActivity, userId, String.valueOf(page),
                String.valueOf(pageSize), new Inter.OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(mLoadingDialog != null && mLoadingDialog.isShowing())
                    mLoadingDialog.dismiss();
                mListView.onRefreshComplete();
                if(baseInfo == null){
                    return;
                }
                int resultCode = SettingsManager
                        .getResultCode(baseInfo);
                if (resultCode == 0) {
                    Message msg = handler.obtainMessage(REQUEST_EXTENSION_SUCCESS_WHAT);
                    msg.obj = baseInfo.getmYQYRewardPageInfo();
                    handler.sendMessage(msg);
                }else{
//							Util.toastLong(MyInvitationActivity.this, baseInfo.getMsg());
                }
            }
        });
        taks.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }
}
