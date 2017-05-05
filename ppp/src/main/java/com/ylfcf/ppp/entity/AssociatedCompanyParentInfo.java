package com.ylfcf.ppp.entity;

/**
 * 关联公司对象
 * @author Administrator
 *
 */
public class AssociatedCompanyParentInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5955591544753445093L;
	
	private String loan	;//	借款方
	private String recommend;//推荐方
	private String guarantee;//担保方
	private AssociatedCompanyInfo loanInfo;
	private AssociatedCompanyInfo recommendInfo;
	private AssociatedCompanyInfo guaranteeInfo;
	public String getLoan() {
		return loan;
	}
	public void setLoan(String loan) {
		this.loan = loan;
	}
	public String getRecommend() {
		return recommend;
	}
	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	public String getGuarantee() {
		return guarantee;
	}
	public void setGuarantee(String guarantee) {
		this.guarantee = guarantee;
	}
	public AssociatedCompanyInfo getLoanInfo() {
		return loanInfo;
	}
	public void setLoanInfo(AssociatedCompanyInfo loanInfo) {
		this.loanInfo = loanInfo;
	}
	public AssociatedCompanyInfo getRecommendInfo() {
		return recommendInfo;
	}
	public void setRecommendInfo(AssociatedCompanyInfo recommendInfo) {
		this.recommendInfo = recommendInfo;
	}
	public AssociatedCompanyInfo getGuaranteeInfo() {
		return guaranteeInfo;
	}
	public void setGuaranteeInfo(AssociatedCompanyInfo guaranteeInfo) {
		this.guaranteeInfo = guaranteeInfo;
	}
	
}
