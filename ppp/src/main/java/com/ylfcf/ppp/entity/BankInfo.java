package com.ylfcf.ppp.entity;

/**
 * 银行卡信息
 * @author Mr.liu
 *
 */
public class BankInfo implements java.io.Serializable {
	private static final long serialVersionUID = -5075564702768627876L;

	private String id;
	private String pay_way_id;//支付通道的id
	private String pay_way_name;//支付通道的名字
	private String bank_code;//银行简称
	private String bank_name;//银行的名字、
	private String single_quota;//单次限额 单位万 0表示无限额
	private String daily_quota;//单日限额 单位万 0表示无限额
	private String status;//状态 启用
	private String remark;//标注
	private String add_time;//添加时间
	private String update_time;
	private String bankPrompt;//该银行卡的限额说明
	private int bankLogo;//银行卡的Logo
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPay_way_id() {
		return pay_way_id;
	}
	public void setPay_way_id(String pay_way_id) {
		this.pay_way_id = pay_way_id;
	}
	public String getPay_way_name() {
		return pay_way_name;
	}
	public void setPay_way_name(String pay_way_name) {
		this.pay_way_name = pay_way_name;
	}
	public String getBank_code() {
		return bank_code;
	}
	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getSingle_quota() {
		return single_quota;
	}
	public void setSingle_quota(String single_quota) {
		this.single_quota = single_quota;
	}
	public String getDaily_quota() {
		return daily_quota;
	}
	public void setDaily_quota(String daily_quota) {
		this.daily_quota = daily_quota;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getBankPrompt() {
		return bankPrompt;
	}
	public void setBankPrompt(String bankPrompt) {
		this.bankPrompt = bankPrompt;
	}
	public int getBankLogo() {
		return bankLogo;
	}
	public void setBankLogo(int bankLogo) {
		this.bankLogo = bankLogo;
	}

}
