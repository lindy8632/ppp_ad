package com.ylfcf.ppp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.ylfcf.ppp.common.FileUtil;

import java.io.File;
/**
 * 图片加载类
 * @author Mr.liu
 *
 */
public class ImageLoaderManager {
	/**
	 * 图片加载方式的版本，有两种加载方式，true为google老的版本，false为新的版本
	 */
	public static boolean OLD_VERSION = false;

	public static ImageLoaderConfiguration config;

	/**
	 * ImageLoader实例
	 * 
	 * @return
	 */
	public static ImageLoader newInstance() {
		return ImageLoader.getInstance();
	}

	/**
	 * 在程序的Application中调用 配置ImageLoader
	 * 
	 * @param context
	 */
//	public static void configurationImageLoader(Context context) {
//		config = new ImageLoaderConfiguration.Builder(context)
//				.threadPriority(Thread.NORM_PRIORITY - 2)// 线程优先级
//				.denyCacheImageMultipleSizesInMemory()//自动缩放
//				.diskCacheFileNameGenerator(new Md5FileNameGenerator())// 将保存的时候的URI名称用MD5
//				//.memoryCacheSize(4 * 1024 *1024) //设置缓存大小
//				//.memoryCache(new LruMemoryCache(4 * 1024 *1024))
//				.diskCacheSize(50 * 1024 * 1024) // 50 Mb
//				.tasksProcessingOrder(QueueProcessingType.LIFO)
//				//.writeDebugLogs()// Remove for release app
//				.diskCache(new UnlimitedDiscCache(new File(FileUtil.getCachePath(context))))// 自定义缓存路径
//				.build();
//		ImageLoader.getInstance().init(config);
//	}
	
	public static void configurationImageLoader(Context context) {
		config = new ImageLoaderConfiguration.Builder(context)
				.threadPoolSize(2)  
				.threadPriority(Thread.NORM_PRIORITY)// 线程优先级
				.denyCacheImageMultipleSizesInMemory()//自动缩放
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())// 将保存的时候的URI名称用MD5
//				.memoryCacheSize(4 * 1024 *1024) //设置缓存大小
//				.memoryCache(new LruMemoryCache(4 * 1024 *1024))
				.diskCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				//.writeDebugLogs()// Remove for release app
				.diskCache(new UnlimitedDiscCache(new File(FileUtil.getCachePath(context))))// 自定义缓存路径
				.build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 清除内存缓存
	 */
	public static void clearMemoryCache() {
		newInstance().clearMemoryCache();
	}

	/**
	 * 清除本地缓存
	 */
	public static void clearDiskCache() {
		newInstance().clearDiskCache();
	}

	/**
	 * option配置
	 * 
	 * @param loadingImage
	 * @param errorIamge
	 */
	public static DisplayImageOptions configurationOption(int loadingImage, int errorIamge) {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(loadingImage)
				.showImageForEmptyUri(errorIamge)
				.showImageOnFail(errorIamge)
				//.delayBeforeLoading(1000)//延迟1000加载
				.cacheInMemory(false) //缓存至内存
				.cacheOnDisk(true) //缓存至SD卡
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		return options;
	}

	/**
	 * option配置
	 * 
	 * @param loadingImage
	 * @param errorIamge
	 * @param cornerRadiusPixels 圆角弧度
	 */
	public static DisplayImageOptions configurationOption(int loadingImage, int errorIamge ,int cornerRadiusPixels) {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(loadingImage)
				.showImageForEmptyUri(errorIamge)
				.showImageOnFail(errorIamge)
				//.delayBeforeLoading(1000)//延迟1000加载
				.displayer(new RoundedBitmapDisplayer(cornerRadiusPixels))
				.cacheInMemory(true) //缓存至内存
				.cacheOnDisk(true) //缓存至SD卡
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		return options;
	}
	
	/**
	 * 图片加载
	 * 
	 * @param imageLoader
	 * @param imageUrl
	 * @param imageView
	 * @param options
	 * @param loadingListener
	 * @param progressListener
	 */
	public static void loadingImage(ImageLoader imageLoader, String imageUrl, ImageView imageView,
			DisplayImageOptions options, SimpleImageLoadingListener loadingListener,
			ImageLoadingProgressListener progressListener) {
		imageLoader.displayImage(imageUrl, imageView, options, loadingListener, progressListener);
	}
}
