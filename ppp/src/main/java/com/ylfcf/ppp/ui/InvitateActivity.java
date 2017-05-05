package com.ylfcf.ppp.ui;

import java.util.Hashtable;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.umeng.socialize.UMShareAPI;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncExtensionNewPageInfo;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ExtensionNewPageInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;
import com.ylfcf.ppp.util.Constants.TopicType;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.view.InvitateFriendsPopupwindow;
import com.ylfcf.ppp.widget.LoadingDialog;

/**
 * 邀请有奖
 * 用户已经实名过之后才可以邀请好友，未实名的话先让用户进行实名认证。
 * @author jianbing
 * 
 */
public class InvitateActivity extends BaseActivity implements OnClickListener {
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private LinearLayout mainLayout, verifyLayout,unVerifyLayout;

	private ImageView qrCodeImage;// 二维码
	private Button invitateBtn;
	private Button verifyBtn;
	private TextView extensionCodeTV;//邀请码
	private LinearLayout friendsCountLayout;
	private TextView friendsCount;
	private TextView knowMoreTV;//了解更多

	private int page = 0;
	private int pageSize = 20;
	private ExtensionNewPageInfo pageInfo;
	private LoadingDialog loadingDialog;
	private String promotedURL = null;
	private int QR_WIDTH = 0;
	private int QR_HEIGHT = 0;

