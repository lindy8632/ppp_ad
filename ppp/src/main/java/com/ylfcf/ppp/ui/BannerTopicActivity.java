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
 * ר��ҳ
 * @author Administrator
 *
 */
public class BannerTopicActivity extends BaseActivity implements OnClickListener{
	private static final String LOTTERY_URL = "http://wap.ylfcf.com/home/index/lottery.html";//	��ת�̵Ļҳ��
	private static final int POPUPWINDOW_START_WHAT = 2712;
	private static final int DOWNLOAD_PIC_WHAT = 2713;

	private LinearLayout mainLayout;
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private WebView webview;
	private BannerInfo banner;
	private String topicType = "";//ר������֣����ݺ�̨��Լ���ġ�
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
			//��ֵ�͵Ļ
			topTitleTV.setText("��ֵ��");
			topLayout.setBackgroundColor(getResources().getColor(R.color.topic_chongzhisong_topcolor));
		}else if(TopicType.ZHUCESONG.equals(topicType)){
			//ע����
			topTitleTV.setText("ע����");
		}else if(TopicType.JIAXI.equals(topicType)){
			topTitleTV.setText("��Ϣˬ����");
		}else if(TopicType.TOUZIFANLI.equals(topicType)){
			topTitleTV.setText("Ͷ�ʷ���");
		}else if(TopicType.XINGYUNZHUANPAN.equals(topicType)){
			topTitleTV.setText("����ת��");
		}else if(TopicType.YYY_JX.equals(topicType)){
			topLayout.setBackgroundColor(getResources().getColor(R.color.topic_yyyjiaxi_topcolor));
			topTitleTV.setText("�������� �ӼӼ�Ϣ");
		}else if(TopicType.TUIGUANGYUAN.equals(topicType)){
			topTitleTV.setText("�ƹ�Աר������");
		}else if(TopicType.FRIENDS_CIRCLE.equals(topicType)){
			topTitleTV.setText("��ǿ����Ȧ");
		}else{
			topTitleTV.setText("ר������");
		}
		
		webview = (WebView) findViewById(R.id.banner_topic_activity_webview);
		this.webview.getSettings().setSupportZoom(false);  
        this.webview.getSettings().setJavaScriptEnabled(true);  //֧��js
        this.webview.getSettings().setDomStorageEnabled(true);
		this.webview.addJavascriptInterface(new JavascriptAndroidInterface(this),"android");
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//����URL ����activity����ת
				if(TopicType.CHONGZHISONG.equals(topicType)){
					//��ֵ��ר��
					chongzhisong(url);
				}else if(TopicType.ZHUCESONG.equals(topicType)){
					//ע����
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
					//��ҳ�������
					mLoadingDialog.dismiss();
				}else{
					//��ҳ������...
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
					YLFLogger.d("����ǰ��������������������������"+userid);
					YLFLogger.d("���ܺ󣺡�����������������������"+SimpleCrypto.encrypt(userid));
					YLFLogger.d("6�·ݻ���ӣ���������������������������������������������������������"+
							banner.getLink_url().replace("#app","?app_socket="+userIdCrypto+"#app"));
				}catch (Exception e){
				}
			}else{
				webview.loadUrl(banner.getLink_url());
				YLFLogger.d("6�·ݻ���ӣ���������������������������������������������������������"+
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
	 * ������������ʾ��
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
	 * ����URL
	 * @param url
	 */
	private void loadURL(String url){
		if(url == null){
			return;
		}
		if(url.contains("/home/yyy/yyyDetail")){
			//Ԫ��ӯ��Ϣ��ת  Ԫ��ӯ������ҳ��
			Intent intent = new Intent(BannerTopicActivity.this,BorrowDetailYYYActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/borrow/borrowlist") || url.contains("/home/borrow/borrowList")){
			//��ת�����Ŵ����б�ҳ��
			Intent intent = new Intent(BannerTopicActivity.this,BorrowListZXDActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/vip/borrowlist") || url.contains("/home/vip/borrowList")){
			//��ת��vip���б�ҳ��
			Intent intent = new Intent(BannerTopicActivity.this,BorrowListVIPActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/borrow/borrowDetail") || url.contains("/home/borrow/borrowdetail")){
			//���ֱ�����
			Intent intent = new Intent(BannerTopicActivity.this,BorrowDetailXSBActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/index/register")){
			//ע��ҳ��
			Intent intent = new Intent(BannerTopicActivity.this,RegisteActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/index/login")){
			//��¼ҳ��
			Intent intent = new Intent(BannerTopicActivity.this,LoginActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/index/vipregister") || url.contains("/home/index/vipRegister")){
			//vip�û�ע��ҳ��
			Intent intent = new Intent(BannerTopicActivity.this,RegisterVIPActivity.class);
			startActivity(intent);
		}else if(url.contains("/home/promotion/hdInvite") || url.contains("/home/promotion/hdinvite") ||
				url.contains("/promotion/hdInvite")){
			//��ת���������ҳ�棬�����ж���û�е�¼
			shared();
		}else if(url.contains("/home/index/promoter")){
			//�붮ʲô���Ƽ���
			Intent intentBanner = new Intent(BannerTopicActivity.this,BannerTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setArticle_id(TopicType.TUIGUANGYUAN);
			bannerInfo.setLink_url(URLGenerator.PROMOTER_URL);
			intentBanner.putExtra("BannerInfo", bannerInfo);
			startActivity(intentBanner);
			finish();
		}else if(url.contains("/home/borrow")){
			//��ҳ���Ͷ���б�ҳ��
			SettingsManager.setMainProductListFlag(getApplicationContext(), true);
			mApp.finishAllActivityExceptMain();
		}else if(url.contains("/home/seckill/seckilldetail")){
			//��ת���������ҳ��
			Intent intentMBDetail = new Intent(BannerTopicActivity.this,BorrowDetailXSMBActivity.class);
			startActivity(intentMBDetail);
			finish();
		}else if(url.contains("/home/index/fljh") || url.contains("/home/index/yhfl")){
			//��ȡ�û�����
			Intent intent = new Intent(BannerTopicActivity.this,PrizeRegionTempActivity.class);
			startActivity(intent);
			finish();
		}else if(url.contains("/home/wdy/wdydetail")){
			//нӯ�ƻ�����ҳ��
			Intent intent = new Intent(BannerTopicActivity.this,BorrowDetailWDYActivity.class);
			startActivity(intent);
			finish();
		}else if(url.contains("/home/index/hd")){
			Intent intent = new Intent(BannerTopicActivity.this,ActivitysRegionActivity.class);
			startActivity(intent);
		}else{
			//����������°汾
//			Util.toastLong(BannerTopicActivity.this, "����������°汾");
		}
	}
	
	private void shared(){
		String userId = SettingsManager.getUserId(getApplicationContext());
		if(userId != null && !"".equals(userId)){
			//�ѵ�¼
			if(SettingsManager.isPersonalUser(getApplicationContext())){
				if("�����Ƽ�".equals(banner.getFrom_where()) || "��������".equals(banner.getFrom_where())){
					Intent intent = new Intent();
					setResult(200,intent);
					finish();
				}else{
					checkIsVerify("�����н�");
				}
			}else{
				
			}
		}else{
			//δ��¼
			Intent intent = new Intent(BannerTopicActivity.this,LoginActivity.class);
			intent.putExtra("FLAG", "from_mainfragment_activity_shared");
			startActivity(intent);
		}
	}
	
	/**
	 * ��ֵ��
	 * @param url
	 */
	private  void chongzhisong(String url){
		if(url != null && url.contains("/home/recharge/")){
			String userId = SettingsManager.getUserId(getApplicationContext());
			if(userId == null || "".equals(userId)){
				//δ��½
				Intent intent = new Intent(BannerTopicActivity.this,LoginActivity.class);
				startActivity(intent);
			}else{
				SettingsManager.setMainAccountFlag(getApplicationContext(), true);
				finish();
			}
		}
	}
	
	/**
	 * ע����
	 * @param url
	 */
	private void zhucesong(String url){
		if(url != null && url.contains("/home/borrow")){
			//�����鿴
			SettingsManager.setMainProductListFlag(getApplicationContext(), true);
			finish();
		}else if(url != null && url.contains("/home/index/register")){
			//���ע��
			String userId = SettingsManager.getUserId(getApplicationContext());
			if(userId == null || "".equals(userId)){
				//δ��½
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
	 * ����ת��
	 * @param url
	 */
	private void xingyunzhuanpan(String url){
		//��ת�����
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
		sureBtn.setText("ȥ�μ�");
		contentText.setText("ʹ��������򿪻ҳ��");
		AlertDialog.Builder builder=new AlertDialog.Builder(BannerTopicActivity.this, R.style.Dialog_Transparent);  //�ȵõ�������  
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
        //��������������ˣ���������ʾ����  
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
	 * ��֤�û��Ƿ��Ѿ���֤
	 * @param type ����ֵ��,�����֡�,"�����н�"
	 */
	private void checkIsVerify(final String type){
		RequestApis.requestIsVerify(BannerTopicActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//�û��Ѿ�ʵ��
					Intent intent = new Intent(BannerTopicActivity.this,InvitateActivity.class);
					intent.putExtra("is_verify", true);
					startActivity(intent);
				}else{
					//�û�û��ʵ��
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