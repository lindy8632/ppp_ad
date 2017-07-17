package com.ylfcf.ppp.entity;

import java.util.List;

/**
 * 基础类  Json解析对象
 * @author Mr.liu
 *
 */
public class BaseInfo implements java.io.Serializable{

	private static final long serialVersionUID = 7558055137995192708L;
	
	private String time;
	/*
	 * 网络请求返回的错误编号 0表示成功
	 */
	private String error_id;
	private String error;
	/*
	 * 网络请求的结果描述
	 */
	private String msg;

	private ProjectInfo mProjectInfo;
	private UserInfo userInfo;
	private UserCardInfo userCardInfo;
	private ProductPageInfo productPageInfo;
	private ProductInfo mProductInfo;
	private RechargeOrderInfo rechargeOrderInfo;
	private WithdrawOrderPageInfo withdrawOrderPageInfo;
	private FundsDetailsPageInfo fundsDetailsPageInfo;
	private UserRMBAccountInfo rmbAccountInfo;
	private StatisticInfo mStatisticInfo;
	private InvestRecordPageInfo mInvestRecordPageInfo;//投资记录
	private UserYUANAccountInfo yuanAccountInfo;//元金币账户
	private TYJPageInfo mTYJPageInfo;//体验金
	private ExtensionPageInfo extensionPageInfo;//推广收益
	private ExtensionNewPageInfo extensionNewPageInfo;//推广收益（新的接口）
	private PrizePageInfo mPrizePageInfo;//奖品
	private PrizeActiveInfo mPrizeActiveInfo;//奖品活动
	private ArticleInfo mArticleInfo;
	private ArticlePageInfo mArticlePageInfo;
	private BannerPageInfo mBannerPageInfo;//banner
	private AccountTotalInfo accountTotalInfo;
	private YXBProductInfo yxbProductInfo;//元信宝
	private YXBProductLogInfo yxbProductLogInfo;//元信宝每日统计
	private YXBUserAccountInfo yxbUserAccountInfo;//元信宝用户中心
	private YXBRedeemRecordPageInfo yxbRedeemRecordPageInfo;//元信宝赎回记录
	private YXBInvestRecordPageInfo yxbInvestRecordPageInfo;//元信宝认购记录
	private YXBCheckRedeemInfo yxbCheckRedeemInfo;
	private RedBagPageInfo mRedBagPageInfo;//我的红包
	private AssociatedCompanyParentInfo associatedCompanyParentInfo;//关联公司
	private SRZXAppointRecordPageInfo mSRZXAppointRecordPageInfo;//私人尊享产品预约记录
	private List<SRZXAppointRecordInfo> srzxAppointRecordList;//私人尊享产品预约记录列表
	private JiaxiquanPageInfo mJiaxiquanPageInfo;//加息券列表
	private JiaxiquanInfo mJiaxiquanInfo;//加息券详情
	private BankPageInfo bankPageInfo;//
	private PrizeInfo mPrizeInfo;//奖品
	private PrizeCodeInfo mPrizeCodeInfo;
	private XCFLDrawInfo xcflDrawInfo;//17年领压岁钱 领奖数据
	private JXQRuleInfo mJXQRuleInfo;//加息券使用规则
	private WDYChildRecordPageInfo mWDYChildRecordPageInfo;
	private RechargeRecordPageInfo mRechargeRecordPageInfo;
	private RechargeResultInfo mRechargeResultInfo;
	private SignResultInfo signResultInfo;
	private GiftPageInfo mGiftPageInfo;//礼品列表
	private GiftCodeInfo mGiftCodeInfo;
	private HDPrizePageInfo mHDPrizePageInfo;
	private InvestResultInfo mInvestResultInfo;
	private LotteryCodeInfo mLotteryCodeInfo;
	private RobcashMoneyInfo mRobcashMoneyInfo;
	private ActivePageInfo mActivePageInfo;
	private PopBannerInfo mPopBannerInfo;
	private FriendsPageInfo mFriendsPageInfo;

	public FriendsPageInfo getmFriendsPageInfo() {
		return mFriendsPageInfo;
	}

	public void setmFriendsPageInfo(FriendsPageInfo mFriendsPageInfo) {
		this.mFriendsPageInfo = mFriendsPageInfo;
	}

	public ProjectInfo getmProjectInfo() {
		return mProjectInfo;
	}

	public void setmProjectInfo(ProjectInfo mProjectInfo) {
		this.mProjectInfo = mProjectInfo;
	}

