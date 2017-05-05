package com.ylfcf.ppp.entity;

import java.util.List;

public class GiftInfo implements java.io.Serializable{
	private static final long serialVersionUID = -5681177563407962739L;
	private String id;
	private String name;
	private String pic;
	private String description;
	private String add_time;//
	private String update_time;
	private String rules_pc;
	private String rules_wap;
	private String rules_app;
	private List<String> rulesAppList;
	private String type;
	private String external_link_pc;
	private String external_link_wap;
	private String is_show;
	private String has_code;
	private String get_time;//奖品的领取时间
	private boolean isGetOver = false;//本奖品是否已经领完
	private boolean isGet = false;//用户是否已经领过该奖品
	private boolean isLogin = false;//是否已经登录
	private int isStart = -1;//-1表示未开始；0表示已开始;1表示已经结束
	
	public String getRules_app() {
		return rules_app;
	}
	public void setRules_app(String rules_app) {
		this.rules_app = rules_app;
	}
	public List<String> getRulesAppList() {
		return rulesAppList;
	}
	public void setRulesAppList(List<String> rulesAppList) {
		this.rulesAppList = rulesAppList;
	}
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
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
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
	public String getGet_time() {
		return get_time;
	}
	public void setGet_time(String get_time) {
		this.get_time = get_time;
	}
	public boolean isGetOver() {
		return isGetOver;
	}
	public void setGetOver(boolean isGetOver) {
		this.isGetOver = isGetOver;
	}
	public boolean isGet() {
		return isGet;
	}
	public void setGet(boolean isGet) {
		this.isGet = isGet;
	}
	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	public int getIsStart() {
		return isStart;
	}
	public void setIsStart(int isStart) {
		this.isStart = isStart;
	}
	
}
