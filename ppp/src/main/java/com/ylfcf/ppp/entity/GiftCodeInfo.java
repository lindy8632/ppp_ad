package com.ylfcf.ppp.entity;

public class GiftCodeInfo implements java.io.Serializable{

	/**
	 * ¿Ò∆∑–≈œ¢
	 */
	private static final long serialVersionUID = 3503322273783914372L;
	
	private String id;
	private String gift_id;
	private String gift_code;
	private String status;
	private String add_time;
	private String user_id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGift_id() {
		return gift_id;
	}
	public void setGift_id(String gift_id) {
		this.gift_id = gift_id;
	}
	public String getGift_code() {
		return gift_code;
	}
	public void setGift_code(String gift_code) {
		this.gift_code = gift_code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}
