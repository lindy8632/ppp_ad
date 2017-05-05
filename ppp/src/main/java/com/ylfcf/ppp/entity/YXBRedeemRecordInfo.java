package com.ylfcf.ppp.entity;

/**
 * 元信宝赎回记录
 * @author Administrator
 */
public class YXBRedeemRecordInfo implements java.io.Serializable{

	private static final long serialVersionUID = -6056630263458648278L;
	
	private String id;
	private String order_id;
	private String user_id;
	private String apply_money;
	private String apply_money_fee;
	private String apply_money_interest;
	private String real_money;
	private String repayment_money;
	private String apply_time;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getApply_money() {
		return apply_money;
	}
	public void setApply_money(String apply_money) {
		this.apply_money = apply_money;
	}
	public String getApply_money_fee() {
		return apply_money_fee;
	}
	public void setApply_money_fee(String apply_money_fee) {
		this.apply_money_fee = apply_money_fee;
	}
	public String getApply_money_interest() {
		return apply_money_interest;
	}
	public void setApply_money_interest(String apply_money_interest) {
		this.apply_money_interest = apply_money_interest;
	}
	public String getReal_money() {
		return real_money;
	}
	public void setReal_money(String real_money) {
		this.real_money = real_money;
	}
	public String getRepayment_money() {
		return repayment_money;
	}
	public void setRepayment_money(String repayment_money) {
		this.repayment_money = repayment_money;
	}
	public String getApply_time() {
		return apply_time;
	}
	public void setApply_time(String apply_time) {
		this.apply_time = apply_time;
	}
	
}
