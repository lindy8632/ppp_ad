package com.ylfcf.ppp.entity;

/**
 * Banner
 * @author Mr.liu
 *
 */
public class BannerInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1020103972732542369L;
	/*
	 * Banner的图片
	 */
	private String pic_url;
	private String id;
	private String type;//专题、文章、开机启动页
	private String link_url;//专题的链接
	private String article_id;//文章id
	private String status;//正常、关闭
	private String add_time;
	private String from_where;//客户端单独加的字段，表示是从哪个页面跳转过去的

	public String getFrom_where() {
		return from_where;
	}

	public void setFrom_where(String from_where) {
		this.from_where = from_where;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

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

	public String getLink_url() {
		return link_url;
	}

	public void setLink_url(String link_url) {
		this.link_url = link_url;
	}

	public String getArticle_id() {
		return article_id;
	}

	public void setArticle_id(String article_id) {
		this.article_id = article_id;
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
}
