package com.ylfcf.ppp.entity;

import java.util.List;

/**
 * 元信宝 认购记录
 * @author Administrator
 *
 */
public class YXBInvestRecordPageInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1553375927988503860L;
	private String list;
	private String total;
	private List<YXBInvestRecordInfo> yxbInvestRecordList;
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
	public List<YXBInvestRecordInfo> getYxbInvestRecordList() {
		return yxbInvestRecordList;
	}
	public void setYxbInvestRecordList(List<YXBInvestRecordInfo> yxbInvestRecordList) {
		this.yxbInvestRecordList = yxbInvestRecordList;
	}
	
}
