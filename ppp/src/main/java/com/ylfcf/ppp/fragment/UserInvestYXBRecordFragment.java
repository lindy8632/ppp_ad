package com.ylfcf.ppp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.ui.UserInvestRecordActivity;
import com.ylfcf.ppp.widget.PagerSlidingTabStrip;

/**
 * 用户投资记录 -- 元信宝
 * @author Mr.liu
 *
 */
public class UserInvestYXBRecordFragment extends BaseFragment{
	private UserInvestRecordActivity mainActivity;
	private View rootView;

	private PagerSlidingTabStrip yxbPagerSlidingTabStrip;
	private ViewPager yxbViewPager;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainActivity = (UserInvestRecordActivity) getActivity();
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.userinvest_yxb_record_fragment, null);
			findViews(rootView);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	private void findViews(View view) {
		yxbPagerSlidingTabStrip = (PagerSlidingTabStrip)view.findViewById(R.id.userinvest_yxb_record_fragment_tab);
		yxbViewPager = (ViewPager)view.findViewById(R.id.userinvest_yxb_record_fragment_viewpager);
		yxbViewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
		yxbViewPager.setOffscreenPageLimit(1);
		yxbPagerSlidingTabStrip.setViewPager(yxbViewPager);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
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
	
}
