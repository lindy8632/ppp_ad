package com.ylfcf.ppp.receiver;

import com.ylfcf.ppp.util.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

/**
 * 网络状态改变监听广播
 * @author Administrator
 *
 */
public class NetworkStatusReceiver extends BroadcastReceiver{
	public NetworkStateListener mNetworkStateListener;

	public NetworkStatusReceiver(NetworkStateListener networkStateListener) {
		this.mNetworkStateListener = networkStateListener;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		try {
			if (action.equals(Constants.ACTION_NET_CHANGE) || action.equals(Constants.ACTION_NET_WIFI_STATE_CHANGED)) {
				boolean networkState = isOpenNetwork(context);
				if (mNetworkStateListener != null) {
					if (networkState) {
						mNetworkStateListener.onNetworkEnabled();
					} else {
						mNetworkStateListener.onNetworkDisabled();
					}
				}
			}
		} catch (Exception e) {
		}
	}
	
	/**
	 * 获取WIFI状态
	 * 
	 * @param context
	 * @return
	 */
	public boolean getWifiState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (State.CONNECTED == state) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 无线网络时候开发
	 * 
	 * @param context
	 * @return
	 */
	private boolean isOpenNetwork(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager != null && connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}
	
	/**
	 * 首页网络状态广播监听接口
	 * 
	 */
	public interface NetworkStateListener {
		/**
		 * 网络连接
		 */
		void onNetworkEnabled();

		/**
		 * 网络断开
		 */
		void onNetworkDisabled();

	}
}
