package com.ylfcf.ppp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.adapter.ProductDataAdapter;
import com.ylfcf.ppp.async.AsyncYYYCurrentUserInvest;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectCailiaoInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.inter.Inter.OnIsBindingListener;
import com.ylfcf.ppp.inter.Inter.OnIsVerifyListener;
import com.ylfcf.ppp.util.ImageLoaderManager;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.widget.GridViewWithHeaderAndFooter;

import java.util.ArrayList;

/**
 * 元月盈的担保材料
 * @author Mr.liu
 *
 */
public class YYYProductDataActivity extends BaseActivity implements OnClickListener{
	private static final int REQUEST_CURRENT_USER_INVEST_WHAT = 7424;
	private static final int REQUEST_CURRENT_USER_INVEST_SUCCESS = 7425;
	private static final int REQUEST_CURRENT_USER_INVEST_NODATA = 7426;
	
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	
	private GridViewWithHeaderAndFooter dataGridView;
	private Button investBtn;
	private ProductDataAdapter adapter;
	
	private ProjectInfo projectInfo;
	private ProductInfo productInfo;
	private LayoutInflater layoutInflater = null;
	private View bottomView;
	private ArrayList<ProjectCailiaoInfo> noMarksCailiaoList = new ArrayList<ProjectCailiaoInfo>();
	private ArrayList<ProjectCailiaoInfo> marksCailiaoList = new ArrayList<ProjectCailiaoInfo>();
	
	private boolean isInvested = false;//用户是否投标过
	private AlertDialog.Builder builder = null; // 先得到构造器
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_CURRENT_USER_INVEST_WHAT:
				requestYYYCurrentUserInvest(SettingsManager.getUserId(getApplicationContext()), productInfo.getId());
				break;
			case REQUEST_CURRENT_USER_INVEST_SUCCESS:
				break;
			case REQUEST_CURRENT_USER_INVEST_NODATA:
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
		setContentView(R.layout.product_data_activity);
		
		builder = new AlertDialog.Builder(YYYProductDataActivity.this,
				R.style.Dialog_Transparent); // 先得到构造器
		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		if(bundle != null){
			projectInfo = (ProjectInfo) bundle.getSerializable("PROJECT_INFO");
			productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
		}
		findViews();
		if(productInfo != null){
			handler.sendEmptyMessage(REQUEST_CURRENT_USER_INVEST_WHAT);
		}
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("担保材料");
		bottomView = layoutInflater.inflate(R.layout.bottom_button_invest_layout, null);
		investBtn = (Button) bottomView.findViewById(R.id.product_data_activity_bidBtn);
		investBtn.setOnClickListener(this);
		dataGridView = (GridViewWithHeaderAndFooter)findViewById(R.id.product_data_gv);
		dataGridView.addFooterView(bottomView);
		dataGridView.setVisibility(View.VISIBLE);
		if(productInfo != null){
			if("未满标".equals(productInfo.getMoney_status())){
				investBtn.setEnabled(true);
				investBtn.setText("立即投资");
			}else{
				investBtn.setEnabled(false);
				investBtn.setText("投资结束");
			}
		}
		
		dataGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(YYYProductDataActivity.this,ProductDataDetailsActivity.class);
				if(isInvested){
					intent.putExtra("cailiao_list",noMarksCailiaoList);
				}else{
					intent.putExtra("cailiao_list",marksCailiaoList);
				}
				intent.putExtra("position", position);
				startActivity(intent);
			}
		});
		initAdapter();
	}
	
	private void initAdapter(){
		adapter = new ProductDataAdapter(YYYProductDataActivity.this,layoutInflater);
		dataGridView.setAdapter(adapter);
	}
	
	private void initImgData(){
		noMarksCailiaoList.clear();
		marksCailiaoList.clear();
		if(projectInfo != null){
			noMarksCailiaoList.addAll(projectInfo.getCailiaoNoMarkList());
			marksCailiaoList.addAll(projectInfo.getCailiaoMarkList());
		}
		if(isInvested){
			adapter.setItems(noMarksCailiaoList);
		}else{
			adapter.setItems(marksCailiaoList);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ImageLoaderManager.clearMemoryCache();
		handler.removeCallbacksAndMessages(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_topbar_left_layout:
			finish();
			break;
		case R.id.product_data_activity_bidBtn:
			// 从SettingsManager中读取密码，如果为空意味着没有登录。
			boolean isLogin = !SettingsManager.getLoginPassword(YYYProductDataActivity.this).isEmpty()
					&& !SettingsManager.getUser(YYYProductDataActivity.this).isEmpty();
			// isLogin = true;// 测试
			Intent intent = new Intent();
			// 1、检测是否已经登录
			if (isLogin) {
				// 已经登录，跳转到购买页面
//				intent.putExtra("PRODUCT_INFO", productInfo);
				checkIsVerify("元月盈投资");
			} else {
				// 未登录，跳转到登录页面
				intent.setClass(YYYProductDataActivity.this,LoginActivity.class);
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
					intent.setClass(YYYProductDataActivity.this,UserVerifyActivity.class);
					Bundle bundle = new Bundle();
					if("新手标".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "新手标投资");
					}else if("vip".equals(productInfo.getBorrow_type())){
						bundle.putString("type", "VIP投资");
					}else{
						bundle.putString("type", "政信贷投资");
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
					}else{
						bundle.putString("type", "政信贷投资");
					}
					bundle.putSerializable("PRODUCT_INFO", productInfo);
					intent.putExtra("bundle", bundle);
					intent.setClass(YYYProductDataActivity.this, BindCardActivity.class);
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
		RequestApis.requestIsVerify(YYYProductDataActivity.this, SettingsManager.getUserId(getApplicationContext()), new OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//用户已经实名,此处只判断有没有实名即可，不再判断有没有绑卡
//					checkIsBindCard(type);
					if("新手标投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(YYYProductDataActivity.this, BidXSBActivity.class);
					}else if("VIP投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(YYYProductDataActivity.this, BidVIPActivity.class);
					}else if("政信贷投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(YYYProductDataActivity.this, BidZXDActivity.class);
					}else if("元月盈投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(YYYProductDataActivity.this, BidYYYActivity.class);
					}
					startActivity(intent);
					investBtn.setEnabled(true);
					finish();
				}else{
					//用户没有实名
					showMsgDialog(YYYProductDataActivity.this, "实名认证", "请先实名认证！");
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
		RequestApis.requestIsBinding(YYYProductDataActivity.this, SettingsManager.getUserId(getApplicationContext()), "宝付", new OnIsBindingListener() {
			@Override
			public void isBinding(boolean flag, Object object) {
				Intent intent = new Intent();
				if(flag){
					//用户已经绑卡
					if("新手标投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(YYYProductDataActivity.this, BidXSBActivity.class);
					}else if("VIP投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(YYYProductDataActivity.this, BidVIPActivity.class);
					}else if("政信贷投资".equals(type)){
						intent.putExtra("PRODUCT_INFO", productInfo);
						intent.setClass(YYYProductDataActivity.this, BidZXDActivity.class);
					}
					startActivity(intent);
					investBtn.setEnabled(true);
					finish();
				}else{
					//用户还没有绑卡
					showMsgDialog(YYYProductDataActivity.this, "绑卡", "因我司变更支付渠道，请您重新绑卡！");
				}
			}
		});
	}
	
	/**
	 * 判断当前用户是否投资过VIP该标的
	 * @param investUserId
	 * @param borrowId
	 */
	private void requestYYYCurrentUserInvest(String investUserId,String borrowId){
		if(mLoadingDialog != null){
			mLoadingDialog.show();
		}
		AsyncYYYCurrentUserInvest task = new AsyncYYYCurrentUserInvest(YYYProductDataActivity.this, investUserId, borrowId, 
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if(mLoadingDialog != null && mLoadingDialog.isShowing()){
							mLoadingDialog.dismiss();
						}
						if(baseInfo != null){
							int resultCode = SettingsManager.getResultCode(baseInfo);
							if(resultCode == 0){
								isInvested = true;
							}else{
								isInvested = false;
							}
							initImgData();
						}else{
							isInvested = false;
						}
					}
				});
		task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
