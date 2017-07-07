package com.jackie.sample.contact.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.jackie.sample.contact.utils.ColorUtils;

/**
 * Created by Jackie on 2017/7/6.
 *
 */

public class IndexBar extends ViewGroup {
    private Paint mPaint;

    private int mWidth;
    private int mHeight;

    private int mChildWidth;

    private float mCenterY;
    private String mLetter = "";
    private int mPosition;

    private boolean mIsShowLetter;


    private final float CIRCLE_RADIUS = 100;

    public IndexBar(Context context) {
        this(context, null);
    }

    public IndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        setWillNotDraw(false);

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        //MeasureSpec封装了父View传给子View的布局要求
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                mWidth = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                mWidth = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                mHeight = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                mHeight = heightSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        if (childCount < 0) {
            return;
        }

        //得到SideBar
        View childView = getChildAt(0);
        mChildWidth = childView.getMeasuredWidth();

        //把SlideBar排列到最右侧
        childView.layout(mWidth - mChildWidth, 0, mWidth, mHeight);
    }

    /**
     * @param centerY   要绘制的圆的Y坐标
     * @param letter    要绘制的字母
     * @param position  字母所在的位置
     */
    public void setDrawData(float centerY, String letter, int position) {
        this.mCenterY = centerY;
        this.mLetter = letter;
        this.mPosition = position;
        mIsShowLetter = true;
        invalidate();
    }

    /**
     * 通过标志位来控制是否显示圆
     * @param isShowLetter     是否显示圆
     */
    public void setTagStatus(boolean isShowLetter) {
        this.mIsShowLetter = isShowLetter;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mIsShowLetter) {
            //根据位置来不断变换Paint的颜色
            ColorUtils.setPaintColor(mPaint, mPosition);
            //绘制圆和文字
            canvas.drawCircle((mWidth - mChildWidth) / 2, mCenterY, CIRCLE_RADIUS, mPaint);
            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize(80);
            canvas.drawText(mLetter, (mWidth - mChildWidth - mPaint.measureText(mLetter)) / 2, mCenterY - (mPaint.ascent() + mPaint.descent()) / 2, mPaint);
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return super.generateLayoutParams(p);
    }
}
