package com.ylfcf.ppp.entity;
/**
 * 红包
 */
public class RedBagInfo implements java.io.Serializable{

	private static final long serialVersionUID = 2018728050961034688L;
	
	private String id;
	private String money;
	private String need_invest_money;//需要投资的金额
	private String use_status;
	private String end_time;
	private String borrow_name;
	private String use_time;
	private String remark;
	private String borrow_type;//保盈稳盈速盈  用逗号隔开
	private String mixed_use;
	
	public String getBorrow_name() {
		return borrow_name;
	}
	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}
	public String getUse_time() {
		return use_time;
	}
	public void setUse_time(String use_time) {
		this.use_time = use_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBorrow_type() {
		return borrow_type;
	}
	public void setBorrow_type(String borrow_type) {
		this.borrow_type = borrow_type;
	}
	public String getMixed_use() {
		return mixed_use;
	}
	public void setMixed_use(String mixed_use) {
		this.mixed_use = mixed_use;
	}
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
	public String getUse_status() {
		return use_status;
	}
	public void setUse_status(String use_status) {
		this.use_status = use_status;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	
}
