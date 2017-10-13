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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.MyJXQListAdapter;
import com.ylfcf.ppp.adapter.MyJXQListAdapter.OnJXQItemClickListener;
import com.ylfcf.ppp.async.AsyncJXQPageInfo;
import com.ylfcf.ppp.async.AsyncJXQTransfer;
import com.ylfcf.ppp.async.AsyncJXQTransferGetSubUser;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.JiaxiquanInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.ui.BorrowListZXDActivity;
import com.ylfcf.ppp.ui.MyJXQActivity;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 加息券未使用
 * @author Mr.liu
 *
 */
public class MyJXQNousedFragment extends BaseFragment{
	private static final String className = "MyJXQNousedFragment";
	private MyJXQActivity.OnJXQNousedTransferSucListener mOnJXQNousedTransferSucListener;
	public final int REQUEST_JXQ_LIST_WHAT = 1800;
	private final int REQUEST_JXQ_LIST_SUCCESS = 1801;
	private final int REQUEST_JXQ_LIST_FAILE = 1802;

	private MyJXQActivity mainActivity;
	private View rootView;

	private MyJXQListAdapter mMyJXQListAdapter;
	private PullToRefreshListView pullToRefreshListView;
	private TextView nodataText;
	private List<JiaxiquanInfo> jxqList = new ArrayList<JiaxiquanInfo>();

	private int pageNo = 1;
	private int pageSize = 20;
	private boolean isFirst = true;
	private boolean isLoadMore = false;// 加载更多
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_JXQ_LIST_WHAT:
				requestJXQList(SettingsManager.getUserId(mainActivity
						.getApplicationContext()), "未使用");
				break;
			case REQUEST_JXQ_LIST_SUCCESS:
				Date endDate = null;
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				pullToRefreshListView.setVisibility(View.VISIBLE);
				nodataText.setVisibility(View.GONE);
				if (baseInfo != null) {
					if (!isLoadMore) {
						jxqList.clear();
					}
					for(int i=0;i<baseInfo.getmJiaxiquanPageInfo().getInfoList().size();i++){
						JiaxiquanInfo info = baseInfo.getmJiaxiquanPageInfo().getInfoList().get(i);
						try {
							endDate = sdf.parse(info.getEffective_end_time());
							if(endDate.compareTo(sdf.parse(baseInfo.getTime())) == 1){
								//表示加息券还未过期
								jxqList.add(info);
							}
						} catch (Exception e) {
						}
					}

					if(jxqList.size() <= 0){
						pullToRefreshListView.setVisibility(View.GONE);
						nodataText.setVisibility(View.VISIBLE);
					}else{
						pullToRefreshListView.setVisibility(View.VISIBLE);
						nodataText.setVisibility(View.GONE);
						mMyJXQListAdapter.setItems(jxqList,baseInfo.getTime());
					}
				}
				isLoadMore = false;
				pullToRefreshListView.onRefreshComplete();
				break;
			case REQUEST_JXQ_LIST_FAILE:
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

