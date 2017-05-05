package com.ylfcf.ppp.ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncXSMBCurrentUserInvest;
import com.ylfcf.ppp.async.AsyncXSMBDetail;
import com.ylfcf.ppp.async.AsyncXSMBSelectone;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 限时秒标 安全保障
 * 
 * @author Mr.liu
 * 
 */
public class ProductSafetyXSMBActivity extends BaseActivity implements
		OnClickListener {
	private static final int REFRESH_VIEW = 5710;
	private static final int REQUEST_XSMB_REFRESH_WHAT = 8291;// 请求接口刷新数据
	private static final int REQUEST_XSMB_BTNCLICK_WHAT = 8292;// 点击“立即秒杀”按钮
	private static final int REFRESH_REMAIN_TIME = 8293;// 刷新下个秒标的剩余时间
	private static final int REQUEST_CURRENT_USERINVEST = 8294;//请求当前用户是否投资过该秒标
	
	private static final int REQUEST_XSMB_SELECTONE = 8296;

	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	// 产品交易结构更改之前的布局
	private LinearLayout beforeLayout;
	private TextView zjaqTV;
	private TextView zcaqTV;

	// 产品交易结构更改之后的布局
	private LinearLayout afterLayout;
	private TextView dbcsTV;
	private TextView hklyTV;

	private Button investBtn;// 立即投资
	private ProjectInfo projectInfo;
	private ProductInfo productInfo;
	private InvestRecordInfo recordInfo;
	private AlertDialog.Builder builder = null; // 先得到构造器
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// 请求请求原因
	private enum ReasonFlag {
		REFRESH_DATA, // 页面刷新数据
		BTN_CLICK,// 通过按钮点击
		RECORD_DATA //投资记录跳转
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_XSMB_REFRESH_WHAT:
				requestXSMBDetails("发布",ReasonFlag.REFRESH_DATA);
				break;
			case REFRESH_VIEW:
				Bundle bundle = (Bundle) msg.obj;
				refreshView(bundle);
				break;
			case REQUEST_XSMB_BTNCLICK_WHAT:
				requestXSMBDetails("发布", ReasonFlag.BTN_CLICK);
				break;
			case REQUEST_XSMB_SELECTONE:
				requestXSMBSelectone(recordInfo.getBorrow_id(), "发布",ReasonFlag.RECORD_DATA);
				break;
			case REQUEST_CURRENT_USERINVEST:
				requestCurrentUserInvest(SettingsManager.getUserId(getApplicationContext()),productInfo.getId());
				break;
			case REFRESH_REMAIN_TIME:
				long times = (Long) msg.obj;
				updateCountDown(times);
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
		setContentView(R.layout.product_safety_activity);
		mApp.addActivity(this);
		builder = new AlertDialog.Builder(ProductSafetyXSMBActivity.this,
				R.style.Dialog_Transparent); // 先得到构造器
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		if (bundle != null) {
			projectInfo = (ProjectInfo) bundle.getSerializable("PROJECT_INFO");
			productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
			recordInfo = (InvestRecordInfo) bundle.getSerializable("InvestRecordInfo");
		}
		findViews();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(recordInfo == null){
			handler.sendEmptyMessage(REQUEST_XSMB_REFRESH_WHAT);
		}else{
			handler.sendEmptyMessage(REQUEST_XSMB_SELECTONE);
		}
	}

	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("安全保障");

		beforeLayout = (LinearLayout) findViewById(R.id.product_safety_activity_before_layout);
		zjaqTV = (TextView) findViewById(R.id.productsafety_activity_zjaq);
		zcaqTV = (TextView) findViewById(R.id.productsafety_activity_zcaq);

		afterLayout = (LinearLayout) findViewById(R.id.product_safety_activity_after_layout);
		dbcsTV = (TextView) findViewById(R.id.productsafety_activity_dbcs);
		hklyTV = (TextView) findViewById(R.id.productsafety_activity_after_hkly);

		investBtn = (Button) findViewById(R.id.product_safety_activity_bidBtn);
		investBtn.setEnabled(false);
		investBtn.setOnClickListener(this);

		if (projectInfo == null) {
			beforeLayout.setVisibility(View.GONE);
			afterLayout.setVisibility(View.VISIBLE);
			return;
		}
		if ("发布".equals(projectInfo.getStatus())) {
			beforeLayout.setVisibility(View.GONE);
			afterLayout.setVisibility(View.VISIBLE);
		} else {
			beforeLayout.setVisibility(View.VISIBLE);
			afterLayout.setVisibility(View.GONE);
		}

		initData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

	private void initData() {
		if (projectInfo != null) {
			// 产品交易结构更改之前的“资金安全”
			new ImageLoadThread(projectInfo.getCapital_safe(), 0).start();
			// 产品交易结构更改之前的“还款来源”
			new ImageLoadThread(projectInfo.getRepay_from(), 1).start();

			// 产品交易结构更改之前的“担保措施”
			new ImageLoadThread(projectInfo.getMeasures(), 2).start();
			// 产品交易结构更改之后的“还款来源”
			new ImageLoadThread(projectInfo.getRepay_from(), 3).start();

		}
	}

	/**
	 * 刷新控件
	 * 
	 * @param bundle
	 */
	private void refreshView(Bundle bundle) {
		if (bundle == null)
			return;
		CharSequence htmlText = bundle.getCharSequence("HTML_TEXT");
		int position = bundle.getInt("POSITION");
		if (position == 0) {
			// 产品结构更改之前的“资金安全”
			if (htmlText.length() > 0) {
				zjaqTV.setText(htmlText);
			}
		} else if (position == 1) {
			// 产品结构更改之前的“还款来源”
			zcaqTV.setText(htmlText);
		} else if (position == 2) {
			// 产品结构更改之后的“担保措施”
			dbcsTV.setText(htmlText);
		} else if (position == 3) {
			// 产品结构更改之后的“还款来源”
			hklyTV.setText(htmlText);
		}
	}

	class ImageLoadThread extends Thread {
		private String htmlText1 = "";
		private int position1 = 0;

		public ImageLoadThread(String htmlText, int position) {
			this.htmlText1 = htmlText;
			this.position1 = position;
		}

		@Override
		public void run() {
			/**
			 * 要实现图片的显示需要使用Html.fromHtml的一个重构方法：public static Spanned fromHtml
			 * (String source, Html.ImageGetterimageGetter, Html.TagHandler
			 * tagHandler)其中Html.ImageGetter是一个接口，我们要实现此接口，在它的getDrawable
			 * (String source)方法中返回图片的Drawable对象才可以。
			 */
			ImageGetter imageGetter = new ImageGetter() {
				@Override
				public Drawable getDrawable(String source) {
					// TODO Auto-generated method stub
					URL url;
					Drawable drawable = null;
					try {
						url = new URL(source);
						int[] screen = SettingsManager
								.getScreenDispaly(ProductSafetyXSMBActivity.this);
						drawable = Drawable.createFromStream(url.openStream(),
								null);
						if (drawable != null) {
							int imageIntrinsicWidth = drawable
									.getIntrinsicWidth();
							float imageIntrinsicHeight = (float) drawable
									.getIntrinsicHeight();
							int curImageHeight = (int) (screen[0] * (imageIntrinsicHeight / imageIntrinsicWidth));
							drawable.setBounds(0, 0, screen[0], curImageHeight);// 四个参数含义为左上角、右下角坐标确定的一个矩形，图片就在这个矩形范围内画出来
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return drawable;
				}
			};
			CharSequence htmlText = Html.fromHtml(htmlText1, imageGetter, null);
			Message msg = handler.obtainMessage(REFRESH_VIEW);
			Bundle bundle = new Bundle();
			bundle.putCharSequence("HTML_TEXT", htmlText);
			bundle.putInt("POSITION", position1);
			msg.obj = bundle;
			handler.sendMessage(msg);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.product_safety_activity_bidBtn:
			// 从SettingsManager中读取密码，如果为空意味着没有登录。
			boolean isLogin = !SettingsManager.getLoginPassword(
					ProductSafetyXSMBActivity.this).isEmpty()
					&& !SettingsManager.getUser(ProductSafetyXSMBActivity.this)
							.isEmpty();
			// isLogin = true;// 测试
			Intent intent = new Intent();
			// 1、检测是否已经登录
			if (isLogin) {
				// 请求是否还可以购买
				handler.sendEmptyMessage(REQUEST_XSMB_BTNCLICK_WHAT);
			} else {
				// 未登录，跳转到登录页面
				intent.setClass(ProductSafetyXSMBActivity.this,
						LoginActivity.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	private void initViewData(ProductInfo productInfo, Enum flag) {
		if (flag == ReasonFlag.REFRESH_DATA || flag == ReasonFlag.RECORD_DATA) {
			// 刷新数据
			if ("未满标".equals(productInfo.getMoney_status())) {
				initBidBtnCountDown(productInfo, flag);// 倒计时
			} else {
				investBtn.setEnabled(false);
				investBtn.setText("投资结束");
				investBtn.setTextSize(
						TypedValue.COMPLEX_UNIT_PX,
						getResources().getDimensionPixelSize(
								R.dimen.common_measure_26dp));
			}
		} else if (flag == ReasonFlag.BTN_CLICK) {
			// 点击“立即秒杀”按钮
			if ("未满标".equals(productInfo.getMoney_status())) {
				// 未满标，再请求用户是否已经投资过该秒标
				initBidBtnCountDown(productInfo, flag);// 倒计时
			} else {
				// 已满标
				showPromptDialog("1");
				investBtn.setEnabled(false);
				investBtn.setText("投资结束");
				investBtn.setTextSize(
						TypedValue.COMPLEX_UNIT_PX,
						getResources().getDimensionPixelSize(
								R.dimen.common_measure_26dp));
			}
		}
	}

	/**
	 * 投资按钮倒计时
	 * 
	 * @param productInfo
	 * @throws ParseException
	 */
	long remainTimeL = 0l;

	private void initBidBtnCountDown(ProductInfo productInfo, Enum flag) {
		investBtn.setEnabled(false);
		String nowTimeStr = productInfo.getNow_time();
		String willStartTimeStr = productInfo.getWill_start_time();// 开始时间
		int hour = 0, minute = 0, second = 0;
		String hourStr = "", minuteStr = "", secondStr = "";
		try {
			Date nowDate = sdf.parse(nowTimeStr);
			Date willStartDate = sdf.parse(willStartTimeStr);
			remainTimeL = willStartDate.getTime() - nowDate.getTime();
			if (remainTimeL >= 0) {
				hour = (int) (remainTimeL / 1000 / 3600);
				minute = (int) ((remainTimeL / 1000 % 3600) / 60);
				second = (int) (remainTimeL / 1000 % 3600 % 60);
				if (hour < 10) {
					hourStr = "0" + hour;
				} else {
					hourStr = hour + "";
				}
				if (minute < 10) {
					minuteStr = "0" + minute;
				} else {
					minuteStr = minute + "";
				}
				if (second < 10) {
					secondStr = "0" + second;
				} else {
					secondStr = second + "";
				}
				investBtn.setText("距离下一场“秒杀”开始还剩：" + hourStr + "时" + minuteStr
						+ "分" + secondStr + "秒");
				investBtn.setTextSize(
						TypedValue.COMPLEX_UNIT_PX,
						getResources().getDimensionPixelSize(
								R.dimen.common_measure_21dp));
				Message msg = handler.obtainMessage(REFRESH_REMAIN_TIME);
				msg.obj = remainTimeL;
				handler.sendMessageDelayed(msg, 1000l);
			} else {
				// //正在投资中。。
				investBtn.setText("立即秒杀");
				investBtn.setEnabled(true);
				investBtn.setTextSize(
						TypedValue.COMPLEX_UNIT_PX,
						getResources().getDimensionPixelSize(
								R.dimen.common_measure_26dp));
				if (flag == ReasonFlag.BTN_CLICK) {
					handler.sendEmptyMessage(REQUEST_CURRENT_USERINVEST);
				} else if (flag == ReasonFlag.REFRESH_DATA) {

				}
			}
		} catch (ParseException e) {
			investBtn.setText("投资结束");
			investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
					.getDimensionPixelSize(R.dimen.common_measure_26dp));
			e.printStackTrace();
		}
	}

	// 即时更新倒计时
	private void updateCountDown(long times) {
		int hour = 0, minute = 0, second = 0;
		String hourStr = "", minuteStr = "", secondStr = "";
		times -= 1000;
		if (times <= 0) {
			investBtn.setEnabled(true);
			investBtn.setText("立即秒杀");
			investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
					.getDimensionPixelSize(R.dimen.common_measure_26dp));
			return;
		}
		hour = (int) (times / 1000 / 3600);
		minute = (int) ((times / 1000 % 3600) / 60);
		second = (int) (times / 1000 % 3600 % 60);
		if (hour < 10) {
			hourStr = "0" + hour;
		} else {
			hourStr = hour + "";
		}
		if (minute < 10) {
			minuteStr = "0" + minute;
		} else {
			minuteStr = minute + "";
		}
		if (second < 10) {
			secondStr = "0" + second;
		} else {
			secondStr = second + "";
		}
		investBtn.setText("距离下一场“秒杀”开始还剩：" + hourStr + "时" + minuteStr + "分"
				+ secondStr + "秒");
		investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimensionPixelSize(R.dimen.common_measure_21dp));
		Message msg = handler.obtainMessage(REFRESH_REMAIN_TIME);
		msg.obj = times;
		handler.sendMessageDelayed(msg, 1000L);
	}

	/**
	 * 
	 * @param flag
	 *            1表示已抢光 0表示秒杀机会用完
	 */
	private void showPromptDialog(final String flag) {
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.borrow_detail_prompt_layout, null);
		Button leftBtn = (Button) contentView
				.findViewById(R.id.borrow_detail_prompt_layout_leftbtn);
		Button rightBtn = (Button) contentView
				.findViewById(R.id.borrow_detail_prompt_layout_rightbtn);
		TextView topTV = (TextView) contentView
				.findViewById(R.id.borrow_detail_prompt_top_text);
		TextView bottomTV = (TextView) contentView
				.findViewById(R.id.borrow_detail_prompt_bottom_text);
		if ("0".equals(flag)) {
			bottomTV.setVisibility(View.VISIBLE);
			rightBtn.setVisibility(View.VISIBLE);
			topTV.setText("您的本场秒杀机会已使用");
			bottomTV.setText("每场秒杀，每个用户只有一次秒杀机会哟~");
			leftBtn.setText("查看投资记录");
			leftBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
			leftBtn.setBackground(getResources().getDrawable(R.drawable.style_rect_fillet_blue));
			rightBtn.setText("关注其他项目");
		} else if ("1".equals(flag)) {
			bottomTV.setVisibility(View.GONE);
			rightBtn.setVisibility(View.GONE);
			topTV.setText("已秒光！\n请下一场再来试试~");
			leftBtn.setText("确定");
			leftBtn.setTextColor(getResources().getColor(R.color.white));
			leftBtn.setBackground(getResources().getDrawable(R.drawable.style_rect_fillet_filling_blue));
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				R.style.Dialog_Transparent); // 先得到构造器
		builder.setView(contentView);
		builder.setCancelable(true);
		final AlertDialog dialog = builder.create();
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("0".equals(flag)) {
					Intent intent = new Intent(ProductSafetyXSMBActivity.this,
							UserInvestRecordActivity.class);
					intent.putExtra("from_where", "秒标");
					startActivity(intent);
				}
				dialog.dismiss();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingsManager.setMainProductListFlag(getApplicationContext(),
						true);
				dialog.dismiss();
				mApp.finishAllActivityExceptMain();
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
	 * 秒标详情
	 * 
	 * @param reasonFlag
	 *            自动刷新数据 or 通过按钮点击
	 * @param isFirst
	 *            是否首次请求
	 */
	private void requestXSMBDetails(String borrowStatus, final Enum flag) {
		AsyncXSMBDetail xsmbTask = new AsyncXSMBDetail(
				ProductSafetyXSMBActivity.this, borrowStatus,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								investBtn.setVisibility(View.VISIBLE);
								productInfo = baseInfo.getmProductInfo();
								initViewData(productInfo, flag);
							} else {
								investBtn.setVisibility(View.GONE);
							}
						}
					}
				});
		xsmbTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 根据id获取秒标详情
	 * @param borrowId
	 * @param borrowStatus
	 */
	private void requestXSMBSelectone(String borrowId,String borrowStatus,final Enum reasonFlag){
		AsyncXSMBSelectone task = new AsyncXSMBSelectone(ProductSafetyXSMBActivity.this, borrowId, borrowStatus, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								investBtn.setVisibility(View.VISIBLE);
								productInfo = baseInfo.getmProductInfo();
								initViewData(productInfo, reasonFlag);
							} else {
								investBtn.setVisibility(View.GONE);
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 判断当前用户是否投资过该秒标
	 * @param userId
	 * @param borrowId
	 */
	private void requestCurrentUserInvest(String userId,String borrowId){
		AsyncXSMBCurrentUserInvest task = new AsyncXSMBCurrentUserInvest(ProductSafetyXSMBActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//没有投资过该秒标
								Intent intent = new Intent(ProductSafetyXSMBActivity.this,BidXSMBActivity.class);
								intent.putExtra("PRODUCT_INFO", productInfo);
								startActivity(intent);
								finish();
							}else{
								//投资过该秒标
								showPromptDialog("0");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

}
