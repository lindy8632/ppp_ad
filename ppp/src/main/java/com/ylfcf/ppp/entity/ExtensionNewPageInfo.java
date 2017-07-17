package com.ylfcf.ppp.entity;

import java.util.List;

/**
 * 推广收益分页处理 （新的接口）
 * @author Mr.liu
 *
 */
public class ExtensionNewPageInfo implements java.io.Serializable{

	private static final long serialVersionUID = 3268698894885514135L;
	
	private String list;
	private String total;
	private String extension_user_count;//推荐的好友的个数
	private String reward_total;//总共赚取了多少
	private String one_total;//一级好友
	private String second_total;//二级好友
	private String other_total;//历史好友
	private List<ExtensionNewInfo> extensionList;

	public String getOne_total() {
		return one_total;
	}

	public void setOne_total(String one_total) {
		this.one_total = one_total;
	}

	public String getSecond_total() {
		return second_total;
	}

	public void setSecond_total(String second_total) {
		this.second_total = second_total;
	}

	public String getOther_total() {
		return other_total;
	}

	public void setOther_total(String other_total) {
		this.other_total = other_total;
	}

	public String getList() {
		return list;
	}


	public void setList(String list) {
		this.list = list;
	}


	public String getTotal() {
		return total;
	}


	public void setTotal(String total) {
		this.total = total;
	}


	public String getExtension_user_count() {
		return extension_user_count;
	}


	public void setExtension_user_count(String extension_user_count) {
		this.extension_user_count = extension_user_count;
	}


	public String getReward_total() {
		return reward_total;
	}


	public void setReward_total(String reward_total) {
		this.reward_total = reward_total;
	}


	public List<ExtensionNewInfo> getExtensionList() {
		return extensionList;
	}

	public void setExtensionList(List<ExtensionNewInfo> extensionList) {
		this.extensionList = extensionList;
	}

}
