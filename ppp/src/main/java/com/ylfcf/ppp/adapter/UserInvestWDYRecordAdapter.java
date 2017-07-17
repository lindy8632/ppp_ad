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
import com.ylfcf.ppp.entity.WDYChildRecordInfo;
import com.ylfcf.ppp.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
/**
 * 稳定赢
 * @author Mr.liu
 *
 */
public class UserInvestWDYRecordAdapter extends ArrayAdapter<InvestRecordInfo>{
	private static final int RESOURCE_ID = R.layout.invest_lczq_records_item;
	private Context context;
	private List<InvestRecordInfo> investRecordList = null;
	private LayoutInflater layoutInflater = null;
	private OnWDYItemClickListener onWDYItemClickListener = null;
	private String fromWhere;
	private String systemTime = "";
	long systemTimeL = 0l;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public UserInvestWDYRecordAdapter(Context context,OnWDYItemClickListener onWDYItemClickListener,String fromWhere) {
		super(context, RESOURCE_ID);
		this.context = context;
		this.onWDYItemClickListener = onWDYItemClickListener;
		this.fromWhere = fromWhere;
		investRecordList = new ArrayList<InvestRecordInfo>();
		layoutInflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * 对外方法，动态改变listview的item并进行刷新
	 * @param recordList
	 */
	public void setItems(List<InvestRecordInfo> recordList,String systemTime) {
		this.systemTime = systemTime;
		investRecordList.clear();
		if (recordList != null) {
			investRecordList.addAll(recordList);
		}
		try {
			systemTimeL = sdf1.parse(systemTime).getTime();
		} catch (Exception e) {
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
					.findViewById(R.id.invest_records_lczq_item_borrowname);
			viewHolder.investType = (TextView) convertView
					.findViewById(R.id.invest_records_lczq_item_investtype);
			viewHolder.firstTime = (TextView) convertView
					.findViewById(R.id.invest_records_lczq_item_firsttime);
			viewHolder.nextAddTime = (TextView) convertView
					.findViewById(R.id.invest_records_lczq_item_nexttime);
			viewHolder.firstMoney = (TextView) convertView
					.findViewById(R.id.invest_record_lczq_item_firstmoney);
			viewHolder.totalBidMoney = (TextView) convertView
					.findViewById(R.id.invest_record_lczq_item_totalbidmoney);
			viewHolder.totalYQDays = (TextView) convertView
					.findViewById(R.id.invest_record_lczq_item_yanqi_days);
			viewHolder.interestMoneyTitle = (TextView) convertView
					.findViewById(R.id.invest_lczq_records_item_interestmoney_title);
			viewHolder.interestMoney = (TextView) convertView
					.findViewById(R.id.invest_record_lczq_item_interestmoney);
			viewHolder.remark = (TextView) convertView
					.findViewById(R.id.invest_records_lczq_item_remark);
			viewHolder.catCompactBtn = (Button) convertView
					.findViewById(R.id.invest_record_lczq_item_catcompact);
			viewHolder.catBidRecords = (Button) convertView.
					findViewById(R.id.invest_record_lczq_item_catrecord);
			viewHolder.nhllTitleTV = (TextView) convertView
					.findViewById(R.id.invest_lczq_records_item_nhlltitle);
			viewHolder.nhllTV = (TextView) convertView
					.findViewById(R.id.invest_lczq_records_item_nhll);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.borrowName.setText("薪盈计划-第"+info.getBorrow_period()+"期");// 标名
		//setTag时的id必须是ResourceId
		viewHolder.catCompactBtn.setTag(R.id.tag_first,info);
		viewHolder.catCompactBtn.setTag(R.id.tag_second,curPosition);
		viewHolder.catBidRecords.setTag(R.id.tag_first,info);
		viewHolder.catBidRecords.setTag(R.id.tag_second,curPosition);
		if("投资中".equals(info.getStatus())){
			viewHolder.nhllTitleTV.setText("本期预期年化收益率");
			viewHolder.investType.setText("在投");
			viewHolder.investType.setBackgroundResource(R.drawable.style_rect_fillet_filling_yellow);
			viewHolder.interestMoneyTitle.setText("预期收益");
			if(info.getInterest_start_time() != null && !"0000-00-00 00:00:00".equals(info.getInterest_start_time())){
				//起息了
				if(!"一 一".equals(getTodayNextTime(info.getWdyChildRecordList()))){
					try {
						viewHolder.nextAddTime.setText("下期加入日："+getTodayNextTime(info.getWdyChildRecordList()).split(" ")[0]);
					} catch (Exception e) {
						viewHolder.nextAddTime.setText("下期加入日：一 一");
					}
				}else{
					viewHolder.nextAddTime.setText("下期加入日："+getTodayNextTime(info.getWdyChildRecordList()));
				}
				
				
				if(info.getWdy_pro_interest() != null){
					try {
						viewHolder.interestMoney.setText(Util.double2PointDouble(Double.parseDouble(info.getWdy_pro_interest()))+"元");
					} catch (Exception e) {
						viewHolder.interestMoney.setText(info.getWdy_pro_interest()+"元");
					}
				}else{
					viewHolder.interestMoney.setText("0元");
				}
			}else{
				viewHolder.nextAddTime.setText("下期加入日：一 一");
				viewHolder.interestMoney.setText("0元");
			}
		}else if("已还款".equals(info.getStatus())){
			viewHolder.nhllTitleTV.setText("本期预期年化收益率");
			viewHolder.investType.setText(info.getStatus());
			viewHolder.investType.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			viewHolder.nextAddTime.setText("还款日期："+info.getInterest_end_time().split(" ")[0]);
			viewHolder.interestMoneyTitle.setText("实际收益");
			if(info.getWdy_real_interest() != null){
				try {
					viewHolder.interestMoney.setText(Util.double2PointDouble(Double.parseDouble(info.getWdy_real_interest()))+"元");
				} catch (Exception e) {
					viewHolder.interestMoney.setText(info.getWdy_pro_interest()+"元");
				}
			}else{
				viewHolder.interestMoney.setText(info.getWdy_pro_interest()+"元");
			}
		}

		double baseRateD = 0d;
		double addRateD = 0d;
		try{
			baseRateD = Double.parseDouble(info.getInterest_rate());
			addRateD = Double.parseDouble(info.getCoupon_interest_add());
		}catch (Exception e){
			e.printStackTrace();
		}
		if("1".equals(info.getPeriod())){
			//首期
			viewHolder.nhllTV.setText(Util.double2PointDouble(baseRateD+addRateD)+"%");
		}else{
			viewHolder.nhllTV.setText(Util.double2PointDouble(baseRateD)+"%");
		}
		try {
			viewHolder.firstTime.setText("首投日：" + info.getAdd_time().split(" ")[0]);
		} catch (Exception e) {
			viewHolder.firstTime.setText("首投日：一 一");
		}
		double firstMoney = 0d;
		double totalBidMoney = 0d;
		try {
			firstMoney = Double.parseDouble(info.getMoney());
			totalBidMoney = Double.parseDouble(info.getTotal_money());
			viewHolder.firstMoney.setText((int)firstMoney+"元");
			viewHolder.totalBidMoney.setText((int)totalBidMoney+"元");
		} catch (Exception e) {
			viewHolder.firstMoney.setText(info.getMoney()+"元");
			viewHolder.totalBidMoney.setText(info.getTotal_money()+"元");
		}
		if(addRateD > 0){
			viewHolder.remark.setText("首期共加息"+Util.double2PointDouble(addRateD)+"%");
		}else{
			viewHolder.remark.setText("一 一");
		}
		if (position == 0) {
			convertView.setPadding(0, context.getResources()
					.getDimensionPixelSize(R.dimen.common_measure_15dp), 0, 0);
		}
		if("0".equals(info.getIs_generated_records())){
			//不可点击
			viewHolder.catBidRecords.setEnabled(false);
			viewHolder.catBidRecords.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
		}else if("1".equals(info.getIs_generated_records())){
			viewHolder.catBidRecords.setEnabled(true);
			viewHolder.catBidRecords.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
		}
		if(info.getInterest_start_time() != null && !"0000-00-00 00:00:00".equals(info.getInterest_start_time())){
			//表示起息了
			viewHolder.catCompactBtn.setEnabled(true);
			viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
			
			if(info.getTotalDelay() != null){
				viewHolder.totalYQDays.setText(info.getTotalDelay()+"天");
			}else{
				viewHolder.totalYQDays.setText("0天");
			}
		}else{
			//还没起息
			viewHolder.catCompactBtn.setEnabled(false);
			viewHolder.catCompactBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
			
			viewHolder.totalYQDays.setText("0天");
		}
		viewHolder.catCompactBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onWDYItemClickListener != null)
					onWDYItemClickListener.onCatCompact((Integer)v.getTag(R.id.tag_second), v,
						(InvestRecordInfo)v.getTag(R.id.tag_first));
			}
		});
		viewHolder.catBidRecords.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onWDYItemClickListener != null)
					onWDYItemClickListener.onCatBidRecord((Integer)v.getTag(R.id.tag_second), v, 
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
		TextView investType;//状态
		TextView firstTime;//首投日
		TextView nextAddTime;// 下期加入日
		TextView firstMoney;// 首投本金
		TextView totalBidMoney;//累计出借
		TextView totalYQDays;//累计延期	
		TextView interestMoneyTitle;//收益的标题
		TextView interestMoney;//投资收益
		TextView remark;
		Button catCompactBtn;// 查看合同
		Button catBidRecords;//查看出借记录
		TextView nhllTitleTV;
		TextView nhllTV;
	}
	
	/**
	 * 元月盈用户投资记录页面，当用户点击“查看合同”和“预约赎回"按钮时的回调方法
	 * @author Mr.liu
	 *
	 */
	public interface OnWDYItemClickListener{
		/*
		 * 查看合同
		 */
		void onCatCompact(int position,View v,InvestRecordInfo info);
		/*
		 * 查看出借记录
		 */
		void onCatBidRecord(int position,View v,InvestRecordInfo info);
		/*
		 * 支付本期出借金额
		 */
		void onBidCurrentPeroid(int position,View v,InvestRecordInfo info);
	}
	
	/**
	 * 如果日期是今天，则下期加入日不正确，要自己算。显示的是下一期的nexttime
	 */
	private String getTodayNextTime(List<WDYChildRecordInfo> childRecordList){
		if(childRecordList == null || childRecordList.size() == 0){
			return "";
		}
		String todayNextTime = "";
		long planAddTimeL = 0l;
		for(int i=0;i<childRecordList.size();i++){
			WDYChildRecordInfo info = childRecordList.get(i);
			try {
				planAddTimeL = sdf1.parse(info.getPlan_add_time()).getTime();
			} catch (Exception e) {
			}
			if(systemTimeL < planAddTimeL){
				todayNextTime = info.getPlan_add_time();
				break;
			}
			if("".equals(todayNextTime)){
				//说明是最后一期
				todayNextTime = "一 一";
			}
		}
		return todayNextTime;
	}
}
