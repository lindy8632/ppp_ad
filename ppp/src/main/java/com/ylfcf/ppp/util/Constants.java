package com.ylfcf.ppp.util;

/**
 * 常量类
 * @author Mr.liu
 *
 */
public class Constants {
	/** 网络连接发生变化Action */
	public static final String ACTION_NET_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
	/** 网络连接WIFI状态发生变化Action */
	public static final String ACTION_NET_WIFI_STATE_CHANGED = "android.net.wifi.WIFI_AP_STATE_CHANGED";
	
	/**
	 * 手势密码点的状态
	 * @author Mr.liu
	 */
	public class TouchType {
		public static final int POINT_STATE_NORMAL = 0; // 正常状态

		public static final int POINT_STATE_SELECTED = 1; //按下状态

		public static final int POINT_STATE_WRONG = 2; // 错误状态
	}
	
	public class UserType {
		public static final String USER_NORMAL_PERSONAL = "normal_personal";//个人用户
		public static final String USER_VIP_PERSONAL = "vip_personal";//vip个人用户
		public static final String USER_COMPANY = "company";//企业用户
	}
	
	/**
	 * 提现类型
	 * @author Mr.liu
	 *
	 */
	public class WithdrawType {
		public static final String SHENHEZHONG = "审核中";
		public static final String SUCCESS = "成功";
	}
	
	/**
	 * 文章类型
	 * @author Mr.liu
	 *
	 */
	public class ArticleType {
		public static final String NOTICE = "网站公告";
		public static final String NEWS = "行业新闻"	;
		public static final String INFOR_NEW = "最新资讯";
		public static final String INFOR_HOT = "热门资讯";
		public static final String ABOUT_US = "关于我们";
	}
	
	/**
	 * 专题的类型（是什么专题）
	 * @author Administrator
	 *
	 */
	public class TopicType {
		public static final String CHONGZHISONG = "10";//充值送
		public static final String ZHUCESONG = "20";//注册送
		public static final String JIAXI = "30";//加息爽翻天
		public static final String TOUZIFANLI = "40";//投资返利
		public static final String XINGYUNZHUANPAN = "50";//幸运大转盘
		public static final String HOT_SUMMER = "60";//清凉一夏赚不停
		public static final String YYY_JX = "70";//元月盈加息活动
		public static final String RED_BAG = "80";//红包专题
		public static final String TUIGUANGYUAN = "90";//推广员的专题
		public static final String FRIENDS_CIRCLE = "100";//最强朋友圈
		public static final String FLOAT_RATE = "110";//浮动利率
		public static final String JXQ_RULE = "120";//加息券使用规则
		public static final String GQQD = "130";//国庆签到
		public static final String OCT_TZFX = "140";//10月投资迎新春红包
		public static final String CXFT_FLDJ = "150";//持续复投，福利叠加
		public static final String DOUBLE_ELEVEN_2016 = "160";//双十一活动
		public static final String TWOYEARS_FANXIAN = "170";//两周年返现活动。
		public static final String SECKILL = "180";//限时秒标活动
	}
	
	/**
	 * 由Banner图片要跳转的Activity的编号（对应BannerInfo对象里面的article_id字段）
	 * @author Mr.liu
	 *
	 */
	public class ActivityCode{
		public static final String YYY_DETAILS_ACTIVITY = "1";//元月盈详情页面。
		public static final String XSB_DETAILS_ACTIVITY = "2";//新手标详情页面。
		public static final String DQLC_LIST_ACTIVITY = "3";//定期理财产品的列表页面。
		public static final String VIP_LIST_ACTIVITY = "4";//VIP产品的列表页面。
		public static final String DZP_TEMP_ACTIVITY = "5";//中秋大转盘活动页面。
		public static final String SRZX_APPOINT_ACTIVITY = "6";//私人尊享产品的预约页面
		public static final String FLJH_ACTIVITY = "7";//会员福利计划
		public static final String XCFL_ACTIVITY = "8";//新春抢红包	
		public static final String LXFX_ACTIVITY = "9";//乐享返现 开年红
		public static final String SIGN_ACTIVITY = "10";//三月份签到活动
		public static final String FLJH_ACTIVITY_02 = "11";//会员福利计划2期
		public static final String YQHY_ACTIVITY = "12";//4月份活动邀请好友返现
		public static final String QXJ5_ACTIVITY = "13";//5月份每周一抢现金
	}
	
	public class PrizeName{
		public static final String PRIZE_NAME_JINKANGNI = "金康尼优惠券";
		public static final String PRIZE_NAME_VITA = "VITA健身电子券";
		public static final String PRIZE_NAME_YOUHUA = "柚花少女礼品券";
	}
}
