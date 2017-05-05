package com.ylfcf.ppp.entity;
/**
 * 资金明细对象
 */
public class FundsDetailsInfo implements java.io.Serializable{
	
	private static final long serialVersionUID = -4392824425138773238L;
	private String add_time;
	private String remark;
	private String money;
	private String use_money;//可用余额
	private String frozen_money;//冻结金额
	private String collection_money;//待收金额
	private String user_id;
	private String type;
	private String order_id;
	private String in_or_out;
	private String total_money;
	private String recharge_money;
	private String repayment_money;
	private String add_ip;
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getUse_money() {
		return use_money;
	}
	public void setUse_money(String use_money) {
		this.use_money = use_money;
	}
	public String getFrozen_money() {
		return frozen_money;
	}
	public void setFrozen_money(String frozen_money) {
		this.frozen_money = frozen_money;
	}
	public String getCollection_money() {
		return collection_money;
	}
	public void setCollection_money(String collection_money) {
		this.collection_money = collection_money;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getIn_or_out() {
		return in_or_out;
	}
	public void setIn_or_out(String in_or_out) {
		this.in_or_out = in_or_out;
	}
	public String getTotal_money() {
		return total_money;
	}
	public void setTotal_money(String total_money) {
		this.total_money = total_money;
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
	public String getAdd_ip() {
		return add_ip;
	}
	public void setAdd_ip(String add_ip) {
		this.add_ip = add_ip;
	}
}
