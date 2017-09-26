package com.ylfcf.ppp.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.PopBannerInfo;
import com.ylfcf.ppp.ui.BannerTopicActivity;
import com.ylfcf.ppp.util.ImageLoaderManager;

/**
 * 首页弹窗
 * Created by Administrator on 2017/6/26.
 */

public class CommonBannerPopwindow extends PopupWindow implements
        View.OnClickListener {
    private ImageView deleteImg;
    private ImageView contentImg;
    private Activity context;
    private WindowManager.LayoutParams lp = null;
    private PopBannerInfo mPopBannerInfo;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public CommonBannerPopwindow(Context context) {
        super(context);
    }

    public CommonBannerPopwindow(Context context, View convertView,
                                 int width, int height, PopBannerInfo popInfo) {
        super(convertView, width, height);
        this.context = (Activity) context;
        this.mPopBannerInfo = popInfo;
        this.imageLoader = ImageLoaderManager.newInstance();
        options = ImageLoaderManager.configurationOption(R.drawable.icon_pop_empty,
                R.drawable.icon_pop_empty);
        findViews(convertView);
    }

    private void findViews(View popView) {
        deleteImg = (ImageView) popView.findViewById(R.id.common_banner_popwindow_delete_btn);
        deleteImg.setOnClickListener(this);
        contentImg = (ImageView) popView.findViewById(R.id.common_banner_popwindow_main_content);
        contentImg.setOnClickListener(this);
        ImageLoaderManager.loadingImage(imageLoader, mPopBannerInfo.getPic(),contentImg, options, null, null);
        lp = context.getWindow().getAttributes();
        lp.alpha = 0.3f;
        context.getWindow().setAttributes(lp);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        lp.alpha = 1.0f;
        context.getWindow().setAttributes(lp);
    }

    public void show(View parentView) {
        ColorDrawable cd = new ColorDrawable(0x000000);
        this.setBackgroundDrawable(cd);// 使得返回键有效 并且去除popupwindow圆角的黑色背景 点击之外的地方自动消失
        this.setAnimationStyle(R.style.rechargeMsgPopwindowStyle);
        this.setOutsideTouchable(false);
        this.setFocusable(true);
        this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_banner_popwindow_delete_btn:
                dismiss();
                break;
            case R.id.common_banner_popwindow_main_content:
                Intent intentBanner = new Intent(context, BannerTopicActivity.class);
                BannerInfo info = new BannerInfo();
                info.setLink_url(mPopBannerInfo.getUrl_link());
                intentBanner.putExtra("BannerInfo",info);
                context.startActivity(intentBanner);
                dismiss();
                break;
            default:
                break;
        }
    }
}