	public PopBannerInfo getmPopBannerInfo() {
		return mPopBannerInfo;
	}

	public void setmPopBannerInfo(PopBannerInfo mPopBannerInfo) {
		this.mPopBannerInfo = mPopBannerInfo;
	}

	public ActivePageInfo getmActivePageInfo() {
		return mActivePageInfo;
	}

	public void setmActivePageInfo(ActivePageInfo mActivePageInfo) {
		this.mActivePageInfo = mActivePageInfo;
	}

	public RobcashMoneyInfo getmRobcashMoneyInfo() {
		return mRobcashMoneyInfo;
	}

	public void setmRobcashMoneyInfo(RobcashMoneyInfo mRobcashMoneyInfo) {
		this.mRobcashMoneyInfo = mRobcashMoneyInfo;
	}

	public LotteryCodeInfo getmLotteryCodeInfo() {
		return mLotteryCodeInfo;
	}

	public void setmLotteryCodeInfo(LotteryCodeInfo mLotteryCodeInfo) {
		this.mLotteryCodeInfo = mLotteryCodeInfo;
	}

	public InvestResultInfo getmInvestResultInfo() {
		return mInvestResultInfo;
	}

	public void setmInvestResultInfo(InvestResultInfo mInvestResultInfo) {
		this.mInvestResultInfo = mInvestResultInfo;
	}

