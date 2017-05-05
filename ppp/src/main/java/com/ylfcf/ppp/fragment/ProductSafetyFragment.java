package com.ylfcf.ppp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.ui.BorrowDetailZXDActivity;

/**
 * °²È«±£ÕÏ
 * @author Administrator
 *
 */
public class ProductSafetyFragment extends BaseFragment{
	private BorrowDetailZXDActivity borrowDetailActivity;
	private TextView zjaqTV;
	private TextView zcaqTV;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		borrowDetailActivity = (BorrowDetailZXDActivity) getActivity();
		View view = inflater.inflate(R.layout.productsafety_fragment, container, false);
		findViews(view);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	private void findViews(View view){
		zjaqTV = (TextView) view.findViewById(R.id.productsafety_fragment_zjaq);
		zcaqTV = (TextView) view.findViewById(R.id.productsafety_fragment_zcaq);
		borrowDetailActivity.setOnProductSafetyListener(new OnProductSafetyListener() {
			@Override
			public void back(ProjectInfo info) {
				zcaqTV.setText(Html.fromHtml(info.getRepay_from()));
			}
		});
	}
	
	public interface OnProductSafetyListener{
		public void back(ProjectInfo info);
	}

}
