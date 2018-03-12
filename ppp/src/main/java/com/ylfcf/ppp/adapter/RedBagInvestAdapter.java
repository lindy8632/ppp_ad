package com.ylfcf.ppp.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.RedBagInfo;
import com.ylfcf.ppp.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前用户可使用的红包列表
 * @author Administrator
 *
 */
public class RedBagInvestAdapter extends ArrayAdapter<RedBagInfo>{
	private static final int RESOURCE_ID = R.layout.experience_listview_item;  
	private Context context;
	private List<RedBagInfo> redbagList = null;
	private LayoutInflater layoutInflater = null;
	private int prePosition;
	
	public RedBagInvestAdapter(Context context,int prePosition){
		super(context, RESOURCE_ID);
		this.context = context;
		this.prePosition = prePosition;
		redbagList = new ArrayList<RedBagInfo>();
		layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * 对外方法，动态改变listview的item并进行刷新
	 * @param list
	 */
	public void setItems(List<RedBagInfo> list){
		redbagList.clear();
		if(list != null){
			redbagList.addAll(list);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return redbagList.size();
	}

	@Override
	public RedBagInfo getItem(int position) {
		return redbagList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		RedBagInfo info = redbagList.get(position);
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.text = (TextView)convertView.findViewById(R.id.experience_listview_item_text);
			viewHolder.text1 = (TextView)convertView.findViewById(R.id.experience_listview_item_text1);
			viewHolder.duihao = (ImageView)convertView.findViewById(R.id.experience_listview_item_img);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		int needInvestMoneyInt = 0;
		try {
			needInvestMoneyInt = Integer.parseInt(info.getNeed_invest_money());
		} catch (Exception e) {
		}
		if(prePosition == position){
			viewHolder.duihao.setImageResource(R.drawable.duihao_selected);
		}else{
			viewHolder.duihao.setImageResource(R.drawable.duihao_unselected);
		}
		viewHolder.text.setText(Util.formatRate(info.getMoney())+"元");
		if(needInvestMoneyInt <= 0){
			viewHolder.text1.setText(Html.fromHtml("需投资<font color='#31B2FE'>"+info.getNeed_invest_money()+"元</font>及以上可用"));
		}else{
			if(needInvestMoneyInt<10000){
				viewHolder.text1.setText(Html.fromHtml("需投资<font color='#31B2FE'>"+info.getNeed_invest_money()+"元</font>及以上可用"));
			}else{
				viewHolder.text1.setText(Html.fromHtml("需投资<font color='#31B2FE'>"+Util.formatRate(String.valueOf(needInvestMoneyInt/10000d))+"万元</font>及以上可用"));
			}
		}
		return convertView;
	}
	
	/**
	 * 内部类，定义Item的元素
	 * @author Mr.liu
	 *
	 */
	class ViewHolder{
		TextView text;//红包额度
		TextView text1;//投资条件
		ImageView duihao;
	}
}
