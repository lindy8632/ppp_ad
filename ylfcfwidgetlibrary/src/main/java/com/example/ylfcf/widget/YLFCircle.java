package com.example.ylfcf.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/8/14.
 */

public class YLFCircle extends View {
    private int circleX = 0;//圆心X坐标
    private int circleY = 0;//圆心Y坐标
    private int radius = 0;
    private int width = 0;
    private float startAngle = 270;
    private float angleA,angleB,angleC;
    private int colorA,colorB,colorC,colorDef;

    private Context context;

    /**
     *
     * @param context
     */
    public YLFCircle(Context context) {
        super(context);
    }

    public YLFCircle(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        this.context = context;
    }

    public void initCircleData(int circleX,int circleY,int radius,int width){
        this.circleX = circleX;
        this.circleY = circleY;
        this.radius = radius;
        this.width = width;
    }

    public void setAngles(float angleA,float angleB,float angleC){
        this.angleA = angleA;
        this.angleB = angleB;
        this.angleC = angleC;
    }

    public void setColors(int colorA,int colorB,int colorC,int colorDef){
        this.colorA = colorA;
        this.colorB = colorB;
        this.colorC = colorC;
        this.colorDef = colorDef;
    }

    int startTotalAngles = 90;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(angleA == 0f && angleB == 0f && angleC == 0f){
            CircleRing cRingDef = new CircleRing(circleX, circleY, radius, width, colorDef);
            cRingDef.drawCircleRing(canvas, startAngle,360f*startTotalAngles/360);
        }else{
            if(angleA > 0){
                CircleRing cRingA = new CircleRing(circleX, circleY, radius, width, colorA);
                if(startTotalAngles >= 360 && angleA*startTotalAngles < 360){
                    //角度不足1度，则设为度
                    cRingA.drawCircleRing(canvas, startAngle,1);
                }else{
                    cRingA.drawCircleRing(canvas, startAngle,angleA*startTotalAngles/360);
                }
            }

            if(angleB > 0){
                CircleRing cRingB = new CircleRing(circleX, circleY, radius, width, colorB);
                if(startTotalAngles >= 360 && angleB*startTotalAngles < 360){
                    cRingB.drawCircleRing(canvas, startAngle + angleA*startTotalAngles/360 - 1,1);
                }else{
                    cRingB.drawCircleRing(canvas, startAngle + angleA*startTotalAngles/360 - 1,(angleB+2)*startTotalAngles/360);
                }
            }

            if(angleC > 0){
                CircleRing cRingC = new CircleRing(circleX, circleY, radius, width, colorC);
                if(startTotalAngles >= 360){
                    cRingC.drawCircleRing(canvas, startAngle + (angleA + angleB)*startTotalAngles/360 - 2, 363 - angleA - angleB);
                }else{
                    cRingC.drawCircleRing(canvas, startAngle + (angleA + angleB)*startTotalAngles/360 - 2, angleC*startTotalAngles/360);
                }
            }
        }

        startTotalAngles += 6;
        if(startTotalAngles > 360)
            return;
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        invalidate();
    }
}
