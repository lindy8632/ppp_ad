package com.ylfcf.ppp.entity;

import java.util.List;
/**
 * ³äÖµ¼ÇÂ¼
 * @author Mr.liu
 *
 */
public class RechargeRecordPageInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2648637065533874095L;
	private String list;
	private String total;
	private List<RechargeRecordInfo> rechargeRecordList;
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
	public List<RechargeRecordInfo> getRechargeRecordList() {
		return rechargeRecordList;
	}
	public void setRechargeRecordList(List<RechargeRecordInfo> rechargeRecordList) {
		this.rechargeRecordList = rechargeRecordList;
	}
	
}
