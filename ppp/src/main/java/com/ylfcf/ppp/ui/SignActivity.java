package com.ylfcf.ppp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.SignAdapter;
import com.ylfcf.ppp.entity.SignInfo;
import com.ylfcf.ppp.entity.SignResultInfo;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 签到页面
 * @author Mr.liu
 *
 */
public class SignActivity extends BaseActivity implements OnClickListener{
	private static final String className = "SignActivity";
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private GridView signGridView;
	private TextView monthTV;//几月
	private Button catJXQBtn,ljjxBtn;//查看加息券，立即加息
	private List<SignInfo> signList = new ArrayList<SignInfo>();
	private SignAdapter signAdapter = null;
	private SignResultInfo mSignResultInfo = null;
	private String systemTime;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//用来判断当前月份的第一天是从星期几开始的
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
	private SimpleDateFormat sdfMonth = new SimpleDateFormat("M");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sign_activity);
		mSignResultInfo = (SignResultInfo) getIntent().getSerializableExtra("SignResultInfo");
		systemTime = getIntent().getStringExtra("SystemTime");
		findViews();
		initData(mSignResultInfo,systemTime);
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("签到");
		
		monthTV = (TextView) findViewById(R.id.sign_activity_month);
		catJXQBtn = (Button) findViewById(R.id.sign_activity_cat_jxq_btn);
		catJXQBtn.setOnClickListener(this);
		ljjxBtn = (Button) findViewById(R.id.sign_activity_ljjx_btn);
		ljjxBtn.setOnClickListener(this);
		signGridView = (GridView) findViewById(R.id.sign_activity_gridview);
		signGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
		initAdapter();
	}
	
	private void initData(SignResultInfo mSignResultInfo,String systemTime){
		//初始化日期控件数据
		int days = Util.getCurrentMonthDays(systemTime);//当前月份有多少天
		String weekStr = null;
		try {
			monthTV.setText(sdfMonth.format(sdf1.parse(systemTime))+"月");
		} catch (Exception e) {
			monthTV.setText(sdfMonth.format(new Date())+"月");
		}
		try {
			weekStr = Util.getWeekOfDate(sdf2.parse(systemTime));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}//当月的第一天是星期几
		int startPosition = Util.getPositionOfCurWeek(weekStr);
		
		List<String> signedDays = mSignResultInfo.getSignDateList();
		for(int i = 0;i < ((days+startPosition)/7+1)*7;i++){
			SignInfo info = new SignInfo();
			info.setSigned(false);
			info.setToday(false);
			if(i < startPosition){
				info.setDay("");
			}else if(i>=startPosition && i<=days+startPosition-1){
				info.setDay(String.valueOf(i-startPosition+1));
			}else{
				info.setDay("");
			}
			signList.add(info);
		}
		for(int i=0;i<signedDays.size();i++){
			String signedDay = signedDays.get(i);
			int signedDayPosition = 0;
			try {
				signedDayPosition = Integer.parseInt(signedDay);
			} catch (Exception e) {
			}
			signList.get(signedDayPosition+startPosition).setSigned(true);
		}

		Calendar c = Calendar.getInstance();
		int datenum=c.get(Calendar.DATE);
		signList.get(datenum+startPosition-1).setToday(true);
		updateAdapter();
	}
	
	private void initAdapter(){
		signAdapter = new SignAdapter(SignActivity.this);
		signGridView.setAdapter(signAdapter);
	}

	private void updateAdapter(){
		signAdapter.setItems(signList);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.sign_activity_cat_jxq_btn:
			//查看加息券
			Intent intentJXQ = new Intent(SignActivity.this,MyJXQActivity.class);
			startActivity(intentJXQ);
			break;
		case R.id.sign_activity_ljjx_btn:
			//立即加息
			Intent intentZXD = new Intent(SignActivity.this,BorrowListZXDActivity.class);
			startActivity(intentZXD);
			break;
		default:
			break;
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
}
