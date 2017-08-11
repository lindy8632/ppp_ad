package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 元月盈常见问题
 * @author Mr.liu
 *
 */
public class YYYProductCJWTActivity extends BaseActivity implements OnClickListener{
	private static final String className = "YYYProductCJWTActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private WebView webview;
	private String fromWhere;
	private ProductInfo mProductInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.yyy_cjwt_activity);
		fromWhere = getIntent().getStringExtra("from_where");
		mProductInfo = (ProductInfo) getIntent().getSerializableExtra("PRODUCT_INFO");
		findViews();
	}

	private void findViews(){
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		if("yyy".equals(fromWhere)){
			topTitleTV.setText("元月盈常见问题");
		}else if("wdy".equals(fromWhere)){
			topTitleTV.setText("薪盈计划常见问题");
		}else{
			topTitleTV.setText("常见问题");
		}
		
		
		webview = (WebView) findViewById(R.id.yyycjwt_activity_wv);
		this.webview.getSettings().setSupportZoom(false);  
        this.webview.getSettings().setJavaScriptEnabled(true);  //支持js
        this.webview.getSettings().setDomStorageEnabled(true); 
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//拦截URL 进行activity的跳转
				boolean isLogin = !SettingsManager.getLoginPassword(YYYProductCJWTActivity.this).isEmpty()
						&& !SettingsManager.getUser(YYYProductCJWTActivity.this).isEmpty();
				Intent intent = new Intent();
				if(isLogin){
					if("yyy".equals(fromWhere)){
						intent.setClass(YYYProductCJWTActivity.this,BidYYYActivity.class);
						intent.putExtra("PRODUCT_INFO", mProductInfo);
						startActivity(intent);
					}else if("wdy".equals(fromWhere)){
						intent.setClass(YYYProductCJWTActivity.this,BidWDYActivity.class);
						intent.putExtra("PRODUCT_INFO", mProductInfo);
						startActivity(intent);
					}
				}else{
					intent.setClass(YYYProductCJWTActivity.this,LoginActivity.class);
					startActivity(intent);
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
		if("yyy".equals(fromWhere)){
			webview.loadUrl(URLGenerator.YYY_CJWT_URL);
		}else if("wdy".equals(fromWhere)){
			webview.loadUrl(URLGenerator.XYJH_CJWT_URL.replace("borrowid", mProductInfo.getId()));
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
}
