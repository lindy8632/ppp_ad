package com.ylfcf.ppp.ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncArticle;
import com.ylfcf.ppp.entity.ArticleInfo;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.ui.ArticleDetailsActivity.ImageLoadThread;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.widget.LoadingDialog;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * banner详情---公告
 * @author jianbing
 *
 */
public class BannerDetailsActivity extends BaseActivity implements OnClickListener{
	private static final int REFRESH_VIEW = 5810;
	
	private BannerInfo mBannerInfo;
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private TextView title,time,content;
	private TextView nodataText;
	private LinearLayout contentLayout;//公告的主布局
	
	private LoadingDialog loadingDialog;
	private  ArticleInfo articleInfoTemp;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if(loadingDialog != null && loadingDialog.isShowing()){
					loadingDialog.dismiss();
				}
				CharSequence text = (CharSequence) msg.obj;
				title.setText(articleInfoTemp.getTitle());
				if(releaseTime != null && !"".equals(releaseTime)){
					time.setText("发布时间："+releaseTime);
				}else{
					time.setText("发布时间："+articleInfoTemp.getAdd_time());
				}
				
				content.setText(text);
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.banner_details_activity);
		
		mBannerInfo = (BannerInfo) getIntent().getSerializableExtra("BannerInfo");
		loadingDialog = new LoadingDialog(BannerDetailsActivity.this, "正在加载...", R.anim.loading);
		findViews();
		if(mBannerInfo != null){
			requestArticle(mBannerInfo.getArticle_id());
		}
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("公告");
		
		title = (TextView)findViewById(R.id.banner_details_actiivty_title);
		time = (TextView)findViewById(R.id.banner_details_actiivty_time);
		content = (TextView)findViewById(R.id.banner_details_actiivty_content);
		content.setMovementMethod(LinkMovementMethod.getInstance());//让textview里面的超链接点击响应
		nodataText = (TextView) findViewById(R.id.banner_details_activity_nodata);
		contentLayout = (LinearLayout) findViewById(R.id.banner_details_activity_content_layout);
	}
	
	private String releaseTime = "";//经过处理过的发布时间
	private void initData(ArticleInfo info){
		if(info == null){
			contentLayout.setVisibility(View.GONE);
			nodataText.setVisibility(View.VISIBLE);
			return;
		}
		try {
			Date date = sdf.parse(info.getAdd_time());
			releaseTime = sdf.format(date);
		} catch (Exception e) {
		}
		new ImageLoadThread().start();
		
		//分割线。
		if(info.getTitle() != null && !"".equals(info.getTitle()) && info.getContent() != null && !"".equals(info.getContent())){
			contentLayout.setVisibility(View.VISIBLE);
			nodataText.setVisibility(View.GONE);
			String addTime = "";
			try {
				Date addDate = sdf.parse(info.getAdd_time());
				addTime = sdf.format(addDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			title.setText(info.getTitle());
			if(addTime != null && !"".equals(addTime)){
				time.setText("发布时间："+addTime);
			}else{
				time.setText("发布时间："+info.getAdd_time());
			}
			
			content.setText(Html.fromHtml(info.getContent()));
		}else{
			contentLayout.setVisibility(View.GONE);
			nodataText.setVisibility(View.VISIBLE);
		}
		
	}

	class ImageLoadThread extends Thread {
		@Override
		public void run() {
			/**
			 * 要实现图片的显示需要使用Html.fromHtml的一个重构方法：public static Spanned fromHtml
			 * (String source, Html.ImageGetterimageGetter, Html.TagHandler
			 * tagHandler)其中Html.ImageGetter是一个接口，我们要实现此接口，在它的getDrawable
			 * (String source)方法中返回图片的Drawable对象才可以。
			 */
			ImageGetter imageGetter = new ImageGetter() {
				@Override
				public Drawable getDrawable(String source) {
					// TODO Auto-generated method stub
					URL url;
					Drawable drawable = null;
					try {
						url = new URL(source);
						int[] screen = SettingsManager.getScreenDispaly(BannerDetailsActivity.this);
						drawable = Drawable.createFromStream(url.openStream(),null);
						if(drawable != null){
							int imageIntrinsicWidth = drawable.getIntrinsicWidth();
							float imageIntrinsicHeight = (float)drawable.getIntrinsicHeight();
							int curImageHeight = (int) (screen[0]*(imageIntrinsicHeight/imageIntrinsicWidth));
							drawable.setBounds(0, 0, screen[0],curImageHeight);//四个参数含义为左上角、右下角坐标确定的一个矩形，图片就在这个矩形范围内画出来
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return drawable;
				}
			};
			CharSequence htmlText = Html.fromHtml(articleInfoTemp.getContent(),imageGetter, null);
			Message msg = handler.obtainMessage(REFRESH_VIEW);
			msg.obj = htmlText;
			handler.sendMessage(msg);

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
	 * 请求文章详情
	 * @param id
	 */
	private void requestArticle(String id){
		if(loadingDialog != null){
			loadingDialog.show();
		}
		AsyncArticle articleTask = new AsyncArticle(BannerDetailsActivity.this, id,new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(loadingDialog != null && loadingDialog.isShowing()){
					loadingDialog.dismiss();
				}
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						articleInfoTemp = baseInfo.getmArticleInfo();
						initData(articleInfoTemp);
					}else{
						contentLayout.setVisibility(View.GONE);
						nodataText.setVisibility(View.VISIBLE);
					}
				}else{
					contentLayout.setVisibility(View.GONE);
					nodataText.setVisibility(View.VISIBLE);
				}
			}
		});
		articleTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
