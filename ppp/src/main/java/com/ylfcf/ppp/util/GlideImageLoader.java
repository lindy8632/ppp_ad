package com.ylfcf.ppp.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ylfcf.ppp.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Administrator on 2017/8/30
 */

public class GlideImageLoader extends ImageLoader{
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        if(imageView != null){
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        Glide.with(context.getApplicationContext())
                .load(path)
                .placeholder(R.drawable.icon_empty)//加载等待过程中
                .error(R.drawable.icon_error)//加载失败
                .into(imageView);
    }
}
