package com.ylfcf.ppp.ui;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.ylfcf.ppp.Permission.BasePermissionActivity;
import com.ylfcf.ppp.Permission.PermissionCallBackM;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.MainFragmentAdapter;
import com.ylfcf.ppp.async.AsyncAPIQuery;
import com.ylfcf.ppp.async.AsyncAddPhoneInfo;
import com.ylfcf.ppp.async.AsyncPopBanner;
import com.ylfcf.ppp.async.AsyncProductPageInfo;
import com.ylfcf.ppp.entity.AppInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.PopBannerInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.entity.YXBProductInfo;
import com.ylfcf.ppp.entity.YXBProductLogInfo;
import com.ylfcf.ppp.inter.Inter.OnApiQueryBack;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.receiver.DownLoadCompleteReceiver;
import com.ylfcf.ppp.receiver.NetworkStatusReceiver;
import com.ylfcf.ppp.receiver.NetworkStatusReceiver.NetworkStateListener;
import com.ylfcf.ppp.util.Constants;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.view.CommonBannerPopwindow;
import com.ylfcf.ppp.view.UpdatePopupwindow;
import com.ylfcf.ppp.widget.LoadingDialog;
import com.ylfcf.ppp.widget.NavigationBarView;
import com.ylfcf.ppp.widget.NoScrollViewPager;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 主页面
 * 首页（FirstPageFragment）理财（LicaiFragment）我的（UserFragment）更多（MoreFragment）四个页面导航
 * 版本升级接口
 * 网络状态监听
 * @author Administrator
 *
 */
public class MainFragmentActivity extends BasePermissionActivity implements OnClickListener, NetworkStateListener{
	public static final String MESSAGE_RECEIVED_ACTION = "com.ylfcf.ppp.MESSAGE_RECEIVED_ACTION";

	private static final int DOWNLOAD_SUCCESS = 2105;
	private static final int RQ_STORAGE_PERM = 123;//存储权限请求码

	private static final int REQUEST_BANNERPOP_WHAT = 2106;
	
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	private MessageReceiver mMessageReceiver;
	private NetworkStatusReceiver mNetworkStatusReceiver;
	
	private NoScrollViewPager viewpager;
	private MainFragmentAdapter fragmentAdapter = null;
	public FragmentManager fragmentManager = null;
	
	private NavigationBarView mNavigationBarView;

	public RelativeLayout mainLayout;
	
	public DownloadManager downManager ;
	public DownLoadCompleteReceiver downloadReceiver;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	public static boolean isForeground = false;
	private boolean isFirst = true;
	public LoadingDialog loadingDialog;
	
