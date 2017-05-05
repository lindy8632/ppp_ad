package com.ylfcf.ppp.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncUserSelectOne;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.UserInfo;
import com.ylfcf.ppp.inter.Inter.OnGetUserInfoByPhone;
import com.ylfcf.ppp.ui.FundsDetailsActivity;
import com.ylfcf.ppp.ui.RechargeActivity.OnRechargeErrorInter;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 充值失败的信息提示
 * @author Mr.liu
 *
 */
public class RechargeErrorPopwindow extends PopupWindow implements
					OnClickListener{
	private TextView title;//错误标题
	private TextView content;//错误提示内容
	private Button repeatBtn,catRecordBtn;
	private Activity context;
	private WindowManager.LayoutParams lp = null;
	private OnRechargeErrorInter mOnRechargeErrorInter;

	private String errorMsg;
	public RechargeErrorPopwindow(Context context) {
		super(context);
	}

	public RechargeErrorPopwindow(Context context, View convertView,
			int width, int height,String errorMsg,OnRechargeErrorInter inter) {
		super(convertView, width, height);
		this.context = (Activity) context;
		this.errorMsg = errorMsg;
		this.mOnRechargeErrorInter = inter;
		findViews(convertView);
	}

	private void findViews(View popView) {
		lp = context.getWindow().getAttributes();
		lp.alpha = 0.4f;
		context.getWindow().setAttributes(lp);
		repeatBtn = (Button) popView.findViewById(R.id.recharge_error_popwindow_repeat);
		repeatBtn.setOnClickListener(this);
		catRecordBtn = (Button) popView.findViewById(R.id.recharge_error_popwindow_cat_record);
		catRecordBtn.setOnClickListener(this);
		title = (TextView) popView.findViewById(R.id.recharge_error_popwindow_title);
		title.setText("充值失败");
		content = (TextView) popView.findViewById(R.id.recharge_error_popwindow_content);
		content.setText("失败原因："+errorMsg);
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
		this.setOutsideTouchable(false);//点击popwindow外部不消失
		this.setFocusable(false);
		this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
	}

	@Override
	public void setOnDismissListener(OnDismissListener onDismissListener) {
		super.setOnDismissListener(onDismissListener);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.recharge_error_popwindow_repeat:
			//继续充值
			mOnRechargeErrorInter.back(true);
			dismiss();
			break;
		case R.id.recharge_error_popwindow_cat_record:
			//查看充值记录
			mOnRechargeErrorInter.back(true);
			requestUserInfo(SettingsManager.getUserId(context), "");
			dismiss();
			break;
		default:
			break;
		}
	}

	/**
	 * 请求用户信息，根据hf_user_id字段判断用户是否有汇付账户
	 * @param userId
	 * @param phone
	 */
	private void requestUserInfo(final String userId,String phone){
		AsyncUserSelectOne userTask = new AsyncUserSelectOne(context, userId, phone, "", new OnGetUserInfoByPhone() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						UserInfo userInfo = baseInfo.getUserInfo();
						Intent intent = new Intent(context,FundsDetailsActivity.class);
						intent.putExtra("userinfo", userInfo);
						context.startActivity(intent);
					}
				}
			}
		});
		userTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
