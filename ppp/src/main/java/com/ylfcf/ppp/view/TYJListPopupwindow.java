package com.ylfcf.ppp.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.ExperienceAdapter;
import com.ylfcf.ppp.entity.TYJInfo;
import com.ylfcf.ppp.ui.BidZXDActivity.OnTYJWindowItemClickListener;

/**
 * 投资页面可使用的体验金列表
 * @author Mr.liu
 */
public class TYJListPopupwindow extends PopupWindow implements OnClickListener {
	private ListView tyjListView;
	private TextView titleText;
	private OnTYJWindowItemClickListener onItemClickListener;
	private ExperienceAdapter adapter;
	private Activity context;
	private WindowManager.LayoutParams lp = null;

	public TYJListPopupwindow(Context context) {
		super(context);
	}

	public TYJListPopupwindow(Context context, View convertView, int width,
			int height) {
		super(convertView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.context = (Activity) context;
		findViews(convertView);
	}

	private void findViews(View popView) {
		lp = context.getWindow().getAttributes();
		lp.alpha = 0.4f;
		context.getWindow().setAttributes(lp);

		titleText = (TextView) popView
				.findViewById(R.id.tyj_list_popupwindow_title);
		titleText.setText("请选择体验金");
		tyjListView = (ListView) popView
				.findViewById(R.id.tyj_list_popupwindow_listview);
		tyjListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (onItemClickListener != null) {
					onItemClickListener.onItemClickListener(view, position);
					dismiss();
				}
			}
		});
		adapter = new ExperienceAdapter(context);
		tyjListView.setAdapter(adapter);
	}

	public void show(View parentView, List<TYJInfo> list,
			OnTYJWindowItemClickListener listener) {
		this.onItemClickListener = listener;
		ColorDrawable cd = new ColorDrawable(0x000000);
		this.setBackgroundDrawable(cd);// 使得返回键有效 并且去除popupwindow圆角的黑色背景
		this.setAnimationStyle(R.style.bidPopwindowStyle);
		this.setOutsideTouchable(false);
		this.setFocusable(true);
		this.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
		updateAdapter(list);
	}

	private void updateAdapter(List<TYJInfo> list) {
		adapter.setItems(list);
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
		default:
			break;
		}
	}
}