	boolean hasTask = false;
	boolean isExit = false;
	Timer tExit = new Timer(); 
	TimerTask task = new TimerTask() {
		public void run() {
			isExit = false;
			hasTask = true;
		}
	};
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case DOWNLOAD_SUCCESS:
				if(downloadBtn != null){
					downloadBtn.setText("立即安装");
					downloadBtn.setEnabled(true);
				}
				break;
			case REQUEST_BANNERPOP_WHAT:
				requestPopBanner();
				break;
			default:
				break;
			}
		}
	};
	
	private int page = 0;
	private int pageSize =10;
	public static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
	
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}
	
	/**
	 * 注册网络监听广播
	 */
	public void registerNetStatusReceiver(){
		mNetworkStatusReceiver = new NetworkStatusReceiver(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.ACTION_NET_CHANGE);
		filter.addAction(Constants.ACTION_NET_WIFI_STATE_CHANGED);
		registerReceiver(mNetworkStatusReceiver, filter);
	}
	
	/**
	 * 下载监听
	 */
	public void downloadChangeObserver(){
		DownloadChangeObserver downloadObserver = new DownloadChangeObserver(null);  
        getContentResolver().registerContentObserver(CONTENT_URI, true, downloadObserver); 
	}
	
	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
              String messge = intent.getStringExtra(KEY_MESSAGE);
              String extras = intent.getStringExtra(KEY_EXTRAS);
              StringBuilder showMsg = new StringBuilder();
              showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
              if (!extras.isEmpty()) {
            	  showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
              }
			}
		}
	}
	long lastDownloadId = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_fragment_activity);
		/** 设置是否对日志信息进行加密, 默认false(不加密). */
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
			MobclickAgent.enableEncrypt(true);//6.0.0版本及以后
		}else{
//			AnalyticsConfig.enableEncrypt(true);//6.0.0版本以前
		}
		this.loadingDialog = mLoadingDialog;
		downManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		filter.addAction("android.intent.action.PACKAGE_REPLACED");
		downloadReceiver = new DownLoadCompleteReceiver();
		registerReceiver(downloadReceiver, filter);
		registerMessageReceiver();
		registerNetStatusReceiver();
		YLFLogger.d("手机型号："+ android.os.Build.MODEL + "\n" + "SDK版本：" + 
				android.os.Build.VERSION.SDK + "\n系统版本号：" + android.os.Build.VERSION.RELEASE);
		fragmentManager = getSupportFragmentManager();
		
		float scale = getResources().getDisplayMetrics().density;
		YLFLogger.d("本机的像素密度：-----"+scale);
		findViews();

		//检查版本更新
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				requestAPIQuery(Util.getClientVersion(MainFragmentActivity.this));
			}
		}, 2000L);
		
		String userId = SettingsManager.getUserId(getApplicationContext());
		String phone = SettingsManager.getUser(getApplicationContext());
		if(!userId.isEmpty() && !phone.isEmpty()){
			addPhoneInfo(userId, phone, "", "");
		}
		requestProductPageInfo("", "发布","未满标","是","","");//请求产品列表
		downloadChangeObserver();
	}

	class DownloadChangeObserver extends ContentObserver {
		public DownloadChangeObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			queryDownloadStatus();
		}
	}

	/**
	 * 动态请求存储权限
	 */
	private void requestStoragePermission(){
		requestPermission(RQ_STORAGE_PERM, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                "本次操作需要系统存储权限，否则无法正常进行。", new PermissionCallBackM() {
                    @Override
                    public void onPermissionGrantedM(int requestCode, String... perms) {
                        startDownloadAPK();
                    }
                    @Override
                    public void onPermissionDeniedM(int requestCode, String... perms) {
                        Util.toastLong(MainFragmentActivity.this,"用户拒绝了存储权限。");
                    }
                });
	}

	/**
	 * 监听下载状态
	 */
	private void queryDownloadStatus() {
		lastDownloadId = SettingsManager.getLong(getApplicationContext(), SettingsManager.DOWNLOAD_APK_NUM, 0);
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(lastDownloadId);
		Cursor c = downManager.query(query);
		if (c != null && c.moveToFirst()) {
			int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

			int reasonIdx = c.getColumnIndex(DownloadManager.COLUMN_REASON);
			int titleIdx = c.getColumnIndex(DownloadManager.COLUMN_TITLE);
			int fileSizeIdx = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
			int bytesDLIdx = c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
			String title = c.getString(titleIdx);
			int fileSize = c.getInt(fileSizeIdx);
			int bytesDL = c.getInt(bytesDLIdx);

			// Translate the pause reason to friendly text.
			int reason = c.getInt(reasonIdx);
			StringBuilder sb = new StringBuilder();
			sb.append(title).append("\n");
			sb.append("Downloaded _____________________________________________________________").append(bytesDL).append(" / ").append(fileSize);
			// Display the status
			YLFLogger.d("tag", sb.toString());
			switch (status) {
			case DownloadManager.STATUS_PAUSED:
				//暂停
				YLFLogger.d("tag", "STATUS_PAUSED");
			case DownloadManager.STATUS_PENDING:
				//等待下载
				YLFLogger.d("tag", "STATUS_PENDING");
			case DownloadManager.STATUS_RUNNING:
				// 正在下载，不做任何事情
				YLFLogger.d("tag", "STATUS_RUNNING");
				if(downloadPs != null){
					downloadPs.setProgress(bytesDL*100/fileSize);
				}
				if(downloadPsText != null){
					downloadPsText.setText(Util.double2PointDouble(bytesDL/(1024.0*1024))+"M/"+Util.double2PointDouble(fileSize/(1024.0*1024)) + "M");
				}
				break;
			case DownloadManager.STATUS_SUCCESSFUL:
				// 完成
				YLFLogger.d("tag", "下载完成from mainfragmentactivity");
				Message msg = handler.obtainMessage(DOWNLOAD_SUCCESS);
				handler.sendMessage(msg);
				if(downloadPs != null){
					downloadPs.setProgress(bytesDL*100/fileSize);
				}
				if(downloadPsText != null){
					downloadPsText.setText(Util.double2PointDouble(bytesDL/(1024.0*1024))+"M/"+Util.double2PointDouble(fileSize/(1024.0*1024)) + "M");
				}
				break;
			case DownloadManager.STATUS_FAILED:
				// 清除已下载的内容，重新下载
				YLFLogger.d("tag", "STATUS_FAILED");
				downManager.remove(lastDownloadId);
				break;
			}
		}
	}
	
	/**
	 * 首页弹窗
	 */
	private void showCommonBannerPopwindow(PopBannerInfo popInfo){
		View popView = LayoutInflater.from(this).inflate(
				R.layout.common_banner_popwindow_layout, null);
		int[] screen = SettingsManager.getScreenDispaly(this);
		int width = screen[0];
		int height = screen[1];
		CommonBannerPopwindow popwindow = new CommonBannerPopwindow(this,
				popView, width, height,popInfo);
		popwindow.show(mainLayout);
	}
	
	@Override
	protected void onResume() {	
		super.onResume();
		UMengStatistics.statisticsResume(this);//友盟统计时长
		isForeground = true;
		isExit = false;
		YLFLogger.d("MainFragmentActivity-OnResume()");
		boolean productListFlag = SettingsManager.getMainProductListFlag(getApplicationContext());
		boolean accountFlag = SettingsManager.getMainAccountFlag(getApplicationContext());
		boolean firstpageFlag = SettingsManager.getMainFirstpageFlag(getApplicationContext());
		if(firstpageFlag){
			setViewPagerCurPostion(0);
			SettingsManager.setMainFirstpageFlag(getApplicationContext(), false);
		}
		if(productListFlag){
			setViewPagerCurPostion(1);
			SettingsManager.setMainProductListFlag(getApplicationContext(), false);
		}
		if(accountFlag){
			setViewPagerCurPostion(2);
			SettingsManager.setMainAccountFlag(getApplicationContext(), false);
		}
		isFirst = false;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		UMengStatistics.statisticsPause(this);//友盟统计时长
		isForeground = false;
	}

	private void findViews(){
		mNavigationBarView = (NavigationBarView) findViewById(R.id.main_navigation_bar);
		mainLayout = (RelativeLayout)findViewById(R.id.main_fragment_activity_mainlayout);
		viewpager = (NoScrollViewPager)findViewById(R.id.main_fragment_activity_viewpager);
		viewpager.setOffscreenPageLimit(3);//首页的4个fragment数据都保留
		viewpager.setScanScroll(false);//禁止滑动
		mNavigationBarView.setViewPager(viewpager);
		fragmentAdapter = new MainFragmentAdapter(fragmentManager,new OnFirstPageZXDOnClickListener() {
			@Override
			public void back() {
				setViewPagerCurPostion(1);
			}
		},new OnUserFragmentLoginSucListener(){
			@Override
			public void onLoginSuc(UserInfo info) {
				//产品登录成功的回调
			}
		},new OnMoreFragmentLogoutListener(){
			@Override
			public void onLogoutSuc() {
				//退出成功的回调
				fragmentAdapter.getItem(2).setUserVisibleHint(true);
			}
		},new OnFirstPageHYTJOnClickListener(){

			@Override
			public void hytjOnClick() {
				setViewPagerCurPostion(2);
			}
		});
		viewpager.setAdapter(fragmentAdapter);
	}

	public void setViewPagerCurPostion(int position){
		if(mNavigationBarView != null){
			mNavigationBarView.setViewPagerCurrentPosition(position);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(isExit == false){
				isExit = true;
				Util.toastShort(MainFragmentActivity.this, "再按一次退出应用");
				if(!hasTask){
					tExit.schedule(task, 3000L,3000L);
				}
			}else{
				if(tExit != null){
					tExit.cancel();
				}
				finish();
				UMengStatistics.onKillProcess(this);//保存友盟统计数据
				android.os.Process.killProcess(android.os.Process.myPid());//杀进程
			}
		}
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mMessageReceiver);
		unregisterReceiver(mNetworkStatusReceiver);
		unregisterReceiver(downloadReceiver);
		ImageLoader.getInstance().clearMemoryCache();
		handler.removeCallbacksAndMessages(null);
	}

	/**
	 * 获取版本信息以及升级包的URL
	 * @param 
	 * @author waggoner.wang
	 */
	private AppInfo mAppInfo;
	private void requestAPIQuery(int versionCode){
		AsyncAPIQuery apiQueryTask = new AsyncAPIQuery(MainFragmentActivity.this,
				versionCode, new OnApiQueryBack() {
			@Override
			public void back(AppInfo info) {
				if(info != null){
					String flag = info.getDo_update();
					if("true".equals(flag)){
						//需要升级
						showUpdateDialog(info);
                        mAppInfo = info;
					}else{
						//不需要升级
						handler.sendEmptyMessage(REQUEST_BANNERPOP_WHAT);
					}
				}else{
					handler.sendEmptyMessage(REQUEST_BANNERPOP_WHAT);
				}
			}
		});
		apiQueryTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 将手机信息加入到后台数据库
	 * @param userId
	 * @param phone
	 * @param location
	 * @param contact
	 */
	private void addPhoneInfo(String userId,String phone,String location,String contact){
		String phoneModel = android.os.Build.MODEL;
		String sdkVersion = android.os.Build.VERSION.SDK;
		String systemVersion = android.os.Build.VERSION.RELEASE;
		AsyncAddPhoneInfo addPhoneInfoTask = new AsyncAddPhoneInfo(MainFragmentActivity.this, userId, phone, phoneModel, 
				sdkVersion, systemVersion, "Android", location, contact, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						
					}
				});
		addPhoneInfoTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	private void showUpdateDialog(final AppInfo info) {
		View popView = LayoutInflater.from(this).inflate(
				R.layout.update_window_layout, null);
		int[] screen = SettingsManager.getScreenDispaly(MainFragmentActivity.this);
		int width = screen[0] * 4 / 5;
		int height = screen[1] * 3 / 5 + 20;
		UpdatePopupwindow popwindow = new UpdatePopupwindow(MainFragmentActivity.this,
				popView, width, height,info,downManager,new OnDownLoadListener() {
					@Override
					public void onDownLoad(long lastDownId) {
                        requestStoragePermission();
					}
				},new OnUpdateWindowDismiss(){
			@Override
			public void onDismiss() {
				//手动关闭更新弹窗
				handler.sendEmptyMessage(REQUEST_BANNERPOP_WHAT);
			}
		});
		popwindow.show(mainLayout);
	}
	
	ProgressBar downloadPs = null;
	TextView downloadPsText = null;
	Button downloadBtn = null;
	private void showDownloadDialog(){
		View contentView = LayoutInflater.from(MainFragmentActivity.this).inflate(R.layout.download_progress_dialog, null);
		downloadPs = (ProgressBar) contentView.findViewById(R.id.download_progress_dialog_ps);
		downloadPsText = (TextView) contentView.findViewById(R.id.download_progress_dialog_pstext);
		downloadBtn = (Button) contentView.findViewById(R.id.download_progress_dialog_btn);
		downloadBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				long myDwonloadID = SettingsManager.getLong(getApplicationContext(), SettingsManager.DOWNLOAD_APK_NUM, 0);
				String serviceString = Context.DOWNLOAD_SERVICE;
		        DownloadManager dManager = (DownloadManager) getSystemService(serviceString);
		        try {
		        	Intent install = new Intent(Intent.ACTION_VIEW);
					Uri downloadFileUri = dManager
							.getUriForDownloadedFile(myDwonloadID);
					install.setDataAndType(downloadFileUri,
							"application/vnd.android.package-archive");
					install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(install);
				} catch (Exception e) {
					e.printStackTrace();
					YLFLogger.d("——————————————————————————安装不成功————————————————————————————————");
				}
			}
		});
		AlertDialog.Builder builder=new AlertDialog.Builder(MainFragmentActivity.this, R.style.Dialog_Transparent);  //先得到构造器  
        builder.setView(contentView);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        //参数都设置完成了，创建并显示出来  
        dialog.show();  
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*6/7;
        dialog.getWindow().setAttributes(lp);
	}

	/**
	 * 产品列表
	 */
	private void requestProductPageInfo(String borrowType,String borrowStatus,
			String moneyStatus,String isShow,String isWap,String plan){
		AsyncProductPageInfo productTask = new AsyncProductPageInfo(MainFragmentActivity.this, 
				String.valueOf(page), String.valueOf(pageSize),borrowType,borrowStatus,
				moneyStatus,isShow,isWap,plan,false,"2","2",new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								if(onRequestBorrowListListener2 != null){
									onRequestBorrowListListener2.back(baseInfo);
								}
								if(onRequestBorrowListListener1 != null){
									onRequestBorrowListListener1.back(baseInfo);
								}
							}else{
							}
						}else{
						}
					}
				});
		productTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 首页弹窗
	 */
	private void requestPopBanner(){
		AsyncPopBanner popTask = new AsyncPopBanner(MainFragmentActivity.this, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						PopBannerInfo info = baseInfo.getmPopBannerInfo();
						showCommonBannerPopwindow(info);
					}
				}else{
				}
			}
		});
		popTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	OnRequestBorrowListListener onRequestBorrowListListener1;//首页回调
	OnRequestBorrowListListener onRequestBorrowListListener2;//理财列表页面回调
	public void setOnRequestBorrowListener(OnRequestBorrowListListener listener1,OnRequestBorrowListListener listener2){
		if(listener1 != null){
			this.onRequestBorrowListListener1 = listener1;
		}
		if(listener2 != null){
			this.onRequestBorrowListListener2 = listener2;
		}
	}
	
	public interface OnRequestBorrowListListener{
		void back(BaseInfo baseInfo);
	}
	
	OnYXBDataListener mOnYXBDataListener1;//首页元信宝位置的回调
	OnYXBDataListener mOnYXBDataListener2;//理财元信宝位置的回调
	public void setOnYXBDataListener(OnYXBDataListener listener1,OnYXBDataListener listener2){
		if(listener1 != null){
			this.mOnYXBDataListener1 = listener1;
		}
		if(listener2 != null){
			this.mOnYXBDataListener2 = listener2;
		}
	}
	
	public interface OnYXBDataListener{
		void back(YXBProductInfo mYXBProductInfo,YXBProductLogInfo mYXBProductLogInfo);
	}

	/**
	 * 首页点击好友推荐按钮
	 */
	public interface OnFirstPageHYTJOnClickListener{
		void hytjOnClick();
	}

	/**
	 * 点击首页的政信贷跳到理财页面
	 * @author Administrator
	 *
	 */
	public interface OnFirstPageZXDOnClickListener{
		void back();
	}
	
	/**
	 * userfragment页面登录成功后的回调
	 * @author Mr.liu
	 *
	 */
	public interface OnUserFragmentLoginSucListener{
		void onLoginSuc(UserInfo userInfo);
	}
	
	/**
	 * morefragment页面退出登录
	 * @author Mr.liu
	 *
	 */
	public interface OnMoreFragmentLogoutListener{
		void onLogoutSuc();
	}
	
	OnNetStatusChangeListener onNetStatusChangeListener1;//首页fragment
	OnNetStatusChangeListener onNetStatusChangeListener2;//理财页面fragment
	public void setOnNetStatusChangeListener(OnNetStatusChangeListener listener1,OnNetStatusChangeListener listener2){
		if(listener1 != null){
			this.onNetStatusChangeListener1 = listener1;
		}
		if(listener2 != null){
			this.onNetStatusChangeListener2 = listener2;
		}
	}
	
	public interface OnDownLoadListener{
		void onDownLoad(long lastDownId);
	}

	/**
	 * 升级弹窗手动取消
	 */
	public interface OnUpdateWindowDismiss{
		void onDismiss();
	}

	public interface OnNetStatusChangeListener{
		void onNetStatusChange(boolean enabled);
	}
	
	/**
	 * 网络连接
	 */
	@Override
	public void onNetworkEnabled() {
		YLFLogger.d("---------------------------------------网络连接------------------------------------");
		if(onNetStatusChangeListener1 != null){
			onNetStatusChangeListener1.onNetStatusChange(true);
		}
		if(onNetStatusChangeListener2 != null){
			onNetStatusChangeListener2.onNetStatusChange(true);
		}
		requestProductPageInfo("", "发布","未满标","是","","");//请求产品列表
	}

	/**
	 * 网络断开
	 */
	@Override
	public void onNetworkDisabled() {
		YLFLogger.d("---------------------------------------网络断开------------------------------------");
		Util.toastLong(this, "网络已断开");
		if(onNetStatusChangeListener1 != null){
			onNetStatusChangeListener1.onNetStatusChange(false);
		}
		if(onNetStatusChangeListener2 != null){
			onNetStatusChangeListener2.onNetStatusChange(false);
		}
	}

    /**
     * 从官网下载apk
     */
    public void startDownloadAPK(){
        long myDwonloadID = SettingsManager.getLong(MainFragmentActivity.this, SettingsManager.DOWNLOAD_APK_NUM, 0);
        Intent install = new Intent(Intent.ACTION_VIEW);
        Uri downloadFileUri = downManager
                .getUriForDownloadedFile(myDwonloadID);
        if(downloadFileUri != null){
            install.setDataAndType(downloadFileUri,
                    "application/vnd.android.package-archive");
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(install);
            return;
        }

        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(mAppInfo.getNew_version_url()));
        // 设置在什么网络情况下进行下载
