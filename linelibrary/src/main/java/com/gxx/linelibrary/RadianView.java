package com.gxx.linelibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gxx.linelibrary.utils.DensityUtil;


/**
  * 半圆弧view
  */
public class RadianView extends View {
    private Paint mUnderPaint;
    private Paint mForegroundPaint;
    private int mPaintWidth = 0;
    private float mInputProgress;

    public RadianView(Context context) {
        this(context,null);
    }

    public RadianView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RadianView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.RadianView);
        int underColor = typedArray.getColor(R.styleable.RadianView_radian_view_under_color, Color.BLUE);
        int foregroundColor = typedArray.getColor(R.styleable.RadianView_radian_view_foreground_color,Color.WHITE);
        int paintWidth = typedArray.getDimensionPixelSize(R.styleable.RadianView_radian_view_paint_width, DensityUtil.dpToPx(context,5.0f));
        typedArray.recycle();

        this.mPaintWidth = paintWidth;

        mUnderPaint = new Paint();
        mUnderPaint.setAntiAlias(true);
        mUnderPaint.setColor(underColor);
        mUnderPaint.setStyle(Paint.Style.STROKE);
        mUnderPaint.setStrokeWidth(mPaintWidth);
        mUnderPaint.setStrokeJoin(Paint.Join.ROUND);
        mUnderPaint.setStrokeCap(Paint.Cap.ROUND);

        mForegroundPaint = new Paint();
        mForegroundPaint.setAntiAlias(true);
        mForegroundPaint.setColor(foregroundColor);
        mForegroundPaint.setStyle(Paint.Style.STROKE);
        mForegroundPaint.setStrokeWidth(mPaintWidth);
        mForegroundPaint.setStrokeJoin(Paint.Join.ROUND);
        mForegroundPaint.setStrokeCap(Paint.Cap.ROUND);

    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        RectF underRect = new RectF(mPaintWidth / 2,mPaintWidth / 2,getMeasuredWidth() - mPaintWidth / 2,getMeasuredHeight());
        canvas.drawArc(underRect,135,270,false,mUnderPaint);


        RectF ForegroundRectF = new RectF(mPaintWidth / 2,mPaintWidth / 2,getMeasuredWidth() - mPaintWidth / 2,getMeasuredHeight());
        canvas.drawArc(ForegroundRectF,135,270 * mInputProgress,false,mForegroundPaint);
    }


    public void setProgress(float progress){
        this.mInputProgress = progress;

        if(mInputProgress >= 1.0f){
            mInputProgress = 1.0f;
        }

        if(mInputProgress <= 0.0f){
            mInputProgress = 0.0f;
        }

        invalidate();
    }



    public void setUnderColor(int color){
        mUnderPaint.setColor(color);
    }

    public void setForegroundColor(int color){
        mForegroundPaint.setColor(color);
    }
}
