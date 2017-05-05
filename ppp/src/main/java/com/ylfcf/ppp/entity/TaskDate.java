package com.ylfcf.ppp.entity;

/**
 * 计时器对象
 * @author Mr.liu
 *
 */
public class TaskDate {
	private long time = 0;
	private String id;

	public TaskDate() {
	}

	public TaskDate(String id, long time) {
		this.id = id;
		this.time = time;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
