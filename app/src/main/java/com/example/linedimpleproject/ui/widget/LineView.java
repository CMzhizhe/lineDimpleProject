package com.example.linedimpleproject.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.linedimpleproject.R;
import com.gxx.linelibrary.utils.DensityUtil;

public class LineView extends View {
    private Paint mUnderPaint = null;
    private Paint mForegroundPaint = null;

    public LineView(Context context) {
        this(context,null);
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mUnderPaint = new Paint();
        mUnderPaint.setAntiAlias(true);
        mUnderPaint.setColor(context.getResources().getColor(R.color.color_DCE1E8));
        mUnderPaint.setStyle(Paint.Style.FILL);
        mUnderPaint.setStrokeWidth(DensityUtil.dpToPx(context,10.0f));
        mUnderPaint.setStrokeJoin(Paint.Join.ROUND);
        mUnderPaint.setStrokeCap(Paint.Cap.ROUND);


        mForegroundPaint = new Paint();
        mForegroundPaint.setAntiAlias(true);
        mForegroundPaint.setColor(context.getResources().getColor(R.color.color_0DE28F));
        mForegroundPaint.setStyle(Paint.Style.FILL);
        mForegroundPaint.setStrokeWidth(DensityUtil.dpToPx(context,10.0f));
        mForegroundPaint.setStrokeJoin(Paint.Join.ROUND);
        mForegroundPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        

    }
}
