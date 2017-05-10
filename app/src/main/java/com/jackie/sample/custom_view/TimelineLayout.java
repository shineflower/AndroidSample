package com.jackie.sample.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2017/3/8.
 * 时间轴控件
 */

public class TimelineLayout extends LinearLayout {
    private Context mContext;

    private int mLineMarginLeft;
    private int mLineMarginTop;
    private int mLineStrokeWidth;
    private int mLineColor;;
    private int mPointSize;
    private int mPointColor;
    private Bitmap mIcon;

    private Paint mLinePaint;  //线的画笔
    private Paint mPointPaint;  //点的画笔

    //第一个点的位置
    private int mFirstX;
    private int mFirstY;
    //最后一个图标的位置
    private int mLastX;
    private int mLastY;

    public TimelineLayout(Context context) {
        this(context, null);
    }

    public TimelineLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimelineLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TimelineLayout);
        mLineMarginLeft = ta.getDimensionPixelOffset(R.styleable.TimelineLayout_line_margin_left, 10);
        mLineMarginTop = ta.getDimensionPixelOffset(R.styleable.TimelineLayout_line_margin_top, 0);
        mLineStrokeWidth = ta.getDimensionPixelOffset(R.styleable.TimelineLayout_line_stroke_width, 2);
        mLineColor = ta.getColor(R.styleable.TimelineLayout_line_color, 0xff3dd1a5);
        mPointSize = ta.getDimensionPixelSize(R.styleable.TimelineLayout_point_size, 8);
        mPointColor = ta.getDimensionPixelOffset(R.styleable.TimelineLayout_point_color, 0xff3dd1a5);

        int iconRes = ta.getResourceId(R.styleable.TimelineLayout_icon_src, R.drawable.ic_ok);
        BitmapDrawable drawable = (BitmapDrawable) context.getResources().getDrawable(iconRes);
        if (drawable != null) {
            mIcon = drawable.getBitmap();
        }

        ta.recycle();

        setWillNotDraw(false);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setDither(true);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mLineStrokeWidth);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setDither(true);
        mPointPaint.setColor(mPointColor);
        mPointPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawTimeline(canvas);
    }

    private void drawTimeline(Canvas canvas) {
        int childCount = getChildCount();

        if (childCount > 0) {
            if (childCount > 1) {
                //大于1，证明至少有2个，也就是第一个和第二个之间连成线，第一个和最后一个分别有点和icon
                drawFirstPoint(canvas);
                drawLastIcon(canvas);
                drawBetweenLine(canvas);
            } else if (childCount == 1) {
                drawFirstPoint(canvas);
            }
        }
    }

    private void drawFirstPoint(Canvas canvas) {
        View child = getChildAt(0);
        if (child != null) {
            int top = child.getTop();
            mFirstX = mLineMarginLeft;
            mFirstY = top + child.getPaddingTop() + mLineMarginTop;

            //画圆
            canvas.drawCircle(mFirstX, mFirstY, mPointSize, mPointPaint);
        }
    }

    private void drawLastIcon(Canvas canvas) {
        View child = getChildAt(getChildCount() - 1);
        if (child != null) {
            int top = child.getTop();
            mLastX = mLineMarginLeft;
            mLastY = top + child.getPaddingTop() + mLineMarginTop;

            //画图
            canvas.drawBitmap(mIcon, mLastX - (mIcon.getWidth() >> 1), mLastY, null);
        }
    }

    private void drawBetweenLine(Canvas canvas) {
        //从开始的点到最后的图标之间，画一条线
        canvas.drawLine(mFirstX, mFirstY, mLastX, mLastY, mLinePaint);
        for (int i = 0; i < getChildCount() - 1; i++) {
            //画圆
            int top = getChildAt(i).getTop();
            int y = top + getChildAt(i).getPaddingTop() + mLineMarginTop;
            canvas.drawCircle(mFirstX, y, mPointSize, mPointPaint);
        }
    }

    public int getLineMarginLeft() {
        return mLineMarginLeft;
    }

    public void setLineMarginLeft(int lineMarginLeft) {
        this.mLineMarginLeft = lineMarginLeft;
        invalidate();
    }
}