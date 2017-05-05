package com.ylfcf.ppp.view;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.ui.SRZXAppointActivity.OnWheelFinishListener;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.util.YLFLogger;
import com.ylfcf.ppp.widget.TosAdapterView;
import com.ylfcf.ppp.widget.TosAdapterView.OnItemClickListener;
import com.ylfcf.ppp.widget.TosGallery;
import com.ylfcf.ppp.widget.WheelView;

/**
 * 私人尊享 预约页面 日期选择
 * @author Mr.liu
 *
 */
public class SRZXAppointDatePickerPopwindow extends PopupWindow implements OnClickListener,com.ylfcf.ppp.widget.TosGallery.OnEndFlingListener{
	ArrayList<TextInfo> mMonths = new ArrayList<TextInfo>();
    ArrayList<TextInfo> mYears = new ArrayList<TextInfo>();
    ArrayList<TextInfo> mDays = new ArrayList<TextInfo>();
    ArrayList<TextInfo> mHours = new ArrayList<TextInfo>();
    ArrayList<TextInfo> mMinutes = new ArrayList<TextInfo>();
    
    private static final int[] DAYS_PER_MONTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    private static final String[] MONTH_NAME = { "01", "02", "03", "04", "05", "06", "07",
            "08", "09", "10", "11", "12"};
    
    int mCurDay = 0;
    int mCurMonth = 0;
    int mCurYear = 0;
    int mCurHour = 0;
    int mCurMinute = 0;
    
    int startYear = 2016;
    int endYear = 2038;

	private LayoutInflater layoutInflater;
	private Activity context;
	private OnWheelFinishListener onFinishListener;
	private WindowManager.LayoutParams lp = null;
	private WheelView yearWheel,monthWheel,dayWheel,hourWheel,minuteWheel;
	
	
	public SRZXAppointDatePickerPopwindow(Context context) {
		super(context);
	}

