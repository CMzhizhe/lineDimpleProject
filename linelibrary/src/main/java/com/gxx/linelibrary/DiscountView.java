package com.gxx.linelibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.gxx.linelibrary.model.DiscountXYModel;
import com.gxx.linelibrary.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class DiscountView extends View {
    private Paint mUnderPaint;
    private Paint mForegroundPaint;
    private int mHorizontalLine = 0;
    private int mVerticalLine = 0;
    private  int mIntGerPart = -1;//整数部分，这个是个数
    private float mInputProgress = 0.0f;
    private List<DiscountXYModel> mList = new ArrayList<DiscountXYModel>();
    private final int mMaxCount = 13;

    public DiscountView(Context context) {
        this(context,null);
    }

    public DiscountView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DiscountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.DiscountView);
        int underColor = typedArray.getColor(R.styleable.DiscountView_discount_view_under_color,Color.BLUE);
        int foregroundColor = typedArray.getColor(R.styleable.DiscountView_discount_view_foreground_color,Color.WHITE);
        int paintWidth = typedArray.getDimensionPixelSize(R.styleable.DiscountView_discount_view_paint_width, DensityUtil.dpToPx(context,5.0f));
        typedArray.recycle();

        mUnderPaint = new Paint();
        mUnderPaint.setAntiAlias(true);
        mUnderPaint.setColor(underColor);
        mUnderPaint.setStyle(Paint.Style.STROKE);
        mUnderPaint.setStrokeWidth(paintWidth);
        mUnderPaint.setStrokeJoin(Paint.Join.ROUND);
        mUnderPaint.setStrokeCap(Paint.Cap.ROUND);

        mForegroundPaint = new Paint();
        mForegroundPaint.setAntiAlias(true);
        mForegroundPaint.setColor(foregroundColor);
        mForegroundPaint.setStyle(Paint.Style.STROKE);
        mForegroundPaint.setStrokeWidth(paintWidth);
        mForegroundPaint.setStrokeJoin(Paint.Join.ROUND);
        mForegroundPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //整个宽度 = 长横线有7段
        //整个高度 = 整个高度 / 4  * 4
         mHorizontalLine = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / 7;
         mVerticalLine = getMeasuredHeight() / 4;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if(!mList.isEmpty()){
            return;
        }

        //将除开,开始的点以外，其他的点都存入进集合
        //第一笔
        mList.add(new DiscountXYModel(1 * mHorizontalLine,getMeasuredHeight() / 2));
        //第二笔
        mList.add(new DiscountXYModel(1 * mHorizontalLine,getMeasuredHeight() / 4));
        //第三笔
        mList.add(new DiscountXYModel(2 * mHorizontalLine,getMeasuredHeight() / 4));
        //第四笔
        mList.add(new DiscountXYModel(2 * mHorizontalLine,getMeasuredHeight()  - mVerticalLine / 2));
        //第五笔
        mList.add(new DiscountXYModel(3 * mHorizontalLine,getMeasuredHeight() - mVerticalLine / 2));
        //第六笔
        mList.add(new DiscountXYModel(3 * mHorizontalLine,0 + mVerticalLine / 2));
        //第七笔
        mList.add(new DiscountXYModel(4 * mHorizontalLine, 0 + mVerticalLine / 2));
        //第八笔
        mList.add(new DiscountXYModel(4 * mHorizontalLine, getMeasuredHeight() - mVerticalLine / 2));
        //第九笔
        mList.add(new DiscountXYModel(5 * mHorizontalLine, getMeasuredHeight() - mVerticalLine / 2));
        //第十笔
        mList.add(new DiscountXYModel(5 * mHorizontalLine, getMeasuredHeight() / 4));
        //第十一笔
        mList.add(new DiscountXYModel(6 * mHorizontalLine, getMeasuredHeight() / 4));
        //第十二笔
        mList.add(new DiscountXYModel(6 * mHorizontalLine, getMeasuredHeight() / 2));
        //第十三笔，结束
        mList.add(new DiscountXYModel(7 * mHorizontalLine ,getMeasuredHeight() / 2));

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        drawPath(canvas, mUnderPaint);
        drawPath(canvas, mForegroundPaint);
    }


    /**
      * 绘制路径
      */
    private void drawPath(Canvas canvas, Paint paint){
        Path path = new Path();
        //开始
        path.moveTo(0 * mHorizontalLine + getPaddingLeft() ,getMeasuredHeight() / 2);

        if(paint == mUnderPaint){//底部的需要全部绘制
            for (int i = 0; i < mList.size(); i++) {
                path.lineTo(mList.get(i).getX(),mList.get(i).getY());
            }
            canvas.drawPath(path,paint);
        }else{
            if(mIntGerPart < 0){
                return;
            }

            for (int i = 0; i < mList.size(); i++) {
                path.lineTo(mList.get(i).getX(),mList.get(i).getY());
            }
            PathMeasure pathMeasure = new PathMeasure(path, false);
            float pathLength = pathMeasure.getLength();
            // 计算当前百分比对应的路径长度
            float distance = pathLength * mInputProgress;
            // 创建Path副本用于绘制
            Path drawingPath = new Path();
            // 从路径起点开始绘制到当前百分比位置
            pathMeasure.getSegment(0, distance, drawingPath, true);
            canvas.drawPath(drawingPath,paint);

        }
    }


   public void setProgress(float progress){
       this.mInputProgress = progress;

       if(mInputProgress >= 1.0f){
           mInputProgress = 1.0f;
       }

       if(mInputProgress <= 0.0f){
           mInputProgress = 0.0f;
       }

      float percentNum =  (progress * mMaxCount);
      if(percentNum < 1.0f){
          percentNum = 0.0f;
      }

      mIntGerPart = (int) percentNum;

      invalidate();
   }


   public void setUnderColor(int color){
        mUnderPaint.setColor(color);
   }

   public void setForegroundColor(int color){
        mForegroundPaint.setColor(color);
   }

}
