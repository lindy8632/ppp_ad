package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ylfcf.view.CustomerFooter;
import com.example.ylfcf.widget.XRefreshView;
import com.example.ylfcf.widget.XRefreshView.SimpleXRefreshListener;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.TransferedCouponAdapter;
import com.ylfcf.ppp.async.AsyncTransferCoupon;
import com.ylfcf.ppp.async.AsyncTransferedCouponList;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.FriendInfo;
import com.ylfcf.ppp.entity.JiaxiquanInfo;
import com.ylfcf.ppp.entity.JiaxiquanPageInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * 请选择加息券
 * Created by Administrator on 2017/7/7.
 */

public class ChooseJXQActivity extends BaseActivity implements View.OnClickListener{
    private static final String className = "ChooseJXQActivity";
    private static final int REQUEST_TRANSFERED_COUPON_LIST_WHAT = 8412;
    private static final int REQUEST_TRANSFERED_COUPON_LIST_SUC = 8413;
    private static final int REQUEST_TRANSFERED_COUPON_LIST_NODATA = 8414;

    private static final int REQUEST_TRANSFER_COUPON_WHAT = 8415;
    private static final int REQUEST_TRANSFER_COUPON_SUC = 8416;
    private static final int REQUEST_TRANSFER_COUPON_NODATA = 8417;

    private LinearLayout topLeftBtn;
    private TextView topTitleTV;
    private XRefreshView mXRefreshView;
    private ListView mListView;
    private Button chooseAllBtn,cancelAllBtn,sureBtn;
    private TextView nodataTV;
    private LinearLayout pageLayout;
    private TextView posTV;//当前位置
    private TextView totalTV;//加息券总数
    private LinearLayout bottomLayout;

    private int page = 0;
    private int pageSize = 10;
    private List<JiaxiquanInfo> jxqTempList = new ArrayList<JiaxiquanInfo>();

    private boolean isRefresh = false;//是否下拉刷新数据。
    private boolean isLoadMore = false;//上否是上拉加载更多