	public SRZXAppointDatePickerPopwindow(Context context, View convertView, int width,
			int height,OnWheelFinishListener listener) {
		super(convertView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.context = (Activity) context;
		this.onFinishListener = listener;
		findViews(convertView);
		prepareData();
	}

	private void findViews(View popView) {
		lp = context.getWindow().getAttributes();
		lp.alpha = 0.4f;
		context.getWindow().setAttributes(lp);

		layoutInflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		
		yearWheel = (WheelView) popView.findViewById(R.id.srzx_appoint_popwindow_wheel_year);
		yearWheel.setOnEndFlingListener(this);
		yearWheel.setSoundEffectsEnabled(true);
		yearWheel.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(TosAdapterView<?> parent, View view,
					int position, long id) {
//				Util.toastLong(context, position+"年");
				refreshPlanTime();
				dismiss();
			}
		});
		monthWheel = (WheelView) popView.findViewById(R.id.srzx_appoint_popwindow_wheel_month);
		monthWheel.setOnEndFlingListener(this);
		monthWheel.setSoundEffectsEnabled(true);
		monthWheel.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(TosAdapterView<?> parent, View view,
					int position, long id) {
//				Util.toastLong(context, position+"月");
				refreshPlanTime();
				dismiss();
			}
		});
		dayWheel = (WheelView) popView.findViewById(R.id.srzx_appoint_popwindow_wheel_day);
		dayWheel.setOnEndFlingListener(this);
		dayWheel.setSoundEffectsEnabled(true);
		dayWheel.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(TosAdapterView<?> parent, View view,
					int position, long id) {
//				Util.toastLong(context, position+"日");
				refreshPlanTime();
				dismiss();
			}
		});
		hourWheel = (WheelView) popView.findViewById(R.id.srzx_appoint_popwindow_wheel_hour);
		hourWheel.setOnEndFlingListener(this);
		hourWheel.setSoundEffectsEnabled(true);
		hourWheel.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(TosAdapterView<?> parent, View view,
					int position, long id) {
//				Util.toastLong(context, position+"时");
				refreshPlanTime();
				dismiss();
			}
		});
		minuteWheel = (WheelView) popView.findViewById(R.id.srzx_appoint_popwindow_wheel_minute);
		minuteWheel.setOnEndFlingListener(this);
		minuteWheel.setSoundEffectsEnabled(true);
		minuteWheel.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(TosAdapterView<?> parent, View view,
					int position, long id) {
//				Util.toastLong(context, position+"分");
				refreshPlanTime();
				dismiss();
			}
		});
		initAdapter();
	}

	public void show(View parentView) {
		ColorDrawable cd = new ColorDrawable(0x000000);
		this.setBackgroundDrawable(cd);// 使得返回键有效 并且去除popupwindow圆角的黑色背景
		this.setAnimationStyle(R.style.bidPopwindowStyle);
		this.setOutsideTouchable(false);
		this.setFocusable(true);
		this.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
	}

	private void initAdapter(){
		yearWheel.setAdapter(new WheelTextAdapter(context));
        monthWheel.setAdapter(new WheelTextAdapter(context));
        dayWheel.setAdapter(new WheelTextAdapter(context));
        hourWheel.setAdapter(new WheelTextAdapter(context));
        minuteWheel.setAdapter(new WheelTextAdapter(context));
	}
	
	protected class TextInfo {
        public TextInfo(int index, String text, boolean isSelected) {
            mIndex = index;
            mText = text;
            mIsSelected = isSelected;

            if (isSelected) {
                mColor = Color.BLUE;
            }
        }

        public int mIndex;
        public String mText;
        public boolean mIsSelected = false;
        public int mColor = Color.BLACK;
    }
	
	private void refreshPlanTime(){
		StringBuffer sb = new StringBuffer();
        sb.append(mYears.get(mCurYear - startYear).mText).append("-").append(mMonths.get(mCurMonth).mText).append("-")
        	.append(mDays.get(mCurDay).mText).append(" ").append(mHours.get(mCurHour).mText).append(":").append(mMinutes.get(mCurMinute).mText);
        onFinishListener.onFinish(sb.toString());
        YLFLogger.d("ppp", sb.toString());
	}
	
	private void setDay(int day) {
        if (day != mCurDay) {
        	mCurDay = day;
        	refreshPlanTime();
        }
    }
	
    private void setYear(int year) {
        if (year != mCurYear) {
            mCurYear = year;
            refreshPlanTime();
        }
    }

    private void setMonth(int month) {
        if (month != mCurMonth) {
            mCurMonth = month;
            if(mCurMonth == 1 && mCurDay > 27){
            	mCurDay = 27;
            }
            if(mCurMonth == 3 || mCurMonth == 5 || mCurMonth == 8 || mCurMonth == 10){
            	if(mCurDay > 29){
            		mCurDay = 29;
            	}
            }
            Calendar calendar = Calendar.getInstance();
            int date = calendar.get(Calendar.DATE);
            prepareDayData(mCurYear, month, date);
            refreshPlanTime();
        }
    }
    
    private void setHour(int hour){
    	if(hour != mCurHour){
    		mCurHour = hour;
    		refreshPlanTime();
    	}
    }
	
    private void setMinute(int minute){
    	if(minute != mCurMinute){
    		mCurMinute = minute;
    		refreshPlanTime();
    	}
    }
    
    private void prepareDayData(int year, int month, int curDate) {
    	mDays.clear();
        int days = DAYS_PER_MONTH[month];

        // The February.
        if (1 == month) {
            days = isLeapYear(year) ? 29 : 28;
        }
        for (int i = 0; i <= days - 1; ++i) {
        	if(i < 9){
        		mDays.add(new TextInfo(i, "0"+(i + 1), (i == curDate - 1)));
        	}else{
        		mDays.add(new TextInfo(i, String.valueOf(i + 1), (i == curDate - 1)));
        	}
        }
        ((WheelTextAdapter) dayWheel.getAdapter()).setData(mDays);
    }
    
    private boolean isLeapYear(int year) {
        return ((0 == year % 4) && (0 != year % 100) || (0 == year % 400));
    }
    
    /**
     * 初始化数据
     */
    private void prepareData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        mCurDay = day - 1;
        mCurMonth = month;
        mCurYear = year;
        mCurHour = hour;
        mCurMinute = minute;

        for (int i = 0; i < MONTH_NAME.length; ++i) {
            mMonths.add(new TextInfo(i, MONTH_NAME[i], (i == month)));
        }

        for (int i = startYear; i <= endYear; ++i) {
            mYears.add(new TextInfo(i, String.valueOf(i), (i == year)));
        }

        for(int i = 0;i <= 23;i++){
        	if(i <= 9){
        		mHours.add(new TextInfo(i, "0"+i, (i == hour)));
        	}else{
        		mHours.add(new TextInfo(i, String.valueOf(i), (i == hour)));
        	}
        }
        
        for(int i = 0;i <= 59;i++){
        	if(i <= 9){
        		mMinutes.add(new TextInfo(i, "0"+(i), (i == minute)));
        	}else{
        		mMinutes.add(new TextInfo(i, String.valueOf(i), (i == minute)));
        	}
        }
        
        ((WheelTextAdapter) monthWheel.getAdapter()).setData(mMonths);
        ((WheelTextAdapter) yearWheel.getAdapter()).setData(mYears);
        ((WheelTextAdapter) hourWheel.getAdapter()).setData(mHours);
        ((WheelTextAdapter) minuteWheel.getAdapter()).setData(mMinutes);
        
        prepareDayData(year, month, day);

        monthWheel.setSelection(month);
        yearWheel.setSelection(year - startYear);
        dayWheel.setSelection(day - 1);
        hourWheel.setSelection(hour);
        minuteWheel.setSelection(minute);
    }
    
	@Override
	public void dismiss() {
		super.dismiss();
		lp.alpha = 1.0f;
		context.getWindow().setAttributes(lp);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}

	@Override
	public void onEndFling(com.ylfcf.ppp.widget.TosGallery v) {
		int pos = v.getSelectedItemPosition();

        if (v == dayWheel) {
            TextInfo info = mDays.get(pos);
            setDay(info.mIndex);
        } else if (v == monthWheel) {
            TextInfo info = mMonths.get(pos);
            setMonth(info.mIndex);
        } else if (v == yearWheel) {
            TextInfo info = mYears.get(pos);
            setYear(info.mIndex);
        } else if (v == hourWheel) {
        	TextInfo info = mHours.get(pos);
        	setHour(info.mIndex);
        } else if (v == minuteWheel) {
        	TextInfo info = mMinutes.get(pos);
        	setMinute(info.mIndex);
        }
	}
	
	/**
	 * 
	 * @author Mr.liu
	 *
	 */
	protected class WheelTextAdapter extends BaseAdapter {
        ArrayList<TextInfo> mData = null;
        int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        int mHeight = 50;
        Context mContext = null;

        public WheelTextAdapter(Context context) {
            mContext = context;
            mHeight = (int) Util.pixelToDp(context, mHeight);
        }

        public void setData(ArrayList<TextInfo> data) {
            mData = data;
            this.notifyDataSetChanged();
        }

        public void setItemSize(int width, int height) {
            mWidth = width;
            mHeight = (int) Util.pixelToDp(mContext, height);
        }

        @Override
        public int getCount() {
            return (null != mData) ? mData.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = null;

            if (null == convertView) {
                convertView = new TextView(mContext);
                convertView.setLayoutParams(new TosGallery.LayoutParams(mWidth, mHeight));
                textView = (TextView) convertView;
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                textView.setTextColor(Color.BLACK);
            }

            if (null == textView) {
                textView = (TextView) convertView;
            }

            TextInfo info = mData.get(position);
            textView.setText(info.mText);
            textView.setTextColor(info.mColor);

            return convertView;
        }
    }
}
