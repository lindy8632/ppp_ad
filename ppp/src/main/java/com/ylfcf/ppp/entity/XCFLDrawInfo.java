package com.ylfcf.ppp.entity;
/**
 * 新春抢压岁钱 领奖接口返回的对象
 * @author Mr.liu
 *
 */
public class XCFLDrawInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8246999615751337101L;
	
	private String id;
	private String prizeType;
	private String perPrice;
	private String prize;
	private String positionId;
	private String 	leftDrawTimes;
	private String leftReceiveDrawTimes;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrizeType() {
		return prizeType;
	}
	public void setPrizeType(String prizeType) {
		this.prizeType = prizeType;
	}
	public String getPerPrice() {
		return perPrice;
	}
	public void setPerPrice(String perPrice) {
		this.perPrice = perPrice;
	}
	public String getPrize() {
		return prize;
	}
	public void setPrize(String prize) {
		this.prize = prize;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getLeftDrawTimes() {
		return leftDrawTimes;
	}
	public void setLeftDrawTimes(String leftDrawTimes) {
		this.leftDrawTimes = leftDrawTimes;
	}
	public String getLeftReceiveDrawTimes() {
		return leftReceiveDrawTimes;
	}
	public void setLeftReceiveDrawTimes(String leftReceiveDrawTimes) {
		this.leftReceiveDrawTimes = leftReceiveDrawTimes;
	}
	
}
