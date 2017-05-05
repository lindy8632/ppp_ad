package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.graphics.Paint;
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
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.fragment.MyHBNousedFragment;
import com.ylfcf.ppp.fragment.MyHBOverdueFragment;
import com.ylfcf.ppp.fragment.MyHBUsedFragment;
import com.ylfcf.ppp.widget.LoadingDialog;
import com.ylfcf.ppp.widget.PagerSlidingTabStrip;

/**
 * 我的红包
 * 
 * @author Administrator
 * 
 */
public class MyHongbaoActivity extends BaseActivity implements OnClickListener {
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private TextView rightTV;

	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	public LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_hongbao_layout);
		loadingDialog = new LoadingDialog(MyHongbaoActivity.this, "正在加载...",
				R.anim.loading);
		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("我的红包");
		rightTV = (TextView) findViewById(R.id.common_topbar_right_text);
		rightTV.setVisibility(View.VISIBLE);
		rightTV.setText("红包规则");
		rightTV.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
		rightTV.getPaint().setAntiAlias(true);//抗锯齿
		rightTV.setOnClickListener(this);
		mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.my_hongbao_tab);
		mViewPager = (ViewPager) findViewById(R.id.my_hongbao_viewpager);
		mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		mViewPager.setOffscreenPageLimit(2);
		mPagerSlidingTabStrip.setViewPager(mViewPager);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.common_topbar_right_text:
			Intent intentBanner = new Intent(MyHongbaoActivity.this,BannerTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setArticle_id("80");
			bannerInfo.setLink_url("http://wap.ylfcf.com/home/index/redbag.html#app");
			intentBanner.putExtra("BannerInfo", bannerInfo);
			startActivity(intentBanner);
			break;
		default:
			break;
		}
	}

	private MyHBNousedFragment nousedFragment;
	private MyHBUsedFragment usedFragment;
	private MyHBOverdueFragment overdueFragment;

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
					nousedFragment = new MyHBNousedFragment();
				}
				return nousedFragment;
			case 1:
				if (usedFragment == null) {
					usedFragment = new MyHBUsedFragment();
				}
				return usedFragment;
			case 2:
				if (overdueFragment == null) {
					overdueFragment = new MyHBOverdueFragment();
				}
				return overdueFragment;
			default:
				return null;
			}
		}
	}
}
