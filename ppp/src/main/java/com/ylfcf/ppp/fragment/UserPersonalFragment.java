package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncGetLCSName;
import com.ylfcf.ppp.async.AsyncHuiFuRMBAccount;
import com.ylfcf.ppp.async.AsyncPrizeList;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.async.AsyncUserYUANAccount;
import com.ylfcf.ppp.async.AsyncXCFLActiveTime;
import com.ylfcf.ppp.async.AsyncYJBInterest;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.entity.UserYUANAccountInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.inter.Inter.OnUserYUANAccountInter;
import com.ylfcf.ppp.ptr.PtrClassicFrameLayout;
import com.ylfcf.ppp.ptr.PtrDefaultHandler;
import com.ylfcf.ppp.ptr.PtrFrameLayout;
import com.ylfcf.ppp.ptr.PtrHandler;
import com.ylfcf.ppp.ui.AccountCenterActivity;
import com.ylfcf.ppp.ui.AccountSettingActivity;
import com.ylfcf.ppp.ui.AccountSettingCompActivity;
import com.ylfcf.ppp.ui.AwardDetailsActivity;
import com.ylfcf.ppp.ui.BannerTopicActivity;
import com.ylfcf.ppp.ui.BindCardActivity;
import com.ylfcf.ppp.ui.BorrowListYJYActivity;
import com.ylfcf.ppp.ui.FundsDetailsActivity;
import com.ylfcf.ppp.ui.InvitateActivity;
import com.ylfcf.ppp.ui.LXFXTempActivity;
import com.ylfcf.ppp.ui.MainFragmentActivity;
import com.ylfcf.ppp.ui.PrizeRegion2TempActivity;
import com.ylfcf.ppp.ui.RechargeActivity;
import com.ylfcf.ppp.ui.RechargeCompActivity;
import com.ylfcf.ppp.ui.UserInvestRecordActivity;
import com.ylfcf.ppp.ui.UserVerifyActivity;
import com.ylfcf.ppp.ui.WithdrawActivity;
import com.ylfcf.ppp.ui.WithdrawCompActivity;
import com.ylfcf.ppp.ui.WithdrawPwdGetbackActivity;
import com.ylfcf.ppp.util.Constants;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;

import java.text.DecimalFormat;

import static com.ylfcf.ppp.R.id.my_account_personal_zscp_invest_btn;

/**
 * Created by Administrator on 2017/9/18.
 */

public class UserPersonalFragment extends BaseFragment implements View.OnClickListener{
    private static final int REQUEST_GET_USERINFO_WHAT = 1003;
    private static final int REQUEST_GET_USERINFO_SUCCESS = 1004;

    private static final int REQUEST_YUANMONEY_WHAT = 1005;
    private static final int REQUEST_YUANMOENY_SUC = 1006;

    private static final int REQUEST_HYFL_WHAT = 1009;

    private static final int REQUEST_LCS_WHAT = 1010;

    private MainFragmentActivity mainActivity;
    private View rootView;

    //个人用户顶部
    private TextView usernameTV;//个人用户的用户名
    private TextView zhyeTotalTV;//账户总余额
    private TextView zhyeBalanceTV;//账户余额
    private TextView withdrawBtn;
    private TextView rechargeBtn;

    //未实名的提示布局
    private LinearLayout unverifyLayout;
    private TextView unverifyPromptTV;
    private ImageView unverifyXImg;

    //元聚盈模块
    private LinearLayout yjyLayout;//元聚盈模块
    private Button yjyInvestBtn,yjyAppointBtn;//元聚盈投资以及预约按钮

    private LinearLayout yqyjLayout;//邀请有奖
    private TextView yqyjText;
    private TextView yqyjPrompt;
    private TextView usedYJBTV;//元金币可用金额
    private LinearLayout jlmxLayout;//奖励明细
    private LinearLayout zjmxLayout;//资金明细
    private LinearLayout tbjlLayout;//投标记录
    private LinearLayout zhszLayout;//账户设置
    private View line1,line2,line3,line4,line5,line6;
    private RelativeLayout accouncenterLayout;
    private View personalMainLayout;
    private com.ylfcf.ppp.ptr.PtrClassicFrameLayout mainRefreshLayout;
    private ImageView phoneLogo,idcardLogo,bankcardLogo,headLogo;

