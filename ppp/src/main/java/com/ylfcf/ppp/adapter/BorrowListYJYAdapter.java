package com.ylfcf.ppp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.ProductInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 元聚盈列表
 * Created by Administrator on 2017/7/25.
 */

public class BorrowListYJYAdapter extends ArrayAdapter<ProductInfo> {
    private static final int RESOURCE_ID = R.layout.bid_yjy_item;
    private final LayoutInflater mInflater;

    private List<ProductInfo> bidItems;
    private Context context;
    private OnYJYItemClickListener mOnYJYItemClickListener;

    public BorrowListYJYAdapter(Context context,OnYJYItemClickListener mOnYJYItemClickListener) {
        super(context, RESOURCE_ID);
        this.context = context;
        this.mOnYJYItemClickListener = mOnYJYItemClickListener;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bidItems = new ArrayList<ProductInfo>();
    }

    /**
     * 对外方法，动态改变listview的item并进行刷新
     * @param productList
     */
    public void setItems(List<ProductInfo> productList){
        this.bidItems.clear();
        if(productList != null) {
            this.bidItems.addAll(productList);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return bidItems.size();
    }

    @Override
    public ProductInfo getItem(int position) {
        return bidItems.get(position);
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
        final ProductInfo info = bidItems.get(position);
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView	= mInflater.inflate(RESOURCE_ID, null);
            viewHolder.projectName = (TextView) convertView.findViewById(R.id.bid_yjy_item_project_name);
            viewHolder.interestRateMin = (TextView) convertView.findViewById(R.id.bid_yjy_item_invest_rate_min);
            viewHolder.middleText = (TextView) convertView.findViewById(R.id.bid_yjy_item_text0);
            viewHolder.interestRateMax = (TextView) convertView.findViewById(R.id.bid_yjy_item_invest_rate_max);
            viewHolder.timeLimit = (TextView) convertView.findViewById(R.id.bid_yjy_item_invest_time_limit);
            viewHolder.totalMoney = (TextView) convertView.findViewById(R.id.bid_yjy_item_invest_total_money);
            viewHolder.angleImg = (ImageView)convertView.findViewById(R.id.bid_yjy_item_angle);
            viewHolder.extraInterestLayout = (RelativeLayout) convertView.findViewById(R.id.bid_yjy_item_extra_interest_layout);
            viewHolder.extraInterestText = (TextView) convertView.findViewById(R.id.bid_yjy_item_extra_interest_text);
            viewHolder.nhsyText = (TextView) convertView.findViewById(R.id.bid_yjy_item_nhsy_text);
            viewHolder.bidBtn = (Button) convertView.findViewById(R.id.bid_yjy_item_bidbtn);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.projectName.setText(info.getBorrow_name());
        viewHolder.timeLimit.setText(info.getInvest_period());
        double totalMoneyL = 0d;
        try {
            totalMoneyL = Double.parseDouble(info.getTotal_money());
        } catch (Exception e) {
        }
        if(totalMoneyL%10000 == 0){
            viewHolder.totalMoney.setText((int)(totalMoneyL/10000)+"");
        }else{
            viewHolder.totalMoney.setText(totalMoneyL/10000+"");
        }

        if("未满标".equals(info.getMoney_status())){
            viewHolder.bidBtn.setText("我要投标");
            viewHolder.bidBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_blue);
        }else if("已满标".equals(info.getMoney_status()) || "已放款".equals(info.getMoney_status())){
            viewHolder.bidBtn.setText("还款中");
            viewHolder.bidBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
        }else{
            viewHolder.bidBtn.setText("已还款");
            viewHolder.bidBtn.setBackgroundResource(R.drawable.style_rect_fillet_filling_gray);
        }
        String bite = info.getBite();
        float biteFloat = 0f;
        int biteInt = 0;
        if(bite!=null){
            bite = bite.replace("%", "");
        }
        try {
            biteFloat = Float.parseFloat(bite)*100;
            biteInt = (int)biteFloat;
        } catch (Exception e) {
        }
        viewHolder.angleImg.setImageResource(R.drawable.my_account_personal_zscp_huangguan_logo);
        viewHolder.nhsyText.setText("预期年化收益率");

        double extraInterestD = 0d;
        String extraInterest = info.getAndroid_interest_rate();
        try {
            extraInterestD = Double.parseDouble(extraInterest);
        } catch (Exception e) {
        }
        if(extraInterestD > 0){
            viewHolder.extraInterestLayout.setVisibility(View.VISIBLE);
            viewHolder.extraInterestText.setText("+"+info.getAndroid_interest_rate());
        }else{
            viewHolder.extraInterestLayout.setVisibility(View.GONE);
        }
        if(position == 0){
            convertView.setPadding(0, context.getResources().getDimensionPixelSize(R.dimen.common_measure_10dp), 0, 0);
        }else{
            convertView.setPadding(0, 0, 0, 0);
        }
        viewHolder.interestRateMin.setVisibility(View.GONE);
        viewHolder.middleText.setVisibility(View.GONE);
        viewHolder.interestRateMax.setText(info.getInterest_rate());
        viewHolder.bidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnYJYItemClickListener.back(info,position);
            }
        });

        return convertView;
    }

    /**
     * 内部类，定义了Item的元素
     * @author Mr.liu
     *
     */
    class ViewHolder{
        ImageView angleImg;
        TextView projectName;
        TextView interestRateMin;//年化收益最小利率
        TextView middleText;//两个利率之间的符号
        TextView interestRateMax;//年化收益最大利率
        TextView timeLimit;//期限
        TextView totalMoney;//募集总金额
        TextView nhsyText;//年化收益四个字在VIP产品中改成了“业绩比较基准”
        RelativeLayout extraInterestLayout;// 加息的布局
        TextView extraInterestText;//加息的text
        Button bidBtn;
    }

    public interface OnYJYItemClickListener{
        void back(ProductInfo info,int position);
    }
}
