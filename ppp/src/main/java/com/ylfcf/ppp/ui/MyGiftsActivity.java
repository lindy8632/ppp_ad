package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.PrizeAdapter;
import com.ylfcf.ppp.async.AsyncGiftCode;
import com.ylfcf.ppp.async.AsyncLottery;
import com.ylfcf.ppp.async.AsyncLotteryCode;
import com.ylfcf.ppp.async.AsyncPrizeCode;
import com.ylfcf.ppp.async.AsyncPrizeList;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.GiftCodeInfo;
import com.ylfcf.ppp.entity.LotteryCodeInfo;
import com.ylfcf.ppp.entity.PrizeCodeInfo;
import com.ylfcf.ppp.entity.PrizeInfo;
import com.ylfcf.ppp.entity.PrizePageInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.view.LotteryDetailPopwindow;
import com.ylfcf.ppp.widget.RefreshLayout;
import com.ylfcf.ppp.widget.RefreshLayout.OnLoadListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的礼品
 * @author jianbing
 *
 */
public class MyGiftsActivity extends BaseActivity implements OnClickListener{
	private static final String className = "MyGiftsActivity";
	private static final int REQUEST_PRIZELIST_WHAT = 8211;
	private static final int REQUEST_PRIZELIST_SUCCESS = 8212;
	private static final int REQUEST_PRIZELIST_NODATA = 8213;
	
	private static final int REQUEST_PRIZECODE_WHAT = 8214;
	private static final int REQUEST_GIFTCODE_WHAT = 8215;

	private static final int REQUEST_LOTTERY_DETAILS_WHAT = 8216;
	private static final int REQUEST_LOTTERY_DETAILS_SUCCESS = 8217;

	private static final int REQUEST_LOTTERYCODE_WHAT = 8218;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private RefreshLayout mainRefreshLayout;
	private ListView giftList;
	private TextView promptText;
	
	private PrizeAdapter prizeAdapter;
	
	private int pageNo = 0;
	private int pageSize = 20;
	private List<PrizeInfo> prizeTotalList = new ArrayList<PrizeInfo>();
	
	private boolean isLoadMore = false;
	private boolean isUpdated = false;
	private boolean isFrist = true;//是否首次加载...
	private LinearLayout mainLayout;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_PRIZELIST_WHAT:
				requestPrizeList(SettingsManager.getUserId(getApplicationContext()), "");
				break;
			case REQUEST_PRIZELIST_SUCCESS:
				
				break;
			case REQUEST_PRIZELIST_NODATA:
				
