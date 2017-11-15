package com.ylfcf.ppp.util;

import java.io.UnsupportedEncodingException;

/**
 * URL类
 * @author Administrator
 *
 */
public class URLGenerator {
	//正式环境
	private static final String API_DOMAIN_URL = "http://www.ylfcf.com";//API环境
	private static final String WAP_DOMAIN_URL = "http://wap.ylfcf.com";//WAP环境
	private static final String API2_DOMAIN_URL = "http://api.ylfcf.com";//

	//https测试环境
//	private static final String API_DOMAIN_URL = "https://test1.ylfcf.com";//API环境
//	private static final String WAP_DOMAIN_URL = "https://wap.test1.ylfcf.com";//WAP环境
//	private static final String API2_DOMAIN_URL = "https://api.test1.ylfcf.com";//

	//验证环境
//	private static final String API_DOMAIN_URL = "http://www.dev.ylfcf.com";//API环境
//	private static final String WAP_DOMAIN_URL = "http://dev.wap.ylfcf.com";//WAP环境
//	private static final String API2_DOMAIN_URL = "http://api.dev.ylfcf.com";//

	//测试环境
//	private static final String API_DOMAIN_URL = "http://www.test.ylfcf.com";//API环境
//	private static final String WAP_DOMAIN_URL = "http://wap.test.ylfcf.com";//WAP环境
//	private static final String API2_DOMAIN_URL = "http://api.dev.ylfcf.com";//

	//朱礼涛开发环境
//	private static final String API_DOMAIN_URL = "http://www.ylf.com";//API环境
//	private static final String WAP_DOMAIN_URL = "http://www.ylf_chat.com";//WAP环境
//	private static final String API2_DOMAIN_URL = "http://api.dev.ylfcf.com";//

	//徐卫兵开发环境
//	private static final String API_DOMAIN_URL = "http://www.m_ylf.com";//API环境
//	private static final String WAP_DOMAIN_URL = "http://www.ylf_chat.com";//WAP环境
//	private static final String API2_DOMAIN_URL = "http://api.dev.ylfcf.com";//

	//杨永豪开发环境
//	private static final String API_DOMAIN_URL = "http://www.api.com";//API环境
//	private static final String WAP_DOMAIN_URL = "http://www.ylf_chat.com";//WAP环境
//	private static final String API2_DOMAIN_URL = "http://api.dev.ylfcf.com";//

//	private static final String API_DOMAIN_URL = "http://www.web";//API环境
//	private static final String WAP_DOMAIN_URL = "http://www.ylf_chat.com";//WAP环境
//	private static final String API2_DOMAIN_URL = "http://api.dev.ylfcf.com";//

	//https
//	private static final String API_DOMAIN_URL = "https://test1.ylfcf.com";
//	private static final String WAP_DOMAIN_URL = "https://wap.test.ylfcf.com";//WAP环境
//	private static final String API2_DOMAIN_URL = "https://api.dev.ylfcf.com";//

	//开发环境局域网
//	private static final String API_DOMAIN_URL = "http://web.dev.com";
//	private static final String WAP_DOMAIN_URL = "http://wap.test.ylfcf.com";//WAP环境
//	private static final String API2_DOMAIN_URL = "http://api.dev.ylfcf.com";//

	//开发环境本地
//	private static final String API_DOMAIN_URL = "http://www.m_ylf.com";//API环境
//	private static final String WAP_DOMAIN_URL = "http://wap.test.ylfcf.com";//WAP环境
//	private static final String API2_DOMAIN_URL = "http://api.dev.ylfcf.com";//

	private static final String BASE_URL = API_DOMAIN_URL + "/API/to_api.php";
	private final String ADD_PHONEINFO_URL = API2_DOMAIN_URL + "/user/phone_info/add";//将手机的系统版本、手机型号等信息存入后台

	public static final String REGISTE_AGREEMENT_URL = WAP_DOMAIN_URL + "/home/index/protocol#app";

	public static final String TWOYEARS_TZFX_URL = WAP_DOMAIN_URL + "/home/index/zlq.html";
	public static final String REDBAG_RULE_URL = WAP_DOMAIN_URL + "/home/index/redbag.html";//红包使用规则
	public static final String YUANMONEY_RULE_URL = WAP_DOMAIN_URL + "/home/Index/yuanGoldCoin.html#app";//元金币使用规则
	public static final String JXQ_RULE_URL = WAP_DOMAIN_URL + "/home/index/increaseInterest";//加息券使用规则
	public static final String FLOAT_RATE_URL = WAP_DOMAIN_URL + "/home/index/floatrate#app";//浮动利率的专题
	public static final String ZQDZP_WAP_URL = WAP_DOMAIN_URL + "/home/index/mdlottery.html";//中秋大转盘活动的wap页面
	public static final String XCFL_WAP_URL = WAP_DOMAIN_URL + "/home/index/qhb.html";//2017年新春抢红包红包专题
	public static final String JUNE_ACTIVE_WAP_URL = WAP_DOMAIN_URL + "/home/index/JuneActive#app";//2017年6月份活动
	public static final String SIGN_WAP_URL = WAP_DOMAIN_URL + "/home/index/qd";//签到活动专题
	public static final String YQHY_WAP_URL = WAP_DOMAIN_URL + "/home/index/fxjh";//四月份邀请好友返现活动
	public static final String HYFL02_WAP_URL = WAP_DOMAIN_URL + "/home/index/fuli2";//会员福利计划2期
	public static final String LXFX_WAP_URL = WAP_DOMAIN_URL + "/home/index/kmh";//2017年乐享返现 开年红活动
	public static final String LXJ5_WAP_URL = WAP_DOMAIN_URL + "/home/index/qxj";//五月份抢现金活动
	public static final String PROMOTED_BASE_URL = WAP_DOMAIN_URL + "/home/index/friendReg";// 邀请注册的二维码
	public static final String YYY_COMPACT = API_DOMAIN_URL + "/borrow-yyyprotocol-userid-recordid.html";// 元月盈借款协议url
	public static final String VIP_COMPACT = API_DOMAIN_URL + "/home/vip/vipProtocol/id/userid/invest/recordid";// vip产品的借款协议，userid和recordid为0时为空模板
	public static final String VIP_BLANK_COMPACT = API_DOMAIN_URL + "/home/vip/vipProtocol/borrow/borrowid";//vip空白合同
	public static final String SRZX_COMPACT = API_DOMAIN_URL + "/appoint/borrowProtocolData/id/recordid/info/userid";//私人尊享合同
	public static final String SRZX_BLANK_COMPACT = API_DOMAIN_URL + "/appoint/borrowProtocol/id/borrowid";//私人尊享空合同
	public static final String ZXD_XSB_COMPACT = API_DOMAIN_URL + "/home/downfiles/protocol?id=recordid&info=userid";//元政盈和新手标有数据的合同
	public static final String ZXD_XSB_BLANK_COMPACT = API_DOMAIN_URL + "/downfiles/index/id/borrowid";//元政盈和新手标空白合同
	public static final String XSMB_BLANK_COMPACT = API_DOMAIN_URL + "/home/seckill/seckillProtocol/id/borrowid";//限时秒标合同空模板
	public static final String XSMB_COMPACT = API_DOMAIN_URL + "/home/seckill/protocolData/id/recordid/info/userid";//限时秒标有数据的合同
	public static final String WDY_BLANK_COMPACT = API_DOMAIN_URL + "/wdy/wdyContract/id/borrowid";//稳定盈合同空模板
	public static final String WDY_COMPACT = API_DOMAIN_URL + "/wdy/wdyContractData/id/recordid/info/userid";//稳定盈有数据的合同
	public static final String YJY_BLANK_COMPACT = API_DOMAIN_URL + "/ygzx/ygzxProtocol/id/borrowid";//稳定盈合同空模板
	public static final String YJY_COMPACT = API_DOMAIN_URL + "/ygzx/ygzxProtocol/id/recordid/info/userid";//稳定盈有数据的合同

	public static final String VIP_CJWT_URL = WAP_DOMAIN_URL + "/home/vip/vipquestion.html#app";// vip常见问题
	public static final String YYY_CJWT_URL = WAP_DOMAIN_URL + "/home/yyy/yyyquestion#app";// 元月盈的常见问题
	public static final String XYJH_CJWT_URL = WAP_DOMAIN_URL + "/home/wdy/wdyQuestion/id/borrowid#app";// 稳定盈（薪盈计划）的常见问题
	public static final String YYY_XMJS_URL = WAP_DOMAIN_URL + "/home/yyy/yyyproject#app";// 元月盈的项目介绍
	public static final String XSB_XMJS_URL = WAP_DOMAIN_URL + "/home/index/novice/id/borrowid#app";//新手标介绍
	public static final String PROMOTER_URL = WAP_DOMAIN_URL + "/home/index/promoter.html#app";//推广员的专题
	public static final String XSMB_XMJS_URL = WAP_DOMAIN_URL + "/home/seckill/seckillProduct#app";//限时秒标项目介绍
	public static final String XYJH_XMJS_URL = WAP_DOMAIN_URL + "/home/wdy/wdyProject/id/borrowid#app";//薪盈计划(稳定盈)项目介绍,borrowid为当前标的id
	public static final String RECHARGE_PROOF_URL = WAP_DOMAIN_URL + "/home/App/rechargeDetail/id/rechargeId#app";//充值凭证

	//专题 页面
	public static final String SRZX_TOPIC_URL = WAP_DOMAIN_URL + "/home/Pvip/orderPro.html#app";//私人尊享预约专题页
	public static final String YJY_TOPIC_URL = WAP_DOMAIN_URL + "/home/Ygzx/yjyOrderPro.html#app";//元聚盈预约专题页

	private final String mOSType = "2";// Android:2 , IOS:10
	private final String API_ROUTER_URL = API_DOMAIN_URL + "/api_router.php";// 版本更新接口

	public static final String YXB_INTRO_URL = WAP_DOMAIN_URL + "/home/index/yxbandroid.html";// 元信宝介绍页面
	public static final String BORROW_CAILIAO_BASE_URL = "http://admin.ylfcf.com";// 标的资质证明图片的baseurl

