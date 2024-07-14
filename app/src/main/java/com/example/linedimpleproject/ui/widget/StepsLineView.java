package com.example.linedimpleproject.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.linedimpleproject.R;
import com.gxx.linelibrary.utils.DensityUtil;

public class StepsLineView extends View {
    private int mGap = 0;//间隙
    private int mCount = 6;//条的个数
    private Paint mUnderPaint = null;
    private Paint mForegroundPaint = null;
    private int mPaintWidth = 0;
    private float mInputProgress;
    private int mAverageLength = 0;

    public StepsLineView(Context context) {
        this(context,null);
    }

    public StepsLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StepsLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mGap = DensityUtil.dpToPx(context,2.0f);
        mPaintWidth =  DensityUtil.dpToPx(context,5.0f);

        mUnderPaint = new Paint();
        mUnderPaint.setAntiAlias(true);
        mUnderPaint.setColor(context.getResources().getColor(R.color.color_DCE1E8));
        mUnderPaint.setStyle(Paint.Style.STROKE);
        mUnderPaint.setStrokeWidth(mPaintWidth);
        mUnderPaint.setStrokeJoin(Paint.Join.ROUND);
        mUnderPaint.setStrokeCap(Paint.Cap.ROUND);


        mForegroundPaint = new Paint();
        mForegroundPaint.setAntiAlias(true);
        mForegroundPaint.setColor(context.getResources().getColor(R.color.color_0F67FE));
        mForegroundPaint.setStyle(Paint.Style.STROKE);
        mForegroundPaint.setStrokeWidth(mPaintWidth);
        mForegroundPaint.setStrokeJoin(Paint.Join.ROUND);
        mForegroundPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //平均每一个线的长度
        mAverageLength = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - (mCount - 1) * mGap) / mCount;
        setMeasuredDimension(widthMeasureSpec,mPaintWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        float startX = 0 + getPaddingLeft();
        for (int i = 0; i < mCount; i++) {
            RectF rectF = new RectF(startX,mPaintWidth / 2,(i + 1) * mAverageLength,mPaintWidth / 2);
            canvas.drawRoundRect(rectF,DensityUtil.dpToPx(getContext(),3.0f),DensityUtil.dpToPx(getContext(),3.0f),mUnderPaint);
            startX = startX + rectF.width() + (i + 1) * mGap ;
        }
        
    }
}
