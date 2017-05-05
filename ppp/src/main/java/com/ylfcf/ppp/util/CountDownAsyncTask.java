package com.ylfcf.ppp.util;

import com.ylfcf.ppp.entity.TaskDate;

import android.os.Handler;
import android.os.Message;

/**
 * µ¹¼ÆÊ±
 * @author Administrator
 *
 */
public class CountDownAsyncTask implements Runnable{
	public static final int PROGRESS_UPDATE = 1000;
	public static final int FINISH = 1001;
	
	private long currentTime = 0;
	private long taskTime = 0;
	private long intervalTime = 0;
	private String id;
	private Handler handler = null;
	private boolean cancel = false;
	
	public CountDownAsyncTask(Handler handler,String id,long currentTime,long taskTime,long intervalTime){
		this.handler = handler;
		this.id = id;
		this.currentTime = currentTime;
		this.taskTime = taskTime;
		this.intervalTime = intervalTime;
	}
	
	public void setCancel(boolean cancel){
		this.cancel = cancel;
	}
	
	public boolean isCancel() {
		return cancel;
	}

	@Override
	public void run() {
		long time = taskTime - currentTime;
		while(time > 0 && !cancel){
			try {
				TaskDate date = new TaskDate(id,time);
				Message msg = handler.obtainMessage(PROGRESS_UPDATE);
				msg.obj = date;
				handler.sendMessage(msg); 
				
				Thread.sleep(intervalTime);
				time -= intervalTime;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		cancel = true;
		Message msg = handler.obtainMessage(FINISH);
		msg.obj = id;
		handler.sendMessage(msg);
	}
}
