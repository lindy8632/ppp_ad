package com.ylfcf.ppp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.ShareInfo;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.util.Constants.TopicType;
import com.ylfcf.ppp.util.ImageLoaderManager;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.SimpleCrypto;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.view.InvitateFriendsPopupwindow;

import java.net.URLEncoder;

/**
 * 专题页
 * @author Administrator
 *
 */
public class BannerTopicActivity extends BaseActivity implements OnClickListener{
	private static final String LOTTERY_URL = "http://wap.ylfcf.com/home/index/lottery.html";//	大转盘的活动页面
	private static final int POPUPWINDOW_START_WHAT = 2712;
	private static final int DOWNLOAD_PIC_WHAT = 2713;

	private LinearLayout mainLayout;
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private WebView webview;
	private BannerInfo banner;
	private String topicType = "";//专题的名字，根据后台来约定的。
	private RelativeLayout topLayout;
	public Bitmap sharePicBitmap = null;
	private String userid;
	private boolean isFirstLoad = true;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case POPUPWINDOW_START_WHAT:
					ShareInfo info = (ShareInfo)msg.obj;
					showFriendsSharedWindow(info.getTitle(),info.getContent(),info.getActiveURL(),info.getSharePicURL());
					break;
				case DOWNLOAD_PIC_WHAT:

