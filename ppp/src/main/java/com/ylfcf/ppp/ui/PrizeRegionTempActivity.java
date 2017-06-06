package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncFLJHReceivePrize;
import com.ylfcf.ppp.async.AsyncPrize;
import com.ylfcf.ppp.async.AsyncPrizeActive;
import com.ylfcf.ppp.async.AsyncPrizeCode;
import com.ylfcf.ppp.async.AsyncYYYInvestByTimeSpan;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.PrizeActiveInfo;
import com.ylfcf.ppp.entity.PrizeInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.Constants.PrizeName;
import com.ylfcf.ppp.util.Constants.UserType;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.URLGenerator;

import java.text.ParseException;
import java.util.Date;

/**
 * 会员礼品专区
 * 
 * @author Mr.liu
 * 
 */
public class PrizeRegionTempActivity extends BaseActivity implements OnClickListener{
	private static final int REQUEST_PRIZE_DETAILS_BY_NAME_VITA = 9019;
	private static final int REQUEST_PRIZE_DETAILS_BY_NAME_YOUHUASHAONV = 9020;//柚花少女礼品券
	private static final int REQUEST_PRIZE_DETAILS_BY_NAME_JKN = 9021;//此用户是否领取过金康尼
	
	private static final int REQUEST_PRIZECODE_DETAILS_JKN = 9022;//金康尼优惠券是否领完
	private static final int REQUEST_PRIZECODE_DETAILS_VITA = 9023;//VITA健身券是否领完
	private static final int REQUEST_PRIZECODE_DETAILS_YOUHUASHAONV = 9024;//柚花少女礼品券
	
	private static final int REQUEST_YYYINVEST_BYTIMESPAN = 9205;//判断用户在此活动内是否投资过元月盈
	
	private static final int REQUEST_RECEIVE_PRIZE_JKN = 9206;//领取金尼康
	private static final int REQUEST_RECEIVE_PRIZE_VITA = 9207;//领取VITA
	private static final int REQUEST_RECEIVE_PRIZE_YOUHUASHAONV = 9208;//柚花少女礼品券
	private LinearLayout topLeftBtn;
	private LinearLayout mainLayout;
	private TextView topTitleTV;
	
	private TextView ruleTV1,ruleTV2,ruleTV3,ruleTV4;//规则
	private Button getBtn1,getBtn2,getBtn3;//领取
	private ImageView img1,img2,img3;
	private RelativeLayout ruleLayout1,ruleLayout2,ruleLayout3,ruleLayout4;
	private RelativeLayout ruleDelBtn1,ruleDelBtn2,ruleDelBtn3,ruleDelBtn4;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_PRIZE_DETAILS_BY_NAME_JKN:
				requestPrizeInfoByName(SettingsManager.getUserId(getApplicationContext()), PrizeName.PRIZE_NAME_JINKANGNI);
				break;
			case REQUEST_PRIZE_DETAILS_BY_NAME_VITA:
				requestPrizeInfoByName(SettingsManager.getUserId(getApplicationContext()), PrizeName.PRIZE_NAME_VITA);
				break;
			case REQUEST_PRIZE_DETAILS_BY_NAME_YOUHUASHAONV:
				requestPrizeInfoByName(SettingsManager.getUserId(getApplicationContext()), PrizeName.PRIZE_NAME_YOUHUA);
				break;
			case REQUEST_PRIZECODE_DETAILS_JKN:
				requestPrizeCodeSelectone("", "", PrizeName.PRIZE_NAME_JINKANGNI, "未使用", "", "");
				break;
			case REQUEST_PRIZECODE_DETAILS_VITA:
				requestPrizeCodeSelectone("", "", PrizeName.PRIZE_NAME_VITA, "未使用", "", "");
				break;
			case REQUEST_PRIZECODE_DETAILS_YOUHUASHAONV:
				requestPrizeCodeSelectone("", "", PrizeName.PRIZE_NAME_YOUHUA, "未使用", "", "");
				break;
			case REQUEST_YYYINVEST_BYTIMESPAN:
				requestYYYInvestByTimeSpan(SettingsManager.getUserId(getApplicationContext()), SettingsManager.fljhStartDate, SettingsManager.fljhEndDate);
				break;
			case REQUEST_RECEIVE_PRIZE_JKN:
				fljhReceivePrize(SettingsManager.getUserId(getApplicationContext()), PrizeName.PRIZE_NAME_JINKANGNI, "","HYFL_01", 
						"投资元月盈领取", "福利计划", "已兑换", SettingsManager.USER_FROM);
				break;
			case REQUEST_RECEIVE_PRIZE_VITA:
				fljhReceivePrize(SettingsManager.getUserId(getApplicationContext()), PrizeName.PRIZE_NAME_VITA, "","HYFL_01", 
						"注册领取", "福利计划", "已兑换", SettingsManager.USER_FROM);
				break;
			case REQUEST_RECEIVE_PRIZE_YOUHUASHAONV:
				fljhReceivePrize(SettingsManager.getUserId(getApplicationContext()), PrizeName.PRIZE_NAME_YOUHUA,"", "HYFL_01", 
						"注册领取", "福利计划", "已兑换", SettingsManager.USER_FROM);
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
		setContentView(R.layout.prize_region_temp_activity);
		
