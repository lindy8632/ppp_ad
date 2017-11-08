package com.ylfcf.ppp.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.PrizeInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 我的礼物
 * @author Administrator
 * 
 */
public class PrizeAdapter extends ArrayAdapter<PrizeInfo> {
	private static final int RESOURCE_ID = R.layout.prize_listview_item;
	private final LayoutInflater mInflater;
	
	private List<PrizeInfo> prizeList;
	private Context context;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private OnItemMRCJListener mOnItemMRCJListener;

	public PrizeAdapter(Context context,OnItemMRCJListener mOnItemMRCJListener) {
		super(context, RESOURCE_ID);
		this.context = context;
		this.mOnItemMRCJListener = mOnItemMRCJListener;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		prizeList = new ArrayList<PrizeInfo>();
	}

	/**
	 * 对外方法，动态改变listview的item并进行刷新
	 * @param prizeList
	 */
	public void setItems(List<PrizeInfo> prizeList) {
		this.prizeList.clear();
		if (prizeList != null) {
			this.prizeList.addAll(prizeList);
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return prizeList.size();
	}

	@Override
	public PrizeInfo getItem(int position) {
		return prizeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 设置具体一个标的的显示内容
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		final PrizeInfo info = prizeList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(RESOURCE_ID, null);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.prize_listview_item_title);
			viewHolder.ticket = (TextView) convertView
					.findViewById(R.id.prize_listview_item_ticket);
			viewHolder.endTime = (TextView) convertView
					.findViewById(R.id.prize_listview_item_endtime);
			viewHolder.moneyText = (TextView) convertView
					.findViewById(R.id.prize_listview_item_money);
			viewHolder.unitText = (TextView) convertView
					.findViewById(R.id.prize_listview_item_unit);
			viewHolder.remark = (TextView) convertView.
					findViewById(R.id.prize_listview_item_remark);
			viewHolder.catDetails = (Button)convertView.
					findViewById(R.id.prize_listview_item_catdetails_btn);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if(info.getOperating_remark() != null && !"".equals(info.getOperating_remark())){
			viewHolder.remark.setText("备注："+info.getOperating_remark());	
		}else{
			viewHolder.remark.setText("备注：--");
		}
		if(info.getOperating_remark().contains("转盘") && !info.getOperating_remark().contains("中秋")){
			//幸运转盘
			viewHolder.ticket.setVisibility(View.GONE);
			viewHolder.moneyText.setVisibility(View.GONE);
			viewHolder.catDetails.setVisibility(View.GONE);
			viewHolder.endTime.setVisibility(View.VISIBLE);
			if(info.getPrize().contains("转盘机会")){
				viewHolder.title.setText(info.getOperating_remark()+info.getGet_nums()+"次");
				viewHolder.endTime.setText("获取时间："+info.getSend_time());
				viewHolder.unitText.setVisibility(View.VISIBLE);
				viewHolder.unitText.setText("当日有效");
			}else if(info.getPrize().contains("话费")){
				viewHolder.title.setText(info.getPrize());
				viewHolder.endTime.setText("奖品于活动结束7个工作日内统计发放");
				viewHolder.unitText.setVisibility(View.GONE);
			}
		}else if(info.getOperating_remark().contains("转盘") && info.getOperating_remark().contains("中秋")){
			//2016年中秋大转盘活动
			viewHolder.ticket.setVisibility(View.GONE);
			viewHolder.moneyText.setVisibility(View.GONE);
			viewHolder.unitText.setVisibility(View.GONE);
			viewHolder.catDetails.setVisibility(View.GONE);
			viewHolder.endTime.setVisibility(View.VISIBLE);
			viewHolder.title.setText(info.getPrize());
			viewHolder.endTime.setText("获取时间："+info.getAdd_time());
		}else if(info.getOperating_remark().contains("周年感恩回馈") && info.getOperating_remark().contains("返现")){
			//2016两周年感恩反馈活动
			viewHolder.ticket.setVisibility(View.GONE);
			viewHolder.moneyText.setVisibility(View.GONE);
			viewHolder.unitText.setVisibility(View.GONE);
			viewHolder.endTime.setVisibility(View.GONE);
			viewHolder.catDetails.setVisibility(View.GONE);
			viewHolder.title.setText(info.getPrize());
			viewHolder.remark.setText(info.getOperating_remark()+"(账户中心可查看)");
		}else if("HYFL_01".equals(info.getActive_title())){
			//会员福利计划活动
			viewHolder.ticket.setVisibility(View.VISIBLE);
			viewHolder.ticket.setText("兑换码："+info.getPrize_code());
			viewHolder.title.setText(info.getName());
			viewHolder.endTime.setVisibility(View.GONE);
			viewHolder.remark.setVisibility(View.VISIBLE);
			viewHolder.remark.setText("备注："+info.getOperating_remark());
			viewHolder.moneyText.setVisibility(View.GONE);
			viewHolder.unitText.setVisibility(View.GONE);
			viewHolder.catDetails.setVisibility(View.GONE);
		}else if("HYFL_02".equals(info.getActive_title())){
			viewHolder.catDetails.setVisibility(View.GONE);
			//会员福利02期
			if(info.getPrize_code() != null && !"".equals(info.getPrize_code())){
				viewHolder.ticket.setVisibility(View.VISIBLE);
				viewHolder.ticket.setText("兑换码："+info.getPrize_code());
				viewHolder.title.setText(info.getName());
				viewHolder.endTime.setVisibility(View.GONE);
				viewHolder.remark.setVisibility(View.VISIBLE);
				viewHolder.remark.setText("备注："+info.getOperating_remark());
				viewHolder.moneyText.setVisibility(View.GONE);
				viewHolder.unitText.setVisibility(View.GONE);
			}else{
				viewHolder.ticket.setVisibility(View.VISIBLE);
				viewHolder.ticket.setText("兑换码：券码是您在元立方平台注册的手机号码");
				viewHolder.title.setText(info.getName());
				viewHolder.endTime.setVisibility(View.GONE);
				viewHolder.remark.setVisibility(View.VISIBLE);
				viewHolder.remark.setText("备注："+info.getOperating_remark());
				viewHolder.moneyText.setVisibility(View.GONE);
				viewHolder.unitText.setVisibility(View.GONE);
			}
			SpannableStringBuilder builder = new SpannableStringBuilder(viewHolder.ticket.getText().toString());  
			ForegroundColorSpan redSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.orange_text)); 
			builder.setSpan(redSpan, 4, viewHolder.ticket.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
			viewHolder.ticket.setText(builder);  
		}else if("YXHB_01".equals(info.getActive_title())){
			//2017年新春领红包
			viewHolder.moneyText.setVisibility(View.GONE);
			viewHolder.unitText.setVisibility(View.GONE);
			viewHolder.ticket.setVisibility(View.GONE);
			viewHolder.catDetails.setVisibility(View.GONE);
			viewHolder.endTime.setVisibility(View.VISIBLE);
			viewHolder.remark.setVisibility(View.VISIBLE);
			viewHolder.title.setText(info.getPrize());
			if(!info.getOperating_remark().isEmpty()){
				viewHolder.remark.setText("备注："+info.getOperating_remark());
			}else{
				viewHolder.remark.setText("备注：一 一");
			}
			if(info.getPrize().contains("元金币") || info.getPrize().contains("现金") || info.getPrize().contains("领取机会")){
				viewHolder.endTime.setText("领取时间："+info.getAdd_time());
			}else if(info.getPrize().contains("红包") || info.getPrize().contains("加息券")){
				//有效期为add_time基础上加32天。
				try {
					Date addDate = sdf.parse(info.getAdd_time());
					long endTimeL = addDate.getTime() + 31*24*3600*1000L;
					String endTimeStr = sdf.format(new Date(endTimeL));
					viewHolder.endTime.setText("有效期至：" + endTimeStr.split(" ")[0]+" 23:59:59");
				} catch (ParseException e) {
					e.printStackTrace();
					viewHolder.endTime.setText("有效期至：" + info.getEnd_time());//这个时间其实是错误的，只是在解析出现问题的时候默认显示。
				}
			}else if(info.getPrize().contains("拉杆箱")){
				viewHolder.endTime.setText(info.getRemark());
			}
		}else if("MRCJ".equals(info.getActive_title()) || "MZLY".equals(info.getActive_title())){
			//微信活动每日抽奖
			viewHolder.title.setText(info.getName());
			viewHolder.remark.setVisibility(View.VISIBLE);
			viewHolder.moneyText.setVisibility(View.GONE);
			viewHolder.unitText.setVisibility(View.GONE);
			if("draw".equals(info.getSource()) || "MZLY".equals(info.getActive_title())){
				if(info.getPrize_code() == null || "".equals(info.getPrize_code())){
					viewHolder.remark.setVisibility(View.GONE);
				}else{
					viewHolder.remark.setVisibility(View.VISIBLE);
					viewHolder.remark.setText("兑换码："+info.getPrize_code());
				}
				viewHolder.endTime.setVisibility(View.GONE);
				viewHolder.catDetails.setVisibility(View.VISIBLE);
				viewHolder.ticket.setVisibility(View.VISIBLE);
				viewHolder.ticket.setText(info.getOperating_remark());
			}else{
				//获取抽奖机会
				viewHolder.endTime.setVisibility(View.VISIBLE);
				viewHolder.catDetails.setVisibility(View.GONE);
				viewHolder.ticket.setVisibility(View.GONE);
				viewHolder.remark.setText("备注："+info.getSend_time().split(" ")[0]+"当天有效");
				viewHolder.endTime.setText(info.getOperating_remark());
			}
			viewHolder.catDetails.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mOnItemMRCJListener.onClick(position,info);
				}
			});
		}else if("LYNF_01".equals(info.getActive_title())){
			//五月份活动 抢羊奶粉活动
			viewHolder.endTime.setVisibility(View.GONE);
			viewHolder.title.setText(info.getName());
			viewHolder.ticket.setVisibility(View.VISIBLE);
			viewHolder.ticket.setText(info.getRemark());
			viewHolder.remark.setVisibility(View.VISIBLE);
			viewHolder.remark.setText("备注："+info.getOperating_remark());

		}else if("MONDAY_ROB_CASH".equals(info.getActive_title())){
			//五月份活动 每周一抢现金
			viewHolder.title.setText("现金"+info.getName()+"元");
			viewHolder.endTime.setVisibility(View.VISIBLE);
			viewHolder.endTime.setText("领取时间："+info.getAdd_time());
			viewHolder.remark.setVisibility(View.VISIBLE);
			viewHolder.remark.setText("备注："+info.getOperating_remark());
		}else if("QYHD2017".equals(info.getActive_title())){
			//2017年7月份活动
			viewHolder.title.setText(info.getName());
			if(info.getPrize().contains("抽奖机会") || info.getPrize().contains("蜂蜜")){
				viewHolder.endTime.setVisibility(View.GONE);
				viewHolder.remark.setText("备注："+info.getOperating_remark()+"\n						"+info.getRemark());
			}else if(info.getPrize().contains("加息券")){
				viewHolder.remark.setText("备注："+info.getOperating_remark());
				viewHolder.endTime.setVisibility(View.VISIBLE);
				viewHolder.endTime.setText("领取时间：" + info.getSend_time());
			}
		}else if("GQCJ201710".equals(info.getActive_title())){
			//2017年10月活动
			viewHolder.endTime.setVisibility(View.VISIBLE);
			viewHolder.catDetails.setVisibility(View.GONE);
			String name = info.getName();
			viewHolder.title.setText(name);
			if (name != null && name.contains("元金币") || name.contains("红包") || name.contains("加息券")) {
				viewHolder.endTime.setText("领取时间：" + info.getSend_time());
			} else if (name.contains("抽奖机会")){
				viewHolder.endTime.setText("有效期截止到活动结束" );
			}else if(name != null && name.contains("食用油")){
				viewHolder.endTime.setText(info.getRemark());
			}

		}else if("WXCJ201710".equals(info.getActive_title()) || "WXCJ201711".equals(info.getActive_title())){
			//2017微信抽奖活动
			viewHolder.endTime.setVisibility(View.VISIBLE);
			viewHolder.catDetails.setVisibility(View.GONE);
			String name = info.getName();
			viewHolder.title.setText(name);
			if (name != null && name.contains("红包")) {
				viewHolder.endTime.setText("领取时间：" + info.getSend_time());
			} else if (name != null && name.contains("抽奖机会")){
				viewHolder.endTime.setText(info.getOperating_remark());
				viewHolder.remark.setText("备注：有效期截止到活动结束");
			}else if(name != null && name.contains("现金")){
				viewHolder.endTime.setText(info.getOperating_remark());
				viewHolder.remark.setText("备注："+info.getRemark());
			}else if(name != null && name.contains("流量")){
				viewHolder.title.setText(info.getRewardInfoEntity().getMoney()+"M手机流量");
				viewHolder.endTime.setText(info.getOperating_remark());
				viewHolder.remark.setText("备注："+info.getRemark());
			}else{
				viewHolder.endTime.setText(info.getOperating_remark());
				viewHolder.remark.setText("备注："+info.getRemark());
			}
		}else{
			viewHolder.endTime.setVisibility(View.VISIBLE);
			viewHolder.catDetails.setVisibility(View.GONE);
			viewHolder.ticket.setVisibility(View.VISIBLE);
			viewHolder.moneyText.setVisibility(View.VISIBLE);
			if ("已兑换".equals(info.getStatus())
					&& info.getRewardInfoEntity() != null) {
				viewHolder.ticket.setVisibility(View.VISIBLE);
				if (info.getRewardInfoEntity().getCode() == null
						|| "null".equals(info.getRewardInfoEntity().getCode())
						|| "NULL".equals(info.getRewardInfoEntity().getCode())) {
					viewHolder.ticket.setText("券码：");
				} else {
					viewHolder.ticket.setText("券码："
							+ info.getRewardInfoEntity().getCode());
				}

			} else {
				viewHolder.ticket.setVisibility(View.GONE);
			}
			String name = info.getName();
			viewHolder.title.setText(name);
			if (name != null && name.contains("元金币") || name.contains("现金")) {
				viewHolder.endTime.setText("领取时间：" + info.getSend_time());
			} else {
				viewHolder.endTime.setText("有效期至：" + info.getEnd_time());
			}

			if (info != null && info.getRewardInfoEntity() != null) {
				viewHolder.moneyText.setText(info.getRewardInfoEntity().getMoney());
			}

			if (name != null && name.contains("流量")) {
				if (info != null && info.getRewardInfoEntity() != null) {
					viewHolder.unitText.setVisibility(View.VISIBLE);
					viewHolder.unitText.setText("M");
				}
			} else {
				if (info != null && info.getRewardInfoEntity() != null) {
					viewHolder.unitText.setVisibility(View.VISIBLE);
					viewHolder.unitText.setText("元");
				}
			}
		}
		
		return convertView;
	}

	/**
	 * 内部类，定义Item的元素
	 * @author Mr.liu
	 *
	 */
	class ViewHolder {
		TextView title;
		TextView ticket;// 券
		TextView endTime;// 有效期
		TextView moneyText;
		TextView unitText;
		TextView remark;
		Button catDetails;//查看详情
	}

	/**
	 * 每日抽奖查看详情按钮
	 */
	public interface OnItemMRCJListener{
		void onClick(int position,PrizeInfo prize);
	}
}
