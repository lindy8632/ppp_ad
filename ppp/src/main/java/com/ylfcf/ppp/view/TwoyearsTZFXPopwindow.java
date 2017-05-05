package com.ylfcf.ppp.view;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.ui.BannerDetailsActivity;
import com.ylfcf.ppp.ui.BannerTopicActivity;
import com.ylfcf.ppp.ui.BorrowListZXDActivity;
import com.ylfcf.ppp.util.URLGenerator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

/**
 * 两周年感恩回馈活动
 * @author Mr.liu
 *
 */
public class TwoyearsTZFXPopwindow  extends PopupWindow implements OnClickListener{
	private ImageView deleteImg;
	private ImageView contentImg;
	private Activity context;
	private WindowManager.LayoutParams lp = null;

	public TwoyearsTZFXPopwindow(Context context) {
		super(context);
	}

	public TwoyearsTZFXPopwindow(Context context, View convertView,
			int width, int height) {
		super(convertView, width, height);
		this.context = (Activity) context;
		findViews(convertView);
	}

	private void findViews(View popView) {
		deleteImg = (ImageView) popView.findViewById(R.id.twoyears_tzfx_popwindow_delete_btn);
		deleteImg.setOnClickListener(this);
		contentImg = (ImageView) popView.findViewById(R.id.twoyears_tzfx_popwindow_main_content);
		contentImg.setOnClickListener(this);
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
		case R.id.twoyears_tzfx_popwindow_delete_btn:
			dismiss();
			break;
		case R.id.twoyears_tzfx_popwindow_main_content:
			Intent intentBanner = new Intent(context,BannerTopicActivity.class);
			BannerInfo bannerInfo = new BannerInfo();
			bannerInfo.setArticle_id("170");
			bannerInfo.setLink_url(URLGenerator.TWOYEARS_TZFX_URL);
			intentBanner.putExtra("BannerInfo", bannerInfo);
			context.startActivity(intentBanner);
			dismiss();
			break;
		default:
			break;
		}
	}

}
