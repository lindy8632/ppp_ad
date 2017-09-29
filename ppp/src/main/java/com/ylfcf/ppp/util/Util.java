/**
 * All rights reserved.
 */
package com.ylfcf.ppp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.ProductInfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Waggoner.wang
 *
 */
public class Util {

    private static final String TAG = "Util";
	private final static String DOMAIN = "ylfcf.com";
	
    public static final String KEY_EXTRA_REGISTER_OR_FORGETPW_FLAG = "register_or_forgetpw_flag";

    public static final String KEY_EXTRA_PAGE_TITLE = "KEY_EXTRA_PAGE_TITLE";
    public static final String KEY_EXTRA_PAGE_URL = "KEY_EXTRA_PAGE_URL";

    public static final int VALUE_IMMEDIATELY_REGISTER = 1;
    public static final int VALUE_FORGET_PASSWORD = 2;

    public static final String REQ_SMS_AUTH = "http://service.ylfcf.com/sendsmsmessage/send_regist_sms_message.json";
    public static final String STATUS_SUCCESS = "20000";
    public static final String KEY_EXTRA_SMS_PHONE_NUMBER = "KEY_EXTRA_SMS_PHONE_NUMBER";
    public static final String KEY_EXTRA_SMS_AUTH_CODE_FLG = "KEY_EXTRA_SMS_AUTH_CODE_FLG";
    public static final String KEY_EXTRA_SMS_AUTH_CODE = "KEY_EXTRA_SMS_AUTH_CODE";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 23:00:00");
    private static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd 01:00:00");
    private static SimpleDateFormat sdfMonth = new SimpleDateFormat("M");
    private static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
    
    private static Toast mToast = null;
    
    private static Handler handler = new Handler();

	private static Runnable run = new Runnable() {
		public void run() {
			if (mToast != null) {
				mToast.cancel();
			}
		}
	};

	/**
	 * 薪盈计划预期收益
	 * @param rate 年化收益
	 * @param money 投资金额
	 * @param month 月数
	 * @return
	 */
	public static String getWDYInterest(String rate,String couponRateStr,int money,String month){
		double rateD = 0d;
		double couponRateD = 0d;
		int monthI = 0;
		double interestD = 0d;
		double interestTemp = 0d;
		try {
			rateD = Double.parseDouble(rate);
			monthI = Integer.parseInt(month);
		} catch (Exception e) {
		}
		try{
			couponRateD = Double.parseDouble(couponRateStr);
		}catch (Exception e){

		}
		for(int i=1;i<=monthI;i++){
			if(i == monthI){
				interestTemp = money * (rateD + couponRateD)/ 100 * i /12;
			}else{
				interestTemp = money * rateD / 100 * i /12;
			}
			interestD += interestTemp;
		}
		return Util.double2PointDoubleOne(interestD);
	}
	
	/**
	 * 判断元月盈是否可以赎回
	 * 规则：起息时间起才可以申请赎回。即满标时间+1天
	 * @param recordInfo
	 * @return
	 */
	public static boolean isReturnYYY(InvestRecordInfo recordInfo){
		long nowTimeL = System.currentTimeMillis();
		long interestStartTime = 0l;
		if(recordInfo != null){
			if("0000-00-00 00:00:00".equals(recordInfo.getInterest_start_time())){
				return false;
			}
			try {
				Date dateS = sdf.parse(recordInfo.getInterest_start_time());
				interestStartTime = dateS.getTime();
				if(nowTimeL > interestStartTime){
					return true;
				}
			} catch (Exception e) {
			}
		}
		return false;
	}
	
	public static String replaceHtmlURL(String string) {
		String[] sources = { "\\" };
		String[] destinations = { "" };
		String result = TextUtils.replace(string, sources, destinations).toString();
		if((result.indexOf(DOMAIN)<=0)&&(result.indexOf("test")<=0))
			result = result.replaceAll("-", "/");
		return result;
	}
	
