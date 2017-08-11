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
import com.ylfcf.ppp.async.AsyncTYJPageInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.TYJInfo;
import com.ylfcf.ppp.entity.TYJPageInfo;
import com.ylfcf.ppp.fragment.MyTYJNousedFragment;
import com.ylfcf.ppp.fragment.MyTYJOverdueFragment;
import com.ylfcf.ppp.fragment.MyTYJUsedFragment;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的体验金
 * 
 * @author jianbing
 * 
 */
public class MyTYJActivity extends BaseActivity implements OnClickListener {
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;

	private List<TYJInfo> nousedList = new ArrayList<TYJInfo>();// 未使用
	private List<TYJInfo> usedList = new ArrayList<TYJInfo>();// 已使用
	private List<TYJInfo> overdueList = new ArrayList<TYJInfo>();// 已过期
	private int pageNo = 0;
	private int pageSize = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mytyj_activity);
		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("我的体验金");

		mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.my_tyj_activity_tab);
		mViewPager = (ViewPager) findViewById(R.id.my_tyj_activity_viewpager);
		mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mViewPager.setOffscreenPageLimit(2);
		mPagerSlidingTabStrip.setViewPager(mViewPager);
		requestTYJPageInfo("",
				SettingsManager.getUserId(getApplicationContext()), "", "");
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
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsResume(this);//友盟统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		UMengStatistics.statisticsPause(this);//友盟统计时长
	}

	private MyTYJNousedFragment nousedFragment;
	private MyTYJUsedFragment usedFragment;
	private MyTYJOverdueFragment overdueFragment;

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private final String[] titles = { "未使用", "已使用", "已过期" };

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
				if (nousedFragment == null) {
					nousedFragment = new MyTYJNousedFragment();
				}
				return nousedFragment;
			case 1:
				if (usedFragment == null) {
					usedFragment = new MyTYJUsedFragment();
				}
				return usedFragment;
			case 2:
				if (overdueFragment == null) {
					overdueFragment = new MyTYJOverdueFragment();
				}
				return overdueFragment;
			default:
				return null;
			}
		}
	}

	/**
	 * 获取体验金所有的列表
	 * 
	 * @param status
	 * @param userId
	 * @param putStatus
	 * @param activeTitle
	 */
	private void requestTYJPageInfo(String status, String userId,
			String putStatus, String activeTitle) {
		if (mLoadingDialog != null) {
			mLoadingDialog.show();
		}

		AsyncTYJPageInfo asyncTask = new AsyncTYJPageInfo(MyTYJActivity.this,
				String.valueOf(pageNo), String.valueOf(pageSize), status,
				userId, putStatus, activeTitle, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								initDatas(baseInfo.getmTYJPageInfo());
							} else {
								if (nousedFragment != null) {
									nousedFragment.updateAdapter(nousedList);
								}
								if (usedFragment != null) {
									usedFragment.updateAdapter(usedList);
								}
								if (overdueFragment != null) {
									overdueFragment.updateAdapter(overdueList);
								}
							}
						} else {
							if (nousedFragment != null) {
								nousedFragment.updateAdapter(nousedList);
							}
							if (usedFragment != null) {
								usedFragment.updateAdapter(usedList);
							}
							if (overdueFragment != null) {
								overdueFragment.updateAdapter(overdueList);
							}
						}
					}
				});
		asyncTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	private void initDatas(TYJPageInfo pageInfo) {
		if (pageInfo == null) {
			return;
		}
		List<TYJInfo> tyjList = pageInfo.getTyjList();
		int size = tyjList.size();
		for (int i = 0; i < size; i++) {
			TYJInfo info = tyjList.get(i);
			long nowTime = System.currentTimeMillis();
			long endTime = Util.string2date(info.getEnd_time());// 过期时间
			if ("已使用".equals(info.getStatus())) {
				usedList.add(info);
			} else {
				if (nowTime > endTime) {
					// 已过期
					overdueList.add(info);
				} else if ("未使用".equals(info.getStatus())) {
					nousedList.add(info);
				}
			}
		}

		if (nousedFragment != null) {
			nousedFragment.updateAdapter(nousedList);
		}
		if (usedFragment != null) {
			usedFragment.updateAdapter(usedList);
		}
		if (overdueFragment != null) {
			overdueFragment.updateAdapter(overdueList);
		}
	}

}
