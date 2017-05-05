package com.ylfcf.ppp.fragment;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.ui.ArticleListActivity;
import com.ylfcf.ppp.ui.CommQuesActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * 常见问题
 * 
 * @author Administrator
 * 
 */
public class CommQuesFragment extends BaseFragment implements OnClickListener{
	private CommQuesActivity commQuesActivity;
	private View rootView;
	private int position;
	private ScrollView mainAccountLayout;
	private ScrollView mainInterestLayout;
	private ScrollView mainRechargeLayout;
	
	//账户和密码
	private LinearLayout accountMain1tLayout,accountMain2tLayout,accountMain3tLayout,accountMain4tLayout,accountMain5tLayout;
	private LinearLayout accountDetail1tLayout,accountDetail2tLayout,accountDetail3tLayout,accountDetail4tLayout,accountDetail5tLayout;
	private ImageView accountArrow1,accountArrow2,accountArrow3,accountArrow4,accountArrow5;
	
	//收益和利率
	private LinearLayout interestMain1tLayout,interestMain2tLayout,interestMain3tLayout,interestMain4tLayout,interestMain5tLayout,
				interestMain6tLayout,interestMain7tLayout,interestMain8tLayout;
	private LinearLayout interestDetail1tLayout,interestDetail2tLayout,interestDetail3tLayout,interestDetail4tLayout,interestDetail5tLayout,
				interestDetail6tLayout,interestDetail7tLayout,interestDetail8tLayout;
	private ImageView interestArrow1,interestArrow2,interestArrow3,interestArrow4,
					interestArrow5,interestArrow6,interestArrow7,interestArrow8;

	//充值和提现
	private LinearLayout rechargeMain1tLayout,rechargeMain2tLayout,rechargeMain3tLayout,rechargeMain4tLayout,rechargeMain5tLayout,
				rechargeMain6tLayout,rechargeMain7tLayout,rechargeMain8tLayout,rechargeMain9tLayout,rechargeMain10tLayout,rechargeMain11tLayout,rechargeMain12tLayout;
	private LinearLayout rechargeDetail1tLayout,rechargeDetail2tLayout,rechargeDetail3tLayout,rechargeDetail4tLayout,rechargeDetail5tLayout,
				rechargeDetail6tLayout,rechargeDetail7tLayout,rechargeDetail8tLayout,rechargeDetail9tLayout,rechargeDetail10tLayout,rechargeDetail11tLayout,
				rechargeDetail12tLayout;
	private ImageView rechargeArrow1,rechargeArrow2,rechargeArrow3,rechargeArrow4,rechargeArrow5,rechargeArrow6,
				rechargeArrow7,rechargeArrow8,rechargeArrow9,rechargeArrow10,rechargeArrow11,rechargeArrow12;
	
