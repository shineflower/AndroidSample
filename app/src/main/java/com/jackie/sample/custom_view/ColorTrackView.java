package com.jackie.sample.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jackie.sample.R;
import com.jackie.sample.utils.DensityUtils;

/**
 * Created by Jackie on 2017/5/17.
 */

public class ColorTrackView extends View {
    private String mText;
    private int mTextSize;

    private int mTextOriginColor;
    private int mTextChangeColor;

    private float mProgress;

    public enum Direction {
        LEFT, RIGHT
    }

    private int mDirection;

    private Paint mPaint;
    private int mTextWidth;
    private Rect mTextBounds;

    private int mRealWidth;
    private int mTextStartX;

    private static final int DIRECTION_LEFT = 0;
    private static final int DIRECTION_RIGHT = 1;

    public ColorTrackView(Context context) {
        this(context, null);
    }

    public ColorTrackView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mText = "Jackie";
        mTextSize = DensityUtils.sp2px(context, 30);

        mTextOriginColor = 0xff000000;
        mTextChangeColor = 0xffff0000;

        mProgress = 0;

        mDirection = DIRECTION_LEFT;

        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.ColorTrackView);
        mText = ta.getString(R.styleable.ColorTrackView_track_text);
        mTextSize = ta.getDimensionPixelSize(
                R.styleable.ColorTrackView_track_text_size, mTextSize);
        mTextOriginColor = ta.getColor(
                R.styleable.ColorTrackView_text_origin_color,
                mTextOriginColor);
        mTextChangeColor = ta.getColor(
                R.styleable.ColorTrackView_text_change_color,
                mTextChangeColor);
        mProgress = ta.getFloat(R.styleable.ColorTrackView_progress, mProgress);

        mDirection = ta.getInt(R.styleable.ColorTrackView_direction, mDirection);

        ta.recycle();

        mTextBounds = new Rect();

        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        measureText();
    }

    private void measureText() {
        mTextWidth = (int) mPaint.measureText(mText);
        mPaint.getTextBounds(mText, 0, mText.length(), mTextBounds);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        setMeasuredDimension(width, height);

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mTextStartX = mRealWidth / 2 - mTextWidth / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDirection == DIRECTION_LEFT) {
            drawChangeLeft(canvas);
            drawOriginLeft(canvas);
        } else {
            drawOriginRight(canvas);
            drawChangeRight(canvas);
        }
    }

    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);

        int width = 0;

        switch (mode) {
            case MeasureSpec.EXACTLY:
                width = size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                width = mTextWidth;
                break;
        }

        //AT_MOST不应该大于父类传入的值
        width = mode == MeasureSpec.AT_MOST ? Math.min(width, size) : width;
        return width + getPaddingLeft() + getPaddingRight();
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        int height = 0;

        switch (mode) {
            case MeasureSpec.EXACTLY:
                height = size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                height = mTextBounds.height();
                break;
        }

        //AT_MOST不应该大于父类传入的值
        height = mode == MeasureSpec.AT_MOST ? Math.min(height, size) : height;
        return height + getPaddingTop() + getPaddingBottom();
    }

    private void drawChangeRight(Canvas canvas) {
        drawText(canvas, mTextChangeColor, (int) (mTextStartX +(1 - mProgress) * mTextWidth), mTextStartX+mTextWidth);
    }

    private void drawOriginRight(Canvas canvas) {
        drawText(canvas, mTextOriginColor, mTextStartX, (int) (mTextStartX + (1 - mProgress) * mTextWidth));
    }

    private void drawChangeLeft(Canvas canvas) {
        drawText(canvas, mTextChangeColor, mTextStartX, (int) (mTextStartX + mProgress * mTextWidth));
    }

    private void drawOriginLeft(Canvas canvas) {
        drawText(canvas, mTextOriginColor, (int) (mTextStartX + mProgress * mTextWidth), mTextStartX + mTextWidth);
    }

    private void drawText(Canvas canvas, int color, int startX, int endX) {
        mPaint.setColor(color);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipRect(startX, 0, endX, getMeasuredHeight());
        canvas.drawText(mText, mTextStartX, getMeasuredHeight() / 2 +
        mTextBounds.height() / 2, mPaint);

        canvas.restore();
    }

    public void setDirection(int direction) {
        mDirection = direction;
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidate();
    }
}
