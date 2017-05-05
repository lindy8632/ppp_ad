package com.ylfcf.ppp.entity;

import java.util.List;

/**
 * 资金明细 分页处理
 */
public class FundsDetailsPageInfo implements java.io.Serializable {
	
	private static final long serialVersionUID = -6507856587739079288L;
	private String list;
	private List<FundsDetailsInfo> fundsDetailsList;

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public List<FundsDetailsInfo> getFundsDetailsList() {
		return fundsDetailsList;
	}

	public void setFundsDetailsList(List<FundsDetailsInfo> fundsDetailsList) {
		this.fundsDetailsList = fundsDetailsList;
	}

}