//		request.setAllowedNetworkTypes(Request.NETWORK_WIFI);
//		request.setAllowedNetworkTypes(Request.NETWORK_MOBILE);
        // 设置通知栏标题
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("元立方理财");
        request.setDescription("正在下载...");
        request.setAllowedOverRoaming(false);
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(mAppInfo.getNew_version_url()));
        request.setMimeType(mimeString);
        // 设置文件存放目录
        if(Build.VERSION.SDK_INT >= 23){
            request.setDestinationInExternalPublicDir("/apk/", "ylfcf.apk");//6.0以后的系统上要自定义下载目录，否则不弹出升级提示框。
        }else{
            request.setDestinationInExternalFilesDir(MainFragmentActivity.this,
                    Environment.DIRECTORY_DOWNLOADS, "ylfcf");
        }

//		int idx = info.getNew_version_url().lastIndexOf("/");
//        String apkName = info.getNew_version_url().substring(idx+1);
        long id = downManager.enqueue(request);// 下载的服务进程号
        SettingsManager.setLong(MainFragmentActivity.this, SettingsManager.DOWNLOAD_APK_NUM, id);
        if ("1".equals(mAppInfo.getForce_update())) {
//            ondownloadListener.onDownLoad(id);//显示下载的进度条
            showDownloadDialog();
        } else {

        }
//        dismiss();
    }
}
