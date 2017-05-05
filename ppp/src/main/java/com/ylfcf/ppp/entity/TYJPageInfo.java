package com.ylfcf.ppp.entity;

import java.util.List;
/**
 * 体验金分页处理
 * @author Mr.liu
 */
public class TYJPageInfo implements java.io.Serializable{
	private static final long serialVersionUID = -6272700963455665204L;
	private String list;
	private String total;
	private List<TYJInfo> tyjList;
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
	public List<TYJInfo> getTyjList() {
		return tyjList;
	}
	public void setTyjList(List<TYJInfo> tyjList) {
		this.tyjList = tyjList;
	}
	
}
