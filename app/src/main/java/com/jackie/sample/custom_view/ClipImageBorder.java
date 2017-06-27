package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Jackie on 2017/5/15.
 */

public class ClipImageBorder extends View {
    /**
     * 水平方向与View的边距
     */
    private int mHorizontalPadding = 20;
    /**
     * 垂直方向与View的边距
     */
    private int mVerticalPadding;
    /**
     * 绘制的矩形的宽度
     */
    private int mWidth;
    /**
     * 边框的宽度 单位dp
     */
    private int mBorderWidth = 1;

    private Paint mPaint;

    public ClipImageBorder(Context context) {
        this(context, null);
    }

    public ClipImageBorder(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageBorder(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //计算padding的px
        mHorizontalPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources().getDisplayMetrics());

        mBorderWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources().getDisplayMetrics());

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        //计算矩形区域的宽度
        mWidth = width - mHorizontalPadding * 2;
        //计算距离屏幕垂直边界的边距
        mVerticalPadding = (height - mWidth) / 2;  //宽度和高度一致
        mPaint.setColor(Color.parseColor("#aa000000"));
        mPaint.setStyle(Paint.Style.FILL);
        //绘制左边
        canvas.drawRect(0, 0, mHorizontalPadding, height, mPaint);
        //绘制右边
        canvas.drawRect(width - mHorizontalPadding, 0, width, height, mPaint);
        //绘制上边
        canvas.drawRect(mHorizontalPadding, 0, width - mHorizontalPadding,
                mVerticalPadding, mPaint);
        // 绘制下边
        canvas.drawRect(mHorizontalPadding, height - mVerticalPadding,
                width - mHorizontalPadding, height, mPaint);

        // 绘制外边框
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(mHorizontalPadding, mVerticalPadding, width - mHorizontalPadding,
                height - mVerticalPadding, mPaint);
    }

    public void setHorizontalPadding(int horizontalPadding) {
        this.mHorizontalPadding = horizontalPadding;
    }
}
