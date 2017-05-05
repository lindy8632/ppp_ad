package com.ylfcf.ppp.async;

import java.util.concurrent.ExecutorService;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;

public class AsyncTaskBase extends AsyncTask<String, Void, String> {
	
	public AsyncTaskBase(){}
	
	@Override
	protected String doInBackground(String... params) {
		return null;
	}

	/**
	 * Ö´ÐÐ
	 */
	@SuppressLint("NewApi")
	public void executeAsyncTask(ExecutorService executorService) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			this.execute("");
		} else {
			this.executeOnExecutor(executorService, "");
		}
	}
}
