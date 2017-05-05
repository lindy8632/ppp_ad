package com.ylfcf.ppp.entity;

/**
 * 奖品的信息
 * @author Administrator
 *
 */
public class RewardInfo implements java.io.Serializable{
	
	private static final long serialVersionUID = -4139724701652368539L;
	
	private String money;
	private String code;
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
