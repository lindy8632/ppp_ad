package com.ylfcf.ppp.entity;
/**
 * 奖品 对应的活动信息
 */
public class PrizeActiveInfo implements java.io.Serializable {

	private static final long serialVersionUID = -157357848533748859L;

	private String id;
	private String active_title;// 活动标识
	private String name;// 活动名字
	private String start_time;
	private String end_time;
	private String number;
	private String money;
	private String time_limit;
	private String type;
	private String status;
	private String use_start_time;
	private String use_end_time;
	private String add_time;
	private String activate_way;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActive_title() {
		return active_title;
	}

	public void setActive_title(String active_title) {
		this.active_title = active_title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getTime_limit() {
		return time_limit;
	}

	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUse_start_time() {
		return use_start_time;
	}

	public void setUse_start_time(String use_start_time) {
		this.use_start_time = use_start_time;
	}

	public String getUse_end_time() {
		return use_end_time;
	}

	public void setUse_end_time(String use_end_time) {
		this.use_end_time = use_end_time;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public String getActivate_way() {
		return activate_way;
	}

	public void setActivate_way(String activate_way) {
		this.activate_way = activate_way;
	}

}
