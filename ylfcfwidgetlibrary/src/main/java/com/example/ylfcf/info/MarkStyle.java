package com.example.ylfcf.info;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/8/17.
 */

public class MarkStyle {
    public static final int BACKGROUND = 1;
    public static final int DOT = 2;
    public static final int LEFTSIDEBAR = 3;
    public static final int RIGHTSIDEBAR = 4;
    public static final int TEXT = 5;
    public static final int DEFAULT = 10;

    public static int defaultColor = Color.rgb(49,178,254);

    public static String text;
    public static int textColor;

    public static int current = DEFAULT;

    public static Drawable todayBackground = new Drawable() {
        private Paint paint;
        {
            paint = new Paint();
            paint.setColor(Color.rgb(63, 81, 200));
        }
        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(canvas.getWidth() / 2,
                    canvas.getHeight() / 2,
                    (int)(canvas.getHeight() / 2.5),
                    paint);
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }
    };

    public static Drawable choose = new Drawable() {
        private Paint paint;
        {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(defaultColor);
        }
        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(canvas.getWidth() / 2,
                    canvas.getHeight() / 2,
                    (int)(canvas.getHeight() / 2.5),
                    paint);
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }
    };

    private int style;
    private int color;

    public MarkStyle() {
        this.style = MarkStyle.DEFAULT;
        this.color = MarkStyle.defaultColor;
    }

    public MarkStyle(int style, int color) {
        this.style = style;
        this.color = color;
    }

    public int getStyle() {
        return style;
    }

    public MarkStyle setStyle(int style) {
        this.style = style;
        return this;
    }

    public int getColor() {
        return color;
    }

    public MarkStyle setColor(int color) {
        this.color = color;
        return this;
    }
}
