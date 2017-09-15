package com.ylfcf.ppp.entity;

import java.util.List;

/**
 * 产品分页对象
 */
public class ProductPageInfo implements java.io.Serializable{

	private static final long serialVersionUID = 1344149830472511721L;
	
	private String list;
	private String total;
	private String count;
	private List<ProductInfo> productList;
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
	public List<ProductInfo> getProductList() {
		return productList;
	}
	public void setProductList(List<ProductInfo> productList) {
		this.productList = productList;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
}
