package com.example.ylfcf.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.Log;

/**
 * Created by Administrator on 2017/8/14.
 */

public class ProgressRing {
    int ringX;
    int ringY;
    int ringRadius;
    int ringWidth;
    int headBallX;
    int headBallY;
    Paint paint;
    Shader sweepGradient;
    //private static final int RED = 230, GREEN = 85, BLUE = 35; //基础颜色，这里是橙红色
    private static final int RED = 230, GREEN = 0, BLUE = 0; //基础颜色，这里是橙红色
    private static final int MIN_ALPHA = 30; //最小不透明度
    private static final int MAX_ALPHA = 255; //最大不透明度
    /*
    //圆环颜色
    //渐变顺序不同，显示的方向不同
    private static int[] changeColors = new int[]{
            Color.argb(MAX_ALPHA, RED, GREEN, BLUE),
            Color.argb(MIN_ALPHA, RED, GREEN, BLUE),
            Color.argb(MIN_ALPHA, RED, GREEN, BLUE),
//            Color.TRANSPARENT,
    };
    */
    private int[] changeColors = new int[]{
            Color.argb(MIN_ALPHA, RED, GREEN, BLUE),
            Color.argb(MIN_ALPHA, RED, GREEN, BLUE),
            Color.argb(MAX_ALPHA, RED, GREEN, BLUE),
//            Color.TRANSPARENT,
    };

    public ProgressRing(int ringX, int ringY, int ringRadius, int ringWidth, int ringColor) {
        this.ringX = ringX;
        this.ringY =  ringY;
        this.ringRadius =  ringRadius;
        this.ringWidth=  ringWidth;
        paint = new Paint();
        paint.reset();
        paint.setColor(ringColor);
        paint.setAntiAlias(true); //消除锯齿
        paint.setStrokeWidth(ringWidth);
        paint.setStyle(Paint.Style.STROKE);  //绘制空心圆或 空心矩形,只显示边缘的线，不显示内部
    }

    public void drawProgressRing(Canvas canvas, int rotateDegree) {
        paint.reset();
        RectF rect = new RectF(ringX - ringRadius, ringY - ringRadius, ringX + ringRadius, ringY + ringRadius);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(ringWidth);
        paint.setStyle(Paint.Style.STROKE);
        //设置渐变
        sweepGradient = new SweepGradient(ringX, ringY, changeColors, null);
        //按照圆心旋转
        Matrix matrix = new Matrix();
        matrix.setRotate(rotateDegree, ringX, ringY);
        sweepGradient.setLocalMatrix(matrix);
        paint.setShader(sweepGradient);
        canvas.drawArc(rect, 0, 360, false, paint);
        //绘制进度环开头的小圆点
        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(MAX_ALPHA, RED, GREEN, BLUE));
        //使用三角函数时，需要把角度转为弧度
        headBallX = ringX + (int)(ringRadius * Math.cos((double)rotateDegree/180 * Math.PI));
        Log.e("degree", "degree: " + rotateDegree + "cos: " + Math.cos((double)rotateDegree/180 * Math.PI));
        headBallY = ringY + (int)(ringRadius * Math.sin((double)rotateDegree/180 * Math.PI));
        canvas.drawCircle(headBallX, headBallY, ringWidth/ 2, paint);
    }
}
