package com.ylfcf.ppp.entity;

import java.util.List;

/**
 * 推广收益分页处理
 * @author Mr.liu
 *
 */
public class ExtensionPageInfo implements java.io.Serializable{
	private static final long serialVersionUID = -889465986450598587L;
	private String list;//推广的收益列表
	private String user_list;//推广的用户列表
	private String total;
	private List<ExtensionIncomeInfo> incomeInfoList;
	private List<ExtensionUserInfo> userInfoList;
	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
	}
	public String getUser_list() {
		return user_list;
	}
	public void setUser_list(String user_list) {
		this.user_list = user_list;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public List<ExtensionIncomeInfo> getIncomeInfoList() {
		return incomeInfoList;
	}
	public void setIncomeInfoList(List<ExtensionIncomeInfo> incomeInfoList) {
		this.incomeInfoList = incomeInfoList;
	}
	public List<ExtensionUserInfo> getUserInfoList() {
		return userInfoList;
	}
	public void setUserInfoList(List<ExtensionUserInfo> userInfoList) {
		this.userInfoList = userInfoList;
	}
	
}
