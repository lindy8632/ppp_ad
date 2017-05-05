package com.ylfcf.ppp.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.ProjectCailiaoInfo;
import com.ylfcf.ppp.util.ImageLoaderManager;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.Util;

/**
 * 相关资料gridview的适配器
 * @author Administrator
 *
 */
public class ProductDataAdapter extends BaseAdapter{
	private static final int RESOURCE_ID = R.layout.product_data_girdview_item;
	private List<ProjectCailiaoInfo> imageURLList = new ArrayList<ProjectCailiaoInfo>();
	private Context context;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private AnimateFirstDisplayListener animateFirstListener = new AnimateFirstDisplayListener();  
	
	public ProductDataAdapter(Context context,LayoutInflater layoutInflater){
		super();
		this.context = context;
		this.layoutInflater = layoutInflater;
		this.imageLoader = ImageLoaderManager.newInstance();
		options = ImageLoaderManager.configurationOption(R.drawable.default_vertical_logo,
				R.drawable.default_vertical_logo);
	}
	
	/**
	 * 对外方法，动态改变listview的item并进行刷新
	 * @param list
	 */
	public void setItems(List<ProjectCailiaoInfo> list){
		imageURLList.clear();
		if(list !=  null){
			imageURLList.addAll(list);
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return imageURLList.size();
	}

	@Override
	public Object getItem(int position) {
		return imageURLList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ProjectCailiaoInfo cailiaoInfo = imageURLList.get(position);
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.product_data_gridview_item_imageview);
			holder.title = (TextView) convertView.findViewById(R.id.product_data_gridview_title);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		int width = Util.getGridViewItemWidth(context);
		int height = (int) (width*1.41);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		holder.imageView.setLayoutParams(params);
		holder.title.setText(cailiaoInfo.getTitle());
		String imageURL = cailiaoInfo.getImgURL();
		if(!imageURL.contains("http")){
			imageURL = URLGenerator.BORROW_CAILIAO_BASE_URL + imageURL;
		}
		ImageLoaderManager.loadingImage(imageLoader, imageURL, holder.imageView, options, animateFirstListener, null);
		return convertView;
	}
	
	/** 
     * 图片加载第一次显示监听器 
     * @author Administrator 
     * 
     */  
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {  
          
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());  
  
        @Override  
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {  
            if (loadedImage != null) {  
                ImageView imageView = (ImageView) view;  
                // 是否第一次显示  
                boolean firstDisplay = !displayedImages.contains(imageUri);  
                if (firstDisplay) {  
                    // 图片淡入效果  
                    FadeInBitmapDisplayer.animate(imageView, 500);  
                    displayedImages.add(imageUri);  
                }  
            }  
        }  
    }  
	
    /**
     * 内部类，定义Item的元素
     * @author Mr.liu
     *
     */
	class ViewHolder{
		ImageView imageView;
		TextView title;
	}

}
