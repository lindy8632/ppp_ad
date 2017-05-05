package com.ylfcf.ppp.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.TYJListAdapter;
import com.ylfcf.ppp.adapter.TYJListAdapter.OnTYJListItemClickListener;
import com.ylfcf.ppp.async.AsyncTYJPageInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.TYJInfo;
import com.ylfcf.ppp.entity.TYJPageInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.ui.MyTYJActivity;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;

/**
 * 我的体验金---已使用
 * @author jianbing
 *
 */
public class MyTYJUsedFragment extends BaseFragment{
	private MyTYJActivity mytyjActivity;
	private View rootView;
	
	private ListView usedListView;
	private TYJListAdapter tyjAdapter;
	private int pageNo = 0;
	private int pageSize = 20;
	private TextView nodataText;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mytyjActivity = (MyTYJActivity) getActivity();
		if(rootView==null){
            rootView=inflater.inflate(R.layout.mytyj_used_fragment, null);
            findViews(rootView);
        }
		//缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        } 
        return rootView;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	private void findViews(View view){
		usedListView = (ListView) view.findViewById(R.id.mytyj_used_fragment_listview);
		nodataText = (TextView)view.findViewById(R.id.mytyj_used_fragment_nodata);
		tyjAdapter = new TYJListAdapter(mytyjActivity, new OnTYJListItemClickListener() {
			@Override
			public void onclick(View v, int position) {
				mytyjActivity.finish();
			}
		});
		usedListView.setAdapter(tyjAdapter);
	}
	
	public void updateAdapter(List<TYJInfo> list){
		if(list == null || list.size() < 1){
			usedListView.setVisibility(View.GONE);
			nodataText.setVisibility(View.VISIBLE);
		}else{
			usedListView.setVisibility(View.VISIBLE);
			nodataText.setVisibility(View.GONE);
		}
		tyjAdapter.setItems(list);
	}
}
