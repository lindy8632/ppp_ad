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
import com.ylfcf.ppp.async.AsyncAsscociatedCompany;
import com.ylfcf.ppp.async.AsyncXSBIscanbuy;
import com.ylfcf.ppp.entity.AssociatedCompanyInfo;
import com.ylfcf.ppp.entity.AssociatedCompanyParentInfo;
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

/**
 * 项目信息
 * 
 * @author Administrator
 * 
 */
public class ProductInfoActivity extends BaseActivity implements
		OnClickListener {
	private static final int REFRESH_VIEW = 5700;
	
	private static final int REQUEST_ASSC_WHAT = 7321;
	private static final int REQUEST_ASSC_SUCCESS = 7322;
	private static final int REQUEST_ASSC_NODATA = 7323;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;

	//项目结构更改后的布局
	private LinearLayout afterLayout;
	private TextView xmjsTV;//项目介绍
	private TextView jkfTV,jkfjsTV,//借款方
		tjfTV,tjfjsTV,//推荐方
		dbfTV,dbfjsTV;//担保方
	private LinearLayout tjfIntroLayout,tjfLayout;

	private View tjfLine1,tjfLine2;
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
	private AlertDialog.Builder builder = null; // 先得到构造器
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_ASSC_WHAT:
				requestAssociatedCompany(projectInfo.getLoan_id(), projectInfo.getRecommend_id(), projectInfo.getGuarantee_id());
				break;
			case REQUEST_ASSC_SUCCESS:
				AssociatedCompanyParentInfo parentInfo = (AssociatedCompanyParentInfo) msg.obj;
				initXMYSBData(parentInfo);
				break;
			case REQUEST_ASSC_NODATA:
				
				break;
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
		setContentView(R.layout.borrow_info_activity);
		builder = new AlertDialog.Builder(ProductInfoActivity.this,
				R.style.Dialog_Transparent); // 先得到构造器
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		if(bundle != null){
			productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
			projectInfo = (ProjectInfo) bundle.getSerializable("PROJECT_INFO");
		}
		findViews();
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
		tjfIntroLayout = (LinearLayout) findViewById(R.id.zxd_xmysb_layout_tjf_intro_layout);
		tjfLayout = (LinearLayout) findViewById(R.id.zxd_xmysb_layout_tjf_layout);
		tjfLine1 = findViewById(R.id.zxd_xmysb_tjf_line1);
		tjfLine2 = findViewById(R.id.zxd_xmysb_tjf_line2);
		if(productInfo != null && productInfo.getInterest_period() != null){
			if(productInfo.getInterest_period().contains("32") && SettingsManager.checkFloatRate(productInfo)){
				sygzLayout.setVisibility(View.VISIBLE);
				sygzTV.setVisibility(View.VISIBLE);
				sygzImg.setVisibility(View.VISIBLE);
				sygzTV.setText("32天定期理财产品”预期年化收益率“6%起步,根据投资本金的增加,上调预期年化收益率,6.3%封顶。");
				sygzImg.setBackgroundResource(R.drawable.borrow_details_yyt_sygz_img);
	        }else if(productInfo.getInterest_period().contains("92") && SettingsManager.checkFloatRate(productInfo)){
	        	sygzLayout.setVisibility(View.VISIBLE);
				sygzTV.setVisibility(View.VISIBLE);
				sygzImg.setVisibility(View.VISIBLE);
	        	sygzTV.setText("92天定期理财产品”预期年化收益率“6.5%起步,根据投资本金的增加,上调预期年化收益率,6.8%封顶。");
				sygzImg.setBackgroundResource(R.drawable.borrow_details_yjr_sygz_img);
	        }else if(productInfo.getInterest_period().contains("182") && SettingsManager.checkFloatRate(productInfo)){
	        	sygzLayout.setVisibility(View.VISIBLE);
				sygzTV.setVisibility(View.VISIBLE);
				sygzImg.setVisibility(View.VISIBLE);
	        	sygzTV.setText("182天定期理财产品”预期年化收益率“7%起步,根据投资本金的增加,上调预期年化收益率,7.3%封顶。");
				sygzImg.setBackgroundResource(R.drawable.borrow_details_ydh_sygz_img);
	        }
		}
		
		investBtn = (Button) findViewById(R.id.borrow_info_activity_bidBtn);
		investBtn.setOnClickListener(this);
		if("2".equals(projectInfo.getType())){
			//这一类型的资产包不会显示推荐方
			tjfLayout.setVisibility(View.GONE);
			tjfIntroLayout.setVisibility(View.GONE);
			tjfLine1.setVisibility(View.GONE);
			tjfLine2.setVisibility(View.GONE);
		}else{
			tjfLayout.setVisibility(View.VISIBLE);
			tjfIntroLayout.setVisibility(View.VISIBLE);
			tjfLine1.setVisibility(View.VISIBLE);
			tjfLine2.setVisibility(View.VISIBLE);
		}
		if(productInfo != null && BorrowType.VIP.equals(productInfo.getBorrow_type())){
			vipPromptText.setVisibility(View.VISIBLE);
			vipPromptText.setText("*提示：\n1. 本产品不参与平台其他优惠活动。\n2.元立方金服拥有本产品的最终解释权。");
			if("30".equals(projectInfo.getId())){//新的资产包不再显示推荐方
				tjfLayout.setVisibility(View.GONE);
				tjfIntroLayout.setVisibility(View.GONE);
				tjfLine1.setVisibility(View.GONE);
				tjfLine2.setVisibility(View.GONE);
			}else{
				tjfLayout.setVisibility(View.VISIBLE);
				tjfIntroLayout.setVisibility(View.VISIBLE);
				tjfLine1.setVisibility(View.VISIBLE);
				tjfLine2.setVisibility(View.VISIBLE);
			}
		}else{
			vipPromptText.setVisibility(View.VISIBLE);
			vipPromptText.setText("*提示：\n* 元立方金服拥有本产品的最终解释权。");
		}
		if(productInfo != null){
			if("未满标".equals(productInfo.getMoney_status())){
				if(SettingsManager.checkActiveStatusBySysTime(productInfo.getAdd_time(),SettingsManager.yyyJIAXIStartTime,
						SettingsManager.yyyJIAXIEndTime) == 0 && "元年鑫".equals(productInfo.getBorrow_type())&& Constants.UserType.USER_COMPANY.
						equals(SettingsManager.getUserType(ProductInfoActivity.this))){
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
						int[] screen = SettingsManager.getScreenDispaly(ProductInfoActivity.this);
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
			boolean isLogin = !SettingsManager.getLoginPassword(ProductInfoActivity.this).isEmpty()
					&& !SettingsManager.getUser(ProductInfoActivity.this).isEmpty();
			// isLogin = true;// 测试
			Intent intent = new Intent();
			// 1、检测是否已经登录
			if (isLogin) {
				//判断是否实名绑卡
				if("新手标".equals(productInfo.getBorrow_type())){
					isCanbuyXSB(SettingsManager.getUserId(getApplicationContext()), productInfo.getId());
				}else if(BorrowType.VIP.equals(productInfo.getBorrow_type())){
					checkIsVip();
				}else if(BorrowType.SUYING.equals(productInfo.getBorrow_type()) || BorrowType.BAOYING.equals(productInfo.getBorrow_type()) ||
						BorrowType.WENYING.equals(productInfo.getBorrow_type()) || BorrowType.YUANNIANXIN.equals(productInfo.getBorrow_type())){
					checkIsVerify("政信贷投资");
				}else{
					//私人尊享
					if(productInfo.getBorrow_name().contains("私人尊享")){
						checkIsVerify("私人尊享");
					}
				}
			} else {
				// 未登录，跳转到登录页面
				intent.setClass(ProductInfoActivity.this,LoginActivity.class);
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
					intent.setClass(ProductInfoActivity.this,UserVerifyActivity.class);
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
					intent.setClass(ProductInfoActivity.this, BindCardActivity.class);
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
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		RequestApis.requestIsVerify(ProductInfoActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(mLoadingDialog != null && mLoadingDialog.isShowing()){
					mLoadingDialog.dismiss();
				}
				Intent intent = new Intent();
				if(flag){
					//用户已经实名，在这个页面只判断是否实名即可。不判断有没有绑卡
					if("新手标投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoActivity.this, BidXSBActivity.class);
					}else if("政信贷投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoActivity.this, BidZXDActivity.class);
					}else if("VIP投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoActivity.this, BidVIPActivity.class);
					}else if("私人尊享".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoActivity.this, BidSRZXActivity.class);
					}
					investBtn.setEnabled(true);
					startActivity(intent);
					finish();
				}else{
					//用户没有实名
					showMsgDialog(ProductInfoActivity.this, "实名认证", "请先实名认证！");
				}
			}
			@Override
			public void isSetWithdrawPwd(boolean flag, Object object) {
			}
		});
	}
	
	/**
	 * 判断用户是否为vip用户
	 */
	private void checkIsVip(){
		RequestApis.requestIsVip(ProductInfoActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVipUserListener() {
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
				Intent intent = new Intent(ProductInfoActivity.this,VIPProductCJWTActivity.class);
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
	 * 判断用户是否已经绑卡
	 * @param type "充值提现"
	 */
	private void checkIsBindCard(final String type){
		RequestApis.requestIsBinding(ProductInfoActivity.this, SettingsManager.getUserId(getApplicationContext()), "宝付", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//用户已经绑卡
					if("新手标投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoActivity.this, BidXSBActivity.class);
					}else if("政信贷投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoActivity.this, BidZXDActivity.class);
					}else if("VIP投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(ProductInfoActivity.this, BidVIPActivity.class);
					}
					startActivity(intent);
					investBtn.setEnabled(true);
					finish();
				}else{
					//用户还没有绑卡
					showMsgDialog(ProductInfoActivity.this, "绑卡", "因我司变更支付渠道，请您重新绑卡！");
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
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		AsyncAsscociatedCompany task = new AsyncAsscociatedCompany(ProductInfoActivity.this, loanId, recommendId, guaranteeId, 
				new OnCommonInter(){
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
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
		AsyncXSBIscanbuy task = new AsyncXSBIscanbuy(ProductInfoActivity.this, userId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								//用户可以购买新手标
								Intent intent = new Intent();
								intent.putExtra("PRODUCT_INFO", productInfo);
								intent.setClass(ProductInfoActivity.this, BidXSBActivity.class);
								startActivity(intent);
							}else if(resultCode == 1001){
								//请先进行实名
								showMsgDialog(ProductInfoActivity.this, "实名认证", "请先实名认证！");
							}else if(resultCode == 1002){
								//请先进行绑卡
								boolean isNewUser = SettingsManager.checkIsNewUser(SettingsManager.getUserRegTime(getApplicationContext()));
								if(isNewUser){
									showMsgDialog(ProductInfoActivity.this, "绑卡", "请您先绑卡！");
								}else{
									showMsgDialog(ProductInfoActivity.this, "绑卡", "因我司变更支付渠道，请您重新绑卡！");
								}
							}else{
								showMsgDialog(ProductInfoActivity.this, "不能购买新手标", "此产品限首次购买用户专享！");
							}
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
