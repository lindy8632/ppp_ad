package com.ylfcf.ppp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.FriendInfo;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的好友
 * Created by Administrator on 2017/7/6.
 */

public class MyFriendAdapter extends ArrayAdapter<FriendInfo> {
    private static final int RESOURCE_ID = R.layout.myfriends_listview_item;
    private final LayoutInflater mInflater;

    private List<FriendInfo> friendList;
    private Context context;
    private OnFriendItemClickListener mOnFriendItemClickListener;
    private boolean isLcs = false;

    public MyFriendAdapter(Context context,OnFriendItemClickListener mOnFriendItemClickListener) {
        super(context, RESOURCE_ID);
        this.context = context;
        this.mOnFriendItemClickListener = mOnFriendItemClickListener;
        mInflater	= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        friendList	= new ArrayList<FriendInfo>();
    }

    /**
     * 对外方法，动态改变listview的item并进行刷新
     * @param list
     */
    public void setItems(List<FriendInfo> list,boolean isLcs){
        this.isLcs = isLcs;
        this.friendList.clear();
        if(list != null) {
            this.friendList.addAll(list);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public FriendInfo getItem(int position) {
        return friendList.get(position);
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
        final FriendInfo info = friendList.get(position);
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView	= mInflater.inflate(RESOURCE_ID, null);
            viewHolder.phoneTV = (TextView) convertView.findViewById(R.id.myfriends_activity_phonetv);
            viewHolder.nameTV = (TextView) convertView.findViewById(R.id.myfriends_activity_nametv);
            viewHolder.statusTV = (TextView) convertView.findViewById(R.id.myfriends_activity_statustv);
            viewHolder.registerTimeTV = (TextView) convertView.findViewById(R.id.myfriends_listview_item_registertime);
            viewHolder.btn = (Button) convertView.findViewById(R.id.myfriends_listview_item_btn);
            viewHolder.btnTableRow = (TableRow) convertView.findViewById(R.id.myfriends_activity_item_btntablerow);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(isLcs){
            viewHolder.btnTableRow.setVisibility(View.VISIBLE);
        }else{
            viewHolder.btnTableRow.setVisibility(View.GONE);
        }
        viewHolder.phoneTV.setText(info.getPhone());
        if(info.getReal_name() == null || "".equals(info.getReal_name())){
            viewHolder.nameTV.setText("一 一");
        }else{
            viewHolder.nameTV.setText(Util.hidRealName2(info.getReal_name()));
        }
        if("0".equals(info.getStatus())){
            //注册
            viewHolder.statusTV.setText("注册");
        }else if("1".equals(info.getStatus())){
            //实名
            viewHolder.statusTV.setText("实名");
        }else if("2".equals(info.getStatus())){
            //投资
            viewHolder.statusTV.setText("投资");
        }
        if(info.getReg_time() != null && !"".equals(info.getReg_time())){
            viewHolder.registerTimeTV.setText(info.getReg_time().split(" ")[0]);
        }
        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFriendItemClickListener.onItemClick(position,info);
            }
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        if(position == friendList.size() - 1){
            params.bottomMargin = 20;
            convertView.setLayoutParams(params);
        }else{
            params.bottomMargin = 0;
            convertView.setLayoutParams(params);
        }
        YLFLogger.d("position:--------------------"+position);
        return convertView;
    }

    /**
     * 内部类，定义Item的元素
     * @author Mr.liu
     *
     */
    class ViewHolder{
        TextView phoneTV;
        TextView nameTV;
        TextView statusTV;
        TextView registerTimeTV;//注册时间
        Button btn;//赠送加息券
        TableRow btnTableRow;
    }

    public interface OnFriendItemClickListener{
        void onItemClick(int position,FriendInfo friendInfo);
    }
}
