package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncAddPhoneInfo;
import com.ylfcf.ppp.async.AsyncCompLogin;
import com.ylfcf.ppp.async.AsyncLogin;
import com.ylfcf.ppp.db.DBGesturePwdManager;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.GesturePwdEntity;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnLoginInter;
import com.ylfcf.ppp.ui.ForgetPwdActivity;
import com.ylfcf.ppp.ui.GestureEditActivity;
import com.ylfcf.ppp.ui.MainFragmentActivity;
import com.ylfcf.ppp.ui.RegisteActivity;
import com.ylfcf.ppp.util.Constants.UserType;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;


/**
 * 首页的登录页面。
 * Created by Administrator on 2017/9/18.
 */

public class UserLoginFragment extends BaseFragment implements View.OnClickListener{
    private static final int REQUEST_PERSONAL_LOGIN_SUCCESS_WHAT = 1001;
    private static final int REQUEST_PERSONAL_LOGIN_EXCEPTION_WHAT = 1002;

    private static final int REQUEST_COMPANY_LOGIN_SUCCESS_WHAT = 1005;

    private MainFragmentActivity mainActivity;
    private View rootView;

    private Button navPersonalBtn,navCompanyBtn;
    private View personalLoginLayout,companyLoginLayout;

    /**
     * 个人登录
     */
    private TextView registerPersonalTV,forgetPwdPersonalTV;
    private Button loginPersonalBtn;
    private EditText phonePersonalET,pwdPersonalET;
    private String phonePersonal = "";
    private String pwdPersonal = "";
    private String userId = "";
    private String hfuserId = "";

