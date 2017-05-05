package com.ylfcf.ppp.entity;

public class JXQRuleInfo implements java.io.Serializable{
	
	/**
	 * 加息券规则
	 */
	private static final long serialVersionUID = -8256331336831291438L;
	private String id;
	private String money;
	private String need_invest_money;
	private String borrow_type;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getNeed_invest_money() {
		return need_invest_money;
	}
	public void setNeed_invest_money(String need_invest_money) {
		this.need_invest_money = need_invest_money;
	}
	public String getBorrow_type() {
		return borrow_type;
	}
	public void setBorrow_type(String borrow_type) {
		this.borrow_type = borrow_type;
	}
	
}
