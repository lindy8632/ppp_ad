package com.ylfcf.ppp.entity;

public class UserCardInfo implements java.io.Serializable {

	/**
	 * 用户银行卡信息
	 */
	private static final long serialVersionUID = 271828231621552318L;

	private String id;
	private String user_id;
	private String bank_card;// 银行卡号
	private String bank_name;//银行的名字
	private String province_code;//省
	private String city_code;//市
	private String branch_name;//分行
	private String local_bank_id;//本地银行id
	private String bank_num;//银行号
	private String bank_code;//银行
	private String type;//用户类型，vip用户还是普通用户
	private String is_binding;// 是否绑定

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getProvince_code() {
		return province_code;
	}

	public void setProvince_code(String province_code) {
		this.province_code = province_code;
	}

	public String getCity_code() {
		return city_code;
	}

	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}

	public String getBranch_name() {
		return branch_name;
	}

	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}

	public String getLocal_bank_id() {
		return local_bank_id;
	}

	public void setLocal_bank_id(String local_bank_id) {
		this.local_bank_id = local_bank_id;
	}

	public String getBank_num() {
		return bank_num;
	}

	public void setBank_num(String bank_num) {
		this.bank_num = bank_num;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getBank_card() {
		return bank_card;
	}

	public void setBank_card(String bank_card) {
		this.bank_card = bank_card;
	}

	public String getIs_binding() {
		return is_binding;
	}

	public void setIs_binding(String is_binding) {
		this.is_binding = is_binding;
	}

}
