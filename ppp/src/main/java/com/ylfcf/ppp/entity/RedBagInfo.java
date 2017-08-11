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
	private String start_time;
	private String end_time;
	private String add_time;
	private String status;
	private String search_condition;
	private String borrow_name;
	private String borrow_id;
	private String borrow_invest_id;
	private String use_time;
	private String remark;
	private String borrow_type;//所投资的产品类型
	private String invest_type;//能使用该红包的产品类型
	private String mixed_use;
	private String repay_time;//
	private String show_need_invest_money;

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSearch_condition() {
		return search_condition;
	}

	public void setSearch_condition(String search_condition) {
		this.search_condition = search_condition;
	}

	public String getBorrow_invest_id() {
		return borrow_invest_id;
	}

	public void setBorrow_invest_id(String borrow_invest_id) {
		this.borrow_invest_id = borrow_invest_id;
	}

	public String getRepay_time() {
		return repay_time;
	}

	public void setRepay_time(String repay_time) {
		this.repay_time = repay_time;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getShow_need_invest_money() {
		return show_need_invest_money;
	}

	public void setShow_need_invest_money(String show_need_invest_money) {
		this.show_need_invest_money = show_need_invest_money;
	}

	public String getBorrow_id() {
		return borrow_id;
	}

	public void setBorrow_id(String borrow_id) {
		this.borrow_id = borrow_id;
	}

	public String getInvest_type() {
		return invest_type;
	}

	public void setInvest_type(String invest_type) {
		this.invest_type = invest_type;
	}

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
