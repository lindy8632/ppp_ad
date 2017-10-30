/**
  * All rights reserved.
 */
package com.ylfcf.ppp.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.BankInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.util.Constants.UserType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Settings of this application.
 * 工具类
 * @author Waggoner.wang
 */

public class SettingsManager extends DefaultPreferences {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//宝付支付通道上线时间
	private static final String bfPublishDate = "2016-06-29 15:00:00";
	//中秋大转盘活动截止时间
	private static final String dzpPrizeEndDate = "2016-09-21 23:59:59";
	//定期理财浮动利率上线时间
	private static final String floatRateStartDate = "2016-09-14 23:59:59";
	
	//国庆加息活动开始和结束时间
	private static final String guoqingJiaxiStartDate = "2016-09-28 00:00:00";
	private static final String guoqingJiaxiEndDate = "2016-10-10 23:59:59";
	
	//两周年感恩回馈活动开始和结束时间
	private static final String twoyearsTZFXStartDate = "2016-12-01 00:00:00";
	private static final String twoyearsTZFXEndDate = "2016-12-22 23:59:59";
	
	//限时秒标活动的开始和结束时间
	public static final String xsmbStartDate = "2016-12-23 00:00:00";
	public static final String xsmbEndDate = "2016-12-31 23:59:59";
	
	//会员福利计划活动开始和结束时间
	public static final String fljhStartDate = "2016-12-23 00:00:00";
	public static final String fljhEndDate = "2017-02-05 23:59:59";
	
	//新春红包活动的开始和结束时间
	private static final String xchbStartDate = "2017-01-05 00:00:00";
	private static final String xchbEndDate = "2017-02-05 23:59:59";
	
	//开年红 乐享返现
	private static final String lxfxStartDate = "2017-02-06 00:00:00";
	private static final String lxfxEndDate = "2017-02-23 23:59:59";
	
	//签到活动开始结束时间
	private static final String signStartDate = "2017-03-01 00:00:00";
	private static final String signEndDate = "2017-03-27 00:00:00";
	
	//邀请好友活动开始结束时间
	private static final String yqhyStartDate = "2017-04-01 00:00:00";
	private static final String yqhyEndDate = "2017-05-31 00:00:00";

	public static final String yyyJIAXIStartTime = "2017-06-01 11:00:00";
	public static final String yyyJIAXIEndTime = "2017-07-01 00:00:00";

	//2017年7月份活动开始结束时间
	public static final String activeJuly2017_StartTime = "2017-07-03 00:00:00";
	public static final String activeJuly2017_EndTime = "2017-07-31 00:00:00";

	//2017年十月双节活动
	public static final String activeOct2017_StartTime = "2017-10-01 00:00:00";
	public static final String activeOct2017_EndTime = "2017-10-08 23:59:59";

	//2017年双11加息活动
	public static final String activeNov2017_StartTime = "2017-10-10 00:00:00";
	public static final String activeNov2017_EndTime = "2017-11-12 23:59:59";


	public static final String USER_FROM = "安卓APP";//用户来源，是来源于元立方网站还是微信还是app等等。此处为写死
	public static final String APP_FIRST	= "appfirst";//判断应用是否是首次打开。
	public static final String USER_PASSWORD	= "password";
	public static final String USER_ACCOUNT		= "useraccount";//对于个人用户来说是手机号 对于企业用户来说是企业
	public static final String USER_COMP_PHONE = "comp_phone";//企业用户的联系手机号
	public static final String USER_NAME	=	"username";
	public static final String USER_TYPE = "usertype";//用户类型 personal:个人用户  company:企业用户
	public static final String USER_ID 		=	 "userid";
	public static final String USER_REG_TIME = "userregtime";//用户注册时间
	public static final String XMPP_ACCOUNT		= "xmppaccount";
	public static final String XMPP_PASSWORD	= "xmpppassword";
	public static final String LOGIN_DATE		= "LoginDate";
	public static final String MY_PHONE_NUMBER	= "my_phone_number";
	public static final String masterSeed       = "uidasdf9031348ader";
	public static final String GET_MSG_SEND_KAIGUAN = "msg_send_kaiguan";//是否接受消息推送
	public static final String DEAL_PWD_KAIGUAN = "msg_send_kaiguan";//交易密码开关
	public static final String DEAL_PWD = "deal_pwd";//交易密码
	public static final String MAINACTIVITY_PRODUCTLIST = "productlist";//跳转到首页时是否要跳到产品列表页面
	public static final String MAINACTIVITY_ACCOUNT = "account";//跳转到首页时是否要跳到我的账户页面
	public static final String MAINACTIVITY_FIRSTPAGE = "firstpage";//首页
	public static final String DOWNLOAD_APK_NUM = "download_apk";//apk下载的进程号
	public static final String SHARED_BANNER_CACHE_TIME = "banner_cache_time";
	public static final String ACCOUNTCENTER_ISFLOAT = "account_center_float";
	
