package com.gxx.linelibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gxx.linelibrary.utils.DensityUtil;

public class RecentView  extends View {
    private float mInputProgress;
    private int mCorner = 0;
    private Paint mUnderPaint = null;
    private Paint mForegroundPaint = null;
    private Path mPath = new Path();
    public RecentView(Context context) {
        this(context,null);
    }

    public RecentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RecentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecentView);
        int underColor = typedArray.getColor(R.styleable.RecentView_recent_view_under_color, Color.GRAY);
        int foregroundColor = typedArray.getColor(R.styleable.RecentView_recent_view_foreground_color,Color.BLUE);
        int corner = typedArray.getDimensionPixelSize(R.styleable.LineView_line_view_corner,DensityUtil.dpToPx(context,13.0f));
        typedArray.recycle();

        mCorner = corner;
        mUnderPaint = new Paint();
        mUnderPaint.setAntiAlias(true);
        mUnderPaint.setColor(underColor);
        mUnderPaint.setStyle(Paint.Style.FILL);


        mForegroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mForegroundPaint.setAntiAlias(true);
        mForegroundPaint.setColor(foregroundColor);
        mForegroundPaint.setStyle(Paint.Style.FILL);

    }



    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), mCorner, mCorner, mUnderPaint);

        float rightPercent = getMeasuredWidth() * mInputProgress;
        if (rightPercent <= mCorner * 2 ){
            canvas.save();
            mPath.reset();
            mPath.addRoundRect(new RectF(0, 0,mCorner * 2, getMeasuredHeight()), mCorner,mCorner, Path.Direction.CW);
            canvas.clipPath(mPath);
            canvas.drawRoundRect(new RectF(0, 0, rightPercent , getMeasuredHeight()),mCorner,mCorner,mForegroundPaint);
            canvas.restore();
        }else {
            if (rightPercent > getMeasuredWidth()){
                rightPercent = getMeasuredWidth();
            }
            canvas.drawRoundRect(new RectF(0, 0, rightPercent , getMeasuredHeight()),mCorner,mCorner,mForegroundPaint);
        }

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

    public void setUnderColor(int color){
        mUnderPaint.setColor(color);
    }

    public void setForegroundColor(int color){
        mForegroundPaint.setColor(color);
    }

}
