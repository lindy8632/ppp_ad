package com.ylfcf.ppp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.ActiveInfo;
import com.ylfcf.ppp.util.ImageLoaderManager;
import com.ylfcf.ppp.util.URLGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 活动专区
 * Created by Administrator on 2017/5/17.
 */

public class ActivitysRegionAdapter extends ArrayAdapter<ActiveInfo>{
    private static final int RESOURCE_ID = R.layout.active_region_list_item;
    private Context context;
    private List<ActiveInfo> activeList = null;
    private LayoutInflater layoutInflater = null;
    private int count = 0;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private AnimateFirstDisplayListener animateFirstListener = new AnimateFirstDisplayListener();

    public ActivitysRegionAdapter(Context context){
        super(context, RESOURCE_ID);
        this.context = context;
        activeList = new ArrayList<ActiveInfo>();
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.imageLoader = ImageLoaderManager.newInstance();
        options = ImageLoaderManager.configurationOption(R.drawable.default_vertical_logo,
                R.drawable.default_vertical_logo);
    }

    /**
     * 对外方法，动态改变listview的item并进行刷新
     * @param activeList
     */
    public void setItems(List<ActiveInfo> activeList){
        this.activeList.clear();
        this.count = activeList.size();
        if(activeList != null){
            this.activeList.addAll(activeList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return activeList.size();
    }

    @Override
    public ActiveInfo getItem(int position) {
        return activeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        ActiveInfo info = activeList.get(position);
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(RESOURCE_ID, null);
            viewHolder.activeLogo = (ImageView)convertView.findViewById(R.id.active_region_list_item_image);
            viewHolder.status = (TextView)convertView.findViewById(R.id.active_region_list_item_tv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if("-5".equals(info.getmActiveStatus().getStatus())){
            //活动已结束
            viewHolder.status.setText("活动结束");
            viewHolder.status.setBackgroundColor(context.getResources().getColor(R.color.edittext_hint_color));
        }else{
            if("-4".equals(info.getmActiveStatus().getStatus())){
                //活动尚未开始
                viewHolder.status.setText("敬请期待");
            }else{
                viewHolder.status.setText("活动进行中");
            }
            viewHolder.status.setBackgroundColor(context.getResources().getColor(R.color.common_topbar_bg_color));
        }
        String imageURL = info.getPic();
        if(!imageURL.contains("http")){
            imageURL = URLGenerator.BORROW_CAILIAO_BASE_URL + imageURL;
        }
        ImageLoaderManager.loadingImage(imageLoader, imageURL, viewHolder.activeLogo, options, animateFirstListener, null);
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
        TextView status;
        ImageView activeLogo;
    }
}
