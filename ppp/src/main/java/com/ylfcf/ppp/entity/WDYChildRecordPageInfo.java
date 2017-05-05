package com.ylfcf.ppp.entity;

import java.util.List;

public class WDYChildRecordPageInfo implements java.io.Serializable{

	/**
	 * 稳定盈子投资记录
	 */
	private static final long serialVersionUID = 1925226630286870664L;

	private String list;
	private String total;
	private List<WDYChildRecordInfo> wdyChildRecordList;
	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public List<WDYChildRecordInfo> getWdyChildRecordList() {
		return wdyChildRecordList;
	}
	public void setWdyChildRecordList(List<WDYChildRecordInfo> wdyChildRecordList) {
		this.wdyChildRecordList = wdyChildRecordList;
	}
	
}
