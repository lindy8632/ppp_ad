package com.ylfcf.ppp.entity;

import java.util.List;
/**
 * 红包分页处理
 */
public class RedBagPageInfo implements java.io.Serializable {

	private static final long serialVersionUID = -6813690178210486563L;
	private String list;
	private List<RedBagInfo> redbagList;

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public List<RedBagInfo> getRedbagList() {
		return redbagList;
	}

	public void setRedbagList(List<RedBagInfo> redbagList) {
		this.redbagList = redbagList;
	}

}
