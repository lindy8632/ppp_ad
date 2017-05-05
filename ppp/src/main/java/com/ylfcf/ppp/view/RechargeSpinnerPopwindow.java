package com.ylfcf.ppp.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.BankInfo;
import com.ylfcf.ppp.ui.BindCardActivity.OnSpinnerItemClickListener;

/**
 *充值银行列表的poppwindow
 * @author Administrator
 *
 */
public class RechargeSpinnerPopwindow extends PopupWindow{
	private ListView listview;
	private List<BankInfo> list = null;
	private OnSpinnerItemClickListener onSpinnerItemClickListener = null;
	private LayoutInflater layoutInflater = null;
	private Activity context;
	private WindowManager.LayoutParams lp = null;
	
	public RechargeSpinnerPopwindow(Context context){
		super(context);
	}
	
	public RechargeSpinnerPopwindow(Context context,View convertView,int width,int height,List<BankInfo> bankList,
			OnSpinnerItemClickListener onSpinnerItemClickListener,LayoutInflater layoutInflater){
		super(convertView, width, height);
		this.context = (Activity) context;
		this.list = bankList;
		this.onSpinnerItemClickListener = onSpinnerItemClickListener;
		this.layoutInflater = layoutInflater;
		findViews(convertView);
	}
	
	private void findViews(View popView){
		lp = context.getWindow().getAttributes();
		lp.alpha = 0.4f;
		context.getWindow().setAttributes(lp);
		
		listview = (ListView) popView.findViewById(R.id.recharge_spinner_poplayout_listview);
		listview.setAdapter(new RechargeSpinnerAdapter());
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onSpinnerItemClickListener.onClick(view, position);
				dismiss();
			}
		});
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
		lp.alpha = 1.0f;
		context.getWindow().setAttributes(lp);
	}

	public void show(View parentView){
		ColorDrawable cd = new ColorDrawable(0x000000);
		this.setBackgroundDrawable(cd);//使得返回键有效 并且去除popupwindow圆角的黑色背景
		this.setAnimationStyle(R.style.rechargeMsgPopwindowStyle);
		this.setOutsideTouchable(false);
		this.setFocusable(true);
		this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
	}
	
	class RechargeSpinnerAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			BankInfo info = list.get(position);
			if(convertView == null){
				convertView = layoutInflater.inflate(R.layout.recharge_spinner_item, null);
			}
			TextView textView = (TextView) convertView.findViewById(R.id.recharge_spinner_item_text);
			textView.setText(info.getBank_name());
			return convertView;
		}
	}

}