	private UserInfo userInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.invitate_activity);
		boolean isVerify = getIntent().getBooleanExtra("is_verify", false);
		findViews(isVerify);
		QR_WIDTH = getResources().getDimensionPixelSize(R.dimen.common_measure_170dp);
		QR_HEIGHT = getResources().getDimensionPixelSize(R.dimen.common_measure_170dp);
		loadingDialog = new LoadingDialog(InvitateActivity.this, "正在加载...",
				R.anim.loading);
		requestExtension(SettingsManager.getUserId(getApplicationContext()));
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				requestUserInfo(SettingsManager.getUserId(getApplicationContext()),true);
			}
		}, 300L);
	}

	private void findViews(boolean flag) {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("壕友推荐");

		mainLayout = (LinearLayout) findViewById(R.id.invitate_activity_main_layout);
		verifyLayout = (LinearLayout) findViewById(R.id.invitate_activity_verify_layout);
		unVerifyLayout = (LinearLayout) findViewById(R.id.invitate_activity_unverify_layout);
		qrCodeImage = (ImageView) findViewById(R.id.invitate_activity_qrcode);
		qrCodeImage.setOnClickListener(this);
		invitateBtn = (Button) findViewById(R.id.invitate_activity_btn);
		invitateBtn.setOnClickListener(this);
		verifyBtn = (Button) findViewById(R.id.invitate_activity_verify_btn);
		verifyBtn.setOnClickListener(this);
		extensionCodeTV = (TextView) findViewById(R.id.invitate_activity_code);
		friendsCountLayout = (LinearLayout) findViewById(R.id.invitate_activity_friends_count_layout);
		friendsCountLayout.setOnClickListener(this);
		friendsCount = (TextView) findViewById(R.id.invitate_activity_count_tv);
		knowMoreTV = (TextView) findViewById(R.id.invitate_activity_know_more);
		knowMoreTV.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
		knowMoreTV.getPaint().setAntiAlias(true);//抗锯齿
		knowMoreTV.setOnClickListener(this);
		if(flag){
			verifyLayout.setVisibility(View.VISIBLE);
		}else{
			unVerifyLayout.setVisibility(View.VISIBLE);
		}
	}

	private void initQRCode(String url) {
		createQRImage(url);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.invitate_activity_btn:
			showFriendsInvitaWindow();
			break;
		case R.id.invitate_activity_friends_count_layout:
			Intent intent = new Intent(InvitateActivity.this,
					MyInvitationActivity.class);
			intent.putExtra("ExtensionPageInfo", pageInfo);
			startActivity(intent);
			break;
		case R.id.invitate_activity_qrcode:
			showBigEWM();
			break;
		case R.id.invitate_activity_know_more:
			//跳转到推广活动的专题页面
			Intent intentBanner = new Intent(InvitateActivity.this,BannerTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setArticle_id(TopicType.TUIGUANGYUAN);
			bannerInfo.setLink_url(URLGenerator.PROMOTER_URL);
			bannerInfo.setFrom_where("壕友推荐");
			intentBanner.putExtra("BannerInfo", bannerInfo);
			startActivity(intentBanner);
			break;
		case R.id.invitate_activity_verify_btn:
			//立即实名认证
			Intent intentVerify = new Intent(InvitateActivity.this,UserVerifyActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("type", "邀请有奖");
			intentVerify.putExtra("bundle", bundle);
			startActivity(intentVerify);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult( requestCode, resultCode, data);
	}

	/**
	 * 弹出提示框
	 */
	private void showFriendsInvitaWindow() {
		View popView = LayoutInflater.from(this).inflate(
				R.layout.invitate_friends_popupwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(InvitateActivity.this);
		int width = screen[0];
		int height = screen[1] / 5 * 2;
		InvitateFriendsPopupwindow popwindow = new InvitateFriendsPopupwindow(InvitateActivity.this,
				popView, width, height);
		popwindow.show(mainLayout,promotedURL,"邀请有奖");
	}

	/**
	 * 全屏显示二维码
	 */
	private void showBigEWM(){
		View contentView = LayoutInflater.from(InvitateActivity.this).inflate(R.layout.yqyj_ewm_dialog, null);
		ImageView img = (ImageView) contentView.findViewById(R.id.yqyj_ewm_img);
		img.setImageBitmap((Bitmap)qrCodeImage.getTag());
		AlertDialog.Builder builder=new AlertDialog.Builder(InvitateActivity.this, R.style.Dialog_Transparent);  //先得到构造器  
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        //参数都设置完成了，创建并显示出来  
        dialog.show();  
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*6/7;
        lp.height = display.getWidth()*6/7;
        dialog.getWindow().setAttributes(lp);
	}
	
	public void createQRImage(String url) {
		try {
			// 判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1) {
				return;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < QR_HEIGHT; y++) {
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QR_WIDTH + x] = 0xff000000;
					} else {
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			// 显示到一个ImageView上面
			qrCodeImage.setImageBitmap(bitmap);
			qrCodeImage.setClickable(true);
			qrCodeImage.setTag(bitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 邀请好友的信息
	 * @param userId
	 */
	private void requestExtension(String userId) {
		loadingDialog.show();
		AsyncExtensionNewPageInfo taks = new AsyncExtensionNewPageInfo(
				InvitateActivity.this, userId, String.valueOf(page),
				String.valueOf(pageSize), new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						loadingDialog.dismiss();
						int resultCode = SettingsManager
								.getResultCode(baseInfo);
						if (resultCode == 1 || resultCode == -1) {
							pageInfo = baseInfo.getExtensionNewPageInfo();
							friendsCount.setText(pageInfo.getExtension_user_count() + "位");
						}
					}
				});
		taks.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 加载二维码
	 * @param userId
	 * @param flag true表示有易联账户并且已经绑定过卡
	 */
	private void requestUserInfo(String userId,final boolean flag) {
		AsyncUserSelectOne task = new AsyncUserSelectOne(InvitateActivity.this,
				userId, "", "", new OnGetUserInfoByPhone() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(loadingDialog != null && loadingDialog.isShowing()){
							loadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								UserInfo info = baseInfo.getUserInfo();
									//有汇付账户，说明已经实名过
									promotedURL = URLGenerator.PROMOTED_BASE_URL
											+ "?extension_code="
											+ info.getPhone();
									initQRCode(promotedURL);
									extensionCodeTV.setText(info.getPromoted_code());
							}else{
								initQRCode(URLGenerator.PROMOTED_BASE_URL);
							}
						}else{
							initQRCode(URLGenerator.PROMOTED_BASE_URL);
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
