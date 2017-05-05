package com.ylfcf.ppp.entity;

/**
 * 短信模板类型
 * @author Administrator
 *
 */
public class SMSType {
	public static String SMS_REGISTER = "register_code";//注册验证码
	public static String SMS_FORGET_PASSWORD = "app_forget_pwd";//忘记密码页面的找回密码
	public static String SMS_GETBACK_DEAL_PASSWORD = "forgot_trading_pwd";//找回交易密码
	public static String SMS_REGISTER_SUCCESS = "register_success";//注册成功
	public static String SMS_INVEST_SUCCESS = "invest_success";//投资成功
	public static String SMS_WITHDRAW_APPLY = "apply_cash";//申请提现
	public static String SMS_SETTING_DEAL_PWD = "pwd_code";//设置提现密码验证码
}
