package com.ylfcf.ppp.util;

import android.content.Context;
import android.os.AsyncTask;

import com.ylfcf.ppp.async.AsyncUserBankCard;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.async.AsyncYXBInvestRecord;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserCardInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.inter.Inter.OnIsVipUserListener;
import com.ylfcf.ppp.inter.Inter.OnIsYXBInvestorListener;
import com.ylfcf.ppp.inter.Inter.OnUserBankCardInter;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;

/**
 * 接口请求的API
 * @author Mr.liu
 *
 */
public class RequestApis {
	/**
	 * 请求用户是否已经实名认证接口
	 * @param userId 用户iD
	 * @param verifyListener 是否实名认证的接口回调
	 */
	public static void requestIsVerify(Context context,String userId,final OnIsVerifyListener verifyListener){
		AsyncUserSelectOne task = new AsyncUserSelectOne(context, userId, "", "", new OnGetUserInfoByPhone() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo == null){
					baseInfo = new BaseInfo();
					baseInfo.setMsg("您的网络不给力");
				}
				int resultCode = SettingsManager.getResultCode(baseInfo);
				if(resultCode == 0){
					UserInfo userInfo = baseInfo.getUserInfo();
					if(userInfo != null && !"".equals(userInfo.getReal_name()) && !"".equals(userInfo.getId_number())){
						//已经实名认证
						verifyListener.isVerify(true, userInfo);
					}else{
						verifyListener.isVerify(false, baseInfo.getMsg());
					}
					if(userInfo != null && !"".equals(userInfo.getDeal_pwd())){
						//已经设置提现密码
						verifyListener.isSetWithdrawPwd(true, userInfo);
					}else{
						verifyListener.isSetWithdrawPwd(false, baseInfo.getMsg());
					}
				}else{
					verifyListener.isVerify(false, baseInfo.getMsg());
					verifyListener.isSetWithdrawPwd(false, baseInfo.getMsg());
				}
			}
		});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 判断是否为VIP用户
	 * @param context
	 * @param userId
	 * @param vipListener
	 */
	public static void requestIsVip(Context context,String userId,final OnIsVipUserListener vipListener){
		AsyncUserSelectOne task = new AsyncUserSelectOne(context, userId, "", "", new OnGetUserInfoByPhone() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo == null){
					baseInfo = new BaseInfo();
					baseInfo.setMsg("您的网络不给力");
				}
				int resultCode = SettingsManager.getResultCode(baseInfo);
				if(resultCode == 0){
					UserInfo userInfo = baseInfo.getUserInfo();
					if(userInfo != null && userInfo.getType().contains("vip")){
						//已经实名认证
						vipListener.isVip(true);
					}else{
						vipListener.isVip(false);
					}
				}else{
					vipListener.isVip(false);
				}
			}
		});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 请求用户是否已经绑卡接口
	 * @param context
	 * @param userId 用户ID
	 * @param type  枚举类型：宝付
	 * @param bindindListener 接口请求成功后的回调接口
	 */
	public static void requestIsBinding(Context context,String userId,String type,final OnIsBindingListener bindindListener){
		AsyncUserBankCard task = new AsyncUserBankCard(context, userId, type, new OnUserBankCardInter() {
			@Override
			public void back(BaseInfo info) {
				if(info == null){
					info = new BaseInfo();
					info.setMsg("您的网络不给力");
				}
				int resultCode = SettingsManager.getResultCode(info);
				if (resultCode == 0) {
					UserCardInfo bankCardInfo = info.getUserCardInfo();
					if (bankCardInfo != null && "是".equals(bankCardInfo.getIs_binding())) {
						//已绑卡
						bindindListener.isBinding(true, bankCardInfo);
					} else {
						//未绑卡
						bindindListener.isBinding(false, info.getMsg());
					}
				} else {
					//请求失败，如网络原因等等
					bindindListener.isBinding(false, info.getMsg());
				} 
			}
		});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 判断是否投资过元信宝
	 * @param userId
	 * @param page
	 * @param pageSize
	 */
	public static void requestIsYXBInvestor(Context context,String userId,int page,int pageSize,final OnIsYXBInvestorListener listener){
		AsyncYXBInvestRecord yxbInvestTask = new AsyncYXBInvestRecord(context, "", userId, "", String.valueOf(page), String.valueOf(pageSize), 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								listener.isYXBInvestor(true);
							}else{
								listener.isYXBInvestor(false);
							}
						}else{
							listener.isYXBInvestor(false);
						}
					}
				});
		yxbInvestTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
