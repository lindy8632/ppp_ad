package com.ylfcf.ppp.entity;

import java.util.List;
/**
 * 投资记录列表对象
 */
public class InvestRecordPageInfo implements java.io.Serializable {

	private static final long serialVersionUID = 5807661024158315017L;

	private String list;
	private String total;
	private String sum_money;
	private String money;
	private List<InvestRecordInfo> investRecordList;

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

	public String getSum_money() {
		return sum_money;
	}

	public void setSum_money(String sum_money) {
		this.sum_money = sum_money;
	}

	public List<InvestRecordInfo> getInvestRecordList() {
		return investRecordList;
	}

	public void setInvestRecordList(List<InvestRecordInfo> investRecordList) {
		this.investRecordList = investRecordList;
	}

}