		findViews();
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("会员礼品专区");
		
		mainLayout = (LinearLayout) findViewById(R.id.dzp_temp_activity_mainlayout);
		ruleTV1 = (TextView) findViewById(R.id.prize_region_temp_activity_first_text);
		ruleTV1.setOnClickListener(this);
		ruleTV2 = (TextView) findViewById(R.id.prize_region_temp_activity_sec_text);
		ruleTV2.setOnClickListener(this);
		ruleTV3 = (TextView) findViewById(R.id.prize_region_temp_activity_third_text);
		ruleTV3.setOnClickListener(this);
		ruleTV4 = (TextView) findViewById(R.id.prize_region_temp_activity_fourth_text);
		ruleTV4.setOnClickListener(this);
		ruleLayout1 = (RelativeLayout) findViewById(R.id.prize_region_temp_activity_rule_layout1);
		ruleLayout2 = (RelativeLayout) findViewById(R.id.prize_region_temp_activity_rule_layout2);
		ruleLayout3 = (RelativeLayout) findViewById(R.id.prize_region_temp_activity_rule_layout3);
		ruleLayout4 = (RelativeLayout) findViewById(R.id.prize_region_temp_activity_rule_layout4);
		ruleDelBtn1 = (RelativeLayout) findViewById(R.id.prize_region_temp_activity_rule1_delbtn1);
		ruleDelBtn1.setOnClickListener(this);
		ruleDelBtn2 = (RelativeLayout) findViewById(R.id.prize_region_temp_activity_rule1_delbtn2);
		ruleDelBtn2.setOnClickListener(this);
		ruleDelBtn3 = (RelativeLayout) findViewById(R.id.prize_region_temp_activity_rule1_delbtn3);
		ruleDelBtn3.setOnClickListener(this);
		ruleDelBtn4 = (RelativeLayout) findViewById(R.id.prize_region_temp_activity_rule1_delbtn4);
		ruleDelBtn4.setOnClickListener(this);
		
		getBtn1 = (Button) findViewById(R.id.prize_region_temp_activity_first_btn);
		getBtn1.setOnClickListener(this);
		getBtn2 = (Button) findViewById(R.id.prize_region_temp_activity_sec_btn);
		getBtn2.setOnClickListener(this);
		getBtn3 = (Button) findViewById(R.id.prize_region_temp_activity_third_btn);
		getBtn3.setOnClickListener(this);
		img1 = (ImageView) findViewById(R.id.prize_region_temp_activity_bottom_first_image);
		img1.setOnClickListener(this);
		img2 = (ImageView) findViewById(R.id.prize_region_temp_activity_bottom_second_image);
		img2.setOnClickListener(this);
		img3 = (ImageView) findViewById(R.id.prize_region_temp_activity_bottom_third_image);
		img3.setOnClickListener(this);
		
		if(SettingsManager.checkTwoYearsTZFXActivity()){
			//两周年立赚返现活动正在进行中
			img1.setBackgroundResource(R.drawable.prize_region_first_image_start);
		}else{
			img1.setBackgroundResource(R.drawable.prize_region_first_image_end);
		}
		
