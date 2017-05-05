package com.ylfcf.ppp.entity;

public class WDYChildRecordInfo implements java.io.Serializable{

	/**
	 * 稳定盈子投资记录
	 */
	private static final long serialVersionUID = 3850128261112314589L;
	
	private String id;
	private String invest_id;
	private String borrow_id;
	private String which_term;
	private String plan_add_time;
	private String fact_add_time;
	private String delay_days;
	private String invest_money;
	private String interest_money;
	private String add_time;
	private String is_invest;
	private String invest_user_id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInvest_id() {
		return invest_id;
	}
	public void setInvest_id(String invest_id) {
		this.invest_id = invest_id;
	}
	public String getBorrow_id() {
		return borrow_id;
	}
	public void setBorrow_id(String borrow_id) {
		this.borrow_id = borrow_id;
	}
	public String getWhich_term() {
		return which_term;
	}
	public void setWhich_term(String which_term) {
		this.which_term = which_term;
	}
	public String getPlan_add_time() {
		return plan_add_time;
	}
	public void setPlan_add_time(String plan_add_time) {
		this.plan_add_time = plan_add_time;
	}
	public String getFact_add_time() {
		return fact_add_time;
	}
	public void setFact_add_time(String fact_add_time) {
		this.fact_add_time = fact_add_time;
	}
	public String getDelay_days() {
		return delay_days;
	}
	public void setDelay_days(String delay_days) {
		this.delay_days = delay_days;
	}
	public String getInvest_money() {
		return invest_money;
	}
	public void setInvest_money(String invest_money) {
		this.invest_money = invest_money;
	}
	public String getInterest_money() {
		return interest_money;
	}
	public void setInterest_money(String interest_money) {
		this.interest_money = interest_money;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getIs_invest() {
		return is_invest;
	}
	public void setIs_invest(String is_invest) {
		this.is_invest = is_invest;
	}
	public String getInvest_user_id() {
		return invest_user_id;
	}
	public void setInvest_user_id(String invest_user_id) {
		this.invest_user_id = invest_user_id;
	}
	
}
