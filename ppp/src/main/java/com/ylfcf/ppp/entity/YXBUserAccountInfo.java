package com.ylfcf.ppp.entity;
/**
 * 元信宝用户中心
 */
public class YXBUserAccountInfo implements java.io.Serializable {

	private static final long serialVersionUID = -179663846932879390L;

	private String id;
	private String user_id;
	private String sum_invest_money;// 总共投资的金额（已认购）
	private String last_interest_money;// 昨日收益
	private String sum_interest_money;// 累计收益
	private String need_apply_withdraw_money;// 当天可赎回金额
	private String apply_withdraw_money;// 当天已赎回金额
	private String update_time;

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

	public String getSum_invest_money() {
		return sum_invest_money;
	}

	public void setSum_invest_money(String sum_invest_money) {
		this.sum_invest_money = sum_invest_money;
	}

	public String getLast_interest_money() {
		return last_interest_money;
	}

	public void setLast_interest_money(String last_interest_money) {
		this.last_interest_money = last_interest_money;
	}

	public String getSum_interest_money() {
		return sum_interest_money;
	}

	public void setSum_interest_money(String sum_interest_money) {
		this.sum_interest_money = sum_interest_money;
	}

	public String getNeed_apply_withdraw_money() {
		return need_apply_withdraw_money;
	}

	public void setNeed_apply_withdraw_money(String need_apply_withdraw_money) {
		this.need_apply_withdraw_money = need_apply_withdraw_money;
	}

	public String getApply_withdraw_money() {
		return apply_withdraw_money;
	}

	public void setApply_withdraw_money(String apply_withdraw_money) {
		this.apply_withdraw_money = apply_withdraw_money;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

}
