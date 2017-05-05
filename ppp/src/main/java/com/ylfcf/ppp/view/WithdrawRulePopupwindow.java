package com.ylfcf.ppp.view;

import com.ylfcf.ppp.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;

/**
 * 提现规则
 * 
 * @author Administrator
 * 
 */
public class WithdrawRulePopupwindow extends PopupWindow implements
		OnClickListener {
	private Button okBtn;
	private Activity context;
	private WindowManager.LayoutParams lp = null;

	public WithdrawRulePopupwindow(Context context) {
		super(context);
	}

	public WithdrawRulePopupwindow(Context context, View convertView,
			int width, int height) {
		super(convertView, width, height);
		this.context = (Activity) context;
		findViews(convertView);
	}

	private void findViews(View popView) {
		lp = context.getWindow().getAttributes();
		lp.alpha = 0.4f;
		context.getWindow().setAttributes(lp);

		okBtn = (Button) popView.findViewById(R.id.withdraw_rule_pop_btn);
		okBtn.setOnClickListener(this);
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
		case R.id.withdraw_rule_pop_btn:
			this.dismiss();
			break;

		default:
			break;
		}
	}

}
