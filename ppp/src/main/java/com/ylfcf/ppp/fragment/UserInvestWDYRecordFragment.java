package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.UserInvestWDYRecordAdapter;
import com.ylfcf.ppp.adapter.UserInvestWDYRecordAdapter.OnWDYItemClickListener;
import com.ylfcf.ppp.async.AsyncWDYChildRecord;
import com.ylfcf.ppp.async.AsyncWDYInvestRecord;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.InvestRecordPageInfo;
import com.ylfcf.ppp.entity.WDYChildRecordInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.ui.BorrowDetailWDYActivity;
import com.ylfcf.ppp.ui.CompactActivity;
import com.ylfcf.ppp.ui.UserInvestRecordActivity;
import com.ylfcf.ppp.ui.WDYLendRecordDetailActivity;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.YLFLogger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 稳定赢（薪盈计划）投资记录页面
 * @author Mr.liu
 *
 */
public class UserInvestWDYRecordFragment extends BaseFragment{
	private static final int REQUEST_INVEST_RECORD_WHAT = 1021;
	private static final int REQUEST_INVEST_RECORD_SUCCESS = 1022;
	private static final int REQUEST_INVEST_RECORD_NODATA = 1023;	//无数据
	
	private static final int REQUEST_CHILD_RECORD_WHAT = 1024;

	private UserInvestRecordActivity mainActivity;
	private View rootView;

