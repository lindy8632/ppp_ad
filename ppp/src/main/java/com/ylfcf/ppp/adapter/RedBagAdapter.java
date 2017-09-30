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
import com.ylfcf.ppp.entity.RedBagInfo;
import com.ylfcf.ppp.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 红包适配器
 * 
 * @author Administrator
 */
public class RedBagAdapter extends ArrayAdapter<RedBagInfo> {
	private static final int RESOURCE_ID = R.layout.redbag_item;
	private Context context;
	private LayoutInflater layoutInflater;
	private List<RedBagInfo> redbagList;
	private OnHBListItemClickListener onItemClickListener;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Date sysNowTime;

	public RedBagAdapter(Context context, OnHBListItemClickListener listener) {
		super(context, RESOURCE_ID);
		this.context = context;
		this.onItemClickListener = listener;
		redbagList = new ArrayList<RedBagInfo>();
		layoutInflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * 对外方法，动态改变listview的item并进行刷新
	 * @param list
	 * @param nowTime 当前系统时间
	 */
	public void setItems(List<RedBagInfo> list,String nowTime) {
		try {
			this.sysNowTime = sdf.parse(nowTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.redbagList.clear();
		if (list != null) {
			this.redbagList.addAll(list);
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

	int curPosition = 0;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		curPosition = position;
		RedBagInfo info = redbagList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(RESOURCE_ID, null);
			viewHolder.eduText = (TextView) convertView
					.findViewById(R.id.redbag_item_edu);
			viewHolder.validityText = (TextView) convertView
					.findViewById(R.id.regbag_item_validity);
			viewHolder.useLimitText = (TextView) convertView
					.findViewById(R.id.regbag_item_use_limit);
			viewHolder.btn = (Button) convertView
					.findViewById(R.id.redbag_list_item_btn);
			viewHolder.remark = (TextView) convertView
					.findViewById(R.id.regbag_item_remark);
			viewHolder.useFanweiText = (TextView) convertView
					.findViewById(R.id.regbag_item_usefanwei);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if ("未使用".equals(info.getUse_status())) {
			viewHolder.btn.setVisibility(View.VISIBLE);
			Date endDate = null;
			Date startDate = null;
			try {
				endDate = sdf.parse(info.getEnd_time());
				startDate = sdf.parse(info.getStart_time());
				if(sysNowTime.compareTo(startDate) == 1 && endDate.compareTo(sysNowTime) == 1){
					//说明红包已经生效
					viewHolder.btn.setEnabled(true);
					viewHolder.btn.setTextColor(context.getResources().getColor(R.color.common_topbar_bg_color));
					viewHolder.btn.setBackgroundResource(R.drawable.style_rect_fillet_blue_15dp);
				}else{
					//红包未生效
					viewHolder.btn.setEnabled(false);
					viewHolder.btn.setTextColor(context.getResources().getColor(R.color.gray));
					viewHolder.btn.setBackgroundResource(R.drawable.style_rect_fillet_gray_15dp);
				}
				if(endDate.compareTo(sysNowTime) == -1){
					//已过期
					viewHolder.btn.setVisibility(View.GONE);
				}else{
					viewHolder.btn.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
			}


		} else {
			viewHolder.btn.setVisibility(View.GONE);
		}

		viewHolder.useFanweiText.setText("适用范围："+info.getInvest_type());
		viewHolder.btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick(v, curPosition);
				}
			}
		});
		if(info.getRemark() != null && !"".equals(info.getRemark())){
			viewHolder.remark.setText("备注："+info.getRemark());
		}else{
			viewHolder.remark.setText("备注： — —");
		}
		viewHolder.eduText.setText(Util.formatRate(info.getMoney()));
		if("已使用".equals(info.getUse_status())){
			viewHolder.useFanweiText.setText("投资标的：" + info.getBorrow_name());
			viewHolder.validityText.setText("使用时间：" + info.getUse_time());
			viewHolder.useLimitText.setText("变现日期：" + info.getRepay_time().split(" ")[0]);
		}else{
			viewHolder.validityText.setText("有效期：" + info.getStart_time().split(" ")[0] + " ~ "
					+info.getEnd_time().split(" ")[0]);
			int needInvestMoneyI = 0;
			try{
				needInvestMoneyI = Integer.parseInt(info.getNeed_invest_money());
			}catch (Exception e){

			}
			if(needInvestMoneyI >= 10000){
				if(needInvestMoneyI % 10000 == 0){
					viewHolder.useLimitText.setText("使用规则：单笔投资金额不低于"
							+ needInvestMoneyI/10000 + "万元");
				}else{
					viewHolder.useLimitText.setText("使用规则：单笔投资金额不低于"
							+ needInvestMoneyI/10000d + "万元");
				}
			}else if(needInvestMoneyI <= 0){
				viewHolder.useLimitText.setText("使用规则：无");
			}else{
				viewHolder.useLimitText.setText("使用规则：单笔投资金额不低于"
						+ needInvestMoneyI + "元");
			}
		}
		return convertView;
	}

	/**
	 * 内部类，定义Item的类型
	 * @author Mr.liu
	 *
	 */
	class ViewHolder {
		TextView eduText;// 额度
		TextView useFanweiText;//使用范围
		TextView validityText;// 有效期
		TextView useLimitText;// 使用限制
		TextView remark;//备注
		Button btn;
	}

	public interface OnHBListItemClickListener {
		void onItemClick(View v, int position);
	}
}
