package com.ylfcf.ppp.entity;

import java.util.List;

/**
 * 产品信息/ 标的信息
 */
public class ProductInfo implements java.io.Serializable {

	private static final long serialVersionUID = 8370003969027655415L;

	private String id;
	/*
	 * 产品类型
	 * 枚举类型：'保盈','稳盈','元鑫泰三','元鑫泰二','元鑫泰一','速盈'
	 */
	private String borrow_type;
	/*
	 * 标的名字
	 */
	private String borrow_name;
	/*
	 * 产品期数（元月盈新增字段）
	 */
	private String borrow_period;
	/*
	 * 冻结期，以天为单位（元月盈新增字段）
	 */
	private String frozen_period;
	/*
	 * 最高投资金额（元月盈新增字段）
	 */
	private String invest_highest;
	/*
	 * 产品Logo（元月盈新增字段）
	 */
	private String logo;
	/*
	 * 担保材料（打码） （元月盈新增字段）
	 */
	private String materials;
	/*
	 * 担保材料（没打码）（元月盈新增字段）
	 */
	private String materials_nomark;
	/*
	 * 担保材料名称（元月盈新增字段）
	 */
	private String imgs_name;
	/*
	 *  项目的id 
	 *  一个项目可以分成好多期的标
	 */
	private String project_id;
	/*
	 * 借款方用户id
	 */
	private String user_id;
	/*
	 * 标的总金额
	 */
	private String total_money;
	/*
	 * 实际投资金额
	 */
	private String invest_money;
	/*
	 * 单笔投资
	 */
	private String single_invest_money;
	/*
	 * 年化收益
	 */
	private String interest_rate;
	/*
	 * 标的开始时间
	 */
	private String start_time;
	private String end_time;
	/*
	 * 满标时间
	 */
	private String full_time;
	/*
	 * 计息周期,以天为单位
	 */
	private String interest_period;
	/*
	 * 标的募集期
	 * 以天为单位
	 */
	private String collect_period;
	/*
	 * 计息结束时间
	 */
	private String interest_end_time;
	/*
	 * 到期还本方式
	 * '到期还本付息','按月付息到期还本付息'
	 */
	private String repay_way;
	/*
	 * 流标时间
	 */
	private String flow_time;
	/*
	 * 标的状态
	 * 枚举类型：'审核中','初审通过','初审拒绝','复审通过','复审拒绝','发布','流标','还款中','还款完成'
	 */
	private String borrow_status;
	/*
	 * '未满标','已满标','已放款','已还款'
	 */
	private String money_status;
	/*
	 * 标的最新更新时间
	 */
	private String update_time;
	private String now_time;
	private String will_start_time;//秒标--- 下个标的开始时间
	/*
	 * 标的添加时间
	 */
	private String add_time;
	private String plan_publish_time;//私人尊享产品中 预约发布时间
	private String danbaohan;//担保函
	private String add_ip;
	private String invest_use;// 投资用途
	private String invest_period;//投资期限（update）
	private String invest_horizon;// 投资期限
	private String is_show;// 投资期限
	private String invest_lowest;// 最低可投多少钱
	private String is_TYJ;// 是否开启元金币 value:开启/关闭
	private String is_coin;// 是否开启元金币 value:开启/关闭
	private String active_title;
	private String bite;// 3.35%
	private String is_wap;// 是否移动端专享标
	private String up_money;// 递增金额
	private String introduce;// 产品介绍
	private String where_ids;// 投资去向表id
	private String pc_interest_rate;// pc端加息利率
	private String wap_interest_rate;// 微信wap版加息的利率
	private String android_interest_rate;// android版加息的利率
	private String ios_interest_rate;// ios版加息的利率
	private String min_rate;//
	private String max_rate;
	private String min_max_rate;
	private String list;// 预期收益率的list，不同的投资金额对应不同的利率
	private List<YJHBiteInfo> yjhBiteList;// 元计划的利率集合
	private String invest_day;//每月投资日（稳定赢产品新加参数）
	private String interest_period_month;//投资期限（单位月）
	private String return_period;
	private String interest_free_period;//免延期费的天数
	private String invest_status;//投资状态
	
