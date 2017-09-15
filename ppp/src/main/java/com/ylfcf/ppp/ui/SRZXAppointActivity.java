package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.AppointRecordAdapter;
import com.ylfcf.ppp.async.AsyncAppointAdd;
import com.ylfcf.ppp.async.AsyncAppointRecord;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.SRZXAppointRecordInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.Constants.UserType;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.view.SRZXAppointDatePickerPopwindow;
import com.ylfcf.ppp.widget.AuthImageView;
import com.ylfcf.ppp.widget.MyScrollView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 私人尊享产品预约页面
 * @author Mr.liu
 *
 */
public class SRZXAppointActivity extends BaseActivity implements OnClickListener{
	private static final String className = "SRZXAppointActivity";
	private static final int REQUEST_APPOINTRECORD_WHAT = 1921;
	private static final int REQUEST_APPOINTRECORD_SUCCESS = 1922;
	
	private static final int LISTVIEW_START_SCROLL = 1933;
	
	private LinearLayout mainLayout;
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private int page = 0;
	private int pageSize = 50;
	
	private TextView navPersonalText,navCompanyText;
	private View middleLine;
	private LinearLayout personalLayout,companyLayout;
	
	//个人预约
	private EditText appointMoney;//预约金额
	private Spinner spinner;//期限
	private TextView appointRate;//年化收益率
	private LinearLayout planLayout;//
	private TextView planText;//计划买入时间
	private EditText authCodeET;//验证码
	private AuthImageView authCodeImg;//图片验证码
	private Button changeAuthBtn;//换一张
	private Button commitBtn;//提交
	private String sysImgAuthStr = "";
	private String interestPeroid = "";//预约期限
	
	//企业预约
	private EditText appointMoneyComp;
	private Spinner spinnerComp;
	private LinearLayout planLayoutComp;//
	private TextView planTextComp;
	private EditText authCodeETComp;//验证码
	private AuthImageView authCodeImgComp;//图片验证码
	private Button changeAuthBtnComp;//换一张
	private Button commitBtnComp;//提交
	private String sysImgAuthStrComp = "";
	private String interestPeroidComp = "";//预约期限
	
	private ListView recordListView;//
	private MyScrollView scrollView;
	
	private AppointRecordAdapter recordAdapter;

	//预约接口请求的参数
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_APPOINTRECORD_WHAT:
				requestAppointRecord(page, pageSize);
				break;
			case REQUEST_APPOINTRECORD_SUCCESS:
				List<SRZXAppointRecordInfo> srzxAppointList = (List<SRZXAppointRecordInfo>) msg.obj;
				if(srzxAppointList != null){
					updateAdapter(srzxAppointList);
				}
				break;
			case LISTVIEW_START_SCROLL:
				startScroll();
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
		setContentView(R.layout.srzx_appoint_activity);
		findViews();
		handler.sendEmptyMessage(REQUEST_APPOINTRECORD_WHAT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 1220 || resultCode == 1221){
			initAppointLayout();
		}
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
		topTitleTV.setText("私人尊享");
		
		navPersonalText = (TextView) findViewById(R.id.srzx_appoint_activity_nav_personal);
		navPersonalText.setOnClickListener(this);
		navCompanyText = (TextView) findViewById(R.id.srzx_appoint_activity_nav_company);
		navCompanyText.setOnClickListener(this);
		middleLine = findViewById(R.id.srzx_appoint_activity_middle_line);
		personalLayout = (LinearLayout) findViewById(R.id.srzx_appoint_activity_personal_layout);
		companyLayout = (LinearLayout) findViewById(R.id.srzx_appoint_activity_company_layout);
		
		scrollView = (MyScrollView) findViewById(R.id.srzx_appoint_activity_scrollview);
		mainLayout = (LinearLayout) findViewById(R.id.srzx_appoint_activity_mainlayout);
		
