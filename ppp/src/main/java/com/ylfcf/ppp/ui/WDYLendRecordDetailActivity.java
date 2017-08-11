package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Html;
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
import android.widget.ListView;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.WDYLendRecordAdapter;
import com.ylfcf.ppp.adapter.WDYLendRecordAdapter.WdyReinvestInter;
import com.ylfcf.ppp.async.AsyncWDYChildRecord;
import com.ylfcf.ppp.async.AsyncWDYReInvest;
import com.ylfcf.ppp.async.AsyncYiLianRMBAccount;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.UserRMBAccountInfo;
import com.ylfcf.ppp.entity.WDYChildRecordInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

import java.util.List;

/**
 * 稳定盈产品的出借记录
 * @author Mr.liu
 *
 */
public class WDYLendRecordDetailActivity extends BaseActivity implements OnClickListener{
	private static final String className = "WDYLendRecordDetailActivity";
	private static final int REQUEST_LENDRECORD_WHAT = 8219;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private LayoutInflater mLayoutInflater;
	
	private View topLayout;
	private TextView totalInvestMoney;//累计投资金额
	private TextView totalDelayDays;//累计延期
	private TextView totalInterestMoney;//当前累计收益
	
	private ListView mListView;
	private WDYLendRecordAdapter lendAdapter;
	private InvestRecordInfo investRecordInfo;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_LENDRECORD_WHAT:
				getChildRecordById(investRecordInfo.getId());
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
		setContentView(R.layout.lczq_lendrecord_detail_activity);
		investRecordInfo = (InvestRecordInfo) getIntent().getSerializableExtra("invest_record");
		mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		findViews();
		handler.sendEmptyMessage(REQUEST_LENDRECORD_WHAT);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

	private void findViews(){
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("出借记录");
		
		topLayout = mLayoutInflater.inflate(R.layout.lczq_lendrecord_top_layout, null);
		totalInvestMoney = (TextView) topLayout.findViewById(R.id.lczq_lendrecord_top_layout_totalinvestmoney);
		totalDelayDays = (TextView) topLayout.findViewById(R.id.lczq_lendrecord_top_layout_totaldelaydays);
		totalInterestMoney = (TextView) topLayout.findViewById(R.id.lczq_lendrecord_top_layout_totalinterest);
		totalInvestMoney.setText(investRecordInfo.getTotalLend());
		totalDelayDays.setText(investRecordInfo.getTotalDelay());
		totalInterestMoney.setText(Html.fromHtml(getResources().getString(R.string.bid_wdy_lend_record_interest)
				.replace("INTEREST", Util.double2PointDouble(Double.parseDouble(investRecordInfo.getWdy_pro_interest())))));
		SpannableStringBuilder builder = new SpannableStringBuilder(totalInterestMoney.getText().toString());  
		  
		//ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色  
		ForegroundColorSpan blueSpan = new ForegroundColorSpan(getResources().getColor(R.color.common_topbar_bg_color));  
		builder.setSpan(blueSpan, 0,Util.double2PointDouble(Double.parseDouble(investRecordInfo.getWdy_pro_interest())).length(),
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);  
		totalInterestMoney.setText(builder);  

		mListView = (ListView) findViewById(R.id.lczq_lendrecord_detail_activity_listview);
		lendAdapter = new WDYLendRecordAdapter(this,investRecordInfo.getEnd_time(),new WdyReinvestInter() {
			@Override
			public void reinvest(WDYChildRecordInfo info) {
				//点击投资，先判断下余额
				requestUserAccountInfo(SettingsManager.getUserId(getApplicationContext()),info);
			}
		});
		mListView.addHeaderView(topLayout);
		mListView.setAdapter(lendAdapter);
	}
	
