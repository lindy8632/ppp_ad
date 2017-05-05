package com.ylfcf.ppp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ylfcf.ppp.R;

/**
 * 会员专区 规则的弹窗
 * @author Mr.liu
 *
 */
public class PrizeRegionRuleWindow extends PopupWindow implements OnClickListener{
	private TextView deleteBtn;
	private TextView contentTV;
	private int position;
	private Activity context;
	private WindowManager.LayoutParams lp = null;

	public PrizeRegionRuleWindow(Context context) {
		super(context);
	}

	public PrizeRegionRuleWindow(Context context, View convertView,
			int width, int height,int position) {
		super(convertView, width, height);
		this.context = (Activity) context;
		this.position = position;
		findViews(convertView);
	}

	private void findViews(View popView) {
		lp = context.getWindow().getAttributes();
		lp.alpha = 0.4f;
		context.getWindow().setAttributes(lp);
		deleteBtn = (TextView) popView.findViewById(R.id.prize_region_rule_window_delete);
		deleteBtn.setOnClickListener(this);
		contentTV = (TextView) popView.findViewById(R.id.prize_region_rule_window_text);
		contentTV.setText("复制复制复制粘贴");
		if(position == 1){
//			contentTV.setText("【使用说明】\n1、此券只适用于上海地区，仅限前200名。\n2、此券只限非会员本人在VITA连锁的所有门店使用。\n3、请至少提前一天向门店预约，凭有效身份证入场。\n4、有效期至2017年4月30日。\n【门店地址】\n【陆家嘴-国际会议中心店】滨江大道2727号号9楼  客服热线：021-33847902\n【八佰伴-汤臣洲际店】张杨路777号4楼  客服热线：021-31397415\n【人民广场-索菲特海仑店】南京东路505号7楼  客服热线：021-63517987\n【金桥-华美达店】新金桥路18号4楼  客服热线：021-31666761");
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
		lp.alpha = 1.0f;
		context.getWindow().setAttributes(lp);
	}

	public void show(View parentView) {
		ColorDrawable cd = new ColorDrawable(0x000000);
//		this.setBackgroundDrawable(cd);// 使得返回键有效 并且去除popupwindow圆角的黑色背景 点击之外的地方自动消失
		this.setAnimationStyle(R.style.rechargeMsgPopwindowStyle);
		this.setOutsideTouchable(false);
		this.setFocusable(true);
		this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.prize_region_rule_window_delete:
			dismiss();
			break;
		default:
			break;
		}
	}
}
