package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncAPIQuery;
import com.ylfcf.ppp.entity.AppInfo;
import com.ylfcf.ppp.inter.Inter.OnApiQueryBack;
import com.ylfcf.ppp.ui.ArticleListActivity;
import com.ylfcf.ppp.ui.BrandIntroActivity;
import com.ylfcf.ppp.ui.CommQuesActivity;
import com.ylfcf.ppp.ui.MainFragmentActivity;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnDownLoadListener;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnMoreFragmentLogoutListener;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.view.UpdatePopupwindow;


/**
 * 更多
 * @author Administrator
 *
 */
public class MoreFragment extends BaseFragment implements OnClickListener{
	private static final String className = "MoreFragment";
	private MainFragmentActivity mainActivity;
	
	private LinearLayout ppjsLayout;//品牌介绍
	private LinearLayout xwggLayout;//新闻公告
	private LinearLayout cjwtLayout;//常见问题
	private LinearLayout kfrxLayout;//客服热线
	private LinearLayout jcxbbLayout;//检查新版本
	private LinearLayout mainlayout;
	private ToggleButton msgTenderBtn;
	private TextView versionText;
	
	private Button logoutBtn;
	
	private View topLayout;
	private TextView topTitle;
	private LinearLayout topbarLeftLayout;
	private View rootView;
	
	/**
	 * 创建当前Fragment的实例对象
	 * @param position
	 * @return
	 */
	static OnMoreFragmentLogoutListener logoutSucListener;
	public static Fragment newInstance(int position,OnMoreFragmentLogoutListener logoutListener) {
		MoreFragment f = new MoreFragment();
		logoutSucListener = logoutListener;
		Bundle args = new Bundle();
		args.putInt("num", position);
		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mainActivity = (MainFragmentActivity) getActivity();
		if(rootView==null){
            rootView=inflater.inflate(R.layout.more_fragment, null);
            findViews(rootView);
        }
		//缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        } 
        return rootView;
	}

