package com.ylfcf.ppp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.FundsDetailsInfo;
import com.ylfcf.ppp.util.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 资金明细列表adapter
 * @author Administrator
 *
 */
public class FundsDetailsAdapter extends ArrayAdapter<FundsDetailsInfo>{
	
	private static final int RESOURCE_ID = R.layout.funds_details_listview_item;  
	private Context context;
	private List<FundsDetailsInfo> fundsDetailsList = null;
	private LayoutInflater layoutInflater = null;
	private String type;//表示是汇付还是易联   因为汇付跟易联的充值字段数据库不一致
	private String investType;//表示是元信宝还是政信贷（yxb | zxd）
	private DecimalFormat df = new  DecimalFormat("0.00");
	
	public FundsDetailsAdapter(Context context,String type,String investType){
		super(context, RESOURCE_ID);
		this.context = context;
		this.type = type;
		this.investType = investType;
		fundsDetailsList = new ArrayList<FundsDetailsInfo>();
		layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * 对外方法，动态改变listview的item并进行刷新
	 * @param list
	 */
	public void setItems(List<FundsDetailsInfo> list){
		this.fundsDetailsList.clear();
		if(list != null){
			fundsDetailsList.addAll(list);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return fundsDetailsList.size();
	}

	@Override
	public FundsDetailsInfo getItem(int position) {
		return fundsDetailsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	double moneyD = 0d;
	double userMoneyD = 0d;
	double frozenMoneyD = 0d;
	double collectionMoneyD = 0d;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		FundsDetailsInfo info = fundsDetailsList.get(position);
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.textName1 = (TextView)convertView.findViewById(R.id.funds_details_item_textname1);
			viewHolder.typeText = (TextView)convertView.findViewById(R.id.funds_details_item_type);
			viewHolder.balance = (TextView)convertView.findViewById(R.id.funds_details_item_balance);
			viewHolder.time = (TextView)convertView.findViewById(R.id.funds_details_item_time);
			viewHolder.changedMoney = (TextView)convertView.findViewById(R.id.funds_details_item_changed_money);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		try {
			moneyD = Double.parseDouble(info.getMoney());
		} catch (Exception e) {
		}
		try {
			userMoneyD = Double.parseDouble(info.getUse_money());
		} catch (Exception e) {
		}
		try {
			frozenMoneyD = Double.parseDouble(info.getFrozen_money());
		} catch (Exception e) {
		}
		try {
			collectionMoneyD = Double.parseDouble(info.getCollection_money());
		} catch (Exception e) {
		}
		if("yxb".equals(investType)){
			viewHolder.textName1.setText("可用金额：");
			viewHolder.balance.setText(df.format(moneyD));
			viewHolder.changedMoney.setText(null);
		}else{
			viewHolder.textName1.setText("可用余额：");
			viewHolder.balance.setText(df.format(userMoneyD));
			viewHolder.changedMoney.setText(Util.formatRate(String.valueOf(moneyD)));
		}
		if("现金券本金".equals(info.getRemark())){
			viewHolder.typeText.setText("红包本金");
		}else if("现金券收益".equals(info.getRemark())){
			viewHolder.typeText.setText("红包收益");
		}else{
			viewHolder.typeText.setText(info.getRemark());
		}
		viewHolder.time.setText("时间："+info.getAdd_time());
		return convertView;
	}
	
	/**
	 * 内部类，定义Item的元素
	 * @author Mr.liu
	 *
	 */
	class ViewHolder{
		TextView textName1;
		TextView typeText;
		TextView time;//投资类型
		TextView balance;//可用余额
		TextView changedMoney;//资金变动金额
	}

}
