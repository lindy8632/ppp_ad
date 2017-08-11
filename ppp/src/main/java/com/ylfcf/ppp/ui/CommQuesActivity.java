package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.fragment.CommQuesFragment;
import com.ylfcf.ppp.util.UMengStatistics;

/**
 * 常见问题
 * 
 * @author Administrator
 * 
 */
public class CommQuesActivity extends BaseActivity implements OnClickListener {
	private static final String className = "CommQuesActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private ViewPager mViewPager;
	
	private ImageView accountSafeArrow,//账号和密码
	interestArrow,//收益和利率
	rechargeArrow;//充值和提现
	private LinearLayout accountTab,interestTab,rechargeTab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.comm_ques_activity);

		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("常见问题");

		mViewPager = (ViewPager) findViewById(R.id.comm_ques_activity_viewpager);
		mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mViewPager.setOffscreenPageLimit(2);
		
		accountSafeArrow = (ImageView) findViewById(R.id.comm_ques_activity_accountsafe_arrow);
		interestArrow = (ImageView) findViewById(R.id.comm_ques_activity_interest_arrow);
		rechargeArrow = (ImageView) findViewById(R.id.comm_ques_activity_withdraw_arrow);
		
		accountTab = (LinearLayout) findViewById(R.id.comm_ques_activity_account_tab);
		accountTab.setOnClickListener(this);
		interestTab = (LinearLayout) findViewById(R.id.comm_ques_activity_interest_tab);
		interestTab.setOnClickListener(this);
		rechargeTab = (LinearLayout) findViewById(R.id.comm_ques_activity_withdraw_tab);
		rechargeTab.setOnClickListener(this);
		initListeners();
	}

	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//友盟统计页面跳转
		UMengStatistics.statisticsResume(this);//友盟统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);//友盟统计页面跳转
		UMengStatistics.statisticsPause(this);//友盟统计时长
	}

	@SuppressWarnings("deprecation")
	private void initListeners(){
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				if(arg0 == 0){
					accountSafeArrow.setVisibility(View.VISIBLE);
					interestArrow.setVisibility(View.GONE);
					rechargeArrow.setVisibility(View.GONE);
				}else if(arg0 == 1){
					accountSafeArrow.setVisibility(View.GONE);
					interestArrow.setVisibility(View.VISIBLE);
					rechargeArrow.setVisibility(View.GONE);
				}else if(arg0 == 2){
					accountSafeArrow.setVisibility(View.GONE);
					interestArrow.setVisibility(View.GONE);
					rechargeArrow.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.comm_ques_activity_account_tab:
			mViewPager.setCurrentItem(0);
			accountSafeArrow.setVisibility(View.VISIBLE);
			interestArrow.setVisibility(View.GONE);
			rechargeArrow.setVisibility(View.GONE);
			break;
		case R.id.comm_ques_activity_interest_tab:
			mViewPager.setCurrentItem(1);
			accountSafeArrow.setVisibility(View.GONE);
			interestArrow.setVisibility(View.VISIBLE);
			rechargeArrow.setVisibility(View.GONE);
			break;
		case R.id.comm_ques_activity_withdraw_tab:
			mViewPager.setCurrentItem(2);
			accountSafeArrow.setVisibility(View.GONE);
			interestArrow.setVisibility(View.GONE);
			rechargeArrow.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	private CommQuesFragment accountSafeFragment;
	private CommQuesFragment interestFragment;
	private CommQuesFragment withdrawFragment;

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private final String[] titles = { "账号和密码", "收益和利率", "充值和提现" };

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return super.instantiateItem(container, position);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (accountSafeFragment == null) {
					accountSafeFragment = new CommQuesFragment(0);
				}
				return accountSafeFragment;
			case 1:
				if (interestFragment == null) {
					interestFragment = new CommQuesFragment(1);
				}
				return interestFragment;
			case 2:
				if (withdrawFragment == null) {
					withdrawFragment = new CommQuesFragment(2);
				}
				return withdrawFragment;
			default:
				return null;
			}
		}
	}
}
