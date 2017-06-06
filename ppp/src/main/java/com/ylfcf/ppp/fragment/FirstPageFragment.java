package com.ylfcf.ppp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncArticleList;
import com.ylfcf.ppp.async.AsyncBanner;
import com.ylfcf.ppp.common.FileUtil;
import com.ylfcf.ppp.entity.ArticleInfo;
import com.ylfcf.ppp.entity.BannerInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.YXBProductInfo;
import com.ylfcf.ppp.entity.YXBProductLogInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.inter.Inter.OnCommonInter;
import com.ylfcf.ppp.parse.JsonParseBanner;
import com.ylfcf.ppp.ui.ActivitysRegionActivity;
import com.ylfcf.ppp.ui.ArticleListActivity;
import com.ylfcf.ppp.ui.BannerDetailsActivity;
import com.ylfcf.ppp.ui.BannerTopicActivity;
import com.ylfcf.ppp.ui.BorrowDetailXSBActivity;
import com.ylfcf.ppp.ui.BorrowDetailYYYActivity;
import com.ylfcf.ppp.ui.BorrowListVIPActivity;
import com.ylfcf.ppp.ui.BorrowListZXDActivity;
import com.ylfcf.ppp.ui.InvitateActivity;
import com.ylfcf.ppp.ui.LXFXTempActivity;
import com.ylfcf.ppp.ui.LXJ5TempActivity;
import com.ylfcf.ppp.ui.MainFragmentActivity;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnFirstPageZXDOnClickListener;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnNetStatusChangeListener;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnRequestBorrowListListener;
import com.ylfcf.ppp.ui.MainFragmentActivity.OnYXBDataListener;
import com.ylfcf.ppp.ui.PrizeRegion2TempActivity;
import com.ylfcf.ppp.ui.PrizeRegionTempActivity;
import com.ylfcf.ppp.ui.SRZXAppointActivity;
import com.ylfcf.ppp.ui.SignTopicTempActivity;
import com.ylfcf.ppp.ui.UserVerifyActivity;
import com.ylfcf.ppp.ui.YQHYTempActivity;
import com.ylfcf.ppp.util.Constants.ActivityCode;
import com.ylfcf.ppp.util.Constants.ArticleType;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.widget.CycleViewPager;
import com.ylfcf.ppp.widget.CycleViewPager.ImageCycleViewListener;
import com.ylfcf.ppp.widget.ImageViewFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 * 
 * @author Administrator
 * 
 */
public class FirstPageFragment extends BaseFragment implements OnClickListener {
	private static final String className = "FirstPageFragment";

	private static final int REQUEST_ARTICLELIST_WHAT = 5701;// 公告
	private static final int REQUEST_ARTICLELIST_SUCCESS = 5702;
	private static final int REFRESH_NOTICE = 5703;
	
	private MainFragmentActivity mainActivity;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	private List<ImageView> views = new ArrayList<ImageView>();
	private List<BannerInfo> bannerList = new ArrayList<BannerInfo>();
	private CycleViewPager cycleViewPager;// banner
	private ViewPager subjectViewPager;// 系列标的viewpager
	private Button hytjBtn,hdzqBtn;//壕友推荐 活动专区按钮

	/*
	 * 新手标
	 */
	private View xsbLayout;
	private RelativeLayout xsbMainLayout;
	private Button xsbInvestBtn;//新手标的立即投资按钮
	
	private LinearLayout noticeLayout;// 公告的布局
	private TextView noticeTitle, noticeTime;
	private List<View> viewsList = new ArrayList<View>();

	private ImageView defaultImg;
	private View rootView;

	private int page = 0;
	private int pageSize = 20;
	private boolean isFirst = true;// 是否首次进入主页面

	private List<ArticleInfo> articleList;
	private ProductInfo xsbInfo;
	private FragmentManager fragmentManager = null;