	private void findViews(View view){
		topLayout = view.findViewById(R.id.more_fragment_toplayout);
		topbarLeftLayout = (LinearLayout) topLayout.findViewById(R.id.common_topbar_left_layout);
		topbarLeftLayout.setVisibility(View.GONE);
		topTitle = (TextView)topLayout.findViewById(R.id.common_page_title);
		topTitle.setText("更多");
		
		mainlayout = (LinearLayout)view.findViewById(R.id.more_fragment_mainlayout);
		ppjsLayout = (LinearLayout)view.findViewById(R.id.more_ppjs_layout);
		ppjsLayout.setOnClickListener(this);
		xwggLayout = (LinearLayout)view.findViewById(R.id.more_xwgg_layout);
		xwggLayout.setOnClickListener(this);
		cjwtLayout = (LinearLayout)view.findViewById(R.id.more_cjwt_layout);
		cjwtLayout.setOnClickListener(this);
		kfrxLayout = (LinearLayout)view.findViewById(R.id.more_kfrx_layout);
		kfrxLayout.setOnClickListener(this);
		jcxbbLayout = (LinearLayout)view.findViewById(R.id.more_jcxbb_layout);
		jcxbbLayout.setOnClickListener(this);
		logoutBtn = (Button)view.findViewById(R.id.more_fragment_logout_btn);
		logoutBtn.setOnClickListener(this);
		versionText = (TextView)view.findViewById(R.id.more_fragment_versioncode);
		versionText.setText("V"+Util.getVersionName(mainActivity));
		boolean sendMsgFlag = SettingsManager.getMsgSendFlag(mainActivity.getApplicationContext());
		msgTenderBtn = (ToggleButton) view.findViewById(R.id.morefragment_msgtender_toggle);
		if(sendMsgFlag){
			msgTenderBtn.setChecked(true);
		}else{
			msgTenderBtn.setChecked(false);
		}
		msgTenderBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					//打开
					mainActivity.mPushAgent.enable();
					SettingsManager.setMsgSendFlag(mainActivity.getApplicationContext(), true);
				}else{
					//关闭
					mainActivity.mPushAgent.disable();
					SettingsManager.setMsgSendFlag(mainActivity.getApplicationContext(), false);
				}
			}
		});
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		YLFLogger.d("MoreFragment ------onResume()");
		UMengStatistics.statisticsOnPageStart(className);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
			if(mainActivity == null){
				mainActivity = (MainFragmentActivity) getActivity();
			}
			String userId = SettingsManager.getUserId(mainActivity.getApplicationContext());
			if(userId != null && !"".equals(userId)){
				if(logoutBtn != null){
					logoutBtn.setVisibility(View.VISIBLE);
				}
			}else{
				if(logoutBtn != null){
					logoutBtn.setVisibility(View.GONE);
				}
			}
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_ppjs_layout:
			Intent intent = new Intent(mainActivity,BrandIntroActivity.class);
			startActivity(intent);
			break;
		case R.id.more_xwgg_layout:
			//新闻公告
			Intent intentAr = new Intent(mainActivity,ArticleListActivity.class);
			startActivity(intentAr);
			break;
		case R.id.more_cjwt_layout:
			Intent intentCommQues = new Intent(mainActivity,CommQuesActivity.class);
			startActivity(intentCommQues);
			break;
		case R.id.more_kfrx_layout:
			contactUs("4001501568");
			break;
		case R.id.more_jcxbb_layout:
			checkVersion(Util.getClientVersion(mainActivity));
			break;
		case R.id.more_fragment_logout_btn:
			showLogoutDialog();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 用户退出
	 */
	private void logout(){
		if(mainActivity.loadingDialog != null){
			mainActivity.loadingDialog.show();
			SettingsManager.setUser(mainActivity,"");
			SettingsManager.setLoginPassword(mainActivity,"",true);
			SettingsManager.setUserId(mainActivity,"");
			SettingsManager.setUserName(mainActivity,"");
			SettingsManager.setUserRegTime(mainActivity, "");
			SettingsManager.setUserType(mainActivity, "");
			SettingsManager.setCompPhone(mainActivity, "");
			
			new Handler().postDelayed(new Runnable(){
				@Override
				public void run() {
					mainActivity.loadingDialog.dismiss();
					logoutBtn.setVisibility(View.GONE);
					Util.toastShort(mainActivity, "用户已退出");
					logoutSucListener.onLogoutSuc();
				}
			}, 1000L);
		}
	}
	
	private void contactUs(String phoneNumber){
		Intent intent = new Intent(Intent.ACTION_DIAL);
	    intent.setData(Uri.parse("tel:" + phoneNumber));
	    if (intent.resolveActivity(mainActivity.getPackageManager()) != null) {
	        startActivity(intent);
	    }
	}
	/**
	 * 登录成功回调
	 * @author Administrator
	 *
	 */
	public interface OnLoginSuccessListener{
		public void onLoginSuccess();
	}
	
	/**
	 * 检查是否有新版本
	 */
	private void checkVersion(int versionCode){
		if(mainActivity.loadingDialog != null){
			mainActivity.loadingDialog.show();
		}
		AsyncAPIQuery apiQueryTask = new AsyncAPIQuery(mainActivity, versionCode, new OnApiQueryBack() {
			@Override
			public void back(AppInfo info) {
				if(mainActivity.loadingDialog != null && mainActivity.loadingDialog.isShowing()){
					mainActivity.loadingDialog.dismiss();
				}
				if(info != null){
					String flag = info.getDo_update();
					if("true".equals(flag)){
						//需要升级
						showUpdateDialog(info);
					}else{
						//不需要升级
						Util.toastLong(mainActivity, "已是最新版本");
					}
				}else{
				}
			}
		});
		apiQueryTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	
	private void showUpdateDialog(AppInfo info) {
		View popView = LayoutInflater.from(mainActivity).inflate(
				R.layout.update_window_layout, null);
		int[] screen = SettingsManager.getScreenDispaly(mainActivity);
		int width = screen[0] * 4 / 5;
		int height = screen[1] * 3 / 5 + 20;
		UpdatePopupwindow popwindow = new UpdatePopupwindow(mainActivity,
				popView, width, height,info,mainActivity.downManager,new OnDownLoadListener() {
					@Override
					public void onDownLoad(long lastDownId) {
						mainActivity.registerMessageReceiver();
					}
				});
		popwindow.show(mainlayout);
	}
	
	/**
	 * 退出登录的Dialog
	 */
	private void showLogoutDialog(){
		View contentView = LayoutInflater.from(mainActivity).inflate(R.layout.logout_dialog, null);
		final Button sureBtn = (Button) contentView.findViewById(R.id.logout_dialog_sure_btn);
		final Button cancelBtn = (Button) contentView.findViewById(R.id.logout_dialog_cancel_btn);
		AlertDialog.Builder builder=new AlertDialog.Builder(mainActivity, R.style.Dialog_Transparent);  //先得到构造器  
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				logout();
			}
		});
        cancelBtn.setOnClickListener(new OnClickListener() {
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
}
