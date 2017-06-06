package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
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
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ExtensionNewInfo;
import com.ylfcf.ppp.entity.ExtensionNewPageInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.Constants.TopicType;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.URLGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的邀请
 * 
 * @author jianbing
 * 
 */
public class MyInvitationActivity extends BaseActivity implements
		OnClickListener {
	private static final int REQUEST_EXTENSION_WHAT = 1200;
	private static final int REQUEST_EXTENSION_SUCCESS_WHAT = 1201;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView friendCountTV;
	private TextView moneyTV;
	private Button qsztcBtn;//轻松赚提成
	private Button catTipsBtn;//查看提示
	private PullToRefreshListView mListView;
	private ExtensionNewPageInfo mExtensionPageInfo;
	private ExtensionAdapter adapter;
	private int page = 0;
	private int pageSize = 20;
	private List<ExtensionNewInfo> extensionList = new ArrayList<ExtensionNewInfo>();
	private boolean isLoadMore = false;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_EXTENSION_WHAT:
				requestExtension(SettingsManager.getUserId(getApplicationContext()));
				break;
			case REQUEST_EXTENSION_SUCCESS_WHAT:
				ExtensionNewPageInfo pageInfo = (ExtensionNewPageInfo) msg.obj;
				if(pageInfo != null){
					if(isLoadMore){
						extensionList.addAll(pageInfo.getExtensionList());
					}else{
						extensionList.clear();
						extensionList.addAll(pageInfo.getExtensionList());
					}
					updateAdapter(extensionList);
				}
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
		friendCountTV = (TextView) findViewById(R.id.myinvitation_activity_friends_tv);
		moneyTV = (TextView) findViewById(R.id.myinvitation_activity_money_tv);
		mListView = (PullToRefreshListView) findViewById(R.id.myinvitation_listview);
		mListView.setMode(Mode.BOTH);
		initListeners();
		initData();
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
		if(mExtensionPageInfo == null){
			friendCountTV.setText("0");
			moneyTV.setText("0");
			return;
		}
		friendCountTV.setText(mExtensionPageInfo.getExtension_user_count());
		moneyTV.setText(mExtensionPageInfo.getReward_total());
		if(mExtensionPageInfo.getExtensionList() == null || mExtensionPageInfo.getExtensionList().size() <= 0)
			return;
		extensionList.addAll(mExtensionPageInfo.getExtensionList());
		try {
			initAdapter(mExtensionPageInfo.getExtensionList());
		} catch (Exception e) {
		}
	}
	
	private void initAdapter(List<ExtensionNewInfo> list) {
		adapter = new ExtensionAdapter(MyInvitationActivity.this);
		mListView.setAdapter(adapter);
		if (list != null) {
			updateAdapter(list);
		}
	}

	private void updateAdapter(List<ExtensionNewInfo> list) {
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
		if(mLoadingDialog != null){
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
}
