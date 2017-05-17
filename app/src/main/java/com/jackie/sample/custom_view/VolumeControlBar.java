package com.jackie.sample.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2017/5/17.
 * 音量调节
 */

public class VolumeControlBar extends View {
    //第一圈的颜色
    private int mFirstColor;
    //第二圈的颜色
    private int mSecondColor;
    //圈的宽度
    private int mCircleWidth;
    //个数
    private int mDotCount;
    //每个块间的间隙
    private int mSplitSize;
    //中间的图片
    private Bitmap mCenterImage;

    private Paint mPaint;
    private Rect mRect;

    private int mDownY, mUpY;

    //当前进度
    private int mCurrentCount = 3;

    public VolumeControlBar(Context context) {
        this(context, null);
    }

    public VolumeControlBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VolumeControlBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VolumeControlBar, defStyleAttr, 0);
        int count = ta.getIndexCount();

        for (int i = 0; i < count; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.VolumeControlBar_first_color:
                    mFirstColor = ta.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.VolumeControlBar_second_color:
                    mSecondColor = ta.getColor(attr, Color.CYAN);
                    break;
                case R.styleable.VolumeControlBar_circle_width:
                    mCircleWidth = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.VolumeControlBar_dot_count:
                    mDotCount = ta.getInt(attr, 20);// 默认20
                    break;
                case R.styleable.VolumeControlBar_split_size:
                    mSplitSize = ta.getInt(attr, 20);
                    break;
                case R.styleable.VolumeControlBar_center_image:
                    mCenterImage = BitmapFactory.decodeResource(getResources(), ta.getResourceId(attr, 0));
                    break;
            }
        }

        ta.recycle();

        mPaint = new Paint();
        mRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStrokeWidth(mCircleWidth); // 设置圆环的宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 定义线段断电形状为圆头
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心

        int center = getWidth() / 2;
        int outerRadius = center - mCircleWidth / 2;  //获得外圆的半径

        //画小块
        drawOval(canvas, center, outerRadius);

        //计算内切正方形的位置
        int innerRadius = outerRadius - mCircleWidth / 2;  //获得内圆的半径
        //内切正方形的距离顶部 = mCircleWidth + innerRadius - √2 / 2
        mRect.left = (int) (innerRadius - Math.sqrt(2) * 1.0f / 2 * innerRadius) + mCircleWidth;
        //内切正方形的距离左边 = mCircleWidth + innerRadius - √2 / 2
        mRect.top = (int) (innerRadius - Math.sqrt(2) * 1.0f / 2 * innerRadius) + mCircleWidth;
        mRect.right = (int) (mRect.left + Math.sqrt(2) * innerRadius);
        mRect.bottom = (int) (mRect.top + Math.sqrt(2) * innerRadius);

        //如果图片比较小，那个根据图片的尺寸放置到正中心
        if (mCenterImage.getWidth() < Math.sqrt(2) * innerRadius) {
            mRect.left = (int) (mRect.left + Math.sqrt(2) * 1.0f / 2 * innerRadius - mCenterImage.getWidth() * 1.0f / 2);
            mRect.top = (int) (mRect.top + Math.sqrt(2) * 1.0f / 2 * innerRadius- mCenterImage.getHeight() * 1.0f / 2);
            mRect.right = mRect.left + mCenterImage.getWidth();
            mRect.bottom = mRect.top + mCenterImage.getHeight();
        }

        //绘图
        canvas.drawBitmap(mCenterImage, null, mRect, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                mUpY = (int) event.getY();

                if (mUpY > mDownY) { //下滑
                    down();
                } else {
                    up();
                }

                break;
        }
        return true;
    }

    /**
     * 根据参数画出每个小块
     * @param canvas  画布
     * @param center  中点
     * @param radius  半径
     */
    private void drawOval(Canvas canvas, int center, int radius) {
        // 根据需要画的个数以及间隙计算每个块块所占的比例*360
        float itemSize = (360 * 1.0f - mDotCount * mSplitSize) / mDotCount;
        // 用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);

        mPaint.setColor(mFirstColor);

        for (int i = 0; i < mDotCount; i++) {
            canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false, mPaint);
        }

        mPaint.setColor(mSecondColor);

        for (int i = 0; i < mCurrentCount; i++) {
            canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false, mPaint);
        }
    }

    public void up() {
        mCurrentCount++;
        postInvalidate();
    }

    public void down() {
        mCurrentCount--;
        postInvalidate();
    }
}
