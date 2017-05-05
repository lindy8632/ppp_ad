package com.ylfcf.ppp.adapter;

import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.GiftInfo;
import com.ylfcf.ppp.util.ImageLoaderManager;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.YLFLogger;
/**
 * 福利计划页面礼品列表
 * @author Mr.liu
 *
 */
public class GiftAdapter extends ArrayAdapter<GiftInfo>{
	private static final int RESOURCE_ID = R.layout.gift_item;
	private final LayoutInflater mInflater;

	private OnGiftItemClickListener mOnGiftItemClickListener;
	private List<GiftInfo> giftList;
	private Context context;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private boolean isFirstReresh;//是否是第一次刷新adapter，目的是避免图片的重复加载
	private Map<Integer,Boolean> statusMap = new HashMap<Integer,Boolean>();
	private boolean flag = false;
	
	public GiftAdapter(Context context,OnGiftItemClickListener mOnGiftItemClickListener) {
		super(context, RESOURCE_ID);
		this.context = context;
		this.mOnGiftItemClickListener = mOnGiftItemClickListener;
		this.imageLoader = ImageLoaderManager.newInstance();
		options = ImageLoaderManager.configurationOption(R.drawable.gift_item_default_logo,
				R.drawable.gift_item_default_logo);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		giftList = new ArrayList<GiftInfo>();
	}

