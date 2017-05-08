package com.jackie.sample.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2016/12/27.
 * 自定义水平进度条
 */

public class HorizontalProgressBar extends ProgressBar {
    private static final int DEFAULT_UNREACHABLE_COLOR = 0XFFD3D6DA;
    private static final int DEFAULT_UNREACHABLE_HEIGHT = 2;
    private static final int DEFAULT_REACHABLE_COLOR = 0XFFFC00D1;
    private static final int DEFAULT_REACHABLE_HEIGHT = 2;
    private static final int DEFAULT_TEXT_SIZE = 10; //sp
    private static final int DEFAULT_TEXT_COLOR = DEFAULT_REACHABLE_COLOR;
    private static final int DEFAULT_TEXT_OFFSET = 10;

    protected int mUnreachableColor = DEFAULT_UNREACHABLE_COLOR;
    protected int mUnreachableHeight = dp2px(DEFAULT_UNREACHABLE_HEIGHT);
    protected int mReachableColor = DEFAULT_REACHABLE_COLOR;
    protected int mReachableHeight = dp2px(DEFAULT_REACHABLE_HEIGHT);
    protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    protected int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);

    protected Paint mPaint = new Paint();

    protected int mRealWidth;

    public HorizontalProgressBar(Context context) {
        this(context, null);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        obtainStyledAttrs(context, attrs);
    }

    private void obtainStyledAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.HorizontalProgressBar);

        mUnreachableColor = ta.getColor(R.styleable.HorizontalProgressBar_progress_unreachable_color,
                mUnreachableColor);

        mUnreachableHeight = (int) ta.getDimension(R.styleable.HorizontalProgressBar_progress_unreachable_height,
                mUnreachableHeight);

        mReachableColor = ta.getColor(R.styleable.HorizontalProgressBar_progress_reachable_color,
                mReachableColor);

        mReachableHeight = (int) ta.getDimension(R.styleable.HorizontalProgressBar_progress_reachable_height,
                mReachableHeight);

        mTextSize = (int) ta.getDimension(R.styleable.HorizontalProgressBar_progress_text_size,
                mTextSize);

        mTextColor = ta.getColor(R.styleable.HorizontalProgressBar_progress_text_color,
                mTextColor);

        mTextOffset = (int) ta.getDimension(R.styleable.HorizontalProgressBar_progress_text_offset,
                mTextOffset);

        ta.recycle();

        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //水平控件宽度不支持warp_content
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        setMeasuredDimension(width, height);

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();

        canvas.translate(getPaddingLeft(), getHeight() / 2);

        boolean noNeedUnreachable = false;

        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);

        float radio = getProgress() * 1.0f / getMax();
        float progressX = radio * mRealWidth;
        if (progressX + textWidth > mRealWidth) {
            progressX = mRealWidth - textWidth;
            noNeedUnreachable = true;
        }

        //draw reachable bar
        float endX = progressX - mTextOffset / 2;
        if (endX > 0) {
            mPaint.setColor(mReachableColor);
            mPaint.setStrokeWidth(mReachableHeight);
            canvas.drawLine(0, 0, endX, 0, mPaint);
        }

        //draw text
        mPaint.setColor(mTextColor);
        int y = (int) (-(mPaint.descent() + mPaint.ascent()) / 2);
        canvas.drawText(text, progressX, y, mPaint);

        //draw unreachable bar
        if (!noNeedUnreachable) {
            float start = progressX + mTextOffset / 2 + textWidth;
            mPaint.setTextSize(mUnreachableColor);
            mPaint.setStrokeWidth(mUnreachableHeight);
            canvas.drawLine(start, 0, mRealWidth, 0, mPaint);
        }
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;

        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            result = getPaddingTop() + getPaddingBottom() + Math.max(Math.max(mUnreachableHeight, mReachableHeight), Math.abs(textHeight));

            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }

        return result;
    }

    protected int dp2px(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                getResources().getDisplayMetrics());
    }

    protected int sp2px(int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue,
                getResources().getDisplayMetrics());
    }
}
