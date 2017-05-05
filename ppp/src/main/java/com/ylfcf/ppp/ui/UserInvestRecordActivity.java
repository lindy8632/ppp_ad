package com.ylfcf.ppp.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnScrollChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncInvestSRZXUserRecord;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.TYJInfo;
import com.ylfcf.ppp.entity.TYJPageInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.fragment.MyTYJNousedFragment;
import com.ylfcf.ppp.fragment.MyTYJOverdueFragment;
import com.ylfcf.ppp.fragment.MyTYJUsedFragment;
import com.ylfcf.ppp.fragment.UserInvestWDYRecordFragment;
import com.ylfcf.ppp.fragment.UserInvestSRZXRecordFragment;
import com.ylfcf.ppp.fragment.UserInvestVIPRecordFragment;
import com.ylfcf.ppp.fragment.UserInvestXSMBRecordFragment;
import com.ylfcf.ppp.fragment.UserInvestYXBRecordFragment;
import com.ylfcf.ppp.fragment.UserInvestYYYRecordFragment;
import com.ylfcf.ppp.fragment.UserInvestZXDRecordFragment;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;
import com.ylfcf.ppp.inter.Inter.OnIsYXBInvestorListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.widget.LoadingDialog;
import com.ylfcf.ppp.widget.PagerSlidingTabStrip;

/**
 * 用户投标记录页面 包括元月盈、元政盈、元信宝、VIP产品以及预约产品、零存整取产品
 * @author Mr.liu
 *
 */
public class UserInvestRecordActivity extends BaseActivity implements OnClickListener{
	private static final int REQUEST_USERINFO_WHAT = 1221;//请求用户个人信息
	private static final int REQUEST_USERINFO_SUCCESS = 1222;
	
	private static final int REQUEST_ISINVESTED_YXB_WHAT = 1223;//判断用户是否投资过元信宝
	
	private static final int REQUEST_ISINVESTED_SRZX_WAHT = 1224;//是否投资过私人尊享产品
	private static final int REQUEST_ISINVESTED_SRZX_SUCCESS = 1225;
	private static final int REQUEST_ISINVESTED_SRZX_NODATA = 1227;
	
	private static final int INIT_ADAPTER = 1226;//	初始化适配器
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private View jianbianView;//viewpager右侧的阴影
	public LoadingDialog loadingDialog;
	private String fromWhere;
	
