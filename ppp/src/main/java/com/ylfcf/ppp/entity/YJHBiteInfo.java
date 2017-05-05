package com.ylfcf.ppp.entity;
/**
 * 元计划的利率（不同的投资金额对应不用的利率）
 */
public class YJHBiteInfo implements java.io.Serializable {

	private static final long serialVersionUID = -155259593695423288L;

	private String id;
	private String min_money;
	private String max_money;
	private String interest_rate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMin_money() {
		return min_money;
	}

	public void setMin_money(String min_money) {
		this.min_money = min_money;
	}

	public String getMax_money() {
		return max_money;
	}

	public void setMax_money(String max_money) {
		this.max_money = max_money;
	}

	public String getInterest_rate() {
		return interest_rate;
	}

	public void setInterest_rate(String interest_rate) {
		this.interest_rate = interest_rate;
	}

}
