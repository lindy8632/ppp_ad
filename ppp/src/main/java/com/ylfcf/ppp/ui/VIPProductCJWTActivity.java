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
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.widget.LoadingDialog;
/**
 * VIP产品常见问题
 * @author Mr.liu
 *
 */
public class VIPProductCJWTActivity extends BaseActivity implements OnClickListener{
	private WebView wv;
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private LoadingDialog loadingDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.vip_product_cjwt_activity);
		loadingDialog = new LoadingDialog(VIPProductCJWTActivity.this, "正在加载...", R.anim.loading);
		findViews();
	}

	private void findViews(){
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("常见问题");
		
		wv = (WebView) findViewById(R.id.vip_product_cjwt_activity_wv);
		this.wv.getSettings().setSupportZoom(false);  
        this.wv.getSettings().setJavaScriptEnabled(true);  //支持js
        this.wv.getSettings().setDomStorageEnabled(true); 
        wv.setWebChromeClient(new WebChromeClient(){
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
		wv.loadUrl(URLGenerator.VIP_CJWT_URL);
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
