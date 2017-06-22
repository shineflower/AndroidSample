package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2017/6/21.
 */

public class RecorderView extends View {
    private boolean mIsStop = false;  //是否结束语音

    private Paint mPaint;
    private RectF mRectF;

    private int mMax;
    private float mDegree;  //当前进度 以角度为单位

    private Bitmap mBitmap;

    private OnGestureListener mOnGestureListener;

    private static final int STROKE_WIDTH = 6;

    public interface OnGestureListener {
        void onLongClick();
        void onClick();
        void onLift();
        void onOver();
    }

    public void setOnGestureListener(OnGestureListener onGestureListener){
        this.mOnGestureListener = onGestureListener;
    }

    public RecorderView(Context context) {
        this(context, null);
    }

    public RecorderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecorderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        mIsStop = false;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#0971ce"));
        mPaint.setStrokeWidth(STROKE_WIDTH);
        mPaint.setStyle(Paint.Style.STROKE);

        //设置绘制大小
        mRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();

        mRectF.left = STROKE_WIDTH / 2;
        mRectF.top = STROKE_WIDTH / 2;
        mRectF.right = width - STROKE_WIDTH / 2;
        mRectF.bottom = width - STROKE_WIDTH / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mIsStop) {
            mBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.recorder_voice));
        } else {
            mBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.recorder_finish));
        }

        /**
         *  Rect  src: 是对图片进行裁截，若是空null，则显示整个图片
         *  RectF dst：是图片在Canvas画布中显示的区域，
         *  大于src则把src的裁截区放大
         *  小于src则把src的裁截区缩小
         */
        canvas.drawBitmap(mBitmap, null, mRectF, null);
        //绘制进度
        canvas.drawArc(mRectF, 270, mDegree, false, mPaint);
    }

    public void setMax(int max){
        this.mMax = max;
    }

    /**
     * 设置进度
     */
    public void setProgress(float progress){

        float ratio = progress / mMax;
        mDegree = 370 * ratio;

        invalidate();

        if(ratio >= 1){
            if(mOnGestureListener != null) {
                mOnGestureListener.onOver();
            }
        }
    }

    public void setStop(boolean isStop) {
        this.mIsStop = isStop;
    }
}