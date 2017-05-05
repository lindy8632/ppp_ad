package com.ylfcf.ppp.entity;

/**
 * @author Mr.liu
 * 描述：用户信息类
 */
public class UserInfo implements java.io.Serializable {

	private static final long serialVersionUID = -6672267238135167211L;

	private String id;
	/*
	 * 用户名
	 */
	private String user_name;
	/*
	 * 手机号码
	 */
	private String phone;
	/*
	 * 用户真实姓名
	 */
	private String real_name;
	/*
	 * 密码
	 */
	private String password;
	/*
	 * 用户类型 
	 * 枚举类型：'内部借款','内部投资','外部借款','外部投资','虚拟账户'
	 */
	private String type;
	private String co_mobile;
	private String co_phone;	
	private String reg_ip;
	private String init_pwd;//是否修改过密码
	private String sales_phone;
	private String user_status;
	/*
	 * 元金币
	 */
	private String coin;
	/*
	 * 邮箱
	 */
	private String email;
	/*
	 * 身份证号码
	 */
	private String id_number;
	/*
	 * 性别 
	 * 枚举类型：'男','女'
	 * 默认为男
	 */
	private String sex;
	/*
	 * 汇付生成的id
	 */
	private String hf_user_id;
	/*
	 * 注册时间 
	 * 格式：yyyy-MM-dd HH:mm:ss
	 */
	private String reg_time;
	/*
	 * 用户来源
	 * 枚举类型：'领克特','九盟推广','A5联盟','唯一传媒','元立方活动','元立方官网','安卓APP','惠享游','宜尚互动','苹果APP','券妈妈','元立方微信服务号','司马钱'
	 * 默认'元立方官网'
	 */
	private String user_from;
	/*
	 * 在APP端表示来源于某个渠道平台
	 * 比如'应用宝'等
	 */
	private String user_from_sub;
	/*
	 * 微信Openid
	 */
	private String open_id;
	/*
	 * 被推荐人的推广码
	 */
	private String extension_code;
	/*
	 * 推广码
	 */
	private String promoted_code;
	/*
	 * 推荐人id
	 */
	private String extension_user_id;
	/*
	 * 邮编
	 */
	private String post_code;
	/*
	 * 地址
	 */
	private String address;
	/*
	 * 登录时间
	 * 格式：yyyy-MM-dd HH:mm:ss
	 */
	private String login_time;
	/*
	 * 记录临时数据，如打地鼠获得积分，分享次数
	 */
	private String tmp_data;
	/*
	 * 交易密码状态
	 * 枚举类型：'关闭','开启'
	 * 默认：'关闭'
	 */
	private String deal_enabled;
	/*
	 * 交易密码
	 */
	private String deal_pwd;
	/*
	 * 手机号码归属地（省）
	 */
	private String phone_province;
	/*
	 * 实名验证次数
	 */
	private String verify_times;
	/*
	 * 提现密码输错次数
	 */
	private String deal_pwd_times;
	
	private String yyzz_code;//营业执照
	private String jgxy_code;//机构信用号
	private String khxk_code;//开户许可号
	private String bank_name;//银行名字
	private String bank_card;//银行卡
	private String yyzz_img;//营业执照图片
	private String jgxy_img;//机构信用图片
	private String khxk_img;//开户许可图片
	
	public String getYyzz_code() {
		return yyzz_code;
	}

	public void setYyzz_code(String yyzz_code) {
		this.yyzz_code = yyzz_code;
	}

	public String getJgxy_code() {
		return jgxy_code;
	}

	public void setJgxy_code(String jgxy_code) {
		this.jgxy_code = jgxy_code;
	}

	public String getKhxk_code() {
		return khxk_code;
	}

