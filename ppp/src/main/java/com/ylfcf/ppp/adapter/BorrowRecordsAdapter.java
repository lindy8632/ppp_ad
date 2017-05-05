package com.ylfcf.ppp.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 某支标的投资记录（不是用户的投资记录...）
 * @author Administrator
 *
 */
public class BorrowRecordsAdapter extends ArrayAdapter<InvestRecordInfo>{
	private static final int RESOURCE_ID = R.layout.borrow_records_item;  
	private Context context;
	private List<InvestRecordInfo> investRecordList = null;
	private LayoutInflater layoutInflater = null;
	private DecimalFormat df = new DecimalFormat("#.00");//数字格式化，#表示一个非0的数字
	
	public BorrowRecordsAdapter(Context context){
		super(context, RESOURCE_ID);
		this.context = context;
		investRecordList = new ArrayList<InvestRecordInfo>();
		layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * 对外方法，动态改变listview的item并进行刷新
	 * @param recordList
	 */
	public void setItems(List<InvestRecordInfo> recordList){
		investRecordList.clear();
		if(recordList != null){
			investRecordList.addAll(recordList);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return investRecordList.size();
	}

	@Override
	public InvestRecordInfo getItem(int position) {
		return investRecordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		double moneyD = 0d;
		InvestRecordInfo info = investRecordList.get(position);
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.investor = (TextView)convertView.findViewById(R.id.borrow_records_item_investor);
			viewHolder.investType = (TextView)convertView.findViewById(R.id.borrow_records_item_type);
			viewHolder.investMoney = (TextView)convertView.findViewById(R.id.borrow_records_item_money);
			viewHolder.investTime = (TextView)convertView.findViewById(R.id.borrow_records_item_time);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(position%2 == 0){
			convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
		}else{
			convertView.setBackgroundColor(context.getResources().getColor(R.color.blue5));
		}
		
		viewHolder.investor.setText(Util.hiddenUsername(info.getUser_name()));//用户名
		viewHolder.investType.setText("手动");
		if(info.getAdd_time() == null || "".equals(info.getAdd_time())){
			viewHolder.investTime.setText(info.getFirst_borrow_time());
		}else{
			viewHolder.investTime.setText(info.getAdd_time());
		}
		try {
			moneyD = Double.parseDouble(info.getMoney());
			if(moneyD >= 10000){
				viewHolder.investMoney.setText(Util.double2PointDouble(moneyD/10000)+"万");
			}else{
				viewHolder.investMoney.setText(Util.double2PointDouble(moneyD));
			}
		} catch (Exception e) {
			viewHolder.investMoney.setText(info.getMoney());
		}
		return convertView;
	}
	
	/**
	 * 内部类，定义Item的元素
	 * @author Mr.liu
	 *
	 */
	class ViewHolder{
		TextView investor;//投资者
		TextView investType;//投资类型
		TextView investMoney;
		TextView investTime;
	}

}