	/**
	 * 判断当前月份一共有多少天
	 * @param nowTime
	 * @return
	 */
	public static int getCurrentMonthDays(String nowTime){
		int yearPosition = 0;
		int monthPosition = 0;
		try {
			String yearStr = sdfYear.format(sdf.parse(nowTime));
			String monthStr = sdfMonth.format(sdf.parse(nowTime));
			yearPosition = Integer.parseInt(yearStr);
			monthPosition = Integer.parseInt(monthStr);
		} catch (Exception e) {
			String yearStr = sdfYear.format(new Date());
			String monthStr = sdfMonth.format(new Date());
			yearPosition = Integer.parseInt(yearStr);
			monthPosition = Integer.parseInt(monthStr);
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,yearPosition);
		cal.set(Calendar.MONTH, monthPosition-1);//Java月份才0开始算
		int dateOfMonth = cal.getActualMaximum(Calendar.DATE);
		return dateOfMonth;
	}
	
	/**
	 * 判断某日是星期几
	 * @param dt
	 * @return
	 */
	public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
	
	public static int getPositionOfCurWeek(String weekStr){
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		int position = 0;
		for(int i=0;i<weekDays.length;i++){
			if(weekStr.equals(weekDays[i])){
				position = i;
				break;
			}
		}
		return position;
	}
	
	/**
	 * 在当前日期的基础上加一个自然月
	 * @param nowTime
	 * @return
	 */
	public static String addMonthByNow(String nowTime){
		Date nowDate = null;
		try {
			nowDate = sdf.parse(nowTime);
		} catch (Exception e) {
			nowDate = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowDate);
		calendar.add(Calendar.MONTH, 1);
		return sdf.format(calendar.getTime());
	}
	