	private void updateAdapter(List<WDYChildRecordInfo> list,String sysTime){
		lendAdapter.setItems(list,sysTime);
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
	
	/**
	 * 确认投资的dialog
	 */
	private void showInvestDialog(final String type,final WDYChildRecordInfo childRecordInfo) {
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.wdy_lendrecord_reinvest_dialog, null);
		Button deleteBtn = (Button) contentView.
				findViewById(R.id.wdy_lendrecord_reinvest_dialog_delete);
		Button leftBtn = (Button) contentView
				.findViewById(R.id.wdy_lendrecord_reinvest_leftbtn);
		Button rightBtn = (Button) contentView
				.findViewById(R.id.wdy_lendrecord_reinvest_rightbtn);
		ImageView logo = (ImageView) contentView.
				findViewById(R.id.wdy_lendrecord_reinvest_logo);
		View line = contentView.findViewById(R.id.wdy_lendrecord_reinvest_line);
		TextView contentTV = (TextView) contentView
				.findViewById(R.id.wdy_lendrecord_reinvest_text);
		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				R.style.Dialog_Transparent); // 先得到构造器
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		if("账户余额不足".equals(type)){
			leftBtn.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
			logo.setVisibility(View.VISIBLE);
			logo.setBackgroundResource(R.drawable.yxb_redeem_error_icon);
			contentTV.setText("账户余额不足！");
			rightBtn.setText("去充值");
		}else if("复投".equals(type)){
			leftBtn.setVisibility(View.VISIBLE);
			line.setVisibility(View.VISIBLE);
			logo.setVisibility(View.GONE);
			contentTV.setText("确定支付本期金额："+ investRecordInfo.getMoney()+"元吗？");
			rightBtn.setText("去充值");
		}else if("投资成功".equals(type)){
			leftBtn.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
			logo.setVisibility(View.VISIBLE);
			logo.setBackgroundResource(R.drawable.yxb_redeem_success_icon);
			contentTV.setText("投资成功！");
			rightBtn.setText("确定");
		}
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if("复投".equals(type)){
					wdyReInvest(childRecordInfo.getId(), SettingsManager.getUserId(getApplicationContext()), investRecordInfo.getMoney());
				}
				dialog.dismiss();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if("复投".equals(type) || "账户余额不足".equals(type)){
					//去充值
					//先判断下是否实名绑卡
					if(SettingsManager.isPersonalUser(WDYLendRecordDetailActivity.this)){
						checkIsVerify("稳定盈复投充值");
					}else if(SettingsManager.isCompanyUser(WDYLendRecordDetailActivity.this)){
						Intent intentRechargeComp = new Intent(WDYLendRecordDetailActivity.this,RechargeCompActivity.class);
						startActivity(intentRechargeComp);
					}
				}else if("投资成功".equals(type)){
					//刷新
					handler.sendEmptyMessage(REQUEST_LENDRECORD_WHAT);
				}
				dialog.dismiss();
			}
		});
		deleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if("投资成功".equals(type)){
					//刷新
					handler.sendEmptyMessage(REQUEST_LENDRECORD_WHAT);
				}
				dialog.dismiss();
			}
		});
		// 参数都设置完成了，创建并显示出来
		dialog.show();
		// 设置dialog的宽度
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth() * 6 / 7;
		dialog.getWindow().setAttributes(lp);
	}
	
	/**
	 * 验证用户是否已经认证
	 * @param type “充值”,“提现”，“邀请有奖”
	 */
	private void checkIsVerify(final String type){
		RequestApis.requestIsVerify(WDYLendRecordDetailActivity.this, SettingsManager.getUserId(WDYLendRecordDetailActivity.this), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(flag){
					//用户已经实名
					checkIsBindCard(type);
				}else{
					//用户没有实名
					Intent intent = new Intent(WDYLendRecordDetailActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
				}
			}

			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}
	
	/**
	 * 判断用户是否已经绑卡
	 * @param type "充值","提现","邀请有奖"
	 */
	private void checkIsBindCard(final String type){
		RequestApis.requestIsBinding(WDYLendRecordDetailActivity.this, 
				SettingsManager.getUserId(WDYLendRecordDetailActivity.this), "宝付", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){ 
					//用户已经绑卡
					if("稳定盈复投充值".equals(type)){
						//那么直接跳到充值页面
						intent.setClass(WDYLendRecordDetailActivity.this, RechargeActivity.class);
					}
				}else{
					//用户还没有绑卡
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					intent.putExtra("bundle", bundle);
					intent.setClass(WDYLendRecordDetailActivity.this, BindCardActivity.class);
				}
				startActivity(intent);
			}
		});
	}
	
	/**
	 * 获取子记录
	 * @param recordId
	 */
	private void getChildRecordById(String recordId){
		AsyncWDYChildRecord childRecordTask = new AsyncWDYChildRecord(WDYLendRecordDetailActivity.this, recordId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								List<WDYChildRecordInfo> childRecordList = baseInfo.getmWDYChildRecordPageInfo().getWdyChildRecordList();
								updateAdapter(childRecordList,baseInfo.getTime());
							}
						}	
					}
				});
		childRecordTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 稳定盈复投
	 * @param wdyLogid
	 * @param userId
	 * @param money
	 */
	private void wdyReInvest(String wdyLogid,String userId,String money){
		AsyncWDYReInvest reinvestTask = new AsyncWDYReInvest(WDYLendRecordDetailActivity.this, wdyLogid, userId, money, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//复投成功
								showInvestDialog("投资成功", null);
							}
						}
					}
				});
		reinvestTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 用户账户信息
	 */
	private void requestUserAccountInfo(String userId,final WDYChildRecordInfo childRecordInfo) {
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		AsyncYiLianRMBAccount yilianTask = new AsyncYiLianRMBAccount(
				WDYLendRecordDetailActivity.this, userId, new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null){
							mLoadingDialog.dismiss();
						}
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								UserRMBAccountInfo info = baseInfo
										.getRmbAccountInfo();
								double useMoneyD = 0d;//账户余额
								double investMoneyD = 0d;//投资金额
								try {
									useMoneyD = Double.parseDouble(info.getUse_money());
									investMoneyD = Double.parseDouble(investRecordInfo.getMoney());
								} catch (Exception e) {
								}
								if(investMoneyD > useMoneyD){
									//账户余额不足
									showInvestDialog("账户余额不足",childRecordInfo);
								}else{
									showInvestDialog("复投",childRecordInfo);
								}
							}else{
								Util.toastLong(WDYLendRecordDetailActivity.this, baseInfo.getMsg());
							}
						}
					}
				});
		yilianTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