	public void setKhxk_code(String khxk_code) {
		this.khxk_code = khxk_code;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_card() {
		return bank_card;
	}

	public void setBank_card(String bank_card) {
		this.bank_card = bank_card;
	}

	public String getYyzz_img() {
		return yyzz_img;
	}

	public void setYyzz_img(String yyzz_img) {
		this.yyzz_img = yyzz_img;
	}

	public String getJgxy_img() {
		return jgxy_img;
	}

	public void setJgxy_img(String jgxy_img) {
		this.jgxy_img = jgxy_img;
	}

	public String getKhxk_img() {
		return khxk_img;
	}

	public void setKhxk_img(String khxk_img) {
		this.khxk_img = khxk_img;
	}

	public String getCo_mobile() {
		return co_mobile;
	}

	public void setCo_mobile(String co_mobile) {
		this.co_mobile = co_mobile;
	}

	public String getCo_phone() {
		return co_phone;
	}

	public void setCo_phone(String co_phone) {
		this.co_phone = co_phone;
	}

	public String getReg_ip() {
		return reg_ip;
	}

	public void setReg_ip(String reg_ip) {
		this.reg_ip = reg_ip;
	}

	public String getInit_pwd() {
		return init_pwd;
	}

	public void setInit_pwd(String init_pwd) {
		this.init_pwd = init_pwd;
	}

	public String getSales_phone() {
		return sales_phone;
	}

	public void setSales_phone(String sales_phone) {
		this.sales_phone = sales_phone;
	}

	public String getUser_status() {
		return user_status;
	}

	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}

	/**
	 * 获取交易密码状态
	 * @return '关闭','开启'
	 */
	public String getDeal_enabled() {
		return deal_enabled;
	}

	/**
	 * 设置交易密码状态
	 * @param deal_enabled
	 */
	public void setDeal_enabled(String deal_enabled) {
		this.deal_enabled = deal_enabled;
	}

	/**
	 * 获取交易密码
	 * @return
	 */
	public String getDeal_pwd() {
		return deal_pwd;
	}

	/**
	 * 设置交易密码
	 * @param deal_pwd
	 */
	public void setDeal_pwd(String deal_pwd) {
		this.deal_pwd = deal_pwd;
	}

	/**
	 * 获取元金币
	 * @return
	 */
	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

	public String getExtension_code() {
		return extension_code;
	}

	public void setExtension_code(String extension_code) {
		this.extension_code = extension_code;
	}

	public String getPromoted_code() {
		return promoted_code;
	}

	public void setPromoted_code(String promoted_code) {
		this.promoted_code = promoted_code;
	}

	public String getExtension_user_id() {
		return extension_user_id;
	}

	public void setExtension_user_id(String extension_user_id) {
		this.extension_user_id = extension_user_id;
	}

	public String getPost_code() {
		return post_code;
	}

	public void setPost_code(String post_code) {
		this.post_code = post_code;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLogin_time() {
		return login_time;
	}

	public void setLogin_time(String login_time) {
		this.login_time = login_time;
	}

	public String getTmp_data() {
		return tmp_data;
	}

	public void setTmp_data(String tmp_data) {
		this.tmp_data = tmp_data;
	}

	public String getReg_time() {
		return reg_time;
	}

	public void setReg_time(String reg_time) {
		this.reg_time = reg_time;
	}

	public String getUser_from() {
		return user_from;
	}

	public void setUser_from(String user_from) {
		this.user_from = user_from;
	}

	public String getUser_from_sub() {
		return user_from_sub;
	}

	public void setUser_from_sub(String user_from_sub) {
		this.user_from_sub = user_from_sub;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取用户名
	 * @return
	 */
	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId_number() {
		return id_number;
	}

	public void setId_number(String id_number) {
		this.id_number = id_number;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getHf_user_id() {
		return hf_user_id;
	}

	public void setHf_user_id(String hf_user_id) {
		this.hf_user_id = hf_user_id;
	}

	public String getPhone_province() {
		return phone_province;
	}

	public void setPhone_province(String phone_province) {
		this.phone_province = phone_province;
	}

	public String getVerify_times() {
		return verify_times;
	}

	public void setVerify_times(String verify_times) {
		this.verify_times = verify_times;
	}

	public String getDeal_pwd_times() {
		return deal_pwd_times;
	}

	public void setDeal_pwd_times(String deal_pwd_times) {
		this.deal_pwd_times = deal_pwd_times;
	}
}