	public CommQuesFragment(int position) {
		this.position = position;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		commQuesActivity = (CommQuesActivity) getActivity();
		if (rootView == null) {
			if (position == 0) {
				rootView = inflater.inflate(R.layout.comm_ques_account_safe_fragment, null);
				findAccountSafeViews(rootView);
			} else if (position == 1) {
				rootView = inflater.inflate(R.layout.comm_ques_interest_fragment, null);
				findInterestViews(rootView);
			} else if (position == 2) {
				rootView = inflater.inflate(R.layout.comm_ques_recharge_fragment, null);
				findRechargeViews(rootView);
			}
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	private void findAccountSafeViews(View view) {
		mainAccountLayout = (ScrollView) view.findViewById(R.id.comm_ques_account_safe_mainlayout);
		
		accountMain1tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_account_safe_1t_main_layout);
		accountMain1tLayout.setOnClickListener(this);
		accountMain2tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_account_safe_2t_main_layout);
		accountMain2tLayout.setOnClickListener(this);
		accountMain3tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_account_safe_3t_main_layout);
		accountMain3tLayout.setOnClickListener(this);
		accountMain4tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_account_safe_4t_main_layout);
		accountMain4tLayout.setOnClickListener(this);
		accountMain5tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_account_safe_5t_main_layout);
		accountMain5tLayout.setOnClickListener(this);
		
		accountDetail1tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_account_safe_1t_layout);
		accountDetail2tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_account_safe_2t_layout);
		accountDetail3tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_account_safe_3t_layout);
		accountDetail4tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_account_safe_4t_layout);
		accountDetail5tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_account_safe_5t_layout);
		
		accountArrow1 = (ImageView) view.findViewById(R.id.comm_ques_account_safe_fragment_arrow1);
		accountArrow2 = (ImageView) view.findViewById(R.id.comm_ques_account_safe_fragment_arrow2);
		accountArrow3 = (ImageView) view.findViewById(R.id.comm_ques_account_safe_fragment_arrow3);
		accountArrow4 = (ImageView) view.findViewById(R.id.comm_ques_account_safe_fragment_arrow4);
		accountArrow5 = (ImageView) view.findViewById(R.id.comm_ques_account_safe_fragment_arrow5);
	}

	private void findInterestViews(View view) {
		mainInterestLayout = (ScrollView) view.findViewById(R.id.comm_ques_interest_mainlayout);
		
		interestMain1tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_interest_1t_main_layout);
		interestMain1tLayout.setOnClickListener(this);
		interestMain2tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_interest_2t_main_layout);
		interestMain2tLayout.setOnClickListener(this);
		interestMain3tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_interest_3t_main_layout);
		interestMain3tLayout.setOnClickListener(this);
		interestMain4tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_interest_4t_main_layout);
		interestMain4tLayout.setOnClickListener(this);
		interestMain5tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_interest_5t_main_layout);
		interestMain5tLayout.setOnClickListener(this);
		interestMain6tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_interest_6t_main_layout);
		interestMain6tLayout.setOnClickListener(this);
		interestMain7tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_interest_7t_main_layout);
		interestMain7tLayout.setOnClickListener(this);
		interestMain8tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_interest_8t_main_layout);
		interestMain8tLayout.setOnClickListener(this);
		
		interestDetail1tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_interest_1t_layout);
		interestDetail2tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_interest_2t_layout);
		interestDetail3tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_interest_3t_layout);
		interestDetail4tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_interest_4t_layout);
		interestDetail5tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_interest_5t_layout);
		interestDetail6tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_interest_6t_layout);
		interestDetail7tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_interest_7t_layout);
		interestDetail8tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_interest_8t_layout);
		
		interestArrow1 = (ImageView) view.findViewById(R.id.comm_ques_interest_arrow1);
		interestArrow2 = (ImageView) view.findViewById(R.id.comm_ques_interest_arrow2);
		interestArrow3 = (ImageView) view.findViewById(R.id.comm_ques_interest_arrow3);
		interestArrow4 = (ImageView) view.findViewById(R.id.comm_ques_interest_arrow4);
		interestArrow5 = (ImageView) view.findViewById(R.id.comm_ques_interest_arrow5);
		interestArrow6 = (ImageView) view.findViewById(R.id.comm_ques_interest_arrow6);
		interestArrow7 = (ImageView) view.findViewById(R.id.comm_ques_interest_arrow7);
		interestArrow8 = (ImageView) view.findViewById(R.id.comm_ques_interest_arrow8);
		
	}

	private void findRechargeViews(View view) {
		mainRechargeLayout = (ScrollView) view.findViewById(R.id.comm_ques_recharge_mainlayout);
		
		rechargeMain1tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_1t_main_layout);
		rechargeMain1tLayout.setOnClickListener(this);
		rechargeMain2tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_2t_main_layout);
		rechargeMain2tLayout.setOnClickListener(this);
		rechargeMain3tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_3t_main_layout);
		rechargeMain3tLayout.setOnClickListener(this);
		rechargeMain4tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_4t_main_layout);
		rechargeMain4tLayout.setOnClickListener(this);
		rechargeMain5tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_5t_main_layout);
		rechargeMain5tLayout.setOnClickListener(this);
		rechargeMain6tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_6t_main_layout);
		rechargeMain6tLayout.setOnClickListener(this);
		rechargeMain7tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_7t_main_layout);
		rechargeMain7tLayout.setOnClickListener(this);
		rechargeMain8tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_8t_main_layout);
		rechargeMain8tLayout.setOnClickListener(this);
		rechargeMain9tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_9t_main_layout);
		rechargeMain9tLayout.setOnClickListener(this);
		rechargeMain10tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_10t_main_layout);
		rechargeMain10tLayout.setOnClickListener(this);
		rechargeMain11tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_11t_main_layout);
		rechargeMain11tLayout.setOnClickListener(this);
		rechargeMain12tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_12t_main_layout);
		rechargeMain12tLayout.setOnClickListener(this);
		
		rechargeDetail1tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_1t_layout);
		rechargeDetail2tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_2t_layout);
		rechargeDetail3tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_3t_layout);
		rechargeDetail4tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_4t_layout);
		rechargeDetail5tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_5t_layout);
		rechargeDetail6tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_6t_layout);
		rechargeDetail7tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_7t_layout);
		rechargeDetail8tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_8t_layout);
		rechargeDetail9tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_9t_layout);
		rechargeDetail10tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_10t_layout);
		rechargeDetail11tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_11t_layout);
		rechargeDetail12tLayout = (LinearLayout) view.findViewById(R.id.comm_ques_recharge_12t_layout);
		
		rechargeArrow1 = (ImageView) view.findViewById(R.id.comm_ques_recharge_arrow1);
		rechargeArrow2 = (ImageView) view.findViewById(R.id.comm_ques_recharge_arrow2);
		rechargeArrow3 = (ImageView) view.findViewById(R.id.comm_ques_recharge_arrow3);
		rechargeArrow4 = (ImageView) view.findViewById(R.id.comm_ques_recharge_arrow4);
		rechargeArrow5 = (ImageView) view.findViewById(R.id.comm_ques_recharge_arrow5);
		rechargeArrow6 = (ImageView) view.findViewById(R.id.comm_ques_recharge_arrow6);
		rechargeArrow7 = (ImageView) view.findViewById(R.id.comm_ques_recharge_arrow7);
		rechargeArrow8 = (ImageView) view.findViewById(R.id.comm_ques_recharge_arrow8);
		rechargeArrow9 = (ImageView) view.findViewById(R.id.comm_ques_recharge_arrow9);
		rechargeArrow10 = (ImageView) view.findViewById(R.id.comm_ques_recharge_arrow10);
		rechargeArrow11 = (ImageView) view.findViewById(R.id.comm_ques_recharge_arrow11);
		rechargeArrow12 = (ImageView) view.findViewById(R.id.comm_ques_recharge_arrow12);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comm_ques_account_safe_1t_main_layout:
			if(accountDetail1tLayout.isShown()){
				rotateEnd(accountArrow1);
				accountDetail1tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(accountArrow1);
				accountDetail1tLayout.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.comm_ques_account_safe_2t_main_layout:
			if(accountDetail2tLayout.isShown()){
				rotateEnd(accountArrow2);
				accountDetail2tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(accountArrow2);
				accountDetail2tLayout.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.comm_ques_account_safe_3t_main_layout:
			if(accountDetail3tLayout.isShown()){
				rotateEnd(accountArrow3);
				accountDetail3tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(accountArrow3);
				accountDetail3tLayout.setVisibility(View.VISIBLE);
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						mainAccountLayout.scrollTo(0, mainAccountLayout.getHeight() + accountDetail3tLayout.getHeight());
					}
				});
			}
			break;
		case R.id.comm_ques_account_safe_4t_main_layout:
			if(accountDetail4tLayout.isShown()){
				rotateEnd(accountArrow4);
				accountDetail4tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(accountArrow4);
				accountDetail4tLayout.setVisibility(View.VISIBLE);
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						mainAccountLayout.scrollTo(0, mainAccountLayout.getHeight() + accountDetail4tLayout.getHeight());
					}
				});
			}
			break;
		case R.id.comm_ques_account_safe_5t_main_layout:
			if(accountDetail5tLayout.isShown()){
				rotateEnd(accountArrow5);
				accountDetail5tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(accountArrow5);
				accountDetail5tLayout.setVisibility(View.VISIBLE);
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						mainAccountLayout.scrollTo(0, mainAccountLayout.getHeight() + accountDetail5tLayout.getHeight());
					}
				});
			}
			break;
		case R.id.comm_ques_interest_1t_main_layout:
			if(interestDetail1tLayout.isShown()){
				rotateEnd(interestArrow1);
				interestDetail1tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(interestArrow1);
				interestDetail1tLayout.setVisibility(View.VISIBLE);
			}