	private List<String> titlesList = new ArrayList<String>();

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_USERINFO_WHAT:
				if(SettingsManager.isPersonalUser(getApplicationContext())){
					requestUserInfo(SettingsManager.getUserId(getApplicationContext()), SettingsManager.getUser(getApplicationContext()));
				}else if(SettingsManager.isCompanyUser(getApplicationContext())){
					requestUserInfo(SettingsManager.getUserId(getApplicationContext()), "");
				}
				break;
			case REQUEST_USERINFO_SUCCESS:
				UserInfo mUserInfo = (UserInfo) msg.obj;
				if(mUserInfo.getType().contains("vip")){
					titlesList.add(3, "VIP");
				}else{
				}
				break;
			case REQUEST_ISINVESTED_YXB_WHAT:
				isYXBInvestor();
				break;
			case REQUEST_ISINVESTED_SRZX_WAHT:
				getSRZXInvestRecordList(SettingsManager.getUserId(getApplicationContext()), "");
				break;
			case REQUEST_ISINVESTED_SRZX_SUCCESS:
				if(titlesList.contains("VIP") && titlesList.contains("元信宝")){
					titlesList.add(5, "私人尊享");
					titlesList.add(6, "秒标");
				}else if(titlesList.contains("VIP") || titlesList.contains("元信宝")){
					titlesList.add(4, "私人尊享");
					titlesList.add(5, "秒标");
				}else if(!titlesList.contains("VIP") && !titlesList.contains("元信宝")){
					titlesList.add(3, "私人尊享");
					titlesList.add(4, "秒标");
				}
				break;
			case REQUEST_ISINVESTED_SRZX_NODATA:
				if(titlesList.contains("VIP") && titlesList.contains("元信宝")){
					titlesList.add(5, "秒标");
				}else if(titlesList.contains("VIP") || titlesList.contains("元信宝")){
					titlesList.add(4, "秒标");
				}else if(!titlesList.contains("VIP") && !titlesList.contains("元信宝")){
					titlesList.add(3, "秒标");
				}
				break;
			case INIT_ADAPTER:
				initAdapter();
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.userinvest_record_activity);
		fromWhere = getIntent().getStringExtra("from_where");
		loadingDialog = new LoadingDialog(UserInvestRecordActivity.this, "正在加载...",
				R.anim.loading);
		initTitleList();
		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("投资记录");

		jianbianView = findViewById(R.id.userinvest_record_activity_tab_jianbian);
		mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.userinvest_record_activity_tab);
		mViewPager = (ViewPager) findViewById(R.id.userinvest_record_activity_viewpager);
		mPagerSlidingTabStrip.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				YLFLogger.d("onPageSelected*****"+arg0);
				if(titlesList.size() > 3){
					if(arg0 == titlesList.size() - 1){
						jianbianView.setVisibility(View.GONE);
					}else{
						jianbianView.setVisibility(View.VISIBLE);
					}
				}else{
					jianbianView.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				YLFLogger.d("onPageScrolled"+"*****arg0="+ arg0
						+ "******arg1="+arg1+"******arg2="+arg2);
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				YLFLogger.d("onPageScrollStateChanged*****" + arg0);
			}
		});
		handler.sendEmptyMessage(REQUEST_USERINFO_WHAT);
	}
	
	private void initAdapter(){
		if(titlesList.size() >= 4){
			jianbianView.setVisibility(View.VISIBLE);
		}
		int curPosition = 0;
		mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mViewPager.setOffscreenPageLimit(0);
		mPagerSlidingTabStrip.setViewPager(mViewPager);
		for(int i=0;i<titlesList.size();i++){
			if("元政盈".equals(titlesList.get(i)) && "元政盈".equals(fromWhere)){
				curPosition = i;
			}else if("元月盈".equals(titlesList.get(i)) && "元月盈".equals(fromWhere)){
				curPosition = i;
			}else if("薪盈计划".equals(titlesList.get(i)) && "稳定盈".equals(fromWhere)){
				curPosition = i;
			}else if("VIP".equals(titlesList.get(i)) && "VIP".equals(fromWhere)){
				curPosition = i;
			}else if("元信宝".equals(titlesList.get(i)) && "元信宝".equals(fromWhere)){
				curPosition = i;
			}else if("私人尊享".equals(titlesList.get(i)) && "私人尊享".equals(fromWhere)){
				curPosition = i;
			}else if("秒标".equals(titlesList.get(i)) && "秒标".equals(fromWhere)){
				curPosition = i;
			}
		}
		mViewPager.setCurrentItem(curPosition);
	}
	
	private void initTitleList(){
		titlesList.add(0, "元政盈");
		titlesList.add(1, "元月盈");
		titlesList.add(2,"薪盈计划");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	private UserInvestZXDRecordFragment zxdRecordFragment;//政信贷（元政盈）
	private UserInvestYYYRecordFragment yyyRecordFragment;//元月盈
	private UserInvestWDYRecordFragment lczqRecordFragment;//零存整取产品
	private UserInvestVIPRecordFragment vipRecordFragment;//vip产品
	private UserInvestYXBRecordFragment yxbRecordFragment;//元信宝
	private UserInvestSRZXRecordFragment srzxRecordFragment;//私人尊享产品
	private UserInvestXSMBRecordFragment xsmbRecordFragment;//限时秒标

	public class MyPagerAdapter extends FragmentPagerAdapter {
		
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titlesList.get(position);
		}

		@Override
		public int getCount() {
			return titlesList.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return super.instantiateItem(container, position);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (zxdRecordFragment == null) {
					zxdRecordFragment = new UserInvestZXDRecordFragment();
				}
				return zxdRecordFragment;
			case 1:
				if (yyyRecordFragment == null) {
					yyyRecordFragment = new UserInvestYYYRecordFragment();
				}
				return yyyRecordFragment;
			case 2:
				if (lczqRecordFragment == null) {
					lczqRecordFragment = new UserInvestWDYRecordFragment();
				}
				return lczqRecordFragment;
			case 3:
				if("VIP".equals(titlesList.get(3))){
					if (vipRecordFragment == null) {
						vipRecordFragment = new UserInvestVIPRecordFragment();
					}
					return vipRecordFragment;
				}else if("元信宝".equals(titlesList.get(3))){
					if (yxbRecordFragment == null) {
						yxbRecordFragment = new UserInvestYXBRecordFragment();
					}
					return yxbRecordFragment;
				}else if("私人尊享".equals(titlesList.get(3))){
					if (srzxRecordFragment == null) {
						srzxRecordFragment = new UserInvestSRZXRecordFragment();
					}
					return srzxRecordFragment;
				}else if("秒标".equals(titlesList.get(3))){
					if (xsmbRecordFragment == null) {
						xsmbRecordFragment = new UserInvestXSMBRecordFragment();
					}
					return xsmbRecordFragment;
				}
			case 4:
				if("元信宝".equals(titlesList.get(4))){
					if (yxbRecordFragment == null) {
						yxbRecordFragment = new UserInvestYXBRecordFragment();
					}
					return yxbRecordFragment;
				}else if("私人尊享".equals(titlesList.get(4))){
					if (srzxRecordFragment == null) {
						srzxRecordFragment = new UserInvestSRZXRecordFragment();
					}
					return srzxRecordFragment;
				}else if("秒标".equals(titlesList.get(4))){
					if (xsmbRecordFragment == null) {
						xsmbRecordFragment = new UserInvestXSMBRecordFragment();
					}
					return xsmbRecordFragment;
				}
			case 5:
				if("私人尊享".equals(titlesList.get(5))){
					if(srzxRecordFragment == null) {
						srzxRecordFragment = new UserInvestSRZXRecordFragment();
					}
					return srzxRecordFragment;
				} else if("秒标".equals(titlesList.get(5))){
					if (xsmbRecordFragment == null) {
						xsmbRecordFragment = new UserInvestXSMBRecordFragment();
					}
					return xsmbRecordFragment;
				}
			case 6:
				if (xsmbRecordFragment == null) {
					xsmbRecordFragment = new UserInvestXSMBRecordFragment();
				}
				return xsmbRecordFragment;
			default:
				return null;
			}
		}
	}
	
	private void isYXBInvestor(){
		RequestApis.requestIsYXBInvestor(this, SettingsManager.getUserId(getApplicationContext()), 0, 10, new OnIsYXBInvestorListener() {
			@Override
			public void isYXBInvestor(boolean flag) {
				if(flag){
					//投资过元信宝
					if(titlesList.contains("VIP")){
						titlesList.add(4, "元信宝");
					}else{
						titlesList.add(3, "元信宝");
					}
				}else{
					//没有投资过元信宝
				}
				handler.sendEmptyMessage(REQUEST_ISINVESTED_SRZX_WAHT);
			}
		});
	}

	/**
	 * 请求用户信息，根据hf_user_id字段判断用户是否有汇付账户
	 * @param userId
	 * @param phone
	 */
	private void requestUserInfo(final String userId,String phone){
		AsyncUserSelectOne userTask = new AsyncUserSelectOne(this, userId, phone, "", new OnGetUserInfoByPhone() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						UserInfo userInfo = baseInfo.getUserInfo();
						Message msg = handler.obtainMessage(REQUEST_USERINFO_SUCCESS);
						msg.obj = userInfo;
						handler.sendMessage(msg);
					}else{
					}
				}else{
				}
				handler.sendEmptyMessage(REQUEST_ISINVESTED_YXB_WHAT);
			}
		});
		userTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 获取私人尊享投资记录列表
	 * @param investUserId
	 * @param borrowId
	 * @param status
	 * @param pageNo
	 * @param pageSize
	 */
	private void getSRZXInvestRecordList(String investUserId,String borrowId){
		AsyncInvestSRZXUserRecord asyncInvestRecord = new AsyncInvestSRZXUserRecord(this, investUserId, 
				0, 5, new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0 && baseInfo.getmInvestRecordPageInfo() != null){
								List<InvestRecordInfo> recordList = baseInfo.getmInvestRecordPageInfo().getInvestRecordList();
								if(recordList != null && recordList.size() > 0){
									//投资过私人尊享
									handler.sendEmptyMessage(REQUEST_ISINVESTED_SRZX_SUCCESS);
								}else{
									handler.sendEmptyMessage(REQUEST_ISINVESTED_SRZX_NODATA);
								}
							}else{
								handler.sendEmptyMessage(REQUEST_ISINVESTED_SRZX_NODATA);
							}
						}else{
							handler.sendEmptyMessage(REQUEST_ISINVESTED_SRZX_NODATA);
						}
						handler.sendEmptyMessage(INIT_ADAPTER);
					}
		});
		asyncInvestRecord.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
