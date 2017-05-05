package com.ylfcf.ppp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.ui.RegisteActivity.OnRegisteCompSucClickListener;

/**
 * 企业用户注册成功
 * @author Mr.liu
 *
 */
public class RegisteSucCompWindow extends PopupWindow implements OnClickListener{
	private Activity context;
	private Button okBtn;
	private TextView content;

	private String phone;
	private WindowManager.LayoutParams lp = null;
	private OnRegisteCompSucClickListener clickListener;
	
	public RegisteSucCompWindow(Context context) {
		super(context);
	}

	public RegisteSucCompWindow(Context context, View convertView, int width,
			int height,String phone,OnRegisteCompSucClickListener clickListener) {
		super(convertView, width, height);
		this.context = (Activity) context;
		this.phone = phone;
		this.clickListener = clickListener;
		findViews(convertView);
	}

	private void findViews(View popView) {
		lp = context.getWindow().getAttributes();
		lp.alpha = 0.4f;
		context.getWindow().setAttributes(lp);

		okBtn = (Button) popView.findViewById(R.id.registe_suc_comp_layout_btn);
		okBtn.setOnClickListener(this);
		content = (TextView) popView
				.findViewById(R.id.registe_suc_comp_layout_content);
		String contentStr = "我们将在2个工作日内拨打\n【"+phone+"】与您联系，请保持此手机号的畅通，感谢配合！";
		content.setText(contentStr);
		try {
			SpannableStringBuilder builder = new SpannableStringBuilder(content.getText().toString());  
			ForegroundColorSpan blueSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.common_topbar_bg_color));  
			ForegroundColorSpan graySpan = new ForegroundColorSpan(context.getResources().getColor(R.color.gray)); 
			builder.setSpan(graySpan, 0, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
			builder.setSpan(blueSpan, 13, 26, Spannable.SPAN_INCLUSIVE_INCLUSIVE);  
			builder.setSpan(graySpan, 26, contentStr.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
			content.setText(builder);
		} catch (Exception e) {
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
		lp.alpha = 1.0f;
		context.getWindow().setAttributes(lp);
		if(clickListener != null)
			clickListener.onClick();
	}

	@SuppressWarnings("deprecation")
	public void show(View parentView) {
//		this.setBackgroundDrawable(new BitmapDrawable()); // 使得返回键有效
		context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); 
		this.setAnimationStyle(R.style.rechargeMsgPopwindowStyle);
		this.setOutsideTouchable(false);
		this.setFocusable(true);
		this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.registe_suc_comp_layout_btn:
			if(clickListener != null)
			clickListener.onClick();
			this.dismiss();
			break;
		default:
			break;
		}
	}
}
