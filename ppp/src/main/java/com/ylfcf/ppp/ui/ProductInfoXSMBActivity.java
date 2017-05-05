package com.ylfcf.ppp.ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncAsscociatedCompany;
import com.ylfcf.ppp.async.AsyncXSBIscanbuy;
import com.ylfcf.ppp.async.AsyncXSMBCurrentUserInvest;
import com.ylfcf.ppp.async.AsyncXSMBDetail;
import com.ylfcf.ppp.async.AsyncXSMBSelectone;
import com.ylfcf.ppp.entity.AssociatedCompanyInfo;
import com.ylfcf.ppp.entity.AssociatedCompanyParentInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.BorrowType;
import com.ylfcf.ppp.entity.InvestRecordInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.inter.Inter.OnIsVipUserListener;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.widget.LoadingDialog;

/**
 * 限时秒标 项目信息
 * @author Mr.liu
 *
 */
public class ProductInfoXSMBActivity extends BaseActivity implements
	OnClickListener {
	private static final int REFRESH_VIEW = 5700;
	
	private static final int REQUEST_ASSC_WHAT = 7321;
	private static final int REQUEST_ASSC_SUCCESS = 7322;
	private static final int REQUEST_ASSC_NODATA = 7323;
	
	private static final int REQUEST_XSMB_REFRESH_WHAT = 8291;// 请求接口刷新数据
	private static final int REQUEST_CURRENT_USERINVEST = 8294;//请求当前用户是否投资过该秒标
	private static final int REFRESH_REMAIN_TIME = 8293;//刷新下个秒标的剩余时间
	private static final int REQUEST_XSMB_BTNCLICK_WHAT = 8292;//点击“立即秒杀”按钮
	
	private static final int REQUEST_XSMB_SELECTONE = 8296;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	//项目结构更改后的布局
	private LinearLayout afterLayout;
	private TextView xmjsTV;//项目介绍
	private TextView jkfTV,jkfjsTV,//借款方
		tjfTV,tjfjsTV,//推荐方
		dbfTV,dbfjsTV;//担保方
	private TextView vipPromptText;//vip产品备注
	private ImageView sygzImg;//收益规则的图片
	//项目结构更改前的布局
	private LinearLayout beforeLayout;
	private TextView zjytTV,//资金用途
		rzjsTV,//融资介绍
		dbcsTV,//担保措施
		tzldTV,//投资亮点
		sygzTV;//收益规则
	private LinearLayout sygzLayout;//收益规则

	private Button investBtn;
	private ProjectInfo projectInfo;
	private ProductInfo productInfo;
	private InvestRecordInfo recordInfo;
	private LoadingDialog loadingDialog;
	private AlertDialog.Builder builder = null; // 先得到构造器
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private enum ReasonFlag {
		REFRESH_DATA, // 页面刷新数据
		BTN_CLICK,// 通过按钮点击
		RECORD_DATA //投资记录跳转
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_XSMB_REFRESH_WHAT:
				requestXSMBDetails("发布",ReasonFlag.REFRESH_DATA);
				break;
			case REQUEST_XSMB_BTNCLICK_WHAT:
				requestXSMBDetails("发布",ReasonFlag.BTN_CLICK);
			case REQUEST_CURRENT_USERINVEST:
				requestCurrentUserInvest(SettingsManager.getUserId(getApplicationContext()),productInfo.getId());
				break;
			case REQUEST_ASSC_WHAT:
				requestAssociatedCompany(projectInfo.getLoan_id(), projectInfo.getRecommend_id(), projectInfo.getGuarantee_id());
				break;
			case REQUEST_ASSC_SUCCESS:
				AssociatedCompanyParentInfo parentInfo = (AssociatedCompanyParentInfo) msg.obj;
				initXMYSBData(parentInfo);
				break;
			case REQUEST_ASSC_NODATA:
				
				break;
			case REFRESH_REMAIN_TIME:
				long times = (Long) msg.obj;
				updateCountDown(times);
				break;
			case REFRESH_VIEW:
				Bundle bundle = (Bundle) msg.obj;
				refreshView(bundle);
				break;
			case REQUEST_XSMB_SELECTONE:
				requestXSMBSelectone(recordInfo.getBorrow_id(), "发布",ReasonFlag.RECORD_DATA);
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
		setContentView(R.layout.borrow_info_activity);
		mApp.addActivity(this);
		builder = new AlertDialog.Builder(ProductInfoXSMBActivity.this,
				R.style.Dialog_Transparent); // 先得到构造器
		loadingDialog = new LoadingDialog(ProductInfoXSMBActivity.this, "正在加载...", R.anim.loading);
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		if(bundle != null){
			productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
			projectInfo = (ProjectInfo) bundle.getSerializable("PROJECT_INFO");
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
	
	private void findViews() {
		topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView) findViewById(R.id.common_page_title);
		topTitleTV.setText("项目信息");

		afterLayout = (LinearLayout) findViewById(R.id.borrow_info_activity_after_layout);
		xmjsTV = (TextView) findViewById(R.id.borrow_info_activity_xmjs);
		jkfTV = (TextView) findViewById(R.id.zxd_xmysb_layout_jkf_text);
		jkfjsTV = (TextView) findViewById(R.id.zxd_xmysb_layout_jkfjs_text);
		tjfTV = (TextView) findViewById(R.id.zxd_xmysb_layout_tjf_text);
		tjfjsTV = (TextView) findViewById(R.id.zxd_xmysb_layout_tjfjs_text);
		dbfTV = (TextView) findViewById(R.id.zxd_xmysb_layout_dbf_text);
		dbfjsTV = (TextView) findViewById(R.id.zxd_xmysb_layout_dbfjs_text);
		vipPromptText = (TextView)findViewById(R.id.borrow_info_activity_vipprompt);
		
		beforeLayout = (LinearLayout) findViewById(R.id.borrow_info_activity_before_layout);
		zjytTV = (TextView) findViewById(R.id.borrow_info_activity_zjyt);
		rzjsTV = (TextView) findViewById(R.id.borrow_info_activity_rzjs);
		dbcsTV = (TextView) findViewById(R.id.borrow_info_activity_dbcs);
		tzldTV = (TextView) findViewById(R.id.borrow_info_activity_tzld);
		sygzTV = (TextView) findViewById(R.id.borrow_info_activity_sygz);
		sygzImg = (ImageView) findViewById(R.id.borrow_info_activity_sygz_img);
		sygzLayout = (LinearLayout) findViewById(R.id.borrow_info_activity_sygz_layout);
		
		investBtn = (Button) findViewById(R.id.borrow_info_activity_bidBtn);
		investBtn.setEnabled(false);
		investBtn.setOnClickListener(this);
		if(productInfo != null && BorrowType.VIP.equals(productInfo.getBorrow_type())){
			vipPromptText.setVisibility(View.VISIBLE);
			vipPromptText.setText("*提示：\n1. 本产品不参与平台其他优惠活动。\n2.元立方金服拥有本产品的最终解释权。");
		}else{
			vipPromptText.setVisibility(View.VISIBLE);
			vipPromptText.setText("*提示：\n* 元立方金服拥有本产品的最终解释权。");
		}
		
		if(projectInfo != null){
			if("发布".equals(projectInfo.getStatus())){
				afterLayout.setVisibility(View.VISIBLE);
				beforeLayout.setVisibility(View.GONE);
				handler.sendEmptyMessage(REQUEST_ASSC_WHAT);
			}else{
				afterLayout.setVisibility(View.GONE);
				beforeLayout.setVisibility(View.VISIBLE);
			}
		}
		initData();
	}

	private void initData(){
		if (projectInfo != null) {
			new ImageLoadThread(projectInfo.getCapital(),0).start();
			
			new ImageLoadThread(projectInfo.getIntroduced(),1).start();
			
			new ImageLoadThread(projectInfo.getMeasures(),2).start();
			
			new ImageLoadThread(projectInfo.getInvest_point(), 3).start();

			new ImageLoadThread(projectInfo.getSummary(), 4).start();
		}
	}
	
	/**
	 * 刷新页面
	 * @param bundle
	 */
	private void refreshView(Bundle bundle){
		if(bundle == null)
			return;
		CharSequence htmlText = bundle.getCharSequence("HTML_TEXT");
		int position = bundle.getInt("POSITION");
		if(position == 0){
			//产品结构更改之前的“资金用途”
			zjytTV.setText(htmlText);
		}else if(position == 1){
			//产品结构更改之前的“融资方介绍”
			rzjsTV.setText(htmlText);
		}else if(position == 2){
			//产品结构更改之前的“融资方介绍”
			dbcsTV.setText(htmlText);
		}else if(position == 3){
			//产品结构更改之前的“投资亮点”
			tzldTV.setText(htmlText);
		}else if(position == 4){
			//产品结构更改之后的“项目介绍”
			xmjsTV.setText(htmlText);
		}
	}
	
	class ImageLoadThread extends Thread {
		private String htmlText1 = "";
		private  int position1 = 0;
		public ImageLoadThread(String htmlText,int position){
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
						int[] screen = SettingsManager.getScreenDispaly(ProductInfoXSMBActivity.this);
						drawable = Drawable.createFromStream(url.openStream(),null);
						if(drawable != null){
							int imageIntrinsicWidth = drawable.getIntrinsicWidth();
							float imageIntrinsicHeight = (float)drawable.getIntrinsicHeight();
							int curImageHeight = (int) (screen[0]*(imageIntrinsicHeight/imageIntrinsicWidth));
							drawable.setBounds(0, 0, screen[0],curImageHeight);//四个参数含义为左上角、右下角坐标确定的一个矩形，图片就在这个矩形范围内画出来
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
	
	/**
	 * 初始化项目要素表
	 */
	private void initXMYSBData(AssociatedCompanyParentInfo parentInfo){
		AssociatedCompanyInfo loanInfo = parentInfo.getLoanInfo();
		AssociatedCompanyInfo recommendInfo = parentInfo.getRecommendInfo();
		AssociatedCompanyInfo guaranteeInfo = parentInfo.getGuaranteeInfo();
		
		jkfTV.setText(Html.fromHtml(loanInfo.getCompany_name()));
		jkfjsTV.setText(Html.fromHtml(loanInfo.getIntroduce().trim()));
		tjfTV.setText(Html.fromHtml(recommendInfo.getCompany_name().trim()));
		tjfjsTV.setText(Html.fromHtml(recommendInfo.getIntroduce().trim()));
		dbfTV.setText(Html.fromHtml(guaranteeInfo.getCompany_name().trim()));
		dbfjsTV.setText(Html.fromHtml(guaranteeInfo.getIntroduce().trim()));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.borrow_info_activity_bidBtn:
			// 从SettingsManager中读取密码，如果为空意味着没有登录。
			boolean isLogin = !SettingsManager.getLoginPassword(ProductInfoXSMBActivity.this).isEmpty()
					&& !SettingsManager.getUser(ProductInfoXSMBActivity.this).isEmpty();
			// isLogin = true;// 测试
			Intent intent = new Intent();
			// 1、检测是否已经登录
			if (isLogin) {
				//判断是否实名绑卡
				handler.sendEmptyMessage(REQUEST_XSMB_BTNCLICK_WHAT);
//				if("新手标".equals(productInfo.getBorrow_type())){
//					isCanbuyXSB(SettingsManager.getUserId(getApplicationContext()), productInfo.getId());
//				}else if(BorrowType.VIP.equals(productInfo.getBorrow_type())){
//					checkIsVip();
//				}else if(BorrowType.SUYING.equals(productInfo.getBorrow_type()) || BorrowType.BAOYING.equals(productInfo.getBorrow_type()) ||
//						BorrowType.WENYING.equals(productInfo.getBorrow_type())){
//					checkIsVerify("政信贷投资");
//				}else if("秒标".equals(productInfo.getBorrow_type())){
//					checkIsVerify("秒标");
//				}else{
//					//私人尊享
//					if(productInfo.getBorrow_name().contains("私人尊享")){
//						checkIsVerify("私人尊享");
//					}
//				}
			} else {
				// 未登录，跳转到登录页面
				intent.setClass(ProductInfoXSMBActivity.this,LoginActivity.class);
				startActivity(intent);
				investBtn.setEnabled(true);
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 显示弹出框
	 * @param type
	 * @param msg
	 */
	private void showMsgDialog(Context context,final String type,String msg){
		View contentView = LayoutInflater.from(context)
				.inflate(R.layout.borrow_details_msg_dialog, null);
		final Button sureBtn = (Button) contentView
				.findViewById(R.id.borrow_details_msg_dialog_surebtn);
		final TextView msgTV = (TextView) contentView
				.findViewById(R.id.borrow_details_msg_dialog_msg);
		final ImageView delBtn = (ImageView) contentView
				.findViewById(R.id.borrow_details_msg_dialog_delete);
		if("不能购买新手标".equals(type)){
			sureBtn.setVisibility(View.GONE);
		}else{
			sureBtn.setVisibility(View.VISIBLE);
		}
		msgTV.setText(msg);
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if("实名认证".equals(type)){
					intent.setClass(ProductInfoXSMBActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					if("新手标".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "新手标投资");
					}else if("vip".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "VIP投资");
					}else if(BorrowType.SUYING.equals(productInfo.getBorrow_type()) || BorrowType.BAOYING.equals(productInfo.getBorrow_type())
							|| BorrowType.WENYING.equals(productInfo.getBorrow_type())){
						bundle.putString("type", "政信贷投资");
					}else if("秒标".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "秒标投资");
					}else{
						if(productInfo.getBorrow_name().contains("私人尊享")){
							bundle.putString("type", "私人尊享");
						}
					}
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
					investBtn.setEnabled(true);
				}else if("绑卡".equals(type)){
					Bundle bundle = new Bundle();
					if("新手标".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "新手标投资");
					}else if("vip".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "VIP投资");
					}else if(BorrowType.SUYING.equals(productInfo.getBorrow_type()) || BorrowType.BAOYING.equals(productInfo.getBorrow_type())
							|| BorrowType.WENYING.equals(productInfo.getBorrow_type())){
						bundle.putString("type", "政信贷投资");
					}else{
						if(productInfo.getBorrow_name().contains("私人尊享")){
							bundle.putString("type", "私人尊享");
						}
					}
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					intent.setClass(ProductInfoXSMBActivity.this, BindCardActivity.class);
					startActivity(intent);
					investBtn.setEnabled(true);
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
		// 参数都设置完成了，创建并显示出来
		dialog.show();
		// 设置dialog的宽度
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth() * 6 / 7;
		lp.height = display.getHeight()/3;
		dialog.getWindow().setAttributes(lp);
	}
	
	/**
	 * 验证用户是否已经认证
	 * @param type “充值”,“提现”
	 */
	private void checkIsVerify(final String type){
		investBtn.setEnabled(false);
		RequestApis.requestIsVerify(ProductInfoXSMBActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//用户已经实名，在这个页面只判断是否实名即可。不判断有没有绑卡
//					checkIsBindCard(type);
					if("新手标投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoXSMBActivity.this, BidXSBActivity.class);
					}else if("政信贷投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoXSMBActivity.this, BidZXDActivity.class);
					}else if("VIP投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoXSMBActivity.this, BidVIPActivity.class);
					}else if("私人尊享".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoXSMBActivity.this, BidSRZXActivity.class);
					}else if("秒标".equals(type)){
						// 请求是否还可以购买
						handler.sendEmptyMessage(REQUEST_XSMB_BTNCLICK_WHAT);
						return;
					}
					startActivity(intent);
					investBtn.setEnabled(true);
					finish();
				}else{
					//用户没有实名
					showMsgDialog(ProductInfoXSMBActivity.this, "实名认证", "请先实名认证！");
				}
			}
			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}
	
	//即时更新倒计时
			private void updateCountDown(long times){
				int hour = 0,minute = 0,second = 0;
				String hourStr = "",minuteStr = "",secondStr = "";
				times -= 1000;
				if(times <= 0){
					investBtn.setEnabled(true);
					investBtn.setText("立即秒杀");
					investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
					return;
				}
				hour = (int) (times/1000/3600);
				minute = (int) ((times/1000%3600)/60);
				second = (int) (times/1000%3600%60);
				if(hour < 10){
					hourStr = "0" + hour;
				}else{
					hourStr = hour + "";
				}
				if(minute < 10){
					minuteStr = "0" + minute;
				}else{
					minuteStr = minute + "";
				}
				if(second < 10){
					secondStr = "0" + second;
				}else{
					secondStr = second + "";
				}
				investBtn.setText("距离下一场“秒杀”开始还剩："+hourStr+"时"+minuteStr+"分"+secondStr+"秒");
				investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_21dp));
				Message msg = handler.obtainMessage(REFRESH_REMAIN_TIME);
				msg.obj = times;
				handler.sendMessageDelayed(msg, 1000L);
			}
	
	/**
	 * 判断用户是否为vip用户
	 */
	private void checkIsVip(){
		RequestApis.requestIsVip(ProductInfoXSMBActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVipUserListener() {
			@Override
			public void isVip(boolean isvip) {
				if(isvip){
					checkIsVerify("VIP投资"); // 只判断有没有实名，不再判断是否绑卡
				}else{
					//非VIP用户不能投资
					showCanotInvestVIPDialog();
				}
			}
		});
	}
	
	/**
	 * 显示弹出框  非VIP用户不能购买元月盈
	 * @param type
	 * @param msg
	 */
	private void showCanotInvestVIPDialog(){
		View contentView = LayoutInflater.from(this)
				.inflate(R.layout.borrow_details_vip_msg_dialog, null);
		final Button leftBtn = (Button) contentView.
				findViewById(R.id.borrow_details_vip_msg_dialog_leftbtn);
		final Button rightBtn = (Button) contentView.
				findViewById(R.id.borrow_details_vip_msg_dialog_rightbtn);
		final TextView msgTV = (TextView) contentView
				.findViewById(R.id.borrow_details_vip_msg_dialog_msg);
		final ImageView delBtn = (ImageView) contentView
				.findViewById(R.id.borrow_details_vip_msg_dialog_delete);
		msgTV.setText("非VIP用户不能购买VIP产品啦~");
		builder.setView(contentView);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				setResult(111,intent);
				dialog.dismiss();
				finish();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProductInfoXSMBActivity.this,VIPProductCJWTActivity.class);
				startActivity(intent);
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
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = display.getWidth() * 6 / 7;
		lp.height = display.getHeight()/3;
		dialog.getWindow().setAttributes(lp);
	}
	
	/**
	 * 
	 * @param flag 1表示已抢光 0表示秒杀机会用完
	 */
	private void showPromptDialog(final String flag){
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
		if("0".equals(flag)){
			bottomTV.setVisibility(View.VISIBLE);
			rightBtn.setVisibility(View.VISIBLE);
			topTV.setText("您的本场秒杀机会已使用");
			bottomTV.setText("每场秒杀，每个用户只有一次秒杀机会哟~");
			leftBtn.setText("查看投资记录");
			leftBtn.setTextColor(getResources().getColor(R.color.common_topbar_bg_color));
			leftBtn.setBackground(getResources().getDrawable(R.drawable.style_rect_fillet_blue));
			rightBtn.setText("关注其他项目");
		}else if("1".equals(flag)){
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
				if("0".equals(flag)){
					Intent intent = new Intent(ProductInfoXSMBActivity.this,UserInvestRecordActivity.class);
					intent.putExtra("from_where", "秒标");
					startActivity(intent);
				}
				dialog.dismiss();
			}
		});
		rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingsManager.setMainProductListFlag(getApplicationContext(), true);	
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
	 * 投资按钮倒计时
	 * @param productInfo
	 * @throws ParseException 
	 */
	long remainTimeL = 0l;
	private void initBidBtnCountDown(ProductInfo productInfo,Enum flag){
		investBtn.setEnabled(false);
		String nowTimeStr = productInfo.getNow_time();
		String willStartTimeStr = productInfo.getWill_start_time();//开始时间
		int hour = 0,minute = 0,second = 0;
		String hourStr = "",minuteStr = "",secondStr = "";
		try {
			Date nowDate = sdf.parse(nowTimeStr);
			Date willStartDate = sdf.parse(willStartTimeStr);
			remainTimeL = willStartDate.getTime() - nowDate.getTime();
			if(remainTimeL >= 0){
				hour = (int) (remainTimeL/1000/3600);
				minute = (int) ((remainTimeL/1000%3600)/60);
				second = (int) (remainTimeL/1000%3600%60);
				if(hour < 10){
					hourStr = "0" + hour;
				}else{
					hourStr = hour + "";
				}
				if(minute < 10){
					minuteStr = "0" + minute;
				}else{
					minuteStr = minute + "";
				}
				if(second < 10){
					secondStr = "0" + second;
				}else{
					secondStr = second + "";
				}
				investBtn.setText("距离下一场“秒杀”开始还剩："+hourStr+"时"+minuteStr+"分"+secondStr+"秒");
				investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_21dp));
				Message msg = handler.obtainMessage(REFRESH_REMAIN_TIME);
				msg.obj = remainTimeL;
				handler.sendMessage(msg);
			}else{
//				//正在投资中。。
				investBtn.setText("立即秒杀");
				investBtn.setEnabled(true);
				investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
				if(flag == ReasonFlag.BTN_CLICK){
					handler.sendEmptyMessage(REQUEST_CURRENT_USERINVEST);
				}else if(flag == ReasonFlag.REFRESH_DATA){
					
				}
			}
		} catch (ParseException e) {
			investBtn.setText("投资结束");
			investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
			e.printStackTrace();
		}
	}
	
	private void initViewData(ProductInfo productInfo,Enum flag) {
		if(flag == ReasonFlag.REFRESH_DATA || flag == ReasonFlag.RECORD_DATA){
			//刷新数据
			if("未满标".equals(productInfo.getMoney_status())){
				initBidBtnCountDown(productInfo,flag);//倒计时
			}else{
				investBtn.setEnabled(false);
				investBtn.setText("投资结束");
				investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
			}
		}else if(flag == ReasonFlag.BTN_CLICK){
			//点击“立即秒杀”按钮
			if("未满标".equals(productInfo.getMoney_status())){
				//未满标，再请求用户是否已经投资过该秒标
				initBidBtnCountDown(productInfo,flag);//倒计时
			}else{
				//已满标
				investBtn.setEnabled(false);
				investBtn.setText("投资结束");
				investBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX ,getResources().getDimensionPixelSize(R.dimen.common_measure_26dp));
				showPromptDialog("1");
			}
		}
	}
	
	/**
	 * 判断用户是否已经绑卡
	 * @param type "充值提现"
	 */
	private void checkIsBindCard(final String type){
		RequestApis.requestIsBinding(ProductInfoXSMBActivity.this, SettingsManager.getUserId(getApplicationContext()), "宝付", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//用户已经绑卡
					if("新手标投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoXSMBActivity.this, BidXSBActivity.class);
					}else if("政信贷投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoXSMBActivity.this, BidZXDActivity.class);
					}else if("VIP投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoXSMBActivity.this, BidVIPActivity.class);
					}
					startActivity(intent);
					investBtn.setEnabled(true);
					finish();
				}else{
					//用户还没有绑卡
					showMsgDialog(ProductInfoXSMBActivity.this, "绑卡", "因我司变更支付渠道，请您重新绑卡！");
				}
			}
		});
	}
	
	/**
	 * 关联公司
	 * @param loanId
	 * @param recommendId
	 * @param guaranteeId
	 */
	private void requestAssociatedCompany(String loanId,String recommendId,String guaranteeId){
		if(loadingDialog != null){
			loadingDialog.show();
		}
		AsyncAsscociatedCompany task = new AsyncAsscociatedCompany(ProductInfoXSMBActivity.this, loanId, recommendId, guaranteeId, 
				new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						if(loadingDialog != null && loadingDialog.isShowing()){
							loadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								AssociatedCompanyParentInfo info = baseInfo.getAssociatedCompanyParentInfo();
								Message msg = handler.obtainMessage(REQUEST_ASSC_SUCCESS);
								msg.obj = info;
								handler.sendMessage(msg);
							}else{
								Message msg = handler.obtainMessage(REQUEST_ASSC_NODATA);
								msg.obj = baseInfo;
								handler.sendMessage(msg);
							}
						}
					}
		});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 判断是否可以购买新手标
	 * @param userId
	 * @param borrowId
	 */
	private void isCanbuyXSB(String userId,String borrowId){
		AsyncXSBIscanbuy task = new AsyncXSBIscanbuy(ProductInfoXSMBActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//用户可以购买新手标
								Intent intent = new Intent();
								intent.putExtra("PRODUCT_INFO", productInfo);
								intent.setClass(ProductInfoXSMBActivity.this, BidXSBActivity.class);
								startActivity(intent);
							}else if(resultCode == 1001){
								//请先进行实名
								showMsgDialog(ProductInfoXSMBActivity.this, "实名认证", "请先实名认证！");
							}else if(resultCode == 1002){
								//请先进行绑卡
								boolean isNewUser = SettingsManager.checkIsNewUser(SettingsManager.getUserRegTime(getApplicationContext()));
								if(isNewUser){
									showMsgDialog(ProductInfoXSMBActivity.this, "绑卡", "请您先绑卡！");
								}else{
									showMsgDialog(ProductInfoXSMBActivity.this, "绑卡", "因我司变更支付渠道，请您重新绑卡！");
								}
							}else{
								showMsgDialog(ProductInfoXSMBActivity.this, "不能购买新手标", "此产品限首次购买用户专享！");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
	
	/**
	 * 秒标详情
	 * 
	 * @param reasonFlag
	 *            自动刷新数据 or 通过按钮点击
	 * @param isFirst
	 *            是否首次请求
	 */
	private void requestXSMBDetails(String borrowStatus,final Enum flag) {
		AsyncXSMBDetail xsmbTask = new AsyncXSMBDetail(
				ProductInfoXSMBActivity.this, borrowStatus,new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								investBtn.setVisibility(View.VISIBLE);
								productInfo = baseInfo.getmProductInfo();
								initViewData(productInfo,flag);
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
		AsyncXSMBSelectone task = new AsyncXSMBSelectone(ProductInfoXSMBActivity.this, borrowId, borrowStatus, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								investBtn.setVisibility(View.VISIBLE);
								productInfo = baseInfo.getmProductInfo();
								initViewData(productInfo,reasonFlag);
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
		AsyncXSMBCurrentUserInvest task = new AsyncXSMBCurrentUserInvest(ProductInfoXSMBActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						investBtn.setEnabled(true);
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//没有投资过该秒标
								Intent intent = new Intent(ProductInfoXSMBActivity.this,BidXSMBActivity.class);
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