	public MyJXQNousedFragment(MyJXQActivity.OnJXQNousedTransferSucListener mOnJXQNousedTransferSucListener){
		this.mOnJXQNousedTransferSucListener = mOnJXQNousedTransferSucListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainActivity = (MyJXQActivity) getActivity();
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.myhb_noused_fragment, null);
			findViews(rootView);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
			handler.sendEmptyMessage(REQUEST_JXQ_LIST_WHAT);
		}
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
	
	private void findViews(View view) {
		pullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.myhb_noused_fragment_pull_refresh_list);
		nodataText = (TextView) view
				.findViewById(R.id.myhb_noused_fragment_nodata);
		mMyJXQListAdapter = new MyJXQListAdapter(mainActivity,
				new OnJXQItemClickListener() {
					@Override
					public void onClick(JiaxiquanInfo jxqInfo,int position) {
						if("0".equals(jxqInfo.getTransfer())){
							Intent intent = new Intent(mainActivity,BorrowListZXDActivity.class);
							startActivity(intent);
						}else if("1".equals(jxqInfo.getTransfer())){
							//转让加息券
							showJXQTransferEditDialog(jxqInfo);
						}
					}
				});
		pullToRefreshListView.setAdapter(mMyJXQListAdapter);
		initListeners();
	}

	private void initListeners() {
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 下拉刷新
						isLoadMore = false;
						pageNo = 1;
						handler.sendEmptyMessage(REQUEST_JXQ_LIST_WHAT);
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
								handler.sendEmptyMessage(REQUEST_JXQ_LIST_WHAT);
							}
						}, 1000L);

					}
				});
	}

	/**
	 * 加息券转让
	 */
	private void showJXQTransferEditDialog(final JiaxiquanInfo jxqInfo){
		View contentView = LayoutInflater.from(mainActivity).inflate(R.layout.jxq_transfer_edit_dialog_layout, null);
		final Button okBtn = (Button) contentView.findViewById(R.id.jxq_transfer_edit_dialog_okbtn);
		ImageView delBtn = (ImageView) contentView.findViewById(R.id.jxq_transfer_edit_dialog_delbtn);
		final EditText phoneET = (EditText) contentView.findViewById(R.id.jxq_transfer_edit_dialog_phone);
		final TextView promptTV = (TextView) contentView.findViewById(R.id.jxq_transfer_edit_dialog_prompt);
		AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity, R.style.Dialog_Transparent);  //先得到构造器
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkReceiverData(phoneET,promptTV,dialog,jxqInfo);
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
		okBtn.requestFocus();
		okBtn.requestFocusFromTouch();
		WindowManager windowManager = mainActivity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth()*6/7;
		dialog.getWindow().setAttributes(lp);
	}

	/**
	 * 是否将加息券转让给xxx
	 */
	private void showJXQTransferPromptDialog(final UserInfo userInfo,final JiaxiquanInfo jxqInfo){
		View contentView = LayoutInflater.from(mainActivity).inflate(R.layout.jxq_transfer_prompt_dialog_layout, null);
		final Button okBtn = (Button) contentView.findViewById(R.id.jxq_transfer_prompt_dialog_okbtn);
		ImageView delBtn = (ImageView) contentView.findViewById(R.id.jxq_transfer_prompt_dialog_delbtn);
		final TextView contentTV = (TextView) contentView.findViewById(R.id.jxq_transfer_prompt_dialog_content);
		if(userInfo.getReal_name() != null && !"".equals(userInfo.getReal_name())){
			contentTV.setText("确定把加息券转让给"+Util.hidRealName2(userInfo.getReal_name())+"，手机号："+Util.hidPhoneNum(userInfo.getPhone())+"?");
		}else{
			contentTV.setText("确定把加息券转让给"+"手机号："+Util.hidPhoneNum(userInfo.getPhone())+"?");
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity, R.style.Dialog_Transparent);  //先得到构造器
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				requestTransferAddinterest(userInfo,jxqInfo);
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
		WindowManager windowManager = mainActivity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth()*6/7;
		dialog.getWindow().setAttributes(lp);
	}

	/**
	 * 加息券转让成功
	 */
	private void showJXQTransferSucDialog(UserInfo userInfo,JiaxiquanInfo jxqInfo){
		View contentView = LayoutInflater.from(mainActivity).inflate(R.layout.jxq_transfer_suc_dialog_layout, null);
		final Button okBtn = (Button) contentView.findViewById(R.id.jxq_transfer_suc_dialog_okbtn);
		ImageView delBtn = (ImageView) contentView.findViewById(R.id.jxq_transfer_suc_dialog_delbtn);
		TextView receiverName = (TextView) contentView.findViewById(R.id.jxq_transfer_suc_dialog_receivername);
		TextView receiverPhone = (TextView) contentView.findViewById(R.id.jxq_transfer_suc_dialog_receiverphone);
		if(userInfo.getReal_name() != null && !"".equals(userInfo.getReal_name())){
			receiverName.setVisibility(View.VISIBLE);
			receiverName.setText("接收人姓名: "+Util.hidRealName2(userInfo.getReal_name()));
		}else{
			receiverName.setVisibility(View.GONE);
		}
		receiverPhone.setText("接收人手机号: "+Util.hidPhoneNum(userInfo.getPhone()));
		AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity, R.style.Dialog_Transparent);  //先得到构造器
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				mOnJXQNousedTransferSucListener.onSuccess();
			}
		});
		delBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				mOnJXQNousedTransferSucListener.onSuccess();
			}
		});
		//参数都设置完成了，创建并显示出来
		dialog.show();
		WindowManager windowManager = mainActivity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth()*6/7;
		dialog.getWindow().setAttributes(lp);
	}

	private void checkReceiverData(EditText phoneET,TextView promptTV,AlertDialog dialog,JiaxiquanInfo jxqInfo){
		if(phoneET == null || promptTV == null)
			return;
		String phone = phoneET.getEditableText().toString();
		if(Util.checkPhoneNumber(phone)){
			promptTV.setVisibility(View.GONE);
			requestSubUser(phone,SettingsManager.getUserId(mainActivity),promptTV,dialog,jxqInfo);
		}else{
			promptTV.setVisibility(View.VISIBLE);
			promptTV.setText("请输入正确的手机号");
		}
	}

	/**
	 * 转让加息券
	 * @param userInfo
	 * @param jxqInfo
	 */
	private void requestTransferAddinterest(final UserInfo userInfo,final JiaxiquanInfo jxqInfo){
		AsyncJXQTransfer transferTask = new AsyncJXQTransfer(mainActivity, userInfo.getId(), jxqInfo.getId(), new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						showJXQTransferSucDialog(userInfo,jxqInfo);
					}else{
						Util.toastLong(mainActivity,"此加息券已被转赠");
					}
				}else{
					Util.toastLong(mainActivity,"转让失败，请求数据异常");
				}
			}
		});
		transferTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 获取理财师的直接好友
	 * @param phone
	 * @param userId
	 */
	private void requestSubUser(final String phone,String userId,final TextView promptTV,final AlertDialog dialog,final JiaxiquanInfo jxqInfo){
		AsyncJXQTransferGetSubUser subUserTask = new AsyncJXQTransferGetSubUser(mainActivity, phone, userId, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						//是其直接好友
						UserInfo userInfo = baseInfo.getUserInfo();
						userInfo.setPhone(phone);
						dialog.dismiss();
						promptTV.setVisibility(View.GONE);
						showJXQTransferPromptDialog(userInfo,jxqInfo);
					}else if(resultCode == -1){
						//不是其直接好友
						promptTV.setVisibility(View.VISIBLE);
						promptTV.setText("此手机号不是您的直接好友，不能转让");
					}else{
						Util.toastLong(mainActivity,baseInfo.getMsg());
					}
				}else{
					Util.toastLong(mainActivity,"请求数据异常");
				}
			}
		});
		subUserTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	private void requestJXQList(String userId, String useStatus) {
		if (isFirst) {
			mainActivity.loadingDialog.show();
		}
		isFirst = false;
		AsyncJXQPageInfo redbagTask = new AsyncJXQPageInfo(mainActivity, userId,useStatus,
				String.valueOf(pageNo),String.valueOf(pageSize), new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mainActivity.loadingDialog.isShowing()) {
							mainActivity.loadingDialog.dismiss();
						}

						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler.obtainMessage(REQUEST_JXQ_LIST_SUCCESS);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							} else {
								Message msg = handler.obtainMessage(REQUEST_JXQ_LIST_FAILE);
								msg.obj = baseInfo.getError();
								handler.sendMessage(msg);
							}
						} else {
							Message msg = handler
									.obtainMessage(REQUEST_JXQ_LIST_FAILE);
							handler.sendMessage(msg);
						}
					}
				});
		redbagTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
