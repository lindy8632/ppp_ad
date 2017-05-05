package com.ylfcf.ppp.entity;

import java.util.List;

/**
 * 公告、新闻、资讯列表分页处理
 * @author Mr.liu
 *
 */
public class ArticlePageInfo implements java.io.Serializable {

	private static final long serialVersionUID = 8081547048439036509L;

	private String list;
	private String total;
	private List<ArticleInfo> articleList;

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

	public List<ArticleInfo> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<ArticleInfo> articleList) {
		this.articleList = articleList;
	}

}
