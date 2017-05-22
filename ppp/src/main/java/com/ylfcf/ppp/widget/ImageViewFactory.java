package com.ylfcf.ppp.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.util.ImageLoaderManager;

/**
 * ImageView创建工厂
 */
public class ImageViewFactory {
	static DisplayImageOptions options = ImageLoaderManager.configurationOption(R.drawable.icon_empty,
			R.drawable.icon_empty);
	/**
	 * 获取ImageView视图的同时加载显示url
	 * banner的imageview
	 * @return
	 */
	public static ImageView getImageView(Context context, String url) {
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(
				R.layout.view_banner, null);
		ImageLoader.getInstance().displayImage(url, imageView);
		ImageLoaderManager.loadingImage(ImageLoaderManager.newInstance(), url, imageView, options, null, null);//加载后台图片
		return imageView;
	}
}