    private TransferedCouponAdapter adapter;
    private FriendInfo mFriendInfo;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REQUEST_TRANSFERED_COUPON_LIST_WHAT:
                    requestTransferedCouponList(SettingsManager.getUserId(getApplicationContext()),"未使用","1");
                    break;
                case REQUEST_TRANSFERED_COUPON_LIST_SUC:
                    nodataTV.setVisibility(View.GONE);
                    bottomLayout.setVisibility(View.VISIBLE);
                    mXRefreshView.setVisibility(View.VISIBLE);
                    JiaxiquanPageInfo pageInfo = (JiaxiquanPageInfo)msg.obj;
                    if(pageInfo == null)
                        return;
                    pageLayout.setVisibility(View.VISIBLE);
                    totalTV.setText(pageInfo.getTotal());
                    if(isRefresh){
                        jxqTempList.clear();
                    }
                    jxqTempList.addAll(pageInfo.getInfoList());
                    updateAdapter(jxqTempList);
                    isRefresh = false;
                    isLoadMore = false;
                    break;
                case REQUEST_TRANSFERED_COUPON_LIST_NODATA:
                    if(isLoadMore){
                        nodataTV.setVisibility(View.GONE);
                        bottomLayout.setVisibility(View.VISIBLE);
                        mXRefreshView.setVisibility(View.VISIBLE);
                        mXRefreshView.setLoadComplete(true);
                    }else if(isRefresh){
                        nodataTV.setVisibility(View.VISIBLE);
                        bottomLayout.setVisibility(View.GONE);
                        mXRefreshView.setVisibility(View.GONE);
                        pageLayout.setVisibility(View.GONE);
                    }
                    isRefresh = false;
                    isLoadMore = false;
                    break;
                case REQUEST_TRANSFER_COUPON_WHAT:
                    checkTransferCoupons();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_jxq_activity);
        mFriendInfo = (FriendInfo) getIntent().getSerializableExtra("friendInfo");
        findViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UMengStatistics.statisticsOnPageStart(className);//友盟统计页面跳转
        UMengStatistics.statisticsResume(this);//友盟统计时长
        isRefresh = true;
        isLoadMore = false;
        page = 0;
        handler.sendEmptyMessage(REQUEST_TRANSFERED_COUPON_LIST_WHAT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UMengStatistics.statisticsOnPageEnd(className);//友盟统计页面跳转
        UMengStatistics.statisticsPause(this);//友盟统计时长
    }

    private void findViews(){
        topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
        topLeftBtn.setOnClickListener(this);
        topTitleTV = (TextView) findViewById(R.id.common_page_title);
        topTitleTV.setText("请选择加息券");

        nodataTV = (TextView) findViewById(R.id.choose_jxq_activity_nodata);
        bottomLayout = (LinearLayout) findViewById(R.id.choose_jxq_activity_bottomlayout);
        mXRefreshView = (XRefreshView) findViewById(R.id.choosejxq_activity_xrefreshview);
        mListView = (ListView) findViewById(R.id.choosejxq_activity_listview);
        chooseAllBtn = (Button) findViewById(R.id.choosejxq_activity_allchoose_btn);
        chooseAllBtn.setOnClickListener(this);
        cancelAllBtn = (Button) findViewById(R.id.choosejxq_activity_cancelchoose_btn);
        cancelAllBtn.setOnClickListener(this);
        sureBtn = (Button) findViewById(R.id.choosejxq_activity_ensure_btn);
        sureBtn.setOnClickListener(this);
        pageLayout = (LinearLayout) findViewById(R.id.choose_jxq_activity_page_layout);
        pageLayout.setOnClickListener(this);
        posTV = (TextView) findViewById(R.id.choose_jxq_activity_page_position);
        totalTV = (TextView) findViewById(R.id.choose_jxq_activity_page_total);

        initXRefrshView();
        initAdapter();
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
        mXRefreshView.setScrollBackDuration(300);
    }

    private void initListeners() {
        mXRefreshView.setXRefreshViewListener(new SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                isRefresh = true;
                isLoadMore = false;
                mXRefreshView.setLoadComplete(false);
                page = 0;
                handler.sendEmptyMessage(REQUEST_TRANSFERED_COUPON_LIST_WHAT);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                isRefresh = false;
                isLoadMore = true;
                page ++;
                handler.sendEmptyMessage(REQUEST_TRANSFERED_COUPON_LIST_WHAT);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JiaxiquanInfo info = (JiaxiquanInfo)parent.getAdapter().getItem(position);
                for(int i=0;i<jxqTempList.size();i++){
                    JiaxiquanInfo infoTemp = jxqTempList.get(i);
                    if(info.getId().equals(infoTemp.getId())){
                        if(info.isChecked()){
                            jxqTempList.get(i).setChecked(false);
                        }else{
                            jxqTempList.get(i).setChecked(true);
                        }
                    }
                }
                updateAdapter(jxqTempList);
            }
        });

        mXRefreshView.setOnAbsListViewScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                YLFLogger.d("mListView-mXRefreshView.onScroll():"+"\nfirstVisibleItem:"+firstVisibleItem+"\nvisibleItemCount:"
                        +visibleItemCount+"\ntotalItemCount:"+totalItemCount);
                posTV.setText(String.valueOf(firstVisibleItem + visibleItemCount));
            }
        });
    }

    private void updateAdapter(List<JiaxiquanInfo> list){
        adapter.setItems(list);
    }

    private void initAdapter(){
        adapter = new TransferedCouponAdapter(ChooseJXQActivity.this,
                new TransferedCouponAdapter.OnJXQItemClickListener(){
            @Override
            public void onClick(JiaxiquanInfo jxqInfo, int position) {
                for(int i=0;i<jxqTempList.size();i++){
                    JiaxiquanInfo info = jxqTempList.get(i);
                    if(jxqInfo.getId().equals(info.getId())){
                        if(jxqInfo.isChecked()){
                            jxqTempList.get(i).setChecked(false);
                        }else{
                            jxqTempList.get(i).setChecked(true);
                        }
                    }
                }
                updateAdapter(jxqTempList);
            }
        });
        mListView.setAdapter(adapter);
    }

    List<JiaxiquanInfo> checkedList = new ArrayList<JiaxiquanInfo>();
    private void checkTransferCoupons(){
        checkedList.clear();
        for(int i=0;i<jxqTempList.size();i++){
            if(jxqTempList.get(i).isChecked()){
                checkedList.add(jxqTempList.get(i));
            }
        }
        if(checkedList.size() <= 0){
            showPleaseChooseCouponPrompt();
        }else{
            showSureTransferCouponPrompt();
        }
    }

    private void requestTransferCoupons(){
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<checkedList.size();i++){
            if(i == checkedList.size() - 1){
                //最后一个
                sb.append(checkedList.get(i).getId());
            }else{
                sb.append(checkedList.get(i).getId()).append(",");
            }
        }
        transferCoupons(mFriendInfo.getId(),sb.toString());
    }

    /**
     *请勾选加息券
     */
    private void showPleaseChooseCouponPrompt(){
        View contentView = LayoutInflater.from(ChooseJXQActivity.this).inflate(R.layout.myinvitation_tips_dialog_layout, null);
        TextView titleTV = (TextView) contentView.findViewById(R.id.myinvitation_tips_title);
        titleTV.setVisibility(View.GONE);
        TextView contentTV = (TextView) contentView.findViewById(R.id.myinvitation_tips_content);
        contentTV.setPadding(0,50,0,50);
        contentTV.setText("请勾选加息券");
        contentTV.setGravity(Gravity.CENTER);
        final Button okBtn = (Button) contentView.findViewById(R.id.myinvitation_tips_dialog_sure_btn);
        AlertDialog.Builder builder=new AlertDialog.Builder(ChooseJXQActivity.this, R.style.Dialog_Transparent);  //先得到构造器
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

    /**
     * 确定将加息券转让给xxx
     */
    private void showSureTransferCouponPrompt(){
        View contentView = LayoutInflater.from(ChooseJXQActivity.this).inflate(R.layout.jxq_transfer_prompt_dialog_layout, null);
        final Button okBtn = (Button) contentView.findViewById(R.id.jxq_transfer_prompt_dialog_okbtn);
        ImageView delBtn = (ImageView) contentView.findViewById(R.id.jxq_transfer_prompt_dialog_delbtn);
        final TextView contentTV = (TextView) contentView.findViewById(R.id.jxq_transfer_prompt_dialog_content);
        if(mFriendInfo.getReal_name() != null && !"".equals(mFriendInfo.getReal_name())){
            contentTV.setText("确定把"+checkedList.size()+"张加息券转让给"+ Util.hidRealName2(mFriendInfo.getReal_name())+"，\n手机号："+Util.hidPhoneNum(mFriendInfo.getPhone())+"?");
        }else{
            contentTV.setText("确定把"+checkedList.size()+"张加息券转让给\n"+"手机号："+Util.hidPhoneNum(mFriendInfo.getPhone())+"?");
        }
        contentTV.setGravity(Gravity.CENTER);
        AlertDialog.Builder builder = new AlertDialog.Builder(ChooseJXQActivity.this, R.style.Dialog_Transparent);  //先得到构造器
        builder.setView(contentView);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                requestTransferCoupons();
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener(){
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
        lp.width = display.getWidth()*6/7;
        lp.height = display.getHeight() / 3;
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * 转让成功提示
     */
    private void showTransferCouponSucPrompt(){
        View contentView = LayoutInflater.from(ChooseJXQActivity.this).inflate(R.layout.jxq_transfer_suc_dialog_layout, null);
        final Button okBtn = (Button) contentView.findViewById(R.id.jxq_transfer_suc_dialog_okbtn);
        ImageView delBtn = (ImageView) contentView.findViewById(R.id.jxq_transfer_suc_dialog_delbtn);
        TextView receiverName = (TextView) contentView.findViewById(R.id.jxq_transfer_suc_dialog_receivername);
        TextView receiverPhone = (TextView) contentView.findViewById(R.id.jxq_transfer_suc_dialog_receiverphone);
        if(mFriendInfo.getReal_name() != null && !"".equals(mFriendInfo.getReal_name())){
            receiverName.setVisibility(View.VISIBLE);
            receiverName.setText("接收人姓名: "+Util.hidRealName2(mFriendInfo.getReal_name()));
        }else{
            receiverName.setVisibility(View.GONE);
        }
        receiverPhone.setText("接收人手机号: "+Util.hidPhoneNum(mFriendInfo.getPhone()));
        AlertDialog.Builder builder = new AlertDialog.Builder(ChooseJXQActivity.this, R.style.Dialog_Transparent);  //先得到构造器
        builder.setView(contentView);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(ChooseJXQActivity.this,MyJXQActivity.class);
                intent.putExtra("cur_position",1);
                startActivity(intent);
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(ChooseJXQActivity.this,MyJXQActivity.class);
                intent.putExtra("cur_position",1);
                startActivity(intent);
            }
        });
        //参数都设置完成了，创建并显示出来
        dialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*6/7;
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_topbar_left_layout:
                finish();
                break;
            case R.id.choosejxq_activity_allchoose_btn:
                //全选
                checkAllVisibleDatas();
                break;
            case R.id.choosejxq_activity_cancelchoose_btn:
                //取消全部
                cancelAllVisibleDatas();
                break;
            case R.id.choosejxq_activity_ensure_btn:
                //确定
                handler.sendEmptyMessage(REQUEST_TRANSFER_COUPON_WHAT);
                break;
            case R.id.choose_jxq_activity_page_layout:
                mListView.smoothScrollToPosition(0);
                break;
        }
    }

    //全选
    private void checkAllVisibleDatas(){
        for(int i=0;i<jxqTempList.size();i++){
            jxqTempList.get(i).setChecked(true);
        }
        updateAdapter(jxqTempList);
    }

    //取消全选
    private void cancelAllVisibleDatas(){
        for(int i=0;i<jxqTempList.size();i++){
            jxqTempList.get(i).setChecked(false);
        }
        updateAdapter(jxqTempList);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * 可转让加息券列表
     * @param userId
     * @param useStatus
     * @param transfer 0表示不可转让 1表示可转让 2表示已转让
     */
    private void requestTransferedCouponList(String userId,String useStatus,String transfer){
        if(mLoadingDialog != null && !isFinishing()){
            mLoadingDialog.show();
        }
        AsyncTransferedCouponList couponListTask = new AsyncTransferedCouponList(ChooseJXQActivity.this,
                userId, useStatus, String.valueOf(page),
                String.valueOf(pageSize), transfer, new Inter.OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                    mLoadingDialog.dismiss();
                }
                mXRefreshView.setVisibility(View.VISIBLE);
                updateRefreshListViewStatus(isRefresh,isLoadMore);
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        Message msg = handler.obtainMessage(REQUEST_TRANSFERED_COUPON_LIST_SUC);
                        msg.obj = baseInfo.getmJiaxiquanPageInfo();
                        handler.sendMessage(msg);
                    }else{
                        Message msg = handler.obtainMessage(REQUEST_TRANSFERED_COUPON_LIST_NODATA);
                        msg.obj = baseInfo.getMsg();
                        handler.sendMessage(msg);
                    }
                }else{
                    Message msg = handler.obtainMessage(REQUEST_TRANSFERED_COUPON_LIST_NODATA);
                    msg.obj = "您的网络不给力";
                    handler.sendMessage(msg);
                }
            }
        });
        couponListTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * 转让多张加息券
     * @param userId 接收加息券人的id
     * @param couponIds 加息券的id,用逗号分隔
     */
    private void transferCoupons(String userId,String couponIds){
        if(mLoadingDialog != null && !isFinishing()){
            mLoadingDialog.show();
        }
        AsyncTransferCoupon couponTask = new AsyncTransferCoupon(ChooseJXQActivity.this, userId, couponIds,
                new Inter.OnCommonInter() {
                    @Override
                    public void back(BaseInfo baseInfo) {
                        if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                            mLoadingDialog.dismiss();
                        }
                        if(baseInfo != null){
                            int resultCode = SettingsManager.getResultCode(baseInfo);
                            if(resultCode == 0){
                                //转让成功
                                showTransferCouponSucPrompt();
                            }else{
                                Util.toastLong(ChooseJXQActivity.this,baseInfo.getMsg());
                            }
                        }
                    }
                });
        couponTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }
}
