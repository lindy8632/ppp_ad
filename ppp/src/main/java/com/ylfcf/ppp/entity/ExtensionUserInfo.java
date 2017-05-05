package com.ylfcf.ppp.entity;
/**
 * 推广的用户
 */
public class ExtensionUserInfo implements java.io.Serializable,Comparable<ExtensionUserInfo>{

	private static final long serialVersionUID = 8422140440053251597L;
	private String id;
	private String user_name;
	private String real_name;
	private String type;
	private String phone;
	private String email;
	private String id_number;
	private String sex;
	private String reg_time;
	private String reg_ip;
	private String password;
	private String hf_user_id;
	private String hf_reg_time;
	private String user_from;
	private String user_from_sub;
	private String user_from_host;
	private String open_id;
	private String extension_code;
	private String promoted_code;
	private String extension_user_id;
	private String address;
	private String post_code;
	private String login_time;
	private String deal_enabled;
	private String deal_pwd;
	private String tmp_data;
	private String phone_province;
	private String all_account;
	private String has_account;
	private String wait_account;
	private String status;//已注册一投资
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getId_number() {
		return id_number;
	}
	public void setId_number(String id_number) {
		this.id_number = id_number;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getReg_time() {
		return reg_time;
	}
	public void setReg_time(String reg_time) {
		this.reg_time = reg_time;
	}
	public String getReg_ip() {
		return reg_ip;
	}
	public void setReg_ip(String reg_ip) {
		this.reg_ip = reg_ip;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHf_user_id() {
		return hf_user_id;
	}
	public void setHf_user_id(String hf_user_id) {
		this.hf_user_id = hf_user_id;
	}
	public String getHf_reg_time() {
		return hf_reg_time;
	}
	public void setHf_reg_time(String hf_reg_time) {
		this.hf_reg_time = hf_reg_time;
	}
	public String getUser_from() {
		return user_from;
	}
	public void setUser_from(String user_from) {
		this.user_from = user_from;
	}
	public String getUser_from_sub() {
		return user_from_sub;
	}
	public void setUser_from_sub(String user_from_sub) {
		this.user_from_sub = user_from_sub;
	}
	public String getUser_from_host() {
		return user_from_host;
	}
	public void setUser_from_host(String user_from_host) {
		this.user_from_host = user_from_host;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
	public String getExtension_code() {
		return extension_code;
	}
	public void setExtension_code(String extension_code) {
		this.extension_code = extension_code;
	}
	public String getPromoted_code() {
		return promoted_code;
	}
	public void setPromoted_code(String promoted_code) {
		this.promoted_code = promoted_code;
	}
	public String getExtension_user_id() {
		return extension_user_id;
	}
	public void setExtension_user_id(String extension_user_id) {
		this.extension_user_id = extension_user_id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPost_code() {
		return post_code;
	}
	public void setPost_code(String post_code) {
		this.post_code = post_code;
	}
	public String getLogin_time() {
		return login_time;
	}
	public void setLogin_time(String login_time) {
		this.login_time = login_time;
	}
	public String getDeal_enabled() {
		return deal_enabled;
	}
	public void setDeal_enabled(String deal_enabled) {
		this.deal_enabled = deal_enabled;
	}
	public String getDeal_pwd() {
		return deal_pwd;
	}
	public void setDeal_pwd(String deal_pwd) {
		this.deal_pwd = deal_pwd;
	}
	public String getTmp_data() {
		return tmp_data;
	}
	public void setTmp_data(String tmp_data) {
		this.tmp_data = tmp_data;
	}
	public String getPhone_province() {
		return phone_province;
	}
	public void setPhone_province(String phone_province) {
		this.phone_province = phone_province;
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
	@Override
	public int compareTo(ExtensionUserInfo another) {
		return this.getAll_account().compareTo(another.getAll_account());
	}
}
