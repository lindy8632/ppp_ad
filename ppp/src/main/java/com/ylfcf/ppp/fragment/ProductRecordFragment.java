package com.ylfcf.ppp.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.BorrowRecordsAdapter;
import com.ylfcf.ppp.adapter.UserInvestRecordAdapter;
import com.ylfcf.ppp.async.AsyncInvestRecord;
import com.ylfcf.ppp.async.AsyncInvestTotalRecord;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.InvestRecordPageInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnInvestRecordListInter;
import com.ylfcf.ppp.ui.BorrowDetailZXDActivity;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 产品详情--投资记录
 * 
 * @author Administrator
 * 
 */
public class ProductRecordFragment extends BaseFragment {
	private static final int REQUEST_INVEST_RECORD_WHAT = 1021;
	private static final int REQUEST_INVEST_RECORD_SUCCESS = 1022;
	private static final int REQUEST_INVEST_RECORD_NODATA = 1023; // 无数据

	private BorrowDetailZXDActivity borrowDetailActivity;
	private ListView recordList;
	private LayoutInflater layoutInflater;
	private View headerView;

	private BorrowRecordsAdapter investRecordsAdapter;
	private List<InvestRecordInfo> investRecordList = new ArrayList<InvestRecordInfo>();

	// 请求参数
	private String investUserId = "";// 878
	private String borrowId = "";// 1214
	private String status = "";// 投资中
	private int pageNo = 0;
	private int pageSize = 100;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_INVEST_RECORD_WHAT:
				getInvestRecordList(investUserId, borrowId, status, "1");
				break;
			case REQUEST_INVEST_RECORD_SUCCESS:
				InvestRecordPageInfo pageInfo = (InvestRecordPageInfo) msg.obj;
				investRecordList.clear();
				investRecordList.addAll(pageInfo.getInvestRecordList());
				updateAdapter(investRecordList);
				break;
			case REQUEST_INVEST_RECORD_NODATA:
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		borrowDetailActivity = (BorrowDetailZXDActivity) getActivity();
		borrowId = borrowDetailActivity.productInfo.getId();
		layoutInflater = (LayoutInflater) borrowDetailActivity
				.getSystemService(borrowDetailActivity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.productrecord_fragment,
				container, false);
		findViews(view);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	private void findViews(View view) {
		headerView = layoutInflater.inflate(R.layout.invest_records_header,
				null);
		recordList = (ListView) view.findViewById(R.id.product_record_listview);
		recordList.addHeaderView(headerView);
		investRecordsAdapter = new BorrowRecordsAdapter(borrowDetailActivity);
		recordList.setAdapter(investRecordsAdapter);
		handler.sendEmptyMessage(REQUEST_INVEST_RECORD_WHAT);
	}

	private void updateAdapter(List<InvestRecordInfo> recordList) {
		investRecordsAdapter.setItems(recordList);
	}

	/**
	 * 获取投资记录列表
	 * 
	 * @param investUserId
	 * @param borrowId
	 * @param status
	 * @param pageNo
	 * @param pageSize
	 */
	private void getInvestRecordList(String investUserId, String borrowId,
			String status, String isAddCoin) {
		AsyncInvestRecord asyncInvestRecord = new AsyncInvestRecord(
				borrowDetailActivity, investUserId, borrowId, status,
				isAddCoin, pageNo, pageSize, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = handler
										.obtainMessage(REQUEST_INVEST_RECORD_SUCCESS);
								msg.obj = baseInfo.getmInvestRecordPageInfo();
								handler.sendMessage(msg);
							} else {
								Message msg = handler
										.obtainMessage(REQUEST_INVEST_RECORD_NODATA);
								msg.obj = baseInfo.getMsg();
								handler.sendMessage(msg);
							}
						}

					}
				});
		asyncInvestRecord.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
