package com.ylfcf.ppp.entity;

import java.util.List;

public class HDPrizePageInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 30703194580103930L;
	private String list;
	private String total;
	private List<PrizeInfo> prizeList;
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
	public List<PrizeInfo> getPrizeList() {
		return prizeList;
	}
	public void setPrizeList(List<PrizeInfo> prizeList) {
		this.prizeList = prizeList;
	}
	
}
