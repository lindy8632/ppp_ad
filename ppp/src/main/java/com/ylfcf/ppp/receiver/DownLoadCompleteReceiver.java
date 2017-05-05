package com.ylfcf.ppp.receiver;

import java.io.File;

import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.webkit.MimeTypeMap;

/**
 * 下载更新广播
 * @author Administrator
 *
 */
public class DownLoadCompleteReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
			long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			String serviceString = Context.DOWNLOAD_SERVICE;
            DownloadManager dManager = (DownloadManager) context.getSystemService(serviceString);
			 if (dManager.getUriForDownloadedFile(myDwonloadID) != null) {
                 //自动安装apk
                 installAPK(dManager.getUriForDownloadedFile(myDwonloadID), context);
                 //installAPK(context);
             } else {
            	 Util.toastLong(context, "下载失败！");
             }
//			long id = SettingsManager.getLong(context, SettingsManager.DOWNLOAD_APK_NUM, 0);
//			if(myDwonloadID == id){
//				String serviceString = Context.DOWNLOAD_SERVICE;
//	            DownloadManager dManager = (DownloadManager) context.getSystemService(serviceString);
//	            Intent install = new Intent(Intent.ACTION_VIEW);
//	            Uri downloadFileUri = dManager.getUriForDownloadedFile(myDwonloadID);
//	            install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
//	            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	            context.startActivity(install);  
//			}
		}else if(intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)){
			//点击通知栏
		}else if(intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")){
			//新版本安装成功,将原来的下载进程号清0
			SettingsManager.setLong(context, SettingsManager.DOWNLOAD_APK_NUM, 0);
		}
	}
	
	 private void installAPK(Uri apk, Context context) {
         if (Build.VERSION.SDK_INT < 23) {
             Intent intents = new Intent();
             intents.setAction("android.intent.action.VIEW");
             intents.addCategory("android.intent.category.DEFAULT");
             intents.setType("application/vnd.android.package-archive");
             intents.setData(apk);
             intents.setDataAndType(apk, "application/vnd.android.package-archive");
             intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             context.startActivity(intents);
         } else {
             File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/apk/"+ "ylfcf.apk");
             if (file.exists()) {
                 openFile(file, context);
             }
         }
     }

//     private void installAPK(Context context) {
//         File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +DOWNLOADPATH+ "member.apk");
//         if (file.exists()) {
//             openFile(file, context);
//         } else {
//             Util.toastLong(context, "下载失败");
//         }
//     }

     public void openFile(File file, Context context) {
         Intent intent = new Intent();
         intent.addFlags(268435456);
         intent.setAction("android.intent.action.VIEW");
         String type = getMIMEType(file);
         intent.setDataAndType(Uri.fromFile(file), type);
         try {
             context.startActivity(intent);
         } catch (Exception var5) {
             var5.printStackTrace();
             Util.toastLong(context, "没有找到打开此类文件的程序");
         }

     }

     public String getMIMEType(File var0) {
         String var1 = "";
         String var2 = var0.getName();
         String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
         var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
         return var1;
     }
}
