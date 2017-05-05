package com.ylfcf.ppp.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.YXBRedeemRecordInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/**
 * 元信宝投资记录
 * @author Administrator
 *
 */
public class YXBRedeemRecordAdapter extends ArrayAdapter<YXBRedeemRecordInfo>{
	private final static int RESOURCE_ID = R.layout.yxb_trans_record_list_item;
	
	private List<YXBRedeemRecordInfo> yxbRecordList;
	private Context context;
	private LayoutInflater layoutInflater;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
	
	public YXBRedeemRecordAdapter(Context context) {
		super(context, RESOURCE_ID);
		this.context = context;
		yxbRecordList = new ArrayList<YXBRedeemRecordInfo>();
		layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * 对外方法，动态改变listview的item并进行刷新
	 * @param list
	 */
	public void setItems(List<YXBRedeemRecordInfo> list){
		yxbRecordList.clear();
		if(list != null){
			yxbRecordList.addAll(list);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return yxbRecordList.size();
	}

	@Override
	public YXBRedeemRecordInfo getItem(int position) {
		return yxbRecordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		YXBRedeemRecordInfo info = yxbRecordList.get(position);
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.yxbMoney = (TextView)convertView.findViewById(R.id.yxb_trans_record_item_money);
			viewHolder.dateText = (TextView)convertView.findViewById(R.id.yxb_trans_record_item_date);
			viewHolder.typeText = (TextView)convertView.findViewById(R.id.yxb_trans_record_item_type);
			viewHolder.statusText = (TextView)convertView.findViewById(R.id.yxb_trans_record_item_status);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.yxbMoney.setText(info.getApply_money());
		viewHolder.typeText.setText("赎回");
		viewHolder.statusText.setText("赎回成功");
		try {
			Date applyDate = sdf.parse(info.getApply_time());
			viewHolder.dateText.setText(sdf1.format(applyDate));
		} catch (ParseException e) {
			e.printStackTrace();
			viewHolder.dateText.setText(info.getApply_time());
		}
		return convertView;
	}

	/**
	 * 内部类，定义Item的元素
	 * @author Mr.liu
	 *
	 */
	class ViewHolder{
		TextView yxbMoney;//交易金额
		TextView dateText;
		TextView typeText;//类型
		TextView statusText;//状态
	}
}
