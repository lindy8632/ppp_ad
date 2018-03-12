package com.ylfcf.ppp.ui;

import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * Created by Administrator on 2017/12/26.
 */

public class BannerSimpleTopicActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout mainLayout;
    private LinearLayout topLeftBtn;
    private WebView webview;
    private TextView topTitleTV;
    private BannerInfo banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.banner_topic_activity);
        banner = (BannerInfo) getIntent().getSerializableExtra("BannerInfo");
        findViews();
    }

    private void findViews(){
        topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
        topLeftBtn.setOnClickListener(this);
        topTitleTV = (TextView)findViewById(R.id.common_page_title);
        mainLayout = (LinearLayout) findViewById(R.id.banner_topic_activity_mainlayout);
        webview = (WebView) findViewById(R.id.banner_topic_activity_webview);
        topTitleTV.setText("专题详情");

        webview.getSettings().setSupportZoom(false);
        webview.getSettings().setJavaScriptEnabled(true);  //支持js
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//允许js弹窗
        webview.getSettings().setDomStorageEnabled(true);
        webview.setLayerType(View.LAYER_TYPE_SOFTWARE,null);//加速
        //android5.0以上默认不支持mix content,所以此处开启
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //拦截URL 进行activity的跳转
                return true;
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();
            }
        });
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress == 100 && mLoadingDialog.isShowing()){
                    //网页加载完成
                    mLoadingDialog.dismiss();
                    YLFLogger.d("webview加载完成："+String.valueOf(newProgress));
                }else if(newProgress != 100 && !mLoadingDialog.isShowing()){
                    //网页加载中...,Activity没有销毁的时候显示
                    if(!isFinishing()){
                        mLoadingDialog.show();
                        YLFLogger.d("webview加载中："+String.valueOf(newProgress));
                    }
                }
            }
        });
        webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        if(banner != null)
        webview.loadUrl(banner.getLink_url());
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
