package com.ylfcf.ppp.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.RedBagInvestAdapter;
import com.ylfcf.ppp.entity.RedBagInfo;
import com.ylfcf.ppp.ui.BidZXDActivity.OnHBWindowItemClickListener;

/**
 * 当前用户可用红包列表的window
 * 
 * @author Administrator
 * 
 */
public class HBListPopupwindow extends PopupWindow implements OnClickListener {
	private ListView hbListView;
	private TextView titleText;
	private LayoutInflater layoutInflater;
	private View headerView;
	private OnHBWindowItemClickListener onItemClickListener;
	private RedBagInvestAdapter adapter;
	private Activity context;
	private String title;
	private WindowManager.LayoutParams lp = null;

	public HBListPopupwindow(Context context) {
		super(context);
	}

	public HBListPopupwindow(Context context, View convertView, int width,
			int height,String title) {
		super(convertView, LayoutParams.MATCH_PARENT, height);
		this.context = (Activity) context;
		this.title = title;
		findViews(convertView);
	}

	private void findViews(View popView) {
		lp = context.getWindow().getAttributes();
		lp.alpha = 0.4f;
		context.getWindow().setAttributes(lp);

		layoutInflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		headerView = layoutInflater.inflate(R.layout.red_bag_listview_header,
				null);
		titleText = (TextView) popView
				.findViewById(R.id.tyj_list_popupwindow_title);
		titleText.setText(title);
		hbListView = (ListView) popView
				.findViewById(R.id.tyj_list_popupwindow_listview);
		hbListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (onItemClickListener != null) {
					onItemClickListener.onItemClickListener(view, position);
					dismiss();
				}
			}
		});
		adapter = new RedBagInvestAdapter(context);
		hbListView.addHeaderView(headerView);
		hbListView.setAdapter(adapter);
	}

	public void show(View parentView, List<RedBagInfo> list,
			OnHBWindowItemClickListener listener) {
		this.onItemClickListener = listener;
		ColorDrawable cd = new ColorDrawable(0x000000);
		this.setBackgroundDrawable(cd);// 使得返回键有效 并且去除popupwindow圆角的黑色背景
		this.setAnimationStyle(R.style.bidPopwindowStyle);
		this.setOutsideTouchable(false);
		this.setFocusable(true);
		this.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
		updateAdapter(list);
	}

	private void updateAdapter(List<RedBagInfo> list) {
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
