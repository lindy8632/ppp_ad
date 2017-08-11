package com.ylfcf.ppp.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.GiftAdapter;
import com.ylfcf.ppp.adapter.GiftAdapter.OnGiftItemClickListener;
import com.ylfcf.ppp.async.AsyncFLJHReceivePrize;
import com.ylfcf.ppp.async.AsyncGiftCode;
import com.ylfcf.ppp.async.AsyncHDGiftList;
import com.ylfcf.ppp.async.AsyncHDPrizeList;
import com.ylfcf.ppp.async.AsyncPrize;
import com.ylfcf.ppp.async.AsyncPrizeCode;
import com.ylfcf.ppp.async.AsyncXCFLActiveTime;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.GiftCodeInfo;
import com.ylfcf.ppp.entity.GiftInfo;
import com.ylfcf.ppp.entity.GiftPageInfo;
import com.ylfcf.ppp.entity.PrizeInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.URLGenerator;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.view.InvitateFriendsPopupwindow;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 会员福利计划2期
 * @author Mr.liu
 *
 */
public class PrizeRegion2TempActivity extends BaseActivity implements OnClickListener{
	private static final int REQUEST_PRIZELIST_WHAT = 5410;
	private static final int REQUEST_PRIZELIST_SUCCESS = 5411;
	
	private static final int REQUEST_ACTIVE_ISSTART_WHAT = 5412;//判断活动是否已经开始
	private static final int REQUEST_GIFT_ISGETOVER_HASCODE_WHAT = 5413;//判断有券码的礼品是否已经领取完毕
	private static final int REQUEST_GIFT_ISGETOVER_NOHASCODE_WHAT = 5416;//判断无券码的礼品是否已经领取完毕
//	private static final int REQUEST_GIFT_ISGET_BYNAME_WHAT = 5414;//根据礼品名字判断某个用户是否领取过该奖品
	private static final int REQUEST_GIFTHASCODE_ISGET_WHAT = 5415;//判断用户是否已经领取过带券码的礼品
	private static final int REQUEST_GIFTNOCODE_ISGET_WHAT = 5416;//判断用户是否已经领取过不带券码的礼品
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private LinearLayout mainLayout;
	private ListView mListView;
	private GiftAdapter mGiftAdapter;
	private List<GiftInfo> giftListTemp = new ArrayList<GiftInfo>();//礼品的临时集合，用于刷新adapter
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	private String systemTime = null;//系统当前时间
	private int page = 0;
	private int pageSize = 50;
	private View topView;
	private View bottomView;//
	private Button catMoreBtn,shareBtn;
	private LayoutInflater mLayoutInflater;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_PRIZELIST_WHAT:
				getGiftList("电子券", "是");
				break;
			case REQUEST_PRIZELIST_SUCCESS:
				break;
			case REQUEST_ACTIVE_ISSTART_WHAT:
				requestActiveTime("HYFL_02");
				break;
			case REQUEST_GIFT_ISGETOVER_HASCODE_WHAT:
				//判断有券码的礼品是否已经领取完毕
				GiftInfo info = (GiftInfo) msg.obj;
				int position = msg.arg1;
				requestGiftHasCodeIsGetOver(position, info.getId(),"未领取");
				break;
			case REQUEST_GIFTNOCODE_ISGET_WHAT:
				GiftInfo giftNocode = (GiftInfo) msg.obj;
				int positionNocode = msg.arg1;
				requestPrizeInfoByName(positionNocode,SettingsManager.getUserId(getApplicationContext()), giftNocode.getName());
				break;
			case REQUEST_GIFTHASCODE_ISGET_WHAT:
				GiftInfo gift = (GiftInfo) msg.obj;
				int positionG = msg.arg1;
				requestGiftHasCodeIsGet(positionG, gift.getId(), SettingsManager.getUserId(getApplicationContext()));
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
		setContentView(R.layout.prize_region2_temp_activity);
		systemTime = sdf.format(new Date());
		mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		findViews();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		handler.sendEmptyMessage(REQUEST_PRIZELIST_WHAT);
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("会员礼品专区");
		
