package com.ylfcf.ppp.entity;
/**
 * 手势密码数据库实体对象
 */
public class GesturePwdEntity implements java.io.Serializable{

	private static final long serialVersionUID = 8684221940279570296L;
	
	private String userId;
	private String phone;
	private String status;//该用户是否开启手势密码   0：未开启  1：开启
	private String pwd;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
