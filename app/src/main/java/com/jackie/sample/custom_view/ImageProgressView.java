package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by Jackie on 2017/10/14.
 * 图片进度条
 */

public class ImageProgressView extends AppCompatImageView {
    private Paint mPaint;
    private RectF mRectF = new RectF();

    private int mProgress;

    public ImageProgressView(Context context) {
        this(context ,null);
    }

    public ImageProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.parseColor("#70000000"));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int width = getWidth();
        int height = getHeight();

//        mRectF.set(0, 0, width, height);
        mRectF.set(- width / 2, - height / 2, width + width / 2, height + height / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float startAngle = (float) (360.0 * (mProgress / 100.0)) - 90;
        float sweepAngle = 270 - startAngle;

        canvas.drawArc(mRectF, startAngle, sweepAngle, true, mPaint);
    }

    public void setProgress(int progress) {
        this.mProgress = progress;

        invalidate();
    }
}
