package com.example.ylfcf.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Administrator on 2017/8/14.
 */

public class CircleRing {
    int ringX;
    int ringY;
    int ringRadius;
    int ringWidth;
    Paint paint;

    //    public CircleRing(int ringX, int ringY, int ringRadius, int ringWidth, int ringColor, int startAngle) {
    public CircleRing(int ringX, int ringY, int ringRadius, int ringWidth, int ringColor) {
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

    public void drawCircleRing(Canvas canvas, float startAngle,
                               float angle) {
        RectF rect = new RectF(ringX - ringRadius, ringY - ringRadius, ringX + ringRadius, ringY + ringRadius);
        //false 不画圆心
        paint.setAlpha(255);
        canvas.drawArc(rect, startAngle, angle, false, paint);
    }
}
