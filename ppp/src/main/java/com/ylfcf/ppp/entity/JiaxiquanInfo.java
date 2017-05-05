package com.ylfcf.ppp.entity;

public class JiaxiquanInfo implements java.io.Serializable{

	/**
	 * 加息券
	 */
	private static final long serialVersionUID = 1621908363008789121L;

	private String id;
	private String coupon_id;
	private String user_id;
	private String user_name;
	private String add_time;
	private String effective_start_time;
	private String effective_end_time;
	private String real_name;
	private String phone;
	private String min_invest_money;
	private String money;
	private String coupon_from;
	private String days;
	private String use_status;
	private String remark;
	private String use_time;
	private String repay_time;
	private String end_time;
	private String borrow_id;
	private String borrow_name;
	private String operation;
	private String need_invest_money;
	private String use_borrow_type;
	private String use_borrow_id;
	private String use_borrow_name;
	private String use_borrow_invest_id;
	private String borrow_type;//投资适用范围
	
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getMin_invest_money() {
		return min_invest_money;
	}
	public void setMin_invest_money(String min_invest_money) {
		this.min_invest_money = min_invest_money;
	}
	public String getCoupon_from() {
		return coupon_from;
	}
	public void setCoupon_from(String coupon_from) {
		this.coupon_from = coupon_from;
	}
	public String getUse_borrow_type() {
		return use_borrow_type;
	}
	public void setUse_borrow_type(String use_borrow_type) {
		this.use_borrow_type = use_borrow_type;
	}
	public String getUse_borrow_id() {
		return use_borrow_id;
	}
	public void setUse_borrow_id(String use_borrow_id) {
		this.use_borrow_id = use_borrow_id;
	}
	public String getUse_borrow_name() {
		return use_borrow_name;
	}
	public void setUse_borrow_name(String use_borrow_name) {
		this.use_borrow_name = use_borrow_name;
	}
	public String getUse_borrow_invest_id() {
		return use_borrow_invest_id;
	}
	public void setUse_borrow_invest_id(String use_borrow_invest_id) {
		this.use_borrow_invest_id = use_borrow_invest_id;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCoupon_id() {
		return coupon_id;
	}
	public void setCoupon_id(String coupon_id) {
		this.coupon_id = coupon_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getEffective_start_time() {
		return effective_start_time;
	}
	public void setEffective_start_time(String effective_start_time) {
		this.effective_start_time = effective_start_time;
	}
	public String getEffective_end_time() {
		return effective_end_time;
	}
	public void setEffective_end_time(String effective_end_time) {
		this.effective_end_time = effective_end_time;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getUse_status() {
		return use_status;
	}
	public void setUse_status(String use_status) {
		this.use_status = use_status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUse_time() {
		return use_time;
	}
	public void setUse_time(String use_time) {
		this.use_time = use_time;
	}
	public String getRepay_time() {
		return repay_time;
	}
	public void setRepay_time(String repay_time) {
		this.repay_time = repay_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getBorrow_id() {
		return borrow_id;
	}
	public void setBorrow_id(String borrow_id) {
		this.borrow_id = borrow_id;
	}
	public String getBorrow_name() {
		return borrow_name;
	}
	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
}
