package com.ylfcf.ppp.entity;

/**
 * 体验金
 * @author Mr.liu
 */
public class TYJInfo implements java.io.Serializable{
	private static final long serialVersionUID = 2905062963483464627L;
	
	/*
	 * 体验金码（MD 16位）
	 */
	private String experience_code;
	/*
	 * 活动标识
	 */
	private String active_title;
	/*
	 * 用户id
	 */
	private String user_id;
	/*
	 * 体验金有效期开始时间
	 * 格式：yyyy-MM-dd HH:mm:ss
	 */
	private String start_time;
	/*
	 * 体验金有效期结束时间
	 * 格式：yyyy-MM-dd HH:mm:ss
	 */
	private String end_time;
	/*
	 * 体验金金额
	 */
	private String account;
	/*
	 * 实际使用时间
	 * 格式：yyyy-MM-dd HH:mm:ss
	 */
	private String use_time;
	/*
	 * 计息天数
	 */
	private String time_limit;
	/*
	 * 收益金额
	 */
	private String interest;
	/*
	 * 标的id
	 */
	private String borrow_id;
	/*
	 * 标的名字
	 */
	private String borrow_name;
	/*
	 * 投资id
	 */
	private String tender_id;
	/*
	 * 备注
	 */
	private String remark;
	/*
	 * 获取时间
	 * 格式：yyyy-MM-dd HH:mm:ss
	 */
	private String add_time;
	/*
	 * 兑付时间
	 * 格式：yyyy-MM-dd HH:mm:ss
	 */
	private String repay_time;
	/*
	 * 可发放状态
	 * 枚举类型：'关闭','锁定','审核中','已发放','拒绝','可发放'
	 */
	private String put_status;
	/*
	 * 提取次数
	 */
	private String test_num;
	/*
	 * 需要满足的投资金额
	 */
	private String need_invest_money;
	/*
	 * 状态
	 * 枚举类型：'未使用','已使用'
	 * 默认：'未使用'
	 */
	private String status;
	public String getExperience_code() {
		return experience_code;
	}
	public void setExperience_code(String experience_code) {
		this.experience_code = experience_code;
	}
	public String getActive_title() {
		return active_title;
	}
	public void setActive_title(String active_title) {
		this.active_title = active_title;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
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
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getUse_time() {
		return use_time;
	}
	public void setUse_time(String use_time) {
		this.use_time = use_time;
	}
	public String getTime_limit() {
		return time_limit;
	}
	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getBorrow_id() {
		return borrow_id;
	}
	public void setBorrow_id(String borrow_id) {
		this.borrow_id = borrow_id;
	}
	public String getBorrow_name() {
		return borrow_name;
	}
	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}
	public String getTender_id() {
		return tender_id;
	}
	public void setTender_id(String tender_id) {
		this.tender_id = tender_id;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getRepay_time() {
		return repay_time;
	}
	public void setRepay_time(String repay_time) {
		this.repay_time = repay_time;
	}
	public String getPut_status() {
		return put_status;
	}
	public void setPut_status(String put_status) {
		this.put_status = put_status;
	}
	public String getTest_num() {
		return test_num;
	}
	public void setTest_num(String test_num) {
		this.test_num = test_num;
	}
	public String getNeed_invest_money() {
		return need_invest_money;
	}
	public void setNeed_invest_money(String need_invest_money) {
		this.need_invest_money = need_invest_money;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