	/**
	 * 对外方法，动态改变listview的item并进行刷新
	 * 
	 * @param productList
	 */
	public void setItems(List<GiftInfo> giftList,boolean isFirstReresh) {
		this.giftList.clear();
		this.isFirstReresh = isFirstReresh;
		if (giftList != null) {
			this.giftList.addAll(giftList);
		}
		for(int i=0;i<giftList.size();i++){
			statusMap.put(i, false);
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return giftList.size();
	}

	@Override
	public GiftInfo getItem(int position) {
		return giftList.get(position);
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
		final GiftInfo info = giftList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(RESOURCE_ID, null);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.gift_item_title);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.gift_item_content);
			viewHolder.rules = (TextView) convertView
					.findViewById(R.id.gift_item_rules);
			viewHolder.logo = (ImageView) convertView
					.findViewById(R.id.gift_item_logo);
			viewHolder.getBtn = (Button) convertView
					.findViewById(R.id.gift_item_btn);
			viewHolder.rulesLayout = (RelativeLayout) convertView
					.findViewById(R.id.gift_item_rule_layout);
			viewHolder.ruleContent = (TextView) convertView
					.findViewById(R.id.gift_item_rule_content);
			viewHolder.ruleBtn = (Button) convertView
					.findViewById(R.id.gift_item_rule_btn);
			viewHolder.ruleDel = (RelativeLayout) convertView.findViewById(R.id.gift_item_rule_delbtn);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.title.setText(info.getName());
		viewHolder.content.setText(info.getDescription());
		if(info.getExternal_link_wap() != null && !"".equals(info.getExternal_link_wap())){
			viewHolder.ruleBtn.setTag(info.getExternal_link_wap());
			viewHolder.ruleBtn.setVisibility(View.VISIBLE);
		}else{
			viewHolder.ruleBtn.setVisibility(View.GONE);
		}
		StringBuffer sb = new StringBuffer();
		if(info.getRulesAppList() != null){
			for(int i=0;i<info.getRulesAppList().size();i++){
				sb.append(info.getRulesAppList().get(i)).append("\n");
			}
		}
		viewHolder.ruleContent.setText(sb.toString().replace("&#039;", "'").replace("&quot;", "\"")
				.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&"));
		viewHolder.ruleContent.setMovementMethod(ScrollingMovementMethod.getInstance());
		viewHolder.ruleContent.setSelected(true);
		//首先判断活动已经开始
		if(info.getIsStart() == 0){
			//活动已经开始
			if(info.isLogin()){
				//已经登录
				if(SettingsManager.isPersonalUser(context.getApplicationContext())){
					if(info.isGetOver()){
						//已经领完了
						viewHolder.getBtn.setEnabled(false);
						viewHolder.getBtn.setText("已领完");
						viewHolder.getBtn.setTextColor(context.getResources().getColor(R.color.gray));
						viewHolder.getBtn.setBackgroundResource(R.drawable.gift_item_end_btn);
					}else{
						//还没领完
						if(info.isGet()){
							//已经领取
							viewHolder.getBtn.setEnabled(false);
							viewHolder.getBtn.setText("已领取");
							viewHolder.getBtn.setTextColor(context.getResources().getColor(R.color.gray));
							viewHolder.getBtn.setBackgroundResource(R.drawable.gift_item_end_btn);
						}else{
							viewHolder.getBtn.setEnabled(true);
							viewHolder.getBtn.setText("领取");
							viewHolder.getBtn.setTextColor(context.getResources().getColor(R.color.prize_region_gift_item_tv_color));
							viewHolder.getBtn.setBackgroundResource(R.drawable.gift_item_get_btn);
						}
					}
				}else{
					if(info.isGetOver()){
						//已经领完了
						viewHolder.getBtn.setEnabled(false);
						viewHolder.getBtn.setText("已领完");
						viewHolder.getBtn.setTextColor(context.getResources().getColor(R.color.gray));
						viewHolder.getBtn.setBackgroundResource(R.drawable.gift_item_end_btn);
					}else{
						//还没领完
						viewHolder.getBtn.setEnabled(false);
						viewHolder.getBtn.setText("领取");
						viewHolder.getBtn.setTextColor(context.getResources().getColor(R.color.gray));
						viewHolder.getBtn.setBackgroundResource(R.drawable.gift_item_end_btn);
					}
				}
				
			}else{
				//未登录
				viewHolder.getBtn.setEnabled(true);
				viewHolder.getBtn.setText("请登录");
				viewHolder.getBtn.setTextColor(context.getResources().getColor(R.color.prize_region_gift_item_tv_color));
				viewHolder.getBtn.setBackgroundResource(R.drawable.gift_item_get_btn);
			}
		}else if(info.getIsStart() == -1){
			//活动未开始
			viewHolder.getBtn.setEnabled(false);
			viewHolder.getBtn.setText("敬请期待");
			viewHolder.getBtn.setTextColor(context.getResources().getColor(R.color.gray));
			viewHolder.getBtn.setBackgroundResource(R.drawable.gift_item_end_btn);
		}else if(info.getIsStart() == 1){
			//活动已经结束
			viewHolder.getBtn.setEnabled(false);
			viewHolder.getBtn.setText("活动结束");
			viewHolder.getBtn.setTextColor(context.getResources().getColor(R.color.gray));
			viewHolder.getBtn.setBackgroundResource(R.drawable.gift_item_end_btn);
		}
		viewHolder.rules.setTag(viewHolder.rulesLayout);
		viewHolder.rules.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				if(!statusMap.get(position)){
//					RelativeLayout ruleLayoutR = (RelativeLayout) v.getTag();
//					ruleLayoutR.setVisibility(View.VISIBLE);
//				}
//				flag = !statusMap.get(position);
				statusMap.put(position, true);
				notifyDataSetChanged();
			}
		});
		viewHolder.getBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnGiftItemClickListener.onclick(info, position);
			}
		});
		viewHolder.ruleDel.setTag(viewHolder.rulesLayout);
		viewHolder.ruleDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				statusMap.put(position, false);
				notifyDataSetChanged();
			}
		});
		viewHolder.ruleBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//qianwang商城
				String url = (String) v.getTag();
				Uri uri = Uri.parse(url);
	                Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
	                context.startActivity(intent);  
			}
		});
		if(statusMap.get(position)){
			viewHolder.rulesLayout.setVisibility(View.VISIBLE);
		}else{
			viewHolder.rulesLayout.setVisibility(View.GONE);
		}
		YLFLogger.d("会员福利计划2期奖品图片:"+info.getPic());
		ImageLoaderManager.loadingImage(imageLoader, info.getPic(), viewHolder.logo, options, null, null);
		return convertView;
	}

	/**
	 * 内部类，定义了Item的元素
	 * 
	 * @author Mr.liu
	 * 
	 */
	class ViewHolder {
		ImageView logo;//礼品logo
		TextView title;//礼品名字
		TextView content;//礼品介绍
		TextView rules;//规则
		Button getBtn;//领取按钮
		RelativeLayout rulesLayout;//规则
		TextView ruleContent;
		Button ruleBtn;//前往商城
		RelativeLayout ruleDel;
	}

	public interface OnGiftItemClickListener{
		void onclick(GiftInfo info,int position);
		void ruleOnClick(View v);
	}
}
