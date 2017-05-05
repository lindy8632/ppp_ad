package com.ylfcf.ppp.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.util.Constants.TopicType;

public class WebViewCookieTestActivity extends BaseActivity implements OnClickListener{
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private WebView mWebView;
	String url = "http://wap.test.ylfcf.com/home/index/qd";  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webview_cookie_activity);
		findViews();
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("Cookie测试");
		
		CookieSyncManager.createInstance(this);  
		CookieManager cookieManager = CookieManager.getInstance();  
		cookieManager.setAcceptCookie(true);  
		cookieManager.setCookie(url, "UID=EBFDA984906B62C444931EA0");  
		cookieManager.setCookie(url, "SID=EBFDA984906BEE91F90362C444931EA0");  
		cookieManager.setCookie(url, "PSTM=14572770");  
		cookieManager.setCookie(url, "TTDSS=Hl3NVU0N3ltZm9OWHhubHVQZW1BRThLdGhLaFc5TnVtQWd1S2g1REcwNVhTS3RXQVFBQ");  
		if (Build.VERSION.SDK_INT < 21) {  
		    CookieSyncManager.getInstance().sync();  
		} else {  
		    CookieManager.getInstance().flush();  
		}  
		mWebView = (WebView) findViewById(R.id.webview1);
		this.mWebView.getSettings().setSupportZoom(false);  
        this.mWebView.getSettings().setJavaScriptEnabled(true);  //支持js
        this.mWebView.getSettings().setDomStorageEnabled(true); 
        mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//拦截URL 进行activity的跳转
				return false;
			}
			
		});
        mWebView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {	
			}
		});
		mWebView.loadUrl(url);
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
}
