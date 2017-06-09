package com.jackie.sample.wechat_camera.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * Created by Jackie on 2017/6/8.
 * 向下箭头的退出按钮
 */

public class ReturnButton extends View {

    private int mSize;

    private int mCenterX;
    private int mCenterY;
    private float mStrokeWidth;

    private Paint mPaint;
    private Path mPath;

    public ReturnButton(Context context, int size) {
        this(context);
        this.mSize = size;
        mCenterX = size / 2;
        mCenterY = size / 2;

        mStrokeWidth = size / 15f;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);

        mPath = new Path();
    }

    public ReturnButton(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSize, mSize / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.moveTo(mStrokeWidth, mStrokeWidth /2);
        mPath.lineTo(mCenterX, mCenterY - mStrokeWidth /2);
        mPath.lineTo(mSize - mStrokeWidth, mStrokeWidth /2);
        canvas.drawPath(mPath, mPaint);
    }
}