    private UserInfo mUserInfo;
    private UserRMBAccountInfo yilianAccountInfo;//易联账户信息
    private UserRMBAccountInfo huifuAccountInfo;//汇付账户信息
    private BaseInfo yjbInterestBaseInfo;//元金币产生的收益
    private String hfuserId = "";
    private boolean isSetWithdrawPwd = false;//用户是否已经设置交易密码
    private boolean isVerify,isBindcard;
    private boolean isLcs;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_GET_USERINFO_WHAT:
                    requestUserInfo(SettingsManager.getUserId(getActivity().getApplicationContext()),
                            SettingsManager.getUser(getActivity().getApplicationContext()));
                    break;
                case REQUEST_GET_USERINFO_SUCCESS:
                    mUserInfo = (UserInfo) msg.obj;
                    hfuserId = mUserInfo.getHf_user_id();
                    handler.sendEmptyMessage(REQUEST_LCS_WHAT);
                    if(hfuserId != null && !"".equals(hfuserId)){
                        requestYilianAccount(mUserInfo.getId(),true);
                        return;
                    }
                    requestYilianAccount(mUserInfo.getId(),false);
                    break;
                case REQUEST_YUANMONEY_WHAT:
                    requestYuanMoney(SettingsManager.getUserId(getActivity().getApplicationContext()));
                    break;
                case REQUEST_HYFL_WHAT:
                    requestActiveTime("HYFL_02");
                    break;
                case REQUEST_LCS_WHAT:
                    requestLcsName(SettingsManager.getUser(mainActivity.getApplicationContext()));
                    break;
            }
        }
    };

    public static UserPersonalFragment newInstance(){
        UserPersonalFragment fragment = new UserPersonalFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mainActivity = (MainFragmentActivity) getActivity();
        if(rootView==null){
            rootView=inflater.inflate(R.layout.user_personal_fragment, null);
        }
        findViews(rootView);
//		//缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        YLFLogger.d("UserFragment -- onCreateView");
        handler.sendEmptyMessage(REQUEST_GET_USERINFO_WHAT);
        handler.sendEmptyMessage(REQUEST_YUANMONEY_WHAT);
        //判断会员福利2期是否还在进行
        handler.sendEmptyMessage(REQUEST_HYFL_WHAT);
        //检查是否实名绑卡
        checkIsVerify("初始化");
        return rootView;
    }

    private void findViews(View view){
        initRefreshLayout(view);
        usernameTV = (TextView)view.findViewById(R.id.my_account_personal_username);
        zhyeTotalTV = (TextView)view.findViewById(R.id.my_account_personal_totalmoney_tv);
        zhyeBalanceTV = (TextView)view.findViewById(R.id.my_account_personal_balancemoney_tv);
        withdrawBtn = (TextView)view.findViewById(R.id.my_account_personal_withdraw_btn);
        withdrawBtn.setOnClickListener(this);
        rechargeBtn = (TextView)view.findViewById(R.id.my_account_personal_recharge_btn);
        rechargeBtn.setOnClickListener(this);
        unverifyLayout = (LinearLayout) view.findViewById(R.id.my_account_personal_unverify_layout);
        unverifyLayout.setOnClickListener(this);
        unverifyPromptTV = (TextView) view.findViewById(R.id.my_account_personal_unverify_prompt);
        unverifyXImg = (ImageView) view.findViewById(R.id.my_account_personal_x_img);
        unverifyXImg.setOnClickListener(this);

        //元聚盈
        yjyLayout = (LinearLayout) view.findViewById(R.id.my_account_personal_zscp_layout);
        yjyInvestBtn = (Button) view.findViewById(my_account_personal_zscp_invest_btn);
        yjyInvestBtn.setOnClickListener(this);
        yjyAppointBtn = (Button) view.findViewById(R.id.my_account_personal_zscp_yy_btn);
        yjyAppointBtn.setOnClickListener(this);

        yqyjLayout = (LinearLayout)view.findViewById(R.id.my_account_personal_yqyj_layout);
        yqyjLayout.setOnClickListener(this);
        yqyjText = (TextView) view.findViewById(R.id.my_account_personal_yqyj_text);
        yqyjPrompt = (TextView) view.findViewById(R.id.my_account_personal_yqyj_prompt);
        jlmxLayout = (LinearLayout)view.findViewById(R.id.my_account_personal_jlmx_layout);
        jlmxLayout.setOnClickListener(this);
        usedYJBTV = (TextView)view.findViewById(R.id.my_account_personal_used_yjb);
        zjmxLayout = (LinearLayout)view.findViewById(R.id.my_account_personal_zjmx_layout);
        zjmxLayout.setOnClickListener(this);
        tbjlLayout = (LinearLayout)view.findViewById(R.id.my_account_personal_tbjl_layout);
        tbjlLayout.setOnClickListener(this);
        zhszLayout = (LinearLayout)view.findViewById(R.id.my_account_personal_zhsz_layout);
        zhszLayout.setOnClickListener(this);
        line1 = view.findViewById(R.id.my_account_personal_line1);
        line2 = view.findViewById(R.id.my_account_personal_line2);
        line3 = view.findViewById(R.id.my_account_personal_line3);
        line4 = view.findViewById(R.id.my_account_personal_line4);
        line5 = view.findViewById(R.id.my_account_personal_line5);
        line6 = view.findViewById(R.id.my_account_personal_line6);
        accouncenterLayout = (RelativeLayout)view.findViewById(R.id.my_account_personal_layout_account_center);
        accouncenterLayout.setOnClickListener(this);
        personalMainLayout = view.findViewById(R.id.user_personal_fragment_layout);
        phoneLogo = (ImageView)view.findViewById(R.id.my_account_personal_phone_img);
        idcardLogo = (ImageView)view.findViewById(R.id.my_account_personal_idcard_img);
        bankcardLogo = (ImageView)view.findViewById(R.id.my_account_personal_bankcard_img);
        headLogo = (ImageView)view.findViewById(R.id.my_account_personal_headimg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_account_personal_withdraw_btn:
                //提现
                withdrawBtn.setEnabled(false);
                checkIsVerify("提现");
                break;
            case R.id.my_account_personal_recharge_btn:
                //充值
                if(SettingsManager.isPersonalUser(mainActivity)){
                    checkIsVerify("充值");
                }else if(SettingsManager.isCompanyUser(mainActivity)){
                    Intent intentRechargeComp = new Intent(mainActivity,RechargeCompActivity.class);
                    startActivity(intentRechargeComp);
                }

                break;
            case R.id.my_account_personal_jlmx_layout:
                Intent intentAward = new Intent(mainActivity,AwardDetailsActivity.class);
                startActivity(intentAward);
                break;
            case R.id.my_account_personal_zjmx_layout:
                //资金明细
                Intent intentFund = new Intent(mainActivity,FundsDetailsActivity.class);
                intentFund.putExtra("userinfo", mUserInfo);
                startActivity(intentFund);
                break;
            case R.id.my_account_personal_tbjl_layout:
                Intent intentUserRecord = new Intent(mainActivity,UserInvestRecordActivity.class);
                startActivity(intentUserRecord);
                break;
            case R.id.my_account_personal_yqyj_layout:
                //邀请有奖、
                yqyjLayout.setEnabled(false);
                checkIsVerify("邀请有奖");
                break;
            case R.id.my_account_personal_zhsz_layout:
                //账户设置
                if(SettingsManager.isPersonalUser(mainActivity)){
                    zhszLayout.setEnabled(false);
                    checkIsVerify("账户设置");
                }else if(SettingsManager.isCompanyUser(mainActivity)){
                    Intent intentZHSZComp = new Intent(mainActivity,AccountSettingCompActivity.class);
                    startActivity(intentZHSZComp);
                }
                break;
            case my_account_personal_zscp_invest_btn:
                //元聚盈投资按钮
                Intent yjyInvestIntent = new Intent(mainActivity,BorrowListYJYActivity.class);
                startActivity(yjyInvestIntent);
                break;
            case R.id.my_account_personal_zscp_yy_btn:
                //元聚盈预约
                Intent intentYJYAppoint = new Intent(getActivity(),BannerTopicActivity.class);
                BannerInfo info = new BannerInfo();
                info.setArticle_id(Constants.TopicType.YJY_APPOINT);
                info.setLink_url(URLGenerator.YJY_TOPIC_URL);
                intentYJYAppoint.putExtra("BannerInfo",info);
                startActivity(intentYJYAppoint);
                break;
            case R.id.my_account_personal_unverify_layout:
                //点击去实名去认证
                String tag = (String)unverifyLayout.getTag();
                Intent intent = new Intent();
                if(!isVerify){
                    //未实名
                    intent.setClass(getActivity(),UserVerifyActivity.class);
                }else if(!isBindcard){
                    intent.setClass(getActivity(),BindCardActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.my_account_personal_x_img:
                unverifyLayout.setVisibility(View.GONE);
                break;
            case R.id.my_account_personal_layout_account_center:
                //跳转账户中心
                Intent accCenterIntent = new Intent(mainActivity, AccountCenterActivity.class);
                accCenterIntent.putExtra("ylUserRMBAccountInfo",yilianAccountInfo);
                accCenterIntent.putExtra("hfUserRMBAccountInfo",huifuAccountInfo);
                accCenterIntent.putExtra("yjbBaseInfo",yjbInterestBaseInfo);
                startActivity(accCenterIntent);
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && (!isBindcard || !isVerify)){
            checkIsVerify("初始化");
        }
    }

    private void initUserInfoData(boolean isVerify, boolean isBinding){
        unverifyLayout.setVisibility(View.VISIBLE);
        if(isVerify){
            idcardLogo.setBackgroundResource(R.drawable.my_account_personal_idcard_light);
            if(isBinding){
                unverifyLayout.setVisibility(View.GONE);
                bankcardLogo.setBackgroundResource(R.drawable.my_account_personal_bankcard_light);
            }else{
                bankcardLogo.setBackgroundResource(R.drawable.my_account_personal_bankcard_logo);
                unverifyPromptTV.setText("您尚未绑定银行卡，点击去绑定");
            }
        }else{
            idcardLogo.setBackgroundResource(R.drawable.my_account_personal_idcard_logo);
            bankcardLogo.setBackgroundResource(R.drawable.my_account_personal_bankcard_logo);
            unverifyPromptTV.setText("您尚未完成实名认证，点击去实名");
        }
    }

    /**
     * 下拉刷新的布局
     * @param v
     */
    private void initRefreshLayout(View v){
        mainRefreshLayout = (PtrClassicFrameLayout) v.findViewById(R.id.user_personal_fragment_refresh_layout);
        mainRefreshLayout.setLastUpdateTimeRelateObject(this);
        mainRefreshLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                handler.sendEmptyMessageDelayed(REQUEST_GET_USERINFO_WHAT,200L);
                handler.sendEmptyMessageDelayed(REQUEST_YUANMONEY_WHAT,300L);
                if(!isVerify || !isBindcard){
                    checkIsVerify("初始化");
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, personalMainLayout, header);
            }
        });
        mainRefreshLayout.setResistance(1.7f);
        mainRefreshLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        mainRefreshLayout.setDurationToClose(200);
        mainRefreshLayout.setDurationToCloseHeader(1000);
        // default is false
        mainRefreshLayout.setPullToRefresh(false);
        // default is true
        mainRefreshLayout.setKeepHeaderWhenRefresh(true);
    }

    private void initAccountData(UserRMBAccountInfo yilianAccount,UserRMBAccountInfo huifuAccount,double yjbInterest){
        double yilianBalance = 0d;
        double huifuBalance = 0d;
        double totalBalance = 0d;
        double dsBalance = 0d;//待收
        double djBalance = 0d;//冻结
        double accuntTotal = 0d;
        DecimalFormat df = new DecimalFormat("#.00");
        try {
            dsBalance = Double.parseDouble(yilianAccount.getCollection_money()) + yjbInterest;
        } catch (Exception e) {
        }
        try {
            djBalance = Double.parseDouble(yilianAccount.getFrozen_money());
        } catch (Exception e) {
        }
        try {
            yilianBalance = Double.parseDouble(yilianAccount.getUse_money());
            if(huifuAccount != null){
                huifuBalance = Double.parseDouble(huifuAccount.getUse_money());
            }
            totalBalance = yilianBalance + huifuBalance;
            if(totalBalance < 1){
                zhyeBalanceTV.setText("0"+df.format(totalBalance));
            }else{
                zhyeBalanceTV.setText(df.format(totalBalance));
            }
            accuntTotal = totalBalance + dsBalance + djBalance;
            if(accuntTotal < 1){
                zhyeTotalTV.setText("0"+df.format(accuntTotal));
            }else{
                zhyeTotalTV.setText(df.format(accuntTotal));
            }
        } catch (Exception e) {
        }
    }

    /**
     * 领取加息券的Dialog
     */
    private void showGetJXQDialog(final String type) {
        View contentView = LayoutInflater.from(mainActivity).inflate(
                R.layout.user_fragment_lxfx_jxq, null);
        Button leftBtn = (Button) contentView.findViewById(R.id.user_fragment_lxfx_dialog_layout_leftBtn);
        Button rightBtn = (Button) contentView.findViewById(R.id.user_fragment_lxfx_dialog_layout_rightBtn);
        ImageView delBtn = (ImageView) contentView.findViewById(R.id.user_fragment_lxfx_dialog_layout_delbtn);
        TextView content = (TextView) contentView.findViewById(R.id.user_fragment_lxfx_content);
        if("JXQ".equals(type)){
            content.setText("您有一张加息券未领取！");
        }else if("HYFL_02".equals(type)){
            content.setText("亲爱的用户，您有20种礼品可领取哟~");
        }
        final CheckBox cb = (CheckBox) contentView.findViewById(R.id.user_fragment_lxfx_dialog_cb);
        AlertDialog.Builder builder = new AlertDialog.Builder(
                mainActivity, R.style.Dialog_Transparent); // 先得到构造器
        builder.setView(contentView);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        final String keyLXFX = SettingsManager.getUserId(getActivity())+"lxfx";
        final String keyHYFL = SettingsManager.getUserId(getActivity())+"hyfl02";
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked()){
                    if("JXQ".equals(type)){
                        SettingsManager.setLXFXJXQFlag(mainActivity,keyLXFX, false);
                    }else if("HYFL_02".equals(type)){
                        SettingsManager.setHYFLFlag(mainActivity,keyHYFL, false);
                    }
                }else{
                    if("JXQ".equals(type)){
                        SettingsManager.setLXFXJXQFlag(mainActivity,keyLXFX, true);
                    }else if("HYFL_02".equals(type)){
                        SettingsManager.setHYFLFlag(mainActivity,keyHYFL, true);
                    }
                }
                if("JXQ".equals(type)){
                    Intent intent = new Intent(mainActivity,LXFXTempActivity.class);
                    startActivity(intent);
                }else if("HYFL_02".equals(type)){
                    Intent intent = new Intent(mainActivity,PrizeRegion2TempActivity.class);
                    startActivity(intent);
                }
                dialog.dismiss();
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked()){
                    if("JXQ".equals(type)){
                        SettingsManager.setLXFXJXQFlag(mainActivity,keyLXFX, false);
                    }else if("HYFL_02".equals(type)){
                        SettingsManager.setHYFLFlag(mainActivity,keyHYFL, false);
                    }
                }else{
                    if("JXQ".equals(type)){
                        SettingsManager.setLXFXJXQFlag(mainActivity,keyLXFX, true);
                    }else if("HYFL_02".equals(type)){
                        SettingsManager.setHYFLFlag(mainActivity,keyHYFL, true);
                    }
                }
                dialog.dismiss();
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked()){
                    if("JXQ".equals(type)){
                        SettingsManager.setLXFXJXQFlag(mainActivity,keyLXFX, false);
                    }else if("HYFL_02".equals(type)){
                        SettingsManager.setHYFLFlag(mainActivity,keyHYFL, false);
                    }
                }else{
                    if("JXQ".equals(type)){
                        SettingsManager.setLXFXJXQFlag(mainActivity,keyLXFX, true);
                    }else if("HYFL_02".equals(type)){
                        SettingsManager.setHYFLFlag(mainActivity,keyHYFL, true);
                    }
                }
                dialog.dismiss();
            }
        });
        // 参数都设置完成了，创建并显示出来
        dialog.show();
        WindowManager windowManager = mainActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth() * 4 / 5;
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * 请求用户信息，根据hf_user_id字段判断用户是否有汇付账户
     * @param userId
     * @param phone
     */
    private void requestUserInfo(final String userId,String phone){
        AsyncUserSelectOne userTask = new AsyncUserSelectOne(mainActivity, userId, phone,"", "", new OnGetUserInfoByPhone() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        UserInfo userInfo = baseInfo.getUserInfo();
                        Message msg = handler.obtainMessage(REQUEST_GET_USERINFO_SUCCESS);
                        msg.obj = userInfo;
                        handler.sendMessage(msg);
                    }else{
                        requestYilianAccount(userId,false);
                    }
                }else{
                    requestYilianAccount(userId,false);
                }
            }
        });
        userTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * 易联账户
     * @param userId
     * @param isRequestHuifu 是否有汇付的账户
     */
    private void requestYilianAccount(final String userId,final boolean isRequestHuifu){
        AsyncYiLianRMBAccount yilianTask = new AsyncYiLianRMBAccount(mainActivity, userId, new OnCommonInter(){
            @Override
            public void back(BaseInfo info) {
                if(info != null){
                    int resultCode = SettingsManager.getResultCode(info);
                    if(resultCode == 0){
                        yilianAccountInfo = info.getRmbAccountInfo();
                        if(isRequestHuifu){
                            requestHuifuAccount(userId);
                        }else{
                            requestYJBInterest(userId, "未还款");
                        }
                    }
                }
            }
        });
        yilianTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * 元金币账户
     * @param userId
     */
    private void requestYuanMoney(String userId){
        AsyncUserYUANAccount accountTask = new AsyncUserYUANAccount(mainActivity,
                userId, new OnUserYUANAccountInter() {
            @Override
            public void back(BaseInfo info) {
                mainRefreshLayout.refreshComplete();
                if(info != null){
                    int resultCode = SettingsManager.getResultCode(info);
                    if(resultCode == 0){
                        UserYUANAccountInfo accountInfo = info.getYuanAccountInfo();
                        if(accountInfo != null){
                            double coinD = 0d;
                            try {
                                coinD= Double.parseDouble(accountInfo.getUse_coin());
                            } catch (Exception e) {
                            }
                            if(coinD <= 0){
                                usedYJBTV.setVisibility(View.GONE);
                            }else{
                                usedYJBTV.setVisibility(View.VISIBLE);
                                usedYJBTV.setText(accountInfo.getUse_coin()+"元金币可用");
                            }
                        }
                    }else{
                        usedYJBTV.setVisibility(View.GONE);
                    }
                }else{
                    usedYJBTV.setVisibility(View.GONE);
                }
            }
        });
        accountTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * 判断活动是否已经开始
     * @param activeTitle
     */
    private void requestActiveTime(String activeTitle){
        AsyncXCFLActiveTime task = new AsyncXCFLActiveTime(mainActivity, activeTitle,
                new OnCommonInter() {
                    @Override
                    public void back(BaseInfo baseInfo) {
                        if(baseInfo != null){
                            int resultCode = SettingsManager.getResultCode(baseInfo);
                            if (resultCode == 0) {
                                //活动已开始
                                requestPrizeList(SettingsManager.getUserId(getActivity().getApplicationContext()));
                            } else if (resultCode == -3) {
                                //活动结束
                            } else if (resultCode == -2) {
                                //活动还没开始
                            }
                        }
                    }
                });
        task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * 验证用户是否已经认证
     * @param type “充值”,“提现”，“邀请有奖”
     */
    private void checkIsVerify(final String type){
        if(mainActivity.loadingDialog != null){
            mainActivity.loadingDialog.show();
        }
        RequestApis.requestIsVerify(mainActivity, SettingsManager.getUserId(mainActivity), new OnIsVerifyListener() {
            @Override
            public void isVerify(boolean flag, Object object) {
                isVerify = flag;
                if(mainActivity.loadingDialog != null && mainActivity.loadingDialog.isShowing()){
                    mainActivity.loadingDialog.dismiss();
                }
                if("邀请有奖".equals(type)){
                    rechargeBtn.setEnabled(true);
                    withdrawBtn.setEnabled(true);
                    yqyjLayout.setEnabled(true);
                    zhszLayout.setEnabled(true);
                    Intent intent = new Intent();
                    intent.setClass(mainActivity,InvitateActivity.class);
                    intent.putExtra("is_verify", flag);
                    startActivity(intent);
                    return;
                }
                if(flag){
                    //用户已经实名
                    checkIsBindCard(type);
                }else{
                    if("初始化".equals(type)){
                        initUserInfoData(false,false);
                        return;
                    }
                    rechargeBtn.setEnabled(true);
                    withdrawBtn.setEnabled(true);
                    yqyjLayout.setEnabled(true);
                    zhszLayout.setEnabled(true);
                    if("账户设置".equals(type)){
                        Intent intentAccountSetting = new Intent(mainActivity,AccountSettingActivity.class);
                        intentAccountSetting.putExtra("is_binding", false);
                        startActivity(intentAccountSetting);
                        return;
                    }
                    //用户没有实名
                    Intent intent = new Intent(mainActivity,UserVerifyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);
                }
            }

            @Override
            public void isSetWithdrawPwd(boolean flag, Object object) {
                //用户是否已经设置提现密码
                isSetWithdrawPwd = flag;
                if(SettingsManager.isCompanyUser(getActivity().getApplicationContext())&&"提现".equals(type)){
                    rechargeBtn.setEnabled(true);
                    withdrawBtn.setEnabled(true);
                    yqyjLayout.setEnabled(true);
                    zhszLayout.setEnabled(true);
                    Intent intent = new Intent();
                    //企业用户
                    //要先判断用户是否已经设置提现密码（已经在判断用户是否实名的时候判断过，即字段isSetWithdrawPwd）
                    if(isSetWithdrawPwd){
                        //用户已经设置提现密码
                        intent.setClass(mainActivity, WithdrawCompActivity.class);
                    }else{
                        intent.setClass(mainActivity, WithdrawPwdGetbackActivity.class);
                        intent.putExtra("type", "设置");
                    }
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 判断用户是否已经绑卡
     * @param type "充值","提现","邀请有奖"
     */
    private void checkIsBindCard(final String type){
        RequestApis.requestIsBinding(mainActivity, SettingsManager.getUserId(mainActivity), "宝付", new OnIsBindingListener() {
            @Override
            public void isBinding(boolean flag, Object object) {
                isBindcard = flag;
                rechargeBtn.setEnabled(true);
                withdrawBtn.setEnabled(true);
                yqyjLayout.setEnabled(true);
                zhszLayout.setEnabled(true);
                Intent intent = new Intent();
                if(flag){
                    if("初始化".equals(type)){
                        initUserInfoData(true,true);
                        return;
                    }
                    //用户已经绑卡
                    if("充值".equals(type)){
                        //那么直接跳到充值页面
                        intent.setClass(mainActivity, RechargeActivity.class);
                    }else if("提现".equals(type)){
                        //要先判断用户是否已经设置提现密码（已经在判断用户是否实名的时候判断过，即字段isSetWithdrawPwd）
                        if(isSetWithdrawPwd){
                            //用户已经设置提现密码
                            intent.setClass(mainActivity, WithdrawActivity.class);
                        }else{
                            intent.setClass(mainActivity, WithdrawPwdGetbackActivity.class);
                            intent.putExtra("type", "设置");
                        }
                    }else if("邀请有奖".equals(type)){
                        intent.setClass(mainActivity, InvitateActivity.class);
                    }else if("账户设置".equals(type)){
                        intent.setClass(mainActivity, AccountSettingActivity.class);
                        intent.putExtra("is_binding", true);
                    }
                    startActivity(intent);
                }else{
                    if("初始化".equals(type)){
                        initUserInfoData(true,false);
                        return;
                    }
                    //用户还没有绑卡
                    if("账户设置".equals(type)){
                        intent.setClass(mainActivity, AccountSettingActivity.class);
                        intent.putExtra("is_binding", false);
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putString("type", type);
                        intent.putExtra("bundle", bundle);
                        intent.setClass(mainActivity, BindCardActivity.class);
                    }
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 汇付账户信息
     * @param userId
     */
    private void requestHuifuAccount(final String userId){
        AsyncHuiFuRMBAccount huifuTask = new AsyncHuiFuRMBAccount(mainActivity, userId, new OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        huifuAccountInfo = baseInfo.getRmbAccountInfo();
                        requestYJBInterest(userId, "未还款");
                    }
                }
            }
        });
        huifuTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * 元金币本金及收益
     * @param userId
     * @param repayStatus
     */
    private void requestYJBInterest(String userId,String repayStatus){
        AsyncYJBInterest yjbTask = new AsyncYJBInterest(mainActivity, userId, repayStatus, new OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        yjbInterestBaseInfo = baseInfo;
                        try {
                            initAccountData(yilianAccountInfo,huifuAccountInfo,Double.parseDouble(baseInfo.getMsg()));
                        } catch (Exception e) {
                            initAccountData(yilianAccountInfo,huifuAccountInfo,0);
                        }
                    }else{
                        initAccountData(yilianAccountInfo,huifuAccountInfo,0);
                    }
                }
            }
        });
        yjbTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * 我的礼品列表
     * @param userId
     */
    private void requestPrizeList(String userId){
        AsyncPrizeList prizeListTask = new AsyncPrizeList(mainActivity, userId, "0", "5", "","HYFL_02",
                new OnCommonInter(){
                    @Override
                    public void back(BaseInfo baseInfo) {
                        if(baseInfo != null){
                            int resultCode = SettingsManager.getResultCode(baseInfo);
                            if(resultCode == 0){
                                //
                            }else{
                                //没有领取过,福利计划2期
                                if(SettingsManager.getLXFXJXQFlag(mainActivity,SettingsManager.getUserId(getActivity())+"hyfl02")){
                                    showGetJXQDialog("HYFL_02");
                                }
                            }
                        }else{
                        }
                    }
                });
        prizeListTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * 获取理财师的名字
     * @param phone
     */
    private void requestLcsName(String phone){
        AsyncGetLCSName lcsTask = new AsyncGetLCSName(mainActivity, phone, new Inter.OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {
                if(baseInfo != null){
                    int resultCode = SettingsManager.getResultCode(baseInfo);
                    if(resultCode == 0){
                        //是理财师
                        isLcs = true;
                        yjyLayout.setVisibility(View.VISIBLE);
                    }else{
                        //非理财师
                        isLcs = false;
                        yjyLayout.setVisibility(View.GONE);
                    }
                }else{
                    isLcs = false;
                    yjyLayout.setVisibility(View.GONE);
                }
                if(isLcs){
                    if("".equals(mUserInfo.getReal_name())){
                        usernameTV.setText("您好，尊贵的"+mUserInfo.getUser_name());
                    }else{
                        usernameTV.setText("您好，尊贵的"+mUserInfo.getReal_name());
                    }
                    headLogo.setBackgroundResource(R.drawable.my_account_personal_lcs_headlogo);
                    yqyjPrompt.setText("君子爱财，取自友道");
                    yqyjText.setText("元财道");
                }else{
                    if("".equals(mUserInfo.getReal_name())){
                        usernameTV.setText("您好，尊敬的"+mUserInfo.getUser_name());
                    }else{
                        usernameTV.setText("您好，尊敬的"+mUserInfo.getReal_name());
                    }
                    headLogo.setBackgroundResource(R.drawable.my_account_personal_nor_headlogo);
                    yqyjPrompt.setText("");
                    yqyjText.setText("邀请有奖");
                }
            }
        });
        lcsTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }
}