		if(SettingsManager.checkXSMBActivity(new Date()) == 0){
			//限时秒标活动进行中
			img2.setBackgroundResource(R.drawable.prize_region_second_image_start);
		}else if(SettingsManager.checkXSMBActivity(new Date()) == -1){
			img2.setBackgroundResource(R.drawable.prize_region_second_image_end);
		}else if(SettingsManager.checkXSMBActivity(new Date()) == 1){
			img2.setBackgroundResource(R.drawable.prize_region_second_image_qidai);
		}
		
		if(SettingsManager.checkXCHBActivity(new Date()) == 0){
			//新春红包活动进行中
			img3.setBackgroundResource(R.drawable.prize_region_third_image_start);
		}else if(SettingsManager.checkXCHBActivity(new Date()) == -1){
			//活动结束
			img3.setBackgroundResource(R.drawable.prize_region_third_image_end);
		}else if(SettingsManager.checkXCHBActivity(new Date()) == 1){
			//活动还未开始
			img3.setBackgroundResource(R.drawable.prize_region_third_image_qidai);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		boolean isLogin = !SettingsManager.getLoginPassword(PrizeRegionTempActivity.this).isEmpty()
				&& !SettingsManager.getUser(PrizeRegionTempActivity.this).isEmpty();
		if(isLogin){
			initLoginedView();
		}else{
			initUnloginView();
		}
	}
	
	//已登录
	private void initLoginedView(){
		String userType = SettingsManager.getUserType(PrizeRegionTempActivity.this);
		if(UserType.USER_NORMAL_PERSONAL.equals(userType) || UserType.USER_VIP_PERSONAL.equals(userType)){
			//个人用户
			getBtn1.setTextColor(getResources().getColor(R.color.prize_region_color2));
			getBtn1.setEnabled(true);
			getBtn1.setText("领 取");
			getBtn2.setTextColor(getResources().getColor(R.color.prize_region_color2));
			getBtn2.setEnabled(true);
			getBtn2.setText("领 取");
			getBtn3.setTextColor(getResources().getColor(R.color.prize_region_color2));
			getBtn3.setEnabled(true);
			getBtn3.setText("领 取");
			requestPrizeActive("HYFL_01", "");
		}else if(UserType.USER_COMPANY.equals(userType)){
			//企业用户
			getBtn1.setTextColor(getResources().getColor(R.color.gray));
			getBtn1.setEnabled(false);
			getBtn1.setText("领 取");
			getBtn2.setTextColor(getResources().getColor(R.color.gray));
			getBtn2.setEnabled(false);
			getBtn2.setText("领 取");
			getBtn3.setTextColor(getResources().getColor(R.color.gray));
			getBtn3.setEnabled(false);
			getBtn3.setText("领 取");
		}
	}
	
