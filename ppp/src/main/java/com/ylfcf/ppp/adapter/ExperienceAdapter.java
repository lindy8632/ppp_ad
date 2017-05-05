package com.ylfcf.ppp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.TYJInfo;

/**
 * 政信贷的投资页面--使用体验金
 * @author Mr.liu
 */
public class ExperienceAdapter extends ArrayAdapter<TYJInfo>{
	private static final int RESOURCE_ID = R.layout.experience_listview_item;  
	private Context context;
	private List<TYJInfo> experienceList = null;
	private LayoutInflater layoutInflater = null;
	
	public ExperienceAdapter(Context context){
		super(context, RESOURCE_ID);
		this.context = context;
		experienceList = new ArrayList<TYJInfo>();
		layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * 对外方法，动态改变listview的item并进行刷新
	 * @param list
	 */
	public void setItems(List<TYJInfo> list){
		experienceList.clear();
		if(list != null){
			experienceList.addAll(list);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return experienceList.size();
	}

	@Override
	public TYJInfo getItem(int position) {
		return experienceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		TYJInfo info = experienceList.get(position);
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.text = (TextView)convertView.findViewById(R.id.experience_listview_item_text);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		int needInvestMoneyInt = 0;
		try {
			needInvestMoneyInt = Integer.parseInt(info.getNeed_invest_money());
		} catch (Exception e) {
		}
		if(needInvestMoneyInt <= 0){
			viewHolder.text.setText(info.getAccount()+"元体验金");
		}else{
			viewHolder.text.setText(info.getAccount()+"元体验金，"+"需投资"+info.getNeed_invest_money()+"元以上可用");
		}
		return convertView;
	}
	
	/**
	 * 内部类，定义Item的元素
	 * @author Mr.liu
	 *
	 */
	class ViewHolder{
		TextView text;
	}
}