				break;
			case REQUEST_PRIZECODE_WHAT:
				Bundle bundle =	(Bundle) msg.obj;
				PrizeInfo info = (PrizeInfo) bundle.getSerializable("prize_info");
				int position = bundle.getInt("position");
				requestPrizeCode(info.getPrize(), SettingsManager.getUserId(getApplicationContext()),position);
				break;
			case REQUEST_GIFTCODE_WHAT:
				PrizeInfo info1 = (PrizeInfo) msg.obj;
				int position1 = msg.arg1;
				requestGiftCode(position1, info1.getGift_id());
				break;
			case REQUEST_LOTTERY_DETAILS_WHAT:
				PrizeInfo info2 = (PrizeInfo)msg.obj;
				int position2 = msg.arg1;
				requestLotteryDetails(position2,info2.getName());
				break;
			case REQUEST_LOTTERYCODE_WHAT:
				PrizeInfo info3 = (PrizeInfo)msg.obj;
				int position3 = msg.arg1;
				requestLotteryCode(position3,info3.getId(),SettingsManager.getUserId(getApplicationContext()));
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_gifts_layout);
		findViews();
		handler.sendEmptyMessage(REQUEST_PRIZELIST_WHAT);
	}

	@Override
	protected void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);//友盟统计页面跳转
		UMengStatistics.statisticsResume(this);//友盟统计时长
	}

	@Override
	protected void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);//友盟统计页面跳转
		UMengStatistics.statisticsPause(this);//友盟统计时长
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("我的礼品");

		mainLayout = (LinearLayout)findViewById(R.id.my_gift_layout_mainlayout);
		mainRefreshLayout = (RefreshLayout)findViewById(R.id.mygifts_reflayout);
		giftList = (ListView)findViewById(R.id.mygifts_listview);
		promptText = (TextView)findViewById(R.id.mygifts_prompt);
		initListeners();
	}

	//.
	private void initListeners(){
		prizeAdapter = new PrizeAdapter(MyGiftsActivity.this, new PrizeAdapter.OnItemMRCJListener() {
			@Override
			public void onClick(int position, PrizeInfo prize) {
				if(prize.getProduct_detail() != null && !"".equals(prize.getProduct_detail())){
					showLotteryDetailsDialog(prize);
				}else{
					Util.toastLong(MyGiftsActivity.this,"暂无数据");
				}
			}
		});
		giftList.setAdapter(prizeAdapter);
		// 设置下拉刷新监听器  
		mainRefreshLayout.setOnRefreshListener(new OnRefreshListener() {  
            @Override  
            public void onRefresh() {  
            	mainRefreshLayout.postDelayed(new Runnable() {  
                    @Override  
                    public void run() {  
                    	pageNo = 0;
                    	updateLoadStatus(true, false);
                        // 更新数据  
                        handler.sendEmptyMessage(REQUEST_PRIZELIST_WHAT);
                        // 更新完后调用该方法结束刷新  
                        mainRefreshLayout.setRefreshing(false);  
                    }  
                }, 1000);  
            }  
        });  
  
        // 加载监听器  
		mainRefreshLayout.setOnLoadListener(new OnLoadListener() {  
            @Override  
            public void onLoad() {  
            	mainRefreshLayout.postDelayed(new Runnable() {  
                    @Override  
                    public void run() {  
                    	++pageNo;
                    	updateLoadStatus(false, true);
                    	handler.sendEmptyMessage(REQUEST_PRIZELIST_WHAT);
                        // 加载完后调用该方法  
                    	mainRefreshLayout.setLoading(false);  
                    }  
                }, 1500);  
            }  
        });  
	}
	
	private void updateLoadStatus(boolean isRef,boolean isload){
		this.isUpdated = isRef;
		this.isLoadMore = isload;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		default:
			break;
		}
	}

	private void showLotteryDetailsDialog(PrizeInfo info){
			View popView = LayoutInflater.from(this).inflate(R.layout.lottery_detail_poplayout, null);
			int[] screen = SettingsManager.getScreenDispaly(MyGiftsActivity.this);
			int width = screen[0]*4/5;
			int height = screen[1]*2/5;
			LotteryDetailPopwindow popwindow = new LotteryDetailPopwindow(MyGiftsActivity.this,popView, width, height);
			popwindow.show(mainLayout,info);
	}

	private void updatePrizeAdapter(List<PrizeInfo> list){
		prizeAdapter.setItems(list);
	}
	
	//将会员福利计划活动的记录再次请求接口将编码字段加进去。
	private void addPrizeCode(List<PrizeInfo> listPrize){
		List<PrizeInfo> list1 = listPrize;
		//根据时间排序
//		Collections.sort(list1,new Comparator<PrizeInfo>() {
//			@Override
//			public int compare(PrizeInfo info1, PrizeInfo info2) {
//				return info2.getAdd_time().compareTo(info1.getAdd_time());
//			}
//		});
		updatePrizeAdapter(list1);
		for(int i=0;i<list1.size();i++){
			PrizeInfo prizeInfo = list1.get(i);
			if("HYFL_01".equals(prizeInfo.getActive_title())){
				Message msg = handler.obtainMessage(REQUEST_PRIZECODE_WHAT);
				Bundle bundle = new Bundle();
				bundle.putInt("position", i);
				bundle.putSerializable("prize_info", prizeInfo);
				msg.obj = bundle;
				handler.sendMessage(msg);
			}else if("HYFL_02".equals(prizeInfo.getActive_title())){
				Message msg = handler.obtainMessage(REQUEST_GIFTCODE_WHAT);
				msg.obj = prizeInfo;
				msg.arg1 = i;
				handler.sendMessage(msg);
			}else if("MRCJ".equals(prizeInfo.getActive_title()) || "MZLY".equals(prizeInfo.getActive_title())){
				//微信公众号活动 -- 每日抽奖 每周礼遇
				Message msg = handler.obtainMessage(REQUEST_LOTTERY_DETAILS_WHAT);
				msg.obj = prizeInfo;
				msg.arg1 = i;
				handler.sendMessage(msg);
			}
		}
	}

	/**
	 * 获取奖品详情
	 * @param name
     */
	private void requestLotteryDetails(final int position,String name){
		AsyncLottery lotteryTask = new AsyncLottery(MyGiftsActivity.this, "", name, "", new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if (resultCode == 0) {
						PrizeInfo prizeInfo = baseInfo.getmPrizeInfo();
						prizeTotalList.get(position).setProduct_detail(prizeInfo.getProduct_detail());
						if("是".equals(prizeInfo.getHas_code())){
							Message msg = handler.obtainMessage(REQUEST_LOTTERYCODE_WHAT);
							msg.obj = prizeInfo;
							msg.arg1 = position;
							handler.sendMessage(msg);
						}else{
							updatePrizeAdapter(prizeTotalList);
						}
					}
				}
			}
		});
		lotteryTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 我的礼品列表
	 * @param userId
	 * @param source
	 */
	private void requestPrizeList(String userId,String source){
		if(mLoadingDialog != null && isFrist){
			mLoadingDialog.show();
		}
		AsyncPrizeList prizeListTask = new AsyncPrizeList(MyGiftsActivity.this, userId, String.valueOf(pageNo), String.valueOf(pageSize), source,"",
				new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null){
							mLoadingDialog.dismiss();
						}
						isFrist = false;
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								mainRefreshLayout.setVisibility(View.VISIBLE);
								promptText.setVisibility(View.GONE);
								
								List<PrizeInfo> overTimeList = null;
								List<PrizeInfo> unusedList = null;
								List<PrizeInfo> usedList = null;
								PrizePageInfo pageInfo = baseInfo.getmPrizePageInfo();
								if(pageInfo != null){
									overTimeList = pageInfo.getOverTimeList();
									unusedList = pageInfo.getUnUsedList();
									usedList = pageInfo.getUsedList();
								}
								if(isUpdated){
									prizeTotalList.clear();	
								}
								if(unusedList != null){
									prizeTotalList.addAll(unusedList);
								}
								if(usedList != null){
									prizeTotalList.addAll(usedList);
								}
								if(overTimeList != null){
									prizeTotalList.addAll(overTimeList);
								}
								addPrizeCode(prizeTotalList);
							}else{
								if(isLoadMore){
									mainRefreshLayout.setVisibility(View.VISIBLE);
									promptText.setVisibility(View.GONE);
								}else{
									mainRefreshLayout.setVisibility(View.GONE);
									promptText.setVisibility(View.VISIBLE);
								}
							}
						}else{
							if(isLoadMore){
								mainRefreshLayout.setVisibility(View.VISIBLE);
								promptText.setVisibility(View.GONE);
							}else{
								mainRefreshLayout.setVisibility(View.GONE);
								promptText.setVisibility(View.VISIBLE);
							}
						}
					}
		});
		prizeListTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 礼品兑换券
	 * @param lotteryId
	 * @param userId
     */
	private void requestLotteryCode(final int position,String lotteryId,String userId){
		AsyncLotteryCode lotteryCodeTask = new AsyncLotteryCode(MyGiftsActivity.this, lotteryId, userId, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						LotteryCodeInfo codeInfo = baseInfo.getmLotteryCodeInfo();
						prizeTotalList.get(position).setPrize_code(codeInfo.getLottery_code());
					}
					updatePrizeAdapter(prizeTotalList);
				}
			}
		});
		lotteryCodeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 获取优惠券详情
	 * @param prizeName
	 * @param userId
	 */
	private void requestPrizeCode(String prizeName,String userId,final int position){
		AsyncPrizeCode task = new AsyncPrizeCode(MyGiftsActivity.this, "", "", prizeName, "", "", userId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								PrizeCodeInfo info = baseInfo.getmPrizeCodeInfo();
								prizeTotalList.get(position).setPrize_code(info.getPrize_code());
							}
							updatePrizeAdapter(prizeTotalList);
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 有券码的礼品是否已经领取过
	 * @param position
	 */
	private void requestGiftCode(final int position,String giftid){
		AsyncGiftCode giftCodeTask = new AsyncGiftCode(MyGiftsActivity.this, "", giftid,
				SettingsManager.getUserId(getApplicationContext()),"", new OnCommonInter(){
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						GiftCodeInfo info = baseInfo.getmGiftCodeInfo();
						prizeTotalList.get(position).setPrize_code(info.getGift_code());
					}else{
						prizeTotalList.get(position).setPrize_code("");
					}
					updatePrizeAdapter(prizeTotalList);
				}
			}
		});
		giftCodeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
