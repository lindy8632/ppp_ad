package com.ylfcf.ppp.fragment;

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
import com.ylfcf.ppp.entity.TYJInfo;
import com.ylfcf.ppp.ui.MyTYJActivity;
import com.ylfcf.ppp.util.UMengStatistics;

import java.util.List;

/**
 * 我的体验金----已失效
 * 
 * @author jianbing
 * 
 */
public class MyTYJOverdueFragment extends BaseFragment {
	private static final String className = "MyTYJOverdueFragment";
	private MyTYJActivity mytyjActivity;
	private View rootView;

	private ListView usedListView;
	private TYJListAdapter tyjAdapter;
	private int pageNo = 0;
	private int pageSize = 20;
	private TextView overdueText;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mytyjActivity = (MyTYJActivity) getActivity();
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.mytyj_overdue_fragment, null);
			findViews(rootView);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//友盟统计页面跳转
	}

	@Override
	public void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);//友盟统计页面跳转
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private void findViews(View view) {
		overdueText = (TextView) view
				.findViewById(R.id.mytyj_overdue_fragment_nodata);
		usedListView = (ListView) view
				.findViewById(R.id.mytyj_overdue_fragment_listview);
		tyjAdapter = new TYJListAdapter(mytyjActivity,
				new OnTYJListItemClickListener() {
					@Override
					public void onclick(View v, int position) {
						mytyjActivity.finish();
					}
				});
		usedListView.setAdapter(tyjAdapter);
	}

	public void updateAdapter(List<TYJInfo> list) {
		if (list == null || list.size() < 1) {
			usedListView.setVisibility(View.GONE);
			overdueText.setVisibility(View.VISIBLE);
		} else {
			usedListView.setVisibility(View.VISIBLE);
			overdueText.setVisibility(View.GONE);
		}
		tyjAdapter.setItems(list);
	}

}
