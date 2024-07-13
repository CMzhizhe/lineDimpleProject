package com.gxx.linelibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gxx.linelibrary.utils.DensityUtil;


public class RectangularView extends View {
    //含有渐变色的线条，可以设置2个，或者3个色值，在变化的路上会留下色值
    private static final int GRADIENT_STYLE_1 = 1;
    //总共就可以设置2个色值，最后会变成一个色值
    private static final int GRADIENT_STYLE_2 = 2;

    private Paint mUnderPaint = null;
    private Paint mForegroundPaint = null;
    private int mPaintWidth = 0;
    private int mCorner = 0;
    private float mInputProgress;
    private RectF mUnderRect = null;
    private Path mPath = new Path();
    private PathMeasure pathMeasure = null;
    private int mGradientStyle = -1; //风格类型，1表示
    private float[] fromColorArr = new float[3];
    private float[] toColorArr = new float[3];
    private float[] hsvArr = new float[3];
    private int[] mForegroundColors = null;
    private LinearGradient mGradient = null;


    public RectangularView(Context context) {
        this(context,null);
    }

    public RectangularView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RectangularView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RectangularView);
        int underColor = typedArray.getColor(R.styleable.RectangularView_rectangular_view_under_color, Color.BLUE);
        int foregroundColor = typedArray.getColor(R.styleable.RectangularView_rectangular_view_foreground_color,Color.WHITE);
        int paintWidth = typedArray.getDimensionPixelSize(R.styleable.RectangularView_rectangular_view_paint_width, DensityUtil.dpToPx(context,5.0f));
        int corner = typedArray.getDimensionPixelSize(R.styleable.RectangularView_rectangular_view_corner, DensityUtil.dpToPx(context,7.0f));
        int resourceId = typedArray.getResourceId(R.styleable.RectangularView_rectangular_view_foreground_color_array, 0);
        int gradientStyle = typedArray.getInt(R.styleable.RectangularView_rectangular_view_foreground_gradient_style,0);
        if (resourceId > 0 && gradientStyle > 0) {
            mForegroundColors = context.getResources().getIntArray(typedArray.getResourceId(R.styleable.RectangularView_rectangular_view_foreground_color_array, 0));
        }
        typedArray.recycle();
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mUnderRect = new RectF(mPaintWidth / 2,mPaintWidth / 2,getMeasuredWidth() - mPaintWidth / 2,getMeasuredHeight()  - mPaintWidth / 2);
        //将RectF转换为path
        mPath.moveTo(mUnderRect.left + mCorner, mUnderRect.top );
        mPath.lineTo(mUnderRect.right - mCorner, mUnderRect.top);
        mPath.arcTo(new RectF(mUnderRect.right - 2 * mCorner, mUnderRect.top, mUnderRect.right, mUnderRect.top + 2 * mCorner), 270, 90, false);
        mPath.lineTo(mUnderRect.right, mUnderRect.bottom - mCorner);
        mPath.arcTo(new RectF(mUnderRect.right - 2 * mCorner, mUnderRect.bottom - 2 * mCorner, mUnderRect.right, mUnderRect.bottom), 0, 90, false);
        mPath.lineTo(mUnderRect.left + mCorner, mUnderRect.bottom);
        mPath.arcTo(new RectF(mUnderRect.left, mUnderRect.bottom - 2 * mCorner, mUnderRect.left + 2 * mCorner, mUnderRect.bottom), 90, 90, false);
        mPath.lineTo(mUnderRect.left, mUnderRect.top + mCorner);
        mPath.arcTo(new RectF(mUnderRect.left, mUnderRect.top, mUnderRect.left + 2 * mCorner, mUnderRect.top + 2 * mCorner), 180, 90, false);
        mPath.close();
        pathMeasure = new PathMeasure(mPath, false);

        //设置渐变
        if(mForegroundColors!=null && mForegroundColors.length >=2 && mGradientStyle!=0){
            if(mGradientStyle == GRADIENT_STYLE_1){
                if (mForegroundColors.length > 3){
                    throw new  IllegalArgumentException("不支持多余3个的");
                }

                if(mForegroundColors.length == 2){
                    mGradient =  new LinearGradient(
                            mUnderRect.left, mUnderRect.top,
                            mUnderRect.right, mUnderRect.bottom,
                            mForegroundColors,
                            new float[] { 0f, 1f },
                            Shader.TileMode.CLAMP
                    );
                }else{
                    mGradient =  new LinearGradient(
                            mUnderRect.left, mUnderRect.top,
                            mUnderRect.right, mUnderRect.bottom,
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
        canvas.drawRoundRect(mUnderRect,mCorner,mCorner,mUnderPaint);
        // 获取路径总长度
        float pathLength = pathMeasure.getLength();
        // 计算当前百分比对应的路径长度
        float distance = pathLength * mInputProgress;
        // 创建Path副本用于绘制
        Path drawingPath = new Path();
        // 从路径起点开始绘制到当前百分比位置
        pathMeasure.getSegment(0, distance, drawingPath, true);
        // 在Canvas上绘制
        canvas.drawPath(drawingPath, mForegroundPaint);
    }


    public void setProgress(float progress) {
        this.mInputProgress = progress;

        if (mInputProgress >= 1.0f) {
            mInputProgress = 1.0f;
        }

        if (mInputProgress <= 0.0f) {
            mInputProgress = 0.0f;
        }

        if (mGradientStyle == GRADIENT_STYLE_2 && mForegroundColors != null && mForegroundColors.length > 0  ) {
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

    public void setUnderColor(int color){
        mUnderPaint.setColor(color);
    }

    public void setForegroundColor(int color){
        mForegroundPaint.setColor(color);
    }
}
