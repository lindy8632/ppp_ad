package com.ylfcf.ppp.entity;

/**
 * 推广收益（新接口）
 */
public class ExtensionNewInfo implements java.io.Serializable{
	private static final long serialVersionUID = -3425832688279185837L;
	
	private String 	invest_user_name;//被推荐人的姓名
	private String invest_user_mobile;//被推荐人的手机号
	private String invest_time;
	private String borrow_name;
	private String interest_rate;
	private String interest_period;
	private String interest_start_time;
	private String 	percentage;
	private String return_time;
	private String invest_money;
	public String getInvest_user_name() {
		return invest_user_name;
	}
	public void setInvest_user_name(String invest_user_name) {
		this.invest_user_name = invest_user_name;
	}
	public String getInvest_user_mobile() {
		return invest_user_mobile;
	}
	public void setInvest_user_mobile(String invest_user_mobile) {
		this.invest_user_mobile = invest_user_mobile;
	}
	public String getInvest_time() {
		return invest_time;
	}
	public void setInvest_time(String invest_time) {
		this.invest_time = invest_time;
	}
	public String getBorrow_name() {
		return borrow_name;
	}
	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}
	public String getInterest_rate() {
		return interest_rate;
	}
	public void setInterest_rate(String interest_rate) {
		this.interest_rate = interest_rate;
	}
	public String getInterest_period() {
		return interest_period;
	}
	public void setInterest_period(String interest_period) {
		this.interest_period = interest_period;
	}
	public String getInterest_start_time() {
		return interest_start_time;
	}
	public void setInterest_start_time(String interest_start_time) {
		this.interest_start_time = interest_start_time;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	public String getReturn_time() {
		return return_time;
	}
	public void setReturn_time(String return_time) {
		this.return_time = return_time;
	}
	public String getInvest_money() {
		return invest_money;
	}
	public void setInvest_money(String invest_money) {
		this.invest_money = invest_money;
	}
}
