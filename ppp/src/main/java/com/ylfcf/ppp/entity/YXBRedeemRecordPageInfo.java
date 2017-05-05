package com.ylfcf.ppp.entity;

import java.util.List;

/**
 * 元信宝赎回记录列表
 */
public class YXBRedeemRecordPageInfo implements java.io.Serializable {

	private static final long serialVersionUID = 6771828539226352367L;
	private String list;
	private String total;
	private List<YXBRedeemRecordInfo> yxbRecordList;

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

	public List<YXBRedeemRecordInfo> getYxbRecordList() {
		return yxbRecordList;
	}

	public void setYxbRecordList(List<YXBRedeemRecordInfo> yxbRecordList) {
		this.yxbRecordList = yxbRecordList;
	}

}
