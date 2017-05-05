package com.ylfcf.ppp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.PrizeInfo;

/**
 * 提现规则
 * @author Administrator
 *
 */
public class LotteryDetailPopwindow extends PopupWindow implements OnClickListener{
	private Button delBtn;
	private TextView contentView;
	private Activity context;
	private WindowManager.LayoutParams lp = null;
	
	public LotteryDetailPopwindow(Context context){
		super(context);
	}
	
	public LotteryDetailPopwindow(Context context, View convertView, int width, int height){
		super(convertView, width, RelativeLayout.LayoutParams.WRAP_CONTENT);
		this.context = (Activity) context;
		findViews(convertView);
	}
	
	private void findViews(View popView){
		lp = context.getWindow().getAttributes();
		lp.alpha = 0.4f;
		context.getWindow().setAttributes(lp);

		delBtn = (Button) popView.findViewById(R.id.lottery_detail_poplayout_delbtn);
		delBtn.setOnClickListener(this);
		contentView = (TextView)popView.findViewById(R.id.lottery_detail_poplayout_content);
	}
	
	public void show(View parentView, PrizeInfo info){
		ColorDrawable cd = new ColorDrawable(0x000000);
		this.setBackgroundDrawable(cd);//使得返回键有效 并且去除popupwindow圆角的黑色背景
		this.setAnimationStyle(R.style.rechargeMsgPopwindowStyle);
		this.setOutsideTouchable(false);
		this.setFocusable(true);
		this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
		contentView.setText(Html.fromHtml(info.getProduct_detail()));
	}

	@Override
	public void dismiss() {
		super.dismiss();
		lp.alpha = 1.0f;
		context.getWindow().setAttributes(lp);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lottery_detail_poplayout_delbtn:
			this.dismiss();
			break;

		default:
			break;
		}
	}

}
