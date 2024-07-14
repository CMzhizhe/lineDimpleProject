package com.example.linedimpleproject.ui.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class StepsLineView extends View {
    private int mGap = 0;//间隙
    private int mCount = 0;//条的个数
    private Paint mUnderPaint = null;
    private Paint mForegroundPaint = null;
    private int mPaintWidth = 0;
    private float mInputProgress;

    public StepsLineView(Context context) {
        this(context,null);
    }

    public StepsLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StepsLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
