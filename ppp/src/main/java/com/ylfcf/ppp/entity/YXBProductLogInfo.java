package com.ylfcf.ppp.entity;
/**
 * 产品每日统计
 */
public class YXBProductLogInfo implements java.io.Serializable{

	private static final long serialVersionUID = 2726268922820877418L;
	
	private String id;
	private String product_id;
	private String product_name;
	private String date_time;
	private String need_raise_money;//当天可募集资金（剩余募集额度）
	private String raise_money;//当天已募集金额
	private String need_apply_withdraw_money;//当天可申请赎回资金
	private String apply_withdraw_money;//当天申请赎回的资金
	private String withdraw_interest_money;//当天申请赎回结算收益
	private String withdraw_manage_money;//申请赎回的管理费金额
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getDate_time() {
		return date_time;
	}
	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}
	public String getNeed_raise_money() {
		return need_raise_money;
	}
	public void setNeed_raise_money(String need_raise_money) {
		this.need_raise_money = need_raise_money;
	}
	public String getRaise_money() {
		return raise_money;
	}
	public void setRaise_money(String raise_money) {
		this.raise_money = raise_money;
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
	public String getWithdraw_interest_money() {
		return withdraw_interest_money;
	}
	public void setWithdraw_interest_money(String withdraw_interest_money) {
		this.withdraw_interest_money = withdraw_interest_money;
	}
	public String getWithdraw_manage_money() {
		return withdraw_manage_money;
	}
	public void setWithdraw_manage_money(String withdraw_manage_money) {
		this.withdraw_manage_money = withdraw_manage_money;
	}
		
}
