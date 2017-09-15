package com.ylfcf.ppp.ui;

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
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 元信宝介绍页面
 * @author Administrator
 *
 */
public class YXBIntroActivity extends BaseActivity implements OnClickListener{
	private WebView webview;
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.yxb_intro_activity);
		findViews();
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("元信宝");
		
		webview = (WebView)findViewById(R.id.yxb_intro_activity_webview);
		this.webview.getSettings().setSupportZoom(false);  
        this.webview.getSettings().setJavaScriptEnabled(true);  //支持js
        this.webview.getSettings().setDomStorageEnabled(true); 
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//拦截URL 进行activity的跳转
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
		webview.loadUrl(URLGenerator.YXB_INTRO_URL);
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
