package com.ylfcf.ppp.entity;
/**
 * 统计     投资了多少赚了多少
 */
public class StatisticInfo implements java.io.Serializable{

	private static final long serialVersionUID = -3565070069957462297L;
	
	private String use_money;
	private String collection_money;
	private String frozen_money;
	private String total_money;
	private String recharge;
	private String has_capital;
	private String has_interest;
	private String tender;
	private String wait_interest;
	public String getUse_money() {
		return use_money;
	}
	public void setUse_money(String use_money) {
		this.use_money = use_money;
	}
	public String getCollection_money() {
		return collection_money;
	}
	public void setCollection_money(String collection_money) {
		this.collection_money = collection_money;
	}
	public String getFrozen_money() {
		return frozen_money;
	}
	public void setFrozen_money(String frozen_money) {
		this.frozen_money = frozen_money;
	}
	public String getTotal_money() {
		return total_money;
	}
	public void setTotal_money(String total_money) {
		this.total_money = total_money;
	}
	public String getRecharge() {
		return recharge;
	}
	public void setRecharge(String recharge) {
		this.recharge = recharge;
	}
	public String getHas_capital() {
		return has_capital;
	}
	public void setHas_capital(String has_capital) {
		this.has_capital = has_capital;
	}
	public String getHas_interest() {
		return has_interest;
	}
	public void setHas_interest(String has_interest) {
		this.has_interest = has_interest;
	}
	public String getTender() {
		return tender;
	}
	public void setTender(String tender) {
		this.tender = tender;
	}
	public String getWait_interest() {
		return wait_interest;
	}
	public void setWait_interest(String wait_interest) {
		this.wait_interest = wait_interest;
	}
	
}
