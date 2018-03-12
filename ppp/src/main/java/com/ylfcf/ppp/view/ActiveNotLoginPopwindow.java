package com.ylfcf.ppp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.ui.BannerTopicActivity;

/**
 * 提示未登录的 弹窗
 * Created by Administrator on 2018/1/25.
 */

public class ActiveNotLoginPopwindow extends PopupWindow implements
        View.OnClickListener {
    private Button leftBtn,rightBtn,delBtn;
    private Activity context;
    private WindowManager.LayoutParams lp = null;
    private BannerTopicActivity.NotLoginWindowBtnsListener listener;

    public ActiveNotLoginPopwindow(Context context) {
        super(context);
    }

    public ActiveNotLoginPopwindow(Context context, View convertView,
                           int width, int height,BannerTopicActivity.NotLoginWindowBtnsListener listener) {
        super(convertView, width, height);
        this.context = (Activity) context;
        this.listener = listener;
        findViews(convertView);
    }

    private void findViews(View popView) {
        lp = context.getWindow().getAttributes();
        lp.alpha = 0.4f;
        context.getWindow().setAttributes(lp);
        leftBtn = (Button) popView.findViewById(R.id.active_notlogin_left_btn);
        rightBtn = (Button) popView.findViewById(R.id.active_notlogin_right_btn);
        delBtn = (Button) popView.findViewById(R.id.active_notlogin_del_btn);
        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
        delBtn.setOnClickListener(this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        lp.alpha = 1.0f;
        context.getWindow().setAttributes(lp);
    }

    public void show(View parentView) {
        ColorDrawable cd = new ColorDrawable(0x000000);
        this.setBackgroundDrawable(cd);// 使得返回键有效 并且去除popupwindow圆角的黑色背景
        this.setAnimationStyle(R.style.rechargeMsgPopwindowStyle);
        this.setOutsideTouchable(false);
        this.setFocusable(true);
        this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.active_notlogin_left_btn:
                if(listener != null)
                    listener.leftBtnBack();
                this.dismiss();
                break;
            case R.id.active_notlogin_right_btn:
                if(listener != null)
                    listener.rightBtnBack();
                this.dismiss();
                break;
            case R.id.active_notlogin_del_btn:
                this.dismiss();
                break;
            default:
                break;
        }
    }

}
