package com.ylfcf.ppp.entity;

public class RechargeResultInfo implements java.io.Serializable{
	/**
	 * 充值凭证
	 */
	private static final long serialVersionUID = 868246136238107956L;
	private String order_sn;//充值凭证id
	private String resp_msg;//交易结果
	
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public String getResp_msg() {
		return resp_msg;
	}
	public void setResp_msg(String resp_msg) {
		this.resp_msg = resp_msg;
	}
	
}
