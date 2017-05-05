package com.ylfcf.ppp.entity;

/**
 * 描述：账户信息（累计投资，累计收益）
 */
public class AccountTotalInfo implements java.io.Serializable {

	private static final long serialVersionUID = -9191190723447209452L;
	/*
	 * 投资金额
	 */
	private String invest_money;
	private String invest_count;
	private String wait_repayment_money;
	private String wait_repayment_count;
	/*
	 * 还款金额
	 */
	private String repayment_money;
	private String repayment_count;
	private String sum_invest_money;
	private String sum_invest_count;
	private String wait_interest;
	private String has_interest;
	private String sum_interest;

	public String getInvest_money() {
		return invest_money;
	}

	public void setInvest_money(String invest_money) {
		this.invest_money = invest_money;
	}

	public String getInvest_count() {
		return invest_count;
	}

	public void setInvest_count(String invest_count) {
		this.invest_count = invest_count;
	}

	public String getWait_repayment_money() {
		return wait_repayment_money;
	}

	public void setWait_repayment_money(String wait_repayment_money) {
		this.wait_repayment_money = wait_repayment_money;
	}

	public String getWait_repayment_count() {
		return wait_repayment_count;
	}

	public void setWait_repayment_count(String wait_repayment_count) {
		this.wait_repayment_count = wait_repayment_count;
	}

	public String getRepayment_money() {
		return repayment_money;
	}

	public void setRepayment_money(String repayment_money) {
		this.repayment_money = repayment_money;
	}

	public String getRepayment_count() {
		return repayment_count;
	}

	public void setRepayment_count(String repayment_count) {
		this.repayment_count = repayment_count;
	}

	public String getSum_invest_money() {
		return sum_invest_money;
	}

	public void setSum_invest_money(String sum_invest_money) {
		this.sum_invest_money = sum_invest_money;
	}

	public String getSum_invest_count() {
		return sum_invest_count;
	}

	public void setSum_invest_count(String sum_invest_count) {
		this.sum_invest_count = sum_invest_count;
	}

	public String getWait_interest() {
		return wait_interest;
	}

	public void setWait_interest(String wait_interest) {
		this.wait_interest = wait_interest;
	}

	public String getHas_interest() {
		return has_interest;
	}

	public void setHas_interest(String has_interest) {
		this.has_interest = has_interest;
	}

	public String getSum_interest() {
		return sum_interest;
	}

	public void setSum_interest(String sum_interest) {
		this.sum_interest = sum_interest;
	}

}
