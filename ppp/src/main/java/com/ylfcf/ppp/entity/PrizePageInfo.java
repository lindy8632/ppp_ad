package com.ylfcf.ppp.entity;

import java.util.List;
/**
 * 奖品分页信息
 */
public class PrizePageInfo implements java.io.Serializable {

	private static final long serialVersionUID = -5076173863901651593L;

	private String overTime;
	private String unUsed;
	private String used;
	private List<PrizeInfo> overTimeList;
	private List<PrizeInfo> unUsedList;
	private List<PrizeInfo> usedList;
	
	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}

	public List<PrizeInfo> getUsedList() {
		return usedList;
	}

	public void setUsedList(List<PrizeInfo> usedList) {
		this.usedList = usedList;
	}

	public String getOverTime() {
		return overTime;
	}

	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}

	public String getUnUsed() {
		return unUsed;
	}

	public void setUnUsed(String unUsed) {
		this.unUsed = unUsed;
	}

	public List<PrizeInfo> getOverTimeList() {
		return overTimeList;
	}

	public void setOverTimeList(List<PrizeInfo> overTimeList) {
		this.overTimeList = overTimeList;
	}

	public List<PrizeInfo> getUnUsedList() {
		return unUsedList;
	}

	public void setUnUsedList(List<PrizeInfo> unUsedList) {
		this.unUsedList = unUsedList;
	}

}
