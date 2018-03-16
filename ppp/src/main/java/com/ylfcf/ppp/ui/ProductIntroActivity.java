package com.ylfcf.ppp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncXSBIscanbuy;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 产品介绍页面  元月盈介绍、新手标介绍等
 * @author Mr.liu
 *
 */
public class ProductIntroActivity extends BaseActivity implements OnClickListener{
	private static final String className = "ProductIntroActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private WebView webview;
	private String fromWhere;//yyy:元月盈   xsb:新手标
	private String loadURL = "";
	private ProductInfo productInfo;
	private AlertDialog.Builder builder = null; // 先得到构造器
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.yyyintro_activity);
		builder = new AlertDialog.Builder(ProductIntroActivity.this,
				R.style.Dialog_Transparent); // 先得到构造器
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		if(bundle != null){
			productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
			fromWhere = bundle.getString("from_where");
		}
		findView();
	}
	
	private void findView(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		if("xsb".equals(fromWhere)){
			topTitleTV.setText("新手标介绍");
			loadURL = URLGenerator.XSB_XMJS_URL.replace("borrowid", productInfo == null?"borrowid":productInfo.getId());
		}else if("yyy".equals(fromWhere)){
			topTitleTV.setText("元月盈介绍");
			loadURL = URLGenerator.YYY_XMJS_URL;
		}else if("wdy".equals(fromWhere)){
			//稳定赢，薪盈计划
			topTitleTV.setText("薪盈计划介绍");
			loadURL = URLGenerator.XYJH_XMJS_URL.replace("borrowid", productInfo == null?"borrowid":productInfo.getId());
		}
		
		webview = (WebView) findViewById(R.id.yyyintro_activity_wv);
		this.webview.getSettings().setSupportZoom(false);  
        this.webview.getSettings().setJavaScriptEnabled(true);  //支持js
        this.webview.getSettings().setDomStorageEnabled(true); 
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//拦截URL 进行activity的跳转
				boolean isLogin = !SettingsManager.getLoginPassword(ProductIntroActivity.this).isEmpty()
						&& !SettingsManager.getUser(ProductIntroActivity.this).isEmpty();
				Intent intent = new Intent();
				if(isLogin){
					if("xsb".equals(fromWhere) && (url.contains("/home/borrow/borrowDetail/id/") ||
						url.contains("/home/borrow/borrowInvest/id/"))){
						//跳转到新手标的投资页面
						checkXSB();
					}else if("yyy".equals(fromWhere)){
						//跳转到元月盈的投资页面
						intent.setClass(ProductIntroActivity.this,
								BidYYYActivity.class);
						intent.putExtra("PRODUCT_INFO", productInfo);
						startActivity(intent);
					}else if("wdy".equals(fromWhere) && url.contains("/home/wdy/wdyinvest")){
						//跳转到稳定盈投资页面
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductIntroActivity.this, BidWDYActivity.class);
						startActivity(intent);
					}
				}else{
					intent.setClass(ProductIntroActivity.this,LoginActivity.class);
					startActivity(intent);
				}
				return true;
			}
		});
		webview.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {	
				if(newProgress == 100 && mLoadingDialog.isShowing()){
					//网页加载完成
					mLoadingDialog.dismiss();
				}else if(newProgress != 100 && !mLoadingDialog.isShowing() && !isFinishing()){
					//网页加载中...
					mLoadingDialog.show();
				}
			}
		});
		webview.loadUrl(loadURL);
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

	private void checkXSB(){
		boolean isLogin = !SettingsManager.getLoginPassword(ProductIntroActivity.this).isEmpty()
				&& !SettingsManager.getUser(ProductIntroActivity.this).isEmpty();
		// isLogin = true;// 测试
		Intent intent = new Intent();
		// 1、检测是否已经登录
		if (isLogin) {
			//判断是否实名绑卡
			isCanbuyXSB(SettingsManager.getUserId(getApplicationContext()), productInfo.getId());
		} else {
			// 未登录，跳转到登录页面
			intent.setClass(ProductIntroActivity.this,LoginActivity.class);
			startActivity(intent);
			finish();
		}
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
	 * 显示弹出框
	 * @param type
	 * @param msg
	 */
	private void showMsgDialog(Context context,final String type,String msg){
		View contentView = LayoutInflater.from(context)
				.inflate(R.layout.borrow_details_msg_dialog, null);
		final Button sureBtn = (Button) contentView
				.findViewById(R.id.borrow_details_msg_dialog_surebtn);
		final TextView msgTV = (TextView) contentView
				.findViewById(R.id.borrow_details_msg_dialog_msg);
		final ImageView delBtn = (ImageView) contentView
				.findViewById(R.id.borrow_details_msg_dialog_delete);
		if("不能购买新手标".equals(type)){
			sureBtn.setVisibility(View.GONE);
		}else{
			sureBtn.setVisibility(View.VISIBLE);
		}
		msgTV.setText(msg);
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if("实名认证".equals(type)){
					intent.setClass(ProductIntroActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					if("新手标".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "新手标投资");
					}else if("vip".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "VIP投资");
					}else{
						bundle.putString("type", "政信贷投资");
					}
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
				}else if("绑卡".equals(type)){
					Bundle bundle = new Bundle();
					if("新手标".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "新手标投资");
					}else if("vip".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "VIP投资");
					}else{
						bundle.putString("type", "政信贷投资");
					}
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					intent.setClass(ProductIntroActivity.this, BindCardActivity.class);
					startActivity(intent);
				}
				dialog.dismiss();
			}
		});
		delBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// 参数都设置完成了，创建并显示出来
		dialog.show();
		// 设置dialog的宽度
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth() * 6 / 7;
		lp.height = display.getHeight()/3;
		dialog.getWindow().setAttributes(lp);
	}
	
	/**
	 * 判断是否可以购买新手标
	 * @param userId
	 * @param borrowId
	 */
	private void isCanbuyXSB(String userId,String borrowId){
		AsyncXSBIscanbuy task = new AsyncXSBIscanbuy(ProductIntroActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//用户可以购买新手标
								Intent intent = new Intent();
								intent.putExtra("PRODUCT_INFO", productInfo);
								intent.setClass(ProductIntroActivity.this, BidXSBActivity.class);
								startActivity(intent);
							}else if(resultCode == 1001){
								//请先进行实名
								showMsgDialog(ProductIntroActivity.this, "实名认证", "请先实名认证！");
							}else if(resultCode == 1002){
								//请先进行绑卡
								boolean isNewUser = SettingsManager.checkIsNewUser(SettingsManager.getUserRegTime(getApplicationContext()));
								if(isNewUser){
									showMsgDialog(ProductIntroActivity.this, "绑卡", "请您先绑卡！");
								}else{
									showMsgDialog(ProductIntroActivity.this, "绑卡", "因我司变更支付渠道，请您重新绑卡！");
								}
							}else{
								showMsgDialog(ProductIntroActivity.this, "不能购买新手标", "此产品限首次购买用户专享！");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
