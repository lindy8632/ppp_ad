package com.ylfcf.ppp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.JXQInvestAdapter;
import com.ylfcf.ppp.entity.JiaxiquanInfo;
import com.ylfcf.ppp.ui.BidZXDActivity.OnHBWindowItemClickListener;

import java.util.List;
/**
 * 加息券
 * @author Mr.liu
 *
 */
public class JXQListPopupwindow extends PopupWindow implements OnClickListener{
	private ListView jxqListView;
	private TextView titleText;
	private LayoutInflater layoutInflater;
	private View headerView;
	private ImageView hearderImg;
	private ImageView xImg;
	private OnHBWindowItemClickListener onItemClickListener;
	private JXQInvestAdapter adapter;
	private Activity context;
	private String title;
	private int position;
	private WindowManager.LayoutParams lp = null;

	public JXQListPopupwindow(Context context) {
		super(context);
	}

	public JXQListPopupwindow(Context context, View convertView, int width,
			int height,String title,int position) {
		super(convertView, LayoutParams.MATCH_PARENT, height);
		this.context = (Activity) context;
		this.title = title;
		this.position = position;
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
		TextView footText = (TextView) headerView.findViewById(R.id.red_bag_listview_header_text);
		footText.setText("不使用加息券");
		hearderImg = headerView.findViewById(R.id.red_bag_listview_header_img);
		if(position == 0){
			hearderImg.setImageResource(R.drawable.duihao_selected);
			footText.setTextColor(context.getResources().getColor(R.color.black));
		}else{
			hearderImg.setImageResource(R.drawable.duihao_unselected);
			footText.setTextColor(context.getResources().getColor(R.color.gray1));
		}
		xImg = (ImageView) popView.findViewById(R.id.tyj_list_popupwindow_x_img);

		titleText = (TextView) popView
				.findViewById(R.id.tyj_list_popupwindow_title);
		titleText.setText(title);
		jxqListView = (ListView) popView
				.findViewById(R.id.tyj_list_popupwindow_listview);
		jxqListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (onItemClickListener != null) {
					onItemClickListener.onItemClickListener(view, position);
					dismiss();
				}
			}
		});
		xImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		adapter = new JXQInvestAdapter(context,position - 1);
		jxqListView.addHeaderView(headerView);
		jxqListView.setAdapter(adapter);
	}

	public void show(View parentView, List<JiaxiquanInfo> list,
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

	private void updateAdapter(List<JiaxiquanInfo> list) {
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
