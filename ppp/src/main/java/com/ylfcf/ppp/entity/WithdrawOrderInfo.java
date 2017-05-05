package com.ylfcf.ppp.entity;
/**
 * 提现订单
 */
public class WithdrawOrderInfo implements java.io.Serializable{
	private static final long serialVersionUID = -4845184844646139401L;
	private String id;
	private String user_id;
	private String cash_order;
	private String cash_account;
	
	private String bank_name;//银行的名字
	private String counter_fee;
	private String management_fee;
	private String recharge_money;
	
	private String repayment_money;//还款
	private String frozen_money;
	private String status;
	private String add_time;
	private String add_ip;
	private String remark;
	private String warning;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getCash_order() {
		return cash_order;
	}
	public void setCash_order(String cash_order) {
		this.cash_order = cash_order;
	}
	public String getCash_account() {
		return cash_account;
	}
	public void setCash_account(String cash_account) {
		this.cash_account = cash_account;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getCounter_fee() {
		return counter_fee;
	}
	public void setCounter_fee(String counter_fee) {
		this.counter_fee = counter_fee;
	}
	public String getManagement_fee() {
		return management_fee;
	}
	public void setManagement_fee(String management_fee) {
		this.management_fee = management_fee;
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
	public String getFrozen_money() {
		return frozen_money;
	}
	public void setFrozen_money(String frozen_money) {
		this.frozen_money = frozen_money;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getAdd_ip() {
		return add_ip;
	}
	public void setAdd_ip(String add_ip) {
		this.add_ip = add_ip;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getWarning() {
		return warning;
	}
	public void setWarning(String warning) {
		this.warning = warning;
	}
	
}