		appointMoney = (EditText) findViewById(R.id.srzx_appoint_activity_money);
		spinner = (Spinner) findViewById(R.id.srzx_appoint_activity_spinner);
		appointRate = (TextView) findViewById(R.id.srzx_appoint_activity_rate);
		planLayout = (LinearLayout) findViewById(R.id.srzx_appoint_activity_plan_layout);
		planLayout.setOnClickListener(this);
		planText = (TextView) findViewById(R.id.srzx_appont_activity_plan_time);
		authCodeET = (EditText) findViewById(R.id.srzx_appoint_activity_et_authcode);
		authCodeImg = (AuthImageView) findViewById(R.id.srzx_appoint_activity_image_authcode);
		sysImgAuthStr = getResponseStr(authCodeImg.getValidataAndSetImage(getRandomInteger()));
		changeAuthBtn = (Button) findViewById(R.id.srzx_appoint_activity_image_authcode_change);
		changeAuthBtn.setOnClickListener(this);
		commitBtn = (Button) findViewById(R.id.srzx_appoint_activity_commit_btn);
		commitBtn.setOnClickListener(this);
		
		//企业用户
		appointMoneyComp = (EditText) findViewById(R.id.srzx_appoint_activity_comp_money);
		spinnerComp = (Spinner) findViewById(R.id.srzx_appoint_activity_comp_spinner);
		planLayoutComp = (LinearLayout) findViewById(R.id.srzx_appoint_activity_plan_comp_layout);
		planLayoutComp.setOnClickListener(this);
		planTextComp = (TextView) findViewById(R.id.srzx_appont_activity_plan_comp_time);
		authCodeETComp = (EditText) findViewById(R.id.srzx_appoint_activity_et_comp_authcode);
		authCodeImgComp = (AuthImageView) findViewById(R.id.srzx_appoint_activity_image_comp_authcode);
		sysImgAuthStrComp = getResponseStr(authCodeImgComp.getValidataAndSetImage(getRandomInteger()));
		changeAuthBtnComp = (Button) findViewById(R.id.srzx_appoint_activity_image_authcode_comp_change);
		changeAuthBtnComp.setOnClickListener(this);
		commitBtnComp = (Button) findViewById(R.id.srzx_appoint_activity_commit_comp_btn);
		commitBtnComp.setOnClickListener(this);
		
