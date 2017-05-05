package com.ylfcf.ppp.entity;

/**
 * 新闻资讯公告的对象
 * @author Mr.liu
 *
 */
public class ArticleInfo implements java.io.Serializable{
	private static final long serialVersionUID = -8159007845784039125L;
	
	private String id;
	/*
	 * 文章分类
	 * 枚举类型：'行业新闻','网站公告','最新资讯','热门资讯','关于我们'
	 * 默认：'行业新闻'
	 */
	private String type;
	/*
	 * 文章标题
	 */
	private String title;
	/*
	 * 文章简介
	 */
	private String summary;
	/*
	 * 文章内容
	 */
	private String content;
	/*
	 * 文章封面的URL
	 */
	private String pic_url;
	/*
	 * SEO标题
	 */
	private String seo_title;
	/*
	 * SEO关键字
	 */
	private String seo_keywords;
	/*
	 * SEO描述
	 */
	private String seo_description;
	/*
	 * 作者
	 */
	private String admin_id;
	/*
	 * 排序值
	 */
	private String sort;
	/*
	 * 是否置顶
	 */
	private String is_order;
	/*
	 * 文章状态
	 * 枚举类型：'正常','关闭','删除'
	 * 默认：'正常'
	 */
	private String status;
	/*
	 * 是否重要
	 * 枚举类型：'是','否'
	 * 默认：'否'
	 */
	private String is_important;
	/*
	 * 创建时间
	 */
	private String add_time;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public String getSeo_title() {
		return seo_title;
	}
	public void setSeo_title(String seo_title) {
		this.seo_title = seo_title;
	}
	public String getSeo_keywords() {
		return seo_keywords;
	}
	public void setSeo_keywords(String seo_keywords) {
		this.seo_keywords = seo_keywords;
	}
	public String getSeo_description() {
		return seo_description;
	}
	public void setSeo_description(String seo_description) {
		this.seo_description = seo_description;
	}
	public String getAdmin_id() {
		return admin_id;
	}
	public void setAdmin_id(String admin_id) {
		this.admin_id = admin_id;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getIs_order() {
		return is_order;
	}
	public void setIs_order(String is_order) {
		this.is_order = is_order;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIs_important() {
		return is_important;
	}
	public void setIs_important(String is_important) {
		this.is_important = is_important;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	
}
