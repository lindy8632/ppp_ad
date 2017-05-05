package com.ylfcf.ppp.entity;

import java.util.List;

public class GiftPageInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2282783911157070468L;
	
	private String list;
	private List<GiftInfo> giftList;
	public GiftPageInfo(){}
	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public List<GiftInfo> getGiftList() {
		return giftList;
	}

	public void setGiftList(List<GiftInfo> giftList) {
		this.giftList = giftList;
	}
}
