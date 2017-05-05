package com.ylfcf.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Android boot receiver.
 * 
 * @author Waggoner.wang
 */
public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// if (Application.getInstance().connectionStartAtBoot()) {
		// //context.startService(BaseAppService.createIntent(context));
		// } else {
		// android.os.Process.killProcess(android.os.Process.myPid());
		// }
	}
}
