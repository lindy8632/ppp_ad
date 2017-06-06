package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.fragment.YXBInvestRecordFragment;
import com.ylfcf.ppp.fragment.YXBRedeemRecordFragment;
import com.ylfcf.ppp.widget.PagerSlidingTabStrip;

/**
 * 用户交易记录(认购记录和赎回记录) ---- 元信宝
 * @author Administrator
 */
public class YXBTransRecordActivity extends BaseActivity implements OnClickListener{
	private View topLayout;
	private TextView topTitle;
	private LinearLayout topLeftLayout;
	
	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.yxb_trans_record_activity);
		mApp.addActivity(this);
		findViews();
	}
	
	private void findViews(){
		topLayout = findViewById(R.id.yxb_trans_record_activity_toplayout);
		topLeftLayout = (LinearLayout) topLayout.findViewById(R.id.common_topbar_left_layout);
		topLeftLayout.setOnClickListener(this);
		topTitle = (TextView) topLayout.findViewById(R.id.common_page_title);
		topTitle.setText("交易记录");
		
		mPagerSlidingTabStrip = (PagerSlidingTabStrip)findViewById(R.id.yxb_trans_record_activity_tab);
		mViewPager = (ViewPager)findViewById(R.id.yxb_trans_record_activity_viewpager);
		mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mViewPager.setOffscreenPageLimit(1);
		mPagerSlidingTabStrip.setViewPager(mViewPager);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mApp.removeActivity(this);
	}
	
	private YXBInvestRecordFragment yxbInvestRecordFragment;
	private YXBRedeemRecordFragment yxbRedeemRecordFragment;
	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private final String[] titles = { "认购记录", "赎回记录"};

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
				if (yxbInvestRecordFragment == null) {
					yxbInvestRecordFragment = new YXBInvestRecordFragment();
				}
				return yxbInvestRecordFragment;
			case 1:
				if (yxbRedeemRecordFragment == null) {
					yxbRedeemRecordFragment = new YXBRedeemRecordFragment();
				}
				return yxbRedeemRecordFragment;
			default:
				return null;
			}
		}

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
}
