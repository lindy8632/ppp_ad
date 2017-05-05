package com.ylfcf.ppp.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ylfcf.ppp.fragment.FirstPageFragment;
import com.ylfcf.ppp.fragment.LicaiFragment;
import com.ylfcf.ppp.fragment.MoreFragment;
import com.ylfcf.ppp.fragment.UserFragment;
import com.ylfcf.ppp.ui.MainFragmentActivity;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnFirstPageZXDOnClickListener;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnMoreFragmentLogoutListener;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnUserFragmentLoginSucListener;

/**
 * 主页面fragment的adapter
 * @author Administrator
 *
 */
public class MainFragmentAdapter extends FragmentStatePagerAdapter{
		//最新推荐综合页
		public static final int FIRSTPAGE_FRAGMENT_FRAG = 0;//首页
		public static final int BORROWLIST_FRAGMENT_FRAG = 1;//产品列表
		public static final int USER_FRAGMENT_FRAG = 2;//用户
		public static final int MORE_FRAGMENT_FRAG = 3;//更多
		
		public boolean isTrue = true;
		private OnFirstPageZXDOnClickListener listener;
		private OnUserFragmentLoginSucListener onLoginSucListener;
		private OnMoreFragmentLogoutListener logoutListener;
		private MainFragmentActivity.OnFirstPageHYTJOnClickListener hytjListener;
		
		public MainFragmentAdapter(FragmentManager fm,OnFirstPageZXDOnClickListener firstZXDOnClickListener,OnUserFragmentLoginSucListener onLoginSucListener,
				OnMoreFragmentLogoutListener logoutListener,MainFragmentActivity.OnFirstPageHYTJOnClickListener hytjListener) {
			super(fm);
			this.listener = firstZXDOnClickListener;
			this.onLoginSucListener = onLoginSucListener;
			this.logoutListener = logoutListener;
			this.hytjListener = hytjListener;
		}

		@Override
		public Object instantiateItem(ViewGroup arg0, int arg1) {
			return super.instantiateItem(arg0, arg1);
		}
		
		@Override
		public Fragment getItem(int arg0) {

			if(!isTrue) {
				return null;
			}
			Fragment fragment = null;
			int index = arg0 % 4;//可以无限循环
			switch(index) {
			case FIRSTPAGE_FRAGMENT_FRAG:
				fragment = FirstPageFragment.newInstance(index,listener,hytjListener);
				break;
			case BORROWLIST_FRAGMENT_FRAG:
				fragment = LicaiFragment.newInstance(index);
				break;
			case USER_FRAGMENT_FRAG:
				fragment = UserFragment.newInstance(index,onLoginSucListener);
				break;
			case MORE_FRAGMENT_FRAG:
				fragment = MoreFragment.newInstance(index,logoutListener);
				break;
			}
			return fragment;
		}
		
		@Override
		public Parcelable saveState() {
			return null;
		}
		
		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}
		
		@Override  
		public int getItemPosition(Object object) {  
			if(!isTrue) {
				return PagerAdapter.POSITION_NONE;
			}
			return PagerAdapter.POSITION_UNCHANGED;  
		}

		@Override
		public int getCount() {
			if(!isTrue) {
				return 0;
			}
			return 4;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
		}

}
