package com.ylfcf.ppp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Html.ImageGetter;
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
import com.ylfcf.ppp.async.AsyncXSBIscanbuy;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.BorrowType;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.inter.Inter.OnIsVipUserListener;
import com.ylfcf.ppp.util.Constants;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * 安全保障
 * @author Administrator
 *
 */
public class ProductSafetyActivity extends BaseActivity implements OnClickListener{
	private static final int REFRESH_VIEW = 5710;
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	//产品交易结构更改之前的布局
	private LinearLayout beforeLayout;
	private TextView zjaqTV;
	private TextView zcaqTV;
	
	//产品交易结构更改之后的布局
	private LinearLayout afterLayout;
	private TextView dbcsTV;
	private TextView hklyTV;
	
	private Button investBtn;//立即投资
	private ProjectInfo projectInfo;
	private ProductInfo productInfo;
	private AlertDialog.Builder builder = null; // 先得到构造器

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				Bundle bundle = (Bundle) msg.obj;
				refreshView(bundle);
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
		builder = new AlertDialog.Builder(ProductSafetyActivity.this,
				R.style.Dialog_Transparent); // 先得到构造器
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		if(bundle != null){
			projectInfo = (ProjectInfo) bundle.getSerializable("PROJECT_INFO");
			productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
		}
		findViews();
	}

	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("安全保障");
		
		beforeLayout = (LinearLayout) findViewById(R.id.product_safety_activity_before_layout);
		zjaqTV = (TextView) findViewById(R.id.productsafety_activity_zjaq);
		zcaqTV = (TextView) findViewById(R.id.productsafety_activity_zcaq);
		
		afterLayout = (LinearLayout) findViewById(R.id.product_safety_activity_after_layout);
		dbcsTV = (TextView) findViewById(R.id.productsafety_activity_dbcs);
		hklyTV = (TextView) findViewById(R.id.productsafety_activity_after_hkly);
		
		investBtn = (Button) findViewById(R.id.product_safety_activity_bidBtn);
		investBtn.setOnClickListener(this);
		Date addDate = null;
		try{
			addDate = sdf.parse(productInfo.getAdd_time());
		}catch (Exception e){
			e.printStackTrace();
		}
		if(productInfo != null){
			if("未满标".equals(productInfo.getMoney_status())){
				if(SettingsManager.checkActiveStatusBySysTime(productInfo.getAdd_time(),SettingsManager.yyyJIAXIStartTime,
						SettingsManager.yyyJIAXIEndTime) == 0 && "元年鑫".equals(productInfo.getBorrow_type())&& Constants.UserType.USER_COMPANY.
						equals(SettingsManager.getUserType(ProductSafetyActivity.this))){
					investBtn.setEnabled(false);
				}else{
					investBtn.setEnabled(true);
				}
				investBtn.setText("立即投资");
			}else{
				investBtn.setEnabled(false);
				investBtn.setText("投资已结束");
			}
		} 
		if(projectInfo == null){
			beforeLayout.setVisibility(View.GONE);
			afterLayout.setVisibility(View.VISIBLE);
			return;
		}
		if("发布".equals(projectInfo.getStatus())){
			beforeLayout.setVisibility(View.GONE);
			afterLayout.setVisibility(View.VISIBLE);
		}else{
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
	
	private void initData(){
		if(projectInfo!=null){
			//产品交易结构更改之前的“资金安全”
			new ImageLoadThread(projectInfo.getCapital_safe(),0).start();
			//产品交易结构更改之前的“还款来源”
			new ImageLoadThread(projectInfo.getRepay_from(),1).start();
			
			//产品交易结构更改之前的“担保措施”
			new ImageLoadThread(projectInfo.getMeasures(),2).start();
			//产品交易结构更改之后的“还款来源”
			new ImageLoadThread(projectInfo.getRepay_from(),3).start();
			
		}
	}
	
	/**
	 * 刷新控件
	 * @param bundle
	 */
	private void refreshView(Bundle bundle){
		if(bundle == null)
			return;
		CharSequence htmlText = bundle.getCharSequence("HTML_TEXT");
		int position = bundle.getInt("POSITION");
		if(position == 0){
			//产品结构更改之前的“资金安全”
			if(htmlText.length() > 0){
				zjaqTV.setText(htmlText);
			}
		}else if(position == 1){
			//产品结构更改之前的“还款来源”
			zcaqTV.setText(htmlText);
		}else if(position == 2){
			//产品结构更改之后的“担保措施”
			dbcsTV.setText(htmlText);
		}else if(position == 3){
			//产品结构更改之后的“还款来源”
			hklyTV.setText(htmlText);
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
						int[] screen = SettingsManager.getScreenDispaly(ProductSafetyActivity.this);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.product_safety_activity_bidBtn:
			// 从SettingsManager中读取密码，如果为空意味着没有登录。
			boolean isLogin = !SettingsManager.getLoginPassword(
					ProductSafetyActivity.this).isEmpty()
					&& !SettingsManager.getUser(ProductSafetyActivity.this).isEmpty();
			// isLogin = true;// 测试
			Intent intent = new Intent();
			// 1、检测是否已经登录
			if (isLogin) {
				if("新手标".equals(productInfo.getBorrow_type())){
					isCanbuyXSB(SettingsManager.getUserId(getApplicationContext()), productInfo.getId());
				}else if("vip".equals(productInfo.getBorrow_type())){
					checkIsVip();
				}else if(BorrowType.SUYING.equals(productInfo.getBorrow_type()) || BorrowType.BAOYING.equals(productInfo.getBorrow_type())
							|| BorrowType.WENYING.equals(productInfo.getBorrow_type()) || BorrowType.YUANNIANXIN.equals(productInfo.getBorrow_type())){
					checkIsVerify("政信贷投资");
				}else{
					//私人尊享
					if(productInfo.getBorrow_name().contains("私人尊享")){
						checkIsVerify("私人尊享");
					}
				}
			} else {
				// 未登录，跳转到登录页面
				intent.setClass(ProductSafetyActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
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
					intent.setClass(ProductSafetyActivity.this,UserVerifyActivity.class);
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
					intent.setClass(ProductSafetyActivity.this, BindCardActivity.class);
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
	 * 判断用户是否为vip用户
	 */
	private void checkIsVip(){
		RequestApis.requestIsVip(ProductSafetyActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVipUserListener() {
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
	 */
	private void showCanotInvestVIPDialog(){
		View contentView = LayoutInflater.from(this)
				.inflate(R.layout.borrow_details_vip_msg_dialog, null);
		final Button leftBtn = (Button) contentView
				.findViewById(R.id.borrow_details_vip_msg_dialog_leftbtn);
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
				Intent intent = new Intent(ProductSafetyActivity.this,VIPProductCJWTActivity.class);
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
	 * 验证用户是否已经认证
	 * @param type “充值”,“提现”
	 */
	private void checkIsVerify(final String type){
		investBtn.setEnabled(false);
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		RequestApis.requestIsVerify(ProductSafetyActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				Intent intent = new Intent();
				if(flag){
					//用户已经实名，在这个页面只判断是否实名即可。不判断有没有绑卡
//					checkIsBindCard(type);
					if("新手标投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductSafetyActivity.this, BidXSBActivity.class);
					}else if("政信贷投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductSafetyActivity.this, BidZXDActivity.class);
					}else if("VIP投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductSafetyActivity.this, BidVIPActivity.class);
					}else if("私人尊享".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductSafetyActivity.this, BidSRZXActivity.class);
					}
					investBtn.setEnabled(true);
					startActivity(intent);
					finish();
				}else{
					//用户没有实名
					showMsgDialog(ProductSafetyActivity.this, "实名认证", "请先实名认证！");
				}
			}
			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}
	
	/**
	 * 判断用户是否已经绑卡
	 * @param type "充值提现"
	 */
	private void checkIsBindCard(final String type){
		RequestApis.requestIsBinding(ProductSafetyActivity.this, SettingsManager.getUserId(getApplicationContext()), "宝付", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//用户已经绑卡
					if("新手标投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductSafetyActivity.this, BidXSBActivity.class);
					}else if("政信贷投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductSafetyActivity.this, BidZXDActivity.class);
					}else if("VIP投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductSafetyActivity.this, BidVIPActivity.class);
					}
					startActivity(intent);
					investBtn.setEnabled(true);
					finish();
				}else{
					//用户还没有绑卡
					showMsgDialog(ProductSafetyActivity.this, "绑卡", "因我司变更支付渠道，请您重新绑卡！");
				}
				
			}
		});
	}
	
	/**
	 * 判断是否可以购买新手标
	 * @param userId
	 * @param borrowId
	 */
	private void isCanbuyXSB(String userId,String borrowId){
		AsyncXSBIscanbuy task = new AsyncXSBIscanbuy(ProductSafetyActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//用户可以购买新手标
								Intent intent = new Intent();
								intent.putExtra("PRODUCT_INFO", productInfo);
								intent.setClass(ProductSafetyActivity.this, BidXSBActivity.class);
								startActivity(intent);
							}else if(resultCode == 1001){
								//请先进行实名
								showMsgDialog(ProductSafetyActivity.this, "实名认证", "请先实名认证！");
							}else if(resultCode == 1002){
								//请先进行绑卡
								boolean isNewUser = SettingsManager.checkIsNewUser(SettingsManager.getUserRegTime(getApplicationContext()));
								if(isNewUser){
									showMsgDialog(ProductSafetyActivity.this, "绑卡", "请您先绑卡！");
								}else{
									showMsgDialog(ProductSafetyActivity.this, "绑卡", "因我司变更支付渠道，请您重新绑卡！");
								}
							}else{
								showMsgDialog(ProductSafetyActivity.this, "不能购买新手标", "此产品限首次购买用户专享！");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