	private Handler hanlder = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_ARTICLELIST_WHAT:
				requestNoticeList("正常", ArticleType.NOTICE);
				break;
			case REQUEST_ARTICLELIST_SUCCESS:
				BaseInfo baseInfo = (BaseInfo) msg.obj;
				if (baseInfo != null) {
					articleList = baseInfo.getmArticlePageInfo()
							.getArticleList();
					initNoticeData();
				}
				break;
			case REFRESH_NOTICE:
				initNoticeData();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 创建当前Fragment的实例对象
	 * 
	 * @param position
	 * @return
	 */
	private static OnFirstPageZXDOnClickListener firstPageZXDListener;
	private static MainFragmentActivity.OnFirstPageHYTJOnClickListener hytjOnClickListener;
	public static Fragment newInstance(int position, OnFirstPageZXDOnClickListener listener, MainFragmentActivity.OnFirstPageHYTJOnClickListener hytjListener) {
		FirstPageFragment f = new FirstPageFragment();
		Bundle args = new Bundle();
		args.putInt("num", position);
		f.setArguments(args);
		firstPageZXDListener = listener;
		hytjOnClickListener = hytjListener;
		return f;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mainActivity = (MainFragmentActivity) getActivity();
		fragmentManager = getActivity().getSupportFragmentManager();
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.first_page_fragment, null);
			findViews(rootView, inflater);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		requestBanner("正常", "");
		hanlder.sendEmptyMessage(REQUEST_ARTICLELIST_WHAT);
		return rootView;
	}

	int mm = 0;
	private void findViews(View view, LayoutInflater inflater) {
		xsbLayout = inflater.inflate(R.layout.first_page_subject_xsb, null);
		viewsList.add(xsbLayout);
		
		xsbMainLayout = (RelativeLayout)xsbLayout.findViewById(R.id.first_page_subject_xsb_mainlayout);
		xsbMainLayout.setOnClickListener(this);
		xsbInvestBtn = (Button)xsbLayout.findViewById(R.id.first_page_subject_xsb_bidBtn);
		xsbInvestBtn.setOnClickListener(this);
		hytjBtn = (Button)view.findViewById(R.id.first_page_fragment_hytj_btn);
		hytjBtn.setOnClickListener(this);
		hdzqBtn = (Button)view.findViewById(R.id.first_page_fragment_hdzq_btn);
		hdzqBtn.setOnClickListener(this);

		defaultImg = (ImageView) view
				.findViewById(R.id.first_page_fragment_default_img);
		noticeLayout = (LinearLayout) view
				.findViewById(R.id.first_page_fragment_notice_layout);
		noticeLayout.setOnClickListener(this);
		noticeTitle = (TextView) view
				.findViewById(R.id.first_page_fragment_notice_text);
		noticeTime = (TextView) view
				.findViewById(R.id.first_page_fragment_notice_time);
//		cycleViewPager = new CycleViewPager();
//		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//		fragmentTransaction.add(R.id.first_page_fragment_cycleviewpager_layout, cycleViewPager);
		cycleViewPager = (CycleViewPager) getActivity().getFragmentManager()
				.findFragmentById(R.id.main_fragment1_banner_layout);
		// cycleViewPager.setScrollable(false);//禁止滑动
		subjectViewPager = (ViewPager) view
				.findViewById(R.id.first_page_fragment_viewpager);
		subjectViewPager.setAdapter(new SubjectPagerAdapter(viewsList));
		subjectViewPager.setCurrentItem(0);

		mainActivity.setOnRequestBorrowListener(
				new OnRequestBorrowListListener() {
					@Override
					public void back(BaseInfo baseInfo) {
					}
				}, null);

		mainActivity.setOnYXBDataListener(new OnYXBDataListener() {
			@Override
			public void back(YXBProductInfo mYXBProductInfo,
					YXBProductLogInfo mYXBProductLogInfo) {
			}
		}, null);
		mainActivity.setOnNetStatusChangeListener(new OnNetStatusChangeListener() {
			@Override
			public void onNetStatusChange(boolean enabled) {
				if(enabled){
					requestBanner("正常", "");
					hanlder.sendEmptyMessage(REQUEST_ARTICLELIST_WHAT);
				}
			}
		},null);
		initHYTJBtn();
	}

	private void initHYTJBtn(){
		if(SettingsManager.isCompanyUser(mainActivity)){
			//企业用户
			hytjBtn.setEnabled(false);
			hytjBtn.setBackgroundResource(R.drawable.first_page_fragment_hytj_unenable);
		}else{
			hytjBtn.setEnabled(true);
			hytjBtn.setBackgroundResource(R.drawable.first_page_fragment_hytj_enable);
		}
	}

	/**
	 * 初始化公告栏
	 * 
	 * @param
	 */
	private void initNoticeData() {
		if(articleList == null || articleList.size() < 1){
			return;
		}
		ArticleInfo info = articleList.get(0);
		noticeTitle.setText(info.getTitle());
		noticeTime.setText(info.getAdd_time().split(" ")[0].replaceAll("-", "/"));
	}
	
	/**
	 * 初始化banner数据
	 * @param list
	 */
	private void initBannerData(List<BannerInfo> list) {
		views.clear();
		// 将最后一个ImageView添加进来
		views.add(ImageViewFactory.getImageView(getActivity(),
				list.get(list.size() - 1).getPic_url()));
		for (int i = 0; i < list.size(); i++) {
			views.add(ImageViewFactory.getImageView(getActivity(), list.get(i).getPic_url()));
		}
		// 将第一个ImageView添加进来
		views.add(ImageViewFactory.getImageView(getActivity(), list.get(0)
				.getPic_url()));

		// 设置循环，在调用setData方法前调用
		cycleViewPager.setCycle(true);
		// 在加载数据前设置是否循环
		cycleViewPager.setData(views, list, mAdCycleViewListener);
		// 设置轮播
		cycleViewPager.setWheel(true);

		// 设置轮播时间，默认5000ms
		cycleViewPager.setTime(2000);
		// 设置圆点指示图标组居中显示，默认靠右
		cycleViewPager.setIndicatorCenter();
	}

	/**
	 * banner的item的点击事件
	 */
	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
		@Override
		public void onImageClick(BannerInfo info, int position, View imageView) {
			if (cycleViewPager.isCycle()) {
				position = position - 1;
				Intent intent = null;
				if ("文章".equals(info.getType())) {
					if("".equals(info.getArticle_id()) || "0".equals(info.getArticle_id())){
						
					}else{
						intent = new Intent(mainActivity,
								BannerDetailsActivity.class);
						intent.putExtra("BannerInfo", info);
						startActivity(intent);
					}
				} else if ("专题页".equals(info.getType())) {
					intent = new Intent(mainActivity, BannerTopicActivity.class);
					intent.putExtra("BannerInfo", info);
					if (info.getArticle_id() != null
							&& !"".equals(info.getArticle_id())) {
						startActivity(intent);
					}
				}else if("窗口页面".equals(info.getType())){
					if(ActivityCode.YYY_DETAILS_ACTIVITY.equals(info.getArticle_id())){
						//元月盈详情页面
						intent = new Intent(getActivity(),BorrowDetailYYYActivity.class);
						startActivity(intent);
					}else if(ActivityCode.XSB_DETAILS_ACTIVITY.equals(info.getArticle_id())){
						//新手标详情页面
						intent = new Intent(getActivity(),BorrowDetailXSBActivity.class);
						startActivity(intent);
					}else if(ActivityCode.DQLC_LIST_ACTIVITY.equals(info.getArticle_id())){
						//定期理财产品列表页面
						intent = new Intent(getActivity(),BorrowListZXDActivity.class);
						startActivity(intent);
					}else if(ActivityCode.VIP_LIST_ACTIVITY.equals(info.getArticle_id())){
						//VIP产品列表页面
						intent = new Intent(getActivity(),BorrowListVIPActivity.class);
						startActivity(intent);
					}else if(ActivityCode.SRZX_APPOINT_ACTIVITY.equals(info.getArticle_id())){
						//私人尊享预约页面
						intent = new Intent(getActivity(),SRZXAppointActivity.class);
						startActivity(intent);
					}else if(ActivityCode.FLJH_ACTIVITY.equals(info.getArticle_id())){
						//会员福利计划
						intent = new Intent(getActivity(),PrizeRegionTempActivity.class);
						startActivity(intent);
					}else if(ActivityCode.LXFX_ACTIVITY.equals(info.getArticle_id())){
						//乐享返现 开年红
						intent = new Intent(getActivity(),LXFXTempActivity.class);
						startActivity(intent);
					}else if(ActivityCode.SIGN_ACTIVITY.equals(info.getArticle_id())){
						intent = new Intent(getActivity(),SignTopicTempActivity.class);
						startActivity(intent);
					}else if(ActivityCode.FLJH_ACTIVITY_02.equals(info.getArticle_id())){
						intent = new Intent(getActivity(),PrizeRegion2TempActivity.class);
						startActivity(intent);
					}else if(ActivityCode.YQHY_ACTIVITY.equals(info.getArticle_id())){
						intent = new Intent(getActivity(),YQHYTempActivity.class);
						startActivity(intent);
					}else if(ActivityCode.QXJ5_ACTIVITY.equals(info.getArticle_id())){
						intent = new Intent(getActivity(),LXJ5TempActivity.class);
						startActivity(intent);
					}
				}
			}
		}
	};

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		YLFLogger.d("FirstPageFragment -- setUserVisibleHint ---"
				+ isVisibleToUser);
		if(mainActivity != null && isVisibleToUser){
			initHYTJBtn();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		UMengStatistics.statisticsOnPageStart(className);
		YLFLogger.d("FirstPageFragment -- onResume() ---");
	}

	@Override
	public void onPause() {
		super.onPause();
		UMengStatistics.statisticsOnPageEnd(className);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hanlder.removeCallbacksAndMessages(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.first_page_fragment_notice_layout:
			Intent intentArt = new Intent(mainActivity,
					ArticleListActivity.class);
			startActivity(intentArt);
			break;
		case R.id.first_page_subject_xsb_bidBtn:
		case R.id.first_page_subject_xsb_mainlayout:
			Intent intentXSB = new Intent(mainActivity,
					BorrowDetailXSBActivity.class);
			intentXSB.putExtra("PRODUCT_INFO", xsbInfo);
			startActivity(intentXSB);
			break;
		case R.id.first_page_fragment_hytj_btn:
			//壕友推荐
			shared();
			break;
		case R.id.first_page_fragment_hdzq_btn:
			//活动专区
			Intent intentHDZQ = new Intent(mainActivity, ActivitysRegionActivity.class);
			mainActivity.startActivity(intentHDZQ);
			break;
		default:
			break;
		}
	}

	private void shared(){
		String userId = SettingsManager.getUserId(mainActivity.getApplicationContext());
		if(userId != null && !"".equals(userId)){
			//已登录
			hytjBtn.setEnabled(false);
			checkIsVerify("邀请有奖");
		}else{
			//未登录
			hytjOnClickListener.hytjOnClick();
		}
	}

	/**
	 * 验证用户是否已经认证
	 * @param type “充值”,“提现”,"邀请有奖"
	 */
	private void checkIsVerify(final String type){
		if(mainActivity.loadingDialog != null){
			mainActivity.loadingDialog.show();
		}
		RequestApis.requestIsVerify(mainActivity, SettingsManager.getUserId(mainActivity.getApplicationContext()), new Inter.OnIsVerifyListener() {
			@Override
			public void isVerify(boolean flag, Object object) {
				if(mainActivity.loadingDialog != null && mainActivity.loadingDialog.isShowing()){
					mainActivity.loadingDialog.dismiss();
				}
				if("邀请有奖".equals(type)){
					hytjBtn.setEnabled(true);
					Intent yqyjIntent = new Intent(mainActivity,InvitateActivity.class);
					yqyjIntent.putExtra("is_verify", flag);
					startActivity(yqyjIntent);
					return;
				}
				if(flag){
					//用户已经实名
//					checkIsBindCard(type);
				}else{
					//用户没有实名
					Intent intent = new Intent(mainActivity,UserVerifyActivity.class);
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

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	/**
	 * Viewpager的适配器
	 * @author Mr.liu
	 */
	class SubjectPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public SubjectPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;// 构造方法，参数是我们的页卡，这样比较方便。
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));// 删除页卡
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
			container.addView(mListViews.get(position), 0);// 添加页卡
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();// 返回页卡的数量
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// 官方提示这样写
		}
	}

	/**
	 * 请求banner数据
	 * @param status
	 */
	private void requestBanner(String status, String type) {
		String result = null;
		BaseInfo baseInfo = null;
		long nowTime = System.currentTimeMillis();
		long cacheTime = SettingsManager.getBannerCacheTime(mainActivity);
		try {
			byte[] initJsonB = FileUtil.readByte(mainActivity,
					FileUtil.YLFCF_BANNER_CACHE);
			result = new String(initJsonB);
			// 解析init.json
			if (result != null && !"".equals(result)) {
				baseInfo = JsonParseBanner.parseData(result);
			}
		} catch (Exception exx) {
		}
		// 缓存时间大于6分钟，则重新刷新缓存
		if (baseInfo != null && baseInfo.getmBannerPageInfo() != null && baseInfo.getmBannerPageInfo().getBannerList() != null
				&& nowTime - cacheTime < 0.1 * 3600 * 1000) {
			bannerList.clear();
			int size = baseInfo.getmBannerPageInfo().getBannerList().size();
			for (int i = 0; i < size; i++) {
				BannerInfo banner = baseInfo.getmBannerPageInfo()
						.getBannerList().get(i);
				if (!"手机开机页".equals(banner.getType())) {
					bannerList.add(banner);
				}
			}
			initBannerData(bannerList);
			defaultImg.setVisibility(View.GONE);
			return;
		}
		AsyncBanner bannerTask = new AsyncBanner(mainActivity,
				String.valueOf(page), String.valueOf(pageSize), status, type,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								bannerList.clear();
								int size = baseInfo.getmBannerPageInfo()
										.getBannerList().size();
								for (int i = 0; i < size; i++) {
									BannerInfo banner = baseInfo
											.getmBannerPageInfo()
											.getBannerList().get(i);
									if (!"手机开机页".equals(banner.getType())) {
										bannerList.add(banner);
									}
								}
								initBannerData(bannerList);
								defaultImg.setVisibility(View.GONE);
							}
						}
					}
				});
		bannerTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}

	/**
	 * 公告列表 ---- 取第一条最新的数据
	 * 
	 * @param status
	 * @param type
	 */
	private void requestNoticeList(String status, String type) {
		AsyncArticleList articleTask = new AsyncArticleList(mainActivity,
				String.valueOf(0), String.valueOf(1), status, type,
				new OnCommonInter() {
					@Override
					public void back(BaseInfo baseInfo) {
						if (baseInfo != null) {
							int resultCode = SettingsManager
									.getResultCode(baseInfo);
							if (resultCode == 0) {
								Message msg = hanlder
										.obtainMessage(REQUEST_ARTICLELIST_SUCCESS);
								msg.obj = baseInfo;
								hanlder.sendMessage(msg);
							}
						}
					}
				});
		articleTask.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
	}
}
