package com.ylfcf.ppp.view;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.AppInfo;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnDownLoadListener;
import com.ylfcf.ppp.util.MarketUtils;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;

/**
 * 版本更新的window
 * 
 * @author jianbing
 * 
 */
public class UpdatePopupwindow extends PopupWindow implements OnClickListener {
	private Activity context;
	private Button okBtn;
	private Button cancelBtn;
	private TextView updateContent;
	private View line;

	private int width;
	private int height;
	private RelativeLayout mainLayout;
	private AppInfo info;
	private WindowManager.LayoutParams lp = null;

	DownloadManager downManager;
	OnDownLoadListener ondownloadListener;
	public UpdatePopupwindow(Context context) {
		super(context);
	}

	public UpdatePopupwindow(Context context, View convertView, int width,
			int height, AppInfo info, DownloadManager downloadManager,OnDownLoadListener onDownLoadListener) {
		super(convertView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.context = (Activity) context;
		this.downManager = downloadManager;
		this.width = width;
		this.height = height;
		this.info = info;
		this.ondownloadListener = onDownLoadListener;
		findViews(convertView);
	}

	private void findViews(View popView) {
		lp = context.getWindow().getAttributes();
		lp.alpha = 0.4f;
		context.getWindow().setAttributes(lp);

		mainLayout = (RelativeLayout) popView
				.findViewById(R.id.update_window_mainlayout);
		mainLayout.setLayoutParams(new RelativeLayout.LayoutParams(width,
				height));
		okBtn = (Button) popView.findViewById(R.id.update_window_sure_btn);
		okBtn.setOnClickListener(this);
		cancelBtn = (Button) popView
				.findViewById(R.id.update_window_cancel_btn);
		cancelBtn.setOnClickListener(this);
		updateContent = (TextView) popView
				.findViewById(R.id.update_window_content);
		updateContent.setText(info.getBrief());
		line = popView.findViewById(R.id.update_window_line);
		if ("1".equals(info.getForce_update())) {
			// 强制更新
			cancelBtn.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
		} else {
			cancelBtn.setVisibility(View.VISIBLE);
			line.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
		lp.alpha = 1.0f;
		context.getWindow().setAttributes(lp);
	}

	@SuppressWarnings("deprecation")
	public void show(View parentView) {
		if ("1".equals(info.getForce_update())) {

		} else {
			this.setBackgroundDrawable(new BitmapDrawable()); // 使得返回键有效
		}
		context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); 
		this.setAnimationStyle(R.style.rechargeMsgPopwindowStyle);
		this.setOutsideTouchable(false);
		this.setFocusable(true);
		this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.update_window_cancel_btn:
			this.dismiss();
			break;
		case R.id.update_window_sure_btn:
			requestDownloadApk();
			break;
		default:
			break;
		}
	}

	private void requestDownloadApk() {
		try {
			MarketUtils.launchAppDetail(context, "com.ylfcf.ppp", "");//跳转应用商店
		} catch (Exception e) {
			startDownloadAPK();
		}
	}
	
	/**
	 * 从官网下载apk
	 */
	private void startDownloadAPK(){
		long myDwonloadID = SettingsManager.getLong(context, SettingsManager.DOWNLOAD_APK_NUM, 0);
        Intent install = new Intent(Intent.ACTION_VIEW);
		Uri downloadFileUri = downManager
				.getUriForDownloadedFile(myDwonloadID);
		if(downloadFileUri != null){
			install.setDataAndType(downloadFileUri,
					"application/vnd.android.package-archive");
			install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(install);
			return;
		}
		
		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(info.getNew_version_url()));
		// 设置在什么网络情况下进行下载
//		request.setAllowedNetworkTypes(Request.NETWORK_WIFI);
//		request.setAllowedNetworkTypes(Request.NETWORK_MOBILE);
		// 设置通知栏标题
		request.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
		request.setTitle("元立方理财");
		request.setDescription("正在下载...");
		request.setAllowedOverRoaming(false);
		MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(info.getNew_version_url()));
        request.setMimeType(mimeString);
		// 设置文件存放目录
		if(Build.VERSION.SDK_INT >= 23){
			request.setDestinationInExternalPublicDir("/apk/", "ylfcf.apk");//6.0以后的系统上要自定义下载目录，否则不弹出升级提示框。
		}else{
			request.setDestinationInExternalFilesDir(context,
					Environment.DIRECTORY_DOWNLOADS, "ylfcf");
		}
		
//		int idx = info.getNew_version_url().lastIndexOf("/");  
//        String apkName = info.getNew_version_url().substring(idx+1);  
		long id = downManager.enqueue(request);// 下载的服务进程号
		SettingsManager.setLong(context, SettingsManager.DOWNLOAD_APK_NUM, id);
		if ("1".equals(info.getForce_update())) { 
			ondownloadListener.onDownLoad(id);//显示下载的进度条
		} else {
		}
		dismiss();
	}
}
