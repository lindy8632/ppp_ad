package com.ylfcf.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Receiver for network events.
 * 
 * @author Waggoner.wang
 * 
 */
public class ConnectivityReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// if
		// (!ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))
		// return;
		// NetworkInfo networkInfo = (NetworkInfo) intent
		// .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
		// if (networkInfo == null) {
		// LogManager.e(this, "NO INFO");
		// return;
		// }
		// NetworkManager.getInstance().onNetworkChange(networkInfo);
	}

}