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
import com.ylfcf.ppp.ui.UserVerifyActivity;

/**
 * 认证成功弹窗
 * Created by Administrator on 2018/1/12.
 */

public class VerifySucPopwindow extends PopupWindow implements
        View.OnClickListener{
    private Button rechargeBtn;
    private Button cancelBtn;
    private Activity context;
    private WindowManager.LayoutParams lp = null;
    private UserVerifyActivity.OnPopRechargeBtnListener onPopRechargeBtnListener;
    private UserVerifyActivity.OnPopCancelBtnListener onPopCancelBtnListener;

    public VerifySucPopwindow(Context context) {
        super(context);
    }

    public VerifySucPopwindow(Context context, View convertView,
                              int width, int height,
                              UserVerifyActivity.OnPopRechargeBtnListener onPopRechargeBtnListener,
                              UserVerifyActivity.OnPopCancelBtnListener onPopCancelBtnListener) {
        super(convertView, width, height);
        this.context = (Activity) context;
        this.onPopRechargeBtnListener = onPopRechargeBtnListener;
        this.onPopCancelBtnListener = onPopCancelBtnListener;
        findViews(convertView);
    }

    private void findViews(View popView) {
        rechargeBtn = popView.findViewById(R.id.verify_suc_popwindow_recharge_btn);
        rechargeBtn.setOnClickListener(this);
        cancelBtn = popView.findViewById(R.id.verify_suc_popwindow_cancel_btn);
        cancelBtn.setOnClickListener(this);
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
        this.setFocusable(false);
        this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_suc_popwindow_recharge_btn:
                if(onPopRechargeBtnListener != null){
                    onPopRechargeBtnListener.back();
                }
                dismiss();
                break;
            case R.id.verify_suc_popwindow_cancel_btn:
                if(onPopCancelBtnListener != null){
                    onPopCancelBtnListener.back();
                }
                dismiss();
                break;
            default:
                break;
        }
    }
}
