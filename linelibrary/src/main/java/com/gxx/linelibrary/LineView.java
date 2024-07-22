package com.gxx.linelibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
        int underColor = typedArray.getColor(R.styleable.LineView_line_view_under_color, Color.BLUE);
        int foregroundColor = typedArray.getColor(R.styleable.LineView_line_view_foreground_color,Color.WHITE);
        int paintWidth = typedArray.getDimensionPixelSize(R.styleable.LineView_line_view_paint_width, DensityUtil.dpToPx(context,10.0f));
        int corner = typedArray.getDimensionPixelSize(R.styleable.LineView_line_view_corner,DensityUtil.dpToPx(context,3.0f));
        int resourceId = typedArray.getResourceId(R.styleable.LineView_line_view_foreground_color_array,0);
        int gradientStyle = typedArray.getInt(R.styleable.LineView_line_view_foreground_gradient_style,0);
        if(resourceId > 0 && gradientStyle > 0){
            mForegroundColors = context.getResources().getIntArray(typedArray.getResourceId(R.styleable.LineView_line_view_foreground_color_array,0));
        }
        typedArray.recycle();


        if(gradientStyle!=0){
            if(mForegroundColors == null || mForegroundColors.length == 0){
                throw new IllegalArgumentException("请配置 line_view_foreground_color_array ");
            }
        }
        
        this.mGradientStyle = gradientStyle;
        this.mPaintWidth = paintWidth;
        this.mCorner = corner;
   

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
        setMeasuredDimension(widthMeasureSpec,mPaintWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mUnderRectf  = new RectF(getPaddingLeft() + mPaintWidth / 2,mPaintWidth / 2, getWidth() - getPaddingRight() - mPaintWidth / 2,mPaintWidth / 2);

        //设置渐变
        if(mForegroundColors!=null && mForegroundColors.length >=2 && mGradientStyle!=0){
            if(mGradientStyle == GRADIENT_STYLE_1){
                if (mForegroundColors.length > 3){
                    throw new  IllegalArgumentException("不支持多余3个的");
                }

                if(mForegroundColors.length == 2){
                    mGradient =  new LinearGradient(
                            mUnderRectf.left, mUnderRectf.top,
                            mUnderRectf.right, mUnderRectf.bottom,
                            mForegroundColors,
                            new float[] { 0f, 1f },
                            Shader.TileMode.CLAMP
                    );
                }else{
                    mGradient =  new LinearGradient(
                            mUnderRectf.left, mUnderRectf.top,
                            mUnderRectf.right, mUnderRectf.bottom,
                            mForegroundColors,
                            new float[] { 0f, 0.5f, 1f },
                            Shader.TileMode.CLAMP
                    );
                }
                // 设置渐变色
                mForegroundPaint.setShader(mGradient);
            }else if(mGradientStyle == GRADIENT_STYLE_2){
                int startColor = mForegroundColors[0];
                int endColor = mForegroundColors[1];
                Color.colorToHSV(startColor, fromColorArr);
                Color.colorToHSV(endColor, toColorArr);
            }
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        float left = getPaddingLeft() + mPaintWidth / 2;
        canvas.drawRoundRect(mUnderRectf,mCorner,mCorner,mUnderPaint);

        RectF foregroundRectf = null;
        if(left + mUnderRectf.width() * mInputProgress <= left + mPaintWidth / 6){
             foregroundRectf = new RectF(left,mPaintWidth / 2, left + mPaintWidth / 6,mPaintWidth / 2);
        }else{
            foregroundRectf = new RectF(left,mPaintWidth / 2, left + mUnderRectf.width() * mInputProgress,mPaintWidth / 2);
        }
        canvas.drawRoundRect(foregroundRectf,mCorner,mCorner,mForegroundPaint);
    }

    public void setProgress(float progress){
        this.mInputProgress = progress;

        if (mInputProgress >= 1.0f) {
            mInputProgress = 1.0f;
        }

        if (mInputProgress <= 0.0f) {
            mInputProgress = 0.0f;
        }

        if (mGradientStyle == GRADIENT_STYLE_2) {
            hsvArr[0] = fromColorArr[0] + (toColorArr[0] - fromColorArr[0]) * mInputProgress;
            hsvArr[1] = fromColorArr[1] + (toColorArr[1] - fromColorArr[1]) * mInputProgress;
            hsvArr[2] = fromColorArr[2] + (toColorArr[2] - fromColorArr[2]) * mInputProgress;
            mForegroundPaint.setColor(Color.HSVToColor(hsvArr));
        }

        invalidate();
    }

    public void setGradient(LinearGradient linearGradient){
        this.mGradient = linearGradient;
        mGradientStyle = GRADIENT_STYLE_1;
    }

    public void setStartEndColor(int startColor,int endColor){
        Color.colorToHSV(startColor, fromColorArr);
        Color.colorToHSV(endColor, toColorArr);
        mGradientStyle = GRADIENT_STYLE_2;
    }

    public void setUnderColor(int color){
        mUnderPaint.setColor(color);
    }

    public void setForegroundColor(int color){
        mForegroundPaint.setColor(color);
    }

}
