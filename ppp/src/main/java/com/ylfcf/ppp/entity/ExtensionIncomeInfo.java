package com.ylfcf.ppp.entity;

/**
 * 推广的收益列表
 * @author Mr.liu
 */
public class ExtensionIncomeInfo implements java.io.Serializable {
	
	private static final long serialVersionUID = 998170426606563562L;
	private String id;
	/*
	 * 标的id
	 */
	private String borrow_id;
	/*
	 * 年化收益
	 */
	private String interest_rate;
	private String borrow_user_id;
	private String invest_user_id;
	private String invest_order_id;
	private String frozen_order_id;
	private String freeze_trx_id;
	private String sub_order_id;
	private String unfreeze_order_id;
	private String start_time;
	private String end_time;
	private String show_money;
	private String money;
	private String type;
	private String status;
	private String add_time;
	private String sub_time;
	private String repay_time;
	private String add_ip;
	private String invest_from;
	private String invest_from_sub;
	private String invest_from_host;
	private String experience_code;
	private String money_code;
	private String interest_code;
	private String extension_user_id;
	private String extension_valid;
	private String user_name;
	private String phone;
	private String reg_time;
	private String all_account;
	private String has_account;
	private String wait_account;
	private String borrow_name;
	private String invest_horizon;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBorrow_id() {
		return borrow_id;
	}

	public void setBorrow_id(String borrow_id) {
		this.borrow_id = borrow_id;
	}

	public String getInterest_rate() {
		return interest_rate;
	}

	public void setInterest_rate(String interest_rate) {
		this.interest_rate = interest_rate;
	}

	public String getBorrow_user_id() {
		return borrow_user_id;
	}

	public void setBorrow_user_id(String borrow_user_id) {
		this.borrow_user_id = borrow_user_id;
	}

	public String getInvest_user_id() {
		return invest_user_id;
	}

	public void setInvest_user_id(String invest_user_id) {
		this.invest_user_id = invest_user_id;
	}

	public String getInvest_order_id() {
		return invest_order_id;
	}

	public void setInvest_order_id(String invest_order_id) {
		this.invest_order_id = invest_order_id;
	}

	public String getFrozen_order_id() {
		return frozen_order_id;
	}

	public void setFrozen_order_id(String frozen_order_id) {
		this.frozen_order_id = frozen_order_id;
	}

	public String getFreeze_trx_id() {
		return freeze_trx_id;
	}

	public void setFreeze_trx_id(String freeze_trx_id) {
		this.freeze_trx_id = freeze_trx_id;
	}

	public String getSub_order_id() {
		return sub_order_id;
	}

	public void setSub_order_id(String sub_order_id) {
		this.sub_order_id = sub_order_id;
	}

	public String getUnfreeze_order_id() {
		return unfreeze_order_id;
	}

	public void setUnfreeze_order_id(String unfreeze_order_id) {
		this.unfreeze_order_id = unfreeze_order_id;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getShow_money() {
		return show_money;
	}

	public void setShow_money(String show_money) {
		this.show_money = show_money;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getSub_time() {
		return sub_time;
	}

	public void setSub_time(String sub_time) {
		this.sub_time = sub_time;
	}

	public String getRepay_time() {
		return repay_time;
	}

	public void setRepay_time(String repay_time) {
		this.repay_time = repay_time;
	}

	public String getAdd_ip() {
		return add_ip;
	}

	public void setAdd_ip(String add_ip) {
		this.add_ip = add_ip;
	}

	public String getInvest_from() {
		return invest_from;
	}

	public void setInvest_from(String invest_from) {
		this.invest_from = invest_from;
	}

	public String getInvest_from_sub() {
		return invest_from_sub;
	}

	public void setInvest_from_sub(String invest_from_sub) {
		this.invest_from_sub = invest_from_sub;
	}

	public String getInvest_from_host() {
		return invest_from_host;
	}

	public void setInvest_from_host(String invest_from_host) {
		this.invest_from_host = invest_from_host;
	}

	public String getExperience_code() {
		return experience_code;
	}

	public void setExperience_code(String experience_code) {
		this.experience_code = experience_code;
	}

	public String getMoney_code() {
		return money_code;
	}

	public void setMoney_code(String money_code) {
		this.money_code = money_code;
	}

	public String getInterest_code() {
		return interest_code;
	}

	public void setInterest_code(String interest_code) {
		this.interest_code = interest_code;
	}

	public String getExtension_user_id() {
		return extension_user_id;
	}

	public void setExtension_user_id(String extension_user_id) {
		this.extension_user_id = extension_user_id;
	}

	public String getExtension_valid() {
		return extension_valid;
	}

	public void setExtension_valid(String extension_valid) {
		this.extension_valid = extension_valid;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getReg_time() {
		return reg_time;
	}

	public void setReg_time(String reg_time) {
		this.reg_time = reg_time;
	}

	public String getAll_account() {
		return all_account;
	}

	public void setAll_account(String all_account) {
		this.all_account = all_account;
	}

	public String getHas_account() {
		return has_account;
	}

	public void setHas_account(String has_account) {
		this.has_account = has_account;
	}

	public String getWait_account() {
		return wait_account;
	}

	public void setWait_account(String wait_account) {
		this.wait_account = wait_account;
	}

	public String getBorrow_name() {
		return borrow_name;
	}

	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}

	public String getInvest_horizon() {
		return invest_horizon;
	}

	public void setInvest_horizon(String invest_horizon) {
		this.invest_horizon = invest_horizon;
	}
}