//			new Handler().post(new Runnable() {
//				@Override
//				public void run() {
//					mainInterestLayout.scrollTo(0, mainInterestLayout.getHeight() + interestDetail1tLayout.getHeight());
//				}
//			});
			break;
		case R.id.comm_ques_interest_2t_main_layout:
			if(interestDetail2tLayout.isShown()){
				rotateEnd(interestArrow2);
				interestDetail2tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(interestArrow2);
				interestDetail2tLayout.setVisibility(View.VISIBLE);
			}
//			new Handler().post(new Runnable() {
//				@Override
//				public void run() {
//					mainInterestLayout.scrollTo(0, mainInterestLayout.getHeight() + interestDetail2tLayout.getHeight());
//				}
//			});
			break;
		case R.id.comm_ques_interest_3t_main_layout:
			if(interestDetail3tLayout.isShown()){
				rotateEnd(interestArrow3);
				interestDetail3tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(interestArrow3);
				interestDetail3tLayout.setVisibility(View.VISIBLE);
			}
//			new Handler().post(new Runnable() {
//				@Override
//				public void run() {
//					mainInterestLayout.scrollTo(0, mainInterestLayout.getHeight() + interestDetail3tLayout.getHeight());
//				}
//			});
			break;
		case R.id.comm_ques_interest_4t_main_layout:
			if(interestDetail4tLayout.isShown()){
				rotateEnd(interestArrow4);
				interestDetail4tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(interestArrow4);
				interestDetail4tLayout.setVisibility(View.VISIBLE);
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						mainInterestLayout.scrollTo(0, mainInterestLayout.getHeight() + interestDetail4tLayout.getHeight());
					}
				});
			}
			break;
		case R.id.comm_ques_interest_5t_main_layout:
			if(interestDetail5tLayout.isShown()){
				rotateEnd(interestArrow5);
				interestDetail5tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(interestArrow5);
				interestDetail5tLayout.setVisibility(View.VISIBLE);
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						mainInterestLayout.scrollTo(0, mainInterestLayout.getHeight() + interestDetail5tLayout.getHeight());
					}
				});
			}
			break;
		case R.id.comm_ques_interest_6t_main_layout:
			if(interestDetail6tLayout.isShown()){
				rotateEnd(interestArrow6);
				interestDetail6tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(interestArrow6);
				interestDetail6tLayout.setVisibility(View.VISIBLE);
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						mainInterestLayout.scrollTo(0, mainInterestLayout.getHeight() + interestDetail6tLayout.getHeight());
					}
				});
			}
			break;
		case R.id.comm_ques_interest_7t_main_layout:
			if(interestDetail7tLayout.isShown()){
				rotateEnd(interestArrow7);
				interestDetail7tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(interestArrow7);
				interestDetail7tLayout.setVisibility(View.VISIBLE);
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						mainInterestLayout.scrollTo(0, mainInterestLayout.getHeight() + interestDetail7tLayout.getHeight());
					}
				});
			}
			break;
		case R.id.comm_ques_interest_8t_main_layout:
			if(interestDetail8tLayout.isShown()){
				rotateEnd(interestArrow8);
				interestDetail8tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(interestArrow8);
				interestDetail8tLayout.setVisibility(View.VISIBLE);
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						mainInterestLayout.scrollTo(0, mainInterestLayout.getHeight() + interestDetail8tLayout.getHeight());
					}
				});
			}
			break;
		case R.id.comm_ques_recharge_1t_main_layout:
			if(rechargeDetail1tLayout.isShown()){
				rotateEnd(rechargeArrow1);
				rechargeDetail1tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(rechargeArrow1);
				rechargeDetail1tLayout.setVisibility(View.VISIBLE);
			}
