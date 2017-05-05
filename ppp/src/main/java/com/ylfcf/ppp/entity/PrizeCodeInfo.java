package com.ylfcf.ppp.entity;

/**
 * Ω±∆∑–≈œ¢
 */
public class PrizeCodeInfo implements java.io.Serializable{

	private static final long serialVersionUID = 331696140425771834L;
	private String id;
	private String prize_code; 
	private String prize_name;
	private String status;
	private String open_id;
	private String use_time;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrize_code() {
		return prize_code;
	}
	public void setPrize_code(String prize_code) {
		this.prize_code = prize_code;
	}
	public String getPrize_name() {
		return prize_name;
	}
	public void setPrize_name(String prize_name) {
		this.prize_name = prize_name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
	public String getUse_time() {
		return use_time;
	}
	public void setUse_time(String use_time) {
		this.use_time = use_time;
	}
	
}
