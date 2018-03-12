package com.ylfcf.ppp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.ui.CompactActivity;
import com.ylfcf.ppp.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户投资记录
 * 
 * @author Administrator
 * 
 */
public class UserInvestRecordAdapter extends ArrayAdapter<InvestRecordInfo> {
	private static final int RESOURCE_ID = R.layout.invest_records_item;
	private Context context;
	private List<InvestRecordInfo> investRecordList = null;
	private LayoutInflater layoutInflater = null;
	private String fromWhere = "";//vip,dqlc等等产品

	public UserInvestRecordAdapter(Context context,String fromWhere) {
		super(context, RESOURCE_ID);
		this.context = context;
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
		InvestRecordInfo info = investRecordList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.borrowName = (TextView) convertView
					.findViewById(R.id.invest_records_item_borrowname);
			viewHolder.borrowLogo = (ImageView) convertView.findViewById(R.id.invest_records_item_borrowlogo);
			viewHolder.startTime = (TextView) convertView
					.findViewById(R.id.invest_records_item_starttime);
			viewHolder.endTime = (TextView) convertView
					.findViewById(R.id.invest_records_item_endtime);
			viewHolder.rate = (TextView) convertView
					.findViewById(R.id.invest_record_item_rate);
			viewHolder.addRate = (TextView) convertView
					.findViewById(R.id.invest_record_item_add_rate);
			viewHolder.investMoney = (TextView) convertView
					.findViewById(R.id.invest_record_item_invest_money);
			viewHolder.interestMoney = (TextView) convertView
					.findViewById(R.id.invest_record_item_interest_money);
			viewHolder.status = (TextView) convertView
					.findViewById(R.id.invest_records_item_status);
			viewHolder.addLayout = (LinearLayout) convertView.findViewById(R.id.invest_records_item_add_layout);
			viewHolder.nhsyText = (TextView) convertView.findViewById(R.id.invest_records_item_nhsy_text);
			viewHolder.catCompactBtn = (Button) convertView.findViewById(R.id.invest_record_item_catcompact);
			viewHolder.remarkLayout = (LinearLayout) convertView.findViewById(R.id.invest_records_item_remark_layout);
			viewHolder.remark = (TextView) convertView.findViewById(R.id.invest_record_item_interest_remark);
			viewHolder.interestTextTitle = (TextView) convertView.findViewById(R.id.invest_record_item_interest_money_title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.borrowName.setText(info.getBorrow_name());// 标名
		double interestRateD = 0d;//基础利率
		double interestRateFloat = 0d;//浮动利率
		double couponRateD = 0d;//加息券加息
		double interestD = 0d;//首投加息利率
		double androidInterestRateD = 0d;
		try {
			androidInterestRateD = Double.parseDouble(info.getAndroid_interest_rate());
		} catch (Exception e) {
		}
		try {
			interestRateD = Double.parseDouble(info.getInterest_rate());
		} catch (Exception e) {
		}
		try {
			interestRateFloat = Double.parseDouble(info.getInterest_add());
		} catch (Exception e) {
		}
		try {
			couponRateD = Double.parseDouble(info.getCoupon_interest_add());
		} catch (Exception e) {
		}
		try {
			interestD = Double.parseDouble(info.getInterest().replace("%", ""));
		} catch (Exception e) {
		}
		viewHolder.rate.setText(Util.formatRate(String.valueOf(interestRateD + interestRateFloat))+ "%");//年化收益
		double investMoneyD = 0d;
		try {
			investMoneyD = Double.parseDouble(info.getFact_money());
			viewHolder.investMoney.setText((int)investMoneyD + "元");
		} catch (Exception e) {
		}
		viewHolder.status.setText("投资状态:" + info.getStatus());
		viewHolder.catCompactBtn.setTag(info);
		if("yzy".equals(fromWhere)){
			if("新手标".equals(info.getBorrow_type())){
				viewHolder.borrowLogo.setVisibility(View.VISIBLE);
			}else{
				viewHolder.borrowLogo.setVisibility(View.GONE);
			}
			if(info.getAdd_time() == null || "".equals(info.getAdd_time()) || "0000-00-00 00:00:00".equals(info.getAdd_time())){
				viewHolder.startTime.setText("投标日期:  — —");
			}else{
				viewHolder.startTime.setText("投标日期: " + info.getAdd_time().split(" ")[0]);
			}
			if(info.getEnd_time() == null || "".equals(info.getEnd_time()) || "0000-00-00 00:00:00".equals(info.getEnd_time())){
				viewHolder.endTime.setText("到期时间:  — —");
				viewHolder.catCompactBtn.setEnabled(false);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			}else{
				viewHolder.endTime.setText("到期时间: " + info.getEnd_time().split(" ")[0]);
				viewHolder.catCompactBtn.setEnabled(true);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			}
			viewHolder.nhsyText.setText("年化利率");
			viewHolder.addLayout.setVisibility(View.VISIBLE);
			viewHolder.remarkLayout.setVisibility(View.GONE);
			viewHolder.catCompactBtn.setVisibility(View.VISIBLE);			
			//加息
			double sumInterestD = 0d;
			try {
				sumInterestD = Double.parseDouble(info.getSum_interest());                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
			} catch (Exception e) {
			}
			viewHolder.rate.setText(Util.formatRate(String.valueOf(interestRateD + interestRateFloat))+ "%");//年化收益
			if(couponRateD + interestD <= 0){
				viewHolder.addRate.setText("— —");
			}else{
				viewHolder.addRate.setText(Util.formatRate(String.valueOf(couponRateD + interestD))+"%");//加息奖励
			}
			int interestPeroidI = 0;//投资期限
			try {
				interestPeroidI = Integer.parseInt(info.getInterest_period());
			} catch (Exception e) {
			}
			double totalInvestMoneyD = 0d;
			try {
				totalInvestMoneyD = Double.parseDouble(info.getMoney());
			} catch (Exception e) {
			}
			double redbagMoneyD = 0d;
			try{
				redbagMoneyD = Double.parseDouble(info.getRed_bag_money());//红包金额
			}catch (Exception e){

			}
			double totalRate = interestRateD + interestRateFloat + couponRateD + interestD;//基础利率+浮动利率+加息利率+加息券
			double totalInterestD = totalInvestMoneyD * totalRate * interestPeroidI / 36500 + redbagMoneyD * interestRateD * interestPeroidI / 36500;
			if(sumInterestD == 0){
				//还没有跑满标脚本，自己计算
				viewHolder.interestMoney.setText(Util.formatRate(Util.double2PointDouble(totalInterestD)) + "元");//投资收益
			}else{
				//已跑满标脚本，直接取值
				viewHolder.interestMoney.setText(Util.formatRate(info.getSum_interest()) + "元");//投资收益
			}
		}else if("vip".equals(fromWhere)){
			if(info.getInvest_start_time() == null || "".equals(info.getInvest_start_time()) || "0000-00-00 00:00:00".equals(info.getInvest_start_time())){
				viewHolder.startTime.setText("投标日期:  — —");
			}else{
				viewHolder.startTime.setText("投标日期: " + info.getInvest_start_time().split(" ")[0]);
			}
			if(info.getInvest_end_time() == null || "".equals(info.getInvest_end_time()) || "0000-00-00 00:00:00".equals(info.getInvest_end_time())){
				viewHolder.endTime.setText("到期时间:  — —");
				viewHolder.catCompactBtn.setEnabled(false);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			}else{
				viewHolder.endTime.setText("到期时间: " + info.getInvest_end_time().split(" ")[0]);
				viewHolder.catCompactBtn.setEnabled(true);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			}
			viewHolder.nhsyText.setText("业绩比较基准");
			viewHolder.catCompactBtn.setVisibility(View.VISIBLE);
			try {
				viewHolder.interestMoney.setText(Util.formatRate(info.getInterest())+ "元");
			} catch (Exception e) {
				viewHolder.interestMoney.setText("0元");
			}
			viewHolder.addLayout.setVisibility(View.VISIBLE);
			viewHolder.remarkLayout.setVisibility(View.GONE);
			if(androidInterestRateD + couponRateD <= 0){
				viewHolder.addRate.setText("— —");
			}else{
				viewHolder.addRate.setText(Util.formatRate(String.valueOf(androidInterestRateD + couponRateD))+"%");
			}
			try {
				investMoneyD = Double.parseDouble(info.getMoney());
				viewHolder.investMoney.setText((int)investMoneyD + "元");
			} catch (Exception e) {
			}
		}else if("srzx".equals(fromWhere)){
			if(info.getInvest_time() == null || "".equals(info.getInvest_time()) || "0000-00-00 00:00:00".equals(info.getInvest_time())){
				viewHolder.startTime.setText("投标日期:  — —");
			}else{
				viewHolder.startTime.setText("投标日期: " + info.getInvest_time().split(" ")[0]);
			}
			if(info.getInterest_end_time() == null || "".equals(info.getInterest_end_time()) || "0000-00-00 00:00:00".equals(info.getInterest_end_time())){
				viewHolder.endTime.setText("到期时间:  — —");
				viewHolder.catCompactBtn.setEnabled(false);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			}else{
				viewHolder.endTime.setText("到期时间: " + info.getInterest_end_time().split(" ")[0]);
				viewHolder.catCompactBtn.setEnabled(true);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			}
			viewHolder.nhsyText.setText("年化利率");
			viewHolder.addLayout.setVisibility(View.VISIBLE);
			viewHolder.remarkLayout.setVisibility(View.GONE);
			viewHolder.catCompactBtn.setVisibility(View.VISIBLE);
			double interestSRZX = 0d;
			double hbInterestSRZX = 0d;
			try{
				interestSRZX = Double.parseDouble(info.getInterest());
				hbInterestSRZX = Double.parseDouble(info.getRed_bag_interest());
			}catch (Exception e){

			}
			viewHolder.interestMoney.setText(Util.formatRate(String.valueOf(interestSRZX + hbInterestSRZX)) + "元");
			viewHolder.status.setText("投资状态: " + info.getInvest_status());
			//加息
			viewHolder.rate.setText(Util.formatRate(String.valueOf(interestRateD))+ "%");//年化收益
			if(androidInterestRateD + interestRateFloat + couponRateD <= 0){
				viewHolder.addRate.setText("— —");
			}else{
				viewHolder.addRate.setText(Util.formatRate(String.valueOf(androidInterestRateD + interestRateFloat + couponRateD))+"%");
			}
			try {
				investMoneyD = Double.parseDouble(info.getMoney());
				viewHolder.investMoney.setText((int)investMoneyD + "元");
			} catch (Exception e) {
			}
		}else if("yjy".equals(fromWhere)){
			//元聚盈
			if(info.getInvest_time() == null || "".equals(info.getInvest_time()) || "0000-00-00 00:00:00".equals(info.getInvest_time())){
				viewHolder.startTime.setText("投标日期:  — —");
			}else{
				viewHolder.startTime.setText("投标日期: " + info.getInvest_time().split(" ")[0]);
			}
			if(info.getInterest_end_time() == null || "".equals(info.getInterest_end_time()) || "0000-00-00 00:00:00".equals(info.getInterest_end_time())){
				viewHolder.endTime.setText("到期时间:  — —");
				viewHolder.catCompactBtn.setEnabled(false);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			}else{
				viewHolder.endTime.setText("到期时间: " + info.getInterest_end_time().split(" ")[0]);
				viewHolder.catCompactBtn.setEnabled(true);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			}
			viewHolder.nhsyText.setText("年化利率");
			viewHolder.addLayout.setVisibility(View.GONE);
			viewHolder.remarkLayout.setVisibility(View.VISIBLE);
			viewHolder.catCompactBtn.setVisibility(View.VISIBLE);
			viewHolder.interestTextTitle.setText("投资总收益");
			double interestSRZX = 0d;
			double hbInterestSRZX = 0d;
			try{
				interestSRZX = Double.parseDouble(info.getInterest());
				hbInterestSRZX = Double.parseDouble(info.getRed_bag_interest());
			}catch (Exception e){

			}
			viewHolder.interestMoney.setText(Util.formatRate(String.valueOf(interestSRZX + hbInterestSRZX)) + "元");
			viewHolder.status.setText("投资状态: " + info.getInvest_status());
			//加息
			viewHolder.rate.setText(Util.formatRate(String.valueOf(interestRateD))+ "%");//年化收益
			try {
				investMoneyD = Double.parseDouble(info.getMoney());
				viewHolder.investMoney.setText((int)investMoneyD + "元");
			} catch (Exception e) {
			}
		}else if("xsmb".equals(fromWhere)){
			if(info.getInvest_time() == null || "".equals(info.getInvest_time()) || "0000-00-00 00:00:00".equals(info.getInvest_time())){
				viewHolder.startTime.setText("投标日期:  — —");
			}else{
				viewHolder.startTime.setText("投标日期: " + info.getInvest_time().split(" ")[0]);
			}
			if(info.getInterest_end_time() == null || "".equals(info.getInterest_end_time()) || "0000-00-00 00:00:00".equals(info.getInterest_end_time())){
				viewHolder.endTime.setText("到期时间:  — —");
				viewHolder.catCompactBtn.setEnabled(false);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			}else{
				viewHolder.endTime.setText("到期时间: " + info.getInterest_end_time().split(" ")[0]);
				viewHolder.catCompactBtn.setEnabled(true);
				viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			}
			viewHolder.nhsyText.setText("年化利率");
			viewHolder.addLayout.setVisibility(View.GONE);
			viewHolder.remarkLayout.setVisibility(View.VISIBLE);
			viewHolder.catCompactBtn.setVisibility(View.VISIBLE);
			viewHolder.status.setText("投资状态: " + info.getInvest_status());
			int interestDays = 0;
			try {
				interestDays = Integer.parseInt(info.getInterest_days());
			} catch (Exception e) {
			}
			viewHolder.interestMoney.setText(Util.formatRate(Util.double2PointDouble(investMoneyD * interestRateD /100 * interestDays / 365)) + "元");
			viewHolder.rate.setText(Util.formatRate(String.valueOf(interestRateD + interestRateFloat)) + "%");

			try {
				investMoneyD = Double.parseDouble(info.getMoney());
				viewHolder.investMoney.setText((int)investMoneyD + "元");
			} catch (Exception e) {
			}
		}
		viewHolder.catCompactBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InvestRecordInfo recordInfo = (InvestRecordInfo) v.getTag();
				Intent intent = new Intent(context,CompactActivity.class);
				intent.putExtra("invest_record", recordInfo);
				intent.putExtra("from_where", fromWhere);
				context.startActivity(intent);
			}
		});
		if (position == 0) {
			convertView.setPadding(0, context.getResources()
					.getDimensionPixelSize(R.dimen.common_measure_15dp), 0, 0);
		}else{
			convertView.setPadding(0, 0, 0, 0);
		}
		return convertView;
	}

	/**
	 * 内部类，定义Item的元素
	 * @author Mr.liu
	 *
	 */
	class ViewHolder {
		TextView borrowName;
		ImageView borrowLogo;
		TextView startTime;
		TextView endTime;
		TextView rate;// 年化利率
		TextView addRate;// 加息奖励
		TextView investMoney;// 投资金额
		TextView interestMoney;// 收益
		TextView status;
		TextView nhsyText;//年化收益四个字在VIP产品中要改成“业绩比较基准”
		TextView interestTextTitle;
		LinearLayout addLayout;//加息奖励的布局
		LinearLayout remarkLayout;//备注
		TextView remark;//备注
		Button catCompactBtn;
	}
}