	public static final Map<String,BankInfo> bankMap = new LinkedHashMap<String,BankInfo>();//快捷支付银行列表 key为bankcode  value为银行对象
	public static final Map<String,Integer> bankLogosMap = new LinkedHashMap<String,Integer>();//快捷支付银行logo key为bank_code  value为图片资源id
	
	public static String[] bankCodes = new String[]{"ICBC","ABC","BOC","CCB","PSBC","CITIC","CEB","PAB","BCOM","CIB",
		"CMBC","SPDB","SHB","CMB","HXB","CGB"};
	public static int[] bankLogos = new int[]{R.drawable.banklogo_icbc,R.drawable.banklogo_abc,R.drawable.banklogo_boc,R.drawable.banklogo_ccb,R.drawable.banklogo_psbc,
		R.drawable.banklogo_citic,R.drawable.banklogo_ceb,R.drawable.banklogo_pab,R.drawable.banklogo_bcom,R.drawable.banklogo_cib,R.drawable.banklogo_cmbc,
		R.drawable.banklogo_spdb,R.drawable.banklogo_shb,R.drawable.banklogo_cmb,R.drawable.banklogo_hxb,R.drawable.banklogo_cgb};
	/**
	 * 注册的验证码模板
	 */
	private static final String SMS_REGISTE_VERFIY_TEMP = "a:1:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";}";//替换的时候将AUTHCODE替换成四位数字短信验证码即可。
	/**
	 * 找回密码  验证码模板
	 */
	private static final String SMS_FORGET_PWD_VERFIY_TEMP = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"REAL_NAME\";s:11:\"USERNAME\";}";
	
	/**
	 * 找回提现密码
	 */
	private static final String SMS_GETBACK_DEAL_PWD_TEMP = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"USER_NAME\";s:11:\"USERNAME\";}";
	
	/**
	 * 找回提现密码 -- 企业
	 */
	private static final String SMS_GETBACK_DEAL_PWD_COMP_TEMP = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"USER_NAME\";s:12:\"USERNAME\";}";
	
	/**
	 * 找回提现密码 --- 默认模板 用户名为空的情况
	 */
	private static final String SMS_GETBACK_DEAL_PWD_TEMP_DEFAULT = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"USER_NAME\";s:4:\"USERNAME\";}";
	
	/**
	 * 设置提现密码
	 */
	private static final String SMS_SETTING_DEAL_PWD_TEMP = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"REAL_NAME\";s:11:\"USERNAME\";}";
	
	/**
	 * 设置提现密码  ----  企业用户
	 */
	private static final String SMS_SETTING_DEAL_PWD_COMP_TEMP = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"REAL_NAME\";s:12:\"USERNAME\";}";
	
	private static final String SMS_SETTING_DEAL_PWD_TEMP_DEFAULT = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"REAL_NAME\";s:4:\"USERNAME\";}";
	
	/**
	 * 注册成功
	 */
	private static final String SMS_REGISTER_SUCCESS = "a:1:{s:9:\"USER_NAME\";s:11:\"USERNAME\";}";
	
	/**
	 * 申请提现
	 */
	private static final String SMS_APPLY_CASH_TEMP = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"REAL_NAME\";s:11:\"USERNAME\";}";
	
	/**
	 * 申请提现 --  企业
	 */
	private static final String SMS_APPLY_CASH_COMP_TEMP = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"REAL_NAME\";s:12:\"USERNAME\";}";
	
	private static final String SMS_APPLY_CASH_TEMP_DEFAULT = "a:2:{s:11:\"VERIFY_CODE\";s:4:\"AUTHCODE\";s:9:\"REAL_NAME\";s:4:\"USERNAME\";}";
	
	public static ExecutorService FULL_TASK_EXECUTOR = (ExecutorService) Executors.newFixedThreadPool(4);

	private SettingsManager() {
	}

	/**
	 * 获取用户来源，在manifest.xml文件的meta-data属性里面
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String getUserFromSub(Context context){
		ApplicationInfo applicationInfo;
		String userFrom = "";
		try {
			applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			userFrom = applicationInfo.metaData.getString("USER_FROM_SUB");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			userFrom = "";
		}
		return userFrom;
	}
	
	/**
	 * 根据注册时间来判断是否为新用户（即宝付用户）
	 * @param regTime 格式yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static boolean checkIsNewUser(String regTime){
		Date regTimeD = null;
		Date bfPublishTimeD = null;
		try {
			regTimeD = sdf.parse(regTime);
			bfPublishTimeD = sdf.parse(bfPublishDate);
			if(bfPublishTimeD.compareTo(regTimeD) == -1){
				//注册时间大于宝付上线时间为新用户
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * 五月份 每周一抢现金
	 * @param baseInfo
	 * @return
	 */
	public static boolean checkRobCashIsStart(BaseInfo baseInfo){
		int week = 0;
		int day = 0;
		try{
			Date sysDate = sdf.parse(baseInfo.getTime());
			Calendar cal = Calendar.getInstance();
			cal.setTime(sysDate);
			day = cal.get(Calendar.DATE);
			week=cal.get(Calendar.DAY_OF_WEEK)-1;
			YLFLogger.d("今天是本月"+day+"号--------------------"+"今天是星期"+week);
			if(week == 1 && day != 8){
				//是星期一
				int hourInt = cal.get(Calendar.HOUR_OF_DAY);
				if(hourInt >= 20){
					return true;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断定期理财产品浮动利率改造是否上线
	 * @return
	 */
	public static boolean checkFloatRate(ProductInfo info){
		Date borrowStartTime = null;
		Date dqlcFloatRateStartDate = null;
		try {
			dqlcFloatRateStartDate = sdf.parse(floatRateStartDate);
			borrowStartTime = sdf.parse(info.getStart_time());
			if(dqlcFloatRateStartDate.compareTo(borrowStartTime) == -1){
				//表示浮动利率已经上线。
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * 判断国庆加息活动是否进行中
	 * @return
	 */
	public static boolean checkGuoqingJiaxiActivity(){
		Date nowTime = new Date();
		Date guoqingJiaxiStartTime = null;
		Date guoqingJiaxiEndTime = null;
		try {
			guoqingJiaxiStartTime = sdf.parse(guoqingJiaxiStartDate);
			guoqingJiaxiEndTime = sdf.parse(guoqingJiaxiEndDate);
			if(guoqingJiaxiStartTime.compareTo(nowTime) == -1 && guoqingJiaxiEndTime.compareTo(nowTime) == 1){
				//表示加息活动正在进行中
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * 判断两周年感恩回馈活动是否进行中
	 * @return
	 */
	public static boolean checkTwoYearsTZFXActivity(){
		Date nowTime = new Date();
		Date xsmbStartTime = null;
		Date xsmbEndTime = null;
		try {
			xsmbStartTime = sdf.parse(twoyearsTZFXStartDate);
			xsmbEndTime = sdf.parse(twoyearsTZFXEndDate);
			if(xsmbStartTime.compareTo(nowTime) == -1 && xsmbEndTime.compareTo(nowTime) == 1){
				//表示投资返现活动正在进行中
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * 根据系统时间判断某个活动是否开始
	 * @param sysTime 格式yyyy-MM-dd HH:mm:ss
	 * @param startTime 格式yyyy-MM-dd HH:mm:ss
	 * @param endTime 格式yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static int checkActiveStatusBySysTime(String sysTime,String startTime,String endTime){
		Date sysDate = null;
		try{
			sysDate = sdf.parse(sysTime);
		}catch (Exception e){
			e.printStackTrace();
		}

		if(sysTime == null)
			return -1;
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = sdf.parse(startTime);
			endDate = sdf.parse(endTime);
			if(startDate.compareTo(sysDate) == -1 && endDate.compareTo(sysDate) == 1){
				//表示活动正在进行中
				return 0;
			}else if(endDate.compareTo(sysDate) == -1 ){
				//活动结束
				return -1;
			}else if(startDate.compareTo(sysDate) == 1){
				//尚未开始
				return 1;
			}
		} catch (Exception e) {
		}
		return -1;
	}

	/**
	 * 新春红包
	 * @return
	 */
	public static int checkXCHBActivity(Date nowTime){
		Date xchbStartTime = null;
		Date xchbEndTime = null;
		try {
			xchbStartTime = sdf.parse(xchbStartDate);
			xchbEndTime = sdf.parse(xchbEndDate);
			if(xchbStartTime.compareTo(nowTime) == -1 && xchbEndTime.compareTo(nowTime) == 1){
				//表示投资返现活动正在进行中
				return 0;
			}else if(xchbEndTime.compareTo(nowTime) == -1){
				//活动结束
				return -1;
			}else if(xchbStartTime.compareTo(nowTime) == 1){
				//尚未开始
				return 1;
			}
		} catch (Exception e) {
		}
		return -1;
	}
	
	/**
	 * 开年红 乐享返现活动是否开始
	 * @return
	 */
	public static int checkLXFXActivity(){
		Date nowTime = new Date();
		Date lxfxStartTime = null;
		Date lxfxEndTime = null;
		try {
			lxfxStartTime = sdf.parse(lxfxStartDate);
			lxfxEndTime = sdf.parse(lxfxEndDate);
			if(lxfxStartTime.compareTo(nowTime) == -1 && lxfxEndTime.compareTo(nowTime) == 1){
				//表示投资返现活动正在进行中
				return 0;
			}else if(lxfxEndTime.compareTo(nowTime) == -1 ){
				//活动结束
				return -1;
			}else if(lxfxStartTime.compareTo(nowTime) == 1){
				//尚未开始
				return 1;
			}
		} catch (Exception e) {
		}
		return -1;
	}
	
	/**
	 * 签到活动是否已经开始
	 * @return
	 */
	public static int checkSignActivity(Date nowTime){
		Date signStartTime = null;
		Date signEndTime = null;
		try {
			signStartTime = sdf.parse(signStartDate);
			signEndTime = sdf.parse(signEndDate);
			if(signStartTime.compareTo(nowTime) == -1 && signEndTime.compareTo(nowTime) == 1){
				//表示签到活动正在进行中
				return 0;
			}else if(signEndTime.compareTo(nowTime) == -1 ){
				//活动结束
				return -1;
			}else if(signStartTime.compareTo(nowTime) == 1){
				//尚未开始
				return 1;
			}
		} catch (Exception e) {
		}
		return -1;
	}
	
	/**
	 * 邀请好友活动是否已经开始
	 * @return
	 */
	public static int checkYQHYActivity(Date nowTime){
		Date yqhyStartTime = null;
		Date yqhyEndTime = null;
		try {
			yqhyStartTime = sdf.parse(yqhyStartDate);
			yqhyEndTime = sdf.parse(yqhyEndDate);
			if(yqhyStartTime.compareTo(nowTime) == -1 && yqhyEndTime.compareTo(nowTime) == 1){
				//表示签到活动正在进行中
				return 0;
			}else if(yqhyEndTime.compareTo(nowTime) == -1 ){
				//活动结束
				return -1;
			}else if(yqhyStartTime.compareTo(nowTime) == 1){
				//尚未开始
				return 1;
			}
		} catch (Exception e) {
		}
		return -1;
	}
	
	/**
     * 获取屏幕分辨率
     * @param context
     * @return
     */
    public static int[] getScreenDispaly(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int width = 0;
		int height = 0;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			DisplayMetrics dm = new DisplayMetrics();
			windowManager.getDefaultDisplay().getMetrics(dm);
			width  = dm.widthPixels ;
			height = dm.heightPixels;
		}else{
			width = windowManager.getDefaultDisplay().getWidth();// 手机屏幕的宽度
			height = windowManager.getDefaultDisplay().getHeight();// 手机屏幕的高度
		}

		int result[] = { width, height };
		return result;
	}
    
    /**
     * 是否要跳转到产品列表页面
     * @param context
     * @param flag
     */
    public static void setMainProductListFlag(Context context,boolean flag){
    	DefaultPreferences.setBoolean(context, MAINACTIVITY_PRODUCTLIST, flag);
    }

	/**
	 * 账户中心浮窗是否显示
	 * @param context
	 * @param flag
	 */
	public static void setAccountCenterFloatFlag(Context context,boolean flag){
		DefaultPreferences.setBoolean(context,ACCOUNTCENTER_ISFLOAT,flag);
	}
    
    /**
     * 是否要跳转到我的账户页面
     * @param context
     * @param flag
     */
    public static void setMainAccountFlag(Context context,boolean flag){
    	DefaultPreferences.setBoolean(context, MAINACTIVITY_ACCOUNT, flag);
    }
    
    /**
     * 乐享返现 领取的dialog是否显示
     * @param context
     * @param key 此处为userid+"lxfx"
     * @param flag
     */
    public static void setLXFXJXQFlag(Context context,String key,boolean flag){
    	DefaultPreferences.setBoolean(context, key, flag);
    }
    
    /**
     * 会员福利2期 领取的dialog是否显示
     * @param context
     * @param key 此处为userid+"hyfl02"
     * @param flag
     */
    public static void setHYFLFlag(Context context,String key,boolean flag){
    	DefaultPreferences.setBoolean(context, key, flag);
    }
    
    /**
     * 是否要跳转到首页
     * @param context
     * @param flag
     */
    public static void setMainFirstpageFlag(Context context,boolean flag){
    	DefaultPreferences.setBoolean(context, MAINACTIVITY_FIRSTPAGE, flag);
    }
    
    /**
     * 在首页上是否跳转到产品列表页面
     * @param context
     * @return
     */
    public static boolean getMainProductListFlag(Context context){
    	boolean flag = DefaultPreferences.getBoolean(context, MAINACTIVITY_PRODUCTLIST, false);
    	return flag;
    }

    public static boolean getAccountCenterFloatFlag(Context context){
		boolean flag = DefaultPreferences.getBoolean(context, ACCOUNTCENTER_ISFLOAT, false);
		return flag;
	}
    
    /**
     * 在首页上是否跳转到我的账户页面
     * @param context
     * @return
     */
    public static boolean getMainAccountFlag(Context context){
    	boolean flag = DefaultPreferences.getBoolean(context, MAINACTIVITY_ACCOUNT, false);
    	return flag;
    }
    
    /**
     * 用户是否关闭该提示
     * @param key 此处为userid+"lxfx"
     */
    public static boolean getLXFXJXQFlag(Context context,String key){
    	boolean flag = DefaultPreferences.getBoolean(context, key, true);
    	return flag;
    }
    
    /**
     * 用户是否关闭该提示
     * @param key 此处为userid+"hyfl02"
     */
    public static boolean getHYFLFlag(Context context,String key){
    	boolean flag = DefaultPreferences.getBoolean(context, key, true);
    	return flag;
    }
    
    /**
     * 是否跳转到首页
     * @param context
     * @return
     */
    public static boolean getMainFirstpageFlag(Context context){
    	boolean flag = DefaultPreferences.getBoolean(context, MAINACTIVITY_FIRSTPAGE, false);
    	return flag;
    }
    
    /**
     * 是否接受消息推送
     * @param context
     * @param flag
     */
    public static void setMsgSendFlag(Context context,boolean flag){
    	DefaultPreferences.setBoolean(context, GET_MSG_SEND_KAIGUAN, flag);
    }
    
    /**
     * 是否接受消息推送
     * @param context
     * @return
     */
    public static boolean getMsgSendFlag(Context context){
    	boolean flag = DefaultPreferences.getBoolean(context, GET_MSG_SEND_KAIGUAN, true);
    	return flag;
    }
    
    /**
     * 交易密码开关，
     * @param context
     * @param flag
     */
    public static void setDealPwdFlag(Context context,boolean flag){
    	DefaultPreferences.setBoolean(context, DEAL_PWD_KAIGUAN, flag);
    }
    
    /**
     * 交易密码开关，默认要输入
     * @param context
     * @return
     */
    public static boolean getDealPwdFlag(Context context){
    	boolean flag = DefaultPreferences.getBoolean(context, DEAL_PWD_KAIGUAN, true);
    	return flag;
    }
    
    /**
     * 交易密码开关，
     * @param context
     * @param flag
     */
    public static void setDownloadApkNum(Context context,long flag){
    	DefaultPreferences.setLong(context, DOWNLOAD_APK_NUM, flag);
    }
    
    /**
     * 交易密码开关，默认要输入
     * @param context
     * @return
     */
    public static long getDownloadApkNum(Context context){
    	long flag = DefaultPreferences.getLong(context, DOWNLOAD_APK_NUM,0);
    	return flag;
    }
	
	/**
	 * 从DefaultPreference中读取用户账号，经AES解密后返回。
	 * @return 用户账号
	 */
	public static String getUser(Context context) {
		String user = DefaultPreferences.getString(context,USER_ACCOUNT, "");
		try {
//			if (!user.isEmpty())
//				user = SimpleCrypto.decrypt(masterSeed, user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return user;
	}
	
	/**
	 * banner的缓存时间 （缓存刷新时间为12小时）
	 * @param context
	 * @param cacheTime
	 */
	public static void setBannerCacheTime(Context context,long cacheTime){
		DefaultPreferences.setLong(context, SHARED_BANNER_CACHE_TIME, cacheTime);
	}
	
	/**
	 * banner的缓存时间 （缓存刷新时间为12小时）
	 * @param context
	 * @return
	 */
	public static long getBannerCacheTime(Context context){
		long cacheTime = DefaultPreferences.getLong(context, SHARED_BANNER_CACHE_TIME, 0);
		return cacheTime;
	}
	
	public static void setUserName(Context context,String username){
		if(username!=null){
			DefaultPreferences.setString(context,USER_NAME, username);
		}
	}
	
	public static String getUserName(Context context){
		String username = DefaultPreferences.getString(context,USER_NAME, "");
		return username;
	}
	
	public static void setCompPhone(Context context,String compPhone){
		if(compPhone !=null){
			DefaultPreferences.setString(context,USER_COMP_PHONE, compPhone);
		}
	}
	
	public static String getCompPhone(Context context){
		String compPhone = DefaultPreferences.getString(context,USER_COMP_PHONE, "");
		return compPhone;
	}
	
	public static void setUserType(Context context,String userType){
		if(userType!=null){
			DefaultPreferences.setString(context,USER_TYPE, userType);
		}
	}
	
	public static String getUserType(Context context){
		String usertype = DefaultPreferences.getString(context,USER_TYPE, "");
		return usertype;
	}
	
	public static void clearSharedAllData(Context context){
		DefaultPreferences.clearAll(context);
	}
	
	/**
	 * 讲用户账号经AES加密后保存到DefaultPreference
	 * @param account
	 */
	public static void setUser(Context context,String account) {
			try {
//				String encrypt	= SimpleCrypto.encrypt(masterSeed, account);
//				DefaultPreferences.setString(context,USER_ACCOUNT, encrypt);
				DefaultPreferences.setString(context,USER_ACCOUNT, account);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/**
	 * 
	 * @param context
	 * @param userId
	 */
	public static void setUserId(Context context,String userId){
			DefaultPreferences.setString(context,USER_ID, userId);
//			try {
//				String encrypt	= SimpleCrypto.encrypt(masterSeed, userId);
//				DefaultPreferences.setString(context,USER_ID, encrypt);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
	}
	
	public static String getUserId(Context context){
		String userId = DefaultPreferences.getString(context,USER_ID, "");
//		try {
//			if (!userId.isEmpty())
//				userId = SimpleCrypto.decrypt(masterSeed, userId);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return userId;
	}
	
	/**
	 * 用户注册时间
	 * @param context
	 */
	public static void setUserRegTime(Context context,String regTime){
		DefaultPreferences.setString(context,USER_REG_TIME, regTime);
	}
	
	/**
	 * 注册时间
	 * @param context
	 * @return
	 */
	public static String getUserRegTime(Context context){
		String regTime = DefaultPreferences.getString(context,USER_REG_TIME, "");
		return regTime;
	}
	
	public static String getLoginPassword(Context context) {
		String password = DefaultPreferences.getString(context,USER_PASSWORD, "");
//		try {
//			if (!password.isEmpty())
//				password = SimpleCrypto.decrypt(masterSeed, password);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return password;
	}
	
	/**
	 * 
	 * @param context
	 * @param password
	 * @param isEncry  是否需要加密
	 */
	public static void setLoginPassword(Context context,String password,boolean isEncry) {
//			try {
//				String encrypt	= SimpleCrypto.encrypt(masterSeed, password);
//				DefaultPreferences.setString(context,USER_PASSWORD, encrypt);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		if(isEncry){
			String encrypt	= Util.md5Encryption(password);
			DefaultPreferences.setString(context,USER_PASSWORD, encrypt);
		}else{
			DefaultPreferences.setString(context,USER_PASSWORD, password);
		}
			
	}
	
	/**
	 * 判断是否为首次打开APP，空则为首次打开
	 * @return
	 */
	public static String getAppFirst(Context context){
		return DefaultPreferences.getString(context,APP_FIRST, "");
	}
	
	public static void setAppFirst(Context context,String firstApp){
		DefaultPreferences.setString(context,APP_FIRST, firstApp);
	}
	
	public static String getXmppAccount(Context context) {
		return DefaultPreferences.getString(context,XMPP_ACCOUNT, "");
	}
	
	public static void setXmppAccount(Context context,String account) {
		DefaultPreferences.setString(context,XMPP_ACCOUNT, account);
	}
	
	public static String getXmppPassword(Context context) {
		return DefaultPreferences.getString(context,XMPP_PASSWORD, "");
	}
	
	public static void setXmppPassword(Context context,String password) {
		DefaultPreferences.setString(context,XMPP_PASSWORD, password);
	}
	
	public static void setLoginDate(Context context) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        DefaultPreferences.setString(context,LOGIN_DATE, format.format(new Date()));
	}
	
	public static void setMyPhoneNumber(Context context,String phone) {
		DefaultPreferences.setString(context,MY_PHONE_NUMBER, phone);
	}
	
	/**
	 * 第一个是短信验证码
	 * 第二个由短信验证码拼接成的参数
	 * @return
	 */
	public static String[] getSMSRegisteParams(){
		String authCode = getSMSAuthCode();
		return new String[]{authCode,SMS_REGISTE_VERFIY_TEMP.replace("AUTHCODE", authCode)};
	}
	
	/**
	 * 找回密码 短信模板
	 * @param userId  
	 * @return
	 */
	public static String[] getSMSForgetPwdParams(String userId){
		String authCode = getSMSAuthCode();
		String params = SMS_FORGET_PWD_VERFIY_TEMP.replace("AUTHCODE", authCode).replace("USERNAME", userId);
		return new String[]{authCode,params};
	}
	
	/**
	 * 找回提现密码  短信格式
	 * @param username
	 * @return
	 */
	public static String[] getSMSGetbackDealPwdParams(String username){
		String authCode = getSMSAuthCode();
		String params = SMS_GETBACK_DEAL_PWD_TEMP.replace("AUTHCODE", authCode).replace("USERNAME", username);
		return new String[]{authCode,params};
	}
	
	/**
	 * 找回提现密码  短信格式 企业用户
	 * @param username
	 * @return
	 */
	public static String[] getSMSGetbackDealPwdCompParams(String username){
		String authCode = getSMSAuthCode();
		String params = SMS_GETBACK_DEAL_PWD_COMP_TEMP.replace("AUTHCODE", authCode).replace("USERNAME", username);
		return new String[]{authCode,params};
	}
	
	/**
	 * 找回提现密码 默认格式 用户名为空
	 * @return
	 */
	public static String[] getSMSGetbackDealPwdParamsDefault(){
		String authCode = getSMSAuthCode();
		String params = SMS_GETBACK_DEAL_PWD_TEMP_DEFAULT.replace("AUTHCODE", authCode).replace("USERNAME", "用户");
		return new String[]{authCode,params};
	}
	
	/**
	 * 设置提现密码
	 * @param username
	 * @return
	 */
	public static String[] getSMSSettingDealPwdParams(String username){
		String authCode = getSMSAuthCode();
		String params = SMS_SETTING_DEAL_PWD_TEMP.replace("AUTHCODE", authCode).replace("USERNAME", username);
		return new String[]{authCode,params};
	}
	
	/**
	 * 设置提现密码   ----  企业用户
	 * @param username
	 * @return
	 */
	public static String[] getSMSSettingDealPwdCompParams(String username){
		String authCode = getSMSAuthCode();
		String params = SMS_SETTING_DEAL_PWD_COMP_TEMP.replace("AUTHCODE", authCode).replace("USERNAME", username);
		return new String[]{authCode,params};
	}
	
	/**
	 * 设置提现密码 默认模板  用户名为空
	 * @return
	 */
	public static String[] getSMSSettingDealPwdParamsDefault(){
		String authCode = getSMSAuthCode();
		String params = SMS_SETTING_DEAL_PWD_TEMP_DEFAULT.replace("AUTHCODE", authCode).replace("USERNAME", "用户");
		return new String[]{authCode,params};
	}
	
	/**
	 * 注册成功短信模板
	 * @param username
	 * @return
	 */
	public static String[] getSMSRegisteSuccessParams(String username){
		String authCode = getSMSAuthCode();
		String params = SMS_REGISTER_SUCCESS.replace("USERNAME", username);
		return new String[]{authCode,params};	
	}
	
	/**
	 * 申请提现
	 * @param username
	 * @return
	 */
	public static String[] getSMSWithdrawApplyParams(String username){
		String authCode = getSMSAuthCode();
		String params = SMS_APPLY_CASH_TEMP.replace("USERNAME", username).replace("AUTHCODE", authCode);
		return new String[]{authCode,params};	
	}
	
	/**
	 * 申请提现 --- 企业用户
	 * @param username
	 * @return
	 */
	public static String[] getSMSWithdrawApplyCompParams(String username){
		String authCode = getSMSAuthCode();
		String params = SMS_APPLY_CASH_COMP_TEMP.replace("USERNAME", username).replace("AUTHCODE", authCode);
		return new String[]{authCode,params};	
	}
	
	/**
	 * 申请提现  用户名为空
	 * @return
	 */
	public static String[] getSMSWithdrawApplyDefaultParams(){
		String authCode = getSMSAuthCode();
		String params = SMS_APPLY_CASH_TEMP_DEFAULT.replace("USERNAME", "用户").replace("AUTHCODE", authCode);
		return new String[]{authCode,params};	
	}
	
	private static int dTemp;
	private static String getSMSAuthCode(){
		int d = (int) (Math.random()*9000+1000);
		if(d == dTemp){
			getSMSAuthCode();
		}else{
			dTemp = d;
		}
		return String.valueOf(d);
	}
	
	/**
	 * 获取请求状态码
	 * @param baseInfo
	 * @return
	 */
	public static int getResultCode(BaseInfo baseInfo){
		int resultCode = -1;
		try {
			resultCode = Integer.parseInt(baseInfo.getError_id());
		} catch (Exception e) {
		}
		return resultCode;
	}
	
	/**
	 * 获取请求返回信息，仅限于msg为数字的时候  
	 * @param baseInfo
	 * @return
	 */
	public static int getResultMsg(BaseInfo baseInfo){
		int resultMsg = -1;
		try {
			resultMsg = Integer.parseInt(baseInfo.getMsg());
		} catch (Exception e) {
		}
		return resultMsg;
	}
	
	/**
	 * 判断当前用户是不是个人用户
	 * @return
	 */
	public static boolean isPersonalUser(Context context){
		String usertype = SettingsManager.getUserType(context);
		if(UserType.USER_NORMAL_PERSONAL.equals(usertype) || UserType.USER_VIP_PERSONAL.equals(usertype)
				|| usertype == null || "".equals(usertype)){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断当前用户是不是企业用户
	 * @param context
	 * @return
	 */
	public static boolean isCompanyUser(Context context){
		String usertype = SettingsManager.getUserType(context);
		if(UserType.USER_COMPANY.equals(usertype)){
			return true;
		}
		return false;
	}
}
