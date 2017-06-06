package com.ylfcf.ppp.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 品牌介绍
 * @author Administrator
 *
 */
public class BrandIntroActivity extends BaseActivity implements OnClickListener{
	private LinearLayout topLeftBtn;
	private TextView topTitleTV;
	private ViewPager viewpager;
	private CirclePageIndicator indicator;
	private int[] bgs = {R.drawable.brand_intro_bg1,R.drawable.brand_intro_bg2,R.drawable.brand_intro_bg3};
	private List<ImageView> viewList = new ArrayList<ImageView>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.brand_intro_activity);
		findViews();
	}
	
	private void findViews(){
		topLeftBtn = (LinearLayout)findViewById(R.id.common_topbar_left_layout);
		topLeftBtn.setOnClickListener(this);
		topTitleTV = (TextView)findViewById(R.id.common_page_title);
		topTitleTV.setText("品牌介绍");
		
		viewpager = (ViewPager)findViewById(R.id.brand_intro_activity_viewpager);
		indicator = (CirclePageIndicator) findViewById(R.id.brand_intro_activity_indicator);
		for(int i=0;i<bgs.length;i++){
			ImageView view = new ImageView(this);
			view.setImageResource(bgs[i]);
			view.setScaleType(ScaleType.FIT_XY);
			viewList.add(view);
		}
		viewpager.setAdapter(mPagerAdapter);
		indicator.setViewPager(viewpager);
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
	
	PagerAdapter mPagerAdapter = new PagerAdapter(){

        @Override
        //获取当前窗体界面数
        public int getCount() {
            return viewList.size();
        }

        @Override
        //断是否由对象生成界面
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;
        }
        //是从ViewGroup中移出当前View
        public void destroyItem(View arg0, int arg1, Object arg2) {  
                ((ViewPager) arg0).removeView(viewList.get(arg1));  
        }  
        
        //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
        public Object instantiateItem(View arg0, int arg1){
            ((ViewPager)arg0).addView(viewList.get(arg1));
            return viewList.get(arg1);                
        }
    };
}
