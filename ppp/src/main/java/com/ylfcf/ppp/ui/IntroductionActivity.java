/**
 * Show introduction images,and transfer to login page at the end.
 *
 * Copyright ylfcf ShangHai co.
 * All rights reserved.
 */
package com.ylfcf.ppp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.db.DBGesturePwdManager;
import com.ylfcf.ppp.entity.GesturePwdEntity;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;
import com.ylfcf.ppp.viewflow.ViewFlow;

/**
 * 开机介绍页面
 * 
 * @author Waggoner.wang
 * 
 */
public class IntroductionActivity extends BaseActivity {
	private static final String className = "IntroductionActivity";
	private ViewFlow viewFlow;
	// private CircleFlowIndicator circleIndicator;

	private final int[] ids = { R.drawable.intro_page_1,
			R.drawable.intro_page_2, R.drawable.intro_page_3};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.introduction_activity);
		findViews();
	}

	private void findViews() {
		viewFlow = (ViewFlow) findViewById(R.id.intro_activity_viewflow);
		// circleIndicator = (CircleFlowIndicator)
		// findViewById(R.id.intro_activity_viewflow_indic);
		// viewFlow.setFlowIndicator(circleIndicator);
		viewFlow.setAdapter(new ImageAdapter(this), 0);
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

	class ImageAdapter extends BaseAdapter {

		private final LayoutInflater mInflater;

		public ImageAdapter(Context context) {
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return ids.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.intro_image_item, null);
			}
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.intro_imgview);
			imageView.setImageBitmap(getDrawable(ids[position]));
			imageView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (position == ids.length - 1) {
						finish();
						GesturePwdEntity entity = DBGesturePwdManager.getInstance(
								getApplicationContext()).getGesturePwdEntity(SettingsManager.getUserId(getApplicationContext()));
						String gesturePwd = null;
						if (entity != null) {
							gesturePwd = entity.getPwd();
						}
						Intent intent = new Intent();
						if (gesturePwd != null && !"".equals(gesturePwd)) {
							// 手势密码验证
							intent.setClass(IntroductionActivity.this,
									GestureVerifyActivity.class);
						}else{
							intent.setClass(IntroductionActivity.this.getApplicationContext(),
									MainFragmentActivity.class);
						}
						IntroductionActivity.this.startActivity(intent);
					} else {
						viewFlow.snapToScreen(position + 1);
					}
				}
			});
			return convertView;
		}

		private Bitmap getDrawable(int resId) {
			Display disp = getWindowManager().getDefaultDisplay();

			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// BitmapFactory.decodeResource(getResources(), resId, opts);
			opts.inSampleSize = calculateSampleSize(opts, disp.getWidth(),
					disp.getHeight());
			opts.inJustDecodeBounds = false;
			opts.inPreferredConfig = Bitmap.Config.ALPHA_8;
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeResource(getResources(), resId,
						opts);
			} catch (OutOfMemoryError e) {
				return null;
			}
			return bitmap;
		}

		public int calculateSampleSize(BitmapFactory.Options options,
				int reqWidth, int reqHeight) {
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > reqHeight || width > reqWidth) {
				final int halfHeight = height / 2;
				final int halfWidth = width / 2;
				while ((halfHeight / inSampleSize) > reqHeight
						&& (halfWidth / inSampleSize) > reqWidth) {
					inSampleSize += 2;
				}
			}

			return inSampleSize;
		}
	}

}
