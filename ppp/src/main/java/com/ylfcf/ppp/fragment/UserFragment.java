package com.ylfcf.ppp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.ui.MainFragmentActivity;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnUserFragmentLoginSucListener;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * 我的
 * @author Administrator
 *	充值余额是易联和汇付相加
 *	冻结总额和待收总额只请求易联接口即可
 */
public class UserFragment extends BaseFragment{
	private static final String className = "UserFragment";
	private MainFragmentActivity mainActivity;

	private View rootView;
	private View topLayout;
	private TextView topTitle;
	private LinearLayout topbarLeftLayout;

	private FrameLayout contentLayout;

	private String userId;
	private FragmentManager manager;
	private FragmentTransaction transaction;
	private UserLoginFragment mUserLoginFragment;
	private UserPersonalFragment mUserPersonalFragment;
	private UserCompFragment mUserCompFragment;

	/**
	 * 创建当前Fragment的实例对象
	 * @param position
	 * @return
	 */
	static OnUserFragmentLoginSucListener onLoginSuc;
	public static Fragment newInstance(int position,OnUserFragmentLoginSucListener onLoginSucListener) {
		UserFragment f = new UserFragment();
		onLoginSuc = onLoginSucListener;
		Bundle args = new Bundle();
		args.putInt("num", position);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		YLFLogger.d("UserFragment -- onActivityCreated");
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		YLFLogger.d("UserFragment -- onCreate");
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
							 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mainActivity = (MainFragmentActivity) getActivity();
		if(rootView==null){
			rootView=inflater.inflate(R.layout.user_fragment, null);
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
		topLayout = view.findViewById(R.id.user_fragment_toplayout);
		topbarLeftLayout = (LinearLayout) topLayout.findViewById(R.id.common_topbar_left_layout);
		topbarLeftLayout.setVisibility(View.GONE);
		topTitle = (TextView) topLayout.findViewById(R.id.common_page_title);

		contentLayout = (FrameLayout) view.findViewById(R.id.user_fragment_content_layout);
		initMainLayout(null);
	}

	/**
	 * 登录页面
	 */
	private void showLoginFragment(){
		manager = getChildFragmentManager();
		transaction = manager.beginTransaction();
		removeAllFragments();
		mUserLoginFragment = UserLoginFragment.newInstance(new OnUserFragmentLoginSucListener(){
			@Override
			public void onLoginSuc(UserInfo info) {
				initMainLayout(info);
			}
		});
		transaction.replace(R.id.user_fragment_content_layout, mUserLoginFragment);
		transaction.commit();
	}

	/**
	 * 个人用户的账户页面
	 */
	private void showPersonalFragment(){
		manager = getChildFragmentManager();
		transaction = manager.beginTransaction();
		hidFragments();
		if(mUserPersonalFragment != null){
			transaction.show(mUserPersonalFragment);
			return;
		}
		mUserPersonalFragment = UserPersonalFragment.newInstance();
		transaction.replace(R.id.user_fragment_content_layout, mUserPersonalFragment);
		transaction.commit();
	}

	/**
	 * 企业用户的账户页面
	 */
	private void showCompFragment(UserInfo info){
		manager = getChildFragmentManager();
		transaction = manager.beginTransaction();
		hidFragments();
		if(mUserCompFragment != null){
			transaction.show(mUserCompFragment);
			return;
		}
		mUserCompFragment = UserCompFragment.newInstance(info);
		transaction.replace(R.id.user_fragment_content_layout, mUserCompFragment);
		transaction.commit();
	}

	public void removeAllFragments(){
		if(mUserLoginFragment != null){
			transaction.remove(mUserLoginFragment);
			mUserLoginFragment = null;
		}
		if(mUserPersonalFragment != null){
			transaction.remove(mUserPersonalFragment);
			mUserPersonalFragment = null;
		}
		if(mUserCompFragment != null){
			transaction.remove(mUserCompFragment);
			mUserCompFragment = null;
		}
	}

	private void hidFragments(){
		if(mUserLoginFragment != null){
			transaction.hide(mUserLoginFragment);
		}
		if(mUserPersonalFragment != null){
			transaction.hide(mUserPersonalFragment);
		}
		if(mUserCompFragment != null){
			transaction.hide(mUserCompFragment);
		}
	}

	private void initMainLayout(UserInfo info){
		if(mainActivity == null){
			mainActivity = (MainFragmentActivity) getActivity();
		}
		if(mainActivity == null)
			return;
		userId = SettingsManager.getUserId(mainActivity.getApplicationContext());
		if(userId != null && !"".equals(userId)){
			topTitle.setText("我的账户");
			if(SettingsManager.isPersonalUser(mainActivity)){
				//个人账户
				showPersonalFragment();
			}else if(SettingsManager.isCompanyUser(mainActivity)){
				showCompFragment(info);
			}
		}else{
			topTitle.setText("登录");
			showLoginFragment();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		YLFLogger.d("UserFragment -- onStart");
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		YLFLogger.d("UserFragment -- onHiddenChanged");
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		YLFLogger.d("UserFragment -- setUserVisibleHint ---" + isVisibleToUser);
		if(isVisibleToUser){
			initMainLayout(null);
			if(mUserCompFragment != null)
				mUserCompFragment.setUserVisibleHint(true);
			if(mUserPersonalFragment != null)
				mUserPersonalFragment.setUserVisibleHint(true);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		YLFLogger.d("UserFragment ------onDetach()");
	}

	@Override
	public void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);
		YLFLogger.d("UserFragment ------onResume()");
		initMainLayout(null);
	}

	@Override
	public void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);
		YLFLogger.d("UserFragment -- onPause");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		YLFLogger.d("UserFragment -- onDestroy");
	}

}