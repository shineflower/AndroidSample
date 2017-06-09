package com.jackie.sample.wechat_camera.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jackie on 2017/6/8.
 * 对焦框
 */

public class FocusView extends View {
    private int mSize;
    private int mCenterX;
    private int mCenterY;
    private int mLength;
    private Paint mPaint;

    private FocusView(Context context) {
        super(context);
    }

    public FocusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FocusView(Context context, int size) {
        this(context);
        this.mSize = size;
        mPaint = new Paint();

        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xff00cc00);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCenterX = (int) (mSize / 2.0);
        mCenterY = (int) (mSize / 2.0);
        mLength = (int) (mSize / 2.0) - 2;
        setMeasuredDimension(mSize, mSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(mCenterX - mLength, mCenterY - mLength, mCenterX + mLength, mCenterY + mLength, mPaint);
        canvas.drawLine(2, getHeight() / 2, mSize / 10, getHeight() / 2, mPaint);
        canvas.drawLine(getWidth() - 2, getHeight() / 2, getWidth() - mSize / 10, getHeight() / 2, mPaint);
        canvas.drawLine(getWidth() / 2, 2, getWidth() / 2, mSize / 10, mPaint);
        canvas.drawLine(getWidth() / 2, getHeight() - 2, getWidth() / 2, getHeight() - mSize / 10, mPaint);
    }
}