//			new Handler().post(new Runnable() {
//				@Override
//				public void run() {
//					mainRechargeLayout.scrollTo(0, mainRechargeLayout.getHeight() + rechargeDetail1tLayout.getHeight());
//				}
//			});
			break;
		case R.id.comm_ques_recharge_2t_main_layout:
			if(rechargeDetail2tLayout.isShown()){
				rotateEnd(rechargeArrow2);
				rechargeDetail2tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(rechargeArrow2);
				rechargeDetail2tLayout.setVisibility(View.VISIBLE);
			}
//			new Handler().post(new Runnable() {
//				@Override
//				public void run() {
//					mainRechargeLayout.scrollTo(0, mainRechargeLayout.getHeight() + rechargeDetail2tLayout.getHeight());
//				}
//			});
			break;
		case R.id.comm_ques_recharge_3t_main_layout:
			if(rechargeDetail3tLayout.isShown()){
				rotateEnd(rechargeArrow3);
				rechargeDetail3tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(rechargeArrow3);
				rechargeDetail3tLayout.setVisibility(View.VISIBLE);
			}
//			new Handler().post(new Runnable() {
//				@Override
//				public void run() {
//					mainRechargeLayout.scrollTo(0, mainRechargeLayout.getHeight() + rechargeDetail3tLayout.getHeight());
//				}
//			});
			break;
		case R.id.comm_ques_recharge_4t_main_layout:
			if(rechargeDetail4tLayout.isShown()){
				rotateEnd(rechargeArrow4);
				rechargeDetail4tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(rechargeArrow4);
				rechargeDetail4tLayout.setVisibility(View.VISIBLE);
			}
