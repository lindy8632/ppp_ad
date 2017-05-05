package com.ylfcf.ppp.entity;

import java.util.List;
/**
 * banner的分页处理
 */
public class BannerPageInfo implements java.io.Serializable {

	private static final long serialVersionUID = 3539794953551474167L;

	private String list;
	private String total;
	private List<BannerInfo> bannerList;

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

	public List<BannerInfo> getBannerList() {
		return bannerList;
	}

	public void setBannerList(List<BannerInfo> bannerList) {
		this.bannerList = bannerList;
	}

}
