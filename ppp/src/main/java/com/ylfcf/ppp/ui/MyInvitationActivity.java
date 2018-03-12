package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.fragment.MyInvitationYQYFragment;
import com.ylfcf.ppp.fragment.MyInvitationYQZFragment;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 我的邀请
 * @author jianbing
 * 
 */
public class MyInvitationActivity extends BaseActivity implements
		OnClickListener {
	private static final int REQUEST_USERINFO_WHAT = 1203;
	private static final int REQUEST_USERINFO_SUC = 1204;

	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private Button yqyNarBtn,yqzNarBtn;
	private LinearLayout btnsLayout;
	private UserInfo userInfo;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case REQUEST_USERINFO_WHAT:
					requestUserInfo(SettingsManager.getUserId(getApplicationContext()),"");
					break;
				case REQUEST_USERINFO_SUC:
					UserInfo info = (UserInfo)msg.obj;
					if(info.getType().contains("元企盈")){
						btnsLayout.setVisibility(View.VISIBLE);
						onNavBtnOnClick(yqyNarBtn);
					}else{
						onNavBtnOnClick(yqzNarBtn);
					}
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myinvitation_activity);
		fragmentManager = getSupportFragmentManager();
		findViews();
		handler.sendEmptyMessage(REQUEST_USERINFO_WHAT);
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("人脉收益");

		yqyNarBtn = (Button) findViewById(R.id.myinvitation_activity_tab_yqy);
		yqyNarBtn.setOnClickListener(this);
		yqzNarBtn = (Button) findViewById(R.id.myinvitation_activity_tab_yqz);
		yqzNarBtn.setOnClickListener(this);
		btnsLayout = (LinearLayout) findViewById(R.id.myinvitation_activity_narlayout);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.myinvitation_activity_tab_yqy:
		case R.id.myinvitation_activity_tab_yqz:
			onNavBtnOnClick(v);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 100:
			finish();
			break;
		default:
			break;
		}
		switch (resultCode){
		case 200:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	Fragment yqyFragment;
	Fragment yqzFragment;
	public FragmentManager fragmentManager;
	FragmentTransaction trasection;
	private void onNavBtnOnClick(View v){
		trasection = fragmentManager.beginTransaction();
		switch (v.getId()) {
			case R.id.myinvitation_activity_tab_yqy:
				//元企盈
				String flag1 = (String) v.getTag();
				if("0".equals(flag1)){
					yqyNarBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue_left_3dp);
					yqyNarBtn.setTextColor(getResources().getColor(R.color.white));
					yqyNarBtn.setTag("1");
					yqzNarBtn.setBackgroundResource(R.color.transparent);
					yqzNarBtn.setTag("0");
					yqzNarBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
					yqyFragment = fragmentManager.findFragmentByTag("yqytab");
					if(yqyFragment == null){
						yqyFragment = new MyInvitationYQYFragment();
						trasection.add(R.id.myinvitation_activity_mainlayout, yqyFragment,"yqytab");
					}else{
						hidFragments();
						trasection.show(yqyFragment);
					}
					trasection.commit();
				}
				break;
			case R.id.myinvitation_activity_tab_yqz:
				//友钱赚
				String flag2 = (String) v.getTag();
				if("0".equals(flag2)){
					yqyNarBtn.setBackgroundResource(R.color.transparent);
					yqyNarBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
					yqyNarBtn.setTag("0");
					yqzNarBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue_right_3dp);
					yqzNarBtn.setTag("1");
					yqzNarBtn.setTextColor(getResources().getColor(R.color.white));
					yqzFragment = fragmentManager.findFragmentByTag("yqztab");
					if(yqzFragment == null){
						yqzFragment = new MyInvitationYQZFragment();
						trasection.add(R.id.myinvitation_activity_mainlayout, yqzFragment,"yqztab");
					}else{
						hidFragments();
						trasection.show(yqzFragment);
					}
					trasection.commit();
				}
				break;
			default:
				break;
		}
	}

	private void hidFragments(){
		if(yqyFragment != null){
			trasection.hide(yqyFragment);
		}
		if(yqzFragment != null){
			trasection.hide(yqzFragment);
		}
	}

	/**
	 * @param userId
	 * @param phone
	 */
	private void requestUserInfo(final String userId,String phone){
		AsyncUserSelectOne userTask = new AsyncUserSelectOne(MyInvitationActivity.this, userId, phone, "","",
				new Inter.OnGetUserInfoByPhone() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								userInfo = baseInfo.getUserInfo();
								Message msg = handler.obtainMessage(REQUEST_USERINFO_SUC);
								msg.obj = userInfo;
								handler.sendMessage(msg);
							}
						}
					}
				});
		userTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
}