		recordListView = (ListView) findViewById(R.id.srzx_appoint_activity_lv);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				scrollView.smoothScrollTo(0,1);
			}
		}, 100L);
		
		initAdapter();
		initListeners();
		initAppointLayout();
	}
	
	//初始化
	private void initAppointLayout(){
		String userType = SettingsManager.getUserType(SRZXAppointActivity.this);
		if(UserType.USER_NORMAL_PERSONAL.equals(userType) || UserType.USER_VIP_PERSONAL.equals(userType)){
			//个人用户
			navPersonalText.setVisibility(View.VISIBLE);
			navPersonalText.setGravity(Gravity.CENTER_HORIZONTAL);
			navCompanyText.setVisibility(View.GONE);
			middleLine.setVisibility(View.GONE);
			personalLayout.setVisibility(View.VISIBLE);
			companyLayout.setVisibility(View.GONE);
		}else if(UserType.USER_COMPANY.equals(userType)){
			//企业用户
			navPersonalText.setVisibility(View.GONE);
			navCompanyText.setVisibility(View.VISIBLE);
			navCompanyText.setGravity(Gravity.CENTER_HORIZONTAL);
			middleLine.setVisibility(View.GONE);
			personalLayout.setVisibility(View.GONE);
			companyLayout.setVisibility(View.VISIBLE);
		}else{
			//游客
			navPersonalText.setVisibility(View.VISIBLE);
			navCompanyText.setVisibility(View.VISIBLE);
			middleLine.setVisibility(View.VISIBLE);
			personalLayout.setVisibility(View.VISIBLE);
			companyLayout.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 更新预约布局
	 */
	private void updateAppointLayout(View v){
		switch (v.getId()) {
		case R.id.srzx_appoint_activity_nav_personal:
			navPersonalText.setEnabled(false);
			navPersonalText.setTextColor(getResources().getColor(R.color.yellow2));
			navCompanyText.setEnabled(true);
			navCompanyText.setTextColor(getResources().getColor(R.color.yellow));
			personalLayout.setVisibility(View.VISIBLE);
			companyLayout.setVisibility(View.GONE);
			break;
		case R.id.srzx_appoint_activity_nav_company:
			navPersonalText.setEnabled(true);
			navPersonalText.setTextColor(getResources().getColor(R.color.yellow));
			navCompanyText.setEnabled(false);
			navCompanyText.setTextColor(getResources().getColor(R.color.yellow2));
			personalLayout.setVisibility(View.GONE);
			companyLayout.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
	
	private void initAdapter(){
		recordAdapter = new AppointRecordAdapter(SRZXAppointActivity.this);
		recordListView.setAdapter(recordAdapter);
	}
	
	private void updateAdapter(List<SRZXAppointRecordInfo> list){
		recordAdapter.setItems(list);
//		startScroll();
	}
	
	//listview开始滚动
	private void startScroll() {
		recordListView.smoothScrollBy(1, 0);
		handler.sendEmptyMessageDelayed(LISTVIEW_START_SCROLL, 10L);
	}
	
	private void initListeners(){
		appointMoney.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String appointMoneyStr = appointMoney.getText().toString();
				try {
					int appointMoneyI = Integer.parseInt(appointMoneyStr);
					updateRate(appointMoneyI);
				} catch (Exception e) {
				}
			}
		});
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String[] peroidArr = getResources().getStringArray(R.array.srzx_appoint_interest_period);
				TextView tv = (TextView) view;
				tv.setTextColor(getResources().getColor(R.color.gray)); // 设置颜色
				tv.setTextSize(getResources().getDimensionPixelOffset(R.dimen.common_measure_6dp)); // 设置大小
				tv.setGravity(android.view.Gravity.LEFT); // 设置居中
				initSpinnerDatas(peroidArr,position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		spinnerComp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String[] peroidArr = getResources().getStringArray(R.array.srzx_appoint_interest_period);
				TextView tv = (TextView) view;
				tv.setTextColor(getResources().getColor(R.color.gray)); // 设置颜色
				tv.setTextSize(getResources().getDimensionPixelOffset(R.dimen.common_measure_6dp)); // 设置大小
				tv.setGravity(android.view.Gravity.LEFT); // 设置居中
				initSpinnerDatas(peroidArr,position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}
	
	// 获取1~9的4个随机数
	private String[] getRandomInteger() {
		String[] reuestArray = new String[4];
		for (int i = 0; i < 4; i++) {
			reuestArray[i] = String.valueOf((int) (Math.random() * 9 + 1));
		}
		return reuestArray;
	}

	// 获取返回的数组
	private String getResponseStr(String[] response) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String str : response) {
			stringBuffer.append(str);
		}
		return stringBuffer.toString();
	}
	
	/**
	 * 根据输入框刷新利率
	 * @param appointMoney
	 */
	private void updateRate(int appointMoney){
		if(appointMoney < 10){
			appointRate.setText(null);
		}else if(appointMoney >= 10 && appointMoney < 30){
			if("32".equals(interestPeroid)){
				appointRate.setText("6.1");
			}else if("62".equals(interestPeroid)){
				appointRate.setText("6.35");
			}else if("92".equals(interestPeroid)){
				appointRate.setText("6.6");
			}else if("122".equals(interestPeroid)){
				appointRate.setText("6.76");
			}else if("152".equals(interestPeroid)){
				appointRate.setText("6.92");
			}else if("182".equals(interestPeroid)){
				appointRate.setText("7.1");
			}else if("212".equals(interestPeroid)){
				appointRate.setText("7.17");
			}else if("242".equals(interestPeroid)){
				appointRate.setText("7.23");
			}else if("272".equals(interestPeroid)){
				appointRate.setText("7.30");
			}else if("302".equals(interestPeroid)){
				appointRate.setText("7.37");
			}else if("332".equals(interestPeroid)){
				appointRate.setText("7.43");
			}else if("365".equals(interestPeroid)){
				appointRate.setText("7.5");
			}
		}else{
			if("32".equals(interestPeroid)){
				appointRate.setText("6.2");
			}else if("62".equals(interestPeroid)){
				appointRate.setText("6.45");
			}else if("92".equals(interestPeroid)){
				appointRate.setText("6.7");
			}else if("122".equals(interestPeroid)){
				appointRate.setText("6.86");
			}else if("152".equals(interestPeroid)){
				appointRate.setText("7.02");
			}else if("182".equals(interestPeroid)){
				appointRate.setText("7.2");
			}else if("212".equals(interestPeroid)){
				appointRate.setText("7.25");
			}else if("242".equals(interestPeroid)){
				appointRate.setText("7.30");
			}else if("272".equals(interestPeroid)){
				appointRate.setText("7.35");
			}else if("302".equals(interestPeroid)){
				appointRate.setText("7.4");
			}else if("332".equals(interestPeroid)){
				appointRate.setText("7.45");
			}else if("365".equals(interestPeroid)){
				appointRate.setText("7.5");
			}
		}
	}
	
	/**
	 * 初始化spinner相关联的数据
	 * @param peroidArr
	 * @param position
	 */
	private void initSpinnerDatas(String[] peroidArr,int position){
		int appointMoneyInt = 0;
		String appointMoneyStr = appointMoney.getText().toString();
		try {
			appointMoneyInt = Integer.parseInt(appointMoneyStr);
		} catch (Exception e) {
		}
		switch (position) {
		case 0:
			if(appointMoneyInt < 10){
				appointRate.setText(null);
			}else if(appointMoneyInt >= 10 && appointMoneyInt < 30){
				appointRate.setText("6.1");
			}else{
				appointRate.setText("6.2");
			}
			interestPeroid = "32";
			interestPeroidComp = "32";
			break;
		case 1:
			if(appointMoneyInt < 10){
				appointRate.setText(null);
			}else if(appointMoneyInt >= 10 && appointMoneyInt < 30){
				appointRate.setText("6.35");
			}else{
				appointRate.setText("6.45");
			}
			interestPeroid = "62";
			interestPeroidComp = "62";
			break;
		case 2:
			if(appointMoneyInt < 10){
				appointRate.setText(null);
			}else if(appointMoneyInt >= 10 && appointMoneyInt < 30){
				appointRate.setText("6.6");
			}else{
				appointRate.setText("6.7");
			}
			interestPeroid = "92";
			interestPeroidComp = "92";
			break;
		case 3:
			if(appointMoneyInt < 10){
				appointRate.setText(null);
			}else if(appointMoneyInt >= 10 && appointMoneyInt < 30){
				appointRate.setText("6.76");
			}else{
				appointRate.setText("6.86");
			}
			interestPeroid = "122";
			interestPeroidComp = "122";
			break;
		case 4:
			if(appointMoneyInt < 10){
				appointRate.setText(null);
			}else if(appointMoneyInt >= 10 && appointMoneyInt < 30){
				appointRate.setText("6.92");
			}else{
				appointRate.setText("7.02");
			}
			interestPeroid = "152";
			interestPeroidComp = "152";
			break;
		case 5:
			if(appointMoneyInt < 10){
				appointRate.setText(null);
			}else if(appointMoneyInt >= 10 && appointMoneyInt < 30){
				appointRate.setText("7.1");
			}else{
				appointRate.setText("7.2");
			}
			interestPeroid = "182";
			interestPeroidComp = "182";
			break;
		case 6:
			if(appointMoneyInt < 10){
				appointRate.setText(null);
			}else if(appointMoneyInt >= 10 && appointMoneyInt < 30){
				appointRate.setText("7.17");
			}else{
				appointRate.setText("7.25");
			}
			interestPeroid = "212";
			interestPeroidComp = "212";
			break;
		case 7:
			if(appointMoneyInt < 10){
				appointRate.setText(null);
			}else if(appointMoneyInt >= 10 && appointMoneyInt < 30){
				appointRate.setText("7.23");
			}else{
				appointRate.setText("7.30");
			}
			interestPeroid = "242";
			interestPeroidComp = "242";
			break;
		case 8:
			if(appointMoneyInt < 10){
				appointRate.setText(null);
			}else if(appointMoneyInt >= 10 && appointMoneyInt < 30){
				appointRate.setText("7.30");
			}else{
				appointRate.setText("7.35");
			}
			interestPeroid = "272";
			interestPeroidComp = "272";
			break;
		case 9:
			if(appointMoneyInt < 10){
				appointRate.setText(null);
			}else if(appointMoneyInt >= 10 && appointMoneyInt < 30){
				appointRate.setText("7.37");
			}else{
				appointRate.setText("7.40");
			}
			interestPeroid = "302";
			interestPeroidComp = "302";
			break;
		case 10:
			if(appointMoneyInt < 10){
				appointRate.setText(null);
			}else if(appointMoneyInt >= 10 && appointMoneyInt < 30){
				appointRate.setText("7.43");
			}else{
				appointRate.setText("7.45");
			}
			interestPeroid = "332";
			interestPeroidComp = "332";
			break;
		case 11:
			if(appointMoneyInt < 10){
				appointRate.setText(null);
			}else if(appointMoneyInt >= 10 && appointMoneyInt < 30){
				appointRate.setText("7.50");
			}else{
				appointRate.setText("7.50");
			}
			interestPeroid = "365";
			interestPeroidComp = "365";
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.srzx_appoint_activity_plan_layout:
		case R.id.srzx_appoint_activity_plan_comp_layout:
			//计划买入时间
			showAppointPopwindow();
			break;
		case R.id.srzx_appoint_activity_image_authcode_change:
			sysImgAuthStr = getResponseStr(authCodeImg.getValidataAndSetImage(getRandomInteger()));
			break;
		case R.id.srzx_appoint_activity_image_authcode_comp_change:
			sysImgAuthStrComp = getResponseStr(authCodeImgComp.getValidataAndSetImage(getRandomInteger()));
			break;
		case R.id.srzx_appoint_activity_commit_btn:
			//预约
			appoint();
			break;
		case R.id.srzx_appoint_activity_commit_comp_btn:
			appointComp();
			break;
		case R.id.srzx_appoint_activity_nav_personal:
			//个人预约
			updateAppointLayout(navPersonalText);
			break;
		case R.id.srzx_appoint_activity_nav_company:
			//企业预约
			updateAppointLayout(navCompanyText);
			break;
		default:
			break;
		}
	}
	
	//个人用户预约
	private void appoint(){
		boolean isLogin = !SettingsManager.getLoginPassword(
				SRZXAppointActivity.this).isEmpty()
				&& !SettingsManager.getUser(SRZXAppointActivity.this).isEmpty();
		if(isLogin){
			//已登录
			int appointMoneyI = 0;//预约金额
			String planTimeStr = planText.getText().toString();
			String appointMoneyStr = appointMoney.getText().toString();
			String userAuthStr = authCodeET.getText().toString();
			if(appointMoneyStr == null || "".equals(appointMoneyStr)){
				Util.toastLong(SRZXAppointActivity.this, "预约金额不能为空");
			}else{
				try {
					appointMoneyI = Integer.parseInt(appointMoneyStr);
				} catch (Exception e) {
				}
				if(appointMoneyI < 10){
					Util.toastLong(SRZXAppointActivity.this, "预约金额不能低于10万");
				}else{
					if(interestPeroid == null || "".equals(interestPeroid)){
						Util.toastLong(SRZXAppointActivity.this, "请选择定制产品的期限");
					}else{
						if(!sysImgAuthStr.equals(userAuthStr)){
							Util.toastLong(SRZXAppointActivity.this, "请输入正确的图片验证码");
						}else{
							requestAppointAdd(SettingsManager.getUserId(getApplicationContext()), String.valueOf(appointMoneyI*10000), interestPeroid, planTimeStr);
						}
					}
				}
			}
		}else{
			Intent intent = new Intent(SRZXAppointActivity.this,LoginActivity.class);
			intent.putExtra("FLAG", "srzx");
			startActivityForResult(intent, 2001);
		}
	}
	
	//企业用户预约
	private void appointComp(){
		boolean isLogin = !SettingsManager.getLoginPassword(
				SRZXAppointActivity.this).isEmpty()
				&& !SettingsManager.getUser(SRZXAppointActivity.this).isEmpty();
		if(isLogin){
			//已登录
			int appointMoneyI = 0;//预约金额
			String planTimeStr = planTextComp.getText().toString();
			String appointMoneyStr = appointMoneyComp.getText().toString();
			String userAuthStr = authCodeETComp.getText().toString();
			if(appointMoneyStr == null || "".equals(appointMoneyStr)){
				Util.toastLong(SRZXAppointActivity.this, "预约金额不能为空");
			}else{
				try {
					appointMoneyI = Integer.parseInt(appointMoneyStr);
				} catch (Exception e) {
				}
				if(appointMoneyI < 10){
					Util.toastLong(SRZXAppointActivity.this, "预约金额不能低于10万");
				}else{
					if(interestPeroidComp == null || "".equals(interestPeroidComp)){
						Util.toastLong(SRZXAppointActivity.this, "请选择定制产品的期限");
					}else{
						if(!sysImgAuthStrComp.equals(userAuthStr)){
							Util.toastLong(SRZXAppointActivity.this, "请输入正确的图片验证码");
						}else{
							requestAppointAdd(SettingsManager.getUserId(getApplicationContext()), String.valueOf(appointMoneyI*10000), interestPeroidComp, planTimeStr);
						}
					}
				}
			}
		}else{
			Intent intent = new Intent(SRZXAppointActivity.this,LoginActivity.class);
			intent.putExtra("FLAG", "srzx");
			startActivityForResult(intent, 2002);
		}
	}
	
	/**
	 * 预约计划时间选择器
	 */
	private void showAppointPopwindow(){
		View popView = LayoutInflater.from(this).inflate(
				R.layout.srzx_appoint_popwindow, null);
		int[] screen = SettingsManager.getScreenDispaly(this);
		int width = screen[0] * 4 / 5;
		int height = screen[1] * 3 / 5 + 20;
		SRZXAppointDatePickerPopwindow popwindow = new SRZXAppointDatePickerPopwindow(SRZXAppointActivity.this, popView, width, height,
				new OnWheelFinishListener(){
					@Override
					public void onFinish(String dateStr) {
						planText.setText(dateStr);
						planTextComp.setText(dateStr);
					}
		});
		popwindow.show(mainLayout);
	}
	
	/**
	 * 个人用户预约
	 * @param userId
	 * @param money 单位元
	 * @param interestPeriod 投资期限 单位天
	 * @param purchaseTime 计划购买时间 格式yyyy-MM-dd HH:mm:ss
	 */
	private void requestAppointAdd(String userId,String money,String interestPeriod,String purchaseTime){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncAppointAdd appointAddTask = new AsyncAppointAdd(SRZXAppointActivity.this, userId, money, interestPeriod, purchaseTime, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//预约成功
								if(SettingsManager.isPersonalUser(getApplicationContext())){
									appointMoney.setText(null);
									spinner.setSelection(0);
									appointRate.setText(null);
									planText.setText(null);
									authCodeET.setText(null);
								}else if(SettingsManager.isCompanyUser(getApplicationContext())){
									appointMoneyComp.setText(null);
									spinnerComp.setSelection(0);
									planTextComp.setText(null);
									authCodeETComp.setText(null);
								}
								Util.toastLong(SRZXAppointActivity.this, "恭喜您，预约定制产品成功");
								handler.sendEmptyMessage(REQUEST_APPOINTRECORD_WHAT);
							}else{
								Util.toastLong(SRZXAppointActivity.this, baseInfo.getMsg());
							}
						}
					}
				});
		appointAddTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 私人尊享产品预约记录
	 * @param page
	 * @param pageSize
	 */
	private void requestAppointRecord(int page,int pageSize){
		if(mLoadingDialog != null && !isFinishing()){
			mLoadingDialog.show();
		}
		AsyncAppointRecord recordTask = new AsyncAppointRecord(SRZXAppointActivity.this, String.valueOf(page), String.valueOf(pageSize), 
				new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								Message msg = handler.obtainMessage(REQUEST_APPOINTRECORD_SUCCESS);
								msg.obj = baseInfo.getSrzxAppointRecordList();
								handler.sendMessage(msg);
							}
						}
					}
		});
		recordTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 时间滚轮 停止时返回时间 格式：yyyy-MM-dd HH:mm
	 * @author Mr.liu
	 *
	 */
	public interface OnWheelFinishListener{
		void onFinish(String dateStr);
	}
}
