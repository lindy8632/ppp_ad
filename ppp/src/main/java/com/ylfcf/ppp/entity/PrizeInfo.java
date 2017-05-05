package com.ylfcf.ppp.entity;

/**
 * 奖品信息
 */
public class PrizeInfo implements java.io.Serializable{

	private static final long serialVersionUID = 476519974531232642L;
	
	private String id;
	private String user_id;
	private String user_name;
	private String prize;
	private String prize_code;
	private String add_time;
	private String active_title;
	private String remark;
	private String status;
	private String send_time;
	private String add_ip;
	private String operating_remark;
	private String start_time;
	private String end_time;//有效期
	private String name;
	private String activate_way;
	private String rewardInfo;
	private String get_nums;
	private String used_nums;
	private String sign_id;
	private String source;
	private String order_id;
	private String exchange_results;
	private String is_delete;
	private String golden_num;
	private String prize_type;
	
	private String pic;//奖品图片
	private String description;//描述
	private String update_time;
	private String rules_pc;
	private String rules_wap;//规则
	private String type;//奖品类型
	private String external_link_pc;//
	private String external_link_wap;
	private String is_show;
	private String has_code;
	private String gift_id;
	private String win_probability;
	private String total_amount;
	private String remain_amount;
	private String product_detail;

	public String getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}

	public String getWin_probability() {
		return win_probability;
	}

	public void setWin_probability(String win_probability) {
		this.win_probability = win_probability;
	}

	public String getProduct_detail() {
		return product_detail;
	}

	public void setProduct_detail(String product_detail) {
		this.product_detail = product_detail;
	}

	public String getRemain_amount() {
		return remain_amount;
	}

	public void setRemain_amount(String remain_amount) {
		this.remain_amount = remain_amount;
	}

	public String getGift_id() {
		return gift_id;
	}
	public void setGift_id(String gift_id) {
		this.gift_id = gift_id;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getRules_pc() {
		return rules_pc;
	}
	public void setRules_pc(String rules_pc) {
		this.rules_pc = rules_pc;
	}
	public String getRules_wap() {
		return rules_wap;
	}
	public void setRules_wap(String rules_wap) {
		this.rules_wap = rules_wap;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getExternal_link_pc() {
		return external_link_pc;
	}
	public void setExternal_link_pc(String external_link_pc) {
		this.external_link_pc = external_link_pc;
	}
	public String getExternal_link_wap() {
		return external_link_wap;
	}
	public void setExternal_link_wap(String external_link_wap) {
		this.external_link_wap = external_link_wap;
	}
	public String getIs_show() {
		return is_show;
	}
	public void setIs_show(String is_show) {
		this.is_show = is_show;
	}
	public String getHas_code() {
		return has_code;
	}
	public void setHas_code(String has_code) {
		this.has_code = has_code;
	}
	public String getPrize_type() {
		return prize_type;
	}
	public void setPrize_type(String prize_type) {
		this.prize_type = prize_type;
	}
	private RewardInfo rewardInfoEntity;
	
	public String getPrize_code() {
		return prize_code;
	}
	public void setPrize_code(String prize_code) {
		this.prize_code = prize_code;
	}
	public String getSign_id() {
		return sign_id;
	}
	public void setSign_id(String sign_id) {
		this.sign_id = sign_id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getExchange_results() {
		return exchange_results;
	}
	public void setExchange_results(String exchange_results) {
		this.exchange_results = exchange_results;
	}
	public String getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}
	public String getGolden_num() {
		return golden_num;
	}
	public void setGolden_num(String golden_num) {
		this.golden_num = golden_num;
	}
	public String getGet_nums() {
		return get_nums;
	}
	public void setGet_nums(String get_nums) {
		this.get_nums = get_nums;
	}
	public String getUsed_nums() {
		return used_nums;
	}
	public void setUsed_nums(String used_nums) {
		this.used_nums = used_nums;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getActivate_way() {
		return activate_way;
	}
	public void setActivate_way(String activate_way) {
		this.activate_way = activate_way;
	}
	public String getRewardInfo() {
		return rewardInfo;
	}
	public void setRewardInfo(String rewardInfo) {
		this.rewardInfo = rewardInfo;
	}
	public RewardInfo getRewardInfoEntity() {
		return rewardInfoEntity;
	}
	public void setRewardInfoEntity(RewardInfo rewardInfoEntity) {
		this.rewardInfoEntity = rewardInfoEntity;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPrize() {
		return prize;
	}
	public void setPrize(String prize) {
		this.prize = prize;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getActive_title() {
		return active_title;
	}
	public void setActive_title(String active_title) {
		this.active_title = active_title;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSend_time() {
		return send_time;
	}
	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}
	public String getAdd_ip() {
		return add_ip;
	}
	public void setAdd_ip(String add_ip) {
		this.add_ip = add_ip;
	}
	public String getOperating_remark() {
		return operating_remark;
	}
	public void setOperating_remark(String operating_remark) {
		this.operating_remark = operating_remark;
	}
}