	//游客状态
	private void initUnloginView(){
		getBtn1.setEnabled(true);
		getBtn1.setText("请登录");
		getBtn1.setTextColor(getResources().getColor(R.color.prize_region_color2));
		getBtn2.setEnabled(true);
		getBtn2.setText("请登录");
		getBtn2.setTextColor(getResources().getColor(R.color.prize_region_color2));
		getBtn3.setEnabled(true);
		getBtn3.setText("请登录");
		getBtn3.setTextColor(getResources().getColor(R.color.prize_region_color2));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.prize_region_temp_activity_first_text:
			showRuleDialog(1);
			break;
		case R.id.prize_region_temp_activity_sec_text:
			showRuleDialog(2);
			break;
		case R.id.prize_region_temp_activity_third_text:
			showRuleDialog(3);
			break;
		case R.id.prize_region_temp_activity_fourth_text:
			showRuleDialog(4);
			break;
		case R.id.prize_region_temp_activity_rule1_delbtn1:
			ruleLayout1.setVisibility(View.INVISIBLE);
			getBtn1.setVisibility(View.VISIBLE);
			break;
		case R.id.prize_region_temp_activity_rule1_delbtn2:
			ruleLayout2.setVisibility(View.INVISIBLE);
			getBtn2.setVisibility(View.VISIBLE);
			break;
		case R.id.prize_region_temp_activity_rule1_delbtn3:
			ruleLayout3.setVisibility(View.INVISIBLE);
			getBtn3.setVisibility(View.VISIBLE);
			break;
		case R.id.prize_region_temp_activity_rule1_delbtn4:
			ruleLayout4.setVisibility(View.INVISIBLE);
			break;
		case R.id.prize_region_temp_activity_first_btn:
			// 从SettingsManager中读取密码，如果为空意味着没有登录。
			boolean isLoginF = !SettingsManager.getLoginPassword(
					PrizeRegionTempActivity.this).isEmpty()
					&& !SettingsManager.getUser(PrizeRegionTempActivity.this).isEmpty();
			if(isLoginF){
				handler.sendEmptyMessage(REQUEST_RECEIVE_PRIZE_VITA);
			}else{
				Intent intent1 = new Intent(PrizeRegionTempActivity.this,LoginActivity.class);
				startActivity(intent1);
			}
			break;
		case R.id.prize_region_temp_activity_sec_btn:
			// 从SettingsManager中读取密码，如果为空意味着没有登录。
			boolean isLoginS = !SettingsManager.getLoginPassword(
					PrizeRegionTempActivity.this).isEmpty()
					&& !SettingsManager.getUser(PrizeRegionTempActivity.this).isEmpty();
			if(isLoginS){
				handler.sendEmptyMessage(REQUEST_RECEIVE_PRIZE_YOUHUASHAONV);
			}else{
				Intent intent2 = new Intent(PrizeRegionTempActivity.this,LoginActivity.class);
				startActivity(intent2);
			}
			break;
		case R.id.prize_region_temp_activity_third_btn:
			// 金康尼
			boolean isLoginT = !SettingsManager.getLoginPassword(
					PrizeRegionTempActivity.this).isEmpty()
					&& !SettingsManager.getUser(PrizeRegionTempActivity.this).isEmpty();
			if(isLoginT){
				//先判断有没有投资过元月盈
				handler.sendEmptyMessage(REQUEST_YYYINVEST_BYTIMESPAN);
			}else{
				Intent intent2 = new Intent(PrizeRegionTempActivity.this,LoginActivity.class);
				startActivity(intent2);
			}
			break;
		case R.id.prize_region_temp_activity_bottom_first_image:
			//两周年投资返现活动
			if(SettingsManager.checkTwoYearsTZFXActivity()){
				Intent intentBanner = new Intent(PrizeRegionTempActivity.this,BannerTopicActivity.class);
				BannerInfo bannerInfo = new BannerInfo();
				bannerInfo.setArticle_id("170");
				bannerInfo.setLink_url(URLGenerator.TWOYEARS_TZFX_URL);
				intentBanner.putExtra("BannerInfo", bannerInfo);
				startActivity(intentBanner);
				finish();
			}
			break;
		case R.id.prize_region_temp_activity_bottom_second_image:
			//限时秒标
			if(SettingsManager.checkXSMBActivity(new Date()) == 0){
				Intent intent = new Intent(PrizeRegionTempActivity.this,BorrowDetailXSMBActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.prize_region_temp_activity_bottom_third_image:
			//新春红包
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
	
	//规则
	private void showRuleDialog(int position){
		if(position == 1){
			ruleLayout1.setVisibility(View.VISIBLE);
			getBtn1.setVisibility(View.INVISIBLE);
		}else if(position == 2){
			ruleLayout2.setVisibility(View.VISIBLE);
			getBtn2.setVisibility(View.INVISIBLE);
		}else if(position == 3){
			ruleLayout3.setVisibility(View.VISIBLE);
			getBtn3.setVisibility(View.INVISIBLE);
		}else if(position == 4){
			ruleLayout4.setVisibility(View.VISIBLE);
		}
//		View popView = LayoutInflater.from(this).inflate(
//				R.layout.prize_region_rule_window, null);
//		int[] screen = SettingsManager.getScreenDispaly(this);
//		int width = screen[0] * 6 / 7;
//		int height = screen[1] * 2 / 7;
//		PrizeRegionRuleWindow popwindow = new PrizeRegionRuleWindow(this,
//				popView, width, height,position);
//		popwindow.show(mainLayout);
	}
	
	/**
	 * 判断活动是否开始
	 * @param baseInfo
	 * @throws ParseException 
	 */
	private void isFLJHActiveStart(BaseInfo baseInfo){
		PrizeActiveInfo activeInfo = baseInfo.getmPrizeActiveInfo();
		String nowTimeStr = baseInfo.getTime();
		String startTimeStr = activeInfo.getStart_time();
		String endTimeStr = activeInfo.getEnd_time();
		Date nowDate = null,startDate = null,endDate = null;
		try {
			nowDate = sdf.parse(nowTimeStr);
			startDate = sdf.parse(startTimeStr);
			endDate = sdf.parse(endTimeStr);
			if(nowDate.getTime() < startDate.getTime()){
				//活动未开始
				getBtn1.setTextColor(getResources().getColor(R.color.prize_region_color1));
				getBtn1.setEnabled(false);
				getBtn1.setText("敬请期待");
				getBtn1.setBackgroundResource(R.drawable.prize_region_btn_enable);
				getBtn2.setTextColor(getResources().getColor(R.color.prize_region_color1));
				getBtn2.setEnabled(false);
				getBtn2.setText("敬请期待");
				getBtn2.setBackgroundResource(R.drawable.prize_region_btn_enable);
				getBtn3.setTextColor(getResources().getColor(R.color.prize_region_color1));
				getBtn3.setEnabled(false);
				getBtn3.setText("敬请期待");
				getBtn3.setBackgroundResource(R.drawable.prize_region_btn_enable);
			}else if(nowDate.getTime() >= startDate.getTime() && nowDate.getTime() < endDate.getTime()){
				//活动正在进行中
				handler.sendEmptyMessageDelayed(REQUEST_PRIZECODE_DETAILS_JKN, 100l);
				handler.sendEmptyMessageDelayed(REQUEST_PRIZECODE_DETAILS_VITA, 200l);
				handler.sendEmptyMessageDelayed(REQUEST_PRIZECODE_DETAILS_YOUHUASHAONV, 300l);
			}else{
				//活动已结束
				getBtn1.setTextColor(getResources().getColor(R.color.gray));
				getBtn1.setEnabled(false);
				getBtn1.setText("活动结束");
				getBtn2.setTextColor(getResources().getColor(R.color.gray));
				getBtn2.setEnabled(false);
				getBtn2.setText("活动结束");
				getBtn3.setTextColor(getResources().getColor(R.color.gray));
				getBtn3.setEnabled(false);
				getBtn3.setText("活动结束");
			}
		} catch (ParseException e) {
			getBtn1.setTextColor(getResources().getColor(R.color.prize_region_color1));
			getBtn1.setEnabled(false);
			getBtn1.setText("敬请期待");
			getBtn1.setBackgroundResource(R.drawable.prize_region_btn_enable);
			getBtn2.setTextColor(getResources().getColor(R.color.prize_region_color1));
			getBtn2.setEnabled(false);
			getBtn2.setText("敬请期待");
			getBtn2.setBackgroundResource(R.drawable.prize_region_btn_enable);
			getBtn3.setTextColor(getResources().getColor(R.color.prize_region_color1));
			getBtn3.setEnabled(false);
			getBtn3.setText("敬请期待");
			getBtn3.setBackgroundResource(R.drawable.prize_region_btn_enable);
			e.printStackTrace();
		}
	}
	
	/**
	 * Dialog
	 */
	private void showPromptDialog(final String type){
		View contentView = LayoutInflater.from(PrizeRegionTempActivity.this).inflate(R.layout.prize_region_prompt_dialog, null);
		final TextView topText = (TextView) contentView.findViewById(R.id.prize_region_prompt_dialog_top_text);
		final TextView bottomText = (TextView) contentView.findViewById(R.id.prize_region_prompt_dialog_bottom_text);
		final ImageView delBtn = (ImageView) contentView.findViewById(R.id.prize_region_prompt_dialog_delbtn);
		final Button btn = (Button) contentView.findViewById(R.id.prize_region_prompt_dialog_btn);
		if("领取成功".equals(type)){
			topText.setVisibility(View.VISIBLE);
			topText.setText("恭喜您领取成功！");
			bottomText.setText("能让您喜欢是我的荣幸！");
			btn.setText("查看礼品");
		}else if("投资元月盈".equals(type)){
			topText.setVisibility(View.GONE);
			String str = "活动期间完成元月盈投资\n每人可获得1次领取”金康尼商城\n1500元代金券“的机会";
			int fstart = str.indexOf("1次");
			int fend = fstart + 1;
			SpannableStringBuilder style = new SpannableStringBuilder(str);
			style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), fstart, fend,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			bottomText.setText(style);
			btn.setText("去投资");
		}else{
			topText.setVisibility(View.GONE);
			bottomText.setText(type);
			btn.setText("我知道了");
		}
		AlertDialog.Builder builder=new AlertDialog.Builder(PrizeRegionTempActivity.this, R.style.Dialog_Transparent);  //先得到构造器  
        builder.setView(contentView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if("领取成功".equals(type)){
					Intent intentPrizeRecord = new Intent(PrizeRegionTempActivity.this,MyGiftsActivity.class);
					startActivity(intentPrizeRecord);
				}else if("投资元月盈".equals(type)){
					Intent intentYYYInvest = new Intent(PrizeRegionTempActivity.this,BorrowDetailYYYActivity.class);
					startActivity(intentYYYInvest);
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
        //参数都设置完成了，创建并显示出来  
        dialog.show();  
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth()*4/5;
        dialog.getWindow().setAttributes(lp);
	}
	
	/**
	 * 判断该用户是否已经领取该奖品
	 * @param userId
	 */
	private void requestPrizeInfoByName(String userId,final String prizeName){
		AsyncPrize prizeTask = new AsyncPrize(PrizeRegionTempActivity.this, userId, prizeName, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						//已经领取
						if(PrizeName.PRIZE_NAME_VITA.equals(prizeName)){
							//VITA优惠券已领取
							getBtn1.setTextColor(getResources().getColor(R.color.prize_region_color1));
							getBtn1.setEnabled(false);
							getBtn1.setText("已领取");
							getBtn1.setBackgroundResource(R.drawable.prize_region_btn_enable);
						}else if(PrizeName.PRIZE_NAME_YOUHUA.equals(prizeName)){
							getBtn2.setTextColor(getResources().getColor(R.color.prize_region_color1));
							getBtn2.setEnabled(false);
							getBtn2.setText("已领取");
							getBtn2.setBackgroundResource(R.drawable.prize_region_btn_enable);
						}else if(PrizeName.PRIZE_NAME_JINKANGNI.equals(prizeName)){
							//金康尼优惠券已领完
							getBtn3.setTextColor(getResources().getColor(R.color.prize_region_color1));
							getBtn3.setEnabled(false);
							getBtn3.setText("已领取");
							getBtn3.setBackgroundResource(R.drawable.prize_region_btn_enable);
						}
					}else{
						PrizeInfo info = baseInfo.getmPrizeInfo();
						if(PrizeName.PRIZE_NAME_VITA.equals(prizeName)){
							//VITA优惠券已领取
							getBtn1.setTextColor(getResources().getColor(R.color.prize_region_color2));
							getBtn1.setEnabled(true);
							getBtn1.setText("领 取");
						}else if(PrizeName.PRIZE_NAME_YOUHUA.equals(prizeName)){
							getBtn2.setTextColor(getResources().getColor(R.color.prize_region_color2));
							getBtn2.setEnabled(true);
							getBtn2.setText("领 取");
						}else if(PrizeName.PRIZE_NAME_JINKANGNI.equals(prizeName)){
							//金康尼优惠券已领完
							getBtn3.setTextColor(getResources().getColor(R.color.prize_region_color2));
							getBtn3.setEnabled(true);
							getBtn3.setText("领 取");
						}
					}
				}
			}
		});
		prizeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 请求该奖品是否已经领完
	 */
	private void requestPrizeCodeSelectone(String id,String prizeCode,final String prizeName,
			String status,String openid,String userId){
		AsyncPrizeCode prizeCodeTask = new AsyncPrizeCode(PrizeRegionTempActivity.this, id, 
				prizeCode, prizeName, status, openid, userId,new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//奖品未领完
								if(PrizeName.PRIZE_NAME_VITA.equals(prizeName)){
									//VITA优惠券已领完
									handler.sendEmptyMessage(REQUEST_PRIZE_DETAILS_BY_NAME_VITA);
								}else if(PrizeName.PRIZE_NAME_YOUHUA.equals(prizeName)){
									//金康尼优惠券
									handler.sendEmptyMessage(REQUEST_PRIZE_DETAILS_BY_NAME_YOUHUASHAONV);
								}else if(PrizeName.PRIZE_NAME_JINKANGNI.equals(prizeName)){
									//远东食品
									handler.sendEmptyMessage(REQUEST_PRIZE_DETAILS_BY_NAME_JKN);
								}
							}else{
								//奖品已经领完
								if(PrizeName.PRIZE_NAME_VITA.equals(prizeName)){
									//VITA优惠券已领完
									getBtn1.setTextColor(getResources().getColor(R.color.prize_region_color1));
									getBtn1.setEnabled(false);
									getBtn1.setText("礼品已领完");
									getBtn1.setBackgroundResource(R.drawable.prize_region_btn_enable);
								}else if(PrizeName.PRIZE_NAME_YOUHUA.equals(prizeName)){
									getBtn2.setTextColor(getResources().getColor(R.color.prize_region_color1));
									getBtn2.setEnabled(false);
									getBtn2.setText("礼品已领完");
									getBtn2.setBackgroundResource(R.drawable.prize_region_btn_enable);
								}else if(PrizeName.PRIZE_NAME_JINKANGNI.equals(prizeName)){
									//金康尼优惠券已领完
									getBtn3.setTextColor(getResources().getColor(R.color.prize_region_color1));
									getBtn3.setEnabled(false);
									getBtn3.setText("礼品已领完");
									getBtn3.setBackgroundResource(R.drawable.prize_region_btn_enable);
								}
							}
						}
					}
				});
		prizeCodeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 判断当前活动是否开始
	 * @param activeTitle 活动编号 HYFL_01
	 * @param id 
	 */
	private void requestPrizeActive(String activeTitle,String id){
		AsyncPrizeActive activeTask = new AsyncPrizeActive(PrizeRegionTempActivity.this, activeTitle, id, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							isFLJHActiveStart(baseInfo);
						}
					}
				});
		activeTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 在某段时间内是否投资过元月盈
	 * @param userId
	 * @param startTime
	 * @param endTime
	 */
	private void requestYYYInvestByTimeSpan(String userId,String startTime,String endTime){
		AsyncYYYInvestByTimeSpan splashTask = new AsyncYYYInvestByTimeSpan(PrizeRegionTempActivity.this, userId, startTime, endTime, new OnCommonInter() {
			@Override
			public void back(BaseInfo baseInfo) {
				if(baseInfo != null){
					int resultCode = SettingsManager.getResultCode(baseInfo);
					if(resultCode == 0){
						handler.sendEmptyMessage(REQUEST_RECEIVE_PRIZE_JKN);
					}else if(resultCode == -1){
						//活动期间未投资元月盈
						showPromptDialog("投资元月盈");
					}
				}
			}
		});
		splashTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 领取奖品
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
		AsyncFLJHReceivePrize task = new AsyncFLJHReceivePrize(PrizeRegionTempActivity.this, userId, prize, giftId,activeTitle, operatingRemark,
				remark, status, source, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//领取成功
								showPromptDialog("领取成功");
								if(PrizeName.PRIZE_NAME_JINKANGNI.equals(prize)){
									handler.sendEmptyMessage(REQUEST_PRIZE_DETAILS_BY_NAME_JKN);
								}else if(PrizeName.PRIZE_NAME_VITA.equals(prize)){
									handler.sendEmptyMessage(REQUEST_PRIZE_DETAILS_BY_NAME_VITA);
								}else if(PrizeName.PRIZE_NAME_YOUHUA.equals(prize)){
									handler.sendEmptyMessage(REQUEST_PRIZE_DETAILS_BY_NAME_YOUHUASHAONV);
								}
							}else if(resultCode == -1){
								//已经领取
								showPromptDialog(baseInfo.getMsg());
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
