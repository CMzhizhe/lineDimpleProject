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

public class BorderLine extends View {

    private Paint mUnderPaint = null;
    private Paint mForegroundPaint = null;
    private Paint mForegroundBorderPaint = null;
    private float mInputProgress;
    private int mCorner = 0;
    private int mPaintWidth = 0;
    private int mBorderPaintWidth = 0;
    private RectF mUnderRectf;

    public BorderLine(Context context) {
        this(context,null);
    }

    public BorderLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BorderLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.BorderLine);
        int underColor = typedArray.getColor(R.styleable.BorderLine_border_line_under_color,Color.GRAY);
        int foregroundColor = typedArray.getColor(R.styleable.BorderLine_border_line_foreground_color,Color.RED);
        int foregroundBorderColor = typedArray.getColor(R.styleable.BorderLine_border_line_foreground_border_color,Color.BLUE);
        int paintWidth = typedArray.getDimensionPixelSize(R.styleable.BorderLine_border_line_paint_width, DensityUtil.dpToPx(context,13.0f));
        int borderPaintWidth = typedArray.getDimensionPixelSize(R.styleable.BorderLine_border_line_foreground_border_paint_width, DensityUtil.dpToPx(context,5.0f));
        int corner = typedArray.getDimensionPixelSize(R.styleable.BorderLine_border_line_corner,DensityUtil.dpToPx(context,5.0f));
        typedArray.recycle();

        mPaintWidth = paintWidth;
        mBorderPaintWidth = borderPaintWidth;
        mCorner = corner;

        mUnderPaint = new Paint();
        mUnderPaint.setAntiAlias(true);
        mUnderPaint.setColor(underColor);

        mForegroundPaint = new Paint();
        mForegroundPaint.setAntiAlias(true);
        mForegroundPaint.setColor(foregroundColor);


        mForegroundBorderPaint = new Paint();
        mForegroundBorderPaint.setAntiAlias(true);
        mForegroundBorderPaint.setColor(foregroundBorderColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,mPaintWidth + mBorderPaintWidth * 2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mUnderRectf  = new RectF(mBorderPaintWidth,0,getWidth() - mBorderPaintWidth,mPaintWidth);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(0,(getMeasuredHeight() - mPaintWidth) / 2);
        canvas.drawRoundRect(mUnderRectf,mCorner,mCorner,mUnderPaint);
        canvas.restore();


        float left = getMeasuredHeight() / 2;
        int borderHeight = getMeasuredHeight();
        RectF borderForegroundRectf = null;
        if(left + getWidth() * mInputProgress <= left + getMeasuredHeight() / 6){
            borderForegroundRectf = new RectF(0,0, left + borderHeight / 6,borderHeight);
        }else{
            borderForegroundRectf = new RectF(0,0, (left + getWidth() * mInputProgress) > getWidth() ? getWidth() : left + getWidth() * mInputProgress,borderHeight);
        }
        canvas.drawRoundRect(borderForegroundRectf,mCorner,mCorner,mForegroundBorderPaint);



        RectF foregroundRectf = new RectF(mBorderPaintWidth,0,borderForegroundRectf.width() - mBorderPaintWidth,mPaintWidth);
        canvas.save();
        canvas.translate(0,(getMeasuredHeight() - mPaintWidth) / 2);
        canvas.drawRoundRect(foregroundRectf,mCorner,mCorner,mForegroundPaint);
        canvas.restore();

       /* left = mBorderPaintWidth;
        RectF foregroundRectf = null;
        if(left + mUnderRectf.width() * mInputProgress <= left + mPaintWidth / 6){
            foregroundRectf = new RectF(left,0 , left + mPaintWidth / 6,mPaintWidth);
        }else{
            foregroundRectf = new RectF(left,0 , left + mUnderRectf.width() * mInputProgress ,mPaintWidth );
        }
        canvas.save();
        canvas.translate(0,(getMeasuredHeight() - mPaintWidth) / 2);
        canvas.drawRoundRect(foregroundRectf,mCorner,mCorner,mForegroundPaint);
        canvas.restore();*/


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
