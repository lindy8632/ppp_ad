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
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.ExtensionAdapter;
import com.ylfcf.ppp.async.AsyncExtensionNewPageInfo;
import com.ylfcf.ppp.async.AsyncGetLCSName;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ExtensionNewInfo;
import com.ylfcf.ppp.entity.ExtensionNewPageInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.Constants.TopicType;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.URLGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的邀请
 * @author jianbing
 * 
 */
public class MyInvitationActivity extends BaseActivity implements
		OnClickListener {
	private static final int REQUEST_EXTENSION_WHAT = 1200;
	private static final int REQUEST_EXTENSION_SUCCESS_WHAT = 1201;
	private static final int REQUEST_USERINFO_WHAT = 1203;
	private static final int REQUEST_USERINFO_SUC = 1204;

	private static final int REQUEST_LCS_WHAT = 1202;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView totalMoneyTV;
	private TextView myselfMoneyTV;
	private TextView oneMoneyTV;
	private TextView twoMoneyTV;
	private TextView otherMoneyTV;
	private LinearLayout totalMoneyLayout,myselfMoneyLayout,oneMoneyLayout,twoMoneyLayout,otherMoneyLayout;
	private Button qsztcBtn;//轻松赚提成
	private Button catTipsBtn;//查看提示
	private PullToRefreshListView mListView;
	private TextView nodataTV;//暂无数据
	private ExtensionNewPageInfo mExtensionPageInfo;
	private ExtensionAdapter adapter;
	private int page = 0;
	private int pageSize = 20;
	private List<ExtensionNewInfo> extensionList = new ArrayList<ExtensionNewInfo>();
	private boolean isLoadMore = false;
	private boolean isLcs = false;
	private UserInfo userInfo = null;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_EXTENSION_WHAT:
				requestExtension(SettingsManager.getUserId(getApplicationContext()));
				break;
			case REQUEST_EXTENSION_SUCCESS_WHAT:
				mExtensionPageInfo = (ExtensionNewPageInfo) msg.obj;
				if(mExtensionPageInfo != null){
					if(isLoadMore){
						extensionList.addAll(mExtensionPageInfo.getExtensionList());
					}else{
						extensionList.clear();
						extensionList.addAll(mExtensionPageInfo.getExtensionList());
					}
					initData();
					updateAdapter(extensionList);
				}
				break;
			case REQUEST_LCS_WHAT:
				requestLcsName(SettingsManager.getUser(getApplicationContext()));
				break;
			case REQUEST_USERINFO_WHAT:
				requestUserInfo(SettingsManager.getUserId(getApplicationContext()),"");
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
		setContentView(R.layout.myinvitation_activity);
		mExtensionPageInfo = (ExtensionNewPageInfo) getIntent()
				.getSerializableExtra("ExtensionPageInfo");
		findViews();
		handler.sendEmptyMessage(REQUEST_LCS_WHAT);
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("人脉收益");

		qsztcBtn = (Button) findViewById(R.id.myinvitation_activity_top_btn);
		qsztcBtn.setOnClickListener(this);
		catTipsBtn = (Button) findViewById(R.id.myinvitation_activity_top_cat_tipsbtn);
		catTipsBtn.setOnClickListener(this);
		totalMoneyTV = (TextView) findViewById(R.id.myinvitation_activity_totalmoney);
		myselfMoneyTV = (TextView) findViewById(R.id.myinvitation_activity_myselfmoney);
		oneMoneyTV = (TextView) findViewById(R.id.myinvitation_activity_onemoney);
		twoMoneyTV = (TextView) findViewById(R.id.myinvitation_activity_twomoney);
		otherMoneyTV = (TextView) findViewById(R.id.myinvitation_activity_othermoney);
		totalMoneyLayout = (LinearLayout) findViewById(R.id.myinvitation_activity_totalmoney_layout);
		myselfMoneyLayout = (LinearLayout) findViewById(R.id.myinvitation_activity_myself_layout);
		oneMoneyLayout = (LinearLayout) findViewById(R.id.myinvitation_activity_onemoney_layout);
		twoMoneyLayout = (LinearLayout) findViewById(R.id.myinvitation_activity_twomoney_layout);
		otherMoneyLayout = (LinearLayout) findViewById(R.id.myinvitation_activity_othermoney_layout);
		nodataTV = (TextView) findViewById(R.id.myinvitation_activity_nodata);
		mListView = (PullToRefreshListView) findViewById(R.id.myinvitation_listview);
		mListView.setMode(Mode.PULL_FROM_END);
		initListeners();
		initAdapter();
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

	private void initData(){
		if(isLcs || (userInfo != null && "微企汇".equals(userInfo.getType()) && "0".equals(userInfo.getExtension_user_id()))){
			//理财师或者微企汇的A级用户
			totalMoneyLayout.setVisibility(View.VISIBLE);
			totalMoneyLayout.setGravity(Gravity.LEFT);
			oneMoneyLayout.setVisibility(View.VISIBLE);
			twoMoneyLayout.setVisibility(View.VISIBLE);
			double otherTotalD = 0d;
			try{
				otherTotalD = Double.parseDouble(mExtensionPageInfo.getOther_total());
			}catch (Exception e){
				e.printStackTrace();
			}
			if(otherTotalD > 0){
				otherMoneyLayout.setVisibility(View.VISIBLE);
			}else{
				otherMoneyLayout.setVisibility(View.GONE);
			}
			if(userInfo != null && "微企汇".equals(userInfo.getType())
					&& "0".equals(userInfo.getExtension_user_id())){
				//微企汇A级用户不显示历史好友
				otherMoneyLayout.setVisibility(View.GONE);
			}
			if(mExtensionPageInfo != null && isLcs){
				try{
					double myselfMoneyD = Double.parseDouble(mExtensionPageInfo.getMyself_total());
					if(myselfMoneyD > 0){
						myselfMoneyLayout.setVisibility(View.VISIBLE);
					}else{
						myselfMoneyLayout.setVisibility(View.GONE);
					}
				}catch (Exception e){
					myselfMoneyLayout.setVisibility(View.GONE);
					e.printStackTrace();
				}
			}else{
				myselfMoneyLayout.setVisibility(View.GONE);
			}
		}else{
			totalMoneyLayout.setVisibility(View.VISIBLE);
			totalMoneyLayout.setGravity(Gravity.CENTER_HORIZONTAL);
			oneMoneyLayout.setVisibility(View.GONE);
			twoMoneyLayout.setVisibility(View.GONE);
			otherMoneyLayout.setVisibility(View.GONE);
		}
		if(mExtensionPageInfo == null){
			totalMoneyTV.setText("0");
			myselfMoneyTV.setText("0");
			oneMoneyTV.setText("0");
			twoMoneyTV.setText("0");
			otherMoneyTV.setText("0");
			return;
		}
		totalMoneyTV.setText(mExtensionPageInfo.getReward_total());
		myselfMoneyTV.setText(mExtensionPageInfo.getMyself_total());
		oneMoneyTV.setText(mExtensionPageInfo.getOne_total());
		twoMoneyTV.setText(mExtensionPageInfo.getSecond_total());
		otherMoneyTV.setText(mExtensionPageInfo.getOther_total());

		if(mExtensionPageInfo.getExtensionList() == null || mExtensionPageInfo.getExtensionList().size() <= 0 ){
			mListView.setVisibility(View.GONE);
			nodataTV.setVisibility(View.VISIBLE);
			return;
		}
		mListView.setVisibility(View.VISIBLE);
		nodataTV.setVisibility(View.GONE);
	}
	
	private void initAdapter() {
		adapter = new ExtensionAdapter(MyInvitationActivity.this);
		mListView.setAdapter(adapter);
	}

	private void updateAdapter(List<ExtensionNewInfo> list) {
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
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.myinvitation_activity_top_btn:
			//轻松赚提成
			Intent intentBanner = new Intent(MyInvitationActivity.this,BannerTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setArticle_id(TopicType.TUIGUANGYUAN);
			bannerInfo.setLink_url(URLGenerator.PROMOTER_URL);
			bannerInfo.setFrom_where("人脉收益");
			intentBanner.putExtra("BannerInfo", bannerInfo);
			startActivityForResult(intentBanner, 100);
			break;
		case R.id.myinvitation_activity_top_cat_tipsbtn:
			//查看提示
			showTipsDialog();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 100:
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 退出登录的Dialog
	 */
	private void showTipsDialog(){
		View contentView = LayoutInflater.from(MyInvitationActivity.this).inflate(R.layout.myinvitation_tips_dialog_layout, null);
		final Button okBtn = (Button) contentView.findViewById(R.id.myinvitation_tips_dialog_sure_btn);
		final TextView contentTV = (TextView) contentView.findViewById(R.id.myinvitation_tips_content);
		if(userInfo != null && "微企汇".equals(userInfo.getType()) && "0".equals(userInfo.getExtension_user_id())){
			//微企汇A级用户
			double otherTotalD = 0d;
			try{
				otherTotalD = Double.parseDouble(mExtensionPageInfo.getOther_total());
			}catch (Exception e){
				e.printStackTrace();
			}
			if(otherTotalD <= 0){
				//理财师的直接好友里面没有晋级为理财师的情况
				contentTV.setText(getResources().getString(R.string.myinvitation_tips_string));
			}else{
				//理财师的直接好友里面有晋级为理财师的情况
				contentTV.setText(getResources().getString(R.string.myinvitation_tips_string1));
			}
		}else if(isLcs){
			//理财师A级用户
			double otherTotalD = 0d;
			try{
				otherTotalD = Double.parseDouble(mExtensionPageInfo.getOther_total());
			}catch (Exception e){
				e.printStackTrace();
			}
			if(otherTotalD <= 0){
				//理财师的直接好友里面没有晋级为理财师的情况
				contentTV.setText(getResources().getString(R.string.myinvitation_tips_string4));
			}else{
				//理财师的直接好友里面有晋级为理财师的情况
				contentTV.setText(getResources().getString(R.string.myinvitation_tips_string5));
			}
		}else if(userInfo != null && "微企汇".equals(userInfo.getType()) && !"0".equals(userInfo.getExtension_user_id())){
			//微企汇的B级C级用户
			contentTV.setText(getResources().getString(R.string.myinvitation_tips_string3));
		}else{
			//非理财师
			contentTV.setText(getResources().getString(R.string.myinvitation_tips_string2));
		}
		AlertDialog.Builder builder=new AlertDialog.Builder(MyInvitationActivity.this, R.style.Dialog_Transparent);  //先得到构造器
		builder.setView(contentView);
		builder.setCancelable(true);
		final AlertDialog dialog = builder.create();
		okBtn.setOnClickListener(new OnClickListener() {
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
	 * 邀请好友的信息
	 * @param userId
	 */
	private void requestExtension(String userId) {
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncExtensionNewPageInfo taks = new AsyncExtensionNewPageInfo(
				MyInvitationActivity.this, userId, String.valueOf(page),
				String.valueOf(pageSize), new OnCommonInter() {
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
						if (resultCode == 1) {
							Message msg = handler.obtainMessage(REQUEST_EXTENSION_SUCCESS_WHAT);
							msg.obj = baseInfo.getExtensionNewPageInfo();
							handler.sendMessage(msg);
						}else{
//							Util.toastLong(MyInvitationActivity.this, baseInfo.getMsg());
						}
					}
				});
		taks.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 获取
	 * @param phone
	 */
	private void requestLcsName(String phone){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncGetLCSName lcsTask = new AsyncGetLCSName(MyInvitationActivity.this, phone, new Inter.OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						//是理财师
						isLcs = true;
					}else{
						//非理财师
						isLcs = false;
					}
				}else{
					isLcs = false;
				}
				handler.sendEmptyMessage(REQUEST_USERINFO_WHAT);
			}
		});
		lcsTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * @param userId
	 * @param phone
	 */
	private void requestUserInfo(final String userId,String phone){
		AsyncUserSelectOne userTask = new AsyncUserSelectOne(MyInvitationActivity.this, userId, phone, "","",
				new Inter.OnGetUserInfoByPhone() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						userInfo = baseInfo.getUserInfo();
					}
				}
				handler.sendEmptyMessage(REQUEST_EXTENSION_WHAT);
			}
		});
		userTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
