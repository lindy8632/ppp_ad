package com.ylfcf.ppp.entity;

import java.util.List;
/**
 * 私人尊享预约记录 分页
 * @author Mr.liu
 *
 */
public class SRZXAppointRecordPageInfo implements java.io.Serializable{

	private static final long serialVersionUID = -2559356585686868492L;
	private String list;
	private String total;
	private List<SRZXAppointRecordInfo> recordInfo;
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
	public List<SRZXAppointRecordInfo> getRecordInfo() {
		return recordInfo;
	}
	public void setRecordInfo(List<SRZXAppointRecordInfo> recordInfo) {
		this.recordInfo = recordInfo;
	}
	
}
