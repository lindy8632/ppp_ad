package com.ylfcf.ppp.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.ArticleAdapter.ViewHolder;
import com.ylfcf.ppp.entity.ArticleInfo;
import com.ylfcf.ppp.entity.SignInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 签到的适配器
 * @author Mr.liu
 *
 */
public class SignAdapter extends ArrayAdapter<SignInfo>{
	private static final int RESOURCE_ID = R.layout.sign_item;
	private List<SignInfo> signList = null;
	private Context context;
	private LayoutInflater layoutInflater;
	
	public SignAdapter(Context context) {
		super(context, RESOURCE_ID);
		this.context = context;
		layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		signList = new ArrayList<SignInfo>();
	}
	
	/**
	 * 对外方法，动态改变listview的item并进行刷新
	 * @param articleList
	 */
	public void setItems(List<SignInfo> signList){
		this.signList.clear();
		if(signList != null){
			this.signList.addAll(signList);
		}
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return signList.size();
	}
	
	@Override
	public SignInfo getItem(int position) {
		return signList.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SignInfo info = signList.get(position);
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.layout = (RelativeLayout) convertView.findViewById(R.id.sign_item_layout);
			viewHolder.day = (TextView) convertView.findViewById(R.id.sign_item_day);
			viewHolder.logo = (ImageView) convertView.findViewById(R.id.sign_item_img);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.day.setText(info.getDay());
		if(info.isSigned()){
			viewHolder.logo.setVisibility(View.VISIBLE);
			if(info.isToday()){
				viewHolder.layout.setBackgroundColor(context.getResources().getColor(R.color.signed_today_bg_color));
				viewHolder.logo.setBackgroundResource(R.drawable.signed_logo_today);
				viewHolder.day.setTextColor(context.getResources().getColor(R.color.white));
			}else{
				viewHolder.layout.setBackgroundColor(context.getResources().getColor(R.color.white));
				viewHolder.day.setTextColor(context.getResources().getColor(R.color.gray));
				viewHolder.logo.setBackgroundResource(R.drawable.signed_logo);
			}
		}
		return convertView;
	}

	/**
	 * 内部类，定义item的元素
	 * @author Mr.liu
	 *
	 */
	class ViewHolder{
		RelativeLayout layout;//item布局
		TextView day;//日期
		ImageView logo;
	}

}
