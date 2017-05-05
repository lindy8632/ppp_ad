package com.ylfcf.ppp.entity;

/**
 * 用户的元金币账户
 * 
 * @author Administrator
 * 
 */
public class UserYUANAccountInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3483315603736251308L;

	private String user_id;
	private String total_coin;
	private String use_coin;
	private String frozen_coin;
	private String collection_coin;
	private String draw_num;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getTotal_coin() {
		return total_coin;
	}

	public void setTotal_coin(String total_coin) {
		this.total_coin = total_coin;
	}

	public String getUse_coin() {
		return use_coin;
	}

	public void setUse_coin(String use_coin) {
		this.use_coin = use_coin;
	}

	public String getFrozen_coin() {
		return frozen_coin;
	}

	public void setFrozen_coin(String frozen_coin) {
		this.frozen_coin = frozen_coin;
	}

	public String getCollection_coin() {
		return collection_coin;
	}

	public void setCollection_coin(String collection_coin) {
		this.collection_coin = collection_coin;
	}

	public String getDraw_num() {
		return draw_num;
	}

	public void setDraw_num(String draw_num) {
		this.draw_num = draw_num;
	}

}
