package com.ylfcf.ppp.ui;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.widget.LoadingDialog;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 注册服务协议
 * 
 * @author Administrator
 * 
 */
public class RegisteAgreementActivity extends BaseActivity implements
		OnClickListener {
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private WebView webView;
	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.registe_agreement_activity);

		loadingDialog = new LoadingDialog(RegisteAgreementActivity.this, "正在加载...", R.anim.loading);
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
					loadingDialog.dismiss();
				}else{
					//网页加载中...
					loadingDialog.show();
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
}
