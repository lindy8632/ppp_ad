package com.ylfcf.ppp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.BankInfo;

/**
 * 限额说明
 * @author Administrator
 *
 */
public class LimitMoneyAdapter extends ArrayAdapter<BankInfo>{
	private static final int RESOURCE_ID = R.layout.limit_prompt_listview_item;
	private final LayoutInflater mInflater;
    
    private List<BankInfo> bankList;
    private Context context;
    
	public LimitMoneyAdapter(Context context) {
		super(context, RESOURCE_ID);
		this.context = context;
		mInflater	= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		bankList	= new ArrayList<BankInfo>();
	}
	
	/**
	 * 对外方法，动态改变listview的item并进行刷新
	 * @param list
	 */
	public void setItems(List<BankInfo> list){
		this.bankList.clear();
		if(list != null) {
			this.bankList.addAll(list);
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return bankList.size();
	}

	@Override
	public BankInfo getItem(int position) {
		return bankList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 设置具体一个标的的显示内容
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		String singleQuota = "",dailyQuota = "";
		BankInfo info = bankList.get(position);
		if (convertView == null){
			viewHolder = new ViewHolder();
			convertView	= mInflater.inflate(RESOURCE_ID, null);
			viewHolder.bankLogo = (ImageView) convertView.findViewById(R.id.limit_prompt_listview_item_cardlogo);
			viewHolder.bankName = (TextView) convertView.findViewById(R.id.limit_prompt_listview_item_bankname);
			viewHolder.bankPrompt = (TextView) convertView.findViewById(R.id.limit_prompt_listview_item_prompt);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
        viewHolder.bankName.setText(info.getBank_name());
        if("0".equals(info.getSingle_quota())){
        	singleQuota = "无限额";
        }else{
        	singleQuota = info.getSingle_quota() + "万";
        }
        if("0".equals(info.getDaily_quota())){
        	dailyQuota = "无限额";
        }else{
        	dailyQuota = info.getDaily_quota() + "万";
        }
        viewHolder.bankPrompt.setText("单笔"+singleQuota+"  单日"+ dailyQuota);
		return convertView;
	}
	
	/**
	 * 内部类，定义Item的元素
	 * @author Mr.liu
	 *
	 */
	class ViewHolder{
		ImageView bankLogo;
		TextView bankName;
		TextView bankPrompt;
	}

}