					break;
				default:
					break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.banner_topic_activity);
		banner = (BannerInfo) getIntent().getSerializableExtra("BannerInfo");
		if(banner != null){
			topicType = banner.getArticle_id();
		}
		findViews();
		userid = SettingsManager.getUserId(BannerTopicActivity.this);
		if(userid == null || "".equals(userid)){
			loadURL();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		userid = SettingsManager.getUserId(BannerTopicActivity.this);
		if(userid != null && !"".equals(userid) && isFirstLoad){
			loadURL();
		}
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topLayout = (RelativeLayout) findViewById(R.id.banner_topic_activity_toplayout);
		mainLayout = (LinearLayout) findViewById(R.id.banner_topic_activity_mainlayout);
		if(TopicType.CHONGZHISONG.equals(topicType)){
			//充值送的活动
			topTitleTV.setText("充值送");
			topLayout.setBackgroundColor(getResources().getColor(R.color.topic_chongzhisong_topcolor));
		}else if(TopicType.ZHUCESONG.equals(topicType)){
			//注册送
			topTitleTV.setText("注册送");
		}else if(TopicType.JIAXI.equals(topicType)){
			topTitleTV.setText("加息爽翻天");
		}else if(TopicType.TOUZIFANLI.equals(topicType)){
			topTitleTV.setText("投资返利");
		}else if(TopicType.XINGYUNZHUANPAN.equals(topicType)){
			topTitleTV.setText("幸运转盘");
		}else if(TopicType.YYY_JX.equals(topicType)){
			topLayout.setBackgroundColor(getResources().getColor(R.color.topic_yyyjiaxi_topcolor));
			topTitleTV.setText("懒人理财 加加加息");
		}else if(TopicType.TUIGUANGYUAN.equals(topicType)){
			topTitleTV.setText("推广员专题详情");
		}else if(TopicType.FRIENDS_CIRCLE.equals(topicType)){
			topTitleTV.setText("最强朋友圈");
		}else{
			topTitleTV.setText("专题详情");
		}
		
		webview = (WebView) findViewById(R.id.banner_topic_activity_webview);
		this.webview.getSettings().setSupportZoom(false);  
        this.webview.getSettings().setJavaScriptEnabled(true);  //支持js
        this.webview.getSettings().setDomStorageEnabled(true);
		this.webview.addJavascriptInterface(new JavascriptAndroidInterface(this),"android");
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//拦截URL 进行activity的跳转
				if(TopicType.CHONGZHISONG.equals(topicType)){
					//充值送专题
					chongzhisong(url);
				}else if(TopicType.ZHUCESONG.equals(topicType)){
					//注册送
					zhucesong(url);
				}else if(TopicType.XINGYUNZHUANPAN.equals(topicType)){
					xingyunzhuanpan(url);
				}else{
					loadURL(url);
				}
				return true;
			}
		});
		webview.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if(newProgress == 100){
					//网页加载完成
					mLoadingDialog.dismiss();
				}else{
					//网页加载中...
					mLoadingDialog.show();
				}
			}
		});
	}

	class DownloadIMGThread extends Thread{
		ShareInfo info;
		DownloadIMGThread(ShareInfo mShareInfo){
			this.info = mShareInfo;
		}
		@Override
		public void run() {
			super.run();
			sharePicBitmap = ImageLoaderManager.newInstance().loadImageSync(info.getSharePicURL());
		}
	}

	private void loadURL(){
		if(banner != null){
			String userIdCrypto = "";
			if(userid != null && !"".equals(userid)){
				try{
					userIdCrypto = URLEncoder.encode(SimpleCrypto.encrypt(userid),"utf-8");
					webview.loadUrl(banner.getLink_url().replace("#app","?app_socket="+userIdCrypto+"#app"));
					isFirstLoad = false;
					YLFLogger.d("加密前：————————————"+userid);
					YLFLogger.d("加密后：————————————"+SimpleCrypto.encrypt(userid));
					YLFLogger.d("6月份活动链接：————————————————————————————"+
							banner.getLink_url().replace("#app","?app_socket="+userIdCrypto+"#app"));
				}catch (Exception e){
				}
			}else{
				webview.loadUrl(banner.getLink_url());
				YLFLogger.d("6月份活动链接：————————————————————————————"+
						banner.getLink_url());
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		CookieSyncManager.createInstance(BannerTopicActivity.this);
		CookieManager.getInstance().removeAllCookie();
		handler.removeCallbacksAndMessages(null);
	}

	public class JavascriptAndroidInterface{
		Context mContext;
		public JavascriptAndroidInterface(Context context){
			mContext = context;
		}
		@JavascriptInterface
		public void share(final String title,final String content,
						  final String activeURL,final String picURL){
			ShareInfo shareInfo = new ShareInfo();
			shareInfo.setSharePicURL(picURL);
			shareInfo.setContent(content);
			shareInfo.setTitle(title);
			shareInfo.setActiveURL(activeURL);
			new DownloadIMGThread(shareInfo).start();
			Message msg = handler.obtainMessage(POPUPWINDOW_START_WHAT);
			msg.obj = shareInfo;
			handler.sendMessageDelayed(msg,300L);
		}
	}

	/**
	 * 弹出分享的提示框
	 */
	private void showFriendsSharedWindow(String title,String content,String activeURL,String picURL) {
		if(mLoadingDialog != null && mLoadingDialog.isShowing()){
			mLoadingDialog.dismiss();
		}
		View popView = LayoutInflater.from(this).inflate(R.layout.invitate_friends_popupwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(BannerTopicActivity.this);
		int width = screen[0];
		int height = screen[1] / 5 * 2;
		InvitateFriendsPopupwindow popwindow = new InvitateFriendsPopupwindow(BannerTopicActivity.this,
				popView, width, height);
		ShareInfo info = new ShareInfo();
		info.setTitle(title);
		info.setContent(content);
		info.setActiveURL(activeURL);
		info.setSharePicURL(picURL);
		popwindow.show(mainLayout, banner.getLink_url(),"",info,sharePicBitmap);
	}

	/**
	 * 拦截URL
	 * @param url
	 */
	private void loadURL(String url){
		if(url == null){
			return;
		}
		if(url.contains("/home/yyy/yyyDetail")){
			//元月盈加息跳转  元月盈的详情页面
			Intent intent = new Intent(BannerTopicActivity.this,BorrowDetailYYYActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/borrow/borrowlist") || url.contains("/home/borrow/borrowList")){
			//跳转到政信贷的列表页面
			Intent intent = new Intent(BannerTopicActivity.this,BorrowListZXDActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/vip/borrowlist") || url.contains("/home/vip/borrowList")){
			//跳转到vip的列表页面
			Intent intent = new Intent(BannerTopicActivity.this,BorrowListVIPActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/borrow/borrowDetail") || url.contains("/home/borrow/borrowdetail")){
			//新手标详情
			Intent intent = new Intent(BannerTopicActivity.this,BorrowDetailXSBActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/index/register")){
			//注册页面
			Intent intent = new Intent(BannerTopicActivity.this,RegisteActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/index/login")){
			//登录页面
			Intent intent = new Intent(BannerTopicActivity.this,LoginActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/index/vipregister") || url.contains("/home/index/vipRegister")){
			//vip用户注册页面
			Intent intent = new Intent(BannerTopicActivity.this,RegisterVIPActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/promotion/hdInvite") || url.contains("/home/promotion/hdinvite") ||
				url.contains("/promotion/hdInvite")){
			//跳转到邀请好友页面，首先判断有没有登录
			shared();
		}else if(url.contains("/home/index/promoter")){
			//秒懂什么是推荐人
			Intent intentBanner = new Intent(BannerTopicActivity.this,BannerTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setArticle_id(TopicType.TUIGUANGYUAN);
			bannerInfo.setLink_url(URLGenerator.PROMOTER_URL);
			intentBanner.putExtra("BannerInfo", bannerInfo);
			startActivity(intentBanner);
			finish();
		}else if(url.contains("/home/borrow")){
			//主页面的投资列表页面
			SettingsManager.setMainProductListFlag(getApplicationContext(), true);
			mApp.finishAllActivityExceptMain();
		}else if(url.contains("/home/seckill/seckilldetail")){
			//跳转到秒标详情页面
			Intent intentMBDetail = new Intent(BannerTopicActivity.this,BorrowDetailXSMBActivity.class);
			startActivity(intentMBDetail);
			finish();
		}else if(url.contains("/home/index/fljh") || url.contains("/home/index/yhfl")){
			//领取用户福利
			Intent intent = new Intent(BannerTopicActivity.this,PrizeRegionTempActivity.class);
			startActivity(intent);
			finish();
		}else if(url.contains("/home/wdy/wdydetail")){
			//薪盈计划详情页面
			Intent intent = new Intent(BannerTopicActivity.this,BorrowDetailWDYActivity.class);
			startActivity(intent);
			finish();
		}else if(url.contains("/home/index/hd")){
			Intent intent = new Intent(BannerTopicActivity.this,ActivitysRegionActivity.class);
			startActivity(intent);
		}else{
			//请更新至最新版本
//			Util.toastLong(BannerTopicActivity.this, "请更新至最新版本");
		}
	}
	
	private void shared(){
		String userId = SettingsManager.getUserId(getApplicationContext());
		if(userId != null && !"".equals(userId)){
			//已登录
			if(SettingsManager.isPersonalUser(getApplicationContext())){
				if("壕友推荐".equals(banner.getFrom_where()) || "人脉收益".equals(banner.getFrom_where())){
					Intent intent = new Intent();
					setResult(200,intent);
					finish();
				}else{
					checkIsVerify("邀请有奖");
				}
			}else{
				
			}
		}else{
			//未登录
			Intent intent = new Intent(BannerTopicActivity.this,LoginActivity.class);
			intent.putExtra("FLAG", "from_mainfragment_activity_shared");
			startActivity(intent);
		}
	}
	
	/**
	 * 充值送
	 * @param url
	 */
	private  void chongzhisong(String url){
		if(url != null && url.contains("/home/recharge/")){
			String userId = SettingsManager.getUserId(getApplicationContext());
			if(userId == null || "".equals(userId)){
				//未登陆
				Intent intent = new Intent(BannerTopicActivity.this,LoginActivity.class);
				startActivity(intent);
			}else{
				SettingsManager.setMainAccountFlag(getApplicationContext(), true);
				finish();
			}
		}
	}
	
	/**
	 * 注册送
	 * @param url
	 */
	private void zhucesong(String url){
		if(url != null && url.contains("/home/borrow")){
			//立即查看
			SettingsManager.setMainProductListFlag(getApplicationContext(), true);
			finish();
		}else if(url != null && url.contains("/home/index/register")){
			//免费注册
			String userId = SettingsManager.getUserId(getApplicationContext());
			if(userId == null || "".equals(userId)){
				//未登陆
				Intent intent = new Intent(BannerTopicActivity.this,RegisteActivity.class);
				startActivity(intent);
				finish();
			}else{
				SettingsManager.setMainAccountFlag(getApplicationContext(), true);
				finish();
			}
		}
	}
	
	/**
	 * 幸运转盘
	 * @param url
	 */
	private void xingyunzhuanpan(String url){
		//跳转浏览器
		if(url != null && url.contains("/home/index/login")){
			showPromptDialog();
		}else{
			intentToLotteryBrowser();
		}
	}
	
	private void intentToLotteryBrowser(){
		Uri uri = Uri.parse(LOTTERY_URL);  
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
        startActivity(intent);  
	}
	
	private void showPromptDialog(){
		View contentView = LayoutInflater.from(BannerTopicActivity.this).inflate(R.layout.banner_prompt_dialog, null);
		final Button sureBtn = (Button) contentView.findViewById(R.id.banner_prompt_dialog_sure_btn);
		final Button cancelBtn = (Button) contentView.findViewById(R.id.banner_prompt_dialog_cancel_btn);
		final TextView contentText = (TextView) contentView.findViewById(R.id.banner_prompt_dialog_content_text);
		sureBtn.setText("去参加");
		contentText.setText("使用浏览器打开活动页面");
		AlertDialog.Builder builder=new AlertDialog.Builder(BannerTopicActivity.this, R.style.Dialog_Transparent);  //先得到构造器  
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				intentToLotteryBrowser();
			}
		});
        cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
        //参数都设置完成了，创建并显示出来  
        dialog.show();  
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*6/7;
        dialog.getWindow().setAttributes(lp);
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
	
	/**
	 * 验证用户是否已经认证
	 * @param type “充值”,“提现”,"邀请有奖"
	 */
	private void checkIsVerify(final String type){
		RequestApis.requestIsVerify(BannerTopicActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//用户已经实名
					Intent intent = new Intent(BannerTopicActivity.this,InvitateActivity.class);
					intent.putExtra("is_verify", true);
					startActivity(intent);
				}else{
					//用户没有实名
					Intent intent = new Intent(BannerTopicActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
				}
			}

			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}
}
