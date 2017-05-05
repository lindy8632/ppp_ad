package com.ylfcf.ppp.entity;
/**
 * 元信宝产品
 */
public class YXBProductInfo implements java.io.Serializable {

	private static final long serialVersionUID = -5171065101217062729L;
	private String id;
	private String name;
	private String status;// 已发布、审核中等等
	private String transfer_total_money;// 资产转让总价款
	private String min_invest_money;// 最小投资金额
	private String max_invest_money;// 最大投资金额
	private String add_invest_money;// 递增金额
	private String freeze_time;// 冻结期（天）
	private String year_get_interest;// 年化收益率
	private String start_time;// 上线时间
	private String end_time;// 截止时间
	private String get_interest_time;// 起息时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTransfer_total_money() {
		return transfer_total_money;
	}

	public void setTransfer_total_money(String transfer_total_money) {
		this.transfer_total_money = transfer_total_money;
	}

	public String getMin_invest_money() {
		return min_invest_money;
	}

	public void setMin_invest_money(String min_invest_money) {
		this.min_invest_money = min_invest_money;
	}

	public String getMax_invest_money() {
		return max_invest_money;
	}

	public void setMax_invest_money(String max_invest_money) {
		this.max_invest_money = max_invest_money;
	}

	public String getAdd_invest_money() {
		return add_invest_money;
	}

	public void setAdd_invest_money(String add_invest_money) {
		this.add_invest_money = add_invest_money;
	}

	public String getFreeze_time() {
		return freeze_time;
	}

	public void setFreeze_time(String freeze_time) {
		this.freeze_time = freeze_time;
	}

	public String getYear_get_interest() {
		return year_get_interest;
	}

	public void setYear_get_interest(String year_get_interest) {
		this.year_get_interest = year_get_interest;
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

	public String getGet_interest_time() {
		return get_interest_time;
	}

	public void setGet_interest_time(String get_interest_time) {
		this.get_interest_time = get_interest_time;
	}

}
