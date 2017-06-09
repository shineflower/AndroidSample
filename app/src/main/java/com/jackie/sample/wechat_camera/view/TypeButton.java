package com.jackie.sample.wechat_camera.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jackie on 2017/6/8.
 * 拍照或录制完成后弹出的确认和返回按钮
 */

public class TypeButton extends View {
    public static final int TYPE_CANCEL = 0x001;
    public static final int TYPE_CONFIRM = 0x002;
    private int mButtonType;
    private int mButtonSize;

    private float mCenterX;
    private float mCenterY;
    private float mButtonRadius;

    private Paint mPaint;
    private Path mPath;
    private float mStrokeWidth;

    private float mIndex;
    private RectF mRectF;

    public TypeButton(Context context) {
        super(context);
    }

    public TypeButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TypeButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TypeButton(Context context, int type, int size) {
        super(context);

        this.mButtonType = type;
        mButtonSize = size;
        mButtonRadius = size / 2.0f;
        mCenterX = size / 2.0f;
        mCenterY = size / 2.0f;

        mPaint = new Paint();
        mPath = new Path();
        mStrokeWidth = size / 50f;
        mIndex = mButtonSize / 12f;
        mRectF = new RectF(mCenterX, mCenterY - mIndex, mCenterX + mIndex * 2, mCenterY + mIndex);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mButtonSize, mButtonSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //如果类型为取消，则绘制内部为返回箭头
        if (mButtonType == TYPE_CANCEL) {
            mPaint.setAntiAlias(true);
            mPaint.setColor(0xeecccccc);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(mCenterX, mCenterY, mButtonRadius, mPaint);

            mPaint.setColor(Color.BLACK);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStrokeWidth);

            mPath.moveTo(mCenterX - mIndex / 7, mCenterY + mIndex);
            mPath.lineTo(mCenterX + mIndex, mCenterY + mIndex);

            mPath.arcTo(mRectF, 90, -180);
            mPath.lineTo(mCenterX - mIndex, mCenterY - mIndex);
            canvas.drawPath(mPath, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            mPath.reset();
            mPath.moveTo(mCenterX - mIndex, (float) (mCenterY - mIndex * 1.5));
            mPath.lineTo(mCenterX - mIndex, (float) (mCenterY - mIndex / 2.3));
            mPath.lineTo((float) (mCenterX - mIndex * 1.6), mCenterY - mIndex);
            mPath.close();
            canvas.drawPath(mPath, mPaint);
        }

        //如果类型为确认，则绘制绿色勾
        if (mButtonType == TYPE_CONFIRM) {
            mPaint.setAntiAlias(true);
            mPaint.setColor(0xFFFFFFFF);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(mCenterX, mCenterY, mButtonRadius, mPaint);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(0xFF00CC00);
            mPaint.setStrokeWidth(mStrokeWidth);

            mPath.moveTo(mCenterX - mButtonSize / 6f, mCenterY);
            mPath.lineTo(mCenterX - mButtonSize / 21.2f, mCenterY + mButtonSize / 7.7f);
            mPath.lineTo(mCenterX + mButtonSize / 4.0f, mCenterY - mButtonSize / 8.5f);
            mPath.lineTo(mCenterX - mButtonSize / 21.2f, mCenterY + mButtonSize / 9.4f);
            mPath.close();
            canvas.drawPath(mPath, mPaint);
        }
    }
}
