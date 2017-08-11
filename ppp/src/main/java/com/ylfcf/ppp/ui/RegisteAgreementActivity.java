package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;

/**
 * 注册服务协议
 * 
 * @author Administrator
 * 
 */
public class RegisteAgreementActivity extends BaseActivity implements
		OnClickListener {
	private static final String className = "RegisteAgreementActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.registe_agreement_activity);

		findViews();
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("注册服务协议");
		
		webView = (WebView) findViewById(R.id.registe_agreement_activity_wv);
		this.webView.getSettings().setSupportZoom(false);  
        this.webView.getSettings().setJavaScriptEnabled(true);  //支持js
        this.webView.getSettings().setDomStorageEnabled(true); 
        webView.setWebChromeClient(new WebChromeClient(){
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
        webView.loadUrl(URLGenerator.REGISTE_AGREEMENT_URL);
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