	/**
	 * 晚11点到凌晨1点之间元信宝是不能进行操作的。包括认购和赎回
	 * @return
	 * @throws Exception 
	 */
	public static boolean isYXBENabled(){
		long nowTimeL = System.currentTimeMillis();
		long todayNight11;
		long todayMorning1;
		try {
			todayNight11 = sdf.parse(sdf2.format(new Date())).getTime();
			todayMorning1 = sdf.parse(sdf3.format(new Date())).getTime();//当天凌晨1点的时间戳
			if(nowTimeL > todayNight11 || nowTimeL < todayMorning1){
				return false;
			}else{
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}//当天晚上11点的时间戳
		return true;
	}
	
	/**
	 * 计算两点之间的距离   （x,y） --- (cx,cy)
	 * @param x
	 * @param y
	 * @param cx
	 * @param cy
	 * @return
	 */
	public static double getDistance(int x,int y,int cx,int cy){
		int _x = Math.abs(x - cx);
		int _y = Math.abs(y - cy);
		return Math.sqrt(_x*_x+_y*_y);
	}
	
	/**
	 * 用逗号将数字型字符串每三位分割(整形)
	 * @param data
	 * @return
	 */
	public static String commaSpliteData(String data){
		String returnStr = "";
		try {
			double dataD = Double.parseDouble(data);
			DecimalFormat df = new DecimalFormat("#,###");
			returnStr = df.format(dataD);
		} catch (Exception e) {
		}
		return returnStr;
	}
	
	public static String htmlEncode(String s){
		String str = "";
		if(s.length() == 0)
			return "";
		 s = str.replace("/&/g", "&gt;");   
		 s = s.replace("/</g", "&lt;");   
		  s = s.replace("/>/g", "&gt;");   
		  s = s.replace("/ /g", "&nbsp;");   
		  s = s.replace("/\'/g", "&#39;");   
		  s = s.replace("/\"/g", "&quot;");   
		  s = s.replace("/\n/g", "<br>");   
		  return s;   

	}

	/**
	 * 初始化利率，将多余的0去除
	 * @param rate
	 * @return
	 */
	public static String formatRate(String rate){
		double rateD = 0d;
		try {
			rateD = Double.parseDouble(rate);
			if((rateD * 10)%10 == 0){
				//说明利率是整数，没有小数
				return String.valueOf((int)rateD);
			}else{
				if((rateD * 10)%1 == 0){
					return Util.double2PointDoubleOne(rateD);
				}else{
					return rate;
				}
			}
		} catch (Exception e) {
		}
		return rate;
	}
	
	/**
	 * 用逗号将数字型字符串每三位分割(浮点型，小数点后两位小数)
	 * @param data
	 * @return
	 */
	public static String commaSpliteDataFloat(String data){
		String returnStr = "";
		DecimalFormat df = null;
		try {
			double dataD = Double.parseDouble(data);
			if(dataD < 1){
				df = new DecimalFormat("0.00");
			}else{
				df = new DecimalFormat("#,###.00");
			}
			returnStr = df.format(dataD);
		} catch (Exception e) {
		}
		return returnStr;
	}
	
	/**
	 * 将double类型的数字转化成2位小数的字符串
	 * @param data
	 * @return
	 */
	public static String double2PointDouble(double data){
		DecimalFormat df = new DecimalFormat("#.00");
		if(data < 1){
			return "0"+df.format(data);
		}else{
			return df.format(data);
		}
	}
	
	/**
	 * 将double类型的数字转化成1位小数的字符串
	 * @param data
	 * @return
	 */
	public static String double2PointDoubleOne(double data){
		DecimalFormat df = new DecimalFormat("#.0");
		if(data < 1){
			return "0"+df.format(data);
		}else{
			return df.format(data);
		}
	}
	
	/**
	 * 将float类型的数字转成1位小数的字符串
	 * @param data
	 * @return
	 */
	public static String float2PointFloat(float data){
		DecimalFormat df = new DecimalFormat("#.0");
		if(data < 1){
			return "0"+df.format(data);
		}else{
			return df.format(data);
		}
	}
	
	/**
	 * 将数据库中moneyStatus转化成
	 * @return
	 * @throws ParseException 
	 */
	public static String MoneyStatusCG(ProductInfo productInfo) throws ParseException{
		long fullTimeL = sdf1.parse(productInfo.getFull_time()).getTime();//实际满标时间
		long todayL = sdf1.parse(sdf1.format(new Date())).getTime();
		int periodInt = Integer.parseInt(productInfo.getCollect_period());
		long planFullTimeL = sdf1.parse(productInfo.getStart_time()).getTime() + 1000*3600*24*periodInt;//计划满标时间
		if("已放款".equals(productInfo.getMoney_status())){
			if(todayL == fullTimeL){
				//如果当天满标
				return "已满标";
			}else if(todayL == planFullTimeL && todayL <= fullTimeL){
				//如果当天是预计满标时间且实际满标时间是当天或者晚于当天
				return "已满标";
			}else{
				return "还款中";
			}
		}else if("未满标".equals(productInfo.getMoney_status())){
			return "募集中";
		}else if("已满标".equals(productInfo.getMoney_status())){
			return "已满标";
		}else if("已还款".equals(productInfo.getMoney_status())){
			return "已兑付";
		}
		return "";
	}

	public static String replaceUrl(String string) {
		String[] sources = { "\\" };
		String[] destinations = { "" };
		String result = TextUtils.replace(string, sources, destinations).toString();
		return result;
	}
	
	public static void hiddenSoftInputWindow(Activity activity){
		((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
		hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
	}
	
	public static String hiddenBankCard(String bankCard){
		int length = bankCard.length();
		String hiddenBankCard = bankCard.substring(0, 4) + " ******** " + bankCard.substring(length - 4, length);
		return hiddenBankCard;
	}
	
	public static String hiddenUsername(String username){
		int length = username.length();
		String hiddenUsername = "";
		try {
			hiddenUsername = username.substring(0, 3) + "****" + username.substring(length - 3, length);
		} catch (Exception e) {
		}
		return hiddenUsername;
	}
	
	public static String hidPhoneNum(String phone){
		int length = phone.length();
		String hiddenPhone = "";
		if(length > 4){
			hiddenPhone = phone.substring(0, 3) + "****" + phone.substring(length - 4, length);
		}
		return hiddenPhone;
	}
	
	/**
	 * 隐藏姓
	 * @param name
	 * @return
	 */
	public static String hidRealName(String name){
		String hiddenRealName = null;
		if(name != null){
			int length = name.length();
			if(length > 1){
				hiddenRealName = "*"+name.substring(1, length);
			}
		}
		
		return hiddenRealName;
	}
	
	/**
	 * 隐藏名字
	 * @param realName
	 * @return
	 */
	public static String hidRealName2(String realName){
		String hiddenRealName = null;
		if(realName != null){
			int length = realName.length();
			if(length > 1){
				hiddenRealName = realName.substring(0, 1) + "**";
			}else{
				hiddenRealName = realName;
			}
		}
		
		return hiddenRealName;
	}
	
	/**
	 * 隐藏身份证号码
	 * @param idnumber
	 * @return
	 */
	public static String hidIdNumber(String idnumber){
		String hiddenIdnumber = null;
		if(idnumber != null){
			int length = idnumber.length();
			if(length > 14){
				hiddenIdnumber = idnumber.substring(0, 6) + " ******** " + idnumber.substring(length - 4, length);
			}
		}
		return hiddenIdnumber;
	}
	
	/**
	 * Check to see if the given number is mobile No. or not.
	 * @param phoneNo
	 * @return true - if is mobile No. false - illegal mobile No.
	 */
	public static boolean checkPhoneNumber(String phoneNo) {
		
		if (phoneNo == null || phoneNo.length() == 0)
			return false;
		
		String regex	= "^[1][0-9]{10}$";
		Pattern pattern	= Pattern.compile(regex);
		Matcher matcher	= pattern.matcher(phoneNo);
		return matcher.find();
	}
	
	public static boolean checkPassword(String password) {
    	if(password.length()>=6&&password.length()<=16&&password.indexOf(" ")<=0)
    		return true;
    	else
    		return false;
	}
	
	/**
	 * 显示长时间提示Toast
	 * 
	 * @param context
	 * @param text
	 */
	public static void toastLong(Context context, String text) {
		showToast(context, text, Toast.LENGTH_LONG);
	}

	/**
	 * 显示短时间提示Toast
	 * 
	 * @param context
	 * @param text
	 */
	public static void toastShort(Context context, String text) {
		showToast(context, text, Toast.LENGTH_SHORT);
	}
	
	private static void showToast(Context context, String msg, int duration) {
		// 空值判断
		if (context == null) {
			return;
		}
		if (TextUtils.isEmpty(msg)) {
			return;
		}
		handler.removeCallbacks(run);
		int delayedTime = 0;
		switch (duration) {
		case Toast.LENGTH_SHORT:// Toast.LENGTH_SHORT值为0，对应的持续时间大概为1s
			delayedTime = 1000;
			break;
		case Toast.LENGTH_LONG:// Toast.LENGTH_LONG值为1，对应的持续时间大概为3s
			delayedTime = 3000;
			break;
			
		default:
			break;
		}
		if (mToast == null) {
			mToast = Toast.makeText(context, msg, duration);
//			mToast = new Toast(context);
		}else{
			mToast.setText(msg);
			mToast.setDuration(duration);
		}
		handler.postDelayed(run, delayedTime);
		mToast.show();
	}

	/**
	 * Convert the seconds integer into readable form.
	 * @param time - seconds
	 * @return readable seconds string.
	 */
    public static String turnDisplayTime(int time)
    {
    	int minute=time/1000/60;
    	int second=(time/1000%60)==0?1:(time/1000%60);
    	String disPlayMinute=String.valueOf(minute);
    	String disPlaySecond=String.valueOf(second);
    	if(minute<10)
    		disPlayMinute = "0" + String.valueOf(minute);
    	if(second<10)
    		disPlaySecond = "0" + String.valueOf(second);
    	String displayTime = disPlayMinute+":"+disPlaySecond;
    	return displayTime;
    }
    
    /**
    *md5摘要
    * @param plainText
    *            明文
    * @return 32位密文
    */
   public static String md5Encryption(String plainText) {
       String re_md5 = new String();
       try {
           MessageDigest md = MessageDigest.getInstance("MD5");
           md.update(plainText.getBytes());
           byte b[] = md.digest();
           int i;

           StringBuffer buf = new StringBuffer("");
           for (int offset = 0; offset < b.length; offset++) {
               i = b[offset];
               if (i < 0)
                   i += 256;
               if (i < 16)
                   buf.append("0");
               buf.append(Integer.toHexString(i));
           }

           re_md5 = buf.toString();

       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
       }
       return re_md5;
   }
   
   /**
	 * 获取客户端版本号
	 * 
	 * @param activity
	 * @return
	 */
	public static int getClientVersion(Context activity) {
		int ClientVersion = -1;
		try {
			PackageManager packagemanager = activity.getPackageManager();
			String packageName = activity.getPackageName();
			ClientVersion = packagemanager.getPackageInfo(packageName, 0).versionCode;
		} catch (NameNotFoundException ex) {
			ex.printStackTrace();
		}
		return -1 == ClientVersion ? -1 : ClientVersion;
	}
	
	public static String getVersionName(Context context) {
		String version = null;
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}
	
	/**
	 * 时间字符串转date
	 * @param dateStr
	 * @return
	 */
	public static long string2date(String dateStr){
		Date date = null;
		try {
			date = sdf.parse(dateStr);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 获取分辨率 宽和高
	 * 
	 * @param activity
	 * @return screens[0]宽 screens[1]高
	 */
	public static int[] getScreenWidthAndHeight(Context activity) {
		int[] screens = new int[2];
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) activity).getWindowManager().getDefaultDisplay().getMetrics(dm);
		screens[0] = dm.widthPixels;
		screens[1] = dm.heightPixels;
		return screens;
	}

	
	/**
	 * 获取gridview的item宽度，
	 * @param activity
	 */
	public static int getGridViewItemWidth(Context activity) {
		// 屏幕分辨率
		int location[] = Util.getScreenWidthAndHeight(activity);
		// gridView marginLeft
		int marginLeft = activity.getResources().getDimensionPixelSize(
				R.dimen.common_measure_20dp);
		// gridView marginRight
		int marginRight = activity.getResources().getDimensionPixelSize(
				R.dimen.common_measure_20dp);
		// horizontalSpace
		int horizontalSpace = activity.getResources().getDimensionPixelSize(
				R.dimen.common_measure_15dp);
		int width = location[0] - (marginLeft + marginRight + horizontalSpace);
		return width/2;
	}
	
	
	/**
     * Hides the input method.
     * 
     * @param context context
     * @param view The currently focused view
     * @return success or not.
     */
    public static boolean hideInputMethod(Context context, View view) {
        if (context == null || view == null) {
            return false;
        }

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        return false;
    }

    /**
     * Show the input method.
     * 
     * @param context context
     * @param view The currently focused view, which would like to receive soft keyboard input
     * @return success or not.
     */
    public static boolean showInputMethod(Context context, View view) {
        if (context == null || view == null) {
            return false;
        }

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.showSoftInput(view, 0);
        }

        return false;
    }

    public static float pixelToDp(Context context, float val) {
        float density = context.getResources().getDisplayMetrics().density;
        return val * density;
    }
    
    public static String getHashedFileName(String url) {   
        if (url == null || url.endsWith("/" )) {
            return null ;
        }

        String suffix = getSuffix(url);
        StringBuilder sb = null;
        
        try {
            MessageDigest digest = MessageDigest. getInstance("MD5");
            byte[] dstbytes = digest.digest(url.getBytes("UTF-8")); // GMaFroid uses UTF-16LE
            sb = new StringBuilder();
            for (int i = 0; i < dstbytes.length; i++) {
                sb.append(Integer. toHexString(dstbytes[i] & 0xff));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (null != sb && null != suffix) {
            return sb.toString() + "." + suffix;
        }
       
        return null;
    }
    
    private static String getSuffix(String fileName) {
        int dot_point = fileName.lastIndexOf( ".");
        int sl_point = fileName.lastIndexOf( "/");
        if (dot_point < sl_point) {
            return "" ;
        }
        
        if (dot_point != -1) {
            return fileName.substring(dot_point + 1);
        }
        
        return null;
    }
    
    /**
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     *
     * @param context The application's environment.
     * @param intent The Intent action to check for availability.
     *
     * @return True if an Intent with the specified action can be sent and
     *         responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();

        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        return list.size() > 0;
    }
}
