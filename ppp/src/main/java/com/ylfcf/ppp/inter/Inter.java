package com.ylfcf.ppp.inter;

import com.ylfcf.ppp.entity.AppInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordPageInfo;
import com.ylfcf.ppp.entity.ProductPageInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.entity.UserCardInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.entity.UserYUANAccountInfo;

/**
 * 回调接口类
 * @author Administrator
 *
 */
public class Inter {
	
	public interface OnApiQueryBack{
		void back(AppInfo object);
	}
	
	/**
	 * 通用的回调接口
	 * @author Administrator
	 *
	 */
	public interface OnCommonInter{
		void back(BaseInfo baseInfo);
	}
	
	/**
	 * 注册
	 * @author Administrator
	 *
	 */
	public interface OnRegisteInter{
		void back(BaseInfo baseInfo);
	}
	
	/**
	 * 登录
	 * @author Administrator
	 *
	 */
	public interface OnLoginInter{
		void back(BaseInfo baseInfo);
	}
	
	/**
	 * 用户的人民币账户
	 * @author Administrator
	 *
	 */
	public interface OnUserRMBAccountInter{
		void back(UserRMBAccountInfo info);
	}
	
	/**
	 * 用户的元金币账户
	 * @author Administrator
	 *
	 */
	public interface OnUserYUANAccountInter{
		void back(BaseInfo info);
	}
	
	/**
	 * 投资
	 * @author Administrator
	 *
	 */
	public interface OnBorrowInvestInter{
		void back(BaseInfo baseInfo);
	}
	
	/**
	 * 投资记录列表
	 * @author Administrator
	 *
	 */
	public interface OnInvestRecordListInter{
		void back(InvestRecordPageInfo pageInfo);
	}
	
	/**
	 * 更新用户信息
	 * @author Administrator
	 *
	 */
	public interface OnUpdateUserInfoInter{
		void back(BaseInfo baseInfo);
	}
	
	/**
	 * 根据手机号码获取用户信息
	 * @author Administrator
	 *
	 */
	public interface OnGetUserInfoByPhone{
		void back(BaseInfo baseInfo);
	}
	
	/**
	 * 项目详情
	 * @author Administrator
	 */
	public interface OnProjectDetails{
		void back(ProjectInfo projectInfo);
	}
	
	/**
	 * 用户银行卡信息
	 * @author Administrator
	 *
	 */
	public interface OnUserBankCardInter{
		void back(BaseInfo info);
	}
	
	/**
	 * 用户是否已经实名认证
	 * @author Mr.liu
	 */
	public interface OnIsVerifyListener{
		/*
		 * 用户是否已经实名认证
		 */
		void isVerify(boolean flag ,Object object);
		/*
		 * 用户是否已经设置交易密码
		 */
		void isSetWithdrawPwd(boolean flag,Object object);
	}
	
	/**
	 * 用户是否已绑卡
	 * @author Mr.liu
	 */
	public interface OnIsBindingListener{
		void isBinding(boolean flag, Object object);
	}
	
	/**
	 * 是否为VIP用户
	 * @author Mr.liu
	 *
	 */
	public interface OnIsVipUserListener{
		void isVip(boolean isvip);
	}
	
	/**
	 * 用户是否投资过元信宝
	 * @author Mr.liu
	 *
	 */
	public interface OnIsYXBInvestorListener{
		void isYXBInvestor(boolean flag);
	}
}
