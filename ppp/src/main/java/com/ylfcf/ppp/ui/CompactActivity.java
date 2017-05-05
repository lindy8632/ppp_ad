package com.ylfcf.ppp.ui;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.widget.LoadingDialog;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 用户合同页面 新手标、元政盈、元月盈 、私人尊享产品
 * @author Mr.liu
 *
 */
public class CompactActivity extends BaseActivity implements OnClickListener{
	private InvestRecordInfo investInfo;
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private LoadingDialog loadingDialog;
	
	private WebView webview;
	private String contentURL;
	private String fromWhere;
	private ProductInfo mProductInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.compact_activity);
		loadingDialog = new LoadingDialog(CompactActivity.this,"正在加载..." , R.anim.loading);
		investInfo = (InvestRecordInfo) getIntent().getSerializableExtra("invest_record") ;
		fromWhere = getIntent().getStringExtra("from_where");
		mProductInfo = (ProductInfo) getIntent().getSerializableExtra("mProductInfo");
		findViews();
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		if("vip".equals(fromWhere)){
			topTitleTV.setText("产品协议");
		}else{
			topTitleTV.setText("借款协议");
		}
		
		webview = (WebView) findViewById(R.id.yyy_compact_activity_wv);
		WebSettings webSettings = webview.getSettings();  
		webSettings.setJavaScriptEnabled(true);  
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);  
		webSettings.setUseWideViewPort(true);//关键点  
		
		webview.setWebChromeClient(new WebChromeClient(){
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
		
		if(investInfo == null){
			if("vip".equals(fromWhere)){
				if(mProductInfo != null){
					contentURL = URLGenerator.VIP_BLANK_COMPACT.replace("borrowid", mProductInfo.getId());
				}
			}else if("yyy".equals(fromWhere)){
				contentURL = URLGenerator.YYY_COMPACT.replace("userid", "0").
						replace("recordid", "0");
			}else if("srzx".equals(fromWhere)){
				if(mProductInfo != null){
					contentURL = URLGenerator.SRZX_BLANK_COMPACT.
							replace("borrowid", mProductInfo.getId());
				}
			}else if("yzy".equals(fromWhere) || "xsb".equals(fromWhere)){
				if(mProductInfo != null){
					contentURL = URLGenerator.ZXD_XSB_BLANK_COMPACT.replace("borrowid", mProductInfo.getId());
				}
			}else if("xsmb".equals(fromWhere)){
				if(mProductInfo != null){
					contentURL = URLGenerator.XSMB_BLANK_COMPACT.replace("borrowid", mProductInfo.getId());
				}
			}else if("wdy".equals(fromWhere)){
				if(mProductInfo != null){
					contentURL = URLGenerator.WDY_BLANK_COMPACT.replace("borrowid", mProductInfo.getId());
				}
			}
		}else{
			if("vip".equals(fromWhere)){
				contentURL = URLGenerator.VIP_COMPACT.replace("userid", SettingsManager.getUserId(CompactActivity.this)).
						replace("recordid", investInfo.getId());
			}else if("yyy".equals(fromWhere)){
				contentURL = URLGenerator.YYY_COMPACT.replace("userid", SettingsManager.getUserId(CompactActivity.this)).
						replace("recordid", investInfo.getId());
			}else if("srzx".equals(fromWhere)){
				contentURL = URLGenerator.SRZX_COMPACT.replace("userid", SettingsManager.getUserId(CompactActivity.this)).
						replace("recordid", investInfo.getBorrow_id());
			}else if("yzy".equals(fromWhere)){
				contentURL = URLGenerator.ZXD_XSB_COMPACT.replace("recordid", investInfo.getId())
						.replace("userid", SettingsManager.getUserId(CompactActivity.this));
			}else if("xsmb".equals(fromWhere)){
				contentURL = URLGenerator.XSMB_COMPACT.replace("recordid", investInfo.getId())
						.replace("userid", SettingsManager.getUserId(CompactActivity.this));
			}else if("wdy".equals(fromWhere)){
				contentURL = URLGenerator.WDY_COMPACT.replace("recordid", investInfo.getId())
						.replace("userid", SettingsManager.getUserId(CompactActivity.this));
			}
		}
		webview.loadUrl(contentURL);
		
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
