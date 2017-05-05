package com.ylfcf.ppp.entity;
/**
 * 元信宝认购记录
 * @author Administrator
 */
public class YXBInvestRecordInfo implements java.io.Serializable{
	private static final long serialVersionUID = -1155476034702397009L;
	private String id;
	private String product_id;
	private String product_name;
	private String order_id;
	private String user_id;
	private String order_money;
	private String money;
	private String recharge_money;
	private String repayment_money;
	private String interest_money;
	private String withdraw_status;
	private String interest_status;
	private String manage_status;
	private String order_time;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getOrder_money() {
		return order_money;
	}
	public void setOrder_money(String order_money) {
		this.order_money = order_money;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getRecharge_money() {
		return recharge_money;
	}
	public void setRecharge_money(String recharge_money) {
		this.recharge_money = recharge_money;
	}
	public String getRepayment_money() {
		return repayment_money;
	}
	public void setRepayment_money(String repayment_money) {
		this.repayment_money = repayment_money;
	}
	public String getInterest_money() {
		return interest_money;
	}
	public void setInterest_money(String interest_money) {
		this.interest_money = interest_money;
	}
	public String getWithdraw_status() {
		return withdraw_status;
	}
	public void setWithdraw_status(String withdraw_status) {
		this.withdraw_status = withdraw_status;
	}
	public String getInterest_status() {
		return interest_status;
	}
	public void setInterest_status(String interest_status) {
		this.interest_status = interest_status;
	}
	public String getManage_status() {
		return manage_status;
	}
	public void setManage_status(String manage_status) {
		this.manage_status = manage_status;
	}
	public String getOrder_time() {
		return order_time;
	}
	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}
	
}
