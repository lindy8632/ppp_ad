package com.ylfcf.ppp.fragment;

import java.util.List;

import android.content.Intent;
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
import com.ylfcf.ppp.ui.MainFragmentActivity;
import com.ylfcf.ppp.ui.MyTYJActivity;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 我的体验金---未使用
 * @author jianbing
 *
 */
public class MyTYJNousedFragment extends BaseFragment{
	private MyTYJActivity mytyjActivity;
	private View rootView;
	private TextView nodataText;
	
	private ListView nousedListView;
	private TYJListAdapter tyjAdapter;
	private int pageNo = 0;
	private int pageSize = 20;
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
            rootView=inflater.inflate(R.layout.mytyj_noused_fragment, null);
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
		nousedListView = (ListView) view.findViewById(R.id.mytyj_noused_fragment_listview);
		nodataText = (TextView)view.findViewById(R.id.mytyj_noused_fragment_nodata);
		tyjAdapter = new TYJListAdapter(mytyjActivity, new OnTYJListItemClickListener() {
			@Override
			public void onclick(View v, int position) {
				SettingsManager.setMainProductListFlag(mytyjActivity, true);
				Intent intent = new Intent(mytyjActivity,MainFragmentActivity.class);
				startActivity(intent);
				mytyjActivity.finish();
			}
		});
		nousedListView.setAdapter(tyjAdapter);
	}
	
	public void updateAdapter(List<TYJInfo> list){
		if(list == null || list.size() < 1){
			nousedListView.setVisibility(View.GONE);
			nodataText.setVisibility(View.VISIBLE);
		}else{
			nousedListView.setVisibility(View.VISIBLE);
			nodataText.setVisibility(View.GONE);
		}
		tyjAdapter.setItems(list);
	}
	
}
