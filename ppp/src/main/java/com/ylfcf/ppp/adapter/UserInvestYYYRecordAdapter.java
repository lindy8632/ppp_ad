package com.ylfcf.ppp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 元月盈用户投资记录
 * @author Mr.liu
 *
 */
public class UserInvestYYYRecordAdapter extends ArrayAdapter<InvestRecordInfo> {
	private static final int RESOURCE_ID = R.layout.invest_records_yyy_item;
	private Context context;
	private List<InvestRecordInfo> investRecordList = null;
	private LayoutInflater layoutInflater = null;
	private OnYYYItemClickListener onYYYItemClickListener = null;
	private String fromWhere;

	public UserInvestYYYRecordAdapter(Context context,OnYYYItemClickListener onYYYItemClickListener,String fromWhere) {
		super(context, RESOURCE_ID);
		this.context = context;
		this.onYYYItemClickListener = onYYYItemClickListener;
		this.fromWhere = fromWhere;
		investRecordList = new ArrayList<InvestRecordInfo>();
		layoutInflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * 对外方法，动态改变listview的item并进行刷新
	 * @param recordList
	 */
	public void setItems(List<InvestRecordInfo> recordList) {
		investRecordList.clear();
		if (recordList != null) {
			investRecordList.addAll(recordList);
		}
		this.notifyDataSetChanged();
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

	private int curPosition = 0;
	private InvestRecordInfo info = null;
	ViewHolder viewHolder = null;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		curPosition = position;
		info = investRecordList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.borrowName = (TextView) convertView
					.findViewById(R.id.invest_records_yyy_item_borrowname);
			viewHolder.investType = (TextView) convertView
					.findViewById(R.id.invest_records_yyy_item_investtype);
			viewHolder.firstTime = (TextView) convertView
					.findViewById(R.id.invest_records_yyy_item_firsttime);
			viewHolder.nearEndTime = (TextView) convertView
					.findViewById(R.id.invest_records_yyy_item_nearendtime);
			viewHolder.firstMoney = (TextView) convertView
					.findViewById(R.id.invest_record_yyy_item_firstmoney);
			viewHolder.nowMoney = (TextView) convertView
					.findViewById(R.id.invest_record_yyy_item_nowmoney);
			viewHolder.catCompactBtn = (Button) convertView
					.findViewById(R.id.invest_record_yyy_item_catcompact);
			viewHolder.applyOrCancelBtn = (Button) convertView
					.findViewById(R.id.invest_record_yyy_item_applyorcancel);
			viewHolder.interestMoney = (TextView) convertView.findViewById(R.id.invest_record_yyy_item_interestmoney);//投资收益
			viewHolder.remark = (TextView) convertView.findViewById(R.id.invest_records_yyy_item_remark);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.borrowName.setText("元月盈-第"+info.getBorrow_period()+"期");// 标名
		//setTag时的id必须是ResourceId
		viewHolder.catCompactBtn.setTag(R.id.tag_first,info);
		viewHolder.catCompactBtn.setTag(R.id.tag_second,curPosition);
		viewHolder.applyOrCancelBtn.setTag(R.id.tag_first,info);
		viewHolder.applyOrCancelBtn.setTag(R.id.tag_second,curPosition);
		if("投资中".equals(info.getReturn_status())){
			viewHolder.investType.setText(info.getInvest_status());
			viewHolder.investType.setTextColor(context.getResources().getColor(R.color.white));
			if("首投".equals(info.getInvest_status())){
				viewHolder.investType.setBackgroundResource(R.drawable.style_rect_fillet_filling_yellow);
			}else if("复投".equals(info.getInvest_status())){
				viewHolder.investType.setBackgroundResource(R.drawable.style_rect_fillet_filling_orange);
			}
		}else{
			viewHolder.investType.setText(info.getReturn_status());
			viewHolder.investType.setTextColor(context.getResources().getColor(R.color.gray));
			viewHolder.investType.setBackgroundResource(R.drawable.style_rect_fillet_filling_edit_white);
		}
		
		viewHolder.firstTime.setText("首投日: " + info.getFirst_borrow_time().split(" ")[0]);
		int times = 0;//投资次数，大于0表示复投
		long nowTime = System.currentTimeMillis();
		long interestStartTime = 0l;//起息时间
		try {
			interestStartTime = Util.string2date(info.getInterest_start_time());
		} catch (Exception e) {
		}
		try {
			times = Integer.parseInt(info.getInvest_times());
		} catch (Exception e) {
		}
		if(times > 0){
			//复投
			if(nowTime > interestStartTime){
				//表示已经起息
				viewHolder.nearEndTime.setText("最近到期日: "+info.getInterest_end_time().split(" ")[0]);
				viewHolder.catCompactBtn.setEnabled(true);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			}else{
				viewHolder.nearEndTime.setText("最近到期日:  — —");
				viewHolder.catCompactBtn.setEnabled(false);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			}
		}else{
			//首投
			if(info.getInterest_start_time() == null || "".equals(info.getInterest_start_time()) || "0000-00-00 00:00:00".equals(info.getInterest_start_time())){
				viewHolder.nearEndTime.setText("最近到期日:  — —");
				viewHolder.catCompactBtn.setEnabled(false);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			}else{
				viewHolder.nearEndTime.setText("最近到期日: "+info.getInterest_end_time().split(" ")[0]);
				viewHolder.catCompactBtn.setEnabled(true);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			}
		}
		double firstMoneyD = 0d;
		try {
			firstMoneyD = Double.parseDouble(info.getMoney());
			if(firstMoneyD < 10000){
				viewHolder.firstMoney.setText(Util.double2PointDouble(firstMoneyD) + "元");
			}else{
				viewHolder.firstMoney.setText(Util.double2PointDouble(firstMoneyD/10000) + "万元");
			}
		} catch (Exception e) {
		}
		double interestMoneyD = 0d;
		double investMoneyD = 0d;
		double moneyD = 0d;
		try {
			interestMoneyD = Double.parseDouble(info.getInvest_interest());
		} catch (Exception e) {
		}
		try{
			investMoneyD = Double.parseDouble(info.getInvest_money());
			moneyD = Double.parseDouble(info.getMoney());
		}catch (Exception e){

		}
		if("已赎回".equals(info.getReturn_status())){
			viewHolder.nowMoney.setText("0.00元");
			viewHolder.interestMoney.setText(Util.double2PointDouble(investMoneyD - moneyD) + "元");
		}else{
			viewHolder.nowMoney.setText(info.getInvest_money() + "元");
			viewHolder.interestMoney.setText(Util.double2PointDouble(interestMoneyD) + "元");
		}
		double interestAddD = 0d;
		try {
			interestAddD = Double.parseDouble(info.getInterest_add());
		} catch (Exception e) {
		}
		if(info == null || info.getInterest_add().isEmpty() || interestAddD == 0){
			viewHolder.remark.setVisibility(View.GONE);
			viewHolder.remark.setText("备注: — —");
		}else{
			viewHolder.remark.setVisibility(View.VISIBLE);
			viewHolder.remark.setText("备注: 首投加息" + info.getInterest_add() + "%");
		}
		if("投资中".equals(info.getReturn_status())){
			boolean isReturn = Util.isReturnYYY(info);
			if(isReturn){
				viewHolder.applyOrCancelBtn.setEnabled(true);
				viewHolder.applyOrCancelBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			}else{
				viewHolder.applyOrCancelBtn.setEnabled(false);
				viewHolder.applyOrCancelBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			}
			viewHolder.applyOrCancelBtn.setText("预约赎回");
			
		}else if("已赎回".equals(info.getReturn_status())){
			//已赎回
			viewHolder.applyOrCancelBtn.setText("撤销赎回");
			viewHolder.applyOrCancelBtn.setEnabled(false);
			viewHolder.applyOrCancelBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			
			viewHolder.investType.setText("已赎回");
			viewHolder.investType.setTextColor(context.getResources().getColor(R.color.white));
			viewHolder.investType.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
		}else if("赎回中".equals(info.getReturn_status())){
			viewHolder.applyOrCancelBtn.setText("撤销赎回");
			viewHolder.applyOrCancelBtn.setEnabled(true);
			viewHolder.applyOrCancelBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			
			viewHolder.investType.setText("待收");
			viewHolder.investType.setTextColor(context.getResources().getColor(R.color.gray));
			viewHolder.investType.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
		}
		
		if (position == 0) {
			convertView.setPadding(0, context.getResources()
					.getDimensionPixelSize(R.dimen.common_measure_15dp), 0, 0);
		}
		if("yyy_zjmx".equals(fromWhere)){
			//元月盈资金明细
			viewHolder.catCompactBtn.setVisibility(View.GONE);
			viewHolder.applyOrCancelBtn.setVisibility(View.GONE);
		}else if("yyy_record".equals(fromWhere)){
			//元月盈投资记录
			viewHolder.catCompactBtn.setVisibility(View.VISIBLE);
			viewHolder.applyOrCancelBtn.setVisibility(View.VISIBLE);
		}
		viewHolder.catCompactBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onYYYItemClickListener != null)
				onYYYItemClickListener.onCatCompact((Integer)v.getTag(R.id.tag_second), v,
						(InvestRecordInfo)v.getTag(R.id.tag_first));
			}
		});
		viewHolder.applyOrCancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onYYYItemClickListener != null)
				onYYYItemClickListener.onApplyOrCancel((Integer)v.getTag(R.id.tag_second), v, 
						(InvestRecordInfo)v.getTag(R.id.tag_first));
			}
		});
		return convertView;
	}

	/**
	 * 内部类，定义Item的元素
	 * @author Mr.liu
	 *
	 */
	class ViewHolder {
		TextView borrowName;//标的名字
		TextView investType;//首投 ， 复投等等
		TextView firstTime;//首投日
		TextView nearEndTime;// 最近到期日
		TextView firstMoney;// 首投本金
		TextView nowMoney;// 在投金额
		TextView interestMoney;//投资收益
		TextView remark;
		Button catCompactBtn;// 查看合同
		Button applyOrCancelBtn;//预约或者取消赎回
	}
	
	/**
	 * 元月盈用户投资记录页面，当用户点击“查看合同”和“预约赎回"按钮时的回调方法
	 * @author Mr.liu
	 *
	 */
	public interface OnYYYItemClickListener{
		/*
		 * 查看合同
		 */
		void onCatCompact(int position,View v,InvestRecordInfo info);
		/*
		 * 预约或者撤销赎回
		 */
		void onApplyOrCancel(int position,View v,InvestRecordInfo info);
	}
}
