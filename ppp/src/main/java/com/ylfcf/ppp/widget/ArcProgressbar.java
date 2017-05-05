package com.ylfcf.ppp.widget;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.util.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 弧形进度条
 * @author Administrator
 *  progress  的区间范围为0 - 260
 */
public class ArcProgressbar extends View{
	private Context context;
	public ArcProgressbar(Context context) {
        super(context);
        this.context = context;
        smallBgColor = context.getResources().getColor(R.color.yxb_arc_ps_bg);
        barColor  = context.getResources().getColor(R.color.yxb_arc_ps_bar);
    }
 
    public ArcProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        smallBgColor = context.getResources().getColor(R.color.yxb_arc_ps_bg);
        barColor  = context.getResources().getColor(R.color.yxb_arc_ps_bar);
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init(canvas);
    }
 
    private int arcRadiusTemp = 0;//圆的半径
    private int cxTemp;//圆心x坐标
    private int cyTemp;//圆心y坐标
    
    private void init(Canvas canvas) {
    	int[] params = Util.getScreenWidthAndHeight(context);
        // 画弧形的矩阵区域。
    	float left = context.getResources().getDimensionPixelSize(R.dimen.common_measure_100dp);
    	float top = context.getResources().getDimensionPixelSize(R.dimen.common_measure_20dp);
    	float right = params[0] - left;
    	float bottom = params[0] - left*2 + top;
    	diameter = (int) (params[0] - left*2);//直径 
        rectBg = new RectF(left, top, right, bottom);//必须是个正方形
 
        // 计算弧形的圆心和半径。
        int arcRadius = (int) (diameter / 2);
        int cx1 = params[0] / 2;
        int cy1 = (int) (arcRadius+top);
        arcRadiusTemp = arcRadius;
        cxTemp = cx1;
        cyTemp = cy1;
        		
        // ProgressBar结尾和开始画2个圆，实现ProgressBar的圆角。
        mPaintCircleLeft = new Paint();
        mPaintCircleLeft.setAntiAlias(true);
        mPaintCircleLeft.setColor(barColor);
        
        mPaintCircleRight = new Paint();
        mPaintCircleRight.setAntiAlias(true);
        mPaintCircleRight.setColor(smallBgColor);
        
        Paint mPaintCircleCir = new Paint();
        mPaintCircleCir.setAntiAlias(true);
        mPaintCircleCir.setColor(getResources().getColor(R.color.black));
        
        canvas.drawCircle(
                (float) (cx1 + arcRadius * Math.cos(startAngle * 3.1415 / 180)),
                (float) (cy1 + arcRadius * Math.sin(startAngle * 3.1415 / 180)),
                barStrokeWidth / 2 + 1, mPaintCircleLeft);// 小圆
        canvas.drawCircle(
                (float) (cx1 + arcRadius * Math.cos((180 - startAngle) * 3.14 / 180)),
                (float) (cy1 + arcRadius * Math.sin((180 - startAngle) * 3.14 / 180)),
                barStrokeWidth / 2 + 1, mPaintCircleRight);// 小圆
        
        // 弧形背景。
//        mPaintBg = new Paint();
//        mPaintBg.setAntiAlias(true);
//        mPaintBg.setStyle(Style.STROKE);
//        mPaintBg.setStrokeWidth(bgStrokeWidth);
//        mPaintBg.setColor(bgColor);
//        canvas.drawArc(rectBg, startAngle, endAngle, false, mPaintBg);
 
        // 弧形小背景。
        if (showSmallBg) {
            mPaintSmallBg = new Paint();
            mPaintSmallBg.setAntiAlias(true);
            mPaintSmallBg.setStyle(Style.STROKE);
            mPaintSmallBg.setStrokeWidth(barStrokeWidth);
            mPaintSmallBg.setColor(smallBgColor);
            canvas.drawArc(rectBg, startAngle, endAngle, false, mPaintSmallBg);
        }
 
        // 弧形ProgressBar。
        mPaintBar = new Paint();
        LinearGradient lg=new LinearGradient((float) (cx1 + arcRadius * Math.cos(startAngle * 3.1415 / 180))
        		,(float) (cy1 + arcRadius * Math.sin(startAngle * 3.1415 / 180))
        		,(float) (cx1 + arcRadius * Math.cos((180 - startAngle) * 3.14 / 180))
        		,(float) (cy1 + arcRadius * Math.sin((180 - startAngle) * 3.14 / 180))
        		,getResources().getColor(R.color.yxb_arc_ps_bar),
        		getResources().getColor(R.color.yxb_arc_ps_bg),TileMode.MIRROR);
        mPaintBar.setShader(lg);
        mPaintBar.setAntiAlias(true);
        mPaintBar.setStyle(Style.STROKE);
        mPaintBar.setStrokeWidth(barStrokeWidth);
        mPaintBar.setColor(barColor);
        canvas.drawArc(rectBg, startAngle, progress, false, mPaintBar);
 
        // 随ProgressBar移动的圆。
        if (showMoveCircle) {
        	//外层黄色的圆
        	mPaintCircleLeft.setColor(smallBgColor);
        	canvas.drawCircle(
                    (float) (cx1 + arcRadius
                            * Math.cos(angleOfMoveCircle * 3.1415 / 180)),
                    (float) (cy1 + arcRadius
                            * Math.sin(angleOfMoveCircle * 3.1415 / 180)),
                    bgStrokeWidth / 2 + 5, mPaintCircleLeft);// 小圆
        	
        	//里层红色的圆
        	mPaintCircleLeft.setColor(barColor);
            canvas.drawCircle(
                    (float) (cx1 + arcRadius
                            * Math.cos(angleOfMoveCircle * 3.1415 / 180)),
                    (float) (cy1 + arcRadius
                            * Math.sin(angleOfMoveCircle * 3.1415 / 180)),
                    bgStrokeWidth / 2, mPaintCircleLeft);// 小圆
        }
        invalidate();
    }
 
    /**
     * 
     * @param progress
     */
    public void addProgress(int _progress) {
    	angleOfMoveCircle = 140;
    	progress = 0;
        progress = +_progress;
        angleOfMoveCircle += _progress;
        System.out.println(progress);
        if (progress > endAngle) {
            progress = 0;
            angleOfMoveCircle = startAngle;
        }
        invalidate();
    }
    
    //将圆外的点击事件拦截掉
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(action != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent( event);
        }
        int x = (int)event.getX();
        int y = (int)event.getY();
        double distance = Util.getDistance(x, y, cxTemp, cyTemp);
        if(distance > arcRadiusTemp){
        	return false;
        }
        return super.onTouchEvent( event);
    }
 
    /**
     * 设置弧形背景的画笔宽度。
     */
    public void setBgStrokeWidth(int bgStrokeWidth) {
        this.bgStrokeWidth = bgStrokeWidth;
    }
 
    /**
     * 设置弧形ProgressBar的画笔宽度。
     */
    public void setBarStrokeWidth(int barStrokeWidth) {
        this.barStrokeWidth = barStrokeWidth;
    }
 
    /**
     * 设置弧形背景的颜色。
     */
    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }
 
    /**
     * 设置弧形ProgressBar的颜色。
     */
    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }
 
    /**
     * 设置弧形小背景的颜色。
     */
    public void setSmallBgColor(int smallBgColor) {
        this.smallBgColor = smallBgColor;
    }
 
    /**
     * 设置弧形的直径。
     */
    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }
 
    /**
     * 是否显示小背景。
     */
    public void setShowSmallBg(boolean showSmallBg) {
        this.showSmallBg = showSmallBg;
    }
 
    /**
     * 是否显示移动的小圆。
     */
    public void setShowMoveCircle(boolean showMoveCircle) {
        this.showMoveCircle = showMoveCircle;
    }
 
    private int bgStrokeWidth = 44;
    private int barStrokeWidth = 15;
    private int bgColor = Color.TRANSPARENT;
    private int barColor = 0;//弧形进度条上bar的颜色
    private int smallBgColor = 0;//弧形背景颜色
    private int progress = 0;
    private int angleOfMoveCircle = 140;// 移动小园的起始角度。
    private int startAngle = 140;//起始角度，以x轴为起始
    private int endAngle = 260;//相对于起始点的位置的角度
    private Paint mPaintBar = null;
    private Paint mPaintSmallBg = null;
    private Paint mPaintBg = null;
    private Paint mPaintCircleLeft = null;
    private Paint mPaintCircleRight = null;
    private RectF rectBg = null;
    /**
     * 直徑。
     */
    private int diameter = 0;
 
    private boolean showSmallBg = true;// 是否显示小背景。
    private boolean showMoveCircle = true;// 是否显示移动的小园。
 
}
