package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.view.InvitateFriendsPopupwindow;

import java.util.Hashtable;

/**
 * 邀请有奖
 * 用户已经实名过之后才可以邀请好友，未实名的话先让用户进行实名认证。
 * @author jianbing
 * 
 */
public class InvitateActivity extends BaseActivity implements OnClickListener {
	private static final String className = "InvitateActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	private LinearLayout mainLayout;

	private ImageView qrCodeImage;// 二维码
	private Button invitateBtn;
	private Button compUserFriendsBtn;//好友投资列表
	private Button bottomBtn;//底部按钮
	private Button catFriendsBtn;//查看好友详情
	private TextView knowMoreTV;//了解更多
	private ImageView wayLogo1,wayLogo2,wayLogo3;
    private TextView way1Content;
	private LinearLayout btnsLayout,tipsLayout;

	private int page = 0;
	private int pageSize = 20;
	private ExtensionNewPageInfo pageInfo;
	private String promotedURL = null;
	private int QR_WIDTH = 0;
	private int QR_HEIGHT = 0;
	private UserInfo userInfo;
	private boolean isVerify = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.invitate_activity);
		isVerify = getIntent().getBooleanExtra("is_verify", false);
		findViews(isVerify);
		QR_WIDTH = getResources().getDimensionPixelSize(R.dimen.common_measure_170dp);
		QR_HEIGHT = getResources().getDimensionPixelSize(R.dimen.common_measure_170dp);
		requestExtension(SettingsManager.getUserId(getApplicationContext()));
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				requestUserInfo(SettingsManager.getUserId(getApplicationContext()),"",isVerify);
			}
		}, 300L);
	}

	private void findViews(boolean flag) {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("壕友推荐");

		mainLayout = (LinearLayout) findViewById(R.id.invitate_activity_main_layout);
		qrCodeImage = (ImageView) findViewById(R.id.invitate_activity_qrcode);
		qrCodeImage.setOnClickListener(this);
		invitateBtn = (Button) findViewById(R.id.invitate_activity_btn);
		invitateBtn.setOnClickListener(this);
		compUserFriendsBtn = (Button) findViewById(R.id.invitate_activity_yqy_rewardlistbtn);
		compUserFriendsBtn.setOnClickListener(this);
		if(SettingsManager.isCompanyUser(getApplicationContext())){
			compUserFriendsBtn.setVisibility(View.VISIBLE);
		}else{
			compUserFriendsBtn.setVisibility(View.GONE);
		}
		knowMoreTV = (TextView) findViewById(R.id.invitate_activity_know_more);
		knowMoreTV.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
		knowMoreTV.getPaint().setAntiAlias(true);//抗锯齿
		knowMoreTV.setOnClickListener(this);
		bottomBtn = (Button) findViewById(R.id.invitate_activity_btn_bottom);
		bottomBtn.setOnClickListener(this);
		catFriendsBtn = (Button) findViewById(R.id.invitate_activity_btn_bottom_catfriends);
		catFriendsBtn.setOnClickListener(this);
        way1Content = (TextView) findViewById(R.id.invitate_activity_way_one_content);
        SpannableStringBuilder builder = new SpannableStringBuilder(way1Content.getText().toString());
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(getResources().getColor(R.color.common_topbar_bg_color));
        builder.setSpan(blueSpan, 29, 34, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        way1Content.setText(builder);

        wayLogo1 = (ImageView) findViewById(R.id.invitate_activity_way_one_logo);
		wayLogo2 = (ImageView) findViewById(R.id.invitate_activity_way_two_logo);
		wayLogo3 = (ImageView) findViewById(R.id.invitate_activity_way_three_logo);
		if(flag){
			//已实名
			wayLogo1.setVisibility(View.GONE);
			wayLogo2.setVisibility(View.GONE);
			wayLogo3.setVisibility(View.GONE);
			catFriendsBtn.setVisibility(View.VISIBLE);
			invitateBtn.setEnabled(true);
			bottomBtn.setText("查看推荐奖励");
			bottomBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
			bottomBtn.setBackgroundResource(R.drawable.style_rect_fillet_blue);
		}else{
			//未实名
			wayLogo1.setVisibility(View.VISIBLE);
			wayLogo2.setVisibility(View.VISIBLE);
			wayLogo3.setVisibility(View.VISIBLE);
			catFriendsBtn.setVisibility(View.GONE);
			invitateBtn.setEnabled(false);
			bottomBtn.setText("完成实名认证，激活另外两种推荐方式");
			bottomBtn.setTextColor(getResources().getColor(R.color.white));
			bottomBtn.setBackgroundResource(R.drawable.blue_fillet_btn_selector);
		}

		btnsLayout = (LinearLayout) findViewById(R.id.invitate_activity_btns_layout);
		tipsLayout = (LinearLayout) findViewById(R.id.invitate_activity_tips_layout);
		if(SettingsManager.isCompanyUser(getApplicationContext())){
			btnsLayout.setVisibility(View.GONE);
			tipsLayout.setVisibility(View.GONE);
		}else{
			btnsLayout.setVisibility(View.VISIBLE);
			tipsLayout.setVisibility(View.VISIBLE);
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
		case R.id.invitate_activity_btn_bottom:
			if(isVerify){
				//已实名
				Intent intent = new Intent(InvitateActivity.this,
					MyInvitationActivity.class);
				intent.putExtra("ExtensionPageInfo", pageInfo);
				startActivity(intent);
			}else{
				Intent intentVerify = new Intent(InvitateActivity.this,UserVerifyActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("type", "邀请有奖");
				intentVerify.putExtra("bundle", bundle);
				startActivity(intentVerify);
			}
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
		case R.id.invitate_activity_btn_bottom_catfriends:
			Intent intentFriends = new Intent(InvitateActivity.this,MyFriendsActivity.class);
			startActivity(intentFriends);
			break;
		case R.id.invitate_activity_yqy_rewardlistbtn:
			//好友投资
			Intent intentComp = new Intent(InvitateActivity.this,CompUserFriendsActivity.class);
			startActivity(intentComp);
			break;
		default:
			break;
		}
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		UMShareAPI.get(this).release();//友盟分享内存泄露的处理
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
		popwindow.show(mainLayout,promotedURL,"邀请有奖",null);
	}

	/**
	 * 全屏显示二维码
	 */
	private void showBigEWM(){
		if(!isVerify){
			return;
		}
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
				qrCodeImage.setImageResource(R.drawable.invitate_qr_default_logo);
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
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncExtensionNewPageInfo taks = new AsyncExtensionNewPageInfo(
				InvitateActivity.this, userId, String.valueOf(page),
				String.valueOf(pageSize), new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 1 || resultCode == -1) {
								pageInfo = baseInfo.getExtensionNewPageInfo();
							}
						}
					}
				});
		taks.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 加载二维码
	 * @param userId
	 * @param isVerify true表示实名过
	 */
	private void requestUserInfo(String userId,String coMobile,final boolean isVerify) {
		AsyncUserSelectOne task = new AsyncUserSelectOne(InvitateActivity.this,
				userId, "",coMobile, "", new OnGetUserInfoByPhone() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if (resultCode == 0) {
								UserInfo info = baseInfo.getUserInfo();
									//有汇付账户，说明已经实名过
									promotedURL = URLGenerator.PROMOTED_BASE_URL
											+ "?extension_code="
											+ info.getPromoted_code();
									if(isVerify){
										initQRCode(promotedURL);
									}else{
										initQRCode(null);
									}
							}else{
								initQRCode(null);
							}
						}else{
							initQRCode(null);
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