//			new Handler().post(new Runnable() {
//				@Override
//				public void run() {
//					mainRechargeLayout.scrollTo(0, mainRechargeLayout.getHeight() + rechargeDetail4tLayout.getHeight());
//				}
//			});
			break;
		case R.id.comm_ques_recharge_5t_main_layout:
			if(rechargeDetail5tLayout.isShown()){
				rotateEnd(rechargeArrow5);
				rechargeDetail5tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(rechargeArrow5);
				rechargeDetail5tLayout.setVisibility(View.VISIBLE);
//				new Handler().post(new Runnable() {
//					@Override
//					public void run() {
//						mainRechargeLayout.scrollTo(0, mainRechargeLayout.getHeight() + rechargeDetail5tLayout.getHeight());
//					}
//				});
			}
			break;
		case R.id.comm_ques_recharge_6t_main_layout:
			if(rechargeDetail6tLayout.isShown()){
				rotateEnd(rechargeArrow6);
				rechargeDetail6tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(rechargeArrow6);
				rechargeDetail6tLayout.setVisibility(View.VISIBLE);
//				new Handler().post(new Runnable() {
//					@Override
//					public void run() {
//						mainRechargeLayout.scrollTo(0, mainRechargeLayout.getHeight() + rechargeDetail6tLayout.getHeight());
//					}
//				});
			}
			break;
		case R.id.comm_ques_recharge_7t_main_layout:
			if(rechargeDetail7tLayout.isShown()){
				rotateEnd(rechargeArrow7);
				rechargeDetail7tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(rechargeArrow7);
				rechargeDetail7tLayout.setVisibility(View.VISIBLE);
//				new Handler().post(new Runnable() {
//					@Override
//					public void run() {
//						mainRechargeLayout.scrollTo(0, mainRechargeLayout.getHeight() + rechargeDetail7tLayout.getHeight());
//					}
//				});
			}
			break;
		case R.id.comm_ques_recharge_8t_main_layout:
			if(rechargeDetail8tLayout.isShown()){
				rotateEnd(rechargeArrow8);
				rechargeDetail8tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(rechargeArrow8);
				rechargeDetail8tLayout.setVisibility(View.VISIBLE);
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						mainRechargeLayout.scrollTo(0, mainRechargeLayout.getHeight() + rechargeDetail8tLayout.getHeight());
					}
				});
			}
			break;
		case R.id.comm_ques_recharge_9t_main_layout:
			if(rechargeDetail9tLayout.isShown()){
				rotateEnd(rechargeArrow9);
				rechargeDetail9tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(rechargeArrow9);
				rechargeDetail9tLayout.setVisibility(View.VISIBLE);
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						mainRechargeLayout.scrollTo(0, mainRechargeLayout.getHeight() + rechargeDetail9tLayout.getHeight());
					}
				});
			}
			break;
		case R.id.comm_ques_recharge_10t_main_layout:
			if(rechargeDetail10tLayout.isShown()){
				rotateEnd(rechargeArrow10);
				rechargeDetail10tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(rechargeArrow10);
				rechargeDetail10tLayout.setVisibility(View.VISIBLE);
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						mainRechargeLayout.scrollTo(0, mainRechargeLayout.getHeight() + rechargeDetail10tLayout.getHeight());
					}
				});
			}
			break;
		case R.id.comm_ques_recharge_11t_main_layout:
			if(rechargeDetail11tLayout.isShown()){
				rotateEnd(rechargeArrow11);
				rechargeDetail11tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(rechargeArrow11);
				rechargeDetail11tLayout.setVisibility(View.VISIBLE);
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						mainRechargeLayout.scrollTo(0, mainRechargeLayout.getHeight() + rechargeDetail11tLayout.getHeight());
					}
				});
			}
			break;
		case R.id.comm_ques_recharge_12t_main_layout:
			if(rechargeDetail12tLayout.isShown()){
				rotateEnd(rechargeArrow12);
				rechargeDetail12tLayout.setVisibility(View.GONE);
			}else{
				rotateStart(rechargeArrow12);
				rechargeDetail12tLayout.setVisibility(View.VISIBLE);
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						mainRechargeLayout.scrollTo(0, mainRechargeLayout.getHeight() + rechargeDetail12tLayout.getHeight());
					}
				});
			}
			break;
		default:
			break;
		}
	}
	
	private void rotateStart(ImageView img){
		ObjectAnimator.ofFloat(img, "rotation", 0.0F, 90.0F)
		.setDuration(200)  
        .start();  
	}
	
	private void rotateEnd(ImageView img){
		ObjectAnimator.ofFloat(img, "rotation", 90.0F, 0.0F)
		.setDuration(200)  
        .start(); 
	}
	
}