	private final String PRODUCT_LIST_URL = "/borrow/borrow/newSelectList";// 产品列表
	private final String PRODUCT_SELECTONE_URL = "/borrow/borrow/selectOne";
	private final String USER_REGISTE_URL = "/user/user/reg";
	private final String USER_LOGIN_URL = "/user/user/login";
	private final String SEND_SMS_AUTH = "/sms/log/create";// 发送短信验证码
	private final String USER_RMB_HUIFU_ACCOUNT = "/user/account/selectOne";// 汇付用户人民币账户
	private final String USER_RMB_YILIAN_ACCOUNT = "/user/new_account/selectOne";// 易联用户人民币账户
	private final String USER_YUAN_MONEY_ACCOUNT = "/user/hd_coin/selectOne";// 元金币账户
	private final String INVESTMENT = "/borrow/invest/newTender";// 投资
	private final String CURRENT_USER_INVEST = "/borrow/invest/currentUserInvest";// 获取当前用户是否投资过该标的
	private final String BORROW_INVEST_SELECTLIST_ANDTOTAL = "/borrow/invest/selectListAndTotal";// 总的投资记录列表
	private final String BORROW_INVEST_SELECTLIST = "/borrow/invest/selectList";
	private final String UPDATE_USER_INFO = "/user/user/update";// 更新用户信息，修改密码个人信息等等
	private final String CHECK_REGISTER = "/user/user/checkRegister";// 判断用户是否注册过
	private final String USERINFO_BY_PHONE = "/user/user/selectOneByPhone";// 根据手机号码查询单条用户信息
	private final String PROJECT_DETAILS = "/project/project/selectOne";// 根据id获取项目的详情
	private final String ASSOCIATED_COMPANY_URL = "/project/associated_company/getCompanyInfo";// 获取关联公司的信息
	private final String WITHDRAW_MONEY = "/user/cash/toNewCash";// 提现
	private final String WITHDRAW_CANCEL_URL = "/user/cash/auditCash";//提现取消
	private final String RECHARGE = "/user/recharge/toYLRecharge";// 充值
	private final String RECHARGE_ORDER = "/user/recharge/toOrderRecharge";// 生成充值订单
	private final String USER_ISVERIFY = "/user/bank/selectOne";// 判断用户是否已认证
	private final String USER_VERIFY = "/user/recharge/toVerify";// 用户认证接口
	private final String USER_SELECT_ONE = "/user/user/selectOne";// 查询单个用户的个人信息
	private final String YILIAN_SMS = "/user/recharge/toYlSms";// 易联发送短信验证码（重新发送的时候调用此接口）
	private final String EXCHANGE_PASSWORD_CHECK = "/user/user/checkDealPwd";// 验证交易密码是否正确
	private final String WITHDRAW_LIST_URL = "/user/cash/cashLogList";// 提现列表
	private final String HUIFU_FUNDS_DETAILS_LIST = "/user/account_log/selectList";// 汇付账户资金明细列表
	private final String YILIAN_FUNDS_DETAILS_LIST = "/user/new_account_log/selectList";// 易联账户资金明细列表
	private final String FUNDS_DETAILS_LIST = "/user/new_account_log/selectCommonList";// 资金明细列表（易联、汇付、宝付）
	private final String YILIAN_STATISTIC = "/user/new_account_log/statistic";// 易联账户统计，比如投资了多少，收益了多少
	private final String HUIFU_STATISTIC = "/user/account_log/statistic";// 汇付账户统计，比如投资了多少，收益了多少
	private final String CHANGE_ADDRESS_AND_ZIPCODE = "/user/user/updateAddress";// 修改地址邮编
	private final String MYTYJ_URL = "/active/experience/selectList";// 我的体验金
	private final String EXTENSION_INCOME_URL = "/hd/interest/selectList";// 推广收益
	private final String EXTENSION_NEWINCOME_URL = "/promoter/promoterLog/getRewardList";//推广收益新的接口
	private final String PRIZE_LIST_URL = "/hd/prize/selectListWithActive";// 奖品列表
	private final String HDPRIZE_LIST_URL = "/hd/prize/selectList";//奖品列表
	private final String ACTIVE_INFO = "/active/active/selectOne";
	private final String ACTICLE_LIST_URL = "/article/article/selectList";// 公告、新闻、资讯列表
	private final String ARTICLE_URL = "/article/article/selectOne";// 公告
	private final String BANNERLIST_URL = "/article/article/selectPhoneBannerList";// banner
	private final String ARTICLE_TYJLIST_BYSTATUS = "/active/experience/selectListByStatus";// 根据用户id和状态获取体验金列表

	// 元信宝接口
	private final String YXB_PRODUCT = "/yxb/product/selectOne";// 产品
	private final String YXB_PRODUCT_LOG = "/yxb/product_log/selectOne";// 产品每日统计
	private final String YXB_INVEST = "/yxb/invest_detail/invest";// 元信宝的认购接口
	private final String YXB_USER_CENTER = "/yxb/user_center/selectOne";// 元信宝用户中心
	private final String YXB_REDEEM_RECORD = "/yxb/redeem_detail/selectList";// 元信宝赎回记录
	private final String YXB_INVEST_RECORD = "/yxb/invest_detail/selectList";// 元信宝认购记录
	private final String YXB_ACCOUNT_LIST = "/yxb/new_account_log/selectList";// 元信宝资金明细
	private final String YXB_REDEEM_URL = "/yxb/redeem_detail/redeem";// 元信宝的赎回接口
	private final String YXB_CHECK_REDEEM_URL = "/yxb/redeem_detail/checkRedeem";// 元信宝赎回的时候判断收不收手续费

	// 红包
	private final String REDBAG_LIST_URL = "/active/red_bag_log/getRedBagList";// 我的红包列表
	private final String REDBAG_CURRENTUSER_LIST = "/active/red_bag_log/getCurrentUserRedBagList";// 当前用户可以使用的红包列表
	private final String REDBAG_SELECTONE_URL = "/active/red_bag_log/selectOne";//查询某个红包

	// 宝付支付接口
	private final String BF_BINDCARD_URL = "/user/bank/bindCard";// 宝付绑卡接口
	private final String BF_SENDBINDCARD_MSG_URL = "/user/bank/sendBindCardMsg";// 宝付发送绑卡的验证码
	private final String BF_RECHARGE_URL = "/user/bank/recharge";// 宝付支付接口
	private final String BF_SENDRECHARGE_MSG_URL = "/user/bank/sendRechargeMsg";// 宝付发送充值的验证码
	private final String BF_VERIFY_URL = "/user/bank/verifyName";// 宝付实名认证接口

	// 新手标
	private final String XSB_DETAILS_URL = "/borrow/borrow/getNoviceStandard";// 新手标详情
	private final String XSB_ISCANBUY_URL = "/borrow/invest/isCanBuyNewHandBorrow";// 判断能否购买新手标

	// 元月盈
	private final String YYY_BORROW_LIST = "/yyy/borrow/selectList";// 元月盈的产品列表页面，暂时没用
	private final String YYY_BORROW_SELECTONE = "/yyy/borrow/getOneShowData";// 元月盈产品详情(最新的那支元月盈标的)
	private final String YYY_BORROW_SELECTONE_BYID = "/yyy/borrow/selectOne";// 根据borrowid获取某支元月盈的标的详情
	private final String YYY_INVEST_RECORD = "/yyy/borrow_invest/selectList";// 标的投资记录
	private final String YYY_USER_INVEST_RECORD = "/yyy/borrow_invest/investMagementNew";// 用户投资记录
	private final String YYY_APPLYORCANCEL_RETURN = "/yyy/return/add";// 元月盈申请和取消赎回的API
	private final String YYY_BORROW_INVEST = "/yyy/borrow_invest/invest";// 元月盈投标
	private final String YYY_CURRENTUSER_INVEST_URL = "/yyy/borrow_invest/currentUserInvest";// 判断当前用户是否投资过元月盈的某个标

	// VIP产品
	private final String VIP_GETLCSNAME_URL = "/user/user/getLxsName";// 获取理财师的名字
	private final String VIP_RECORDLIST_URL = "/vip/borrow_invest/selectList";// VIP产品的投资记录
	private final String VIP_INVEST_URL = "/vip/borrow_invest/invest";// VIP产品的投资接口
	private final String VIP_CURRENTUSER_INVEST_URL = "/vip/borrow_invest/currentUserInvest";// 判断当前用户是否投资过VIP的某个标

	// 中秋大转盘活动接口
	private final String DZP_LOTTERY_TIMES = "/hd/zqj_turntable/getLotteryTimes";//获取现有抽奖次数
	private final String DZP_USER_RECORDS = "/hd/zqj_turntable/getLotteryRecord";//用户的中奖记录
	private final String DZP_LOTTERY_RECORDS = "/hd/zqj_turntable/getLotteryList";//获取该活动的中奖记录
	private final String DZP_DRAW_PRIZE = "/hd/zqj_turntable/dzpDrawPrize";//抽取奖励
	private final String DZP_IS_SHARE = "/hd/wechat_prize/isShare";

	//私人尊享接口
	private final String APPOINT_BORROW_LIST = "/appoint/borrow/selectList";//私人尊享产品列表
	private final String APPOINT_BORROW_DETAILS = "/appoint/borrow/selectOne";//私人尊享产品详情
	private final String APPOINT_BORROW_INVEST = "/appoint/borrow_invest/invest";//私人尊享产品投标接口
	private final String APPOINT_BORROW_INVEST_RECORD = "/appoint/borrow_invest/selectList";//私人尊享投资记录（适合某支标的投资记录）
	private final String APPOINT_BORROW_INVEST_USER_RECORD = "/appoint/borrow_invest/selectListForPPP";//私人尊享用户投资记录
	private final String APPOINT_RECORD = "/appoint/appoint/appointList";//私人尊享产品预约记录
	private final String APPOINT_BORROW_APPOINT = "/appoint/appoint/add";//私人尊享产品预约接口

	//加息券
	private final String JXQ_LIST_URL = "/addInterest/add_interest_log/optimizedSelectList";//我的加息券列表
	private final String JXQ_SELECTONE_URL = "/addInterest/add_interest_use_rule/selectOne";
	//
	private final String YJB_INTEREST = "/borrow/invest_repayment/getCoinsAndInterest";//元金币收益以及本金
	private final String QUICK_PAY_BANK = "/pay_way/quick_pay_bank/selectList";//快捷支付银行列表

	//企业用户
	private final String COMP_APPLY_REGISTE_URL = "/co_user/co_user/comOrder";//企业用户申请注册
	private final String COMP_LOGIN_URL = "/co_user/co_user/comLogin";//企业用户登录接口
	private final String COMP_APPLY_CASH_URL = "/co_user/co_cash/toNewCash";//企业用户申请提现
	private final String COMP_USERINFO_URL = "/co_user/co_user/selectOne";//获取企业用户信息

	//限时秒标
	private final String XSMB_BORROW_DETAIL = "/mb/borrow/getShowData";//秒标详情
	private final String XSMB_BORROW_SELECTONE = "/mb/borrow/selectOne";//根据id获取秒标详情
	private final String XSMB_CURRENT_USER_INVEST = "/mb/borrow_invest/currentUserInvest";//当前用户是否投资过该秒标
	private final String XSMB_INVEST_URL = "/mb/borrow_invest/invest";//限时秒标投标接口
	private final String XSMB_INVEST_RECORD_URL = "/mb/borrow_invest/getSeckillInfo";//限时秒标投资记录

	//会员福利计划
	private final String FLJH_SELECTPRICE_BYNAME_URL = "/hd/prize/selectPriceByName";//根据用户id和奖品名称判断用户是否领取过
	private final String FLJH_PRIZECODE_SELECTONE_URL = "/hd/prize_code/selectOne";//获取单条奖品信息
	private final String FLJH_YYYINVEST_BYTIMESPAN = "/yyy/borrow_invest/getUserYyyInvestInfoByTimeSpan";//判断用户在活动期间是否投资过元月盈
	private final String FLJH_RECEIVE_PRIZE_URL = "/hd/prize/receivePrize";//领取奖品

	//新春领红包
	private final String XCFL_LOTTERY_TIMES_URL = "/hd/spring_festival/getLotteryTimes";//迎新春压岁钱抽奖次数
	private final String XCFL_LOTTERY_DRAW_PRIZE_URL = "/hd/spring_festival/springFestivalDrawPrize";//迎新春领取接口
	private final String XCFL_CHECK_ACTIVE_START_URL = "/hd/spring_festival/checkActiveTime";//查看活动是否开始

	//稳定盈
	private final String WDY_BORROW_DETAIL_URL = "/wdy/borrow/getShowData";//稳定赢最新的那个产品的详情
	private final String WDY_BORROW_DETAIL_SELECTONE_URL = "/wdy/borrow/selectOne";//稳定盈产品详情根据id
	private final String WDY_INVEST_URL = "/wdy/borrow_invest/invest";//稳定盈投资接口
	private final String WDY_INVEST_DETAIL_URL = "/wdy/borrow_invest/selectInvestDetail";//稳定盈标的投资记录
	private final String WDY_INVEST_RECORD_URL = "/wdy/borrow_invest/selectList";//稳定盈投资记录
	private final String WDY_CURRENT_USER_INVEST = "/wdy/borrow_invest/currentUserInvest";//当前用户是否投资过稳定盈
	private final String WDY_BORROWINVESTLOG_SELECTLIST_URL = "/wdy/borrow_invest_log/selectList";//根据投资记录id获取此条记录对应的所以子投资情况。
	private final String WDY_REINVEST_URL = "/wdy/borrow_invest_log/reInvest";//薪资计划产品复投