	public String getInvest_day() {
		return invest_day;
	}

	public void setInvest_day(String invest_day) {
		this.invest_day = invest_day;
	}

	public String getInterest_period_month() {
		return interest_period_month;
	}

	public void setInterest_period_month(String interest_period_month) {
		this.interest_period_month = interest_period_month;
	}

	public String getReturn_period() {
		return return_period;
	}

	public void setReturn_period(String return_period) {
		this.return_period = return_period;
	}

	public String getInterest_free_period() {
		return interest_free_period;
	}

	public void setInterest_free_period(String interest_free_period) {
		this.interest_free_period = interest_free_period;
	}

	public String getInvest_status() {
		return invest_status;
	}

	public void setInvest_status(String invest_status) {
		this.invest_status = invest_status;
	}

	public String getSingle_invest_money() {
		return single_invest_money;
	}

	public void setSingle_invest_money(String single_invest_money) {
		this.single_invest_money = single_invest_money;
	}

	public String getNow_time() {
		return now_time;
	}

	public void setNow_time(String now_time) {
		this.now_time = now_time;
	}

	public String getWill_start_time() {
		return will_start_time;
	}

	public void setWill_start_time(String will_start_time) {
		this.will_start_time = will_start_time;
	}

	public String getInvest_period() {
		return invest_period;
	}