	private UserInvestWDYRecordAdapter investWDYRecordsAdapter;
	private List<InvestRecordInfo> investRecordList = new ArrayList<InvestRecordInfo>();
	private PullToRefreshListView pullToRefreshListView;
	private TextView nodataText;
	private View topLayout;
	private LayoutInflater mLayoutInflater;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String systemTime = "";//系统当前时间
	//请求参数
	private String status = "";//投资中
	private int pageNo = 0;
	private int pageSize = 20;
	private boolean isFirst = true;
	private boolean isLoadMore = false;// 加载更多
	private AlertDialog.Builder builder = null; // 先得到构造器
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_INVEST_RECORD_WHAT:
				getInvestRecordList(SettingsManager.getUserId(mainActivity.getApplicationContext()), "");
				break;
			case REQUEST_INVEST_RECORD_SUCCESS:
				InvestRecordPageInfo pageInfo = (InvestRecordPageInfo) msg.obj;
				pullToRefreshListView.setVisibility(View.VISIBLE);
				nodataText.setVisibility(View.GONE);
				if (pageInfo != null) {
					if (!isLoadMore) {
						investRecordList.clear();
					}
					investRecordList.addAll(pageInfo.getInvestRecordList());
					combRecordData(investRecordList);
					investWDYRecordsAdapter.setItems(investRecordList,systemTime);
				}
				isLoadMore = false;
				pullToRefreshListView.onRefreshComplete();
				break;
			case REQUEST_INVEST_RECORD_NODATA:
				if(!isLoadMore){
					pullToRefreshListView.setVisibility(View.GONE);
					nodataText.setVisibility(View.VISIBLE);
				}
				isLoadMore = false;
				pullToRefreshListView.onRefreshComplete();
				break;
			case REQUEST_CHILD_RECORD_WHAT:
				int position = (Integer) msg.obj;
				getChildRecordById(position);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainActivity = (UserInvestRecordActivity) getActivity();
		mLayoutInflater = (LayoutInflater) mainActivity.getSystemService(mainActivity.LAYOUT_INFLATER_SERVICE);
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.userinvest_zxd_record_fragment, null);
			findViews(rootView);
		}
		builder = new AlertDialog.Builder(mainActivity,
				R.style.Dialog_Transparent); // 先得到构造器
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		handler.sendEmptyMessage(REQUEST_INVEST_RECORD_WHAT);
		return rootView;
	}

	private void findViews(View view) {
		pullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.userinvest_zxd_record_fragment_pull_refresh_list);
		nodataText = (TextView) view
				.findViewById(R.id.userinvest_zxd_record_fragment_nodata);
		topLayout = mLayoutInflater.inflate(R.layout.wdy_investrecord_top_layout, null);
		pullToRefreshListView.getRefreshableView().addHeaderView(topLayout);
		investWDYRecordsAdapter = new UserInvestWDYRecordAdapter(mainActivity,new OnWDYItemClickListener() {
			@Override
			public void onCatCompact(int position, View v, InvestRecordInfo info) {
				//查看合同
				Intent intentCampact = new Intent(mainActivity,CompactActivity.class);
				intentCampact.putExtra("invest_record", info);
				intentCampact.putExtra("from_where", "wdy");
				startActivity(intentCampact);
			}
			@Override
			public void onCatBidRecord(int position, View v, InvestRecordInfo info) {
				//查看出借记录
				Intent intentLend = new Intent(mainActivity,WDYLendRecordDetailActivity.class);
				intentLend.putExtra("invest_record", info);
				startActivity(intentLend);
			}
			@Override
			public void onBidCurrentPeroid(int position, View v, InvestRecordInfo info) {
			}
		},"wdy");
		pullToRefreshListView.setAdapter(investWDYRecordsAdapter);
		initListeners();
	}

	/**
	 * 请求接口进行数据合并
	 * @param investRecordList
	 */
	private void combRecordData(List<InvestRecordInfo> investRecordList){
		if(investRecordList == null){
			return;
		}
		for(int i=0;i<investRecordList.size();i++){
			Message msg = handler.obtainMessage(REQUEST_CHILD_RECORD_WHAT);
			msg.obj = i;
			handler.sendMessage(msg);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	private void initListeners() {
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 下拉刷新
						pageNo = 0;
						handler.sendEmptyMessage(REQUEST_INVEST_RECORD_WHAT);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 上拉加载更多
						pageNo++;
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								isLoadMore = true;
								handler.sendEmptyMessage(REQUEST_INVEST_RECORD_WHAT);
							}
						}, 1000L);
					}
				});
		pullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				InvestRecordInfo info = (InvestRecordInfo) parent.getItemAtPosition(position);
				if(info == null){
					ShowWDYRecordPrompt();
					return;
				}
				Intent intent = new Intent(mainActivity,BorrowDetailWDYActivity.class);
				intent.putExtra("InvestRecordInfo", info);
				startActivity(intent);
			}
		});
	}

	/**
	 * 稳定盈提示
	 */
	private void ShowWDYRecordPrompt(){
		View contentView = LayoutInflater.from(mainActivity)
				.inflate(R.layout.wdy_invest_record_prompt, null);
		final Button okBtn = (Button) contentView
				.findViewById(R.id.wdy_invest_record_prompt_dialog_btn);
		final ImageView delBtn = (ImageView) contentView
				.findViewById(R.id.wdy_invest_record_prompt_dialog_delete);
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		delBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// 参数都设置完成了，创建并显示出来
		dialog.show();
		// 设置dialog的宽度
		WindowManager windowManager = mainActivity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth() * 6 / 7;
		dialog.getWindow().setAttributes(lp);
	}
	
	/**
	 * 获取投资记录列表
	 * @param investUserId
	 * @param borrowId
	 */
	private void getInvestRecordList(String investUserId,String borrowId){
		if(mainActivity.loadingDialog != null && isFirst){
			mainActivity.loadingDialog.show();
		}
		isFirst = false;
		AsyncWDYInvestRecord asyncInvestRecord = new AsyncWDYInvestRecord(mainActivity,borrowId,investUserId, "0",
				"0", pageNo, pageSize, new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						if(mainActivity.loadingDialog.isShowing()){
							mainActivity.loadingDialog.dismiss();
						}
						if(baseInfo != null){
							systemTime = baseInfo.getTime();
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								Message msg = handler.obtainMessage(REQUEST_INVEST_RECORD_SUCCESS);
								msg.obj = baseInfo.getmInvestRecordPageInfo();
								handler.sendMessage(msg);
							}else{
								Message msg = handler.obtainMessage(REQUEST_INVEST_RECORD_NODATA);
								msg.obj = baseInfo.getMsg();
								handler.sendMessage(msg);
							}
						}
					}
		});
		asyncInvestRecord.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 获取子记录()
	 */
	private void getChildRecordById(final int position){
		InvestRecordInfo recordInfo = investRecordList.get(position);
		AsyncWDYChildRecord childRecordTask = new AsyncWDYChildRecord(mainActivity, recordInfo.getId(), 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								List<WDYChildRecordInfo> childRecordList = baseInfo.getmWDYChildRecordPageInfo().getWdyChildRecordList();
								combWdyChildRecordData(baseInfo,childRecordList,position);
							}
						}	
					}
				});
		childRecordTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 
	 * @param childList
	 * @param position
	 */
	private void combWdyChildRecordData(BaseInfo baseInfo,List<WDYChildRecordInfo> childList,int position){
		InvestRecordInfo recordInfo = investRecordList.get(position);
		long currentTimeL = 0l;
		long nextTimeL = 0l;//下期计划加入时间
		try {
			currentTimeL = sdf.parse(baseInfo.getTime()).getTime();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		int totalDelayDaysI = 0;
		double totalInterestMoneyD = 0;
		for(int i=0;i<childList.size();i++){
			long nextTimeTempL = 0l;
			int delayDaysI = 0;
			double interestMoneyD = 0;
			WDYChildRecordInfo info = childList.get(i);
			String delayDays = info.getDelay_days();//延期天数
			String interestMoney = info.getInterest_money();//收益（不算延期的手续费）
			try {
				nextTimeTempL = sdf.parse(info.getPlan_add_time()).getTime();
			} catch (Exception e) {
			}
			if(currentTimeL < nextTimeTempL){
				nextTimeL = nextTimeTempL;
			}
			try {
				delayDaysI = Integer.parseInt(delayDays);
				interestMoneyD = Double.parseDouble(interestMoney);
			} catch (Exception e) {
			}
			totalDelayDaysI = totalDelayDaysI + delayDaysI;
			totalInterestMoneyD = totalInterestMoneyD + interestMoneyD;
		}
		//计算因延期产生的费用
		double totalDelayD = 0;//总共允许的延期天数
		int interestFreePeriodI = 0;//每年允许的延期天数
		int interestPeriodMonthI = 0;//投资期限（单位月）
		double totalInvestDaysD = 0d;//总的投资天数
		double delayInterestD = 0d;//延期产生手续费
		try {
			interestFreePeriodI = Integer.parseInt(recordInfo.getInterest_free_period());
			interestPeriodMonthI = Integer.parseInt(recordInfo.getInterest_period_month());
			totalDelayD = interestFreePeriodI * interestPeriodMonthI / 12.0;
			totalInvestDaysD = 365.0d * interestPeriodMonthI / 12;
		} catch (Exception e) {
		}
		if(totalDelayDaysI > totalDelayD){
			//说明有延期的情况发生	
			delayInterestD = (totalDelayDaysI - totalDelayD) * totalInterestMoneyD / totalInvestDaysD;
		}
		investRecordList.get(position).setWdy_pro_interest(String.valueOf(totalInterestMoneyD));
		YLFLogger.d("预期收益："+totalInterestMoneyD);
		totalInterestMoneyD = totalInterestMoneyD - delayInterestD;
		investRecordList.get(position).setTotalLend(recordInfo.getTotal_money());
		investRecordList.get(position).setTotalDelay(String.valueOf(totalDelayDaysI));
		YLFLogger.d("延期天数："+totalDelayDaysI);
		investRecordList.get(position).setWdy_real_interest(String.valueOf(totalInterestMoneyD));
		YLFLogger.d("实际收益："+totalInterestMoneyD);
		investRecordList.get(position).setWdyChildRecordList(childList);
		investWDYRecordsAdapter.setItems(investRecordList,systemTime);//刷新适配器
	}
}
