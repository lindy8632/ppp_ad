package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.util.Constants.TopicType;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.view.InvitateFriendsPopupwindow;

/**
 * 邀请好友返现专题
 * @author Mr.liu
 *
 */
public class YQHYTempActivity extends BaseActivity implements OnClickListener{
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private Button btn1,btn2,btn3;
	private LinearLayout mainLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.yqhy_temp_activity);
		findViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		boolean isLogin = !SettingsManager.getLoginPassword(
				YQHYTempActivity.this).isEmpty()
				&& !SettingsManager.getUser(YQHYTempActivity.this)
						.isEmpty();
		if(isLogin && SettingsManager.isCompanyUser(getApplicationContext())){
			//企业用户
			btn1.setEnabled(false);
			btn1.setBackgroundResource(R.drawable.yqhy_btn01_unenabled);
		}else{
			btn1.setEnabled(true);
			btn1.setBackgroundResource(R.drawable.yqhy_btn01_enabled);
		}
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("邀请好友奖励");
		mainLayout = (LinearLayout) findViewById(R.id.yqhy_temp_activity_mainlayout);
		
		btn1 = (Button) findViewById(R.id.yqhy_temp_activity_btn1);
		btn1.setOnClickListener(this);
		btn2 = (Button) findViewById(R.id.yqhy_temp_activity_btn2);
		btn2.setOnClickListener(this);
		btn3 = (Button) findViewById(R.id.yqhy_temp_activity_btn3);
		btn3.setOnClickListener(this);
	}

	/**
	 * 退出登录的Dialog
	 */
	private void showLoginDialog(){
		View contentView = LayoutInflater.from(YQHYTempActivity.this).inflate(R.layout.unlogin_dialog_layout, null);
		final Button delBtn = (Button) contentView.findViewById(R.id.unlogin_dialog_layout_delbtn);
		final Button loginBtn = (Button) contentView.findViewById(R.id.unlogin_dialog_layout_loginbtn);
		AlertDialog.Builder builder=new AlertDialog.Builder(YQHYTempActivity.this, R.style.Dialog_Transparent);  //先得到构造器  
        builder.setView(contentView);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        delBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
        loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(YQHYTempActivity.this,LoginActivity.class);
				startActivity(intent);
				dialog.dismiss();
			}
		});
        //参数都设置完成了，创建并显示出来  
        dialog.show();  
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*5/7;
        dialog.getWindow().setAttributes(lp);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.yqhy_temp_activity_btn1:
			//立即邀请好友
			boolean isLogin = !SettingsManager.getLoginPassword(
					YQHYTempActivity.this).isEmpty()
					&& !SettingsManager.getUser(YQHYTempActivity.this)
							.isEmpty();
			if(isLogin){
				//先判断有没有实名认证
				checkIsVerify("邀请好友");
			}else{
				showLoginDialog();
			}
			break;
		case R.id.yqhy_temp_activity_btn2:
			//了解推广员活动详情
			Intent intentActDetail = new Intent(YQHYTempActivity.this,BannerTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setArticle_id(TopicType.TUIGUANGYUAN);
			bannerInfo.setLink_url(URLGenerator.PROMOTER_URL);
			bannerInfo.setFrom_where("");
			intentActDetail.putExtra("BannerInfo", bannerInfo);
			startActivity(intentActDetail);
			break;
		case R.id.yqhy_temp_activity_btn3:
			//分享此活动
			showFriendsSharedWindow();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 弹出分享的提示框
	 */
	private void showFriendsSharedWindow() {
		View popView = LayoutInflater.from(this).inflate(
				R.layout.invitate_friends_popupwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(YQHYTempActivity.this);
		int width = screen[0];
		int height = screen[1] / 5 * 2;
		InvitateFriendsPopupwindow popwindow = new InvitateFriendsPopupwindow(YQHYTempActivity.this,
				popView, width, height);
		popwindow.show(mainLayout,URLGenerator.YQHY_WAP_URL,"四月份推广活动",null,null);
	}
	
	/**
	 * 验证用户是否已经认证
	 * @param type “充值”,“提现”，“邀请有奖”
	 */
	private void checkIsVerify(final String type){
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		RequestApis.requestIsVerify(YQHYTempActivity.this, SettingsManager.getUserId(YQHYTempActivity.this), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(mLoadingDialog != null){
					mLoadingDialog.dismiss();
				}
				if(SettingsManager.isCompanyUser(YQHYTempActivity.this)){
					return;
				}
				Intent yqyjIntent = new Intent(YQHYTempActivity.this,InvitateActivity.class);
				yqyjIntent.putExtra("is_verify", flag);
				startActivity(yqyjIntent);
			}

			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}
}