	public void setInvest_period(String invest_period) {
		this.invest_period = invest_period;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getPlan_publish_time() {
		return plan_publish_time;
	}

	public void setPlan_publish_time(String plan_publish_time) {
		this.plan_publish_time = plan_publish_time;
	}

	public String getDanbaohan() {
		return danbaohan;
	}

	public void setDanbaohan(String danbaohan) {
		this.danbaohan = danbaohan;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public List<YJHBiteInfo> getYjhBiteList() {
		return yjhBiteList;
	}

	public void setYjhBiteList(List<YJHBiteInfo> yjhBiteList) {
		this.yjhBiteList = yjhBiteList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBorrow_type() {
		return borrow_type;
	}

	public void setBorrow_type(String borrow_type) {
		this.borrow_type = borrow_type;
	}

	public String getBorrow_name() {
		return borrow_name;
	}

	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getTotal_money() {
		return total_money;
	}

	public void setTotal_money(String total_money) {
		this.total_money = total_money;
	}

	public String getInvest_money() {
		return invest_money;
	}

	public void setInvest_money(String invest_money) {
		this.invest_money = invest_money;
	}

	public String getInterest_rate() {
		return interest_rate;
	}

	public void setInterest_rate(String interest_rate) {
		this.interest_rate = interest_rate;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getFull_time() {
		return full_time;
	}

	public void setFull_time(String full_time) {
		this.full_time = full_time;
	}

	public String getInterest_period() {
		return interest_period;
	}

	public void setInterest_period(String interest_period) {
		this.interest_period = interest_period;
	}

	public String getCollect_period() {
		return collect_period;
	}

	public void setCollect_period(String collect_period) {
		this.collect_period = collect_period;
	}

	public String getInterest_end_time() {
		return interest_end_time;
	}

	public void setInterest_end_time(String interest_end_time) {
		this.interest_end_time = interest_end_time;
	}

	public String getRepay_way() {
		return repay_way;
	}

	public void setRepay_way(String repay_way) {
		this.repay_way = repay_way;
	}

	public String getFlow_time() {
		return flow_time;
	}

	public void setFlow_time(String flow_time) {
		this.flow_time = flow_time;
	}

	public String getBorrow_status() {
		return borrow_status;
	}

	public void setBorrow_status(String borrow_status) {
		this.borrow_status = borrow_status;
	}

	public String getMoney_status() {
		return money_status;
	}

	public void setMoney_status(String money_status) {
		this.money_status = money_status;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public String getAdd_ip() {
		return add_ip;
	}

	public void setAdd_ip(String add_ip) {
		this.add_ip = add_ip;
	}

	public String getInvest_use() {
		return invest_use;
	}

	public void setInvest_use(String invest_use) {
		this.invest_use = invest_use;
	}

	public String getInvest_horizon() {
		return invest_horizon;
	}

	public void setInvest_horizon(String invest_horizon) {
		this.invest_horizon = invest_horizon;
	}

	public String getIs_show() {
		return is_show;
	}

	public void setIs_show(String is_show) {
		this.is_show = is_show;
	}

	public String getInvest_lowest() {
		return invest_lowest;
	}

	public void setInvest_lowest(String invest_lowest) {
		this.invest_lowest = invest_lowest;
	}

	public String getIs_TYJ() {
		return is_TYJ;
	}

	public void setIs_TYJ(String is_TYJ) {
		this.is_TYJ = is_TYJ;
	}

	public String getIs_coin() {
		return is_coin;
	}

	public void setIs_coin(String is_coin) {
		this.is_coin = is_coin;
	}

	public String getActive_title() {
		return active_title;
	}

	public void setActive_title(String active_title) {
		this.active_title = active_title;
	}

	public String getBite() {
		return bite;
	}

	public void setBite(String bite) {
		this.bite = bite;
	}

	public String getIs_wap() {
		return is_wap;
	}

	public void setIs_wap(String is_wap) {
		this.is_wap = is_wap;
	}

	public String getPc_interest_rate() {
		return pc_interest_rate;
	}

	public void setPc_interest_rate(String pc_interest_rate) {
		this.pc_interest_rate = pc_interest_rate;
	}

	public String getWap_interest_rate() {
		return wap_interest_rate;
	}

	public void setWap_interest_rate(String wap_interest_rate) {
		this.wap_interest_rate = wap_interest_rate;
	}

	public String getAndroid_interest_rate() {
		return android_interest_rate;
	}

	public void setAndroid_interest_rate(String android_interest_rate) {
		this.android_interest_rate = android_interest_rate;
	}

	public String getIos_interest_rate() {
		return ios_interest_rate;
	}

	public void setIos_interest_rate(String ios_interest_rate) {
		this.ios_interest_rate = ios_interest_rate;
	}

	public String getUp_money() {
		return up_money;
	}

	public void setUp_money(String up_money) {
		this.up_money = up_money;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getWhere_ids() {
		return where_ids;
	}

	public void setWhere_ids(String where_ids) {
		this.where_ids = where_ids;
	}

	public String getMin_rate() {
		return min_rate;
	}

	public void setMin_rate(String min_rate) {
		this.min_rate = min_rate;
	}

	public String getMax_rate() {
		return max_rate;
	}

	public void setMax_rate(String max_rate) {
		this.max_rate = max_rate;
	}

	public String getMin_max_rate() {
		return min_max_rate;
	}

	public void setMin_max_rate(String min_max_rate) {
		this.min_max_rate = min_max_rate;
	}

	public String getBorrow_period() {
		return borrow_period;
	}

	public void setBorrow_period(String borrow_period) {
		this.borrow_period = borrow_period;
	}

	public String getFrozen_period() {
		return frozen_period;
	}

	public void setFrozen_period(String frozen_period) {
		this.frozen_period = frozen_period;
	}

	public String getInvest_highest() {
		return invest_highest;
	}

	public void setInvest_highest(String invest_highest) {
		this.invest_highest = invest_highest;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getMaterials() {
		return materials;
	}

	public void setMaterials(String materials) {
		this.materials = materials;
	}

	public String getMaterials_nomark() {
		return materials_nomark;
	}

	public void setMaterials_nomark(String materials_nomark) {
		this.materials_nomark = materials_nomark;
	}

	public String getImgs_name() {
		return imgs_name;
	}

	public void setImgs_name(String imgs_name) {
		this.imgs_name = imgs_name;
	}
}