    /**
     * 企业登录
     */
    private TextView registerCompanyTV,forgetPwdCompanyTV;
    private Button loginCompanyBtn;
    private EditText phoneCompanyET,pwdCompanyET;
    private String usernameCompany = "";
    private String pwdCompany = "";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REQUEST_PERSONAL_LOGIN_SUCCESS_WHAT:
                    BaseInfo baseInfo = (BaseInfo) msg.obj;
                    UserInfo userInfo = null;
                    if(baseInfo != null){
                        userInfo = baseInfo.getUserInfo();
                        if(userInfo != null){
                            SettingsManager.setUser(mainActivity,phonePersonal);
                            SettingsManager.setLoginPassword(mainActivity,pwdPersonal,true);
                            SettingsManager.setUserId(mainActivity, userInfo.getId());
                            SettingsManager.setUserName(mainActivity, userInfo.getUser_name());
                            SettingsManager.setUserRegTime(mainActivity, userInfo.getReg_time());
                            if("vip".equals(userInfo.getType())){
                                SettingsManager.setUserType(mainActivity, UserType.USER_VIP_PERSONAL);
                            }else{
                                SettingsManager.setUserType(mainActivity, UserType.USER_NORMAL_PERSONAL);
                            }
                            addPhoneInfo(userInfo.getId(), phonePersonal, "", "");
                            onLoginSuc.onLoginSuc(userInfo);
                        }
                    }
                    break;
                case REQUEST_COMPANY_LOGIN_SUCCESS_WHAT:
                    BaseInfo baseInfoComp = (BaseInfo) msg.obj;
                    if(baseInfoComp != null) {
                        UserInfo mUserInfo = baseInfoComp.getUserInfo();
                        SettingsManager.setUser(mainActivity, usernameCompany);
                        SettingsManager.setLoginPassword(mainActivity, pwdCompany, true);
                        SettingsManager.setUserId(mainActivity, mUserInfo.getId());
                        SettingsManager.setUserName(mainActivity, mUserInfo.getUser_name());
                        SettingsManager.setUserRegTime(mainActivity, mUserInfo.getReg_time());
                        SettingsManager.setUserType(mainActivity, UserType.USER_COMPANY);
                        SettingsManager.setCompPhone(mainActivity, mUserInfo.getCo_mobile());
                        addPhoneInfo(mUserInfo.getId(), mUserInfo.getCo_phone(), "", "");
                        onLoginSuc.onLoginSuc(mUserInfo);
                    }
                    break;
                case REQUEST_PERSONAL_LOGIN_EXCEPTION_WHAT:
                    String loginMsg = (String) msg.obj;
                    Util.toastShort(mainActivity, loginMsg);
                    break;
            }
        }
    };

    private MainFragmentActivity.OnUserFragmentLoginSucListener onLoginSuc;
    public static UserLoginFragment newInstance(MainFragmentActivity.OnUserFragmentLoginSucListener onUserFragmentLoginSucListener){
        UserLoginFragment fragment = new UserLoginFragment(onUserFragmentLoginSucListener);
        return fragment;
    }

    public UserLoginFragment(MainFragmentActivity.OnUserFragmentLoginSucListener
                                     onUserFragmentLoginSucListener){
        this.onLoginSuc = onUserFragmentLoginSucListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mainActivity = (MainFragmentActivity) getActivity();
        if(rootView==null){
            rootView=inflater.inflate(R.layout.user_login_fragment, null);
        }
        findViews(rootView);
//		//缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        YLFLogger.d("UserFragment -- onCreateView");
        return rootView;
    }

    private void findViews(View view){
        navPersonalBtn = (Button) view.findViewById(R.id.user_login_fragment_nav_personal_btn);
        navPersonalBtn.setOnClickListener(this);
        navCompanyBtn = (Button) view.findViewById(R.id.user_longi_fragment_nav_company_btn);
        navCompanyBtn.setOnClickListener(this);
        personalLoginLayout = view.findViewById(R.id.user_login_fragment_personal_login_layout);
        companyLoginLayout = view.findViewById(R.id.user_login_fragment_company_login_layout);

        //个人用户登录页面
        phonePersonalET = (EditText)view.findViewById(R.id.login_personal_phone_et);
        pwdPersonalET = (EditText)view.findViewById(R.id.login_personal_pwd_et);
        loginPersonalBtn = (Button)view.findViewById(R.id.login_personal_loginbtn);
        loginPersonalBtn.setOnClickListener(this);
        registerPersonalTV = (TextView)view.findViewById(R.id.login_personal_register_tv);
        registerPersonalTV.setOnClickListener(this);
        forgetPwdPersonalTV = (TextView)view.findViewById(R.id.login_personal_forget_pwd_tv);
        forgetPwdPersonalTV.setOnClickListener(this);

        //企业用户登录页面
        phoneCompanyET = (EditText)view.findViewById(R.id.login_company_phone_et);
        pwdCompanyET = (EditText)view.findViewById(R.id.login_company_pwd_et);
        loginCompanyBtn = (Button)view.findViewById(R.id.login_company_loginbtn);
        loginCompanyBtn.setOnClickListener(this);
        registerCompanyTV = (TextView)view.findViewById(R.id.login_company_register_tv);
        registerCompanyTV.setOnClickListener(this);
        forgetPwdCompanyTV = (TextView)view.findViewById(R.id.login_company_forget_pwd_tv);
        forgetPwdCompanyTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_personal_loginbtn:
                checkPersonalUserData();
                break;
            case R.id.login_company_loginbtn:
                checkCompanyUserData();
                break;
            case R.id.login_personal_register_tv:
                Intent intentPer = new Intent(mainActivity,RegisteActivity.class);
                intentPer.putExtra("from_where", UserType.USER_NORMAL_PERSONAL);
                startActivity(intentPer);
                break;
            case R.id.login_company_register_tv:
                Intent intentComp = new Intent(mainActivity,RegisteActivity.class);
                intentComp.putExtra("from_where", UserType.USER_COMPANY);
                startActivity(intentComp);
                break;
            case R.id.login_personal_forget_pwd_tv:
                Intent intentFP = new Intent(mainActivity,ForgetPwdActivity.class);
                intentFP.putExtra("from_where", UserType.USER_NORMAL_PERSONAL);
                startActivity(intentFP);
                break;
            case R.id.login_company_forget_pwd_tv:
                Intent intentFC = new Intent(mainActivity,ForgetPwdActivity.class);
                intentFC.putExtra("from_where", UserType.USER_COMPANY);
                startActivity(intentFC);
                break;
            case R.id.user_login_fragment_nav_personal_btn:
                //个人登录的导航
                initPersonalLayout();
                break;
            case R.id.user_longi_fragment_nav_company_btn:
                //企业登录导航
                initCompanyLayout();
                break;
        }
    }

    /**
     * 检查个人用户登录信息
     */
    private void checkPersonalUserData(){
        //隐藏键盘
        Util.hiddenSoftInputWindow(mainActivity);
        phonePersonal = phonePersonalET.getText().toString().trim();
        pwdPersonal = pwdPersonalET.getText().toString().trim();
        if(Util.checkPhoneNumber(phonePersonal)) {
            if(Util.checkPassword(pwdPersonal)) {
                requestLogin(phonePersonal,pwdPersonal);
            }else{
                Util.toastShort(mainActivity, "请输入登录密码");
            }
        }else{
            Util.toastShort(mainActivity, "手机号码不合法");
        }
    }

    /**
     * 检查企业用户登录信息
     */
    private void checkCompanyUserData(){
        //隐藏键盘
        try{
            Util.hiddenSoftInputWindow(mainActivity);//当此界面没有焦点的时候会报出空指针的异常。
        }catch (Exception e){
            e.printStackTrace();
        }
        usernameCompany = phoneCompanyET.getText().toString().trim();
        pwdCompany = pwdCompanyET.getText().toString().trim();
        if (!usernameCompany.isEmpty()) {
            if (Util.checkPassword(pwdCompany)) {
                requestCompLogin(usernameCompany, pwdCompany);
            } else {
                Util.toastShort(mainActivity, "请输入登录密码");
            }
        } else {
            Util.toastShort(mainActivity, "请输入用户名");
        }
    }

    /**
     * 初始化个人登录的布局
     */
    private void initPersonalLayout(){
        personalLoginLayout.setVisibility(View.VISIBLE);
        companyLoginLayout.setVisibility(View.GONE);
        navPersonalBtn.setEnabled(false);
        navPersonalBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
        navCompanyBtn.setEnabled(true);
        navCompanyBtn.setTextColor(getResources().getColor(R.color.gray));
    }

    /**
     * 初始化企业登录的布局
     */
    private void initCompanyLayout(){
        personalLoginLayout.setVisibility(View.GONE);
        companyLoginLayout.setVisibility(View.VISIBLE);
        navPersonalBtn.setEnabled(true);
        navPersonalBtn.setTextColor(getResources().getColor(R.color.gray));
        navCompanyBtn.setEnabled(false);
        navCompanyBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
    }

    /**
     *个人用户登录
     * @param phone
     * @param pwd
     */
    private void requestLogin(String phone,String pwd){
        mainActivity.loadingDialog.show();
        AsyncLogin loginTask = new AsyncLogin(mainActivity, phone, pwd, new OnLoginInter() {
            @Override
            public void back(final BaseInfo baseInfo) {
                mainActivity.loadingDialog.dismiss();
                if(baseInfo == null){
                    Message msg = handler.obtainMessage(REQUEST_PERSONAL_LOGIN_EXCEPTION_WHAT);
                    msg.obj = "您的网络不给力";
                    handler.sendMessage(msg);
                    return;
                }
                int resultCode = SettingsManager.getResultCode(baseInfo);
                if(resultCode == 0){
                    UserInfo user = baseInfo.getUserInfo();
                    if(user != null){
                        GesturePwdEntity entity = DBGesturePwdManager.getInstance(getActivity().getApplicationContext()).getGesturePwdEntity(user.getId());
                        if(entity != null && !entity.getPwd().isEmpty()){

                        }else{
                            Intent intent = new Intent(mainActivity,GestureEditActivity.class);
                            startActivity(intent);
                        }
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = handler.obtainMessage(REQUEST_PERSONAL_LOGIN_SUCCESS_WHAT);
                            msg.obj = baseInfo;
                            handler.sendMessage(msg);
                        }
                    }, 200L);
                } else if(resultCode == -1){
                    Message msg = handler.obtainMessage(REQUEST_PERSONAL_LOGIN_EXCEPTION_WHAT);
                    msg.obj = "用户名或者密码错误！";
                    handler.sendMessage(msg);
                }else{
                    Message msg = handler.obtainMessage(REQUEST_PERSONAL_LOGIN_EXCEPTION_WHAT);
                    msg.obj = baseInfo.getMsg();
                    handler.sendMessage(msg);
                }
            }
        });
        loginTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * 企业用户登录
     * @param username
     * @param password
     */
    private void requestCompLogin(String username,String password){
        if(mainActivity.loadingDialog != null){
            mainActivity.loadingDialog.show();
        }
        AsyncCompLogin loginTask = new AsyncCompLogin(mainActivity, username, password,
                new OnCommonInter(){
                    @Override
                    public void back(final BaseInfo baseInfo) {
                        mainActivity.loadingDialog.dismiss();
                        if(baseInfo == null){
                            Message msg = handler.obtainMessage(REQUEST_PERSONAL_LOGIN_EXCEPTION_WHAT);
                            msg.obj = "您的网络不给力";
                            handler.sendMessage(msg);
                            return;
                        }
                        int resultCode = SettingsManager.getResultCode(baseInfo);
                        if(resultCode == 0){
                            UserInfo user = baseInfo.getUserInfo();
                            if(user != null){
                                GesturePwdEntity entity = DBGesturePwdManager.getInstance(getActivity().getApplicationContext()).getGesturePwdEntity(user.getId());
                                if(entity != null && !entity.getPwd().isEmpty()){

                                }else{
                                    Intent intent = new Intent(mainActivity,GestureEditActivity.class);
                                    startActivity(intent);
                                }
                            }

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Message msg = handler.obtainMessage(REQUEST_COMPANY_LOGIN_SUCCESS_WHAT);
                                    msg.obj = baseInfo;
                                    handler.sendMessage(msg);
                                }
                            }, 200L);
                        }else{
                            Message msg = handler.obtainMessage(REQUEST_PERSONAL_LOGIN_EXCEPTION_WHAT);
                            msg.obj = baseInfo.getMsg();
                            handler.sendMessage(msg);
                        }
                    }
                });
        loginTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }

    /**
     * 将手机信息加入到后台数据库
     * @param userId
     * @param phone
     * @param location
     * @param contact
     */
    private void addPhoneInfo(String userId,String phone,String location,String contact){
        String phoneModel = android.os.Build.MODEL;
        String sdkVersion = android.os.Build.VERSION.SDK;
        String systemVersion = android.os.Build.VERSION.RELEASE;
        AsyncAddPhoneInfo addPhoneInfoTask = new AsyncAddPhoneInfo(mainActivity, userId, phone, phoneModel,
                sdkVersion, systemVersion, "android", location, contact, new OnCommonInter() {
            @Override
            public void back(BaseInfo baseInfo) {

            }
        });
        addPhoneInfoTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }
}
