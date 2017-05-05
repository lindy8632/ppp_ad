package com.ylfcf.ppp.entity;
/**
 * 私人尊享产品预约记录
 * @author Mr.liu
 *
 */
public class SRZXAppointRecordInfo implements java.io.Serializable{
	
	private static final long serialVersionUID = 6130675599076026531L;
	private String id;
	private String user_id;
	private String user_type;//用户类型 企业 个人
	private String appoint_add_time;
	private String mobile;
	private String user_name;
	private String money;
	private String interest_period;
	private String 	borrow_period;
	private String interest_rate;
	private String purchase_time;
	private String add_ip;
	private String is_invest;
	private String borrow_status;
	private String money_status;
	
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getAdd_ip() {
		return add_ip;
	}
	public void setAdd_ip(String add_ip) {
		this.add_ip = add_ip;
	}
	public String getIs_invest() {
		return is_invest;
	}
	public void setIs_invest(String is_invest) {
		this.is_invest = is_invest;
	}
	public String getBorrow_status() {
		return borrow_status;
	}
	public void setBorrow_status(String borrow_status) {
		this.borrow_status = borrow_status;
	}
	public String getMoney_status() {
		return money_status;
	}
	public void setMoney_status(String money_status) {
		this.money_status = money_status;
	}
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
	public String getAppoint_add_time() {
		return appoint_add_time;
	}
	public void setAppoint_add_time(String appoint_add_time) {
		this.appoint_add_time = appoint_add_time;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getInterest_period() {
		return interest_period;
	}
	public void setInterest_period(String interest_period) {
		this.interest_period = interest_period;
	}
	public String getBorrow_period() {
		return borrow_period;
	}
	public void setBorrow_period(String borrow_period) {
		this.borrow_period = borrow_period;
	}
	public String getInterest_rate() {
		return interest_rate;
	}
	public void setInterest_rate(String interest_rate) {
		this.interest_rate = interest_rate;
	}
	public String getPurchase_time() {
		return purchase_time;
	}
	public void setPurchase_time(String purchase_time) {
		this.purchase_time = purchase_time;
	}
	
}
