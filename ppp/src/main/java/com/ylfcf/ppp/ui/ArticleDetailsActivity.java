package com.ylfcf.ppp.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncArticle;
import com.ylfcf.ppp.entity.ArticleInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 公告、新闻、资讯详情页面
 * @author Administrator
 *
 */
public class ArticleDetailsActivity extends BaseActivity implements OnClickListener{
	private static final String className = "ArticleDetailsActivity";
	private static final int REFRESH_VIEW = 5800;
	private static final int REQUEST_ARTICLE_WHAT = 5801;

	private ArticleInfo mArticleInfo;
	private String titleStr;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private TextView title,time,content;

	private  ArticleInfo articleInfoTemp;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_ARTICLE_WHAT:
				requestArticle(mArticleInfo.getId());
				break;
			case REFRESH_VIEW:
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
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
		setContentView(R.layout.article_detail_activity);
		Bundle bundle = getIntent().getBundleExtra("bundle");
		titleStr = bundle.getString("title");
		mArticleInfo = (ArticleInfo) bundle.getSerializable("ARTICLE_INFO");
		findViews();
		if(mArticleInfo != null){
			handler.sendEmptyMessage(REQUEST_ARTICLE_WHAT);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//友盟统计页面跳转
		UMengStatistics.statisticsResume(this);//友盟统计
	}

	@Override
	protected void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);//友盟统计页面跳转
		UMengStatistics.statisticsPause(this);//友盟统计
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText(titleStr);
		
		title = (TextView)findViewById(R.id.article_details_activity_title);
		time = (TextView)findViewById(R.id.article_details_activity_time);
		content = (TextView)findViewById(R.id.article_details_activity_content);
	}
	
	private String releaseTime = "";//经过处理过的发布时间
	private void initData(ArticleInfo info){
		if(info == null){
			return;
		}
		try {
			Date date = sdf.parse(info.getAdd_time());
			releaseTime = sdf.format(date);
		} catch (Exception e) {
		}
		new ImageLoadThread().start();
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
						int[] screen = SettingsManager.getScreenDispaly(ArticleDetailsActivity.this);
						drawable = Drawable.createFromStream(url.openStream(),null);
						if(drawable != null){
							int imageIntrinsicWidth = drawable.getIntrinsicWidth();
							float imageIntrinsicHeight = (float)drawable.getIntrinsicHeight();
							int curImageWidth = screen[0] - 2*(getResources().getDimensionPixelSize(R.dimen.common_measure_20dp));
							int curImageHeight = (int) (curImageWidth*(imageIntrinsicHeight/imageIntrinsicWidth));
							drawable.setBounds(0, 0, curImageWidth,curImageHeight);//四个参数含义为左上角、右下角坐标确定的一个矩形，图片就在这个矩形范围内画出来
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return drawable;
				}
			};
			CharSequence htmlText = null;
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
				htmlText = Html.fromHtml(articleInfoTemp.getContent(),Html.FROM_HTML_MODE_LEGACY,imageGetter,null);
			}else{
				htmlText = Html.fromHtml(articleInfoTemp.getContent(),imageGetter,null);
			}
			Message msg = handler.obtainMessage(REFRESH_VIEW);
			msg.obj = htmlText;
			handler.sendMessage(msg);

		}
	}
	
	private void requestArticle(String id){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncArticle articleTask = new AsyncArticle(ArticleDetailsActivity.this, id,new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						articleInfoTemp = baseInfo.getmArticleInfo();
						initData(articleInfoTemp);
					}else{
						Util.toastLong(ArticleDetailsActivity.this,baseInfo.getMsg());
					}
				}
			}
		});
		articleTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
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