	public HDPrizePageInfo getmHDPrizePageInfo() {
		return mHDPrizePageInfo;
	}
	public void setmHDPrizePageInfo(HDPrizePageInfo mHDPrizePageInfo) {
		this.mHDPrizePageInfo = mHDPrizePageInfo;
	}
	public GiftCodeInfo getmGiftCodeInfo() {
		return mGiftCodeInfo;
	}
	public void setmGiftCodeInfo(GiftCodeInfo mGiftCodeInfo) {
		this.mGiftCodeInfo = mGiftCodeInfo;
	}
	public GiftPageInfo getmGiftPageInfo() {
		return mGiftPageInfo;
	}
	public void setmGiftPageInfo(GiftPageInfo mGiftPageInfo) {
		this.mGiftPageInfo = mGiftPageInfo;
	}
	public SignResultInfo getSignResultInfo() {
		return signResultInfo;
	}
	public void setSignResultInfo(SignResultInfo signResultInfo) {
		this.signResultInfo = signResultInfo;
	}
	public RechargeResultInfo getmRechargeResultInfo() {
		return mRechargeResultInfo;
	}
	public void setmRechargeResultInfo(RechargeResultInfo mRechargeResultInfo) {
		this.mRechargeResultInfo = mRechargeResultInfo;
	}
	public RechargeRecordPageInfo getmRechargeRecordPageInfo() {
		return mRechargeRecordPageInfo;
	}
	public void setmRechargeRecordPageInfo(
			RechargeRecordPageInfo mRechargeRecordPageInfo) {
		this.mRechargeRecordPageInfo = mRechargeRecordPageInfo;
	}
	public WDYChildRecordPageInfo getmWDYChildRecordPageInfo() {
		return mWDYChildRecordPageInfo;
	}
	public void setmWDYChildRecordPageInfo(
			WDYChildRecordPageInfo mWDYChildRecordPageInfo) {
		this.mWDYChildRecordPageInfo = mWDYChildRecordPageInfo;
	}
	public JXQRuleInfo getmJXQRuleInfo() {
		return mJXQRuleInfo;
	}
	public void setmJXQRuleInfo(JXQRuleInfo mJXQRuleInfo) {
		this.mJXQRuleInfo = mJXQRuleInfo;
	}
	public XCFLDrawInfo getXcflDrawInfo() {
		return xcflDrawInfo;
	}
	public void setXcflDrawInfo(XCFLDrawInfo xcflDrawInfo) {
		this.xcflDrawInfo = xcflDrawInfo;
	}
	public PrizeCodeInfo getmPrizeCodeInfo() {
		return mPrizeCodeInfo;
	}
	public void setmPrizeCodeInfo(PrizeCodeInfo mPrizeCodeInfo) {
		this.mPrizeCodeInfo = mPrizeCodeInfo;
	}
	public PrizeInfo getmPrizeInfo() {
		return mPrizeInfo;
	}
	public void setmPrizeInfo(PrizeInfo mPrizeInfo) {
		this.mPrizeInfo = mPrizeInfo;
	}
	public List<SRZXAppointRecordInfo> getSrzxAppointRecordList() {
		return srzxAppointRecordList;
	}
	public void setSrzxAppointRecordList(
			List<SRZXAppointRecordInfo> srzxAppointRecordList) {
		this.srzxAppointRecordList = srzxAppointRecordList;
	}
	public BankPageInfo getBankPageInfo() {
		return bankPageInfo;
	}
	public void setBankPageInfo(BankPageInfo bankPageInfo) {
		this.bankPageInfo = bankPageInfo;
	}
	public JiaxiquanInfo getmJiaxiquanInfo() {
		return mJiaxiquanInfo;
	}
	public void setmJiaxiquanInfo(JiaxiquanInfo mJiaxiquanInfo) {
		this.mJiaxiquanInfo = mJiaxiquanInfo;
	}
	public JiaxiquanPageInfo getmJiaxiquanPageInfo() {
		return mJiaxiquanPageInfo;
	}
	public void setmJiaxiquanPageInfo(JiaxiquanPageInfo mJiaxiquanPageInfo) {
		this.mJiaxiquanPageInfo = mJiaxiquanPageInfo;
	}
	public SRZXAppointRecordPageInfo getmSRZXAppointRecordPageInfo() {
		return mSRZXAppointRecordPageInfo;
	}
	public void setmSRZXAppointRecordPageInfo(
			SRZXAppointRecordPageInfo mSRZXAppointRecordPageInfo) {
		this.mSRZXAppointRecordPageInfo = mSRZXAppointRecordPageInfo;
	}
	public AssociatedCompanyParentInfo getAssociatedCompanyParentInfo() {
		return associatedCompanyParentInfo;
	}
	public void setAssociatedCompanyParentInfo(
			AssociatedCompanyParentInfo associatedCompanyParentInfo) {
		this.associatedCompanyParentInfo = associatedCompanyParentInfo;
	}
	public ArticlePageInfo getmArticlePageInfo() {
		return mArticlePageInfo;
	}
	public void setmArticlePageInfo(ArticlePageInfo mArticlePageInfo) {
		this.mArticlePageInfo = mArticlePageInfo;
	}
	public RedBagPageInfo getmRedBagPageInfo() {
		return mRedBagPageInfo;
	}
	public void setmRedBagPageInfo(RedBagPageInfo mRedBagPageInfo) {
		this.mRedBagPageInfo = mRedBagPageInfo;
	}
	public YXBCheckRedeemInfo getYxbCheckRedeemInfo() {
		return yxbCheckRedeemInfo;
	}
	public void setYxbCheckRedeemInfo(YXBCheckRedeemInfo yxbCheckRedeemInfo) {
		this.yxbCheckRedeemInfo = yxbCheckRedeemInfo;
	}
	public YXBInvestRecordPageInfo getYxbInvestRecordPageInfo() {
		return yxbInvestRecordPageInfo;
	}
	public void setYxbInvestRecordPageInfo(
			YXBInvestRecordPageInfo yxbInvestRecordPageInfo) {
		this.yxbInvestRecordPageInfo = yxbInvestRecordPageInfo;
	}
	public YXBRedeemRecordPageInfo getYxbRedeemRecordPageInfo() {
		return yxbRedeemRecordPageInfo;
	}
	public void setYxbRedeemRecordPageInfo(
			YXBRedeemRecordPageInfo yxbRedeemRecordPageInfo) {
		this.yxbRedeemRecordPageInfo = yxbRedeemRecordPageInfo;
	}
	public YXBUserAccountInfo getYxbUserAccountInfo() {
		return yxbUserAccountInfo;
	}
	public void setYxbUserAccountInfo(YXBUserAccountInfo yxbUserAccountInfo) {
		this.yxbUserAccountInfo = yxbUserAccountInfo;
	}
	public YXBProductLogInfo getYxbProductLogInfo() {
		return yxbProductLogInfo;
	}
	public void setYxbProductLogInfo(YXBProductLogInfo yxbProductLogInfo) {
		this.yxbProductLogInfo = yxbProductLogInfo;
	}
	public YXBProductInfo getYxbProductInfo() {
		return yxbProductInfo;
	}
	public void setYxbProductInfo(YXBProductInfo yxbProductInfo) {
		this.yxbProductInfo = yxbProductInfo;
	}
	public AccountTotalInfo getAccountTotalInfo() {
		return accountTotalInfo;
	}
	public void setAccountTotalInfo(AccountTotalInfo accountTotalInfo) {
		this.accountTotalInfo = accountTotalInfo;
	}
	public BannerPageInfo getmBannerPageInfo() {
		return mBannerPageInfo;
	}
	public void setmBannerPageInfo(BannerPageInfo mBannerPageInfo) {
		this.mBannerPageInfo = mBannerPageInfo;
	}
	public ArticleInfo getmArticleInfo() {
		return mArticleInfo;
	}
	public void setmArticleInfo(ArticleInfo mArticleInfo) {
		this.mArticleInfo = mArticleInfo;
	}
	public RechargeOrderInfo getRechargeOrderInfo() {
		return rechargeOrderInfo;
	}
	public void setRechargeOrderInfo(RechargeOrderInfo rechargeOrderInfo) {
		this.rechargeOrderInfo = rechargeOrderInfo;
	}
	public TYJPageInfo getmTYJPageInfo() {
		return mTYJPageInfo;
	}
	public void setmTYJPageInfo(TYJPageInfo mTYJPageInfo) {
		this.mTYJPageInfo = mTYJPageInfo;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getError_id() {
		return error_id;
	}
	public void setError_id(String error_id) {
		this.error_id = error_id;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public UserCardInfo getUserCardInfo() {
		return userCardInfo;
	}
	public void setUserCardInfo(UserCardInfo userCardInfo) {
		this.userCardInfo = userCardInfo;
	}
	public ProductPageInfo getProductPageInfo() {
		return productPageInfo;
	}
	public void setProductPageInfo(ProductPageInfo productPageInfo) {
		this.productPageInfo = productPageInfo;
	}
	public WithdrawOrderPageInfo getWithdrawOrderPageInfo() {
		return withdrawOrderPageInfo;
	}
	public void setWithdrawOrderPageInfo(WithdrawOrderPageInfo withdrawOrderPageInfo) {
		this.withdrawOrderPageInfo = withdrawOrderPageInfo;
	}
	public FundsDetailsPageInfo getFundsDetailsPageInfo() {
		return fundsDetailsPageInfo;
	}
	public void setFundsDetailsPageInfo(FundsDetailsPageInfo fundsDetailsPageInfo) {
		this.fundsDetailsPageInfo = fundsDetailsPageInfo;
	}
	public UserRMBAccountInfo getRmbAccountInfo() {
		return rmbAccountInfo;
	}
	public void setRmbAccountInfo(UserRMBAccountInfo rmbAccountInfo) {
		this.rmbAccountInfo = rmbAccountInfo;
	}
	public StatisticInfo getmStatisticInfo() {
		return mStatisticInfo;
	}
	public void setmStatisticInfo(StatisticInfo mStatisticInfo) {
		this.mStatisticInfo = mStatisticInfo;
	}
	public InvestRecordPageInfo getmInvestRecordPageInfo() {
		return mInvestRecordPageInfo;
	}
	public void setmInvestRecordPageInfo(InvestRecordPageInfo mInvestRecordPageInfo) {
		this.mInvestRecordPageInfo = mInvestRecordPageInfo;
	}
	public UserYUANAccountInfo getYuanAccountInfo() {
		return yuanAccountInfo;
	}
	public void setYuanAccountInfo(UserYUANAccountInfo yuanAccountInfo) {
		this.yuanAccountInfo = yuanAccountInfo;
	}
	public ExtensionPageInfo getExtensionPageInfo() {
		return extensionPageInfo;
	}
	public void setExtensionPageInfo(ExtensionPageInfo extensionPageInfo) {
		this.extensionPageInfo = extensionPageInfo;
	}
	public PrizePageInfo getmPrizePageInfo() {
		return mPrizePageInfo;
	}
	public void setmPrizePageInfo(PrizePageInfo mPrizePageInfo) {
		this.mPrizePageInfo = mPrizePageInfo;
	}
	public PrizeActiveInfo getmPrizeActiveInfo() {
		return mPrizeActiveInfo;
	}
	public void setmPrizeActiveInfo(PrizeActiveInfo mPrizeActiveInfo) {
		this.mPrizeActiveInfo = mPrizeActiveInfo;
	}
	public ProductInfo getmProductInfo() {
		return mProductInfo;
	}
	public void setmProductInfo(ProductInfo mProductInfo) {
		this.mProductInfo = mProductInfo;
	}
	public ExtensionNewPageInfo getExtensionNewPageInfo() {
		return extensionNewPageInfo;
	}
	public void setExtensionNewPageInfo(ExtensionNewPageInfo extensionNewPageInfo) {
		this.extensionNewPageInfo = extensionNewPageInfo;
	}
	
}
