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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StepsLineView extends View {
    private int mGap = 0;//间隙
    private static final int mCount = 6;//条的个数
    private Paint mUnderPaint = null;
    private Paint mForegroundPaint = null;
    private int mPaintWidth = 0;
    private float mInputProgress;
    private int mAverageLength = 0;
    private List<RectF> mForegroundList = new ArrayList<>();
    private float[] mProgress;
    private int mCorner = 0;


    public StepsLineView(Context context) {
        this(context,null);
    }

    public StepsLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StepsLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.StepsLineView);
        int underColor = typedArray.getColor(R.styleable.StepsLineView_step_live_view_under_color,Color.GRAY);
        int foregroundColor = typedArray.getColor(R.styleable.StepsLineView_step_live_view_foreground_color,Color.BLUE);
        int paintWidth = typedArray.getDimensionPixelSize(R.styleable.DiscountView_discount_view_paint_width, DensityUtil.dpToPx(context,5.0f));
        int gap = typedArray.getDimensionPixelSize(R.styleable.StepsLineView_step_live_view_gap,DensityUtil.dpToPx(context,2.0f));
        int corner = typedArray.getDimensionPixelSize(R.styleable.StepsLineView_step_live_view_corner,DensityUtil.dpToPx(context,3.0f));
        typedArray.recycle();

        mGap = gap;
        mPaintWidth =  paintWidth;
        mCorner = corner;

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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //平均每一个线的长度
        mAverageLength = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - mCount * mPaintWidth  - (mCount - 1) * mGap) / mCount;
        setMeasuredDimension(widthMeasureSpec,mPaintWidth);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        drawUnderLine(canvas);
        drawForegroundLine(canvas);
    }

    /**
      * 绘制底色
      */
    private void drawUnderLine(Canvas canvas){
        float startX = getPaddingLeft() + mPaintWidth / 2;
        float endX = startX + mAverageLength;

        for (int i = 0; i < mCount; i++) {
            RectF rectF = new RectF(startX,mPaintWidth / 2,endX,mPaintWidth / 2);
            startX = startX + rectF.width() + mGap + mPaintWidth ;
            endX = startX + mAverageLength;
            canvas.drawRoundRect(rectF,mCorner,mCorner,mUnderPaint);
        }
    }


    /**
      * 绘制上层
      */
    private void drawForegroundLine(Canvas canvas){
        for (int i = 0; i < mForegroundList.size(); i++) {
            RectF rectF = mForegroundList.get(i);
            canvas.drawRect(rectF.left,rectF.top,rectF.left + rectF.width() * mProgress[i],rectF.bottom,mForegroundPaint);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mForegroundList.isEmpty()){
            float startX = getPaddingLeft() + mPaintWidth / 2;
            float endX = startX + mAverageLength;
            for (int i = 0; i < mCount; i++) {
                RectF rectF = new RectF(startX,mPaintWidth / 2,endX,mPaintWidth / 2);
                startX = startX + rectF.width() + mGap + mPaintWidth ;
                endX = startX + mAverageLength;
                mForegroundList.add(rectF);
            }

            mProgress = new float[mForegroundList.size()];
            Arrays.fill(mProgress,0.0f);
        }
    }

    public void setProgress(float progress){
        this.mInputProgress = progress;

        if(progress == 0){
            mProgress = new float[mForegroundList.size()];
            Arrays.fill(mProgress,0.0f);
        }

        if (mInputProgress > 1.0f) {
            mInputProgress = 1.0f;
        }

        if (mInputProgress < 0.0f) {
            mInputProgress = 0.0f;
        }


        float averageProgress = 1.0f / mCount;
        for (int i = 0; i < mCount; i++) {
            if(progress <= averageProgress * 1){
                mProgress[0] = progress / averageProgress;
            }else if(progress <= averageProgress * 2){
                mProgress[0] = 1.0f;
                mProgress[1] = (progress - averageProgress * 1) / averageProgress;
            }else if (progress <= averageProgress * 3){
                mProgress[0] = 1.0f;
                mProgress[1] = 1.0f;
                mProgress[2] = (progress - averageProgress * 2) / averageProgress;
            }else if (progress <= averageProgress * 4){
                mProgress[0] = 1.0f;
                mProgress[1] = 1.0f;
                mProgress[2] = 1.0f;
                mProgress[3] = (progress - averageProgress * 3) / averageProgress;
            }else if (progress <= averageProgress * 5){
                mProgress[0] = 1.0f;
                mProgress[1] = 1.0f;
                mProgress[2] = 1.0f;
                mProgress[3] = 1.0f;
                mProgress[4] = (progress - averageProgress * 4) / averageProgress;
            }else if (progress <= averageProgress * 6){
                mProgress[0] = 1.0f;
                mProgress[1] = 1.0f;
                mProgress[2] = 1.0f;
                mProgress[3] = 1.0f;
                mProgress[4] = 1.0f;
                mProgress[5] = (progress - averageProgress * 5) / averageProgress;
            }
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
