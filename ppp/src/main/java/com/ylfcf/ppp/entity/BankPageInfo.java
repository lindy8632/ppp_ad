package com.ylfcf.ppp.entity;

import java.util.List;
/**
 * 银行列表 分页处理
 * @author Mr.liu
 *
 */
public class BankPageInfo implements java.io.Serializable{
	private static final long serialVersionUID = -6738506443003897715L;
	
	private String list;
	private String total;
	private List<BankInfo> bankList;
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
	public List<BankInfo> getBankList() {
		return bankList;
	}
	public void setBankList(List<BankInfo> bankList) {
		this.bankList = bankList;
	}

}
