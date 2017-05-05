package com.ylfcf.ppp.entity;

import java.util.ArrayList;

/**
 * 项目信息
 * @author Administrator
 *
 */
public class ProjectInfo implements java.io.Serializable{

	/**
	 * 项目信息
	 */
	private static final long serialVersionUID = -8304373447735506476L;
	
	private String id;
	private String name;
	private String img;//项目logo
	private String summary;//概要
	private String loan_id;//借款方id
	private String recommend_id;//推荐方id
	private String guarantee_id;//担保方id
	private String capital;//资金用途
	private String capital_safe;//资金安全
	private String introduced;//融资方介绍
	private String measures;//担保措施
	private String repay_from;//还款来源
	private String invest_point;//投资亮点
	private String danbaohan;//担保函图片
	private String materials;//担保材料（打码）
	private String materials_nomark;//担保材料，无码
	private ArrayList<ProjectCailiaoInfo> cailiaoMarkList;//打码的材料的集合
	private ArrayList<ProjectCailiaoInfo> cailiaoNoMarkList;//没有打码的材料的集合
	private String imgs_name;//相关资料对应的图片名称(现在叫担保资料名称）
	private String safe;//安全保障
	private String status;//项目有效、发布等等...
	private String add_time;
	private String update_time;
	private String type;//资产包的类型。

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<ProjectCailiaoInfo> getCailiaoNoMarkList() {
		return cailiaoNoMarkList;
	}
	public void setCailiaoNoMarkList(ArrayList<ProjectCailiaoInfo> cailiaoNoMarkList) {
		this.cailiaoNoMarkList = cailiaoNoMarkList;
	}
	public ArrayList<ProjectCailiaoInfo> getCailiaoMarkList() {
		return cailiaoMarkList;
	}
	public void setCailiaoMarkList(ArrayList<ProjectCailiaoInfo> cailiaoMarkList) {
		this.cailiaoMarkList = cailiaoMarkList;
	}
	public String getLoan_id() {
		return loan_id;
	}
	public void setLoan_id(String loan_id) {
		this.loan_id = loan_id;
	}
	public String getRecommend_id() {
		return recommend_id;
	}
	public void setRecommend_id(String recommend_id) {
		this.recommend_id = recommend_id;
	}
	public String getGuarantee_id() {
		return guarantee_id;
	}
	public void setGuarantee_id(String guarantee_id) {
		this.guarantee_id = guarantee_id;
	}
	public String getMaterials_nomark() {
		return materials_nomark;
	}
	public void setMaterials_nomark(String materials_nomark) {
		this.materials_nomark = materials_nomark;
	}
	public String getCapital_safe() {
		return capital_safe;
	}
	public void setCapital_safe(String capital_safe) {
		this.capital_safe = capital_safe;
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
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getCapital() {
		return capital;
	}
	public void setCapital(String capital) {
		this.capital = capital;
	}
	public String getIntroduced() {
		return introduced;
	}
	public void setIntroduced(String introduced) {
		this.introduced = introduced;
	}
	public String getMeasures() {
		return measures;
	}
	public void setMeasures(String measures) {
		this.measures = measures;
	}
	public String getRepay_from() {
		return repay_from;
	}
	public void setRepay_from(String repay_from) {
		this.repay_from = repay_from;
	}
	public String getInvest_point() {
		return invest_point;
	}
	public void setInvest_point(String invest_point) {
		this.invest_point = invest_point;
	}
	public String getDanbaohan() {
		return danbaohan;
	}
	public void setDanbaohan(String danbaohan) {
		this.danbaohan = danbaohan;
	}
	public String getMaterials() {
		return materials;
	}
	public void setMaterials(String materials) {
		this.materials = materials;
	}
	public String getImgs_name() {
		return imgs_name;
	}
	public void setImgs_name(String imgs_name) {
		this.imgs_name = imgs_name;
	}
	public String getSafe() {
		return safe;
	}
	public void setSafe(String safe) {
		this.safe = safe;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	
}
