package com.example.linedimpleproject.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.linedimpleproject.R;
import com.gxx.linelibrary.utils.DensityUtil;

public class LineView extends View {
    //含有渐变色的线条，可以设置2个，或者3个色值，在变化的路上会留下色值
    private static final int GRADIENT_STYLE_1 = 1;
    //总共就可以设置2个色值，最后会变成一个色值
    private static final int GRADIENT_STYLE_2 = 2;
    private Paint mUnderPaint = null;
    private Paint mForegroundPaint = null;
    private int mPaintWidth = 0;
    private float mInputProgress;
    private int mCorner = 0;
    private RectF mUnderRectf;
    private int mGradientStyle = -1; //风格类型，1表示
    private float[] fromColorArr = new float[3];
    private float[] toColorArr = new float[3];
    private float[] hsvArr = new float[3];
    private int[] mForegroundColors = null;
    private LinearGradient mGradient = null;
    public LineView(Context context) {
        this(context,null);
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, com.gxx.linelibrary.R.styleable.LineView);

        typedArray.recycle();

        mPaintWidth = DensityUtil.dpToPx(context,10.0f);

        mUnderPaint = new Paint();
        mUnderPaint.setAntiAlias(true);
        mUnderPaint.setColor(context.getResources().getColor(R.color.color_DCE1E8));
        mUnderPaint.setStyle(Paint.Style.STROKE);
        mUnderPaint.setStrokeWidth(mPaintWidth);
        mUnderPaint.setStrokeJoin(Paint.Join.ROUND);
        mUnderPaint.setStrokeCap(Paint.Cap.ROUND);


        mForegroundPaint = new Paint();
        mForegroundPaint.setAntiAlias(true);
        mForegroundPaint.setColor(context.getResources().getColor(R.color.color_0DE28F));
        mForegroundPaint.setStyle(Paint.Style.STROKE);
        mForegroundPaint.setStrokeWidth(mPaintWidth);
        mForegroundPaint.setStrokeJoin(Paint.Join.ROUND);
        mForegroundPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,mPaintWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mUnderRectf  = new RectF(getPaddingLeft() + mPaintWidth / 2,mPaintWidth / 2, getWidth() - getPaddingRight() - mPaintWidth / 2,mPaintWidth / 2);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        float left = getPaddingLeft() + mPaintWidth / 2;
        canvas.drawRoundRect(mUnderRectf,DensityUtil.dpToPx(getContext(),3.0f),DensityUtil.dpToPx(getContext(),3.0f),mUnderPaint);

        RectF foregroundRectf = null;
        if(left + mUnderRectf.width() * mInputProgress <= left + mPaintWidth / 6){
             foregroundRectf = new RectF(left,mPaintWidth / 2, left + mPaintWidth / 6,mPaintWidth / 2);
        }else{
            foregroundRectf = new RectF(left,mPaintWidth / 2, left + mUnderRectf.width() * mInputProgress,mPaintWidth / 2);
        }
        canvas.drawRoundRect(foregroundRectf,DensityUtil.dpToPx(getContext(),3.0f),DensityUtil.dpToPx(getContext(),3.0f),mForegroundPaint);
    }

    public void setProgress(float progress){
        this.mInputProgress = progress;

        if (mInputProgress >= 1.0f) {
            mInputProgress = 1.0f;
        }

        if (mInputProgress <= 0.0f) {
            mInputProgress = 0.0f;
        }

        invalidate();
    }
}