	//乐享返现 开年红
	private final String LXFX_GET_JXQ_URL = "/addInterest/add_interest/add";//领取加息券
	private final String LXFX_GET_JXQID_URL = "/addInterest/add_interest_use_rule/selectOne";//根据规则获取规则id
	private final String LXFX_ISGET_JXQ_URL = "/addInterest/add_interest_log/selectList";//是否领取过加息券

	private final String SYSTEM_NOW_TIME_URL = "/mb/borrow/getNowTime";//系统当前时间

	private final String RECHARGE_RECORD_LIST_URL = "/user/recharge/selectRechargeList";//充值记录列表

	//三月份签到活动
	private final String HD_SIGN_ISDAYSIGNED_URL = "/hd/sign/isDaySigned";//某天是否已经签到
	private final String HD_SIGN_SIGN_URL = "/hd/sign/marchAdd";//签到接口

	//会员福利计划2期
	private final String HD_PRIZE_LIST_URL = "/hd/gift/selectList";//礼品列表
	private final String HD_GIFTCODE_SELECTONE_URL = "/hd/gift_code/selectOne";//根据礼品id查询该用户是否已经领取过奖品

	//微信公众号升级抽奖活动
	private final String HD_LOTTERY_SELECTONE_URL = "/hd/lottery/selectOne";//获取奖品详情
	private final String HD_LOTTERYCODE_SELECTONE_URL = "/hd/lottery_code/selectOne";//获取礼品券码

	//五月份 每周一 抢现金活动
	private final String HD_ROBCASH_GETGIFT_URL = "/hd/rob_cash/getInvestGift";//领取绵羊奶粉
	private final String HD_ROBCASH_CHECK_ISRECEIVE_URL = "/hd/rob_cash/checkUserIsReceiveStatus";//是否已经领取或已领完绵羊奶粉
	private final String HD_ROBCASH_ROB_URL = "/hd/rob_cash/rob";//抢现金
	private final String HD_ROBCASH_CASH_URL = "/hd/rob_cash/getNowRob";//下周或者本周待抢金额

	//活动专区列表接口
	private final String ACTIVE_REGION_SELECTLIST_URL = "/active/active/selectListWithBanner";

	//加息券转让
	private final String JXQ_TRANSFER_GETSUBUSER_URL = "/user/user/getSubUser";//获取理财师的直接好友
	private final String JXQ_TRANSFER_TRANS_URL = "/addInterest/add_interest_log/changeAddInterestUser";//转让加息券

	//首页活动弹窗
	private final String POPBANNER_URL = "/active/banner/selectPopOne";//首页弹窗图片

	//转让加息券
	private final String MYFRIENDS_LIST_URL = "/promoter/promoterLog/myFriends";
	private final String TRANS_COUPONS_URL = "/addInterest/add_interest_log/oneUserGetMoreAddInterestUser";//转让多张加息券
	private final String TRANSFERED_COUPONS_LIST_URL = "/addInterest/add_interest_log/selectAlreadyClassifiedList";//可转让的加息券列表

	//员工专属产品（元聚盈）
	private final String YGZX_BORROWLIST_URL = "/ygzx/borrow/selectList";//员工专属产品列表
	private final String YGZX_BORROW_DETAILS_URL = "/ygzx/borrow/selectOne";//根据id获得某个标详情
	private final String YGZX_BORROWINVEST_RECORD_URL = "/ygzx/borrow_invest/selectList";//投资记录
	private final String YGZX_BORROWINVEST_URL = "/ygzx/borrow_invest/invest";//员工专属产品投标
	private final String YGZX_BORROWINVEST_LIST_URL = "/ygzx/borrow_invest/getUserInvestList";//根据userid和borrowid获取用户投资的某支标的详情
	//账户中心
	private final String ACCOUNTLOG_REPAYMENTINFO_URL = "/user/account_log/repaymentInfo";//回款日历

	private static URLGenerator mUrlGenerator;

	private URLGenerator() {
	}

	public static URLGenerator getInstance() {
		if (mUrlGenerator == null) {
			mUrlGenerator = new URLGenerator();
		}
		return mUrlGenerator;
	}

	/**
	 * 版本更新
	 *
	 * @param versionCode
	 * @return
	 */
	public String[] getApiRouterUrl(int versionCode) {
		StringBuffer sb = new StringBuffer();
		sb.append("version_code=").append(versionCode).append("&os_type=")
				.append(mOSType);
		return new String[] { API_ROUTER_URL, sb.toString() };
	}

