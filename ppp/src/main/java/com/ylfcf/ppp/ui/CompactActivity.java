package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.URLGenerator;
/**
 * 用户合同页面 新手标、元政盈、元月盈 、私人尊享产品
 * @author Mr.liu
 *
 */
public class CompactActivity extends BaseActivity implements OnClickListener{
	private static final String className = "CompactActivity";
	private InvestRecordInfo investInfo;
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private WebView webview;
	private String contentURL;
	private String fromWhere;
	private ProductInfo mProductInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.compact_activity);
		investInfo = (InvestRecordInfo) getIntent().getSerializableExtra("invest_record") ;
		fromWhere = getIntent().getStringExtra("from_where");
		mProductInfo = (ProductInfo) getIntent().getSerializableExtra("mProductInfo");
		findViews();
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

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		if("wdy".equals(fromWhere)){
			topTitleTV.setText("薪盈计划服务协议书");
		}else{
			topTitleTV.setText("产品协议");
		}
		
		webview = (WebView) findViewById(R.id.yyy_compact_activity_wv);
		WebSettings webSettings = webview.getSettings();  
		webSettings.setJavaScriptEnabled(true);  
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);  
		webSettings.setUseWideViewPort(true);//关键点  
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
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
			}else if("yjy".equals(fromWhere)){
				if(mProductInfo != null){
					contentURL = URLGenerator.YJY_BLANK_COMPACT.replace("borrowid", mProductInfo.getId());
				}
			}
		}else{
			if("vip".equals(fromWhere)){
				contentURL = URLGenerator.VIP_COMPACT.replace("userid", SettingsManager.getUserId(CompactActivity.this)).
						replace("recordid", investInfo.getId());
			}else if("yyy".equals(fromWhere)){
				if(investInfo.getFirst_borrow_time().compareTo(SettingsManager.eleContract_startTime) < 0
						|| SettingsManager.isCompanyUser(getApplicationContext())){
					//电子签章上线之前的合同
					contentURL = URLGenerator.YYY_COMPACT.replace("userid", SettingsManager.getUserId(CompactActivity.this)).
							replace("recordid", investInfo.getId());
				}else{
					//电子签章上线之后的合同
					contentURL = URLGenerator.YYY_PDF_COMPACT.replace("userid", SettingsManager.getUserId(CompactActivity.this)).
							replace("recordid", investInfo.getId());
				}
			}else if("srzx".equals(fromWhere)){
				if(investInfo.getInterest_start_time().compareTo(SettingsManager.eleContract_startTime) < 0
						|| SettingsManager.isCompanyUser(getApplicationContext())){
					//电子签章上线之前的合同
					contentURL = URLGenerator.SRZX_COMPACT.replace("userid", SettingsManager.getUserId(CompactActivity.this)).
							replace("recordid", investInfo.getBorrow_id());
				}else{
					//电子签章上线之后的合同
					contentURL = URLGenerator.SRZX_PDF_COMPACT.replace("userid", SettingsManager.getUserId(CompactActivity.this)).
							replace("recordid", investInfo.getBorrow_id());
				}

			}else if("yzy".equals(fromWhere)){
				if(investInfo.getStart_time().compareTo(SettingsManager.eleContract_startTime) < 0
						|| SettingsManager.isCompanyUser(getApplicationContext())){
					//电子签章上线之前的合同
					contentURL = URLGenerator.ZXD_XSB_COMPACT.replace("recordid", investInfo.getId())
							.replace("userid", SettingsManager.getUserId(CompactActivity.this));
				}else{
					//电子签章上线之后的合同
					contentURL = URLGenerator.ZXD_XSB_PDF_COMPACT.replace("recordid", investInfo.getId())
							.replace("userid", SettingsManager.getUserId(CompactActivity.this));
				}
			}else if("xsmb".equals(fromWhere)){
				contentURL = URLGenerator.XSMB_COMPACT.replace("recordid", investInfo.getId())
						.replace("userid", SettingsManager.getUserId(CompactActivity.this));
			}else if("wdy".equals(fromWhere)){
				if(investInfo.getAdd_time().compareTo(SettingsManager.eleContract_startTime) < 0
						|| SettingsManager.isCompanyUser(getApplicationContext())){
					//电子签章上线之前的合同
					contentURL = URLGenerator.WDY_COMPACT.replace("recordid", investInfo.getId())
							.replace("userid", SettingsManager.getUserId(CompactActivity.this));
				}else{
					//电子签章上线之后的合同
					contentURL = URLGenerator.WDY_PDF_COMPACT.replace("recordid", investInfo.getId())
							.replace("userid", SettingsManager.getUserId(CompactActivity.this));
				}
			}else if("yjy".equals(fromWhere)){
				if(investInfo.getInterest_start_time().compareTo(SettingsManager.eleContract_startTime) < 0
						|| SettingsManager.isCompanyUser(getApplicationContext())){
					//电子签章上线之前的合同
					contentURL = URLGenerator.YJY_COMPACT.replace("recordid", investInfo.getBorrow_id())
							.replace("userid", SettingsManager.getUserId(CompactActivity.this));
				}else{
					//电子签章上线之后的合同
					contentURL = URLGenerator.YJY_PDF_COMPACT.replace("recordid", investInfo.getBorrow_id())
							.replace("userid", SettingsManager.getUserId(CompactActivity.this));
				}
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