		mainLayout = (LinearLayout) findViewById(R.id.prize_region2_temp_activity_mainlayout);
		topView = mLayoutInflater.inflate(R.layout.prize_region2_toplayout, null);
		bottomView = mLayoutInflater.inflate(R.layout.prize_region2_bottomlayout, null);
		catMoreBtn = (Button) bottomView.findViewById(R.id.prize_region2_bottom_catmore_btn);
		catMoreBtn.setOnClickListener(this);
		shareBtn = (Button) bottomView.findViewById(R.id.prize_region2_bottom_share_btn);
		shareBtn.setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.prize_region2_temp_activity_listview);
		mListView.addHeaderView(topView);
		mListView.addFooterView(bottomView);
		mGiftAdapter = new GiftAdapter(PrizeRegion2TempActivity.this,new OnGiftItemClickListener() {
			@Override
			public void onclick(GiftInfo info, int position) {
				//
				if(info.isLogin()){
					//如果已经登录，说明这个按钮是领取
					try {
						checkReceivePrize(info,position);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}else{
					//未登录跳转到登录页面
					Intent intent = new Intent(PrizeRegion2TempActivity.this,LoginActivity.class);
					intent.putExtra("FLAG", "HYFL_02");
					startActivity(intent);
				}
			}

			@Override
			public void ruleOnClick(View v) {
				
			}
		});
		mListView.setAdapter(mGiftAdapter);
	}
	
	private void updateAdapter(List<GiftInfo> giftList,boolean isFirstReresh){
		mGiftAdapter.setItems(giftList,isFirstReresh);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.prize_region2_bottom_catmore_btn:
			//查看更多活动
			Intent intent = new Intent(PrizeRegion2TempActivity.this,ActivitysRegionActivity.class);
			startActivity(intent);
			break;
		case R.id.prize_region2_bottom_share_btn:
			//分享
			showFriendsSharedWindow();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

	/**
	 * 活动已结束，初始化布局
	 */
	private void initActiveEndData(){
		if(mLoadingDialog != null){
			mLoadingDialog.dismiss();
		}
		for(int i=0;i<giftListTemp.size();i++){
			giftListTemp.get(i).setIsStart(1);
		}
		updateAdapter(giftListTemp,false);
	}
	
	/**
	 * 活动还未开始，初始化布局
	 */
	private void initActiveUnstartData(){
		if(mLoadingDialog != null){
			mLoadingDialog.dismiss();
		}
		for(int i=0;i<giftListTemp.size();i++){
			giftListTemp.get(i).setIsStart(-1);
		}
		updateAdapter(giftListTemp,false);
	}
	
	/**
	 * 活动已经开始，初始化布局
	 */
	int hasCodeGL = 0;//有券码的礼品个数
	int noCodeGL = 0;//没券码的礼品个数
	private void initActiveStartData(){
		hasCodeGL = 0;
		noCodeGL = 0;
		boolean isLogin = !SettingsManager.getLoginPassword(
				PrizeRegion2TempActivity.this).isEmpty()
				&& !SettingsManager.getUser(PrizeRegion2TempActivity.this)
						.isEmpty();
		for(int i=0;i<giftListTemp.size();i++){
			if("是".equals(giftListTemp.get(i).getHas_code())){
				hasCodeGL++;
			}
		}
		noCodeGL = giftListTemp.size() - hasCodeGL;
		for(int i=0;i<giftListTemp.size();i++){
			giftListTemp.get(i).setIsStart(0);
			if(isLogin){
				giftListTemp.get(i).setLogin(true);
				if(SettingsManager.isPersonalUser(getApplicationContext())){
					if("是".equals(giftListTemp.get(i).getHas_code())){
						Message msg = handler.obtainMessage(REQUEST_GIFT_ISGETOVER_HASCODE_WHAT);
						msg.obj = giftListTemp.get(i);
						msg.arg1 = i;
						handler.sendMessage(msg);
					}else{
						//没有券码的礼品不用判断有没有领完,直接判断有没有领过
						Message msg = handler.obtainMessage(REQUEST_GIFTNOCODE_ISGET_WHAT);
						msg.obj = giftListTemp.get(i);
						msg.arg1 = i;
						handler.sendMessage(msg);
					}
				}else{
					mLoadingDialog.dismiss();
					updateAdapter(giftListTemp,false);
				}
			}else{
				if(mLoadingDialog != null){
					mLoadingDialog.dismiss();
				}
				giftListTemp.get(i).setLogin(false);
				updateAdapter(giftListTemp,false);
			}
		}
	}
	
	//判断当天是否已经领取过礼品，是否可以继续领取。根据GiftInfo 里面的get_time字段来判断，如果为今天，说明今天已经不能再领了。
	boolean isGet = false;
	private void checkReceivePrize(GiftInfo info,int position) throws ParseException{
		isTodayGetGift(info);
	}
	
	/**
	 * 弹出分享的提示框
	 */
	private void showFriendsSharedWindow() {
		View popView = LayoutInflater.from(this).inflate(
				R.layout.invitate_friends_popupwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(PrizeRegion2TempActivity.this);
		int width = screen[0];
		int height = screen[1] / 5 * 2;
		InvitateFriendsPopupwindow popwindow = new InvitateFriendsPopupwindow(PrizeRegion2TempActivity.this,
				popView, width, height);
		popwindow.show(mainLayout,URLGenerator.HYFL02_WAP_URL,"会员福利二期",null);
	}
	
	/**
	 * Dialog
	 */
	private void showPromptDialog(final String type){
		View contentView = LayoutInflater.from(PrizeRegion2TempActivity.this).inflate(R.layout.prize_region_prompt_dialog, null);
		final TextView topText = (TextView) contentView.findViewById(R.id.prize_region_prompt_dialog_top_text);
		final TextView bottomText = (TextView) contentView.findViewById(R.id.prize_region_prompt_dialog_bottom_text);
		final ImageView delBtn = (ImageView) contentView.findViewById(R.id.prize_region_prompt_dialog_delbtn);
		final Button btn = (Button) contentView.findViewById(R.id.prize_region_prompt_dialog_btn);
		if("领取成功".equals(type)){
			topText.setVisibility(View.VISIBLE);
			topText.setText("恭喜您领取成功！");
			bottomText.setText("能让您喜欢是我的荣幸！");
			bottomText.setTextColor(getResources().getColor(R.color.gray));
			btn.setText("查看礼品");
		}else if("已领过".equals(type)){
			topText.setVisibility(View.VISIBLE);
			topText.setText("今天您已经领过礼品了，");
			bottomText.setText("记得明天再来！");
			bottomText.setTextColor(getResources().getColor(R.color.red));
			btn.setText("确定");
		}else{
			topText.setVisibility(View.GONE);
			bottomText.setText(type);
			btn.setText("我知道了");
		}
		AlertDialog.Builder builder=new AlertDialog.Builder(PrizeRegion2TempActivity.this, R.style.Dialog_Transparent);  //先得到构造器  
        builder.setView(contentView);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if("领取成功".equals(type)){
					Intent intentPrizeRecord = new Intent(PrizeRegion2TempActivity.this,MyGiftsActivity.class);
					startActivity(intentPrizeRecord);
				}
				dialog.dismiss();
			}
		});
        delBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
        dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if("领取成功".equals(type)){
					//刷新界面
					handler.sendEmptyMessage(REQUEST_PRIZELIST_WHAT);
				}
			}
		});
        //参数都设置完成了，创建并显示出来  
        dialog.show();  
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*4/5;
        dialog.getWindow().setAttributes(lp);
	}
	
	/**
	 * 判断活动是否已经开始
	 * @param activeTitle
	 */
	private void requestActiveTime(String activeTitle){
		AsyncXCFLActiveTime task = new AsyncXCFLActiveTime(PrizeRegion2TempActivity.this, activeTitle, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if (resultCode == 0) {
								//活动已开始
								initActiveStartData();
							} else if (resultCode == -3) {
								//活动结束
								initActiveEndData();
							} else if (resultCode == -2) {
								//活动还没开始
								initActiveUnstartData();
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 领取礼品列表
	 * @param type
	 * @param isShow
	 */
	private void getGiftList(String type,String isShow){
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		AsyncHDGiftList giftListTask = new AsyncHDGiftList(PrizeRegion2TempActivity.this, 
				type, isShow, String.valueOf(page),String.valueOf(pageSize),new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					systemTime = baseInfo.getTime();
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						GiftPageInfo pageInfo = baseInfo.getmGiftPageInfo();
						if(pageInfo != null){
							giftListTemp.clear();
							giftListTemp.addAll(pageInfo.getGiftList());
							for(int i=0;i<giftListTemp.size();i++){
								String picURL = "";
								try {
									Document doc = Jsoup.parse(Html.fromHtml(giftListTemp.get(i).getPic()).toString());
									Elements elements = doc.getElementsByTag("img");
									picURL = elements.attr("src");
									giftListTemp.get(i).setPic(picURL);
								} catch (Exception e) {
								}
							}
							handler.sendEmptyMessage(REQUEST_ACTIVE_ISSTART_WHAT);
						}else{
							mLoadingDialog.dismiss();
						}
					}else{
						mLoadingDialog.dismiss();
					}
				}
			}
		});
		giftListTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 判断该用户是否已经领取该奖品
	 * @param userId
	 * @param prizeName
	 */
	int prizeM = 0;
	private void requestPrizeInfoByName(final int position,String userId,final String prizeName){
		AsyncPrize prizeTask = new AsyncPrize(PrizeRegion2TempActivity.this, userId, prizeName, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				++prizeM;
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						PrizeInfo info = baseInfo.getmPrizeInfo();
						//已经领取
						if("否".equals(giftListTemp.get(position).getHas_code())){
							giftListTemp.get(position).setGet(true);
							giftListTemp.get(position).setGet_time(info.getAdd_time());
						}
					}else{
						//未领取
						if("否".equals(giftListTemp.get(position).getHas_code())){
							giftListTemp.get(position).setGet(false);
							giftListTemp.get(position).setGet_time(null);
						}
					}
				}
				if(mLoadingDialog != null){
					mLoadingDialog.dismiss();
				}
				if(prizeM >= noCodeGL - 1){
					updateAdapter(giftListTemp, false);
					prizeM = 0;
				}
			}
		});
		prizeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 判断带券码的礼品是否已经领取完毕
	 * @param giftId
	 * @param userId
	 */
	int getoverM = 0;
	private void requestGiftHasCodeIsGetOver(final int position,String giftId,String status){
		AsyncGiftCode giftCodeTask = new AsyncGiftCode(PrizeRegion2TempActivity.this, "",giftId, "",status, new OnCommonInter(){
			@Override
			public void back(BaseInfo baseInfo){
				++getoverM;
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						//奖品未领完
						if("是".equals(giftListTemp.get(position).getHas_code())){
							giftListTemp.get(position).setGetOver(false);
							//再判断该用户是否领过
							Message msg = handler.obtainMessage(REQUEST_GIFTHASCODE_ISGET_WHAT);
							msg.obj = giftListTemp.get(position);
							msg.arg1 = position;
							handler.sendMessage(msg);
							YLFLogger.d(giftListTemp.get(position).getName()+"未领完。");
						}
					}else{
						if(mLoadingDialog != null){
							mLoadingDialog.dismiss();
						}
						//奖品已经领完
						giftListTemp.get(position).setGetOver(true);
						YLFLogger.d(giftListTemp.get(position).getName()+"已领完。");
					}
					if(getoverM >= hasCodeGL - 1){
						updateAdapter(giftListTemp,false);
						getoverM = 0;
					}
				}
			}
		});
		giftCodeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 有券码的礼品是否已经领取过
	 * @param position
	 * @param giftId
	 * @param userId
	 */
	int isGetI = 0;
	private void requestGiftHasCodeIsGet(final int position,String giftId,String userId){
		AsyncGiftCode giftCodeTask = new AsyncGiftCode(PrizeRegion2TempActivity.this, "",giftId, userId,"", new OnCommonInter(){
			@Override
			public void back(BaseInfo baseInfo) {
				++isGetI;
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						//已经领过
						GiftCodeInfo info = baseInfo.getmGiftCodeInfo();
						if("是".equals(giftListTemp.get(position).getHas_code())){
							giftListTemp.get(position).setGet(true);
							giftListTemp.get(position).setGet_time(info.getAdd_time());
						}
					}else{
						//还未领取
						if("是".equals(giftListTemp.get(position).getHas_code())){
							giftListTemp.get(position).setGet(false);
							giftListTemp.get(position).setGet_time(null);
						}
					}
				}
				if(isGetI >= hasCodeGL - 1){
					updateAdapter(giftListTemp,false);
					isGetI = 0;
				}
				if(mLoadingDialog != null){
					mLoadingDialog.dismiss();
				}
			}
		});
		giftCodeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 判断今天是否已经领取过奖品。
	 * @param position
	 * @param userId
	 */
	private void requestIsGetGiftToday(final int position,final GiftInfo giftInfo,String userId){
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		AsyncGiftCode giftCodeTask = new AsyncGiftCode(PrizeRegion2TempActivity.this, "",giftInfo.getId(), userId,"", new OnCommonInter(){
			@Override
			public void back(BaseInfo baseInfo) {
				if(mLoadingDialog != null){
					mLoadingDialog.dismiss();
				}
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						//已经领过
						GiftCodeInfo info = baseInfo.getmGiftCodeInfo();
						Date dateNow = null;
						try {
							dateNow = sdf1.parse(systemTime);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						Date getDate = null;
						try {
							getDate = sdf1.parse(info.getAdd_time());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if(dateNow.compareTo(getDate) == 0){
							//说明是同一天
							showPromptDialog("已领过");
						}else{
							fljhReceivePrize(SettingsManager.getUserId(getApplicationContext()), giftInfo.getName(), info.getId(),"HYFL_02", 
									"会员福利", "福利计划", "已兑换", SettingsManager.USER_FROM);
						}
					}else{
						//还未领取
						if("是".equals(giftListTemp.get(position).getHas_code())){
							giftListTemp.get(position).setGet(false);
							giftListTemp.get(position).setGet_time(null);
						}
						fljhReceivePrize(SettingsManager.getUserId(getApplicationContext()), giftInfo.getName(), giftInfo.getId(),"HYFL_02", 
								"会员福利", "福利计划", "已兑换", SettingsManager.USER_FROM);
					}
					updateAdapter(giftListTemp,false);
				}
				if(mLoadingDialog != null){
					mLoadingDialog.dismiss();
				}
			}
		});
		giftCodeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	private void isTodayGetGift(final GiftInfo giftInfo){
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		AsyncHDPrizeList hdprizeTask = new AsyncHDPrizeList(PrizeRegion2TempActivity.this, 
				SettingsManager.getUserId(getApplicationContext()), "HYFL_02", String.valueOf(page), String.valueOf(pageSize), new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						boolean isget = false;
						if(mLoadingDialog != null){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								List<PrizeInfo> prizeList = baseInfo.getmHDPrizePageInfo().getPrizeList();
								Date dateNow = null;
								try {
									dateNow = sdf1.parse(systemTime);
								} catch (ParseException e) {
									e.printStackTrace();
								}
								for(int i=0;i<prizeList.size();i++){
									PrizeInfo info = prizeList.get(i);
									Date getDate = null;
									try {
										getDate = sdf1.parse(info.getAdd_time());
									} catch (ParseException e) {
										e.printStackTrace();
									}
									if(dateNow.compareTo(getDate) == 0){
										//说明是同一天
										isget = true;
									}
								}
								if(isget){
									//领过
									showPromptDialog("已领过");
								}else{
									fljhReceivePrize(SettingsManager.getUserId(getApplicationContext()), giftInfo.getName(), giftInfo.getId(),"HYFL_02", 
											"会员福利", "福利计划", "已兑换", SettingsManager.USER_FROM);
								}
							}else{
								fljhReceivePrize(SettingsManager.getUserId(getApplicationContext()), giftInfo.getName(), giftInfo.getId(),"HYFL_02", 
										"会员福利", "福利计划", "已兑换", SettingsManager.USER_FROM);
							}
						}
					}
				});
		hdprizeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 请求该奖品是否已经领完
	 * @param position 位置
	 * @param prizeName
	 * @param status
	 */
	private void requestPrizeCodeSelectone(final int position,final String prizeName,String status){
		AsyncPrizeCode prizeCodeTask = new AsyncPrizeCode(PrizeRegion2TempActivity.this, "", 
				"", prizeName, status, "", "",new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
//							int resultCode = SettingsManager.getResultCode(baseInfo);
//							if(resultCode == 0){
//								//奖品未领完
//								giftListTemp.get(position).setGetOver(false);
//								//再判断该用户是否领过
//								Message msg = handler.obtainMessage(REQUEST_GIFT_ISGET_BYID_WHAT);
//								msg.obj = giftListTemp.get(position);
//								msg.arg1 = position;
//								handler.sendMessage(msg);
//							}else{
//								//奖品已经领完
//								giftListTemp.get(position).setGetOver(true);
//								if(position == giftListTemp.size() - 1){
//									updateAdapter(giftListTemp,false);
//								}
//							}
						}
					}
				});
		prizeCodeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 领奖
	 * @param userId
	 * @param prize
	 * @param activeTitle
	 * @param operatingRemark
	 * @param remark
	 * @param status
	 * @param source
	 */
	private void fljhReceivePrize(String userId,final String prize,String giftId,String activeTitle,String operatingRemark,
			String remark,String status,String source){
		AsyncFLJHReceivePrize task = new AsyncFLJHReceivePrize(PrizeRegion2TempActivity.this, userId, prize,giftId, activeTitle, operatingRemark,
				remark, status, source, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//领取成功
								showPromptDialog("领取成功");
							}else if(resultCode == -1){
								//已经领取
								showPromptDialog("已领过");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