	/**
	 * 获取产品列表的URL
	 *
	 * @param pageNo
	 * @param pageSize
	 * @param borrowType
	 *            速盈 稳盈 保盈 为空的话就是全部
	 * @param moneyStatus
	 *            未满标 已满标 已放款 已还款
	 * @param plan
	 *            为1时表示是元计划的产品列表，不传则表示政信贷
	 * @param enableShow
	 *            是否在手机app端显示 1可以显示，0不显示
	 * @param isNewHand
	 *            是否是新手标 1：是 2：否
	 * @param isVip
	 *            是否显示vip产品 无 0 是 1 否 2
	 * @return
	 */
	public String[] getProduceListUrl(String pageNo, String pageSize,
									  String borrowType, String borrowStatus,String moneyStatus, String isShow,
									  String isWap, String plan, String enableShow, String isNewHand,
									  String isVip) {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(PRODUCT_LIST_URL).append("&page=")
					.append(pageNo).append("&page_size=").append(pageSize);
			if (borrowType != null && !"".equals(borrowType)) {
				sb.append("&borrow_type=").append(borrowType);
			}
			if (borrowStatus != null && !"".equals(borrowStatus)) {
				sb.append("&borrow_status=").append(borrowStatus);
			}
			if (moneyStatus != null && !"".equals(moneyStatus)) {
				sb.append("&money_status=").append(moneyStatus);
			}
			if (isShow != null && !"".equals(isShow)) {
				sb.append("&is_show=").append(isShow);
			}
			if (isWap != null && !"".equals(isWap)) {
				sb.append("&is_wap=").append(isWap);
			}
			if (plan != null && !"".equals(plan)) {
				sb.append("&plan=").append(plan);
			}
			if (enableShow != null && !"".equals(enableShow)) {
				sb.append("&enableShow=").append(enableShow);
			}
			if (isNewHand != null && !"".equals(isNewHand)) {
				sb.append("&is_newHand=").append(isNewHand);
			}
			if (isVip != null && !"".equals(isVip)) {
				sb.append("&is_vip=").append(isVip);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 根据id获取某支标的信息
	 *
	 * @param id
	 * @param borrowStatus
	 * @param plan
	 *            1表示元计划
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getBorrowOnewURL(String id, String borrowStatus, String plan)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(PRODUCT_SELECTONE_URL).append("&id=")
				.append(id);
		if (borrowStatus != null && !"".equals(borrowStatus)) {
			sb.append("&borrow_status=").append(borrowStatus);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 注册URL
	 *
	 * @param phone
	 * @param password
	 * @param open_id
	 * @param user_from
	 * @param user_from_sub
	 * @param user_form_host
	 * @param extension_code
	 * @param type
	 *            用户类型 普通用户 VIP用户
	 * @param sales_phone
	 *            理财师手机号
	 * @return
	 */
	public String[] getUserRegisteURL(String phone, String password,
									  String open_id, String user_from, String user_from_sub,
									  String user_form_host, String extension_code, String type,
									  String sales_phone) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(USER_REGISTE_URL).append("&phone=")
				.append(phone).append("&password=")
				.append(Util.md5Encryption(password));
		if (open_id != null && !open_id.isEmpty()) {
			sb.append("&open_id=").append(open_id);
		}
		if (user_from!=null && !user_from.isEmpty()) {
			sb.append("&user_from=").append(user_from);
		}
		if (user_from_sub != null && !user_from_sub.isEmpty()) {
			sb.append("&user_from_sub=").append(user_from_sub);
		}
		if (user_form_host != null && !user_form_host.isEmpty()) {
			sb.append("&user_form_host=").append(user_form_host);
		}
		if (extension_code != null && !extension_code.isEmpty()) {
			sb.append("&extension_code=").append(extension_code);
		}
		if (type != null && !type.isEmpty()) {
			sb.append("&type=").append(type);
		}
		if (sales_phone != null && !sales_phone.isEmpty()) {
			sb.append("&sales_phone=").append(sales_phone);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 登录URL
	 * @param phone
	 * @param password
	 * @return
	 */
	public String[] getUserLoginURL(String phone, String password) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(USER_LOGIN_URL).append("&user_name=")
				.append(phone).append("&password=")
				.append(Util.md5Encryption(password));
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 根据ID获取汇付人民币账户
	 *
	 * @param userId
	 *            num
	 * @return
	 */
	public String[] getHuiFuRMBAccountURL(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(USER_RMB_HUIFU_ACCOUNT).append("&user_id=")
				.append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取易联人民币账户
	 *
	 * @param userId
	 * @return
	 */
	public String[] getYiLianRMBAccountURL(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(USER_RMB_YILIAN_ACCOUNT).append("&user_id=")
				.append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 根据ID获取元金币账户
	 *
	 * @param userId
	 *            num
	 * @return
	 */
	public String[] getUserYUANMoneyAccountURL(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(USER_YUAN_MONEY_ACCOUNT).append("&user_id=")
				.append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 立即投资
	 *
	 * @param borrowId
	 *            num 产品id
	 * @param investUserId
	 *            num 投资用户id
	 * @param money
	 *            投资金额（包括元金币金额）
	 * @param bonusMoney
	 *            元金币金额
	 * @param investFrom
	 *            投资来源
	 * @param investFromSub
	 *            二级来源
	 * @param experienceCode
	 *            体验金编号
	 * @param investFromHost
	 *            投资来源网址
	 * @param merPriv
	 *            商户私有域
	 * @param redBagLogId
	 *            红包id
	 * @return
	 */
	public String[] getBorrowInvestURL(String borrowId, String investUserId,
									   String money, int bonusMoney, String investFrom,
									   String investFromSub, String experienceCode, String investFromHost,
									   String merPriv, String redBagLogId,String couponLogId) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(INVESTMENT).append("&borrow_id=")
				.append(borrowId).append("&invest_user_id=")
				.append(investUserId).append("&money=").append(money)
				.append("&bonus_money=").append(bonusMoney);
		if (investFrom != null && !"".equals(investFrom)) {
			sb.append("&invest_from=").append(investFrom);
		}
		if (experienceCode != null && !"".equals(experienceCode)) {
			sb.append("&experience_code=").append(experienceCode);
		}
		if (investFromHost != null && !"".equals(investFromHost)) {
			sb.append("&invest_from_host=").append(investFromHost);
		}
		if (merPriv != null && !"".equals(merPriv)) {
			sb.append("&mer_priv=").append(merPriv);
		}
		if (investFromSub != null && !"".equals(investFromSub)) {
			sb.append("&invest_from_sub=").append(investFromSub);
		}
		if (redBagLogId != null && !"".equals(redBagLogId)
				&& !"null".equals(redBagLogId) && !"NULL".equals(redBagLogId)) {
			sb.append("&red_bag_log_id=").append(redBagLogId);
		}
		if (couponLogId != null && !"".equals(couponLogId)
				&& !"null".equals(couponLogId) && !"NULL".equals(couponLogId)) {
			sb.append("&coupon_log_id=").append(couponLogId);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 判断当前用户是否投资过该标的
	 *
	 * @param investUserId
	 * @param borrowId
	 * @return
	 */
	public String[] getCurrentUserInvestURL(String investUserId, String borrowId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(CURRENT_USER_INVEST)
				.append("&invest_user_id=").append(investUserId)
				.append("&borrow_id=").append(borrowId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 投资记录
	 *
	 * @param investUserId
	 *            用户在元立方的id 非必须
	 * @param borrowId
	 *            产品的id 非必须
	 * @param status
	 *            投资状态 非必须
	 * @param page
	 *            页码 必须
	 * @param pageSize
	 *            必须
	 * @return
	 */
	public String[] getBorrowInvestSelectListAndTotalURL(String investUserId,
														 String borrowId, String status, int page, int pageSize) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(BORROW_INVEST_SELECTLIST_ANDTOTAL)
				.append("&page=").append(page).append("&page_size=")
				.append(pageSize);
		if (!"".equals(investUserId) && investUserId != null) {
			sb.append("&invest_user_id=").append(investUserId);
		}
		if (!"".equals(borrowId) && borrowId != null) {
			sb.append("&borrow_id=").append(borrowId);
		}
		if (!"".equals(status) && status != null) {
			try {
				sb.append("&status=").append(status);
			} catch (Exception e) {
			}
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 投资记录
	 *
	 * @param investUserId
	 *            投资用户的id
	 * @param borrowId
	 *            标的id
	 * @param status
	 * @param isAddCoin
	 *            是否加上元金币 0没加1已加
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getBorrowInvestSelectListURL(String investUserId,
												 String borrowId, String status, String isAddCoin, int page,
												 int pageSize) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(BORROW_INVEST_SELECTLIST);
		if (investUserId != null && !"".equals(investUserId)) {
			sb.append("&invest_user_id=").append(investUserId);
		}
		if (status != null && !"".equals(status)) {
			sb.append("&status=").append(status);
		}
		if (isAddCoin != null && !"".equals(isAddCoin)) {
			sb.append("&is_add_coin=").append(isAddCoin);
		}

		sb.append("&borrow_id=").append(borrowId).append("&page=").append(page)
				.append("&page_size=").append(pageSize);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 更新用户信息接口，包括邮箱，密码，手机号等等
	 *
	 * @param id
	 * @param password
	 * @param phone
	 * @param email
	 * @param openId
	 * @param dealEnabled
	 *            交易密码是否开启 开启/关闭
	 * @param dealPwd
	 *            交易密码
	 * @param initPwd 密码修改的次数
	 * @return
	 */
	public String[] getUpdateUserInfoURL(String id, String password,
										 String phone, String email, String openId, String dealEnabled,
										 String dealPwd, String tmpData,String initPwd) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(UPDATE_USER_INFO).append("&id=").append(id);
		if (password != null && !"".equals(password)) {
			sb.append("&password=").append(Util.md5Encryption(password));
		}

		if (phone != null && !"".equals(phone)) {
			sb.append("&phone=").append(phone);
		}
		if (email != null && !"".equals(email)) {
			sb.append("&email=").append(email);
		}
		if (openId != null && !"".equals(openId)) {
			sb.append("&open_id=").append(openId);
		}
		if (dealEnabled != null && !"".equals(dealEnabled)) {
			sb.append("&deal_enabled=").append(dealEnabled);
		}

		if (dealPwd != null && !"".equals(dealPwd)) {
			sb.append("&deal_pwd=").append(Util.md5Encryption(dealPwd));
		}
		if (tmpData != null && !"".equals(tmpData)) {
			sb.append("&tmp_data=").append(tmpData);
		}
		if(initPwd != null && !"".equals(initPwd)){
			sb.append("&init_pwd=").append(initPwd);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 发送短信验证码
	 *
	 * @param phone
	 * @param template
	 *            register_code表示是注册页面的短信验证码模板,forgot_pwd表示是找回密码时的短信验证码模板
	 * @param params
	 * @param verfiy
	 *            此参数对于短信验证码暂时无效
	 * @return
	 */
	public String[] getSMSAuthNumURL(String phone, String template,
									 String params, String verfiy) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(SEND_SMS_AUTH).append("&phone=")
				.append(phone).append("&template=").append(template)
				.append("&params=").append(params).append("&verfiy=")
				.append(verfiy);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 检验用户该号码是否注册(此接口返回用户信息太少，建议不用) 注册的时候判断此号码是否已经被注册
	 *
	 * @param phone
	 *            一般为手机号
	 * @return
	 */
	public String[] getCheckRegisterURL(String phone) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(CHECK_REGISTER).append("&user_name=")
				.append(phone);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 根据手机号码获取用户信息 ，找回密码的时候用到此接口
	 *
	 * @param phone
	 * @return
	 */
	public String[] getUserInfoByPhone(String phone) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(USERINFO_BY_PHONE).append("&phone=")
				.append(phone);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 根据Id获取单条项目的详情
	 *
	 * @param id
	 * @return
	 */
	public String[] getProjectDetailsById(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(PROJECT_DETAILS).append("&id=").append(id);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 关联公司的信息
	 *
	 * @param loanId
	 * @param recommendId
	 * @param guaranteeId
	 * @return
	 */
	public String[] getAssociatedCompanyURL(String loanId, String recommendId,
											String guaranteeId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(ASSOCIATED_COMPANY_URL).append("&loan_id=")
				.append(loanId).append("&recommend_id=").append(recommendId)
				.append("&guarantee_id=").append(guaranteeId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 充值
	 *
	 * @param userId
	 *            用户id
	 * @param account
	 *            充值金额
	 * @param bankCard
	 *            银行卡号
	 * @param realName
	 *            真实姓名
	 * @param idNumber
	 *            身份证号码
	 * @return
	 */
	public String[] getRechargeURL(String userId, String account,
								   String bankCard, String phone, String realName, String idNumber) {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(RECHARGE).append("&user_id=")
					.append(userId).append("&account=").append(account)
					.append("&bank_card=").append(bankCard)
					.append("&bank_phone=").append(phone).append("&real_name=")
					.append(realName)
					.append("&id_number=").append(idNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 提现
	 *
	 * @param userId
	 *            用户ID
	 * @param cashAccount
	 *            提现金额
	 * @return
	 */
	public String[] getWithdrawURL(String userId, String cashAccount) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WITHDRAW_MONEY).append("&user_id=")
				.append(userId).append("&cash_account=").append(cashAccount);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 取消提现
	 *
	 * @param id
	 *            提现ID
	 * @param status
	 *            变更的状态
	 * @param auditor
	 *            操作人ID
	 * @param auditType
	 *            操作人类型
	 * @return
	 * @throws Exception
	 */
	public String[] getWithdrawCancelURL(String id, String status,
										 String auditor, String auditType) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WITHDRAW_CANCEL_URL).append("&id=")
				.append(id).append("&status=")
				.append(status).append("&auditor=")
				.append(auditor).append("&audit_type=")
				.append(auditType);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 生成充值订单
	 *
	 * @param userId
	 * @param orderId
	 *            充值接口返回的订单号
	 * @param smsCode
	 *            充值接口返回的短信码
	 * @return
	 */
	public String[] getRechargeOrderURL(String userId, String orderId,
										String smsCode) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(RECHARGE_ORDER).append("&user_id=")
				.append(userId).append("&order=").append(orderId)
				.append("&sms_code=").append(smsCode);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 根据用户id查询 是否已认证
	 *
	 * @param userId
	 * @param type
	 *            枚举类型：宝付、易联
	 * @return
	 */
	public String[] getUserBankInfoById(String userId, String type) {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(USER_ISVERIFY).append("&user_id=")
					.append(userId).append("&type=")
					.append(type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 用户认证
	 *
	 * @param userId
	 * @param bankCard
	 *            银行卡号
	 * @param realName
	 * @param idNumber
	 *            身份证号码
	 * @param bankPhone
	 *            银行预留手机号
	 * @return
	 */
	public String[] getUserVerifyURL(String userId, String bankCard,
									 String realName, String idNumber, String bankPhone) {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(USER_VERIFY).append("&user_id=")
					.append(userId).append("&bank_card=").append(bankCard)
					.append("&real_name=")
					.append(realName)
					.append("&id_number=").append(idNumber);
			if (bankPhone != null && !"".equals(bankPhone)) {
				sb.append("&bank_phone=").append(bankPhone);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取一个用户的个人信息
	 *
	 * @param id
	 * @param phone
	 * @param opneId
	 * @param coMobile 企业用户手机号
	 * @return
	 */
	public String[] getUserInfo(String id, String phone,String coMobile, String opneId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(USER_SELECT_ONE);
		if (id != null && !"".equals(id)) {
			sb.append("&id=").append(id);
		}
		if (phone != null && !"".equals(phone)) {
			sb.append("&phone=").append(phone);
		}
		if (coMobile != null && !"".equals(coMobile)) {
			sb.append("&co_mobile=").append(coMobile);
		}
		if (opneId != null && !"".equals(opneId)) {
			sb.append("&open_id=").append(opneId);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 易联重新发送短信验证码
	 *
	 * @param userId
	 * @param order
	 * @return
	 */
	public String[] getYilianSMSURL(String userId, String order) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YILIAN_SMS).append("&user_id=")
				.append(userId).append("&order=").append(order);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 验证交易密码是否输入正确
	 *
	 * @param userId
	 * @param dealPwd
	 *            交易密码 不加密
	 * @return
	 */
	public String[] getExchangePwdCheck(String userId, String dealPwd) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(EXCHANGE_PASSWORD_CHECK).append("&id=")
				.append(userId).append("&deal_pwd=")
				.append(Util.md5Encryption(dealPwd));
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 提现列表
	 *
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @param status
	 *            成功、审核中
	 * @param startTime
	 *            格式2015-12-01 11:20:31
	 * @param endTime
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getWithdrawListURL(String userId, String page,
									   String pageSize, String status, String startTime, String endTime)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WITHDRAW_LIST_URL).append("&user_id=")
				.append(userId).append("&page=").append(page)
				.append("&page_size=").append(pageSize);
		if (status != null && !"".equals(status)) {
			sb.append("&status=").append(status);
		}
		if (startTime != null && !"".equals(startTime)) {
			sb.append("&start_time=").append(startTime);
		}
		if (endTime != null && !"".equals(endTime)) {
			sb.append("&end_time=").append(endTime);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 汇付账户资金列表
	 *
	 * @param userId
	 * @param type
	 * @param page
	 * @param pageSize
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String[] getHuifuFundsDetaislListURL(String userId, String type,
												String page, String pageSize, String startTime, String endTime) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HUIFU_FUNDS_DETAILS_LIST)
				.append("&user_id=").append(userId).append("&page=")
				.append(page).append("&page_size=").append(pageSize);
		if (type != null && !"".equals(type)) {
			sb.append("&type=").append(type);
		}
		if (startTime != null && !"".equals(startTime)) {
			sb.append("&start_time=").append(startTime);
		}
		if (endTime != null && !"".equals(endTime)) {
			sb.append("&end_time=").append(endTime);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 易联账户资金列表
	 *
	 * @param userId
	 * @param type
	 *            资金操作类型 如Repayment
	 * @param page
	 * @param pageSize
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String[] getYilianFundsDetaislListURL(String userId, String type,
												 String page, String pageSize, String startTime, String endTime) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YILIAN_FUNDS_DETAILS_LIST)
				.append("&user_id=").append(userId).append("&page=")
				.append(page).append("&page_size=").append(pageSize);
		if (type != null && !"".equals(type)) {
			sb.append("&type=").append(type);
		}
		if (startTime != null && !"".equals(startTime)) {
			sb.append("&start_time=").append(startTime);
		}
		if (endTime != null && !"".equals(endTime)) {
			sb.append("&end_time=").append(endTime);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 资金明细列表
	 *
	 * @param userId
	 * @param type
	 *            资金操作类型 Repayment
	 * @param page
	 * @param pageSize
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String[] getFundsDetailsListURL(String userId, String type,
										   String page, String pageSize, String startTime, String endTime) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(FUNDS_DETAILS_LIST).append("&user_id=")
				.append(userId).append("&page=").append(page)
				.append("&page_size=").append(pageSize);
		if (type != null && !"".equals(type)) {
			sb.append("&type=").append(type);
		}
		if (startTime != null && !"".equals(startTime)) {
			sb.append("&start_time=").append(startTime);
		}
		if (endTime != null && !"".equals(endTime)) {
			sb.append("&end_time=").append(endTime);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 易联统计
	 *
	 * @param userId
	 * @return
	 */
	public String[] getYilianStatistic(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YILIAN_STATISTIC).append("&user_id=")
				.append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 汇付统计
	 *
	 * @param userId
	 * @return
	 */
	public String[] getHuifuStatistic(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HUIFU_STATISTIC).append("&user_id=")
				.append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 修改地址邮编
	 *
	 * @param userId
	 * @param address
	 * @param postCode
	 *            邮编
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getAddressURL(String userId, String address, String postCode)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(CHANGE_ADDRESS_AND_ZIPCODE).append("&id=")
				.append(userId).append("&address=")
				.append(address).append("&post_code=").append(postCode);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取体验金
	 *
	 * @param userId
	 *            邮编
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getTYJURL(String page, String pageSize, String status,
							  String userId, String putStatus, String activeTitle)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(MYTYJ_URL).append("&page=").append(page)
				.append("&page_size=").append(pageSize).append("&user_id=")
				.append(userId);
		if (status != null && !"".equals(status)) {
			sb.append("&status=").append(status);
		}
		if (putStatus != null && !"".equals(putStatus)) {
			sb.append("&put_status=").append(putStatus);
		}
		if (activeTitle != null && !"".equals(activeTitle)) {
			sb.append("&active_title=").append(activeTitle);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 推广收益
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getExtensionIncomeURL(String userId, String pageNo,
										  String pageSize) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(EXTENSION_INCOME_URL)
				.append("&extension_user_id=").append(userId).append("&page=")
				.append(pageNo).append("&page_size=").append(pageSize)
				.append("&is_wap=").append("1");
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 推广收益 新的接口
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getNewExtensionIncomeURL(String userId, String pageNo,
											 String pageSize) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(EXTENSION_NEWINCOME_URL)
				.append("&extension_user_id=").append(userId).append("&page=")
				.append(pageNo).append("&page_size=").append(pageSize);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 奖品列表 --- 再根据active_title这个字段去查询活动信息，看活动什么时候结束，可以判断出哪些已经过期，然后再根据已激活未激活进行分类
	 *
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @param source
	 * @return
	 */
	public String[] getPrizeListURL(String page, String pageSize,
									String userId, String source,String activeTitle) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(PRIZE_LIST_URL).append("&page=")
				.append(page).append("&page_size=").append(pageSize);
		if (userId != null && !"".equals(userId)) {
			sb.append("&user_id=").append(userId);
		}
		if (source != null && !"".equals(source)) {
			sb.append("&source=").append(source);
		}
		if (activeTitle != null && !"".equals(activeTitle)) {
			sb.append("&active_title=").append(activeTitle);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取奖品活动信息
	 *
	 * @param activeTitle
	 * @param id
	 * @return
	 */
	public String[] getPrizeActiveInfo(String activeTitle, String id) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(ACTIVE_INFO).append("&active_title=")
				.append(activeTitle);
		if (id != null && !"".equals(id)) {
			sb.append("&id=").append(id);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取公告
	 *
	 * @param id
	 * @return
	 */
	public String[] getArticleURL(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(ARTICLE_URL).append("&id=").append(id);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取公告、新闻、咨询列表
	 *
	 * @param status
	 *            正常、关闭、删除
	 * @param type
	 *            行业新闻、网站公告、最新资讯、热门资讯、关于我们
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public String[] getArticleListURL(String status, String type, String page,
									  String pageSize) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(ACTICLE_LIST_URL).append("&page=")
				.append(page).append("&page_size=").append(pageSize);
		if (status != null && !"".equals(status)) {
			sb.append("&status=").append(status);
		}
		if (type != null && !"".equals(type)) {
			sb.append("&type=").append(type);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取banner
	 *
	 * @param page
	 * @param pageSize
	 * @param status
	 *            正常
	 * @return
	 * @throws Exception
	 */
	public String[] getBannerURL(String page, String pageSize, String status,
								 String type) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(BANNERLIST_URL).append("&page=")
				.append(page).append("&page_size=").append(pageSize)
				.append("&status=").append(status);
		if (!type.isEmpty()) {
			sb.append("&type=").append(type);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 根据用户的id和status获取体验金列表
	 *
	 * @param status
	 *            未使用，已使用等
	 * @param user_id
	 * @param put_status
	 *            可发放状态
	 * @param active_title
	 *            活动标识 TYJ_02
	 * @return
	 */
	public String[] getArticleTYJListByStatus(String status, String user_id,
											  String put_status, String active_title) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(ARTICLE_TYJLIST_BYSTATUS).append("&status=")
				.append(status).append("&user_id=")
				.append(user_id);
		if (put_status != null && !"".equals(put_status)) {
			sb.append("&put_status=").append(put_status);
		}
		if (active_title != null && !"".equals(active_title)) {
			sb.append("&active_title=").append(active_title);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取元信宝产品URL
	 *
	 * @param id
	 * @param status
	 *            已发布
	 * @return
	 */
	public String[] getYXBProductURL(String id, String status) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YXB_PRODUCT);
		if (id != null && !"".equals(id)) {
			sb.append("&id=").append(id);
		}
		if (status != null && !"".equals(status)) {
			sb.append("&status=").append(status);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取元信宝每日统计URL
	 *
	 * @param id
	 * @param dateTime
	 *            yyyy-MM-dd 00:00:00(凌晨0点整)
	 * @return
	 */
	public String[] getYXBProductLogURL(String id, String dateTime) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YXB_PRODUCT_LOG);
		if (id != null && !"".equals(id)) {
			sb.append("&id=").append(id);
		}
		if (dateTime != null && !"".equals(dateTime)) {
			sb.append("&date_time=").append(dateTime);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 元信宝 ---- 认购
	 *
	 * @param productId
	 * @param userId
	 * @param orderMoney
	 * @return
	 */
	public String[] getYXBInvestURL(String productId, String userId,
									String orderMoney) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YXB_INVEST);
		sb.append("&product_id=").append(productId).append("&user_id=")
				.append(userId).append("&order_money=").append(orderMoney);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 元信宝用户中心
	 *
	 * @param userId
	 * @return
	 */
	public String[] getYXBUserCenterURL(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YXB_USER_CENTER);
		sb.append("&user_id=").append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 元信宝赎回记录
	 *
	 * @param id
	 *            记录的id，并非产品的id
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getYXBRedeemRecordsURL(String id, String userId,
										   String page, String pageSize) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YXB_REDEEM_RECORD);
		sb.append("&page=").append(page).append("&page_size=").append(pageSize);
		if (id != null && !"".equals(id)) {
			sb.append("&id=").append(id);
		}
		if (userId != null && !"".equals(userId)) {
			sb.append("&user_id=").append(userId);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 元信宝 认购记录
	 *
	 * @param id
	 * @param userId
	 * @param interestStatus
	 *            //是否计息
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getYXBInvestRecordsURL(String id, String userId,
										   String interestStatus, String page, String pageSize)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YXB_INVEST_RECORD);
		sb.append("&page=").append(page).append("&page_size=").append(pageSize)
				.append("&user_id=").append(userId);
		if (id != null && !"".equals(id)) {
			sb.append("&id=").append(id);
		}
		if (interestStatus != null && !"".equals(interestStatus)) {
			sb.append("&interest_status=").append(interestStatus);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 元信宝资金明细接口
	 *
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getYXBNewAccountLog(String userId, String page,
										String pageSize) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YXB_ACCOUNT_LIST);
		sb.append("&user_id=").append(userId).append("&page_size=")
				.append(pageSize).append("&page=").append(page);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 元信宝赎回接口
	 *
	 * @param productId
	 * @param userId
	 * @param applyMoney
	 * @return
	 */
	public String[] getYXBRedeemURL(String productId, String userId,
									String applyMoney) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YXB_REDEEM_URL);
		sb.append("&product_id=").append(productId).append("&user_id=")
				.append(userId).append("&apply_money=").append(applyMoney);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 赎回验证接口
	 *
	 * @param productId
	 * @param userId
	 * @param applyMoney
	 * @return
	 */
	public String[] getYXBCheckRedeemURL(String productId, String userId,
										 String applyMoney) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YXB_CHECK_REDEEM_URL);
		sb.append("&product_id=").append(productId).append("&user_id=")
				.append(userId).append("&apply_money=").append(applyMoney);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 我的红包列表
	 *
	 * @param userId
	 * @param flag
	 *            1:未使用 2：已使用 3：已过期
	 * @return
	 */
	public String[] getMyRedBagListURL(String userId, String flag,String page,String pageSize) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(REDBAG_LIST_URL);
		sb.append("&user_id=").append(userId).append("&flag=").append(flag)
				.append("&page=").append(page).append("&page_size=").append(pageSize);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 将手机信息存入后台
	 *
	 * @param userId
	 * @param phone
	 * @param phoneModel
	 *            手机品牌型号
	 * @param sdkVersion
	 *            sdk版本号
	 * @param systemVersion
	 *            手机系统版本号
	 * @param phoneType
	 *            手机类型 Android | IOS
	 * @param location
	 * @param contact
	 * @return
	 */
	public String[] getAddPhoneInfoURL(String userId, String phone,
									   String phoneModel, String sdkVersion, String systemVersion,
									   String phoneType, String location, String contact) {
		StringBuffer sb = new StringBuffer();
		sb.append("user_id=").append(userId).append("&phone=").append(phone)
				.append("&phone_model=").append(phoneModel)
				.append("&sdk_version=").append(sdkVersion)
				.append("&system_version=").append(systemVersion)
				.append("&phone_type=").append(phoneType).append("&location=")
				.append(location).append("&contact=").append(contact);
		return new String[] { ADD_PHONEINFO_URL, sb.toString() };
	}

	/**
	 * 当前用户可以使用的红包列表
	 *
	 * @param userId
	 * @param borrowType
	 *            稳赢 速盈 保盈
	 * @return
	 */
	public String[] getRedbagCurrentUserListURL(String userId, String borrowType) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(REDBAG_CURRENTUSER_LIST);
		try {
			sb.append("&user_id=").append(userId).append("&borrow_type=")
					.append(borrowType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取宝付绑卡的URL
	 *
	 * @param userId
	 *            用户id
	 * @param idNum
	 *            身份证号码
	 * @param realName
	 *            真实姓名
	 * @param bankCard
	 *            银行卡卡号
	 * @param bankName
	 *            银行名称
	 * @param bankCode
	 *            银行的简称
	 * @param bankPhone
	 *            银行预留手机号码
	 * @param smsCode
	 *            短信验证码
	 * @param order_sn
	 *            订单号
	 * @return
	 */
	public String[] getBFBindCardURL(String userId, String idNum,
									 String realName, String bankCard, String bankName, String bankCode,
									 String bankPhone, String smsCode, String order_sn) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(BF_BINDCARD_URL);
		try {
			sb.append("&user_id=").append(userId).append("&id_num=")
					.append(idNum).append("&real_name=").append(realName)
					.append("&bank_card=").append(bankCard)
					.append("&bank_code=").append(bankCode)
					.append("&bank_phone=").append(bankPhone)
					.append("&sms_code=").append(smsCode).append("&order_sn=")
					.append(order_sn);
			if (bankName != null && !"".equals(bankName)) {
				sb.append("&bank_name=").append(bankName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 宝付支付接口
	 *
	 * @param userId
	 *            用户id
	 * @param amount
	 *            充值金额
	 * @param smsCode
	 *            短信验证码
	 * @return
	 */
	public String[] getBFRechargeURL(String userId, String amount,
									 String smsCode, String orderId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(BF_RECHARGE_URL);
		sb.append("&user_id=").append(userId).append("&amount=").append(amount)
				.append("&sms_code=").append(smsCode).append("&order_sn=")
				.append(orderId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 宝付发送绑卡的验证码
	 *
	 * @param userId
	 * @param bankCard
	 * @param bankPhone
	 * @return
	 */
	public String[] getBFSendBindcardURL(String userId, String bankCard,
										 String bankPhone) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(BF_SENDBINDCARD_MSG_URL);
		sb.append("&user_id=").append(userId).append("&bank_card=")
				.append(bankCard).append("&bank_phone=").append(bankPhone);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 宝付发送充值的验证码
	 *
	 * @param userId
	 * @param amount
	 * @return
	 */
	public String[] getBFSendRechargeURL(String userId, String amount) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(BF_SENDRECHARGE_MSG_URL);
		sb.append("&user_id=").append(userId).append("&amount=").append(amount);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 宝付实名认证的URL
	 *
	 * @param userId
	 *            用户ID
	 * @param idNum
	 *            用户身份证号码
	 * @param realName
	 *            真实姓名
	 * @return
	 * @throws Exception
	 */
	public String[] getBFVerifyURL(String userId, String idNum, String realName)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(BF_VERIFY_URL);
		sb.append("&user_id=").append(userId).append("&id_card=").append(idNum)
				.append("&id_holder=")
				.append(realName);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 新手标详情
	 *
	 * @param borrowStatus
	 *            发布
	 * @return
	 */
	public String[] getXSBDetailsURL(String borrowStatus) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(XSB_DETAILS_URL);
		try {
			sb.append("&borrow_status=").append(borrowStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 判断能否购买新手标
	 *
	 * @param userId
	 * @param borrowId
	 * @return
	 */
	public String[] getIsCanBuyXSBURL(String userId, String borrowId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(XSB_ISCANBUY_URL);
		sb.append("&borrow_id=").append(borrowId).append("&invest_user_id=")
				.append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 元月盈产品列表URL
	 *
	 * @param borrowStatus
	 *            发布；审核中...等等
	 * @param moneyStatus
	 *            未满标；已满标等等...
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public String[] getYYYBorrowListURL(String borrowStatus,
										String moneyStatus, String page, String pageSize) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YYY_BORROW_LIST);
		sb.append("&page=").append(page).append("&page_size=").append(pageSize);
		if (borrowStatus != null && !"".equals(borrowStatus)) {
			sb.append("&borrow_status=").append(borrowStatus);
		}
		if (moneyStatus != null && !"".equals(moneyStatus)) {
			sb.append("&money_status=").append(moneyStatus);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取正在售的元月盈产品详情
	 *
	 * @param borrowStatus
	 *            发布...等等
	 *            未满标/已满标 等等
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getYYYBorrowDetailsURL(String borrowStatus)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YYY_BORROW_SELECTONE);
		if (borrowStatus != null && !"".equals(borrowStatus)) {
			sb.append("&borrow_status=").append(borrowStatus);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 根据borrowid获取元月盈某支标的详情
	 *
	 * @param id
	 *            标的id
	 * @param borrowStatus
	 *            如发布等
	 * @param moneyStatus
	 *            如未满标、已满标等
	 * @return
	 * @throws Exception
	 */
	public String[] getYYYBorrowDetailsByIdURL(String id, String borrowStatus,
											   String moneyStatus) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YYY_BORROW_SELECTONE_BYID);
		sb.append("&id=").append(id);
		if (borrowStatus != null && !"".equals(borrowStatus)) {
			sb.append("&borrow_status=").append(borrowStatus);
		}
		if (moneyStatus != null && !"".equals(moneyStatus)) {
			sb.append("&money_status=").append(moneyStatus);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 元月盈标的投资记录
	 *
	 * @param borrowId
	 *            元月盈产品id
	 * @param investStatus
	 *            投资状态：首投、复投
	 * @param investUserId
	 *            投资用户id
	 * @param returnStatus
	 *            赎回状态 投资中、已赎回
	 * @param type
	 *            投资类型：用户投资
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getYYYInvestRecordURL(String borrowId, String investStatus,
										  String investUserId, String returnStatus, String type, String page,
										  String pageSize) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YYY_INVEST_RECORD);
		sb.append("&page=").append(page).append("&page_size=").append(pageSize);
		if (borrowId != null && !"".equals(borrowId)) {
			sb.append("&borrow_id=").append(borrowId);
		}
		if (investStatus != null && !"".equals(investStatus)) {
			sb.append("&invest_status=").append(investStatus);
		}
		if (investUserId != null && !"".equals(investUserId)) {
			sb.append("&invest_user_id=").append(investUserId);
		}
		if (returnStatus != null && !"".equals(returnStatus)) {
			sb.append("&return_status=").append(returnStatus);
		}
		if (type != null && !"".equals(type)) {
			sb.append("&type=").append(type);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 元月盈用户投资记录
	 *
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getYYYUserInvestRecordURL(String userId, String page,
											  String pageSize) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YYY_USER_INVEST_RECORD);
		sb.append("&invest_user_id=").append(userId).append("&page=")
				.append(page).append("&page_size=").append(pageSize);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 元月盈申请和取消赎回的API
	 *
	 * @param investId
	 *            元月盈投资记录id
	 * @param userId
	 * @param type
	 *            1为预约赎回2为取消预约
	 * @return
	 */
	public String[] getYYYApplyOrCancelReturnURL(String investId,
												 String userId, String type) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YYY_APPLYORCANCEL_RETURN);
		sb.append("&invest_id=").append(investId).append("&invest_user_id=")
				.append(userId).append("&type=").append(type);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 元月盈投标URL
	 *
	 * @param borrowId
	 * @param money
	 * @param userId
	 * @param investFrom 由于后台一开始没考虑周全，此参数表示投资来源，跟政信贷有所不同。此处将一级来源和二级来源进行了合并，如果来自官网的app，则为官网App安卓版，如果来自第三方市场，比如应用宝，则为应用宝
	 * @return
	 */
	public String[] getYYYBorrowInvestURL(String borrowId, String money,
										  String userId,String investFrom,String couponId,String redbagId) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YYY_BORROW_INVEST);
		sb.append("&borrow_id=").append(borrowId).append("&money=")
				.append(money).append("&invest_user_id=").append(userId);
		if(investFrom != null && !"".equals(investFrom)){
			sb.append("&invest_from=").append(investFrom);
		}
		if(couponId != null && !"".equals(couponId) && !"null".equals(couponId) && !"NULL".equals(couponId)){
			sb.append("&add_interest_log_id=").append(couponId);
		}
		if(redbagId != null && !"".equals(redbagId) && !"null".equals(redbagId) && !"NULL".equals(redbagId)){
			sb.append("&red_bag_log_id=").append(redbagId);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 判断当前用户是否投资过vip该标的
	 *
	 * @param investUserId
	 * @param borrowId
	 * @return
	 */
	public String[] getYYYCurrentUserInvestURL(String investUserId,
											   String borrowId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YYY_CURRENTUSER_INVEST_URL)
				.append("&invest_user_id=").append(investUserId)
				.append("&borrow_id=").append(borrowId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 根据手机号码获取理财师的名字
	 *
	 * @param phone
	 * @return
	 */
	public String[] getLCSNameURL(String phone) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(VIP_GETLCSNAME_URL);
		sb.append("&sales_phone=").append(phone);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * VIP投资记录列表
	 *
	 * @param borrowId
	 * @param status
	 * @param investUserId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public String[] getVIPRecordListURL(String borrowId, String status,
										String investUserId, String page, String pageSize) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(VIP_RECORDLIST_URL);
		if (!borrowId.isEmpty()) {
			sb.append("&borrow_id=").append(borrowId);
		}
		if (!status.isEmpty()) {
			sb.append("&status=").append(status);
		}
		if (!investUserId.isEmpty()) {
			sb.append("&invest_user_id=").append(investUserId);
		}
		sb.append("&type=").append("用户投资");
		sb.append("&page=").append(page).append("&page_size=").append(pageSize);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * VIP产品投资接口
	 *
	 * @param borrowId
	 * @param money
	 * @param investUserId
	 * @return
	 */
	public String[] getVIPInvestURL(String borrowId, String money,
									String investUserId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(VIP_INVEST_URL);
		sb.append("&borrow_id=").append(borrowId).append("&money=")
				.append(money).append("&invest_user_id=").append(investUserId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 判断当前用户是否投资过vip该标的
	 *
	 * @param investUserId
	 * @param borrowId
	 * @return
	 */
	public String[] getVIPCurrentUserInvestURL(String investUserId,
											   String borrowId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(VIP_CURRENTUSER_INVEST_URL)
				.append("&invest_user_id=").append(investUserId)
				.append("&borrow_id=").append(borrowId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取大转盘抽奖次数
	 * @param userId
	 * @return
	 */
	public String[] getDZPLotteryTimesURL(String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(DZP_LOTTERY_TIMES).append("&user_id=").append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取用户的中奖记录
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getDZPUserRecordsURL(String userId,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(DZP_USER_RECORDS).append("&user_id=").append(userId)
				.append("&page=").append(page).append("&page_size=").append(pageSize);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取该活动的中奖记录
	 * @param activeTitle
	 * @return
	 */
	public String[] getDZPLotteryRecordsURL(String activeTitle){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(DZP_LOTTERY_RECORDS).append("&active_title=").append(activeTitle);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取抽奖结果
	 * @param userId
	 * @return
	 */
	public String[] getDZPDrawPrizeURL(String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(DZP_DRAW_PRIZE).append("&user_id=").append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 大转盘分享成功后请求此接口，对抽奖次数进行+1
	 * @param userId
	 * @return
	 */
	public String[] getDZPIsshared(String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(DZP_IS_SHARE).append("&user_id=").append(userId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 私人尊享产品列表
	 * @param borrowStatus
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public String[] getAppointBorrowList(String borrowStatus,String moneyStatus,String page,String pageSize) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(APPOINT_BORROW_LIST).append("&borrow_status=")
				.append(borrowStatus)
				.append("&page=").append(page).append("&page_size=").append(pageSize);
		if(moneyStatus != null && !"".equals(moneyStatus)){
			sb.append("&money_status=").append(moneyStatus);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 私人尊享产品详情
	 * @param borrowId
	 * @return
	 */
	public String[] getAppointBorrowDetails(String borrowId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(APPOINT_BORROW_DETAILS).append("&id=")
				.append(borrowId);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 私人尊享投标接口
	 * @param borrowId
	 * @param userId
	 * @param money 投标金额
	 * @param investFrom 投资来源 安卓APP
	 * @param redbagId 红包id
	 * @return
	 * @throws Exception
	 */
	public String[] getAppointBorrowInvest(String borrowId,String userId,String money,String investFrom,String redbagId,String couponId) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(APPOINT_BORROW_INVEST).append("&borrow_id=")
				.append(borrowId).append("&user_id=").append(userId).append("&money=").append(money);
		if(investFrom != null && !"".equals(investFrom)){
			sb.append("&invest_from=").append(investFrom);
		}
		if(redbagId != null && !"".equals(redbagId) && !"null".equals(redbagId) && !"NULL".equals(redbagId)){
			sb.append("&red_bag_log_id=").append(redbagId);
		}
		if(couponId != null && !"".equals(couponId) && !"null".equals(couponId) && !"NULL".equals(couponId)){
			sb.append("&add_interest_log_id=").append(couponId);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 私人尊享投资记录(适合某个标的投资记录)
	 * @param userId 非必传
	 * @param borrowId 非必传
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getAppointBorrowInvestRecord(String userId,String borrowId,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(APPOINT_BORROW_INVEST_RECORD).append("&page=")
				.append(page).append("&page_size=").append(pageSize);
		if(userId != null && !"".equals(userId)){
			sb.append("&user_id=").append(userId);
		}
		if(borrowId != null && !"".equals(borrowId)){
			sb.append("&borrow_id=").append(borrowId);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 私人尊享用户投资记录
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getAppointBorrowInvestUserRecord(String userId,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(APPOINT_BORROW_INVEST_USER_RECORD).append("&page=")
				.append(page).append("&page_size=").append(pageSize);
		if(userId != null && !"".equals(userId)){
			sb.append("&user_id=").append(userId);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 预约记录
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getAppointRecordURL(String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(APPOINT_RECORD).append("&borrow_period=").append("0").append("&page=")
				.append(page).append("&page_size=").append(pageSize);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 私人尊享产品预约接口
	 * @param userId
	 * @param money 单位元
	 * @param period 单位天
	 * @param purchaseTime  计划购买时间  yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public String[] getAppointBorrowAppointURL(String userId,String money,String period,String purchaseTime){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(APPOINT_BORROW_APPOINT).append("&user_id=")
				.append(userId).append("&money=").append(money).append("&interest_period=").append(period);
		if(purchaseTime != null && !"".equals(purchaseTime)){
			sb.append("&purchase_time=").append(purchaseTime);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 我的加息券列表
	 * @param userId
	 * @param userStatus
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public String[] getJXQListURL(String userId,String userStatus,String page,String pageSize) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(JXQ_LIST_URL).append("&user_id=")
				.append(userId).append("&use_status=").append(userStatus).append("&page=").append(page)
				.append("&page_size=").append(pageSize);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取某个加息券的详情
	 * @param id
	 * @param money
	 * @return
	 */
	public String[] getJXQDetailsURL(String id,String money){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(JXQ_SELECTONE_URL).append("&id=")
				.append(id).append("&money=").append(money);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取元金币收益
	 * @param userId
	 * @param repayStatus
	 * @return
	 */
	public String[] getYJBInterestURL(String userId,String repayStatus){
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(YJB_INTEREST).append("&invest_user_id=")
					.append(userId).append("&repay_status=").append(repayStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 快捷支付银行列表
	 * @param status 启用，禁用
	 * @param payWayName 支付通道名字 宝付支付
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getQuickPaybankList(String status,String payWayName,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(QUICK_PAY_BANK).append("&status=").append(status)
					.append("&pay_way_name=").append(payWayName).append("&page=").append(page).append("&page_size=").append(pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 企业用户申请注册
	 * @param phone
	 * @param userFrom 用户来源
	 * @param extensionCode 推广码
	 * @return
	 */
	public String[] getCompApplyRegisteURL(String phone,String userFrom,String extensionCode){
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(COMP_APPLY_REGISTE_URL).append("&phone=").append(phone).append("&user_from=").
					append(userFrom).append("&extension_code=").append(extensionCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 企业用户登录
	 * @param username 企业用户的用户名， 不是手机号
	 * @param password
	 * @return
	 */
	public String[] getCompLoginURL(String username,String password){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(COMP_LOGIN_URL).append("&user_name=")
				.append(username).append("&password=")
				.append(Util.md5Encryption(password));
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 企业用户申请提现
	 * @param userId
	 * @param cashAccount 提现金额
	 * @return
	 */
	public String[] getCompApplyCashURL(String userId,String cashAccount){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(COMP_APPLY_CASH_URL).append("&user_id=")
				.append(userId).append("&cash_account=")
				.append(cashAccount);
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取企业用户信息
	 * @param userId
	 * @param yyzzCode 营业执照
	 * @param jgxyCode 机构信用号
	 * @param khxkCode 开户许可号
	 * @return
	 */
	public String[] getCompUserInfo(String userId,String yyzzCode,String jgxyCode,String khxkCode){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(COMP_USERINFO_URL);
		if(!userId.isEmpty()){
			sb.append("&user_id=").append(userId);
		}
		if(!yyzzCode.isEmpty()){
			sb.append("&yyzz_code=").append(yyzzCode);
		}
		if(!jgxyCode.isEmpty()){
			sb.append("&jgxy_code=").append(jgxyCode);
		}
		if(!khxkCode.isEmpty()){
			sb.append("&khxk_code=").append(khxkCode);
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 限时秒标详情
	 * @return
	 */
	public String[] getXSMBDetailURL(String borrowStatus){
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(XSMB_BORROW_DETAIL).append("&borrow_status=").append(borrowStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 根据id获取秒标详情
	 * @param borrowId
	 * @param borrowStatus
	 * @return
	 */
	public String[] getXSMBSelectOneURL(String borrowId,String borrowStatus){
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(XSMB_BORROW_SELECTONE).append("&id=").append(borrowId).append("&borrow_status=").append(borrowStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 当前用户是否购买过该秒标
	 * @param userId
	 * @param borrowId
	 * @return
	 */
	public String[] getXSMBCurrentUserInvestURL(String userId,String borrowId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(XSMB_CURRENT_USER_INVEST).append("&borrow_id=").append(borrowId).
				append("&user_id=").append(userId);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 限时秒标投标接口
	 * @param borrowId
	 * @param money
	 * @param userId
	 * @param investFrom
	 * @return
	 */
	public String[] getXSMBInvestURL(String borrowId,String money,String userId,String investFrom){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(XSMB_INVEST_URL).append("&borrow_id=").append(borrowId).append("&money=").append(money)
				.append("&invest_user_id=").append(userId);
		if(!investFrom.isEmpty()){
			try {
				sb.append("&invest_from=").append(investFrom);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 获取秒标的投资记录
	 * @param userId
	 * @param investStatus 投资状态
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getXSMBInvestRecordURL(String userId,String investStatus,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(XSMB_INVEST_RECORD_URL).append("&user_id=").append(userId).append("&page=").append(page)
				.append("&page_size=").append(pageSize);
		if(!investStatus.isEmpty()){
			sb.append("&invest_status=").append(investStatus);
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 根据用户id和奖品名称判断用户是否领取过
	 * @param userId
	 * @param prizeName
	 * @return
	 */
	public String[] getFLJHSelectPriceByName(String userId,String prizeName){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(FLJH_SELECTPRICE_BYNAME_URL).append("&user_id=").append(userId);
		if(!prizeName.isEmpty()){
			sb.append("&prize=").append(prizeName);
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 查询单条奖品信息
	 * @param id 奖品编号
	 * @param prizeCode 奖品编码
	 * @param prizeName 奖品名称
	 * @param status 已使用 未使用
	 * @param openid 微信Openid
	 * @param userId
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getFLJHPrizeCodeSelectOne(String id,String prizeCode,String prizeName,
											  String status,String openid,String userId) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(FLJH_PRIZECODE_SELECTONE_URL);
		if(!id.isEmpty()){
			sb.append("&id=").append(id);
		}
		if(!prizeCode.isEmpty()){
			sb.append("&prize_code=").append(prizeCode);
		}
		if(!prizeName.isEmpty()){
			sb.append("&prize_name=").append(prizeName);
		}
		if(!status.isEmpty()){
			sb.append("&status=").append(status);
		}
		if(!openid.isEmpty()){
			sb.append("&open_id=").append(openid);
		}
		if(!userId.isEmpty()){
			sb.append("&user_id=").append(userId);
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 判断活动期间用户是否投资过元月盈
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String[] getFLJHYYYInvestByTime(String userId,String startTime,String endTime){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(FLJH_YYYINVEST_BYTIMESPAN).append("&user_id=").append(userId);
		if(!startTime.isEmpty()){
			sb.append("&start_time=").append(startTime);
		}
		if(!endTime.isEmpty()){
			sb.append("&end_time=").append(endTime);
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 领取奖品
	 * @param userId
	 * @param prize 奖品名称
	 * @param activeTitle 活动标识
	 * @param operatingRemark 备注
	 * @param remark 备注
	 * @param status 奖品状态
	 * @param source
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getFLJHReceivePrizeURL(String userId,String prize,String giftId,String activeTitle,
										   String operatingRemark,String remark,String status,String source) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(FLJH_RECEIVE_PRIZE_URL).append("&user_id=").append(userId).
				append("&prize=").append(prize).append("&active_title=").append(activeTitle).
				append("&gift_id=").append(giftId);
		if(!operatingRemark.isEmpty()){
			sb.append("&operating_remark=").append(operatingRemark);
		}
		if(!remark.isEmpty()){
			sb.append("&remark=").append(remark);
		}
		if(!status.isEmpty()){
			sb.append("&status=").append(status);
		}
		if(!source.isEmpty()){
			sb.append("&source=").append(source);
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 获取新春福利 领取压岁钱的次数
	 * @param userId
	 * @param activeTitle
	 * @return
	 */
	public String[] getXCFLLotteryTimes(String userId,String activeTitle){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(XCFL_LOTTERY_TIMES_URL).append("&user_id=").
				append(userId).append("&active_title=").append(activeTitle);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 新春福利 领奖
	 * @param userId
	 * @return
	 */
	public String[] getXCFLDrawPrize(String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(XCFL_LOTTERY_DRAW_PRIZE_URL).append("&user_id=").append(userId);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 判断活动是否开始
	 * @param activeTitle
	 * @return
	 */
	public String[] getXCFLActiveTime(String activeTitle){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(XCFL_CHECK_ACTIVE_START_URL).append("&active_title=").append(activeTitle);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 获取最新一期的稳定赢产品详情
	 * @param borrowStatus
	 * @param isShow
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getWDYBorrowDetailsURL(String borrowStatus,String isShow) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WDY_BORROW_DETAIL_URL).append("&borrow_status=").append(borrowStatus).
				append("&is_show=").append(isShow);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 稳定盈投资接口
	 * @param borrowId
	 * @param money
	 * @param userId
	 * @param investFrom
	 * @param coinMoney
	 * @param couponId
	 * @param redbagId
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getWDYBorrowInvestURL(String borrowId,String money,String userId,
										  String investFrom,String coinMoney,String couponId,String redbagId) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WDY_INVEST_URL).append("&borrow_id=").append(borrowId).append("&money=").append(money).
				append("&invest_user_id=").append(userId).append("&invest_from=").append(investFrom);
		if(coinMoney != null && !"".equals(coinMoney)){
			sb.append("&coin=").append(coinMoney);
		}
		if(couponId != null && !"".equals(couponId)){
			sb.append("&add_coupon_id=").append(couponId);
		}
		if(redbagId != null && !"".equals(redbagId)){
			sb.append("&red_bag_id=").append(redbagId);
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 稳定盈标的投资记录
	 * @param borrowId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getWDYBorrowInvestDetailURL(String borrowId,String page,
												String pageSize) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WDY_INVEST_DETAIL_URL);
		sb.append("&page=").append(page).append("&page_size=").append(pageSize);
		if (borrowId != null && !"".equals(borrowId)) {
			sb.append("&borrow_id=").append(borrowId);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 获取稳定盈投资记录
	 * @param borrowId
	 * @param userId
	 * @param type 0
	 * @param status 0
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getWDYBorrowInvestRecordURL(String borrowId,String userId,String type,
												String status,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WDY_INVEST_RECORD_URL);
		sb.append("&page=").append(page).append("&page_size=").append(pageSize);
		if (borrowId != null && !"".equals(borrowId)) {
			sb.append("&borrow_id=").append(borrowId);
		}
		if (userId != null && !"".equals(userId)) {
			sb.append("&invest_user_id=").append(userId);
		}
		if (type != null && !"".equals(type)) {
			sb.append("&type=").append(type);
		}
		if (status != null && !"".equals(status)) {
			sb.append("&status=").append(status);
		}
		return new String[] { BASE_URL, sb.toString() };
	}

	/**
	 * 乐享返现 开年红 领取加息券
	 * @param userId
	 * @param ticket 加息券规则id
	 * @param startTime 加息券有效期开始时间 yyyy-MM-dd HH:mm:ss
	 * @param endTime 加息券有效期结束时间  yyyy-MM-dd HH:mm:ss
	 * @param remark 备注 开门红 乐享返现
	 * @param type 发送方式
	 * @param isBatch 是批量还是指定用户发送
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getLxfxJXQURL(String userId,String ticket,String startTime,String endTime,String remark,String type,String isBatch) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(LXFX_GET_JXQ_URL).append("&user_id=").append(userId).append("&ticket=").append(ticket).
				append("&start_time=").append(startTime).append("&end_time=").append(endTime).append("&remark=").append(remark).
				append("&type=").append(type).append("&is_batch=").append(isBatch);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 根据规则获取相应的加息券的规则id
	 * @param money 加息券的金额
	 * @param needInvestMoney
	 * @param borrowType 标的类型，多类型用,分隔
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getJXQIDURL(String money,String needInvestMoney,String borrowType) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(LXFX_GET_JXQID_URL).append("&money=").append(money).append("&need_invest_money=").append(needInvestMoney).
				append("&borrow_type=").append(borrowType);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 乐享返现是否领取了加息券
	 * @param userId
	 * @param couponFrom
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String[] getIsGetJXQURL(String userId,String couponFrom,
								   String useStatus,String page,String pageSize,
								   String transfer) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(LXFX_ISGET_JXQ_URL).append("&user_id=").append(userId);
		if(couponFrom != null && !"".equals(couponFrom)){
			sb.append("&coupon_from=").append(couponFrom);
		}
		if(useStatus != null && !"".equals(useStatus)){
			sb.append("&use_status=").append(useStatus);
		}
		if(page != null && !"".equals(page)){
			sb.append("&page=").append(page);
		}
		if(pageSize != null && !"".equals(pageSize)){
			sb.append("&page_size=").append(pageSize);
		}
		if(transfer != null && !"".equals(transfer)){
			sb.append("&transfer=").append(transfer);
		}
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 当前用户是否投资过稳定盈
	 * @param borrowId
	 * @param userId
	 * @return
	 */
	public String[] getWDYCurrentUserInvestURL(String borrowId,String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WDY_CURRENT_USER_INVEST).append("&borrow_id=").append(borrowId).
				append("&user_id=").append(userId);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 根据投资记录的id获取此记录对应的所有投资状况。
	 * @param investRecordId
	 * @return
	 */
	public String[] getWDYBorrowInvestLogSelectListURL(String investRecordId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WDY_BORROWINVESTLOG_SELECTLIST_URL).append("&invest_id=").append(investRecordId);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 根据id获取稳定盈的详情
	 * @param borrowId
	 * @return
	 */
	public String[] getWDYBorrowDetailById(String borrowId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WDY_BORROW_DETAIL_SELECTONE_URL).append("&id=").append(borrowId);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 稳定盈复投
	 * @param wdyLogid
	 * @param userId
	 * @param money
	 * @return
	 */
	public String[] getWDYReinvestURL(String wdyLogid,String userId,String money){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(WDY_REINVEST_URL).append("&wdy_log_id=").append(wdyLogid).append("&user_id=").append(userId)
				.append("&money=").append(money);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 充值记录列表
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public String[] getRechargeRecordListURL(String page,String pageSize,String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(RECHARGE_RECORD_LIST_URL).append("&page=").append(page).append("&page_size=").append(pageSize)
				.append("&user_id=").append(userId);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 系统当前时间
	 * @param type
	 * @return
	 */
	public String[] getSystemNowTimeURL(String type){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(SYSTEM_NOW_TIME_URL).append("&type=").append(type);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 判断某天是否已经签到
	 * @param userId
	 * @param day yyyy-MM-dd
	 * @return
	 */
	public String[] getHDSignIsDaySigned(String userId,String day){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_SIGN_ISDAYSIGNED_URL).append("&user_id=").
				append(userId).append("&day=").append(day);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 3月份签到活动的签到接口
	 * @param userId
	 * @return
	 */
	public String[] getHDSignURL(String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_SIGN_SIGN_URL).append("&user_id=").
				append(userId);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 礼品列表
	 * @param type
	 * @param isShow
	 * @return
	 */
	public String[] getHDGiftListURL(String type,String isShow,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("_URL_=").append(HD_PRIZE_LIST_URL).append("&type=").
					append(type).append("&is_show=").append(isShow).append("&is_app=").
					append("是").append("&page=").
					append(page).append("&page_size=").append(pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 根据礼品id查询该用户是否已经领取过该礼品
	 * @param id 券id
	 * @param giftId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String[] getHDGiftById(String id,String giftId,String userId,String status) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_GIFTCODE_SELECTONE_URL);
		if(!id.isEmpty()){
			sb.append("&id=").append(id);
		}
		if(!giftId.isEmpty()){
			sb.append("&gift_id=").append(giftId);
		}
		if(!userId.isEmpty()){
			sb.append("&user_id=").append(userId);
		}
		if(!status.isEmpty()){
			sb.append("&status=").append(status);
		}
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 *
	 * @param userId
	 * @param activeTitle
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getHDPrizeListURL(String userId,String activeTitle,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HDPRIZE_LIST_URL);
		sb.append("&user_id=").append(userId).append("&active_title=").append(activeTitle).append("&page=")
				.append(page).append("&page_size=").append(pageSize);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 获取奖品详情
	 * @param id
	 * @param name
	 * @param type
	 * @return
	 */
	public String[] getHDLotterySelectoneURL(String id,String name,String type) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_LOTTERY_SELECTONE_URL);
		if(!id.isEmpty()){
			sb.append("&id=").append(id);
		}
		if(!name.isEmpty()){
			sb.append("&name=").append(name);
		}
		if(!type.isEmpty()){
			sb.append("&type=").append(type);
		}
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 获取礼品的券码
	 * @param lotteryId
	 * @param userId
	 * @return
	 */
	public String[] getHDLotteryCodeURL(String lotteryId,String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_LOTTERYCODE_SELECTONE_URL);
		if(!lotteryId.isEmpty()){
			sb.append("&lottery_id=").append(lotteryId);
		}
		if(!userId.isEmpty()){
			sb.append("&user_id=").append(userId);
		}
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 领取绵羊奶粉
	 * @param userId
	 * @return
	 */
	public String[] getHDRobCashGetGiftURL(String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_ROBCASH_GETGIFT_URL);
		sb.append("&user_id=").append(userId);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 是否已经领取或者领完羊奶粉
	 * @param userId
	 * @param activeTitle
	 * @return
	 */
	public String[] getHDRobCashCheckIsReceiveURL(String userId,String activeTitle){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_ROBCASH_CHECK_ISRECEIVE_URL);
		sb.append("&user_id=").append(userId).append("&active_title=").append(activeTitle);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 五月份活动抢现金
	 * @param userId
	 * @return
	 */
	public String[] getHDRobCashRobURL(String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_ROBCASH_ROB_URL);
		sb.append("&user_id=").append(userId);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 下周代抢金额
	 * @return
	 */
	public String[] getHDRobCashCashURL(String nowTime){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_ROBCASH_CASH_URL).append("&now_time=").append(nowTime);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 用户是否已经领取过羊奶粉
	 * @param userId
	 * @param activeTitle
	 * @return
	 */
	public String[] getHDRobCashIsReceiveGift(String userId,String activeTitle){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(HD_ROBCASH_CHECK_ISRECEIVE_URL);
		sb.append("&user_id=").append(userId).append("&active_title=").append(activeTitle);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 活动专区列表
	 * @param page
	 * @param pageSize
	 * @param status
	 * @param fromWhere
	 * @param picShowStatus
	 * @return
	 */
	public String[] getActiveRegionList(String page,String pageSize,String status,String fromWhere,String picShowStatus) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(ACTIVE_REGION_SELECTLIST_URL);
		sb.append("&page=").append(page).append("&page_size=").append(pageSize).append("&status=").append(status)
				.append("&banner_where=").append(fromWhere).append("&banner_pic_show_status=").append(picShowStatus);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 获取理财师的直接好友
	 * @param phone
	 * @param userId
	 * @return
	 */
	public String[] getSubUserURL(String phone,String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(JXQ_TRANSFER_GETSUBUSER_URL);
		sb.append("&phone=").append(phone).append("&user_id=").append(userId);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 转让加息券
	 * @param userId 受让人的id
	 * @param addInterestId 加息券的id
	 * @return
	 */
	public String[] getTransferURL(String userId,String addInterestId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(JXQ_TRANSFER_TRANS_URL);
		sb.append("&user_id=").append(userId).append("&add_interest_id=").append(addInterestId);
		return new String[]{BASE_URL,sb.toString()};
	}

	/**
	 * 首页弹窗banner
	 * @return
	 */
	public String[] getPopBannerURL(){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(POPBANNER_URL);
		sb.append("&banner_where=").append("app_pop");
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 获取好友列表
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getMyFriendsListURL(String userId,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(MYFRIENDS_LIST_URL);
		sb.append("&user_id=").append(userId).append("&page=").
				append(page).append("&page_size=").append(pageSize);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 获取加息券列表
	 * @param userId
	 * @param useStatus 未使用
	 * @param page
	 * @param pageSize
	 * @param transfer 1表示未转让 2表示已转让 0表示不可转让
	 * @return
	 */
	public String[] getTransferedCouponListURL(String userId,String useStatus,
											   String page,String pageSize,String transfer){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(TRANSFERED_COUPONS_LIST_URL);
		sb.append("&user_id=").append(userId).append("&page=").
				append(page).append("&page_size=").append(pageSize).append("&use_status=").append(useStatus).
				append("&transfer=").append(transfer);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 转让多张加息券
	 * @param userId 接收加息券的人的id
	 * @param couponIds 多张加息券的id，用逗号分隔
	 * @return
	 */
	public String[] getTransferCouponURL(String userId,String couponIds){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(TRANS_COUPONS_URL);
		sb.append("&user_id=").append(userId).append("&add_interest_id=").append(couponIds);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 员工专享产品（元聚盈）
	 * @param borrowStatus
	 * @param moneyStatus
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getYGZXBorrowListURL(String borrowStatus,String moneyStatus,String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YGZX_BORROWLIST_URL);
		sb.append("&borrow_status=").append(borrowStatus);
		if(moneyStatus != null && !"".equals(moneyStatus)){
			sb.append("&money_status=").append(moneyStatus);
		}
		sb.append("&page=")
			.append(page).append("&page_size=").append(pageSize);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 员工专享产品详情
	 * @param borrowId
	 * @return
	 */
	public String[] getYGZXBorrowDetailsURL(String borrowId) {
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YGZX_BORROW_DETAILS_URL);
		sb.append("&id=").append(borrowId);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 员工专享投资记录
	 * @param userId
	 * @param borrowId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[] getYGZXBorrowInvestRecordURL(String userId,String borrowId, String page,String pageSize){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YGZX_BORROWINVEST_RECORD_URL);
		sb.append("&page=").append(page).append("&page_size=").append(pageSize);
		if(userId != null && !"".equals(userId)){
			sb.append("&user_id=").append(userId);
		}
		if(borrowId != null && !"".equals(borrowId)){
			sb.append("&borrow_id=").append(borrowId);
		}
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 员工专属产品投资
	 * @param userId
	 * @param borrowId
	 * @param money
	 * @param from
	 * @return
	 */
	public String[] getYGZXBorrowInvestURL(String userId,String borrowId,String money,String from){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YGZX_BORROWINVEST_URL);
		sb.append("&user_id=").append(userId).append("&borrow_id=").append(borrowId).append("&money=").append(money).append("&invest_from=").append(from);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 获取一个红包信息
	 * @param borrowId
	 * @param investId 投资Id
	 * @return
	 */
	public String[] getRedbagSelectoneURL(String borrowId,String investId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(REDBAG_SELECTONE_URL);
		sb.append("&borrow_id=").append(borrowId).append("&borrow_invest_id=").append(investId);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 获取员工专享某个标的详情
	 * @param userId
	 * @param borrowId
	 * @return
	 */
	public String[] getYGZXBorrowById(String userId,String borrowId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(YGZX_BORROWINVEST_LIST_URL);
		sb.append("&borrow_id=").append(borrowId).append("&user_id=").append(userId);
		return new String[]{BASE_URL, sb.toString()};
	}

	/**
	 * 账户中心回款信息
	 * @param userId
	 * @return
	 */
	public String[] getAccountLogRepaymentURL(String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("_URL_=").append(ACCOUNTLOG_REPAYMENTINFO_URL);
		sb.append("&user_id=").append(userId);
		return new String[]{BASE_URL, sb.toString()};
	}
}