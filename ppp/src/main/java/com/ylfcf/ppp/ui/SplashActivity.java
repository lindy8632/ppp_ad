package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncBanner;
import com.ylfcf.ppp.common.FileUtil;
import com.ylfcf.ppp.db.DBGesturePwdManager;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.GesturePwdEntity;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseBanner;
import com.ylfcf.ppp.util.ImageLoaderManager;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.YLFLogger;

/**
 * 开机页面
 * 
 * @author Administrator
 * 
 */
public class SplashActivity extends BaseActivity {
	private static final int GOTO_MAINACTIVITY = 10;
	private static final int REQUEST_BANNER = 20;
	private ImageView splashImage;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Animation mAnimation = null;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GOTO_MAINACTIVITY:
				gotoMainActivity();
				break;
			case REQUEST_BANNER:
				requestBanner("正常", "");
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
		setContentView(R.layout.splash_activity);

		mAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_enlarge);
		mAnimation.setFillAfter(true);
		imageLoader = ImageLoaderManager.newInstance();
		options = ImageLoaderManager.configurationOption(
				R.drawable.splash_default, R.drawable.splash_default);
		findViews();
		handler.sendEmptyMessageDelayed(REQUEST_BANNER, 1500L);
	}

	private void findViews() {
		// 方法1 Android获得屏幕的宽和高
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();
		DisplayMetrics metric = getResources().getDisplayMetrics();
		YLFLogger.d("屏幕分辨率：\n" + "宽：" + screenWidth + "\n" + "高："
				+ screenHeight + "\n" + "屏幕密度：" + metric.density);
		splashImage = (ImageView) findViewById(R.id.splash_activity_image);
	}

	/**
	 * 配置ImageLoder
	 */
	@SuppressWarnings("deprecation")
	private void configImageLoader() {
		// 初始化ImageLoader
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.splash_default) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.splash_default) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.splash_default) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(false) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
				.bitmapConfig(Bitmap.Config.ARGB_8888) // default
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(options)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())// 使用md5对URL进行加密命名
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);
	}

	private void gotoMainActivity() {
		Intent intent = new Intent();
		String userId = SettingsManager.getUserId(getApplicationContext());
		GesturePwdEntity entity = DBGesturePwdManager.getInstance(
				getApplicationContext()).getGesturePwdEntity(userId);
		String gesturePwd = null;
		if (entity != null) {
			gesturePwd = entity.getPwd();
		}

		if ("".equals(SettingsManager.getAppFirst(SplashActivity.this))) {
			// APP第一次运行，启动介绍页面
			SettingsManager.setAppFirst(SplashActivity.this, "true");
			intent.setClass(SplashActivity.this, IntroductionActivity.class);
		} else {
			if (gesturePwd != null && !"".equals(gesturePwd)) {
				// 手势密码验证
				intent.setClass(SplashActivity.this,
						GestureVerifyActivity.class);
			} else {
				// 用户主界面
				intent.setClass(SplashActivity.this, MainFragmentActivity.class);
			}
		}
		startActivity(intent);
		if(bannerTask != null){
			bannerTask.cancel(true);
		}
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ImageLoaderManager.clearMemoryCache();
		handler.removeCallbacksAndMessages(null);
	}

	/**
	 * banner
	 * 
	 * @param status
	 */
	AsyncBanner bannerTask = null;
	private void requestBanner(String status, String type) {
		String result = null;
		BaseInfo baseInfo = null;
		try {
			byte[] initJsonB = FileUtil.readByte(SplashActivity.this,
					FileUtil.YLFCF_BANNER_CACHE);
			result = new String(initJsonB);
			// 解析init.json
			if (result != null && !"".equals(result)) {
				baseInfo = JsonParseBanner.parseData(result);
			}
		} catch (Exception exx) {
		}
		
		if (baseInfo != null && baseInfo.getmBannerPageInfo() != null 
				&& baseInfo.getmBannerPageInfo().getBannerList() != null) {
			int size = baseInfo.getmBannerPageInfo().getBannerList().size();
			for (int i = 0; i < size; i++) {
				BannerInfo info = baseInfo.getmBannerPageInfo().getBannerList().get(i);
				if ("手机开机页".equals(info.getType())) {
					ImageLoaderManager.loadingImage(imageLoader, info.getPic_url(),
									splashImage, options, null, null);// 加载后台图片
					// splashImage.startAnimation(mAnimation);//放大动画
				} else {
					splashImage.setBackgroundResource(R.drawable.splash_default);
				}
			}
		}
		handler.sendEmptyMessageDelayed(GOTO_MAINACTIVITY, 2000L);
		bannerTask = new AsyncBanner(SplashActivity.this,
				String.valueOf(0), String.valueOf(20), status, type,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
					}
				});
		bannerTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

}
