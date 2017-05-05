package com.ylfcf.ppp.entity;
/**
 * 提现订单分页
 */
import java.util.List;

public class WithdrawOrderPageInfo implements java.io.Serializable{

	private static final long serialVersionUID = -3784538194407705535L;
	
	private String list;
	private String total;
	private List<WithdrawOrderInfo> orderList;
	
	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public List<WithdrawOrderInfo> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<WithdrawOrderInfo> orderList) {
		this.orderList = orderList;
	}
	
}
