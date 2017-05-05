package com.ylfcf.ppp.entity;

/**
 * APP更新
 * @author Mr.liu
 *
 */
public class AppInfo implements java.io.Serializable {
	private static final long serialVersionUID = -3682826618277679420L;

	private String version;//版本号
	private String new_version_url;//新版本的下载链接
	private String force_update;// 1表示强制更新，0表示普通升级
	private String do_update;// 是否需要更新 true，false
	private String brief;//更新内容

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getNew_version_url() {
		return new_version_url;
	}

	public void setNew_version_url(String new_version_url) {
		this.new_version_url = new_version_url;
	}

	public String getForce_update() {
		return force_update;
	}

	public void setForce_update(String force_update) {
		this.force_update = force_update;
	}

	public String getDo_update() {
		return do_update;
	}

	public void setDo_update(String do_update) {
		this.do_update = do_update;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

}
